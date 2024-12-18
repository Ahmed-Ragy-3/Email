import React from 'react'
import EmailList from '../components/EmailList'

function Scheduled({emails, pagination}) {
    const scheduledEmails = emails
  return (
    <div className='text-white'>
    <EmailList emails={scheduledEmails} emailsPerPage={pagination} />
    </div>
  )
}

export default Scheduled