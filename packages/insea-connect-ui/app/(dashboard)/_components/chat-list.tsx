import { ScrollArea } from "@/components/ui/scroll-area";
import ChatItem from "./chat-item";
import { useSession } from "next-auth/react";
import { useQuery } from "@tanstack/react-query";
import axios from "axios";
import { CONVERSATIONS_ENDPOINT } from "@/lib/constants";

interface ChatListProps {
  search?: string;
}

const ChatList = ({ search }: ChatListProps) => {
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

  if (isPending) {
    return <div>Loading...</div>;
  }

  return (
    <ScrollArea className="h-full mt-2 pt-2">
      <div className="flex flex-col gap-2 px-4 h-full">
        {conversations?.map((conversation, i) => (
          <span key={i}>conversation</span>
        ))}
      </div>
    </ScrollArea>
  );
};

export default ChatList;
