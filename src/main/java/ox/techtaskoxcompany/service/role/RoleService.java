package ox.techtaskoxcompany.service.role;

import ox.techtaskoxcompany.model.Role;
import ox.techtaskoxcompany.model.Role.RoleName;

public interface RoleService {
    Role getRoleByRoleName(RoleName roleName);
}
