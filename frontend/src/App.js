import React from "react";

import Sidebar from "./components/sidebar";
import Navbar from "./components/Navbar";
import Mainbox from "./components/Mainbox";

const App = () => {
  return (
    <div className="h-screen overflow-clip bg-[#003C43]">
      
      <Navbar></Navbar>
      <div className="h-full flex">
        
        <Sidebar></Sidebar>
        <Mainbox></Mainbox>
      </div>
    </div>
  );
};

export default App;
