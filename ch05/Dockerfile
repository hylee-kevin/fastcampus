FROM node:20-alpine3.17

RUN apk add --no-cache tini curl

WORKDIR /app

COPY runapp.js .

EXPOSE 6060

ENTRYPOINT ["/sbin/tini", "--"]

CMD ["node", "runapp.js"]
