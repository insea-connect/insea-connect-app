"use client";
import { usePathname } from "next/navigation";
import { useDegreePathContext } from "../../layout";
import { useSession } from "next-auth/react";
import { useQuery } from "@tanstack/react-query";
import axios from "axios";
import { DRIVE_ITEMS_ENDPOINT } from "@/lib/constants";
import DriveItemList from "../../_components/drive-item-list";
import DriveItemListSkeleton from "../../_components/drive-item-list-skeleton";

const DriveFolderPage = () => {
  const { degreePath } = useDegreePathContext();
  const pathname = usePathname();
  const { data } = useSession();

  const folderId = pathname.split("/").pop();
  const access_token = data?.tokens.access_token;

  const { data: driveItems, isPending } = useQuery({
    queryKey: ["drive-items", degreePath, folderId],
    queryFn: async () => {
      const { data } = await axios.get(
        DRIVE_ITEMS_ENDPOINT(degreePath, folderId),
        {
          headers: {
            Authorization: `Bearer ${access_token}`,
          },
        }
      );
      return data;
    },
  });

  if (isPending) return <DriveItemListSkeleton />;

  return <DriveItemList driveItems={driveItems} />;
};

export default DriveFolderPage;
