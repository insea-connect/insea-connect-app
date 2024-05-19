import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import { ThemeProvider } from "@/components/provider/theme-provider";
import { SessionProvider } from "next-auth/react";
import { ModalProvider } from "@/components/provider/modal-provider";
import { Toaster } from "@/components/ui/toaster";
import QueryProvider from "@/components/provider/query-provider";
const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "INSEA Connect",
  description: "An app for your daily INSEA needs",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className={inter.className}>
        <ThemeProvider
          attribute="class"
          defaultTheme="light"
          enableSystem={false}
          disableTransitionOnChange
        >
          <SessionProvider>
            <QueryProvider>
              <ModalProvider />
              {children}
              <Toaster />
            </QueryProvider>
          </SessionProvider>
        </ThemeProvider>
      </body>
    </html>
  );
}
