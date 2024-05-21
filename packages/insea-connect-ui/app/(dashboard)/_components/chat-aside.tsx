"use client";
import { Input } from "@/components/ui/input";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Search, SquarePen } from "lucide-react";
import NewChatButton from "@/components/new-chat-button";
import { useState } from "react";
import ConversationList from "./conversation-list";
import GroupList from "./group-list";
import { usePathname, useRouter } from "next/navigation";
import { cn } from "@/lib/utils";

interface ChatAsideProps {
  asideClassName?: string;
  divClassName?: string;
}

const ChatAside = ({ asideClassName, divClassName }: ChatAsideProps) => {
  const pathname = usePathname();
  const isRootChatPage = pathname === "/chat";
  const [searchTerm, setSearchTerm] = useState("");
  return (
    <aside
      className={cn(
        isRootChatPage
          ? "sm:block flex-1 lg:flex-shrink-0"
          : "hidden lg:order-first lg:block lg:flex-shrink-0"
      )}
    >
      <div
        className={cn(
          isRootChatPage
            ? "gap-3 pt-2 relative flex h-full w-full lg:w-96 flex-col border-r"
            : "gap-3 pt-2 relative flex h-full w-96 flex-col border-r"
        )}
      >
        <div className="flex flex-col px-4 gap-2">
          <div className="flex items-center justify-between">
            <h2 className="text-2xl font-semibold tracking-tight first:mt-0">
              Chats
            </h2>
            <NewChatButton />
          </div>
          <div className="relative">
            <Search className="absolute left-2.5 top-2.5 h-4 w-4 text-muted-foreground" />
            <Input
              type="search"
              placeholder="Search..."
              onChange={(e) => setSearchTerm(e.target.value)}
              value={searchTerm}
              className="w-full bg-background pl-8 "
            />
          </div>
        </div>
        <Tabs className="w-full" defaultValue="conversations">
          <div className="px-4">
            <TabsList className="rounded-md grid w-full grid-cols-2 ">
              <TabsTrigger value="conversations">Conversations</TabsTrigger>
              <TabsTrigger value="groups">Groups</TabsTrigger>
            </TabsList>
          </div>
          <TabsContent value="conversations" className="relative">
            <ConversationList search={searchTerm} />
          </TabsContent>
          <TabsContent value="groups">
            <GroupList search={searchTerm} />
          </TabsContent>
        </Tabs>
      </div>
    </aside>
  );
};

export default ChatAside;
