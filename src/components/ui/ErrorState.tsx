import React from 'react';
import { colors } from '../../styles/theme';

interface ErrorStateProps {
  title?: string;
  message: string;
  onRetry?: () => void;
}

export const ErrorState: React.FC<ErrorStateProps> = ({
  title = 'Something went wrong',
  message,
  onRetry,
}) => {
  return (
    <div className="w-full py-8 px-5 border rounded-3xl flex flex-col items-center justify-center gap-4 text-center" style={{ backgroundColor: colors.errorContainer, borderColor: `${colors.error}30` }}>
      <div 
        className="w-12 h-12 rounded-full flex items-center justify-center"
        style={{ backgroundColor: `${colors.error}15` }}
      >
        <span className="material-symbols-outlined text-[28px] font-bold" style={{ color: colors.error }}>
          gpp_maybe
        </span>
      </div>

      <div className="flex flex-col gap-1 max-w-xs">
        <h3 className="text-sm font-extrabold tracking-tight" style={{ color: colors.onSurface }}>
          {title}
        </h3>
        <p className="text-xs leading-relaxed" style={{ color: colors.error }}>
          {message}
        </p>
      </div>

      {onRetry && (
        <button
          onClick={onRetry}
          className="text-xs font-bold bg-white border px-4 py-2 rounded-full hover:bg-neutral-50 active:scale-95 transition-all duration-150"
          style={{ borderColor: colors.outlineVariant, color: colors.onSurface }}
        >
          Try Again
        </button>
      )}
    </div>
  );
};
