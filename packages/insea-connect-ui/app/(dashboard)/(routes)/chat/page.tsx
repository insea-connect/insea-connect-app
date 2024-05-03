import ChatArea from "../../_components/chat-area";
import ChatAside from "../../_components/chat-aside";

const MainPage = () => {
  return (
    <main className="flex flex-1 overflow-hidden">
      <ChatArea />
      <ChatAside />
    </main>
  );
};

export default MainPage;
