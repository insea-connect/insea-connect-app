import { Select, SelectContent, SelectTrigger } from "@/components/ui/select";
import DegreePathCombobox from "../../_components/degree-path-combobox";
import YearCombobox from "../../_components/year-combobox";
import { Button } from "@/components/ui/button";
import { Plus } from "lucide-react";

const DrivePage = () => {
  return (
    <div className="w-full">
      <header className="h-24 w-full border-b py-2 px-4 flex flex-col gap-2">
        <h2 className="text-xl font-semibold">Drive Storage</h2>
        <div className="items-center flex justify-between">
          <div className="flex gap-2 items-center">
            <DegreePathCombobox />
            <YearCombobox />
          </div>
          <Button className="flex items-center">
            <Plus className="w-4 h-4 mr-2" />
            <span>Upload file</span>
          </Button>
        </div>
      </header>
    </div>
  );
};

export default DrivePage;
