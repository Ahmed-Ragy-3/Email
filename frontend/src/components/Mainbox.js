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

function Mainbox({ folders, searchQuery , setFolders}) {
  console.log("folders in mainbox are ", folders);
  let path = window.location.pathname;
  console.log(path);
  let newmails;

  if (path === "/") {
    newmails = folders[0]?.mails || [];
  } else if (path === "/drafts") {
    newmails = folders[1]?.mails || [];
  } else if (path === "/sent") {
    newmails = folders[2]?.mails || [];
  } else if (path === "/trash") {
    newmails = folders[3]?.mails || [];
  } else if (path === "/starred") {
    newmails = folders[4]?.mails || [];
  } else if (path === "/scheduled") {
    newmails = folders[5]?.mails || [];
  } else if (path === "/spam") {
    newmails = folders[6]?.mails || [];
  } else if (path === "/scheduled") {
    newmails = folders[7]?.mails || [];
  }
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
    console.log(dateA, dateB);
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
    className="p-2 bg-[#824e4e] text-white rounded-md hover:bg-[#714848]"
  >
    <option value="dateString" className="bg-[#824e4e] hover:bg-[#714848]">
      Date
    </option>
    <option value="subject" className="bg-[#824e4e] hover:bg-[#714848]">
      Subject
    </option>
    <option value="sender" className="bg-[#824e4e] hover:bg-[#714848]">
      Sender
    </option>
    <option value="importance" className="bg-[#824e4e] hover:bg-[#714848]">
      Importance
    </option>
  </select>

  <button
    onClick={toggleSortOrder}
    className="ml-4 p-2 bg-[#824e4e] text-white rounded-md hover:bg-[#714848]"
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
            className="p-2 bg-[#824e4e] text-white rounded-md hover:bg-[#714848]"
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
            className="p-2 bg-[#824e4e] text-white rounded-md hover:bg-[#714848]"
          />
          <span className="mx-2 text-white">to</span>
          <input
            type="date"
            id="end-date"
            value={filterEndDate}
            onChange={(e) => setFilterEndDate(e.target.value)}
            className="p-2 bg-[#824e4e] text-white rounded-md hover:bg-[#714848]"
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
            className="p-2 bg-[#824e4e] text-white rounded-md hover:bg-[#714848]"
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
            className="p-2 bg-[#824e4e] text-white rounded-md hover:bg-[#714848]"
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
          <select name="pagination-select" value={paginationNumber} id="" className="p-2 bg-[#824e4e] text-white rounded-md hover:bg-[#714848]" onChange={(e)=>{
              setpaginationNumber(Number(e.target.value))
          }}>
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
          className="p-2 bg-transparent hover:bg-[#714848] text-white border-2 rounded-3xl"
        >
          Clear Filters
        </button>
      </div>
          <div className="text-white">
            <EmailList emails={filteredEmails} emailsPerPage={paginationNumber} setFolders={setFolders} folders={folders}></EmailList>
          </div>
    </div>
  );
}

export default Mainbox;
