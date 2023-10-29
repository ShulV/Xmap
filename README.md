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
