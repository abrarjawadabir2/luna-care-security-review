import React from 'react';
import { colors } from '../styles/theme';
import { AppShell } from '../components/layout/AppShell';
import { PrimaryCard } from '../components/ui/PrimaryCard';

interface CrisisProps {
  onNavigate: (route: string) => void;
}

export const CrisisSupportPage: React.FC<CrisisProps> = ({ onNavigate }) => {
  return (
    <AppShell hideBottomNav>
      <div className="flex flex-col gap-6 py-6 w-full animate-fade-in">
        
        {/* Dynamic header */}
        <div className="flex items-center justify-between px-1">
          <div className="flex flex-col">
            <span className="text-[10px] font-extrabold uppercase tracking-wider text-rose-500">Urgent Safety Center</span>
            <h2 className="text-2xl font-black text-rose-800" style={{ fontFamily: 'Plus Jakarta Sans' }}>
              We are here for you 🕊️
            </h2>
          </div>
          <button
            onClick={() => onNavigate('home')}
            className="p-1.5 rounded-full hover:bg-neutral-100"
            style={{ color: colors.primary }}
          >
            <span className="material-symbols-outlined text-2xl font-bold">home</span>
          </button>
        </div>

        {/* SEC 1: MAIN CONSOLATION HEADER */}
        <PrimaryCard className="flex flex-col gap-2 border-2 border-rose-400" variant="low">
          <h3 className="text-sm font-extrabold text-rose-800 flex items-center gap-1.5">
            <span className="material-symbols-outlined text-base">gpp_maybe</span>
            You are not alone.
          </h3>
          <p className="text-xs leading-relaxed" style={{ color: colors.onSurfaceVariant }}>
            If you are going through a heavy emotional surge, experiencing extreme anxiety, or experiencing self-harm urges, please reach out to our active community resources or direct national medical lifelines.
          </p>
        </PrimaryCard>

        {/* SEC 2: LOCAL BULLET LIST HOTLINES */}
        <div className="flex flex-col gap-3">
          <h4 className="text-xs font-extrabold uppercase tracking-wider text-neutral-500 px-1">
            Regional Crisis Helplines
          </h4>

          <PrimaryCard className="flex flex-col gap-2">
            <div className="flex justify-between items-center text-xs font-bold" style={{ color: colors.primary }}>
              <span>National Crisis Support Desk</span>
              <span>Direct Dial: 109 📞</span>
            </div>
            <p className="text-[11px]" style={{ color: colors.onSurfaceVariant }}>
              Providing free, completely confidential, 24/7 listening support and emotional safety counsel across the country.
            </p>
          </PrimaryCard>

          <PrimaryCard className="flex flex-col gap-2">
            <div className="flex justify-between items-center text-xs font-bold" style={{ color: colors.secondary }}>
              <span>Mental Health & Suicide Desk - Kaan পেতে রই</span>
              <span>Hotline: +88017012200📞</span>
            </div>
            <p className="text-[11px]" style={{ color: colors.onSurfaceVariant }}>
              Safe, respectful listening hotline. Speak with highly trained warm advisors willing to comfort you.
            </p>
          </PrimaryCard>

          <PrimaryCard className="flex flex-col gap-2">
            <div className="flex justify-between items-center text-xs font-bold" style={{ color: colors.tertiary }}>
              <span>National Emergency Services</span>
              <span>Direct Dial: 999 🚨</span>
            </div>
            <p className="text-[11px]" style={{ color: colors.onSurfaceVariant }}>
              Call immediately for urgent localized dispatch, ambulance assets, or safety interventions.
            </p>
          </PrimaryCard>
        </div>

        {/* SEC 3: COGNITIVE REFLECTION PROTOCOLS */}
        <PrimaryCard variant="container" className="flex flex-col gap-2">
          <h4 className="text-xs font-extrabold uppercase tracking-wider" style={{ color: colors.primary }}>
            💡 Deep Breathing Relief Cycle (4-7-8 Technique)
          </h4>
          <p className="text-[11px] leading-relaxed" style={{ color: colors.onSurfaceVariant }}>
            1. Breathe in through your nose smoothly for 4 seconds.<br />
            2. Fully hold your breath with relaxation for 7 seconds.<br />
            3. Exhale completely, releasing all abdominal tension, over 8 seconds.
          </p>
        </PrimaryCard>

        <button
          onClick={() => onNavigate('home')}
          className="w-full min-h-[48px] border-2 rounded-2xl flex items-center justify-center font-extrabold text-xs transition-all tracking-wider hover:bg-neutral-50 active:scale-95 text-neutral-700"
          style={{ borderColor: colors.outlineVariant }}
        >
          Return to My Personal Dashboard 🏡
        </button>

      </div>
    </AppShell>
  );
};

export default CrisisSupportPage;
