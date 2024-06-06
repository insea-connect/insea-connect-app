import React, { useCallback, useState } from "react";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "../ui/dialog";
import { useModal } from "@/hooks/use-modal-store";
import { Button } from "../ui/button";
import { File, X } from "lucide-react";
import { formatFileSize } from "@/lib/utils";
import axios from "axios";
import { useSession } from "next-auth/react";
import { UPLOAD_FILE_ENDPOINT } from "@/lib/constants";
import { useQueryClient } from "@tanstack/react-query";
import { useToast } from "../ui/use-toast";

const NewFileModal = () => {
  const { isOpen, onClose, type, data } = useModal();
  const [file, setFile] = useState<File | null>(null);
  const { data: session } = useSession();
  const queryClient = useQueryClient();
  const { toast } = useToast();

  const isModalOpen = isOpen && type === "new-file";
  const folderId = data?.folderId;
  const degreePathId = data?.degreePathId;
  const access_token = session?.tokens.access_token;

  const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event.target.files) {
      setFile(event.target.files[0]);
    }
  };

  const handleSubmit = async () => {
    if (!file || !file.name) return;

    const formData = new FormData();
    formData.append("file", file);

    try {
      const url = folderId
        ? UPLOAD_FILE_ENDPOINT(degreePathId, folderId)
        : UPLOAD_FILE_ENDPOINT(degreePathId);
      await axios.post(url, formData, {
        headers: {
          Authorization: `Bearer ${access_token}`,
          "Content-Type": "multipart/form-data",
        },
      });

      toast({
        title: "File uploaded",
        description: `${file.name} has been uploaded successfully`,
      });
      setFile(null);
      queryClient.invalidateQueries({
        queryKey: ["drive-items"],
      });
      onClose();
    } catch (error) {
      toast({
        title: "Error",
        description: "An error occurred while uploading the file",
        variant: "destructive",
      });
    }
  };

  const handleClose = () => {
    onClose();
  };
  return (
    <Dialog open={isModalOpen} onOpenChange={handleClose}>
      <DialogContent className="p-0 overflow-hidden">
        <DialogHeader className="pt-8 px-6">
          <DialogTitle className="text-2xl text-center font-bold">
            Create a new file
          </DialogTitle>
          <DialogDescription>
            Drag and drop files here or click the button below to upload files
          </DialogDescription>
        </DialogHeader>

        <div className="px-6 flex items-center justify-center w-full">
          <label
            htmlFor="dropzone-file"
            className="flex flex-col items-center justify-center w-full h-64 border-2 border-gray-300 border-dashed rounded-lg cursor-pointer bg-gray-50 dark:hover:bg-bray-800 dark:bg-gray-700 hover:bg-gray-100 dark:border-gray-600 dark:hover:border-gray-500 dark:hover:bg-gray-600"
          >
            <div className="flex flex-col items-center justify-center pt-5 pb-6">
              <svg
                className="w-8 h-8 mb-4 text-gray-500 dark:text-gray-400"
                aria-hidden="true"
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 20 16"
              >
                <path
                  stroke="currentColor"
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth="2"
                  d="M13 13h3a3 3 0 0 0 0-6h-.025A5.56 5.56 0 0 0 16 6.5 5.5 5.5 0 0 0 5.207 5.021C5.137 5.017 5.071 5 5 5a4 4 0 0 0 0 8h2.167M10 15V6m0 0L8 8m2-2 2 2"
                />
              </svg>
              <p className="mb-2 text-sm text-gray-500 dark:text-gray-400">
                <span className="font-semibold">Click to upload</span> or drag
                and drop
              </p>
              <p className="text-xs text-gray-500 dark:text-gray-400">
                SVG, PNG, JPG or GIF (MAX. 800x400px)
              </p>
            </div>
            <input
              id="dropzone-file"
              type="file"
              className="hidden"
              onChange={handleFileChange}
            />
          </label>
        </div>
        {file && (
          <div className="px-6 py-4 flex items-center justify-between">
            <div className="flex items-center gap-2">
              <File className="size-10" />
              <div className="flex flex-col">
                <span className="text-sm">{file.name}</span>
                <span className="text-sm text-gray-500">
                  {formatFileSize(file.size)}
                </span>
              </div>
            </div>
            <X
              className="size-6 cursor-pointer hover:text-red-700 text-red-500"
              onClick={() => {
                setFile(null);
              }}
            />
          </div>
        )}
        <DialogFooter className="px-6 py-4">
          <Button
            type="submit"
            disabled={!file}
            className="w-full"
            onClick={handleSubmit}
          >
            Upload file
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};

export default NewFileModal;
