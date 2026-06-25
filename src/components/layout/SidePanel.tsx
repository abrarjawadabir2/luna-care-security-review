import React from 'react';
import { colors } from '../../styles/theme';

interface SidePanelProps {
  isOpen: boolean;
  onClose: () => void;
  userRole?: string;
  onNavigate: (routeId: string) => void;
}

export const SidePanel: React.FC<SidePanelProps> = ({
  isOpen,
  onClose,
  userRole = 'user',
  onNavigate,
}) => {
  if (!isOpen) return null;

  const menuItems = [
    { id: 'profile', label: 'User Profile 👤', icon: 'person' },
    { id: 'permissions', label: 'Permission Center 🔏', icon: 'security' },
    { id: 'bookmarks', label: 'My Bookmarks 🔖', icon: 'bookmarks' },
    { id: 'medical-journal', label: 'Medical Journal 🩺', icon: 'medical_services' },
    { id: 'reminders', label: 'Medicine Reminders ⏰', icon: 'notifications_active' },
    { id: 'awareness', label: 'Health Awareness 📖', icon: 'auto_stories' },
    { id: 'ai-assistant', label: 'Luna AI Assistant ✨', icon: 'psychology' },
    { id: 'subscription', label: 'Premium Subscription 👑', icon: 'workspace_premium' },
    { id: 'theme-panel', label: 'Theme & Colors 🎨', icon: 'palette' },
    { id: 'old-home', label: 'Return to Old Home 🏡', icon: 'arrow_back' },
    { id: 'logout', label: 'Logout Securely 🚪', icon: 'logout', danger: true },
  ];

  return (
    <div className="fixed inset-0 z-50 flex overflow-hidden">
      {/* Backdrop overlay */}
      <div 
        className="absolute inset-0 bg-black/40 backdrop-blur-sm transition-opacity duration-300" 
        onClick={onClose}
      />

      {/* Panel container */}
      <div 
        className="relative flex flex-col w-[82%] max-w-sm h-full shadow-2xl transition-transform duration-300 transform translate-x-0"
        style={{ backgroundColor: colors.background }}
      >
        {/* Top Header Block */}
        <div 
          className="p-6 border-b flex flex-col gap-2"
          style={{ borderColor: colors.outlineVariant }}
        >
          <div className="flex items-center justify-between w-full">
            <h2 className="text-xl font-extrabold tracking-tight" style={{ color: colors.primary, fontFamily: 'Plus Jakarta Sans' }}>
              LunaCare Menu
            </h2>
            <button 
              onClick={onClose}
              className="p-1 rounded-full hover:bg-neutral-100"
              style={{ color: colors.onSurfaceVariant }}
            >
              <span className="material-symbols-outlined text-2xl">close</span>
            </button>
          </div>
          
          <div className="flex items-center gap-3 mt-2">
            <div 
              className="w-10 h-10 rounded-full flex items-center justify-center text-sm font-bold shadow-sm"
              style={{ backgroundColor: colors.primaryContainer, color: colors.onSurface }}
            >
              A
            </div>
            <div>
              <p className="text-sm font-bold" style={{ color: colors.onSurface }}>Abir</p>
              <p className="text-[10px] uppercase font-bold tracking-wider" style={{ color: colors.primary }}>
                Role: {userRole}
              </p>
            </div>
          </div>
        </div>

        {/* Scrolling Menu list */}
        <div className="flex-grow overflow-y-auto px-4 py-3 flex flex-col gap-1.5">
          {menuItems.map((item) => (
            <button
              key={item.id}
              onClick={() => {
                onNavigate(item.id);
                onClose();
              }}
              className="flex items-center gap-4 w-full px-4 py-3 rounded-2xl text-left text-sm font-semibold transition-all duration-200 hover:bg-white/50 focus:ring-2 focus:ring-[#8a4d4e]/30"
              style={{ 
                color: item.danger ? colors.error : colors.onSurface,
              }}
            >
              <span className="material-symbols-outlined text-xl">{item.icon}</span>
              <span>{item.label}</span>
            </button>
          ))}
        </div>

        {/* Bottom privacy footer */}
        <div className="p-4 border-t text-center text-[10px]" style={{ borderColor: colors.outlineVariant, color: colors.onSurfaceVariant }}>
          <p>© 2026 LunaCare. Privacy first medical platform.</p>
          <p className="mt-1 font-bold">End-to-End Client Side Encrypted 🔒</p>
        </div>
      </div>
    </div>
  );
};
