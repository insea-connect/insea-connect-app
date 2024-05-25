import { ReactNode } from "react";
import Navbar from "./_components/navbar";
import { auth } from "@/auth";
import { SocketProvider } from "@/components/provider/socket-provider";
import { ModalProvider } from "@/components/provider/modal-provider";
import HeartbeatProvider from "@/components/provider/heartbeat-provider";

const DashboardLayout = async ({ children }: { children: ReactNode }) => {
  return (
    <SocketProvider>
      <HeartbeatProvider />
      <div className="flex min-h-dvh w-full pl-[57px] xl:pl-64">
        <Navbar />
        {children}
      </div>
      <ModalProvider />
    </SocketProvider>
  );
};

export default DashboardLayout;
