import ChatArea from "@/app/(dashboard)/_components/chat-area";
import ChatAside from "@/app/(dashboard)/_components/chat-aside";

const ConversationPage = () => {
  return (
    <main className="flex flex-1 overflow-hidden">
      <ChatArea />
      <ChatAside
        asideClassName="hidden lg:order-first lg:block lg:flex-shrink-0"
        divClassName="gap-3 pt-2 relative flex h-full w-96 flex-col border-r"
      />
    </main>
  );
};

export default ConversationPage;
