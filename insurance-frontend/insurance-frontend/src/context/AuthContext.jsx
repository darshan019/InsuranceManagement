import { createContext, useContext, useState, useEffect } from 'react';
import api from '../api';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);

  useEffect(() => {
    const token = localStorage.getItem('token');
    const role = localStorage.getItem('role');
    const email = localStorage.getItem('email');
    const userId = localStorage.getItem('userId');
    if (token && role) {
      setUser({ token, role, email, userId });
    }
  }, []);

  const login = async (role, email, password) => {
    // role is 'ADMIN', 'AGENT', or 'CUSTOMER'
    const endpoint = '/auth/' + role.toLowerCase() + '/login';
    const res = await api.post(endpoint, { email, password });
    const data = res.data;
    // data = { token, role, email } - or { token, role, email, userId }
    // if the backend LoginResponse was updated to include userId

    // Prefer userId from response if present; otherwise look it up.
    let userId = data.userId || null;
    try {
      if (userId) {
        // Already got it, skip lookup
      } else if (data.role === 'CUSTOMER') {
        const all = await api.get('/api/customers', {
          headers: { Authorization: 'Bearer ' + data.token }
        }).catch(() => null);
        // Customer doesn't have permission to list customers,
        // so this may fail. Fallback: extract from token payload.
        if (all && all.data) {
          const me = all.data.find((c) => c.email === data.email);
          if (me) userId = me.customerId;
        }
      } else if (data.role === 'AGENT') {
        const all = await api.get('/api/agents/', {
          headers: { Authorization: 'Bearer ' + data.token }
        }).catch(() => null);
        if (all && all.data) {
          const me = all.data.find((a) => a.email === data.email);
          if (me) userId = me.agentId;
        }
      } else if (data.role === 'ADMIN') {
        const all = await api.get('/api/admins', {
          headers: { Authorization: 'Bearer ' + data.token }
        }).catch(() => null);
        if (all && all.data) {
          const me = all.data.find((a) => a.email === data.email);
          if (me) userId = me.adminId;
        }
      }
    } catch (err) {
      // ignore - userId is optional for some views
      console.warn('Could not fetch userId', err);
    }

    localStorage.setItem('token', data.token);
    localStorage.setItem('role', data.role);
    localStorage.setItem('email', data.email);
    if (userId) localStorage.setItem('userId', userId);

    setUser({
      token: data.token,
      role: data.role,
      email: data.email,
      userId: userId
    });

    return data;
  };

  const logout = async () => {
    try {
      await api.post('/auth/logout');
    } catch (err) {
      // ignore - logout should still work locally
    }
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('email');
    localStorage.removeItem('userId');
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
