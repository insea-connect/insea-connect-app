"use client";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import {
  Tooltip,
  TooltipContent,
  TooltipTrigger,
} from "@/components/ui/tooltip";
import { Moon } from "lucide-react";
import { useMediaQuery } from "usehooks-ts";
const LowerNav = () => {
  const isSmallScreen = useMediaQuery("(max-width: 1280px)");
  return (
    <nav className="mt-auto flex flex-col items-center xl:items-start gap-2  p-2">
      <Tooltip>
        <TooltipTrigger asChild>
          <Button
            variant="ghost"
            size={isSmallScreen ? "icon" : "default"}
            className="rounded-lg xl:w-full hover:text-primary xl:justify-start gap-2 xl:items-center"
            aria-label="Theme"
          >
            <Moon className="size-5" />
            <span className="hidden xl:inline">Night Mode</span>
          </Button>
        </TooltipTrigger>
        <TooltipContent side="right" sideOffset={5} className="xl:hidden">
          Theme
        </TooltipContent>
      </Tooltip>
      <div className="xl:flex xl:gap-2 xl:px-3 xl:items-center">
        <Avatar className="h-7 w-7">
          <AvatarImage src="/avatar.jpg" alt="John Doe" />
          <AvatarFallback>JD</AvatarFallback>
        </Avatar>
        <span className="hidden xl:inline text-base font-medium">John Doe</span>
      </div>
    </nav>
  );
};

export default LowerNav;
