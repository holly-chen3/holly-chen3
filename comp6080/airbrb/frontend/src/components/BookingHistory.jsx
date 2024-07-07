import React from 'react';
import { Typography, Paper } from '@mui/material';
import BookingRequest from './BookingRequest';

export const BookingHistory = (props) => {
  const { bookings } = props;

  const prevBookingInfo = bookings
    .filter((booking) => booking.status !== 'pending')
    .map((booking) => {
      return (
        <BookingRequest key={booking.id} active={false} booking={booking} />
      )
    })

  return (
    <Paper elevation={5} sx={{ p: 5 }}>
      <Typography variant="h4" gutterBottom textAlign={'center'}>
        History of Booking Requests
      </Typography>
      {prevBookingInfo.length !== 0
        ? (
            prevBookingInfo
          )
        : (
        <Typography variant="body1" textAlign={'center'}>
          This listing has no booking history.
        </Typography>
          )}
    </Paper>
  )
}
