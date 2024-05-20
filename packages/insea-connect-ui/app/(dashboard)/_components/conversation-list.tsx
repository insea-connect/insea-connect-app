import { ScrollArea } from "@/components/ui/scroll-area";
import ChatItem from "./chat-item";
import { useSession } from "next-auth/react";
import { useQuery } from "@tanstack/react-query";
import axios from "axios";
import { CONVERSATIONS_ENDPOINT } from "@/lib/constants";
import ChatListSkeleton from "./chat-list-skeleton";
import useUserProfile from "@/hooks/use-user-profile";

interface ConversationListProps {
  search?: string;
}

const ConversationList = ({ search }: ConversationListProps) => {
  const { isPending: isUserProfilePending, data: userProfile } =
    useUserProfile();
  const { data } = useSession();
  const { isPending, data: conversations } = useQuery({
    queryKey: ["conversations"],
    queryFn: async () => {
      const { data: result } = await axios.get(CONVERSATIONS_ENDPOINT, {
        headers: {
          Authorization: `Bearer ${data?.tokens.access_token}`,
        },
      });
      return result;
    },
  });

  if (isPending && isUserProfilePending) {
    return <ChatListSkeleton />;
  }

  return (
    <ScrollArea className="h-full mt-2 pt-2">
      <div className="flex flex-col gap-2 px-4 h-full">
        {conversations?.map((conversation: any) => (
          <ChatItem
            key={conversation.chatId}
            username={conversation.username}
            message={conversation?.lastMessage?.content}
            date={conversation?.lastMessage?.timestamp}
            isCurrentUser={
              userProfile.id === conversation?.lastMessage?.senderId
            }
          />
        ))}
      </div>
    </ScrollArea>
  );
};

export default ConversationList;
