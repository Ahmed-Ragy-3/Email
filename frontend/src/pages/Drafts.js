import EmailList from "../components/EmailList";

const Drafts = ({emails , pagination}) => {
  // Filter emails to only include those in the "inbox" folder
  const draftEmails = emails

  return (
    <div className="text-white">
      <EmailList emails = {draftEmails}  emailsPerPage={pagination}></EmailList>
    </div>
  );
  };
  
  export default Drafts;