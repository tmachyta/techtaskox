package ox.techtaskoxcompany.service.client;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ox.techtaskoxcompany.dto.client.ClientDto;
import ox.techtaskoxcompany.dto.client.ClientSearchParametersDto;
import ox.techtaskoxcompany.dto.client.CreateClientRequestDto;
import ox.techtaskoxcompany.exception.EntityNotFoundException;
import ox.techtaskoxcompany.mapper.client.ClientMapper;
import ox.techtaskoxcompany.model.Client;
import ox.techtaskoxcompany.repository.client.ClientRepository;
import ox.techtaskoxcompany.repository.client.ClientSpecificationBuilder;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final ClientSpecificationBuilder clientSpecificationBuilder;

    @Override
    public ClientDto save(CreateClientRequestDto requestDto) {
        Client client = clientMapper.toModel(requestDto);
        return clientMapper.toDto(clientRepository.save(client));
    }

    @Override
    public List<ClientDto> getAll(Pageable pageable) {
        return clientRepository.findAll(pageable)
                .stream()
                .map(clientMapper::toDto)
                .toList();
    }

    @Override
    public ClientDto findById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find client by id " + id));
        return clientMapper.toDto(client);
    }

    @Override
    public void deleteById(Long id) {
        clientRepository.deleteById(id);
    }

    @Override
    public ClientDto updateById(Long id, CreateClientRequestDto requestDto) {
        Client existedClient = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find client by id " + id));
        existedClient.setName(requestDto.getName());
        existedClient.setAddress(requestDto.getAddress());
        existedClient.setField(requestDto.getField());
        return clientMapper.toDto(clientRepository.save(existedClient));
    }

    @Override
    public List<ClientDto> search(ClientSearchParametersDto params) {
        Specification<Client> clientSpecification = clientSpecificationBuilder.build(params);
        return clientRepository.findAll(clientSpecification)
                .stream()
                .map(clientMapper::toDto)
                .toList();
    }
}
