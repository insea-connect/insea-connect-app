"use client";

import { useSession } from "next-auth/react";
import { Avatar, AvatarFallback } from "./ui/avatar";
import { useQuery } from "@tanstack/react-query";
import axios from "axios";
import { USER_INFO_ENDPOINT } from "@/lib/constants";
import { capitalize, getInitials } from "@/lib/utils";
import { Skeleton } from "./ui/skeleton";

const UserProfile = () => {
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
  return (
    <>
      <Avatar className="h-7 w-7">
        <AvatarFallback>
          {getInitials(resultData?.username ?? "Unknown User")}
        </AvatarFallback>
      </Avatar>
      <span className="hidden xl:inline text-base font-medium">
        {capitalize(resultData?.username ?? "Unknown User")}
      </span>
    </>
  );
};

export default UserProfile;
