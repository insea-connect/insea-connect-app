import ChatAreaBlank from "../../_components/chat-area-blank";
import ChatAside from "../../_components/chat-aside";

const MainPage = async () => {
  return (
    <main className="flex flex-1 overflow-hidden">
      <ChatAside
        asideClassName="sm:block flex-1 lg:flex-shrink-0"
        divClassName="gap-3 pt-2 relative flex h-full w-full lg:w-96 flex-col border-r"
      />
      <ChatAreaBlank />
    </main>
  );
};

export default MainPage;
