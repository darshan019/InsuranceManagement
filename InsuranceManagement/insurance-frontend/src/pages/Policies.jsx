import { useEffect, useState, useMemo } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api';
import { useAuth } from '../context/AuthContext';

export default function Policies() {
  const { user } = useAuth();
  const navigate = useNavigate();
  const [policies, setPolicies] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  // Search + filter (admin/agent only)
  const [searchTerm, setSearchTerm] = useState('');
  const [statusFilter, setStatusFilter] = useState('ALL');

  // Payment modal
  const [payingPolicy, setPayingPolicy] = useState(null);
  const [paymentMethod, setPaymentMethod] = useState('UPI');
  const [paying, setPaying] = useState(false);

  const role = user ? user.role : '';

  const loadPolicies = async () => {
    setLoading(true);
    try {
      let res;
      if (role === 'ADMIN') {
        res = await api.get('/api/policies');
      } else if (role === 'AGENT') {
        res = await api.get('/api/agent/' + user.userId + '/policies');
      } else if (role === 'CUSTOMER') {
        res = await api.get('/api/customer/' + user.userId + '/policies');
      }
      setPolicies(res.data || []);
    } catch (err) {
      setError('Failed to load policies');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (user) loadPolicies();
    // eslint-disable-next-line
  }, [user]);

  const handleCancel = async (policyNumber) => {
    if (!window.confirm('Cancel policy ' + policyNumber + '?')) return;
    try {
      await api.put('/api/customer/' + user.userId + '/policies/cancel/' + policyNumber);
      setSuccess('Policy cancelled');
      loadPolicies();
    } catch (err) {
      setError('Failed to cancel policy');
    }
  };

  const handleCheckPayment = async (id) => {
    try {
      await api.patch('/api/policy/update/' + id);
      setSuccess('Policy payment status refreshed');
      loadPolicies();
    } catch (err) {
      setError('Failed to refresh policy');
    }
  };

  const handleFileClaim = (policy) => {
    navigate('/customer/claims', {
      state: {
        policyId: policy.policyId,
        policyNumber: policy.policyNumber
      }
    });
  };

  const isPayable = (p) => {
    const s = (p.status || '').toLowerCase();
    if (s.includes('cancel')) return false;
    if (!p.nextPremiumDate) return true;
    return new Date(p.nextPremiumDate) <= new Date();
  };

  const isClaimable = (p) => {
    const s = (p.status || '').toLowerCase();
    return s.includes('active');
  };

  const formatDate = (d) => (d ? d.slice(0, 10) : '-');

  const openPaymentModal = (policy) => {
    setPayingPolicy(policy);
    setPaymentMethod('UPI');
    setError('');
    setSuccess('');
  };

  const closePaymentModal = () => setPayingPolicy(null);

  const submitPayment = async () => {
    if (!payingPolicy) return;
    setPaying(true);
    setError('');
    setSuccess('');
    try {
      const payload = {
        customer: { customerId: user.userId },
        policy: { policyId: payingPolicy.policyId },
        amount: payingPolicy.template.premiumAmount,
        paymentMethod: paymentMethod,
        status: 'SUCCESS'
      };
      await api.post('/api/payment', payload);
      setSuccess('Payment successful for policy ' + payingPolicy.policyNumber);
      setPayingPolicy(null);
      loadPolicies();
    } catch (err) {
      const msg = err.response && err.response.data
        ? (typeof err.response.data === 'string'
            ? err.response.data
            : (err.response.data.message || err.response.data.reason || 'Payment failed'))
        : 'Payment failed';
      setError(msg);
    } finally {
      setPaying(false);
    }
  };

  const getBadgeClass = (status) => {
    if (!status) return 'badge';
    const s = status.toLowerCase();
    if (s.includes('active')) return 'badge badge-active';
    if (s.includes('pending')) return 'badge badge-pending';
    if (s.includes('due')) return 'badge badge-rejected';
    if (s.includes('cancel')) return 'badge badge-cancelled';
    return 'badge badge-pending';
  };

  // Filter + search logic (admin/agent view)
  const visiblePolicies = useMemo(() => {
    if (role === 'CUSTOMER') return policies;

    const term = searchTerm.trim().toLowerCase();
    return policies.filter((p) => {
      // Status filter
      if (statusFilter !== 'ALL') {
        const s = (p.status || '').toUpperCase();
        if (!s.includes(statusFilter)) return false;
      }

      // Search filter
      if (!term) return true;
      const fields = [
        String(p.policyId || ''),
        (p.policyNumber || '').toLowerCase(),
        (p.customer && p.customer.username ? p.customer.username : '').toLowerCase(),
        (p.customer && p.customer.email ? p.customer.email : '').toLowerCase(),
        (p.template && p.template.templateName ? p.template.templateName : '').toLowerCase(),
        (p.template && p.template.agent && p.template.agent.name
          ? p.template.agent.name : '').toLowerCase()
      ];
      return fields.some((f) => f.includes(term));
    });
  }, [policies, searchTerm, statusFilter, role]);

  const showSearchBar = role === 'ADMIN' || role === 'AGENT';

  return (
    <div className="container">
      <h1>
        {role === 'CUSTOMER' ? 'My Policies' : role === 'AGENT' ? 'My Policies' : 'All Policies'}
      </h1>

      {role === 'CUSTOMER' && (
        <div className="info-msg">
          Premium is paid once per year. You can only file a claim while the
          policy is <b>ACTIVE</b>. Each policy allows only <b>one claim</b>.
        </div>
      )}

      {error && <div className="error-msg">{error}</div>}
      {success && <div className="success-msg">{success}</div>}

      {showSearchBar && (
        <div className="search-bar">
          <input
            type="text"
            className="search-input"
            placeholder="Search by Policy ID, number, customer, plan, or agent..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
          <select
            className="search-select"
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
          >
            <option value="ALL">All statuses</option>
            <option value="ACTIVE">Active</option>
            <option value="PENDING">Pending</option>
            <option value="DUE">Due</option>
            <option value="CANCEL">Cancelled</option>
          </select>
          <span className="search-count">
            {visiblePolicies.length} of {policies.length}
          </span>
        </div>
      )}

      {loading ? (
        <div className="loading">Loading...</div>
      ) : (
        <table>
          <thead>
            <tr>
              <th>Policy ID</th>
              <th>Policy #</th>
              {role !== 'CUSTOMER' && <th>Customer</th>}
              <th>Plan</th>
              <th>Premium</th>
              <th>Start</th>
              <th>End</th>
              <th>Next Premium Due</th>
              <th>Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {visiblePolicies.length === 0 ? (
              <tr>
                <td colSpan="10" className="text-center">
                  {policies.length === 0
                    ? 'No policies found'
                    : 'No policies match your search'}
                </td>
              </tr>
            ) : visiblePolicies.map((p) => {
              const payable = isPayable(p);
              const claimable = isClaimable(p);
              const statusLower = (p.status || '').toLowerCase();
              const isCancelled = statusLower.includes('cancel');
              return (
                <tr key={p.policyId}>
                  <td><b>{p.policyId}</b></td>
                  <td>{p.policyNumber}</td>
                  {role !== 'CUSTOMER' && <td>{p.customer && p.customer.username}</td>}
                  <td>{p.template && p.template.templateName}</td>
                  <td>{p.template && p.template.premiumAmount}</td>
                  <td>{formatDate(p.startDate)}</td>
                  <td>{formatDate(p.endDate)}</td>
                  <td>
                    {p.nextPremiumDate
                      ? formatDate(p.nextPremiumDate)
                      : <span className="subtle">Not yet paid</span>}
                  </td>
                  <td><span className={getBadgeClass(p.status)}>{p.status}</span></td>
                  <td>
                    {(role === 'ADMIN' || role === 'AGENT') && (
                      <button className="btn btn-primary" onClick={() => handleCheckPayment(p.policyId)}>
                        Refresh
                      </button>
                    )}
                    {role === 'CUSTOMER' && !isCancelled && (
                      <>
                        {payable ? (
                          <button className="btn btn-success" onClick={() => openPaymentModal(p)}>
                            Pay Premium
                          </button>
                        ) : (
                          <button
                            className="btn"
                            disabled
                            style={{ backgroundColor: '#f3f4f6', color: '#9aa5b1', cursor: 'not-allowed', borderColor: '#e4e7eb' }}
                            title={'Already paid. Next premium due ' + formatDate(p.nextPremiumDate)}
                          >
                            Paid &#10003;
                          </button>
                        )}
                        {claimable && (
                          <button className="btn btn-primary" onClick={() => handleFileClaim(p)}>
                            File Claim
                          </button>
                        )}
                        {statusLower.includes('active') && (
                          <button className="btn btn-warning" onClick={() => handleCancel(p.policyNumber)}>
                            Cancel
                          </button>
                        )}
                      </>
                    )}
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      )}

      {payingPolicy && (
        <div className="modal-overlay" onClick={closePaymentModal}>
          <div className="modal-box" onClick={(e) => e.stopPropagation()}>
            <h3>Pay Premium</h3>
            <p>
              Policy: <b>{payingPolicy.policyNumber}</b><br />
              Plan: <b>{payingPolicy.template && payingPolicy.template.templateName}</b><br />
              Amount: <b>&#8377; {payingPolicy.template && payingPolicy.template.premiumAmount}</b>
            </p>

            <div className="info-msg">This payment covers the next 1-year term.</div>

            <div className="form-group">
              <label>Payment Method</label>
              <select value={paymentMethod} onChange={(e) => setPaymentMethod(e.target.value)}>
                <option value="UPI">UPI</option>
                <option value="CREDIT_CARD">Credit Card</option>
                <option value="DEBIT_CARD">Debit Card</option>
                <option value="NET_BANKING">Net Banking</option>
                <option value="CASH">Cash</option>
              </select>
            </div>

            <div className="flex-row">
              <button className="btn btn-success" onClick={submitPayment} disabled={paying}>
                {paying ? 'Processing...' : 'Confirm Payment'}
              </button>
              <button className="btn btn-danger" onClick={closePaymentModal} disabled={paying}>
                Cancel
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
