import NextAuth from "next-auth";
import Credentials from "next-auth/providers/credentials";
import axios from "axios";

const AUTH_SERVER_URL = process.env.NEXT_PUBLIC_AUTH_SERVER_URL;

export const { handlers, signIn, signOut, auth } = NextAuth({
  providers: [
    Credentials({
      credentials: {
        email: {},
        password: {},
      },

      authorize: async (credentials) => {
        if (!credentials.email || !credentials.password) return null;

        const { email, password } = credentials;

        const result = await axios.post(`${AUTH_SERVER_URL}/auth/login`, {
          email,
          password,
        });

        if (result.status === 401) return null;

        const user = result.data;

        return user;
      },
    }),
  ],
});
