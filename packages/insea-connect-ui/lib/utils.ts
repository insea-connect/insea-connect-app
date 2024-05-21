import { type ClassValue, clsx } from "clsx";
import { twMerge } from "tailwind-merge";
import TimeAgo from "javascript-time-ago";
import en from "javascript-time-ago/locale/en";
TimeAgo.addDefaultLocale(en);

export function formatToTimeAgo(date: string) {
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

/**
 * Extract chat id from path
 * IMPORTANT: We are assuming that the conversation id and group id can never be the same
 * @param path - string
 * @returns string
 */
export const extractSelectedChatId = (path: string) => {
  const splittedPath = path.split("/");
  if (splittedPath.length < 3) return null;
  const chatId = path.split("/")[2];

  return chatId.split("-")[1];
};
