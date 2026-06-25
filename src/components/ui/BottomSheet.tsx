import React from 'react';
import { colors } from '../../styles/theme';
import { PrimaryButton } from './PrimaryButton';

interface BottomSheetProps {
  isOpen: boolean;
  onClose: () => void;
  title: string;
  onSave?: () => void;
  saveLabel?: string;
  children: React.ReactNode;
}

export const BottomSheet: React.FC<BottomSheetProps> = ({
  isOpen,
  onClose,
  title,
  onSave,
  saveLabel = 'Save changes',
  children,
}) => {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-end justify-center overflow-hidden">
      {/* Blurred Overlay backdrop */}
      <div 
        className="absolute inset-0 bg-black/40 backdrop-blur-sm transition-opacity duration-300"
        onClick={onClose}
      />

      {/* Floating Bottom Sheet */}
      <div 
        className="relative w-full max-w-lg rounded-t-3xl shadow-gentle-lg border-t flex flex-col max-h-[85vh] animate-slide-up"
        style={{ 
          backgroundColor: '#ffffff',
          borderColor: colors.outlineVariant
        }}
      >
        {/* Top Header & Drag handles visual style */}
        <div className="w-full flex flex-col items-center pt-3 pb-4 border-b" style={{ borderColor: colors.outlineVariant }}>
          <div className="w-12 h-1.5 rounded-full bg-neutral-300 mb-3" />
          
          <div className="w-full px-5 flex items-center justify-between">
            <h3 className="text-base font-extrabold tracking-tight" style={{ color: colors.onSurface, fontFamily: 'Plus Jakarta Sans' }}>
              {title}
            </h3>
            
            <button 
              onClick={onClose}
              className="p-1 rounded-full hover:bg-neutral-100"
              style={{ color: colors.onSurfaceVariant }}
            >
              <span className="material-symbols-outlined text-xl">close</span>
            </button>
          </div>
        </div>

        {/* Scrollable interior body */}
        <div className="p-5 overflow-y-auto flex-grow flex flex-col gap-4">
          {children}
        </div>

        {/* Bottom confirmation actionable item */}
        {onSave && (
          <div className="p-4 border-t flex gap-3" style={{ borderColor: colors.outlineVariant, backgroundColor: colors.background }}>
            <PrimaryButton 
              label={saveLabel}
              onClick={onSave}
              fullWidth
            />
          </div>
        )}
      </div>
    </div>
  );
};
