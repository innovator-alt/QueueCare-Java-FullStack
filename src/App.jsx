import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './contexts/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import Dashboard from './pages/Dashboard';
import Login from './pages/Login';
import Register from './pages/Register';
import './App.css';

function AppRoutes() {
  const { user } = useAuth();

  return (
    <Routes>
      <Route path="/login" element={user ? <Navigate to={`/${user.role.toLowerCase()}`} /> : <Login />} />
      <Route path="/register" element={user ? <Navigate to={`/${user.role.toLowerCase()}`} /> : <Register />} />
      
      <Route path="/admin" element={
        <ProtectedRoute allowedRoles={['ADMIN']}>
          <Dashboard />
        </ProtectedRoute>
      } />
      
      <Route path="/doctor" element={
        <ProtectedRoute allowedRoles={['DOCTOR']}>
          <Dashboard />
        </ProtectedRoute>
      } />
      
      <Route path="/reception" element={
        <ProtectedRoute allowedRoles={['RECEPTIONIST']}>
          <Dashboard />
        </ProtectedRoute>
      } />
      
      <Route path="/patient" element={
        <ProtectedRoute allowedRoles={['PATIENT']}>
          <Dashboard />
        </ProtectedRoute>
      } />
      
      <Route path="/" element={
        user ? 
          <Navigate to={`/${user.role.toLowerCase()}`} replace /> : 
          <Navigate to="/login" replace />
      } />
      
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}

function App() {
  return (
    <Router>
      <AuthProvider>
        <AppRoutes />
      </AuthProvider>
    </Router>
  );
}

export default App;
