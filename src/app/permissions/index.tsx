import React from 'react';
import { colors } from '../../styles/theme';
import { AppShell } from '../../components/layout/AppShell';
import { PrimaryCard } from '../../components/ui/PrimaryCard';

interface PermissionsProps {
  onNavigate: (route: string) => void;
}

export const PermissionsPage: React.FC<PermissionsProps> = ({ onNavigate }) => {
  const permissionsList = [
    {
      title: 'Geographic Search Location 📍',
      purpose: 'Finding local dispensaries/pharmacies and clinics.',
      dataCollected: 'Approximate coordinates during search query.',
      retention: 'Momentary memory. Cleared immediately, never sent to remote engines.',
    },
    {
      title: 'Camera Access 📸',
      purpose: 'Adding prescription photo receipts securely on private clinical logs.',
      dataCollected: 'Raw receipt snapshots stored locally on phone hardware memory.',
      retention: 'Strictly on-device. Hides entirely from cloud backups.',
    },
    {
      title: 'Notification Subsystem ⏰',
      purpose: 'Reminding clean cup cycles or medicine dosages on specific daily ticks.',
      dataCollected: 'Local timing triggers.',
      retention: 'Client side background alerts with no remote storage.',
    },
  ];

  return (
    <AppShell>
      <div className="flex flex-col gap-5 py-4 w-full animate-fade-in">
        
        {/* Header view */}
        <div className="flex items-center justify-between px-1">
          <div className="flex flex-col">
            <h2 className="text-2xl font-extrabold" style={{ color: colors.primary, fontFamily: 'Plus Jakarta Sans' }}>
              Permission Center
            </h2>
            <p className="text-xs" style={{ color: colors.onSurfaceVariant }}>
              Transparent device integration details
            </p>
          </div>
          <button
            onClick={() => onNavigate('home')}
            className="p-1.5 rounded-full hover:bg-neutral-100"
            style={{ color: colors.primary }}
          >
            <span className="material-symbols-outlined text-2xl">home</span>
          </button>
        </div>

        {/* SEC 1: MAIN SECURITY SUMMARY */}
        <PrimaryCard variant="container" className="flex flex-col gap-2">
          <h3 className="text-sm font-extrabold flex items-center gap-1.5" style={{ color: colors.primary }}>
            <span className="material-symbols-outlined text-base">verified_user</span>
            Sovereign Trust Manifesto
          </h3>
          <p className="text-xs leading-relaxed" style={{ color: colors.onSurfaceVariant }}>
            LunaCare respects client sovereignty. Under no circumstance does this health app sell logged indices, body cycles data or mood journals. You configure exact operational permission boundaries.
          </p>
        </PrimaryCard>

        {/* SEC 2: CARDS DETAILED EXPLANATION */}
        <div className="flex flex-col gap-4">
          {permissionsList.map((perm, index) => (
            <PrimaryCard key={index} className="flex flex-col gap-2">
              <h4 className="text-sm font-extrabold" style={{ color: colors.onSurface }}>
                {perm.title}
              </h4>
              
              <div className="flex flex-col gap-1 text-[11px] leading-relaxed">
                <p>💡 <span className="font-bold text-neutral-600">Why needed:</span> {perm.purpose}</p>
                <p>📦 <span className="font-bold text-neutral-600">Collected Content:</span> {perm.dataCollected}</p>
                <p>🔒 <span className="font-bold text-neutral-600">Data Lifespan:</span> <span className="font-semibold text-green-700">{perm.retention}</span></p>
              </div>
            </PrimaryCard>
          ))}
        </div>

      </div>
    </AppShell>
  );
};

export default PermissionsPage;
