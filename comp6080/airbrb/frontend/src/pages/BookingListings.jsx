import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { BookingInfo } from '../components/BookingInfo';
import { BookingHistory } from '../components/BookingHistory';
import { RequestHistory } from '../components/RequestHistory';
import { ApiAuthGet, ApiGet } from './ApiCalls';
import { Box, Grid, Typography } from '@mui/material';

export const BookingListings = () => {
  const { listingId } = useParams();
  const [listings, setListings] = useState([]);

  const [bookings, setBookings] = useState([]);
  const [listingInfo, setListingInfo] = useState({});
  const [activeBookings, setActiveBookings] = useState([]);
  const [timeOnline, setTimeOnline] = useState(null);
  const [loading, setLoading] = useState(true);

  const mapListings = (body) => {
    setListings(body.listings);
  }
  useEffect(() => {
    const getListings = () => {
      ApiGet('listings', mapListings);
    }
    getListings();
  }, []);

  useEffect(() => {
    const getBookings = async () => {
      try {
        const response = await ApiAuthGet('bookings');
        const { bookings: bookingsData } = response;
        setBookings(bookingsData.filter((booking) => booking.listingId === listingId));
        setActiveBookings(
          bookingsData.filter(
            (booking) => booking.status === 'pending' && booking.listingId === listingId
          )
        );
      } catch (error) {
        console.error(`Error fetching bookings: ${error.message}`);
      }
    };
    if (listingId) {
      getBookings();
      const foundListing = listings.find((listing) => listing.id === parseInt(listingId));
      if (foundListing) {
        setListingInfo(foundListing);
      }
    }
  }, [listings, listingId]);

  useEffect(() => {
    const loadListingDetails = async () => {
      try {
        const res = await ApiAuthGet(`listings/${listingId}`);
        const currList = res.listing;
        setListingInfo(currList);
      } catch (error) {
        console.log(error.response);
      }
    }
    loadListingDetails();
  }, [listingId]);

  // https://www.geeksforgeeks.org/how-to-calculate-the-number-of-days-between-two-dates-in-javascript/
  const getTimeOnline = (datePosted, currDay) => {
    const oneDay = 1000 * 60 * 60 * 24;
    const diffTime = currDay.getTime() - datePosted.getTime();
    return Math.round(diffTime / oneDay);
  };

  useEffect(() => {
    if (listingInfo && listingInfo.postedOn) {
      const datePosted = new Date(listingInfo.postedOn);
      const currDay = new Date();
      const calculatedTimeOnline = getTimeOnline(datePosted, currDay);
      setTimeOnline(calculatedTimeOnline);
    }
  }, [listingInfo]);

  useEffect(() => {
    if (timeOnline !== null) {
      setLoading(false);
    }
  }, [timeOnline]);

  const getDaysBooked = (bookings) => {
    const currDay = new Date();

    let daysBooked = 0;
    bookings.forEach((booking) => {
      const checkIn = new Date(booking.dateRange.start);
      const checkOut = new Date(booking.dateRange.end);
      if (booking.status === 'accepted' &&
          checkIn.getFullYear() === currDay.getFullYear()) {
        const bookingLength = getTimeOnline(checkIn, checkOut);
        daysBooked += bookingLength;
      }
    })
    return daysBooked;
  }

  const numDaysBooked = getDaysBooked(bookings);

  const getProfit = (bookings) => {
    const currDay = new Date();

    let profit = 0;
    bookings.forEach((booking) => {
      const checkIn = new Date(booking.dateRange.start);
      if (booking.status === 'accepted' &&
          checkIn.getFullYear() === currDay.getFullYear()) {
        profit += booking.totalPrice;
      }
    })
    return profit;
  }

  const totalProfit = getProfit(bookings);

  const basicFlex = {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
  };

  return (
    <>
      {!loading && (
        <>
          <Typography variant="h3" textAlign={'center'} sx={{ my: 5 }}>
            Booking History and Information for {listingInfo.title}
          </Typography>
          <Box sx={{ ...basicFlex, mx: 3 }}>
            <Grid container spacing={3}>
              <Grid item xs={4}>
                <BookingInfo
                  timeOnline={timeOnline}
                  numDaysBooked={numDaysBooked}
                  totalProfit={totalProfit}
                />
              </Grid>
              <Grid item xs={8}>
                <RequestHistory
                  bookings={bookings}
                  activeBookings={activeBookings}
                  setActiveBookings={setActiveBookings}
                />
              </Grid>
              <Grid item xs={12}>
                <BookingHistory
                  bookings={bookings}
                />
              </Grid>
            </Grid>
          </Box>
        </>
      )}
    </>
  )
}

export default BookingListings;
