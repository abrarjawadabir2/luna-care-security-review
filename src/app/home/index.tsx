import React from 'react';
import { colors } from '../../styles/theme';
import { AppShell } from '../../components/layout/AppShell';
import { TopAppBar } from '../../components/layout/TopAppBar';
import { PrimaryCard } from '../../components/ui/PrimaryCard';
import { QuickActionCard } from '../../components/ui/QuickActionCard';
import { WarningCard } from '../../components/ui/WarningCard';
import { UserMode } from '../../lib/navigationMode';

interface HomeProps {
  onNavigate: (route: string) => void;
  userMode: UserMode;
  userName?: string;
  onOpenSidePanel: () => void;
}

export const HomePage: React.FC<HomeProps> = ({
  onNavigate,
  userMode,
  userName = 'Abir',
  onOpenSidePanel,
}) => {
  return (
    <div className="w-full flex flex-col min-h-screen">
      <TopAppBar 
        title="LunaCare" 
        userName={userName}
        onMenuClick={onOpenSidePanel}
        onNotificationsClick={() => onNavigate('notifications')}
        onSettingsClick={() => onNavigate('settings')}
      />

      <AppShell userMode={userMode}>
        <div className="flex flex-col gap-5 py-4 w-full">
          
          {/* SEC 1: MAIN HERO TAILORED HEADER CARD */}
          {userMode === 'SELF_TRACKING' && (
            <PrimaryCard variant="container" className="flex flex-col gap-3 relative overflow-hidden">
              <div className="absolute right-[-20px] top-[-20px] opacity-10 pointer-events-none">
                <span className="material-symbols-outlined text-[120px] text-[#8a4d4e] select-none">calendar_view_month</span>
              </div>

              <div className="flex justify-between items-start">
                <div className="flex flex-col">
                  <span className="text-[10px] font-extrabold uppercase tracking-widest" style={{ color: colors.primary }}>
                    Current Estimated Status
                  </span>
                  <h3 className="text-2xl font-black mt-0.5" style={{ color: colors.onSurface, fontFamily: 'Plus Jakarta Sans' }}>
                    Cycle Day 10 🩸
                  </h3>
                </div>
                <button 
                  onClick={() => onNavigate('cycle')}
                  className="px-3 py-1.5 rounded-full text-[10px] font-bold border transition-all hover:bg-white"
                  style={{ borderColor: colors.outlineVariant, color: colors.primary }}
                >
                  Edit History ✏️
                </button>
              </div>

              <div className="flex flex-col gap-0.5">
                <div className="flex items-center gap-1.5 text-xs font-bold" style={{ color: colors.onSurface }}>
                  <span className="w-2.5 h-2.5 rounded-full" style={{ backgroundColor: colors.secondary }} />
                  <span>Phase: Follicular Phase</span>
                </div>
                <p className="text-[11px] leading-relaxed mt-1" style={{ color: colors.onSurfaceVariant }}>
                  Estimation: Your next period is expected to begin in <span className="font-extrabold" style={{ color: colors.primary }}>18 days</span>. This is an average based on your logged calendar history.
                </p>
              </div>
            </PrimaryCard>
          )}

          {userMode === 'SUPPORT_MODE' && (
            <PrimaryCard variant="container" className="flex flex-col gap-2 relative overflow-hidden">
              <div className="absolute right-[-10px] top-[-10px] opacity-10 pointer-events-none">
                <span className="material-symbols-outlined text-[80px] text-[#8D4D39]">volunteer_activism</span>
              </div>
              <span className="text-[10px] font-extrabold uppercase tracking-wider" style={{ color: colors.tertiary }}>
                Support with respect 🤝
              </span>
              <h3 className="text-xl font-extrabold" style={{ color: colors.onSurface, fontFamily: 'Plus Jakarta Sans' }}>
                How to Care for Her Today 🌱
              </h3>
              <p className="text-xs leading-relaxed" style={{ color: colors.onSurfaceVariant }}>
                No assumptions, just gentle presence. Keep warm drinks or comforting hot bags handy. Browse active PMS checklists or learn standard non-invasive care routines safely.
              </p>
            </PrimaryCard>
          )}

          {userMode === 'EDUCATION_ONLY' && (
            <PrimaryCard variant="container" className="flex flex-col gap-2">
              <span className="text-[10px] font-extrabold uppercase tracking-wider" style={{ color: colors.secondary }}>
                Health Education Library 📖
              </span>
              <h3 className="text-xl font-extrabold" style={{ color: colors.onSurface, fontFamily: 'Plus Jakarta Sans' }}>
                Learn Period Health Basics
              </h3>
              <p className="text-xs leading-relaxed" style={{ color: colors.onSurfaceVariant }}>
                Browse verified clinical guidelines regarding severe PMS, endometriosis indicators, safe menstrual cup usage and cleaning protocols without tracking logs.
              </p>
            </PrimaryCard>
          )}

          {/* SEC 2: QUICK ACTION GRID BUTTONS */}
          <div className="flex flex-col gap-3">
            <h4 className="text-xs font-extrabold tracking-wider uppercase px-1" style={{ color: colors.onSurfaceVariant }}>
              Quick Actions
            </h4>
            
            {userMode === 'SELF_TRACKING' && (
              <div className="grid grid-cols-2 gap-3">
                <QuickActionCard label="Log Symptoms" icon="medical_information" onClick={() => onNavigate('cycle')} />
                <QuickActionCard label="Mood Check-In" icon="mood" onClick={() => onNavigate('mood')} />
                <QuickActionCard label="Medical Journal" icon="clinical_researching" onClick={() => onNavigate('medical-journal')} />
                <QuickActionCard label="Find Nearby Help" icon="map" onClick={() => onNavigate('care')} />
              </div>
            )}

            {userMode === 'SUPPORT_MODE' && (
              <div className="grid grid-cols-2 gap-3">
                <QuickActionCard label="Support Basics" icon="menu_book" onClick={() => onNavigate('learn')} />
                <QuickActionCard label="Care Products" icon="local_mall" onClick={() => onNavigate('care')} />
                <QuickActionCard label="Warning Signs" icon="gpp_maybe" onClick={() => onNavigate('learn')} />
                <QuickActionCard label="Find Clinics" icon="map" onClick={() => onNavigate('care')} />
              </div>
            )}

            {userMode === 'EDUCATION_ONLY' && (
              <div className="grid grid-cols-2 gap-3">
                <QuickActionCard label="PMS Awareness" icon="menu_book" onClick={() => onNavigate('learn')} />
                <QuickActionCard label="Cup Safety Guide" icon="wash" onClick={() => onNavigate('learn')} />
                <QuickActionCard label="Mental Wellbeing" icon="psychology" onClick={() => onNavigate('mood')} />
                <QuickActionCard label="Care Products" icon="local_mall" onClick={() => onNavigate('care')} />
              </div>
            )}
          </div>

          {/* SEC 3: DAILY FOCUS SECTION / CARDS */}
          <div className="flex flex-col gap-3">
            <h4 className="text-xs font-extrabold tracking-wider uppercase px-1" style={{ color: colors.onSurfaceVariant }}>
              Daily Educational Focus
            </h4>

            {userMode === 'SELF_TRACKING' && (
              <div className="flex flex-col gap-3">
                <PrimaryCard 
                  onClick={() => onNavigate('learn')}
                  className="flex flex-col gap-1.5 focus:ring-2 focus:ring-[#8a4d4e]/40 p-4 border"
                >
                  <span className="text-[9px] font-bold uppercase tracking-wider" style={{ color: colors.primary }}>
                    Article Series
                  </span>
                  <h4 className="text-sm font-extrabold" style={{ color: colors.onSurface }}>
                    Severe Menstrual Pain (PCOS & Endometriosis) 🩺
                  </h4>
                  <p className="text-[11px] leading-relaxed" style={{ color: colors.onSurfaceVariant }}>
                    Understand biological pathways behind extreme cramps and know exactly when manual clinical consultations are recommended.
                  </p>
                </PrimaryCard>

                <PrimaryCard 
                  onClick={() => onNavigate('ai')}
                  className="flex flex-col gap-1.5 p-4 border relative overflow-hidden"
                >
                  <div className="absolute right-[-15px] top-[-15px] opacity-10 pointer-events-none">
                    <span className="material-symbols-outlined text-[80px]" style={{ color: colors.primary }}>psychology</span>
                  </div>
                  <span className="text-[9px] font-bold uppercase tracking-wider" style={{ color: colors.secondary }}>
                    Luna AI Companion
                  </span>
                  <h4 className="text-sm font-extrabold" style={{ color: colors.onSurface }}>
                    Consult Luna Intelligence Spark ✨
                  </h4>
                  <p className="text-[11px] leading-relaxed" style={{ color: colors.onSurfaceVariant }}>
                    Ask general cycle hygiene questions securely. Client logs are kept private; zero clinical data are transferred to servers.
                  </p>
                </PrimaryCard>
              </div>
            )}

            {userMode !== 'SELF_TRACKING' && (
              <PrimaryCard onClick={() => onNavigate('learn')} className="flex flex-col gap-1.5 p-4 border">
                <span className="text-[9px] font-bold uppercase tracking-wider" style={{ color: colors.tertiary }}>
                  Support Spotlight
                </span>
                <h4 className="text-sm font-extrabold" style={{ color: colors.onSurface }}>
                  Menstrual Cup Insertion & Cleaning Safety 🧼
                </h4>
                <p className="text-[11px] leading-relaxed" style={{ color: colors.onSurfaceVariant }}>
                  Practical, respectful instructions regarding standard silicone hygiene. Great educational blueprint for modern adolescent companions.
                </p>
              </PrimaryCard>
            )}
          </div>

          {/* SEC 4: WARNINGS / SAFETY INFOS */}
          <WarningCard
            variant="urgent"
            title="Emergency Red Flags"
            description="If she experiences severe continuous localized pelvic pain, vomiting, or persistent high fever with a menstrual cup, seek prompt emergency care."
            actionLabel="View Crisis Support Contacts"
            onActionClick={() => onNavigate('crisis')}
          />

        </div>
      </AppShell>
    </div>
  );
};

export default HomePage;
