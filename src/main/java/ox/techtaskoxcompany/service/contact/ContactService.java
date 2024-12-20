package ox.techtaskoxcompany.service.contact;

import java.util.List;
import org.springframework.data.domain.Pageable;
import ox.techtaskoxcompany.dto.contact.ContactDto;
import ox.techtaskoxcompany.dto.contact.ContactSearchParametersDto;
import ox.techtaskoxcompany.dto.contact.CreateContactRequestDto;

public interface ContactService {
    ContactDto save(CreateContactRequestDto requestDto);

    List<ContactDto> getAll(Pageable pageable);

    ContactDto findById(Long id);

    void deleteById(Long id);

    ContactDto updateById(Long id, CreateContactRequestDto requestDto);

    List<ContactDto> search(ContactSearchParametersDto params);

    ContactDto updateClientById(Long id, CreateContactRequestDto requestDto);
}
