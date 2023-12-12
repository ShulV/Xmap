<a href="https://github.com/ShulV/Xmap/blob/main/README_en.md">Английская версия</a>

<h1>🗺️ Xmap (Карта мест)</h1>

<h2>❓ Небольшая помощь для разработчиков</h2>

<h3>👉 Как открыть Swagger документацию:</h3>
<p>http://localhost:8080/swagger-ui/index.html</p>

<h3>👉 Как получить коллекцию для Postman:</h3>
<p>Скачайте последнюю версию с https://github.com/ShulV/Xmap/tree/main/postman и импортируйте в Postman.</p>

<h3>👉 Как инициализировать базу данных (PostgreSQL):</h3>
<ul>
  <li>Создайте все таблицы, индексы, функции и т.д., также вставьте основные справочники: https://github.com/ShulV/Xmap/blob/main/sql/generate_DB.sql</li>
  <li>Вставьте справочники для стран/регионов/городов: https://github.com/ShulV/Xmap/blob/main/sql/insert_cities_regions_countries.sql</li>
  <li>Вставьте пользователя и некоторые реальные споты: https://github.com/ShulV/Xmap/blob/main/sql/additional_insert_scripts.sql</li>
</ul>

<h3>👉 Как просмотреть модель базы данных:</h3>
<p>Скачайте и откройте в специальных UML-инструментах: https://github.com/ShulV/Xmap/blob/main/uml/relation_model.csv</p>
<p>Разработчики должны держать эту модель в актуальном состоянии или добавлять в репо новые её версии, указывая в названии дату изменения.</p>

<h3>👉 Как писать логи:</h3>
<ul>
  <li>Используйте snake_case для переменных</li>
  <li>Записывайте переменные в квадратных скобках ('[', ']')</li>
  <li>Пишите двоеточие (':') перед блоком переменных</li>
  <li><code>logger.atInfo()</code> - информация, <code>logger.atWarn()</code> - предупреждения, <code>logger.atError()</code> - критические ошибки</li>
</ul>

```java
logger.atInfo().log("Информация об изображении создана: [image_info_id = '{}']", imageInfo.getId());
```

<h3>👉 Код стайл:</h3>
<p>Максимальное количество символов в строке - 130. По дефолту IntelliJ IDEA устанавливает ограничительную линию на 120 (перенастроить).</p>

<h4>Как называть ключи JSON:</h4>
<p>(snake_case)</p>

```javascript
"some_key_name" : "value" 
```

<p>Многие библиотеки JSON (в Spring Framework), такие как Jackson, поддерживают формат camelCase из коробки.</p>
<p>Но было решено, что snake_case более удобен для чтения.</p>

<h4>Как называть ключи в структурах данных например в HashMap ("key_name", "value"):</h4>
<p>snake_case</p>

```java
Map<String, String>; someMap = new HashMap<>();
someMap.put("key_name_in_snake_case", "value");
```

<h4>Как называть пути роутов (эндпоинтов):</h4>
<p>kebab-case</p>

```java
@RequestMapping("/api/v1/image-service") //для всего контроллера

@PostMapping("/spot-image/{id}") //для метода
```

<h4>Размеры методов и классов:</h4>
<p>Максимальная длина методов - 100 строк. Максимальная длина класса - 1000 строк.</p>
<p>Если размер метода или класса превышает эти значения, новая бизнес-логика может быть добавлена только в виде вызовов методов других функций.</p>
<p>Но лучше внимательно рефакторить!</p>

<h4>Структура пакетов:</h4>
<p>Когда появляется более одного контроллера, сервиса, репозитория, модели, DTO и т.д., мы объединяем их в один пакет.</p>

<h3>👉 Внедрение зависимостей с использованием Lombok:</h3>
<p>Используйте @RequiredArgsConstructor и только его. 1 конструктор для внедрения зависимостей (без аннотации @Autowired).</p>
<p>Lombok сгенерирует конструктор, который принимает все необходимые зависимости, помеченные из переменных final или @NonNull.</p>

<h3>👉 Swagger V.3</h3>
<div>
  <ul>
  <li>
    <b>@Operation</b>: Эта аннотация используется для документирования отдельных операций в вашем контроллере. Она позволяет предоставить подробное описание операции, включая краткое описание (summary), подробное описание (description), теги (tags), параметры запроса, схему запроса и ответа.

        ```java
          @Operation(
            summary = "Get user by ID",
            description = "Returns a single user by ID",
            parameters = {
                @Parameter(name = "userId", description = "User ID", in = ParameterIn.PATH)
            },
            responses = {
                @ApiResponse(responseCode = "200", description = "Successful operation", 
                  content = @Content(mediaType = "application/json")),
                @ApiResponse(responseCode = "404", description = "User not found")
            },
            tags = {"User Operations"}
            )
          ```
          
  </li>
  <li>
      <b>@Parameter</b>: Используется для описания параметров запроса. Например, параметры пути, параметры запроса, параметры заголовков и т. д.

        ```java
          @Parameter(name = "userId", description = "User ID", in = ParameterIn.PATH)
          ```
          
  </li>
  <li>
    <b>@RequestBody</b>: Определяет тело запроса для операции. Используется для описания тела запроса для операций типа POST, PUT, и др.

        ```java
          @RequestBody(description = "User details", required = true, content = @Content(mediaType = "application/json"))
          ```
          
  </li>
  <li>
    <b>@ApiResponse</b>: Описывает возможные ответы от операции. Указывает код состояния HTTP, описание и схему ответа.

        ```java
          @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation", 
            content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "User not found")
            })
          ```
          
  </li>
  <li>
    <b>@Hidden</b>: Скрывает операцию в документации Swagger. Полезно, например, если вы хотите временно исключить операцию из документации.

        ```java
          @Hidden
          ```
          
  </li>
  <li>
    <b>@Tag</b>: Используется для группировки операций в документации Swagger по тегам. Это удобно для организации и структурирования больших API.

        ```java
          @Tag(name = "User Operations", description = "Endpoints for user-related operations")
          ```
          
  </li>
  
  </ul>
</div>

<h3>👉 Работа с access и refresh токенами</h3>
<div>
  <h4>Как кодировать/декодировать?</h4>
  <img src='https://github.com/ShulV/Xmap/blob/main/readme-images/doc/jwt_io_example.jpg' width='60%'>
</div>

<h2>❓ Некоторые пока что нерешенные проблемы приложения:</h2>
<ul>
  <li>Проблема Hibernate N+1</li>
  <li>LOMBOK в тестах не работает, не понял почему. Внедряем зависимости там с помощью одного конструктора с @Autowired (без него даже с одним конструктором почему-то не работает).</li>
</ul>
