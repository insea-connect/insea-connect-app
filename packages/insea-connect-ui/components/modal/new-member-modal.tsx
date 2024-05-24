"use client";

import { useSession } from "next-auth/react";
import { useRouter } from "next/navigation";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "../ui/dialog";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import axios from "axios";
import { ALL_USERS_ENDPOINT, GROUP_MEMBERS } from "@/lib/constants";
import { ScrollArea } from "../ui/scroll-area";
import { AvatarFallback } from "@radix-ui/react-avatar";
import { Search, SendHorizonal } from "lucide-react";
import { Avatar } from "../ui/avatar";
import {
  capitalize,
  cn,
  generateConversationId,
  getInitials,
} from "@/lib/utils";
import { Button, buttonVariants } from "../ui/button";
import { Input } from "../ui/input";
import { useState } from "react";
import useUserProfile from "@/hooks/use-user-profile";
import { useModal } from "@/hooks/use-modal-store";
import { Checkbox } from "../ui/checkbox";
import { useToast } from "../ui/use-toast";

// TODO: Refactor this component, it rerenders two times on
const NewMemberModal = () => {
  const queryClient = useQueryClient();
  const { data: session } = useSession();
  //   const { data: userProfile, isPending: isUserProfilePending } =
  //     useUserProfile();

  const { toast } = useToast();
  const [searchTerm, setSearchTerm] = useState("");
  const router = useRouter();
  const { isOpen, onClose, type, data } = useModal();
  const isModalOpen = isOpen && type === "new-member";
  const groupId = data?.groupId;
  const { isPending: AreUsersPending, data: users } = useQuery({
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
    refetchInterval: false,
    refetchOnWindowFocus: false,
  });

  const { isPending: AreMembersPending, data: members } = useQuery({
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
    refetchInterval: false,
    refetchOnWindowFocus: false,
  });

  const { mutate: addMembers } = useMutation({
    mutationFn: async (members: any) => {
      const { data: result } = await axios.post(
        GROUP_MEMBERS(groupId),
        {
          members,
        },
        {
          headers: {
            Authorization: `Bearer ${session?.tokens.access_token}`,
          },
        }
      );
      return result;
    },
    onSuccess: () => {
      toast({
        title: "Success",
        description: "Member(s) added successfully",
      });
    },
  });

  if (AreMembersPending || AreUsersPending) {
    return null;
  }

  const nonMembers = users.filter((user: any) => {
    return !members.some((member: any) => member.id === user.id);
  });

  const filteredNonMembers = nonMembers.filter((user: any) =>
    user.username.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const handleClose = () => {
    setSearchTerm("");
    onClose();
  };

  return (
    <Dialog open={isModalOpen} onOpenChange={handleClose}>
      <DialogContent className="p-0 pb-6 overflow-hidden z-[9999]">
        <DialogHeader className="pt-8 px-8">
          <DialogTitle className="text-2xl font-bold">
            Add members to group
          </DialogTitle>
          <DialogDescription className="">
            Search for a user to add to the group
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
        <form
          onSubmit={(e) => {
            e.preventDefault();
            const formData = new FormData(e.target as HTMLFormElement);
            const members = formData.getAll("members");
            addMembers(members);
            onClose();
          }}
        >
          <ScrollArea className="h-80 px-4">
            {filteredNonMembers.map((user: any) => (
              <label
                htmlFor={user.id}
                key={user.id}
                className={cn(
                  buttonVariants({
                    variant: "ghost",
                  }),
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
                    <p className="font-bold">
                      {capitalize(user.username ?? "unknown user")}
                    </p>
                    <p className="text-sm text-gray-500">{user.email}</p>
                  </div>
                </div>
                <Checkbox
                  id={user.id}
                  value={user.id}
                  name="members"
                  className="border-muted"
                />
              </label>
            ))}
          </ScrollArea>
          <DialogFooter className="px-8 pt-4">
            <Button type="submit">Add members</Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
};

export default NewMemberModal;
