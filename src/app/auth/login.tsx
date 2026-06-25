import React, { useState } from 'react';
import { colors } from '../../styles/theme';
import { AppShell } from '../../components/layout/AppShell';
import { FormField } from '../../components/ui/FormField';
import { PrimaryButton } from '../../components/ui/PrimaryButton';
import { PrimaryCard } from '../../components/ui/PrimaryCard';

interface LoginProps {
  onNavigate: (route: string) => void;
  onLoginSuccess: () => void;
}

export const LoginPage: React.FC<LoginProps> = ({ onNavigate, onLoginSuccess }) => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');

  const handleLogin = (e: React.FormEvent) => {
    e.preventDefault();
    if (!email.includes('@')) {
      setError('Please enter a valid email address.');
      return;
    }
    if (password.length < 6) {
      setError('Password must contain at least 6 characters.');
      return;
    }
    setError('');
    // Successful login simulation
    onLoginSuccess();
    onNavigate('home');
  };

  return (
    <AppShell hideBottomNav>
      <div className="flex-grow flex flex-col justify-center py-6 px-2 min-h-[85vh]">
        <div className="flex flex-col gap-6 w-full max-w-sm mx-auto">
          
          <div className="text-center flex flex-col gap-1.5 mb-2">
            <span className="material-symbols-outlined text-[44px]" style={{ color: colors.primary }}>
              vpn_key
            </span>
            <h2 className="text-2xl font-extrabold" style={{ color: colors.primary, fontFamily: 'Plus Jakarta Sans' }}>
              Welcome back
            </h2>
            <p className="text-xs" style={{ color: colors.onSurfaceVariant }}>
              Your private health details remain secure on your hardware 🔐
            </p>
          </div>

          <form onSubmit={handleLogin} className="flex flex-col gap-4">
            <FormField
              label="Email Address"
              type="email"
              placeholder="example@lunacare.com"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              icon="mail"
              required
            />

            <FormField
              label="Password"
              type="password"
              placeholder="••••••••"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              icon="lock"
              required
            />

            {error && (
              <p className="text-xs font-bold leading-none px-1" style={{ color: colors.error }}>
                ⚠️ {error}
              </p>
            )}

            <PrimaryButton 
              label="Secure Sign In" 
              type="submit"
              fullWidth
              style={{ marginTop: '8px' }}
            />
          </form>

          {/* Health awareness preview card */}
          <PrimaryCard variant="low" className="p-4 flex flex-col gap-1.5 border-dashed">
            <h4 className="text-xs font-bold flex items-center gap-1.5" style={{ color: colors.primary }}>
              <span className="material-symbols-outlined text-sm">auto_stories</span>
              Private Health Library Preview:
            </h4>
            <p className="text-[10px] leading-relaxed" style={{ color: colors.onSurfaceVariant }}>
              Learn safely about PMS, PCOS, PCOD, PMOS, healthy menstrual cup choices, and secure doctor consultation journals without trackers.
            </p>
          </PrimaryCard>

          <div className="flex flex-col gap-3 text-center mt-2">
            <div className="flex items-center justify-between text-xs font-bold px-2">
              <button onClick={() => onNavigate('register')} className="hover:underline" style={{ color: colors.primary }}>
                Create Account
              </button>
              <span style={{ color: colors.outline }}>•</span>
              <button className="hover:underline" style={{ color: colors.secondary }}>
                Forgot Password?
              </button>
            </div>

            <button 
              onClick={() => onNavigate('home')}
              className="text-xs font-bold mt-4"
              style={{ color: colors.onSurfaceVariant }}
            >
              Continue as Guest 👤
            </button>
          </div>

        </div>
      </div>
    </AppShell>
  );
};

export default LoginPage;
