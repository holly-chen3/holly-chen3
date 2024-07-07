import React from 'react';

export const TetroCell = ({ coordinate, state }) => {
  const occupied = (state === "occupied");
  return (
    <>
    {occupied ? <div className='active-tetro'></div> : <div className='tetro-cell'></div>}
    </>
  );
}