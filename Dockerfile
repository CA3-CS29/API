FROM adoptopenjdk/openjdk11:latest

RUN mkdir -p /app

WORKDIR /app

COPY build/libs/api-1.jar ./app.jar

EXPOSE $PORT

CMD [ "java", "-jar", "./app.jar" ]