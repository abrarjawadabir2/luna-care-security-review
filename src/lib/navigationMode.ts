export type UserMode = 'SELF_TRACKING' | 'SUPPORT_MODE' | 'EDUCATION_ONLY';

export interface TabInfo {
  id: string;
  label: string;
  icon: string;
}

export const getTabsForUserMode = (mode: UserMode): TabInfo[] => {
  switch (mode) {
    case 'SELF_TRACKING':
      return [
        { id: 'home', label: 'Home', icon: 'home' },
        { id: 'cycle', label: 'Cycle', icon: 'calendar_today' },
        { id: 'mood', label: 'Mood', icon: 'mood' },
        { id: 'learn', label: 'Learn', icon: 'menu_book' },
        { id: 'journal', label: 'Journal', icon: 'edit_note' },
        { id: 'care', label: 'Care', icon: 'local_mall' },
      ];
    case 'SUPPORT_MODE':
      return [
        { id: 'home', label: 'Home', icon: 'home' },
        { id: 'support', label: 'Support', icon: 'supervised_user_circle' },
        { id: 'learn', label: 'Learn', icon: 'menu_book' },
        { id: 'care', label: 'Care', icon: 'local_mall' },
        { id: 'journal', label: 'Notes', icon: 'edit_note' },
        { id: 'settings', label: 'Settings', icon: 'settings' },
      ];
    case 'EDUCATION_ONLY':
      return [
        { id: 'home', label: 'Home', icon: 'home' },
        { id: 'learn', label: 'Learn', icon: 'menu_book' },
        { id: 'care', label: 'Care', icon: 'local_mall' },
        { id: 'mood', label: 'Mind', icon: 'psychology' },
        { id: 'settings', label: 'Settings', icon: 'settings' },
      ];
    default:
      return [
        { id: 'home', label: 'Home', icon: 'home' },
        { id: 'learn', label: 'Learn', icon: 'menu_book' },
        { id: 'care', label: 'Care', icon: 'local_mall' },
        { id: 'settings', label: 'Settings', icon: 'settings' },
      ];
  }
};
