# College Fee Portal

A robust, enterprise-grade College Fee Portal designed to streamline academic fee payments for students and administration. Built with a modern **Java/Spring Boot** backend and a dynamic **React/Vite** frontend.

## 🌟 Key Features

### For Students
- **Secure Authentication:** BCrypt-based password encryption and session management.
- **Automated Fee Calculation:** Fees are calculated dynamically based on admission year, course, and category (e.g., General, OBC, SC/ST).
- **Sequential Payments:** Enforces structured payment windows to ensure fees are paid in the correct sequence.
- **Receipt Generation:** Automatically generates downloadable PDF receipts for successful transactions.
- **Dashboard:** An intuitive interface to track pending and completed payments.

### For Administrators
- **Admin Dashboard:** Centralized view for managing student data and fee structures.
- **Fee Structure Management:** Ability to define and update official multi-category fee structures.
- **Payment Tracking:** Monitor transactions and ensure atomicity and consistency across the database.

## 🛠️ Technology Stack

**Backend:**
- Java 21
- Spring Boot
- Spring Security (Authentication & Authorization)
- Spring Data JPA (Hibernate)
- MySQL Database
- OpenPDF (for receipt generation)
- Lombok

**Frontend:**
- React 19 (via Vite)
- React Router DOM
- Lucide React (for icons)
- Vanilla CSS / Modular styling

## 🚀 Getting Started

### Prerequisites
- [Java 21+](https://jdk.java.net/)
- [Node.js](https://nodejs.org/) & npm
- [MySQL Server](https://dev.mysql.com/downloads/mysql/)

### Local Setup Instructions

#### 1. Database Configuration
Create a MySQL database for the application (e.g., `fee_portal`):
```sql
CREATE DATABASE fee_portal;
```
Update your database credentials in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/fee_portal
spring.datasource.username=root
spring.datasource.password=your_password
```

#### 2. Running the Backend (Spring Boot)
Open a terminal in the root directory and run the Maven wrapper:
```bash
./mvnw spring-boot:run
```
The backend server will start on `http://localhost:8080`.

#### 3. Running the Frontend (React/Vite)
Open a new terminal and navigate to the `frontend` directory:
```bash
cd frontend
npm install
npm run dev
```
The frontend application will typically be accessible at `http://localhost:5173`.

## 📂 Project Structure
```
FeePortal/
├── frontend/             # React application (Vite setup)
│   ├── src/              # React components, pages, and assets
│   └── package.json      # Node dependencies and scripts
├── src/                  # Spring Boot application
│   └── main/java/...     # Controllers, Services, Repositories, Entities
├── pom.xml               # Maven dependencies and build configuration
└── README.md             # Project documentation
```

## 🔒 Security & Architecture
- **Transaction Atomicity:** Ensures that partial or failed payments do not corrupt student fee records.
- **Role-Based Access:** Distinct access levels for `STUDENT` and `ADMIN` roles.
- **RESTful API:** Clean separation of concerns between the frontend client and the backend server.
