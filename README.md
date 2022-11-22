# Audio Manager

A Microservice to correct the grammar of an audio file.

## Execution

Executable JAR file is stored in: ./dpm/build/libs/dpm-0.1-all.jar

```bash
java -jar dpm-0.1-all.jar
```
Project: Unpack zip file and open folder with Intellij

## Technolgy Details  for Code Example

Micronaut: Version v3.7.3,
Java: OpenJDK 17,
Gradle: 7.5.1

IDE: IntelliJ IDEA 2022.2.3 (Ultimate Edition)

REST API:
Swagger Viewer: https://editor.swagger.io/
Swagger File is stored in documentation folder
./dpm/build/classes/java/main/META-INF/swagger/dpm-0.0-all.yml


Specification of other parts see next chapter

## Requirements and resulting Modules

To clarify the following description: each hyphen (‘-’) marks a service; each (‘.’) marks a function.
See image.

- Gateway
. Load Balancing
. Security: DOS prevention (limit count requests)



Kubernetes (All services are running in docker containers):

Frontend: 
- Web Server (single page)
Angular
. record audio
. replay
. trigger authentication
. communication to Backend (REST API) 
Thoughts: As an alternative to the Angular frontend, you could also use a cross-platform framework IONIC. This could be used to generate an app using Webview, but such an app has a longer startup time. 


Backend: REST API  (Input: email adress + audio file; Output: corrected audio) , Micronaut Services:
- File Manager
. Rest API: POST with email adress and audio file
. Validate File
. Store File with user info
. Forward file to Queue for Audio processing service (together with unique process id)
Thoughts: instead of forwarding the complete file one could think of putting it into the file server and sending the link to it
. On the other side this would cause two services accessing the same database which is not wanted in such a microservice architecture as far as i know.
- Queue 
. Gathers and forwards all requests for audio processing 
Thoughts: is maybe needed if audio processing takes long time, important if there are many requests at once
- Audio Processing:
. audio to text
. correct text grammatically
. text to audio
. responding with corrected audio

- Authentication
. verify credentials / session

___________

Data Management

Account Management
- Postgres 
- Fields
    - Username
    - Email
    - Password hash with salt and pepper
    - date of creation
    - …

In Memory DB
- redis
- Content: session data 

Audio Manager Database
- Postgres
- Fields
    - user email 
    - Path to File on Fileserver
    - time stamp order
    - Path to processed file
    - unique order id


File Server for audio files


## Discussion


maintainability
- good due to microservice architecture

extendability:
- easy to add service
- scalable via kubernetes and containerization

stability:
- microservice architecture: if one service fails, the others remain
- kubernetes: several instances of the same service

performance:
- queue

security:
- Input Validation
- DOS Protection in Gateway
