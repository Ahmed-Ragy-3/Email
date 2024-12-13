import React from "react";
import EmailList from "../components/EmailList";

const Inbox = ({ emails }) => {

  // Filter emails to only include those in the "inbox" folder
  const inboxEmails = emails.filter(email => email.folder === "inbox");

  return (
    <div className="text-white">
      <EmailList emails = {inboxEmails}/>
    </div>
  );
};

export default Inbox;
