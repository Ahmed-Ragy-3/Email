import EmailList from "../components/EmailList";

const Drafts = ({emails}) => {
  // Filter emails to only include those in the "inbox" folder
  const draftEmails = emails.filter(email => email.folder === "drafts");

  return (
    <div className="text-white">
      <EmailList emails = {draftEmails}></EmailList>
    </div>
  );
  };
  
  export default Drafts;