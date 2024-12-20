package ox.techtaskoxcompany.repository.contact;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ox.techtaskoxcompany.model.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact,
        Long>, JpaSpecificationExecutor<Contact> {
}
