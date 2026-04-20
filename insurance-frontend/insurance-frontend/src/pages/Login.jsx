import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Login() {
  const [step, setStep] = useState(1);           // 1 = pick role, 2 = enter credentials
  const [role, setRole] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const { login } = useAuth();
  const navigate = useNavigate();

  const selectRole = (selectedRole) => {
    setRole(selectedRole);
    setError('');
    setStep(2);
  };

  const goBack = () => {
    setStep(1);
    setEmail('');
    setPassword('');
    setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const data = await login(role, email, password);
      // Redirect based on role
      if (data.role === 'ADMIN') {
        navigate('/admin');
      } else if (data.role === 'AGENT') {
        navigate('/agent');
      } else if (data.role === 'CUSTOMER') {
        navigate('/customer');
      }
    } catch (err) {
      if (err.response && err.response.data) {
        // backend sends plain text for auth errors
        const msg = typeof err.response.data === 'string'
          ? err.response.data
          : (err.response.data.message || 'Login failed');
        setError(msg);
      } else {
        setError('Unable to connect to server. Is the backend running on port 9090?');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="login-wrapper">
      <div className="login-box">
        <h2>Insurance Management</h2>

        {step === 1 && (
          <>
            <p>Please select your role to continue</p>

            <div className="role-buttons">
              <button
                type="button"
                className="role-btn"
                onClick={() => selectRole('ADMIN')}
              >
                Login as Admin
              </button>

              <button
                type="button"
                className="role-btn agent"
                onClick={() => selectRole('AGENT')}
              >
                Login as Agent
              </button>

              <button
                type="button"
                className="role-btn customer"
                onClick={() => selectRole('CUSTOMER')}
              >
                Login as Customer
              </button>
            </div>

            <div className="register-link">
              New customer? <Link to="/register">Register here</Link>
            </div>
          </>
        )}

        {step === 2 && (
          <>
            <button type="button" className="back-link" onClick={goBack}>
              &larr; Change role
            </button>

            <p>Logging in as <b>{role}</b></p>

            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label>Email</label>
                <input
                  type="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                  autoFocus
                />
              </div>

              <div className="form-group">
                <label>Password</label>
                <input
                  type="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                />
              </div>

              {error && <div className="error-msg">{error}</div>}

              <button type="submit" className="submit-btn" disabled={loading}>
                {loading ? 'Logging in...' : 'Login'}
              </button>
            </form>
          </>
        )}
      </div>
    </div>
  );
}
