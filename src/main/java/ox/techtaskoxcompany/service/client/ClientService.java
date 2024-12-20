package ox.techtaskoxcompany.service.client;

import java.util.List;
import org.springframework.data.domain.Pageable;
import ox.techtaskoxcompany.dto.client.ClientDto;
import ox.techtaskoxcompany.dto.client.ClientSearchParametersDto;
import ox.techtaskoxcompany.dto.client.CreateClientRequestDto;

public interface ClientService {
    ClientDto save(CreateClientRequestDto requestDto);

    List<ClientDto> getAll(Pageable pageable);

    ClientDto findById(Long id);

    void deleteById(Long id);

    ClientDto updateById(Long id, CreateClientRequestDto requestDto);

    List<ClientDto> search(ClientSearchParametersDto params);
}
