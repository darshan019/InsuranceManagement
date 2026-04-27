import { Routes, Route, Navigate, useLocation } from 'react-router-dom';
import { useAuth } from './context/AuthContext';
import Navbar from './components/Navbar';
import ProtectedRoute from './components/ProtectedRoute';
import Home from './pages/Home';
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
  const location = useLocation();

  // Public routes where the app navbar should NOT show
  const publicPaths = ['/', '/login', '/register'];
  const isPublic = publicPaths.includes(location.pathname);

  return (
    <>
      {user && !isPublic && <Navbar />}

      <Routes>
        {/* Public */}
        <Route path="/" element={<Home />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        {/* Admin */}
        <Route path="/admin" element={
          <ProtectedRoute allowedRoles={['ADMIN']}><AdminDashboard /></ProtectedRoute>
        } />
        <Route path="/admin/agents" element={
          <ProtectedRoute allowedRoles={['ADMIN']}><Agents /></ProtectedRoute>
        } />
        <Route path="/admin/customers" element={
          <ProtectedRoute allowedRoles={['ADMIN']}><Customers /></ProtectedRoute>
        } />
        <Route path="/admin/policies" element={
          <ProtectedRoute allowedRoles={['ADMIN']}><Policies /></ProtectedRoute>
        } />
        <Route path="/admin/claims" element={
          <ProtectedRoute allowedRoles={['ADMIN']}><Claims /></ProtectedRoute>
        } />
        <Route path="/admin/templates" element={
          <ProtectedRoute allowedRoles={['ADMIN']}><PolicyTemplates /></ProtectedRoute>
        } />
        <Route path="/admin/payments" element={
          <ProtectedRoute allowedRoles={['ADMIN']}><Payments /></ProtectedRoute>
        } />

        {/* Agent */}
        <Route path="/agent" element={
          <ProtectedRoute allowedRoles={['AGENT']}><AgentDashboard /></ProtectedRoute>
        } />
        <Route path="/agent/customers" element={
          <ProtectedRoute allowedRoles={['AGENT']}><Customers /></ProtectedRoute>
        } />
        <Route path="/agent/policies" element={
          <ProtectedRoute allowedRoles={['AGENT']}><Policies /></ProtectedRoute>
        } />
        <Route path="/agent/claims" element={
          <ProtectedRoute allowedRoles={['AGENT']}><Claims /></ProtectedRoute>
        } />
        <Route path="/agent/templates" element={
          <ProtectedRoute allowedRoles={['AGENT']}><PolicyTemplates /></ProtectedRoute>
        } />

        {/* Customer */}
        <Route path="/customer" element={
          <ProtectedRoute allowedRoles={['CUSTOMER']}><CustomerDashboard /></ProtectedRoute>
        } />
        <Route path="/customer/browse" element={
          <ProtectedRoute allowedRoles={['CUSTOMER']}><BrowseTemplates /></ProtectedRoute>
        } />
        <Route path="/customer/policies" element={
          <ProtectedRoute allowedRoles={['CUSTOMER']}><Policies /></ProtectedRoute>
        } />
        <Route path="/customer/claims" element={
          <ProtectedRoute allowedRoles={['CUSTOMER']}><Claims /></ProtectedRoute>
        } />

        {/* Unknown paths go home */}
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </>
  );
}
