# Email Service

## Description
This application acts as an abstraction between 2 email providers.
It supports a fail-over from one provider to the other by doing a simple health check on the providers.  

This application uses MailGun and SendGrid as its providers, please refer to the following for more information on their APIs

* https://sendgrid.com/docs/API_Reference/Web_API_v3/index.html
* https://documentation.mailgun.com/en/latest/user_manual.html#sending-via-api

## Tech stack
* Spring Boot 2.1.9
* Java 1.8 and above
* Maven 3.x  


## Todo
* Unit test cases
* Asynchronous process to handle the email requests if both the providers are down


## Architecture

### Layers
* Resource - Handles the request and response
* Service - Validates and processes the request and generates a response

### Properties files
* application.properties - Default properties file


## Process flow
1. Client sends a request to /send/email
2. SendEmailController#sendEmail() accepts the requests and calls EmailService#sendEmail()
3. EmailService#save() does the following
    * Validates the inputs and will throw BadRequestException if there's an error
    * Executes health check on the primary provider and if it fails it'll try the secondary provider. When both fails it will reply back with 500 error response
    * Creates a connection to an email provider
    * Constructs the request body according to the selected provider
    * If it gets a 'good' response from the provider then reply back with a valid response message
    * If it gets a 'bad' response from the provider then reply back with 500 error response



## Setup

### Mail providers
* Create an account with SendGrid and MailGun
* Take notes on the api url and key
* Update http-api.url and http-api.key of mailgun and sendgrid providers in application.properties

    

## How to run it from the command line

Once you have finished with the setup, you can execute the following command to run it. If you want to run the test you can remove '-Dmaven.test.skip=true' from the command 
```text
mvn clean package -Dmaven.test.skip=true && java -jar -Dspring.profiles.active=local target/emailservice-0.0.1-SNAPSHOT.jar
```

Once started you should see the following lines and can start using it
```text
o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
c.s.e.EmailserviceApplication            : Started EmailserviceApplication in 3.645 seconds (JVM running for 4.269)
```  



## End-points
#### Sending an email
A 'POST' request is used to send an email to one or more recipients. 'to', 'cc' and 'bcc' are optional but at least one has to be set.

Request structure

* "from" - The sender in String - Mandatory
* "to" - An array of recipients in String - Optional - Max 10 recipients
* "cc" - An array of recipients in String - Optional- Max 10 recipients
* "bcc" - An array of recipients in String - Optional - Max 10 recipients
* "subject" - The email subject - Mandatory
* "text" - The email body - Mandatory

Request example

```text
POST /send/email HTTP/1.1
Content-Type: application/json;charset=UTF-8
Host: localhost
Content-Length: <xyz>

{
  "from": "whoami@example.org",
  "to": [
    "john@example.org"
  ],
  "cc": [
	"peter@example.org"
  ],
  "subject": "Test email",
  "text": "Test email from email service application"
}
```

Response example
```text
HTTP/1.1 201 Created
Content-Type: application/json;charset=UTF-8
Content-Length: <xyz>

{
	"message": "Your email sent to the provider successfully"
}
```
