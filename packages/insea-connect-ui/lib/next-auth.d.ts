import NextAuth from "next-auth";
import { JWT } from "next-auth/jwt";
declare module "next-auth" {
  interface Session {
    tokens: {
      access_token: string;
      refresh_token: string;
    };
  }
}

declare module "next-auth/jwt" {
  interface JWT {
    access_token: string;
    expires_in: number;
    refresh_expires_in: number;
    refresh_token: string;
    token_type: string;
    "not-before-policy": number;
    session_state: string;
    scope: string;
  }
}
