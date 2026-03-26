# Sports League Manager - React Frontend

A sleek and modern React-based frontend for the Sports League Manager API, featuring a fixed top navbar, smooth animations, and matte rounded icons.

## Features

✨ **Modern UI Design**
- Gradient navbar at the top
- Smooth animations and transitions
- Responsive design for mobile and desktop
- Clean card-based layout

🎨 **Icon System**
- Font Awesome 6.4.0 for matte rounded icons
- No emoji icons, professional appearance
- Contextual icons for each section

📱 **Components**
- **Players Management** - Add, list, and delete players
- **Teams Management** - Add, list, and delete teams
- **Matches Management** - Schedule, list, and delete matches
- **Leaderboard** - View rankings and manage leaderboard entries
- **Team Player Assignments** - Assign players to teams

## Prerequisites

- Node.js (v14 or higher)
- npm (v6 or higher)

## Installation

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

## Running the Application

### Development Mode

Start the development server:
```bash
npm start
```

The application will open at `http://localhost:3000`

### Build for Production

Create an optimized production build:
```bash
npm run build
```

## Project Structure

```
frontend/
├── public/
│   └── index.html              # Main HTML file
├── src/
│   ├── components/
│   │   ├── Navbar.js           # Top navigation bar
│   │   ├── Players.js          # Players management
│   │   ├── Teams.js            # Teams management
│   │   ├── Matches.js          # Matches management
│   │   ├── Leaderboard.js      # Leaderboard display
│   │   ├── TeamPlayer.js       # Player assignments
│   │   ├── Alert.js            # Alert notifications
│   │   ├── Navbar.css          # Navbar styles
│   │   ├── Alert.css           # Alert styles
│   │   └── SharedStyles.css    # Shared component styles
│   ├── services/
│   │   └── api.js              # API service with Axios
│   ├── App.js                  # Main app component
│   ├── App.css                 # App styles
│   ├── index.js                # React entry point
│   └── index.css               # Global styles
├── package.json                # Dependencies and scripts
└── .gitignore                  # Git ignore rules
```

## API Configuration

The frontend communicates with the backend API at `http://localhost:8080/api`.

To change the API base URL, edit `src/services/api.js`:
```javascript
const API_BASE_URL = 'http://your-api-url:port/api';
```

## Available API Endpoints

### Players
- `GET /api/player/get` - Get all players
- `POST /api/player/add` - Add new player
- `DELETE /api/player/delete/:id` - Delete player

### Teams
- `GET /api/team/get-team` - Get all teams
- `POST /api/team/add-team` - Add new team
- `DELETE /api/team/delete/:id` - Delete team

### Matches
- `GET /api/match/get` - Get all matches
- `POST /api/match/add` - Add new match
- `DELETE /api/match/delete/:id` - Delete match
- `PUT /api/match/update/:id` - Update match

### Leaderboard
- `GET /api/leaderboard/get` - Get all leaderboard entries
- `POST /api/leaderboard/add` - Add leaderboard entry
- `DELETE /api/leaderboard/delete/:id` - Delete entry
- `PUT /api/leaderboard/update/:id` - Update entry

### Team Player
- `POST /api/teamplayer/assign` - Assign player to team

## Styling

The application uses a modern color scheme:
- **Primary Gradient**: `#667eea` → `#764ba2`
- **Background**: Light gradient from `#f5f7fa` to `#c3cfe2`
- **Success**: `#51cf66`
- **Danger**: `#ff6b6b`
- **Text**: `#333` (dark)

## Responsive Design

- **Desktop**: Full layout with all navigation visible
- **Tablet**: Slightly condensed layouts
- **Mobile**: Simplified navigation with icon-only buttons

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## Troubleshooting

### CORS Issues
If you see CORS errors, make sure the backend has CORS enabled. The backend should have:
```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}
```

### Connection Refused
If you get "Connection refused" errors, ensure:
1. Backend is running on `http://localhost:8080`
2. API Base URL in `src/services/api.js` is correct

### Port Already in Use
If port 3000 is in use, you can specify a different port:
```bash
PORT=3001 npm start
```

## Dependencies

- **react** - UI library
- **react-dom** - React DOM renderer
- **react-scripts** - Build scripts for React
- **axios** - HTTP client for API calls
- **@fortawesome/fontawesome-free** - Matte icon library

## License

This project is part of the Sports League Manager system.

