FROM gradle:8.0.2-jdk11-alpine as builder
WORKDIR /build

COPY build.gradle settings.gradle /build/
RUN gradle build -x test --parallel --continue  > /dev/null 2>&1 || true

COPY . /build
RUN gradle clean build -x test --parallel

FROM openjdk:11-slim

COPY --from=builder /build/build/libs/alarmony-0.0.1-SNAPSHOT.jar .

EXPOSE 5000

ENTRYPOINT [                                                \
    "java",                                                 \
    "-jar",                                                 \
    "-Djava.security.egd=file:/dev/./urandom",              \
    "-Dsun.net.inetaddr.ttl=0",                             \
    "alarmony-0.0.1-SNAPSHOT.jar"              \
]
