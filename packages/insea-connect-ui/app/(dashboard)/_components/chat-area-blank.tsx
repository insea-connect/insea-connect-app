import { MessagesSquare } from "lucide-react";

const ChatAreaBlank = () => {
  return (
    <section className="hidden lg:flex bg-muted/30 w-full h-full justify-center items-center">
      <div className="gap-4 max-w-[600px] flex flex-col text-muted-foreground items-center justify-center text-center">
        <MessagesSquare className="w-24 h-24" />
        <h2 className="text-5xl font-bold ">Conversations</h2>

        <p>
          Choose a conversation to get started with your chat. Engage in
          discussions, ask questions, and collaborate with your peers to enhance
          your learning experience.
        </p>
      </div>
    </section>
  );
};

export default ChatAreaBlank;
