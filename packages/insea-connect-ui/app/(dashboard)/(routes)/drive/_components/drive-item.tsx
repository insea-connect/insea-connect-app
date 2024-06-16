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
  FileImage,
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
import { BACKEND_BASE_URL } from "@/lib/constants";
import { useModal } from "@/hooks/use-modal-store";

type fileType =
  | "image/png"
  | "text/plain"
  | "application/pdf"
  | "application/vnd.openxmlformats-officedocument.presentationml.presentation"
  | "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
  | "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
  | "folder"
  | "text"
  | "file"
  | "other";

interface DriveItemProps {
  fileType: fileType;
  fileName: string;
  folderId?: string;
  fileUrl?: string;
}

const DriveItem = ({
  fileType,
  fileName,
  folderId,
  fileUrl,
}: DriveItemProps) => {
  const { onOpen } = useModal();
  const router = useRouter();
  const getIcon = (type: fileType) => {
    if (type === "folder") {
      return <Folder className="size-6 mr-2" />;
    } else if (type.startsWith("image")) {
      return <FileImage className="size-6 mr-2" />;
    } else if (type === "text/plain") {
      return <FileText className={`size-6 mr-2`} />;
    } else if (type === "application/pdf") {
      return <FileText className={`size-6 mr-2 text-red-600`} />;
    } else if (
      type ===
      "application/vnd.openxmlformats-officedocument.presentationml.presentation"
    ) {
      return <FilePieChart className={`size-6 mr-2 text-yellow-600`} />;
    } else if (
      type ===
      "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    ) {
      return <FileText className={`size-6 mr-2 text-blue-600`} />;
    } else if (
      type ===
      "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    ) {
      return <FileSpreadsheet className={`size-6 mr-2 text-green-600`} />;
    } else {
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
          <a
            href={`${BACKEND_BASE_URL}/${fileUrl}`}
            target="_blank"
            className="text-base truncate"
          >
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
              <a href={`${BACKEND_BASE_URL}/${fileUrl}`} target="_blank">
                open
              </a>
            </DropdownMenuItem>
          )}
          <DropdownMenuItem>
            <Info className="h-4 w-4 mr-2" />
            Details
          </DropdownMenuItem>
          <DropdownMenuSeparator />
          <DropdownMenuItem
            className="text-red-500 group"
            onClick={(event) => {
              event.stopPropagation();
              onOpen("delete-drive-item", { itemId: folderId });
            }}
          >
            <Delete className="h-4 w-4 mr-2 group-hover:text-red-700" />
            <span className="group-hover:text-red-700">Delete</span>
          </DropdownMenuItem>
        </DropdownMenuContent>
      </DropdownMenu>
    </div>
  );
};

export default DriveItem;
