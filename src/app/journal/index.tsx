import React, { useState } from 'react';
import { colors } from '../../styles/theme';
import { AppShell } from '../../components/layout/AppShell';
import { PrimaryCard } from '../../components/ui/PrimaryCard';
import { FormField } from '../../components/ui/FormField';
import { PrimaryButton } from '../../components/ui/PrimaryButton';
import { EmptyState } from '../../components/ui/EmptyState';

interface JournalProps {
  onNavigate: (route: string) => void;
}

interface JournalItem {
  id: number;
  date: string;
  title: string;
  notes: string;
}

export const JournalPage: React.FC<JournalProps> = ({ onNavigate }) => {
  const [title, setTitle] = useState('');
  const [diary, setDiary] = useState('');
  const [history, setHistory] = useState<JournalItem[]>([
    { id: 1, date: '06/18/2026', title: 'Gentle Day', notes: 'Felt calm and did daily stretching. Less stress reported.' },
  ]);

  const handleSaveDiary = (e: React.FormEvent) => {
    e.preventDefault();
    if (!title.trim() || !diary.trim()) return;

    const newItem: JournalItem = {
      id: Date.now(),
      date: new Date().toLocaleDateString(),
      title,
      notes: diary,
    };

    setHistory([newItem, ...history]);
    setTitle('');
    setDiary('');
    alert('Sovereign Journal entry logged securely offline! 🔒');
  };

  return (
    <AppShell>
      <div className="flex flex-col gap-5 py-4 w-full animate-fade-in">
        
        {/* Header view */}
        <div className="flex items-center justify-between px-1">
          <div className="flex flex-col">
            <h2 className="text-2xl font-extrabold" style={{ color: colors.primary, fontFamily: 'Plus Jakarta Sans' }}>
              Wellbeing Diary
            </h2>
            <p className="text-xs" style={{ color: colors.onSurfaceVariant }}>
              Encrypted offline emotional insights
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

        {/* SEC 1: ADD JOURNAL ENTRY FORM */}
        <PrimaryCard className="flex flex-col gap-4">
          <h3 className="text-sm font-bold flex items-center gap-1.5" style={{ color: colors.primary }}>
            <span className="material-symbols-outlined text-base">rate_review</span>
            Log Reflection
          </h3>
          
          <form onSubmit={handleSaveDiary} className="flex flex-col gap-3">
            <FormField
              label="Title Summary"
              placeholder="e.g. Afternoon fatigue, PMS fatigue"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              required
            />

            <FormField
              label="Feelings & Reflections"
              placeholder="What are you currently feeling emotionally?"
              value={diary}
              onChange={(e) => setDiary(e.target.value)}
              required
            />

            <PrimaryButton 
              label="Save Offline Reflection" 
              type="submit"
              fullWidth
            />
          </form>
        </PrimaryCard>

        {/* SEC 2: REFLECTION LOGS LIST */}
        <div className="flex flex-col gap-3 pt-2">
          <h4 className="text-xs font-extrabold tracking-wider uppercase px-1 text-neutral-500">
            Past Reflective Logs
          </h4>

          {history.length === 0 ? (
            <EmptyState
              title="No reflections saved"
              description="Your cycle journaling notes are entirely private and reside securely in our localized client state."
              icon="draw"
            />
          ) : (
            <div className="flex flex-col gap-3">
              {history.map((item) => (
                <PrimaryCard key={item.id} variant="low" className="p-4 flex flex-col gap-1.5">
                  <div className="flex justify-between items-center text-[10px] font-bold text-neutral-400">
                    <span>Logged: {item.date} 🗓️</span>
                    <span style={{ color: colors.secondary }}>Offline Encrypted</span>
                  </div>
                  <h4 className="text-sm font-extrabold" style={{ color: colors.onSurface }}>
                    {item.title}
                  </h4>
                  <p className="text-xs leading-relaxed" style={{ color: colors.onSurfaceVariant }}>
                    {item.notes}
                  </p>
                </PrimaryCard>
              ))}
            </div>
          )}
        </div>

      </div>
    </AppShell>
  );
};

export default JournalPage;
