import React, { useState } from 'react';
import '@fortawesome/fontawesome-free/css/all.min.css';
import './App.css';
import Navbar from './components/Navbar';
import Players from './components/Players';
import Teams from './components/Teams';
import Matches from './components/Matches';
import Leaderboard from './components/Leaderboard';
import TeamPlayer from './components/TeamPlayer';

function App() {
  const [activeSection, setActiveSection] = useState('players');

  const renderSection = () => {
    switch (activeSection) {
      case 'players':
        return <Players />;
      case 'teams':
        return <Teams />;
      case 'matches':
        return <Matches />;
      case 'leaderboard':
        return <Leaderboard />;
      case 'teamplayer':
        return <TeamPlayer />;
      default:
        return <Players />;
    }
  };

  return (
    <div className="app">
      <Navbar activeSection={activeSection} setActiveSection={setActiveSection} />
      <div className="app-container">
        {renderSection()}
      </div>
    </div>
  );
}

export default App;

