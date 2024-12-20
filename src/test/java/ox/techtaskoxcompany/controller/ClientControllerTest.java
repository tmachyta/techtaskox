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
import ox.techtaskoxcompany.dto.client.ClientDto;
import ox.techtaskoxcompany.dto.client.CreateClientRequestDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClientControllerTest {
    protected static MockMvc mockMvc;
    private static final Long DEFAULT_ID = 1L;
    private static final Long SECOND_ID = 2L;
    private static final String FIRST_NAME = "client1";
    private static final String SECOND_NAME = "client2";
    private static final String FIELD = "field";
    private static final String ADDRESS = "address";

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
                    new ClassPathResource(
                            "database/client/add-default-clients.sql"
                    )
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
                    new ClassPathResource(
                            "database/client/delete-from-clients.sql"
                    )
            );
        }
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = "classpath:database/client/delete-test-client.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Create a new client")
    void createClient_ValidRequestDto_Success() throws Exception {

        CreateClientRequestDto request = createClientRequestDto(
                FIRST_NAME,
                FIELD,
                ADDRESS
        );

        ClientDto expected = createExpectedClientDto(
                DEFAULT_ID,
                SECOND_NAME,
                FIELD,
                ADDRESS
        );

        String jsonRequest = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(post("/clients")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ClientDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), ClientDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Get list clients")
    void getAll_GivenClientsInCatalog() throws Exception {
        ClientDto firstClient = createExpectedClientDto(
                DEFAULT_ID,
                FIRST_NAME,
                FIELD,
                ADDRESS
        );

        ClientDto secondClient = createExpectedClientDto(
                SECOND_ID,
                SECOND_NAME,
                FIELD,
                ADDRESS
        );

        List<ClientDto> expected = new ArrayList<>();
        expected.add(firstClient);
        expected.add(secondClient);

        MvcResult result = mockMvc.perform(get("/clients")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ClientDto[] actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), ClientDto[].class);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Get client by id")
    @Sql(
            scripts = "classpath:database/client/add-default-clients.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = "classpath:database/client/delete-from-clients.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void getClientById() throws Exception {
        ClientDto expected = createExpectedClientDto(
                SECOND_ID,
                SECOND_NAME,
                FIELD,
                ADDRESS
        );

        MvcResult result = mockMvc.perform(get("/clients/{id}", DEFAULT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ClientDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), ClientDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Soft-Delete client by id")
    void testDeleteById() throws Exception {
        MvcResult result = mockMvc.perform(delete("/clients/{id}", DEFAULT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = "classpath:database/client/delete-from-clients.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Update client by id")
    void updateClientById() throws Exception {
        CreateClientRequestDto request = createClientRequestDto(
                SECOND_NAME,
                FIELD,
                ADDRESS
        );

        ClientDto expected = createExpectedClientDto(
                DEFAULT_ID,
                SECOND_NAME,
                FIELD,
                ADDRESS
        );

        String jsonRequest = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(put("/clients/{id}", DEFAULT_ID)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ClientDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), ClientDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Search client by parameters")
    void searchContacts_GivenSearchParameters() throws Exception {
        ClientDto firstClient = createExpectedClientDto(
                DEFAULT_ID,
                FIRST_NAME,
                FIELD,
                ADDRESS
        );

        List<ClientDto> expected = List.of(firstClient);

        String[] names = {FIRST_NAME};
        String[] fields = {FIELD};

        MvcResult result = mockMvc.perform(get("/clients/search")
                        .param("names", FIRST_NAME)
                        .param("fields", FIELD)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ClientDto[] actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), ClientDto[].class);

        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    private ClientDto createExpectedClientDto(
            Long id,
            String name,
            String field,
            String address) {
        return new ClientDto()
                .setId(id)
                .setName(name)
                .setField(field)
                .setAddress(address);
    }

    private CreateClientRequestDto createClientRequestDto(
            String name,
            String field,
            String address) {
        return new CreateClientRequestDto()
                .setName(name)
                .setField(field)
                .setAddress(address);
    }
}
