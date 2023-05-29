FROM nginx:1.25.0-alpine
COPY index-image.html /usr/share/nginx/html/index.html
COPY docker_logo.png /usr/share/nginx/html/docker_logo.png
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]

