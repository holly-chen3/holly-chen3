import React from 'react';
import { TetroCell } from './components/TetroCell';

export const Tetro = () => {
  const [start, setStart] = React.useState(false);
  const [blocks, setBlocks] = React.useState([]);
  const [activeTetrisType, setActiveTetrisType] = React.useState(null);
  const [activePosition, setActivePosition] = React.useState([0, 0]);
  let result = Array(12).fill();
  for (let i = 0; i < 12; i++) {
    result[i] = Array(10).fill("unoccupied");
  }
  const tetroType = [
    [2, 2],
    [2, 1],
    [1, 1],
  ];
  React.useEffect(() => {
    const occupyBlocks = (occupyStr) => {
      if (activePosition[0] >= 11 || activePosition[1] >= 9) {
        setStart(false);
      }
      if (start) {
        const occupiedX = activeTetrisType[0];
        const occupiedY = activeTetrisType[1];
        for (let i = 0; i < occupiedX; i++) {
          for (let j = 0; j < occupiedY; j++) {
            const tmpBlocks = blocks;
            tmpBlocks[i + activePosition[0]][j + activePosition[1]] = occupyStr;
            setBlocks(tmpBlocks);
          }
        }
      }
    }
    const interval = window.setInterval(() => {
      if (start) {
        occupyBlocks("unoccupied");
        setActivePosition([activePosition[0] + 1, activePosition[1]]);
        occupyBlocks("occupied");
      }
    }, 1000);

    return () => clearInterval(interval);
  }, [activePosition, activeTetrisType, blocks, start]);
  React.useEffect(() => {
    
  }, [activePosition, activeTetrisType, blocks, start]);

  const createTetris = () => {
    const randomIndex = Math.floor(Math.random() * tetroType.length);
    setActivePosition([0, 0]);
    setActiveTetrisType(tetroType[randomIndex]);
  }
  return (
    <div 
      id="tetro-grid" 
      className='screen' 
      onClick={() => { 
        setBlocks(result);
        setStart(true);
        createTetris();
      }}
    >
      {blocks.map((list, y) => {
        return list.map((value, x) => {
          return (<TetroCell key={[x,y]} coordinate={[x,y]} state={value}/>);
        })
      })}
    </div>
  );
}