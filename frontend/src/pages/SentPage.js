import EmailList from "../components/EmailList";

const Sent = ({emails}) => {

  const sentEmails = emails.filter(email => email.folder === "sent");

  return (
    <div className="text-white">
      <EmailList emails={sentEmails}></EmailList>
    </div>
  );
  };
  
  export default Sent;