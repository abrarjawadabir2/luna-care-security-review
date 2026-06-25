/**
 * Secure mathematical calculation formulas for menstrual cycle estimation.
 * Standardizes regional calendar averages and period durations.
 */

export interface CycleStats {
  currentCycleDay: number;
  daysUntilPeriod: number;
  currentPhase: 'MENSTRUAL' | 'FOLLICULAR' | 'OVULATORY' | 'LUTEAL';
  phaseColor: string;
}

export const calculateCycleStats = (
  startDateStr: string | null,
  averageCycleLength: number = 28,
  averagePeriodLength: number = 5
): CycleStats => {
  if (!startDateStr) {
    return {
      currentCycleDay: 1,
      daysUntilPeriod: 14,
      currentPhase: 'FOLLICULAR',
      phaseColor: '#5d5a84',
    };
  }

  const start = new Date(startDateStr);
  const now = new Date();
  
  // Calculate difference in days
  const diffTime = Math.abs(now.getTime() - start.getTime());
  const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24));
  
  // Calculate current progress within cycle
  const currentCycleDay = (diffDays % averageCycleLength) + 1;
  const daysUntilPeriod = averageCycleLength - currentCycleDay;

  // Estimate menstrual phase based on day count
  let currentPhase: CycleStats['currentPhase'] = 'FOLLICULAR';
  let phaseColor = '#5d5a84'; // Secondary theme color

  if (currentCycleDay <= averagePeriodLength) {
    currentPhase = 'MENSTRUAL';
    phaseColor = '#8a4d4e'; // Primary theme color
  } else if (currentCycleDay <= 12) {
    currentPhase = 'FOLLICULAR';
    phaseColor = '#5d5a84'; // Secondary theme color
  } else if (currentCycleDay <= 16) {
    currentPhase = 'OVULATORY';
    phaseColor = '#8d4d39'; // Tertiary terracotta color
  } else {
    currentPhase = 'LUTEAL';
    phaseColor = '#d48c8c'; // Muted Light Rose
  }

  return {
    currentCycleDay,
    daysUntilPeriod,
    currentPhase,
    phaseColor,
  };
};
