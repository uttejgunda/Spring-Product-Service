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


# Day 181

- @Configuration // WE ARE TELLING SPRING THAT, BEFORE MAKING ALL OTHER ANNOTATIONS LIKE REST CONTROLLER, SERVICE ETC - I WANT TO HAVE THIS CONFIGS BEANS. AND THEN OTHERS

>> **Beans** are the objects that Spring creates automatically in the Spring Application Context 

- **@Bean**

_We use this annotation inside the Configuration class for a method, to define how the Bean should be created by Spring. So technically, spring calls this method and creates the returned object_ 

- We used the @Bean to create a Bean for the RestTemplate, which we wanted to use to make the API call to FakeStoreAPI.
- Also we create it as a method inside the configuration class, so that incase if we want to pass any values to the class while creating the object, we can do it in the method (example commented in the config file)
- In the FakeStoreProductService, we put the @Autowired for the RestTemplate variable. So this way, when Spring loads, due to the config Bean, the RestTemplate object will be created by Spring by auto, and then it Autowires it to the matching variable that has @Autowired annotation.

> **POST METHOD IMPLEMENTATION:**
- We created a FakeStoreRequestDto class, so that when the client sends an object in the Request body, it gets converted as an object of this DTO class in our controller parameter.
- Then we pass that object to the createProduct in the FakeStoreProductService
- In the FakeStoreProductService, we make the API call using the _restTemplate.postForObject(url, RequestObjectToSend ,ResponseDtoClass.class)_
- Once we receive the response from Fake store, we store it as an object of our Response DTO class
- Then convert it as per our Database table Product format and return it to the client. 

> **CODE IMPROVISATION**
- We have create a common `convertResponseToProduct` method, which receives the FakeStoreResponse object and converts & returns it. So that we can use this to reduce repeating the conversion process for every API call. 
_(Following DRY principle of coding --- DO NOT REPEAT YOURSELF)_
- Similarly to handle converting Post resonse objects we create another separate method, because in future POST response object may have more attributes compared to GET response object. 
FURTHER CODE IMPROVISATION -

### FURTHER CODE IMPROVISATION
- We can create a method in the FakeStoreResponse class itself, which creates the product and does the conversion process. It uses `this.value` which will help it get the values from its object itself. 
- This way we can even avoid creating a common method in the service class. We can have the method in the response class itself so that we can simply call it. 

> *EXCEPTIONS -*
- Suppose you search for a product with id > 20, then Fake store will not give response. Hence in our code, responseObject will be `null` and we get `nullPointerException`.
- To handle this, we can put an if condition in the service, that if `response == null`, then `throw new Exception("Product not found, id incorrect")`. 
- As this method can throw an exception, we need to add `throws Exception` on the method signature. 

- Then in the Controller, as the particular method can receive an Exception, we need to handle it there. 
- Hence we can put a `try & catch` blocks. 
- Also instead of returning only the product directly from the controller, we create a `ProductResponseDto` class with attributes of `Product` & `status`. So that we can set it either with the product object & success status. Or set it to null product & failure status. 
- And finally return the object of `ProductResponseDto` instead of returning the `Product` directly. 

### IMPROVING EXCEPTION HANDLING -
- When an exception happens, throwing `new Exception()`, it is more general exception. 
- Instead, it is better to create our own Exception. So we can create a class `ProductNotFoundException extends Exception` so that it will have all the properties of the `Exception` parent class.
- We create a constructor inside the class, which will receive the error string argument and passes it to the Exception parent class by using `super`.
- So this was we can change in the `FakeStoreProductService` by using `throws new ProductNotFoundException(String)` instead of throwing general `new Exception()`

### MULTIPLE EXCEPTIONS -
- There can be scenarios that a single methods can throw multiple exceptions. 
- For ex, the `getSingleProduct` method in the `FakeStoreProductService` can throw `ProductNotFoundException` & `DBNotFoundException`.
- In this case, we need to add both the exceptions to the signature of `getSingleProduct`.
- And in the `ProductController`, we need to add another `catch` block handling `DBNotFoundException`.

> *RESPONSE ENTITY -*

- Every Response should ideally have
  - Data
  - StatusCode
  - Information about Response
- Now in the above scenario, incase if exception happens, we are returning a null object. 
- However, the response status is still 200. That should not happen because Error should also be meaningful with right status codes.
- Explained about Status code meanings.... Refer to NxtWave JS status codes lesson for better info if needed.

To handle the problem of sending right statusCodes, we use the `ResponseEntity` class. 
- Make the Controller return the object of ResponseEntity by writing `ResponseEntity<ProductResponseDto>`
- And then, inside the method of the controller, create an object of `ResponseEntity` and pass the `productResponseDto` and also the Status code as below -
>> ResponseEntity<ProductResponseDtc> = new ResponseEntity<>(productResponseDto, HttpStatus.OK)`;

- Similarly, if anything goes wrong, in the catch block we can do 
>> ResponseEntity<ProductResponseDtc> = new ResponseEntity<>(productResponseDto, HttpStatus.INTERNAL_SERVER_ERROR)`;

