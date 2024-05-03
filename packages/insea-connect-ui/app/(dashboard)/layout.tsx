import { ReactNode } from "react";
import Navbar from "./_components/navbar";

const DashboardLayout = ({ children }: { children: ReactNode }) => {
  return (
    <div className="flex h-screen w-full pl-[52px]">
      <Navbar />
      {children}
    </div>
  );
};

export default DashboardLayout;