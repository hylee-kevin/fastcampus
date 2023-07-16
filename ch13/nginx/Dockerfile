FROM nginx:1.23.1-alpine
WORKDIR /etc/nginx/
RUN rm /etc/nginx/conf.d/default.conf
COPY nginx.conf /etc/nginx/conf.d/
EXPOSE 80
