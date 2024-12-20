package ox.techtaskoxcompany.repository.client.spec;

import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ox.techtaskoxcompany.model.Client;
import ox.techtaskoxcompany.repository.SpecificationProvider;

@Component
public class FieldSpecificationProvider implements SpecificationProvider<Client> {
    @Override
    public String getKey() {
        return "field";
    }

    @Override
    public Specification<Client> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) ->
                root.get("field").in(Arrays.stream(params).toArray());
    }
}
