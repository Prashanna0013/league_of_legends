import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';
const LEGACY_API_BASE_URL = 'http://localhost:8080/api';

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

const legacyClient = axios.create({
  baseURL: LEGACY_API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

let authToken = localStorage.getItem('slm_token') || null;

export const setAuthToken = (token) => {
  authToken = token || null;
  if (authToken) {
    localStorage.setItem('slm_token', authToken);
  } else {
    localStorage.removeItem('slm_token');
  }
};

const attachAuth = (config) => {
  if (authToken) {
    config.headers = config.headers || {};
    config.headers.Authorization = `Bearer ${authToken}`;
  }
  return config;
};

apiClient.interceptors.request.use(attachAuth);
legacyClient.interceptors.request.use(attachAuth);

export const authAPI = {
  register: (data) => apiClient.post('/register', data),
  login: (data) => apiClient.post('/login', data),
};

// Legacy players endpoints kept for compatibility
export const playersAPI = {
  getAll: () => legacyClient.get('/player/get'),
  add: (data) => legacyClient.post('/player/add', data),
};

export const teamsAPI = {
  create: (data) => apiClient.post('/teams', data),
  assignPlayer: (data) => apiClient.post('/auction/assign', data),
  lock: (teamId) => apiClient.post(`/teams/${teamId}/lock`),
  getPlayers: (teamId) => apiClient.get(`/teams/${teamId}/players`),
};

export const gamesAPI = {
  create: (data) => apiClient.post('/games', data),
  getAll: () => apiClient.get('/games'),
  upsertRule: (data) => apiClient.post('/score-rules', data),
  getRules: (gameId) => apiClient.get(`/games/${gameId}/rules`),
};

export const matchesAPI = {
  getAll: () => apiClient.get('/matches'),
  create: (data) => apiClient.post('/matches', data),
  score: (data) => apiClient.post('/match-score', data),
};

// Legacy teams endpoints kept for compatibility
export const legacyTeamsAPI = {
  getAll: () => legacyClient.get('/team/get-team'),
};

export const leaderboardAPI = {
  getAll: () => apiClient.get('/leaderboard'),
};

export const analyticsAPI = {
  getPlayerStats: () => apiClient.get('/player-stats'),
  getTopPerformers: (limit = 5) => apiClient.get(`/top-performers?limit=${limit}`),
};

export default apiClient;

