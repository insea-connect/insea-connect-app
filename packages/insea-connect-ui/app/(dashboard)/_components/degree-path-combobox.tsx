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
import { USER_INFO_ENDPOINT } from "@/lib/constants";
import { cn } from "@/lib/utils";
import { useQuery } from "@tanstack/react-query";
import axios from "axios";
import { Check, ChevronsUpDown } from "lucide-react";
import { useSession } from "next-auth/react";
import { useEffect, useState } from "react";

const theDegreePaths = [
  {
    id: 1,
    cycle: "ing",
    major: "DSE",
    pathYear: 1,
  },
  {
    id: 2,
    cycle: "ing",
    major: "DS",
    pathYear: 1,
  },
  {
    id: 3,
    cycle: "ing",
    major: "RO",
    pathYear: 1,
  },
  {
    id: 4,
    cycle: "ing",
    major: "AF",
    pathYear: 1,
  },
  {
    id: 5,
    cycle: "ing",
    major: "SE",
    pathYear: 1,
  },
  {
    id: 6,
    cycle: "ing",
    major: "SD",
    pathYear: 1,
  },
  {
    id: 7,
    cycle: "ing",
    major: "DSE",
    pathYear: 2,
  },
  {
    id: 8,
    cycle: "ing",
    major: "DS",
    pathYear: 2,
  },
  {
    id: 9,
    cycle: "ing",
    major: "RO",
    pathYear: 2,
  },
  {
    id: 10,
    cycle: "ing",
    major: "AF",
    pathYear: 2,
  },
  {
    id: 11,
    cycle: "ing",
    major: "SE",
    pathYear: 2,
  },
  {
    id: 12,
    cycle: "ing",
    major: "SD",
    pathYear: 2,
  },
  {
    id: 13,
    cycle: "ing",
    major: "DSE",
    pathYear: 3,
  },
  {
    id: 14,
    cycle: "ing",
    major: "DS",
    pathYear: 3,
  },
  {
    id: 15,
    cycle: "ing",
    major: "RO",
    pathYear: 3,
  },
  {
    id: 16,
    cycle: "ing",
    major: "AF",
    pathYear: 3,
  },
  {
    id: 17,
    cycle: "ing",
    major: "SE",
    pathYear: 3,
  },
  {
    id: 18,
    cycle: "ing",
    major: "SD",
    pathYear: 3,
  },
];

const degreePaths = [
  {
    id: "1",
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

interface DegreePathComboboxProps {
  value: string;
  setValue: (value: string) => void;
}

const DegreePathCombobox = ({ value, setValue }: DegreePathComboboxProps) => {
  const [open, setOpen] = useState(false);

  const selectedDegreePath = theDegreePaths.find(
    (degreePath) => `${degreePath.id}` === value
  );
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
            ? selectedDegreePath?.pathYear + " " + selectedDegreePath?.major
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
              {theDegreePaths.map((degreePath) => (
                <CommandItem
                  key={degreePath.id}
                  value={`${degreePath.id}`}
                  onSelect={(currentValue) => {
                    setValue(currentValue === value ? "" : currentValue);
                    setOpen(false);
                  }}
                >
                  <Check
                    className={cn(
                      "mr-2 h-4 w-4",
                      value === `${degreePath.id}` ? "opacity-100" : "opacity-0"
                    )}
                  />
                  {degreePath.pathYear} {degreePath.major}
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
