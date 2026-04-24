import { useEffect, useState, useMemo } from 'react';
import api from '../api';

// SVG icons — same style as home page
const IconAll = () => (
  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.6"
       strokeLinecap="round" strokeLinejoin="round" width="24" height="24">
    <rect x="3" y="3" width="7" height="7" rx="1" />
    <rect x="14" y="3" width="7" height="7" rx="1" />
    <rect x="3" y="14" width="7" height="7" rx="1" />
    <rect x="14" y="14" width="7" height="7" rx="1" />
  </svg>
);

const IconMotor = () => (
  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.6"
       strokeLinecap="round" strokeLinejoin="round" width="24" height="24">
    <path d="M5 17h-1a1 1 0 0 1-1-1v-3l2-5a2 2 0 0 1 2-1h10a2 2 0 0 1 2 1l2 5v3a1 1 0 0 1-1 1h-1" />
    <circle cx="7" cy="17" r="2" />
    <circle cx="17" cy="17" r="2" />
    <path d="M9 17h6" />
    <path d="M5 13h14" />
  </svg>
);

const IconLife = () => (
  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.6"
       strokeLinecap="round" strokeLinejoin="round" width="24" height="24">
    <path d="M12 21s-7-4.5-7-10a4 4 0 0 1 7-2.65A4 4 0 0 1 19 11c0 5.5-7 10-7 10z" />
  </svg>
);

const IconProperty = () => (
  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.6"
       strokeLinecap="round" strokeLinejoin="round" width="24" height="24">
    <path d="M3 10l9-7 9 7" />
    <path d="M5 9v11a1 1 0 0 0 1 1h4v-6h4v6h4a1 1 0 0 0 1-1V9" />
  </svg>
);

const IconPhone = () => (
  <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.6"
       strokeLinecap="round" strokeLinejoin="round" width="14" height="14">
    <path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72c.127.96.36 1.903.7 2.81a2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z" />
  </svg>
);

const CATEGORIES = [
  { key: 'ALL', label: 'All Plans', Icon: IconAll },
  { key: 'MOTOR', label: 'Motor', Icon: IconMotor },
  { key: 'LIFE', label: 'Life', Icon: IconLife },
  { key: 'PROPERTY', label: 'Property', Icon: IconProperty }
];

export default function BrowseTemplates() {
  const [templates, setTemplates] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [activeCategory, setActiveCategory] = useState('ALL');

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
      setSuccess('Policy purchased successfully! Go to "My Policies" to pay the first premium.');
    } catch (err) {
      const msg = err.response && err.response.data
        ? (typeof err.response.data === 'string'
            ? err.response.data
            : (err.response.data.message || err.response.data.reason || 'Failed to buy policy'))
        : 'Failed to buy policy';
      setError(msg);
    }
  };

  const filtered = useMemo(() => {
    if (activeCategory === 'ALL') return templates;
    return templates.filter((t) => {
      const typeName = t.insuranceType
        ? (t.insuranceType.typeName || t.insuranceType.name || '')
        : '';
      return typeName.toUpperCase().includes(activeCategory);
    });
  }, [templates, activeCategory]);

  const countFor = (key) => {
    if (key === 'ALL') return templates.length;
    return templates.filter((t) => {
      const typeName = t.insuranceType
        ? (t.insuranceType.typeName || t.insuranceType.name || '')
        : '';
      return typeName.toUpperCase().includes(key);
    }).length;
  };

  return (
    <div className="container">
      <h1>Browse Insurance Plans</h1>
      <p className="subtle">
        Choose a category below, then pick a plan that fits your needs.
      </p>

      {error && <div className="error-msg">{error}</div>}
      {success && <div className="success-msg">{success}</div>}

      <div className="category-tiles">
        {CATEGORIES.map((c) => {
          const Icon = c.Icon;
          return (
            <button
              key={c.key}
              type="button"
              className={'category-tile' + (activeCategory === c.key ? ' active' : '')}
              onClick={() => setActiveCategory(c.key)}
            >
              <div className="category-icon-wrap"><Icon /></div>
              <div className="category-label">{c.label}</div>
              <div className="category-count">{countFor(c.key)} plans</div>
            </button>
          );
        })}
      </div>

      {loading ? (
        <div className="loading">Loading plans...</div>
      ) : filtered.length === 0 ? (
        <div className="empty-state">
          No {activeCategory === 'ALL' ? '' : activeCategory.toLowerCase() + ' '}
          plans available right now.
        </div>
      ) : (
        <div className="plan-grid">
          {filtered.map((t) => {
            const typeName = t.insuranceType
              ? (t.insuranceType.typeName || t.insuranceType.name || 'Plan')
              : 'Plan';
            return (
              <div key={t.templateId} className="plan-card">
                <div className="plan-type">{typeName}</div>
                <h3 className="plan-name">{t.templateName}</h3>

                <div className="plan-details">
                  <div className="plan-row">
                    <span className="plan-label">Category</span>
                    <span className="plan-value">
                      {t.category && (t.category.categoryName || t.category.name)}
                    </span>
                  </div>
                  <div className="plan-row">
                    <span className="plan-label">Premium</span>
                    <span className="plan-value plan-price">&#8377; {t.premiumAmount}</span>
                  </div>
                  <div className="plan-row">
                    <span className="plan-label">Coverage</span>
                    <span className="plan-value">&#8377; {t.coverageAmount}</span>
                  </div>
                </div>

                <div className="agent-block">
                  <div className="agent-label">Your Agent</div>
                  <div className="agent-name">
                    {t.agent && t.agent.name ? t.agent.name : 'Not assigned'}
                  </div>
                  {t.agent && t.agent.phone && (
                    <div className="agent-phone">
                      <IconPhone />
                      <span>{t.agent.phone}</span>
                    </div>
                  )}
                </div>

                <button
                  className="btn btn-primary plan-buy-btn"
                  onClick={() => handleBuy(t.templateId)}
                >
                  Buy This Plan
                </button>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
}
