import { BrowserRouter, Routes, Route, Navigate, Link, useLocation } from 'react-router-dom';
import Registration from './components/Registration';
import Dashboard from './components/Dashboard';
import AdminDashboard from './components/AdminDashboard';
import Login from './components/Login';

function NavBar() {
  const location = useLocation();
  const isAdmin = location.pathname === '/admin';
  const isAuthPage = location.pathname === '/login' || location.pathname === '/register';

  return (
    <nav style={{
      display: 'flex', justifyContent: 'space-between', alignItems: 'center',
      padding: '1rem 2rem', borderBottom: '1px solid var(--border-color)',
      background: 'rgba(15,23,42,0.8)', backdropFilter: 'blur(12px)',
      position: 'sticky', top: 0, zIndex: 100
    }}>
      <Link to="/" style={{ textDecoration: 'none' }}>
        <span style={{ fontWeight: 700, fontSize: '1.2rem', background: 'linear-gradient(135deg,#3b82f6,#8b5cf6)', WebkitBackgroundClip: 'text', WebkitTextFillColor: 'transparent' }}>
          🎓 College Fee Portal
        </span>
      </Link>
      <div style={{ display: 'flex', gap: '1rem' }}>
        {isAuthPage ? (
          <Link to={location.pathname === '/login' ? '/register' : '/login'} style={{ textDecoration: 'none', color: 'var(--text-secondary)', fontSize: '0.9rem', fontWeight: 600 }}>
            {location.pathname === '/login' ? 'Register' : 'Login'}
          </Link>
        ) : (
          <Link to="/login" style={{ textDecoration: 'none', color: 'var(--danger-color)', fontSize: '0.9rem', fontWeight: 600 }} onClick={() => localStorage.removeItem('user')}>
            Logout
          </Link>
        )}
      </div>
    </nav>
  );
}

function App() {
  return (
    <BrowserRouter>
      <NavBar />
      <div className="app-container">
        <Routes>
          <Route path="/" element={<Navigate to="/login" replace />} />
          <Route path="/login" element={
            <>
              <div className="page-header animate-fade-in">
                <h1 className="page-title">College Fee Portal</h1>
                <p className="page-subtitle">Manage your academic fee payments seamlessly</p>
              </div>
              <Login />
            </>
          } />
          <Route path="/register" element={
            <>
              <div className="page-header animate-fade-in">
                <h1 className="page-title">College Fee Portal</h1>
                <p className="page-subtitle">Manage your academic fee payments seamlessly</p>
              </div>
              <Registration />
            </>
          } />
          <Route path="/dashboard/:studentId" element={<Dashboard />} />
          <Route path="/admin" element={<AdminDashboard />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
}

export default App;
