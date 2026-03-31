import React, { useEffect, useMemo, useState } from 'react';
import './Navbar.css';

function Navbar({ activeSection, setActiveSection, navItems, role, onLogout }) {
  const [menuOpen, setMenuOpen] = useState(false);

  const activeLabel = useMemo(() => {
    const hit = navItems.find((n) => n.id === activeSection);
    return hit?.label || 'Menu';
  }, [navItems, activeSection]);

  useEffect(() => {
    setMenuOpen(false);
  }, [activeSection]);

  useEffect(() => {
    const onResize = () => {
      if (window.innerWidth >= 860) setMenuOpen(false);
    };
    window.addEventListener('resize', onResize);
    return () => window.removeEventListener('resize', onResize);
  }, []);

  return (
    <nav className="navbar">
      <div className="navbar-brand">
        <span className="brand-mark" aria-hidden="true">
          <i className="fas fa-crown"></i>
        </span>
        <span>Sports League Manager</span>
      </div>

      <div className="nav-right">
        <button
          type="button"
          className="nav-icon-btn nav-menu-toggle"
          aria-label={menuOpen ? 'Close menu' : 'Open menu'}
          aria-expanded={menuOpen}
          onClick={() => setMenuOpen((v) => !v)}
        >
          <i className={menuOpen ? 'fas fa-xmark' : 'fas fa-bars'}></i>
          <span className="nav-toggle-label">{activeLabel}</span>
        </button>

        <div className={`nav-menu ${menuOpen ? 'open' : ''}`}>
          <ul className="nav-links" aria-label="Sections">
            {navItems.map((item) => (
              <li key={item.id}>
                <button
                  type="button"
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
            <span className="role-pill" title="Signed-in role">
              <i className="fas fa-id-badge" aria-hidden="true"></i>
              <span>{role}</span>
            </span>
            <button type="button" className="nav-btn logout-btn" onClick={onLogout}>
              <i className="fas fa-arrow-right-from-bracket"></i>
              <span>Logout</span>
            </button>
          </div>
        </div>
      </div>
    </nav>
  );
}

export default Navbar;

