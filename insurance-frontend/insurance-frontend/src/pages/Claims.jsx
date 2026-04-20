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

  const role = user ? user.role : '';

  // If customer came here from "File Claim" button on Policies page,
  // auto-open the form and pre-fill the policy ID.
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
        // There's no "my claims" endpoint. Instead we get the customer's
        // policies, then fetch each claim by looking at the full list via
        // /api/claims/{claimId}. Easier: get all policies, then filter
        // claims from /api/claims — but that's admin-only. So we use
        // per-claim fetch. A cleaner approach: get my policies first,
        // then we only show claims linked to those policies. Since we
        // can't list all claims, we store claim IDs locally after submit.
        //
        // Simpler solution: call /api/customer/{id}/policies to get their
        // policies, then for each policy look at the backend — but there's
        // no "claims by policy" endpoint either.
        //
        // Best we can do: store submitted claim IDs in localStorage and
        // fetch each one with /api/claims/{id} (customer has access).
        const storedIds = JSON.parse(
          localStorage.getItem('myClaimIds_' + user.userId) || '[]'
        );
        const results = [];
        for (const cid of storedIds) {
          try {
            const r = await api.get('/api/claims/' + cid);
            results.push(r.data);
          } catch (e) { /* claim may have been deleted */ }
        }
        setClaims(results);
      } else {
        // ADMIN / AGENT — list all
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

      // If customer, remember this claim ID locally so we can show it
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
        ? (typeof err.response.data === 'string' ? err.response.data : err.response.data.message)
        : 'Failed to submit claim';
      setError(msg || 'Failed to submit claim. Check the policy ID.');
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

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this claim?')) return;
    try {
      await api.delete('/api/claims/' + id);
      setSuccess('Claim deleted');
      loadClaims();
    } catch (err) {
      setError('Failed to delete claim');
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
      <h1>
        {role === 'CUSTOMER' ? 'My Claims' : 'Claims'}
      </h1>

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
                    <span style={{ fontSize: '12px', color: '#666', marginLeft: '8px' }}>
                      (see "Policy ID" column in "My Policies")
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
              <th>Policy #</th>
              <th>Date Filed</th>
              <th>Description</th>
              <th>Amount</th>
              <th>Status</th>
              {role === 'CUSTOMER' && <th>Approved By</th>}
              {role !== 'CUSTOMER' && <th>Actions</th>}
            </tr>
          </thead>
          <tbody>
            {claims.length === 0 ? (
              <tr>
                <td colSpan="8" className="text-center">
                  {role === 'CUSTOMER'
                    ? 'You have not filed any claims yet.'
                    : 'No claims found'}
                </td>
              </tr>
            ) : claims.map((c) => (
              <tr key={c.claimId}>
                <td>{c.claimId}</td>
                <td>{c.policy && c.policy.policyNumber}</td>
                <td>{c.claimDate ? c.claimDate.slice(0, 10) : '-'}</td>
                <td>{c.description}</td>
                <td>{c.claimAmount}</td>
                <td><span className={getBadgeClass(c.status)}>{c.status}</span></td>
                {role === 'CUSTOMER' && (
                  <td>
                    {c.approvedBy
                      ? (c.approvedBy.username || c.approvedBy.email || 'Admin')
                      : '-'}
                  </td>
                )}
                {role !== 'CUSTOMER' && (
                  <td>
                    {role === 'ADMIN' && c.status && c.status.toLowerCase().includes('pending') && (
                      <button className="btn btn-success" onClick={() => handleApprove(c.claimId)}>
                        Approve
                      </button>
                    )}
                    {role === 'ADMIN' && (
                      <button className="btn btn-danger" onClick={() => handleDelete(c.claimId)}>
                        Delete
                      </button>
                    )}
                  </td>
                )}
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
