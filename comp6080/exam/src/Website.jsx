import React from 'react';
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { Header } from "./components/Header";
import { MathPage } from './pages/Math';
import { Dashboard } from './pages/DashBoard';
import { Connect4 } from './pages/Connect4';
import { Memorisation } from './pages/Memorisation';

export const Website = () => {
  return (
    <BrowserRouter>
      <section>
        <Header />
        <div id="main-body">
          <Routes>
            <Route path="/dashboard" element={<Dashboard />} />
            <Route path="/game/math" element={<MathPage />} />
            <Route path="/game/connect" element={<Connect4 />} />
            <Route path="/game/memory" element={<Memorisation />} />
          </Routes>
        </div>
      </section>
    </BrowserRouter>
  );
}