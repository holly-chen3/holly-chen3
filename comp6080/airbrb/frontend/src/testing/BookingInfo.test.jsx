import React from 'react';
import { render, screen } from '@testing-library/react';
import BookingInfo from '../components/BookingInfo';

describe('BookingInfo component', () => {
  it('renders booking information correctly', () => {
    const timeOnline = 10;
    const numDaysBooked = 100;
    const totalProfit = 1000;

    render(<BookingInfo timeOnline={timeOnline} numDaysBooked={numDaysBooked} totalProfit={totalProfit}/>)

    const timeOnlineText = screen.getByText(`This listing has been online for ${timeOnline} days.`);
    const numDaysBookedText = screen.getByText(`Number of days booked: ${numDaysBooked} days`);
    const totalProfitText = screen.getByText(`Profit: $${totalProfit}`);

    expect(timeOnlineText).toBeInTheDocument();
    expect(numDaysBookedText).toBeInTheDocument();
    expect(totalProfitText).toBeInTheDocument(); 
  })

  it('renders swap between plural and singular forms', () => {
    const timeOnline = 1;
    const numDaysBooked = 1;
    const totalProfit = 1000;

    render(<BookingInfo timeOnline={timeOnline} numDaysBooked={numDaysBooked} totalProfit={totalProfit}/>)

    const timeOnlineText = screen.getByText(`This listing has been online for ${timeOnline} day.`);
    const numDaysBookedText = screen.getByText(`Number of days booked: ${numDaysBooked} day`);

    expect(timeOnlineText).toBeInTheDocument();
    expect(numDaysBookedText).toBeInTheDocument();
  })

})

