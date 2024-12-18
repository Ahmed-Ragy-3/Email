import { useState } from 'react';
import EmailList from './EmailList';

function ParentComponent() {
  const [starredEmails, setStarredEmails] = useState(new Set());

  const emails = [
    { id: 1, subject: 'Subject 1', senderAddress: 'sender1@example.com', content: 'Content 1', dateString: '2024-12-18' },
    { id: 2, subject: 'Subject 2', senderAddress: 'sender2@example.com', content: 'Content 2', dateString: '2024-12-19' },
    // Add more emails as needed
  ];

  return (
    <EmailList
      emails={emails}
      emailsPerPage={5}
      starredEmails={starredEmails}
      setStarredEmails={setStarredEmails}
    />
  );
}

export default ParentComponent;
