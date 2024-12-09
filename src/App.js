import React, { useState } from 'react';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';  // Import the FontAwesomeIcon component
import { faInbox } from '@fortawesome/free-solid-svg-icons';  // Import the "inbox" icon
import { faStar } from '@fortawesome/free-solid-svg-icons';
import { faTrash } from '@fortawesome/free-solid-svg-icons';
import { faCircleExclamation } from '@fortawesome/free-solid-svg-icons';
import { faFile } from '@fortawesome/free-solid-svg-icons';
import { faPaperPlane } from '@fortawesome/free-solid-svg-icons';
import { faMagnifyingGlass } from '@fortawesome/free-solid-svg-icons';

const App = () => {

  const [query, setQuery] = useState(''); // State to track search input

  // Function to handle the change in the input field
  const handleSearchChange = (e) => {
    setQuery(e.target.value);
  };

  // Function to handle search submission using the Enter key
  const handleSearchSubmit = (e) => {
    if (e.key === 'Enter') {
      console.log('Search query submitted:', query);
      // Add your search logic here
    }
  };

  return (
    <div className="h-screen bg-[#135D66]">

      <div className="w-full h-[10%] bg-[#003C43] p-9 flex items-center justify-between">

      <div className="flex items-center w-1/3 h-[80%] ml-[11%]">
        {/* Search bar*/}
        <div className="flex items-center bg-[#135D66] p-3 rounded-full w-full">
          <FontAwesomeIcon
            icon={faMagnifyingGlass}
            className="text-white text-[18px] mr-3 ml-2"
          />
          <input
            type="text"
            value={query}
            onChange={handleSearchChange}
            onKeyDown={handleSearchSubmit}
            placeholder="Search"
            className="w-full bg-[#135D66] text-white placeholder-white focus:outline-none"
          />
        </div>
      </div>

      </div>

      <div className="h-full w-[11%] bg-[#003C43]">

        <button className="text-white p-9 h-16 w-full font-poppins font-medium flex items-center justify-start space-x-3">
          <FontAwesomeIcon icon={faInbox} className="text-white text-[18px]" />
          <span className="text-[18px]">Inbox</span>
        </button>

        <button className="text-white p-9 h-16 w-full font-poppins font-medium flex items-center justify-start space-x-3">
          <FontAwesomeIcon icon={faStar} className="text-white text-[18px]" />
          <span className="text-[18px]">Starred</span>
        </button>

        <button className="text-white p-9 h-16 w-full font-poppins font-medium flex items-center justify-start space-x-3">
          <FontAwesomeIcon icon={faTrash} className="text-white text-[18px]" />
          <span className="text-[18px]">Trash</span>
        </button>

        <button className="text-white p-9 h-16 w-full font-poppins font-medium flex items-center justify-start space-x-3">
          <FontAwesomeIcon icon={faCircleExclamation} className="text-white text-[18px]" />
          <span className="text-[18px]">Spam</span>
        </button>

        <button className="text-white p-9 h-16 w-full font-poppins font-medium flex items-center justify-start space-x-3">
          <FontAwesomeIcon icon={faFile} className="text-white text-[18px]" />
          <span className="text-[18px]">Drafts</span>
        </button>

        <button className="text-white p-9 h-16 w-full font-poppins font-medium flex items-center justify-start space-x-3">
          <FontAwesomeIcon icon={faPaperPlane} className="text-white text-[18px]" />
          <span className="text-[18px]">Sent</span>
        </button>

      </div>

    </div>
  );  
}

export default App;