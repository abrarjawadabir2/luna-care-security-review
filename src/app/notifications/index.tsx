import React from 'react';
import { colors } from '../../styles/theme';
import { AppShell } from '../../components/layout/AppShell';
import { PrimaryCard } from '../../components/ui/PrimaryCard';

interface NotificationsProps {
  onNavigate: (route: string) => void;
}

export const NotificationsPage: React.FC<NotificationsProps> = ({ onNavigate }) => {
  const notificationsList = [
    {
      id: 1,
      type: 'Period',
      title: 'Estimated cycle begins in 18 days 🩸',
      desc: 'Based on your average historical logging patterns. Time to carry extra cotton panties or sanitary cups.',
      time: 'Just now',
    },
    {
      id: 2,
      type: 'Medicine',
      title: 'Scheduled Prescription Dosage Reminder ⏰',
      desc: 'Take your recommended iron vitamins or paracetamol dosage checklist safely.',
      time: '1 hour ago',
    },
    {
      id: 3,
      type: 'Cup Care',
      title: 'Menstrual Cup Sterilization Sanitizing Alarm 🧼',
      desc: 'Remember to boil your silicone active cup in clean water for 5 minutes before active cycle insertions.',
      time: '3 hours ago',
    },
    {
      id: 4,
      type: 'Hydration',
      title: 'Water Intake Milestone 💧',
      desc: 'Sip 250ml water now. Reducing localized dehydration risks lowers muscle pre-menstrual cramp intensities.',
      time: 'Day-tick notification',
    },
  ];

  return (
    <AppShell>
      <div className="flex flex-col gap-5 py-4 w-full animate-fade-in">
        
        {/* Header view */}
        <div className="flex items-center justify-between px-1">
          <div className="flex flex-col">
            <h2 className="text-2xl font-extrabold" style={{ color: colors.primary, fontFamily: 'Plus Jakarta Sans' }}>
              Notifications & Alerts
            </h2>
            <p className="text-xs" style={{ color: colors.onSurfaceVariant }}>
              Secure client reminders panel
            </p>
          </div>
          <button
            onClick={() => onNavigate('home')}
            className="p-1.5 rounded-full hover:bg-neutral-100"
            style={{ color: colors.primary }}
          >
            <span className="material-symbols-outlined text-2xl">home</span>
          </button>
        </div>

        {/* SEC 1: LIST LOOP */}
        <div className="flex flex-col gap-3.5">
          {notificationsList.map((item) => (
            <PrimaryCard key={item.id} className="p-4 flex flex-col gap-2 relative overflow-hidden">
              <div className="flex justify-between items-center text-[10px] font-bold text-neutral-400">
                <span className="uppercase" style={{ color: colors.primary }}>Category: {item.type}</span>
                <span>{item.time}</span>
              </div>

              <h4 className="text-sm font-extrabold" style={{ color: colors.onSurface }}>
                {item.title}
              </h4>
              
              <p className="text-xs leading-relaxed" style={{ color: colors.onSurfaceVariant }}>
                {item.desc}
              </p>
            </PrimaryCard>
          ))}
        </div>

      </div>
    </AppShell>
  );
};

export default NotificationsPage;
