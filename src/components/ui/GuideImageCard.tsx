import React from 'react';
import { colors } from '../../styles/theme';

interface GuideImageCardProps {
  title: string;
  category: string;
  duration?: string;
  onClick: () => void;
  imageUrl?: string;
}

export const GuideImageCard: React.FC<GuideImageCardProps> = ({
  title,
  category,
  duration = '5 min read',
  onClick,
  imageUrl,
}) => {
  return (
    <div
      onClick={onClick}
      className="relative rounded-3xl overflow-hidden shadow-gentle-sm border cursor-pointer group active:scale-[0.99] transition-transform duration-200 h-48 w-full"
      style={{ borderColor: colors.outlineVariant }}
    >
      {/* Background Graphic Placeholder */}
      <div 
        className="absolute inset-0 bg-cover bg-center transition-transform duration-700 group-hover:scale-105"
        style={{ 
          backgroundImage: imageUrl ? `url(${imageUrl})` : 'none',
          backgroundColor: colors.surfaceContainerHighest,
        }}
      />

      {/* Decorative vector shape background if no image */}
      {!imageUrl && (
        <div className="absolute inset-0 opacity-20 pointer-events-none flex items-center justify-center">
          <span className="material-symbols-outlined text-[100px] text-[#8a4d4e] select-none">auto_stories</span>
        </div>
      )}

      {/* Elegant Gradient Scrim overlay from Rose dark to solid shadow */}
      <div className="absolute inset-0 bg-gradient-to-t from-black/80 via-black/40 to-transparent" />

      {/* Floating Category Tag */}
      <div 
        className="absolute top-4 left-4 px-3 py-1 rounded-full text-[10px] font-bold uppercase tracking-wider shadow-sm"
        style={{ backgroundColor: colors.secondaryContainer, color: colors.secondary }}
      >
        {category}
      </div>

      {/* Bottom Info text inside card bounds */}
      <div className="absolute bottom-4 left-4 right-4 flex flex-col justify-end">
        <h3 className="text-white text-base font-bold tracking-tight mb-1 font-heading leading-snug drop-shadow-sm">
          {title}
        </h3>
        <div className="flex items-center gap-1.5 text-white/80 text-[11px] font-medium">
          <span className="material-symbols-outlined text-[12px]">schedule</span>
          <span>{duration}</span>
        </div>
      </div>
    </div>
  );
};
