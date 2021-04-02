FROM adoptopenjdk/maven-openjdk11:latest

WORKDIR /app

COPY . .

ENTRYPOINT ["sh", "test_package_start.sh"]