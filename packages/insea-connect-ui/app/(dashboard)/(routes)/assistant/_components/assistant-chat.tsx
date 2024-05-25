"use client";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { ArrowUp } from "lucide-react";
import { useSession } from "next-auth/react";
import AssistantChatMessages from "./assistant-chat-messages";
import AssistantChatInput from "./assistant-chat-input";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";
import { NEW_ASSISTANT_MESSAGE_ENDPOINT } from "@/lib/constants";
import axios from "axios";

interface AssistantChatProps {
  isLoading: boolean;
}

const AssistantChat = () => {
  const { data } = useSession();
  const queryClient = useQueryClient();
  const [content, setContent] = useState("");
  const { mutate: sendAssistantMessage, isPending: isWaitingResponse } =
    useMutation({
      mutationKey: ["send-assistant-message"],
      onMutate: async (message: string) => {
        queryClient.setQueryData(["bot-messages"], (old: any) => [
          ...old,
          {
            content: message,
            senderId: data?.user_profile.id,
            timestamp: new Date(),
            recipientId: 1,
          },
        ]);

        const { data: response } = await axios.post(
          NEW_ASSISTANT_MESSAGE_ENDPOINT,
          {
            senderId: data?.user_profile.id,
            content: message,
            recipientId: 1,
            threadId: data?.thread_id,
          },
          {
            headers: {
              Authorization: `Bearer ${data?.tokens.access_token}`,
            },
          }
        );

        return response;
      },

      onSettled: () => {
        queryClient.invalidateQueries({
          queryKey: ["bot-messages"],
        });
      },
    });

  return (
    <>
      <AssistantChatMessages isWaitingResponse={isWaitingResponse} />
      <AssistantChatInput
        isLoading={isWaitingResponse}
        onSend={(message: string) => sendAssistantMessage(message)}
        content={content}
        setContent={setContent}
      />
    </>
  );
};

export default AssistantChat;
