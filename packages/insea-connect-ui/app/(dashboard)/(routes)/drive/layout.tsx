"use client";

import { Select, SelectContent, SelectTrigger } from "@/components/ui/select";
import DegreePathCombobox from "../../_components/degree-path-combobox";
import YearCombobox from "../../_components/year-combobox";
import { Button } from "@/components/ui/button";
import { Plus, Search } from "lucide-react";
import { Tabs, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Input } from "@/components/ui/input";
import DriveItemList from "./_components/drive-item-list";
import NewDriveItemButton from "@/components/new-drive-item-button";
import { createContext, useContext, useEffect, useState } from "react";
import axios from "axios";
import { USER_INFO_ENDPOINT } from "@/lib/constants";
import { useQuery } from "@tanstack/react-query";
import { useSession } from "next-auth/react";
import { usePathname } from "next/navigation";

const DegreePathContext = createContext<{
  degreePath: string;
  setDegreePath: (degreePath: string) => void;
}>({
  degreePath: "",
  setDegreePath: () => {},
});

export const useDegreePathContext = () => useContext(DegreePathContext);

const DriveLayout = ({ children }: { children: React.ReactNode }) => {
  const [degreePath, setDegreePath] = useState<string>("");
  const { data: auth } = useSession();
  const pathname = usePathname();

  const folderId = pathname.includes("/folder/")
    ? pathname.split("/").pop()
    : undefined;

  const { data: userProfile } = useQuery({
    queryKey: ["user-profile"],
    queryFn: async () => {
      const { data } = await axios.get(USER_INFO_ENDPOINT, {
        headers: {
          Authorization: `Bearer ${auth?.tokens.access_token}`,
        },
      });

      return data;
    },
    refetchInterval: false,
    refetchOnWindowFocus: false,
  });

  useEffect(() => {
    if (userProfile && userProfile.degreePath) {
      setDegreePath(`${userProfile?.degreePath?.id}`);
    }
  }, [userProfile]);

  return (
    <div className="w-full">
      <header className="w-full py-4 px-4 flex-col lg:flex-row flex md:justify-between gap-2">
        <h2 className="text-2xl font-semibold">Drive Storage</h2>
        <div className="flex gap-2 flex-col sm:flex-row">
          {/* <YearCombobox /> */}
          <DegreePathCombobox value={degreePath} setValue={setDegreePath} />
        </div>
      </header>
      <main className="px-4 flex flex-col gap-3 pt-8 py-0">
        <div className="flex items-center justify-between">
          <h3 className="text-xl font-semibold">All files</h3>
          <NewDriveItemButton degreePathId={degreePath} folderId={folderId} />
        </div>
        <div className="flex flex-col gap-2 md:flex-row md:justify-between items-center">
          <Tabs defaultValue="view-all">
            <TabsList>
              <TabsTrigger value="view-all">View all</TabsTrigger>
              <TabsTrigger value="folders">Folders</TabsTrigger>
              <TabsTrigger value="files">Files</TabsTrigger>
            </TabsList>
          </Tabs>
          <div className="relative lg:w-96 max-w-96">
            <Search className="absolute left-2.5 top-2.5 h-4 w-4 text-muted-foreground" />
            <Input
              type="search"
              placeholder="Search..."
              className="w-full bg-background pl-8 "
            />
          </div>
        </div>

        <DegreePathContext.Provider value={{ degreePath, setDegreePath }}>
          {children}
        </DegreePathContext.Provider>
      </main>
    </div>
  );
};

export default DriveLayout;
