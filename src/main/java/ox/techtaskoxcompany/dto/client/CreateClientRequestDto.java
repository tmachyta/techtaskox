package ox.techtaskoxcompany.dto.client;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CreateClientRequestDto {
    private String name;
    private String field;
    private String address;
}
