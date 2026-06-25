import React, { useState } from 'react';
import { colors } from '../../styles/theme';
import { AppShell } from '../../components/layout/AppShell';
import { PrimaryCard } from '../../components/ui/PrimaryCard';
import { BookmarkButton } from '../../components/ui/BookmarkButton';
import { WarningCard } from '../../components/ui/WarningCard';

interface LearnProps {
  onNavigate: (route: string) => void;
}

export const LearnPage: React.FC<LearnProps> = ({ onNavigate }) => {
  const [bookmarks, setBookmarks] = useState<string[]>(['pnc-key']);

  const toggleBookmark = (slug: string) => {
    if (bookmarks.includes(slug)) {
      setBookmarks(bookmarks.filter((x) => x !== slug));
    } else {
      setBookmarks([...bookmarks, slug]);
    }
  };

  const topics = [
    {
      slug: 'pnc-key',
      title: 'PCOS / PCOD Awareness',
      summary: 'Hormonal parameters causing cysts in regional women ovaries. Leads to skipped cycles and hair changes.',
      warning: 'When to seek help: If cycles are missing for over 90 days straight, see a physician.',
    },
    {
      slug: 'pms-key',
      title: 'Understanding Extreme PMS',
      summary: 'Brief mood crashes, body aches, bloated parameters, and emotional surges preceding flow cycles.',
      warning: 'When to seek help: If symptoms interfere with daily life or prompt severe isolation.',
    },
    {
      slug: 'cup-key',
      title: 'Menstrual Cup Guidelines',
      summary: 'Medical-grade silicon alternatives to disposable plastics. Ecologically safe, reusable up to 10 years.',
      warning: 'Safety first: Boil cup in clean water for 5 minutes before and after every cycle.',
    },
    {
      slug: 'red-flags',
      title: 'Emergency Infection Warning Signs 🚨',
      summary: 'Toxic Shock Syndrome parameters. Very rare but critical. Triggered by unwashed hands or leaving cups in for over 12 hours.',
      warning: 'When to seek help: Immediate emergency ward care if sudden high fever, rash, or fainting occurs.',
    },
  ];

  return (
    <AppShell>
      <div className="flex flex-col gap-5 py-4 w-full animate-fade-in">
        
        {/* Header view */}
        <div className="flex items-center justify-between px-1">
          <div className="flex flex-col">
            <h2 className="text-2xl font-extrabold" style={{ color: colors.primary, fontFamily: 'Plus Jakarta Sans' }}>
              Wellbeing Library
            </h2>
            <p className="text-xs" style={{ color: colors.onSurfaceVariant }}>
              Clinical educational support files
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

        {/* SEC 1: MEDICAL LEGAL DISCLAIMER */}
        <WarningCard
          variant="info"
          title="Educational Platform Only"
          description="None of these segments are intended to override clinical dosage sheets. Always seek manual doctor validation before altering physical healthcare steps."
        />

        {/* SEC 2: ARTICLES ITERATION */}
        <div className="flex flex-col gap-4">
          {topics.map((topic) => {
            const isBookmarked = bookmarks.includes(topic.slug);
            return (
              <PrimaryCard key={topic.slug} className="flex flex-col gap-3">
                <div className="flex justify-between items-start gap-3">
                  <h3 className="text-base font-extrabold leading-tight" style={{ color: colors.onSurface, fontFamily: 'Plus Jakarta Sans' }}>
                    {topic.title}
                  </h3>
                  <BookmarkButton
                    isBookmarked={isBookmarked}
                    onToggle={() => toggleBookmark(topic.slug)}
                  />
                </div>

                <p className="text-xs leading-relaxed" style={{ color: colors.onSurfaceVariant }}>
                  {topic.summary}
                </p>

                {/* Warnings Highlight Section inside card */}
                <div 
                  className="p-3.5 rounded-2xl border text-[11px] font-semibold leading-relaxed"
                  style={{ 
                    backgroundColor: colors.surfaceContainer,
                    borderColor: `${colors.outlineVariant}50`,
                    color: colors.primary
                  }}
                >
                  {topic.warning}
                </div>
              </PrimaryCard>
            );
          })}
        </div>

      </div>
    </AppShell>
  );
};

export default LearnPage;
