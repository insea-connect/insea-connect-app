import { auth } from "@/auth";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  CONVERSATION_INFO_ENDPOINT,
  GROUP_INFO_ENDPOINT,
  USER_INFO_ENDPOINT,
} from "@/lib/constants";
import { getInitials } from "@/lib/utils";
import axios from "axios";
import { Info, Paperclip, SendHorizonal } from "lucide-react";
import ChatAreaHeader from "./chat-area-header";
import ChatMessagesList from "./chat-messages-list";
import MessageInput from "./message-input";
import { getOrCreateConversation } from "@/services/api";

interface ChatAreaProps {
  chatId: string;
}

const ChatArea = async ({ chatId }: ChatAreaProps) => {
  const {
    // @ts-ignore
    tokens: { access_token },
  } = await auth();

  const {
    data: { id },
  } = await axios.get(USER_INFO_ENDPOINT, {
    headers: {
      Authorization: `Bearer ${access_token}`,
    },
  });

  const isGroupChat = chatId.startsWith("group-");

  const theChatId = chatId.split("-")[1];

  let chatName = "";
  let otherUser = null;
  let groupId = null;
  if (isGroupChat) {
    const {
      data: { id, name },
    } = await axios.get(`${GROUP_INFO_ENDPOINT}/${theChatId}`, {
      headers: {
        Authorization: `Bearer ${access_token}`,
      },
    });

    chatName = name;
    groupId = id;
  } else {
    const result = await getOrCreateConversation(theChatId, id, access_token);

    chatName = result.chatName;
    otherUser = result.otherUser;
  }

  return (
    <section
      aria-labelledby="primary-heading"
      className="flex min-w-0 flex-1 flex-col lg:order-last h-dvh"
    >
      <ChatAreaHeader
        chatId={theChatId}
        chatName={chatName}
        isGroup={isGroupChat}
        otherUser={otherUser}
      />
      <ChatMessagesList
        chatId={theChatId}
        isGroup={isGroupChat}
        connectedUserId={id}
      />
      <MessageInput
        isGroup={isGroupChat}
        senderId={id}
        recipientId={isGroupChat ? null : otherUser.id}
        groupId={isGroupChat && groupId}
      />
    </section>
  );
};

export default ChatArea;
