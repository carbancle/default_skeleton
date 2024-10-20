"use client";

import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import React, { useState } from 'react'

const QueryProvider = ({ children }: { children: React.ReactNode }) => {
  const [queryClient] = useState(() => (
    new QueryClient({
      defaultOptions: {
        queries: {
          retry: 1, // API 요청 실패시 재시도 횟수
        }
      }
    })
  ))
  return (
    <QueryClientProvider client={queryClient}>
      {children}
    </QueryClientProvider>
  )
}

export default QueryProvider