import React, { useState } from 'react';
import { colors } from '../../styles/theme';
import { AppShell } from '../../components/layout/AppShell';
import { PrimaryCard } from '../../components/ui/PrimaryCard';
import { BottomSheet } from '../../components/ui/BottomSheet';
import { ChipSelector } from '../../components/ui/ChipSelector';
import { FormField } from '../../components/ui/FormField';

interface CycleProps {
  onNavigate: (route: string) => void;
}

export const CyclePage: React.FC<CycleProps> = ({ onNavigate }) => {
  const [isLogOpen, setIsLogOpen] = useState(false);
  const [noTracking, setNoTracking] = useState(false);
  const [flowIntensity, setFlowIntensity] = useState<string[]>(['medium']);
  const [selectedSymptoms, setSelectedSymptoms] = useState<string[]>(['cramps']);
  const [painLevel, setPainLevel] = useState<number>(3);
  const [logNotes, setLogNotes] = useState('');

  // Local calendar mock list
  const currentMonth = 'June 2026';
  const calendarDays = Array.from({ length: 30 }, (_, i) => i + 1);
  const periodDays = [1, 2, 3, 4, 5];
  const predictedPeriodDays = [28, 29, 30];
  const todayDay = 22;

  const handleSaveLog = () => {
    console.log({
      noTracking,
      flowIntensity,
      selectedSymptoms,
      painLevel,
      logNotes,
    });
    setIsLogOpen(false);
  };

  return (
    <AppShell>
      <div className="flex flex-col gap-5 py-4 w-full">
        
        {/* Dynamic header */}
        <div className="flex items-center justify-between px-1">
          <div className="flex flex-col">
            <h2 className="text-2xl font-extrabold" style={{ color: colors.primary, fontFamily: 'Plus Jakarta Sans' }}>
              Cycle Tracker Calendar
            </h2>
            <p className="text-xs" style={{ color: colors.onSurfaceVariant }}>
              Secure local database logging
            </p>
          </div>
          <button
            onClick={() => onNavigate('home')}
            className="p-1.5 rounded-full hover:bg-neutral-100/80"
            style={{ color: colors.primary }}
          >
            <span className="material-symbols-outlined text-2xl">home</span>
          </button>
        </div>

        {/* SEC 1: CALENDAR VIEW */}
        <PrimaryCard className="flex flex-col gap-4">
          <div className="flex justify-between items-center px-1">
            <span className="text-sm font-bold" style={{ color: colors.onSurface }}>
              {currentMonth} 🗓️
            </span>
            <div className="flex gap-2">
              <button className="p-1 text-sm font-bold border rounded-full hover:bg-neutral-50 px-2.5">Prev</button>
              <button className="p-1 text-sm font-bold border rounded-full hover:bg-neutral-50 px-2.5">Next</button>
            </div>
          </div>

          <div className="grid grid-cols-7 gap-y-3 gap-x-1.5 text-center text-[10px] font-extrabold uppercase py-1 border-b" style={{ borderColor: colors.outlineVariant, color: colors.onSurfaceVariant }}>
            <span>Su</span><span>Mo</span><span>Tu</span><span>We</span><span>Th</span><span>Fr</span><span>Sa</span>
          </div>

          <div className="grid grid-cols-7 gap-y-2 gap-x-1 justify-items-center">
            {calendarDays.map((day) => {
              const isPeriod = periodDays.includes(day);
              const isPredicted = predictedPeriodDays.includes(day);
              const isToday = day === todayDay;

              return (
                <div
                  key={day}
                  className={`w-8 h-8 rounded-full flex items-center justify-center text-xs font-bold relative transition-all duration-300 ${
                    isPeriod ? 'text-white' : isPredicted ? 'text-rose-500' : 'text-neutral-700'
                  }`}
                  style={{
                    backgroundColor: isPeriod 
                      ? colors.primary 
                      : isPredicted 
                        ? `${colors.primaryContainer}50` 
                        : 'transparent',
                    border: isToday ? `2px solid ${colors.secondary}` : 'none',
                  }}
                >
                  {day}
                  {isToday && (
                    <span 
                      className="absolute bottom-[-4px] w-1.5 h-1.5 rounded-full" 
                      style={{ backgroundColor: colors.secondary }}
                    />
                  )}
                </div>
              );
            })}
          </div>

          {/* Color Indicators Legend */}
          <div className="flex flex-wrap gap-4 justify-center text-[10px] font-bold mt-2 pt-3 border-t" style={{ borderColor: colors.outlineVariant, color: colors.onSurfaceVariant }}>
            <div className="flex items-center gap-1.5">
              <span className="w-3 h-3 rounded-full" style={{ backgroundColor: colors.primary }} />
              <span>Flow Days (Active)</span>
            </div>
            <div className="flex items-center gap-1.5">
              <span className="w-3 h-3 rounded-full" style={{ backgroundColor: `${colors.primaryContainer}50` }} />
              <span>Predicted Period</span>
            </div>
            <div className="flex items-center gap-1.5">
              <span className="w-3 h-3 rounded-full border-2" style={{ borderColor: colors.secondary }} />
              <span>Current Day</span>
            </div>
          </div>
        </PrimaryCard>

        {/* SEC 2: STATS SUMMARY PANEL */}
        <div className="grid grid-cols-2 gap-3">
          <PrimaryCard variant="low" className="p-4 flex flex-col gap-1">
            <span className="text-[9px] font-extrabold uppercase tracking-widest text-neutral-500">Average Duration</span>
            <span className="text-xl font-black" style={{ color: colors.primary }}>28 Days</span>
          </PrimaryCard>
          <PrimaryCard variant="low" className="p-4 flex flex-col gap-1">
            <span className="text-[9px] font-extrabold uppercase tracking-widest text-neutral-500">Normal Flow Length</span>
            <span className="text-xl font-black" style={{ color: colors.secondary }}>5 Days</span>
          </PrimaryCard>
        </div>

        {/* SEC 3: FLOATING LOG INTENSITY BUTTON */}
        <button
          onClick={() => setIsLogOpen(true)}
          className="fixed bottom-24 right-6 w-14 h-14 rounded-full shadow-gentle-lg flex items-center justify-center text-white transition-all duration-300 hover:scale-105 active:scale-95 z-40 focus:outline-none focus:ring-2 focus:ring-[#8a4d4e]/50"
          style={{ backgroundColor: colors.primary }}
          aria-label="Add daily symptom log"
        >
          <span className="material-symbols-outlined text-3xl font-bold">add</span>
        </button>

        {/* SEC 4: LOGGING BOTTOM SHEET */}
        <BottomSheet
          isOpen={isLogOpen}
          onClose={() => setIsLogOpen(false)}
          title="Daily Symptom Log & Mood Journal"
          onSave={handleSaveLog}
          saveLabel="Save Secure Log"
        >
          <div className="flex items-center justify-between py-1 border-b" style={{ borderColor: colors.outlineVariant }}>
            <span className="text-xs font-bold" style={{ color: colors.onSurface }}>Temporarily suspend log calculations?</span>
            <input
              type="checkbox"
              checked={noTracking}
              onChange={(e) => setNoTracking(e.target.checked)}
              className="h-4 w-4 rounded text-[#8a4d4e] focus:ring-[#8a4d4e]"
            />
          </div>

          <ChipSelector
            label="Log Flow Intensity"
            singleSelect
            options={[
              { id: 'spotting', label: 'Spotting 💧' },
              { id: 'light', label: 'Light 🩸' },
              { id: 'medium', label: 'Medium 🩸🩸' },
              { id: 'heavy', label: 'Heavy 🩸🩸🩸' },
            ]}
            selectedIds={flowIntensity}
            onChange={setFlowIntensity}
          />

          <ChipSelector
            label="Common Symptoms"
            options={[
              { id: 'cramps', label: 'Cramps ⚡' },
              { id: 'headache', label: 'Headache 🧠' },
              { id: 'fatigue', label: 'Fatigue 😴' },
              { id: 'bloating', label: 'Bloating 🎈' },
              { id: 'acne', label: 'Acne outbreaks ✨' },
            ]}
            selectedIds={selectedSymptoms}
            onChange={setSelectedSymptoms}
          />

          <div className="flex flex-col gap-2 w-full mt-1">
            <span className="text-xs font-extrabold uppercase tracking-wide text-neutral-500">Pain Scale level ({painLevel}/10)</span>
            <input
              type="range"
              min="1"
              max="10"
              value={painLevel}
              onChange={(e) => setPainLevel(parseInt(e.target.value))}
              className="w-full h-2 rounded-lg appearance-none cursor-pointer accent-[#8a4d4e] bg-neutral-200"
            />
          </div>

          <FormField
            label="Reflections / Notes"
            placeholder="How are you feeling physically?"
            value={logNotes}
            onChange={(e) => setLogNotes(e.target.value)}
          />
        </BottomSheet>

      </div>
    </AppShell>
  );
};

export default CyclePage;
