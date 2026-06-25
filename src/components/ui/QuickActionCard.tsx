import React from 'react';
import { colors } from '../../styles/theme';

interface QuickActionCardProps {
  label: string;
  icon: string;
  onClick: () => void;
  accentColor?: string;
}

export const QuickActionCard: React.FC<QuickActionCardProps> = ({
  label,
  icon,
  onClick,
  accentColor,
}) => {
  const colorToken = accentColor || colors.primary;

  return (
    <button
      onClick={onClick}
      className="flex flex-col items-center justify-center p-4 rounded-2xl border text-center transition-all duration-300 hover:shadow-gentle-md active:scale-95 w-full bg-white/60 backdrop-blur-sm"
      style={{ 
        borderColor: colors.outlineVariant,
      }}
    >
      <div 
        className="w-12 h-12 rounded-full flex items-center justify-center mb-2 shadow-sm"
        style={{ backgroundColor: `${colorToken}20` }}
      >
        <span className="material-symbols-outlined text-2xl font-semibold" style={{ color: colorToken }}>
          {icon}
        </span>
      </div>
      <span 
        className="text-[12px] font-bold tracking-tight"
        style={{ color: colors.onSurface }}
      >
        {label}
      </span>
    </button>
  );
};
