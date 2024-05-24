export const BACKEND_BASE_URL = process.env.NEXT_PUBLIC_BACKEND_BASE_URL;
const BACKEND_API_VERSION = "api/v1";
export const USER_INFO_ENDPOINT = `${BACKEND_BASE_URL}/${BACKEND_API_VERSION}/users/me`;
export const CONVERSATIONS_ENDPOINT = `${BACKEND_BASE_URL}/${BACKEND_API_VERSION}/users/me/conversations`;
export const GROUPS_ENDPOINT = `${BACKEND_BASE_URL}/${BACKEND_API_VERSION}/users/me/groups`;

export const CREATE_GROUP_ENDPOINT = `${BACKEND_BASE_URL}/${BACKEND_API_VERSION}/groups`;

export const GROUP_INFO_ENDPOINT = `${BACKEND_BASE_URL}/${BACKEND_API_VERSION}/groups`;

export const CONVERSATION_INFO_ENDPOINT = `${BACKEND_BASE_URL}/${BACKEND_API_VERSION}/conversations`;

export const LOGOUT_ENDPOINT = `${BACKEND_BASE_URL}/api/logout`;

export const ALL_USERS_ENDPOINT = `${BACKEND_BASE_URL}/${BACKEND_API_VERSION}/users`;

export const GROUP_MEMBERS = (groupId: number) =>
  `${BACKEND_BASE_URL}/${BACKEND_API_VERSION}/groups/${groupId}/members`;

export const GROUP_ASSIGN_ADMIN = (groupId: number) =>
  `${BACKEND_BASE_URL}/${BACKEND_API_VERSION}/groups/${groupId}/admins`;

export const GROUP_REMOVE_MEMBER = (groupId: number, userId: number) =>
  `${BACKEND_BASE_URL}/${BACKEND_API_VERSION}/groups/${groupId}/members/${userId}`;

// TODO: make userId a number
export const GROUP_REVOKE_ADMIN = (groupId: number, userId: string) =>
  `${BACKEND_BASE_URL}/${BACKEND_API_VERSION}/groups/${groupId}/admins/${userId}`;

export const BOT_NAME = "bot";
