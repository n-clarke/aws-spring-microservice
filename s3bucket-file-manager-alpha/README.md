# Spring Boot Rest API
This is using a Musician as a usage example.
Where a Musician stores the following details
```
    private Long id;
    private String firstName, lastName, instrument;
    private String[] skills;
```
## API Calls
### View All Musician Details using API call.
```
http://localhost:8080/api/v1/musicians
```

### View Musician by specific Id.
Replace {i} with the id you want to search for.
```
http://localhost:8080/api/v1/musicians/{i}
```
