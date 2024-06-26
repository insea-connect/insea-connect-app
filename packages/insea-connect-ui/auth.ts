import NextAuth from "next-auth";
import Credentials from "next-auth/providers/credentials";
import axios, { AxiosError } from "axios";

const AUTH_SERVER_URL = process.env.NEXT_PUBLIC_AUTH_SERVER_URL;

export const { handlers, signIn, signOut, auth } = NextAuth({
  providers: [
    Credentials({
      credentials: {
        username: {},
        password: {},
      },

      authorize: async (credentials) => {
        if (!credentials.username || !credentials.password) return null;
        const { username, password } = credentials;
        let result = null;
        try {
          result = await axios.post(`${AUTH_SERVER_URL}/api/login`, {
            username,
            password,
          });
        } catch (error) {
          if (error instanceof AxiosError) {
            const { response } = error;
            if (response?.status === 401) {
              throw new Error("Invalid username or password");
            }
          }

          throw new Error("An error occurred while trying to sign in");
        }

        const user = result.data;

        return user;
      },
    }),
  ],

  callbacks: {
    async jwt({ token, user }) {
      if (user) {
        return {
          ...token,
          ...user,
        };
      }

      return token;
    },
    async session({ session, token }) {
      session.tokens = {
        access_token: token.access_token,
        refresh_token: token.refresh_token,
        access_token_duration: token.access_token_duration,
        access_token_expiration: token.access_token_expiration,
        refresh_token_duration: token.access_token_duration,
        refresh_token_expiration: token.access_token_expiration,
      };

      session.thread_id = token.thread_id;
      session.user_profile = token.user;

      return session;
    },
  },
  pages: {
    signIn: "/sign-in",
  },
});
