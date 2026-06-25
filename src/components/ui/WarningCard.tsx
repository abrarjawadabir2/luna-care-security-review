import React from 'react';
import { colors } from '../../styles/theme';

interface WarningCardProps {
  variant: 'info' | 'care' | 'urgent';
  title: string;
  description: string;
  actionLabel?: string;
  onActionClick?: () => void;
}

export const WarningCard: React.FC<WarningCardProps> = ({
  variant,
  title,
  description,
  actionLabel,
  onActionClick,
}) => {
  const getStyling = () => {
    switch (variant) {
      case 'urgent':
        return {
          bg: colors.errorContainer,
          border: colors.error,
          text: colors.error,
          icon: 'emergency_home',
        };
      case 'care':
        return {
          bg: `${colors.tertiaryContainer}30`,
          border: colors.tertiary,
          text: colors.tertiary,
          icon: 'clinical_researching',
        };
      default: // info
        return {
          bg: `${colors.secondaryContainer}25`,
          border: colors.secondary,
          text: colors.secondary,
          icon: 'info',
        };
    }
  };

  const style = getStyling();

  return (
    <div
      className="p-4 rounded-3xl border flex gap-3.5 items-start shadow-gentle-sm animate-shake"
      style={{
        backgroundColor: style.bg,
        borderColor: `${style.border}40`,
      }}
    >
      <div 
        className="w-9 h-9 rounded-full flex items-center justify-center flex-shrink-0"
        style={{ backgroundColor: `${style.border}20` }}
      >
        <span className="material-symbols-outlined font-bold text-xl" style={{ color: style.border }}>
          {style.icon}
        </span>
      </div>

      <div className="flex-grow flex flex-col gap-1">
        <h4 className="text-sm font-bold tracking-tight" style={{ color: colors.onSurface }}>
          {title}
        </h4>
        <p className="text-[11px] leading-relaxed" style={{ color: colors.onSurfaceVariant }}>
          {description}
        </p>

        {actionLabel && onActionClick && (
          <button
            onClick={onActionClick}
            className="text-xs font-bold leading-none mt-2 self-start flex items-center gap-1.5 hover:underline focus:outline-none"
            style={{ color: style.border }}
          >
            <span>{actionLabel}</span>
            <span className="material-symbols-outlined text-sm font-bold">arrow_forward</span>
          </button>
        )}
      </div>
    </div>
  );
};
