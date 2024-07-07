import React from 'react';
import sealLogo from '../logos/little-seal.png';
import { Typography } from '@mui/material';
import { Link } from 'react-router-dom';

export const Header = () => {
  return (
    <div id="header">
      <Typography variant="h6" id="desktop-nav">
        <img src={sealLogo} alt="seal-logo"/>
        <Link to="/dashboard">Dashboard</Link>
        <Link to="/game/math">Math</Link>
        <Link to="/game/connect">Connect 4</Link>
        <Link to="/game/memory">Memorisation</Link>
      </Typography>
      <Typography variant="h6" id="mobile-nav">
        <img src={sealLogo} alt="seal-logo"/>
        <Link to="/dashboard">Da</Link>
        <Link to="/game/math">Ma</Link>
        <Link to="/game/connect">Co</Link>
        <Link to="/game/memory">Me</Link>
      </Typography>
    </div>
  );
};
