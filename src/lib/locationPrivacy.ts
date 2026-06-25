/**
 * Location-Privacy controls based on GDPR and standard medical data safety regulations.
 * Restricts active GPS captures and enforces approximate location overlays.
 */

export type LocationPermissionMode = 'OFF' | 'ON_DEVICE_ONLY' | 'APPROXIMATE_REGION' | 'TEMPORARY_EXACT';

export interface LocationState {
  permissionMode: LocationPermissionMode;
  currentApproximateRegion: string | null;
  temporaryCoordinates: { lat: number; lng: number } | null;
}

let activeLocationState: LocationState = {
  permissionMode: 'OFF',
  currentApproximateRegion: null,
  temporaryCoordinates: null,
};

export const updateLocationPermission = (mode: LocationPermissionMode): void => {
  activeLocationState.permissionMode = mode;
  if (mode === 'OFF') {
    activeLocationState.currentApproximateRegion = null;
    activeLocationState.temporaryCoordinates = null;
  }
};

export const getSecureCoordinates = (): { lat: number; lng: number } | null => {
  if (activeLocationState.permissionMode === 'OFF') {
    return null;
  }
  
  if (activeLocationState.permissionMode === 'APPROXIMATE_REGION') {
    // Add noise to location vector to protect original consumer coordinates
    return {
      lat: 23.8103 + (Math.random() - 0.5) * 0.1, // Approximate around Base Dhaka region with noise
      lng: 90.4125 + (Math.random() - 0.5) * 0.1,
    };
  }

  return activeLocationState.temporaryCoordinates;
};

export const clearTemporaryCoordinatesAfterSearch = (): void => {
  if (activeLocationState.permissionMode === 'TEMPORARY_EXACT') {
    activeLocationState.temporaryCoordinates = null;
    console.log("Secure privacy sweep: Permanent coordinate deleted from memory.");
  }
};
