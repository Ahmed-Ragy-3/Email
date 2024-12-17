import EmailList from "../components/EmailList";

const Starred = ({emails, pagination}) => {

  // Filter emails to only include those in the "inbox" folder
  const starredEmails = emails

  return (
    <div className="text-white">
      <EmailList emails={starredEmails} emailsPerPage={pagination}></EmailList>
    </div>
  );
  };
  
  export default Starred;