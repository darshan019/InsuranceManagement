import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import api from '../api';
import { useAuth } from '../context/AuthContext';

export default function CustomerDashboard() {
  const { user } = useAuth();
  const [stats, setStats] = useState({ policies: 0, payments: 0 });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadStats = async () => {
      if (!user || !user.userId) {
        setLoading(false);
        return;
      }
      try {
        const [policies, payments] = await Promise.all([
          api.get('/api/customer/' + user.userId + '/policies').catch(() => ({ data: [] })),
          api.get('/api/payments/customer/' + user.userId).catch(() => ({ data: [] }))
        ]);
        setStats({
          policies: policies.data.length,
          payments: payments.data.length
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
      <h1>Customer Dashboard</h1>
      <p>Welcome, <b>{user && user.email}</b>!</p>

      {loading ? (
        <div className="loading">Loading...</div>
      ) : (
        <div className="dashboard-grid">
          <div className="dashboard-card">
            <h3>My Policies</h3>
            <div className="count">{stats.policies}</div>
            <Link to="/customer/policies">View</Link>
          </div>

          <div className="dashboard-card">
            <h3>Browse Plans</h3>
            <div className="count">+</div>
            <Link to="/customer/browse">Shop</Link>
          </div>

          <div className="dashboard-card">
            <h3>File a Claim</h3>
            <div className="count">!</div>
            <Link to="/customer/claims">New Claim</Link>
          </div>
        </div>
      )}
    </div>
  );
}
