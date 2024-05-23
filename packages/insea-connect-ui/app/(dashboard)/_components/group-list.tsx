import { ScrollArea } from "@/components/ui/scroll-area";
import ChatItem from "./chat-item";
import { useSession } from "next-auth/react";
import { useQuery } from "@tanstack/react-query";
import axios from "axios";
import { GROUPS_ENDPOINT } from "@/lib/constants";
import ChatListSkeleton from "./chat-list-skeleton";
import GroupChatItem from "./group-chat-item";
import useUserProfile from "@/hooks/use-user-profile";

interface GroupListProps {
  search?: string;
}

const GroupList = ({ search }: GroupListProps) => {
  const { data: userProfile, isPending: isUserProfileLoading } =
    useUserProfile();
  const { data } = useSession();
  const { isPending, data: groups } = useQuery({
    queryKey: ["groups"],
    queryFn: async () => {
      const { data: result } = await axios.get(GROUPS_ENDPOINT, {
        headers: {
          Authorization: `Bearer ${data?.tokens.access_token}`,
        },
      });
      return result;
    },
  });

  if (isPending || isUserProfileLoading) {
    return <ChatListSkeleton />;
  }

  let filteredGroups =
    groups && search
      ? groups.filter((group: any) =>
          group.name.toLowerCase().includes(search.toLowerCase())
        )
      : groups;

  return (
    <ScrollArea className="h-full mt-2 pt-2">
      <div className="flex flex-col gap-2 px-4 h-full">
        {filteredGroups?.map((group: any) => (
          <ChatItem
            key={group.id}
            id={group.id}
            username={group.name}
            message={group?.lastMessage?.content}
            date={group?.lastMessage?.timestamp}
            isCurrentUser={group?.lastMessage?.senderId === userProfile?.id}
            isGroup
            senderName={group?.lastMessage?.senderName}
          />
        ))}
      </div>
    </ScrollArea>
  );
};

export default GroupList;
