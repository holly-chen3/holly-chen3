import React from 'react';
import { fireEvent, render, screen } from '@testing-library/react';
import SearchField from '../components/SearchField';

describe('SearchField Component', () => {

  const mockSetFilterInfo = jest.fn();
  const mockSetListings = jest.fn();

  it('renders all input fields and buttons', () => {
    render(<SearchField setFilterInfo={jest.fn()} setListings={jest.fn()} />)
    expect(screen.getByLabelText('Start Date')).toBeInTheDocument();
    expect(screen.getByLabelText('End Date')).toBeInTheDocument();
    expect(screen.getByLabelText('Title / City')).toBeInTheDocument();
    expect(screen.getByLabelText('Minimum Number of Bedrooms')).toBeInTheDocument();
    expect(screen.getByLabelText('Maximum Number of Bedrooms')).toBeInTheDocument();
    expect(screen.getByLabelText('Maximum Price')).toBeInTheDocument();
    expect(screen.getByLabelText('Minimum Price')).toBeInTheDocument();

    expect(screen.getByRole('button', { name: 'Search' })).toBeInTheDocument();
    expect(screen.getByRole('button', { name: 'Clear Search' })).toBeInTheDocument();
  });

  it('updates state when input fields are changed', () => { 
    render(<SearchField setFilterInfo={jest.fn()} setListings={jest.fn()} />)   
    const startDateInput = screen.getByLabelText('Start Date');
    fireEvent.change(startDateInput, { target: { value: '2023-11-01' } });
    expect(startDateInput.value).toBe('2023-11-01');

    const endDateInput = screen.getByLabelText('End Date');
    fireEvent.change(endDateInput, { target: { value: '2023-11-11' } });
    expect(endDateInput.value).toBe('2023-11-11');

    const titleCity = screen.getByLabelText('Title / City');
    fireEvent.change(titleCity, { target: { value: 'greenbean' } });
    expect(titleCity.value).toBe('greenbean');

    const minBed = screen.getByLabelText('Minimum Number of Bedrooms');
    fireEvent.change(minBed, { target: { value: '2' } });
    expect(minBed.value).toBe('2');

    const maxBed = screen.getByLabelText('Maximum Number of Bedrooms');
    fireEvent.change(maxBed, { target: { value: '10' } });
    expect(maxBed.value).toBe('10');

    const minPrice = screen.getByLabelText('Minimum Price');
    fireEvent.change(minPrice, { target: { value: '100' } });
    expect(minPrice.value).toBe('100');

    const maxPrice = screen.getByLabelText('Maximum Price');
    fireEvent.change(maxPrice, { target: { value: '200' } });
    expect(maxPrice.value).toBe('200');
  });

  it('clears search and resets all fields', () => {
    render(<SearchField setFilterInfo={mockSetFilterInfo} setListings={mockSetListings} />);
    
    const startDateInput = screen.getByLabelText('Start Date');
    fireEvent.change(startDateInput, { target: { value: '2023-11-11' } });

    const clearSearchButton = screen.getByRole('button', { name: 'Clear Search' });
    fireEvent.click(clearSearchButton);

    expect(mockSetFilterInfo).toHaveBeenCalledWith({});
    expect(startDateInput.value).toBe('');
  });

  it('submits search with correct filter info', () => {
    render(<SearchField setFilterInfo={mockSetFilterInfo} setListings={mockSetListings} />);
    
    const searchButton = screen.getByRole('button', { name: 'Search' });
    fireEvent.click(searchButton);
    
    expect(mockSetFilterInfo).toHaveBeenCalledWith({});
  });
});
