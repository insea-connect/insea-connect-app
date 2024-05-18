"use client";
import { FilePlus, FolderPlus, Plus } from "lucide-react";
import { Button } from "./ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "./ui/dropdown-menu";
import { useModal } from "@/hooks/use-modal-store";

const NewDriveItemButton = () => {
  const { onOpen } = useModal();
  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <Button>
          <Plus className="size-4 mr-2" />
          Add
        </Button>
      </DropdownMenuTrigger>
      <DropdownMenuContent>
        <DropdownMenuItem onClick={() => onOpen("new-folder")}>
          <FolderPlus className="size-4 mr-2" />
          New Folder
        </DropdownMenuItem>
        <DropdownMenuItem>
          <FilePlus className="size-4 mr-2" />
          New File
        </DropdownMenuItem>
      </DropdownMenuContent>
    </DropdownMenu>
  );
};

export default NewDriveItemButton;
