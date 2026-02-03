# Piano Teacher Management System

A comprehensive full-stack web application for piano teachers to manage students, schedule lessons, track homework, and monitor progress.

## 🎯 Features

### 📚 Core Functionality
- **Student Management**: Register, edit, and manage student information with pricing details
- **Lesson Scheduling**: Create and manage lesson schedules with calendar view
- **Homework Tracking**: Assign and track homework assignments with due dates
- **Progress Monitoring**: Generate progress reports and track student development
- **Multi-language Support**: English and Chinese language interface

### 🎨 User Interface
- **Responsive Design**: Mobile-friendly Bootstrap 5 interface
- **Modern SPA**: Single Page Application with AngularJS routing
- **Interactive Dashboard**: Real-time statistics and quick actions
- **Professional UI**: Clean, intuitive design for teachers

### 🔐 Security & Authentication
- **Secure Authentication**: Basic Auth with encrypted credentials
- **API Security**: Protected RESTful endpoints
- **Session Management**: Secure user session handling

## 🛠️ Technical Stack

### Backend
- **Framework**: Spring Boot 3.x
- **Security**: Spring Security with Basic Authentication
- **Database**: JPA with H2 (dev) / PostgreSQL (prod)
- **API**: RESTful API with OpenAPI documentation
- **Build Tool**: Maven
- **Java Version**: 21

### Frontend
- **Framework**: AngularJS 1.8
- **UI Library**: Bootstrap 5
- **Routing**: ngRoute for SPA navigation
- **HTTP**: AngularJS HTTP service with interceptors
- **Styling**: Custom CSS with responsive design

### DevOps & Deployment
- **Containerization**: Docker & Docker Compose
- **Web Server**: Nginx (frontend), Embedded Tomcat (backend)
- **Database**: PostgreSQL with volume persistence
- **Health Checks**: Container health monitoring
- **Environment**: Development and production configurations

## 🚀 Quick Start

### Prerequisites
- Docker and Docker Compose installed
- Git for cloning the repository

