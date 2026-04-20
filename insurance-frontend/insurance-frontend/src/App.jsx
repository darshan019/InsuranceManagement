import { Routes, Route, Navigate } from 'react-router-dom';
import { useAuth } from './context/AuthContext';
import Navbar from './components/Navbar';
import ProtectedRoute from './components/ProtectedRoute';

import Login from './pages/Login';
import Register from './pages/Register';
import AdminDashboard from './pages/AdminDashboard';
import AgentDashboard from './pages/AgentDashboard';
import CustomerDashboard from './pages/CustomerDashboard';
import Agents from './pages/Agents';
import Customers from './pages/Customers';
import Policies from './pages/Policies';
import Claims from './pages/Claims';
import PolicyTemplates from './pages/PolicyTemplates';
import Payments from './pages/Payments';
import BrowseTemplates from './pages/BrowseTemplates';

export default function App() {
  const { user } = useAuth();

  // Redirect root (/) to the right place
  const homeRedirect = () => {
    if (!user) return <Navigate to="/login" replace />;
    if (user.role === 'ADMIN') return <Navigate to="/admin" replace />;
    if (user.role === 'AGENT') return <Navigate to="/agent" replace />;
    if (user.role === 'CUSTOMER') return <Navigate to="/customer" replace />;
    return <Navigate to="/login" replace />;
  };

  return (
    <>
      {user && <Navbar />}

      <Routes>
        {/* Public routes */}
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        {/* Admin routes */}
        <Route
          path="/admin"
          element={
            <ProtectedRoute allowedRoles={['ADMIN']}>
              <AdminDashboard />
            </ProtectedRoute>
          }
        />
        <Route
          path="/admin/agents"
          element={
            <ProtectedRoute allowedRoles={['ADMIN']}>
              <Agents />
            </ProtectedRoute>
          }
        />
        <Route
          path="/admin/customers"
          element={
            <ProtectedRoute allowedRoles={['ADMIN']}>
              <Customers />
            </ProtectedRoute>
          }
        />
        <Route
          path="/admin/policies"
          element={
            <ProtectedRoute allowedRoles={['ADMIN']}>
              <Policies />
            </ProtectedRoute>
          }
        />
        <Route
          path="/admin/claims"
          element={
            <ProtectedRoute allowedRoles={['ADMIN']}>
              <Claims />
            </ProtectedRoute>
          }
        />
        <Route
          path="/admin/templates"
          element={
            <ProtectedRoute allowedRoles={['ADMIN']}>
              <PolicyTemplates />
            </ProtectedRoute>
          }
        />
        <Route
          path="/admin/payments"
          element={
            <ProtectedRoute allowedRoles={['ADMIN']}>
              <Payments />
            </ProtectedRoute>
          }
        />

        {/* Agent routes */}
        <Route
          path="/agent"
          element={
            <ProtectedRoute allowedRoles={['AGENT']}>
              <AgentDashboard />
            </ProtectedRoute>
          }
        />
        <Route
          path="/agent/customers"
          element={
            <ProtectedRoute allowedRoles={['AGENT']}>
              <Customers />
            </ProtectedRoute>
          }
        />
        <Route
          path="/agent/policies"
          element={
            <ProtectedRoute allowedRoles={['AGENT']}>
              <Policies />
            </ProtectedRoute>
          }
        />
        <Route
          path="/agent/claims"
          element={
            <ProtectedRoute allowedRoles={['AGENT']}>
              <Claims />
            </ProtectedRoute>
          }
        />
        <Route
          path="/agent/templates"
          element={
            <ProtectedRoute allowedRoles={['AGENT']}>
              <PolicyTemplates />
            </ProtectedRoute>
          }
        />

        {/* Customer routes */}
        <Route
          path="/customer"
          element={
            <ProtectedRoute allowedRoles={['CUSTOMER']}>
              <CustomerDashboard />
            </ProtectedRoute>
          }
        />
        <Route
          path="/customer/browse"
          element={
            <ProtectedRoute allowedRoles={['CUSTOMER']}>
              <BrowseTemplates />
            </ProtectedRoute>
          }
        />
        <Route
          path="/customer/policies"
          element={
            <ProtectedRoute allowedRoles={['CUSTOMER']}>
              <Policies />
            </ProtectedRoute>
          }
        />
        <Route
          path="/customer/claims"
          element={
            <ProtectedRoute allowedRoles={['CUSTOMER']}>
              <Claims />
            </ProtectedRoute>
          }
        />

        {/* Root redirect */}
        <Route path="/" element={homeRedirect()} />
        <Route path="*" element={homeRedirect()} />
      </Routes>
    </>
  );
}
