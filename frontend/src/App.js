import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter, Routes, Route } from "react-router-dom";

import Sidebar from "./components/Sidebar";
import Navbar from "./components/Navbar";
import Mainbox from "./components/Mainbox";

import inbox from "./pages/inbox";
import trash from "./pages/trash";
import starred from "./pages/starred";
import spam from "./pages/spam";
import drafts from "./pages/drafts";
import sent from "./pages/sent";
import compose from "./pages/compose";

// Define NoPage component to handle undefined routes
const NoPage = () => {
  return <div>Page Not Found</div>;
};

// Layout component (to wrap common layout components like Sidebar and Navbar)
const Layout = () => {
  return (
    <div className="h-screen overflow-clip bg-[#003C43]">
      <Navbar />
      <div className="h-full flex">
        <Sidebar />
        <Mainbox />
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
        <Route path="/" element={<Layout />}>
          {/* Define your page routes here */}
          <Route path="inbox" element={<inbox />} />
          <Route path="trash" element={<trash />} />
          <Route path="starred" element={<starred />} />
          <Route path="spam" element={<spam />} />
          <Route path="drafts" element={<drafts />} />
          <Route path="sent" element={<sent />} />
          <Route path="compose" element={<compose />} />
        </Route>

        {/* Catch-all route for undefined paths */}
        <Route path="*" element={<NoPage />} />
      </Routes>
    </BrowserRouter>
  );
}

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(<App />);
