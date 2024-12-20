package ox.techtaskoxcompany.repository.client.spec;

import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ox.techtaskoxcompany.model.Client;
import ox.techtaskoxcompany.repository.SpecificationProvider;

@Component
public class NameSpecificationProvider implements SpecificationProvider<Client> {
    @Override
    public String getKey() {
        return "name";
    }

    @Override
    public Specification<Client> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) ->
                root.get("name").in(Arrays.stream(params).toArray());
    }
}
