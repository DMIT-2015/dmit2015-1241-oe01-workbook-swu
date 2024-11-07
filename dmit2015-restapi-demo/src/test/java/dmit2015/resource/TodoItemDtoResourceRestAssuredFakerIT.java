package dmit2015.resource;

import dmit2015.dto.TodoItemDto;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import net.datafaker.Faker;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

/**
 * This class contains starter code for testing REST API endpoints for CRUD operations using REST-assured.
 *
 * <a href="https://github.com/rest-assured/rest-assured">REST Assured GitHub repo</a>
 * <a href="https://github.com/rest-assured/rest-assured/wiki/Usage">REST Assured Usage</a>
 * <a href="http://www.mastertheboss.com/jboss-frameworks/resteasy/restassured-tutorial">REST Assured Tutorial</a>
 * <a href="https://hamcrest.org/JavaHamcrest/tutorial">Hamcrest Tutorial</a>
 * <a href="https://github.com/FasterXML/jackson-databind">Jackson Data-Binding</a>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TodoItemDtoResourceRestAssuredFakerIT {

    static Faker faker = new Faker();

    final String todoItemDtoResourceUrl = "http://localhost:8090/restapi/TodoItemsDto";

    @BeforeAll
    static void beforeAllTests() {
        // code to execute before all tests in the current test class
    }

    @AfterAll
    static void afterAllTests() {
        // code to execute after all tests in the current test class
    }

    @BeforeEach
    void beforeEachTestMethod() {
        // Code to execute before each test such as creating the test data
    }

    @AfterEach
    void afterEachTestMethod() {
        // code to execute after each test such as deleting the test data
    }

    @Order(1)
    @Test
    void givenTodoItemDtoData_whenCreateTodoItemDto_thenTodoItemDtoIsCreated() throws Exception {
        // Arrange: Set up the initial state
        var currentTodoItemDto = new TodoItemDto();
        currentTodoItemDto.setName(faker.pokemon().move());
        currentTodoItemDto.setComplete(false);

        // Act & Assert
        try (Jsonb jsonb = JsonbBuilder.create()) {
            String jsonBody = jsonb.toJson(currentTodoItemDto);
            given()
                    .contentType(ContentType.JSON)
                    .body(jsonBody)
                    .when()
                    .post(todoItemDtoResourceUrl)
                    .then()
                    .statusCode(201)
                    .header("location", containsString(todoItemDtoResourceUrl))
            ;
        }
    }

    @Order(2)
    @Test
    void givenExistingTodoItemDtoId_whenFindTodoItemDtoById_thenReturnTodoItemDto() throws Exception {
        // Arrange: Set up the initial state
        var currentTodoItemDto = new TodoItemDto();
        currentTodoItemDto.setName(faker.pokemon().move());
        currentTodoItemDto.setComplete(false);

        // Act & Assert
        try (Jsonb jsonb = JsonbBuilder.create()) {
            String jsonBody = jsonb.toJson(currentTodoItemDto);

            Response response = given()
                    .contentType(ContentType.JSON)
                    .body(jsonBody)
                    .when()
                    .post(todoItemDtoResourceUrl);
            var postedDataLocation = response.getHeader("location");

            // Act & Assert
            given()
                    .when()
                    .get(postedDataLocation)
                    .then()
                    .statusCode(200)
                    .body("id", notNullValue())
                    .body("name", equalTo(currentTodoItemDto.getName()))
                    .body("complete", equalTo(currentTodoItemDto.isComplete()))
            ;
        }

    }

    @Order(3)
    @Test
    void givenTodoItemDtoExist_whenFindAllTodoItemDtos_thenReturnTodoItemDtoList() throws Exception {
        // Arrange: Set up the initial state
        try (Jsonb jsonb = JsonbBuilder.create()) {
            // Post 3 records and verify the 3 records are added
            var todoItemDtos = new ArrayList<TodoItemDto>();
            for (int index = 0; index < 3; index++) {
                var currentTodoItemDto = new TodoItemDto();
                currentTodoItemDto.setName(faker.pokemon().move());
                currentTodoItemDto.setComplete(false);

                todoItemDtos.add(currentTodoItemDto);

                given()
                        .contentType(ContentType.JSON)
                        .body(jsonb.toJson(todoItemDtos.get(index)))
                        .when()
                        .post(todoItemDtoResourceUrl)
                        .then()
                        .statusCode(201);
            }

            // Act & Assert: Perform the action and verify the expected outcome
            given()
                    .when()
                    .get(todoItemDtoResourceUrl)
                    .then()
                    .statusCode(200)
                    .body("size()", greaterThan(0))
                    .body("name", hasItems(todoItemDtos.getFirst().getName(), todoItemDtos.getLast().getName()))
                    .body("complete", hasItems(todoItemDtos.getFirst().isComplete(), todoItemDtos.getLast().isComplete()))
            ;

        }

    }

    @Order(4)
    @Test
    void givenUpdatedTodoItemDtoData_whenUpdatedTodoItemDto_thenTodoItemDtoIsUpdated() throws Exception {
        // Arrange: Set up the initial state
        var createTodoItemDto = new TodoItemDto();
        createTodoItemDto.setName(faker.pokemon().move());
        createTodoItemDto.setComplete(false);

        var updateTodoItemDto = new TodoItemDto();
        updateTodoItemDto.setName(faker.pokemon().move());
        updateTodoItemDto.setComplete(true);

        try (Jsonb jsonb = JsonbBuilder.create()) {
            String createJsonBody = jsonb.toJson(createTodoItemDto);

            Response response = given()
                    .contentType(ContentType.JSON)
                    .body(createJsonBody)
                    .when()
                    .post(todoItemDtoResourceUrl);
            var postedDataLocation = response.getHeader("location");
            Long entityId = Long.parseLong(postedDataLocation.substring(postedDataLocation.lastIndexOf("/") + 1));
            updateTodoItemDto.setId(entityId);
            // Act & Assert
            String updateJsonBody = jsonb.toJson(updateTodoItemDto);
            given()
                    .contentType(ContentType.JSON)
                    .body(updateJsonBody)
                    .when()
//                .pathParam("id", entityId)
                    .put(postedDataLocation)
                    .then()
                    .statusCode(200)
                    .body("id", equalTo(entityId.intValue()))
                    .body("name", equalTo(updateTodoItemDto.getName()))
                    .body("complete", equalTo(updateTodoItemDto.isComplete()))
            ;
        }

    }

    @Order(5)
    @Test
    void givenExistingTodoItemDtoId_whenDeleteTodoItemDto_thenTodoItemDtoIsDeleted() throws Exception {
        // Arrange: Set up the initial state
        var currentTodoItemDto = new TodoItemDto();
        currentTodoItemDto.setName(faker.pokemon().move());
        currentTodoItemDto.setComplete(false);

        try (Jsonb jsonb = JsonbBuilder.create()) {
            String jsonBody = jsonb.toJson(currentTodoItemDto);

            Response response = given()
                    .contentType(ContentType.JSON)
                    .body(jsonBody)
                    .when()
                    .post(todoItemDtoResourceUrl);
            var postedDataLocation = response.getHeader("location");
            int entityId = Integer.parseInt(postedDataLocation.substring(postedDataLocation.lastIndexOf("/") + 1));

            // Act & Assert
            given()
//                .pathParam("id", entityId)
                    .when()
                    .delete(postedDataLocation)
                    .then()
                    .statusCode(204);

            // Verify deletion
            given()
                    .when()
                    .delete(postedDataLocation)
                    .then()
                    .statusCode(404);
        }

    }

}