CWD := $(abspath $(patsubst %/,%,$(dir $(abspath $(lastword $(MAKEFILE_LIST))))))

war:
	./gradlew microBundle

run:
	java -jar build/libs/ROOT-microbundle.jar \
    --nocluster \
    --nohostaware

docker-build: war
	docker build . -t api-unity

docker-run:
	docker run -p 8080:8080 -v $(CWD)/build/libs:/opt/payara/deployments api-unity
