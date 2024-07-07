import { Button, Typography } from '@mui/material';
import React from 'react';

export const Dashboard = () => {
  const [remainingGames, setRemainingGames] = React.useState(0);
  const [encourage, setEncourage] = React.useState('Keep going');
  const [gamesWon, setGamesWon] = React.useState(0);
  const fetchData = async () => {
    try {
      const response = await fetch('https://cgi.cse.unsw.edu.au/~cs6080/raw/data/remain.json', {
        method: 'GET',
      });
      const data = await response.json();
      return data.score;
    } catch (error) {
      return await error;
    }
  }
  React.useEffect(() => {
    const start = async() => {
      const startNum = await fetchData();
      localStorage.setItem('remaining', startNum);
      setRemainingGames(startNum);
    }
    if (!localStorage.getItem("remaining")) {
      start();
    } else {
      setRemainingGames(localStorage.getItem("remaining"));
      if (Number(localStorage.getItem("remaining")) === 0) {
        setEncourage("Great job");
      }
    }
    if (!localStorage.getItem('gamesWon')) {
      localStorage.setItem('gamesWon', 0);
      setGamesWon(0);
    } else {
      setGamesWon(localStorage.getItem('gamesWon'));
    }
  }, []);

  const reset = async() => {
    const startNum = await fetchData();
    localStorage.setItem('remaining', startNum);
    setRemainingGames(startNum);
    setEncourage("Keep going");
    localStorage.setItem('gamesWon', 0);
    setGamesWon(0);
  }

  return (
    <div className='screen'>
      <div id="dashboard-grid">
        <Typography id="dashboard-grid-item">
          {remainingGames}
        </Typography>
        <Typography id="dashboard-grid-item">
          {gamesWon}
        </Typography>
        <Typography id="dashboard-grid-item">
          {encourage}
        </Typography>
        <Typography id="dashboard-grid-item">
          <Button variant="text" color="error" onClick={reset}>(reset)</Button>
        </Typography>
      </div>
    </div>

  );
}