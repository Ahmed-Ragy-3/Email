import EmailList from "../components/EmailList";

const Spam = ({emails}) => {

  // Filter emails to only include those in the "inbox" folder
  const spamEmails = emails.filter(email => email.folder === "spam");

  return (
    <div className="text-white">
      <EmailList emails={spamEmails}></EmailList>
    </div>
  );
  };
  
  export default Spam;