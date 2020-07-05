FROM payara/micro:jdk11

ADD build/libs/ROOT.war $DEPLOY_DIR
EXPOSE 8080
CMD ["--nocluster", "--nohostaware", "--deploy", "$DEPLOY_DIR/ROOT.war"]
