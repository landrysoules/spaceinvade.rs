FROM node:wheezy
COPY layouts /blog/
COPY src /blog/
COPY feed.xml gulpfile.js index.js package.json /blog/
WORKDIR /blog

RUN npm install

EXPOSE 3000
CMD ["node", "index.js"]
