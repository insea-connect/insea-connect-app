import { Skeleton } from "@/components/ui/skeleton";
import ChatItemSkeleton from "./chat-item-skeleton";

const ChatListSkeleton = () => {
  return (
    <div className="flex flex-col gap-2 px-4 h-full mt-2 pt-2">
      <ChatItemSkeleton />
      <ChatItemSkeleton />
      <ChatItemSkeleton />
      <ChatItemSkeleton />
      <ChatItemSkeleton />
      <ChatItemSkeleton />
    </div>
  );
};

export default ChatListSkeleton;
