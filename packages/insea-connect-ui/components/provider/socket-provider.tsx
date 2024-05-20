"use client";

import { BACKEND_BASE_URL } from "@/lib/constants";
import { createContext, useContext, useEffect, useState } from "react";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";
// import Stomp from "stompjs";
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

  console.log(isConnected);

  useEffect(() => {
    console.log(BACKEND_BASE_URL);
    if (access_token) {
      const stompClient = new Client({
        webSocketFactory: () => new SockJS(`${BACKEND_BASE_URL}/ws`),
        debug: (str) => console.log(str),
        connectHeaders: {
          Authorization: `Bearer ${access_token}`,
        },
        reconnectDelay: 5000,
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,
        brokerURL: `${BACKEND_BASE_URL}/ws`,
      });

      stompClient.onConnect = () => {
        setIsConnected(true);
      };

      stompClient.onDisconnect = () => {
        setIsConnected(false);
      };

      setSocket(stompClient);

      return () => {
        stompClient.deactivate();
      };
    }
  }, [access_token]);

  return (
    <socketContext.Provider value={{ socket, isConnected }}>
      {children}
    </socketContext.Provider>
  );
};
