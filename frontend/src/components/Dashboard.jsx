import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { CheckCircle2, Lock, Clock, Download, AlertCircle, GraduationCap } from 'lucide-react';
import PaymentModal from './PaymentModal';

const Dashboard = () => {
  const { studentId } = useParams();
  const [feeRecords, setFeeRecords] = useState([]);
  const [selectedRecord, setSelectedRecord] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [successMessage, setSuccessMessage] = useState('');
  const [expandedBreakdown, setExpandedBreakdown] = useState(null);

  const fetchRecords = async () => {
    try {
      const res = await fetch(`http://localhost:8080/api/students/${studentId}/fee-records`, { credentials: 'include' });
      if (res.ok) {
        const data = await res.json();
        setFeeRecords(data);
      }
    } catch (err) {
      console.error('Failed to fetch fee records', err);
    }
  };

  useEffect(() => {
    fetchRecords();
  }, [studentId]);

  const handlePayClick = (record) => {
    setSelectedRecord(record);
    setIsModalOpen(true);
  };

  const handlePaymentSuccess = (transaction, scholarshipAmount) => {
    setIsModalOpen(false);
    if (scholarshipAmount > 0) {
      setSuccessMessage(`Payment of ₹${(parseFloat(selectedRecord.amountDue) - scholarshipAmount).toLocaleString('en-IN')} successful! ₹${scholarshipAmount.toLocaleString('en-IN')} scholarship is pending institution credit.`);
    } else {
      setSuccessMessage(`Payment successful! Reference: ${transaction.referenceId}`);
    }
    fetchRecords(); // Refresh from backend
    setTimeout(() => setSuccessMessage(''), 8000);
  };

  const getStatusConfig = (yearIndex) => {
    const yearOfStudy = yearIndex + 1;
    const record = feeRecords.find(r => r.yearOfStudy === yearOfStudy);

    if (!record) {
      return {
        label: 'LOCKED', icon: <Lock size={18} />,
        color: 'var(--text-secondary)', bg: 'rgba(255,255,255,0.05)',
        text: 'Payment window not yet opened by the college.'
      };
    }

    switch (record.status) {
      case 'PAID':
        return {
          label: 'PAID', icon: <CheckCircle2 size={18} />,
          color: 'var(--success-color)', bg: 'rgba(16,185,129,0.1)',
          text: `Fully settled for ${record.academicYear}`
        };
      case 'PARTIALLY_PAID':
        return {
          label: 'PARTIALLY PAID', icon: <GraduationCap size={18} />,
          color: 'var(--warning-color)', bg: 'rgba(245,158,11,0.1)',
          text: `You paid ₹${Number(record.amountPaid).toLocaleString('en-IN')}. Scholarship of ₹${Number(record.scholarshipAmount).toLocaleString('en-IN')} is pending institution credit.`
        };
      case 'PENDING':
        return {
          label: 'PENDING', icon: <AlertCircle size={18} />,
          color: '#f87171', bg: 'rgba(239,68,68,0.1)',
          text: 'Payment window is open. Pay now.'
        };
      default:
        return {
          label: 'OVERDUE', icon: <Clock size={18} />,
          color: 'var(--danger-color)', bg: 'rgba(239,68,68,0.1)',
          text: 'Payment window has closed.'
        };
    }
  };

  const years = [
    { label: 'First Year (FY)', index: 0 },
    { label: 'Second Year (SY)', index: 1 },
    { label: 'Third Year (TY)', index: 2 },
    { label: 'Final Year', index: 3 },
  ];

  return (
    <div className="animate-fade-in">
      {successMessage && (
        <div style={{ background: 'rgba(16,185,129,0.15)', border: '1px solid var(--success-color)', color: '#fff', padding: '1rem 1.25rem', borderRadius: '10px', marginBottom: '2rem', display: 'flex', alignItems: 'flex-start', gap: '0.75rem' }}>
          <CheckCircle2 size={20} color="var(--success-color)" style={{ flexShrink: 0, marginTop: '2px' }} />
          <span>{successMessage}</span>
        </div>
      )}

      <div style={{ marginBottom: '2rem', display: 'flex', justifyContent: 'space-between', alignItems: 'flex-end', flexWrap: 'wrap', gap: '1rem' }}>
        <div>
          <h2 style={{ margin: '0 0 0.4rem 0', fontSize: '1.75rem' }}>Your Fee Dashboard</h2>
          <p style={{ color: 'var(--text-secondary)', margin: 0 }}>Track and pay your annual course fees</p>
        </div>
        <div style={{ textAlign: 'right' }}>
          <p style={{ margin: 0, fontSize: '0.8rem', color: 'var(--text-secondary)', textTransform: 'uppercase', letterSpacing: '0.05em' }}>Student ID</p>
          <p style={{ margin: 0, fontWeight: 700, fontSize: '1.1rem' }}>#{studentId}</p>
        </div>
      </div>

      <div className="years-grid">
        {years.map(({ label, index }) => {
          const config = getStatusConfig(index);
          const record = feeRecords.find(r => r.yearOfStudy === index + 1);
          const isExpanded = expandedBreakdown === index;

          return (
            <div key={index} className="glass-panel" style={{ padding: '1.5rem', display: 'flex', flexDirection: 'column' }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '1rem' }}>
                <h3 style={{ margin: 0, fontSize: '1.1rem' }}>{label}</h3>
                <span style={{ display: 'flex', alignItems: 'center', gap: '0.3rem', color: config.color, background: config.bg, padding: '0.2rem 0.7rem', borderRadius: '999px', fontSize: '0.7rem', fontWeight: 700, letterSpacing: '0.05em' }}>
                  {config.icon} {config.label}
                </span>
              </div>

              <p style={{ color: 'var(--text-secondary)', fontSize: '0.85rem', margin: '0 0 1rem 0', lineHeight: '1.5' }}>{config.text}</p>

              {record && (
                <>
                  {/* Amount summary */}
                  <div style={{ background: 'rgba(0,0,0,0.2)', padding: '0.875rem 1rem', borderRadius: '8px', marginBottom: '0.75rem' }}>
                    <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '0.85rem', marginBottom: '0.4rem' }}>
                      <span style={{ color: 'var(--text-secondary)' }}>Total Fee</span>
                      <span style={{ fontWeight: 600 }}>₹ {Number(record.amountDue).toLocaleString('en-IN')}</span>
                    </div>
                    {record.scholarshipAmount > 0 && (
                      <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '0.85rem', marginBottom: '0.4rem' }}>
                        <span style={{ color: 'var(--text-secondary)' }}>Scholarship</span>
                        <span style={{ color: 'var(--warning-color)' }}>− ₹ {Number(record.scholarshipAmount).toLocaleString('en-IN')}</span>
                      </div>
                    )}
                    <div style={{ display: 'flex', justifyContent: 'space-between', paddingTop: '0.4rem', borderTop: '1px dashed rgba(255,255,255,0.1)' }}>
                      <span style={{ color: 'var(--text-secondary)', fontSize: '0.8rem' }}>Paid by You</span>
                      <span style={{ fontWeight: 700, color: 'var(--accent-primary)' }}>₹ {Number(record.amountPaid).toLocaleString('en-IN')}</span>
                    </div>
                  </div>

                  {/* Breakdown toggle */}
                  {record.feeStructure && (
                    <button onClick={() => setExpandedBreakdown(isExpanded ? null : index)}
                      style={{ background: 'none', border: 'none', color: 'var(--text-secondary)', cursor: 'pointer', fontSize: '0.8rem', textAlign: 'left', padding: '0 0 0.75rem 0', textDecoration: 'underline' }}>
                      {isExpanded ? 'Hide breakdown ▲' : 'View fee breakdown ▼'}
                    </button>
                  )}

                  {isExpanded && record.feeStructure && (
                    <div style={{ background: 'rgba(0,0,0,0.15)', borderRadius: '6px', padding: '0.75rem', marginBottom: '0.75rem', fontSize: '0.8rem' }}>
                      {[
                        { label: 'Tuition Fee', val: record.feeStructure.tuitionFee },
                        { label: 'Exam Fee', val: record.feeStructure.examFee },
                        { label: 'Development Fee', val: record.feeStructure.developmentFee },
                        { label: 'Other Fee', val: record.feeStructure.otherFee },
                      ].map(({ label, val }) => val > 0 && (
                        <div key={label} style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '0.3rem' }}>
                          <span style={{ color: 'var(--text-secondary)' }}>{label}</span>
                          <span>₹ {Number(val).toLocaleString('en-IN')}</span>
                        </div>
                      ))}
                    </div>
                  )}

                  {/* Actions */}
                  {record.status === 'PAID' && (
                    <button className="btn btn-secondary" style={{ width: '100%', marginTop: 'auto' }}
                      onClick={() => window.open(`http://localhost:8080/api/payment/receipts/${record.id}/download`, '_blank')}>
                      <Download size={16} /> Download Receipt
                    </button>
                  )}
                  {record.status === 'PENDING' && (
                    <button className="btn btn-primary" style={{ width: '100%', marginTop: 'auto' }} onClick={() => handlePayClick(record)}>
                      Pay Fee Now
                    </button>
                  )}
                </>
              )}
            </div>
          );
        })}
      </div>

      <PaymentModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        feeRecord={selectedRecord}
        onSuccess={handlePaymentSuccess}
      />
    </div>
  );
};

export default Dashboard;
