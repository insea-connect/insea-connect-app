"use client";

import { useEffect, useState } from "react";
import NewGroupModal from "../modal/new-group-modal";
import NewFolderModal from "../modal/new-folder-modal";

export const ModalProvider = () => {
  const [isMounted, setIsMounted] = useState(false);

  useEffect(() => {
    setIsMounted(true);
  }, []);

  if (!isMounted) return null;

  return (
    <>
      <NewFolderModal />
      <NewGroupModal />
    </>
  );
};
