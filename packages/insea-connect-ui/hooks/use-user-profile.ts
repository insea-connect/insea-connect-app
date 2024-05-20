import { USER_INFO_ENDPOINT } from "@/lib/constants";
import { useQuery } from "@tanstack/react-query";
import axios from "axios";
import { useSession } from "next-auth/react";

const useUserProfile = () => {
  const { data } = useSession();

  return useQuery({
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
};

export default useUserProfile;
