
package email.backend.databaseAccess;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import email.backend.tables.Attachment;

@EnableJpaRepositories
public interface AttachmentRepository extends JpaRepository<Attachment, Long>{

}