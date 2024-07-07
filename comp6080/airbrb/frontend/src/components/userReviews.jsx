import React from 'react';
import { Typography, Box } from '@mui/material';

export const UserReviews = (props) => {
  const { reviews } = props;
  return (reviews.length > 0
    ? reviews.map((item, index) => (
    <Box name="user-review" key={index}>
      <Typography variant="subtitle1" sx={{ fontWeight: 'bold' }}>
        {item.user}
      </Typography>
      <Typography>
        Rating: {item.rating}/5
      </Typography>
      <Typography>
        Review: {item.userReview}
      </Typography>
    </Box>
    ))
    : (<></>)
  );
}
