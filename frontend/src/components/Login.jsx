import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { Mail, Lock } from 'lucide-react';

const Login = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({ email: '', password: '' });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    
    try {
      const res = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData),
        credentials: 'include'
      });

      if (!res.ok) {
        const errText = await res.text();
        throw new Error(errText || 'Login failed');
      }

      const data = await res.json();
      // Store user details in localStorage for simple frontend state management
      localStorage.setItem('user', JSON.stringify(data));
      
      if (data.role === 'ADMIN') {
        navigate('/admin');
      } else {
        navigate(`/dashboard/${data.id}`);
      }
    } catch (err) {
      console.error(err);
      setError(err.message || 'Login failed. Please check your credentials.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="glass-panel animate-fade-in" style={{ maxWidth: '400px', margin: '4rem auto 0', padding: '2.5rem' }}>
      <div style={{ textAlign: 'center', marginBottom: '2rem' }}>
        <h2 style={{ fontSize: '1.75rem', margin: '0 0 0.5rem 0' }}>Welcome Back</h2>
        <p style={{ color: 'var(--text-secondary)', margin: 0, fontSize: '0.95rem' }}>Log in to view and pay your fees</p>
      </div>

      <form onSubmit={handleSubmit}>
        <div className="input-group">
          <label className="input-label" style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
            <Mail size={16} /> Email Address
          </label>
          <input 
            type="email" 
            required 
            className="input-field" 
            placeholder="student@example.com"
            value={formData.email}
            onChange={e => setFormData({...formData, email: e.target.value})}
          />
        </div>

        <div className="input-group">
          <label className="input-label" style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
            <Lock size={16} /> Password
          </label>
          <input 
            type="password" 
            required 
            className="input-field" 
            placeholder="••••••••"
            value={formData.password}
            onChange={e => setFormData({...formData, password: e.target.value})}
          />
        </div>

        {error && <p style={{ color: 'var(--danger-color)', fontSize: '0.875rem', marginBottom: '1rem', textAlign: 'center' }}>{error}</p>}

        <button type="submit" className="btn btn-primary" style={{ width: '100%', marginTop: '0.5rem' }} disabled={loading}>
          {loading ? 'Logging in...' : 'Log In'}
        </button>
      </form>
      
      <p style={{ textAlign: 'center', margin: '1.5rem 0 0', fontSize: '0.9rem', color: 'var(--text-secondary)' }}>
        Don't have an account? <Link to="/register" style={{ color: 'var(--accent-primary)', textDecoration: 'none', fontWeight: 500 }}>Register</Link>
      </p>
    </div>
  );
};

export default Login;
