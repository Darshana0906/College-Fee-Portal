import { useState, useEffect } from 'react';
import { Calendar, Unlock, CheckCircle2, GraduationCap, Users, AlertCircle } from 'lucide-react';

const AdminDashboard = () => {
  const [activeTab, setActiveTab] = useState('windows');
  const [pendingScholarships, setPendingScholarships] = useState([]);
  const [windowForm, setWindowForm] = useState({
    yearOfStudy: 1,
    academicYear: '2024-2025',
    startDate: '',
    endDate: '',
  });
  const [windowMsg, setWindowMsg] = useState('');
  const [windowErr, setWindowErr] = useState('');
  const [creditingId, setCreditingId] = useState(null);

  const fetchPendingScholarships = async () => {
    try {
      const res = await fetch('http://localhost:8080/api/admin/scholarship-pending', { credentials: 'include' });
      if (res.ok) setPendingScholarships(await res.json());
    } catch (e) { console.error(e); }
  };

  useEffect(() => { fetchPendingScholarships(); }, []);

  const handleOpenWindow = async (e) => {
    e.preventDefault();
    setWindowMsg(''); setWindowErr('');
    try {
      const res = await fetch('http://localhost:8080/api/admin/payment-windows', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(windowForm),
        credentials: 'include'
      });
      if (!res.ok) throw new Error(await res.text());
      setWindowMsg(`✅ Payment window for Year ${windowForm.yearOfStudy} opened successfully!`);
    } catch (err) {
      setWindowErr(`❌ ${err.message}`);
    }
  };

  const handleMarkCredited = async (feeRecordId) => {
    setCreditingId(feeRecordId);
    try {
      const res = await fetch(`http://localhost:8080/api/admin/fee-records/${feeRecordId}/scholarship-credit`, {
        method: 'PUT',
        credentials: 'include'
      });
      if (!res.ok) throw new Error(await res.text());
      await fetchPendingScholarships(); // refresh list
    } catch (err) {
      alert(`Failed: ${err.message}`);
    }
    setCreditingId(null);
  };

  const yearLabels = ['', 'First Year (FY)', 'Second Year (SY)', 'Third Year (TY)', 'Final Year'];

  return (
    <div className="animate-fade-in">
      <div style={{ marginBottom: '2.5rem' }}>
        <h2 style={{ margin: '0 0 0.4rem 0', fontSize: '1.75rem' }}>Admin Dashboard</h2>
        <p style={{ color: 'var(--text-secondary)', margin: 0 }}>Manage payment windows and scholarship credits</p>
      </div>

      {/* Tabs */}
      <div style={{ display: 'flex', gap: '0.5rem', marginBottom: '2rem', borderBottom: '1px solid var(--border-color)', paddingBottom: '0' }}>
        {[
          { id: 'windows', label: 'Payment Windows', icon: <Unlock size={16} /> },
          { id: 'scholarships', label: `Scholarship Pending (${pendingScholarships.length})`, icon: <GraduationCap size={16} /> },
        ].map(tab => (
          <button key={tab.id} onClick={() => setActiveTab(tab.id)}
            style={{
              display: 'flex', alignItems: 'center', gap: '0.5rem',
              background: 'none', border: 'none', color: activeTab === tab.id ? 'var(--accent-primary)' : 'var(--text-secondary)',
              borderBottom: activeTab === tab.id ? '2px solid var(--accent-primary)' : '2px solid transparent',
              paddingBottom: '0.75rem', cursor: 'pointer', fontFamily: 'inherit', fontWeight: 600, fontSize: '0.95rem',
              transition: 'all 0.2s ease'
            }}>
            {tab.icon} {tab.label}
          </button>
        ))}
      </div>

      {/* Payment Windows Tab */}
      {activeTab === 'windows' && (
        <div style={{ maxWidth: '520px' }}>
          <div className="glass-panel" style={{ padding: '2rem' }}>
            <h3 style={{ margin: '0 0 1.5rem 0', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
              <Calendar size={20} color="var(--accent-primary)" /> Open Payment Window
            </h3>

            <form onSubmit={handleOpenWindow}>
              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                <div className="input-group">
                  <label className="input-label">Year of Study</label>
                  <select className="input-field" value={windowForm.yearOfStudy}
                    onChange={e => setWindowForm({ ...windowForm, yearOfStudy: parseInt(e.target.value) })}>
                    <option value={1}>First Year (FY)</option>
                    <option value={2}>Second Year (SY)</option>
                    <option value={3}>Third Year (TY)</option>
                    <option value={4}>Final Year</option>
                  </select>
                </div>
                <div className="input-group">
                  <label className="input-label">Academic Year</label>
                  <input type="text" className="input-field" placeholder="e.g. 2024-2025"
                    value={windowForm.academicYear}
                    onChange={e => setWindowForm({ ...windowForm, academicYear: e.target.value })} />
                </div>
              </div>

              <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
                <div className="input-group">
                  <label className="input-label">Start Date</label>
                  <input type="date" required className="input-field"
                    value={windowForm.startDate}
                    onChange={e => setWindowForm({ ...windowForm, startDate: e.target.value })} />
                </div>
                <div className="input-group">
                  <label className="input-label">End Date</label>
                  <input type="date" required className="input-field"
                    value={windowForm.endDate}
                    onChange={e => setWindowForm({ ...windowForm, endDate: e.target.value })} />
                </div>
              </div>

              {windowMsg && <p style={{ color: 'var(--success-color)', margin: '0 0 1rem', fontSize: '0.875rem' }}>{windowMsg}</p>}
              {windowErr && <p style={{ color: 'var(--danger-color)', margin: '0 0 1rem', fontSize: '0.875rem' }}>{windowErr}</p>}

              <button type="submit" className="btn btn-primary" style={{ width: '100%' }}>
                <Unlock size={16} /> Open Payment Window
              </button>
            </form>
          </div>

          <div className="glass-panel" style={{ padding: '1.5rem', marginTop: '1.5rem' }}>
            <p style={{ margin: 0, display: 'flex', gap: '0.5rem', alignItems: 'flex-start', color: 'var(--text-secondary)', fontSize: '0.875rem', lineHeight: '1.6' }}>
              <AlertCircle size={18} style={{ flexShrink: 0, marginTop: '2px', color: 'var(--warning-color)' }} />
              Opening a payment window will automatically generate fee records for all registered students in that year, locking in the current fee structure amounts.
            </p>
          </div>
        </div>
      )}

      {/* Scholarships Tab */}
      {activeTab === 'scholarships' && (
        <div>
          {pendingScholarships.length === 0 ? (
            <div className="glass-panel" style={{ padding: '3rem', textAlign: 'center' }}>
              <CheckCircle2 size={48} color="var(--success-color)" style={{ marginBottom: '1rem' }} />
              <p style={{ margin: 0, color: 'var(--text-secondary)' }}>No pending scholarship credits. All up to date!</p>
            </div>
          ) : (
            <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
              {pendingScholarships.map(record => (
                <div key={record.id} className="glass-panel" style={{ padding: '1.25rem', display: 'flex', alignItems: 'center', justifyContent: 'space-between', gap: '1rem', flexWrap: 'wrap' }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '1rem' }}>
                    <div style={{ background: 'rgba(245,158,11,0.15)', padding: '0.75rem', borderRadius: '50%', color: 'var(--warning-color)' }}>
                      <GraduationCap size={24} />
                    </div>
                    <div>
                      <p style={{ margin: '0 0 0.2rem', fontWeight: 600 }}>{record.student?.name}</p>
                      <p style={{ margin: 0, color: 'var(--text-secondary)', fontSize: '0.85rem' }}>
                        {yearLabels[record.yearOfStudy]} — {record.academicYear}
                      </p>
                    </div>
                  </div>
                  <div style={{ textAlign: 'right', minWidth: '120px' }}>
                    <p style={{ margin: '0 0 0.2rem', fontWeight: 700, color: 'var(--warning-color)', fontSize: '1.1rem' }}>
                      ₹ {Number(record.scholarshipAmount).toLocaleString('en-IN')}
                    </p>
                    <p style={{ margin: 0, fontSize: '0.75rem', color: 'var(--text-secondary)' }}>Scholarship pending</p>
                  </div>
                  <button className="btn btn-primary" style={{ padding: '0.5rem 1.25rem', fontSize: '0.875rem' }}
                    disabled={creditingId === record.id}
                    onClick={() => handleMarkCredited(record.id)}>
                    {creditingId === record.id ? 'Updating...' : '✓ Mark Credited'}
                  </button>
                </div>
              ))}
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default AdminDashboard;
