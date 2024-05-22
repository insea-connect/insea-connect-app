import { Skeleton } from "@/components/ui/skeleton";
import React from "react";

const ChatMessagesListSkeleton = () => {
  return (
    <div className="flex-1 flex flex-col justify-end px-4 py-2">
      <div className="flex py-3 px-4 items-center space-x-4">
        <Skeleton className="min-h-12 min-w-12 rounded-full bg-gray-100 dark:bg-gray-100/10" />
        <div className="space-y-2 w-full">
          <Skeleton className="h-4 max-w-[250px] bg-gray-100 dark:bg-gray-100/10" />
          <Skeleton className="h-4 max-w-[200px] bg-gray-100 dark:bg-gray-100/10" />
        </div>
      </div>
      <div className="flex py-3 px-4 items-center space-x-4">
        <Skeleton className="min-h-12 min-w-12 rounded-full bg-gray-100 dark:bg-gray-100/10" />
        <div className="space-y-2 w-full">
          <Skeleton className="h-4 max-w-[250px] bg-gray-100 dark:bg-gray-100/10" />
          <Skeleton className="h-4 max-w-[200px] bg-gray-100 dark:bg-gray-100/10" />
        </div>
      </div>
    </div>
  );
};

export default ChatMessagesListSkeleton;
