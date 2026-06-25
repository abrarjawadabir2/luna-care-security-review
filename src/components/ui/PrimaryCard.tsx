import React from 'react';
import { colors } from '../../styles/theme';

interface PrimaryCardProps {
  children: React.ReactNode;
  variant?: 'normal' | 'container' | 'low' | 'high' | 'highest';
  className?: string;
  onClick?: () => void;
}

export const PrimaryCard: React.FC<PrimaryCardProps> = ({
  children,
  variant = 'normal',
  className = '',
  onClick,
}) => {
  const getBgColor = () => {
    switch (variant) {
      case 'container': return colors.surfaceContainer;
      case 'low': return colors.surfaceContainerLow;
      case 'high': return colors.surfaceContainerHigh;
      case 'highest': return colors.surfaceContainerHighest;
      default: return '#ffffff'; // High-contrast crisp card white
    }
  };

  return (
    <div
      onClick={onClick}
      className={`rounded-3xl p-5 border transition-all duration-300 shadow-gentle-sm ${
        onClick ? 'cursor-pointer hover:shadow-gentle-md hover:scale-[1.01] active:scale-[0.99]' : ''
      } ${className}`}
      style={{
        backgroundColor: getBgColor(),
        borderColor: colors.outlineVariant,
      }}
    >
      {children}
    </div>
  );
};
