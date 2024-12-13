import EmailList from "../components/EmailList";

const Starred = ({emails}) => {

  // Filter emails to only include those in the "inbox" folder
  const starredEmails = emails.filter(email => email.folder === "starred");

  return (
    <div className="text-white">
      <EmailList emails={starredEmails}></EmailList>
    </div>
  );
  };
  
  export default Starred;