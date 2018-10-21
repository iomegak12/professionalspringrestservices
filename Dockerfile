FROM maven:latest

COPY . /app

WORKDIR /app

RUN mvn clean install

EXPOSE 8080

ENTRYPOINT java -jar /app/target/easy-notes-0.0.1-SNAPSHOT.jar
