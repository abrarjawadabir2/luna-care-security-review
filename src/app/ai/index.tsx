import React, { useState } from 'react';
import { colors } from '../../styles/theme';
import { AppShell } from '../../components/layout/AppShell';
import { FormField } from '../../components/ui/FormField';
import { PrimaryButton } from '../../components/ui/PrimaryButton';
import { PrimaryCard } from '../../components/ui/PrimaryCard';
import { detectCrisisSignal } from '../../lib/crisisDetection';

interface AIChatProps {
  onNavigate: (route: string) => void;
}

interface Message {
  id: number;
  sender: 'user' | 'luna';
  text: string;
}

export const AIChatPage: React.FC<AIChatProps> = ({ onNavigate }) => {
  const [messages, setMessages] = useState<Message[]>([
    { id: 1, sender: 'luna', text: 'Hello! I am Luna Intelligence. I can help answer general questions about cycle tracking, menstrual cup sanitation, or general stress reliefs securely. How can I guide you today? 🌸' },
  ]);
  const [userInput, setUserInput] = useState('');
  const [tokensUsed, setTokensUsed] = useState(32);
  const tokenLimit = 300;

  const handleSendMessage = (e: React.FormEvent) => {
    e.preventDefault();
    if (!userInput.trim()) return;

    // 1. Lexical crisis signals diagnostic check before submission
    const crisisAnalysis = detectCrisisSignal(userInput);
    if (crisisAnalysis.hasCrisisSignal) {
      onNavigate('crisis');
      return;
    }

    const userMsg: Message = {
      id: Date.now(),
      sender: 'user',
      text: userInput,
    };

    setMessages([...messages, userMsg]);
    setUserInput('');
    setTokensUsed((prev) => prev + 24);

    // Simulated medical hygiene AI guides respuesta
    setTimeout(() => {
      const answers: Record<string, string> = {
        cup: 'Boiling silicone menstrual cups in high-grade clean water for 5 minutes before initially inserting is recommended. Clean with mild fragrance-free wash. Store only in air-permeable cotton bags.',
        pms: 'Severe Pre-Menstrual Syndrome comfort parameters often include drinking warm chamomile tea, keeping muscles relaxed with warm water pads, and prioritizing a regular sleep timeline.',
        pcos: 'Polycystic Ovary Syndrome involves hormonal variations. This frequently causes missed menstrual cycles and elevated stress. Consider regular clinic consultation for personalized dosage regimens.',
      };

      let reply = 'I support general physical hygiene or menstrual guide questions. If you are experiencing extreme localized pelvic cramps or severe bleeding concerns, please book a direct doctor diagnostic appointment.';
      const normInput = userInput.toLowerCase();
      
      if (normInput.includes('cup') || normInput.includes('clean')) {
        reply = answers.cup;
      } else if (normInput.includes('pms') || normInput.includes('pain') || normInput.includes('cramp')) {
        reply = answers.pms;
      } else if (normInput.includes('pcos') || normInput.includes('pcod') || normInput.includes('cyst')) {
        reply = answers.pcos;
      }

      setMessages((prev) => [...prev, {
        id: Date.now() + 1,
        sender: 'luna',
        text: reply,
      }]);
      setTokensUsed((prev) => prev + 48);
    }, 1000);
  };

  return (
    <AppShell>
      <div className="flex flex-col gap-4 py-4 w-full h-[85vh] animate-fade-in justify-between">
        
        {/* Top Header details */}
        <div className="flex items-center justify-between px-1 flex-shrink-0">
          <div className="flex flex-col">
            <h2 className="text-xl font-extrabold flex items-center gap-1.5" style={{ color: colors.primary, fontFamily: 'Plus Jakarta Sans' }}>
              Luna AI Assistant ✨
            </h2>
            <p className="text-[10px]" style={{ color: colors.onSurfaceVariant }}>
              Encrypted Educational Healthcare Chatbot
            </p>
          </div>
          <button
            onClick={() => onNavigate('home')}
            className="p-1.5 rounded-full hover:bg-neutral-100"
            style={{ color: colors.primary }}
          >
            <span className="material-symbols-outlined text-2xl animate-spin-slow">home</span>
          </button>
        </div>

        {/* Dynamic Token Counter Panel */}
        <div className="flex items-center justify-between border rounded-2xl px-4 py-2 bg-white/70 flex-shrink-0 text-[10px] font-bold" style={{ borderColor: colors.outlineVariant, color: colors.onSurfaceVariant }}>
          <span>Free Educational Tokens Used: {tokensUsed} / {tokenLimit}</span>
          <span className="font-extrabold" style={{ color: colors.primary }}>Upgrade Plan 👑</span>
        </div>

        {/* Scrolling Chat View Panel */}
        <div className="flex-grow overflow-y-auto px-1 py-2 flex flex-col gap-3 rounded-2xl bg-white/40 border border-neutral-100 p-3">
          {messages.map((item) => {
            const isLuna = item.sender === 'luna';
            return (
              <div
                key={item.id}
                className={`flex max-w-[85%] flex-col gap-1 p-3 rounded-2xl text-xs font-semibold leading-relaxed ${
                  isLuna 
                    ? 'self-start bg-neutral-100 rounded-tl-none text-neutral-800'
                    : 'self-end text-white rounded-tr-none'
                }`}
                style={{
                  backgroundColor: !isLuna ? colors.primary : '',
                }}
              >
                {item.text}
              </div>
            );
          })}
        </div>

        {/* Input box */}
        <form onSubmit={handleSendMessage} className="flex gap-2 items-center flex-shrink-0 mt-1">
          <div className="flex-grow">
            <FormField
              label=""
              placeholder="Ask about cups hygiene, PCOS advice..."
              value={userInput}
              onChange={(e) => setUserInput(e.target.value)}
              className="p-0 border-0"
              style={{ minHeight: '44px' }}
            />
          </div>
          <div className="w-12 h-12 flex-shrink-0">
            <button
              type="submit"
              className="w-full h-full rounded-2xl flex items-center justify-center text-white active:scale-95 transition-all"
              style={{ backgroundColor: colors.primary }}
              aria-label="Send message"
            >
              <span className="material-symbols-outlined text-xl">send</span>
            </button>
          </div>
        </form>

        {/* Mandatory Educational Disclaimer footer */}
        <div className="text-[9px] text-center px-4 flex-shrink-0" style={{ color: colors.onSurfaceVariant }}>
          🛡️ Luna AI Assistant is programmed strictly for general educational insights. It does not output medical prescriptions or clinical diagnosis.
        </div>

      </div>
    </AppShell>
  );
};

export default AIChatPage;
