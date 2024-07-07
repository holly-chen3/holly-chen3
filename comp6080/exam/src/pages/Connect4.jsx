import React from 'react';
import { ConnectCell } from '../components/ConnectCell';

export const Connect4 = () => {
  const [block, setBlock] = React.useState([]);
  const [player, setPlayer] = React.useState("player1");

  React.useEffect(() => {
    let result = Array(10).fill();
    for (let i = 0; i < 10; i++) {
      result[i] = Array(10).fill(false);
    }
    setBlock(result);
  }, []);

  const insertCoin = (column) => {
    for (let i = 9; i >= 0; i--) {
      if (block[i][column] === false) {
        const tmpBlock = block;
        tmpBlock[i][column] = player;
        console.log(column, i);
        if (player === "player1") {
          setPlayer("player2");
        } else {
          setPlayer("player1");
        }
        setBlock(tmpBlock);
        return;
      }
    }
  }
  // React.useEffect(() => {
  //   for (let i = 9; i >= 0; i--) {
  //     for (let j = 9; j >= 0; j--) {
  //       let count1 = 0;
  //       if(block[i][j] === "player1") {
  //         count1 += 1;
  //         if (count1 === 4) {
  //           alert("Congratulations");
  //           return;
  //         }
  //       }
  //     }
  //   }
  // }, [block]);
  return (
    <div className='screen'>
      <div id="connect-container">
        {block.map((row, y) => {
          return (row.map((cell, x) => {
            return (<ConnectCell key={[x, y]} coordinate={[x, y]} move={insertCoin} occupied={cell}></ConnectCell>);
          }));
        })}
      </div>
    </div>
  );
}