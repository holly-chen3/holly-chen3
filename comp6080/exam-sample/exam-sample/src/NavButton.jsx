import React from 'react';
import { Link } from "react-router-dom";

export const NavButton = ({ page, linkName }) => {
  return (
    <>
      <Link
        style={{
          textDecoration: 'none',
          color: '#D4EBFB',
          textShadow: '1px #5690db'
        }}
        to={page}
        name={page}
      >
        {linkName}
      </Link>
    </>
  );
}