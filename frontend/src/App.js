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

// Define NoPage component to handle undefined routes
// Layout component (to wrap common layout components like Sidebar and Navbar)
const Layout = ({emails}) => {  
  const [searchQuery, setSearchQuery] = useState("")
  const navigate = useNavigate()
  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) {
      navigate('/login');
    }
  }, [navigate]);

  // If no token, do not render the rest of the component
  const token = localStorage.getItem('token');
  if (!token) {
    return null; // Or a loading state, while redirecting
  }
  const decodedToken = jwtDecode(token)
  const userName = decodedToken.name;
  console.log("name : " +userName)
  return (
    <div className="h-screen overflow-clip bg-[#003C43]">
        <Navbar setSearchQuery={setSearchQuery} username={userName}/>
      <div className="h-full flex">
        <Sidebar emails={emails}/>
        <Routes>
          <Route path="/email/:id" element={<FullEmailView emails = {emails}/>}>

          </Route>
          <Route path="*" element={<Mainbox emails={emails} searchQuery={searchQuery}/>}></Route>
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


