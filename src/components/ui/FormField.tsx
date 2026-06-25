import React from 'react';
import { colors } from '../../styles/theme';

interface FormFieldProps extends React.InputHTMLAttributes<HTMLInputElement> {
  label: string;
  helperText?: string;
  error?: string;
  icon?: string;
}

export const FormField: React.FC<FormFieldProps> = ({
  label,
  helperText,
  error,
  icon,
  className = '',
  id,
  type = 'text',
  ...props
}) => {
  return (
    <div className={`flex flex-col gap-1.5 w-full ${className}`}>
      <label 
        htmlFor={id}
        className="text-xs font-extrabold tracking-wide uppercase px-1"
        style={{ color: colors.onSurfaceVariant }}
      >
        {label}
      </label>

      <div className="relative w-full">
        {icon && (
          <span 
            className="material-symbols-outlined absolute left-4 top-1/2 -translate-y-1/2 text-xl pointer-events-none"
            style={{ color: error ? colors.error : colors.onSurfaceVariant }}
          >
            {icon}
          </span>
        )}

        <input
          id={id}
          type={type}
          className={`w-full min-h-[52px] rounded-2xl px-5 border text-sm font-semibold transition-all duration-300 focus:outline-none focus:ring-2 ${
            icon ? 'pl-11' : ''
          }`}
          style={{
            backgroundColor: '#ffffff',
            borderColor: error ? colors.error : colors.outlineVariant,
            color: colors.onSurface,
            boxShadow: '0 1px 2px rgba(138, 77, 78, 0.02)'
          }}
          {...props}
        />
      </div>

      {error ? (
        <p className="text-xs font-bold px-1" style={{ color: colors.error }}>
          ⚠️ {error}
        </p>
      ) : helperText ? (
        <p className="text-[10px] px-1 leading-relaxed" style={{ color: colors.onSurfaceVariant }}>
          {helperText}
        </p>
      ) : null}
    </div>
  );
};
