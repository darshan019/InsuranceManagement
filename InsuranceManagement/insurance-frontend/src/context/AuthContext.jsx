import { createContext, useContext, useState, useEffect } from 'react';
import api from '../api';
const AuthContext = createContext(null);
export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true); // ✅ important
  // Restore session on page refresh
  useEffect(() => {
    const token = localStorage.getItem('token');
    const role = localStorage.getItem('role');
    const email = localStorage.getItem('email');
    const userId = localStorage.getItem('userId');
    if (token && role) {
      setUser({ token, role, email, userId });
    }
    setLoading(false); // ✅ auth check completed
  }, []);
  // Login
  const login = async (email, password) => {
    const res = await api.post('/auth/login', { email, password });
    const data = res.data; // { token, role, email, userId }
    localStorage.setItem('token', data.token);
    localStorage.setItem('role', data.role);
    localStorage.setItem('email', data.email);
    if (data.userId) {
      localStorage.setItem('userId', data.userId);
    }
    setUser({
      token: data.token,
      role: data.role,
      email: data.email,
      userId: data.userId || null
    });
    return data;
  };
  // Logout
  const logout = async () => {
    try {
      await api.post('/auth/logout');
    } catch (e) {
      // ignore
    }
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('email');
    localStorage.removeItem('userId');
    // Clean up any stale per-user cached keys from the old localStorage-based
    // claim flow. Safe to leave in even after the new DB-backed flow takes over.
    Object.keys(localStorage)
      .filter((k) => k.startsWith('myClaimIds_'))
      .forEach((k) => localStorage.removeItem(k));
    setUser(null);
  };

  return (
<AuthContext.Provider value={{ user, loading, login, logout }}>

      {children}
</AuthContext.Provider>

  );

}

export function useAuth() {

  return useContext(AuthContext);

}
