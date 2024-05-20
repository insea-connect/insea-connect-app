import { type ClassValue, clsx } from "clsx";
import { twMerge } from "tailwind-merge";
import TimeAgo from "javascript-time-ago";
import en from "javascript-time-ago/locale/en";
TimeAgo.addDefaultLocale(en);

export function formatToTimeAgo(date: string) {
  console.log("date", date);
  console.log("new Date(date)", new Date(date).toDateString());
  return new TimeAgo("en-US").format(new Date(date));
}

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

export function capitalize(string: string) {
  return string.charAt(0).toUpperCase() + string.slice(1);
}

export function getInitials(fullName: string) {
  if (!fullName) return "";

  const nameParts = fullName.trim().split(" ");

  const initials = nameParts.map((part) => part.charAt(0).toUpperCase());

  return initials.join("");
}

export const parseJwt = (token: string) => {
  try {
    return JSON.parse(atob(token.split(".")[1]));
  } catch (e) {
    return null;
  }
};

// TODO: Add this logic to AuthJS to validate token
export const getIsTokenValid = (token: string) => {
  if (!token) return false;

  const jwtExpireTimestamp = parseJwt(token).exp;

  const jwtExpireDateTime = new Date(jwtExpireTimestamp * 1000);

  if (jwtExpireDateTime < new Date()) {
    console.log("API token expired");
    return false;
  }

  return true;
};
