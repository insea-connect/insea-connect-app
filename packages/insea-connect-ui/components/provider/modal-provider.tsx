"use client";

import { useEffect, useState } from "react";
import NewGroupModal from "../modal/new-group-modal";
import NewFolderModal from "../modal/new-folder-modal";
import NewConversationModal from "../modal/new-conversation-modal";
import GroupSettingsModal from "../modal/group-settings-modal";
import NewMemberModal from "../modal/new-member-modal";

export const ModalProvider = () => {
  const [isMounted, setIsMounted] = useState(false);

  useEffect(() => {
    setIsMounted(true);
  }, []);

  if (!isMounted) return null;

  return (
    <>
      <NewConversationModal />
      <NewFolderModal />
      <NewGroupModal />
      <GroupSettingsModal />
      <NewMemberModal />
    </>
  );
};
