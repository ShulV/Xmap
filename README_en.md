# :world_map: Xmap (Map of spots)
## :question: A little help for the developers
### How to open swagger doc:
http://localhost:8080/swagger-ui/index.html
### How to get a collection for postman:
download last veriosn from https://github.com/ShulV/Xmap/tree/main/postman and import into postman 
### How to initialize the database (PostgreSQL):
<ul>
  <li>Create all tables, indexes, functions and etc, also insert main refrerences: https://github.com/ShulV/Xmap/blob/main/sql/generate_DB.sql</li>
  <li>Insert Countries/Regions/Cities references: https://github.com/ShulV/Xmap/blob/main/sql/insert_cities_regions_countries.sql</li>
  <li>Insert a user and some real spots: https://github.com/ShulV/Xmap/blob/main/sql/additional_insert_scripts.sql</li>
</ul>

### How to view the relational model of a database:
download and open in special UML-tools: https://github.com/ShulV/Xmap/blob/main/uml/relation_model.csv

### How to write logs:
<ul>
  <li>Use snake_case for variables</li>
  <li>Write variables in square brackets ('[', ']')</li>
  <li>Write a colon (':') before variables block</li>
  <li>.atInfo() - informing, .atWarn() - warnings, .atError - critical errors</li>
</ul>

```java
logger.atInfo().log("Image info created: [image_info_id = '{}']", imageInfo.getId());
```

### How to name JSON keys: 
(snake_case)
```javascript
"some_key_name" : "value" 
```
P.S. many JSON libraries (in Spring Framework), such as Jackson, support camelCase format out of the box.

But snake_case is more convenient to read now.

### Size of methods and classes:
The maximum length of methods is 100 lines. The maximum class length is 1000 lines.

If the size of a method or class exceeds these values, then new business logic can only be added in the form of calling methods of other functions. 

But it is better to carefully refactor it!

### Package structure:
When more than one controller, service, repository, model, dto and etc. appears, we put them together in a package.

### Dependency injection via lombok:
Use @RequiredArgsConstructor and only it. 1 constructor for autowiring (without @Autowired annotation).
Lombok will generate a constructor that accepts all required dependencies, marked as final or @NonNull.

## :question: Unresolved app problems:
<ul>
  <li>Hibernate N+1 problem</li>
  <li></li>
  <li></li>
  <li></li>
  <li></li>
</ul>
