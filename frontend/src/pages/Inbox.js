import React from "react";
import EmailList from "../components/EmailList";

const Inbox = ({ emails , pagination}) => {

  // Filter emails to only include those in the "inbox" folder
  const inboxEmails = emails

  return (
    <div className="text-white">
      <EmailList emails = {inboxEmails} emailsPerPage={pagination}/>
    </div>
  );
};

export default Inbox;
