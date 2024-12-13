import React, { useState, useMemo } from "react";
import { Routes, Route } from "react-router-dom";
import Inbox from "../pages/Inbox";
import Sent from "../pages/Sent";
import Spam from "../pages/Spam";
import Starred from "../pages/Starred";
import Trash from "../pages/Trash";
import Drafts from "../pages/DraftsPage";
import FullEmailView from "./FullEmailView";

function Mainbox({ emails , searchQuery}) {
  emails = emails.emails;
  // State to track the sorting method, sort direction, and filters
  const [sortBy, setSortBy] = useState("date"); // Default sort by date
  const [isDescending, setIsDescending] = useState(false); // Default to ascending
  const [filterSender, setFilterSender] = useState(""); // Filter by sender
  const [filterSubject, setFilterSubject] = useState(""); // Filter by subject
  const [filterStartDate, setFilterStartDate] = useState(""); // Filter by start date
  const [filterEndDate, setFilterEndDate] = useState(""); // Filter by end date

  // Function to sort emails by date
  const sortByDate = (a, b) => {
    const dateA = new Date(a.date);
    const dateB = new Date(b.date);
    return isDescending ? dateA - dateB : dateB - dateA; // Toggle sorting order
  };

  // Function to sort emails by subject
  const sortBySubject = (a, b) => {
    return isDescending
      ? b.subject.localeCompare(a.subject) // Reverse alphabetical order
      : a.subject.localeCompare(b.subject); // Alphabetical order
  };

  // Function to sort emails by sender
  const sortBySender = (a, b) => {
    return isDescending
      ? b.sender.localeCompare(a.sender) // Reverse alphabetical order
      : a.sender.localeCompare(b.sender); // Alphabetical order
  };

  // Function to handle sorting based on the selected option
  const handleSort = (e) => {
    setSortBy(e.target.value);
  };

  // Toggle between ascending and descending order
  const toggleSortOrder = () => {
    setIsDescending((prev) => !prev);
  };

  // Function to clear all filters
  const clearFilters = () => {
    setFilterSender("");
    setFilterSubject("");
    setFilterStartDate("");
    setFilterEndDate("");
  };

  // Filter function to filter by sender, subject, and date range
  const filteredEmails = useMemo(() => {
    return emails
        .filter((email) => {
            const senderMatch = filterSender ? email.sender === filterSender : true;
            const subjectMatch = filterSubject ? email.subject === filterSubject : true;
            const startDateMatch = filterStartDate ? new Date(email.date) >= new Date(filterStartDate) : true;
            const endDateMatch = filterEndDate ? new Date(email.date) <= new Date(filterEndDate) : true;
            const searchMatch = 
                searchQuery ? 
                    email.subject.toLowerCase().includes(searchQuery.toLowerCase()) || 
                    email.sender.toLowerCase().includes(searchQuery.toLowerCase()) || 
                    email.body.toLowerCase().includes(searchQuery.toLowerCase()) ||
                    email.date.toLowerCase().includes(searchQuery.toLowerCase())
                : true; 
            return senderMatch && subjectMatch && startDateMatch && endDateMatch && searchMatch;
        })
        .sort(
            sortBy === "subject"
                ? sortBySubject
                : sortBy === "sender"
                ? sortBySender
                : sortByDate
        );
}, [emails, filterSender, filterSubject, filterStartDate, filterEndDate, sortBy, isDescending, searchQuery]);

  // Get unique senders and subjects for dropdowns
  const uniqueSenders = [...new Set(emails.map((email) => email.sender))];
  const uniqueSubjects = [...new Set(emails.map((email) => email.subject))];

  return (
    
    <div
      id="main-box"
      className="h-full basis-[89%] px-6 py-6 bg-[#135D66] overflow-auto rounded-3xl shadow-inner shadow-gray-800 pb-[5%]"
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
          className="p-2 bg-[#135D66] text-white rounded-md hover:bg-[#0A4D5A]"
        >
          <option value="date" className="bg-[#135D66] hover:bg-[#0A4D5A]">
            Date
          </option>
          <option value="subject" className="bg-[#135D66] hover:bg-[#0A4D5A]">
            Subject
          </option>
          <option value="sender" className="bg-[#135D66] hover:bg-[#0A4D5A]">
            Sender
          </option>
        </select>

        {/* Button to toggle ascending/descending */}
        <button
          onClick={toggleSortOrder}
          className="ml-4 p-2 bg-[#135D66] text-white rounded-md hover:bg-[#0A4D5A]"
        >
          {isDescending ? "Descending" : "Ascending"}
        </button>
      </div>

      {/* Filter options */}
      <div className="mb-4 flex items-center space-x-4">
        {/* Sender filter */}
        <div>
          <label htmlFor="sender" className="text-white mr-2">
            Sender
          </label>
          <select
            id="sender"
            value={filterSender}
            onChange={(e) => setFilterSender(e.target.value)}
            className="p-2 bg-[#135D66] text-white rounded-md hover:bg-[#0A4D5A]"
          >
            <option value="">All Senders</option>
            {uniqueSenders.map((sender, index) => (
              <option key={index} value={sender}>
                {sender}
              </option>
            ))}
          </select>
        </div>

        {/* Date filter */}
        <div>
          <label htmlFor="start-date" className="text-white mr-2">
            Date Range
          </label>
          <input
            type="date"
            id="start-date"
            value={filterStartDate}
            onChange={(e) => setFilterStartDate(e.target.value)}
            className="p-2 bg-[#135D66] text-white rounded-md hover:bg-[#0A4D5A]"
          />
          <span className="mx-2 text-white">to</span>
          <input
            type="date"
            id="end-date"
            value={filterEndDate}
            onChange={(e) => setFilterEndDate(e.target.value)}
            className="p-2 bg-[#135D66] text-white rounded-md hover:bg-[#0A4D5A]"
          />
        </div>

        {/* Subject filter */}
        <div>
          <label htmlFor="subject" className="text-white mr-2">
            Subject
          </label>
          <select
            id="subject"
            value={filterSubject}
            onChange={(e) => setFilterSubject(e.target.value)}
            className="p-2 bg-[#135D66] text-white rounded-md hover:bg-[#0A4D5A]"
          >
            <option value="">All Subjects</option>
            {uniqueSubjects.map((subject, index) => (
              <option key={index} value={subject}>
                {subject}
              </option>
            ))}
          </select>
        </div>
        <button
          onClick={clearFilters}
          className="p-2 bg-transparent hover:bg-[#0A4D5A] text-white border-2 rounded-3xl"
        >
          Clear Filters
        </button>
      </div>
      <Routes>
        <Route path="/" element={<Inbox emails={filteredEmails} />} />
        <Route path="/sent" element={<Sent emails={filteredEmails} />} />
        <Route path="/spam" element={<Spam emails={filteredEmails} />} />
        <Route path="/drafts" element={<Drafts emails={filteredEmails} />} />
        <Route path="/starred" element={<Starred emails={filteredEmails} />} />
        <Route path="/trash" element={<Trash emails={filteredEmails} />} />
      </Routes>
    </div>
  );
}

export default Mainbox;
