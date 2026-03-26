import React, { useState, useEffect } from 'react';
import './SharedStyles.css';
import Alert from './Alert';
import { playersAPI } from '../services/api';

function Players() {
  const [players, setPlayers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [alert, setAlert] = useState(null);
  const [formData, setFormData] = useState({
    name: '',
    department: '',
    sport: '',
    skillLevel: '',
    contact: '',
  });

  useEffect(() => {
    loadPlayers();
  }, []);

  const loadPlayers = async () => {
    try {
      setLoading(true);
      const response = await playersAPI.getAll();
      setPlayers(response.data);
    } catch (error) {
      showAlert('Error loading players', 'danger');
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
      await playersAPI.add(formData);
      showAlert('Player added successfully!', 'success');
      setFormData({
        name: '',
        department: '',
        sport: '',
        skillLevel: '',
        contact: '',
      });
      loadPlayers();
    } catch (error) {
      showAlert('Error adding player', 'danger');
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this player?')) {
      try {
        await playersAPI.delete(id);
        showAlert('Player deleted successfully!', 'success');
        loadPlayers();
      } catch (error) {
        showAlert('Error deleting player', 'danger');
      }
    }
  };

  const showAlert = (message, type) => {
    setAlert({ message, type });
  };

  return (
    <div className="section">
      <div className="section-title">
        <i className="fas fa-users"></i>
        <h1>Players Management</h1>
      </div>

      {alert && <Alert type={alert.type} message={alert.message} onClose={() => setAlert(null)} />}

      <div className="card">
        <h3>Add New Player</h3>
        <form onSubmit={handleSubmit} className="form-grid">
          <div className="form-group">
            <label>Name</label>
            <input
              type="text"
              name="name"
              value={formData.name}
              onChange={handleInputChange}
              placeholder="e.g., John Doe"
              required
            />
          </div>
          <div className="form-group">
            <label>Department</label>
            <input
              type="text"
              name="department"
              value={formData.department}
              onChange={handleInputChange}
              placeholder="e.g., Engineering"
              required
            />
          </div>
          <div className="form-group">
            <label>Sport</label>
            <input
              type="text"
              name="sport"
              value={formData.sport}
              onChange={handleInputChange}
              placeholder="e.g., Football"
              required
            />
          </div>
          <div className="form-group">
            <label>Skill Level</label>
            <select
              name="skillLevel"
              value={formData.skillLevel}
              onChange={handleInputChange}
              required
            >
              <option value="">Select Skill Level</option>
              <option value="Beginner">Beginner</option>
              <option value="Intermediate">Intermediate</option>
              <option value="Advanced">Advanced</option>
              <option value="Expert">Expert</option>
            </select>
          </div>
          <div className="form-group">
            <label>Contact</label>
            <input
              type="text"
              name="contact"
              value={formData.contact}
              onChange={handleInputChange}
              placeholder="e.g., email@example.com"
              required
            />
          </div>
          <div className="form-group" style={{ visibility: 'hidden' }}></div>
          <div className="btn-group form-full">
            <button type="submit" className="btn btn-primary">
              <i className="fas fa-plus-circle"></i> Add Player
            </button>
            <button
              type="reset"
              className="btn btn-secondary"
              onClick={() => setFormData({ name: '', department: '', sport: '', skillLevel: '', contact: '' })}
            >
              <i className="fas fa-redo"></i> Clear
            </button>
          </div>
        </form>
      </div>

      <div className="card">
        <h3>Players List</h3>
        {loading ? (
          <div className="empty-state">
            <div className="spinner"></div>
            <p>Loading players...</p>
          </div>
        ) : players.length === 0 ? (
          <div className="empty-state">
            <i className="fas fa-users"></i>
            <p>No players yet. Add one to get started!</p>
          </div>
        ) : (
          <div className="table-responsive">
            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Name</th>
                  <th>Department</th>
                  <th>Sport</th>
                  <th>Skill Level</th>
                  <th>Contact</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {players.map((player) => (
                  <tr key={player.id}>
                    <td>{player.id}</td>
                    <td>{player.name}</td>
                    <td>{player.department}</td>
                    <td>{player.sport}</td>
                    <td>
                      <span className="badge">{player.skillLevel}</span>
                    </td>
                    <td>{player.contact}</td>
                    <td>
                      <button
                        className="icon-btn delete"
                        onClick={() => handleDelete(player.id)}
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

export default Players;

