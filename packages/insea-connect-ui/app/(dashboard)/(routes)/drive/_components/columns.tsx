"use client";
import { ColumnDef } from "@tanstack/react-table";

export const columns: ColumnDef<any>[] = [
  {
    accessorKey: "file-name",
    header: "File Name",
  },
  {
    accessorKey: "created-at",
    header: "Created At",
  },
  {
    accessorKey: "creator",
    header: "Creator",
  },
  {
    accessorKey: "size",
    header: "Size",
  },
];
