import React, { useEffect, useMemo, useState } from 'react';
import '@fortawesome/fontawesome-free/css/all.min.css';
import './App.css';
import './components/SharedStyles.css';
import Sidebar from './components/Sidebar';
import Alert from './components/Alert';
import {
  analyticsAPI,
  authAPI,
  gamesAPI,
  legacyTeamsAPI,
  leaderboardAPI,
  matchesAPI,
  playersAPI,
  setAuthToken,
  teamsAPI,
} from './services/api';

function App() {
  const [activeSection, setActiveSection] = useState('dashboard');
  const [alert, setAlert] = useState(null);
  const [auth, setAuth] = useState({
    token: localStorage.getItem('slm_token') || '',
    userId: localStorage.getItem('slm_userId') || '',
    role: localStorage.getItem('slm_role') || '',
  });

  const [authForm, setAuthForm] = useState({
    mode: 'login',
    name: '',
    email: '',
    password: '',
    role: 'PLAYER',
  });

  const [players, setPlayers] = useState([]);
  const [playerForm, setPlayerForm] = useState({
    name: '',
    department: '',
    sport: '',
    skillLevel: 'Beginner',
    contact: '',
    basePrice: 0,
  });

  const [teamForm, setTeamForm] = useState({ name: '', ownerId: '', budget: 0 });
  const [assignForm, setAssignForm] = useState({ teamId: '', playerId: '', bidAmount: 0 });
  const [teamPlayersView, setTeamPlayersView] = useState([]);
  const [viewTeamId, setViewTeamId] = useState('');

  const [gameForm, setGameForm] = useState({ name: '' });
  const [ruleForm, setRuleForm] = useState({ ruleId: '', gameId: '', ruleName: '', points: 1 });
  const [rulesGameId, setRulesGameId] = useState('');
  const [rules, setRules] = useState([]);

  const [matchForm, setMatchForm] = useState({
    teamAId: '',
    teamBId: '',
    gameId: '',
    venue: '',
    dateTime: '',
    suggestNextSlot: true,
  });
  const [matches, setMatches] = useState([]);
  const [games, setGames] = useState([]);
  const [teams, setTeams] = useState([]);
  const [matchFilters, setMatchFilters] = useState({ gameId: '', status: '' });

  const [scoreForm, setScoreForm] = useState({
    matchId: '',
    playerId: '',
    ruleId: '',
    value: '',
  });
  const [scoreItems, setScoreItems] = useState([]);

  const [leaderboard, setLeaderboard] = useState([]);
  const [playerStats, setPlayerStats] = useState([]);
  const [topPerformers, setTopPerformers] = useState([]);
  const [mostImproved, setMostImproved] = useState(null);

  const loggedIn = Boolean(auth.token);

  useEffect(() => {
    setAuthToken(auth.token || null);
  }, [auth.token]);

  const navItems = useMemo(() => {
    if (!loggedIn) return [];
    const base = [
      { id: 'dashboard', label: 'Dashboard', icon: 'fas fa-grid-2' },
      { id: 'matches', label: 'Matches', icon: 'fas fa-futbol' },
      { id: 'leaderboard', label: 'Leaderboard', icon: 'fas fa-ranking-star' },
      { id: 'analytics', label: 'Analytics', icon: 'fas fa-chart-line' },
    ];
    if (auth.role === 'ADMIN') {
      return [
        { id: 'dashboard', label: 'Dashboard', icon: 'fas fa-grid-2' },
        { id: 'players', label: 'Players', icon: 'fas fa-users' },
        { id: 'teams', label: 'Teams/Auction', icon: 'fas fa-shield-alt' },
        { id: 'games', label: 'Games/Rules', icon: 'fas fa-gamepad' },
        { id: 'matches', label: 'Matches', icon: 'fas fa-futbol' },
        { id: 'leaderboard', label: 'Leaderboard', icon: 'fas fa-ranking-star' },
        { id: 'analytics', label: 'Analytics', icon: 'fas fa-chart-line' },
        { id: 'scoring', label: 'Match Scoring', icon: 'fas fa-calculator' },
      ];
    }
    if (auth.role === 'TEAM_OWNER') {
      return [
        { id: 'dashboard', label: 'Dashboard', icon: 'fas fa-grid-2' },
        { id: 'teams', label: 'Teams/Auction', icon: 'fas fa-shield-alt' },
        { id: 'matches', label: 'Matches', icon: 'fas fa-futbol' },
        { id: 'leaderboard', label: 'Leaderboard', icon: 'fas fa-ranking-star' },
        { id: 'analytics', label: 'Analytics', icon: 'fas fa-chart-line' },
      ];
    }
    return base;
  }, [loggedIn, auth.role]);

  useEffect(() => {
    if (navItems.length > 0 && !navItems.some((i) => i.id === activeSection)) {
      setActiveSection(navItems[0].id);
    }
  }, [navItems, activeSection]);

  const showAlert = (message, type = 'success') => setAlert({ message, type });

  const parseApiError = (error) => {
    if (error?.response?.data && typeof error.response.data === 'string') return error.response.data;
    if (error?.response?.status === 401) return 'Unauthorized. Please login again.';
    if (error?.message) return error.message;
    return 'Request failed';
  };

  const onAuthInput = (e) => {
    const { name, value } = e.target;
    setAuthForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleAuth = async (e) => {
    e.preventDefault();
    try {
      let response;
      if (authForm.mode === 'register') {
        response = await authAPI.register({
          name: authForm.name,
          email: authForm.email,
          password: authForm.password,
          role: authForm.role,
        });
      } else {
        response = await authAPI.login({
          email: authForm.email,
          password: authForm.password,
        });
      }

      const { token, userId, role } = response.data;
      setAuth({ token, userId, role });
      localStorage.setItem('slm_userId', userId);
      localStorage.setItem('slm_role', role);
      showAlert(`${authForm.mode === 'register' ? 'Registered' : 'Logged in'} as ${role}`, 'success');
    } catch (error) {
      showAlert(parseApiError(error), 'danger');
    }
  };

  const logout = () => {
    setAuth({ token: '', userId: '', role: '' });
    setAuthToken(null);
    localStorage.removeItem('slm_userId');
    localStorage.removeItem('slm_role');
    showAlert('Logged out', 'success');
  };

  const loadPlayers = async () => {
    try {
      const response = await playersAPI.getAll();
      setPlayers(Array.isArray(response.data) ? response.data : []);
    } catch (error) {
      showAlert(parseApiError(error), 'danger');
    }
  };

  const addPlayer = async (e) => {
    e.preventDefault();
    try {
      await playersAPI.add({
        ...playerForm,
        basePrice: Number(playerForm.basePrice) || 0,
      });
      setPlayerForm({ name: '', department: '', sport: '', skillLevel: 'Beginner', contact: '', basePrice: 0 });
      showAlert('Player added', 'success');
      loadPlayers();
    } catch (error) {
      showAlert(parseApiError(error), 'danger');
    }
  };

  const createTeam = async (e) => {
    e.preventDefault();
    try {
      await teamsAPI.create({
        name: teamForm.name,
        ownerId: Number(teamForm.ownerId),
        budget: Number(teamForm.budget),
      });
      setTeamForm({ name: '', ownerId: '', budget: 0 });
      showAlert('Team created', 'success');
    } catch (error) {
      showAlert(parseApiError(error), 'danger');
    }
  };

  const assignPlayer = async (e) => {
    e.preventDefault();
    try {
      await teamsAPI.assignPlayer({
        teamId: Number(assignForm.teamId),
        playerId: Number(assignForm.playerId),
        bidAmount: Number(assignForm.bidAmount),
      });
      showAlert('Player assigned via auction', 'success');
      setAssignForm({ teamId: '', playerId: '', bidAmount: 0 });
    } catch (error) {
      showAlert(parseApiError(error), 'danger');
    }
  };

  const lockTeam = async () => {
    if (!viewTeamId) return;
    try {
      await teamsAPI.lock(Number(viewTeamId));
      showAlert(`Team ${viewTeamId} locked`, 'success');
    } catch (error) {
      showAlert(parseApiError(error), 'danger');
    }
  };

  const loadTeamPlayers = async () => {
    if (!viewTeamId) return;
    try {
      const response = await teamsAPI.getPlayers(Number(viewTeamId));
      setTeamPlayersView(Array.isArray(response.data) ? response.data : []);
    } catch (error) {
      showAlert(parseApiError(error), 'danger');
    }
  };

  const createGame = async (e) => {
    e.preventDefault();
    try {
      await gamesAPI.create({ name: gameForm.name });
      setGameForm({ name: '' });
      showAlert('Game created', 'success');
    } catch (error) {
      showAlert(parseApiError(error), 'danger');
    }
  };

  const saveRule = async (e) => {
    e.preventDefault();
    try {
      await gamesAPI.upsertRule({
        ruleId: ruleForm.ruleId ? Number(ruleForm.ruleId) : null,
        gameId: Number(ruleForm.gameId),
        ruleName: ruleForm.ruleName,
        points: Number(ruleForm.points),
      });
      setRuleForm({ ruleId: '', gameId: '', ruleName: '', points: 1 });
      showAlert('Score rule saved', 'success');
    } catch (error) {
      showAlert(parseApiError(error), 'danger');
    }
  };

  const loadRules = async () => {
    if (!rulesGameId) return;
    try {
      const response = await gamesAPI.getRules(Number(rulesGameId));
      setRules(Array.isArray(response.data) ? response.data : []);
    } catch (error) {
      showAlert(parseApiError(error), 'danger');
    }
  };

  const loadMatches = async () => {
    try {
      const response = await matchesAPI.getAll();
      setMatches(Array.isArray(response.data) ? response.data : []);
    } catch (error) {
      showAlert(parseApiError(error), 'danger');
    }
  };

  const loadGames = async () => {
    try {
      const response = await gamesAPI.getAll();
      setGames(Array.isArray(response.data) ? response.data : []);
    } catch (error) {
      setGames([]);
    }
  };

  const loadTeams = async () => {
    try {
      const response = await legacyTeamsAPI.getAll();
      // legacy endpoint returns Optional<List<Team>>; tolerate both {present, empty} styles.
      const data = response.data;
      if (Array.isArray(data)) {
        setTeams(data);
      } else if (Array.isArray(data?.value)) {
        setTeams(data.value);
      } else if (Array.isArray(data?.orElse)) {
        setTeams(data.orElse);
      } else if (Array.isArray(data?.teams)) {
        setTeams(data.teams);
      } else {
        setTeams([]);
      }
    } catch (error) {
      setTeams([]);
    }
  };

  const scheduleMatch = async (e) => {
    e.preventDefault();
    try {
      await matchesAPI.create({
        teamAId: Number(matchForm.teamAId),
        teamBId: Number(matchForm.teamBId),
        gameId: Number(matchForm.gameId),
        venue: matchForm.venue,
        dateTime: matchForm.dateTime,
        suggestNextSlot: Boolean(matchForm.suggestNextSlot),
      });
      showAlert('Match scheduled', 'success');
      setMatchForm({
        teamAId: '',
        teamBId: '',
        gameId: '',
        venue: '',
        dateTime: '',
        suggestNextSlot: true,
      });
      loadMatches();
    } catch (error) {
      showAlert(parseApiError(error), 'danger');
    }
  };

  const addScoreItem = () => {
    if (!scoreForm.playerId || !scoreForm.ruleId || scoreForm.value === '') return;
    setScoreItems((prev) => [
      ...prev,
      {
        playerId: Number(scoreForm.playerId),
        ruleId: Number(scoreForm.ruleId),
        value: Number(scoreForm.value),
      },
    ]);
    setScoreForm((prev) => ({ ...prev, playerId: '', ruleId: '', value: '' }));
  };

  const submitScore = async (e) => {
    e.preventDefault();
    try {
      if (!scoreForm.matchId || scoreItems.length === 0) {
        showAlert('Match ID and score items are required', 'danger');
        return;
      }
      await matchesAPI.score({
        matchId: Number(scoreForm.matchId),
        scores: scoreItems,
      });
      showAlert('Match score submitted', 'success');
      setScoreItems([]);
      setScoreForm({ matchId: '', playerId: '', ruleId: '', value: '' });
      loadMatches();
      loadLeaderboard();
      loadAnalytics();
    } catch (error) {
      showAlert(parseApiError(error), 'danger');
    }
  };

  const loadLeaderboard = async () => {
    try {
      const response = await leaderboardAPI.getAll();
      setLeaderboard(Array.isArray(response.data) ? response.data : []);
    } catch (error) {
      showAlert(parseApiError(error), 'danger');
    }
  };

  const loadAnalytics = async () => {
    try {
      const [statsRes, topRes] = await Promise.all([
        analyticsAPI.getPlayerStats(),
        analyticsAPI.getTopPerformers(5),
      ]);
      setPlayerStats(Array.isArray(statsRes.data) ? statsRes.data : []);
      setTopPerformers(Array.isArray(topRes.data?.topPerformers) ? topRes.data.topPerformers : []);
      setMostImproved(topRes.data?.mostImproved || null);
    } catch (error) {
      showAlert(parseApiError(error), 'danger');
    }
  };

  useEffect(() => {
    if (!loggedIn) return;
    loadMatches();
    loadLeaderboard();
    loadAnalytics();
    loadGames();
    loadTeams();
    if (auth.role === 'ADMIN') {
      loadPlayers();
    }
  }, [loggedIn, auth.role]);

  const teamNameById = useMemo(() => {
    const map = new Map();
    teams.forEach((t) => map.set(Number(t.id), t.name));
    return map;
  }, [teams]);

  const gameNameById = useMemo(() => {
    const map = new Map();
    games.forEach((g) => map.set(Number(g.id), g.name));
    return map;
  }, [games]);

  const renderSection = () => {
    switch (activeSection) {
      case 'dashboard':
        return (
          <div className="section">
            <div className="section-title"><i className="fas fa-grid-2"></i><h1>Dashboard</h1></div>
            <div className="card">
              <h3>Overview</h3>
              <div className="form-grid">
                <div className="card" style={{ margin: 0, background: 'var(--surface-2)' }}>
                  <h3 style={{ marginBottom: 8 }}>Matches</h3>
                  <p style={{ margin: 0, color: 'var(--text-muted)' }}>{matches.length} scheduled/recorded</p>
                </div>
                <div className="card" style={{ margin: 0, background: 'var(--surface-2)' }}>
                  <h3 style={{ marginBottom: 8 }}>Games</h3>
                  <p style={{ margin: 0, color: 'var(--text-muted)' }}>{games.length} configured</p>
                </div>
                <div className="card" style={{ margin: 0, background: 'var(--surface-2)' }}>
                  <h3 style={{ marginBottom: 8 }}>Teams</h3>
                  <p style={{ margin: 0, color: 'var(--text-muted)' }}>{teams.length} registered</p>
                </div>
                <div className="card" style={{ margin: 0, background: 'var(--surface-2)' }}>
                  <h3 style={{ marginBottom: 8 }}>Top performers</h3>
                  <p style={{ margin: 0, color: 'var(--text-muted)' }}>{topPerformers.length} listed</p>
                </div>
              </div>
              <div className="btn-group" style={{ marginTop: 18 }}>
                <button className="btn btn-secondary" type="button" onClick={() => { loadMatches(); loadGames(); loadTeams(); loadLeaderboard(); loadAnalytics(); }}>
                  <i className="fas fa-rotate"></i>Refresh data
                </button>
                {auth.role === 'ADMIN' && (
                  <button className="btn btn-primary" type="button" onClick={() => setActiveSection('matches')}>
                    <i className="fas fa-calendar-plus"></i>Schedule a match
                  </button>
                )}
              </div>
            </div>
          </div>
        );
      case 'players':
        return (
          <div className="section">
            <div className="section-title"><i className="fas fa-users"></i><h1>Players</h1></div>
            <div className="card" style={{ background: 'var(--surface-2)' }}>
              <h3>Players overview</h3>
              <div className="form-grid">
                <div className="card" style={{ margin: 0 }}>
                  <h3 style={{ marginBottom: 8 }}>Total players</h3>
                  <p style={{ margin: 0, color: 'var(--text-muted)' }}>{players.length}</p>
                </div>
                <div className="card" style={{ margin: 0 }}>
                  <h3 style={{ marginBottom: 8 }}>Quick actions</h3>
                  <div className="btn-group" style={{ marginTop: 0 }}>
                    <button className="btn btn-secondary" type="button" onClick={loadPlayers}><i className="fas fa-rotate"></i>Refresh</button>
                  </div>
                </div>
              </div>
            </div>
            <div className="card">
              <h3>Add Player</h3>
              <form onSubmit={addPlayer} className="form-grid">
                <div className="form-group"><label>Name</label><input value={playerForm.name} onChange={(e) => setPlayerForm({ ...playerForm, name: e.target.value })} required /></div>
                <div className="form-group"><label>Department</label><input value={playerForm.department} onChange={(e) => setPlayerForm({ ...playerForm, department: e.target.value })} required /></div>
                <div className="form-group"><label>Sport</label><input value={playerForm.sport} onChange={(e) => setPlayerForm({ ...playerForm, sport: e.target.value })} required /></div>
                <div className="form-group"><label>Skill</label><input value={playerForm.skillLevel} onChange={(e) => setPlayerForm({ ...playerForm, skillLevel: e.target.value })} required /></div>
                <div className="form-group"><label>Contact</label><input value={playerForm.contact} onChange={(e) => setPlayerForm({ ...playerForm, contact: e.target.value })} required /></div>
                <div className="form-group"><label>Base Price</label><input type="number" value={playerForm.basePrice} onChange={(e) => setPlayerForm({ ...playerForm, basePrice: e.target.value })} /></div>
                <div className="btn-group form-full">
                  <button className="btn btn-primary" type="submit"><i className="fas fa-plus-circle"></i>Add</button>
                  <button className="btn btn-secondary" type="button" onClick={loadPlayers}><i className="fas fa-rotate"></i>Refresh</button>
                </div>
              </form>
            </div>
            <div className="card">
              <h3>Players List</h3>
              <div className="table-responsive">
                <table>
                  <thead><tr><th>ID</th><th>Name</th><th>Department</th><th>Skill</th><th>Base Price</th></tr></thead>
                  <tbody>
                    {players.map((p) => <tr key={p.id}><td>{p.id}</td><td>{p.name}</td><td>{p.department}</td><td>{p.skillLevel}</td><td>{p.basePrice ?? '-'}</td></tr>)}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        );
      case 'teams':
        return (
          <div className="section">
            <div className="section-title"><i className="fas fa-shield-alt"></i><h1>Team + Auction</h1></div>
            <div className="card" style={{ background: 'var(--surface-2)' }}>
              <h3>Teams overview</h3>
              <div className="form-grid">
                <div className="card" style={{ margin: 0 }}>
                  <h3 style={{ marginBottom: 8 }}>Total teams</h3>
                  <p style={{ margin: 0, color: 'var(--text-muted)' }}>{teams.length}</p>
                </div>
                <div className="card" style={{ margin: 0 }}>
                  <h3 style={{ marginBottom: 8 }}>Quick actions</h3>
                  <div className="btn-group" style={{ marginTop: 0 }}>
                    <button className="btn btn-secondary" type="button" onClick={loadTeams}><i className="fas fa-rotate"></i>Refresh</button>
                  </div>
                </div>
              </div>
            </div>
            {auth.role === 'ADMIN' && (
              <div className="card">
                <h3>Create Team (Admin)</h3>
                <form onSubmit={createTeam} className="form-grid">
                  <div className="form-group"><label>Team Name</label><input value={teamForm.name} onChange={(e) => setTeamForm({ ...teamForm, name: e.target.value })} required /></div>
                  <div className="form-group"><label>Owner User ID</label><input type="number" value={teamForm.ownerId} onChange={(e) => setTeamForm({ ...teamForm, ownerId: e.target.value })} required /></div>
                  <div className="form-group"><label>Budget</label><input type="number" value={teamForm.budget} onChange={(e) => setTeamForm({ ...teamForm, budget: e.target.value })} required /></div>
                  <div className="btn-group form-full"><button className="btn btn-primary" type="submit">Create Team</button></div>
                </form>
              </div>
            )}
            <div className="card">
              <h3>Auction Assign (Admin / Team Owner)</h3>
              <form onSubmit={assignPlayer} className="form-grid">
                <div className="form-group"><label>Team ID</label><input type="number" value={assignForm.teamId} onChange={(e) => setAssignForm({ ...assignForm, teamId: e.target.value })} required /></div>
                <div className="form-group"><label>Player ID</label><input type="number" value={assignForm.playerId} onChange={(e) => setAssignForm({ ...assignForm, playerId: e.target.value })} required /></div>
                <div className="form-group"><label>Bid Amount</label><input type="number" value={assignForm.bidAmount} onChange={(e) => setAssignForm({ ...assignForm, bidAmount: e.target.value })} required /></div>
                <div className="btn-group form-full"><button className="btn btn-primary" type="submit">Assign</button></div>
              </form>
            </div>
            <div className="card">
              <h3>View Team Players / Lock Team</h3>
              <div className="form-grid">
                <div className="form-group"><label>Team ID</label><input type="number" value={viewTeamId} onChange={(e) => setViewTeamId(e.target.value)} /></div>
                <div className="btn-group form-full">
                  <button className="btn btn-secondary" type="button" onClick={loadTeamPlayers}>Load Team Players</button>
                  {auth.role === 'ADMIN' && <button className="btn btn-danger" type="button" onClick={lockTeam}>Lock Team</button>}
                </div>
              </div>
              <div className="table-responsive">
                <table>
                  <thead><tr><th>Player ID</th><th>Name</th><th>Skill</th><th>Assigned Price</th></tr></thead>
                  <tbody>
                    {teamPlayersView.map((tp, idx) => <tr key={`${tp.playerId}-${idx}`}><td>{tp.playerId}</td><td>{tp.playerName}</td><td>{tp.skillLevel}</td><td>{tp.assignedPrice}</td></tr>)}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        );
      case 'games':
        return (
          <div className="section">
            <div className="section-title"><i className="fas fa-gamepad"></i><h1>Games & Score Rules</h1></div>
            <div className="card" style={{ background: 'var(--surface-2)' }}>
              <h3>Games overview</h3>
              <div className="form-grid">
                <div className="card" style={{ margin: 0 }}>
                  <h3 style={{ marginBottom: 8 }}>Total games</h3>
                  <p style={{ margin: 0, color: 'var(--text-muted)' }}>{games.length}</p>
                </div>
                <div className="card" style={{ margin: 0 }}>
                  <h3 style={{ marginBottom: 8 }}>Quick actions</h3>
                  <div className="btn-group" style={{ marginTop: 0 }}>
                    <button className="btn btn-secondary" type="button" onClick={loadGames}><i className="fas fa-rotate"></i>Refresh</button>
                  </div>
                </div>
              </div>
            </div>
            <div className="card">
              <h3>Create Game</h3>
              <form onSubmit={createGame} className="form-grid">
                <div className="form-group"><label>Game Name</label><input value={gameForm.name} onChange={(e) => setGameForm({ name: e.target.value })} required /></div>
                <div className="btn-group form-full"><button className="btn btn-primary" type="submit">Create Game</button></div>
              </form>
            </div>
            <div className="card">
              <h3>Create/Update Rule</h3>
              <form onSubmit={saveRule} className="form-grid">
                <div className="form-group"><label>Rule ID (for update)</label><input type="number" value={ruleForm.ruleId} onChange={(e) => setRuleForm({ ...ruleForm, ruleId: e.target.value })} /></div>
                <div className="form-group"><label>Game ID</label><input type="number" value={ruleForm.gameId} onChange={(e) => setRuleForm({ ...ruleForm, gameId: e.target.value })} required /></div>
                <div className="form-group"><label>Rule Name</label><input value={ruleForm.ruleName} onChange={(e) => setRuleForm({ ...ruleForm, ruleName: e.target.value })} required /></div>
                <div className="form-group"><label>Points</label><input type="number" value={ruleForm.points} onChange={(e) => setRuleForm({ ...ruleForm, points: e.target.value })} required /></div>
                <div className="btn-group form-full"><button className="btn btn-primary" type="submit">Save Rule</button></div>
              </form>
            </div>
            <div className="card">
              <h3>Get Rules By Game</h3>
              <div className="form-grid">
                <div className="form-group"><label>Game ID</label><input type="number" value={rulesGameId} onChange={(e) => setRulesGameId(e.target.value)} /></div>
                <div className="btn-group form-full"><button className="btn btn-secondary" type="button" onClick={loadRules}>Load Rules</button></div>
              </div>
              <div className="table-responsive">
                <table>
                  <thead><tr><th>ID</th><th>Rule</th><th>Points</th></tr></thead>
                  <tbody>{rules.map((r) => <tr key={r.id}><td>{r.id}</td><td>{r.ruleName}</td><td>{r.points}</td></tr>)}</tbody>
                </table>
              </div>
            </div>
          </div>
        );
      case 'matches':
        return (
          <div className="section">
            <div className="section-title"><i className="fas fa-futbol"></i><h1>Matches</h1></div>
            <div className="card" style={{ background: 'var(--surface-2)' }}>
              <h3>Matches overview</h3>
              <div className="form-grid">
                <div className="card" style={{ margin: 0 }}>
                  <h3 style={{ marginBottom: 8 }}>Total matches</h3>
                  <p style={{ margin: 0, color: 'var(--text-muted)' }}>{matches.length}</p>
                </div>
                <div className="card" style={{ margin: 0 }}>
                  <h3 style={{ marginBottom: 8 }}>Quick actions</h3>
                  <div className="btn-group" style={{ marginTop: 0 }}>
                    <button className="btn btn-secondary" type="button" onClick={() => { loadMatches(); loadGames(); loadTeams(); }}>
                      <i className="fas fa-rotate"></i>Refresh
                    </button>
                  </div>
                </div>
              </div>
            </div>
            {auth.role === 'ADMIN' && (
              <div className="card">
                <h3>Schedule Match</h3>
                <form onSubmit={scheduleMatch} className="form-grid">
                  <div className="form-group">
                    <label>Team A</label>
                    <select value={matchForm.teamAId} onChange={(e) => setMatchForm({ ...matchForm, teamAId: e.target.value })} required>
                      <option value="">Select team</option>
                      {teams.map((t) => <option key={t.id} value={t.id}>{t.name} (ID {t.id})</option>)}
                    </select>
                  </div>
                  <div className="form-group">
                    <label>Team B</label>
                    <select value={matchForm.teamBId} onChange={(e) => setMatchForm({ ...matchForm, teamBId: e.target.value })} required>
                      <option value="">Select team</option>
                      {teams.map((t) => <option key={t.id} value={t.id}>{t.name} (ID {t.id})</option>)}
                    </select>
                  </div>
                  <div className="form-group">
                    <label>Game</label>
                    <select value={matchForm.gameId} onChange={(e) => setMatchForm({ ...matchForm, gameId: e.target.value })} required>
                      <option value="">Select game</option>
                      {games.map((g) => <option key={g.id} value={g.id}>{g.name} (ID {g.id})</option>)}
                    </select>
                  </div>
                  <div className="form-group"><label>Venue</label><input value={matchForm.venue} onChange={(e) => setMatchForm({ ...matchForm, venue: e.target.value })} required /></div>
                  <div className="form-group"><label>Date/Time</label><input type="datetime-local" value={matchForm.dateTime} onChange={(e) => setMatchForm({ ...matchForm, dateTime: e.target.value })} required /></div>
                  <div className="form-group"><label>Suggest Next Slot</label><select value={matchForm.suggestNextSlot ? 'true' : 'false'} onChange={(e) => setMatchForm({ ...matchForm, suggestNextSlot: e.target.value === 'true' })}><option value="true">Yes</option><option value="false">No</option></select></div>
                  <div className="btn-group form-full"><button className="btn btn-primary" type="submit">Schedule</button></div>
                </form>
              </div>
            )}
            <div className="card">
              <h3>Match List</h3>
              <div className="form-grid" style={{ marginBottom: 6 }}>
                <div className="form-group">
                  <label>Game</label>
                  <select value={matchFilters.gameId} onChange={(e) => setMatchFilters((p) => ({ ...p, gameId: e.target.value }))}>
                    <option value="">All games</option>
                    {games.map((g) => <option key={g.id} value={g.id}>{g.name}</option>)}
                  </select>
                </div>
                <div className="form-group">
                  <label>Status</label>
                  <select value={matchFilters.status} onChange={(e) => setMatchFilters((p) => ({ ...p, status: e.target.value }))}>
                    <option value="">All</option>
                    <option value="SCHEDULED">Scheduled</option>
                    <option value="COMPLETED">Completed</option>
                  </select>
                </div>
                <div className="btn-group form-full" style={{ marginTop: 0 }}>
                  <button className="btn btn-secondary" type="button" onClick={() => { loadMatches(); loadGames(); loadTeams(); }}>
                    <i className="fas fa-rotate"></i>Refresh
                  </button>
                </div>
              </div>
              <div className="table-responsive">
                <table>
                  <thead><tr><th>ID</th><th>Teams</th><th>Game</th><th>Venue</th><th>Date</th><th>Status</th><th>Totals</th><th>Winner</th></tr></thead>
                  <tbody>
                    {matches
                      .filter((m) => (matchFilters.gameId ? String(m.gameId) === String(matchFilters.gameId) : true))
                      .filter((m) => (matchFilters.status ? String(m.status) === String(matchFilters.status) : true))
                      .map((m) => (
                      <tr key={m.id}>
                        <td>{m.id}</td>
                        <td>
                          {teamNameById.get(Number(m.teamAId)) || m.teamAId} vs {teamNameById.get(Number(m.teamBId)) || m.teamBId}
                        </td>
                        <td>{gameNameById.get(Number(m.gameId)) || m.gameId || '-'}</td>
                        <td>{m.venue}</td>
                        <td>{m.dateTime || m.matchDate}</td>
                        <td>{m.status}</td>
                        <td>{m.teamATotal ?? 0} - {m.teamBTotal ?? 0}</td>
                        <td>{m.winnerTeamId ? (teamNameById.get(Number(m.winnerTeamId)) || m.winnerTeamId) : '-'}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        );
      case 'leaderboard':
        return (
          <div className="section">
            <div className="section-title"><i className="fas fa-ranking-star"></i><h1>Leaderboard</h1></div>
            <div className="card">
              <div className="btn-group"><button className="btn btn-secondary" type="button" onClick={loadLeaderboard}>Refresh</button></div>
              <div className="table-responsive">
                <table>
                  <thead><tr><th>#</th><th>Team</th><th>Played</th><th>Wins</th><th>Losses</th><th>Points</th></tr></thead>
                  <tbody>
                    {leaderboard.map((l, idx) => <tr key={`${l.teamId}-${idx}`}><td>{idx + 1}</td><td>{l.teamId}</td><td>{l.matchesPlayed}</td><td>{l.wins}</td><td>{l.losses}</td><td>{l.points}</td></tr>)}
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        );
      case 'analytics':
        return (
          <div className="section">
            <div className="section-title"><i className="fas fa-chart-line"></i><h1>Analytics</h1></div>
            <div className="card">
              <div className="btn-group"><button className="btn btn-secondary" type="button" onClick={loadAnalytics}>Refresh</button></div>
              <h3>Top Performers</h3>
              <div className="table-responsive">
                <table>
                  <thead><tr><th>Player ID</th><th>Name</th><th>Matches</th><th>Total Points</th><th>Wins</th></tr></thead>
                  <tbody>{topPerformers.map((p) => <tr key={p.playerId}><td>{p.playerId}</td><td>{p.playerName}</td><td>{p.matchesPlayed}</td><td>{p.totalPoints}</td><td>{p.wins}</td></tr>)}</tbody>
                </table>
              </div>
              <h3 style={{ marginTop: 24 }}>Most Improved</h3>
              <p>{mostImproved ? `${mostImproved.playerName} (ID ${mostImproved.playerId})` : 'N/A'}</p>
            </div>
            <div className="card">
              <h3>All Player Stats</h3>
              <div className="table-responsive">
                <table>
                  <thead><tr><th>Player</th><th>Matches</th><th>Total Points</th><th>Wins</th></tr></thead>
                  <tbody>{playerStats.map((p) => <tr key={p.playerId}><td>{p.playerName || p.playerId}</td><td>{p.matchesPlayed}</td><td>{p.totalPoints}</td><td>{p.wins}</td></tr>)}</tbody>
                </table>
              </div>
            </div>
          </div>
        );
      case 'scoring':
        return (
          <div className="section">
            <div className="section-title"><i className="fas fa-calculator"></i><h1>Admin Match Scoring</h1></div>
            <div className="card">
              <h3>Add Score Items</h3>
              <div className="form-grid">
                <div className="form-group"><label>Match ID</label><input type="number" value={scoreForm.matchId} onChange={(e) => setScoreForm({ ...scoreForm, matchId: e.target.value })} /></div>
                <div className="form-group"><label>Player ID</label><input type="number" value={scoreForm.playerId} onChange={(e) => setScoreForm({ ...scoreForm, playerId: e.target.value })} /></div>
                <div className="form-group"><label>Rule ID</label><input type="number" value={scoreForm.ruleId} onChange={(e) => setScoreForm({ ...scoreForm, ruleId: e.target.value })} /></div>
                <div className="form-group"><label>Value</label><input type="number" value={scoreForm.value} onChange={(e) => setScoreForm({ ...scoreForm, value: e.target.value })} /></div>
              </div>
              <div className="btn-group">
                <button className="btn btn-secondary" type="button" onClick={addScoreItem}>Add Item</button>
                <button className="btn btn-primary" type="button" onClick={(e) => submitScore(e)}>Submit Score</button>
              </div>
              <div className="table-responsive">
                <table>
                  <thead><tr><th>Player</th><th>Rule</th><th>Value</th></tr></thead>
                  <tbody>{scoreItems.map((s, i) => <tr key={`${s.playerId}-${s.ruleId}-${i}`}><td>{s.playerId}</td><td>{s.ruleId}</td><td>{s.value}</td></tr>)}</tbody>
                </table>
              </div>
            </div>
          </div>
        );
      default:
        return <div className="section"><div className="card"><p>Select a section.</p></div></div>;
    }
  };

  if (!loggedIn) {
    return (
      <div className="app auth-only">
        <div className="app-container auth-container">
          {alert && <Alert type={alert.type} message={alert.message} onClose={() => setAlert(null)} />}
          <div className="card auth-card">
            <div className="auth-header">
              <div className="auth-badge" aria-hidden="true"><i className="fas fa-leaf"></i></div>
              <div>
                <h2 style={{ margin: 0 }}>{authForm.mode === 'login' ? 'Welcome back' : 'Create your account'}</h2>
                <p style={{ margin: '6px 0 0', color: 'var(--text-muted)' }}>
                  {authForm.mode === 'login' ? 'Sign in to manage your league.' : 'Register to join and participate.'}
                </p>
              </div>
            </div>

            <form onSubmit={handleAuth} className="form-grid">
              {authForm.mode === 'register' && (
                <>
                  <div className="form-group">
                    <label>Name</label>
                    <div className="input-with-icon">
                      <i className="fas fa-user"></i>
                      <input name="name" value={authForm.name} onChange={onAuthInput} required placeholder="Your name" />
                    </div>
                  </div>
                  <div className="form-group">
                    <label>Role</label>
                    <div className="input-with-icon">
                      <i className="fas fa-user-tag"></i>
                      <select name="role" value={authForm.role} onChange={onAuthInput}>
                        <option value="ADMIN">Admin</option>
                        <option value="TEAM_OWNER">Team Owner</option>
                        <option value="PLAYER">Player</option>
                      </select>
                    </div>
                  </div>
                </>
              )}
              <div className="form-group">
                <label>Email</label>
                <div className="input-with-icon">
                  <i className="fas fa-envelope"></i>
                  <input type="email" name="email" value={authForm.email} onChange={onAuthInput} required placeholder="name@company.com" />
                </div>
              </div>
              <div className="form-group">
                <label>Password</label>
                <div className="input-with-icon">
                  <i className="fas fa-lock"></i>
                  <input type="password" name="password" value={authForm.password} onChange={onAuthInput} required placeholder="••••••••" />
                </div>
              </div>
              <div className="btn-group form-full">
                <button type="submit" className="btn btn-primary">{authForm.mode === 'login' ? 'Login' : 'Register'}</button>
                <button
                  type="button"
                  className="btn btn-secondary"
                  onClick={() => setAuthForm((prev) => ({ ...prev, mode: prev.mode === 'login' ? 'register' : 'login' }))}
                >
                  Switch to {authForm.mode === 'login' ? 'Register' : 'Login'}
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="app">
      <div className="app-shell">
        <Sidebar
          activeSection={activeSection}
          setActiveSection={setActiveSection}
          navItems={navItems}
          role={auth.role}
          onLogout={logout}
        />
        <main className="main">
          <div className="app-container">
            {alert && <Alert type={alert.type} message={alert.message} onClose={() => setAlert(null)} />}
            {renderSection()}
          </div>
        </main>
      </div>
    </div>
  );
}

export default App;

