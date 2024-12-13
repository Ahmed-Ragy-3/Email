import { faArrowLeft } from "@fortawesome/free-solid-svg-icons/faArrowLeft";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import React from "react";
import { useParams, useNavigate } from "react-router-dom";
import NoPage from "../pages/NoPage";

function FullEmailView({ emails }) {
  const nopage = (handleGoBack) => {
    return (
      <div
        id="main-box"
        className="h-full basis-[89%] px-6 py-6 bg-[#135D66] overflow-auto rounded-3xl shadow-inner shadow-gray-800 pb-[5%] text-white space-y-6"
      >
        <div>
          <button
            onClick={handleGoBack}
            className="text-white hover:scale-125 ease-in-out active:scale-90 transition-transform duration-300"
          >
            <FontAwesomeIcon icon={faArrowLeft} /> Back
          </button>
        </div>

        <div>No Email Found</div>
      </div>
    );
  };
  emails = emails.emails;
  const { id } = useParams();
  const email = emails[id];
  const navigate = useNavigate(); // Get the navigate function

  

  const handleGoBack = () => {
    navigate(-1); // Navigate back one step in history
  };
  if (!email) {
    return nopage(handleGoBack);
  }

  return (
    <div
      id="main-box"
      className="h-full basis-[89%] px-6 py-6 bg-[#135D66] overflow-auto rounded-3xl shadow-inner shadow-gray-800 pb-[5%] text-white space-y-6"
    >
      <button
        onClick={handleGoBack}
        className="text-white hover:scale-125 ease-in-out active:scale-90 transition-transform duration-300"
      >
        <FontAwesomeIcon icon={faArrowLeft} /> Back
      </button>
      <p className="text-2xl">From : {email.sender}</p>
      <p className="text-opacity-40 text-white">Sent : {email.date}</p>
      <p className="text-2xl">Subject : {email.subject}</p>
      <hr />
      <p>{email.body}</p>
      <hr />
    </div>
  );
}

export default FullEmailView;
