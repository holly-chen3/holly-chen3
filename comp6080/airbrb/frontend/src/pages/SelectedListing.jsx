import React from 'react';
import { useParams } from 'react-router-dom';
import { Box, Button, Grid, ListItem, ListItemText, Typography, TextField } from '@mui/material';
import { ApiAuthGet, ApiPut } from './ApiCalls';
import { renderStars, averageRating } from '../components/PropertyCard';
import ImageCarousel from '../components/ImageCarousel';
import { BookingCard } from '../components/BookingCard';
import { UserReviews } from '../components/userReviews';
export default function SelectedListing () {
  const { listingId, daysBooked } = useParams();
  const basicFlex = {
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
    margin: '5% 10%',
  };
  const divFlex = {
    display: 'flex',
    flexDirection: 'row',
    gap: '10px',
  }
  const [list, setList] = React.useState({});
  const avgRatingValue = averageRating(list?.reviews || []);
  const [bookingList, setBookingList] = React.useState([]);
  const [textReview, setTextReview] = React.useState('');
  const [numReview, setNumReview] = React.useState('');
  const [imageList, setImageList] = React.useState([]);

  const submitReview = () => {
    const review = {}
    review.userReview = textReview;
    review.rating = numReview;
    review.user = localStorage.getItem('email');
    ApiPut(`listings/${listingId}/review/${bookingList.find(item => item.status === 'accepted').id}`, { review });
  }
  const userBookings = async () => {
    const data = await ApiAuthGet('bookings');
    let bookings = data?.bookings;
    bookings = bookings.filter((booking) => booking.owner === localStorage.getItem('email') && booking.listingId === listingId);
    setBookingList(bookings);
  }

  const loadListingDetails = async () => {
    try {
      const res = await ApiAuthGet(`listings/${listingId}`);
      const currList = res?.listing;
      setList(currList);
      const images = [{
        imgPath: res?.listing.thumbnail,
        label: 'thumbnail'
      }];
      res?.listing?.metadata?.propertyImages?.forEach((image, index) => {
        images.push({
          imgPath: image,
          label: 'image' + index,
        })
      })
      setImageList(images);
      userBookings();
    } catch (error) {
      console.log(error.response);
    }
  }
  React.useEffect(() => {
    const interval = window.setInterval(() => {
      loadListingDetails();
    }, 1000);

    return () => clearInterval(interval);
  }, []);

  return (
      <Box sx={{ ...basicFlex }}>
          <Typography variant="h4">
            <div>{list?.title}</div>
          </Typography>
          {Object.keys(imageList).length !== 0
            ? <ImageCarousel
            images={imageList}
          />
            : <></>}
        <Box
          sx={{
            display: 'flex',
            flexDirection: 'row',
            justifyContent: 'space-between',
            '@media screen and (max-width: 600px)': {
              flexDirection: 'column',
            },
          }}
        >
          <Box>
            <Typography variant="subtitle1">
              <div>{list?.metadata?.propertyType}</div>
            </Typography>
            <Typography variant="subtitle1">
              <div>{list?.address?.street}, {list?.address?.city}</div>
              <div>{list?.address?.state}, {list?.address?.postcode}, {list?.address?.country}</div>
            </Typography>
            <div style={divFlex}>
              <Typography variant="subtitle1">
                {list?.metadata?.numBedrooms} bedrooms
              </Typography>
              <Typography variant="subtitle1">
                {list?.metadata?.beds} beds
              </Typography>
              <Typography variant="subtitle1">
                {list?.metadata?.numBaths} bathrooms
              </Typography>
            </div>
            <Grid container alignItems="center">
              <Grid item>{renderStars(avgRatingValue)}</Grid>
              <Grid item>
                <Typography variant="body1" sx={{ marginLeft: 1 }}>
                  {avgRatingValue.toFixed(1)} ({list?.reviews?.length} reviews)
                </Typography>
              </Grid>
            </Grid>
            <Typography variant="h6">
              Amenities
            </Typography>
            {list?.metadata?.amenities.map((item) => (
              <ListItem key={item} disablePadding>
                <ListItemText primary={item} />
              </ListItem>
            ))}
            {(bookingList.some(booking => booking.status === 'accepted')
              ? (<>
                <Typography variant="h6">
                  Leave a Review
                </Typography>
                <TextField
                  name="text-review"
                  label="Tell us what you think!"
                  type="text"
                  onChange={(e) => setTextReview(e.target.value)}
                />
                <TextField
                  name="rating"
                  label="Rate your experience out of 5"
                  type="number"
                  onChange={(e) => setNumReview(e.target.value)}
                />
                <br></br>
              <Button name="submit-review" onClick={submitReview}>Submit Review</Button>
              </>
                )
              : (<></>)
            )}
            <Box>
              <Typography variant="h6">
                Reviews
              </Typography>
              <UserReviews reviews={Object.keys(list).length === 0 ? [] : list.reviews}/>
            </Box>
          </Box>
          <BookingCard daysBooked={daysBooked} list={list} bookingList={bookingList} listingId={listingId}/>
        </Box>
      </Box>
  );
}
