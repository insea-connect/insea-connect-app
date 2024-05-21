"use client";

import { signOut, useSession } from "next-auth/react";
import { Avatar, AvatarFallback } from "./ui/avatar";
import { useQuery } from "@tanstack/react-query";
import axios from "axios";
import { LOGOUT_ENDPOINT, USER_INFO_ENDPOINT } from "@/lib/constants";
import { capitalize, getInitials } from "@/lib/utils";
import { Skeleton } from "./ui/skeleton";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "./ui/dropdown-menu";
import { useRouter } from "next/navigation";

const UserProfile = () => {
  const router = useRouter();
  const { data } = useSession();
  const { isLoading, data: resultData } = useQuery({
    queryKey: ["user-profile"],
    queryFn: async () => {
      const { data: userData } = await axios.get(USER_INFO_ENDPOINT, {
        headers: {
          Authorization: `Bearer ${data?.tokens.access_token}`,
        },
      });

      return userData;
    },
  });

  if (isLoading) {
    return <Skeleton className="h-7 w-full" />;
  }

  const onLogOut = async () => {
    const refreshToken = data?.tokens.refresh_token;
    const accessToken = data?.tokens.access_token;

    try {
      await axios.post(
        LOGOUT_ENDPOINT,
        {
          refreshToken,
        },
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        }
      );

      await signOut();
    } catch (error) {
      console.error(error);
    }

    router.refresh();
  };

  return (
    <DropdownMenu>
      <DropdownMenuTrigger>
        <div className="xl:flex xl:gap-2 xl:px-3 xl:items-center relative w-full">
          <Avatar className="h-7 w-7">
            <AvatarFallback>
              {getInitials(resultData?.username ?? "Unknown User")}
            </AvatarFallback>
          </Avatar>
          <span className="hidden xl:inline text-base font-medium">
            {capitalize(resultData?.username ?? "Unknown User")}
          </span>
        </div>
      </DropdownMenuTrigger>
      <DropdownMenuContent>
        <DropdownMenuItem onClick={onLogOut}>Log out</DropdownMenuItem>
      </DropdownMenuContent>
    </DropdownMenu>
  );
};

export default UserProfile;
