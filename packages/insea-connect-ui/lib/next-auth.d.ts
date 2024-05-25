import NextAuth from "next-auth";
import { JWT } from "next-auth/jwt";
declare module "next-auth" {
  interface Session {
    user_profile: {
      id: number;
      username: string;
      email: string;
      roles: string;
    };
    tokens: {
      access_token: string;
      access_token_duration: number;
      access_token_expiration: string;
      refresh_token: string;
      refresh_token_duration: number;
      refresh_token_expiration: string;
    };
    thread_id: string;
  }
}

declare module "next-auth/jwt" {
  interface JWT {
    user: {
      id: number;
      username: string;
      email: string;
      roles: string;
    };
    access_token: string;
    access_token_duration: number;
    access_token_expiration: string;
    refresh_token: string;
    refresh_token_duration: number;
    refresh_token_expiration: string;
    thread_id: string;
  }
}
