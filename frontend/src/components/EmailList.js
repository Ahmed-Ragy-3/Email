import { useEffect, useState } from 'react';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faStar } from '@fortawesome/free-solid-svg-icons';
import { faStar as faRegStar } from '@fortawesome/free-regular-svg-icons';
import { useNavigate } from 'react-router-dom';
import Compose from '../pages/Compose';  // Import the Compose component

function EmailList({ emails, emailsPerPage , setFolders , folders , contacts }) {
  const navigate = useNavigate();
  const [currentPage, setCurrentPage] = useState(1);
  const [starredEmailIds, setStarredEmailIds] = useState(new Set());
  const [isComposeOpen, setIsComposeOpen] = useState(false);
  const [emailToEdit, setEmailToEdit] = useState(null);

  const isStarredFolder = window.location.pathname === "/starred";
  const isDraftsFolder = window.location.pathname === "/drafts"; // Check if the folder is drafts

  useEffect(() => {
    if (isStarredFolder) {
      const starredEmails = new Set();
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

    setStarredEmailIds((prev) => {
      const newStarredEmails = new Set(prev);
      const email = emails.find((email) => email.id === emailId);

      if (newStarredEmails.has(emailId)) {
        newStarredEmails.delete(emailId);

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

  const handleEmailClick = (email) => {
    if (isDraftsFolder) {
      // Open the compose modal with email data
      setEmailToEdit(email);
      setIsComposeOpen(true);
    } else {
      navigate(`/email/${email.id}`);  // Navigate to the full email view if not drafts
    }
  };

  return (
    <div>
      {paginatedEmails.map((email) => (
        <div
          key={email.id}
          className="group bg-[#2a3f59] mb-4 rounded-2xl p-4 shadow-2xl hover:cursor-pointer hover:bg-[#203045] transition-all flex justify-between items-center"
          onClick={() => handleEmailClick(email)}  // Use the new handler
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

          <button
            onClick={(e) => handleStarClick(email.id, e)}
            className="text-2xl text-yellow-400 opacity-0 group-hover:opacity-100 transition-opacity duration-300"
          >
            {starredEmailIds.has(email.id) ? (
              <FontAwesomeIcon icon={faStar} /> 
            ) : (
              <FontAwesomeIcon icon={faRegStar} /> 
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

      {/* Conditionally render Compose Modal if in Drafts folder */}
      {isComposeOpen && (
        <div className='text-black'>
        <Compose
          closeModal={() => setIsComposeOpen(false)}
          emailToEdit={emailToEdit}
          setFolders={setFolders}
          contacts={contacts}
        />
        </div>
      )}
    </div>
  );
}

export default EmailList;
