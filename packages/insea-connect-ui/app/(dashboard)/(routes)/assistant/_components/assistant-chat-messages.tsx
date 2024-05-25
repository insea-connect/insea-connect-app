"use client";
import ChatBubble from "@/app/(dashboard)/_components/chat-bubble";
import ChatMessagesListSkeleton from "@/app/(dashboard)/_components/chat-messages-list-skeleton";
import { ScrollArea } from "@/components/ui/scroll-area";
import { CONVERSATION_INFO_ENDPOINT } from "@/lib/constants";
import { generateConversationId } from "@/lib/utils";
import { useQuery } from "@tanstack/react-query";
import axios from "axios";
import { AnimatePresence } from "framer-motion";
import { useSession } from "next-auth/react";
import { useEffect, useRef } from "react";

const AssistantChatMessages = () => {
  const { data } = useSession();

  const { data: messages, isPending: isMessagesPending } = useQuery({
    queryKey: ["bot-messages"],
    queryFn: async () => {
      const { data: result } = await axios.get(
        `${CONVERSATION_INFO_ENDPOINT}/1_${data?.user_profile.id!}/messages`,
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

  if (isMessagesPending) {
    return <ChatMessagesListSkeleton />;
  }

  return (
    <ScrollArea className="w-full flex-1">
      <AnimatePresence>
        {messages?.map((message: any, index: number) => (
          <ChatBubble
            connectedUserId={data?.user_profile.id as number}
            index={index}
            key={index}
            message={message}
          />
        ))}
      </AnimatePresence>
      <div ref={bottomRef}></div>
    </ScrollArea>
  );
};

export default AssistantChatMessages;
