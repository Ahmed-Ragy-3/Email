import React, { useState , useEffect } from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter, Routes, Route, useNavigate } from "react-router-dom";
import emails from './files/emails.json'
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
// Define NoPage component to handle undefined routes
// Layout component (to wrap common layout components like Sidebar and Navbar)
const Layout = ({emails}) => {  
  const [searchQuery, setSearchQuery] = useState("");
  const [stompClient, setStompClient] = useState(null);
  const [folders, setFolders] = useState([]); // Initialize state as empty array
  const [userName, setUserName] = useState();
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token || typeof token !== 'string') {
      navigate('/login');
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
        console.log("Received ", msg.body);
        const newMail = JSON.parse(msg.body);
        console.log(newMail)
        setFolders((prevFolders) => {
          if (!prevFolders || prevFolders.length === 0) {
            console.error("Folders are empty or undefined");
            return prevFolders; // Early return to prevent errors
          }
        
          // Ensure `mails` exists
          const updatedFolders = [...prevFolders];
          const firstFolder = { ...updatedFolders[0] };
          firstFolder.mails = [...(firstFolder.mails || []), newMail]; // Add newMail immutably
        
          updatedFolders[0] = firstFolder;
          return updatedFolders;
        });
        console.log(folders)
      });
    });
    setStompClient(client);
  
    const getMails = async () => {
      try {
        const response = await axios.get("http://localhost:8080/mail/all", {
          headers: { "Authorization": token }
        });
        console.log(response);
        setFolders(response.data); // Update the folders state here
      } catch (error) {
        console.error("Error fetching emails:", error);
      }
    };
  
    getMails();
  }, []); // Empty dependency array ensures this only runs once on mount
  
  return (
    <div className="h-screen overflow-clip bg-[#223047]">
        <Navbar setSearchQuery={setSearchQuery} username={userName}/>
      <div className="h-full flex">
        <Sidebar emails={emails} client = {stompClient} folders={folders}/>
        <Routes>
          <Route path="/email/:id" element={<FullEmailView emails = {emails}/>}>

          </Route>
          <Route path="*" element={<Mainbox emails={emails} folders ={folders} searchQuery={searchQuery}/>}></Route>
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
        <Route path="*" element={<Layout emails = {emails} />}>
          <Route path="" element={<Inbox />} />
          <Route path="trash" element={<Trash />} />
          <Route path="starred" element={<Starred />} />
          <Route path="spam" element={<Spam />} />
          <Route path="drafts" element={<Drafts />} />
          <Route path="sent" element={<Sent />} />
          {/* <Route path="compose" element={<Compose />} /> */}
          <Route path="email/:id" element={<FullEmailView emails={emails} />} />
        </Route>
        <Route path="/register" element={<Registration/>}></Route>
        <Route path="/login" element={<Login/>}></Route>
        {/* Catch-all route for undefined paths */}
        <Route path="*" element={<NoPage />} />
      </Routes>
    </BrowserRouter>
  );
}


