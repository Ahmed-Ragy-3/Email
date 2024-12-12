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
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";

function Sidebar() {
  const [activeButton, setActiveButton] = useState(""); // State to track the active button
  const location = useLocation(); // Get the current location

  // Button data to dynamically render buttons
  const buttons = [
    { id: "inbox", label: "Inbox", icon: faInbox, path: "/inbox" },
    { id: "starred", label: "Starred", icon: faStar, path: "/starred" },
    { id: "trash", label: "Trash", icon: faTrash, path: "/trash" },
    { id: "spam", label: "Spam", icon: faCircleExclamation, path: "/spam" },
    { id: "drafts", label: "Drafts", icon: faFile, path: "/drafts" },
    { id: "sent", label: "Sent", icon: faPaperPlane, path: "/sent" },
  ];

  const handleButtonClick = (id) => {
    // Avoid toggling for buttons other than "Compose"
    if (activeButton !== id) {
      setActiveButton(id); // Set active button if a new button is clicked
    }
  };

  // Construct the correct path for the "Compose" page based on the current location
  const composePath = `${location.pathname}/compose`;

  return (
    <div id="side-bar" className="h-full basis-[11%] bg-[#003C43] pb-[10%]">
      <div className="w-full flex justify-center mb-10">
        {/* Link to the dynamic "Compose" route */}
        <Link to={composePath}>
          <button className="text-2xl px-4 py-2 bg-[#135D66] rounded-2xl text-white hover:bg-[#0A4D5A] active:bg-[#0E4B50] transform hover:scale-105 active:scale-105 transition duration-200 ease-in-out shadow-2xl shadow-black">
            <FontAwesomeIcon icon={faPenToSquare} /> Compose
          </button>
        </Link>
      </div>
      <div>
        {buttons.map((button) => (
          <Link to={button.path} key={button.id}>
            <button
              onClick={() => handleButtonClick(button.id)}
              className={`p-9 h-16 w-full font-poppins font-medium flex items-center justify-start space-x-3 transform hover:scale-105 active:scale-105 transition duration-200 ease-in-out ${
                activeButton === button.id
                  ? "bg-[#135D66] text-white scale-105"
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
    </div>
  );
}

export default Sidebar;

