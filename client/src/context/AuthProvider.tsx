"use client";

import { AuthenticatedUser } from "@/lib/api";
import { useAuthStore } from "@/store/store";
import { usePathname } from "next/navigation";
import React, { useEffect, useState } from "react";

export const accessTokenIsExpired = (token: string) => {
  if (!token) return true;

  const payload = JSON.parse(atob(token.split('.')[1]));
  const expriy = payload.exp * 1000;
  return Date.now() > expriy;
}



const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const path = usePathname();
  const setTokens = useAuthStore((state) => state.setTokens);
  const clearToken = useAuthStore((state) => state.clearToken);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const initAuth = async () => {
      console.log("init 실행");
      try {
        const tokens = await AuthenticatedUser();
        if (tokens) {
          setTokens(tokens.accessToken, tokens.refreshToken)
        } else {
          clearToken();
        }
      } catch (error) {
        console.error(error);
        clearToken();
      } finally {
        setLoading(false);
      }
    }

    initAuth();
  }, [setTokens, clearToken, path])

  if (loading) return <p>Loading...</p>

  return (
    <>{children}</>
  )
}

export default AuthProvider