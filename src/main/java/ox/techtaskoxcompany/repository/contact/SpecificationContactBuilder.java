package ox.techtaskoxcompany.repository.contact;

import org.springframework.data.jpa.domain.Specification;
import ox.techtaskoxcompany.dto.contact.ContactSearchParametersDto;

public interface SpecificationContactBuilder<T> {
    Specification<T> build(ContactSearchParametersDto searchParameters);
}
