import { create } from "zustand";

interface AuthState {
  accessToken: string | null;
  refreshToken: string | null;
  setTokens: (accessToken: string, refreshToken: string) => void;
  clearToken: () => void;
}

export const useAuthStore = create<AuthState>((set) => ({
  accessToken: null,
  refreshToken: null,
  setTokens: (accessToken, refreshToken) => {
    if (typeof accessToken === "string" && typeof refreshToken === "string") {
      set({ accessToken, refreshToken });
    } else {
      console.error("유효하지 않은 토큰 형식 입니다.");
    }
  },
  clearToken: () => set({ accessToken: null, refreshToken: null }),
}));
