"use client";
import { useEffect, useRef } from "react";
import { AnimatePresence, motion } from "framer-motion";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { useQuery } from "@tanstack/react-query";
import useUserProfile from "@/hooks/use-user-profile";
import axios from "axios";
import { format } from "date-fns";
import { useSession } from "next-auth/react";
import {
  CONVERSATION_INFO_ENDPOINT,
  GROUP_INFO_ENDPOINT,
} from "@/lib/constants";
import { cn, getInitials } from "@/lib/utils";
import ChatBubble from "./chat-bubble";
import ChatMessagesListSkeleton from "./chat-messages-list-skeleton";
import useSocketChat from "@/hooks/use-socket-chat";
import { ScrollArea } from "@/components/ui/scroll-area";

interface ChatMessagesListProps {
  chatId: string;
  isGroup?: boolean;
  connectedUserId?: number;
}

const DATE_FORM = "HH:mm";

const ChatMessagesList = ({
  chatId,
  isGroup,
  connectedUserId,
}: ChatMessagesListProps) => {
  const { isPending: isUserProfilePending, data: userProfile } =
    useUserProfile();

  const { data } = useSession();

  const { data: messages, isPending: isMessagesPending } = useQuery({
    queryKey: ["chat-messages", chatId],
    queryFn: async () => {
      const { data: result } = await axios.get(
        `${
          isGroup ? GROUP_INFO_ENDPOINT : CONVERSATION_INFO_ENDPOINT
        }/${chatId}/messages`,
        {
          headers: {
            Authorization: `Bearer ${data?.tokens.access_token}`,
          },
        }
      );
      return result;
    },
    refetchOnWindowFocus: false,
    refetchInterval: false,
  });

  const bottomRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (bottomRef.current) {
      bottomRef.current.scrollIntoView({ behavior: "smooth" });
    }
  }, [messages]);

  useSocketChat({ chatId });

  if (isMessagesPending && isUserProfilePending)
    return <ChatMessagesListSkeleton />;

  return (
    <ScrollArea className="w-full flex-1">
      <div className="mt-auto">
        <AnimatePresence>
          {messages?.map((message: any, index: number) => (
            <ChatBubble
              connectedUserId={connectedUserId}
              index={index}
              key={index}
              message={message}
            />
          ))}
        </AnimatePresence>
      </div>

      <div ref={bottomRef} />
    </ScrollArea>
  );
};

export default ChatMessagesList;
