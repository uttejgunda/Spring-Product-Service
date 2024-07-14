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


# Day 183

> **REPOSITORY PATTERN -**
- The code for interacting with the DB should always be separate from the Business logic. 
- So code of interacting with database should not be in Service class. They should be an intermediatary class between Service & Database.
- This intermediatary class where we write the SQL queries to execute on Database, is called a **Repository**.
>> SERVICE ---> REPOSITORY CLASS ---> DATABASE
- This way in future if we want to change the type of database, it would not affect with lot of changes in the service code. As if we do it, we will have to change all the SQL queries in the service class.
- This creates **LOOSE COUPLING** between *Service* & *Database*. 

### WHAT IS LOOSE COUPLING? 
- It means, "Any change in one file should not affect other file".
- Here, any change in Database should not affect Service. And any change in Service should not affect Database. 
- Hence we use Repository class as an intermediatory to make Service & Database be independent from each other. 
- Opposite to *LOOSE COUPLING** is **TIGHT COUPLING**

### IMPLEMENTING REPOSITORY PATTERN/LAYER - 
1. Create a `repository` package. And add a class `ProductRepository`. 
2. Here we can write queries that do the CRUD with database/products. 
3. Make this class have `@Repository` annotation. So that Spring will create a bean for it by auto and lets you use it in your Service. 
4. In the `RealProductService` class, use `@Autowired` and create the object/instance of the `ProductRepository`.
5. Now in the methods of `RealProductService`, use the `productRepository` and call the methods. For ex: inside `getAllProducts`, call `productRepository.getAllProducts()`. Inside `createProduct`, call `productRepository.save(product)`. 

