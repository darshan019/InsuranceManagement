import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Navbar() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = async () => {
    await logout();
    navigate('/login');
  };

  if (!user) return null;

  const role = user.role;

  return (
    <div className="navbar">
      <h2>Insurance Management</h2>

      <div className="navbar-links">
        {role === 'ADMIN' && (
          <>
            <Link to="/admin">Dashboard</Link>
            <Link to="/admin/agents">Agents</Link>
            <Link to="/admin/customers">Customers</Link>
            <Link to="/admin/policies">Policies</Link>
            <Link to="/admin/claims">Claims</Link>
            <Link to="/admin/templates">Templates</Link>
            <Link to="/admin/payments">Payments</Link>
          </>
        )}

        {role === 'AGENT' && (
          <>
            <Link to="/agent">Dashboard</Link>
            <Link to="/agent/customers">My Customers</Link>
            <Link to="/agent/policies">My Policies</Link>
            <Link to="/agent/claims">Claims</Link>
            <Link to="/agent/templates">Templates</Link>
          </>
        )}

        {role === 'CUSTOMER' && (
          <>
            <Link to="/customer">Dashboard</Link>
            <Link to="/customer/browse">Browse Plans</Link>
            <Link to="/customer/policies">My Policies</Link>
            <Link to="/customer/claims">File Claim</Link>
          </>
        )}
      </div>

      <div className="navbar-user">
        {user.email} ({role})
        <button className="logout-btn" onClick={handleLogout}>Logout</button>
      </div>
    </div>
  );
}
