import React from 'react';
import { colors } from '../../styles/theme';

interface SecondaryButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  label: string;
  icon?: string;
  fullWidth?: boolean;
}

export const SecondaryButton: React.FC<SecondaryButtonProps> = ({
  label,
  icon,
  fullWidth = false,
  className = '',
  disabled,
  style,
  ...props
}) => {
  return (
    <button
      className={`relative min-h-[48px] px-6 py-3 rounded-2xl flex items-center justify-center gap-2 text-sm font-bold tracking-wide transition-all duration-300 active:scale-95 disabled:scale-100 disabled:opacity-50 border select-none ${
        fullWidth ? 'w-full' : 'w-auto'
      } ${className}`}
      disabled={disabled}
      style={{
        backgroundColor: colors.surfaceContainerLow,
        borderColor: colors.outlineVariant,
        color: colors.primary,
        fontFamily: 'Plus Jakarta Sans',
        ...style
      }}
      {...props}
    >
      {icon && <span className="material-symbols-outlined text-lg">{icon}</span>}
      <span>{label}</span>
      <span className="absolute inset-0 rounded-2xl bg-black/0 active:bg-black/5 transition-colors pointer-events-none" />
    </button>
  );
};
