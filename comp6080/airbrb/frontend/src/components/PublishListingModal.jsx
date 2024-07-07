import React, { useState } from 'react';
import { Box, Button, Container, Modal, TextField, Typography } from '@mui/material';
import { ApiPut } from '../pages/ApiCalls';

const currentDate = new Date().toISOString().split('T')[0];

const formatDate = (date) => {
  const year = date.getFullYear();
  const month = (date.getMonth() + 1).toString().padStart(2, '0');
  const day = date.getDate().toString().padStart(2, '0');
  return `${year}-${month}-${day}`;
}

export const PublishListingModal = (props) => {
  const { listingId, modalOpen, handleClose, published } = props;
  const style = {
    position: 'absolute',
    display: 'flex',
    alignItems: 'center',
    flexDirection: 'column',
    paddingTop: '20px',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    bgcolor: 'background.paper',
    borderRadius: '10px',
    gap: '15px',
    height: '50%',
    width: '50%'
  };

  const [startDate, setStartDate] = useState(null);
  const [endDate, setEndDate] = useState(null);
  const [availability, setAvailability] = useState([]);
  const [publishMessage, setPublishMessage] = useState('');

  const handleAvailability = () => {
    if (startDate === null || endDate === null) {
      setPublishMessage('Invalid Dates!');
    } else if (endDate < startDate) {
      setPublishMessage('The end date must be after the start date!')
    } else {
      const formattedStartDate = formatDate(startDate);
      const formattedEndDate = formatDate(endDate);
      const isOverlap = availability.some((range) => {
        const rangeStart = new Date(range.start);
        const rangeEnd = new Date(range.end);
        const currStart = new Date(formattedStartDate);
        const currEnd = new Date(formattedEndDate);
        return (currStart >= rangeStart && currStart <= rangeEnd) ||
               (currEnd >= rangeStart && currEnd <= rangeEnd) ||
               (rangeStart >= currStart && rangeStart <= currEnd) ||
               (rangeEnd >= currStart && rangeEnd <= currEnd);
      })
      if (isOverlap) {
        setPublishMessage('Overlapping Dates!');
      } else {
        const newAvailability = { start: formattedStartDate, end: formattedEndDate };
        setAvailability((prevAvailability) => [...prevAvailability, newAvailability]);
        setPublishMessage('The specified dates have been published!')
      }
    }
  }

  const publish = () => {
    if (availability.length !== 0) {
      published();
      try {
        ApiPut(`listings/publish/${listingId}`, { availability });
      } catch (error) {
        console.log(error.response.data);
      }
    }
    handleClose();
  };

  return (
    <Modal
      open={modalOpen}
      onClose={handleClose}
      aria-labelledby="modal-modal-title"
      aria-describedby="modal-modal-description"
    >
      <Box sx={style}>
        <Typography id="modal-modal-title" variant="h6" textAlign={'center'}>
          Select Available Dates!
        </Typography>
        <Container sx={{ display: 'flex', flexDirection: 'column', gap: '20px' }}>
          <TextField
            name="publish-start-date"
            label="Start Date"
            type="date"
            value={startDate ? formatDate(startDate) : ''}
            onChange={(e) => setStartDate(new Date(e.target.value))}
            inputProps={{ min: currentDate }}
            InputLabelProps={{
              shrink: true,
            }}
          />
          <TextField
            name="publish-end-date"
            label="End Date"
            type="date"
            value={endDate ? formatDate(endDate) : ''}
            onChange={(e) => setEndDate(new Date(e.target.value))}
            inputProps={{ min: currentDate }}
            InputLabelProps={{
              shrink: true,
            }}
          />
        </Container>
        <Container sx={{ display: 'flex', justifyContent: 'center' }}>
          <p>{publishMessage}</p>
        </Container>
        <Container sx={{ display: 'flex', justifyContent: 'center', gap: '30px' }}>
          <Button name="publish-listing" variant="outlined" color="success" onClick={handleAvailability}>
            Publish
          </Button>
          <Button name="close-publish-modal" variant="outlined" color="error" onClick={publish}>
            Close
          </Button>
        </Container>
      </Box>
    </Modal>
  );
};
