import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { User, Mail, BookOpen, Calendar, Shield, Lock } from 'lucide-react';

const Registration = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    password: '',
    course: '',
    admissionYear: new Date().getFullYear(),
    categoryId: 1 // default to first category
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      const res = await fetch('http://localhost:8080/api/auth/register', { 
        method: 'POST', 
        body: JSON.stringify(formData), 
        headers: {'Content-Type': 'application/json'} 
      });
      
      if (!res.ok) {
        const errText = await res.text();
        throw new Error(errText || 'Registration failed');
      }
      
      const data = await res.json();
      // Registration successful, redirect to login
      navigate('/login', { state: { message: 'Registration successful! Please log in.' } });
    } catch (err) {
      console.error(err);
      setError(err.message || 'Registration failed. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="glass-panel animate-fade-in" style={{ maxWidth: '500px', margin: '0 auto', padding: '2.5rem' }}>
      <div style={{ textAlign: 'center', margin: '0 0 2rem' }}>
        <h2 style={{ fontSize: '1.75rem', margin: '0 0 0.5rem 0' }}>Create Profile</h2>
        <p style={{ color: 'var(--text-secondary)', margin: 0, fontSize: '0.95rem' }}>Register to view and manage your fee structure</p>
      </div>

      <form onSubmit={handleSubmit}>
        <div className="input-group">
          <label className="input-label" style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
            <User size={16} /> Full Name
          </label>
          <input 
            type="text" 
            required 
            className="input-field" 
            placeholder="e.g. John Doe"
            value={formData.name}
            onChange={e => setFormData({...formData, name: e.target.value})}
          />
        </div>

        <div className="input-group">
          <label className="input-label" style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
            <Mail size={16} /> Email Address
          </label>
          <input 
            type="email" 
            required 
            className="input-field" 
            placeholder="john@example.com"
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
            placeholder="Create a strong password"
            value={formData.password}
            onChange={e => setFormData({...formData, password: e.target.value})}
          />
        </div>

        <div className="input-group">
          <label className="input-label" style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
            <BookOpen size={16} /> Course
          </label>
          <input 
            type="text" 
            required 
            className="input-field" 
            placeholder="e.g. B.Tech Computer Science"
            value={formData.course}
            onChange={e => setFormData({...formData, course: e.target.value})}
          />
        </div>

        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
          <div className="input-group">
            <label className="input-label" style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
              <Calendar size={16} /> Admission Year
            </label>
            <input 
              type="number" 
              required 
              className="input-field" 
              value={formData.admissionYear}
              onChange={e => setFormData({...formData, admissionYear: parseInt(e.target.value)})}
            />
          </div>

          <div className="input-group">
            <label className="input-label" style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
              <Shield size={16} /> Category
            </label>
            <select 
              className="input-field"
              value={formData.categoryId}
              onChange={e => setFormData({...formData, categoryId: parseInt(e.target.value)})}
            >
              <option value={1}>General (Open)</option>
              <option value={2}>SC/ST</option>
              <option value={3}>VJ/NT/SBC/OBC (Valid NC)</option>
              <option value={4}>EBC/EWS/SEBC</option>
              <option value={5}>TFWS</option>
              <option value={6}>JK Migrant / NE</option>
              <option value={7}>PMSSS</option>
              <option value={8}>CIWGC</option>
              <option value={9}>Girls OPEN/OBC...</option>
              <option value={10}>JEE All India Quota</option>
              <option value={11}>Spot Round</option>
            </select>
          </div>
        </div>
        
        {error && <p style={{ color: 'var(--danger-color)', fontSize: '0.875rem', marginBottom: '1rem', textAlign: 'center' }}>{error}</p>}

        <button type="submit" className="btn btn-primary" style={{ width: '100%', marginTop: '1rem' }} disabled={loading}>
          {loading ? 'Creating Profile...' : 'Register Profile'}
        </button>
      </form>

      <p style={{ textAlign: 'center', margin: '1.5rem 0 0', fontSize: '0.9rem', color: 'var(--text-secondary)' }}>
        Already registered? <Link to="/login" style={{ color: 'var(--accent-primary)', textDecoration: 'none', fontWeight: 500 }}>Log In</Link>
      </p>
    </div>
  );
};

export default Registration;
