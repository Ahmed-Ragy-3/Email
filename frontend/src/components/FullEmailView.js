import { faArrowLeft } from "@fortawesome/free-solid-svg-icons/faArrowLeft";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React from "react";
import { useParams, useNavigate } from "react-router-dom";

function FullEmailView({ emails }) {
  console.log(emails)
  emails = emails.emails
  const { id } = useParams();
  const navigate = useNavigate();

  const handleGoBack = () => {
    navigate(-1); // Go back to the previous page
  };

  // Find the email by ID
  const email = emails.find((_, index) => index.toString() === id);

  if (!email) {
    return (
      <div className="h-full px-6 py-6 bg-[#135D66] rounded-3xl text-white">
        <button
          onClick={handleGoBack}
          className="text-white hover:scale-125 ease-in-out active:scale-90 transition-transform duration-300"
        >
          <FontAwesomeIcon icon={faArrowLeft} /> Back
        </button>
        <div>No Email Found</div>
      </div>
    );
  }

  return (
    <div
      id="main-box"
      className="h-full basis-[89%] px-6 py-6 bg-[#135D66] overflow-auto rounded-3xl shadow-inner shadow-gray-800 pb-[5%] text-white"
    >
      <button
        onClick={handleGoBack}
        className="text-white hover:scale-125 ease-in-out active:scale-90 transition-transform duration-300"
      >
        <FontAwesomeIcon icon={faArrowLeft} /> Back
      </button>
      <p className="text-2xl">From: {email.sender}</p>
      <p className="text-opacity-40 text-white">Sent: {email.date}</p>
      <p className="text-2xl">Subject: {email.subject}</p>
      <hr />
      <p>{email.body}</p>
      <hr />
    </div>
  );
}

export default FullEmailView;
