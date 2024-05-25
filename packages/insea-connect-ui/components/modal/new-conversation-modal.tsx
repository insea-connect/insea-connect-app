"use client";

import { useModal } from "@/hooks/use-modal-store";
import { useSession } from "next-auth/react";
import { useRouter } from "next/navigation";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from "../ui/dialog";
import { useQuery, useQueryClient } from "@tanstack/react-query";
import axios from "axios";
import { ALL_USERS_ENDPOINT } from "@/lib/constants";
import { ScrollArea } from "../ui/scroll-area";
import { AvatarFallback } from "@radix-ui/react-avatar";
import { Search, SendHorizonal } from "lucide-react";
import { Avatar } from "../ui/avatar";
import { capitalize, generateConversationId, getInitials } from "@/lib/utils";
import { Button } from "../ui/button";
import { Input } from "../ui/input";
import { useState } from "react";
import useUserProfile from "@/hooks/use-user-profile";

const NewConversationModal = () => {
  const queryClient = useQueryClient();
  const { data: session } = useSession();
  const { data: userProfile, isPending: isUserProfilePending } =
    useUserProfile();
  const [searchTerm, setSearchTerm] = useState("");
  const router = useRouter();
  const { isOpen, onClose, type } = useModal();
  const isModalOpen = isOpen && type === "new-conversation";
  const { isPending, data: users } = useQuery({
    queryKey: ["users"],
    queryFn: async () => {
      const { data: result } = await axios.get(ALL_USERS_ENDPOINT, {
        headers: {
          Authorization: `Bearer ${session?.tokens.access_token}`,
        },
      });
      return result;
    },
    enabled: isModalOpen,
  });

  if (isPending || isUserProfilePending) {
    return null;
  }

  let filteredUsers = users.filter((user: any) =>
    user.username.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const handleClose = () => {
    setSearchTerm("");
    onClose();
  };

  return (
    <Dialog open={isModalOpen} onOpenChange={handleClose}>
      <DialogContent className="p-0 pb-6 overflow-hidden z-[9999]">
        <DialogHeader className="pt-8 px-6">
          <DialogTitle className="text-2xl text-center font-bold">
            Start a new conversation
          </DialogTitle>
          <DialogDescription className="text-center">
            Search for a user to start a conversation with
          </DialogDescription>
        </DialogHeader>

        <div className="px-8">
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

        <ScrollArea className="h-80 px-4">
          {filteredUsers.map((user: any) => (
            <Button
              key={user.id}
              variant={"ghost"}
              className="flex gap-3 flex-1 w-full h-16 p-4 justify-start"
              onClick={() => {
                const convId = generateConversationId(user.id, userProfile?.id);
                router.push(`/chat/${convId}`);
                queryClient.invalidateQueries({
                  queryKey: ["conversations"],
                });
                setSearchTerm("");
                onClose();
              }}
            >
              <Avatar className="size-10 flex items-center justify-center bg-muted">
                <AvatarFallback>
                  {getInitials(user.username ?? "Unknown User")}
                </AvatarFallback>
              </Avatar>
              <div className="flex flex-col text-start">
                <p className="font-bold">
                  {capitalize(user.username ?? "unknown user")}
                </p>
                <p className="text-sm text-gray-500">{user.email}</p>
              </div>
            </Button>
          ))}
        </ScrollArea>
      </DialogContent>
    </Dialog>
  );
};

export default NewConversationModal;
