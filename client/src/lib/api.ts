const url = "http://localhost:8080/api/auth";

export const AuthenticatedUser = async () => {
  try {
    const response = await fetch(`${url}/check`, {
      credentials: "include",
    });

    const data = await response.text();

    if (data !== "Not Authenticated") {
      const parsedData = JSON.parse(data);
      if (parsedData.accessToken && parsedData.refreshToken) {
        return {
          accessToken: parsedData.accessToken,
          refreshToken: parsedData.refreshToken,
        };
      } else {
        return null;
      }
    } else {
      return null;
    }
  } catch (error) {
    console.error("Authentication Check Failed", error);
    throw error;
  }
};

export const fetchAuthData = async (accessToken: string) => {
  const response = await fetch(`${url}/me`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${accessToken}`,
    },
  });

  if (response.status === 401 || response.status === 403) {
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
    credentials: "include",
  });

  if (!response.ok) {
    throw new Error("로그인 실패...");
  }

  return response;
};

export const joinUser = async (
  email: string,
  password: string,
  name: string
) => {
  const response = await fetch(`${url}/join`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ email, password, name }),
    credentials: "include",
  });

  if (!response.ok) {
    throw new Error("로그인 실패...");
  }

  return response;
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
