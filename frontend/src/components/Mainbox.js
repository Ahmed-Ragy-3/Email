import React, { useState, useMemo, useEffect } from "react";
import { Routes, Route, useNavigate } from "react-router-dom";
import Inbox from "../pages/Inbox";
import Sent from "../pages/Sent";
import Spam from "../pages/Spam";
import Starred from "../pages/Starred";
import Trash from "../pages/Trash";
import Drafts from "../pages/Drafts";
import FullEmailView from "./FullEmailView";
import axios from "axios";
import Scheduled from "../pages/Scheduled";
import EmailList from "./EmailList";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faAddressBook, faEdit, faTrash } from "@fortawesome/free-solid-svg-icons";
import { faUserGroup } from "@fortawesome/free-solid-svg-icons/faUserGroup";

function Mainbox({ folders, searchQuery, setFolders, contacts, setContacts }) {
  console.log(contacts)
  const [showContactbar, setShowContactbar] = useState(false);
  const [isButtonClicked, setIsButtonClicked] = useState(false);

  const toggleContactbar = () => {
    setShowContactbar(!showContactbar);
    setIsButtonClicked(!isButtonClicked); // Toggle button state for color and position
  };
  const deleteContact = async (contact) =>
  {
    try {
      let token = localStorage.getItem("token");
      console.log("my token is ",token)
      let response = await axios.post(
        "http://localhost:8080/user/contact/delete",
        {
          name: contact.name, // This is the body of the request
          emailAddress: contact.emailAddress, // This is the body of the request
        },
        {
          headers: {
            Authorization: `${token}`, // Pass the token in the Authorization header
          },
        }
      );
      const getContacts = async()=> {
        try {
          const response = await axios.get("http://localhost:8080/user/contacts", {
            headers: { Authorization: token },
          });
          console.log(response);
          setContacts(response.data); // Update the folders state here
        } catch (error) {
          console.error("Error fetching emails:", error);
        }
      }
      getContacts()
    console.log(response)
    } catch (error) {
      console.log(error)
    }
  }
  console.log("folders in mainbox are ", folders);
  let newmails;
  let path = window.location.pathname;
  let normalizedPath =
    path === "/" ? "inbox" : path.slice(1).replace(/\+/g, " ").toLowerCase();

  // Remove '+' characters from the current normalized path
  normalizedPath = normalizedPath.replace(/\+/g, " ");
  normalizedPath = normalizedPath.trim();

  // console.log("Normalized Path:", normalizedPath);

  // Now you can find the folder that matches the normalized path
  const matchedFolder = folders.find((folder) => {
    // Normalize folder name: Replace spaces with "+" and convert to lowercase
    const normalizedFolderName = folder.name.toLowerCase();
    // console.log("Folder Normalized Name:", normalizedFolderName); // Log the normalized folder name
    // console.log("Normalized Path for Comparison:", normalizedPath); // Log the normalized path for comparison
    return normalizedFolderName === normalizedPath;
  });

  // console.log("Matched Folder:", matchedFolder);

  // Get mails from the matched folder, or default to an empty array
  newmails = matchedFolder?.mails || [];
  let emails = newmails;
  console.log(emails);
  const importancePriority = {
    DELAYABLE: 0,
    NORMAL: 1,
    IMPORTANT: 2,
    URGENT: 3,
  };

  const sortByImportance = (a, b) => {
    const importanceA = importancePriority[a.importance] || 0;
    const importanceB = importancePriority[b.importance] || 0;
    return isDescending ? importanceB - importanceA : importanceA - importanceB;
  };

  // State to track sorting, filters, and sorting order
  const [sortBy, setSortBy] = useState("dateString");
  const [isDescending, setIsDescending] = useState(true);
  const [filterSender, setFilterSender] = useState("");
  const [filterSubject, setFilterSubject] = useState("");
  const [filterStartDate, setFilterStartDate] = useState("");
  const [filterEndDate, setFilterEndDate] = useState("");
  const [filterImportance, setFilterImportance] = useState("");
  const parseCustomDate = (dateString) => {
    // Example input: "17 / 12 / 2024 | 2: 18"
    const [datePart, timePart] = dateString.split(" | ");
    if (!timePart) {
      console.error("Invalid date string: ", dateString);
      return new Date(); // Return the current date if timePart is missing
    }
    const [day, month, year] = datePart.split(" / ").map(Number);
    const [hour, minute] = timePart.split(":").map((val) => Number(val.trim()));

    // Convert to valid ISO string
    return new Date(year, month - 1, day, hour, minute);
  };
  // Sorting functions
  const sortByDate = (a, b) => {
    const dateA = parseCustomDate(a.dateString);
    const dateB = parseCustomDate(b.dateString);
    // console.log(dateA, dateB);
    return isDescending ? dateB - dateA : dateA - dateB;
  };

  const sortBySubject = (a, b) => {
    return isDescending
      ? b.subject.localeCompare(a.subject)
      : a.subject.localeCompare(b.subject);
  };

  const sortBySender = (a, b) => {
    return isDescending
      ? b.senderAddress.localeCompare(a.senderAddress)
      : a.senderAddress.localeCompare(b.senderAddress);
  };

  const handleSort = (e) => {
    setSortBy(e.target.value);
  };

  const toggleSortOrder = () => {
    setIsDescending((prev) => !prev);
  };

  const clearFilters = () => {
    setFilterSender("");
    setFilterSubject("");
    setFilterStartDate("");
    setFilterEndDate("");
    setFilterImportance(""); // Clear importance filter
    setpaginationNumber(5);
  };

  // Filtered and sorted emails
  const filteredEmails = useMemo(() => {
    return emails
      .filter((email) => {
        // Function to remove HTML tags from content
        const removeHTMLTags = (text) => text.replace(/<[^>]*>/g, "");

        // Remove HTML tags from email content for search comparison
        const plainContent = removeHTMLTags(email.content).toLowerCase();

        const senderMatch = filterSender
          ? email.senderAddress === filterSender
          : true;
        const subjectMatch = filterSubject
          ? email.subject === filterSubject
          : true;
        const startDateMatch = filterStartDate
          ? new Date(email.dateString) >= new Date(filterStartDate)
          : true;
        const endDateMatch = filterEndDate
          ? new Date(email.dateString) <= new Date(filterEndDate)
          : true;
        const importanceMatch = filterImportance
          ? email.importance === filterImportance
          : true;

        // Modify the search match logic to also use plainContent (without HTML tags)
        const searchMatch = searchQuery
          ? email.subject.toLowerCase().includes(searchQuery.toLowerCase()) ||
            email.senderAddress
              .toLowerCase()
              .includes(searchQuery.toLowerCase()) ||
            plainContent.includes(searchQuery.toLowerCase()) || // Search within plain text content
            email.dateString.toLowerCase().includes(searchQuery.toLowerCase())
          : true;

        return (
          senderMatch &&
          subjectMatch &&
          startDateMatch &&
          endDateMatch &&
          importanceMatch &&
          searchMatch
        );
      })
      .sort(
        sortBy === "subject"
          ? sortBySubject
          : sortBy === "sender"
          ? sortBySender
          : sortBy === "importance"
          ? sortByImportance
          : sortByDate
      );
  }, [
    emails,
    filterSender,
    filterSubject,
    filterStartDate,
    filterEndDate,
    filterImportance, // Added filterImportance to dependency list
    sortBy,
    isDescending,
    searchQuery,
  ]);

  const uniqueSenders = [
    ...new Set(emails.map((email) => email.senderAddress)),
  ];
  const uniqueSubjects = [...new Set(emails.map((email) => email.subject))];
  const [paginationNumber, setpaginationNumber] = useState(5);
  const uniqueImportance = ["DELAYABLE", "NORMAL", "IMPORTANT", "URGENT"]; // Unique importance options
  const addContact = async () => {
    let name = prompt("Select a contact name")
    let emailAddress = prompt("Select a contact email address")
    let token = localStorage.getItem('token')
    try {
      let response = await axios.put("http://localhost:8080/user/contact/add" , {
        name: name, // This is the body of the request
        emailAddress: emailAddress, // This is the body of the request
      },
      {
        headers: {
          Authorization: `${token}`, // Pass the token in the Authorization header
        },
      }
      )
      console.log(response)
      const getContacts = async()=> {
        try {
          const response = await axios.get("http://localhost:8080/user/contacts", {
            headers: { Authorization: token },
          });
          console.log(response);
          setContacts(response.data); // Update the folders state here
        } catch (error) {
          console.error("Error fetching emails:", error);
        }
      }
      getContacts()

    } catch (error) {

    }
  }
  return (
    <div
      id="main-box"
      className="h-full basis-[89%] px-6 py-6 bg-[#2f4562] overflow-auto rounded-3xl shadow-inner shadow-gray-800 pb-[5%]"
    >
      {/* Sorting options */}
      <div className="mb-4 flex items-center">
        <label htmlFor="sort" className="text-white mr-2">
          Sort By
        </label>
        <select
          id="sort"
          value={sortBy}
          onChange={handleSort}
          className="p-2 bg-[#bf6360] text-white rounded-md hover:bg-[#a55755]"
        >
          <option
            value="dateString"
            className="bg-[#bf6360] hover:bg-[#a55755]"
          >
            Date
          </option>
          <option value="subject" className="bg-[#bf6360] hover:bg-[#a55755]">
            Subject
          </option>
          <option value="sender" className="bg-[#bf6360] hover:bg-[#a55755]">
            Sender
          </option>
          <option
            value="importance"
            className="bg-[#bf6360] hover:bg-[#a55755]"
          >
            Importance
          </option>
        </select>

        <button
          onClick={toggleSortOrder}
          className="ml-4 p-2 bg-[#bf6360] text-white rounded-md hover:bg-[#a55755]"
        >
          {isDescending ? "Descending" : "Ascending"}
        </button>
      </div>

      {/* Filter options */}
      <div className="mb-4 flex items-center space-x-4">
        <div>
          <label htmlFor="sender" className="text-white mr-2">
            Sender
          </label>
          <select
            id="sender"
            value={filterSender}
            onChange={(e) => setFilterSender(e.target.value)}
            className="p-2 bg-[#bf6360] text-white rounded-md hover:bg-[#a55755]"
          >
            <option value="">All Senders</option>
            {uniqueSenders.map((sender, index) => (
              <option key={index} value={sender}>
                {sender}
              </option>
            ))}
          </select>
        </div>

        <div>
          <label htmlFor="start-date" className="text-white mr-2">
            Date Range
          </label>
          <input
            type="date"
            id="start-date"
            value={filterStartDate}
            onChange={(e) => setFilterStartDate(e.target.value)}
            className="p-2 bg-[#bf6360] text-white rounded-md hover:bg-[#a55755]"
          />
          <span className="mx-2 text-white">to</span>
          <input
            type="date"
            id="end-date"
            value={filterEndDate}
            onChange={(e) => setFilterEndDate(e.target.value)}
            className="p-2 bg-[#bf6360] text-white rounded-md hover:bg-[#a55755]"
          />
        </div>

        <div>
          <label htmlFor="subject" className="text-white mr-2">
            Subject
          </label>
          <select
            id="subject"
            value={filterSubject}
            onChange={(e) => setFilterSubject(e.target.value)}
            className="p-2 bg-[#bf6360] text-white rounded-md hover:bg-[#a55755]"
          >
            <option value="">All Subjects</option>
            {uniqueSubjects.map((subject, index) => (
              <option key={index} value={subject}>
                {subject}
              </option>
            ))}
          </select>
        </div>
        <div>
          <label htmlFor="importance" className="text-white mr-2">
            Importance
          </label>
          <select
            id="importance"
            value={filterImportance}
            onChange={(e) => setFilterImportance(e.target.value)}
            className="p-2 bg-[#bf6360] text-white rounded-md hover:bg-[#a55755]"
          >
            <option value="">All Importance</option>
            {uniqueImportance.map((importance, index) => (
              <option key={index} value={importance}>
                {importance}
              </option>
            ))}
          </select>
        </div>
        <div>
          <select
            name="pagination-select"
            value={paginationNumber}
            id=""
            className="p-2 bg-[#bf6360] text-white rounded-md hover:bg-[#a55755]"
            onChange={(e) => {
              setpaginationNumber(Number(e.target.value));
            }}
          >
            <option value="1">1</option>
            <option value="2">2</option>
            <option value="3">3</option>
            <option value="4">4</option>
            <option value="5">5</option>
            <option value="10">10</option>
            <option value="20">20</option>
          </select>
        </div>
        <button
          onClick={clearFilters}
          className="p-2 bg-transparent hover:bg-[#a55755] text-white border-2 rounded-3xl"
        >
          Clear Filters
        </button>
      </div>
      <div className="text-white">
        <EmailList
          emails={filteredEmails}
          emailsPerPage={paginationNumber}
          setFolders={setFolders}
          folders={folders}
          contacts={contacts}
        ></EmailList>
      </div>

      <div className="relative">
        {/* Contactbar */}
        <div
          className={`Contactbar fixed bottom-0 right-0 h-[90%] w-64 bg-[#223047] shadow-gray-500 transform transition-transform duration-150 ease-in-out ${
            showContactbar ? "translate-x-0" : "translate-x-full"
          }`}
        >
          {/* Header */}
          <div className="flex justify-between items-center p-4 border-b border-gray-600 text-white font-bold text-lg">
            <span>Contacts</span>
          </div>

          {/* Contact Cards */}
          <div className="p-4 space-y-4 overflow-y-auto h-[calc(100%-50px)]">
            <div className="text-white hover:bg-slate-800 px-4 py-4 rounded-2xl active:scale-95" onClick={addContact}>
              <button>
              <FontAwesomeIcon icon={faUserGroup} /> Add a Friend
              </button>
            </div>
            {contacts.map((contact, index) => (
              <div
                key={index}
                className="p-4 space-y-2 bg-[#2f4562] text-white rounded-lg shadow-md hover:bg-[#3b5575] transform hover:scale-105 transition duration-200"
              >
                <div className="font-bold text-[16px]">{contact.name}</div>
                <div className="text-[14px] text-gray-300">
                  {contact.emailAddress}
                </div>
                <div className="space-x-4">
                  <button onClick={() => deleteContact(contact)}>
                    <FontAwesomeIcon icon={faTrash}></FontAwesomeIcon>
                  </button>
                  <button>
                    <FontAwesomeIcon icon={faEdit}></FontAwesomeIcon>
                  </button>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* Floating Button */}
        <button
          onClick={toggleContactbar}
          className={`contact-button fixed bottom-8 transition-all duration-300 ease-in-out ${
            isButtonClicked
              ? "bg-[#8e4b48] left-[calc(95%-256px)] " // Moves button left when clicked
              : "bg-[#bf6360] right-8" // Initial position of the button
          } text-white p-4 hover:bg-[#a55755] rounded-2xl shadow-lg transform hover:scale-105 z-50`}
        >
          <FontAwesomeIcon icon={faAddressBook} size="lg" />
        </button>
      </div>
    </div>
  );
}

export default Mainbox;
