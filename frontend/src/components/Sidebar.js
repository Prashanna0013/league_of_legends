import React, { useEffect, useMemo, useState } from 'react';
import './Sidebar.css';

function Sidebar({ activeSection, setActiveSection, navItems, role, onLogout }) {
  const [collapsed, setCollapsed] = useState(false);
  const [mobileOpen, setMobileOpen] = useState(false);

  const activeLabel = useMemo(() => {
    const hit = navItems.find((n) => n.id === activeSection);
    return hit?.label || 'Dashboard';
  }, [navItems, activeSection]);

  useEffect(() => {
    setMobileOpen(false);
  }, [activeSection]);

  useEffect(() => {
    const onResize = () => {
      if (window.innerWidth >= 900) setMobileOpen(false);
    };
    window.addEventListener('resize', onResize);
    return () => window.removeEventListener('resize', onResize);
  }, []);

  return (
    <>
      <div className="sidebar-topbar">
        <button
          type="button"
          className="sb-icon-btn"
          aria-label={mobileOpen ? 'Close navigation' : 'Open navigation'}
          aria-expanded={mobileOpen}
          onClick={() => setMobileOpen((v) => !v)}
        >
          <i className={mobileOpen ? 'fas fa-xmark' : 'fas fa-bars'}></i>
        </button>
        <div className="sidebar-topbar-title">
          <span className="sb-dot" aria-hidden="true"></span>
          <span>{activeLabel}</span>
        </div>
      </div>

      <aside className={`sidebar ${collapsed ? 'collapsed' : ''} ${mobileOpen ? 'mobile-open' : ''}`}>
        <div className="sidebar-header">
          <div className="sidebar-brand" role="button" tabIndex={0} onClick={() => setActiveSection('dashboard')}>
            <span className="brand-mark" aria-hidden="true">
              <i className="fas fa-crown"></i>
            </span>
            <div className="brand-text">
              <div className="brand-title">League Manager</div>
              <div className="brand-subtitle">Admin console</div>
            </div>
          </div>

          <button
            type="button"
            className="sb-icon-btn collapse-btn"
            aria-label={collapsed ? 'Expand sidebar' : 'Collapse sidebar'}
            aria-pressed={collapsed}
            onClick={() => setCollapsed((v) => !v)}
          >
            <i className={collapsed ? 'fas fa-angles-right' : 'fas fa-angles-left'}></i>
          </button>
        </div>

        <nav className="sidebar-nav" aria-label="Sections">
          {navItems.map((item) => (
            <button
              key={item.id}
              type="button"
              className={`sidebar-item ${activeSection === item.id ? 'active' : ''}`}
              onClick={() => setActiveSection(item.id)}
              title={item.label}
            >
              <span className="sidebar-item-icon" aria-hidden="true">
                <i className={item.icon}></i>
              </span>
              <span className="sidebar-item-label">{item.label}</span>
            </button>
          ))}
        </nav>

        <div className="sidebar-footer">
          <div className="role-pill" title="Signed-in role">
            <i className="fas fa-id-badge" aria-hidden="true"></i>
            <span className="role-text">{role}</span>
          </div>
          <button type="button" className="sidebar-item logout" onClick={onLogout} title="Logout">
            <span className="sidebar-item-icon" aria-hidden="true">
              <i className="fas fa-arrow-right-from-bracket"></i>
            </span>
            <span className="sidebar-item-label">Logout</span>
          </button>
        </div>
      </aside>

      {mobileOpen && <button className="sidebar-backdrop" aria-label="Close navigation" onClick={() => setMobileOpen(false)} />}
    </>
  );
}

export default Sidebar;

