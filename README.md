# Simple Java Web App

A simple Spring Boot TODO web application using in-memory storage.

## Requirements

- Java 21
- Docker, optional

## Run locally

`./gradlew bootRun`

Open: [http://localhost:8080/](http://localhost:8080/)

## Build Docker image

`docker build -t simple-java-web-app .`

## Run Docker container

`docker run --rm -p 8080:8080 simple-java-web-app`