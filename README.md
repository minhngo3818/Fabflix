## Project Fabflix

#### By: Tuyen Ngo

### Generals
Fabflix is a web application designed for movie enthusiasts, allowing users 
to search for and purchase movies with ease. The platform offers an intuitive 
interface, enabling users to browse a vast catalog, read movie descriptions, 
and make secure transactions. With features aimed at enhancing user experience, 
Fabflix aims to streamline the process of discovering and acquiring films online.

### Instruction of deployment:

  #### Encrypt Passwords
  ```mvn exec:java -Dexec.cleanupDaemonThreads=false -Dexec.mainClass="UpdateSecurePassword" -Dexec.args=<table-name>```  
  user tables: customers, employees
  
  #### Insert XML Data
  ```mvn exec:java -Dexec.cleanupDaemonThreads=false -Dexec.mainClass="XMLParser.XMLInsertDataProcess"```


### Fabflix Info
Website Url HTTP via AWS: <a href="add-your-cloud-ip">fabflix</a>    
Website Url HTTP via GCP: <a href="add-your-cloud-ip">fabflix</a>

### MISC
Procedures File: <a href="https://github.com/minhngo3818/blob/main/scripts/stored_procedure.sql">stored_procedure.sql</a>


### Project 4 Report

- ### Connection Pooling
    - #### Connection Pooling filepath
      
      - <a href="https://github.com/minhngo3818/blob/main/src/main/java/services/_dashboard/AddGenreDbServlet.java">src/main/java/services/_dashboard/AddGenreDbServlet.java</a>
      - <a href="https://github.com/minhngo3818/blob/main/src/main/java/services/_dashboard/AddMovieDbServlet.java">src/main/java/services/_dashboard/AddMovieDbServlet.java</a>
      - <a href="https://github.com/minhngo3818/blob/main/src/main/java/services/_dashboard/AddStarDbServlet.java">src/main/java/services/_dashboard/AddStarDbServlet.java</a>
      - <a href="https://github.com/minhngo3818/blob/main/src/main/java/services/_dashboard/MetadataDbServlet.java">src/main/java/services/_dashboard/MetadataDbServlet.java</a>
      - <a href="https://github.com/minhngo3818/blob/main/src/main/java/services/auth/EmployeeLoginServlet.java">src/main/java/services/auth/EmployeeLoginServlet.java</a>
      - <a href="https://github.com/minhngo3818/blob/main/src/main/java/services/auth/LoginServlet">src/main/java/services/auth/LoginServlet.java</a>
      - <a href="https://github.com/minhngo3818/blob/main/src/main/java/services/cart/CarServlet.java">src/main/java/services/cart/CarServlet.java</a>
      - <a href="https://github.com/minhngo3818/blob/main/src/main/java/services/movies/BrowsedMovieListServlet.java">src/main/java/services/movies/BrowsedMovieListServlet.java</a>
      - <a href="https://github.com/minhngo3818/blob/main/src/main/java/services/movies/GenreListServlet.java">src/main/java/services/movies/GenreListServlet.java</a>
      - <a href="https://github.com/minhngo3818/blob/main/src/main/java/services/movies/MovieListServices.java">src/main/java/services/movies/MovieListServices.java</a>
      - <a href="https://github.com/minhngo3818/blob/main/src/main/java/services/movies/MovieListServlet.java">src/main/java/services/movies/MovieListServlet.java</a>
      - <a href="https://github.com/minhngo3818/blob/main/src/main/java/services/movies/MovieSuggestionServices.java">src/main/java/services/movies/MovieSuggestionServices.java</a>
      - <a href="https://github.com/minhngo3818/blob/main/src/main/java/services/movies/SearchedMovieListServlet.java">src/main/java/services/movies/SearchedMovieListServlet.java</a>
      - <a href="https://github.com/minhngo3818/blob/main/src/main/java/services/movies/SingleMovieServlet.java">src/main/java/services/movies/SingleMovieServlet.java</a>
      - <a href="https://github.com/minhngo3818/blob/main/src/main/java/services/movies/SuggestionMovieListServlet.java">src/main/java/services/movies/SuggestionMovieListServlet.java</a>
      - <a href="https://github.com/minhngo3818/blob/main/src/main/java/services/payment/PaymentServlet.java">src/main/java/services/payment/PaymentServlet.java</a>
      - <a href="https://github.com/minhngo3818/blob/main/src/main/java/services/sales/SalesServlet.java">src/main/java/services/sales/SalesServlet.java</a>
      - <a href="https://github.com/minhngo3818/blob/main/src/main/java/services/stars/SingleStarServlet.java">src/main/java/services/stars/SingleStarServlet.java</a>

    - #### Explain how Connection Pooling is utilized in the Fabflix code.

        The initial problem with connecting to the MySQL database is that new 
        connections are continually being created, queries executed, and then closed. 
        This process consumes significant computing and memory resources for every single request cycle, 
        resulting in slower performance for Fabflix. Connection pooling addresses this inefficiency by 
        utilizing pre-established connections defined in context.xml, allowing for the reuse of connections 
        for future queries. The implemented try-with-resources statement automatically closes connections, 
        placing them in an idle state instead of completely shutting them down. This approach conserves memory 
        for the Tomcat server, which is essential for handling concurrency, 
        thereby enhancing the overall performance of the application.
  
    - #### Explain how Connection Pooling works with two backend SQL.

        Both backend servers also connect to their respective databases in the same manner. Connection pooling operates on 
        both the master and slave backend servers, providing the same efficiency as previously described. 
        This enhances the application’s performance and conserves memory resources on each individual server.

- ### Master/Slave
  - ####  Master/Slave SQL filepath

    - <a href="https://github.com/minhngo3818/blob/main/src/main/java/services/_dashboard/AddGenreDbServlet.java">src/main/java/services/_dashboard/AddGenreDbServlet.java</a>
    - <a href="https://github.com/minhngo3818/blob/main/src/main/java/services/_dashboard/AddMovieDbServlet.java">src/main/java/services/_dashboard/AddMovieDbServlet.java</a>
    - <a href="https://github.com/minhngo3818/blob/main/src/main/java/services/_dashboard/AddStarDbServlet.java">src/main/java/services/_dashboard/AddStarDbServlet.java</a>
    - <a href="https://github.com/minhngo3818/blob/main/src/main/java/services/payment/PaymentServlet.java">src/main/java/services/payment/PaymentServlet.java</a>
    - <a href="https://github.com/minhngo3818/blob/main/src/main/java/UpdateSecurePassword.java">src/main/java/UpdateSecurePassword.java</a>
  
  - #### How read/write requests were routed to Master/Slave SQL?
      The main web application read and writes also mirror to master instance, but only slave instance can read data from the database. Whenever customers or employees perform create/update request (write query), apache2 only 
  redirect write requests only to master server. Reading request or read queries can either transfer to master or slave routes

### Project 5 Report
- Jmeter throughput 3 nodes 1 control plain, 2 app pods: 4,339.152/minute
- Jmeter throughput 4 nodes 1 control plain, 3 app pods: 4,375.566/minute