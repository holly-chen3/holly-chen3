import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import BookingRequest from '../components/BookingRequest';

jest.mock('../pages/ApiCalls');

describe('BookingRequest Component', () => {
  const mockBookingRequest = {
    id: 123,
    owner: 'Bean',
    dateRange: {
      start: '2023-11-17',
      end: '2023-11-20',
    },
    status: 'pending',
  };

  const setActiveBookingsMock = jest.fn();

  it('renders booking details', () => {    
    render(<BookingRequest booking={mockBookingRequest} isActive={true} setActiveBookings={setActiveBookingsMock}/>)
    expect(screen.getByText(`Request made by ${mockBookingRequest.owner} to book from ${mockBookingRequest.dateRange.start} to ${mockBookingRequest.dateRange.end}`)).toBeInTheDocument();
  });

  it('shows Accept and Deny buttons when isActive is true', () => { 
    render(<BookingRequest booking={mockBookingRequest} isActive={true} setActiveBookings={setActiveBookingsMock}/>)   
    expect(screen.getByRole('button', { name: 'Accept' })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: 'Deny' })).toBeInTheDocument();
  });

  it('shows Status chip when isActive is false', () => {
    const mockAcceptedBooking = { ...mockBookingRequest, status: 'accepted' };
    render(<BookingRequest booking={mockAcceptedBooking} isActive={false} setActiveBookings={setActiveBookingsMock}/>);
    expect(screen.getByText('Status: Accepted')).toBeInTheDocument();
  });


  it('triggers handleAcceptRequest when Accept button is clicked', async () => {
    render(<BookingRequest booking={mockBookingRequest} isActive={true} setActiveBookings={setActiveBookingsMock}/>);

    const acceptButton = screen.getByRole('button', { name: 'Accept' });
    fireEvent.click(acceptButton);

    await screen.findByText('Booking was successfully accepted');
    expect(setActiveBookingsMock).toHaveBeenCalledTimes(1);
  });

  it('triggers handleAcceptRequest when Accept button is clicked', async () => {
    render(<BookingRequest booking={mockBookingRequest} isActive={true} setActiveBookings={setActiveBookingsMock}/>);

    const acceptButton = screen.getByRole('button', { name: 'Deny' });
    fireEvent.click(acceptButton);

    await screen.findByText('Booking was successfully declined');
    expect(setActiveBookingsMock).toHaveBeenCalledTimes(1);
  });
});
