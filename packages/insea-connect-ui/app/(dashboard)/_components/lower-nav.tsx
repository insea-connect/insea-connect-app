"use client";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import {
  Tooltip,
  TooltipContent,
  TooltipTrigger,
} from "@/components/ui/tooltip";
import { Paintbrush2 } from "lucide-react";
import { useMediaQuery } from "usehooks-ts";
import { useTheme } from "next-themes";

const LowerNav = () => {
  const isSmallScreen = useMediaQuery("(max-width: 1279px)");
  const { setTheme, theme } = useTheme();

  return (
    <nav className="mt-auto flex flex-col items-center xl:items-start gap-2  p-2">
      <Tooltip>
        <TooltipTrigger asChild>
          <Button
            variant="ghost"
            size={isSmallScreen ? "icon" : "default"}
            className="rounded-md xl:w-full hover:text-primary xl:justify-start gap-2 xl:items-center"
            aria-label="Theme"
            onClick={() => {
              setTheme(theme === "light" ? "dark" : "light");
            }}
          >
            <Paintbrush2 className="size-5" />
            <span className="hidden xl:inline">
              {theme === "light" ? "Dark Mode" : "Light Mode"}
            </span>
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
