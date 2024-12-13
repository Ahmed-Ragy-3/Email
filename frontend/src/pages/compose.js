import React from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faFile, faPaperPlane } from "@fortawesome/free-solid-svg-icons";

function Compose({ closeModal }) {
  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">

      <div className="bg-white p-6 rounded-2xl w-3/5 h-auto">

        {/* Header */}
        <div className="flex justify-between items-center mb-4">
          <h1 className="text-[19px] font-bold">New Message</h1>
          <button onClick={closeModal} className="text-gray-500 hover:text-black text-xl">
            &times;
          </button>
        </div>

        {/* To Field */}
        <div className="flex items-center mb-4">
          <label htmlFor="to" className="block text-[16px] font-semibold mr-4 w-16">
            To
          </label>
          <input
            id="to"
            className="flex-grow p-2 outline-none"
            placeholder="Search or add a contact"
          />
        </div>

        {/* Separator Line */}
        <hr className="mb-4" />

        {/* Subject Field */}
        <div className="flex items-center mb-4">
          <label htmlFor="subject" className="block text-[16px] font-semibold mr-4 w-16">
            Subject
          </label>
          <input
            id="subject"
            type="text"
            className="flex-grow p-2 outline-none"
            placeholder="Add a subject"
          />
        </div>

        {/* Separator Line */}
        <hr className="mb-4" />

        {/* Message Body */}
        <div className="mb-4">
          <textarea
            className="w-full h-40 p-2 outline-none"
            placeholder="Write your message here"
          />
        </div>

        {/* Action Buttons */}
        <div className="flex justify-between items-center">

          {/* Formatting Icons */}
          <div className="flex space-x-2 text-gray-500">
            <button className="p-2 hover:bg-gray-100 rounded">
              <span>B</span>
            </button>
            <button className="p-2 hover:bg-gray-100 rounded">
              <span>I</span>
            </button>
            <button className="p-2 hover:bg-gray-100 rounded">
              <span>U</span>
            </button>
          </div>

          {/* Send and Save Buttons */}
          <div className="flex space-x-2">
            <button className="flex items-center space-x-2 px-4 py-2 bg-[#5d7fd7] text-white rounded-full hover:bg-[#415a98]">
              <FontAwesomeIcon icon={faFile} />
              <span>Save to drafts</span>
            </button>
            <button className="flex items-center space-x-2 px-4 py-2 bg-[#4841ff] text-white rounded-full hover:bg-[#2e2aa4]">
              <FontAwesomeIcon icon={faPaperPlane} />
              <span>Send</span>
            </button>
          </div>

        </div>

      </div>

    </div>
  );
}

export default Compose;