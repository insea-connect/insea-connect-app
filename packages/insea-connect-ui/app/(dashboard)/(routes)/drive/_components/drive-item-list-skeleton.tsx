import { Skeleton } from "@/components/ui/skeleton";

const DriveItemListSkeleton = () => {
  return (
    <div className="py-4 grid gap-4 grid-cols-1 sm:grid-cols-2 lg:grd-cols-3 xl:grid-cols-4 2xl:grid-cols-5">
      {[...Array(5)].map((_, index) => (
        <DriveItemSkeleton key={index} />
      ))}
    </div>
  );
};

const DriveItemSkeleton = () => {
  return <Skeleton className="h-12" />;
};

export default DriveItemListSkeleton;
