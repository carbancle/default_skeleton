/** @type {import('next').NextConfig} */
const isProd = process.env.NODE_ENV === "production";
const nextConfig = {
  // 정적빌드 project의 out/ 디렉토리에 build 파일 생성
  output: isProd ? "export" : undefined,
  /**
   * 활성화시 default path 설정 /name.html -> /name/index.html 로 변경
   */
  trailingSlash: isProd ? true : false,
  // 이미지 로더 사용중 빌드 수행되지 않으므로 비활성화 하여 빌드가 정상적으로 수행되록 함
  // images: {
  //   unoptimized: true,
  // },
};

export default nextConfig;
