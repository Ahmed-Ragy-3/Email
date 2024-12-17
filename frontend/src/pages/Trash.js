import EmailList from "../components/EmailList";

const Trash = ({emails, pagination}) => {

  const trashEmails = emails

  return (
    <div className="text-white">
      <EmailList emails={trashEmails}  emailsPerPage={pagination}></EmailList>
    </div>
  );
  };
  
  export default Trash;