- Now in Product Repository class, we have to implement the methods by writing SQL queries. 
  - For this, we will use a Spring framework that will help us talk with Databases.
  - Go to Maven Central and add the dependency for **Spring Boot Starter Data JPA**. JPA is the interface where we write the queries, Hibernate is the inbuilt ORM which will create the SQL queries based on the method name of JPA interface. 
  - Spring Boot Starter Data JPA Library comes with Hibernate (which is the ORM). But the library which connects & executes the query with Database is done by JDBC. Hibernate only creates the query. JDBC executes the query on Database. 
  - In Maven Central, add the dependency for **MySql Connector J**

  - Now we need to tell/config our Spring App on how to connect to Database. [Accessing Data MySql](https://spring.io/guides/gs/accessing-data-mysql)
  - To configure the connection, go to `application.properties` file and paste the configs as in above link. 
  - Now on the right side *Database* icon of *IntelliJ*, create a connection to the database by typing `root` user and with the mySql original `password`.
  - In the `application.properties` file, we don't want to display the original mySql password. So in the database terminal of intelliJ, write the sql commands of creating a database, creating a user and granting all privileges to that user. 
  - This way, in the `application.properties`, we can make use of the new created user for the database, and display the password given for that user. 

> **MODELS -**
- In our Spring project, models are those whose info/classes is going to be stored in the Database as Tables. 
- Ex: Product, Category ---> we will take products table, category table which will have the columns as per the properties in the Product class. 
- Product class should have the attributes of `id`, `name`, `disc`, `price`, `category`, `imageUrl`. We can also have `createdAt`, `udpatedAt` attribute. All these attributes should be converted into a table in our database. 
- Now the `category` in a product is an object itself. Means category is a separate table in database. Hence, in the product, we should have the `id` of the category stored for the product. So `id` is the `FOREIGN KEY`.

- So this way if `Product` class is converted into a TABLE, every object of the Product class will be like a ROW in the table. 

### Better way to create models -
- Ideally in every model we have attributes of `id`, then the attributes related to that model, and then `cretedAt`, `updatedAt`.
- So we can make a parent class having all the common attributes. We can call it as **Base Model**. 
- And we want the `Product` class to have the common attributes along with its own attributes. Similarly to the `Category` class. 
- We can make use of inheritance here, so that all parent attributes will come.

### Hard Delete -
- This means we delete a particular row entirely from the table completely. You will not be able to recover it and if you want again, you have to create the row again.
- In MySql, the data is stored in the form of a tree called `B+ Tree`. So for every small change, the tree rebalances which has the cost of time complexity.
- So with hard delete, data cannot be recovered and also the table updates are very slow.

### Soft Delete -
- This means marking the row as deleted.
- We can maintain a column in the table as `isDeleted`, so that for every row we can store `true` or `false` whether that row is in deleted state or not.
- So we can easily recover the data again. For example, if we delete a category called `laptop`, you just make the `isDeleted` column have value as `false`.
- Also, there won't be cost of TC, as we are not updating the Tree here since we are not changing the rows, there won't be tree rearrangement and hence updates are much faster.
- We can write the SQL query as below:
>> **select * from Products where isDeleted= false;**


## IMPLEMENTATION OF MODELS IN SPRING -
### How to convert the class into a model in Database - 
- To do this, we can make use of `@Entity` annotation to the `Product` class or whatever model class. 
- Spring will convert all the attributes as an Entity/Model/Table in the database.
**Note:**
- There should be a primary key for every table for the purpose of Indexing and to manage all the rows. 
- So spring will give an error if we don't specify which attribute should be a primary key. 
- We can tell that to Spring using `@Id`, which will make that attribute as primary key for the table.
- And these annotations like `@Entity` & `@Id` will be coming from the `JPA` library, which has all the functionalities to create tables, write sql queries etc by giving a class to it.

<u>**Note:**</u>
- When we do inheritance of common attributes from the parent `BaseModel`, the base model class itself is not going to a table. This is just having the extra attributes to be inherited to required classes.
- So we should not make the `BaseModel` class have the `@Entity` annotation. As this should not be a table. 
- So we can add the annotation as `@MapSuperClass`. This means this class is like a super class and all the inherited classes are sub-classes. And the attributes of the super class are mapped to the sub classes.

### Auto Incrementing ID:
- We want the `id` attribute to have an auto-incrementing value, hence we will use the `long` variable. 
- To make this auto incrementing, we can use the annotation `@GeneratedValue(Strategy=GenerationType.IDENTITY)`
- There are different types of strategies to generate the value - 
  - Identity means auto incrementing,
  - Auto means automatically assign a value which is unique, but it can be anything. Means 2nd row can be No.2 and 3rd row can be No.100. So its a random number. 
  - and more... you can go inside the `GenerationType` class and see.

### TELLING SPRING THAT THE ATTRIBUTE IS A FOREIGN KEY OF ANOTHER TABLE ROW
- For all the attributes, Spring is able to convert the attributes to cols in the `Product` table. But for the `category` attribute in the `Product` class, we were getting an error. 
- This is because `primitive datatypes` like `Integer`, `String`, `Long`, `Boolean` etc can be directly converted into the column of the table. 
- But, the datatype of `category` attribute is a separate model all together. Means, we need to define the relation between the `Category` & `Product` model **_(CARDINALITY)_** 
- As per the below explanation, as the cardinality of Product & Category table is `ManyToOne`, we need to use the annotation `@ManyToOne` in the Product near the category attribute to tell JPA that when creating the tables, the `id` of category should be present in the `Products` table as a column **_(FOREIGN KEY)_**. 
- After Running code, you can find the `id` of the `Category` table as a column in the `Products` table.  

<u>**Note -**</u>
- Never apply the cardinality in both the tables. 
- For ex: If we add `@OneToMany` annotation in for the `Category` table also by maintaining an attribute like `private List<Product> products`, then when you run the code, as the cardinality is mentioned in both `Product` & `Category` class, a mapping table would be created automatically. 
- This is because we can never have a list of values in a single category row. In Relational Databases we can never store multiple values in a row. 
- So now Spring thinks, the relation mentioned in Products table `ManyToOne` and the relation mentioned in Category table `OneToMany` are two separate relations. 
- Hence, Spring adds a mapping table but putting the category id with the product id as it is one to Many relation since we wanted to store the list of products for every category row. 
- So if we still want to maintain the list of products in category table also,  we need to tell Spring that this relation is same vice versa relation as the relation mentioned in `Products` table. 
- To tell spring that it is the same relation at both tables but just an inverse in Category table, we can tell spring as below - 
  - Put the annotation in Category table for the `List<Product> products` as `@OneToMany(mappedBy="category")`
  - This means we are telling that this One to Many relation in Category table is already handled by the category column in the Product table ----> List<Product> table (It can know without mentioning the table name also, because the Product Generic is already a Table which spring knows because of @Entity for Product class. Hence it understands that it is mapped by category in Product table)
  - So this way if we mention the cardinality annotation in both the `Product` & `Category` table, there won't be problem.

### Finding Cardinalities - 
- 1 Product can have 1 category as per our scenario. 
- 1 Category can have many products. 
- So the Cardinality is "MANY TO ONE"

(1)            (many) 
PRODUCT        CATEGORY  === MANY TO ONE  
(many)         (1)
  - For "ONE TO ONE" relation          ----> We store the id of any side on the other side of model.
  - For "MANY TO ONE" or "ONE TO MANY" ----> We store the id of one in many rows like a FOREIGN KEY. 
  - For "MANY TO MANY" relation        ----> We store the data in a MAPPING TABLE



