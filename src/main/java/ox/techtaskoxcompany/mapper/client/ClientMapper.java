package ox.techtaskoxcompany.mapper.client;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ox.techtaskoxcompany.config.MapperConfig;
import ox.techtaskoxcompany.dto.client.ClientDto;
import ox.techtaskoxcompany.dto.client.CreateClientRequestDto;
import ox.techtaskoxcompany.model.Client;

@Mapper(config = MapperConfig.class)
public interface ClientMapper {
    ClientDto toDto(Client client);

    @Mapping(target = "id", ignore = true)
    Client toModel(CreateClientRequestDto request);
}
