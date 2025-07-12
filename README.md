# Notes Web

A full-stack web application for managing personal notes with user authentication, built with Java backend and vanilla JavaScript frontend. <br>
Visit https://notes-web-7lql.onrender.com to use Notes Web !

## Features

- **User Authentication**: Secure user registration and login system
- **Note Management**: Create, read, update, and delete notes
- **Responsive Design**: Works seamlessly on desktop and mobile devices
- **Real-time Updates**: Dynamic note rendering without page refreshes
- **Touch Support**: Optimized for touch devices with dedicated touch event handlers
- **Clean UI**: Modern dark theme with intuitive user interface

## Technology Stack

### Backend
- **Java**: Core backend language
- **PostgreSQL**: Database for persistent storage
- **Java HTTP Server**: Built-in HTTP server for REST API
- **JSON**: Data interchange format
- **JDBC**: Database connectivity

### Frontend
- **HTML5**: Semantic markup structure
- **CSS3**: Responsive styling with modern design
- **Vanilla JavaScript**: Client-side logic with ES6 modules
- **Fetch API**: HTTP requests to backend services

### Build Tool
- **Gradle**: Project build and dependency management

## Project Structure

```
notes-web/app/
├── src/main/java/notes/web/
│   ├── Main.java              # Application entry point
│   ├── RequestHandler.java    # HTTP request routing and handling
│   ├── UserDAO.java           # User data access operations
│   ├── NotesDAO.java          # Notes data access operations
│   └── DBConnection.java      # Database connection management
├── public/
│   ├── index.html             # Landing page
│   ├── login.html             # User login page
│   ├── register.html          # User registration page
│   ├── registeredSuccess.html # Registration success page
│   ├── notes.html             # Main notes interface
│   ├── style.css              # Application styling
│   ├── api.js                 # API communication module
│   ├── login.js               # Login functionality
│   ├── register.js            # Registration functionality
│   └── notes.js               # Notes management functionality
└── config/
    └── db.properties          # Database configuration
```

## Prerequisites

- Java 11 or higher
- PostgreSQL 12 or higher
- Gradle 7.0 or higher

## Database Setup

1. Create a PostgreSQL database for the application
2. Create the required tables:

```sql
-- Users table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL
);

-- Notes table
CREATE TABLE notes (
    id SERIAL PRIMARY KEY,
    username TEXT NOT NULL,
    title TEXT,
    content TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## Configuration

Create a `config/db.properties` file with your database connection details:

```properties
db.url=jdbc:postgresql://localhost:5432/your_database_name
db.user=your_username
db.pass=your_password
```

## Installation & Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd notes-web
   ```

2. **Configure the database**
   - Set up PostgreSQL database
   - Create required tables (see Database Setup section)
   - Update `config/db.properties` with your database credentials

3. **Build the project**
   ```bash
   ./gradlew build
   ```

4. **Run the application**
   ```bash
   ./gradlew run
   ```
   
   Or specify a custom config path:
   ```bash
   java -DconfigPath=config/db.properties -jar build/libs/notes-web.jar
   ```

5. **Access the application**
   - Open your browser and navigate to `http://localhost:8080`
   - The server will start on port 8080 by default

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/register` | Register a new user |
| POST | `/login` | Authenticate user login |
| POST | `/addNote` | Create a new note |
| POST | `/getNotes` | Retrieve user's notes |
| POST | `/updateNote` | Update existing note |
| POST | `/deleteNote` | Delete a note |

## Usage

1. **Registration**: Create a new account on the registration page
2. **Login**: Sign in with your credentials
3. **Add Notes**: Click "Add Note" to create new notes
4. **Edit Notes**: Click on any note card to edit its content
5. **Delete Notes**: Toggle delete mode and click the ❌ icon on notes
6. **Logout**: Use the logout button to end your session

## Security Features

- User authentication with username/password
- SQL injection prevention using prepared statements
- Session management with client-side storage
- Input validation and sanitization

## Browser Compatibility

- Modern browsers with ES6 module support
- Chrome 61+
- Firefox 60+
- Safari 10.1+
- Edge 16+

## Development

The application uses a modular architecture with clear separation of concerns:

- **DAO Pattern**: Data access objects for database operations
- **RESTful API**: Clean HTTP endpoints for client-server communication
- **Responsive Design**: CSS Grid and Flexbox for responsive layouts
- **Error Handling**: Comprehensive error handling on both client and server

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is open source and available under the [MIT License](LICENSE).

## Support

For questions or issues, please open an issue in the repository or contact the development team.

---

**Notes Web** - Simple, secure, and responsive note-taking for everyone.