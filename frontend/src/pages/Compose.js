import React, { useState, useRef } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faFile, faPaperPlane, faTrash, faPalette } from "@fortawesome/free-solid-svg-icons";

function Compose({ closeModal , client}) {
  const [attachments, setAttachments] = useState([]);
  const [attachmentURLs, setAttachmentURLs] = useState([]);
  const [isBold, setIsBold] = useState(false);
  const [isItalic, setIsItalic] = useState(false);
  const [isUnderlined, setIsUnderlined] = useState(false);
  const [textColor, setTextColor] = useState("#000000"); // Default black color
  const [showColorPicker, setShowColorPicker] = useState(false); // Toggle for color picker visibility
  const editorRef = useRef(null); // Ref for the contenteditable div
  const upload = ()=>{
    let e = document.getElementById("text-field")
    console.log(e.innerHTML)
    console.log(client)
    console.log(attachments)
    client.send("/app/send-email", {'email' : "hello mfs"}, attachments)
  };
  // Handle file uploads
  const handleFileUpload = (e) => {
    const files = Array.from(e.target.files);
    setAttachments((prev) => [...prev, ...files]);
    setAttachmentURLs((prev) => [
      ...prev,
      ...files.map((file) => URL.createObjectURL(file)),
    ]);

    e.target.value = ""; // Clear the input value after upload
  };

  // Remove a specific attachment
  const removeAttachment = (index) => {
    // Clean up the object URL
    URL.revokeObjectURL(attachmentURLs[index]);
    setAttachments((prev) => prev.filter((_, i) => i !== index));
    setAttachmentURLs((prev) => prev.filter((_, i) => i !== index));
  };

  // Apply text style to selected text in the contenteditable div
  const applyTextStyle = (style) => {
    document.execCommand(style, false, null); // Use document.execCommand for rich text editing
    switch (style) {
      case "bold":
        setIsBold(!isBold);
        break;
      case "italic":
        setIsItalic(!isItalic);
        break;
      case "underline":
        setIsUnderlined(!isUnderlined);
        break;
      default:
        break;
    }
  };

  // Change the text color
  const changeTextColor = (color) => {
    setTextColor(color);
    document.execCommand("foreColor", false, color);
  };

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white p-6 rounded-2xl w-3/5 h-auto">
        {/* Header */}
        <div className="flex justify-between items-center mb-4">
          <h1 className="text-[19px] font-bold">New Message</h1>
          <button
            onClick={closeModal}
            className="text-gray-500 hover:text-black text-xl"
          >
            &times;
          </button>
        </div>

        {/* To Field */}
        <div className="flex items-center mb-4">
          <label
            htmlFor="to"
            className="block text-[16px] font-semibold mr-4 w-16"
          >
            To
          </label>
          <input
            id="to"
            className="flex-grow p-2 outline-none"
            placeholder="Search or add a contact"
          />
        </div>

        {/* Separator Line */}
        <hr className="mb-4" />

        {/* Subject Field */}
        <div className="flex items-center mb-4">
          <label
            htmlFor="subject"
            className="block text-[16px] font-semibold mr-4 w-16"
          >
            Subject
          </label>
          <input
            id="subject"
            type="text"
            className="flex-grow p-2 outline-none"
            placeholder="Add a subject"
          />
        </div>

        {/* Separator Line */}
        <hr className="mb-4" />

        {/* Message Body */}
        <div className="mb-4">
          <div
            ref={editorRef}
            contentEditable
            className="w-full h-40 p-2 border border-gray-300 rounded-lg outline-none"
            placeholder="Write your message here"
            role="textbox"
            aria-placeholder="Write your message here"
            id="text-field"
          />
        </div>

        {/* Attachments Section */}
        <div className="mb-4">
          <label className="block font-bold mb-2">Attachments:</label>
          <div className="border p-4 rounded-xl mb-2">
            {/* Hidden Input */}
            <input
              id="attachment"
              type="file"
              multiple
              onChange={handleFileUpload}
              className="hidden"
              accept="image/*,application/pdf"
            />
            {/* Styled Label as Button */}
            <label
              htmlFor="attachment"
              className="cursor-pointer inline-block px-4 py-2 bg-blue-500 text-white font-bold rounded hover:bg-blue-600"
            >
              Add Attachments
            </label>
            {/* Display Attachments */}
            {attachments.length === 0 ? (
              <p className="text-gray-500 mt-2">No files selected</p>
            ) : (
              <div className="space-y-2 mt-2">
                {attachments.map((file, index) => (
                  <div
                    key={index}
                    className="flex items-center justify-between bg-gray-100 p-2 rounded-lg"
                  >
                    {file.type.startsWith("image/") ? (
                      <img
                        src={attachmentURLs[index]}
                        alt={file.name}
                        className="w-16 h-16 object-cover rounded mr-2"
                      />
                    ) : (
                      <span className="truncate max-w-[60%] text-sm mr-2">
                        {file.name}
                      </span>
                    )}
                    <button
                      onClick={() => removeAttachment(index)}
                      className="text-red-500 hover:text-red-700"
                      aria-label={`Remove ${file.name}`}
                    >
                      <FontAwesomeIcon icon={faTrash} />
                    </button>
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>

        {/* When to Send Section */}
        <div className="mb-4">
          <label htmlFor="time-options" className="font-bold">
            When to send:
          </label>
          <select
            name="time-options"
            id="time-options"
            className="border-2 border-gray-400 rounded-xl p-2 hover:cursor-pointer active:scale-105"
          >
            <option value="now">Now</option>
            <option value="5-mins">5 mins</option>
            <option value="10-mins">10 mins</option>
            <option value="30-mins">30 mins</option>
            <option value="1-hour">1 hour</option>
            <option value="2-hours">2 hours</option>
            <option value="3-hours">3 hours</option>
            <option value="6-hours">6 hours</option>
            <option value="12-hours">12 hours</option>
            <option value="1-day">1 day</option>
          </select>
        </div>

        {/* Action Buttons */}
        <div className="flex justify-between items-center">
          {/* Formatting Icons */}
          <div className="flex space-x-2 text-gray-500">
            <button
              onClick={() => applyTextStyle("bold")}
              className={`p-2 hover:bg-gray-100 font-bold rounded-full px-4 ${
                isBold ? "bg-gray-300" : "active:bg-gray-400"
              }`}
            >
              <span>B</span>
            </button>
            <button
              onClick={() => applyTextStyle("italic")}
              className={`p-2 hover:bg-gray-100 italic rounded-full px-4 ${
                isItalic ? "bg-gray-300" : "active:bg-gray-400"
              }`}
            >
              <span>I</span>
            </button>
            <button
              onClick={() => applyTextStyle("underline")}
              className={`p-2 hover:bg-gray-100 underline rounded-full px-4 ${
                isUnderlined ? "bg-gray-300" : "active:bg-gray-400"
              }`}
            >
              <span>U</span>
            </button>
            {/* Color Picker Button */}
            <button
              onClick={() => setShowColorPicker(!showColorPicker)}
              className="p-2 hover:bg-gray-100 rounded-full px-4"
            >
              <FontAwesomeIcon icon={faPalette} />
            </button>
            {showColorPicker && (
              <div className="absolute mt-2 bg-white border p-2 rounded-lg shadow-lg z-50">
                <div
                  className="grid grid-cols-4 gap-2"
                  style={{ gridTemplateColumns: "repeat(4, 1fr)" }}
                >
                  {["#000000", "#FF0000", "#00FF00", "#0000FF", "#FFFF00", "#FF00FF", "#00FFFF", "#FFFFFF"].map(
                    (color) => (
                      <button
                        key={color}
                        onClick={() => changeTextColor(color)}
                        style={{ backgroundColor: color }}
                        className={`w-8 h-8 border rounded-full`}
                      />
                    )
                  )}
                </div>
              </div>
            )}
          </div>

          {/* Send and Save Buttons */}
          <div className="flex space-x-2">
            <button className="flex items-center space-x-2 px-4 py-2 bg-[#5d7fd7] text-white rounded-full hover:bg-[#415a98] active:scale-95">
              <FontAwesomeIcon icon={faFile} />
              <span>Save to drafts</span>
            </button>
            <button className="flex items-center space-x-2 px-4 py-2 bg-[#4841ff] text-white rounded-full hover:bg-[#2e2aa4] active:scale-95"
            onClick={upload}>
              <FontAwesomeIcon icon={faPaperPlane} />
              <span>Send</span>
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Compose;
