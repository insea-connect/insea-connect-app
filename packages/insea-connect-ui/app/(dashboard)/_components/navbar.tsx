"use client";
import { Bot, Folder, ListTodo, MessageSquareText, Moon } from "lucide-react";

import Image from "next/image";
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from "@/components/ui/tooltip";
import dynamic from "next/dynamic";
const LowerNav = dynamic(() => import("./lower-nav"), { ssr: false });
const NavbarItem = dynamic(() => import("./navbar-item"), { ssr: false });
const Navbar = () => {
  return (
    <TooltipProvider>
      <aside className="inset-y w-auto shadow-sm fixed  left-0 z-20 flex h-full flex-col border-r xl:w-64 transition ease-out duration-300">
        <a
          href={"/"}
          className="flex items-center justify-center xl:justify-start xl:gap-3 py-2 px-4 xl:px-6"
        >
          <Image
            src="/logo.svg"
            width={25}
            height={32}
            alt="Insea Connect Logo"
          />

          <span className="xl:leading-[1rem] hidden xl:inline text-xl font-semibold">
            <span className="font-medium leading-[1.1rem] text-lg">INSEA</span>{" "}
            <br />
            <span className="text-primary font-bold">Connect.</span>
          </span>
        </a>

        <nav className="grid gap-1 p-2">
          <NavbarItem href="/assistant" label="Assistant" icon={Bot} />

          <NavbarItem href="/chat" label="Chat" icon={MessageSquareText} />
          <NavbarItem href="/drive" label="Drive" icon={Folder} />
          <NavbarItem href="/assignments" label="Assignments" icon={ListTodo} />
        </nav>
        <LowerNav />
      </aside>
    </TooltipProvider>
  );
};

export default Navbar;
