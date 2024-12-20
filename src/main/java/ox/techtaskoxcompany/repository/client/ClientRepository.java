package ox.techtaskoxcompany.repository.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ox.techtaskoxcompany.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>,
        JpaSpecificationExecutor<Client> {
}
