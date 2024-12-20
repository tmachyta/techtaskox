package ox.techtaskoxcompany.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ox.techtaskoxcompany.dto.client.ClientDto;
import ox.techtaskoxcompany.dto.client.ClientSearchParametersDto;
import ox.techtaskoxcompany.dto.client.CreateClientRequestDto;
import ox.techtaskoxcompany.service.client.ClientService;

@Tag(name = "Clients management", description = "Endpoints for managing clients")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/clients")
public class ClientController {
    private final ClientService clientService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @Operation(summary = "Get all clients", description = "Get a list of all available videos")
    public List<ClientDto> findAll(@ParameterObject Pageable pageable) {
        return clientService.getAll(pageable);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    @Operation(summary = "Get client by id", description = "Get available client by id")
    public ClientDto getClientById(@PathVariable Long id) {
        return clientService.findById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Save client to repository",
            description = "Save valid client to repository")
    public ClientDto createClient(@RequestBody @Valid CreateClientRequestDto requestDto) {
        return clientService.save(requestDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete client by id",
            description = "Soft delete of available client by id")
    public void deleteById(@PathVariable Long id) {
        clientService.deleteById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update client by id", description = "Update available client by id")
    public ClientDto updateById(@PathVariable Long id,
                                @RequestBody @Valid CreateClientRequestDto requestDto) {
        return clientService.updateById(id, requestDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    @Operation(summary = "Search clients", description = "Search clients by available parameters")
    public List<ClientDto> searchClients(ClientSearchParametersDto searchParameters) {
        return clientService.search(searchParameters);
    }
}
