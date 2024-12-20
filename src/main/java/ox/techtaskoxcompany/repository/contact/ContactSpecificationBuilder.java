package ox.techtaskoxcompany.repository.contact;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ox.techtaskoxcompany.dto.contact.ContactSearchParametersDto;
import ox.techtaskoxcompany.model.Contact;
import ox.techtaskoxcompany.repository.SpecificationProviderManager;

@Component
@RequiredArgsConstructor
public class ContactSpecificationBuilder implements SpecificationContactBuilder<Contact> {
    private final SpecificationProviderManager<Contact> contactSpecificationProviderManager;

    @Override
    public Specification<Contact> build(ContactSearchParametersDto searchParameters) {
        Specification<Contact> spec = Specification.where(null);
        if (searchParameters.names() != null && searchParameters.names().length > 0) {
            spec = spec.and(contactSpecificationProviderManager.getSpecificationProvider("name")
                    .getSpecification(searchParameters.names()));
        }

        if (searchParameters.lastNames() != null && searchParameters.lastNames().length > 0) {
            spec = spec
                    .and(contactSpecificationProviderManager.getSpecificationProvider("lastNames")
                    .getSpecification(searchParameters.lastNames()));
        }
        return spec;
    }
}
