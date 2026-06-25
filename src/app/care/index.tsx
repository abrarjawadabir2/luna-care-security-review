import React, { useState } from 'react';
import { colors } from '../../styles/theme';
import { AppShell } from '../../components/layout/AppShell';
import { PrimaryCard } from '../../components/ui/PrimaryCard';
import { BookmarkButton } from '../../components/ui/BookmarkButton';
import { WarningCard } from '../../components/ui/WarningCard';
import { FormField } from '../../components/ui/FormField';
import { PrimaryButton } from '../../components/ui/PrimaryButton';
import { clearTemporaryCoordinatesAfterSearch, updateLocationPermission } from '../../lib/locationPrivacy';

interface CareProps {
  onNavigate: (route: string) => void;
}

export const CarePage: React.FC<CareProps> = ({ onNavigate }) => {
  const [gpsMode, setGpsMode] = useState<'OFF' | 'TEMPORARY_SEARCH_ACTIVE'>('OFF');
  const [locationInput, setLocationInput] = useState('');
  const [clinics, setClinics] = useState<string[]>([]);
  const [bookmarkedProducts, setBookmarkedProducts] = useState<string[]>([]);

  const productsList = [
    {
      id: 'cup-silicone',
      title: 'Premium Menstrual Cup',
      benefit: 'Reusable physical protection, eco-safe up to 10 years. Hypoallergenic medical silicon.',
      advice: 'Ensure hands are boiled/cleaned. Boil cup for 5 minutes during initial setup.',
    },
    {
      id: 'panties-cotton',
      title: 'Comfortable Cotton Underwear',
      benefit: 'High airflow reduces localized fungal risks or severe yeast concerns.',
      advice: 'Prefer organic un-dyed cotton blends during high-flow cycles.',
    },
    {
      id: 'bag-comfort',
      title: 'Therapeutic Hot Water Bag',
      benefit: 'Simple thermal relaxation cushions sudden lower abdomen uterine muscle contractions.',
      advice: 'Never place extremely boiling water directly onto skin. Wrap in cotton towel.',
    },
    {
      id: 'dispensary-pads',
      title: 'Organic Bamboo Pads',
      benefit: 'Biodegradable soft pads, super absorbing capacity with zero chemical scents.',
      advice: 'Replace pads every 4 to 6 hours to maintain absolute genital health safety.',
    },
  ];

  const handleProductBookmark = (id: string) => {
    if (bookmarkedProducts.includes(id)) {
      setBookmarkedProducts(bookmarkedProducts.filter((x) => x !== id));
    } else {
      setBookmarkedProducts([...bookmarkedProducts, id]);
    }
  };

  const handleTriggerGpsPrivacySearch = () => {
    // 1. Explicit GPS Consent activation
    updateLocationPermission('TEMPORARY_EXACT');
    setGpsMode('TEMPORARY_SEARCH_ACTIVE');
    
    // Simulate finding local Dhaka dispensaries/clinics
    setLocationInput('📍 Banani Road 11 approximate quadrant detected.');
    setClinics([
      '🏥 Dhaka Central Care Pharmacy - 0.4 km away',
      '🏥 Dhanmondi Women Care Dispensary - 1.2 km away',
      '🏥 Gulshan Maternity Clinic - 2.8 km away',
    ]);

    // 2. Immediate secure coordinate memory clearing for security
    setTimeout(() => {
      clearTemporaryCoordinatesAfterSearch();
      console.log("Secure location privacy: Coordinates sweeped.");
    }, 2000);
  };

  return (
    <AppShell>
      <div className="flex flex-col gap-5 py-4 w-full animate-fade-in">
        
        {/* Header view */}
        <div className="flex items-center justify-between px-1">
          <div className="flex flex-col">
            <h2 className="text-2xl font-extrabold" style={{ color: colors.primary, fontFamily: 'Plus Jakarta Sans' }}>
              Care Shop & Dispensaries
            </h2>
            <p className="text-xs" style={{ color: colors.onSurfaceVariant }}>
              Sovereign product directory & GPS privacy finder
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

        {/* SEC 1: GPS PRIVACY ADVISORY BANNER */}
        <WarningCard
          variant="info"
          title="Dual Location Safety Center"
          description="GPS tracking is strictly OFF by default. We do not store historic physical path tracks. Location coordinates exist momentarily inside local RAM only during active searches."
        />

        {/* SEC 2: LOCAL GPS CLINIC FINDER */}
        <PrimaryCard className="flex flex-col gap-3">
          <h3 className="text-sm font-bold flex items-center gap-1.5" style={{ color: colors.primary }}>
            <span className="material-symbols-outlined text-base">near_me</span>
            Find Nearby Pharmacy / Dispensary
          </h3>

          {gpsMode === 'OFF' ? (
            <div className="flex flex-col gap-2 pt-1">
              <p className="text-xs text-neutral-500 leading-relaxed">
                App location permission is currently offline. Ready to browse secure nearby maps?
              </p>
              <PrimaryButton
                label="Request Location (Exact Temporary Search)"
                icon="location_searching"
                onClick={handleTriggerGpsPrivacySearch}
              />
            </div>
          ) : (
            <div className="flex flex-col gap-3 animate-fade-in">
              <div className="p-3.5 rounded-xl bg-green-50 text-[11px] font-bold text-green-700">
                {locationInput}
              </div>
              
              <div className="flex flex-col gap-2">
                <span className="text-[10px] font-extrabold uppercase text-neutral-400">Available safe regional centers:</span>
                {clinics.map((clinic, index) => (
                  <div key={index} className="text-xs font-semibold p-3 border rounded-xl bg-white/80" style={{ borderColor: colors.outlineVariant }}>
                    {clinic}
                  </div>
                ))}
              </div>

              <button
                onClick={() => {
                  setGpsMode('OFF');
                  setClinics([]);
                  setLocationInput('');
                }}
                className="text-xs text-rose-600 font-extrabold focus:outline-none hover:underline self-end"
              >
                Clear Search Result & Revoke Coordinates 🔒
              </button>
            </div>
          )}
        </PrimaryCard>

        {/* SEC 3: CARE PRODUCTS DIRECTORY LIST */}
        <div className="flex flex-col gap-3 pt-2">
          <h4 className="text-xs font-extrabold tracking-wider uppercase px-1 text-neutral-500">
            Care Essentials Directory
          </h4>

          <div className="flex flex-col gap-4">
            {productsList.map((product) => {
              const isBookmarked = bookmarkedProducts.includes(product.id);
              return (
                <PrimaryCard key={product.id} className="flex flex-col gap-2">
                  <div className="flex justify-between items-start gap-2">
                    <h4 className="text-sm font-extrabold" style={{ color: colors.onSurface }}>
                      {product.title}
                    </h4>
                    <BookmarkButton
                      isBookmarked={isBookmarked}
                      onToggle={() => handleProductBookmark(product.id)}
                    />
                  </div>
                  
                  <p className="text-xs leading-relaxed" style={{ color: colors.onSurfaceVariant }}>
                    {product.benefit}
                  </p>

                  <div className="text-[10px] leading-relaxed p-3 rounded-xl border border-dashed text-neutral-500" style={{ borderColor: `${colors.outlineVariant}80` }}>
                    ⚠️ <span className="font-bold">Sanitary advice:</span> {product.advice}
                  </div>
                </PrimaryCard>
              );
            })}
          </div>
        </div>

      </div>
    </AppShell>
  );
};

export default CarePage;
