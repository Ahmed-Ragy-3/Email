import React from 'react';
import { useNavigate } from 'react-router-dom';

function EmailList({ emails }) {
  const navigate = useNavigate();

  // Handle email click
  const handleEmailClick = (emailId) => {
    navigate(`/email/${emailId}`); // Navigate to full email view
  };

  return (
    <div>
      {emails.map((email, index) => (
        <div
          key={index}
          className='border-2 space-y-2 mb-4 rounded-2xl p-4'
          onClick={() => handleEmailClick(index)} // Pass the email id to the handler
        >
          <h3 className='text-xl'>{email.subject}</h3>
          <h3 className='text-sm !text-opacity-50 text-white'>{email.sender}</h3>
          <h3 className='overflow-clip'>{email.body}</h3>
          <h3 className='text-xs text-white text-opacity-30'>{email.date}</h3>
        </div>
      ))}
    </div>
  );
}

export default EmailList;
