import EmailList from "../components/EmailList";

const Starred = ({emails, pagination , setFolders,folders}) => {

  // Filter emails to only include those in the "inbox" folder
  const starredEmails = emails

  return (
    <div className="text-white">
      <EmailList emails={starredEmails} emailsPerPage={pagination}  setFolders={setFolders}  folders = {folders}></EmailList>
    </div>
  );
  };
  
  export default Starred;