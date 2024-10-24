"use client";

import { useAuthQuery } from "@/hooks/useAuthQuery";
import { usePathname } from "next/navigation";
import React, { createContext, useContext, useEffect, useState } from "react";

interface AuthContextType {
  isAuthenticated: boolean;
  loading: boolean;
  authInfo: {
    email?: string,
    message?: string,
  }
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const pathname = usePathname();
  const {data: authInfo, error, isLoading, refetch} = useAuthQuery();
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  useEffect(() => {
    if(!isLoading && !error) {
      if(authInfo != null) {
        setIsAuthenticated(true);
      }
    }
  }, [isLoading, authInfo, error])

  useEffect(() => {
    // 경로 변경 시 refetch 실행
    refetch();
  }, [pathname, refetch]);
  
  return (
    <AuthContext.Provider value={{ authInfo, isAuthenticated, loading: isLoading}}>
      <>{children}</>
    </AuthContext.Provider>
  )
}

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};