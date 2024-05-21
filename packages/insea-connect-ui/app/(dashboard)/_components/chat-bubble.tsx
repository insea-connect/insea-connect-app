import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { cn, getInitials } from "@/lib/utils";
import { format } from "date-fns";
import { AnimatePresence, motion } from "framer-motion";

interface ChatBubbleProps {
  message: any;
  connectedUserId?: number;
  index: number;
}

const DATE_FORM = "HH:mm";

const ChatBubble = ({ connectedUserId, index, message }: ChatBubbleProps) => {
  return (
    <motion.div
      key={index}
      layout
      initial={{ opacity: 0, scale: 1, y: 50, x: 0 }}
      animate={{ opacity: 1, scale: 1, y: 0, x: 0 }}
      exit={{ opacity: 0, scale: 1, y: 1, x: 0 }}
      transition={{
        opacity: { duration: 0.1 },
        layout: {
          type: "spring",
          bounce: 0.3,
          duration: index * 0.05 + 0.2,
        },
      }}
      style={{
        originX: 0.5,
        originY: 0.5,
      }}
      className={cn(
        "flex gap-2 p-4 whitespace-pre-wrap",
        message.senderId === connectedUserId ? "justify-end" : "justify-start"
      )}
    >
      {message.senderId !== connectedUserId && (
        <Avatar className="flex justify-center items-center">
          <AvatarFallback>{getInitials(message.senderName)}</AvatarFallback>
        </Avatar>
      )}
      <div className="flex flex-col gap-1 w-full max-w-[320px]">
        <div
          className={cn(
            `flex items-end space-x-3 rtl:space-x-reverse`,

            message.senderId === connectedUserId && "hidden"
          )}
        >
          <span
            className={"text-sm font-semibold text-gray-900 dark:text-white"}
          >
            {message.senderName}
          </span>
          <span className="text-xs font-normal text-gray-500 dark:text-gray-400">
            {format(new Date(message.timestamp), DATE_FORM)}
          </span>
        </div>
        <div
          className={cn(
            "flex flex-col leading-1.5 p-4",

            message.senderId === connectedUserId
              ? "rounded-ee-xl rounded-s-xl rounded-es-xl text-white bg-green-900"
              : "rounded-e-xl rounded-es-xl bg-secondary text-gray-900 dark:text-white"
          )}
        >
          <p className="text-sm font-normal ">{message.content}</p>
        </div>
      </div>
    </motion.div>
  );
};

export default ChatBubble;
