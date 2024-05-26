FROM node:18-alpine3.18 as frontend-builder

WORKDIR /app

COPY ./frontend/ .
RUN yarn
RUN yarn build-only

FROM openjdk:17-alpine AS builder

# Copy Working FIles
WORKDIR /app
COPY ./backend/ .

# Build it
RUN chmod +x gradlew
RUN ./gradlew shadowJar

# Stage 2: Create Main Docker File
FROM openjdk:17-alpine
WORKDIR /app

COPY --from=builder /app/build/libs/backend.jar /app/backend.jar
COPY --from=frontend-builder /app/dist /app/web
EXPOSE 3000

ENTRYPOINT ["java", "-jar", "backend.jar"]