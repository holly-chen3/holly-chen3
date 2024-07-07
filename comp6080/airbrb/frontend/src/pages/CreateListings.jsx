import { React, useState } from 'react';
import { Box, Button, Container, Grid, InputAdornment, TextField, Typography } from '@mui/material';
import { fileToDataUrl } from '../FileToData';
import { ApiAuthPost } from './ApiCalls';
import { useNavigate } from 'react-router-dom';

const basicFlex = {
  display: 'flex',
  marginTop: '2%',
};

export default function CreateListings () {
  const navigate = useNavigate();
  const token = localStorage.getItem('token');
  const [createListingData, setCreateListingData] = useState({});
  const [address, setAddress] = useState({});
  const [metadata, setMetadata] = useState({});

  const handleData = async (event) => {
    let { name, value } = event.target;
    if (event.target.type === 'file') {
      const response = await fileToDataUrl(event.target.files[0]);
      value = response;
    }
    setCreateListingData({ ...createListingData, [name]: value });
  }

  const handleAddress = (event) => {
    const { name, value } = event.target;
    setAddress({ ...address, [name]: value });
  }

  const handleMetadata = (event) => {
    let { name, value } = event.target;
    if (name === 'amenities') {
      console.log('amenities');
      if (value.includes(',')) {
        value = value.split(', ');
      } else {
        value = [value];
      }
    }
    setMetadata({ ...metadata, [name]: value });
  }

  const listingSubmitted = () => {
    navigate('/HostedListings')
  }

  const handleCreateSubmit = async (event) => {
    event.preventDefault();
    const createData = { ...createListingData };
    createData.address = address;
    setMetadata({
      ...metadata,
      propertyImages: []
    });
    createData.metadata = metadata;
    console.log(createData)
    ApiAuthPost('listings/new', token, createData, listingSubmitted)
  }

  return (
    <Box sx={{ ...basicFlex, height: '100vh' }}>
      <Container
        sx={{
          ...basicFlex,
          flexDirection: 'column',
          height: '100vh',
        }}
      >
      <Typography variant={'h3'} gutterBottom sx={{ marginLeft: '5%' }}>
        Create your new Listing
      </Typography>

      <Grid container sx={{ mb: 5, textAlign: 'center' }}>
        <Grid item xs={6}>
          <TextField
            required
            name="title"
            label="Listing Title"
            sx={{ m: 1, width: '80%' }}
            onChange={handleData}
          />

          <TextField
            required
            name="street"
            label="Street Address"
            sx={{ m: 1, width: '80%' }}
            onChange={handleAddress}
          />
          <TextField
            required
            name="city"
            label="City"
            sx={{ m: 1, width: '80%' }}
            onChange={handleAddress}
          />
          <TextField
            required
            name="state"
            label="State"
            sx={{ m: 1, width: '80%' }}
            onChange={handleAddress}
          />
          <TextField
            required
            name="postcode"
            label="Post code"
            sx={{ m: 1, width: '80%' }}
            onChange={handleAddress}
          />
          <TextField
            required
            name="country"
            label="Country"
            sx={{ m: 1, width: '80%' }}
            onChange={handleAddress}
          />
          <TextField
            required
            type={'number'}
            name="price"
            label="Listing Price per Night"
            sx={{ m: 1, width: '80%' }}
            onChange={handleData}
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">$</InputAdornment>
              ),
              endAdornment: (
                <InputAdornment position="end">
                  per night
                </InputAdornment>
              ),
            }}
          />
        </Grid>
        <Grid item xs={6}>
          <TextField
            required
            name="thumbnail"
            label="Listing Thumbnail"
            type="file"
            InputLabelProps={{
              shrink: true,
            }}
            sx={{ m: 1, width: '80%' }}
            onChange={handleData}
          />
          <TextField
            required
            name="propertyType"
            label="Property Type"
            sx={{ m: 1, width: '80%' }}
            onChange={handleMetadata}
          />
          <TextField
            required
            type={'number'}
            name="numBaths"
            label="Number of Bathrooms"
            sx={{ m: 1, width: '80%' }}
            onChange={handleMetadata}
          />
          <TextField
            required
            type={'number'}
            name="numBedrooms"
            label="Number of Bedrooms"
            sx={{ m: 1, width: '80%' }}
            onChange={handleMetadata}
          />
          <TextField
            required
            type={'number'}
            name="beds"
            label="Number of Beds"
            sx={{ m: 1, width: '80%' }}
            onChange={handleMetadata}
          />
          <TextField
            required
            name="amenities"
            label="Amenities (separate with a ',')"
            sx={{ m: 1, width: '80%' }}
            onChange={handleMetadata}
          />
        </Grid>
      </Grid>

      <Button
        name="create-listing-button"
        variant="contained"
        sx={{ textAlign: 'center', margin: '0% 20%' }}
        onClick={handleCreateSubmit}
      >
        Create Your Listing
      </Button>
      </Container>
    </Box>
  )
}
