"use client";
import { HEARTBEAT_ENDPOINT } from "@/lib/constants";
import { useQuery } from "@tanstack/react-query";
import axios from "axios";
import { useSession } from "next-auth/react";

const HeartbeatProvider = () => {
  const { data: auth } = useSession();
  useQuery({
    queryKey: ["heartbeat"],
    queryFn: async () => {
      const { data } = await axios.put(
        HEARTBEAT_ENDPOINT,
        {
          status: "ONLINE",
        },
        {
          headers: {
            Authorization: `Bearer ${auth?.tokens.access_token}`,
          },
        }
      );

      return data;
    },

    refetchInterval: 1000 * 60,
  });

  return null;
};

export default HeartbeatProvider;
