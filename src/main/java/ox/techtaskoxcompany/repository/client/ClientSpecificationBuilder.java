package ox.techtaskoxcompany.repository.client;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ox.techtaskoxcompany.dto.client.ClientSearchParametersDto;
import ox.techtaskoxcompany.model.Client;
import ox.techtaskoxcompany.repository.SpecificationProviderManager;

@Component
@RequiredArgsConstructor
public class ClientSpecificationBuilder implements SpecificationClientBuilder<Client> {
    private final SpecificationProviderManager<Client> clientSpecificationProviderManager;

    @Override
    public Specification<Client> build(ClientSearchParametersDto searchParameters) {
        Specification<Client> spec = Specification.where(null);
        if (searchParameters.names() != null && searchParameters.names().length > 0) {
            spec = spec.and(clientSpecificationProviderManager.getSpecificationProvider("name")
                    .getSpecification(searchParameters.names()));
        }

        if (searchParameters.fields() != null && searchParameters.fields().length > 0) {
            spec = spec.and(clientSpecificationProviderManager.getSpecificationProvider("field")
                    .getSpecification(searchParameters.fields()));
        }
        return spec;
    }
}
