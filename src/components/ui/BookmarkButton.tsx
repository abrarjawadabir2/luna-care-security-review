import React from 'react';
import { colors } from '../../styles/theme';

interface BookmarkButtonProps {
  isBookmarked: boolean;
  onToggle: () => void;
  className?: string;
}

export const BookmarkButton: React.FC<BookmarkButtonProps> = ({
  isBookmarked,
  onToggle,
  className = '',
}) => {
  return (
    <button
      onClick={(e) => {
        e.stopPropagation();
        onToggle();
      }}
      className={`p-2 rounded-full hover:bg-neutral-100/80 active:scale-90 transition-all duration-200 focus:outline-none focus:ring-2 focus:ring-[#8a4d4e]/40 ${className}`}
      style={{
        color: isBookmarked ? colors.primary : colors.onSurfaceVariant,
        backgroundColor: isBookmarked ? `${colors.primary}15` : 'transparent'
      }}
      aria-label={isBookmarked ? 'Remove bookmark' : 'Bookmark this item'}
    >
      <span className={`material-symbols-outlined text-2xl ${isBookmarked ? 'fill-1' : ''}`}>
        {isBookmarked ? 'bookmark' : 'bookmark_border'}
      </span>
    </button>
  );
};
