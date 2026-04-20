import { useEffect, useState } from 'react';
import api from '../api';
import { useAuth } from '../context/AuthContext';

export default function BrowseTemplates() {
  const { user } = useAuth();
  const [templates, setTemplates] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  useEffect(() => {
    const load = async () => {
      try {
        const res = await api.get('/api/policy_template/');
        setTemplates(res.data);
      } catch (err) {
        setError('Failed to load plans');
      } finally {
        setLoading(false);
      }
    };
    load();
  }, []);

  const handleBuy = async (templateId) => {
    if (!window.confirm('Are you sure you want to buy this policy?')) return;
    setError('');
    setSuccess('');
    try {
      await api.post('/api/customers/buyPolicy/' + templateId);
      setSuccess('Policy purchased successfully! Check "My Policies".');
    } catch (err) {
      if (err.response && err.response.data) {
        const msg = typeof err.response.data === 'string'
          ? err.response.data
          : (err.response.data.message || 'Failed to buy policy');
        setError(msg);
      } else {
        setError('Failed to buy policy');
      }
    }
  };

  return (
    <div className="container">
      <h1>Browse Insurance Plans</h1>
      <p>Select a plan below to purchase a policy.</p>

      {error && <div className="error-msg">{error}</div>}
      {success && <div className="success-msg">{success}</div>}

      {loading ? (
        <div className="loading">Loading plans...</div>
      ) : (
        <table>
          <thead>
            <tr>
              <th>Plan Name</th>
              <th>Type</th>
              <th>Category</th>
              <th>Premium</th>
              <th>Coverage</th>
              <th>Agent</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {templates.length === 0 ? (
              <tr><td colSpan="7" className="text-center">No plans available</td></tr>
            ) : templates.map((t) => (
              <tr key={t.templateId}>
                <td><b>{t.templateName}</b></td>
                <td>{t.insuranceType && (t.insuranceType.name || t.insuranceType.typeName)}</td>
                <td>{t.category && (t.category.name || t.category.categoryName)}</td>
                <td>₹ {t.premiumAmount}</td>
                <td>₹ {t.coverageAmount}</td>
                <td>{t.agent && t.agent.name}</td>
                <td>
                  <button className="btn btn-success" onClick={() => handleBuy(t.templateId)}>
                    Buy
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
