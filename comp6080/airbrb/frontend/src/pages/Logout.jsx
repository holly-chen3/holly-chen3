import React from 'react';
import { ApiAuthPost } from './ApiCalls';
import { Alert, Collapse, IconButton } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import CloseIcon from '@mui/icons-material/Close';
export default function Logout () {
  const [errorMessage, setErrorMessage] = React.useState('');
  const [open, setOpen] = React.useState(false);
  const navigate = useNavigate();
  const notLoggedOut = (error) => {
    setErrorMessage(error);
  }
  const loggedOut = () => {
    localStorage.clear();
    // TODO: navigate warning
    navigate('/');
  }
  ApiAuthPost('user/auth/logout', localStorage.getItem('token'), {}, loggedOut, notLoggedOut);
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
    </>
  );
}
