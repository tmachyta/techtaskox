package ox.techtaskoxcompany.repository.client;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ox.techtaskoxcompany.model.Client;
import ox.techtaskoxcompany.repository.SpecificationProvider;
import ox.techtaskoxcompany.repository.SpecificationProviderManager;

@Component
@RequiredArgsConstructor
public class ClientSpecificationProviderManager implements SpecificationProviderManager<Client> {
    private final List<SpecificationProvider<Client>> clientSpecificationProviders;

    @Override
    public SpecificationProvider<Client> getSpecificationProvider(String key) {
        return clientSpecificationProviders.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException(
                                "Can't find correct specification provider for key " + key));
    }
}
