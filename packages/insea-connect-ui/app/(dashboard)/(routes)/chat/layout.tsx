import ChatAside from "../../_components/chat-aside";

const ChatLayout = ({
  children,
  params,
}: {
  children: React.ReactNode;
  params: {
    chatId: string;
  };
}) => {
  return (
    <main className="flex flex-1 overflow-hidden">
      <ChatAside />
      {children}
    </main>
  );
};

export default ChatLayout;
