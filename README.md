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
  <li>Use snakecase for variables</li>
  <li>Write variables in square brackets ('[', ']')</li>
  <li>Write a colon (':') before variables block</li>
  <li>.atInfo() - informing, .atWarn() - warnings, .atError - critical errors</li>
</ul>

```java
logger.atInfo().log("Image info created: [image_info_id = '{}']", imageInfo.getId());
```

### Hot to name JSON keys: 
someName (camelCase)
Why? Many JSON libraries (in Spring Framework), such as Jackson, support this format out of the box.

### Size of methods and classes:
The maximum length of methods is 100 lines. The maximum class length is 1000 lines.
If the size of a method or class exceeds these values, then new business logic can only be added in the form of calling methods of other functions. 
But it is better to carefully refactor it!
