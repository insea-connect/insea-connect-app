import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { capitalize, formatToTimeAgo, getInitials } from "@/lib/utils";

interface GroupChatItemProps {
  groupName: string;
  username?: string;
  message: string;
  date: string;
  isCurrentUser?: boolean;
}

const GroupChatItem = ({
  groupName,
  username,
  message,
  date,
  isCurrentUser,
}: GroupChatItemProps) => {
  return (
    <div className="flex items-center py-3 px-4 rounded-md hover:bg-muted/80 cursor-pointer gap-4">
      <Avatar className="h-12 w-12">
        <AvatarFallback>
          {getInitials(groupName ?? "Unknown Group")}
        </AvatarFallback>
      </Avatar>
      <div className="flex flex-col flex-1">
        <div className="flex justify-between">
          <span className="font-semibold">
            {capitalize(groupName ?? "Unknown Group")}
          </span>
          <span className="text-gray-700 dark:text-gray-500 text-[0.75rem] font-normal">
            {formatToTimeAgo(date)}
          </span>
        </div>
        <span className="text-gray-500 text-sm truncate">
          <span className="font-bold">
            {isCurrentUser ? "you" : username}:{" "}
          </span>
          {message}
        </span>
      </div>
    </div>
  );
};

export default GroupChatItem;
