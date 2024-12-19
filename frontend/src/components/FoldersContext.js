import React, { createContext, useContext, useState } from 'react'

const FoldersContext = createContext();

export const useFolders = () => useContext(FoldersContext);

export const FoldersProvider = ({ children }) => {
    const [folders, setFolders] = useState([]);
  
    return (
      <FoldersContext.Provider value={{ folders, setFolders }}>
        {children}
      </FoldersContext.Provider>
    );
  };