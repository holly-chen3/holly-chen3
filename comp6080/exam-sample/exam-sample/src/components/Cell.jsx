import React from 'react';
import { Grid } from '@mui/material';

export const Cell = ({ num, moveCell, blankCell, coordinate }) => {
  console.log(num);
  const imgShrek =
  num === -1
    ? null
    : require("../shrek/" + num + ".png");
  const nextTo = () => {
    const nextCells = [
      [0, 1],
      [-1, 0],
      [1, 0], 
      [0, -1],
    ]
    for (const nextCell of nextCells) {
      console.log(nextCell);
      console.log(nextCell[0] + coordinate[0], nextCell[1] + coordinate[1]);
      console.log(blankCell);
      if(nextCell[0] + coordinate[0] === blankCell[0] && nextCell[1] + coordinate[1] === blankCell[1]) {
        return true;
      }
    }
    return false;
  }
  const move = () => {
    const next = nextTo();
    console.log(next);
    if (next) {
      console.log('hi');
      moveCell(coordinate, blankCell);
    }
  }
  return (
    <Grid item xs={4}>
      {imgShrek !== null ? <img src={imgShrek} alt="shrek-cell" onClick={move} className="cell-image"/> : <></>}
    </Grid>
  );
}