# Nginx를 위한 base 이미지 사용
FROM nginx:alpine

# Nginx 설정 파일을 컨테이너로 복사
COPY ./default.conf /etc/nginx/conf.d/default.conf

# Nginx 실행
CMD ["nginx", "-g", "daemon off;"]