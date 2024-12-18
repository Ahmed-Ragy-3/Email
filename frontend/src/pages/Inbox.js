import React from "react";
import EmailList from "../components/EmailList";

const Inbox = ({ emails , pagination , setFolders,folders}) => {

  // Filter emails to only include those in the "inbox" folder
  const inboxEmails = emails

  return (
    <div className="text-white">
      <EmailList emails = {inboxEmails} emailsPerPage={pagination} setFolders={setFolders}  folders = {folders}/>
    </div>
  );
};

export default Inbox;
