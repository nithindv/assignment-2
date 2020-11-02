
FROM egovio/alpine-node-builder-10:yarn AS build  
WORKDIR /app
ENV NODE_OPTIONS "--max-old-space-size=1792"

# copy the project files
COPY . .
RUN yarn install && yarn run build

# Create runtime image
FROM nginx:mainline-alpine 
ENV WEB_DIR=/var/web/chinook-dashboard

RUN mkdir -p ${WEB_DIR}

COPY --from=build /app/build ${WEB_DIR}/
COPY --from=build /app/nginx.conf /etc/nginx/conf.d/default.conf
