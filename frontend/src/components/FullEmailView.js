import { faArrowLeft } from "@fortawesome/free-solid-svg-icons/faArrowLeft";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import axios from "axios";
import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";

function FullEmailView() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [incoming, setIncoming] = useState([]);
  
  const handleGoBack = () => {
    navigate(-1); // Go back to the previous page
  };

  // Fetch email data by ID
  useEffect(() => {
    const getEmail = async () => {
      const response = await axios.get(`http://localhost:8080/mail/getmail?id=${id}`);
      console.log("I AM COMING ", response.data);
      setIncoming(response.data);
    };
    getEmail();
  }, [id]);

  const [email, setEmail] = useState([]);
  const [attachments, setAttachments] = useState([]);

  useEffect(() => {
    setEmail(incoming.mailDto);
    setAttachments(incoming.attachments || []);
    console.log(incoming);
  }, [incoming]);

  if (!email) {
    return (
      <div className="h-full px-6 py-6 bg-[#2f4562] rounded-3xl text-white">
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
      className="h-full basis-[89%] px-6 py-6 bg-[#2f4562] overflow-auto rounded-3xl shadow-inner shadow-gray-800 pb-[5%] text-white space-y-4"
    >
      <button
        onClick={handleGoBack}
        className="text-white hover:scale-125 ease-in-out active:scale-90 transition-transform duration-300"
      >
        <FontAwesomeIcon icon={faArrowLeft} /> Back
      </button>
      <p className="text-2xl">From: {email.senderAddress}</p>
      <p className="text-opacity-40 text-white">Sent: {email.dateString}</p>
      <p className="text-2xl">Subject: {email.subject}</p>
      <hr />
      <div dangerouslySetInnerHTML={{ __html: email.content }} className="text-3xl py-4"></div>
      <hr />
      
      {/* Display Attachments */}
      {attachments.length > 0 && (
        <div className="mt-6">
          <p className="text-xl font-bold">Attachments:</p>
          <ul className="list-none">
            {attachments.map((attachment, index) => (
              <li key={index} className="py-2">
                <a
                  href={`data:${attachment.type};base64,${attachment.file}`}
                  download={attachment.name}
                  className="text-blue-500 hover:underline"
                >
                  {attachment.name}
                </a>
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
}

export default FullEmailView;
