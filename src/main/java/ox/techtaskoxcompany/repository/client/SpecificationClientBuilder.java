package ox.techtaskoxcompany.repository.client;

import org.springframework.data.jpa.domain.Specification;
import ox.techtaskoxcompany.dto.client.ClientSearchParametersDto;

public interface SpecificationClientBuilder<T> {
    Specification<T> build(ClientSearchParametersDto searchParameters);
}
