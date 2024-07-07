import React from 'react';
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { Header } from './Header';
import { Home } from './Home';
import { Blanko } from './Blanko';
import { Slido } from './Slido';
import { Tetro } from './Tetro';
import { ThemeProvider, createTheme } from '@mui/material/styles';

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

export const Website = () => {
  return (
    <ThemeProvider theme={colorTheme}>
      <BrowserRouter>
        <div>
          <Header />
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/blanko" element={<Blanko />} />
            <Route path="/slido" element={<Slido />} />
            <Route path="/tetro" element={<Tetro />} />
          </Routes>
          <div id="footer"></div>
        </div>
      </BrowserRouter>
    </ThemeProvider>
  );
}