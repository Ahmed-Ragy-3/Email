package email.backend.DTO;

import email.backend.tables.Attachment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.ArrayList;


@Setter
@Getter
@AllArgsConstructor
public class MailDTO {

   private String subject;
   private String content;
   private String senderAddress;
   private String importance;
   private String date;
   private List<Attachment> attachments = new ArrayList<>();
   private List<String> receiversAdresses = new ArrayList<>();

   // @JsonIgnore
   // public void addAttachment(Attachment attachment){
   //    attachments.add(attachment);
   // }

   // @JsonIgnore
   // public void addReceiver(String receiverAdresses){
   //    receiversAdresses.add(receiverAdresses);
   // }

}
