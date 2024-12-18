import React, { useState } from "react";
import { Link, useLocation } from "react-router-dom"; // Import Link and useLocation for dynamic routing
import {
  faInbox,
  faPenToSquare,
  faStar,
  faTrash,
  faCircleExclamation,
  faFile,
  faPaperPlane,
  faClock,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import Compose from "../pages/Compose";

function Sidebar({client , setFolders}) {
  console.log("client is ",client)
  const location = useLocation(); // Get the current location
  const [showComposeModal, setShowComposeModal] = useState(false); // Control modal visibility
  const [activeButton, setActiveButton] = useState("")
  // Button data to dynamically render buttons
  const buttons = [
    { id: "inbox", label: "Inbox", icon: faInbox, path: "/" },
    { id: "starred", label: "Starred", icon: faStar, path: "/starred" },
    { id: "trash", label: "Trash", icon: faTrash, path: "/trash" },
    { id: "spam", label: "Spam", icon: faCircleExclamation, path: "/spam" },
    { id: "drafts", label: "Drafts", icon: faFile, path: "/drafts" },
    { id: "sent", label: "Sent", icon: faPaperPlane, path: "/sent" },
    { id: "scheduled", label: "Scheduled", icon: faClock, path: "/scheduled" },
  ];
  
  // Construct the correct path for the "Compose" page based on the current location
  const composePath = `${location.pathname}/compose`;

  const handleButtonClick = (id) => {
    if (id !== "compose") {
      setActiveButton(id); // Update active button for all buttons except Compose
    }
    if (id === "compose") {
      setShowComposeModal(true); // Show modal when "Compose" is clicked
    }
  };

  const closeModal = () => {
    setShowComposeModal(false); // Close the modal
    setActiveButton(""); // Reset active button when closing modal
  };


  return (
    <div id="side-bar" className="h-full basis-[11%] bg-[#003C43] pb-[10%]">
      <div className="w-full flex justify-center mb-10">
        {/* Link to the dynamic "Compose" route */}
        <button
          onClick={() => handleButtonClick("compose")}
          className="text-2xl px-4 py-2 bg-[#135D66] rounded-2xl text-white hover:bg-[#0A4D5A] active:bg-[#0E4B50] transform hover:scale-105 active:scale-105 transition duration-200 ease-in-out shadow-2xl shadow-black"
        >
          <FontAwesomeIcon icon={faPenToSquare} /> Compose
        </button>

      </div>
      <div>
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
      </div>
      {/* Modal for composing an email, passed as a component */}
      {showComposeModal && (
        <Compose closeModal={closeModal} client = {client} setFolders = {setFolders} /> // Pass closeModal as a prop
      )}

    </div>
  );
}

export default Sidebar;
