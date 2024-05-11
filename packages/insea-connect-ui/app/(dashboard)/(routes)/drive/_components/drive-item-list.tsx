import { Button } from "@/components/ui/button";
import { BsFileEarmarkPdfFill, BsFillFilePdfFill } from "react-icons/bs";
import {
  EllipsisVertical,
  FileSpreadsheet,
  FileText,
  Folder,
} from "lucide-react";
import DriveItem from "./drive-item";

const DriveItemList = () => {
  return (
    <div className="py-4 grid gap-4 grid-cols-1 sm:grid-cols-2 lg:grid-cols-3  xl:grid-cols-4 2xl:grid-cols-5">
      <DriveItem fileType="folder" fileName="Folder 1" />
      <DriveItem fileType="text" fileName="File 1" />
      <DriveItem fileType="spreadsheet" fileName="File 2" />
      <DriveItem fileType="doc" fileName="File 3" />

      <DriveItem fileType="pdf" fileName="File 4" />
      <DriveItem fileType="ppt" fileName="File 5" />
      <DriveItem fileType="other" fileName="File 6" />
    </div>
  );
};

export default DriveItemList;
