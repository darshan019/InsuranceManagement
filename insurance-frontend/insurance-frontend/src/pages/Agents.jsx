import { useEffect, useState } from 'react';
import api from '../api';

export default function Agents() {
  const [agents, setAgents] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [form, setForm] = useState({ name: '', email: '', password: '', phone: '' });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const loadAgents = async () => {
    setLoading(true);
    try {
      const res = await api.get('/api/agents/');
      setAgents(res.data);
    } catch (err) {
      setError('Failed to load agents');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadAgents();
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    try {
      await api.post('/api/agents/', form);
      setSuccess('Agent added successfully');
      setForm({ name: '', email: '', password: '', phone: '' });
      setShowForm(false);
      loadAgents();
    } catch (err) {
      setError('Failed to add agent');
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this agent?')) return;
    try {
      await api.delete('/api/agents/' + id);
      setSuccess('Agent deleted');
      loadAgents();
    } catch (err) {
      setError('Failed to delete agent');
    }
  };

  return (
    <div className="container">
      <h1>Agents</h1>

      {error && <div className="error-msg">{error}</div>}
      {success && <div className="success-msg">{success}</div>}

      <button className="btn btn-primary" onClick={() => setShowForm(!showForm)}>
        {showForm ? 'Cancel' : '+ Add Agent'}
      </button>

      {showForm && (
        <form onSubmit={handleSubmit} className="mt-20">
          <div className="form-group">
            <label>Name</label>
            <input
              type="text"
              value={form.name}
              onChange={(e) => setForm({ ...form, name: e.target.value })}
              required
            />
          </div>
          <div className="form-group">
            <label>Email</label>
            <input
              type="email"
              value={form.email}
              onChange={(e) => setForm({ ...form, email: e.target.value })}
              required
            />
          </div>
          <div className="form-group">
            <label>Password</label>
            <input
              type="password"
              value={form.password}
              onChange={(e) => setForm({ ...form, password: e.target.value })}
              required
            />
          </div>
          <div className="form-group">
            <label>Phone</label>
            <input
              type="text"
              value={form.phone}
              onChange={(e) => setForm({ ...form, phone: e.target.value })}
            />
          </div>
          <button type="submit" className="submit-btn">Save</button>
        </form>
      )}

      {loading ? (
        <div className="loading">Loading...</div>
      ) : (
        <table className="mt-20">
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Email</th>
              <th>Phone</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {agents.length === 0 ? (
              <tr><td colSpan="5" className="text-center">No agents found</td></tr>
            ) : agents.map((a) => (
              <tr key={a.agentId}>
                <td>{a.agentId}</td>
                <td>{a.name}</td>
                <td>{a.email}</td>
                <td>{a.phone}</td>
                <td>
                  <button className="btn btn-danger" onClick={() => handleDelete(a.agentId)}>
                    Delete
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
