import React from 'react';
import { colors } from '../../styles/theme';

interface AppShellProps {
  children: React.ReactNode;
  userMode?: string;
  hideBottomNav?: boolean;
}

export const AppShell: React.FC<AppShellProps> = ({ 
  children, 
  userMode = 'SELF_TRACKING',
  hideBottomNav = false 
}) => {
  return (
    <div 
      className="min-h-screen w-full flex flex-col relative overflow-x-hidden antialiased"
      style={{ 
        backgroundColor: colors.background,
        fontFamily: 'Manrope, sans-serif'
      }}
    >
      {/* Maximum width container for perfect responsive tablet/mobile scaling */}
      <div className="w-full max-w-lg mx-auto flex flex-col flex-grow min-h-screen relative shadow-gentle-lg bg-white/30 backdrop-blur-md">
        
        {/* Main scrollable view area */}
        <main className={`flex-grow flex flex-col w-full ${hideBottomNav ? 'pb-6' : 'pb-24'} px-4 pt-4`}>
          <div className="animate-fade-in w-full h-full flex flex-col">
            {children}
          </div>
        </main>
        
      </div>
    </div>
  );
};
