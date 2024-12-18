import React, { useEffect, useState } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faArrowsRotate, faMagnifyingGlass } from '@fortawesome/free-solid-svg-icons';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
function Navbar({ setSearchQuery , username , setFolders }) {
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
  const getMails = async () => {
    let token = localStorage.getItem('token')
    try {
      const response = await axios.get("http://localhost:8080/mail/all", {
        headers: { Authorization: token },
      });
      console.log(response);
      setFolders(response.data); // Update the folders state here
    } catch (error) {
      console.error("Error fetching emails:", error);
    }
  };
  return (
    <div className="w-full h-[10%] bg-[#223047] p-9 flex items-center justify-between">
      <div>
        
      </div>
      <div className="flex items-center w-1/3 h-[80%] ml-[11%] space-x-4">
      <button onClick={getMails} className='text-3xl text-white active:scale-95 hover:scale-105'>
          <FontAwesomeIcon icon={faArrowsRotate}></FontAwesomeIcon>
        </button>
        {/* Search bar */}
        <div className="flex items-center bg-[#2f4562] p-3 rounded-full w-full shadow-2xl">
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
            className="w-full bg-[#2f4562] text-white placeholder-white focus:outline-none placeholder-opacity-55"
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
