FROM maven:3.8.3-openjdk-17 as builder

COPY ./pom.xml ./pom.xml
COPY ./src ./src
RUN mvn -X clean package

ENTRYPOINT ["mvn"]
CMD ["spring-boot:run"]

EXPOSE 8081
