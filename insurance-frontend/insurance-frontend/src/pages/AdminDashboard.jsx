import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import api from '../api';

export default function AdminDashboard() {
  const [stats, setStats] = useState({
    agents: 0,
    customers: 0,
    policies: 0,
    claims: 0,
    pendingClaims: 0,
    payments: 0
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadStats = async () => {
      try {
        const [agents, customers, policies, claims, pending, payments] = await Promise.all([
          api.get('/api/agents/').catch(() => ({ data: [] })),
          api.get('/api/customers').catch(() => ({ data: [] })),
          api.get('/api/policies').catch(() => ({ data: [] })),
          api.get('/api/claims').catch(() => ({ data: [] })),
          api.get('/api/claims/pending').catch(() => ({ data: [] })),
          api.get('/api/payments').catch(() => ({ data: [] }))
        ]);

        setStats({
          agents: agents.data.length,
          customers: customers.data.length,
          policies: policies.data.length,
          claims: claims.data.length,
          pendingClaims: pending.data.length,
          payments: payments.data.length
        });
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    loadStats();
  }, []);

  return (
    <div className="container">
      <h1>Admin Dashboard</h1>
      <p>Welcome back. Here is an overview of the system.</p>

      {loading ? (
        <div className="loading">Loading stats...</div>
      ) : (
        <div className="dashboard-grid">
          <div className="dashboard-card">
            <h3>Agents</h3>
            <div className="count">{stats.agents}</div>
            <Link to="/admin/agents">Manage</Link>
          </div>

          <div className="dashboard-card">
            <h3>Customers</h3>
            <div className="count">{stats.customers}</div>
            <Link to="/admin/customers">View</Link>
          </div>

          <div className="dashboard-card">
            <h3>Policies</h3>
            <div className="count">{stats.policies}</div>
            <Link to="/admin/policies">View</Link>
          </div>

          <div className="dashboard-card">
            <h3>Total Claims</h3>
            <div className="count">{stats.claims}</div>
            <Link to="/admin/claims">View</Link>
          </div>

          <div className="dashboard-card">
            <h3>Pending Claims</h3>
            <div className="count">{stats.pendingClaims}</div>
            <Link to="/admin/claims">Approve</Link>
          </div>

          <div className="dashboard-card">
            <h3>Payments</h3>
            <div className="count">{stats.payments}</div>
            <Link to="/admin/payments">View</Link>
          </div>
        </div>
      )}
    </div>
  );
}
