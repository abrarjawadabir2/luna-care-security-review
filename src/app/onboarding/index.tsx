import React, { useState } from 'react';
import { colors } from '../../styles/theme';
import { AppShell } from '../../components/layout/AppShell';
import { ProgressHeader } from '../../components/ui/ProgressHeader';
import { OptionCard } from '../../components/ui/OptionCard';
import { PrimaryButton } from '../../components/ui/PrimaryButton';
import { ChipSelector } from '../../components/ui/ChipSelector';

interface OnboardingProps {
  onNavigate: (route: string) => void;
  onOnboardingComplete: (mode: string) => void;
}

export const OnboardingPage: React.FC<OnboardingProps> = ({ 
  onNavigate, 
  onOnboardingComplete 
}) => {
  const [step, setStep] = useState(1);
  const totalSteps = 6;

  // Selected Answers State
  const [selectedMode, setSelectedMode] = useState<string>('SELF_TRACKING');
  const [pronouns, setPronouns] = useState<string[]>(['she/her']);
  const [periodRelevant, setPeriodRelevant] = useState<string>('YES');
  const [supportRelation, setSupportRelation] = useState<string>('PARTNER');
  const [region, setRegion] = useState<string>('DHAKA');
  const [behaviourFocus, setBehaviourFocus] = useState<string[]>(['wellbeing']);

  const handleNext = () => {
    if (step < totalSteps) {
      setStep(step + 1);
    } else {
      // Save user mode and route to Home
      onOnboardingComplete(selectedMode);
      onNavigate('home');
    }
  };

  const handleBack = () => {
    if (step > 1) {
      setStep(step - 1);
    } else {
      onNavigate('index');
    }
  };

  return (
    <AppShell hideBottomNav>
      <div className="flex-grow flex flex-col justify-between py-6 min-h-[85vh]">
        
        {/* Dynamic Progress Indicator header */}
        <ProgressHeader 
          currentStep={step} 
          totalSteps={totalSteps} 
          onBack={handleBack}
        />

        {/* Content Box */}
        <div className="flex-grow flex flex-col justify-center gap-6 py-6 px-1">
          {step === 1 && (
            <div className="flex flex-col gap-4 animate-fade-in">
              <div className="flex flex-col gap-1 mb-2">
                <h3 className="text-xl font-extrabold" style={{ color: colors.primary, fontFamily: 'Plus Jakarta Sans' }}>
                  How do you want to use LunaCare? 🍃
                </h3>
                <p className="text-xs" style={{ color: colors.onSurfaceVariant }}>
                  Your mode determines custom tabs and priority features.
                </p>
              </div>

              <OptionCard
                title="Self-Tracking & Emotional Health"
                description="Monitor cycles, log daily logs, explore mental health options and learn care basics."
                icon="track_changes"
                isSelected={selectedMode === 'SELF_TRACKING'}
                onClick={() => setSelectedMode('SELF_TRACKING')}
              />

              <OptionCard
                title="Support & Care Companion"
                description="Educate yourself to support an active partner, friend, or relative with respect."
                icon="volunteer_activism"
                isSelected={selectedMode === 'SUPPORT_MODE'}
                onClick={() => setSelectedMode('SUPPORT_MODE')}
              />

              <OptionCard
                title="Health Education Library Only"
                description="Securely explore clinical guides without any physical symptoms tracking logs."
                icon="menu_book"
                isSelected={selectedMode === 'EDUCATION_ONLY'}
                onClick={() => setSelectedMode('EDUCATION_ONLY')}
              />
            </div>
          )}

          {step === 2 && (
            <div className="flex flex-col gap-4 animate-fade-in">
              <div className="flex flex-col gap-1">
                <h3 className="text-xl font-extrabold" style={{ color: colors.primary, fontFamily: 'Plus Jakarta Sans' }}>
                  Pronouns & Gender Selection 👥
                </h3>
                <p className="text-xs" style={{ color: colors.onSurfaceVariant }}>
                  Ensures all supportive notes respect your preferred visual language.
                </p>
              </div>

              <ChipSelector
                label="Choose Preferred Pronouns"
                options={[
                  { id: 'she/her', label: 'She / Her', icon: 'person' },
                  { id: 'they/them', label: 'They / Them', icon: 'groups' },
                  { id: 'he/him', label: 'He / Him', icon: 'person_outline' },
                  { id: 'prefer-not', label: 'Prefer secure omission', icon: 'visibility_off' },
                ]}
                selectedIds={pronouns}
                onChange={setPronouns}
              />
            </div>
          )}

          {step === 3 && (
            <div className="flex flex-col gap-4 animate-fade-in">
              {selectedMode === 'SUPPORT_MODE' ? (
                <>
                  <div className="flex flex-col gap-1">
                    <h3 className="text-xl font-extrabold" style={{ color: colors.primary, fontFamily: 'Plus Jakarta Sans' }}>
                      Support Relationship 🤝
                    </h3>
                    <p className="text-xs" style={{ color: colors.onSurfaceVariant }}>
                      Help us custom tailor PMS guidance for your context.
                    </p>
                  </div>

                  <OptionCard
                    title="Active Partner Support"
                    description="Tailored checklists to help cushion extreme menstrual/PMS discomforts."
                    isSelected={supportRelation === 'PARTNER'}
                    onClick={() => setSupportRelation('PARTNER')}
                  />

                  <OptionCard
                    title="Parent/Guardian Care"
                    description="Educational resources for teenage menstrual cup safety and hygiene guide."
                    isSelected={supportRelation === 'PARENT'}
                    onClick={() => setSupportRelation('PARENT')}
                  />
                </>
              ) : (
                <>
                  <div className="flex flex-col gap-1">
                    <h3 className="text-xl font-extrabold" style={{ color: colors.primary, fontFamily: 'Plus Jakarta Sans' }}>
                      Body-relevant period tracking? 🩸
                    </h3>
                    <p className="text-xs" style={{ color: colors.onSurfaceVariant }}>
                      Tell us if you want visible calendars or pure mental wellbeing options.
                    </p>
                  </div>

                  <OptionCard
                    title="Yes, calculate cycle estimations"
                    description="Enables calendar estimates, phase alerts, and symptom flow trackers."
                    isSelected={periodRelevant === 'YES'}
                    onClick={() => setPeriodRelevant('YES')}
                  />

                  <OptionCard
                    title="No, focus only on emotional health"
                    description="Hides active calendar displays. Keeps support focused on stress, anxiety and nutrition."
                    isSelected={periodRelevant === 'NO'}
                    onClick={() => setPeriodRelevant('NO')}
                  />
                </>
              )}
            </div>
          )}

          {step === 4 && (
            <div className="flex flex-col gap-4 animate-fade-in">
              <div className="flex flex-col gap-1">
                <h3 className="text-xl font-extrabold" style={{ color: colors.primary, fontFamily: 'Plus Jakarta Sans' }}>
                  Regional parameters 🌍
                </h3>
                <p className="text-xs" style={{ color: colors.onSurfaceVariant }}>
                  Ensures local dispensary locations/clinics search queries operate accurately.
                </p>
              </div>

              <OptionCard
                title="Dhaka Metropolitan Area"
                description="Configures clinics queries around Central Dhaka, Dhanmondi and Gulshan."
                isSelected={region === 'DHAKA'}
                onClick={() => setRegion('DHAKA')}
              />

              <OptionCard
                title="Chittagong Region"
                description="Configures geographic lists around Agrabad & GEC areas."
                isSelected={region === 'CHITTAGONG'}
                onClick={() => setRegion('CHITTAGONG')}
              />

              <OptionCard
                title="Other Remote Location"
                description="Asks for temporary, offline location criteria only during active manual searches."
                isSelected={region === 'OTHER'}
                onClick={() => setRegion('OTHER')}
              />
            </div>
          )}

          {step === 5 && (
            <div className="flex flex-col gap-4 animate-fade-in">
              <div className="flex flex-col gap-1">
                <h3 className="text-xl font-extrabold" style={{ color: colors.primary, fontFamily: 'Plus Jakarta Sans' }}>
                  Select Behaviour Focus 🎯
                </h3>
                <p className="text-xs" style={{ color: colors.onSurfaceVariant }}>
                  Prioritizes daily coaching insights on your homepage.
                </p>
              </div>

              <ChipSelector
                label="Choose (Multiple allowed)"
                options={[
                  { id: 'wellbeing', label: 'Mental Wellbeing 🧠', icon: 'psychology' },
                  { id: 'cup', label: 'Menstrual Cup Guide 🧼', icon: 'wash' },
                  { id: 'pms', label: 'Severe PMS Comforts 🌸', icon: 'spa' },
                  { id: 'nutrition', label: 'Healthy Nutrition 🥦', icon: 'nutrition' },
                  { id: 'fitness', label: 'Gentle Workouts 🧘‍♀️', icon: 'fit_screen' },
                ]}
                selectedIds={behaviourFocus}
                onChange={setBehaviourFocus}
              />
            </div>
          )}

          {step === 6 && (
            <div className="flex flex-col gap-4 text-center animate-fade-in">
              <div className="w-16 h-16 rounded-full bg-green-50 flex items-center justify-center mx-auto mb-2">
                <span className="material-symbols-outlined text-[36px] text-green-600 font-bold">verified_user</span>
              </div>
              
              <div className="flex flex-col gap-2">
                <h3 className="text-xl font-extrabold" style={{ color: colors.primary, fontFamily: 'Plus Jakarta Sans' }}>
                  Permission & Security Review
                </h3>
                <p className="text-xs px-2 leading-relaxed" style={{ color: colors.onSurfaceVariant }}>
                  LunaCare stores all logs locally using client-side secure databases. You have complete sovereignty over your data.
                </p>
              </div>

              <div 
                className="p-4 rounded-2xl border text-left flex flex-col gap-2 text-[11px] leading-relaxed mx-auto max-w-sm mt-2"
                style={{ backgroundColor: colors.surfaceContainer, borderColor: colors.outlineVariant }}
              >
                <p className="font-bold text-center" style={{ color: colors.primary }}>🛡️ SECURITY PLEDGE:</p>
                <p>• No active trackers are integrated inside our system databases.</p>
                <p>• Your precise GPS credentials are never recorded automatically.</p>
                <p>• AI-powered education scans operate safely; no medical journals are sent to server networks.</p>
              </div>
            </div>
          )}
        </div>

        {/* Next buttons action container */}
        <div className="w-full mt-auto">
          <PrimaryButton 
            label={step === totalSteps ? "Finish & Open App" : "Continue"} 
            icon={step === totalSteps ? "check" : "arrow_forward"}
            fullWidth 
            onClick={handleNext}
          />
        </div>

      </div>
    </AppShell>
  );
};

export default OnboardingPage;
