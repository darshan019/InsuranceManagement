import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import api from '../api';
import { useAuth } from '../context/AuthContext';

export default function AgentDashboard() {
  const { user } = useAuth();
  const [stats, setStats] = useState({ customers: 0, policies: 0, pendingClaims: 0 });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadStats = async () => {
      if (!user || !user.userId) {
        setLoading(false);
        return;
      }
      try {
        const [customers, policies, pending] = await Promise.all([
          api.get('/api/agents/' + user.userId + '/customers').catch(() => ({ data: [] })),
          api.get('/api/agent/' + user.userId + '/policies').catch(() => ({ data: [] })),
          api.get('/api/claims/pending').catch(() => ({ data: [] }))
        ]);
        setStats({
          customers: customers.data.length,
          policies: policies.data.length,
          pendingClaims: pending.data.length
        });
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    loadStats();
  }, [user]);

  return (
    <div className="container">
      <h1>Agent Dashboard</h1>
      <p>Welcome, <b>{user && user.email}</b>. Here is your activity summary.</p>

      {loading ? (
        <div className="loading">Loading...</div>
      ) : (
        <div className="dashboard-grid">
          <div className="dashboard-card">
            <h3>My Customers</h3>
            <div className="count">{stats.customers}</div>
            <Link to="/agent/customers">View</Link>
          </div>

          <div className="dashboard-card">
            <h3>My Policies</h3>
            <div className="count">{stats.policies}</div>
            <Link to="/agent/policies">View</Link>
          </div>

          <div className="dashboard-card">
            <h3>Pending Claims</h3>
            <div className="count">{stats.pendingClaims}</div>
            <Link to="/agent/claims">View</Link>
          </div>
        </div>
      )}

      {(!user || !user.userId) && (
        <div className="info-msg mt-20">
          Note: Could not load your agent ID. Some features may be limited.
        </div>
      )}
    </div>
  );
}
