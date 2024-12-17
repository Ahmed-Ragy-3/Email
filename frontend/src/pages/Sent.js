import EmailList from "../components/EmailList";

const Sent = ({emails , pagination}) => {

  const sentEmails = emails

  return (
    <div className="text-white">
      <EmailList emails={sentEmails} emailsPerPage={pagination}></EmailList>
    </div>
  );
  };
  
  export default Sent;