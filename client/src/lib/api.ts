const url = "http://localhost:8080/api/auth";

export const fetchAuthData = async (accessToken: string) => {
  const response = await fetch(`${url}/me`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${accessToken}`,
    },
  });

  if (response.status === 401 || response.status === 403) {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
    throw new Error("인증 정보가 없습니다.");
  }

  const data = await response.json();
  return data;
};

export const loginUser = async (email: string, password: string) => {
  const response = await fetch(`${url}/login`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ email, password }),
  });

  if (!response.ok) {
    throw new Error("로그인 실패...");
  }

  const data = await response.json();

  return {
    accessToken: data.accessToken,
    refreshToken: data.refreshToken,
  };
};

export const refreshAccessToken = async (refreshToken: string) => {
  const response = await fetch(`${url}/refresh`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ refreshToken }),
    credentials: "include",
  });

  if (!response.ok) throw new Error("토큰 갱신 실패");
  const data = await response.json();
  return data.accessToken;
};
