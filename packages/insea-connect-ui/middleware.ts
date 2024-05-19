import { auth } from "@/auth";

export default auth((req) => {
  if (!req.auth) {
    return Response.redirect(req.nextUrl.origin + "/sign-in");
  }
});

export const config = {
  matcher: [
    "/chat/:path*",
    "/drive/:path*",
    "/assistant/:path*",
    "/assignments",
  ],
};
