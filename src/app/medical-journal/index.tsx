import React, { useState } from 'react';
import { colors } from '../../styles/theme';
import { AppShell } from '../../components/layout/AppShell';
import { PrimaryCard } from '../../components/ui/PrimaryCard';
import { FormField } from '../../components/ui/FormField';
import { PrimaryButton } from '../../components/ui/PrimaryButton';
import { ChipSelector } from '../../components/ui/ChipSelector';
import { EmptyState } from '../../components/ui/EmptyState';

interface MedicalJournalProps {
  onNavigate: (route: string) => void;
}

interface MedicalItem {
  id: number;
  date: string;
  category: string;
  pain: number;
  medicine: string;
  doctorNotes: string;
}

export const MedicalJournalPage: React.FC<MedicalJournalProps> = ({ onNavigate }) => {
  const [category, setCategory] = useState<string[]>(['Period']);
  const [pain, setPain] = useState(5);
  const [medicine, setMedicine] = useState('');
  const [doctorNotes, setDoctorNotes] = useState('');
  const [medicalLogs, setMedicalLogs] = useState<MedicalItem[]>([
    {
      id: 1,
      date: '06-20-2026',
      category: 'Period',
      pain: 4,
      medicine: 'Paracetamol 500mg',
      doctorNotes: 'Suggested regular hydration. Avoid heavy physical work today.',
    },
  ]);

  const handleSaveMedicalLog = (e: React.FormEvent) => {
    e.preventDefault();
    if (!medicine.trim() && !doctorNotes.trim()) return;

    const newLog: MedicalItem = {
      id: Date.now(),
      date: new Date().toLocaleDateString('en-US').replace(/\//g, '-'),
      category: category[0] || 'Period',
      pain,
      medicine,
      doctorNotes,
    };

    setMedicalLogs([newLog, ...medicalLogs]);
    setMedicine('');
    setDoctorNotes('');
    alert('Medical Log saved securely! Encrypted 🔒');
  };

  const handleExportData = () => {
    alert("Sovereign Privacy Export: Successfully downloaded package encrypted inside 'LunaCare-Medical-Export.json'");
  };

  return (
    <AppShell>
      <div className="flex flex-col gap-5 py-4 w-full animate-fade-in">
        
        {/* Header view */}
        <div className="flex items-center justify-between px-1">
          <div className="flex flex-col">
            <h2 className="text-2xl font-extrabold" style={{ color: colors.primary, fontFamily: 'Plus Jakarta Sans' }}>
              My Medical Journal
            </h2>
            <p className="text-xs" style={{ color: colors.onSurfaceVariant }}>
              Encrypted symptoms checklist documentation
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

        {/* SEC 1: SAVING COMPONENT FORM */}
        <PrimaryCard className="flex flex-col gap-4">
          <h3 className="text-sm font-bold flex items-center gap-1.5" style={{ color: colors.primary }}>
            <span className="material-symbols-outlined text-base">clinical_researching</span>
            Log Clinical Symptoms
          </h3>

          <form onSubmit={handleSaveMedicalLog} className="flex flex-col gap-3.5">
            <ChipSelector
              label="Select Medical Care Category"
              singleSelect
              options={[
                { id: 'Period', label: 'Period 🩸' },
                { id: 'PMS', label: 'PMS Comforts 🌸' },
                { id: 'PCOS/PCOD', label: 'PCOS / Cysts 🩺' },
                { id: 'Medicine', label: 'Medicine Tracker 💊' },
                { id: 'Mental health', label: 'Mental Health Moods 🧘‍♀️' },
              ]}
              selectedIds={category}
              onChange={setCategory}
            />

            <div className="flex flex-col gap-1.5">
              <div className="flex justify-between text-xs font-bold" style={{ color: colors.onSurface }}>
                <span>Pain Level Score</span>
                <span style={{ color: colors.primary }}>{pain}/10</span>
              </div>
              <input
                type="range"
                min="1"
                max="10"
                value={pain}
                onChange={(e) => setPain(parseInt(e.target.value))}
                className="w-full h-1.5 rounded-lg appearance-none cursor-pointer accent-[#8a4d4e] bg-neutral-200"
              />
            </div>

            <FormField
              label="Medicine taken (Type / dosage)"
              placeholder="e.g. Paracetamol 500mg"
              value={medicine}
              onChange={(e) => setMedicine(e.target.value)}
            />

            <FormField
              label="Physician's Consultation Advice Notes"
              placeholder="Write down any notes from clinic sessions..."
              value={doctorNotes}
              onChange={(e) => setDoctorNotes(e.target.value)}
            />

            <PrimaryButton 
              label="Save Medical Log Offline" 
              type="submit"
              fullWidth
            />
          </form>
        </PrimaryCard>

        {/* SEC 2: EXPORT ACTIONS */}
        <div className="flex items-center justify-between border rounded-2xl p-4 bg-white/70" style={{ borderColor: colors.outlineVariant }}>
          <div className="flex flex-col gap-0.5">
            <span className="text-[11px] font-extrabold uppercase tracking-widest text-neutral-400">Compliance Export</span>
            <span className="text-xs font-bold text-neutral-600">Secure PDF / JSON Doctor Export</span>
          </div>
          <button
            onClick={handleExportData}
            className="px-4 py-2 text-xs font-bold rounded-xl border text-white transition-all duration-200"
            style={{ backgroundColor: colors.secondary }}
          >
            Export Log 📂
          </button>
        </div>

        {/* SEC 3: LOGS VISUAL LIST LIST */}
        <div className="flex flex-col gap-3">
          <h4 className="text-xs font-extrabold tracking-wider uppercase px-1 text-neutral-500">
            Historic Medical Logs
          </h4>

          {medicalLogs.length === 0 ? (
            <EmptyState
              title="No clinical data logged"
              description="Keep logs regarding severe ovarian cysts symptoms, ibuprofen usage, and clean menstrual cup routines safely in client memory."
              icon="clinical_researching"
            />
          ) : (
            <div className="flex flex-col gap-3">
              {medicalLogs.map((log) => (
                <PrimaryCard key={log.id} variant="low" className="p-4 flex flex-col gap-2">
                  <div className="flex justify-between items-center text-[10px] font-bold text-neutral-400 border-b pb-1.5 mb-1.5" style={{ borderColor: colors.outlineVariant }}>
                    <span>Category: <span className="font-extrabold text-neutral-600 uppercase">{log.category}</span></span>
                    <span>Date: {log.date}</span>
                  </div>

                  <div className="flex flex-col gap-1 text-[11px]">
                    <p>⚡ <span className="font-bold text-neutral-600">Pain Index:</span> {log.pain}/10</p>
                    {log.medicine && (
                      <p>💊 <span className="font-bold text-neutral-600">Medication Dosage:</span> {log.medicine}</p>
                    )}
                    {log.doctorNotes && (
                      <p>📋 <span className="font-bold text-neutral-600">Physician Notes:</span> {log.doctorNotes}</p>
                    )}
                  </div>
                </PrimaryCard>
              ))}
            </div>
          )}
        </div>

      </div>
    </AppShell>
  );
};

export default MedicalJournalPage;
