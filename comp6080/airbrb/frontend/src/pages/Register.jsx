import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Alert, Collapse, Button, Box, IconButton, TextField, Typography } from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import { ApiPost } from './ApiCalls';

export default function Register () {
  const [email, setEmail] = React.useState('');
  const [password, setPassword] = React.useState('');
  const [confirmPassword, setConfirmPassword] = React.useState('');
  const [name, setName] = React.useState('');
  const [open, setOpen] = React.useState(false);
  const [errorMessage, setErrorMessage] = React.useState('');
  const navigate = useNavigate();
  const emailKeyUp = (e) => {
    setEmail(e.target.value);
  }
  const passwordKeyUp = (e) => {
    setPassword(e.target.value);
  }
  const confirmPasswordKeyUp = (e) => {
    setConfirmPassword(e.target.value);
  }
  const nameKeyUp = (e) => {
    setName(e.target.value);
  }
  const registered = (data) => {
    localStorage.setItem('token', data.token);
    localStorage.setItem('email', email);
    navigate('/HostedListings');
  }
  const unregistered = (error) => {
    setOpen(true);
    setErrorMessage(error);
  }
  const apiRegister = () => {
    if (password !== confirmPassword) {
      console.log(password);
      console.log(confirmPassword);
      setOpen(true);
      setErrorMessage('Password does not match!');
    } else {
      console.log('hi');
      ApiPost('user/auth/register', { email, password, name }, registered, unregistered);
    }
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
        Register
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
        name="Email"
        label="Email"
        variant="outlined"
      />
      <Box sx={{ py: 2 }}>
        <Typography variant="p" gutterBottom>
          Name:
        </Typography>
      </Box>
      <TextField
        required
        id="outlined-required"
        type="text"
        onKeyUp={nameKeyUp}
        name="Name"
        label="Name"
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
        type="password"
        onKeyUp={passwordKeyUp}
        name="Password"
        label="Password"
        variant="outlined"
      />
      <Box sx={{ py: 2 }}>
        <Typography variant="p" gutterBottom>
          Confirm Password:
        </Typography>
      </Box>
      <TextField
        required
        id="outlined-required"
        type="password"
        onKeyUp={confirmPasswordKeyUp}
        name="Confirm Password"
        label="Confirm Password"
        variant="outlined"
      />
      <Box sx={{ pt: 2, color: 'pink[200]' }}>
        <Button
          name="Register"
          type="button"
          variant="contained"
          onClick={apiRegister}
        >Register</Button>
      </Box>
    </Box>
    </>
  );
}
