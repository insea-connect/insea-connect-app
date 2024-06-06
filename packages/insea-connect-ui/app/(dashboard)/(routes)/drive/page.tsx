"use client";

import axios from "axios";
import DriveItemList from "./_components/drive-item-list";
import { useDegreePathContext } from "./layout";
import { useQuery } from "@tanstack/react-query";
import { DRIVE_ITEMS_ENDPOINT } from "@/lib/constants";
import { useSession } from "next-auth/react";

const DrivePage = () => {
  const { degreePath } = useDegreePathContext();
  const { data } = useSession();

  const access_token = data?.tokens.access_token;

  const { data: driveItems, isPending } = useQuery({
    queryKey: ["drive-items", degreePath],
    queryFn: async () => {
      const { data } = await axios.get(DRIVE_ITEMS_ENDPOINT(degreePath), {
        headers: {
          Authorization: `Bearer ${access_token}`,
        },
      });
      return data;
    },
  });

  if (isPending) return <div>Loading...</div>;

  return <DriveItemList driveItems={driveItems} />;
};

export default DrivePage;
