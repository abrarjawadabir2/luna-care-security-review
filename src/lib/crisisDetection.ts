/**
 * Safe client-side lexical analysis for emergency/crisis tracking.
 * Securely alerts users to available emergency options without transmitting sensitive messages to open servers.
 */

const CRISIS_KEYWORDS = [
  'kill myself',
  'suicide',
  'want to die',
  'self harm',
  'end my life',
  'hurt myself',
  'depressed severely',
  'cannot go on',
  'severe depression',
  'hopeless',
];

export interface CrisisScreenResult {
  hasCrisisSignal: boolean;
  detectedWord: string | null;
  recommendedHotline: string;
}

export const detectCrisisSignal = (text: string): CrisisScreenResult => {
  const norm = text.toLowerCase();
  
  for (const keyword of CRISIS_KEYWORDS) {
    if (norm.includes(keyword)) {
      return {
        hasCrisisSignal: true,
        detectedWord: keyword,
        recommendedHotline: 'Emergency National Health Line: dial 999 or Crisis Support Desk at 109',
      };
    }
  }

  return {
    hasCrisisSignal: false,
    detectedWord: null,
    recommendedHotline: 'General Support Desk available',
  };
};
