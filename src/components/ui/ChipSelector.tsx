import React from 'react';
import { colors } from '../../styles/theme';

interface ChipOption {
  id: string;
  label: string;
  icon?: string;
}

interface ChipSelectorProps {
  label?: string;
  options: ChipOption[];
  selectedIds: string[];
  onChange: (selectedIds: string[]) => void;
  singleSelect?: boolean;
}

export const ChipSelector: React.FC<ChipSelectorProps> = ({
  label,
  options,
  selectedIds,
  onChange,
  singleSelect = false,
}) => {
  const handleToggle = (id: string) => {
    if (singleSelect) {
      onChange([id]);
    } else {
      if (selectedIds.includes(id)) {
        onChange(selectedIds.filter((x) => x !== id));
      } else {
        onChange([...selectedIds, id]);
      }
    }
  };

  return (
    <div className="flex flex-col gap-2 w-full">
      {label && (
        <span 
          className="text-xs font-extrabold uppercase tracking-wide px-1"
          style={{ color: colors.onSurfaceVariant }}
        >
          {label}
        </span>
      )}

      <div className="flex flex-wrap gap-2 w-full">
        {options.map((option) => {
          const isSelected = selectedIds.includes(option.id);
          return (
            <button
              key={option.id}
              onClick={() => handleToggle(option.id)}
              className="px-4 py-2.5 rounded-full border text-xs font-bold transition-all duration-200 flex items-center gap-1.5 focus:outline-none focus:ring-2 focus:ring-[#8a4d4e]/40"
              style={{
                backgroundColor: isSelected ? colors.primary : '#ffffff',
                borderColor: isSelected ? colors.primary : colors.outlineVariant,
                color: isSelected ? '#ffffff' : colors.onSurface,
              }}
            >
              {option.icon && (
                <span className="material-symbols-outlined text-sm font-bold">
                  {option.icon}
                </span>
              )}
              <span>{option.label}</span>
            </button>
          );
        })}
      </div>
    </div>
  );
};
