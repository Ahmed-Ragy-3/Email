1 - controllers
2 - contacts table
3 - trash restore
4 - scheduled mails handling
5 - caching proxy
6 - Attachments
7 - pagination
8 - web sockets

user:
   id 
   name
   password
   email address
   list of contacts
   list of mailboxes

mail:
   id
   subject
   content
   date
   list of malboxes
   importance
   list of attachments
   list of receivers

mailbox:
   id
   name
   user
   list of mails

contact:
   id
   contact name
   user
   contact

attachment:
   id
   name
   data
   mail



Used design pattern:
   Adapter (dto)
   Fascade (dto)
   Proxy
   Filter   dp
   Factory
   Strategy

   Builder     (spring)
   Singleton   (spring)
