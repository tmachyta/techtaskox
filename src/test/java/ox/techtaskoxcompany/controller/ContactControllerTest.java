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
import ox.techtaskoxcompany.dto.contact.ContactDto;
import ox.techtaskoxcompany.dto.contact.CreateContactRequestDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ContactControllerTest {
    protected static MockMvc mockMvc;
    private static final Long DEFAULT_ID = 1L;
    private static final Long SECOND_ID = 2L;
    private static final Long CLIENT_ID = 1L;
    private static final String FIRST_NAME = "contact1";
    private static final String SECOND_NAME = "contact2";
    private static final String EMAIL = "test@gmail.com";
    private static final String FIRST_LAST_NAME = "contact1";
    private static final String SECOND_LAST_NAME = "contact2";
    private static final String PHONE = "38043223";

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
    @Sql(
            scripts = "classpath:database/contact/delete-test-contact.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Create a new contact")
    void createContact_ValidRequest_Success() throws Exception {
        CreateContactRequestDto request = createContactRequestDto(
                FIRST_NAME,
                FIRST_LAST_NAME,
                EMAIL,
                PHONE,
                CLIENT_ID
        );

        ContactDto expected = createExpectedContactDto(
                DEFAULT_ID,
                FIRST_NAME,
                FIRST_LAST_NAME,
                EMAIL,
                PHONE,
                CLIENT_ID
        );

        String jsonRequest = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(post("/contacts")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ContactDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), ContactDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Get list of contacts")
    void getAll_GivenContactsInCatalog() throws Exception {
        ContactDto firstContact = createExpectedContactDto(
                DEFAULT_ID,
                FIRST_NAME,
                FIRST_LAST_NAME,
                EMAIL,
                PHONE,
                CLIENT_ID
        );

        ContactDto secondContact = createExpectedContactDto(
                SECOND_ID,
                SECOND_NAME,
                SECOND_LAST_NAME,
                EMAIL,
                PHONE,
                CLIENT_ID
        );

        List<ContactDto> expected = new ArrayList<>();
        expected.add(firstContact);
        expected.add(secondContact);
        MvcResult result = mockMvc.perform(get("/contacts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ContactDto[] actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), ContactDto[].class);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Get contact by id")
    void getContactById() throws Exception {

        ContactDto expected = createExpectedContactDto(
                DEFAULT_ID,
                FIRST_NAME,
                FIRST_LAST_NAME,
                EMAIL,
                PHONE,
                CLIENT_ID
        );

        MvcResult result = mockMvc.perform(get("/contacts/{id}", DEFAULT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ContactDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), ContactDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Soft-Delete contact by id")
    void testDeleteById() throws Exception {
        MvcResult result = mockMvc.perform(delete("/contacts/{id}", DEFAULT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Update client id")
    void updateClientId() throws Exception {
        CreateContactRequestDto request = updateClientContactById(CLIENT_ID);

        ContactDto updatedContact = createExpectedContactDto(
                SECOND_ID,
                FIRST_NAME,
                FIRST_LAST_NAME,
                EMAIL,
                PHONE,
                CLIENT_ID
        );

        String jsonRequest = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(put("/contacts/client/{id}", SECOND_ID)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ContactDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), ContactDto.class);

        assertNotNull(actual);
        assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(updatedContact, actual, "id");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Search contacts by parameters")
    void searchContacts_GivenSearchParameters() throws Exception {
        ContactDto firstContact = createExpectedContactDto(
                DEFAULT_ID,
                FIRST_NAME,
                FIRST_LAST_NAME,
                EMAIL,
                PHONE,
                CLIENT_ID
        );

        List<ContactDto> expected = List.of(firstContact);

        String[] names = {FIRST_NAME};
        String[] lastNames = {FIRST_LAST_NAME};

        MvcResult result = mockMvc.perform(get("/contacts/search")
                        .param("names", FIRST_NAME)
                        .param("last_names", FIRST_LAST_NAME)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ContactDto[] actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), ContactDto[].class);

        Assertions.assertEquals(expected, Arrays.stream(actual).toList());
    }

    private ContactDto createExpectedContactDto(
            Long id,
            String name,
            String lastName,
            String email,
            String phone,
            Long clientId
    ) {
        return new ContactDto()
                .setId(id)
                .setName(name)
                .setLastName(lastName)
                .setEmail(email)
                .setPhone(phone)
                .setClientId(clientId);
    }

    private CreateContactRequestDto createContactRequestDto(
            String name,
            String lastName,
            String email,
            String phone,
            Long clientId
    ) {
        return new CreateContactRequestDto()
                .setName(name)
                .setLastName(lastName)
                .setEmail(email)
                .setPhone(phone)
                .setClientId(clientId);
    }

    private CreateContactRequestDto updateClientContactById(Long clientId) {
        return new CreateContactRequestDto()
                .setClientId(clientId);
    }
}
