import React, { useState } from 'react';
import './SharedStyles.css';
import Alert from './Alert';
import { teamPlayerAPI } from '../services/api';

function TeamPlayer() {
  const [alert, setAlert] = useState(null);
  const [formData, setFormData] = useState({
    playerId: '',
    teamId: '',
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const dataToSubmit = {
        playerId: parseInt(formData.playerId),
        teamId: parseInt(formData.teamId),
      };
      const response = await teamPlayerAPI.assign(dataToSubmit);
      showAlert('Player assigned to team successfully!', 'success');
      setFormData({ playerId: '', teamId: '' });
    } catch (error) {
      showAlert(
        error.response?.data || 'Error assigning player to team',
        'danger'
      );
    }
  };

  const showAlert = (message, type) => {
    setAlert({ message, type });
  };

  return (
    <div className="section">
      <div className="section-title">
        <i className="fas fa-user-tie"></i>
        <h1>Player Assignments</h1>
      </div>

      {alert && <Alert type={alert.type} message={alert.message} onClose={() => setAlert(null)} />}

      <div className="card">
        <h3>Assign Player to Team</h3>
        <form onSubmit={handleSubmit} className="form-grid">
          <div className="form-group">
            <label>Player ID</label>
            <input
              type="number"
              name="playerId"
              value={formData.playerId}
              onChange={handleInputChange}
              placeholder="e.g., 1"
              required
            />
          </div>
          <div className="form-group">
            <label>Team ID</label>
            <input
              type="number"
              name="teamId"
              value={formData.teamId}
              onChange={handleInputChange}
              placeholder="e.g., 1"
              required
            />
          </div>
          <div className="form-group" style={{ visibility: 'hidden' }}></div>
          <div className="btn-group form-full">
            <button type="submit" className="btn btn-primary">
              <i className="fas fa-link"></i> Assign Player
            </button>
            <button
              type="reset"
              className="btn btn-secondary"
              onClick={() => setFormData({ playerId: '', teamId: '' })}
            >
              <i className="fas fa-redo"></i> Clear
            </button>
          </div>
        </form>
      </div>

      <div className="card">
        <div className="empty-state">
          <i className="fas fa-info-circle"></i>
          <p>Select a Player ID and Team ID above to assign a player to a team.</p>
          <p style={{ fontSize: '12px', color: '#ccc', marginTop: '16px' }}>
            Make sure both the player and team exist before assigning.
          </p>
        </div>
      </div>
    </div>
  );
}

export default TeamPlayer;

