package email.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import email.backend.tables.Attachment;

@EnableJpaRepositories
@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long>{

}