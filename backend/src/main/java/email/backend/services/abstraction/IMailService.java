package email.backend.services.abstraction;

import email.backend.DTO.AttachedMailDTO;
import email.backend.DTO.MailboxDTO;
import email.backend.tables.Mail;
import email.backend.tables.User;
import email.backend.tables.Mailbox;
import email.backend.tables.Attachment;
import email.backend.services.Importance;

import java.util.List;

public interface IMailService {

    void moveMailsToTrash(Mailbox mailbox, List<Long> ids, User user) throws IllegalArgumentException;

    void moveMailsToMailbox(Mailbox from, Mailbox to, List<Long> mailIds, User user);

    AttachedMailDTO createDraftMail(AttachedMailDTO attachedMailDto, User user);

    AttachedMailDTO editDraftMail(AttachedMailDTO attachedMailDto, User user) throws IllegalArgumentException;

    List<MailboxDTO> getAllMails(User user);

    Mail getMailById(Long mailId);

    List<Mail> getEmailsInMailbox(User user, String mailboxName);

    List<Mail> getMailsBySenderId(Long senderId);

    void deleteTrashMails();

    Importance getImportanceFromString(String importance);

    void sendToDatabase(Mail mail);

    Mail setAttachments(Mail mail, List<Attachment> attachments);
}
