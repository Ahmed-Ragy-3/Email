import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

function EmailList({ emails , emailsPerPage}) {
  const navigate = useNavigate();
  const [currentPage, setCurrentPage] = useState(1);
  

  // Calculate total pages
  let totalPages = Math.ceil(emails.length / emailsPerPage);
  if (emails.length == 0)
  {
    totalPages =1
  }
  // Get emails for the current page
  const paginatedEmails = emails.slice(
    (currentPage - 1) * emailsPerPage,
    currentPage * emailsPerPage
  );

  // Handle email click
  const handleEmailClick = (emailId) => {
    navigate(`/email/${emailId}`); // Navigate to full email view
  };

  // Handle page change
  const handlePageChange = (newPage) => {
    setCurrentPage(newPage);
  };

  return (
    <div>
      {paginatedEmails.map((email) => (
        <div
          key={email.id}
          className="border-2 space-y-2 mb-4 rounded-2xl p-4 hover:cursor-pointer active:scale-95"
          onClick={() => handleEmailClick(email.id)}
        >
          <h3 className="text-xl">{email.subject}</h3>
          <h3 className="text-sm !text-opacity-50 text-white">{email.senderAddress}</h3>
          <div className="overflow-clip text-2xl" dangerouslySetInnerHTML={{ __html: email.content }} ></div>
          <h3 className="text-xs text-white text-opacity-30">{email.dateString}</h3>
        </div>
      ))}

      {/* Pagination Controls */}
      <div className="flex justify-between items-center mt-4">
        <button
          disabled={currentPage === 1}
          onClick={() => handlePageChange(currentPage - 1)}
          className={`px-4 py-2 rounded-lg ${currentPage === 1 ? 'opacity-50 cursor-not-allowed' : 'hover:bg-gray-800'}`}
        >
          Previous
        </button>
        <span className="text-white">{`Page ${currentPage} of ${totalPages}`}</span>
        <button
          disabled={currentPage === totalPages}
          onClick={() => handlePageChange(currentPage + 1)}
          className={`px-4 py-2 rounded-lg ${currentPage === totalPages ? 'opacity-50 cursor-not-allowed' : 'hover:bg-gray-800'}`}
        >
          Next
        </button>
      </div>
    </div>
  );
}

export default EmailList;
