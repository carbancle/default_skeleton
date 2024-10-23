"use client";

import { logoutUser } from "@/lib/api";
import styles from "./page.module.css";
import { useMutation } from "@tanstack/react-query";
import { useAuth } from "@/context/AuthProvider";
import { useRouter } from "next/navigation";

export default function Home() {
  const router = useRouter();
  const { authInfo, loading, isAuthenticated } = useAuth();
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
    mutation.mutate();
    router.push("/login");
  }

  if (loading) return <p>Loading...</p>

  return (
    <div className={styles.page}>
      <main className={styles.main}>
        <p>{(authInfo && isAuthenticated) && authInfo.email}</p>
        <button onClick={logoutHandler}>로그아웃</button>
      </main>
    </div>
  );
}
