import { ReactNode } from "react";
import Navbar from "./_components/navbar";

const DashboardLayout = ({ children }: { children: ReactNode }) => {
  return (
    <div className="flex min-h-dvh w-full pl-[57px] xl:pl-64">
      <Navbar />
      {children}
    </div>
  );
};

export default DashboardLayout;
