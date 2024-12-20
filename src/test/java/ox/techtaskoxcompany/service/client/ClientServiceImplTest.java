package ox.techtaskoxcompany.service.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ox.techtaskoxcompany.dto.client.ClientDto;
import ox.techtaskoxcompany.dto.client.ClientSearchParametersDto;
import ox.techtaskoxcompany.dto.client.CreateClientRequestDto;
import ox.techtaskoxcompany.exception.EntityNotFoundException;
import ox.techtaskoxcompany.mapper.client.ClientMapper;
import ox.techtaskoxcompany.model.Client;
import ox.techtaskoxcompany.repository.client.ClientRepository;
import ox.techtaskoxcompany.repository.client.ClientSpecificationBuilder;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {
    private static final Long CLIENT_ID = 1L;
    private static final Long NON_EXISTED_CLIENT_ID = 50L;
    private static final String OLD_NAME = "Old";
    private static final String NEW_NAME = "Updated";
    private static final String FIELD = "Medicine";

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private ClientSpecificationBuilder clientSpecificationBuilder;

    @InjectMocks
    private ClientServiceImpl clientService;

    @Test
    public void testSuccessfullySaveClient() {
        CreateClientRequestDto requestDto = new CreateClientRequestDto();
        ClientDto clientDto = new ClientDto();
        Client clientToSave = new Client();

        when(clientMapper.toModel(requestDto)).thenReturn(clientToSave);

        when(clientRepository.save(clientToSave))
                .thenReturn(clientToSave);

        when(clientMapper.toDto(clientToSave)).thenReturn(clientDto);

        ClientDto result = clientService.save(requestDto);

        assertNotNull(result);
        assertEquals(clientDto, result);
    }

    @Test
    public void testGetAllClients() {
        Client client = new Client();
        Pageable pageable = PageRequest.of(0, 10);
        List<Client> clients = List.of(new Client());
        List<ClientDto> expectedClients = List.of(new ClientDto());
        Page<Client> page =
                new PageImpl<>(clients, pageable, clients.size());

        when(clientRepository.findAll(pageable)).thenReturn(page);

        when(clientMapper.toDto(client)).thenReturn(new ClientDto());

        List<ClientDto> result = clientService.getAll(pageable);

        Assertions.assertEquals(expectedClients.size(), result.size());
    }

    @Test
    public void testLoadClientById() {
        Client client = new Client();
        client.setId(CLIENT_ID);
        ClientDto clientDto = new ClientDto();
        clientDto.setId(CLIENT_ID);

        when(clientRepository.findById(client.getId()))
                .thenReturn(Optional.of(client));

        when(clientMapper.toDto(client)).thenReturn(clientDto);

        ClientDto result = clientService.findById(CLIENT_ID);

        Assertions.assertEquals(client.getId(), result.getId());
    }

    @Test
    public void testFindClientWithNonExistedId() {
        when(clientRepository.findById(NON_EXISTED_CLIENT_ID))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> clientService.findById(NON_EXISTED_CLIENT_ID));
    }

    @Test
    public void testDeleteClientById() {
        clientService.deleteById(CLIENT_ID);

        when(clientRepository.findById(CLIENT_ID))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> clientService.findById(CLIENT_ID));
    }

    @Test
    public void testUpdateClientSuccessfully() {
        CreateClientRequestDto requestDto = new CreateClientRequestDto();
        requestDto.setName(NEW_NAME);

        ClientDto expectedResult = new ClientDto();
        expectedResult.setName(NEW_NAME);

        Client client = new Client();
        client.setName(OLD_NAME);

        when(clientRepository.findById(CLIENT_ID))
                .thenReturn(Optional.of(client));

        when(clientRepository.save(client)).thenReturn(client);

        when(clientMapper.toDto(client)).thenReturn(expectedResult);

        ClientDto updatedClient =
                clientService.updateById(CLIENT_ID, requestDto);

        Assertions.assertEquals(updatedClient.getName(), expectedResult.getName());
    }

    @Test
    public void testSearchClient() {
        String[] names = {OLD_NAME};
        String[] fields = {FIELD};
        ClientSearchParametersDto searchParams = new ClientSearchParametersDto(names, fields);

        Specification<Client> specification = mock(Specification.class);
        when(clientSpecificationBuilder.build(searchParams)).thenReturn(specification);

        Client client = new Client();
        ClientDto clientDto = new ClientDto();
        List<Client> clients = List.of(client);
        List<ClientDto> expectedContacts = List.of(clientDto);

        when(clientRepository.findAll(specification)).thenReturn(clients);
        when(clientMapper.toDto(client)).thenReturn(clientDto);

        List<ClientDto> result = clientService.search(searchParams);

        Assertions.assertEquals(expectedContacts.size(), result.size());
        Assertions.assertEquals(expectedContacts, result);
    }
}