### Installation & Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/luqizhen/ai-teacher-assistant.git
   cd ai-teacher-assistant
   ```

2. **Start the application with Docker Compose**
   ```bash
   docker-compose up -d
   ```

3. **Access the application**
   - Frontend: http://localhost:80
   - Backend API: http://localhost:8080
   - API Documentation: http://localhost:8080/swagger-ui.html

### Default Credentials
- **Username**: `admin`
- **Password**: `admin123`

## 📁 Project Structure

```
ai-teacher-assistant/
├── backend/                    # Spring Boot REST API
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/pianoteacher/
│   │   │   │   ├── controller/    # REST controllers
│   │   │   │   ├── service/       # Business logic
│   │   │   │   ├── repository/    # Data access
│   │   │   │   ├── model/         # Entity classes
│   │   │   │   ├── dto/           # Data transfer objects
│   │   │   │   └── config/        # Configuration
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       └── messages/      # Internationalization
│   │   └── test/                  # Unit tests
│   ├── pom.xml                   # Maven configuration
│   └── Dockerfile                # Backend container
├── frontend-new/                 # AngularJS SPA
│   ├── src/
│   │   ├── app/
│   │   │   ├── app.js            # Main application
│   │   │   ├── controllers.js    # AngularJS controllers
│   │   │   └── services.js       # API services
│   │   ├── views/                # HTML templates
│   │   ├── assets/css/           # Stylesheets
│   │   └── index.html            # Main page
│   ├── nginx.conf                # Nginx configuration
│   └── Dockerfile                # Frontend container
├── frontend/                     # Legacy frontend (deprecated)
├── specs/                        # Feature specifications
├── docker-compose.yml            # Multi-container setup
└── README.md                     # This file
```

## 🔧 Development

### Backend Development

1. **Build the backend**
   ```bash
   cd backend
   mvn clean package
   ```

2. **Run the backend locally**
   ```bash
   mvn spring-boot:run
   ```

3. **Run tests**
   ```bash
   mvn test
   ```

### Frontend Development

1. **Serve frontend locally**
   ```bash
   cd frontend-new
   python3 -m http.server 3000
   ```

2. **Access frontend**
   - http://localhost:3000

### API Documentation

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI Spec**: http://localhost:8080/v3/api-docs

## 🐳 Docker Deployment

### Production Deployment

1. **Build and deploy all services**
   ```bash
   docker-compose -f docker-compose.yml up -d --build
   ```

2. **Check service status**
   ```bash
   docker-compose ps
   ```

3. **View logs**
   ```bash
   docker-compose logs -f
   ```

### Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `SPRING_PROFILES_ACTIVE` | `dev` | Environment profile |
| `DATABASE_URL` | `jdbc:h2:mem:pianoteacher` | Database connection |
| `POSTGRES_DB` | `pianoteacher` | PostgreSQL database name |
| `POSTGRES_USER` | `postgres` | PostgreSQL username |
| `POSTGRES_PASSWORD` | `password` | PostgreSQL password |

## 📊 API Endpoints

### Authentication
- **Method**: Basic Authentication
- **Credentials**: `admin:admin123`

### Student Management
- `GET /api/students` - List all students
- `POST /api/students` - Create new student
- `GET /api/students/{id}` - Get student by ID
- `PUT /api/students/{id}` - Update student
- `DELETE /api/students/{id}` - Delete student

### Schedule Management
- `GET /api/schedules` - List all schedules
- `POST /api/schedules` - Create new schedule
- `GET /api/schedules/{id}` - Get schedule by ID
- `PUT /api/schedules/{id}` - Update schedule
- `DELETE /api/schedules/{id}` - Delete schedule

### Lesson Content
- `GET /api/lessons` - List all lessons
- `POST /api/lessons` - Create new lesson
- `GET /api/lessons/{id}` - Get lesson by ID
- `PUT /api/lessons/{id}` - Update lesson
- `DELETE /api/lessons/{id}` - Delete lesson

### Progress Reports
- `GET /api/reports` - List all reports
- `POST /api/reports` - Generate new report
- `GET /api/reports/{id}` - Get report by ID

## 🌍 Internationalization

The application supports multiple languages:

- **English** (default): `/messages/messages.properties`
- **Chinese**: `/messages/messages_zh.properties`

### Adding New Languages

1. Create new message file: `messages_{locale}.properties`
2. Add translations for all message keys
3. Update frontend translation object in `app.js`

## 🧪 Testing

### Backend Tests
```bash
cd backend
mvn test
```

### Frontend Testing
Open browser developer tools and check console for any JavaScript errors.

### Integration Testing
```bash
docker-compose up -d
curl -u admin:admin123 http://localhost:8080/api/students
```

## 🔒 Security

### Authentication
- Basic Authentication with Spring Security
- Password encryption with BCrypt
- Session management

### API Security
- All API endpoints require authentication
- CORS configuration for frontend access
- Input validation and sanitization

### Data Protection
- Encrypted password storage
- SQL injection prevention with JPA
- XSS protection in frontend

## 📈 Performance

### Backend Performance
- Database connection pooling with HikariCP
- Lazy loading for JPA entities
- HTTP response caching

### Frontend Performance
- Minified CSS and JavaScript
- Optimized AngularJS digest cycles
- Efficient DOM manipulation

## 🐛 Troubleshooting

### Common Issues

1. **Frontend not loading**
   - Check if AngularJS scripts are loading
   - Verify browser console for JavaScript errors
   - Ensure backend is running on port 8080

2. **API 401 errors**
   - Verify authentication credentials
   - Check if backend is running
   - Ensure Basic Auth headers are being sent

3. **Database connection issues**
   - Check PostgreSQL container status
   - Verify database credentials
   - Check database logs

4. **Docker issues**
   - Ensure Docker is running
   - Check port conflicts
   - Verify Docker Compose configuration

### Logs and Debugging

```bash
# View application logs
docker-compose logs -f backend
docker-compose logs -f frontend

# Check container status
docker-compose ps

# Access container shell
docker-compose exec backend bash
docker-compose exec frontend sh
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature-name`
3. Make your changes
4. Run tests: `mvn test`
5. Commit changes: `git commit -m "Add feature"`
6. Push to branch: `git push origin feature-name`
7. Create a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Support

For questions and support:

- Create an issue on GitHub
- Check the troubleshooting section
- Review the API documentation

## 🎉 Acknowledgments

- Spring Boot framework
- AngularJS framework
- Bootstrap UI library
- Docker containerization platform
- PostgreSQL database
