import React from 'react'
import EmailList from '../components/EmailList'

function Scheduled({emails, pagination, setFolders,folders}) {
    const scheduledEmails = emails
  return (
    <div className='text-white'>
    <EmailList emails={scheduledEmails} emailsPerPage={pagination} setFolders={setFolders}  folders = {folders}/>
    </div>
  )
}

export default Scheduled