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

  const { data } = await axios.get(
    `${
      isGroupChat
        ? `${GROUP_INFO_ENDPOINT}/${theChatId}`
        : `${CONVERSATION_INFO_ENDPOINT}/${theChatId}`
    }`,
    {
      headers: {
        Authorization: `Bearer ${access_token}`,
      },
    }
  );

  console.log(data);

  let chatName = "";
  if (isGroupChat) {
    chatName = data.name;
  } else {
    const otherUser = data.member1.id === id ? data.member2 : data.member1;
    chatName = otherUser.username;
  }

  return (
    <section
      aria-labelledby="primary-heading"
      className="flex h-full min-w-0 flex-1 flex-col overflow-y-auto lg:order-last"
    >
      <ChatAreaHeader chatName={chatName} />
      <ChatMessagesList
        chatId={theChatId}
        isGroup={isGroupChat}
        connectedUserId={id}
      />
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
        />
        <Button size="icon" className="rounded-lg" aria-label="Info">
          <SendHorizonal className="w-5 h-5" />
        </Button>
      </div>
    </section>
  );
};

export default ChatArea;
