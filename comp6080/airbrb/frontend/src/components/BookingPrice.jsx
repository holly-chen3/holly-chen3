import React from 'react';
import { Typography } from '@mui/material';
export const BookingPrice = ({ daysBooked, price }) => {
  return (
    <>
      {(daysBooked !== '0')
        ? (<><Typography variant="h5" component="div">
        ${daysBooked * price} AUD
      </Typography>
      <Typography variant="subtitle2" component="div" color="text.secondary">
        total per stay
      </Typography></>)
        : (<><Typography variant="h5" component="div">
        ${price} AUD
      </Typography>
      <Typography variant="subtitle2" component="div" color="text.secondary">
        total per night
      </Typography></>)
      }
    </>
  );
}
