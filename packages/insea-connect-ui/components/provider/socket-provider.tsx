"use client";

import { BACKEND_BASE_URL } from "@/lib/constants";
import { createContext, useContext, useEffect, useState } from "react";
import { Client } from "@stomp/stompjs";
import { useSession } from "next-auth/react";

type SocketContextType = {
  socket: any | null;
  isConnected: boolean;
};

const socketContext = createContext<SocketContextType>({
  socket: null,
  isConnected: false,
});

export const useSocket = () => {
  return useContext(socketContext);
};

export const SocketProvider = ({ children }: { children: React.ReactNode }) => {
  const { data } = useSession();

  const access_token = data?.tokens?.access_token;

  const [socket, setSocket] = useState<any | null>(null);
  const [isConnected, setIsConnected] = useState(false);

  useEffect(() => {
    console.log(BACKEND_BASE_URL);
    if (access_token) {
      const stompClient = new Client({
        debug: (str) => console.log(str),
        reconnectDelay: 5000,
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,
        brokerURL: `ws://localhost:8081/ws`,
        connectHeaders: {
          Authorization: `Bearer ${access_token}`,
        },
      });

      stompClient.onConnect = () => {
        console.log("Connected to the server");
        setIsConnected(true);
      };

      stompClient.onDisconnect = () => {
        setIsConnected(false);
      };

      stompClient.activate();
      setSocket(stompClient);

      return () => {
        if (stompClient) {
          stompClient.deactivate();
        }

        setSocket(null);
        setIsConnected(false);
      };
    }
  }, [access_token]);

  return (
    <socketContext.Provider value={{ socket, isConnected }}>
      {children}
    </socketContext.Provider>
  );
};
