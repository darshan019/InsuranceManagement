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

  // Unified login — no role parameter. Backend figures out who's who
  // from the email, puts the role in the JWT, and returns it in the response.
  const login = async (email, password) => {
    const res = await api.post('/auth/login', { email, password });
    const data = res.data; // { token, role, email, userId? }

    let userId = data.userId || null;
    try {
      if (userId) {
        // already have it
      } else if (data.role === 'CUSTOMER') {
        const all = await api.get('/api/customers', {
          headers: { Authorization: 'Bearer ' + data.token }
        }).catch(() => null);
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
      // ignore
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
