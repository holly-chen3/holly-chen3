import React from 'react';
import { Container, Typography } from '@mui/material';

export const BookingList = ({ bookingList }) => {
  return (
    <Container sx={{ pb: 2 }}>
    {bookingList.map((booking) => (
      <Typography key={booking.id} variant="subtitle2" component="div" color="text.secondary">
        <Typography variant="subtitle2" component="div">
          <b>Booking ID: {booking.id}</b>
        </Typography>
        <Typography name="booking-dates" variant="subtitle2" component="div">
          Start Date: {booking.dateRange.start} End Date: {booking.dateRange.end}
        </Typography>
        <Typography variant="subtitle2" component="div">
          Booking Status: {booking.status}
        </Typography>
      </Typography>
    ))}
    </Container>
  );
}
