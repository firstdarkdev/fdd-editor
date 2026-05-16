FROM node:22-alpine as frontend-builder

WORKDIR /app

COPY ./frontend/ .
RUN npm install
RUN npm run build

FROM amazoncorretto:17-alpine-jdk AS builder

# Copy Working FIles
WORKDIR /app
COPY ./backend/ .

# Build it
RUN chmod +x gradlew
RUN ./gradlew shadowJar

# Stage 2: Create Main Docker File
FROM amazoncorretto:17-alpine-jdk
WORKDIR /app

COPY --from=builder /app/build/libs/backend.jar /app/backend.jar
COPY --from=frontend-builder /app/dist /app/web
EXPOSE 3000

ENTRYPOINT ["java", "-jar", "backend.jar"]