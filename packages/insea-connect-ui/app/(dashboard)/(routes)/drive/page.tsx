import { Select, SelectContent, SelectTrigger } from "@/components/ui/select";
import DegreePathCombobox from "../../_components/degree-path-combobox";
import YearCombobox from "../../_components/year-combobox";
import { Button } from "@/components/ui/button";
import { Plus, Search } from "lucide-react";
import { Tabs, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Input } from "@/components/ui/input";
import DriveItemList from "./_components/drive-item-list";

const DrivePage = () => {
  return (
    <div className="w-full">
      <header className="h-24 w-full py-4 px-4 flex-col lg:flex-row flex md:justify-between gap-2">
        <h2 className="text-2xl font-semibold">Drive Storage</h2>
        <div className="flex gap-2 ">
          <YearCombobox />
          <DegreePathCombobox />
        </div>
      </header>
      <main className="px-4 flex flex-col gap-3 pt-8 lg:py-0">
        <div className="flex items-center justify-between">
          <h3 className="text-xl font-semibold">All files</h3>
          <Button>
            <Plus className="size-4 mr-2" />
            Upload
          </Button>
        </div>
        <div className="flex flex-col gap-2 md:flex-row md:justify-between items-center">
          <Tabs defaultValue="view-all">
            <TabsList>
              <TabsTrigger value="view-all">View all</TabsTrigger>
              <TabsTrigger value="folders">Folders</TabsTrigger>
              <TabsTrigger value="files">Files</TabsTrigger>
            </TabsList>
          </Tabs>
          <div className="relative w-96">
            <Search className="absolute left-2.5 top-2.5 h-4 w-4 text-muted-foreground" />
            <Input
              type="search"
              placeholder="Search..."
              className="w-full bg-background pl-8 "
            />
          </div>
        </div>

        <DriveItemList />
      </main>
    </div>
  );
};

export default DrivePage;
