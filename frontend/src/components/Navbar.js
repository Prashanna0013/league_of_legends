import React from 'react';
import './Navbar.css';

function Navbar({ activeSection, setActiveSection }) {
  const navItems = [
    { id: 'players', label: 'Players', icon: 'fas fa-users' },
    { id: 'teams', label: 'Teams', icon: 'fas fa-shield' },
    { id: 'matches', label: 'Matches', icon: 'fas fa-futbol' },
    { id: 'leaderboard', label: 'Leaderboard', icon: 'fas fa-ranking-star' },
    { id: 'teamplayer', label: 'Assignments', icon: 'fas fa-user-tie' },
  ];

  return (
    <nav className="navbar">
      <div className="navbar-brand">
        <i className="fas fa-crown"></i>
        <span>Sports League Manager</span>
      </div>
      <ul className="nav-links">
        {navItems.map((item) => (
          <li key={item.id}>
            <button
              className={`nav-btn ${activeSection === item.id ? 'active' : ''}`}
              onClick={() => setActiveSection(item.id)}
            >
              <i className={item.icon}></i>
              <span>{item.label}</span>
            </button>
          </li>
        ))}
      </ul>
    </nav>
  );
}

export default Navbar;

