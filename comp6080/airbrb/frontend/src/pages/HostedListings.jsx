import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Box, Typography, Button } from '@mui/material';
import PropertyCard from '../components/PropertyCard';
import { ApiGet, ApiAuthGet } from './ApiCalls';

export const basicFlex = {
  display: 'flex',
  justifyContent: 'center',
  alignItems: 'center',
};

export default function HostedListings () {
  const [listings, setListings] = React.useState([]);

  const navigate = useNavigate();
  const mapListings = (body) => {
    setListings(body.listings);
  }
  React.useEffect(() => {
    const getListings = () => {
      ApiGet('listings', mapListings);
    }
    getListings();
  }, []);
  const listCards = listings
    .filter((listing) => listing.owner === localStorage.getItem('email'))
    .map((listing) => {
      const getListing = ApiAuthGet(`listings/${listing.id}`)
      return (<PropertyCard key={listing.id} listing={getListing} listingId={listing.id} landing={false}/>);
    })
  return (
    <>
      <Typography variant="h3" textAlign={'center'} sx={{ my: 2 }}>
        Your Listings
      </Typography>
      <Box sx={{ ...basicFlex, my: 2 }}>
        <Button name="new-listing-button" variant="contained" onClick={ () => navigate('../CreateListings') }>
          Create a New Listing
      </Button>
      </Box>
      <Box sx={{ ...basicFlex, flexWrap: 'wrap', mx: 5 }}>
        { listCards }
      </Box>
    </>
  );
}
