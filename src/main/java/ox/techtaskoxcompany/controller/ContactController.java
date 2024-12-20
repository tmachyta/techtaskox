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
import ox.techtaskoxcompany.dto.contact.ContactDto;
import ox.techtaskoxcompany.dto.contact.ContactSearchParametersDto;
import ox.techtaskoxcompany.dto.contact.CreateContactRequestDto;
import ox.techtaskoxcompany.service.contact.ContactService;

@Tag(name = "Contacts management", description = "Endpoints for managing contacts")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/contacts")
public class ContactController {
    private final ContactService contactService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @Operation(summary = "Get all contacts", description = "Get a list of all available contacts")
    public List<ContactDto> findAll(@ParameterObject Pageable pageable) {
        return contactService.getAll(pageable);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    @Operation(summary = "Get contact by id", description = "Get available contact by id")
    public ContactDto getContactById(@PathVariable Long id) {
        return contactService.findById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Save contact to repository",
            description = "Save valid contact to repository")
    public ContactDto createContact(@RequestBody @Valid CreateContactRequestDto requestDto) {
        return contactService.save(requestDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete contact by id",
            description = "Soft delete of available contact by id")
    public void deleteById(@PathVariable Long id) {
        contactService.deleteById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @Operation(summary = "Update contact by id", description = "Update available contact by id")
    public ContactDto updateById(@PathVariable Long id,
                                @RequestBody @Valid CreateContactRequestDto requestDto) {
        return contactService.updateById(id, requestDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    @Operation(summary = "Search contacts", description = "Search contacts by available parameters")
    public List<ContactDto> searchClients(ContactSearchParametersDto searchParameters) {
        return contactService.search(searchParameters);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/client/{id}")
    @Operation(summary = "Update client by id", description = "Update available client by id")
    public ContactDto updateClientById(@PathVariable Long id,
                                       @RequestBody @Valid CreateContactRequestDto requestDto) {
        return contactService.updateClientById(id, requestDto);
    }
}
