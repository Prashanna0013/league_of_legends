# 🚀 Quick Start Guide - Sports League Manager

## Backend Setup

### 1. Ensure Backend is Running
```bash
cd /home/qwerty/Desktop/league_of_legends
./mvnw spring-boot:run
```

Backend will start at: `http://localhost:8080`

## Frontend Setup

### 2. Install Frontend Dependencies
```bash
cd /home/qwerty/Desktop/league_of_legends/frontend
npm install
```

### 3. Start Frontend Development Server
```bash
npm start
```

Frontend will open automatically at: `http://localhost:3000`

## Testing the Application

### Step 1: Add Players
1. Click **"Players"** in the navbar
2. Fill in player details:
   - Name: John Doe
   - Department: Engineering
   - Sport: Football
   - Skill Level: Expert
   - Contact: john@example.com
3. Click **"Add Player"**
4. See the player appear in the Players List

### Step 2: Add Teams
1. Click **"Teams"** in the navbar
2. Fill in team details:
   - Team Name: Alpha Team
   - Owner: John Smith
3. Click **"Add Team"**
4. See the team appear in the Teams List

### Step 3: Schedule Matches
1. Click **"Matches"** in the navbar
2. Fill in match details:
   - Team A ID: 1
   - Team B ID: 2
   - Venue: Central Stadium
   - Match Date: Select a date/time
   - Status: Scheduled
3. Click **"Add Match"**
4. See the match appear in the Matches List

### Step 4: Manage Leaderboard
1. Click **"Leaderboard"** in the navbar
2. Add leaderboard entries:
   - Team ID: 1
   - Matches Played: 5
   - Wins: 3
   - Losses: 2
   - Points: 100
3. Click **"Add Entry"**
4. Entries are automatically sorted by points (highest first)

### Step 5: Assign Players to Teams
1. Click **"Assignments"** in the navbar
2. Enter:
   - Player ID: 1
   - Team ID: 1
3. Click **"Assign Player"**
4. See confirmation message

## UI Features

### Navbar
- **Fixed Position**: Stays at the top while scrolling
- **Gradient Background**: Purple gradient (#667eea → #764ba2)
- **Smooth Animations**: Hover effects on navigation buttons
- **Section Indicators**: Active button is highlighted

### Icons Used (Font Awesome)
- 👥 Users → Players
- 🛡️ Shield → Teams
- ⚽ Football → Matches
- ⭐ Star → Leaderboard
- 👔 User Tie → Assignments
- ➕ Plus Circle → Add/Create
- 🔄 Redo → Clear/Reset
- 🗑️ Trash → Delete
- ✓ Check Circle → Success alerts
- ⚠️ Exclamation → Error alerts

### Cards & Forms
- **Modern Cards**: White background with shadow and hover effects
- **Form Grid**: Responsive layout that adapts to screen size
- **Rounded Inputs**: 10px border radius with focus states
- **Color-Coded Buttons**: Primary (gradient), Secondary (light), Danger (red)

### Tables
- **Hover Effect**: Rows highlight on mouse hover
- **Status Badges**: Color-coded (Green = Completed, Yellow = Scheduled)
- **Inline Actions**: Delete button on each row
- **Responsive**: Scrollable on mobile devices

### Alerts
- **Auto-dismiss**: Closes after 4 seconds
- **Success**: Green background with checkmark
- **Danger**: Red background with warning icon
- **Animations**: Smooth slide-down and fade-out effects

## Common Issues & Solutions

### ❌ "Cannot GET /api/..."
**Solution**: Make sure backend is running at `http://localhost:8080`

### ❌ CORS Error
**Solution**: Backend CORS config is already enabled in `CorsConfig.java`

### ❌ Port 3000 Already in Use
**Solution**: 
```bash
PORT=3001 npm start
```

### ❌ "Module not found" Error
**Solution**: Reinstall dependencies
```bash
rm -rf node_modules package-lock.json
npm install
```

## File Structure

```
league_of_legends/
├── src/main/java/
│   └── com/example/lol/
│       ├── controller/
│       │   ├── PlayerController.java
│       │   ├── TeamController.java
│       │   ├── MatchController.java
│       │   ├── LeaderboardController.java
│       │   ├── TeamPlayerController.java
│       │   └── HomeController.java
│       ├── service/
│       │   ├── PlayerService.java
│       │   ├── TeamService.java
│       │   ├── MatchService.java
│       │   ├── LeaderboardService.java
│       │   └── TeamPlayerService.java
│       ├── entity/
│       │   ├── Player.java
│       │   ├── Team.java
│       │   ├── Match.java
│       │   ├── Leaderboard.java
│       │   └── TeamPlayer.java
│       └── repository/
│           ├── PlayerRepository.java
│           ├── TeamRepository.java
│           ├── MatchRepository.java
│           ├── LeaderboardRepository.java
│           └── TeamPlayerRepository.java
├── frontend/
│   ├── public/
│   ├── src/
│   │   ├── components/
│   │   │   ├── Navbar.js
│   │   │   ├── Players.js
│   │   │   ├── Teams.js
│   │   │   ├── Matches.js
│   │   │   ├── Leaderboard.js
│   │   │   ├── TeamPlayer.js
│   │   │   └── Alert.js
│   │   └── services/
│   │       └── api.js
│   └── package.json
└── pom.xml
```

## API Testing with cURL

### Add Player
```bash
curl -X POST http://localhost:8080/api/player/add \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "department": "Engineering",
    "sport": "Football",
    "skillLevel": "Expert",
    "contact": "john@example.com"
  }'
```

### Add Team
```bash
curl -X POST http://localhost:8080/api/team/add-team \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Alpha Team",
    "owner": "John Smith"
  }'
```

### Get All Players
```bash
curl http://localhost:8080/api/player/get
```

### Delete Player
```bash
curl -X DELETE http://localhost:8080/api/player/delete/1
```

## Performance Tips

- **Lazy Loading**: Components load data only when needed
- **Efficient Rendering**: React optimizes re-renders
- **Debounced Requests**: Form submissions include validation
- **Responsive Images**: Icons scale with screen size

## Browser DevTools

1. **React Developer Tools**: Install from Chrome Web Store
2. **Network Tab**: Monitor API calls and responses
3. **Console**: Check for errors and warnings
4. **Elements**: Inspect styling and structure

## Next Steps

- ✅ Test all CRUD operations
- ✅ Try responsive design on mobile
- ✅ Check API integration
- ✅ Verify form validations
- ✅ Test error handling

## Support

For issues or questions:
1. Check the console for error messages
2. Verify backend and frontend are both running
3. Check API Base URL in `src/services/api.js`
4. Ensure database is properly configured

Happy testing! 🎉

