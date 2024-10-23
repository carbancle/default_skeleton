"use client";

import { loginUser } from "@/lib/api"
import { useMutation } from "@tanstack/react-query"
import { useRouter } from "next/navigation"
import { ChangeEvent, FormEvent, useState } from "react"

interface Login {
  email: string,
  password: string,
}

const LoginPage = () => {
  const router = useRouter();
  const [formData, setFormData] = useState<Login>({
    email: "",
    password: "",
  })

  const mutation = useMutation({
    mutationFn: (loginData: Login) => loginUser(loginData.email, loginData.password),
    onSuccess: () => {
      console.log("로그인 성공");
      router.push("/");
    },
    onError: (error) => {
      alert("로그인 실패");
      console.error("로그인 실패", error);
    },
  })

  const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    })
  }

  const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    mutation.mutate(formData);
  }
  return (
    <>
      <h1>Login</h1>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Email:</label>
          <input
            type="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label>Password:</label>
          <input
            type="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            required
          />
        </div>
        <button type="submit" disabled={mutation.isPending}>
          {mutation.isPending ? "Logging in..." : "Login"}
        </button>
      </form>
      {mutation.isError && <p>Error: {mutation.error?.message}</p>}
    </>
  )
}

export default LoginPage