import React, { useState, useEffect } from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter, Routes, Route, useNavigate } from "react-router-dom";
import emails from "./files/emails.json";
import Sidebar from "./components/Sidebar";
import Navbar from "./components/Navbar";
import Mainbox from "./components/Mainbox";
import Inbox from "./pages/Inbox";
import Trash from "./pages/Trash";
import Starred from "./pages/Starred";
import Spam from "./pages/Spam";
import Drafts from "./pages/Drafts";
import Sent from "./pages/Sent";
import Compose from "./pages/Compose";
import FullEmailView from "./components/FullEmailView";
import NoPage from "./pages/NoPage";
import Registration from "./pages/Registration";
import { jwtDecode } from "jwt-decode";
import Login from "./pages/Login";
import SockJS from "sockjs-client";
import { Stomp } from "@stomp/stompjs";
import axios from "axios";
import { toast, ToastContainer } from "react-toastify";

// Define NoPage component to handle undefined routes
// Layout component (to wrap common layout components like Sidebar and Navbar)
const Layout = ({ emails }) => {
  const [searchQuery, setSearchQuery] = useState("");
  const [contacts, setContacts] = useState([])
  const [stompClient, setStompClient] = useState(null);
  const [folders, setFolders] = useState([]); // Initialize state as empty array
  const [userName, setUserName] = useState();
  console.log(setFolders)
  const navigate = useNavigate();
  function playNotificationSound() {
    const audio = new Audio("/notif.mp3"); // Path to the notif.mp3 file inside the 'files' folder
    audio.play().catch((error) => {
      console.error("Error playing the sound:", error);
      toast.success('New notification received!', {
        position: toast.POSITION.BOTTOM_CENTER,
        autoClose: 3000,
        hideProgressBar: true,
      });
    });
  }
  useEffect(() => {
    const token = localStorage.getItem("token");
    if (!token || typeof token !== "string") {
      navigate("/login");
      return;
    }

    const decodedToken = jwtDecode(token);
    setUserName(decodedToken.name);
    const email = decodedToken.email;

    const socket = new SockJS("http://localhost:8080/ws");
    const client = Stomp.over(socket);

    client.connect({}, () => {
      console.log("Connected to WebSocket server");
      client.subscribe(`/topic/emails/${email}`, (msg) => {
        playNotificationSound();
        console.log("Received ", msg.body);
        let newMail = JSON.parse(msg.body);
        let mailbox = newMail.mailbox;
        newMail = newMail.mailDto;
        console.log(newMail);

        setFolders((prevFolders) => {
          if (!prevFolders || prevFolders.length === 0) {
            console.error("Folders are empty or undefined");
            return prevFolders; // Early return to prevent errors
          }

          // Ensure `mailbox` exists in the folders and update the correct folder
          const updatedFolders = [...prevFolders];
          const folderIndex = updatedFolders.findIndex(
            (folder) => folder.name === mailbox
          );

          if (folderIndex === -1) {
            console.error(`Folder with name ${mailbox} not found`);
            return prevFolders; // Return unchanged folders if mailbox is not found
          }

          // Update the mails for the found folder
          const folderToUpdate = { ...updatedFolders[folderIndex] };
          folderToUpdate.mails = [...(folderToUpdate.mails || []), newMail]; // Add newMail immutably

          updatedFolders[folderIndex] = folderToUpdate; // Replace the updated folder in the array
          return updatedFolders;
        });

        console.log(folders);
      });
    });
    setStompClient(client);

    const getMails = async () => {
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

    getMails();
    const getContacts = async()=> {
      try {
        const response = await axios.get("http://localhost:8080/user/contacts", {
          headers: { Authorization: token },
        });
        console.log("I got new folders " , response);
        setContacts(response.data); // Update the folders state here
      } catch (error) {
        console.error("Error fetching emails:", error);
      }
    }
    getContacts()
  }, []); // Empty dependency array ensures this only runs once on mount

  return (
    <div className="h-screen overflow-clip bg-[#223047]">
        <Navbar setSearchQuery={setSearchQuery} username={userName} setFolders={setFolders}/>
      <div className="h-full flex">
        <ToastContainer></ToastContainer>
        <Sidebar emails={emails} client={stompClient} updateFolders={setFolders} mainFolders ={folders} contacts={contacts}/>
        <Routes>
          <Route
            path="/email/:id"
            element={<FullEmailView emails={emails} />}
          ></Route>
          <Route
            path="*"
            element={
              <Mainbox
                emails={emails}
                folders={folders}
                searchQuery={searchQuery}
                setFolders = {setFolders}
                contacts = {contacts}
                setContacts={setContacts}
              />
            }
          ></Route>
        </Routes>
      </div>
    </div>
  );
};

// The App component with routing setup
export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Main layout route */}
        <Route path="*" element={<Layout emails={emails} />}>
          <Route path="" element={<Inbox />} />
          <Route path="trash" element={<Trash />} />
          <Route path="starred" element={<Starred />} />
          <Route path="spam" element={<Spam />} />
          <Route path="drafts" element={<Drafts />} />
          <Route path="sent" element={<Sent />} />
          {/* <Route path="compose" element={<Compose />} /> */}
          <Route path="email/:id" element={<FullEmailView emails={emails} />} />
        </Route>
        <Route path="/register" element={<Registration />}></Route>
        <Route path="/login" element={<Login />}></Route>
        {/* Catch-all route for undefined paths */}
        <Route path="*" element={<NoPage />} />
      </Routes>
    </BrowserRouter>
  );
}
