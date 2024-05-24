"use client";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import { useModal } from "@/hooks/use-modal-store";
import { getInitials } from "@/lib/utils";
import { Info, Settings } from "lucide-react";

interface ChatAreaHeaderProps {
  chatName: string;
  chatId: string;
  isGroup?: boolean;
}
const ChatAreaHeader = ({ chatName, isGroup, chatId }: ChatAreaHeaderProps) => {
  const { onOpen } = useModal();
  return (
    <header className="h-14 w-full border-b py-2 px-4 flex items-center justify-between">
      <div className="flex items-center gap-2">
        <Avatar>
          <AvatarFallback>
            {getInitials(chatName ?? "Unknown User")}
          </AvatarFallback>
        </Avatar>
        <span className="font-semibold">{chatName}</span>
      </div>
      <Button
        variant="ghost"
        size="icon"
        className="rounded-lg group"
        aria-label="Info"
      >
        {isGroup && (
          <Settings
            className="w-5 h-5 text-muted-foreground group-hover:text-foreground"
            onClick={() =>
              onOpen("group-settings", {
                groupId: chatId,
              })
            }
          />
        )}
      </Button>
    </header>
  );
};

export default ChatAreaHeader;
