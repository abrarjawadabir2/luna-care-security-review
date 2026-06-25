import React from 'react';
import { colors } from '../../styles/theme';

interface LoadingStateProps {
  message?: string;
}

export const LoadingState: React.FC<LoadingStateProps> = ({
  message = 'Securely loading health statistics...',
}) => {
  return (
    <div className="w-full py-16 flex flex-col items-center justify-center gap-4 text-center">
      {/* Premium circular spinner with Rose accent */}
      <div 
        className="w-12 h-12 rounded-full border-4 border-t-transparent animate-spin"
        style={{ 
          borderColor: `${colors.primary}30`,
          borderTopColor: colors.primary 
        }}
      />
      <p className="text-xs font-bold font-heading animate-pulse" style={{ color: colors.primary }}>
        {message}
      </p>
    </div>
  );
};
