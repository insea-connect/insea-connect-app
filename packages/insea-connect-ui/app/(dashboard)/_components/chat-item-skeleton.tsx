import { Skeleton } from "@/components/ui/skeleton";

const ChatItemSkeleton = () => {
  return (
    <div className="flex py-3 px-4 items-center space-x-4">
      <Skeleton className="min-h-12 min-w-12 rounded-full bg-gray-100 dark:bg-gray-100/10" />
      <div className="space-y-2 w-full">
        <Skeleton className="h-4 w-full bg-gray-100 dark:bg-gray-100/10" />
        <Skeleton className="h-4 w-4/5 bg-gray-100 dark:bg-gray-100/10" />
      </div>
    </div>
  );
};

export default ChatItemSkeleton;
