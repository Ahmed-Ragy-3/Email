package email.backend.services.abstraction;

import email.backend.DTO.MailboxDTO;
import email.backend.services.Date;
import email.backend.services.Importance;
import email.backend.tables.Mail;
import email.backend.tables.Mailbox;
import email.backend.tables.User;
import java.util.ArrayList;
import java.util.List;

public interface IMailboxService {

    void addTo(Mailbox mailbox, Mail mail);

    void copyTo(Mailbox mailbox, Mail mail);

    void moveTo(Mailbox from, Mailbox to, Mail mail, User user) throws IllegalArgumentException;

    void moveMailsToMailbox(Mailbox from, Mailbox to, ArrayList<Long> mailIds, User user);

    void deleteFrom(Mailbox mailbox, Mail mail);

    void moveToTrash(Mailbox mailbox, Mail mail, User user) throws IllegalArgumentException;

    void moveMailsToTrash(Mailbox mailbox, List<Long> ids, User user) throws IllegalArgumentException;

    void deleteFromTrash(Mail mail, User user) throws IllegalArgumentException;

    Mailbox getMailbox(Long mailboxId);

    Mailbox getMailbox(User user, String name);

    Mailbox getMailbox(User user, int index) throws IllegalArgumentException;

    Mailbox createMailbox(User user, String name) throws IllegalArgumentException;

    Mailbox editMailbox(User user, MailboxDTO mailboxDto) throws IllegalArgumentException;

    void deleteMailbox(User user, MailboxDTO mailboxDto) throws IllegalArgumentException;

    List<Mail> filter(Long mailboxId, Importance importance, Boolean attachment, Date date1, Date date2);

    List<Mail> sortBy(List<Mail> mails, String sortKey);
}
