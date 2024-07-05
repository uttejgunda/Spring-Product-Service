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

> **EXCEPTIONS -**
- Suppose you search for a product with id > 20, then Fake store will not give response. Hence in our code, responseObject will be `null` and we get `nullPointerException`.
- To handle this, we can put an if condition in the service, that if `response == null`, then `throw new Exception("Product not found, id incorrect")`. 
- As this method can throw an exception, we need to add `throws Exception` on the method signature. 

- Then in the Controller, as the particular method can receive an Exception, we need to handle it there. 
- Hence we can put a `try & catch` blocks. 
- If we don't handle it, then controller returns the stack trace of the error to the client directly. 
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

> **RESPONSE ENTITY -**

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


# Day 182
> **Qualifiers -**
- It gives a specific names to the beans of Spring. Suppose if you have two beans of same type, to distinguish both of them, we can add Qualifiers to beans. 
- For ex, you have `RealProductService` & `FakeStoreProductService`, and both have @Server. 
- So if we want to tell Spring that Controller should use a particular Server among both of them, we can tell with Qualifier. 
- For the server, you can put - 
>> @Server
>> @Qualifier("RealProductService")
- In the Controller, you can tell it to use the particular Server by using Qualifier as below -
>> @Autowired
>>@Qualifier("RealProductService")

- So it is like an alternative for `@Primary`, 
- Especially, when we have multiple servers & controllers, we can use Qualifiers to make every controller work by Autowiring with specific bean 
- Example, we created another controller `HelloController`, to that we are specifying to Autowire with `RealService` Qualifier. 
- Now the `ProductController` & `HelloController`, both are going to work by using two different Services. 


> **Controller Advice -**
- To handle different types of exceptions, we add soo many `try & catch` blocks for every endPoint in the controller. 
- This will make the code become messy with soo many `catch()` blocks. 
- So we can use Controller Advice to solve this problem by handling all exceptions at one place. 
- By using this, we can write controller code without using `try & catch`.
- This way, controller will also throw the exception. And this exception is received by ControllerAdvice class which will handle the error before returning it to the client. 

### How to create/implement the Controller Advice
- Create a new package `controllerAdvice` and add a class `GlobalControllerAdvice`. 
- This `GlobalControllerAdvice` will handle the exceptions for all endpoints automatically as spring calls the controller advice class automaticall incase of exception arising. 
- Now to make Spring call this `GlobalControllerAdvice` automatically, we need to use `@RestControllerAdvice` annotation which tells spring to use this automatically when exceptions arise. 
- Create a method with the annotation `@Exception(ProductNotFoundException.class)` or with any other exception.class. This way when this exception comes, this method will be called by Spring. 
- Inside the method, we can write the code which we used to write in `catch` block. 
- Also, for the parameter to this method, whatever object is thrown during the time of throwing the Exception, that exception object will be received by this parameter similar like how we write `catch(ProductNotFoundException e)`. 
- Then we can create another DTO `errorResponseDto` which we can use to create the response as an object containing the error message. 
- And then again wrap it with ResponseEntity, to ensure status code also comes as per the error. 
- Finally return the ResponseEntity. 

### Another way to handle Exceptions without @RestControllerAdvice
- If we write the above method with `@Exception(ProductNotFoundException.class)` inside the Controller class itself -
  - Then, when an error is thrown in the controller class, Spring will first check if there is a handler in that class itself
  - If it is not there, then only it will try to search for the bean of @RestControllerAdvice and call the method present in the controller Advice

> **JOURNEY OF A REQUEST IN SPRING BOOT -**
- In Spring boot, there is something called as `DISPATCHER SERVLET`. 
- This receives the request from the client and sends it to that particular controller. 
- It is like a central piece, like the manager who handles which request where to go. 
- For ex, there are many controllers - then dispatcher servlet sends the product requests to product controller, payment requests to payments controller etc
- It does the job of mapping a particular endpoint - Ex: `/product/1` to the `getSingleProduct()` method. 

