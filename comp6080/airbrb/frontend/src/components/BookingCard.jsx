import React from 'react';
import { Button, CardActions, CardContent, Container, Paper, Typography } from '@mui/material';
import { ApiAuthPost } from '../pages/ApiCalls';
import { BookingList } from './BookingList';
import { BookingPrice } from './BookingPrice';
import { DateTextField } from './DateTextField';
export const BookingCard = (props) => {
  const { daysBooked, list, bookingList, listingId } = props;
  const [startDate, setStartDate] = React.useState(null);
  const [endDate, setEndDate] = React.useState(null);
  const [bookingMessage, setBookingMessage] = React.useState('');
  const formatDate = (date) => {
    return `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()}`;
  }
  const notBooked = (error) => {
    setBookingMessage(error);
  }
  const booked = (data) => {
    localStorage.setItem('bookingId', data.bookingId);
    setBookingMessage('The specified dates have been booked!');
  }
  const book = (availability) => {
    if (availability.length !== 0) {
      ApiAuthPost(`bookings/new/${listingId}`, localStorage.getItem('token'), availability, booked, notBooked);
    }
  };

  const handleBooking = () => {
    if (startDate === null || endDate === null) {
      setBookingMessage('Invalid Dates!');
    } else if (endDate < startDate) {
      setBookingMessage('The end date must be after the start date!')
    } else {
      const formattedStartDate = formatDate(startDate);
      const formattedEndDate = formatDate(endDate);
      const daysBooked = new Date(endDate).getDate() - new Date(startDate).getDate();
      const newAvailability = {
        dateRange: {
          start: formattedStartDate,
          end: formattedEndDate,
        },
        totalPrice: daysBooked * list?.price,
      };
      book(newAvailability);
    }
  }
  const booking = (
    <>
    {(localStorage.getItem('email') !== null && localStorage.getItem('email') !== list?.owner)
      ? (
    <Container
      sx={{
        display: 'flex',
        flexDirection: 'row',
        paddingTop: '5%',
        '@media screen and (max-width: 600px)': {
          flexDirection: 'column',
        },
      }}
    >
      <DateTextField
        name="booking-start-date"
        label="Start Date"
        date={startDate}
        formatDate={(e) => formatDate(e)}
        setStartDate={(e) => setStartDate(e)}
      />
      <DateTextField
        name="booking-end-date"
        label="End Date"
        date={endDate}
        formatDate={(e) => formatDate(e)}
        setEndDate={(e) => setEndDate(e)}
      />
    </Container>
        )
      : (<></>)}
    </>
  );
  return (
    <Paper
      sx={{
        overflow: 'scroll',
        height: '300px',
      }}
    >
      <CardContent>
        <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
          Price
        </Typography>
        <BookingPrice daysBooked={daysBooked} price={list?.price}/>
        <Container>{booking}</Container>
        <Container sx={{ display: 'flex', justifyContent: 'center' }}>
          <Typography sx={{ pt: 2 }}>{bookingMessage}</Typography>
        </Container>
      </CardContent>
      <CardActions>
        <Button name="booking-button" size="small" onClick={handleBooking} disabled={localStorage.getItem('email') === null || localStorage.getItem('email') === list?.owner}>Book Now</Button>
      </CardActions>
      <Typography sx={{ m: 1.5 }}>Scroll to See Bookings</Typography>
      <BookingList bookingList={bookingList}/>
    </Paper>
  );
}
