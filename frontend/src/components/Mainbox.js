import React from 'react';
import { Routes, Route } from 'react-router-dom';
import Inbox from '../pages/inbox'; // Ensure the component name is capitalized
import Compose from '../pages/compose';
import Sent from '../pages/sent';
import Spam from '../pages/spam';
import Starred from '../pages/starred';
import Trash from '../pages/trash';
import Drafts from '../pages/drafts';

function Mainbox() {
  return (
    <div
      id="main-box"
      className="h-full basis-[89%] px-6 py-6 bg-[#135D66] overflow-scroll rounded-3xl shadow-inner shadow-gray-800">
      <Routes>
        {/* Use the component with proper capitalization */}
        <Route path="/inbox" element={<Inbox />} />
        <Route path="/sent" element={<Sent />} />
        <Route path="/spam" element={<Spam/>} />
        <Route path="/drafts" element={<Drafts />} />
        <Route path="/starred" element={<Starred />} />
        <Route path="/trash" element={<Trash />} />
      </Routes>
    </div>
  );
}

export default Mainbox;
