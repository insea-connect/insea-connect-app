"use client";
import { useMediaQuery } from "usehooks-ts";
import { Button } from "@/components/ui/button";
import {
  Tooltip,
  TooltipContent,
  TooltipTrigger,
} from "@/components/ui/tooltip";
import { cn } from "@/lib/utils";
import { LucideIcon } from "lucide-react";
import { usePathname, useRouter } from "next/navigation";
import { FC } from "react";

interface NavbarItemProps {
  icon: LucideIcon;
  label: string;
  href: string;
}
const NavbarItem: FC<NavbarItemProps> = ({ href, label, icon: Icon }) => {
  const pathname = usePathname();
  const router = useRouter();
  const isActive = pathname === href || pathname.startsWith(href);
  const isSmallScreen = useMediaQuery("(max-width: 1279px)");

  return (
    <Tooltip>
      <TooltipTrigger asChild>
        <Button
          variant="ghost"
          onClick={() => {
            router.push(href);
          }}
          size={isSmallScreen ? "icon" : "default"}
          className={cn(
            "rounded-Rm gap-2 xl:justify-start",
            isActive && "bg-primary/5"
          )}
          aria-label={label}
        >
          <Icon className={cn("size-5", isActive && "text-primary")} />
          <span className={cn("hidden xl:inline", isActive && "text-primary")}>
            {label}
          </span>
        </Button>
      </TooltipTrigger>
      <TooltipContent className="xl:hidden" side="right" sideOffset={5}>
        {label}
      </TooltipContent>
    </Tooltip>
  );
};

export default NavbarItem;
