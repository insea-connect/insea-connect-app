"use client";

import { Button } from "@/components/ui/button";
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
} from "@/components/ui/command";
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover";
import { cn } from "@/lib/utils";
import { Check, ChevronsUpDown } from "lucide-react";
import { useState } from "react";

const degreePaths = [
  {
    label: "Data & Software Engineering",
    value: "data-software-engineering",
  },

  {
    label: "Data Science",
    value: "data-science",
  },

  {
    label: "Operations Research and Decision Support",
    value: "operations-research-decision-support",
  },

  {
    label: "Statistics – Demography",
    value: "statistics-demography",
  },

  {
    label: "Actuarial Science - Finance",
    value: "actuarial-science-finance",
  },

  {
    label: "Statistics - Applied Economics",
    value: "statistics-applied-economics",
  },
];

const DegreePathCombobox = () => {
  const [open, setOpen] = useState(false);
  const [value, setValue] = useState("");
  return (
    <Popover open={open} onOpenChange={setOpen}>
      <PopoverTrigger asChild>
        <Button
          variant="outline"
          role="combobox"
          aria-expanded={open}
          className="max-w-[350px] lg:w-[350px] justify-between"
        >
          {value
            ? degreePaths.find((degreePath) => degreePath.value === value)
                ?.label
            : "Select degree path..."}
          <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
        </Button>
      </PopoverTrigger>
      <PopoverContent className="max-w-[350px] lg:w-[350px] p-0">
        <Command>
          <CommandInput placeholder="Search degree paths..." />
          <CommandEmpty>No degree path found.</CommandEmpty>
          <CommandList>
            <CommandGroup>
              {degreePaths.map((degreePath) => (
                <CommandItem
                  key={degreePath.value}
                  value={degreePath.value}
                  onSelect={(currentValue) => {
                    setValue(currentValue === value ? "" : currentValue);
                    setOpen(false);
                  }}
                >
                  <Check
                    className={cn(
                      "mr-2 h-4 w-4",
                      value === degreePath.value ? "opacity-100" : "opacity-0"
                    )}
                  />
                  {degreePath.label}
                </CommandItem>
              ))}
            </CommandGroup>
          </CommandList>
        </Command>
      </PopoverContent>
    </Popover>
  );
};

export default DegreePathCombobox;
