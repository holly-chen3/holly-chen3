import React from 'react';
import { TextField } from '@mui/material';

export const Square = ({ letter, verifyLetter, index }) => {
  return (
    <div id="square">
      {letter === '_' ? 
        <TextField type="text" sx={{ padding: '0' }} maxLength="1" onChange={(e) => verifyLetter(e.target.value, index)}/>
      : letter
      }
    </div>
  );
}