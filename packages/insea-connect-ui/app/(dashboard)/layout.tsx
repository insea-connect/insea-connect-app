import { ReactNode } from "react";
import Navbar from "./_components/navbar";

const DashboardLayout = ({ children }: { children: ReactNode }) => {
  return (
    <div className="grid h-screen w-full pl-[56px]">
      <Navbar />
      <main>{children}</main>
    </div>
  );
};

export default DashboardLayout;
