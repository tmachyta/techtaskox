package ox.techtaskoxcompany.repository.contact;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ox.techtaskoxcompany.model.Contact;
import ox.techtaskoxcompany.repository.SpecificationProvider;
import ox.techtaskoxcompany.repository.SpecificationProviderManager;

@Component
@RequiredArgsConstructor
public class ContactSpecificationProviderManager implements SpecificationProviderManager<Contact> {
    private final List<SpecificationProvider<Contact>> contactSpecificationProviders;

    @Override
    public SpecificationProvider<Contact> getSpecificationProvider(String key) {
        return contactSpecificationProviders.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException(
                                "Can't find correct specification provider for key " + key));
    }
}
