const url = "http://localhost:8080/api/auth";

export const AuthenticatedUser = async () => {
  try {
    const response = await fetch(`${url}/check`, {
      method: "GET",
      credentials: "include",
    });

    const data = await response.json();

    if (data.message === "Not Authenticated") {
      return null;
    }

    return data;
  } catch (error) {
    console.error("Authentication Check Failed", error);
    throw error;
  }
};

export const fetchAuthData = async () => {
  const response = await fetch(`${url}/me`, {
    method: "GET",
    credentials: "include",
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

export const logoutUser = async () => {
  const response = await fetch(`${url}/logout`, {
    method: "POST",
    headers: {
      'Content-Type': 'application/json',
    },
    credentials: "include",
  })

  if (!response.ok) {
    console.error("Failed to Logout");
  }
}

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
