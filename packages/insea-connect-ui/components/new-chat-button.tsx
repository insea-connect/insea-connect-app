"use client";
import { MessageCirclePlus, SquarePen, Users } from "lucide-react";
import { Button } from "./ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "./ui/dropdown-menu";
import { useModal } from "@/hooks/use-modal-store";

const NewChatButton = () => {
  const { onOpen } = useModal();

  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <Button
          variant={"ghost"}
          size={"icon"}
          className={"rounded-lg"}
          aria-label={"New chat"}
        >
          <SquarePen className={"w-5 h-5"} />
        </Button>
      </DropdownMenuTrigger>
      <DropdownMenuContent>
        <DropdownMenuLabel>New Chat</DropdownMenuLabel>
        <DropdownMenuSeparator />
        <DropdownMenuItem onClick={() => onOpen("new-group")}>
          <Users className={"w-4 h-4 mr-2"} />
          New Group
        </DropdownMenuItem>
        <DropdownMenuItem>
          <MessageCirclePlus className={"w-4 h-4 mr-2"} />
          New Conversation
        </DropdownMenuItem>
      </DropdownMenuContent>
    </DropdownMenu>
  );
};

export default NewChatButton;
