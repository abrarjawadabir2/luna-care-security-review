import React, { useState } from 'react';
import { colors } from '../../styles/theme';
import { AppShell } from '../../components/layout/AppShell';
import { PrimaryCard } from '../../components/ui/PrimaryCard';
import { FormField } from '../../components/ui/FormField';
import { PrimaryButton } from '../../components/ui/PrimaryButton';
import { ChipSelector } from '../../components/ui/ChipSelector';

interface SettingsProps {
  onNavigate: (route: string) => void;
  onLogout: () => void;
}

export const SettingsPage: React.FC<SettingsProps> = ({ onNavigate, onLogout }) => {
  const [name, setName] = useState('Abir');
  const [pronouns, setPronouns] = useState<string[]>(['she/her']);
  const [region, setRegion] = useState<string[]>(['DHAKA']);
  const [religion, setReligion] = useState('');
  const [activeTheme, setActiveTheme] = useState('Luna Rose');

  const handleSaveProfile = () => {
    alert('Sovereign Settings updated securely locally! 🔒');
  };

  const handleExportData = () => {
    alert('Sovereign Export package ready for offline download: LunaCare-Private-Logs.json');
  };

  return (
    <AppShell>
      <div className="flex flex-col gap-5 py-4 w-full animate-fade-in">
        
        {/* Header view */}
        <div className="flex items-center justify-between px-1">
          <div className="flex flex-col">
            <h2 className="text-2xl font-extrabold" style={{ color: colors.primary, fontFamily: 'Plus Jakarta Sans' }}>
              App Preferences
            </h2>
            <p className="text-xs" style={{ color: colors.onSurfaceVariant }}>
              Configure your private parameters
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

        {/* SEC 1: PROFILE CARDS */}
        <PrimaryCard className="flex flex-col gap-4">
          <h3 className="text-sm font-bold flex items-center gap-1.5" style={{ color: colors.primary }}>
            <span className="material-symbols-outlined text-base">manage_accounts</span>
            User Profile Settings
          </h3>

          <FormField
            label="Friendly Name"
            value={name}
            onChange={(e) => setName(e.target.value)}
          />

          <ChipSelector
            label="Preferred Pronouns"
            singleSelect
            options={[
              { id: 'she/her', label: 'She / Her' },
              { id: 'they/them', label: 'They / Them' },
              { id: 'he/him', label: 'He / Him' },
              { id: 'none', label: 'Omit Pronouns 🔒' },
            ]}
            selectedIds={pronouns}
            onChange={setPronouns}
          />

          <ChipSelector
            label="Home Region"
            singleSelect
            options={[
              { id: 'DHAKA', label: 'Dhaka Met' },
              { id: 'CHITTAGONG', label: 'Chittagong' },
              { id: 'OTHER', label: 'Approximate region' },
            ]}
            selectedIds={region}
            onChange={setRegion}
          />

          <FormField
            label="Religious background (Optional / strictly offline)"
            placeholder="e.g. Islam, Hindu, Buddhist..."
            value={religion}
            onChange={(e) => setReligion(e.target.value)}
            helperText="We only utilize this to customize standard educational calendars."
          />

          <PrimaryButton 
            label="Save Private Changes" 
            onClick={handleSaveProfile}
            fullWidth
          />
        </PrimaryCard>

        {/* SEC 2: ACCESS PATTERNS THEME EDITOR */}
        <PrimaryCard variant="container" className="flex flex-col gap-3">
          <h3 className="text-sm font-bold flex items-center gap-1.5" style={{ color: colors.primary }}>
            <span className="material-symbols-outlined text-base">palette</span>
            Theme Panel & Schemes
          </h3>

          <ChipSelector
            options={[
              { id: 'Luna Rose', label: 'Luna Rose 🌸' },
              { id: 'Lavender Calm', label: 'Lavender Calm 🔮' },
              { id: 'Warm Neutral', label: 'Warm Neutral ☕' },
              { id: 'Soft Coral', label: 'Soft Coral 🍊' },
              { id: 'Dark Theme', label: 'Plum Dark Mode 🌙' },
            ]}
            selectedIds={[activeTheme]}
            onChange={(ids) => setActiveTheme(ids[0] || 'Luna Rose')}
            singleSelect
          />
        </PrimaryCard>

        {/* SEC 3: DATA SOVEREIGNTY AND ACCOUNT UTILITIES */}
        <PrimaryCard className="flex flex-col gap-3 border-dashed border-rose-200">
          <h3 className="text-sm font-bold text-rose-800 flex items-center gap-1.5">
            <span className="material-symbols-outlined text-base text-rose-500">health_and_safety</span>
            Data Sovereign Privacy Center
          </h3>
          <p className="text-[11px] leading-relaxed text-neutral-500">
            Export a full encrypted backup pack, or erase your device clinical data logs permanently. Doing so clears all database keys.
          </p>

          <div className="grid grid-cols-2 gap-3 mt-1">
            <button
              onClick={handleExportData}
              className="py-2.5 px-3 rounded-xl text-xs font-bold border hover:bg-neutral-50"
              style={{ color: colors.primary, borderColor: colors.outlineVariant }}
            >
              Export Local Data 📂
            </button>
            <button
              onClick={() => {
                const conf = window.confirm("Security Clear: Are you absolutely sure you want to wipe all local logs?");
                if (conf) {
                  alert("Local Database wiped successfully!");
                }
              }}
              className="py-2.5 px-3 rounded-xl text-xs font-bold bg-rose-50 text-rose-700 hover:bg-rose-100/60"
            >
              Wipe Device memory 🧼
            </button>
          </div>
        </PrimaryCard>

        {/* SEC 4: LOGOUT BUTTON */}
        <button
          onClick={() => {
            onLogout();
            onNavigate('index');
          }}
          className="w-full min-h-[48px] py-3 text-sm font-extrabold text-white rounded-2xl flex items-center justify-center gap-2 shadow-sm focus:outline-none"
          style={{ backgroundColor: colors.primary }}
        >
          <span className="material-symbols-outlined text-lg">logout</span>
          <span>Logout Private Session 🚪</span>
        </button>

      </div>
    </AppShell>
  );
};

export default SettingsPage;
