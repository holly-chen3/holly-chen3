import { React, useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { fileToDataUrl } from '../FileToData';
import { ApiAuthGet, ApiPut } from './ApiCalls';
import { Box, Button, CardMedia, Container, Grid, InputAdornment, TextField, Typography } from '@mui/material';

export const EditListings = () => {
  const useImages = (imageUrls) => {
    const [data, setData] = useState([]);
    useEffect(() => {
      if (imageUrls.length !== 0) {
        const listing = ApiAuthGet(`listings/${listingId}`);
        listing.then((data) => {
          const dataList = data.listing;
          setMetaData((prevData) => ({
            ...dataList.metadata,
            ...prevData,
            propertyImages: imageUrls,
          }));
          setData(metaData);
        })
      }
    }, [imageUrls]);
    return { data };
  }
  const { listingId } = useParams();
  const navigate = useNavigate();

  const [imageUrls, setImageUrls] = useState([]);
  const images = useImages(imageUrls);
  const [listingData, setListingData] = useState({
    res: {},
  });
  const [addressInfo, setAddressInfo] = useState({});
  const [metaData, setMetaData] = useState({});
  useEffect(() => {
    const loadListingDetails = async () => {
      try {
        const res = await ApiAuthGet(`listings/${listingId}`);
        const currList = res.data.listing;
        setListingData({ res: { ...currList } });
      } catch (error) {
        console.log(error.response);
      }
    }
    loadListingDetails();
  }, [listingId])

  const handleNewData = async (event) => {
    if (event.target.type === 'file') {
      try {
        const url = await fileToDataUrl(event.target.files[0]);

        if (event.target.name === 'thumbnail') {
          setListingData((prevData) => ({
            ...prevData,
            res: {
              ...prevData.res,
              thumbnail: url,
            },
          }));
        } else {
          setImageUrls([...imageUrls, url]);
        }
      } catch (error) {
        console.log(error.response);
      }
    } else {
      const name = event.target.name;
      const value = event.target.value;
      setListingData((prevData) => ({
        ...prevData,
        res: {
          ...prevData.res,
          [name]: value,
        },
      }));
    }
  };

  const handleMetadata = async (event) => {
    const listing = ApiAuthGet(`listings/${listingId}`);
    listing.then((data) => {
      const dataList = data.listing;
      const { name, value } = event.target;
      let valueInput = []
      if (name === 'amenities') {
        if (value.includes(',')) {
          valueInput = value.split(', ');
        } else {
          valueInput.push(value);
        }
      }
      if (value === '') {
        setMetaData((prevData) => ({
          ...dataList.metadata,
          ...prevData,
          [name]: dataList.metadata[name],
        }));
      } else {
        setMetaData((prevData) => ({
          ...dataList.metadata,
          ...prevData,
          [name]: valueInput,
        }));
      }
    })
  };

  const handleAddress = async (event) => {
    const listing = ApiAuthGet(`listings/${listingId}`);
    listing.then((data) => {
      const dataList = data.listing;
      const name = event.target.name;
      const value = event.target.value;
      if (value === '') {
        setAddressInfo((prevData) => ({
          ...dataList.address,
          ...prevData,
          [name]: dataList.address[name],
        }));
      } else {
        setAddressInfo((prevData) => ({
          ...dataList.address,
          ...prevData,
          [name]: value,
        }));
      }
    })
  }
  useEffect(() => {
    if (Object.keys(addressInfo).length !== 0) {
      setListingData((prevData) => {
        return ({
          res: {
            ...prevData.res,
            address: addressInfo,
          }
        });
      });
    }
    if (Object.keys(metaData).length !== 0) {
      setListingData((prevData) => ({
        ...prevData,
        res: {
          ...prevData.res,
          metadata: metaData,
        }
      }));
    }
  }, [addressInfo, metaData]);

  const handleEditSubmit = async (event) => {
    console.log(images)
    event.preventDefault()
    console.log(addressInfo);
    console.log(metaData);
    ApiPut(`listings/${listingId}`, listingData.res)
      .then((response) => {
        console.log(response);
      })
      .catch((error) => {
        console.error('Error:', error);
      });
    navigate('../HostedListings');
  }

  const basicFlex = {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
  };

  return (
    <div>
      <Box sx={{ ...basicFlex, height: '100vh' }}>
        <Container sx={{ height: 'inherit', mt: '50px' }}>
          <Container sx={{ ...basicFlex }}>
            <Typography variant={'h3'} gutterBottom>Edit your listing</Typography>
          </Container>
            <Grid container rowSpacing={1} columnSpacing={{ xs: 1, sm: 2, md: 3 }} sx={{ padding: '20px' }}>
              <Grid item xs={12} md={6} sm={6} sx={{ ...basicFlex, marginBottom: '10px' }}>
                <TextField
                  label="Title"
                  type="text"
                  sx={{ width: '60%' }}
                  name="title"
                  onChange={handleNewData}
                >
                </TextField>
              </Grid>
              <Grid item xs={12} md={6} sm={6} sx={{ ...basicFlex, marginBottom: '10px' }}>
                <TextField
                  label="Property Type"
                  type="text"
                  sx={{ width: '60%' }}
                  name="propertyType"
                  onChange={handleMetadata}
                >
                </TextField>
              </Grid>
              <Grid item xs={12} md={6} sm={6} sx={{ ...basicFlex, marginBottom: '10px' }}>
                <TextField
                  label="Street"
                  type="text"
                  sx={{ width: '60%' }}
                  name="street"
                  onChange={handleAddress}
                >
                </TextField>
              </Grid>
              <Grid item xs={12} md={6} sm={6} sx={{ ...basicFlex, marginBottom: '10px' }}>
                <TextField
                  label="City"
                  type="text"
                  sx={{ width: '60%' }}
                  name="city"
                  onChange={handleAddress}
                >
                </TextField>
              </Grid>
              <Grid item xs={12} md={6} sm={6} sx={{ ...basicFlex, marginBottom: '10px' }}>
                <TextField
                  label="State"
                  type="text"
                  sx={{ width: '60%' }}
                  name="state"
                  onChange={handleAddress}
                >
                </TextField>
              </Grid>
              <Grid item xs={12} md={6} sm={6} sx={{ ...basicFlex, marginBottom: '10px' }}>
                <TextField
                  label="Country"
                  type="text"
                  sx={{ width: '60%' }}
                  name="country"
                  onChange={handleAddress}
                >
                </TextField>
              </Grid>
              <Grid item xs={12} md={6} sm={6} sx={{ ...basicFlex, marginBottom: '10px' }}>
                <TextField
                  label="Postcode"
                  type="text"
                  sx={{ width: '60%' }}
                  name="postcode"
                  onChange={handleAddress}
                >
                </TextField>
              </Grid>
              <Grid item xs={12} md={6} sm={6} sx={{ ...basicFlex, marginBottom: '10px' }}>
                <TextField
                  label="Number of Bathrooms"
                  type="number"
                  sx={{ width: '60%' }}
                  name="numBaths"
                  onChange={handleMetadata}
                >
                </TextField>
              </Grid>
              <Grid item xs={12} md={6} sm={6} sx={{ ...basicFlex, marginBottom: '10px' }}>
                <TextField
                  label="Number of Bedrooms"
                  type="number"
                  sx={{ width: '60%' }}
                  name="numBedrooms"
                  onChange={handleMetadata}
                >
                </TextField>
              </Grid>
              <Grid item xs={12} md={6} sm={6} sx={{ ...basicFlex, marginBottom: '10px' }}>
                <TextField
                  label="Number of Beds"
                  type="number"
                  sx={{ width: '60%' }}
                  name="beds"
                  onChange={handleMetadata}
                >
                </TextField>
              </Grid>
              <Grid item xs={12} md={6} sm={6} sx={{ ...basicFlex, marginBottom: '10px' }}>
                <TextField
                  label="Amenities"
                  type="text"
                  sx={{ width: '60%' }}
                  name="amenities"
                  onChange={handleMetadata}
                >
                </TextField>
              </Grid>
              <Grid item xs={12} md={6} sm={6} sx={{ ...basicFlex, marginBottom: '10px' }}>
                <TextField
                  label="Price"
                  type="number"
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
                  sx={{ width: '60%' }}
                  name="price"
                  onChange={handleNewData}
                >
                </TextField>
              </Grid>
              <Grid item xs={12} md={6} sm={6} sx={{ ...basicFlex, marginBottom: '10px' }}>
                <TextField
                  label="Thumbnail"
                  type="file"
                  InputLabelProps={{
                    shrink: true,
                  }}
                  sx={{ width: '60%' }}
                  name="thumbnail"
                  onChange={handleNewData}
                >
                </TextField>
              </Grid>
            </Grid>
            <Grid item xs={12} md={6} sm={6} sx={{ ...basicFlex, marginBottom: '10px' }}>
              <TextField
                label="Additional Images"
                type="file"
                name="images"
                InputLabelProps={{
                  shrink: true,
                }}
                onChange={handleNewData}
              />
            </Grid>
            {imageUrls.map((imageUrl, index) => (
              <CardMedia key={index}component="img" src={imageUrl} alt={`Image ${index}`} style={{ width: '100px', height: '100px', marginRight: '10px' }} />
            ))}
            <Container sx={{ ...basicFlex, gap: '20px' }}>
              <Button name="save-changes" variant="contained" onClick={handleEditSubmit} sx={{ textAlign: 'center' }}>
                Save Changes
              </Button>
            </Container>
        </Container>
      </Box>
    </div>
  )
}
