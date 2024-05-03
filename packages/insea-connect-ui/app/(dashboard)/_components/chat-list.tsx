import { ScrollArea } from "@/components/ui/scroll-area";
import ChatItem from "./chat-item";

const ChatList = () => {
  return (
    <ScrollArea className="h-screen mt-2 pt-2">
      <div className="flex flex-col gap-2 px-4 h-full">
        <ChatItem />
        <ChatItem />
        <ChatItem />
        <ChatItem />
        <ChatItem />
        <ChatItem />
        <ChatItem />
      </div>
    </ScrollArea>
  );
};

export default ChatList;
