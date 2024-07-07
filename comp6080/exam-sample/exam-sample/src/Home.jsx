import React from 'react';

export const Home = () => {
  const fetchData = async () => {
    try {
      const response = await fetch('https://cgi.cse.unsw.edu.au/~cs6080/raw/data/info.json', {
        method: 'GET',
      });
      const data = await response.json();
      console.log(data);
      return data;
    } catch (error) {
      return await error;
    }
  }
  const reset = async() => {
    const startNum = await fetchData();
    localStorage.setItem('gamesWon', startNum.score);
    window.location.reload();
  }
  return (
    <>
      <div className="screen">
        <h1>
          Please choose an option from the navbar.
        </h1>
        <h2>
          Games won: {localStorage.getItem('gamesWon')} <button onClick={reset}>(reset)</button>
        </h2>
      </div>
    </>

  );
}