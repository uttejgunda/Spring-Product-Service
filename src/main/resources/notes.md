# Day 180

> **MVC Architecture -**
- Learnt about the MVC Architecture. 
- Models - Views - Controllers (includes Services & Repo)

> **Annotations Learned -**
- RestController
- @Getter - Creates Getters
- @Setter - Creates Setters
- @NoArgsConstructor // CREATES NO ARGS CONSTRUCTOR
- @AllArgsConstructor // CREATES ALL ARGS CONSTRUCTOR
- @Service - Makes the class as a service
- @Primary - Makes the class as primary service
- @Autowired - Adds the dependency of the service to the controller. (Whichever service has @Primary, that would be selected.)
- @GetMapping - Makes the RestController accept the GET request.
- @PostMapping - Makes the RestController accept the POST request.
- Similarly for all the HTTP requests


> **CLASSES -**
- restTemplate.getForObject(URL, dtoClass.class );
>> DATA TRANSFER OBJECT - Helps to convert the response object as an object of Java DTO class
