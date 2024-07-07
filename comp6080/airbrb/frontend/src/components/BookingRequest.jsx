import React, { useState } from 'react';
import { Alert, Button, Card, CardContent, Chip, Collapse, Grid, IconButton, Typography } from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import { ApiPut } from '../pages/ApiCalls';

export const BookingRequest = (props) => {
  const { booking, isActive, setActiveBookings } = props;

  const [open, setOpen] = useState(false);
  const [message, setMessage] = useState('');
  const [severity, setSeverity] = useState('success');

  const checkInDate = new Date(booking.dateRange.start);
  const checkOutDate = new Date(booking.dateRange.end);

  const handleAcceptRequest = async () => {
    try {
      await ApiPut(`bookings/accept/${booking.id}`);
      setOpen(true);
      setMessage('Booking was successfully accepted');
      setSeverity('success');
      setActiveBookings([]);
    } catch (error) {
      console.log(`Error: ${error.message}`);
    }
  }

  const handleDenyRequest = async () => {
    try {
      await ApiPut(`bookings/decline/${booking.id}`);
      setOpen(true);
      setMessage('Booking was successfully declined');
      setSeverity('error');
      setActiveBookings([]);
    } catch (error) {
      console.log(`Error: ${error.message}`);
    }
  }

  const getStatusChip = () => {
    if (booking.status === 'accepted') {
      return <Chip label="Status: Accepted" color="success"/>;
    } else if (booking.status === 'declined') {
      return <Chip label="Status: Declined" color="error"/>;
    }
    return null;
  };

  const format = (date) => {
    return `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()}`;
  }

  const basicFlex = {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
  };

  return (
    <Card sx={{ my: 3 }}>
      <Collapse in={open}>
        <Alert
          name="booking-request-alert"
          severity={severity}
          action={
            <IconButton
              aria-label="close"
              color="inherit"
              size="small"
              onClick={() => {
                setOpen(false);
              }}
            >
            <CloseIcon fontSize="inherit" />
            </IconButton>
          }
          sx={{ mb: 2 }}
        >
          {message}
        </Alert>
      </Collapse>
      <CardContent>
        <Grid container spacing={2} sx={{ ...basicFlex }}>
          <Grid item xs={9}>
            <Typography variant="body1">
              Request made by {booking.owner} to book from {' '}
              {format(checkInDate)} to {' '}
              {format(checkOutDate)}
            </Typography>
          </Grid>
          <Grid item xs={3}>
            {isActive
              ? (
              <>
                <Button
                  name="accept-booking"
                  sx={{ m: 1 }}
                  variant="contained"
                  color="success"
                  onClick={handleAcceptRequest}
                  >
                    Accept
                </Button>
                <Button
                    variant="contained"
                    color="error"
                    onClick={handleDenyRequest}
                  >
                    Deny
                </Button>
              </>
                )
              : (
              <Typography variant="body1" sx={{ marginLeft: '50px' }}>
                {getStatusChip()}
              </Typography>
                )}
          </Grid>
        </Grid>
      </CardContent>
    </Card>
  )
}

export default BookingRequest;
