import React, { useState, useEffect } from 'react';
import './SharedStyles.css';
import Alert from './Alert';
import { leaderboardAPI } from '../services/api';

function Leaderboard() {
  const [entries, setEntries] = useState([]);
  const [loading, setLoading] = useState(false);
  const [alert, setAlert] = useState(null);
  const [formData, setFormData] = useState({
    teamId: '',
    matchesPlayed: '',
    wins: '',
    losses: '',
    points: '',
  });

  useEffect(() => {
    loadLeaderboard();
  }, []);

  const loadLeaderboard = async () => {
    try {
      setLoading(true);
      const response = await leaderboardAPI.getAll();
      const sorted = response.data.sort((a, b) => b.points - a.points);
      setEntries(sorted);
    } catch (error) {
      showAlert('Error loading leaderboard', 'danger');
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const dataToSubmit = {
        ...formData,
        teamId: parseInt(formData.teamId),
        matchesPlayed: parseInt(formData.matchesPlayed),
        wins: parseInt(formData.wins),
        losses: parseInt(formData.losses),
        points: parseInt(formData.points),
      };
      await leaderboardAPI.add(dataToSubmit);
      showAlert('Leaderboard entry added successfully!', 'success');
      setFormData({
        teamId: '',
        matchesPlayed: '',
        wins: '',
        losses: '',
        points: '',
      });
      loadLeaderboard();
    } catch (error) {
      showAlert('Error adding entry', 'danger');
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this entry?')) {
      try {
        await leaderboardAPI.delete(id);
        showAlert('Entry deleted successfully!', 'success');
        loadLeaderboard();
      } catch (error) {
        showAlert('Error deleting entry', 'danger');
      }
    }
  };

  const showAlert = (message, type) => {
    setAlert({ message, type });
  };

  return (
    <div className="section">
      <div className="section-title">
        <i className="fas fa-ranking-star"></i>
        <h1>Leaderboard Management</h1>
      </div>

      {alert && <Alert type={alert.type} message={alert.message} onClose={() => setAlert(null)} />}

      <div className="card">
        <h3>Add Leaderboard Entry</h3>
        <form onSubmit={handleSubmit} className="form-grid">
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
          <div className="form-group">
            <label>Matches Played</label>
            <input
              type="number"
              name="matchesPlayed"
              value={formData.matchesPlayed}
              onChange={handleInputChange}
              placeholder="e.g., 5"
              required
            />
          </div>
          <div className="form-group">
            <label>Wins</label>
            <input
              type="number"
              name="wins"
              value={formData.wins}
              onChange={handleInputChange}
              placeholder="e.g., 3"
              required
            />
          </div>
          <div className="form-group">
            <label>Losses</label>
            <input
              type="number"
              name="losses"
              value={formData.losses}
              onChange={handleInputChange}
              placeholder="e.g., 2"
              required
            />
          </div>
          <div className="form-group">
            <label>Points</label>
            <input
              type="number"
              name="points"
              value={formData.points}
              onChange={handleInputChange}
              placeholder="e.g., 100"
              required
            />
          </div>
          <div className="form-group" style={{ visibility: 'hidden' }}></div>
          <div className="btn-group form-full">
            <button type="submit" className="btn btn-primary">
              <i className="fas fa-plus-circle"></i> Add Entry
            </button>
            <button
              type="reset"
              className="btn btn-secondary"
              onClick={() =>
                setFormData({
                  teamId: '',
                  matchesPlayed: '',
                  wins: '',
                  losses: '',
                  points: '',
                })
              }
            >
              <i className="fas fa-redo"></i> Clear
            </button>
          </div>
        </form>
      </div>

      <div className="card">
        <h3>Leaderboard Rankings</h3>
        {loading ? (
          <div className="empty-state">
            <div className="spinner"></div>
            <p>Loading leaderboard...</p>
          </div>
        ) : entries.length === 0 ? (
          <div className="empty-state">
            <i className="fas fa-ranking-star"></i>
            <p>No leaderboard entries yet!</p>
          </div>
        ) : (
          <div className="table-responsive">
            <table>
              <thead>
                <tr>
                  <th>Rank</th>
                  <th>ID</th>
                  <th>Team ID</th>
                  <th>Matches</th>
                  <th>Wins</th>
                  <th>Losses</th>
                  <th>Points</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {entries.map((entry, index) => (
                  <tr key={entry.id}>
                    <td>
                      <strong>#{index + 1}</strong>
                    </td>
                    <td>{entry.id}</td>
                    <td>{entry.teamId}</td>
                    <td>{entry.matchesPlayed}</td>
                    <td>
                      <span className="badge success">{entry.wins}</span>
                    </td>
                    <td>
                      <span className="badge danger">{entry.losses}</span>
                    </td>
                    <td>
                      <strong style={{ color: '#667eea', fontSize: '16px' }}>
                        {entry.points}
                      </strong>
                    </td>
                    <td>
                      <button
                        className="icon-btn delete"
                        onClick={() => handleDelete(entry.id)}
                        title="Delete"
                      >
                        <i className="fas fa-trash-alt"></i>
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
}

export default Leaderboard;

