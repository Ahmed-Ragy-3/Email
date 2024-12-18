import EmailList from "../components/EmailList";

const Spam = ({emails, pagination}) => {

  // Filter emails to only include those in the "inbox" folder
  const spamEmails = emails


  return (
    <div className="text-white">
      <EmailList emails={spamEmails} emailsPerPage={pagination}></EmailList>
    </div>
  );
  };
  
  export default Spam;