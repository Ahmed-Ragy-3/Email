import React from 'react'
import { faInbox, faPenToSquare } from "@fortawesome/free-solid-svg-icons"; // Import the "inbox" icon
import { faStar } from "@fortawesome/free-solid-svg-icons";
import { faTrash } from "@fortawesome/free-solid-svg-icons";
import { faCircleExclamation } from "@fortawesome/free-solid-svg-icons";
import { faFile } from "@fortawesome/free-solid-svg-icons";
import { faPaperPlane } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';// Import the FontAwesomeIcon component

function Sidebar() {
  return (
    <div
    id="side-bar"
    className="h-full basis-[11%] bg-[#003C43] pb-[10%]"
  >
    <div className="w-full flex justify-center mb-10">
    <button 
className="
text-2xl 
px-4 
py-2 
bg-[#135D66]
rounded-2xl 
text-white 
hover:bg-[#0A4D5A] 
active:bg-[#0E4B50] 
transform 
hover:scale-105 
active:scale-95 
transition 
duration-200 
ease-in-out shadow-2xl shadow-black
"
>
      <FontAwesomeIcon icon={faPenToSquare} /> Compose
    </button>
  </div>
    <div>
      <button
        className="text-white p-9 h-16 w-full font-poppins font-medium flex items-center justify-start space-x-3 hover:bg-[#0A4D5A]  active:bg-[#0E4B50]  transform 
hover:scale-105 
active:scale-95 
transition 
duration-200 
ease-in-out "
      >
        <FontAwesomeIcon
          icon={faInbox}
          className="text-white text-[18px]"
        />
        <span className="text-[18px]">Inbox</span>
      </button>

      <button
        className="text-white p-9 h-16 w-full font-poppins font-medium flex items-center justify-start space-x-3 hover:bg-[#0A4D5A]  active:bg-[#0E4B50]  transform 
hover:scale-105 
active:scale-95 
transition 
duration-200 
ease-in-out"
      >
        {" "}
        <FontAwesomeIcon
          icon={faStar}
          className="text-white text-[18px]"
        />
        <span className="text-[18px]">Starred</span>
      </button>

      <button
        className="text-white p-9 h-16 w-full font-poppins font-medium flex items-center justify-start space-x-3 hover:bg-[#0A4D5A]  active:bg-[#0E4B50]  transform 
hover:scale-105 
active:scale-95 
transition 
duration-200 
ease-in-out"
      >
        {" "}
        <FontAwesomeIcon
          icon={faTrash}
          className="text-white text-[18px]"
        />
        <span className="text-[18px]">Trash</span>
      </button>

      <button
        className="text-white p-9 h-16 w-full font-poppins font-medium flex items-center justify-start space-x-3 hover:bg-[#0A4D5A]  active:bg-[#0E4B50]  transform 
hover:scale-105 
active:scale-95 
transition 
duration-200 
ease-in-out"
      >
        {" "}
        <FontAwesomeIcon
          icon={faCircleExclamation}
          className="text-white text-[18px]"
        />
        <span className="text-[18px]">Spam</span>
      </button>

      <button
        className="text-white p-9 h-16 w-full font-poppins font-medium flex items-center justify-start space-x-3 hover:bg-[#0A4D5A]  active:bg-[#0E4B50]  transform 
hover:scale-105 
active:scale-95 
transition 
duration-200 
ease-in-out"
      >
        {" "}
        <FontAwesomeIcon
          icon={faFile}
          className="text-white text-[18px]"
        />
        <span className="text-[18px]">Drafts</span>
      </button>

      <button
        className="text-white p-9 h-16 w-full font-poppins font-medium flex items-center justify-start space-x-3 hover:bg-[#0A4D5A]  active:bg-[#0E4B50]  transform 
hover:scale-105 
active:scale-95 
transition 
duration-200 
ease-in-out"
      >
        {" "}
        <FontAwesomeIcon
          icon={faPaperPlane}
          className="text-white text-[18px]"
        />
        <span className="text-[18px]">Sent</span>
      </button>
    </div>
  </div>
  )
}
export default Sidebar;