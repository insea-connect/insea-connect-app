import { useQueryClient } from "@tanstack/react-query";
import { useSession } from "next-auth/react";
import { useToast } from "../ui/use-toast";
import { useModal } from "@/hooks/use-modal-store";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "../ui/dialog";
import { Button } from "../ui/button";
import axios from "axios";
import { DELETE_DRIVE_ITEM_ENDPOINT } from "@/lib/constants";

const DeleteDriveItemModal = () => {
  const { data: session } = useSession();
  const queryClient = useQueryClient();
  const { toast } = useToast();
  const { isOpen, onClose, type, data } = useModal();

  const isModalOpen = isOpen && type === "delete-drive-item";
  const itemId = data?.itemId;
  const access_token = session?.tokens.access_token;

  const handleClose = () => {
    onClose();
  };

  const handleDelete = async () => {
    try {
      await axios.delete(`${DELETE_DRIVE_ITEM_ENDPOINT(itemId)}`, {
        headers: {
          Authorization: `Bearer ${access_token}`,
        },
      });

      toast({
        title: "Item deleted",
        description: "The item has been deleted successfully",
      });
      queryClient.invalidateQueries({
        queryKey: ["drive-items"],
      });
      onClose();
    } catch (error) {
      toast({
        title: "Error",
        description: "An error occurred while deleting the item",
        variant: "destructive",
      });
    }
  };

  return (
    <Dialog open={isModalOpen} onOpenChange={handleClose}>
      <DialogContent className="p-4 overflow-hidden">
        <DialogHeader>
          <DialogTitle>Delete item</DialogTitle>
          <DialogDescription>
            Are you sure you want to delete this item?
          </DialogDescription>
        </DialogHeader>

        <DialogFooter>
          <Button variant="secondary" onClick={handleClose}>
            Cancel
          </Button>
          <Button variant="destructive" onClick={handleDelete}>
            Delete
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};

export default DeleteDriveItemModal;
