import { useSocket } from "@/components/provider/socket-provider";
import { useQueryClient } from "@tanstack/react-query";
import { useEffect, useRef } from "react";

type ChatMessage = {
  conversationId?: string;
  groupId?: number;
  senderId: number;
  content: string;
  timestamp: string;
  isGroup: boolean;
};

const useSocketChat = ({ chatId }: { chatId: string }) => {
  const { socket, isConnected } = useSocket();
  const queryClient = useQueryClient();
  const subscriptionRef = useRef<any>(null);

  useEffect(() => {
    if (!isConnected || !socket) return;

    if (subscriptionRef.current) {
      subscriptionRef.current.unsubscribe();
    }

    subscriptionRef.current = socket?.subscribe(
      `/user/${chatId}/queue/messages`,
      (payload: any) => {
        console.log("from socket body", JSON.parse(payload.body));
        const message: ChatMessage = JSON.parse(payload.body);
        queryClient.setQueryData(["chat-messages", chatId], (oldData: any) => {
          console.log("old data", oldData);

          return [...oldData, message];
        });
      }
    );

    return () => {
      if (socket && subscriptionRef.current)
        subscriptionRef.current.unsubscribe(`/user/${chatId}/queue/messages`);
    };
  }, [socket, queryClient, chatId]);
};

export default useSocketChat;
