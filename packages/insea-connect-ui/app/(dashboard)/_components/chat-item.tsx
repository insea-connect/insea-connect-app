import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";

const ChatItem = () => {
  return (
    <div className="flex items-center py-3 px-4 rounded-md hover:bg-muted/80 cursor-pointer gap-4">
      <Avatar className="h-12 w-12">
        <AvatarImage src="/avatar.jpg" alt="John Doe" />
        <AvatarFallback>JD</AvatarFallback>
      </Avatar>
      <div className="flex flex-col flex-1">
        <div className="flex justify-between">
          <span className="font-semibold">John Doe</span>
          <span className="text-gray-700 text-[0.75rem] font-normal">
            Yesterday
          </span>
        </div>
        <span className="text-gray-500 text-sm truncate">Thank you</span>
      </div>
    </div>
  );
};

export default ChatItem;
