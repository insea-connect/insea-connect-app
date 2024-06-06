import { Button } from "@/components/ui/button";
import { BsFileEarmarkPdfFill, BsFillFilePdfFill } from "react-icons/bs";
import {
  EllipsisVertical,
  FileSpreadsheet,
  FileText,
  Folder,
} from "lucide-react";
import DriveItem from "./drive-item";
import { FILE_PATH } from "@/lib/constants";

interface DriveItemInFolderListProps {
  folderId?: string;
}

const DriveItemInFolderList = ({ folderId }: DriveItemInFolderListProps) => {
  return (
    <div className="py-4 grid gap-4 grid-cols-1 sm:grid-cols-2 lg:grid-cols-3  xl:grid-cols-4 2xl:grid-cols-5">
      {folderId !== "1" ? (
        <DriveItem
          fileType="pdf"
          fileName="Skalli"
          link={`${FILE_PATH}skalli.pdf`}
        />
      ) : (
        <DriveItem
          fileType="pdf"
          fileName="Chapitre 1"
          link={`${FILE_PATH}chapitre1.pdf`}
        />
      )}
    </div>
  );
};

export default DriveItemInFolderList;
