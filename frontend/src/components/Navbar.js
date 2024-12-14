import React, { useEffect, useState } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faMagnifyingGlass } from '@fortawesome/free-solid-svg-icons';
import { useNavigate } from 'react-router-dom';

function Navbar({ setSearchQuery , username }) {
  const [query, setQuery] = useState(""); // State to track search input
  const navigate = useNavigate()
  const logout = ()=>
  {
    localStorage.removeItem('token');
    navigate('/login')
  }
  // Function to handle the change in the input field
  useEffect(() => {
    console.log(query);
  }, [query]);
  const handleSearchChange = (e) => {
    const value = e.target.value;
    setQuery(value);
    setSearchQuery(value); // Update the search query in the parent component
  };

  // Function to handle search submission using the Enter key
  const handleSearchSubmit = (e) => {
    if (e.key === "Enter") {
      console.log("Search query submitted:", query);
      // Add your search logic here if you want to trigger the search explicitly on Enter
    }
  };

  return (
    <div className="w-full h-[10%] bg-[#003C43] p-9 flex items-center justify-between">
      <div>

      </div>
      <div className="flex items-center w-1/3 h-[80%] ml-[11%]">
        {/* Search bar */}
        <div className="flex items-center bg-[#135D66] p-3 rounded-full w-full shadow-2xl">
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
      <div className='text-white flex space-x-8'>
        <div>
          {username}
        </div>
        <div>
          <button onClick={logout}>Log Out</button>
        </div>
      </div>
    </div>
  );
}

export default Navbar;
