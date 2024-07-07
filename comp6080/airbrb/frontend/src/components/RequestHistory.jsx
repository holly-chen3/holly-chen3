import React from 'react';
import { Paper, Typography } from '@mui/material';
import BookingRequest from './BookingRequest';

export const RequestHistory = (props) => {
  const { bookings, setActiveBookings } = props;

  const activeBookingRequests = bookings
    .filter((booking) => booking.status === 'pending')
    .map((booking) => {
      return (
        <BookingRequest
          key={booking.id}
          booking={booking}
          isActive={true}
          setActiveBookings={setActiveBookings}/>
      )
    })

  return (
    <Paper elevation={8} sx={{ p: 5 }}>
      <Typography gutterBottom variant="h4" textAlign={'center'}>
        Active Booking Requests
      </Typography>
      {activeBookingRequests.length !== 0
        ? (
            activeBookingRequests
          )
        : (
        <Typography variant="body1" textAlign={'center'}>
          There are no active booking requests.
        </Typography>
          )}
    </Paper>
  )
}
