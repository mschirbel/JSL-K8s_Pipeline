FROM node:9-alpine
WORKDIR /src
COPY app/ .
RUN npm install --quiet
ENV NAME=Marcelo
EXPOSE 8080
CMD ["npm", "start"]