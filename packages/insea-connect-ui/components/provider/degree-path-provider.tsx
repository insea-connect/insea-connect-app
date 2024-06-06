"use client";
import { createContext, useContext, useState } from "react";

const DegreePathContext = createContext<{
  degreePath: string;
  setDegreePath: (degreePath: string) => void;
}>({
  degreePath: "",
  setDegreePath: () => {},
});

export const useDegreePathContext = () => useContext(DegreePathContext);

export const DegreePathProvider = ({
  children,
}: {
  children: React.ReactNode;
}) => {
  const [degreePath, setDegreePath] = useState<string>("");

  return (
    <DegreePathContext.Provider value={{ degreePath, setDegreePath }}>
      {children}
    </DegreePathContext.Provider>
  );
};
