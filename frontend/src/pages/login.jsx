import React, { useState, useContext } from 'react';
import { AuthContext } from '../context/authContext';
import { useNavigate } from 'react-router-dom';
import { Terminal } from 'lucide-react';

const Login = () => {
  const [isLogin, setIsLogin] = useState(true);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const { login, register } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      if (isLogin) {
        await login(email, password);
      } else {
        await register(email, password);
      }
      navigate('/');
    } catch (err) {
      setError('Authentication failed. Please check your credentials.');
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-950 p-4">
      <div className="max-w-md w-full bg-gray-900 rounded-xl shadow-2xl border border-gray-800 p-8">
        <div className="flex items-center justify-center gap-3 mb-8 text-cyan-400">
          <Terminal size={32} />
          <h1 className="text-3xl font-bold tracking-tight">Dev Diaries</h1>
        </div>
        
        <form onSubmit={handleSubmit} className="space-y-6">
          {error && <div className="text-red-400 text-sm bg-red-950/50 p-3 rounded">{error}</div>}
          
          <div>
            <label className="block text-sm font-medium text-gray-400 mb-2">Developer Email</label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="w-full bg-gray-950 border border-gray-700 rounded-lg px-4 py-3 text-gray-100 focus:outline-none focus:border-cyan-500 focus:ring-1 focus:ring-cyan-500 transition-colors"
              required
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-400 mb-2">Password</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full bg-gray-950 border border-gray-700 rounded-lg px-4 py-3 text-gray-100 focus:outline-none focus:border-cyan-500 focus:ring-1 focus:ring-cyan-500 transition-colors"
              required
            />
          </div>

          <button type="submit" className="w-full bg-cyan-600 hover:bg-cyan-500 text-white font-semibold py-3 rounded-lg transition-colors">
            {isLogin ? 'Initialize Session' : 'Create Profile'}
          </button>
        </form>

        <div className="mt-6 text-center text-gray-500 text-sm">
          {isLogin ? "Don't have a profile? " : "Already initialized? "}
          <button onClick={() => setIsLogin(!isLogin)} className="text-cyan-400 hover:text-cyan-300">
            {isLogin ? 'Register' : 'Login'}
          </button>
        </div>
      </div>
    </div>
  );
};

export default Login;