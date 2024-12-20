package ox.techtaskoxcompany.repository.contact.spec;

import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ox.techtaskoxcompany.model.Contact;
import ox.techtaskoxcompany.repository.SpecificationProvider;

@Component
public class LastNameSpecificationProvider implements SpecificationProvider<Contact> {
    @Override
    public String getKey() {
        return "lastName";
    }

    @Override
    public Specification<Contact> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) ->
                root.get("lastName").in(Arrays.stream(params).toArray());
    }
}
