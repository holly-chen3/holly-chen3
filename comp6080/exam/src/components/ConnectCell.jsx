import React from 'react';

export const ConnectCell = ({ coordinate, move, occupied }) => {
  const startPlay = () => {
    move(coordinate[0]);
  }
  return (
    <div className='square-container' onClick={startPlay}>
      {occupied === "player1" ? <div className='blue'></div> : <></>}
      {occupied === "player2" ? <div className='red'></div> : <></>}
    </div>
  );
}