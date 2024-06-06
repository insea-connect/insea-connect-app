import DriveItem from "./drive-item";

interface DriveItemListProps {
  driveItems: any[];
}

const DriveItemList = ({ driveItems }: DriveItemListProps) => {
  console.log(driveItems);
  return (
    <div className="py-4 grid gap-4 grid-cols-1 sm:grid-cols-2 lg:grid-cols-3  xl:grid-cols-4 2xl:grid-cols-5">
      {driveItems?.map((item) => (
        <DriveItem
          key={item.id}
          fileType={!item.mimeType ? "folder" : item.mimeType}
          fileName={item.name}
          folderId={!item.mimeType ? item.id : undefined}
        />
      ))}
    </div>
  );
};

export default DriveItemList;
