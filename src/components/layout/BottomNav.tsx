import React from 'react';
import { colors } from '../../styles/theme';
import { getTabsForUserMode, UserMode } from '../../lib/navigationMode';

interface BottomNavProps {
  currentTab: string;
  onTabChange: (tabId: string) => void;
  userMode?: UserMode;
}

export const BottomNav: React.FC<BottomNavProps> = ({
  currentTab,
  onTabChange,
  userMode = 'SELF_TRACKING',
}) => {
  const tabs = getTabsForUserMode(userMode);

  return (
    <nav 
      className="fixed bottom-4 left-1/2 -translate-x-1/2 z-50 w-[92%] max-w-md px-3 py-2.5 rounded-3xl shadow-gentle-lg backdrop-blur-xl border flex items-center justify-around transition-all duration-300"
      style={{ 
        backgroundColor: `rgba(248, 235, 234, 0.85)`,
        borderColor: colors.outlineVariant
      }}
    >
      {tabs.map((tab) => {
        const isActive = currentTab === tab.id;
        return (
          <button
            key={tab.id}
            onClick={() => onTabChange(tab.id)}
            className="flex flex-col items-center justify-center flex-1 py-1 relative focus:outline-none"
            aria-label={tab.label}
          >
            {/* Active Highlight Pill */}
            {isActive && (
              <div 
                className="absolute top-0 w-10 h-1 rounded-full"
                style={{ backgroundColor: colors.primary }}
              />
            )}

            <span 
              className={`material-symbols-outlined text-2xl transition-all duration-300 ${isActive ? 'scale-110 font-bold' : ''}`}
              style={{ 
                color: isActive ? colors.primary : colors.onSurfaceVariant 
              }}
            >
              {tab.icon}
            </span>

            <span 
              className="text-[9px] mt-0.5 font-medium transition-all duration-300"
              style={{ 
                color: isActive ? colors.primary : colors.onSurfaceVariant,
                fontWeight: isActive ? 700 : 500
              }}
            >
              {tab.label}
            </span>
          </button>
        );
      })}
    </nav>
  );
};
