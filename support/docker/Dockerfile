FROM openjdk:17.0.1-slim

USER root

EXPOSE 8080

ENV DIRPATH /qsa-storage/qsa-formula-handler
RUN mkdir -p $DIRPATH
RUN apt-get update && apt-get install -y curl

COPY ./qsa-formula-handler-server/build/libs/qsa-formula-handler-final.jar $DIRPATH/qsa-formula-handler.jar
COPY ./support/docker/init.sh $DIRPATH/init.sh
RUN chmod 755 $DIRPATH/init.sh
WORKDIR $DIRPATH

HEALTHCHECK CMD curl --fail http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["sh", "/qsa-storage/qsa-formula-handler/init.sh"]