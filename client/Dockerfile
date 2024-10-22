FROM  node:18-alpine AS base

# Install dependencies only when needed
FROM base AS deps
RUN apk add --no-cache libc6-compat
WORKDIR /app

# Install dependencies based on the preferred package manger
COPY package.json package-lock.json* ./
RUN npm ci

# 1. Build stage (Next.js standalone 모드로 빌드)
FROM base AS builder
WORKDIR /app
COPY --from=deps /app/node_modules ./node_modules

# 앱 소스 복사
COPY . .

# Next.js standalone 모드로 빌드 (standalone 모드를 통해 독립적인 Node.js 서버 실행 가능)
RUN npm run build

# 2. Production stage (Node.js 서버 실행 및 Nginx 리버스 프록시 설정)
FROM base AS runner

# 빌드 결과물을 복사 (Next.js standalone과 필요한 모든 파일)
COPY --from=builder /app/.next/standalone .
COPY --from=builder /app/.next/static ./.next/static
# COPY --from=builder /app/public /app/public

EXPOSE 3000

ENV PORT 3000

CMD ["node", "server.js"]
