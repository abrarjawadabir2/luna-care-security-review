import React from 'react';
import { colors } from '../../styles/theme';

interface OptionCardProps {
  title: string;
  description?: string;
  icon?: string;
  isSelected: boolean;
  onClick: () => void;
}

export const OptionCard: React.FC<OptionCardProps> = ({
  title,
  description,
  icon,
  isSelected,
  onClick,
}) => {
  return (
    <button
      onClick={onClick}
      className="w-full text-left p-5 rounded-3xl border transition-all duration-300 flex items-center justify-between shadow-gentle-sm focus:outline-none focus:ring-2 focus:ring-[#8a4d4e]/40"
      style={{
        backgroundColor: isSelected ? colors.surfaceContainer : '#ffffff',
        borderColor: isSelected ? colors.primary : colors.outlineVariant,
        transform: isSelected ? 'scale(1.02)' : 'none',
        boxShadow: isSelected ? '0 8px 16px rgba(138, 77, 78, 0.08)' : '0 1px 2px rgba(138, 77, 78, 0.03)'
      }}
    >
      <div className="flex gap-4 items-start pr-4">
        {icon && (
          <div 
            className="w-10 h-10 rounded-full flex items-center justify-center flex-shrink-0"
            style={{ 
              backgroundColor: isSelected ? '#ffffff' : colors.surfaceContainerLow,
            }}
          >
            <span className="material-symbols-outlined text-xl" style={{ color: isSelected ? colors.primary : colors.secondary }}>
              {icon}
            </span>
          </div>
        )}

        <div className="flex flex-col gap-0.5">
          <h4 className="text-sm font-extrabold tracking-tight" style={{ color: colors.onSurface }}>
            {title}
          </h4>
          {description && (
            <p className="text-[11px] leading-relaxed" style={{ color: colors.onSurfaceVariant }}>
              {description}
            </p>
          )}
        </div>
      </div>

      {/* Circle checkbox display icon */}
      <div 
        className="w-5 h-5 rounded-full border-2 flex items-center justify-center flex-shrink-0"
        style={{ 
          borderColor: isSelected ? colors.primary : colors.outlineVariant,
          backgroundColor: isSelected ? colors.primary : 'transparent',
        }}
      >
        {isSelected && (
          <span className="material-symbols-outlined text-sm font-bold text-white leading-none">check</span>
        )}
      </div>
    </button>
  );
};
