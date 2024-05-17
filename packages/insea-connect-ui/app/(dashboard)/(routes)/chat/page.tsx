import ChatAreaBlank from "../../_components/chat-area-blank";
import ChatAsideBis from "../../_components/chat-aside-bis";

const MainPage = () => {
  return (
    <main className="flex flex-1 overflow-hidden">
      <ChatAsideBis />
      <ChatAreaBlank />
    </main>
  );
};

export default MainPage;
