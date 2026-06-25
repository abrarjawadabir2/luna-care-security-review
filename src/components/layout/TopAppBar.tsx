import React from 'react';
import { colors } from '../../styles/theme';

interface TopAppBarProps {
  title?: string;
  userName?: string;
  onMenuClick?: () => void;
  onNotificationsClick?: () => void;
  onSettingsClick?: () => void;
}

export const TopAppBar: React.FC<TopAppBarProps> = ({
  title = 'LunaCare',
  userName = 'Abir',
  onMenuClick,
  onNotificationsClick,
  onSettingsClick,
}) => {
  return (
    <header 
      className="sticky top-0 z-40 w-full flex items-center justify-between px-4 py-3 backdrop-blur-lg bg-opacity-90 border-b transition-all duration-300"
      style={{ 
        backgroundColor: `${colors.background}EE`,
        borderColor: colors.outlineVariant,
      }}
    >
      <div className="flex items-center gap-3">
        <button 
          onClick={onMenuClick}
          className="p-2 rounded-full hover:bg-neutral-100 transition-colors focus:ring-2 focus:ring-[#8a4d4e]/50"
          style={{ color: colors.primary }}
          aria-label="Open menu"
        >
          <span className="material-symbols-outlined font-semibold text-2xl">menu</span>
        </button>
        
        <div className="flex flex-col">
          <h1 
            className="text-lg font-bold tracking-tight"
            style={{ 
              color: colors.primary,
              fontFamily: 'Plus Jakarta Sans',
            }}
          >
            {title}
          </h1>
          <p className="text-[11px]" style={{ color: colors.onSurfaceVariant }}>
            Welcome, <span className="font-semibold">{userName}</span> 🕊️
          </p>
        </div>
      </div>

      <div className="flex items-center gap-2">
        <button 
          onClick={onNotificationsClick}
          className="relative p-2 rounded-full hover:bg-neutral-100 transition-colors focus:ring-2 focus:ring-[#8a4d4e]/50"
          style={{ color: colors.secondary }}
          aria-label="View notifications"
        >
          <span className="material-symbols-outlined text-2xl">notifications</span>
          <span 
            className="absolute top-1 right-1 w-2.5 h-2.5 rounded-full border border-white"
            style={{ backgroundColor: colors.error }}
          />
        </button>

        <button 
          onClick={onSettingsClick}
          className="p-2 rounded-full hover:bg-neutral-100 transition-colors focus:ring-2 focus:ring-[#8a4d4e]/50"
          style={{ color: colors.secondary }}
          aria-label="View settings"
        >
          <span className="material-symbols-outlined text-2xl">settings</span>
        </button>
      </div>
    </header>
  );
};
