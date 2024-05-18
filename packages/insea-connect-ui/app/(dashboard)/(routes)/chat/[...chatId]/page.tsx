import ChatArea from "@/app/(dashboard)/_components/chat-area";
import ChatAside from "@/app/(dashboard)/_components/chat-aside";

const ConversationPage = () => {
  return (
    <main className="flex flex-1 overflow-hidden">
      <ChatArea />
      <ChatAside />
    </main>
  );
};

export default ConversationPage;
