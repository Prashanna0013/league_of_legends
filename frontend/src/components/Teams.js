import React, { useState, useEffect } from 'react';
import './SharedStyles.css';
import Alert from './Alert';
import { teamsAPI } from '../services/api';

function Teams() {
  const [teams, setTeams] = useState([]);
  const [loading, setLoading] = useState(false);
  const [alert, setAlert] = useState(null);
  const [formData, setFormData] = useState({
    name: '',
    owner: '',
  });

  useEffect(() => {
    loadTeams();
  }, []);

  const loadTeams = async () => {
    try {
      setLoading(true);
      const response = await teamsAPI.getAll();
      setTeams(response.data);
    } catch (error) {
      showAlert('Error loading teams', 'danger');
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
      await teamsAPI.add(formData);
      showAlert('Team added successfully!', 'success');
      setFormData({ name: '', owner: '' });
      loadTeams();
    } catch (error) {
      showAlert('Error adding team', 'danger');
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this team?')) {
      try {
        await teamsAPI.delete(id);
        showAlert('Team deleted successfully!', 'success');
        loadTeams();
      } catch (error) {
        showAlert('Error deleting team', 'danger');
      }
    }
  };

  const showAlert = (message, type) => {
    setAlert({ message, type });
  };

  return (
    <div className="section">
      <div className="section-title">
        <i className="fas fa-shield"></i>
        <h1>Teams Management</h1>
      </div>

      {alert && <Alert type={alert.type} message={alert.message} onClose={() => setAlert(null)} />}

      <div className="card">
        <h3>Add New Team</h3>
        <form onSubmit={handleSubmit} className="form-grid">
          <div className="form-group">
            <label>Team Name</label>
            <input
              type="text"
              name="name"
              value={formData.name}
              onChange={handleInputChange}
              placeholder="e.g., Alpha Team"
              required
            />
          </div>
          <div className="form-group">
            <label>Owner</label>
            <input
              type="text"
              name="owner"
              value={formData.owner}
              onChange={handleInputChange}
              placeholder="e.g., John Smith"
              required
            />
          </div>
          <div className="form-group" style={{ visibility: 'hidden' }}></div>
          <div className="btn-group form-full">
            <button type="submit" className="btn btn-primary">
              <i className="fas fa-plus-circle"></i> Add Team
            </button>
            <button
              type="reset"
              className="btn btn-secondary"
              onClick={() => setFormData({ name: '', owner: '' })}
            >
              <i className="fas fa-redo"></i> Clear
            </button>
          </div>
        </form>
      </div>

      <div className="card">
        <h3>Teams List</h3>
        {loading ? (
          <div className="empty-state">
            <div className="spinner"></div>
            <p>Loading teams...</p>
          </div>
        ) : teams.length === 0 ? (
          <div className="empty-state">
            <i className="fas fa-shield"></i>
            <p>No teams yet. Create one to get started!</p>
          </div>
        ) : (
          <div className="table-responsive">
            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Team Name</th>
                  <th>Owner</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {teams.map((team) => (
                  <tr key={team.id}>
                    <td>{team.id}</td>
                    <td>
                      <strong>{team.name}</strong>
                    </td>
                    <td>{team.owner}</td>
                    <td>
                      <button
                        className="icon-btn delete"
                        onClick={() => handleDelete(team.id)}
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

export default Teams;

