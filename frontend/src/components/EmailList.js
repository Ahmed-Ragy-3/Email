import { useEffect, useState } from 'react';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faStar } from '@fortawesome/free-solid-svg-icons';
import { faStar as faRegStar, faSquareCheck, faSquare } from '@fortawesome/free-regular-svg-icons';
import { faTrash, faFolder } from '@fortawesome/free-solid-svg-icons';
import { useNavigate } from 'react-router-dom';
import Compose from '../pages/Compose'; // Import the Compose component

function EmailList({ emails, emailsPerPage, setFolders, folders, contacts }) {
  const navigate = useNavigate();
  const [currentPage, setCurrentPage] = useState(1);
  const [selectedEmailIds, setSelectedEmailIds] = useState(new Set());
  const [starredEmailIds, setStarredEmailIds] = useState(new Set());
  const [isComposeOpen, setIsComposeOpen] = useState(false);
  const [emailToEdit, setEmailToEdit] = useState(null);
  const [showFolderDropdown, setShowFolderDropdown] = useState(false); // State to toggle folder dropdown visibility

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

        setFolders((prevFolders) =>
          prevFolders.map((folder) => {
            if (folder.name === "Starred") {
              return {
                ...folder,
                mails: folder.mails.filter((mail) => mail.id !== emailId),
              };
            }
            return folder;
          })
        );
      } else {
        newStarredEmails.add(emailId);

        setFolders((prevFolders) =>
          prevFolders.map((folder) => {
            if (folder.name !== "Starred") {
              return {
                ...folder,
                mails: folder.mails.filter((mail) => mail.id !== emailId), // Remove from other folders
              };
            }
            if (folder.name === "Starred") {
              return {
                ...folder,
                mails: [...folder.mails, email], // Add to the Starred folder
              };
            }
            return folder;
          })
        );
      }

      return newStarredEmails;
    });
  };

  const totalPages = Math.ceil(emails.length / emailsPerPage);
  const paginatedEmails = emails.slice((currentPage - 1) * emailsPerPage, currentPage * emailsPerPage);

  const handleEmailClick = (email) => {
    if (isDraftsFolder) {
      setEmailToEdit(email);
      setIsComposeOpen(true);
    } else {
      navigate(`/email/${email.id}`); // Navigate to the full email view if not drafts
    }
  };

  const handleSelectAll = () => {
    if (selectedEmailIds.size === paginatedEmails.length) {
      setSelectedEmailIds(new Set());
    } else {
      setSelectedEmailIds(new Set(paginatedEmails.map((email) => email.id)));
    }
  };

  const handleSelectEmail = (emailId) => {
    setSelectedEmailIds((prev) => {
      const newSet = new Set(prev);
      if (newSet.has(emailId)) {
        newSet.delete(emailId);
      } else {
        newSet.add(emailId);
      }
      return newSet;
    });
  };

  const handleMoveToFolder = (folderName) => {
    const emailsToMove = emails.filter((email) => selectedEmailIds.has(email.id));
    setFolders((prevFolders) =>
      prevFolders.map((folder) => {
        if (folder.name === folderName) {
          return {
            ...folder,
            mails: [...folder.mails, ...emailsToMove],
          };
        }
        return folder;
      })
    );
    setSelectedEmailIds(new Set());
    setShowFolderDropdown(false); // Close dropdown after moving
  };

  const handleDeleteEmails = () => {
    const remainingEmails = emails.filter((email) => !selectedEmailIds.has(email.id));
    setSelectedEmailIds(new Set());
    // Optionally, update your folder state here
  };

  return (
    <div>
      {/* Select All, Move to Folder, and Delete Actions */}
      <div className="flex items-center justify-between mb-4">
        <button onClick={handleSelectAll}>
          <FontAwesomeIcon
            icon={selectedEmailIds.size === paginatedEmails.length ? faSquareCheck : faSquare}
            className="text-[16px]"
          />
          <span className="ml-2 font-Poppins font-semibold">Select All</span>
        </button>
        <div className="flex space-x-4">
          {/* Move to Folder Dropdown */}
          <div className="relative">
            <button
              onClick={() => setShowFolderDropdown((prev) => !prev)}
              className="flex items-center text-[16px] px-4 py-2 bg-[#bf6360] rounded-2xl hover:scale-105 text-white hover:bg-[#a55755] active:bg-[#8e4b48] transform transition duration-200 ease-in-out shadow-gray-900"
            >
              <FontAwesomeIcon icon={faFolder} className="text-xl" />
              <span className="ml-2 font-Poppins font-semibold">Move to Folder</span>
            </button>
            {showFolderDropdown && (
              <div className="absolute top-8 rounded-2xl w-full bg-gray-700 p-2 shadow">
                {/* Filter folders to exclude Starred and Trash */}
                {folders
                  .filter((folder) => folder.name !== "Starred" && folder.name !== "Trash")
                  .map((folder) => (
                    <button
                      key={folder.name}
                      onClick={() => handleMoveToFolder(folder.name)}
                      className="block px-4 py-2 text-left w-full hover:bg-gray-800"
                    >
                      {folder.name}
                    </button>
                  ))}
              </div>
            )}
          </div>
          {/* Delete Emails */}
          <button onClick={handleDeleteEmails} className="flex items-center text-[16px] px-4 py-2 bg-[#bf6360] rounded-2xl hover:scale-105 text-white hover:bg-[#a55755] active:bg-[#8e4b48] transform transition duration-200 ease-in-out shadow-gray-900">
            <FontAwesomeIcon icon={faTrash} className="text-xl" />
            <span className="ml-2 font-Poppins font-semibold">Delete</span>
          </button>
        </div>
      </div>

      {paginatedEmails.map((email) => (
        <div
          key={email.id}
          className="group bg-[#2a3f59] mb-4 rounded-2xl p-4 shadow-2xl hover:cursor-pointer hover:bg-[#203045] transition-all flex justify-between items-center"
          onClick={() => handleEmailClick(email)}
        >
          <button
            onClick={(e) => {
              e.stopPropagation();
              handleSelectEmail(email.id);
            }}
            className="mr-4"
          >
            <FontAwesomeIcon
              icon={selectedEmailIds.has(email.id) ? faSquareCheck : faSquare}
              className="text-xl"
            />
          </button>
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
        <div className="text-black">
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
