import React from 'react';
import { strs } from './BlankoString';
import { Square } from './components/Square';

export const Blanko = () => {
  const randomString = strs[(Math.floor(Math.random() * strs.length))];
  const randomLetters = (array) => {
    let arr = array.split("");
    for (let i = 0; i < 3; i++) {
      const randomIndex = Math.floor(Math.random() * arr.length);
      console.log(randomIndex);
      if (arr[randomIndex] !== ' ' && arr[randomIndex] !== '_') {
        arr[randomIndex] = '_';
      } else {
        i--;
      }
    }
    console.log(arr);
    return arr;
  }
  const ogArray = randomString.split("");
  const randomArray = randomLetters(randomString);
  const copyArray = randomArray;
  console.log(copyArray);
  const verifyLetter = (letter, index) => {
    if (ogArray[index] === letter) {
      copyArray[index] = letter;
    }
    if (ogArray.toString() === copyArray.toString()) {
      alertCorrect("Correct!");
    }
  }
  const alertCorrect = (string) => {
    alert(string);
    const gamesWon = Number(localStorage.getItem('gamesWon'));
    localStorage.setItem('gamesWon', (gamesWon + 1).toString());
    resetGame();
  }
  const resetGame = () => {
    window.location.reload();
  }
  return (
    <div className="screen">
      <form id="blanko-squares">
        {randomArray.map((letter, index) => <Square key={index} letter={letter} verifyLetter={verifyLetter} index={index}/>)}
      </form>
      <button onClick={resetGame}>Reset Game</button>
    </div>
  );
}