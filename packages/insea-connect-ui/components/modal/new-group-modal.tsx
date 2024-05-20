"use client";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogTitle,
  DialogHeader,
  DialogFooter,
} from "@/components/ui/dialog";
import { useForm } from "react-hook-form";
import * as z from "zod";
import { zodResolver } from "@hookform/resolvers/zod";
import axios from "axios";
import { useToast } from "@/components/ui/use-toast";

import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";

import { useRouter } from "next/navigation";
import { useModal } from "@/hooks/use-modal-store";
import { CREATE_GROUP_ENDPOINT } from "@/lib/constants";
import { useSession } from "next-auth/react";

const schema = z.object({
  name: z.string().min(1, { message: "Name is required" }),
  description: z.string(),
});

const NewGroupModal = () => {
  const { data: session } = useSession();
  const { toast } = useToast();
  const router = useRouter();
  const { isOpen, onClose, type } = useModal();
  const isModalOpen = isOpen && type === "new-group";
  const form = useForm({
    resolver: zodResolver(schema),
    defaultValues: {
      name: "",
      description: "",
    },
  });

  const isLoading = form.formState.isSubmitting;
  const onSubmit = async (values: z.infer<typeof schema>) => {
    try {
      const { data } = await axios.post(
        CREATE_GROUP_ENDPOINT,
        {
          name: values.name,
          description: values.description,
          members: [],
        },
        {
          headers: {
            Authorization: `Bearer ${session?.tokens.access_token}`,
          },
        }
      );
      form.reset();
      router.push(`/chat/group-${data.id}`);
      onClose();
    } catch (error) {
      toast({
        title: "An error occurred",
        description: "Failed to create group. Please try again later.",
        variant: "destructive",
      });
    }
  };

  const handleClose = () => {
    form.reset();
    onClose();
  };

  return (
    <Dialog open={isModalOpen} onOpenChange={handleClose}>
      <DialogContent className="p-0 overflow-hidden">
        <DialogHeader className="pt-8 px-6">
          <DialogTitle className="text-2xl text-center font-bold">
            Create a new group
          </DialogTitle>
          <DialogDescription>
            Create a new group to collaborate with your friends and colleagues.
          </DialogDescription>
        </DialogHeader>
        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-8">
            <div className="space-y-8 px-6">
              <FormField
                control={form.control}
                name="name"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="uppercase text-xs font-bold ">
                      Group name
                    </FormLabel>
                    <FormControl>
                      <Input
                        disabled={isLoading}
                        className="focus-visible:ring-0 focus-visible:ring-offset-0"
                        placeholder="Enter group name"
                        {...field}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="description"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="uppercase text-xs font-bold ">
                      Group description
                    </FormLabel>
                    <FormControl>
                      <Input
                        disabled={isLoading}
                        className="focus-visible:ring-0 focus-visible:ring-offset-0"
                        placeholder="Enter group description"
                        {...field}
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </div>
            <DialogFooter className="px-6 py-4">
              <Button disabled={isLoading}>Create</Button>
            </DialogFooter>
          </form>
        </Form>
      </DialogContent>
    </Dialog>
  );
};

export default NewGroupModal;
