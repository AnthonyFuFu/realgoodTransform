FROM openjdk:17-oracle
ENV upload.file.path="/upload/"
COPY ./target/*.jar /Documents/mydocker/realgood.jar
WORKDIR /Documents/mydocker
RUN sh -c 'touch realgood.jar'
ENTRYPOINT ["java","-jar","realgood.jar"]
EXPOSE 8080