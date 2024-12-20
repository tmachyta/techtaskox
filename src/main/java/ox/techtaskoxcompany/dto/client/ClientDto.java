package ox.techtaskoxcompany.dto.client;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ClientDto {
    private Long id;
    private String name;
    private String field;
    private String address;
}
