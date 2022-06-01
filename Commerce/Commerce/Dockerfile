FROM sapmachine:lts
LABEL author=Grzegorz_Stich
WORKDIR /opt
COPY target/*.jar /opt/CommerceApp.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/opt/CommerceApp.jar"]