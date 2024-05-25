"use client";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { NEW_ASSISTANT_MESSAGE_ENDPOINT } from "@/lib/constants";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import axios from "axios";
import { ArrowUp } from "lucide-react";
import { useSession } from "next-auth/react";
import { useState } from "react";

const AssistantChatInput = () => {
  const queryClient = useQueryClient();
  const [content, setContent] = useState("");
  const { data } = useSession();
  const { mutate: sendAssistantMessage, isPending: isSendingMessage } =
    useMutation({
      mutationKey: ["send-assistant-message"],
      onMutate: async (message: string) => {
        await axios.post(
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

        queryClient.invalidateQueries({
          queryKey: ["bot-messages"],
        });
      },
      onSuccess: () => {
        queryClient.invalidateQueries({
          queryKey: ["bot-messages"],
        });
      },
    });

  return (
    <div className="py-8 relative">
      <Input
        disabled={isSendingMessage}
        type="search"
        placeholder="Send a message..."
        className="w-full bg-background h-14 pl-4 pr-8"
        onChange={(e) => setContent(e.target.value)}
        onKeyDown={(e) => {
          if (e.key === "Enter") {
            sendAssistantMessage(content);
            setContent("");
          }
        }}
      />

      <Button
        disabled={isSendingMessage}
        className="absolute right-4 bottom-1/2 translate-y-1/2 h-9 w-9 px-2"
      >
        <ArrowUp className="text-background" />
      </Button>
    </div>
  );
};

export default AssistantChatInput;
