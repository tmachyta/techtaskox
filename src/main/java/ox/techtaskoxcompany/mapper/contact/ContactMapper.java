package ox.techtaskoxcompany.mapper.contact;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ox.techtaskoxcompany.config.MapperConfig;
import ox.techtaskoxcompany.dto.contact.ContactDto;
import ox.techtaskoxcompany.dto.contact.CreateContactRequestDto;
import ox.techtaskoxcompany.model.Contact;

@Mapper(config = MapperConfig.class)
public interface ContactMapper {
    @Mapping(source = "client.id", target = "clientId")
    ContactDto toDto(Contact client);

    @Mapping(target = "id", ignore = true)
    Contact toModel(CreateContactRequestDto request);
}
