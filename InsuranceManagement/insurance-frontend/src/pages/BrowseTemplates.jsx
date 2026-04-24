import { useEffect, useState, useMemo } from 'react';
import api from '../api';
import {
  FaThLarge,
  FaCar,
  FaHeart,
  FaHome,
  FaPhone
} from 'react-icons/fa';

// Categories configuration
const CATEGORIES = [
  { key: 'ALL', label: 'All Plans', Icon: FaThLarge },
  { key: 'MOTOR', label: 'Motor', Icon: FaCar },
  { key: 'LIFE', label: 'Life', Icon: FaHeart },
  { key: 'PROPERTY', label: 'Property', Icon: FaHome }
];

export default function BrowseTemplates() {
  const [templates, setTemplates] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [activeCategory, setActiveCategory] = useState('ALL');

  // Load policy templates from backend
  useEffect(() => {
    const loadTemplates = async () => {
      try {
        const res = await api.get('/api/policy_template/');
        setTemplates(res.data);
      } catch (err) {
        setError('Failed to load insurance plans');
      } finally {
        setLoading(false);
      }
    };
    loadTemplates();
  }, []);

  // Buy policy (customer)
  const handleBuy = async (templateId) => {
    if (!window.confirm('Are you sure you want to buy this policy?')) return;

    setError('');
    setSuccess('');

    try {
      await api.post('/api/customers/buyPolicy/' + templateId);
      setSuccess(
        'Policy purchased successfully! Go to "My Policies" to complete payment.'
      );
    } catch (err) {
      const msg =
        err.response?.data?.message ||
        err.response?.data ||
        'Failed to buy policy';
      setError(msg);
    }
  };

  // Filter templates by category
  const filteredTemplates = useMemo(() => {
    if (activeCategory === 'ALL') return templates;

    return templates.filter((t) => {
      const typeName =
        t.insuranceType?.typeName ||
        t.insuranceType?.name ||
        '';
      return typeName.toUpperCase().includes(activeCategory);
    });
  }, [templates, activeCategory]);

  // Count templates per category
  const countFor = (key) => {
    if (key === 'ALL') return templates.length;

    return templates.filter((t) => {
      const typeName =
        t.insuranceType?.typeName ||
        t.insuranceType?.name ||
        '';
      return typeName.toUpperCase().includes(key);
    }).length;
  };

  return (
    <div className="container">
      <h1>Browse Insurance Plans</h1>
      <p className="subtle">
        Choose a category and select an insurance plan that fits your needs.
      </p>

      {error && <div className="error-msg">{error}</div>}
      {success && <div className="success-msg">{success}</div>}

      {/* Category Filters */}
      <div className="category-tiles">
        {CATEGORIES.map(({ key, label, Icon }) => (
          <button
            key={key}
            type="button"
            className={
              'category-tile' + (activeCategory === key ? ' active' : '')
            }
            onClick={() => setActiveCategory(key)}
          >
            <div className="category-icon-wrap">
              <Icon size={24} />
            </div>
            <div className="category-label">{label}</div>
            <div className="category-count">{countFor(key)} plans</div>
          </button>
        ))}
      </div>

      {/* Plans Grid */}
      {loading ? (
        <div className="loading">Loading plans...</div>
      ) : filteredTemplates.length === 0 ? (
        <div className="empty-state">
          No {activeCategory === 'ALL' ? '' : activeCategory.toLowerCase() + ' '}
          plans available.
        </div>
      ) : (
        <div className="plan-grid">
          {filteredTemplates.map((t) => {
            const typeName =
              t.insuranceType?.typeName ||
              t.insuranceType?.name ||
              'Plan';

            return (
              <div key={t.templateId} className="plan-card">
                <div className="plan-type">{typeName}</div>
                <h3 className="plan-name">{t.templateName}</h3>

                <div className="plan-details">
                  <div className="plan-row">
                    <span className="plan-label">Category</span>
                    <span className="plan-value">
                      {t.category?.categoryName || t.category?.name}
                    </span>
                  </div>
                  <div className="plan-row">
                    <span className="plan-label">Premium</span>
                    <span className="plan-value plan-price">
                      ₹ {t.premiumAmount}
                    </span>
                  </div>
                  <div className="plan-row">
                    <span className="plan-label">Coverage</span>
                    <span className="plan-value">
                      ₹ {t.coverageAmount}
                    </span>
                  </div>
                </div>

                {/* Agent Info */}
                <div className="agent-block">
                  <div className="agent-label">Your Agent</div>
                  <div className="agent-name">
                    {t.agent?.name || 'Not assigned'}
                  </div>
                  {t.agent?.phone && (
                    <div className="agent-phone">
                      <FaPhone size={14} />
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