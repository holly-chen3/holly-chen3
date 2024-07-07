import React from 'react';
import { TextField } from '@mui/material';

export const DateTextField = ({ name, date, label, formatDate, setStartDate, setEndDate }) => {
  const currentDate = new Date().toISOString().split('T')[0];
  return (
    <TextField
    name={name}
    label={label}
    type="date"
    value={date ? formatDate(date) : ''}
    onChange={(e) => label === 'Start Date' ? setStartDate(new Date(e.target.value)) : setEndDate(new Date(e.target.value))}
    inputProps={{ min: currentDate }}
    InputLabelProps={{
      shrink: true,
    }}
    />
  );
}
