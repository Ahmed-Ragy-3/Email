import React, { useState } from "react";
import { Link, useLocation, useNavigate } from "react-router-dom";
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
  faClock,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import Compose from "../pages/Compose";
import { RemoveScrollBar } from "react-remove-scroll-bar"; // Import the package
import axios from "axios";

function Sidebar({ client, updateFolders, mainFolders }) {
  const navigate = useNavigate();
  const location = useLocation();
  const [showComposeModal, setShowComposeModal] = useState(false);
  const [activeButton, setActiveButton] = useState("");
  const [showFolders, setShowFolders] = useState(false);
  const [folders, setFolders] = useState([
    { name: "Folder 1", path: "/folder1" },
    { name: "Folder 2", path: "/folder2" },
  ]);
  const additionalFolders = mainFolders.slice(7);
  console.log("additional fodlers", additionalFolders);
  const buttons = [
    { id: "inbox", label: "Inbox", icon: faInbox, path: "/" },
    { id: "starred", label: "Starred", icon: faStar, path: "/starred" },
    { id: "trash", label: "Trash", icon: faTrash, path: "/trash" },
    { id: "spam", label: "Spam", icon: faCircleExclamation, path: "/spam" },
    { id: "drafts", label: "Drafts", icon: faFile, path: "/drafts" },
    { id: "sent", label: "Sent", icon: faPaperPlane, path: "/sent" },
    { id: "scheduled", label: "Scheduled", icon: faClock, path: "/scheduled" },
  ];
  const [hovered, setHovered] = useState(null);
  const composePath = `${location.pathname}/compose`;

  const handleMouseEnter = (folderId) => {
    setHovered(folderId); // Set the hovered folder ID when hovering over a folder
  };

  const handleMouseLeave = () => {
    setHovered(null); // Reset the hovered folder ID when mouse leaves
  };

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
    const formattedPath = folder.name.toLowerCase().replace(/\s+/g, "+");
    console.log(formattedPath);
    setActiveButton(folder.name);
    navigate(`/${formattedPath}`);
  };

  const handleAddFolder = async () => {
    const newFolderName = prompt("Enter folder name:");
    try {
      let token = localStorage.getItem("token");
      let response = await axios.put(
        "http://localhost:8080/mailbox/add",
        {
          name: newFolderName,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`, // Replace `token` with your actual token variable
          },
        }
      );
      const getMails = async () => {
        try {
          const response = await axios.get("http://localhost:8080/mail/all", {
            headers: { Authorization: token },
          });
          console.log(response);
          updateFolders(response.data); // Update the folders state here
        } catch (error) {
          console.error("Error fetching emails:", error);
        }
      };

      getMails();
    } catch (error) {
      console.log("something crashed");
    }
  };

  const handleRenameFolder = async (id) => {
    console.log(id);
    const newFolderName = prompt("Enter folder name:");
    try {
      let token = localStorage.getItem("token");
      let response = await axios.post(
        "http://localhost:8080/mailbox/edit",
        {
          id: id,
          name: newFolderName,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`, // Replace `token` with your actual token variable
          },
        }
      );
      const getMails = async () => {
        try {
          const response = await axios.get("http://localhost:8080/mail/all", {
            headers: { Authorization: token },
          });
          console.log(response);
          updateFolders(response.data); // Update the folders state here
        } catch (error) {
          console.error("Error fetching emails:", error);
        }
      };

      getMails();
    } catch (error) {
      console.log("something crashed");
    }
  };

  const handleDeleteFolder = async (folder) => {
    console.log("deleted folder ", folder)
    try {
      let token = localStorage.getItem("token");
      let data = {
        id : folder.id,
        name : folder.name,
      }
      console.log(data)
      let response = await axios.post(
        "http://localhost:8080/mailbox/delete",
        data,
        {
          headers: {
            Authorization: `${token}`, // Replace `token` with your actual token variable
          },
        }
      );
      const getMails = async () => {
        try {
          const response = await axios.get("http://localhost:8080/mail/all", {
            headers: { Authorization: token },
          });
          console.log(response);
          updateFolders(response.data); // Update the folders state here
        } catch (error) {
          console.error("Error fetching emails:", error);
        }
      };

      getMails();
    } catch (error) {
      console.log("something crashed");
    }
  };

  return (
    <div
      id="side-bar"
      className="h-full pb-[10%] basis-[15%] bg-[#223047] relative"
    >
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
          className={`p-9 h-16 w-full font-poppins font-medium flex items-center justify-start space-x-3 transform hover:scale-105 active:scale-105 transition duration-200 ease-in-out ${
            showFolders
              ? "bg-[#2f4562] text-white shadow-inner-tb scale-[105%]"
              : "bg-transparent shadow-none text-white"
          }`}
        >
          <div
            onClick={() => setShowFolders(!showFolders)}
            className="space-x-3"
          >
            <FontAwesomeIcon
              icon={faFolder}
              className="text-white text-[18px]"
            />
            <span className="text-[18px]">Folders</span>
          </div>
          <div className="add-folder-button" onClick={handleAddFolder}>
            <FontAwesomeIcon icon={faPlus} className="text-white text-[18px]" />
          </div>
        </div>

        {showFolders && (
          <div className="pl-5 space-y-2">
            {/* Render additional folders */}
            {additionalFolders.length > 0 ? (
              additionalFolders.map((folder, index) => {
                const folderPath = `/${folder.name
                  .toLowerCase()
                  .replace(/\s+/g, "+")}`;
                return (
                  <div
                    key={index}
                    
                    className={`folder-item p-9 h-16 w-full font-poppins font-medium flex items-center justify-start space-x-3 transform hover:scale-105 active:scale-105 transition duration-200 ease-in-out ${
                      location.pathname === folderPath
                        ? "bg-[#2f4562] text-white shadow-inner-tb scale-[105%]"
                        : "bg-transparent shadow-none text-white"
                    } group`} // Add 'group' class to the parent div
                  >
                    <div onClick={() => handleFolderClick(folder)}>
                    <button
                      className="font-bold w-full"
                      
                    >
                      {folder.name}
                    </button>
                    </div>
                    {/* Folder actions */}
                    <div className="folder-actions inline-block opacity-0 group-hover:opacity-100 space-x-1 transition-opacity duration-300">
                      <button
                        onClick={() => handleRenameFolder(folder.id)}
                        className="text-white text-xs inline-block"
                      >
                        <FontAwesomeIcon icon={faEdit} />
                      </button>
                      <button
                        onClick={() => handleDeleteFolder(folder)}
                        className="text-white text-xs inline-block"
                      >
                        <FontAwesomeIcon icon={faTrashAlt} />
                      </button>
                    </div>
                  </div>
                );
              })
            ) : (
              <p className="text-white">No additional folders available</p>
            )}
          </div>
        )}
      </div>
      {/* Modal for composing an email, passed as a component */}
      {showComposeModal && (
        <Compose
          closeModal={closeModal}
          client={client}
          setFolders={updateFolders}
        /> // Pass closeModal as a prop
      )}
      {showComposeModal && (
        <Compose
          closeModal={closeModal}
          client={client}
          setFolders={updateFolders}
        />
      )}
    </div>
  );
}

export default Sidebar;
