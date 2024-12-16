package email.backend.services;


import email.backend.databaseAccess.MailRepository;
import email.backend.databaseAccess.MailboxRepository;
import email.backend.databaseAccess.UserRepository;
// import email.backend.tables.Mail;

public class services {
   
   UserRepository userRepository;
   
   MailRepository mailRepository;
   
   MailboxRepository mailboxRepository;

   // fucntions to get user Id from token

   // functions to validate given data
   
   // user 
   public void createUser () {
      // takes data 

      // validate data
      // get given data and create a new user with it
      // save new user in data base
      // put user in cache

      // return new user
   }

   public void requestUser() {
      // takes data 

      // validate given data
      // get the user from database
      // put the user in cache

      // return requested user
   }

   // mailbox
   public void getMailboxes() {
      // takes user 

      // mailboxRepository.findByUser(user);
      // put mailboxes in cache
      
      // we should return a list containing only names of the mailboxes
   }

   public void getMailsOfMailbox() {
      // takes Mailbox name, user

      // we have the list of mailboxes in cache so we can use name to get mailbox id
      // mailRepository.findById(mailboxId);

   }

   public void createMailbox() {
      // takes user , mailbox name

      // validate name
      // create a new mailbox with given name and user
   }
   
   public void copyTo() {
      // take mail, destination mailbox

      // add mail to destination mailbox in database
   }

   public void moveTo() {
      // take mail, current mailbox and destination mailbox

      // add mail to destination mailbox in database
      // remove mail from current mailbox in database
   }
   
   public void moveTotrash() {

   }

   // mail
   public void createMail () {
      // takes user
      
      // create a new mail
      // set the sender 
      // mailRepository.save(mail);
      
      // return created mail
   }

   public void editMail() {
      // take mail

      // add recievers, subject, content, date
      // update mail in databae

      // return updated mail
   }

   public void addAttachment() {
      // take mail

      // validate attachment
      // add attachment 
      // update mail in database

      // return updated mail
   }

   public void removeAttachment() {
      // take mail
      
      // remove attachment 
      // update attachment in database

      // return updated mail
   }

   public void createReply() {
      // takes mail

      // create new mail and set its parent, sender 
      // add the created mail to replies of parent mail

      // return created reply
   }

   // contact 
   public void newContact() {
      // takes user, contact emailAddress, Contact name

      // validate emailAddress
      // create new contact
   }

   public void getContacts() {
      // takes user

      // contactRepository.findByUser();

      // return list of contacts
   }

   public void renameContact() {
      // take contact , new name

      // update contact name in database
   }

   public void deleteContact() {
      // take contact

      // delete contact from database
   }

}
