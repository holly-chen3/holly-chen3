import React from 'react';
import { render, screen } from '@testing-library/react';
import { UserReviews } from '../components/userReviews';
import { Box } from '@mui/material';

describe('UserReviews', () => {
  it('renders all user reviews with specified details', () => {
    render(
      <UserReviews
        reviews={[{
          user: 'holly@gmail.com',
          rating: '2',
          userReview: 'It was sooo good',
        }, {
          user: 'hollychen@gmail.com',
          rating: '3',
          userReview: 'Great views',
        }, {
          user: 'hollychen3@gmail.com',
          rating: '4',
          userReview: 'Really relaxing getaway',
        }]}
      />
    );

    expect(screen.getByText('holly@gmail.com')).toBeInTheDocument();
    expect(screen.getByText('Rating: 2/5')).toBeInTheDocument();
    expect(screen.getByText('Review: It was sooo good')).toBeInTheDocument();
    expect(screen.getByText('hollychen@gmail.com')).toBeInTheDocument();
    expect(screen.getByText('Rating: 3/5')).toBeInTheDocument();
    expect(screen.getByText('Review: Great views')).toBeInTheDocument();
    expect(screen.getByText('hollychen3@gmail.com')).toBeInTheDocument();
    expect(screen.getByText('Rating: 4/5')).toBeInTheDocument();
    expect(screen.getByText('Review: Really relaxing getaway')).toBeInTheDocument();
  });
  it('renders empty reviews list', () => {
    render(<UserReviews reviews={[]}/>);
    expect.objectContaining(<></>);
    expect.not.objectContaining(<Box></Box>);
  });
});
