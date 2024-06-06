"use client";
import { buttonVariants } from "@/components/ui/button";
import React from "react";
import {
  Folder,
  FileText,
  FileSpreadsheet,
  EllipsisVertical,
  File,
  FilePieChart,
  Info,
  Download,
  Pen,
  Delete,
} from "lucide-react";
import { cn } from "@/lib/utils";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { useRouter } from "next/navigation";

type fileType =
  | "folder"
  | "text"
  | "spreadsheet"
  | "file"
  | "pdf"
  | "doc"
  | "ppt"
  | "other";

interface DriveItemProps {
  fileType: fileType;
  fileName: string;
  folderId?: string;
  link?: string;
}

const DriveItem = ({ fileType, fileName, folderId, link }: DriveItemProps) => {
  const router = useRouter();
  const getIcon = (type: fileType) => {
    switch (type) {
      case "folder":
        return <Folder className="size-6 mr-2" />;
      case "text":
        return <FileText className={`size-6 mr-2`} />;
      case "pdf":
        return <FileText className={`size-6 mr-2 text-red-600`} />;
      case "ppt":
        return <FilePieChart className={`size-6 mr-2 text-yellow-600`} />;
      case "doc":
        return <FileText className={`size-6 mr-2 text-blue-600`} />;
      case "spreadsheet":
        return <FileSpreadsheet className={`size-6 mr-2 text-green-600`} />;
      default:
        return <File className="size-6 mr-2" />;
    }
  };

  return (
    <div
      onClick={() => {
        if (fileType !== "folder") return;
        router.push(`/drive/folder/${folderId}`);
      }}
      className={cn(
        buttonVariants({ variant: "outline" }),
        "flex cursor-pointer items-center justify-between h-12"
      )}
    >
      <span className="flex items-center w-full truncate">
        {getIcon(fileType)}
        {fileType === "folder" ? (
          <span className="text-base">{fileName}</span>
        ) : (
          <a href="" target="_blank" className="text-base truncate">
            {fileName}
          </a>
        )}
      </span>
      <DropdownMenu>
        <DropdownMenuTrigger asChild>
          <EllipsisVertical className="h-4 w-4 hover:text-primary" />
        </DropdownMenuTrigger>
        <DropdownMenuContent align="end">
          <DropdownMenuLabel>Actions</DropdownMenuLabel>
          <DropdownMenuSeparator />
          <DropdownMenuItem>
            <Pen className="h-4 w-4 mr-2" />
            Rename
          </DropdownMenuItem>
          {fileType !== "folder" && (
            <DropdownMenuItem>
              <Download className="h-4 w-4 mr-2" />
              <a href={link} target="_blank">
                open
              </a>
            </DropdownMenuItem>
          )}
          <DropdownMenuItem>
            <Info className="h-4 w-4 mr-2" />
            Details
          </DropdownMenuItem>
          <DropdownMenuSeparator />
          <DropdownMenuItem className="text-red-500 group">
            <Delete className="h-4 w-4 mr-2 group-hover:text-red-700" />
            <span className="group-hover:text-red-700">Delete</span>
          </DropdownMenuItem>
        </DropdownMenuContent>
      </DropdownMenu>
    </div>
  );
};

export default DriveItem;
