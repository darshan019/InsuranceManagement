import { useEffect, useState } from 'react';
import api from '../api';
import { useAuth } from '../context/AuthContext';

export default function PolicyTemplates() {
  const { user } = useAuth();
  const [templates, setTemplates] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState({
    templateName: '',
    premiumAmount: '',
    coverageAmount: '',
    agentId: '',
    insuranceTypeId: '',
    categoryId: ''
  });
  const [agents, setAgents] = useState([]);
  const [categories, setCategories] = useState([]);
  const [insuranceTypes, setInsuranceTypes] = useState([]);

  const role = user ? user.role : '';
  const isAdmin = role === 'ADMIN';

  const loadTemplates = async () => {
    setLoading(true);
    try {
      let res;
      if (role === 'AGENT' && user.userId) {
        res = await api.get('/api/policy_template/agent/' + user.userId);
      } else {
        res = await api.get('/api/policy_template/');
      }
      setTemplates(res.data || []);
    } catch (err) {
      setError('Failed to load templates');
    } finally {
      setLoading(false);
    }
  };

  const loadLookups = async () => {
    try {
      const [a, c, i] = await Promise.all([
        api.get('/api/agents/').catch(() => ({ data: [] })),
        api.get('/api/categories').catch(() => ({ data: [] })),
        api.get('/api/insurance_types').catch(() => ({ data: [] }))
      ]);
      setAgents(a.data);
      setCategories(c.data);
      setInsuranceTypes(i.data);
    } catch (err) {
      // non-critical
    }
  };

  useEffect(() => {
    if (user) {
      loadTemplates();
      if (isAdmin) loadLookups();
    }
    // eslint-disable-next-line
  }, [user]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    try {
      const payload = {
        templateName: form.templateName,
        premiumAmount: parseFloat(form.premiumAmount),
        coverageAmount: parseFloat(form.coverageAmount),
        agent: { agentId: parseInt(form.agentId) },
        insuranceType: { insuranceTypeId: parseInt(form.insuranceTypeId) },
        category: { categoryId: parseInt(form.categoryId) }
      };
      await api.post('/api/policy_template/', payload);
      setSuccess('Template created');
      setForm({
        templateName: '', premiumAmount: '', coverageAmount: '',
        agentId: '', insuranceTypeId: '', categoryId: ''
      });
      setShowForm(false);
      loadTemplates();
    } catch (err) {
      setError('Failed to create template. Check all fields.');
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this template?')) return;
    try {
      await api.delete('/api/policy_template/' + id);
      setSuccess('Template deleted');
      loadTemplates();
    } catch (err) {
      setError('Failed to delete template');
    }
  };

  return (
    <div className="container">
      <h1>Policy Templates</h1>

      {error && <div className="error-msg">{error}</div>}
      {success && <div className="success-msg">{success}</div>}

      {isAdmin && (
        <>
          <button className="btn btn-primary" onClick={() => setShowForm(!showForm)}>
            {showForm ? 'Cancel' : '+ Add Template'}
          </button>

          {showForm && (
            <form onSubmit={handleSubmit} className="mt-20">
              <div className="form-group">
                <label>Template Name</label>
                <input
                  type="text"
                  value={form.templateName}
                  onChange={(e) => setForm({ ...form, templateName: e.target.value })}
                  required
                />
              </div>
              <div className="form-group">
                <label>Premium Amount</label>
                <input
                  type="number"
                  step="0.01"
                  value={form.premiumAmount}
                  onChange={(e) => setForm({ ...form, premiumAmount: e.target.value })}
                  required
                />
              </div>
              <div className="form-group">
                <label>Coverage Amount</label>
                <input
                  type="number"
                  step="0.01"
                  value={form.coverageAmount}
                  onChange={(e) => setForm({ ...form, coverageAmount: e.target.value })}
                  required
                />
              </div>
              <div className="form-group">
                <label>Agent</label>
                <select
                  value={form.agentId}
                  onChange={(e) => setForm({ ...form, agentId: e.target.value })}
                  required
                >
                  <option value="">-- Select Agent --</option>
                  {agents.map((a) => (
                    <option key={a.agentId} value={a.agentId}>{a.name} ({a.email})</option>
                  ))}
                </select>
              </div>
              <div className="form-group">
                <label>Insurance Type</label>
                <select
                  value={form.insuranceTypeId}
                  onChange={(e) => setForm({ ...form, insuranceTypeId: e.target.value })}
                  required
                >
                  <option value="">-- Select Type --</option>
                  {insuranceTypes.map((t) => (
                    <option key={t.insuranceTypeId || t.id} value={t.insuranceTypeId || t.id}>
                      {t.name || t.typeName}
                    </option>
                  ))}
                </select>
              </div>
              <div className="form-group">
                <label>Category</label>
                <select
                  value={form.categoryId}
                  onChange={(e) => setForm({ ...form, categoryId: e.target.value })}
                  required
                >
                  <option value="">-- Select Category --</option>
                  {categories.map((c) => (
                    <option key={c.categoryId || c.id} value={c.categoryId || c.id}>
                      {c.name || c.categoryName}
                    </option>
                  ))}
                </select>
              </div>
              <button type="submit" className="submit-btn">Save</button>
            </form>
          )}
        </>
      )}

      {loading ? (
        <div className="loading">Loading...</div>
      ) : (
        <table className="mt-20">
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Agent</th>
              <th>Type</th>
              <th>Category</th>
              <th>Premium</th>
              <th>Coverage</th>
              {isAdmin && <th>Actions</th>}
            </tr>
          </thead>
          <tbody>
            {templates.length === 0 ? (
              <tr><td colSpan={isAdmin ? 8 : 7} className="text-center">No templates found</td></tr>
            ) : templates.map((t) => (
              <tr key={t.templateId}>
                <td>{t.templateId}</td>
                <td>{t.templateName}</td>
                <td>{t.agent && t.agent.name}</td>
                <td>{t.insuranceType && (t.insuranceType.name || t.insuranceType.typeName)}</td>
                <td>{t.category && (t.category.name || t.category.categoryName)}</td>
                <td>{t.premiumAmount}</td>
                <td>{t.coverageAmount}</td>
                {isAdmin && (
                  <td>
                    <button className="btn btn-danger" onClick={() => handleDelete(t.templateId)}>
                      Delete
                    </button>
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
