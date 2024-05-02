import { Bot, Folder, MessageSquareText, Moon } from "lucide-react";

import { Button } from "@/components/ui/button";

import Image from "next/image";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from "@/components/ui/tooltip";

const Navbar = () => {
  return (
    <TooltipProvider>
      <aside className="inset-y shadow-sm fixed  left-0 z-20 flex h-full flex-col border-r">
        <a href={"/"} className="flex items-center justify-center p-2">
          <Image
            src="/logo.svg"
            width={25}
            height={32}
            alt="Insea Connect Logo"
          />
        </a>

        <nav className="grid gap-1 p-2">
          <Tooltip>
            <TooltipTrigger asChild>
              <Button
                variant="ghost"
                size="icon"
                className="rounded-lg"
                aria-label="Assistant"
              >
                <Bot className="size-5" />
              </Button>
            </TooltipTrigger>
            <TooltipContent side="right" sideOffset={5}>
              Assistant
            </TooltipContent>
          </Tooltip>
          <Tooltip>
            <TooltipTrigger asChild>
              <Button
                variant="ghost"
                size="icon"
                className="rounded-lg"
                aria-label="chat"
              >
                <MessageSquareText className="size-5" />
              </Button>
            </TooltipTrigger>
            <TooltipContent side="right" sideOffset={5}>
              Chat
            </TooltipContent>
          </Tooltip>
          <Tooltip>
            <TooltipTrigger asChild>
              <Button
                variant="ghost"
                size="icon"
                className="rounded-lg"
                aria-label="drive"
              >
                <Folder className="size-5" />
              </Button>
            </TooltipTrigger>
            <TooltipContent side="right" sideOffset={5}>
              Drive
            </TooltipContent>
          </Tooltip>
        </nav>
        <nav className="mt-auto flex flex-col items-center gap-2  p-2">
          <Tooltip>
            <TooltipTrigger asChild>
              <Button
                variant="ghost"
                size="icon"
                className="mt-auto rounded-lg"
                aria-label="Theme"
              >
                <Moon className="size-5" />
              </Button>
            </TooltipTrigger>
            <TooltipContent side="right" sideOffset={5}>
              Theme
            </TooltipContent>
          </Tooltip>
          <Avatar className="h-9 w-9">
            <AvatarImage src="/avatar.jpg" alt="John Doe" />
            <AvatarFallback>JD</AvatarFallback>
          </Avatar>
        </nav>
      </aside>
    </TooltipProvider>
  );
};

export default Navbar;
