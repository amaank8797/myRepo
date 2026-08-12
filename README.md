Why Maven instead of Gradle?
Answer:
Maven manages dependencies, builds the project, and uses pom.xml to define project configuration.
  Why Spring Web?
Answer:
It allows us to build REST APIs by providing Spring MVC, an embedded Tomcat server, JSON conversion with Jackson, and annotations like @RestController, @GetMapping, etc.
  Why Actuator?
Answer:
It provides production-ready endpoints such as /actuator/health, /actuator/info, and /actuator/metrics to monitor the application's health and runtime information.
  Parent

spring-boot-starter-parent

Question:
Why?
Answer:
It manages compatible versions of Spring Boot dependencies and plugins. We don't need to specify versions for every dependency because the parent POM provides them.
   Java Version

<java.version>21</java.version>

Question:
Why?
Answer:
Tells Maven to compile the project using Java 21.
  Dependency 1

spring-boot-starter-webmvc

This is the most important dependency.
If your manager asks
Why?
Answer:
It provides
* Spring MVC
* Embedded Tomcat
* DispatcherServlet
* REST support
* Jackson
* MVC annotations
Without this dependency
These annotations won't work

@RestController

@GetMapping

@PostMapping

Tomcat won't start.
No REST API.
   Dependency 2

spring-boot-starter-actuator

Purpose
Provides production-ready endpoints.
Example
/actuator/health
/actuator/info
/actuator/metrics
Useful for monitoring.
  Plugin

spring-boot-maven-plugin
Question
Why?
Answer
It packages and runs the Spring Boot application.
Commands like
mvn spring-boot:run
use this plugin.
  Why are we creating a Controller?
Answer
A Controller receives HTTP requests from the client, processes them (or forwards them to the service layer), and returns an HTTP response.
In Spring MVC, the controller acts as the entry point for incoming requests.
  Why @RestController?
Answer:
@RestController tells Spring Boot that this class will handle REST API requests. It combines @Controller and @ResponseBody, meaning the returned value is written directly into the HTTP response body instead of resolving a view.
Without it,
Spring will not treat this class as a REST Controller.
  What happens internally?
This is one of the most common interview questions.

Browser / Postman
        │
        ▼
Embedded Tomcat
        │
        ▼
DispatcherServlet
        │
        ▼
Handler Mapping
        │
        ▼
ProducerController
        │
        ▼
getMessage()
        │
        ▼
Returns String
        │
        ▼
HTTP Response
   Commands to run  Step 1: Verify Maven Wrapper
Run:

./mvnw -v

Why?
This checks that the Maven Wrapper is working and shows the Maven and Java versions being used.

Step 2: Clean the project

./mvnw clean

Why?
It deletes the previous target folder so you get a fresh build.

Step 3: Compile

./mvnw compile

Why?
It compiles all Java source files into .class files.
If there's a syntax error, it will fail here.

Step 4: Run the application

./mvnw spring-boot:run

This is probably the command your manager expects.
It starts the embedded Tomcat server and launches the Spring Boot application.

Another common command
Sometimes managers prefer:

./mvnw clean spring-boot:run

This first cleans the project and then immediately starts it.

  FLOW We only wrote one controller. How did Spring find it?
Answer:
1. main() executes.
2. SpringApplication.run(...) starts the IoC container.
3. Spring scans the base package: com.amaan.producerservice
4. It finds @RestController.
5. It creates a bean for ProducerController.
6. It detects @GetMapping("/message").
7. It registers that URL.
8. When a request comes to /message, Spring invokes getMessage().
   management.endpoints.web.exposure.include
By default, Spring Boot exposes only a limited set of Actuator endpoints.
We're explicitly exposing:
* health
* info
* metrics

  management.endpoint.health.show-details=always
Normally, /actuator/health shows only:
{
  "status": "UP"
}
With this property, Spring can expose more detailed health information (depending on the configured health indicators).    Architecture:-                     Browser
                      |
                      |
      GET /fetch-message
                      |
                      ▼
            Consumer Service
              (Port 8081)
                      |
             RestTemplate
                      |
                      ▼
        http://localhost:8080/message
                      |
                      ▼
            Producer Service
              (Port 8080)
                      |
                      ▼
      "Hello from Producer Service"   Why a Config package?
Because RestTemplate is a Spring Bean.
Instead of creating it every time using:

new RestTemplate();

we'll let Spring manage it.
That is a good Spring Boot practice and gives you a chance to explain Dependency Injection

  Why do we need this?
Your manager may ask:
Why didn't you write

RestTemplate restTemplate = new RestTemplate();

inside the controller?
Answer
Because in Spring Boot, we let the IoC Container manage commonly used objects (Beans). This promotes reusability, loose coupling, and dependency injection.
   Question:
Why @Configuration?
Answer:
It tells Spring that this class contains bean definitions. During application startup, Spring scans this class and registers the beans in the IoC container.
  Question:
Why @Bean?
Answer:
It tells Spring to create, manage, and store the returned object inside the Spring IoC Container. Any other class can then inject and reuse this object.
  Question:
What is RestTemplate?
Answer:
RestTemplate is a synchronous HTTP client provided by Spring. It is used to consume REST APIs by sending HTTP requests like GET, POST, PUT, and DELETE to other services.
   Constructor Injection
Your manager will definitely ask this.

public ConsumerController(RestTemplate restTemplate)

Question:
Where did this RestTemplate come from?
Answer:
Spring created the RestTemplate bean in RestTemplateConfig because of the @Bean annotation. When creating ConsumerController, Spring automatically injects that bean through constructor injection.
   This line
restTemplate.getForObject(url, String.class);

Question:
What does getForObject() do?
Answer:
It sends an HTTP GET request to the specified URL and converts the response into the specified Java type (String in this case).
     Complete Flow

Browser
    │
GET /fetch-message
    │
    ▼
ConsumerController
    │
    ▼
RestTemplate
    │
HTTP GET
    │
    ▼
Producer Service (/message)
    │
Returns
"Hello from Producer Service"
    │
    ▼
Consumer
    │
    ▼
Browser
  we're using constructor injection instead of @Autowired because it's the recommended approach in modern Spring. If your manager asks why, you can say it's preferred because dependencies are mandatory, it improves testability, and it avoids field injection.  2. Why Constructor Injection?
If he asks:
Why not @Autowired?
Answer:
Constructor Injection is the recommended approach because:
* Dependencies become mandatory.
* It makes the class easier to unit test.
* It avoids field injection.
* It supports immutable (final) fields.
That's why we wrote:

private final RestTemplate restTemplate;

public ConsumerController(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
}
 
