FROM payara/micro:jdk11
ADD build/libs/ROOT.war $DEPLOY_DIR
EXPOSE 8080 8181
CMD ["--nocluster", "--nohostaware", "--autoBindSsl", "--deploy", "/opt/payara/deployments/ROOT.war"]
