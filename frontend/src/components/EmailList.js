import { useState } from 'react';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faStar } from '@fortawesome/free-solid-svg-icons'; // Solid star icon
import { faStar as faRegStar } from '@fortawesome/free-regular-svg-icons'; // Regular star icon
import { useNavigate } from 'react-router-dom';

function EmailList({ emails, emailsPerPage }) {
  const navigate = useNavigate();
  const [currentPage, setCurrentPage] = useState(1);
  
  // State to track starred emails locally
  const [starredEmailIds, setStarredEmailIds] = useState(new Set());

  const handleStarClick = (emailId, event) => {
    event.stopPropagation(); // Prevent email navigation when star is clicked
    setStarredEmailIds((prev) => {
      const newStarredEmails = new Set(prev);
      if (newStarredEmails.has(emailId)) {
        newStarredEmails.delete(emailId); // Unstar the email
      } else {
        newStarredEmails.add(emailId); // Star the email
      }
      return newStarredEmails;
    });
  };

  const totalPages = Math.ceil(emails.length / emailsPerPage);
  const paginatedEmails = emails.slice((currentPage - 1) * emailsPerPage, currentPage * emailsPerPage);

  return (
    <div>
      {paginatedEmails.map((email) => (
        <div
          key={email.id}
          className="group bg-[#2a3f59] mb-4 rounded-2xl p-4 shadow-2xl hover:cursor-pointer hover:bg-[#203045] transition-all flex justify-between items-center"
          onClick={() => navigate(`/email/${email.id}`)} // Navigate to full email view
        >
          <div className="flex-1">
            <h3 className="text-xl font-bold group-hover:text-white transition-colors">
              {email.subject}
            </h3>
            <h3 className="text-sm text-gray-400">{email.senderAddress}</h3>
            <div
              className="overflow-clip text-lg text-gray-300 group-hover:text-white transition-colors"
              dangerouslySetInnerHTML={{ __html: email.content }}
            ></div>
            <h3 className="text-xs text-gray-500">{email.dateString}</h3>
          </div>

          {/* Star Icon on the Right */}
          <button
            onClick={(e) => handleStarClick(email.id, e)}
            className="text-2xl text-yellow-400 opacity-0 group-hover:opacity-100 transition-opacity duration-300"
          >
            {starredEmailIds.has(email.id) ? (
              <FontAwesomeIcon icon={faStar} /> // Solid star when clicked
            ) : (
              <FontAwesomeIcon icon={faRegStar} /> // Outline star when not clicked
            )}
          </button>
        </div>
      ))}

      {/* Pagination Controls */}
      <div className="flex justify-between items-center mt-4">
        <button
          disabled={currentPage === 1}
          onClick={() => setCurrentPage(currentPage - 1)}
          className="px-4 py-2 rounded-lg hover:bg-gray-800 disabled:opacity-50"
        >
          Previous
        </button>
        <span className="text-white">{`Page ${currentPage} of ${totalPages}`}</span>
        <button
          disabled={currentPage === totalPages}
          onClick={() => setCurrentPage(currentPage + 1)}
          className="px-4 py-2 rounded-lg hover:bg-gray-800 disabled:opacity-50"
        >
          Next
        </button>
      </div>
    </div>
  );
}

export default EmailList;
