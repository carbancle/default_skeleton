"use client";

import { refreshAccessToken } from "@/lib/api";
import { useAuthStore } from "@/store/store";
import React, { useEffect } from "react";

export const accessTokenIsExpired = (token: string) => {
  if (!token) return true;

  const payload = JSON.parse(atob(token.split('.')[1]));
  const expriy = payload.exp * 1000;
  return Date.now() > expriy;
}

const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const setTokens = useAuthStore((state) => state.setTokens);

  useEffect(() => {
    const initAuth = async () => {
      const localAccessToken = localStorage.getItem("accessToken");
      const localRefreshToken = localStorage.getItem("refreshToken");

      if (localAccessToken && localRefreshToken) {
        setTokens(localAccessToken, localRefreshToken);

        if (accessTokenIsExpired(localAccessToken)) {
          try {
            const newAccessToken = await refreshAccessToken(localRefreshToken);
            setTokens(newAccessToken, localRefreshToken);
            localStorage.setItem("accessToken", newAccessToken);
          } catch (error) {
            console.error("토근 갱신 실패", error);
          }
        }
      }
    }

    initAuth();
  }, [setTokens])
  return (
    <>{children}</>
  )
}

export default AuthProvider