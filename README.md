# mockserver-example
Example/starter implementation of mockserver https://github.com/jamesdbloom/mockserver


## Prerequisites
* [Install Docker](http://docs.docker.com/engine/installation/ "Documentation")


## Running Mock Server using Docker
```
docker run -d --name mockserver -p 1080:1080 -p 1090:1090 jamesdbloom/mockserver
```

## Creating Expectations
```
./gradlew clean run
```
