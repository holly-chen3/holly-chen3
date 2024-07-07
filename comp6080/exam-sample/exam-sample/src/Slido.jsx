import React from 'react';
import { Cell } from './components/Cell';
import { Grid } from '@mui/material';

export const Slido = () => {
  const finishedSlide = [
    [1, 2, 3],
    [4, 5, 6], 
    [7, 8, -1],
  ]
  let gridNums = [1, 2, 3, 4, 5, 6, 7, 8, -1];
  gridNums.sort(() => Math.random() - 0.5);
  const blankPosition = gridNums.indexOf(-1);
  const [grid, setGrid] = React.useState([
    gridNums.slice(0, 3),
    gridNums.slice(3, 6),
    gridNums.slice(6, 9),
  ]);

  const [blankCell, setBlankCell] = React.useState([
    blankPosition % 3,
    Math.floor(blankPosition / 3)
  ]);

  const moveCell = (oldPosition, newPosition) => {
    setBlankCell(oldPosition);
    const tmpGrid = grid;
    const tmpValue = grid[oldPosition[1]][oldPosition[0]];
    tmpGrid[oldPosition[1]][oldPosition[0]] = -1;
    tmpGrid[newPosition[1]][newPosition[0]] = tmpValue;
    setGrid(tmpGrid);
    if (grid === finishedSlide) {
      alert('Correct!');
      window.location.reload();
    }
  }
  return (
    <div className='screen'>
      <Grid container spacing={1} id="grid">
        {grid.map((list, y) => {
          console.log(y);
          return (
            list.map((value, x) => {
              console.log([x, y]);
              return (
                <Cell key={value} coordinate={[x, y]} num={value} blankCell={blankCell} moveCell={moveCell}/>
              );
            })
          )
        }
        )}
      </Grid>
    </div>
  );
}