import { useEffect, useState } from 'react';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faStar } from '@fortawesome/free-solid-svg-icons'; // Solid star icon
import { faStar as faRegStar } from '@fortawesome/free-regular-svg-icons'; // Regular star icon
import { useNavigate } from 'react-router-dom';

function EmailList({ emails, emailsPerPage , setFolders , folders }) {
  const navigate = useNavigate();
  const [currentPage, setCurrentPage] = useState(1);
  
  // State to track starred emails locally
  const [starredEmailIds, setStarredEmailIds] = useState(new Set());

  const isStarredFolder = window.location.pathname === "/starred";

  useEffect(() => {
    if (isStarredFolder) {
      const starredEmails = new Set();
      // Mark emails as starred if they are in the "Starred" folder
      folders.forEach((folder) => {
        if (folder.name === "Starred") {
          folder.mails.forEach((email) => starredEmails.add(email.id));
        }
      });
      setStarredEmailIds(starredEmails);
    }
  }, [isStarredFolder, folders]);

  const handleStarClick = (emailId, event) => {
    event.stopPropagation(); // Prevent email navigation when star is clicked

    // Toggle the star state for the email
    setStarredEmailIds((prev) => {
      const newStarredEmails = new Set(prev);

      // Find the email
      const email = emails.find((email) => email.id === emailId);

      // Unstar the email
      if (newStarredEmails.has(emailId)) {
        newStarredEmails.delete(emailId);

        // Remove email from the "Starred" folder
        setFolders((prevFolders) => {
          return prevFolders.map((folder) => {
            if (folder.name === "Starred") {
              return {
                ...folder,
                mails: folder.mails.filter(mail => mail.id !== emailId),
              };
            }
            return folder;
          });
        });
      } else {
        newStarredEmails.add(emailId);

        // Remove email from its original folder and add it to the "Starred" folder
        setFolders((prevFolders) => {
          return prevFolders.map((folder) => {
            if (folder.name !== "Starred") {
              return {
                ...folder,
                mails: folder.mails.filter(mail => mail.id !== emailId), // Remove from other folders
              };
            }
            if (folder.name === "Starred") {
              return {
                ...folder,
                mails: [...folder.mails, email], // Add to the Starred folder
              };
            }
            return folder;
          });
        });
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
