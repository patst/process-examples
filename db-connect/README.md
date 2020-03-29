# DB Connect example

Start the camunda webapp with a mysql db.
Then connect to the database instance using a sql client and view the tables.

## MySQL instance

Start a MySQL instance using docker:

```
docker run --name camunda-mysql -p 3306:3306 -e MYSQL_USER=camunda -e MYSQL_PASSWORD=camunda -e MYSQL_DATABASE=camunda -e MYSQL_RANDOM_ROOT_PASSWORD=true -d mysql:8.0.16
```

## App configuration

The dependency to the `spring-boot-starter-jdbc` (group id: `org.springframework.boot`) must be added because otherwise the property `spring.datasource` is ignored.

For the specific database a driver dependency must be added. for MySQL that is `mysql-connector-java` (group id: `mysql`).

## DB Client

Use a db client embedded in a IDE like IntelliJ to view the database tables. The connection string is `jdbc:mysql://localhost:3306`.

The database username and password is `camunda`. 