- Dispatcher Servlet (TOMCAT) is like an internal server mode of spring and that is also like a bean. By default TOMCAT is the name of the Dispatcher Servlet. 
- Also, there is something called as `HANDLER MAPPING`. Servlet asks the handler mapping for the correct method to serve the API/endpoint. And handler mapping returns it to the servlet. 
- Then the SERVLET sends the request to that method in that particular CONTROLLER, and then it goes to the particular SERVICE, then to the REPO and finally response it returned to the client. 

  - You can see that Spring does all the mapping internally by putting the below statement in the `application.properties` file in the resources -
  >> logging.level.org.springframework.web: TRACE

### Theory of Spring:
> **SPRING DATA JPA -**
- Till now our server has not done any real work. It is just acting like a proxy, getting products from FakeStoreApi and returning it to the client. 
- But if we want our ProductService to get the products, we need to have our own *DATABASE*.
- Database is a very crucial component for an App. Every database should support the features of 
  1. Create tables
  2. Use CRUD operations on those tables. 

### HOW TO INTERACT WITH DATABASE -
- Suppose if we want to create new product:
  1. We first create a Database object. 
  2. Then connect to DB. 
  3. Next, we execute an SQL Query on the DB and receive all the List<row> rows
  4. Then after, we convert all the rows to List<Product> using a loop. 
  5. Finally return the list of products to the client. 

Hence, to make this cubersome work of working with database for every endpoint, we will use *SPRING DATA*.

### Spring Data -
> **ORM (OBJECT RELATION MAPPING) -**
- ORM does the task of converting our Java classes into SQL tables.
- ORM - Object refer to Classes, Relation refers to Tables and mapping it. 
- All the attributs of the class will become the columns of the tables. If there is an other object as an attribute in our class, then it will be like the foreign key in the table. 
- For ex: For a Product, we have `id`, `title`, `price`, `category`. Here category is the object of an other class. So this will be converted in Sql table using a foreign key.
- Doing the above cubersome steps of interacting with Database is also Error prone. So ORM libraries help us to work with Database easily. 

- ORM says: *"You just tell me what models you have in your codebase. I will automatically create queries for you for those models."*
- So its like if we just say `orm.getAllProducts()`, it will automatically execute the Sql query for it.
- The most popular ORM for Java is **"HIBERNATE"**. Other options are also there like **MyBatis**, **Jooq**.

> **JPA - JAVA PERSISTENCE API**
- After writing our code by using the methods like `hibernate.getAllProducts()`, switching to another ORM like MyBatis or Jooq in future will be difficult as we need to change the whole code as we are directly depending on hibernate in our code.
- Hence, we will use an **Interface** in between App & ORM Libraries. 
- Java built an Interface themselves for this purpose which is called **JPA**. And by default all the ORM libraries implement the JPA Interface.
- So, in our code we should use like `jpa.getAllProducts()`. This way it will be easy for us to switch between databases in future. 

> **JAVA DATABASE CONNECTOR (JDBC)-**
- ORM (Hibernate) is the one which executes the query to Database. 
- Now there can be many types of Databases like: MySql, MongoDB, PostGreSQL. 
- So Hibrenate has to do the hardwork to make its query work for all the types of Databases. 
- Hibernate should only be responsible for its own logic of query and talking to DB. But the query should be executed by some other which connects to Database.
- Hence we will use an other Interface called **JDBC**. 
- The implementations of JDBC Interface will have the code for connecting to the database and execute the Sql query given by the ORM. 

### FLOW OF THE ARCHITECTURE - 
**App** -----> (`getProductById()`) gives method name to **JPA** -----> **ORM** which writes the Sql query for the method -----> **JDBC Interface**, with the help of its implementations connects to the Database of MySql or PostGreSql etc -----> **DATABASE**

