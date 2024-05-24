"use client";

import {
  EllipsisVertical,
  MessageSquareText,
  Search,
  ShieldMinus,
  ShieldPlus,
  UserX2Icon,
} from "lucide-react";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from "../ui/dialog";
import { Input } from "../ui/input";
import { ScrollArea } from "../ui/scroll-area";
import { useModal } from "@/hooks/use-modal-store";
import { useSession } from "next-auth/react";
import useUserProfile from "@/hooks/use-user-profile";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import axios from "axios";
import {
  GROUP_ASSIGN_ADMIN,
  GROUP_MEMBERS,
  GROUP_REMOVE_MEMBER,
} from "@/lib/constants";
import { useState } from "react";
import { buttonVariants } from "../ui/button";
import {
  capitalize,
  cn,
  generateConversationId,
  getInitials,
} from "@/lib/utils";
import { Avatar, AvatarFallback } from "../ui/avatar";
import { Badge } from "../ui/badge";
import {
  DropdownMenu,
  DropdownMenuSeparator,
  DropdownMenuGroup,
  DropdownMenuItem,
  DropdownMenuContent,
  DropdownMenuLabel,
  DropdownMenuTrigger,
} from "../ui/dropdown-menu";
import { useRouter } from "next/navigation";
import Link from "next/link";

const GroupSettingsModal = () => {
  const [searchTerm, setSearchTerm] = useState("");
  const queryClient = useQueryClient();
  const router = useRouter();
  const { isOpen, onClose, type, data } = useModal();
  const { data: session } = useSession();
  const { data: userProfile, isPending: isUserProfilePending } =
    useUserProfile();

  const isModalOpen = isOpen && type === "group-settings";
  const groupId = data?.groupId;

  const { isPending, data: members } = useQuery({
    queryKey: ["groups", "members"],
    queryFn: async () => {
      const { data: result } = await axios.get(GROUP_MEMBERS(groupId), {
        headers: {
          Authorization: `Bearer ${session?.tokens.access_token}`,
        },
      });
      return result;
    },
    enabled: isModalOpen,
  });

  const { mutate: makeAdmin } = useMutation({
    mutationFn: async (userId: string) => {
      await axios.post(
        `${GROUP_ASSIGN_ADMIN(groupId)}`,
        {
          userId,
        },
        {
          headers: {
            Authorization: `Bearer ${session?.tokens.access_token}`,
          },
        }
      );

      return true;
    },
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["groups", "members"],
      });
    },
  });

  // TODO: Modify the revokeAdmin mutation to remove the user from the admin list
  const { mutate: revokeAdmin } = useMutation({
    mutationFn: async (userId: string) => {
      await axios.delete(`${GROUP_ASSIGN_ADMIN(groupId)}`, {
        headers: {
          Authorization: `Bearer ${session?.tokens.access_token}`,
        },
      });
    },
  });

  const { mutate: removeMember } = useMutation({
    mutationFn: async (userId: number) => {
      await axios.delete(`${GROUP_REMOVE_MEMBER(groupId, userId)}`, {
        headers: {
          Authorization: `Bearer ${session?.tokens.access_token}`,
        },
      });
    },
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["groups", "members"],
      });
    },
  });

  if (isPending || isUserProfilePending) {
    return null;
  }

  const currentUserAsMember = members?.find(
    (member: any) => member.id === userProfile?.id
  );

  let filteredMembers = members?.filter((member: any) =>
    member.username.toLowerCase().includes(searchTerm.toLowerCase())
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
            Members
          </DialogTitle>
          <DialogDescription className="text-center">
            {members?.length} members
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
          {filteredMembers.map((user: any) => (
            <div
              key={user.id}
              className={cn(
                buttonVariants({ variant: "ghost" }),
                "flex gap-3 flex-1 w-full h-16 p-4 justify-between items-center"
              )}
            >
              <div className="flex justify-start gap-3">
                <Avatar className="size-10 flex items-center justify-center bg-muted">
                  <AvatarFallback>
                    {getInitials(user.username ?? "Unknown User")}
                  </AvatarFallback>
                </Avatar>
                <div className="flex flex-col text-start">
                  <div className="flex gap-2">
                    <p className="font-bold">
                      {capitalize(user.username ?? "unknown user")}
                    </p>
                    <div className="flex gap-1">
                      {user.isCreator && (
                        <Badge variant={"outline"}>Creator</Badge>
                      )}
                      {user.isAdmin && !user.isCreator && (
                        <Badge variant={"outline"}>Admin</Badge>
                      )}
                    </div>
                  </div>
                  <p className="text-sm text-gray-500">{user.email}</p>
                </div>
              </div>
              {currentUserAsMember?.id !== user.id && (
                <DropdownMenu>
                  <DropdownMenuTrigger asChild>
                    <EllipsisVertical />
                  </DropdownMenuTrigger>
                  <DropdownMenuContent className="z-[9999]">
                    <DropdownMenuLabel>Actions</DropdownMenuLabel>
                    <DropdownMenuSeparator />
                    <DropdownMenuGroup>
                      <DropdownMenuItem>
                        <Link
                          href={`${`/chat/${generateConversationId(
                            userProfile?.id,
                            user.id
                          )}`}`}
                          onClick={() => handleClose()}
                          className="flex items-center cursor-auto"
                        >
                          <MessageSquareText className="size-4 mr-2" />
                          Open Conversation
                        </Link>
                      </DropdownMenuItem>
                    </DropdownMenuGroup>

                    {currentUserAsMember?.isCreator && (
                      <>
                        <DropdownMenuSeparator />

                        <DropdownMenuGroup>
                          {!user.isAdmin ? (
                            <DropdownMenuItem
                              onClick={() => makeAdmin(user.id)}
                            >
                              <ShieldPlus className="size-4 mr-2" />
                              Make group Admin
                            </DropdownMenuItem>
                          ) : (
                            <DropdownMenuItem
                              onClick={() => revokeAdmin(user.id)}
                            >
                              <ShieldMinus className="size-4 mr-2" />
                              Dismiss as Admin
                            </DropdownMenuItem>
                          )}
                        </DropdownMenuGroup>
                      </>
                    )}

                    {((currentUserAsMember?.isAdmin && !user.isAdmin) ||
                      currentUserAsMember?.isCreator) && (
                      <>
                        <DropdownMenuSeparator />
                        <DropdownMenuGroup>
                          <DropdownMenuItem
                            className="group"
                            onClick={() => removeMember(user.id)}
                          >
                            <UserX2Icon className="size-4 mr-2 text-red-500 group-hover:text-red-700" />
                            <span className="text-red-500 group-hover:text-red-700">
                              Remove from group
                            </span>
                          </DropdownMenuItem>
                        </DropdownMenuGroup>
                      </>
                    )}
                  </DropdownMenuContent>
                </DropdownMenu>
              )}
            </div>
          ))}
        </ScrollArea>
      </DialogContent>
    </Dialog>
  );
};

export default GroupSettingsModal;
