import React from 'react';
import { Paper, Typography } from '@mui/material';

export const BookingInfo = (props) => {
  const { timeOnline, numDaysBooked, totalProfit } = props;
  return (
    <>
    <Paper elevation={8} sx={{ p: 5 }}>
      <Typography gutterBottom variant="h4" textAlign={'center'}>
          Booking Information
      </Typography>
      <Typography gutterBottom variant="body1">
          This listing has been online for {timeOnline} {timeOnline === 1 ? 'day' : 'days'}.
      </Typography>
      <Typography gutterBottom variant="body1">
          Number of days booked: {numDaysBooked} {numDaysBooked === 1 ? 'day' : 'days'}
      </Typography>
      <Typography gutterBottom variant="body1">
          Profit: ${totalProfit}
      </Typography>
    </Paper>
    </>
  )
}

export default BookingInfo;
