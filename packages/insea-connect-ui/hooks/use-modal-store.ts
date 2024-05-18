import { create } from "zustand";

export type ModalType = "new-file" | "new-folder" | "new-group";

interface ModalStore {
  type: ModalType | null;
  data: any;
  isOpen: boolean;
  onOpen: (modalType: ModalType, data?: any) => void;
  onClose: () => void;
}

export const useModal = create<ModalStore>((set) => ({
  type: null,
  data: {},
  isOpen: false,
  onOpen: (type, data) => set({ type, data, isOpen: true }),
  onClose: () => set({ type: null, isOpen: false }),
}));
