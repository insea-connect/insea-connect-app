import ChatArea from "@/app/(dashboard)/_components/chat-area";
import ChatAside from "@/app/(dashboard)/_components/chat-aside";

interface ConversationPageProps {
  params: {
    chatId: string;
  };
}

const ConversationPage = ({ params }: ConversationPageProps) => {
  const { chatId } = params;
  return <ChatArea chatId={chatId} />;
};

export default ConversationPage;
