import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Alert, Collapse, IconButton, Typography, Button, Box } from '@mui/material';
import TextField from '@mui/material/TextField';
import CloseIcon from '@mui/icons-material/Close';
import { ApiPost } from './ApiCalls';
export default function Login () {
  const [email, setEmail] = React.useState('');
  const [password, setPassword] = React.useState('');
  const [open, setOpen] = React.useState(false);
  const [errorMessage, setErrorMessage] = React.useState('');
  const navigate = useNavigate();
  const emailKeyUp = (e) => {
    setEmail(e.target.value);
  }
  const passwordKeyUp = (e) => {
    setPassword(e.target.value);
  }
  const loggedIn = (data) => {
    localStorage.setItem('token', data.token);
    localStorage.setItem('email', email);
    navigate('/HostedListings');
  }
  const notLoggedIn = (error) => {
    setOpen(true);
    setErrorMessage(error);
  }
  const apiLogin = () => {
    ApiPost('user/auth/login', { email, password }, loggedIn, notLoggedIn);
  }
  return (
    <>
    <Collapse in={open}>
      <Alert
        severity="error"
        action={
          <IconButton
            aria-label="close"
            color="inherit"
            size="small"
            onClick={() => {
              setOpen(false);
            }}
          >
            <CloseIcon fontSize="inherit" />
          </IconButton>
        }
        sx={{ mb: 2 }}
      >
        {errorMessage}
      </Alert>
    </Collapse>
    <Box sx={{ p: 3 }}>
      <Typography variant="h4" gutterBottom>
        Login
      </Typography>
      <Box sx={{ pb: 2 }}>
        <Typography variant="p" gutterBottom>
          Email:
        </Typography>
      </Box>
      <TextField
        required
        id="outlined-required"
        type="text"
        onKeyUp={emailKeyUp}
        name="login-email"
        label="Email"
        variant="outlined"
      />
      <Box sx={{ py: 2 }}>
        <Typography variant="p" gutterBottom>
          Password:
        </Typography>
      </Box>
      <TextField
        required
        id="outlined-required"
        type="text"
        onKeyUp={passwordKeyUp}
        name="login-password"
        label="Password"
        variant="outlined"
      />
      <Box sx={{ pt: 2 }}>
        <Button
          name="login"
          type="button"
          variant="contained"
          onClick={apiLogin}
        >Login</Button>
      </Box>
    </Box>
    </>
  );
}
