import { useEffect, useState } from 'react';
import api from '../api';
import { useAuth } from '../context/AuthContext';

export default function Payments() {
  const { user } = useAuth();
  const [payments, setPayments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const load = async () => {
      try {
        const res = await api.get('/api/payments');
        setPayments(res.data);
      } catch (err) {
        setError('Failed to load payments');
      } finally {
        setLoading(false);
      }
    };
    if (user) load();
  }, [user]);

  return (
    <div className="container">
      <h1>Payments</h1>

      {error && <div className="error-msg">{error}</div>}

      {loading ? (
        <div className="loading">Loading...</div>
      ) : (
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Customer</th>
              <th>Policy #</th>
              <th>Date</th>
              <th>Amount</th>
              <th>Method</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            {payments.length === 0 ? (
              <tr><td colSpan="7" className="text-center">No payments found</td></tr>
            ) : payments.map((p) => (
              <tr key={p.paymentId}>
                <td>{p.paymentId}</td>
                <td>{p.customer && p.customer.username}</td>
                <td>{p.policy && p.policy.policyNumber}</td>
                <td>{p.paymentDate ? p.paymentDate.slice(0, 10) : '-'}</td>
                <td>{p.amount}</td>
                <td>{p.paymentMethod}</td>
                <td>{p.status}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
