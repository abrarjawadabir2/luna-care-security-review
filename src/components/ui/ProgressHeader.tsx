import React from 'react';
import { colors } from '../../styles/theme';

interface ProgressHeaderProps {
  currentStep: number;
  totalSteps: number;
  onBack?: () => void;
}

export const ProgressHeader: React.FC<ProgressHeaderProps> = ({
  currentStep,
  totalSteps,
  onBack,
}) => {
  const percentage = Math.round((currentStep / totalSteps) * 100);

  return (
    <div className="w-full flex flex-col gap-3 py-2">
      <div className="flex items-center justify-between w-full">
        {onBack ? (
          <button 
            onClick={onBack}
            className="flex items-center gap-1 text-xs font-bold focus:outline-none hover:opacity-85"
            style={{ color: colors.primary }}
          >
            <span className="material-symbols-outlined text-sm font-bold">arrow_back</span>
            <span>Back</span>
          </button>
        ) : (
          <div className="w-8" />
        )}

        <span className="text-xs font-bold tracking-wider" style={{ color: colors.onSurfaceVariant }}>
          Step {currentStep} of {totalSteps}
        </span>

        <span className="text-xs font-bold" style={{ color: colors.primary }}>
          {percentage}%
        </span>
      </div>

      {/* Outer slider bar */}
      <div className="w-full h-2 rounded-full overflow-hidden bg-neutral-200">
        <div 
          className="h-full rounded-full transition-all duration-500 ease-out"
          style={{ 
            backgroundColor: colors.primary,
            width: `${percentage}%`
          }}
        />
      </div>
    </div>
  );
};
