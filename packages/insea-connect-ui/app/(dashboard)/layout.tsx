import { ReactNode } from "react";
import Navbar from "./_components/navbar";
import { auth } from "@/auth";
import { SocketProvider } from "@/components/provider/socket-provider";
import { ModalProvider } from "@/components/provider/modal-provider";

const DashboardLayout = async ({ children }: { children: ReactNode }) => {
  return (
    <SocketProvider>
      <div className="flex min-h-dvh w-full pl-[57px] xl:pl-64">
        <Navbar />
        {children}
      </div>
      <ModalProvider />
    </SocketProvider>
  );
};

export default DashboardLayout;
