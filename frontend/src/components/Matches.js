import React, { useState, useEffect } from 'react';
import './SharedStyles.css';
import Alert from './Alert';
import { matchesAPI } from '../services/api';

function Matches() {
  const [matches, setMatches] = useState([]);
  const [loading, setLoading] = useState(false);
  const [alert, setAlert] = useState(null);
  const [formData, setFormData] = useState({
    teamA: '',
    teamB: '',
    venue: '',
    matchDate: '',
    status: '',
  });

  useEffect(() => {
    loadMatches();
  }, []);

  const loadMatches = async () => {
    try {
      setLoading(true);
      const response = await matchesAPI.getAll();
      setMatches(response.data);
    } catch (error) {
      showAlert('Error loading matches', 'danger');
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
        teamA: parseInt(formData.teamA),
        teamB: parseInt(formData.teamB),
        matchDate: new Date(formData.matchDate).toISOString(),
      };
      await matchesAPI.add(dataToSubmit);
      showAlert('Match added successfully!', 'success');
      setFormData({
        teamA: '',
        teamB: '',
        venue: '',
        matchDate: '',
        status: '',
      });
      loadMatches();
    } catch (error) {
      showAlert('Error adding match', 'danger');
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this match?')) {
      try {
        await matchesAPI.delete(id);
        showAlert('Match deleted successfully!', 'success');
        loadMatches();
      } catch (error) {
        showAlert('Error deleting match', 'danger');
      }
    }
  };

  const showAlert = (message, type) => {
    setAlert({ message, type });
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleString();
  };

  return (
    <div className="section">
      <div className="section-title">
        <i className="fas fa-futbol"></i>
        <h1>Matches Management</h1>
      </div>

      {alert && <Alert type={alert.type} message={alert.message} onClose={() => setAlert(null)} />}

      <div className="card">
        <h3>Add New Match</h3>
        <form onSubmit={handleSubmit} className="form-grid">
          <div className="form-group">
            <label>Team A ID</label>
            <input
              type="number"
              name="teamA"
              value={formData.teamA}
              onChange={handleInputChange}
              placeholder="e.g., 1"
              required
            />
          </div>
          <div className="form-group">
            <label>Team B ID</label>
            <input
              type="number"
              name="teamB"
              value={formData.teamB}
              onChange={handleInputChange}
              placeholder="e.g., 2"
              required
            />
          </div>
          <div className="form-group">
            <label>Venue</label>
            <input
              type="text"
              name="venue"
              value={formData.venue}
              onChange={handleInputChange}
              placeholder="e.g., Stadium A"
              required
            />
          </div>
          <div className="form-group">
            <label>Match Date & Time</label>
            <input
              type="datetime-local"
              name="matchDate"
              value={formData.matchDate}
              onChange={handleInputChange}
              required
            />
          </div>
          <div className="form-group">
            <label>Status</label>
            <select
              name="status"
              value={formData.status}
              onChange={handleInputChange}
              required
            >
              <option value="">Select Status</option>
              <option value="Scheduled">Scheduled</option>
              <option value="Completed">Completed</option>
            </select>
          </div>
          <div className="form-group" style={{ visibility: 'hidden' }}></div>
          <div className="btn-group form-full">
            <button type="submit" className="btn btn-primary">
              <i className="fas fa-plus-circle"></i> Add Match
            </button>
            <button
              type="reset"
              className="btn btn-secondary"
              onClick={() =>
                setFormData({
                  teamA: '',
                  teamB: '',
                  venue: '',
                  matchDate: '',
                  status: '',
                })
              }
            >
              <i className="fas fa-redo"></i> Clear
            </button>
          </div>
        </form>
      </div>

      <div className="card">
        <h3>Matches List</h3>
        {loading ? (
          <div className="empty-state">
            <div className="spinner"></div>
            <p>Loading matches...</p>
          </div>
        ) : matches.length === 0 ? (
          <div className="empty-state">
            <i className="fas fa-futbol"></i>
            <p>No matches yet. Schedule one!</p>
          </div>
        ) : (
          <div className="table-responsive">
            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Team A</th>
                  <th>Team B</th>
                  <th>Venue</th>
                  <th>Date & Time</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {matches.map((match) => (
                  <tr key={match.id}>
                    <td>{match.id}</td>
                    <td>Team {match.teamA}</td>
                    <td>Team {match.teamB}</td>
                    <td>{match.venue}</td>
                    <td>{formatDate(match.matchDate)}</td>
                    <td>
                      <span
                        className={`badge ${
                          match.status === 'Completed' ? 'success' : 'warning'
                        }`}
                      >
                        {match.status}
                      </span>
                    </td>
                    <td>
                      <button
                        className="icon-btn delete"
                        onClick={() => handleDelete(match.id)}
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

export default Matches;

