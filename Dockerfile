FROM maven:3.9.3-amazoncorretto-17
WORKDIR /tests
COPY . .
CMD mvn clean test