import React from 'react';
import './App.css';
import {
  BrowserRouter,
  Routes,
  Route
} from 'react-router-dom';
import AuthNav from './pages/AuthNav';
import Nav from './pages/Nav';
import Login from './pages/Login';
import Register from './pages/Register';
import Logout from './pages/Logout';
import LandingPage from './pages/LandingPage';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import HostedListings from './pages/HostedListings';
import CreateListings from './pages/CreateListings';
import { EditListings } from './pages/EditListings';
import BookingListings from './pages/BookingListings';
import SelectedListing from './pages/SelectedListing';

const colorTheme = createTheme({
  palette: {
    primary: {
      main: '#7986cb',
    },
    secondary: {
      main: '#82b1ff',
    },
  },
  typography: {
    fontFamily: 'Raleway, Arial',
  },
});

function App () {
  const [auth, setAuth] = React.useState(false);
  React.useEffect(() => {
    const interval = window.setInterval(() => {
      if (localStorage.getItem('token') !== null) {
        setAuth(true);
      } else {
        setAuth(false);
      }
    }, 1000);

    return () => clearInterval(interval);
  }, []);

  return (
    <>
      <ThemeProvider theme={colorTheme}>
        <BrowserRouter>
          {auth ? <AuthNav /> : <Nav />}
          <Routes>
            <Route path="/" element={<LandingPage />} />
            <Route path="/HostedListings" element={<HostedListings />} />
            <Route path="/CreateListings" element={<CreateListings />} />
            <Route path="/EditListings/:listingId" element={<EditListings />} />
            <Route path="/:listingId/:daysBooked" element={<SelectedListing />} />
            <Route path="/HostedListings/:listingId" element={<BookingListings />} />
            <Route path="/Login" element={<Login />} />
            <Route path="/Register" element={<Register />} />
            <Route path="/Logout" element={<Logout />}/>
          </Routes>
        </BrowserRouter>
      </ThemeProvider>
    </>
  );
}

export default App;
