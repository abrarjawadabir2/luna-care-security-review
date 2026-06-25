import React, { useState } from 'react';
import { colors } from '../../styles/theme';
import { AppShell } from '../../components/layout/AppShell';
import { FormField } from '../../components/ui/FormField';
import { PrimaryButton } from '../../components/ui/PrimaryButton';

interface RegisterProps {
  onNavigate: (route: string) => void;
  onRegisterSuccess: () => void;
}

export const RegisterPage: React.FC<RegisterProps> = ({ onNavigate, onRegisterSuccess }) => {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [consent, setConsent] = useState(false);
  const [error, setError] = useState('');

  const handleRegister = (e: React.FormEvent) => {
    e.preventDefault();
    if (!name.trim()) {
      setError('Please provide your name.');
      return;
    }
    if (!email.includes('@')) {
      setError('Please enter a valid email address.');
      return;
    }
    if (password.length < 6) {
      setError('Password must contain at least 6 characters.');
      return;
    }
    if (!consent) {
      setError('Please accept the secure privacy agreement.');
      return;
    }
    setError('');
    onRegisterSuccess();
    onNavigate('onboarding');
  };

  return (
    <AppShell hideBottomNav>
      <div className="flex-grow flex flex-col justify-center py-6 px-2 min-h-[85vh]">
        <div className="flex flex-col gap-6 w-full max-w-sm mx-auto">
          
          <div className="text-center flex flex-col gap-1.5 mb-1">
            <span className="material-symbols-outlined text-[44px]" style={{ color: colors.secondary }}>
              person_add
            </span>
            <h2 className="text-2xl font-extrabold" style={{ color: colors.primary, fontFamily: 'Plus Jakarta Sans' }}>
              Create Secure Profile
            </h2>
            <p className="text-xs" style={{ color: colors.onSurfaceVariant }}>
              Secure end-to-end client database registration 🕊️
            </p>
          </div>

          <form onSubmit={handleRegister} className="flex flex-col gap-4">
            <FormField
              label="Friendly Name"
              type="text"
              placeholder="e.g. Abir"
              value={name}
              onChange={(e) => setName(e.target.value)}
              icon="badge"
              required
            />

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
              label="Secure Password"
              type="password"
              placeholder="••••••••"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              icon="lock"
              required
            />

            <div className="flex items-start gap-3 px-1 mt-1 select-none">
              <input
                id="consent-checkbox"
                type="checkbox"
                checked={consent}
                onChange={(e) => setConsent(e.target.checked)}
                className="mt-1 h-4 w-4 rounded border-neutral-300 text-[#8a4d4e] focus:ring-[#8a4d4e]"
              />
              <label htmlFor="consent-checkbox" className="text-[10px] leading-snug" style={{ color: colors.onSurfaceVariant }}>
                I agree to the <span className="font-bold underline cursor-pointer" style={{ color: colors.primary }}>Privacy center guidelines</span>. My information remains stored offline on this terminal for medical safety.
              </label>
            </div>

            {error && (
              <p className="text-xs font-bold leading-none px-1 text-center" style={{ color: colors.error }}>
                ⚠️ {error}
              </p>
            )}

            <PrimaryButton 
              label="Register Safe Account" 
              type="submit"
              fullWidth
              style={{ marginTop: '8px' }}
            />
          </form>

          <div className="flex flex-col gap-3 text-center mt-2">
            <p className="text-xs font-bold">
              Already have an account?{' '}
              <button onClick={() => onNavigate('login')} className="underline hover:opacity-85" style={{ color: colors.primary }}>
                Sign In
              </button>
            </p>

            <button 
              onClick={() => onNavigate('home')}
              className="text-xs font-bold mt-2"
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

export default RegisterPage;
