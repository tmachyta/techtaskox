package ox.techtaskoxcompany.service.contact;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
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
import ox.techtaskoxcompany.service.client.ClientServiceImpl;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {
    private final ContactRepository contactRepository;
    private final ContactMapper contactMapper;
    private final ClientRepository clientRepository;
    private final ContactSpecificationBuilder contactSpecificationBuilder;
    private static final Logger logger = LoggerFactory.getLogger(ContactServiceImpl.class);

    @Override
    public ContactDto save(CreateContactRequestDto requestDto) {
        Client client = clientRepository.findById(requestDto.getClientId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find client by id "
                        + requestDto.getClientId()));
        logger.debug("Found client: {}", client);
        Contact contact = contactMapper.toModel(requestDto);
        contact.setClient(client);
        logger.info("Saving new contact with data: {}", requestDto);
        return contactMapper.toDto(contactRepository.save(contact));
    }

    @Override
    public List<ContactDto> getAll(Pageable pageable) {
        logger.info("Getting list of contacts");
        return contactRepository.findAll(pageable)
                .stream()
                .map(contactMapper::toDto)
                .toList();
    }

    @Override
    public ContactDto findById(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find contact by id " + id));
        logger.debug("Found contact: {}", contact);
        return contactMapper.toDto(contact);
    }

    @Override
    public void deleteById(Long id) {
        logger.info("Delete contact by id");
        contactRepository.deleteById(id);
    }

    @Override
    public ContactDto updateById(Long id, CreateContactRequestDto requestDto) {
        Contact existedContact = contactRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find contact by id " + id));
        logger.debug("Found contact: {}", existedContact);
        existedContact.setName(requestDto.getName());
        existedContact.setLastName(requestDto.getLastName());
        existedContact.setEmail(requestDto.getEmail());
        existedContact.setPhone(requestDto.getPhone());
        logger.info("Saving updated contact with data: {}", existedContact);
        return contactMapper.toDto(contactRepository.save(existedContact));
    }

    @Override
    public List<ContactDto> search(ContactSearchParametersDto params) {
        Specification<Contact> contactSpecification = contactSpecificationBuilder.build(params);
        logger.info("Getting list contact of params: {}", contactSpecification);
        return contactRepository.findAll(contactSpecification)
                .stream()
                .map(contactMapper::toDto)
                .toList();
    }

    @Override
    public ContactDto updateClientById(Long id, CreateContactRequestDto requestDto) {
        Contact existedContact = contactRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find contact by id " + id));
        logger.debug("Found contact: {}", existedContact);

        Client client = clientRepository.findById(requestDto.getClientId())
                .orElseThrow(() -> new EntityNotFoundException("Can't find client by id "
                        + requestDto.getClientId()));
        logger.debug("Found client: {}", client);

        existedContact.setClient(client);
        return contactMapper.toDto(contactRepository.save(existedContact));
    }
}
