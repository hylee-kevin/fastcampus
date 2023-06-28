# Step:1 ubuntu (base image)
FROM ubuntu:20.04

# Step:2 Nginx package install
RUN apt-get -y update && apt-get install -y -q nginx

# Step:3 source file copy
COPY index.html /var/www/html/

# Step:4 expose Port
EXPOSE 80

# Step:5 Nginx start (container execution)
CMD ["nginx", "-g", "daemon off;"]
