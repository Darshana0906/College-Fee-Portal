import { useState } from 'react';
import { X, CreditCard, ShieldCheck, GraduationCap } from 'lucide-react';

const PaymentModal = ({ isOpen, onClose, feeRecord, onSuccess }) => {
  const [pin, setPin] = useState('');
  const [scholarshipAmount, setScholarshipAmount] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  if (!isOpen || !feeRecord) return null;

  const scholarship = parseFloat(scholarshipAmount) || 0;
  const totalDue = parseFloat(feeRecord.amountDue) || 0;
  const studentPayable = Math.max(totalDue - scholarship, 0);

  const handlePay = async (e) => {
    e.preventDefault();
    if (pin.length < 4) {
      setError('Please enter a valid 4-digit PIN');
      return;
    }
    if (scholarship > totalDue) {
      setError('Scholarship amount cannot exceed total fee due');
      return;
    }

    setLoading(true);
    setError('');

    try {
      const res = await fetch(`http://localhost:8080/api/payment/${feeRecord.id}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ scholarshipAmount: scholarship }),
        credentials: 'include'
      });

      if (!res.ok) {
        const errText = await res.text();
        throw new Error(errText || 'Payment failed on server');
      }

      const data = await res.json();
      setLoading(false);
      onSuccess(data, scholarship);
    } catch (err) {
      console.error(err);
      setError(err.message || 'Payment failed. Please try again.');
      setLoading(false);
    }
  };

  const yearLabel = ['', 'First Year (FY)', 'Second Year (SY)', 'Third Year (TY)', 'Final Year'];

  return (
    <div style={{
      position: 'fixed', top: 0, left: 0, right: 0, bottom: 0,
      backgroundColor: 'rgba(0,0,0,0.75)',
      backdropFilter: 'blur(6px)',
      display: 'flex', alignItems: 'center', justifyContent: 'center',
      zIndex: 1000, padding: '1rem'
    }}>
      <div className="glass-panel animate-fade-in" style={{ width: '100%', maxWidth: '480px', padding: '2rem', position: 'relative' }}>
        <button onClick={onClose} style={{ position: 'absolute', top: '1rem', right: '1rem', background: 'transparent', border: 'none', color: 'var(--text-secondary)', cursor: 'pointer' }}>
          <X size={24} />
        </button>

        <div style={{ textAlign: 'center', marginBottom: '1.5rem' }}>
          <div style={{ background: 'rgba(59,130,246,0.15)', width: '60px', height: '60px', borderRadius: '50%', display: 'flex', alignItems: 'center', justifyContent: 'center', margin: '0 auto 1rem', color: 'var(--accent-primary)' }}>
            <CreditCard size={28} />
          </div>
          <h2 style={{ margin: '0 0 0.25rem 0' }}>Secure Payment</h2>
          <p style={{ color: 'var(--text-secondary)', margin: 0, fontSize: '0.9rem' }}>{yearLabel[feeRecord.yearOfStudy]} — {feeRecord.academicYear}</p>
        </div>

        {/* Fee Breakdown */}
        {feeRecord.feeStructure && (
          <div style={{ background: 'rgba(0,0,0,0.2)', borderRadius: '8px', padding: '1rem', marginBottom: '1rem', border: '1px solid rgba(255,255,255,0.05)', fontSize: '0.875rem' }}>
            <p style={{ margin: '0 0 0.75rem 0', fontWeight: 600, color: 'var(--text-secondary)', textTransform: 'uppercase', letterSpacing: '0.05em', fontSize: '0.75rem' }}>Fee Breakdown</p>
            {[
              { label: 'Tuition Fee', value: feeRecord.feeStructure.tuitionFee },
              { label: 'Development Fee', value: feeRecord.feeStructure.developmentFee },
              { label: 'Other Fees', value: feeRecord.feeStructure.otherFee },
              { label: 'Examination Fee', value: feeRecord.feeStructure.examFee },
              { label: 'Miscellaneous Fee', value: feeRecord.feeStructure.miscellaneousFee },
            ].map(({ label, value }) => value > 0 && (
              <div key={label} style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '0.4rem' }}>
                <span style={{ color: 'var(--text-secondary)' }}>{label}</span>
                <span>₹ {Number(value).toLocaleString('en-IN')}</span>
              </div>
            ))}
            <div style={{ display: 'flex', justifyContent: 'space-between', paddingTop: '0.5rem', borderTop: '1px dashed rgba(255,255,255,0.1)', fontWeight: 600 }}>
              <span>Total Due</span>
              <span style={{ color: 'var(--accent-primary)' }}>₹ {totalDue.toLocaleString('en-IN')}</span>
            </div>
          </div>
        )}

        <form onSubmit={handlePay}>
          {/* Scholarship Input */}
          <div className="input-group">
            <label className="input-label" style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
              <GraduationCap size={16} /> Scholarship Amount (if any)
            </label>
            <input
              type="number"
              min="0"
              max={totalDue}
              className="input-field"
              placeholder="₹ 0"
              value={scholarshipAmount}
              onChange={e => setScholarshipAmount(e.target.value)}
            />
            {scholarship > 0 && (
              <p style={{ margin: '0.25rem 0 0', fontSize: '0.8rem', color: 'var(--text-secondary)' }}>
                ₹ {scholarship.toLocaleString('en-IN')} will be marked pending until credited by your institution.
              </p>
            )}
          </div>

          {/* Amount Summary */}
          <div style={{ background: 'rgba(59,130,246,0.08)', border: '1px solid rgba(59,130,246,0.2)', borderRadius: '8px', padding: '1rem', marginBottom: '1.25rem' }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: scholarship > 0 ? '0.5rem' : 0 }}>
              <span style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>You Pay Now</span>
              <span style={{ fontWeight: 700, fontSize: '1.25rem', color: 'var(--accent-primary)' }}>
                ₹ {studentPayable.toLocaleString('en-IN')}
              </span>
            </div>
            {scholarship > 0 && (
              <div style={{ display: 'flex', justifyContent: 'space-between' }}>
                <span style={{ color: 'var(--text-secondary)', fontSize: '0.875rem' }}>Scholarship Pending</span>
                <span style={{ fontWeight: 600, color: 'var(--warning-color)', fontSize: '0.875rem' }}>
                  ₹ {scholarship.toLocaleString('en-IN')}
                </span>
              </div>
            )}
          </div>

          <div className="input-group">
            <label className="input-label" style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
              <ShieldCheck size={16} /> 4-Digit Security PIN
            </label>
            <input
              type="password"
              maxLength={4}
              required
              className="input-field"
              placeholder="••••"
              value={pin}
              onChange={e => setPin(e.target.value)}
              style={{ textAlign: 'center', fontSize: '1.5rem', letterSpacing: '0.5rem' }}
            />
          </div>

          {error && (
            <p style={{ color: 'var(--danger-color)', fontSize: '0.875rem', margin: '-0.5rem 0 1rem', textAlign: 'center' }}>{error}</p>
          )}

          <button type="submit" className="btn btn-primary" style={{ width: '100%' }} disabled={loading}>
            {loading ? 'Processing...' : `Pay ₹ ${studentPayable.toLocaleString('en-IN')}`}
          </button>
        </form>
      </div>
    </div>
  );
};

export default PaymentModal;
