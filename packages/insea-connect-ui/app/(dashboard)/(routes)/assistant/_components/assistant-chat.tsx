"use client";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { ArrowUp } from "lucide-react";
import { useSession } from "next-auth/react";
import AssistantChatMessages from "./assistant-chat-messages";
import AssistantChatInput from "./assistant-chat-input";

const AssistantChat = () => {
  const { data } = useSession();

  return (
    <>
      <AssistantChatMessages />
      <AssistantChatInput />
    </>
  );
};

export default AssistantChat;
