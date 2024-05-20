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
  });

  const messagesContainerRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (messagesContainerRef.current) {
      messagesContainerRef.current.scrollTop =
        messagesContainerRef.current.scrollHeight;
    }
  }, [messages]);

  if (isMessagesPending && isUserProfilePending) return <span>Loading...</span>;

  return (
    <div
      ref={messagesContainerRef}
      className="w-full overflow-y-auto overflow-x-hidden h-full flex flex-col justify-end"
    >
      <AnimatePresence>
        {messages?.map((message: any, index: number) => (
          <motion.div
            key={index}
            layout
            initial={{ opacity: 0, scale: 1, y: 50, x: 0 }}
            animate={{ opacity: 1, scale: 1, y: 0, x: 0 }}
            exit={{ opacity: 0, scale: 1, y: 1, x: 0 }}
            transition={{
              opacity: { duration: 0.1 },
              layout: {
                type: "spring",
                bounce: 0.3,
                duration: messages.indexOf(message) * 0.05 + 0.2,
              },
            }}
            style={{
              originX: 0.5,
              originY: 0.5,
            }}
            className={cn(
              "flex flex-col gap-2 p-4 whitespace-pre-wrap",
              message.senderId === connectedUserId ? "items-end" : "items-start"
            )}
          >
            <div className="flex gap-3 items-center">
              {message.senderId !== connectedUserId && (
                <Avatar className="flex justify-center items-center">
                  <AvatarFallback>
                    {getInitials(message.senderName)}
                  </AvatarFallback>
                </Avatar>
              )}
              <div className="flex flex-col gap-1">
                <div
                  className={cn(`flex items-center justify-between flex-row`)}
                >
                  <span className="font-medium">{message.senderName}</span>
                  <span className="text-gray-500 text-[0.75rem] font-normal">
                    {format(new Date(message.timestamp), DATE_FORM)}
                  </span>
                </div>

                <span
                  className={cn(
                    `bg-accent p-3 rounded-md max-w-xs`,

                    message.senderId === connectedUserId
                      ? ""
                      : "bg-green-800 text-white"
                  )}
                >
                  {message.content}
                </span>
              </div>
              {message.senderId === connectedUserId && (
                <Avatar className="flex justify-center items-center">
                  <AvatarFallback>
                    {getInitials(message.senderName)}
                  </AvatarFallback>
                </Avatar>
              )}
            </div>
          </motion.div>
        ))}
      </AnimatePresence>
    </div>
  );
};

export default ChatMessagesList;
