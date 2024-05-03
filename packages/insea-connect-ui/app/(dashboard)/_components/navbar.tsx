"use client";
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
import { usePathname } from "next/navigation";
import NavbarItem from "./navbar-item";

const Navbar = () => {
  const pathname = usePathname();
  console.log(pathname);
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
          <NavbarItem href="/assistant" label="Assistant" icon={Bot} />

          <NavbarItem href="/chat" label="Chat" icon={MessageSquareText} />

          <NavbarItem href="/drive" label="Drive" icon={Folder} />
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
