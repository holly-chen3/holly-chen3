import React from 'react';
import { Button } from '@mui/material';

export const MathPage = () => {
  const [firstBox, setFirstBox] = React.useState(0);
  const [secondBox, setSecondBox] = React.useState('');
  const [thirdBox, setThirdBox] = React.useState(0);
  React.useEffect(() => {
    const opList = [`+`, `-`, `*`, `/`, `%`];
    const randomNum1 = Math.floor(Math.random() * 50);
    const randomNum3 = Math.floor(Math.random() * 50);
    const randomOp = opList[(Math.floor(Math.random() * opList.length))];
    setFirstBox(randomNum1);
    setThirdBox(randomNum3);
    setSecondBox(randomOp);
  }, []);
  let operations = {
    '+': function (x, y) { return x + y },
    '-': function (x, y) { return x - y },
    '*': function (x, y) { return x * y },
    '/': function (x, y) { return x / y },
    '%': function (x, y) { return x % y },
  }
  const check = (value) => {
    const num = Math.round(value * 10) / 10;
    const realVal = Math.round(operations[secondBox](firstBox, thirdBox) * 10) / 10;
    if (num === realVal) {
      alert('Congratulations');
      localStorage.setItem('gamesWon', Number(localStorage.getItem('gamesWon')) + 1);
      localStorage.setItem('remaining', localStorage.getItem('remaining') - 1);
      window.location.reload();
    }
  }
  return (
    <div className='screen' id="overall-math">
      <div id="math-container">
        <div className='math-box'>{firstBox}</div>
        <div className='math-box'>{secondBox}</div>
        <div className='math-box'>{thirdBox}</div>
        <div className='math-box'>=</div>
        <div className='math-box'><input type='text' id="math-input" onKeyUp={(e) => check(e.target.value)}/></div>
      </div>
      <Button onClick={() => window.location.reload()}>Reset</Button>
    </div>
  );
}