import React, { useState } from 'react';
import { Button, Box, FormControl, InputLabel, MenuItem, Select, TextField } from '@mui/material';
import { averageRating } from './PropertyCard';

const currentDate = new Date().toISOString().split('T')[0];

const formatDate = (date) => {
  const year = date.getFullYear();
  const month = (date.getMonth() + 1).toString().padStart(2, '0');
  const day = date.getDate().toString().padStart(2, '0');
  return `${year}-${month}-${day}`;
}

export const SearchField = (props) => {
  const { setFilterInfo, setListings } = props;

  const [filterText, setFilterText] = useState({});
  const [startDate, setStartDate] = useState(null);
  const [endDate, setEndDate] = useState(null);
  const [rating, setRating] = useState('');

  const inputValues = [
    { label: 'Title / City', name: 'titleCity', value: '', setValue: null },
    { label: 'Minimum Number of Bedrooms', name: 'minBedrooms', value: '', setValue: null },
    { label: 'Maximum Number of Bedrooms', name: 'maxBedrooms', value: '', setValue: null },
    { label: 'Minimum Price', name: 'minPrice', value: '', setValue: null },
    { label: 'Maximum Price', name: 'maxPrice', value: '', setValue: null },
  ];

  inputValues.forEach((input) => {
    const [value, setValue] = useState(input.value);
    input.value = value;
    input.setValue = setValue;
  });

  const handleFilter = (event) => {
    const { name, value } = event.target;
    setFilterText({ ...filterText, [name]: value });
    if (name === 'startDate') {
      console.log(filterText);
    }
  }

  const submitSearch = () => {
    let allFilter = { ...filterText };
    if (startDate && endDate) {
      allFilter = {
        ...filterText,
        startDate,
        endDate,
      }
    }
    setFilterInfo(allFilter);
  }
  const clearSearch = () => {
    setStartDate(null);
    setEndDate(null);
    setFilterText({});
    setFilterInfo({});
    inputValues.forEach((input) => {
      input.setValue('');
    });
    setRating('');
  }
  const sortOnRating = (event) => {
    const { value } = event.target;
    console.log(value);
    setListings((listings) => {
      const sortOrder = value === 'ascending' ? 1 : -1;
      return listings.slice().sort((a, b) => {
        const ratingA = averageRating(a.listing.reviews);
        const ratingB = averageRating(b.listing.reviews);
        if (isNaN(ratingA)) return sortOrder;
        if (isNaN(ratingB)) return -sortOrder;
        return (ratingA - ratingB) * sortOrder;
      });
    });
  };
  const basicFlex = {
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
  };

  const filterStyles = {
    margin: '15px 20px',
  };

  return (
    <Box sx={{ ...basicFlex, flexWrap: 'wrap' }}>
        <TextField
        label="Start Date"
        type="date"
        value={startDate ? formatDate(startDate) : ''}
        sx={{ ...filterStyles }}
        onChange={(e) => setStartDate(new Date(e.target.value))}
        inputProps={{ min: currentDate }}
        InputLabelProps={{
          shrink: true,
        }}
      />
      <TextField
        label="End Date"
        type="date"
        value={endDate ? formatDate(endDate) : ''}
        sx={{ ...filterStyles }}
        onChange={(e) => setEndDate(new Date(e.target.value))}
        inputProps={{ min: currentDate }}
        InputLabelProps={{
          shrink: true,
        }}
      />
      <Box sx={{ display: 'flex', flexWrap: 'wrap', flexDirection: 'row', gap: '20px', justifyContent: 'center' }}>
        {inputValues.map((input, index) => (
          <TextField
            key={index}
            label={input.label}
            name={input.name}
            value={input.value}
            onChange={(e) => {
              handleFilter(e);
              input.setValue(e.target.value);
            }}
          />
        ))}
        <FormControl sx={{ width: '180px' }}>
          <InputLabel id="demo-simple-select-label">Rating</InputLabel>
          <Select
            name="select-rating"
            defaultValue=""
            label="Rating"
            onChange={(e) => {
              setRating(e.target.value);
              sortOnRating(e);
            }}
            value={rating}
          >
            <MenuItem value="ascending">Rating - Ascending Order</MenuItem>
            <MenuItem value="descending">Rating - Descending Order</MenuItem>
          </Select>
        </FormControl>
      </Box>
      <Button
        variant="contained"
        size="large"
        sx={{ ...filterStyles }}
        onClick={submitSearch}
      >
        Search
      </Button>
      <Button
        variant="contained"
        size="large"
        sx={{ ...filterStyles }}
        onClick={clearSearch}
      >
        Clear Search
      </Button>
    </Box>
  )
}

export default SearchField;
