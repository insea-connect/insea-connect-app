import { ScrollArea } from "@/components/ui/scroll-area";
import { AnimatePresence } from "framer-motion";

const AssistantChatMessages = () => {
  return (
    <ScrollArea className="w-full flex-1">
      <AnimatePresence></AnimatePresence>
    </ScrollArea>
  );
};

export default AssistantChatMessages;
