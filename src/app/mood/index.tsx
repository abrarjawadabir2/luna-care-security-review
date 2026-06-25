import React, { useState } from 'react';
import { colors } from '../../styles/theme';
import { AppShell } from '../../components/layout/AppShell';
import { PrimaryCard } from '../../components/ui/PrimaryCard';
import { FormField } from '../../components/ui/FormField';
import { PrimaryButton } from '../../components/ui/PrimaryButton';
import { ChipSelector } from '../../components/ui/ChipSelector';
import { WarningCard } from '../../components/ui/WarningCard';
import { detectCrisisSignal } from '../../lib/crisisDetection';

interface MoodProps {
  onNavigate: (route: string) => void;
}

interface BehaviourLog {
  date: string;
  moodValue: number; // 1-5
  moodLabel: string;
  stress: number;
  anxiety: number;
}

export const MoodPage: React.FC<MoodProps> = ({ onNavigate }) => {
  // Current check-in Form State
  const [selectedMood, setSelectedMood] = useState<string>('Okay');
  const [stress, setStress] = useState(5);
  const [anxiety, setAnxiety] = useState(5);
  const [sleep, setSleep] = useState(7);
  const [energy, setEnergy] = useState(5);
  const [notes, setNotes] = useState('');
  const [crisisAlert, setCrisisAlert] = useState(false);

  // SEC: MOCK STABLE BEHAVIOUR LOGS FOR LAST 30 DAYS (Respecting simulated RLS queries)
  const [behaviourLogs, setBehaviourLogs] = useState<BehaviourLog[]>([
    { date: '06-01', moodValue: 4, moodLabel: 'Good', stress: 3, anxiety: 4 },
    { date: '06-04', moodValue: 5, moodLabel: 'Great', stress: 2, anxiety: 2 },
    { date: '06-07', moodValue: 3, moodLabel: 'Okay', stress: 5, anxiety: 4 },
    { date: '06-10', moodValue: 2, moodLabel: 'Low', stress: 7, anxiety: 6 },
    { date: '06-13', moodValue: 1, moodLabel: 'Very Low', stress: 9, anxiety: 8 },
    { date: '06-16', moodValue: 3, moodLabel: 'Okay', stress: 4, anxiety: 3 },
    { date: '06-19', moodValue: 4, moodLabel: 'Good', stress: 3, anxiety: 2 },
    { date: '06-22', moodValue: 4, moodLabel: 'Good', stress: 4, anxiety: 3 },
  ]);

  const moodsList = [
    { id: 'Great', label: 'Great 🌸', value: 5 },
    { id: 'Good', label: 'Good 😊', value: 4 },
    { id: 'Okay', label: 'Okay 😐', value: 3 },
    { id: 'Low', label: 'Low 😔', value: 2 },
    { id: 'Very Low', label: 'Very Low 😭', value: 1 },
    { id: 'Anxious', label: 'Anxious 😰', value: 1.5 },
    { id: 'Overwhelmed', label: 'Overwhelmed 🤯', value: 1 },
  ];

  const handleNotesChange = (val: string) => {
    setNotes(val);
    const evaluation = detectCrisisSignal(val);
    if (evaluation.hasCrisisSignal) {
      setCrisisAlert(true);
    } else {
      setCrisisAlert(false);
    }
  };

  const handleSaveCheckIn = () => {
    const finalEvaluation = detectCrisisSignal(notes);
    if (finalEvaluation.hasCrisisSignal) {
      onNavigate('crisis');
      return;
    }

    const matchedMood = moodsList.find((x) => x.id === selectedMood) || moodsList[2];
    const todayStr = new Date().toLocaleDateString('en-US', { month: '2-digit', day: '2-digit' }).replace('/', '-');
    
    const newLog: BehaviourLog = {
      date: todayStr,
      moodValue: matchedMood.value,
      moodLabel: matchedMood.id,
      stress,
      anxiety,
    };

    setBehaviourLogs([...behaviourLogs, newLog]);
    
    // Clear forms
    setSelectedMood('Okay');
    setStress(5);
    setAnxiety(5);
    setSleep(7);
    setEnergy(5);
    setNotes('');
    
    alert("Wellbeing check-in saved securely on client-side state 🔒");
  };

  // SEC: SVG PATH PLOTTING (Aesthetic Line Chart representing Recharts trends)
  const chartHeight = 110;
  const chartWidth = 320;
  const paddingX = 25;
  const stepX = (chartWidth - paddingX * 2) / (behaviourLogs.length - 1 || 1);
  
  // Calculate SVG coordinates for Trend Lines
  const points = behaviourLogs.map((log, index) => {
    const x = paddingX + index * stepX;
    // Map mood scale 1-5 to chart bounds
    const y = chartHeight - ((log.moodValue - 1) / 4) * (chartHeight - 20) - 10;
    return `${x},${y}`;
  }).join(' ');

  const stressPoints = behaviourLogs.map((log, index) => {
    const x = paddingX + index * stepX;
    // Map stress scale 1-10 to chart bounds
    const y = chartHeight - ((log.stress - 1) / 9) * (chartHeight - 20) - 10;
    return `${x},${y}`;
  }).join(' ');

  return (
    <AppShell>
      <div className="flex flex-col gap-5 py-4 w-full">
        
        {/* Header view */}
        <div className="flex items-center justify-between px-1">
          <div className="flex flex-col">
            <h2 className="text-2xl font-extrabold" style={{ color: colors.primary, fontFamily: 'Plus Jakarta Sans' }}>
              How are you today?
            </h2>
            <p className="text-xs" style={{ color: colors.onSurfaceVariant }}>
              Sovereign offline mood dashboard
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

        {/* SEC 1: PRE-SAVE CRITICAL WARNING BANNER */}
        {crisisAlert && (
          <WarningCard
            variant="urgent"
            title="Sovereign Safety Alert Detected"
            description="Our local safety filter scanned keywords matching severe psychological distress. Your notes remain 100% private. Tap below to reach direct medical hotlines."
            actionLabel="Connect with Crisis Support Counter"
            onActionClick={() => onNavigate('crisis')}
          />
        )}

        {/* SEC 2: SELECTION CARDS */}
        <PrimaryCard className="flex flex-col gap-4">
          <ChipSelector
            label="Log Current Well-being Accent"
            singleSelect
            options={moodsList}
            selectedIds={[selectedMood]}
            onChange={(ids) => setSelectedMood(ids[0] || 'Okay')}
          />

          {/* Stress Slider */}
          <div className="flex flex-col gap-1.5 mt-1">
            <div className="flex justify-between text-xs font-bold" style={{ color: colors.onSurface }}>
              <span>Log Stress Level</span>
              <span style={{ color: colors.primary }}>{stress}/10</span>
            </div>
            <input
              type="range"
              min="1"
              max="10"
              value={stress}
              onChange={(e) => setStress(parseInt(e.target.value))}
              className="w-full h-1.5 rounded-lg appearance-none cursor-pointer accent-[#8a4d4e] bg-neutral-200"
            />
          </div>

          {/* Anxiety Slider */}
          <div className="flex flex-col gap-1.5">
            <div className="flex justify-between text-xs font-bold" style={{ color: colors.onSurface }}>
              <span>Anxiety & Tension</span>
              <span style={{ color: colors.secondary }}>{anxiety}/10</span>
            </div>
            <input
              type="range"
              min="1"
              max="10"
              value={anxiety}
              onChange={(e) => setAnxiety(parseInt(e.target.value))}
              className="w-full h-1.5 rounded-lg appearance-none cursor-pointer accent-[#5d5a84] bg-neutral-200"
            />
          </div>

          <FormField
            label="Reflection Notes (Secured)"
            placeholder="Write down any secure private logs here..."
            value={notes}
            onChange={(e) => handleNotesChange(e.target.value)}
          />

          <PrimaryButton 
            label="Save Secure Wellbeing Check-In" 
            fullWidth 
            onClick={handleSaveCheckIn}
          />
        </PrimaryCard>

        {/* SEC 3: RECHARTS INSPIRED TREND CHART COMPONENT */}
        <PrimaryCard variant="container" className="flex flex-col gap-3 relative overflow-hidden">
          <div className="flex flex-col px-1">
            <h3 className="text-sm font-extrabold flex items-center gap-1.5" style={{ color: colors.primary }}>
              <span className="material-symbols-outlined text-base">monitoring</span>
              Mood Trends (Last 30 Days)
            </h3>
            <p className="text-[10px]" style={{ color: colors.onSurfaceVariant }}>
              Simulated PostgreSQL RLS queries extracting 'behaviour_logs' trend matrices.
            </p>
          </div>

          {/* Secure interactive SVG Line chart */}
          <div className="w-full flex justify-center bg-white/70 rounded-2xl py-3 px-1 border border-neutral-100">
            <svg viewBox={`0 0 ${chartWidth} ${chartHeight}`} className="w-full h-auto">
              {/* Grid guide Lines */}
              <line x1={paddingX} y1="20" x2={chartWidth - paddingX} y2="20" stroke="#f1e5e4" strokeWidth="1" strokeDasharray="3" />
              <line x1={paddingX} y1="65" x2={chartWidth - paddingX} y2="65" stroke="#f1e5e4" strokeWidth="1" strokeDasharray="3" />
              <line x1={paddingX} y1="100" x2={chartWidth - paddingX} y2="100" stroke="#f1e5e4" strokeWidth="1" strokeDasharray="3" />

              {/* Mood Line (Rose Primary) */}
              <polyline
                fill="none"
                stroke={colors.primary}
                strokeWidth="3.5"
                strokeLinecap="round"
                strokeLinejoin="round"
                points={points}
              />

              {/* Stress Line (Lavender Secondary) */}
              <polyline
                fill="none"
                stroke={colors.secondary}
                strokeWidth="2"
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeDasharray="4"
                points={stressPoints}
              />

              {/* Data points */}
              {behaviourLogs.map((log, index) => {
                const x = paddingX + index * stepX;
                const y = chartHeight - ((log.moodValue - 1) / 4) * (chartHeight - 20) - 10;
                return (
                  <circle
                    key={index}
                    cx={x}
                    cy={y}
                    r="4"
                    fill="#ffffff"
                    stroke={colors.primary}
                    strokeWidth="2.5"
                  />
                );
              })}

              {/* Dates indicator text */}
              {behaviourLogs.map((log, index) => {
                if (index % 2 !== 0 && index !== behaviourLogs.length - 1) return null;
                const x = paddingX + index * stepX;
                return (
                  <text
                    key={index}
                    x={x}
                    y={chartHeight - 2}
                    textAnchor="middle"
                    fill="#524343"
                    fontSize="7"
                    fontWeight="bold"
                  >
                    {log.date}
                  </text>
                );
              })}
            </svg>
          </div>

          <div className="flex gap-4 justify-center text-[9px] font-bold" style={{ color: colors.onSurfaceVariant }}>
            <div className="flex items-center gap-1">
              <span className="w-2.5 h-1 bg-[#8a4d4e] rounded-full inline-block" />
              <span>Well-being (Higher is better)</span>
            </div>
            <div className="flex items-center gap-1">
              <span className="w-2.5 h-1 bg-[#5d5a84] border-dashed border inline-block" />
              <span>Simulated Stress Score</span>
            </div>
          </div>
        </PrimaryCard>

      </div>
    </AppShell>
  );
};

export default MoodPage;
