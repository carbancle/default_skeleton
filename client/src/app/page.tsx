"use client";

import { fetchAuthData } from "@/lib/api";
import styles from "./page.module.css";
import { useQuery } from "@tanstack/react-query";
import { useAuthStore } from "@/store/store";

interface Member {
  id?: number,
  email: string,
  name: string,
}

export default function Home() {
  const accessToken = useAuthStore((state) => state.accessToken);
  const { data, error, isLoading } = useQuery<Member>({
    queryKey: ["AuthData"],
    queryFn: () => fetchAuthData(accessToken!),
    enabled: !!accessToken, // accessToken이 있을 때만 실행
  });

  if (isLoading) return <p>Loading...</p>;
  if (error) return <p>Error Loading Data</p>;
  return (
    <div className={styles.page}>
      <main className={styles.main}>
        <p>{data?.email}</p>
        <p>{data?.name}</p>
      </main>
    </div>
  );
}
