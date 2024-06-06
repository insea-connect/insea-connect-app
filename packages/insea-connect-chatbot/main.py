import logging
from flask import Flask, request, jsonify
import time
from datetime import datetime, timedelta
from openai import OpenAI
from flask_cors import CORS
import threading
import os
from dotenv import load_dotenv
import re


# Setup logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

app = Flask(__name__)
CORS(app)  

API_KEY  = os.getenv('API_KEY', 'default_key')
logger.info("api key "+ API_KEY)
ASSISTANT_ID  = os.getenv('ASSISTANT_ID', 'default_key')


client = OpenAI(api_key=API_KEY)


# Storage for conversation threads, each with an expiration time
conversations = {}
lock = threading.Lock()

def clean_response_text(text):
    return re.sub(r'【\d+:\d+†\w+】', '', text)

# Function to clean up expired conversations
def cleanup_expired_conversations():
    while True:
        with lock:
            now = datetime.now()
            expired_keys = [key for key, value in conversations.items() if value["expires_at"] < now]
            for key in expired_keys:
                del conversations[key]
        time.sleep(60)

# Start the cleanup thread
threading.Thread(target=cleanup_expired_conversations, daemon=True).start()

@app.route('/start_conversation', methods=['POST'])
def start_conversation():
    logger.info("api key "+ API_KEY)

    # Create a conversation thread with an initial empty user message
    thread = client.beta.threads.create(
        messages=[
            {
                "role": "user",
                "content": "Hello"
            }
        ]
    )
    run = client.beta.threads.runs.create(thread_id=thread.id, assistant_id=ASSISTANT_ID)

    with lock:
        conversations[thread.id] = {
            "thread": thread,
            "run": run,
            "expires_at": datetime.now() + timedelta(minutes=15)
        }

    return jsonify({"thread_id": thread.id, "run_id": run.id}), 200

@app.route('/process_request', methods=['POST'])
def process_request():
    # Extract required parameters from the request body
    thread_id = request.json.get('thread_id')
    user_message = request.json.get('message')
    if not thread_id or not user_message:
        return jsonify({"error": "Thread ID and message content are required"}), 400

    with lock:
        conversation = conversations.get(thread_id)

    if not conversation or conversation['expires_at'] < datetime.now():
        return jsonify({"error": "Conversation expired or not found"}), 400

    # Send user's message to the assistant
    client.beta.threads.messages.create(thread_id=thread_id, role='user', content=user_message)

    # Create a new run for this message
    run = client.beta.threads.runs.create(thread_id=thread_id, assistant_id=ASSISTANT_ID)

    # Update the conversation with the new run
    with lock:
        conversation['run'] = run
        conversation['expires_at'] = datetime.now() + timedelta(minutes=15)

    # Wait for the run to complete or time out
    timeout_seconds = 5 * 60  # 5 minutes timeout
    start_time = time.time()

    while run.status != "completed":
        if time.time() - start_time > timeout_seconds:
            logger.error(f"Response timed out for thread {thread_id}")
            return jsonify({"error": "Response timeout"}), 504

        time.sleep(1)
        run = client.beta.threads.runs.retrieve(thread_id=thread_id, run_id=run.id)

    logger.info(f"Retrieved run status: {run.status}")

    # Retrieve all messages from the thread
    message_response = client.beta.threads.messages.list(thread_id=thread_id)
    messages = message_response.data
    logger.info(f"Messages list response: {message_response}")

    if not messages:
        logger.error(f"No messages returned for thread {thread_id}")
        return jsonify({"error": "No messages were returned from the assistant"}), 500

    # Log all returned messages for debugging purposes
    logger.info(f"Messages for thread {thread_id}: {[{'role': m.role, 'content': m.content} for m in messages]}")

    # Retrieve the latest assistant response by checking in reverse
    latest_message = next((m for m in messages if m.role == 'assistant'), None)

    # Check if there is a response and extract the content
    if latest_message and latest_message.content:
        # Extract text from all text blocks in the message
        text_blocks = []
        for block in latest_message.content:
            if "text" in block.__dict__:
                text_blocks.append(block.text.value)
            else:
                text_blocks.append(block.model_extra["text"]["value"])

        # text_blocks = [block.text.value for block in latest_message.content if "text" in block.__dict__ else "hello"]
        response_text = " ".join(text_blocks) if text_blocks else "No valid response received."
    else:
        response_text = 'No valid response received.'
    
     # Clean the response text
    cleaned_response_text = clean_response_text(response_text)
    logger.info(f"Latest assistant response for thread {thread_id}: {cleaned_response_text}")
    return jsonify({"response": response_text}), 200







@app.route('/process_request/stream', methods=['POST'])
def process_request_stream():
    # Extract required parameters from the request body
    thread_id = request.json.get('thread_id')
    user_message = request.json.get('message')
    if not thread_id or not user_message:
        return jsonify({"error": "Thread ID and message content are required"}), 400

    with lock:
        conversation = conversations.get(thread_id)

    if not conversation or conversation['expires_at'] < datetime.now():
        return jsonify({"error": "Conversation expired or not found"}), 400

    # Send user's message to the assistant
    client.beta.threads.messages.create(thread_id=thread_id, role='user', content=user_message)

    # Create a new run for this message
    run = client.beta.threads.runs.create(thread_id=thread_id, assistant_id=ASSISTANT_ID)

    # Update the conversation with the new run
    with lock:
        conversation['run'] = run
        conversation['expires_at'] = datetime.now() + timedelta(minutes=15)

    # Wait for the run to complete or time out
    timeout_seconds = 300 * 60  # 5 minutes timeout
    start_time = time.time()

    while run.status != "completed":
        if time.time() - start_time > timeout_seconds:
            logger.error(f"Response timed out for thread {thread_id}")
            return jsonify({"error": "Response timeout"}), 504

        time.sleep(1)
        run = client.beta.threads.runs.retrieve(thread_id=thread_id, run_id=run.id)

    logger.info(f"Retrieved run status: {run.status}")

    # Retrieve all messages from the thread
    message_response = client.beta.threads.messages.list(thread_id=thread_id)
    messages = message_response.data
    logger.info(f"Messages list response: {message_response}")

    if not messages:
        logger.error(f"No messages returned for thread {thread_id}")
        return jsonify({"error": "No messages were returned from the assistant"}), 500

    # Log all returned messages for debugging purposes
    logger.info(f"Messages for thread {thread_id}: {[{'role': m.role, 'content': m.content} for m in messages]}")

    # Retrieve the latest assistant response by checking in reverse
    latest_message = next((m for m in messages if m.role == 'assistant'), None)

    # Check if there is a response and extract the content
    if latest_message and latest_message.content:
        # Extract text from all text blocks in the message
        text_blocks = []
        for block in latest_message.content:
            if "text" in block.__dict__:
                text_blocks.append(block.text.value)
            else:
                text_blocks.append(block.model_extra["text"]["value"])

        # text_blocks = [block.text.value for block in latest_message.content if "text" in block.__dict__ else "hello"]
        response_text = " ".join(text_blocks) if text_blocks else "No valid response received."
    else:
        response_text = 'No valid response received.'
    
     # Clean the response text
    cleaned_response_text = clean_response_text(response_text)
    logger.info(f"Latest assistant response for thread {thread_id}: {cleaned_response_text}")
    return jsonify({"response": response_text}), 200




if __name__ == "__main__":
    app.run(debug=True ,host='0.0.0.0', port=5001)
