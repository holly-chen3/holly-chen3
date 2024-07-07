import { fireEvent, render, screen } from '@testing-library/react';
import { PublishListingModal } from '../components/PublishListingModal'

describe('PublishListingModal Component', () => {
  const mockProps = {
    listingId: 123, 
    modalOpen: true, 
    handleClose: jest.fn(),
    published: jest.fn(),
  }

  beforeEach(() => {
    render(<PublishListingModal { ...mockProps }/>)
  })

  it('renders modal title', () => {
    expect(screen.getByText('Select Available Dates!')).toBeInTheDocument();
  })

  it('displays the start date and end date fields', () => {
    expect(screen.getByLabelText('Start Date'));
    expect(screen.getByLabelText('End Date'))
  })

  it('updates start date field when changed', () => {
    const startDateInput = screen.getByLabelText('Start Date');
    fireEvent.change(startDateInput, { target: {value: '2023-11-17' } })
    expect(startDateInput.value).toBe('2023-11-17');
  })

  it('updates end date when change', () => {
    const endDateInput = screen.getByLabelText('End Date');
    fireEvent.change(endDateInput, { target: {value: '2023-11-20' } })
    expect(endDateInput.value).toBe('2023-11-20');
  })

  it('displays published message when Publish button is clicked', () => {
    const startDateInput = screen.getByLabelText('Start Date');
    fireEvent.change(startDateInput, { target: {value: '2023-11-17' } })
    const endDateInput = screen.getByLabelText('End Date');
    fireEvent.change(endDateInput, { target: {value: '2023-11-20' } })
    const publishButton = screen.getByRole('button', { name: 'Publish' });
    fireEvent.click(publishButton);
    expect(screen.getByText('The specified dates have been published!')).toBeInTheDocument();
  })

  it('displays incorrect ordering message when Publish button is clicked', () => {
    const startDateInput = screen.getByLabelText('Start Date');
    fireEvent.change(startDateInput, { target: {value: '2023-11-17' } })
    const endDateInput = screen.getByLabelText('End Date');
    fireEvent.change(endDateInput, { target: {value: '2023-11-16' } })
    const publishButton = screen.getByRole('button', { name: 'Publish' });
    fireEvent.click(publishButton);
    expect(screen.getByText('The end date must be after the start date!')).toBeInTheDocument();
  })

  it('displays invalid message when Publish button is clicked', () => {
    const publishButton = screen.getByRole('button', { name: 'Publish' });
    fireEvent.click(publishButton);
    expect(screen.getByText('Invalid Dates!')).toBeInTheDocument();
  })

  it('closes modal when Close button is clicked', () => {
    const closeButton = screen.getByRole('button', { name: 'Close' });
    fireEvent.click(closeButton);
    expect(mockProps.handleClose).toHaveBeenCalledTimes(1);
  })

})
