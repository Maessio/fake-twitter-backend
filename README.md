# Fake Twitter - Backend

This is the backend for a **Fake Twitter** project, a simulated application that replicates the basic functionality of a social network like Twitter. The backend is developed using **Java 21**, **Spring Boot**, and additional libraries like **Spring Security**, **Spring Web**, **JPA**, and **JWT** to handle authentication, token management, and data persistence.

The application includes features like user authentication with JWT tokens, secure API endpoints, and basic CRUD operations for posts and user management. It serves as the backend for the **Fake Twitter** frontend application.

### Technologies Used

- **Java 21**: The latest version of Java, used for building robust and scalable backend applications.
- **Spring Boot**: A framework that simplifies Java web application development by providing embedded servers, dependency management, and auto-configuration.
- **Spring Security**: A powerful and customizable authentication and access control framework for securing the application.
- **Spring Web**: Used to create RESTful APIs to communicate with the frontend.
- **JUnit**: A framework used for testing Java applications, ensuring the correctness and reliability of the backend.
- **Mockito**: A mocking framework used in combination with JUnit to mock objects and isolate tests.
- **JWT (JSON Web Tokens)**: A compact and self-contained way to represent information securely between client and server.
- **JPA (Java Persistence API)**: A framework used to manage database entities and provide a clean abstraction layer over the database.

This backend serves as the core of the Fake Twitter project, handling user authentication, data storage, and API endpoints.

### Useful Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security Documentation](https://spring.io/projects/spring-security)
- [JWT Documentation](https://jwt.io/introduction/)
- [JPA Documentation](https://docs.oracle.com/javaee/7/tutorial/persistence-intro.htm)

### Swagger Documentation

For an interactive API documentation, you can access the Swagger UI at the following endpoint:

[http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/)

Swagger provides an easy-to-use interface to test all available API endpoints directly in your browser. It will display all routes, expected parameters, and possible responses.

### Insomnia Collection

A collection for testing the Fake Twitter API is available in the root directory of the project. The file is named **FakeTwitterCollection**. You can import this collection into **Insomnia** to interact with the backend API and test various endpoints (including authentication and CRUD operations).

To import the collection in **Insomnia**:
1. Open Insomnia.
2. Click on the "Import" button in the top left.
3. Choose "From File" and select the **FakeTwitterCollection** file from the root directory.
4. Now you can use the pre-configured API requests for testing.

### Additional Commands

#### Clone the Repository
Clone the repository to your local machine.

#### Install Dependencies
Ensure that you have **Maven** or **Gradle** installed. Once the repository is cloned, navigate to the project directory and use the following command to install dependencies:

mvn clean install

This will install all necessary dependencies listed in the `pom.xml` (for Maven).

#### Run the Project
After the dependencies are installed, start the Spring Boot application by running the following command:

mvn spring-boot:run

This will start the backend server, and you can access the API at [http://localhost:8080](http://localhost:8080).

#### Running Tests
To run the unit tests with JUnit and Mockito, use the following command:

mvn test



