import {
  CONVERSATIONS_ENDPOINT,
  CONVERSATION_INFO_ENDPOINT,
} from "@/lib/constants";
import axios from "axios";

export const getOrCreateConversation = async (
  chatId: string,
  currentUserId: number,
  accessToken: string
) => {
  // TODO: Validate the chatId

  const chatIdParts = chatId.split("_");

  if (chatIdParts.length !== 2) {
    throw new Error("Invalid chatId");
  }

  const recipientId =
    chatIdParts[0] === `${currentUserId}` ? chatIdParts[1] : chatIdParts[0];

  try {
    await axios.post(
      CONVERSATIONS_ENDPOINT,
      {
        recipientId,
      },
      {
        headers: {
          Authorization: `Bearer ${accessToken}`,
          "Content-Type": "application/json",
          Accept: "application/json",
        },
      }
    );
  } catch (error) {}

  const { data: conversation } = await axios.get(
    `${CONVERSATION_INFO_ENDPOINT}/${chatId}`,
    {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    }
  );

  const otherUser =
    conversation.member1.id === currentUserId
      ? conversation.member2
      : conversation.member1;
  const chatName = otherUser.username;

  return {
    id: conversation.id,
    chatName,
    otherUser,
  };
};
