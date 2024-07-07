import React, { useEffect, useState } from 'react';
import PropertyCard from '../components/PropertyCard';
import { SearchField } from '../components/SearchField';
import { Box, Typography } from '@mui/material';
import { ApiAuthGet, ApiGet } from '../pages/ApiCalls';

export default function LandingPage () {
  const [listings, setListings] = useState([]);
  const [filterInfo, setFilterInfo] = useState({});

  useEffect(() => {
    const getListings = () => {
      ApiGet('listings', mapListings);
    }
    getListings();
  }, []);

  const mapListings = async (body) => {
    try {
      const detailedListings = await Promise.all(body.listings.map(async (listing) => {
        try {
          const detailsResponse = await ApiAuthGet(`listings/${listing.id}`);
          detailsResponse.listingId = listing.id;
          return detailsResponse;
        } catch (error) {
          console.error(`Error fetching details for listing ${listing.id}: ${error.message}`);
          return null;
        }
      }));
      const publishedListings = detailedListings
        .filter((element) => element.listing.published === true)
        .sort((a, b) => a.listing.title.localeCompare(b.listing.title));
      setListings(publishedListings);
    } catch (error) {
      console.error(`Error mapping listings: ${error.message}`);
    }
  };

  const filterListings = (listing, filterInfo) => {
    if (Object.keys(filterInfo).length === 0) {
      return true;
    }
    if (filterInfo.minBedrooms && listing.listing.metadata.numBedrooms < filterInfo.minBedrooms) {
      return false;
    }
    if (filterInfo.maxBedrooms && listing.listing.metadata.numBedrooms > filterInfo.maxBedrooms) {
      return false;
    }
    if (filterInfo.minPrice && parseInt(listing.listing.price) < parseInt(filterInfo.minPrice)) {
      return false;
    }
    if (filterInfo.maxPrice && parseInt(listing.listing.price) > parseInt(filterInfo.maxPrice)) {
      return false;
    }

    if (filterInfo.startDate && filterInfo.endDate) {
      const filterStartDate = new Date(filterInfo.startDate);
      const filterEndDate = new Date(filterInfo.endDate);
      const isAvailable = listing.listing.availability.some(({ start, end }) => {
        const listingStartDate = new Date(start);
        const listingEndDate = new Date(end);
        return (
          (listingStartDate <= filterStartDate && filterEndDate <= listingEndDate)
        )
      })
      if (!isAvailable) {
        return false;
      }
    }
    if (!filterInfo.titleCity) {
      return listing;
    } else if (
      !listing.listing.title.toLowerCase().includes(filterInfo.titleCity) &&
      !listing.listing.address.city.toLowerCase().includes(filterInfo.titleCity)) {
      return false;
    }
    return true;
  }

  const landingListingElements = listings
    .filter((listing) => filterListings(listing, filterInfo))
    .map((listing) => (
        <PropertyCard key={listing.listingId} listing={listing} listingId={listing.listingId} landing={true} filterInfo={filterInfo}/>
    ));

  const basicFlex = {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
  };

  return (
    <>
      <Typography variant="h2" textAlign={'center'} sx={{ my: 3 }}>
        AirBrb
      </Typography>
      <SearchField filterInfo={filterInfo} setFilterInfo={setFilterInfo} setListings={setListings} />
      <Box sx={{ ...basicFlex, flexWrap: 'wrap', mx: 5 }}>
        {landingListingElements.length !== 0
          ? (
              landingListingElements
            )
          : (
          <Typography
            variant="h5"
            textAlign={'center'}
            sx={{ ...basicFlex, height: '50vh' }}
          >
            There are no currently published listings 😔
          </Typography>
            )}
      </Box>
    </>
  );
}
