import React from 'react';
import { render, screen } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import { NavButton } from '../components/NavButton';

describe('NavButton', () => {
  it('renders button with Home Button Option', () => {
    render(
      <BrowserRouter>
        <NavButton page={'Home'} />
      </BrowserRouter>
    );

    expect(screen.getByText('Home')).toBeInTheDocument();
    expect(screen.getByRole('link', { name: /Home/i })).toBeInTheDocument();
  });
  it('renders button with Hosted Listings Button Option', () => {
    render(
      <BrowserRouter>
        <NavButton page={'Hosted Listings'} />
      </BrowserRouter>
    );

    expect(screen.getByText('Hosted Listings')).toBeInTheDocument();
    expect(screen.getByRole('link', { name: /Hosted Listings/i })).toBeInTheDocument();
  });
  it('renders button with Logout Button Option', () => {
    render(
      <BrowserRouter>
        <NavButton page={'Logout'} />
      </BrowserRouter>
    );

    expect(screen.getByText('Logout')).toBeInTheDocument();
    expect(screen.getByRole('link', { name: /Logout/i })).toBeInTheDocument();
  });
  it('renders button with Login Button Option', () => {
    render(
      <BrowserRouter>
        <NavButton page={'Login'} />
      </BrowserRouter>
    );

    expect(screen.getByText('Login')).toBeInTheDocument();
    expect(screen.getByRole('link', { name: /Login/i })).toBeInTheDocument();
  });
  it('renders button with Register Button Option', () => {
    render(
      <BrowserRouter>
        <NavButton page={'Register'} />
      </BrowserRouter>
    );

    expect(screen.getByText('Register')).toBeInTheDocument();
    expect(screen.getByRole('link', { name: /Register/i })).toBeInTheDocument();
  });
  it('triggers onClick when clicked', () => {
    render(
      <BrowserRouter>
        <NavButton page={'Home'} />
      </BrowserRouter>
    );
    expect(screen.getByRole('link', { to: '/' })).toBeInTheDocument();
  });
});
