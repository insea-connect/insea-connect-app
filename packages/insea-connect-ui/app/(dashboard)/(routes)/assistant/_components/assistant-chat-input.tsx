"use client";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { NEW_ASSISTANT_MESSAGE_ENDPOINT } from "@/lib/constants";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import axios from "axios";
import { ArrowUp } from "lucide-react";
import { useSession } from "next-auth/react";
import { useState } from "react";

interface AssistantChatInputProps {
  isLoading: boolean;
  onSend: (content: string) => void;
  content: string;
  setContent: (content: string) => void;
}

const AssistantChatInput = ({
  content,
  isLoading,
  onSend,
  setContent,
}: AssistantChatInputProps) => {
  return (
    <div className="py-8 relative">
      <Input
        disabled={isLoading}
        type="search"
        placeholder="Send a message..."
        className="w-full bg-background h-14 pl-4 pr-8"
        value={content}
        onChange={(e) => setContent(e.target.value)}
        onKeyDown={(e) => {
          if (content.trim() === "") return;
          if (e.key === "Enter") {
            setContent("");
            onSend(content);
          }
        }}
      />

      <Button
        disabled={isLoading}
        className="absolute right-4 bottom-1/2 translate-y-1/2 h-9 w-9 px-2"
      >
        <ArrowUp className="text-background" />
      </Button>
    </div>
  );
};

export default AssistantChatInput;
