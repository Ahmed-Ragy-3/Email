import { faArrowLeft, faEnvelope } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

function NoPage() {
  const [rotate, setRotate] = useState(0);
const navigate = useNavigate()
  // This will rotate the icon continuously
  useEffect(() => {
    const interval = setInterval(() => {
      setRotate((prev) => prev + 5); // Increment the rotation by 5 degrees
    }, 50); // Rotate every 50ms

    return () => clearInterval(interval); // Cleanup interval on unmount
  }, []);
  const handleGoBack = ()=>{
    navigate(-1)
  }
  return (
    <div className='h-screen overflow-clip bg-[#003C43] flex items-center justify-center text-white'>
      <div className="flex flex-col items-center space-y-4">
        <div 
          className="text-[250px] transition-transform duration-500"
          style={{ transform: `rotate(${rotate}deg)` }} // Apply rotation
        >
          <FontAwesomeIcon icon={faEnvelope} />
        </div>
        <div className="text-xl mt-4">
          Page Not Found
        </div>
        <div>
            <button
                    onClick={handleGoBack}
                    className="text-white hover:scale-125 ease-in-out active:scale-90 transition-transform duration-300"
                  >
                    <FontAwesomeIcon icon={faArrowLeft} /> Back
                  </button>
        </div>
      </div>
    </div>
  );
}

export default NoPage;
