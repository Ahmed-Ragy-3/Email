import React from 'react'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faMagnifyingGlass } from '@fortawesome/free-solid-svg-icons'
import { useState } from 'react';

function Navbar() {
    const [query, setQuery] = useState(""); // State to track search input

  // Function to handle the change in the input field
  const handleSearchChange = (e) => {
    setQuery(e.target.value);
  };

  // Function to handle search submission using the Enter key
  const handleSearchSubmit = (e) => {
    if (e.key === "Enter") {
      console.log("Search query submitted:", query);
      // Add your search logic here
    }
  };

  return (
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
              className="w-full bg-[#135D66] text-white placeholder-white focus:outline-none placeholder-opacity-55"
            />
          </div>
        </div>
      </div>
  )
}
export default Navbar;
