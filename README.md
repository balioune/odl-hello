### Prerequisite
You need the followings in order to compile and test this project:
- Java 8
- Maven 3

### Create the hello project:

         mvn archetype:generate -DarchetypeGroupId=org.opendaylight.controller \
         -DarchetypeArtifactId=opendaylight-startup-archetype \
         -DarchetypeVersion=1.1.2-Beryllium-SR2 \
         -DarchetypeRepository=http://nexus.opendaylight.org/content/repositories/opendaylight.release/ \
         -DarchetypeCatalog=https://nexus.opendaylight.org/content/repositories/opendaylight.release/archetype-catalog.xml

## And responding to the prompts as follows:

        Define value for property 'groupId': : org.opendaylight.hello
        Define value for property 'artifactId': : hello
        Define value for property 'package':  org.opendaylight.hello: : 
        Define value for property 'classPrefix':  Hello: : 
        Define value for property 'copyright': : Alioune, BA.

## Build the project

        cd hello/

        mvn clean install -DskipTests

## Running the project
        cd karaf/target/assembly/bin
        ./karaf

- Verifying

At the karaf prompt type and wait to see a message :

        log:tail

The entry point of the project is the class HelloProvider. Feel free to modify it and verify the output on logs
