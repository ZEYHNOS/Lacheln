// src/hooks/useAuth.js
import { useContext, createContext } from "react";

// 1) Make an AuthContext somewhere in your app (if you haven't yet)
export const AuthContext = createContext({
  currentId:   null,
  currentRole: null,
});

// 2) A hook that reads from it:
export function useAuth() {
  return useContext(AuthContext);
}