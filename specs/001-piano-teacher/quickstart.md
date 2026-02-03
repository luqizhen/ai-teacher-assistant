# Quick Start Guide: Piano Teacher Management System

**Date**: 2025-01-30  
**Purpose**: Get the system running quickly for development and deployment

## Prerequisites

### Development Environment
- Java 17 or higher
- Maven 3.8 or higher
- Docker and Docker Compose
- Git
- Modern web browser (Chrome, Firefox, Safari, Edge)

### System Requirements
- Minimum 4GB RAM
- 2GB free disk space
- Internet connection for dependencies

## Quick Start (5 Minutes)

### 1. Clone and Setup
```bash
git clone <repository-url>
cd piano-teacher-system
```

### 2. Start with Docker Compose
```bash
docker-compose up -d
```

### 3. Access the Application
- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080/api/v1
- **API Documentation**: http://localhost:8080/swagger-ui.html

### 4. Default Login
- Username: `admin`
- Password: `admin123`

## Development Setup

### Backend Development

#### 1. Navigate to Backend
```bash
cd backend
```

#### 2. Build and Run
```bash
# Using Maven
mvn clean install
mvn spring-boot:run

# Or using the Dockerfile
docker build -t piano-teacher-backend .
docker run -p 8080:8080 piano-teacher-backend
```

#### 3. Database Setup
- **Development**: Uses H2 in-memory database (no setup required)
- **Production**: Configure PostgreSQL in `application-prod.properties`

#### 4. Test the API
```bash
# Health check
curl http://localhost:8080/actuator/health

# Get all students
curl -u admin:admin123 http://localhost:8080/api/v1/students
```

### Frontend Development

#### 1. Navigate to Frontend
```bash
cd frontend
```

#### 2. Serve Static Files
```bash
# Using Python (simplest)
python -m http.server 3000

# Using Node.js serve
npx serve -s src -p 3000

# Or using Docker
docker build -t piano-teacher-frontend .
docker run -p 3000:80 piano-teacher-frontend
```

#### 3. Configure API Endpoint
Edit `frontend/src/js/api.js` to point to your backend:
```javascript
const API_BASE_URL = 'http://localhost:8080/api/v1';
```

## Production Deployment

### Option 1: Docker Compose (Recommended)
```bash
# Configure environment variables
cp .env.example .env
# Edit .env with your settings

# Deploy
docker-compose -f docker-compose.prod.yml up -d
```

### Option 2: Manual Deployment

#### Backend Deployment
```bash
# Build JAR
cd backend
mvn clean package -Pprod

# Run with PostgreSQL
java -jar target/piano-teacher-1.0.0.jar --spring.profiles.active=prod
```

#### Frontend Deployment
```bash
# Build for production
cd frontend
docker build -t piano-teacher-frontend-prod .

# Serve with nginx
docker run -d -p 80:80 piano-teacher-frontend-prod
```

## Configuration

### Environment Variables

#### Backend (.env)
```bash
# Database
DB_HOST=localhost
DB_PORT=5432
DB_NAME=piano_teacher
DB_USERNAME=postgres
DB_PASSWORD=your_password

# Security
JWT_SECRET=your_jwt_secret_key
ADMIN_PASSWORD=your_admin_password

# Application
SERVER_PORT=8080
CORS_ALLOWED_ORIGINS=http://localhost:3000
```

#### Frontend
```javascript
// frontend/src/js/config.js
window.APP_CONFIG = {
    API_BASE_URL: 'https://api.yourdomain.com/api/v1',
    DEFAULT_LANGUAGE: 'en',
    SUPPORTED_LANGUAGES: ['en', 'zh']
};
```

## Database Setup (Production)

### PostgreSQL Setup
```sql
-- Create database
CREATE DATABASE piano_teacher;

-- Create user
CREATE USER piano_teacher_user WITH PASSWORD 'your_password';

-- Grant permissions
GRANT ALL PRIVILEGES ON DATABASE piano_teacher TO piano_teacher_user;
```

### Database Migration
The application uses Flyway for automatic migrations. Migrations run on startup.

## Testing

### Backend Tests
```bash
cd backend

# Run all tests
mvn test

# Run specific test
mvn test -Dtest=StudentServiceTest

# Generate test coverage report
mvn jacoco:report
```

### Frontend Tests
```bash
cd frontend

# Run E2E tests (if configured)
npm run test:e2e

# Manual testing
# Open http://localhost:3000 in browser
# Test all user workflows
```

## Common Issues & Solutions

### Port Conflicts
- Change ports in `docker-compose.yml`
- Or kill existing processes: `lsof -ti:8080 | xargs kill -9`

### Database Connection Issues
- Verify PostgreSQL is running
- Check connection string in application.properties
- Ensure database user has correct permissions

### CORS Issues
- Configure allowed origins in backend
- Check API_BASE_URL in frontend configuration

### Chinese Character Issues
- Ensure UTF-8 encoding in database
- Verify browser charset settings
- Check font support for Chinese characters

## Development Workflow

### 1. Make Changes
- Edit backend code in `backend/src/`
- Edit frontend code in `frontend/src/`

### 2. Test Locally
```bash
# Backend
cd backend && mvn spring-boot:run

# Frontend
cd frontend && python -m http.server 3000
```

### 3. Run Tests
```bash
# Backend tests
mvn test

# Manual frontend testing
# Open browser and test workflows
```

### 4. Deploy Changes
```bash
# Rebuild and restart containers
docker-compose down
docker-compose up --build -d
```

## API Usage Examples

### Create Student
```bash
curl -X POST http://localhost:8080/api/v1/students \
  -H "Content-Type: application/json" \
  -u admin:admin123 \
  -d '{
    "name": "张小明",
    "age": 12,
    "grade": "六年级",
    "email": "student@example.com",
    "pricing": {
      "hourlyRate": 150.00,
      "lessonDuration": 1.0
    }
  }'
```

### Schedule Lesson
```bash
curl -X POST http://localhost:8080/api/v1/lessons \
  -H "Content-Type: application/json" \
  -u admin:admin123 \
  -d '{
    "startTime": "2025-01-30T14:00:00",
    "endTime": "2025-01-30T15:00:00",
    "studentId": 1
  }'
```

### Get Scheduling Suggestions
```bash
curl "http://localhost:8080/api/v1/schedule/suggestions?studentId=1&startDate=2025-02-01&endDate=2025-02-07&duration=60" \
  -u admin:admin123
```

## Monitoring and Maintenance

### Health Checks
- Backend: http://localhost:8080/actuator/health
- Frontend: Check browser console for errors

### Logs
```bash
# Backend logs
docker-compose logs backend

# Frontend logs (nginx)
docker-compose logs frontend
```

### Backup Database
```bash
# PostgreSQL backup
pg_dump -h localhost -U piano_teacher_user piano_teacher > backup.sql

# Restore
psql -h localhost -U piano_teacher_user piano_teacher < backup.sql
```

## Support

### Documentation
- API Documentation: http://localhost:8080/swagger-ui.html
- Data Model: `specs/001-piano-teacher/data-model.md`
- API Contracts: `specs/001-piano-teacher/contracts/api.yaml`

### Troubleshooting
1. Check container status: `docker-compose ps`
2. Review logs: `docker-compose logs [service]`
3. Verify network connectivity
4. Check environment variables
5. Validate database connection

### Getting Help
- Review this guide first
- Check application logs
- Consult API documentation
- Review data model documentation
