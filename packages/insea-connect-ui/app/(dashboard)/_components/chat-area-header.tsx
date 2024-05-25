"use client";
import { useSocket } from "@/components/provider/socket-provider";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import { useModal } from "@/hooks/use-modal-store";
import { getInitials } from "@/lib/utils";
import { Info, Settings, UserPlus } from "lucide-react";
import { useSession } from "next-auth/react";
import { useEffect, useRef, useState } from "react";

interface ChatAreaHeaderProps {
  chatName: string;
  chatId: string;
  isGroup?: boolean;
}
const ChatAreaHeader = ({ chatName, isGroup, chatId }: ChatAreaHeaderProps) => {
  const { onOpen } = useModal();

  const { data } = useSession();

  const { socket, isConnected } = useSocket();
  const [isTyping, setIsTyping] = useState(false);
  const subscriptionRef = useRef<any>(null);
  const typingTimeoutRef = useRef<any>(null);

  useEffect(() => {
    if (!isConnected || !socket) return;

    if (subscriptionRef.current) {
      subscriptionRef.current.unsubscribe();
    }

    console.log("chatId", chatId);
    subscriptionRef.current = socket?.subscribe(
      `/user/${chatId}/queue/typing`,
      (payload: any) => {
        const { senderId } = JSON.parse(payload.body);

        if (senderId !== data?.user_profile.id) {
          setIsTyping(true);

          if (typingTimeoutRef.current) {
            clearTimeout(typingTimeoutRef.current);
          }

          typingTimeoutRef.current = setTimeout(() => {
            setIsTyping(false);
          }, 100);
        }
      }
    );

    return () => {
      if (socket && subscriptionRef.current)
        subscriptionRef.current.unsubscribe(`/user/${chatId}/queue/typing`);
    };
  }, [isConnected, socket, chatId]);

  return (
    <header className="h-14 w-full border-b py-2 px-4 flex items-center justify-between">
      <div className="flex items-center gap-2">
        <Avatar>
          <AvatarFallback>
            {getInitials(chatName ?? "Unknown User")}
          </AvatarFallback>
        </Avatar>
        <div className="flex flex-col">
          <span className="font-semibold">{chatName}</span>
          {isTyping && (
            <span className="text-muted-foreground text-xs">typing...</span>
          )}
        </div>
      </div>

      {isGroup && (
        <div className="flex items-center gap-2">
          <Button
            variant="ghost"
            size="icon"
            className="rounded-lg group"
            aria-label="Info"
            onClick={() =>
              onOpen("new-member", {
                groupId: chatId,
              })
            }
          >
            <UserPlus className="w-5 h-5 text-muted-foreground group-hover:text-foreground" />
          </Button>
          <Button
            variant="ghost"
            size="icon"
            className="rounded-lg group"
            aria-label="Info"
            onClick={() =>
              onOpen("group-settings", {
                groupId: chatId,
              })
            }
          >
            <Settings className="w-5 h-5 text-muted-foreground group-hover:text-foreground" />
          </Button>
        </div>
      )}
    </header>
  );
};

export default ChatAreaHeader;
