import React from 'react';
import { Link } from 'react-router-dom';

export const NavButton = ({ page }) => {
  let pageName = '';
  if (page === 'Hosted Listings') {
    pageName = 'HostedListings';
  } else {
    pageName = page;
  }
  return (
    <Link
    style={{
      textDecoration: 'none',
      color: 'lightblue',
      textShadow: '1px #5690db'
    }}
    to={page === 'Home' ? '' : '/' + pageName}
    name={page}
    >
      {page}
    </Link>
  )
}
