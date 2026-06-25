import React from 'react';
import { colors } from '../styles/theme';
import { AppShell } from '../components/layout/AppShell';
import { PrimaryButton } from '../components/ui/PrimaryButton';
import { SecondaryButton } from '../components/ui/SecondaryButton';

interface IndexProps {
  onNavigate: (route: string) => void;
}

export const IndexPage: React.FC<IndexProps> = ({ onNavigate }) => {
  return (
    <AppShell hideBottomNav>
      <div className="flex-grow flex flex-col justify-between py-10 px-4 min-h-[80vh]">
        
        {/* Top Spacer */}
        <div className="h-6" />

        {/* Branded hero Section */}
        <div className="flex flex-col items-center text-center gap-6 my-auto">
          {/* Logo illustration */}
          <div 
            className="w-24 h-24 rounded-full flex items-center justify-center shadow-gentle-lg animate-pulse"
            style={{ backgroundColor: colors.surfaceContainer }}
          >
            <span className="material-symbols-outlined text-[54px]" style={{ color: colors.primary }}>
              health_and_safety
            </span>
          </div>

          <div className="flex flex-col gap-3">
            <h1 
              className="text-4xl font-extrabold tracking-tight px-2"
              style={{ 
                color: colors.primary,
                fontFamily: 'Plus Jakarta Sans',
              }}
            >
              LunaCare
            </h1>
            <p 
              className="text-sm px-6 leading-relaxed font-semibold"
              style={{ color: colors.onSurfaceVariant }}
            >
              “Period care, mental wellness, and gentle support.”
            </p>
          </div>
        </div>

        {/* Action button panel */}
        <div className="flex flex-col gap-3 w-full max-w-sm mx-auto mt-auto">
          <PrimaryButton 
            label="Get Started" 
            icon="rocket_launch"
            fullWidth 
            onClick={() => onNavigate('onboarding')}
          />
          
          <SecondaryButton 
            label="Sign In To My Account" 
            fullWidth 
            onClick={() => onNavigate('login')}
          />

          <button 
            onClick={() => onNavigate('home')}
            className="text-xs font-bold py-2 hover:opacity-85"
            style={{ color: colors.secondary }}
          >
            Continue as Guest 👤
          </button>

          {/* Core non-diagnostic medical disclaimer */}
          <div className="text-center mt-4 px-4">
            <p className="text-[10px] leading-relaxed" style={{ color: colors.onSurfaceVariant }}>
              🛡️ <span className="font-bold">Educational Support Only:</span> LunaCare provides cycle education and symptom logs. We do not provide clinical diagnostics or active drug dosage indices. Always seek physician validation before acting.
            </p>
          </div>
        </div>

      </div>
    </AppShell>
  );
};

export default IndexPage;
