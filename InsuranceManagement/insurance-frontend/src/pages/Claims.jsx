import { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import api from '../api';
import { useAuth } from '../context/AuthContext';

export default function Claims() {
  const { user } = useAuth();
  const location = useLocation();
  const [claims, setClaims] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState({ policyId: '', description: '', claimAmount: '' });

  // Reject modal (admin)
  const [rejectingClaim, setRejectingClaim] = useState(null);
  const [rejectReason, setRejectReason] = useState('');
  const [rejecting, setRejecting] = useState(false);

  const role = user ? user.role : '';

  useEffect(() => {
    if (location.state && location.state.policyId) {
      setForm((f) => ({ ...f, policyId: String(location.state.policyId) }));
      setShowForm(true);
    }
  }, [location.state]);

  const loadClaims = async () => {
    setLoading(true);
    try {
      if (role === 'CUSTOMER') {
        const storedIds = JSON.parse(
          localStorage.getItem('myClaimIds_' + user.userId) || '[]'

        );
        const results = [];
        for (const cid of storedIds) {
          try {
            const r = await api.get('/api/claims/' + cid);
            results.push(r.data);
          } catch (e) { /* may be deleted */ }
        }
        setClaims(results);
      } else {
        const res = await api.get('/api/claims');
        setClaims(res.data || []);
      }
    } catch (err) {
      setError('Failed to load claims');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (user) loadClaims();
    // eslint-disable-next-line
  }, [user]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    try {
      const payload = {
        policy: { policyId: parseInt(form.policyId) },
        description: form.description,
        claimAmount: parseFloat(form.claimAmount),
        status: 'PENDING'
      };
      const res = await api.post('/api/claims', payload);

      if (role === 'CUSTOMER' && res.data && res.data.claimId) {
        const key = 'myClaimIds_' + user.userId;
        const existing = JSON.parse(localStorage.getItem(key) || '[]');
        existing.push(res.data.claimId);
        localStorage.setItem(key, JSON.stringify(existing));
      }

      setSuccess('Claim submitted successfully');
      setForm({ policyId: '', description: '', claimAmount: '' });
      setShowForm(false);
      loadClaims();
    } catch (err) {
      const msg = err.response && err.response.data
        ? (typeof err.response.data === 'string'
            ? err.response.data
            : (err.response.data.message || err.response.data.reason || 'Failed to submit claim'))
        : 'Failed to submit claim. Check the policy ID.';
      setError(msg);
    }
  };

  const handleApprove = async (claimId) => {
    if (!user.userId) {
      setError('Your admin ID is unknown. Please re-login.');
      return;
    }
    try {
      await api.patch('/api/admins/' + user.userId + '/' + claimId + '/approve');
      setSuccess('Claim approved');
      loadClaims();
    } catch (err) {
      setError('Failed to approve claim');
    }
  };

  // Open reject modal
  const openRejectModal = (claim) => {
    setRejectingClaim(claim);
    setRejectReason('');
    setError('');
    setSuccess('');
  };

  const closeRejectModal = () => {
    setRejectingClaim(null);
    setRejectReason('');
  };

  const submitReject = async () => {
    if (!rejectingClaim) return;
    if (!rejectReason.trim()) {
      setError('Please provide a reason for rejection.');
      return;
    }
    if (!user.userId) {
      setError('Your admin ID is unknown. Please re-login.');
      return;
    }
    setRejecting(true);
    setError('');
    setSuccess('');
    try {
      await api.patch(
        '/api/admins/' + user.userId + '/' + rejectingClaim.claimId + '/reject',
        { remark: rejectReason.trim() }
      );
      setSuccess('Claim rejected');
      closeRejectModal();
      loadClaims();
    } catch (err) {
      const msg = err.response && err.response.data
        ? (typeof err.response.data === 'string'
            ? err.response.data
            : (err.response.data.message || 'Failed to reject claim'))
        : 'Failed to reject claim';
      setError(msg);
    } finally {
      setRejecting(false);
    }
  };

  const getBadgeClass = (status) => {
    if (!status) return 'badge';
    const s = status.toLowerCase();
    if (s.includes('approved')) return 'badge badge-approved';
    if (s.includes('pending')) return 'badge badge-pending';
    if (s.includes('reject')) return 'badge badge-rejected';
    return 'badge badge-pending';
  };

  return (
    <div className="container">
      <h1>{role === 'CUSTOMER' ? 'My Claims' : 'Claims'}</h1>

      {error && <div className="error-msg">{error}</div>}
      {success && <div className="success-msg">{success}</div>}

      {(role === 'CUSTOMER' || role === 'AGENT') && (
        <>
          <button
            className="btn btn-primary"
            onClick={() => {
              setShowForm(!showForm);
              if (!showForm) setError('');
            }}
          >
            {showForm ? 'Cancel' : '+ File New Claim'}
          </button>

          {showForm && (
            <form onSubmit={handleSubmit} className="mt-20">
              <div className="form-group">
                <label>
                  Policy ID
                  {role === 'CUSTOMER' && (
                    <span className="subtle" style={{ fontSize: '12px', marginLeft: '8px' }}>
                      (see "Policy ID" column in My Policies. Policy must be ACTIVE.)
                    </span>
                  )}
                </label>
                <input
                  type="number"
                  value={form.policyId}
                  onChange={(e) => setForm({ ...form, policyId: e.target.value })}
                  required
                />
              </div>
              <div className="form-group">
                <label>Description</label>
                <textarea
                  value={form.description}
                  onChange={(e) => setForm({ ...form, description: e.target.value })}
                  rows="3"
                  required
                />
              </div>
              <div className="form-group">
                <label>Claim Amount</label>
                <input
                  type="number"
                  step="0.01"
                  value={form.claimAmount}
                  onChange={(e) => setForm({ ...form, claimAmount: e.target.value })}
                  required
                />
              </div>
              <button type="submit" className="submit-btn">Submit Claim</button>
            </form>
          )}
        </>
      )}

      <h3>{role === 'CUSTOMER' ? 'My Claim History' : 'All Claims'}</h3>

      {loading ? (
        <div className="loading">Loading...</div>
      ) : (
        <table className="mt-10">
          <thead>
            <tr>
              <th>Claim ID</th>
              <th>Policy ID</th>
              <th>Policy #</th>
              <th>Date Filed</th>
              <th>Description</th>
              <th>Amount</th>
              <th>Status</th>
              {role === 'CUSTOMER' && <th>Remark / Reason</th>}
              {role === 'ADMIN' && <th>Remark</th>}
              {role === 'CUSTOMER' && <th>Decision By</th>}
              {role === 'ADMIN' && <th>Actions</th>}
            </tr>
          </thead>
          <tbody>
            {claims.length === 0 ? (
              <tr>
                <td colSpan="10" className="text-center">
                  {role === 'CUSTOMER'
                    ? 'You have not filed any claims yet.'
                    : 'No claims found'}
                </td>
              </tr>
            ) : claims.map((c) => {
              const statusLower = (c.status || '').toLowerCase();
              const isPending = statusLower.includes('pending');
              const isRejected = statusLower.includes('reject');
              return (
                <tr key={c.claimId}>
                  <td>{c.claimId}</td>
                  <td><b>{c.policy && c.policy.policyId}</b></td>
                  <td>{c.policy && c.policy.policyNumber}</td>
                  <td>{c.claimDate ? c.claimDate.slice(0, 10) : '-'}</td>
                  <td>{c.description}</td>
                  <td>{c.claimAmount}</td>
                  <td><span className={getBadgeClass(c.status)}>{c.status}</span></td>

                  {/* Remark column for both roles */}
                  <td>
                    {c.remark
                      ? (isRejected
                          ? <span className="remark-rejected">{c.remark}</span>
                          : <span className="subtle">{c.remark}</span>)
                      : <span className="subtle">&mdash;</span>}
                  </td>

                  {role === 'CUSTOMER' && (
                    <td>
                      {c.approvedBy
                        ? (c.approvedBy.username || c.approvedBy.email || 'Admin')
                        : '-'}
                    </td>
                  )}
                  {role === 'ADMIN' && (
                    <td>
                      {isPending ? (
                        <>
                          <button className="btn btn-success" onClick={() => handleApprove(c.claimId)}>
                            Approve
                          </button>
                          <button className="btn btn-danger" onClick={() => openRejectModal(c)}>
                            Reject
                          </button>
                        </>
                      ) : (
                        <span className="subtle" style={{ fontSize: '13px' }}>Decision made</span>
                      )}
                    </td>
                  )}
                </tr>
              );
            })}
          </tbody>
        </table>
      )}

      {/* Reject reason modal */}
      {rejectingClaim && (
        <div className="modal-overlay" onClick={closeRejectModal}>
          <div className="modal-box" onClick={(e) => e.stopPropagation()}>
            <h3>Reject Claim #{rejectingClaim.claimId}</h3>
            <p className="subtle">
              Please provide a reason for rejecting this claim. The customer
              will see your reason on their claim history.
            </p>

            <div className="form-group">
              <label>Reason for rejection</label>
              <textarea
                rows="4"
                value={rejectReason}
                onChange={(e) => setRejectReason(e.target.value)}
                placeholder="e.g. Insufficient documentation, claim exceeds coverage limit, etc."
                autoFocus
              />
            </div>

            <div className="flex-row">
              <button
                className="btn btn-danger"
                onClick={submitReject}
                disabled={rejecting || !rejectReason.trim()}
              >
                {rejecting ? 'Rejecting...' : 'Confirm Reject'}
              </button>
              <button
                className="btn"
                onClick={closeRejectModal}
                disabled={rejecting}
              >
                Cancel
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
