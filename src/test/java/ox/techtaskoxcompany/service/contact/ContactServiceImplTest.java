package ox.techtaskoxcompany.service.contact;

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
import ox.techtaskoxcompany.dto.contact.ContactDto;
import ox.techtaskoxcompany.dto.contact.ContactSearchParametersDto;
import ox.techtaskoxcompany.dto.contact.CreateContactRequestDto;
import ox.techtaskoxcompany.exception.EntityNotFoundException;
import ox.techtaskoxcompany.mapper.contact.ContactMapper;
import ox.techtaskoxcompany.model.Client;
import ox.techtaskoxcompany.model.Contact;
import ox.techtaskoxcompany.repository.client.ClientRepository;
import ox.techtaskoxcompany.repository.contact.ContactRepository;
import ox.techtaskoxcompany.repository.contact.ContactSpecificationBuilder;

@ExtendWith(MockitoExtension.class)
class ContactServiceImplTest {
    private static final Long CONTACT_ID = 1L;
    private static final Long CLIENT_ID = 2L;
    private static final Long NON_EXISTED_CONTACT_ID = 50L;
    private static final String OLD_NAME = "Old";
    private static final String NEW_NAME = "Updated";
    private static final String LAST_NAME = "Lastname";

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private ContactMapper contactMapper;

    @Mock
    private ContactSpecificationBuilder contactSpecificationBuilder;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ContactServiceImpl contactService;

    @Test
    public void testSuccessfullySaveContact() {
        CreateContactRequestDto requestDto = new CreateContactRequestDto();
        requestDto.setClientId(CLIENT_ID);

        Client client = new Client();
        Contact contactToSave = new Contact();
        client.setId(CLIENT_ID);

        when(clientRepository.findById(CLIENT_ID)).thenReturn(Optional.of(client));

        when(contactMapper.toModel(requestDto)).thenReturn(contactToSave);

        contactToSave.setClient(client);

        when(contactRepository.save(contactToSave)).thenReturn(contactToSave);

        ContactDto contactDto = new ContactDto();

        when(contactMapper.toDto(contactToSave)).thenReturn(contactDto);

        ContactDto result = contactService.save(requestDto);

        assertNotNull(result);
    }

    @Test
    public void testGetAllContacts() {
        Contact contact = new Contact();
        Pageable pageable = PageRequest.of(0, 10);
        List<Contact> contacts = List.of(new Contact());
        List<ContactDto> expectedContacts = List.of(new ContactDto());
        Page<Contact> page =
                new PageImpl<>(contacts, pageable, contacts.size());

        when(contactRepository.findAll(pageable)).thenReturn(page);

        when(contactMapper.toDto(contact)).thenReturn(new ContactDto());

        List<ContactDto> result = contactService.getAll(pageable);

        Assertions.assertEquals(expectedContacts.size(), result.size());
    }

    @Test
    public void testGetContactById() {
        Contact contact = new Contact();
        contact.setId(CONTACT_ID);
        ContactDto contactDto = new ContactDto();
        contactDto.setId(CONTACT_ID);

        when(contactRepository.findById(contact.getId()))
                .thenReturn(Optional.of(contact));

        when(contactMapper.toDto(contact)).thenReturn(contactDto);

        ContactDto result = contactService.findById(CONTACT_ID);

        Assertions.assertEquals(contact.getId(), result.getId());
    }

    @Test
    public void testFindContactWithNonExistedId() {
        when(contactRepository.findById(NON_EXISTED_CONTACT_ID))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> contactService.findById(NON_EXISTED_CONTACT_ID));
    }

    @Test
    public void testDeleteContactById() {
        contactService.deleteById(CONTACT_ID);

        when(contactRepository.findById(CONTACT_ID))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> contactService.findById(CONTACT_ID));
    }

    @Test
    public void testUpdateContactSuccessfully() {
        CreateContactRequestDto requestDto = new CreateContactRequestDto();
        requestDto.setName(NEW_NAME);

        ContactDto expectedResult = new ContactDto();
        expectedResult.setName(NEW_NAME);

        Contact contact = new Contact();
        contact.setName(OLD_NAME);

        when(contactRepository.findById(CONTACT_ID))
                .thenReturn(Optional.of(contact));

        when(contactRepository.save(contact)).thenReturn(contact);

        when(contactMapper.toDto(contact)).thenReturn(expectedResult);

        ContactDto updatedClient =
                contactService.updateById(CONTACT_ID, requestDto);

        Assertions.assertEquals(updatedClient.getName(), expectedResult.getName());
    }

    @Test
    public void testSearchCOntact() {
        String[] names = {OLD_NAME};
        String[] lastNames = {LAST_NAME};
        ContactSearchParametersDto searchParams = new ContactSearchParametersDto(names, lastNames);

        Specification<Contact> specification = mock(Specification.class);
        when(contactSpecificationBuilder.build(searchParams)).thenReturn(specification);

        Contact contact = new Contact();
        ContactDto contactDto = new ContactDto();
        List<Contact> contacts = List.of(contact);
        List<ContactDto> expectedContacts = List.of(contactDto);

        when(contactRepository.findAll(specification)).thenReturn(contacts);
        when(contactMapper.toDto(contact)).thenReturn(contactDto);

        List<ContactDto> result = contactService.search(searchParams);

        Assertions.assertEquals(expectedContacts.size(), result.size());
        Assertions.assertEquals(expectedContacts, result);
    }

    @Test
    public void testUpdateClientId() {
        CreateContactRequestDto requestDto = new CreateContactRequestDto();
        requestDto.setClientId(CLIENT_ID);

        Contact contact = new Contact();
        contact.setId(CONTACT_ID);

        Client client = new Client();
        client.setId(CLIENT_ID);

        when(contactRepository.findById(CONTACT_ID)).thenReturn(Optional.of(contact));

        when(clientRepository.findById(CLIENT_ID)).thenReturn(Optional.of(client));
        contact.setClient(client);

        when(contactRepository.save(contact)).thenReturn(contact);

        ContactDto expectedResult = new ContactDto();
        expectedResult.setId(CONTACT_ID);
        expectedResult.setClientId(CLIENT_ID);
        when(contactMapper.toDto(contact)).thenReturn(expectedResult);

        ContactDto updatedContact = contactService.updateClientById(CONTACT_ID, requestDto);

        Assertions.assertEquals(updatedContact.getClientId(), expectedResult.getClientId());
    }
}
