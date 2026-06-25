import React from 'react';
import { colors } from '../../styles/theme';

interface PrimaryButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  label: string;
  icon?: string;
  fullWidth?: boolean;
}

export const PrimaryButton: React.FC<PrimaryButtonProps> = ({
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
      className={`relative min-h-[48px] px-6 py-3 rounded-2xl flex items-center justify-center gap-2 text-sm font-extrabold tracking-wide transition-all duration-300 active:scale-95 disabled:scale-100 disabled:opacity-50 select-none ${
        fullWidth ? 'w-full' : 'w-auto'
      } ${className}`}
      disabled={disabled}
      style={{
        backgroundColor: colors.primary,
        color: '#fff8f7',
        fontFamily: 'Plus Jakarta Sans',
        boxShadow: '0 4px 10px rgba(138, 77, 78, 0.15)',
        ...style
      }}
      {...props}
    >
      {icon && <span className="material-symbols-outlined text-lg">{icon}</span>}
      <span>{label}</span>
      
      {/* Visual Ripple overlay feedback emulation */}
      <span className="absolute inset-0 rounded-2xl bg-white/0 active:bg-white/10 transition-colors pointer-events-none" />
    </button>
  );
};
