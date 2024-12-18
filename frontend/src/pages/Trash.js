import EmailList from "../components/EmailList";

const Trash = ({emails, pagination , setFolders,folders}) => {

  const trashEmails = emails

  return (
    <div className="text-white">
      <EmailList emails={trashEmails}  emailsPerPage={pagination}  setFolders={setFolders}  folders = {folders}></EmailList>
    </div>
  );
  };
  
  export default Trash;