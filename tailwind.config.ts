import type { Config } from 'tailwindcss';

const config: Config = {
  content: [
    './src/**/*.{js,ts,jsx,tsx}',
  ],
  theme: {
    extend: {
      colors: {
        background: '#fff8f7',
        surface: '#fff8f7',
        primary: {
          DEFAULT: '#8a4d4e',
          container: '#d48c8c',
        },
        secondary: {
          DEFAULT: '#5d5a84',
          container: '#d1ccfe',
        },
        tertiary: {
          DEFAULT: '#8d4d39',
          container: '#d88c73',
        },
        error: {
          DEFAULT: '#ba1a1a',
          container: '#ffdad6',
        },
        outline: {
          DEFAULT: '#857372',
          variant: '#d7c2c1',
        },
        on: {
          surface: '#201a1a',
          'surface-variant': '#524343',
        },
        container: {
          low: '#fef1f0',
          DEFAULT: '#f8ebea',
          high: '#f2e5e4',
          highest: '#ece0df',
        },
      },
      fontFamily: {
        sans: ['Manrope', 'sans-serif'],
        heading: ['Plus Jakarta Sans', 'sans-serif'],
      },
      boxShadow: {
        'gentle-sm': '0 1px 2px 0 rgba(138, 77, 78, 0.05)',
        'gentle-md': '0 4px 12px -1px rgba(138, 77, 78, 0.08), 0 2px 4px -1px rgba(138, 77, 78, 0.04)',
        'gentle-lg': '0 12px 24px -3px rgba(138, 77, 78, 0.1), 0 4px 6px -2px rgba(138, 77, 78, 0.05)',
      },
      borderRadius: {
        'xl': '12px',
        '2xl': '16px',
        '3xl': '24px',
      },
    },
  },
  plugins: [],
};

export default config;
