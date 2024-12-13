import React, { useState } from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter, Routes, Route } from "react-router-dom";
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

// Define NoPage component to handle undefined routes
// Layout component (to wrap common layout components like Sidebar and Navbar)
const Layout = ({emails}) => {  
  const [searchQuery, setSearchQuery] = useState("")
  return (
    <div className="h-screen overflow-clip bg-[#003C43]">
        <Navbar setSearchQuery={setSearchQuery}/>
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
        <Route path="/" element={<Layout emails = {emails} />}>
          <Route path="" element={<Inbox />} />
          <Route path="trash" element={<Trash />} />
          <Route path="starred" element={<Starred />} />
          <Route path="spam" element={<Spam />} />
          <Route path="drafts" element={<Drafts />} />
          <Route path="sent" element={<Sent />} />
          {/* <Route path="compose" element={<Compose />} /> */}
          <Route path="/email/:id" element={<FullEmailView emails={emails} />} />
        </Route>

        {/* Catch-all route for undefined paths */}
        <Route path="*" element={<NoPage />} />
      </Routes>
    </BrowserRouter>
  );
}

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(<App />);
