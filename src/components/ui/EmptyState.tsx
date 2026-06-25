import React from 'react';
import { colors } from '../../styles/theme';

interface EmptyStateProps {
  title: string;
  description: string;
  icon?: string;
  actionLabel?: string;
  onActionClick?: () => void;
}

export const EmptyState: React.FC<EmptyStateProps> = ({
  title,
  description,
  icon = 'database_off',
  actionLabel,
  onActionClick,
}) => {
  return (
    <div className="w-full py-12 px-6 flex flex-col items-center text-center justify-center gap-4 bg-white/40 backdrop-blur-sm border rounded-3xl border-dashed border-[#857372]/30">
      <div 
        className="w-16 h-16 rounded-full flex items-center justify-center"
        style={{ backgroundColor: `${colors.secondaryContainer}30` }}
      >
        <span className="material-symbols-outlined text-[36px]" style={{ color: colors.secondary }}>
          {icon}
        </span>
      </div>

      <div className="flex flex-col gap-1.5 max-w-xs">
        <h3 className="text-base font-extrabold tracking-tight" style={{ color: colors.onSurface }}>
          {title}
        </h3>
        <p className="text-xs leading-relaxed" style={{ color: colors.onSurfaceVariant }}>
          {description}
        </p>
      </div>

      {actionLabel && onActionClick && (
        <button
          onClick={onActionClick}
          className="text-xs font-extrabold px-5 py-2.5 rounded-full text-white active:scale-95 transition-all duration-200"
          style={{ backgroundColor: colors.primary }}
        >
          {actionLabel}
        </button>
      )}
    </div>
  );
};
