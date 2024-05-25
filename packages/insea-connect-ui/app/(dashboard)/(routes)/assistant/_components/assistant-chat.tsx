"use client";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { ArrowUp } from "lucide-react";
import { useSession } from "next-auth/react";

const AssistantChat = () => {
  const { data } = useSession();

  return (
    <>
      <div className="flex-1 " />
      <div className="py-8 relative">
        <Input
          type="search"
          placeholder="Send a message..."
          className="w-full bg-background h-14 pl-4 pr-8"
        />

        <Button className="absolute right-4 bottom-1/2 translate-y-1/2 h-9 w-9 px-2">
          <ArrowUp className="text-background" />
        </Button>
      </div>
    </>
  );
};

export default AssistantChat;
