import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import HotelOutlinedIcon from '@mui/icons-material/HotelOutlined';
import ShowerOutlinedIcon from '@mui/icons-material/ShowerOutlined';
import StarRateIcon from '@mui/icons-material/StarRate';
import { Button, Card, CardActions, CardContent, CardMedia, Grid, ListItemIcon, ListItemText, styled, Typography } from '@mui/material';
import { ApiDelete, ApiPut } from '../pages/ApiCalls';
import { PublishListingModal } from './PublishListingModal';

export const averageRating = (reviews) => {
  if (!Array.isArray(reviews) || reviews.length === 0) {
    return 0;
  }
  const sumRating = reviews.reduce((total, review) => {
    const ratingVal = parseInt(review.rating);
    if (!isNaN(ratingVal)) {
      return total + ratingVal;
    } else {
      return total;
    }
  }, 0)
  const averageRating = sumRating / reviews.length;
  return averageRating;
}

const GoldStar = styled(StarRateIcon)({
  color: 'gold',
});

const GreyStar = styled(StarRateIcon)({
  color: 'grey',
});

const InlineFlexDiv = styled('div')({
  display: 'inline-flex',
});

// https://css-tricks.com/five-methods-for-five-star-ratings/
export const renderStars = (avgRating) => {
  const maxStars = 5;
  const fullStars = Math.floor(avgRating);
  const hasHalfStar = avgRating % 1 !== 0;

  return Array.from({ length: maxStars }, (_, idx) => {
    if (idx < fullStars) {
      return <GoldStar key={idx}/>;
    } else if (hasHalfStar && idx === fullStars) {
      const fraction = avgRating - fullStars;
      const clipPathPercentage = (fraction * 100).toFixed(2);
      const clipPath = `polygon(0% 0%, ${clipPathPercentage}% 0%, ${clipPathPercentage}% 100%, 0% 100%)`;

      return (
        <InlineFlexDiv key={idx}>
          <GoldStar
            sx={{ clipPath, marginRight: '-1em' }}
          />
          <GreyStar/>
        </InlineFlexDiv>
      );
    } else {
      return <GreyStar key={idx}/>;
    }
  })
}

export default function PropertyCard (props) {
  const { listingId, listing, landing, filterInfo } = props;
  const [list, setList] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const data = await listing;
        setList(data.listing);
        if (list?.published) {
          unpublishButton();
        } else {
          publishButton();
        }
      } catch (error) {
        console.error(`Error fetching data:${error}`);
      }
    }
    fetchData();
  }, [listing]);

  const spanStyle = {
    marginTop: '-50px',
    fontFamily: 'Arial'
  };

  const avgRatingValue = averageRating(list?.reviews || []);

  const navigate = useNavigate();

  const handleDelete = () => {
    ApiDelete(`listings/${listingId}`)
    window.location.reload();
  }

  const handleEditListing = () => { navigate(`../EditListings/${listingId}`); }

  const [publishListing, setPublishListing] = useState(false);
  const [modalOpen, setModalOpen] = useState(false);
  const [publishButtonText, setPublishButtonText] = useState('');

  useEffect(() => {
    const fetchListings = async () => {
      try {
        const data = await props.listing;
        setList(data.listing);
      } catch (error) {
        console.log(`Error fetching data: ${error}`);
      }
    }
    fetchListings();
  }, [props.listing])

  const handlePublishListing = () => {
    if (publishButtonText === 'Unpublish') {
      publishButton();
      handleUnpublishListing();
    } else {
      if (publishListing === false) {
        setPublishListing(true);
        setModalOpen(true);
      }
    }
  }
  const handleCloseModal = async () => {
    setPublishListing(false);
    setModalOpen(false);
  }

  const handlePublished = () => {
    unpublishButton();
  }

  const handleUnpublishListing = () => {
    ApiPut(`listings/unpublish/${listingId}`);
  };

  const unpublishButton = () => {
    setPublishButtonText('Unpublish');
  };

  const publishButton = () => {
    setPublishButtonText('Publish');
  };
  const handleView = () => {
    if (Object.keys(filterInfo).length === 0) {
      navigate(`/${listingId}/${0}`);
    } else {
      const daysBooked = new Date(filterInfo.endDate).getDate() - new Date(filterInfo.startDate).getDate();
      navigate(`/${listingId}/${daysBooked}`);
    }
  }

  const handleBookingsMore = () => {
    navigate(`./${listingId}`);
  };
  return (
  <>
  <Card
    sx={{ height: '100%', display: 'flex', flexDirection: 'column', margin: '10px' }}
    className="Listing-box"
  >
    <CardMedia
      name="card-thumbnail"
      component="img"
      image={list?.thumbnail}
      height="150"
    />
    <CardContent sx={{ flexGrow: 1 }}>
      <Typography name="card-title" variant="h5">
        {list?.title}
      </Typography>
      <Typography variant="subtitle1">
        <div>{list?.metadata?.propertyType}</div>
      </Typography>
      <Typography variant="subtitle1">
        <div>{list?.address?.street}, {list?.address?.city}</div>
        <div>{list?.address?.state}, {list?.address?.postcode}, {list?.address?.country}</div>
      </Typography>
      <Typography variant="subtitle1">
        <div>${list?.price} per night</div>
      </Typography>
      <Grid container alignItems="center">
        <Grid item>{renderStars(avgRatingValue)}</Grid>
        <Grid item>
          <Typography variant="body1" sx={{ marginLeft: 1 }}>
            {avgRatingValue.toFixed(1)} ({list?.reviews?.length} reviews)
          </Typography>
        </Grid>
      </Grid>
      <div>
        <div>
          <ListItemIcon>
            <HotelOutlinedIcon/>
          </ListItemIcon>
          <span style={{ ...spanStyle }}>{list?.metadata?.beds} beds</span>
        </div>
      <div>
        <ListItemIcon>
          <ShowerOutlinedIcon/>
        </ListItemIcon>
        <span style={{ ...spanStyle }}>{list?.metadata?.numBaths} bathrooms</span>
      </div>
      <div>
        <div style={{ display: 'flex', flexDirection: 'row', flexWrap: 'wrap', justifyContent: 'space-between' }}>{list?.metadata?.amenities.map((amenity) => (
          <ListItemText key={amenity}>
            {amenity}
          </ListItemText>
        ))}</div>
      </div>
      </div>
    </CardContent>
    {!landing
      ? (
        <CardActions>
          <Button onClick={handleDelete}>
            Delete
          </Button>
          <Button name="edit-button" onClick={handleEditListing}>
            Edit
          </Button>
          <Button name="publish-button" onClick={handlePublishListing}>
            {publishButtonText}
          </Button>
          <Button name="bookings-button" onClick={handleBookingsMore}>
            Bookings
          </Button>
        </CardActions>
        )
      : (
        <CardActions>
          <Button
            name="view-listing"
            sx={{ textAlign: 'center' }}
            onClick={handleView}
          >
            View
          </Button>
        </CardActions>
        )
      }
    {publishListing && (
      <PublishListingModal
        modalOpen={modalOpen}
        handleClose={handleCloseModal}
        listingId={listingId}
        published={handlePublished}
      />
    )}
  </Card>
  </>
  )
}
