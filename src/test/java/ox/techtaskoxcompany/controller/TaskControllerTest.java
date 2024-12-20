package ox.techtaskoxcompany.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ox.techtaskoxcompany.dto.task.CreateTaskRequestDto;
import ox.techtaskoxcompany.dto.task.TaskDto;
import ox.techtaskoxcompany.model.Task;
import ox.techtaskoxcompany.model.Task.Status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerTest {
    protected static MockMvc mockMvc;
    private static final Long DEFAULT_ID = 1L;
    private static final Long SECOND_ID = 2L;
    private static final Long CONTACT_ID = 1L;
    private static final String DESCRIPTION = "test";
    private static final Status STATUS_OPEN = Status.OPEN;
    private static final Status STATUS_IN_PROGRESS = Status.IN_PROGRESS;
    private static final LocalDate DEADLINE = LocalDate.parse("2024-12-25");

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/client/add-default-clients.sql")
            );
        }
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/contact/add-default-contacts.sql")
            );
        }
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/task/add-default-tasks.sql")
            );
        }
    }

    @AfterAll
    static void afterAll(
            @Autowired DataSource dataSource
    ) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/task/delete-from-tasks.sql")
            );
        }
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/contact/delete-from-contacts.sql")
            );
        }
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/client/delete-from-clients.sql")
            );
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    /*@Sql(
            scripts = "classpath:database/task/delete-test-task.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )*/
    @DisplayName("Create a new task")
    void createTask_ValidRequest_Success() throws Exception {
        CreateTaskRequestDto request = createTaskRequestDto(
                DESCRIPTION,
                STATUS_OPEN,
                DEADLINE,
                CONTACT_ID
        );

        TaskDto expected = createExpectedDto(
                DEFAULT_ID,
                DESCRIPTION,
                STATUS_OPEN,
                DEADLINE,
                CONTACT_ID
        );

        String jsonRequest = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(post("/tasks")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        TaskDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), TaskDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Get list of tasks")
    void getAll_GivenTasksInCatalog() throws Exception {
        TaskDto firstTask = createExpectedDto(
                DEFAULT_ID,
                DESCRIPTION,
                STATUS_OPEN,
                DEADLINE,
                CONTACT_ID
        );

        TaskDto secondTask = createExpectedDto(
                SECOND_ID,
                DESCRIPTION,
                STATUS_OPEN,
                DEADLINE,
                CONTACT_ID
        );

        List<TaskDto> expected = new ArrayList<>();
        expected.add(firstTask);
        expected.add(secondTask);
        MvcResult result = mockMvc.perform(get("/tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        TaskDto[] actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), TaskDto[].class);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(
            scripts = "classpath:database/task/delete-from-tasks.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/task/add-default-tasks.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @DisplayName("Get contact by id")
    void getTaskById() throws Exception {

        TaskDto expected = createExpectedDto(
                DEFAULT_ID,
                DESCRIPTION,
                STATUS_OPEN,
                DEADLINE,
                CONTACT_ID
        );

        MvcResult result = mockMvc.perform(get("/tasks/{id}", DEFAULT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        TaskDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), TaskDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Soft-Delete task by id")
    void testDeleteById() throws Exception {
        MvcResult result = mockMvc.perform(delete("/tasks/{id}", DEFAULT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Update client id")
    void updateClientId() throws Exception {
        CreateTaskRequestDto request = updateContactTaskById(CONTACT_ID);

        TaskDto updatedTask = createExpectedDto(
                SECOND_ID,
                DESCRIPTION,
                STATUS_OPEN,
                DEADLINE,
                CONTACT_ID
        );

        String jsonRequest = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(put("/tasks/contact/{id}", SECOND_ID)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        TaskDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), TaskDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(updatedTask, actual, "id");
    }

    private TaskDto createExpectedDto(
            Long id,
            String description,
            Task.Status status,
            LocalDate deadline,
            Long contactId
    ) {
        return new TaskDto()
                .setId(id)
                .setDescription(description)
                .setStatus(status)
                .setDeadline(deadline)
                .setContactId(contactId);
    }

    private CreateTaskRequestDto createTaskRequestDto(
            String description,
            Task.Status status,
            LocalDate deadline,
            Long contactId
    ) {
        return new CreateTaskRequestDto()
                .setDescription(description)
                .setStatus(status)
                .setDeadline(deadline)
                .setContactId(contactId);
    }

    private CreateTaskRequestDto updateContactTaskById(Long contactId) {
        return new CreateTaskRequestDto()
                .setContactId(contactId);
    }

    private CreateTaskRequestDto updateTaskStatusById(Status status) {
        return new CreateTaskRequestDto()
                .setStatus(status);
    }
}
