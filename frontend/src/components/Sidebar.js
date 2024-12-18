import React, { useState } from "react";
import { Link, useLocation } from "react-router-dom";
import {
  faInbox,
  faPenToSquare,
  faStar,
  faTrash,
  faCircleExclamation,
  faFile,
  faPaperPlane,
  faFolder,
  faPlus,
  faEdit,
  faTrashAlt,
  faClock
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import Compose from "../pages/Compose";
import { RemoveScrollBar } from "react-remove-scroll-bar"; // Import the package

function Sidebar({ client , updateFolders}) {
  const location = useLocation();
  console.log("side : ", updateFolders)
  const [showComposeModal, setShowComposeModal] = useState(false);
  const [activeButton, setActiveButton] = useState("");
  const [showFolders, setShowFolders] = useState(false);
  const [folders, setFolders] = useState([
    { name: "Folder 1", path: "/folder1" },
    { name: "Folder 2", path: "/folder2" },
  ]);

  const buttons = [
    { id: "inbox", label: "Inbox", icon: faInbox, path: "/" },
    { id: "starred", label: "Starred", icon: faStar, path: "/starred" },
    { id: "trash", label: "Trash", icon: faTrash, path: "/trash" },
    { id: "spam", label: "Spam", icon: faCircleExclamation, path: "/spam" },
    { id: "drafts", label: "Drafts", icon: faFile, path: "/drafts" },
    { id: "sent", label: "Sent", icon: faPaperPlane, path: "/sent" },
    { id: "scheduled", label: "Scheduled", icon: faClock, path: "/scheduled" },
  ];

  const composePath = `${location.pathname}/compose`;

  const handleButtonClick = (id) => {
    if (id !== "compose") {
      setActiveButton(id);
    }
    if (id === "compose") {
      setShowComposeModal(true);
    }
  };

  const closeModal = () => {
    setShowComposeModal(false);
    setActiveButton("");
  };

  const handleFolderClick = (folder) => {
    setActiveButton(folder.name);
  };

  const handleAddFolder = () => {
    const newFolderName = prompt("Enter folder name:");
    if (newFolderName) {
      setFolders([...folders, { name: newFolderName, path: `/folders/${newFolderName}` }]);
    }
  };

  const handleRenameFolder = (index) => {
    const newFolderName = prompt("Enter new folder name:");
    if (newFolderName) {
      const updatedFolders = [...folders];
      updatedFolders[index].name = newFolderName;
      setFolders(updatedFolders);
    }
  };

  const handleDeleteFolder = (index) => {
    const updatedFolders = folders.filter((_, idx) => idx !== index);
    setFolders(updatedFolders);
  };

  return (
    <div id="side-bar" className="h-full pb-[10%] bg-[#223047] relative">
      <RemoveScrollBar /> {/* Apply RemoveScrollBar component here */}

      <div className="w-full flex justify-center mb-10">
        <button
          onClick={() => handleButtonClick("compose")}
          className="text-[20px] px-4 py-2 bg-[#529199] rounded-full text-white hover:bg-[#0A4D5A] active:bg-[#0E4B50] transform transition duration-200 ease-in-out shadow-gray-800"
        >
          <FontAwesomeIcon icon={faPenToSquare} /> Compose
        </button>
      </div>
      
      <div className="custom-scroll">
        {buttons.map((button) => (
          <Link to={button.path} key={button.id}>
            <button
              className={`p-9 h-16 w-full font-poppins font-medium flex items-center justify-start space-x-3 transform hover:scale-105 active:scale-105 transition duration-200 ease-in-out ${
                location.pathname === button.path
                  ? "bg-[#2f4562] text-white shadow-inner-tb scale-[105%]"
                  : "bg-transparent shadow-none text-white"
              }`}
            >
              <FontAwesomeIcon
                icon={button.icon}
                className="text-white text-[18px]"
              />
              <span className="text-[18px]">{button.label}</span>
            </button>
          </Link>
        ))}

        <div
          onClick={() => setShowFolders(!showFolders)}
          className={`p-9 h-16 w-full font-poppins font-medium flex items-center justify-start space-x-3 transform hover:scale-105 active:scale-105 transition duration-200 ease-in-out ${
            showFolders ? "bg-[#2f4562] text-white shadow-inner-tb scale-[105%]" : "bg-transparent shadow-none text-white"
          }`}
        >
          <FontAwesomeIcon icon={faFolder} className="text-white text-[18px]" />
          <span className="text-[18px]">Folders</span>
          <div className="add-folder-button" onClick={handleAddFolder}>
            <FontAwesomeIcon icon={faPlus} className="text-white text-[18px]" />
          </div>
        </div>

        {showFolders && (
          <div className="pl-5 space-y-2">
            {folders.map((folder, index) => (
              <div key={index} className="folder-item p-3 hover:bg-[#2f4562] text-white">
                <button
                  onClick={() => handleFolderClick(folder)}
                  className="w-full text-left"
                >
                  {folder.name}
                </button>

                <div className="folder-actions">
                  <button
                    onClick={() => handleRenameFolder(index)}
                    className="text-white"
                  >
                    <FontAwesomeIcon icon={faEdit} /> Rename
                  </button>
                  <button
                    onClick={() => handleDeleteFolder(index)}
                    className="text-white"
                  >
                    <FontAwesomeIcon icon={faTrashAlt} /> Delete
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
      {/* Modal for composing an email, passed as a component */}
      {showComposeModal && (
        <Compose closeModal={closeModal} client={client} setFolders={updateFolders} /> // Pass closeModal as a prop
      )}

      {showComposeModal && <Compose closeModal={closeModal} client={client} setFolders={updateFolders}/>}
    </div>
  );
}

export default Sidebar;
