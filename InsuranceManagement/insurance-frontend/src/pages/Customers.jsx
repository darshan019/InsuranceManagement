import { useEffect, useState } from 'react';
import api from '../api';
import { useAuth } from '../context/AuthContext';

export default function Customers() {
  const { user } = useAuth();
  const [customers, setCustomers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const isAdmin = user && user.role === 'ADMIN';

  const loadCustomers = async () => {
    setLoading(true);
    try {
      let res;
      if (isAdmin) {
        res = await api.get('/api/customers');
      } else {
        // Agent's customers
        res = await api.get('/api/agents/' + user.userId + '/customers');
      }
      setCustomers(res.data);
    } catch (err) {
      setError('Failed to load customers');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (user) loadCustomers();
    // eslint-disable-next-line
  }, [user]);

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this customer?')) return;
    try {
      await api.delete('/api/customers/' + id);
      setSuccess('Customer deleted');
      loadCustomers();
    } catch (err) {
      setError('Failed to delete customer');
    }
  };

  return (
    <div className="container">
      <h1>{isAdmin ? 'All Customers' : 'My Customers'}</h1>

      {error && <div className="error-msg">{error}</div>}
      {success && <div className="success-msg">{success}</div>}

      {loading ? (
        <div className="loading">Loading...</div>
      ) : (
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Username</th>
              <th>Email</th>
              <th>Address</th>
              <th>DOB</th>
              {isAdmin && <th>Actions</th>}
            </tr>
          </thead>
          <tbody>
            {customers.length === 0 ? (
              <tr>
                <td colSpan={isAdmin ? 6 : 5} className="text-center">No customers found</td>
              </tr>
            ) : customers.map((c) => (
              <tr key={c.customerId}>
                <td>{c.customerId}</td>
                <td>{c.username}</td>
                <td>{c.email}</td>
                <td>{c.address}</td>
                <td>{c.dateOfBirth}</td>
                {isAdmin && (
                  <td>
                    <button className="btn btn-danger" onClick={() => handleDelete(c.customerId)}>
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
