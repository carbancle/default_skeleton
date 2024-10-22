"use client";

import { fetchAuthData, logoutUser } from "@/lib/api";
import styles from "./page.module.css";
import { useMutation, useQuery } from "@tanstack/react-query";
import { useAuthStore } from "@/store/store";

interface Member {
  id?: number,
  email: string,
  name: string,
}

export default function Home() {
  const accessToken = useAuthStore((state) => state.accessToken);
  const clearToken = useAuthStore((state) => state.clearToken);
  const { data, error, isLoading } = useQuery<Member>({
    queryKey: ["AuthData"],
    queryFn: () => fetchAuthData(accessToken!),
    enabled: !!accessToken, // accessToken이 있을 때만 실행
  });

  const mutation = useMutation({
    mutationFn: logoutUser,
    onSuccess: () => {
      console.log("로그아웃 완료");
    },
    onError: (error) => {
      console.error("로그아웃 실패", error);
    }
  })
  const logoutHandler = () => {
    clearToken();
    mutation.mutate();
  }

  if (isLoading) return <p>Loading...</p>;
  if (error) return <p>Error Loading Data</p>;
  return (
    <div className={styles.page}>
      <main className={styles.main}>
        <p>{data?.email}</p>
        <p>{data?.name}</p>
        <button onClick={logoutHandler}>로그아웃</button>
      </main>
    </div>
  );
}
