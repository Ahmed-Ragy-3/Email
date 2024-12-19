import EmailList from "../components/EmailList";

const Sent = ({emails , pagination , setFolders,folders}) => {

  const sentEmails = emails

  return (
    <div className="text-white">
      <EmailList emails={sentEmails} emailsPerPage={pagination}  setFolders={setFolders}  folders = {folders}></EmailList>
    </div>
  );
  };
  
  export default Sent;