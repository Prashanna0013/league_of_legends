import React from 'react';
import './Navbar.css';

function Navbar({ activeSection, setActiveSection, navItems, role, onLogout }) {
  return (
    <nav className="navbar">
      <div className="navbar-brand">
        <i className="fas fa-crown"></i>
        <span>Sports League Manager</span>
      </div>
      <div className="nav-right">
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
        <div className="user-chip">
          <span>{role}</span>
          <button className="nav-btn logout-btn" onClick={onLogout}>
            <i className="fas fa-sign-out-alt"></i>
            <span>Logout</span>
          </button>
        </div>
      </div>
    </nav>
  );
}

export default Navbar;

