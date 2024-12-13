import EmailList from "../components/EmailList";

const Trash = ({emails}) => {

  const trashEmails = emails.filter(email => email.folder === "trash");

  return (
    <div className="text-white">
      <EmailList emails={trashEmails}></EmailList>
    </div>
  );
  };
  
  export default Trash;