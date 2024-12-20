package ox.techtaskoxcompany.dto.contact;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateContactRequestDto {
    private String name;
    private String lastName;
    private String email;
    private String phone;
    private Long clientId;
}