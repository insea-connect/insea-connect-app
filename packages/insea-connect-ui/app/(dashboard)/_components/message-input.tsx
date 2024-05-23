"use client";

import { useSocket } from "@/components/provider/socket-provider";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { useQueryClient } from "@tanstack/react-query";
import { Paperclip, SendHorizonal } from "lucide-react";
import { useState } from "react";

interface MessageInputProps {
  isGroup: boolean;
  senderId: number;
  recipientId?: number;
  groupId?: number;
}

const MessageInput = ({
  isGroup,
  recipientId,
  senderId,
  groupId,
}: MessageInputProps) => {
  const { socket } = useSocket();
  const [content, setContent] = useState("");
  const queryClient = useQueryClient();

  const onSendMessage = () => {
    const destination = isGroup ? `/app/sendgroupmessage` : `/app/sendmessage`;
    const body = !isGroup
      ? {
          senderId,
          recipientId,
          content,
        }
      : {
          groupId,
          senderId,
          content,
        };
    socket?.publish({
      destination,
      body: JSON.stringify(body),
    });

    queryClient.invalidateQueries({
      queryKey: ["conversations"],
    });

    queryClient.invalidateQueries({
      queryKey: ["groups"],
    });
    setContent("");
  };
  return (
    <div className="h-14 border-t flex py-2 px-4 gap-4 items-center">
      <Button
        variant="ghost"
        size="icon"
        className="rounded-lg group"
        aria-label="Info"
      >
        <Paperclip className="w-5 h-5 text-muted-foreground group-hover:text-foreground" />
      </Button>
      <Input
        type="text"
        placeholder="Type a message..."
        className="flex-1 bg-background px-4 py-2 rounded-md"
        value={content}
        onChange={(e) => setContent(e.target.value)}
        onKeyDown={(e) => {
          if (e.key === "Enter") {
            onSendMessage();
          }
        }}
      />
      <Button
        onClick={onSendMessage}
        size="icon"
        className="rounded-lg"
        aria-label="Info"
      >
        <SendHorizonal className="w-5 h-5" />
      </Button>
    </div>
  );
};

export default MessageInput;
