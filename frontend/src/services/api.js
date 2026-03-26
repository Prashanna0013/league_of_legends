import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Players API
export const playersAPI = {
  getAll: () => apiClient.get('/player/get'),
  add: (data) => apiClient.post('/player/add', data),
  delete: (id) => apiClient.delete(`/player/delete/${id}`),
  getById: (id) => apiClient.get(`/player/get/${id}`),
};

// Teams API
export const teamsAPI = {
  getAll: () => apiClient.get('/team/get-team'),
  add: (data) => apiClient.post('/team/add-team', data),
  delete: (id) => apiClient.delete(`/team/delete/${id}`),
  getById: (id) => apiClient.get(`/team/get-id/${id}`),
  getByName: (name) => apiClient.get(`/team/get-name/${name}`),
};

// Matches API
export const matchesAPI = {
  getAll: () => apiClient.get('/match/get'),
  add: (data) => apiClient.post('/match/add', data),
  delete: (id) => apiClient.delete(`/match/delete/${id}`),
  update: (id, data) => apiClient.put(`/match/update/${id}`, data),
  getById: (id) => apiClient.get(`/match/get/${id}`),
};

// Leaderboard API
export const leaderboardAPI = {
  getAll: () => apiClient.get('/leaderboard/get'),
  add: (data) => apiClient.post('/leaderboard/add', data),
  delete: (id) => apiClient.delete(`/leaderboard/delete/${id}`),
  update: (id, data) => apiClient.put(`/leaderboard/update/${id}`, data),
  getById: (id) => apiClient.get(`/leaderboard/get/${id}`),
  getByTeamId: (teamId) => apiClient.get(`/leaderboard/get/team/${teamId}`),
};

// Team Player API
export const teamPlayerAPI = {
  assign: (data) => apiClient.post('/teamplayer/assign', data),
};

export default apiClient;

