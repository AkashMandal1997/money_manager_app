# 💰 Money Manager

A comprehensive full-stack personal finance management application that helps users track their income, expenses, and financial health with intuitive visualizations and insights.

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen)
![Java](https://img.shields.io/badge/Java-21-orange)
![React](https://img.shields.io/badge/React-18.2-blue)
![Vite](https://img.shields.io/badge/Vite-6.0-646CFF)
![TailwindCSS](https://img.shields.io/badge/TailwindCSS-4.1-38B2AC)
![License](https://img.shields.io/badge/license-MIT-green)

## 📋 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Architecture](#-architecture)
- [Getting Started](#-getting-started)
  - [Prerequisites](#prerequisites)
  - [Backend Setup](#backend-setup)
  - [Frontend Setup](#frontend-setup)
- [Environment Variables](#-environment-variables)
- [API Documentation](#-api-documentation)
- [Deployment](#-deployment)
- [Project Structure](#-project-structure)
- [Contributing](#-contributing)
- [License](#-license)

## ✨ Features

### Core Functionality
- 🔐 **User Authentication & Authorization** - Secure JWT-based authentication with Spring Security
- 💵 **Income Management** - Track multiple income sources with categories and timestamps
- 💳 **Expense Tracking** - Record and categorize expenses with detailed descriptions
- 📊 **Interactive Dashboard** - Real-time financial overview with charts and statistics
- 🏷️ **Category Management** - Create custom categories for income and expenses with emoji support
- 📈 **Data Visualization** - Pie charts, line graphs, and detailed financial insights using Recharts
- 🔍 **Advanced Filtering** - Filter transactions by date range, category, and type
- 📧 **Email Notifications** - Send financial reports via email
- 📥 **Excel Export** - Download income and expense data as Excel files
- 👤 **User Profile** - Manage profile information with photo upload support
- 🌐 **Responsive Design** - Fully responsive UI that works on all devices

### Technical Features
- RESTful API architecture
- Database support for PostgreSQL (production) and MySQL (local)
- JWT token-based security
- File upload integration with Cloudinary
- Email service integration with Brevo (SendinBlue)
- Scheduled tasks with Spring Scheduler
- Excel generation with Apache POI
- Modern React with hooks and context API
- Optimized build with Vite

## 🛠 Tech Stack

### Backend
- **Framework:** Spring Boot 3.5.7
- **Language:** Java 21
- **Security:** Spring Security + JWT (jjwt 0.13.0)
- **Database:** PostgreSQL (Production), MySQL (Development)
- **ORM:** Spring Data JPA / Hibernate
- **Build Tool:** Maven
- **Email:** Spring Mail + Brevo SMTP
- **Excel Export:** Apache POI 5.4.1
- **Additional:** Lombok, Spring Actuator, Spring Dotenv

### Frontend
- **Framework:** React 18.2
- **Build Tool:** Vite 6.0
- **Styling:** TailwindCSS 4.1
- **Routing:** React Router DOM 7.6
- **HTTP Client:** Axios 1.10
- **Charts:** Recharts 3.0
- **UI Components:** Lucide React (icons)
- **Emoji Picker:** emoji-picker-react 4.12
- **Notifications:** React Hot Toast 2.5
- **Date Handling:** Moment.js 2.30

### DevOps
- **Containerization:** Docker
- **Base Image:** Eclipse Temurin 21 JRE
- **Deployment:** Production-ready with environment profiles

## 🏗 Architecture

```
money_manager/
├── moneymanager/              # Spring Boot Backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/in/bushansirgur/moneymanager/
│   │   │   │   ├── config/           # Security & CORS configuration
│   │   │   │   ├── controller/       # REST API endpoints
│   │   │   │   ├── dto/              # Data Transfer Objects
│   │   │   │   ├── entity/           # JPA Entities
│   │   │   │   ├── repository/       # Data repositories
│   │   │   │   ├── security/         # JWT & Security utilities
│   │   │   │   ├── service/          # Business logic
│   │   │   │   └── util/             # Helper utilities
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       └── application-prod.properties
│   │   └── test/
│   ├── Dockerfile
│   └── pom.xml
│
└── moneymanagerwebapp/        # React Frontend
    ├── src/
    │   ├── components/        # Reusable UI components
    │   ├── context/           # React Context for state management
    │   ├── hooks/             # Custom React hooks
    │   ├── pages/             # Page components
    │   ├── util/              # API config & utilities
    │   ├── App.jsx
    │   └── main.jsx
    ├── public/
    ├── index.html
    ├── package.json
    └── vite.config.js
```

## 🚀 Getting Started

### Prerequisites

- **Java Development Kit (JDK) 21** or higher
- **Maven 3.8+** (or use included Maven wrapper)
- **Node.js 18+** and npm/yarn
- **PostgreSQL 14+** or **MySQL 8+**
- **Git**

### Backend Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/AkashMandal1997/money_manager_app.git
   cd money_manager_app/moneymanager
   ```

2. **Configure Database**
   
   Create a `.env` file in the `moneymanager` directory:
   ```env
   # PostgreSQL Configuration (Production)
   POSTGRES_URL=jdbc:postgresql://localhost:5432/moneymanager
   POSTGRES_USERNAME=your_username
   POSTGRES_PASSWORD=your_password
   
   # Email Configuration (Brevo)
   BREVO_USERNAME=your_brevo_username
   BREVO_PASSWORD=your_brevo_api_key
   BREVO_FROM_EMAIL=your_verified_sender_email
   ```

   Or for MySQL (local development), update `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/moneymanager
   spring.datasource.username=root
   spring.datasource.password=your_password
   ```

3. **Build the application**
   ```bash
   ./mvnw clean install
   # or
   mvn clean install
   ```

4. **Run the backend**
   ```bash
   ./mvnw spring-boot:run
   # or
   mvn spring-boot:run
   ```

   The API will be available at `http://localhost:8080/api/v1.0`

### Frontend Setup

1. **Navigate to frontend directory**
   ```bash
   cd ../moneymanagerwebapp
   ```

2. **Install dependencies**
   ```bash
   npm install
   # or
   yarn install
   ```

3. **Configure API endpoint**
   
   Update `src/util/apiEndpoints.js` if needed:
   ```javascript
   export const BASE_URL = "http://localhost:8080/api/v1.0";
   ```

4. **Run the development server**
   ```bash
   npm run dev
   # or
   yarn dev
   ```

   The application will be available at `http://localhost:5173`

## 🔐 Environment Variables

### Backend (.env)

| Variable | Description | Required |
|----------|-------------|----------|
| `POSTGRES_URL` | PostgreSQL connection URL | Yes (Production) |
| `POSTGRES_USERNAME` | PostgreSQL username | Yes (Production) |
| `POSTGRES_PASSWORD` | PostgreSQL password | Yes (Production) |
| `BREVO_USERNAME` | Brevo SMTP username | Yes |
| `BREVO_PASSWORD` | Brevo API key | Yes |
| `BREVO_FROM_EMAIL` | Verified sender email | Yes |

### Frontend

The frontend uses hardcoded API endpoints in `src/util/apiEndpoints.js`. For production, update the `BASE_URL` to your production backend URL.

## 📡 API Documentation

### Authentication Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1.0/register` | Register new user |
| POST | `/api/v1.0/login` | User login |
| GET | `/api/v1.0/profile` | Get user profile |

### Income Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1.0/incomes` | Get all incomes |
| POST | `/api/v1.0/incomes` | Add new income |
| DELETE | `/api/v1.0/incomes/{id}` | Delete income |

### Expense Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1.0/expenses` | Get all expenses |
| POST | `/api/v1.0/expenses` | Add new expense |
| DELETE | `/api/v1.0/expenses/{id}` | Delete expense |

### Category Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1.0/categories` | Get all categories |
| GET | `/api/v1.0/categories/{type}` | Get categories by type |
| POST | `/api/v1.0/categories` | Add new category |
| PUT | `/api/v1.0/categories/{id}` | Update category |

### Additional Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1.0/dashboard` | Get dashboard statistics |
| POST | `/api/v1.0/filter` | Apply filters |
| GET | `/api/v1.0/excel/download/income` | Download income Excel |
| GET | `/api/v1.0/excel/download/expense` | Download expense Excel |
| POST | `/api/v1.0/email/income-excel` | Email income report |
| POST | `/api/v1.0/email/expense-excel` | Email expense report |

## 🐳 Deployment

### Using Docker

1. **Build the JAR file**
   ```bash
   cd moneymanager
   ./mvnw clean package -DskipTests
   ```

2. **Build Docker image**
   ```bash
   docker build -t money-manager-backend .
   ```

3. **Run the container**
   ```bash
   docker run -p 8080:8080 \
     -e POSTGRES_URL=your_db_url \
     -e POSTGRES_USERNAME=your_username \
     -e POSTGRES_PASSWORD=your_password \
     -e BREVO_USERNAME=your_brevo_username \
     -e BREVO_PASSWORD=your_brevo_password \
     -e BREVO_FROM_EMAIL=your_email \
     money-manager-backend
   ```

### Frontend Deployment

1. **Build for production**
   ```bash
   cd moneymanagerwebapp
   npm run build
   ```

2. **Deploy the `dist` folder** to your hosting service (Vercel, Netlify, etc.)

### Production Considerations

- Enable HTTPS for secure communication
- Use environment-specific profiles (`application-prod.properties`)
- Configure CORS to allow only your frontend domain
- Set up database backups and monitoring
- Use a process manager (PM2) or container orchestration for backend
- Enable Spring Actuator endpoints for health checks

## 📂 Project Structure

### Backend Controllers
- `CategoryController` - Category CRUD operations
- `DashboardController` - Dashboard statistics
- `EmailController` - Email service endpoints
- `ExcelController` - Excel export functionality
- `ExpenseController` - Expense management
- `FilterController` - Transaction filtering
- `IncomeController` - Income management
- `ProfileController` - User profile management

### Frontend Pages
- `LandingPage` - Marketing/home page
- `Login/Signup` - Authentication pages
- `Home` (Dashboard) - Financial overview
- `Income` - Income management
- `Expense` - Expense tracking
- `Category` - Category management
- `Filter` - Advanced filtering

### Key Components
- `CustomPieChart` - Expense/income distribution
- `CustomLineChart` - Time-based financial trends
- `RecentTransactions` - Recent activity display
- `FinanceOverview` - Summary cards
- `EmojiPickerPopup` - Category emoji selection

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Development Guidelines
- Follow Java code conventions and Spring Boot best practices
- Use React hooks and functional components
- Write meaningful commit messages
- Add tests for new features
- Update documentation as needed

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👨‍💻 Author

**Akash Mandal**

- GitHub: [@AkashMandal1997](https://github.com/AkashMandal1997)
- Repository: [money_manager_app](https://github.com/AkashMandal1997/money_manager_app)

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- React community for the amazing ecosystem
- Contributors and supporters of open-source libraries used in this project

## 📞 Support

For issues, questions, or suggestions, please open an issue on GitHub or contact the maintainer.

---

⭐ If you find this project helpful, please consider giving it a star on GitHub!
