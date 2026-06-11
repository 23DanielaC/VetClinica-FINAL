/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{vue,js,ts,jsx,tsx}'],
  theme: {
    extend: {
      colors: {
        primary: '#1565C0',
        'primary-dark': '#0D47A1',
        'primary-light': '#1976D2',
        accent: '#FFB300',
        'accent-dark': '#FF8F00',
        base: '#ECEFF1',
      }
    }
  },
  plugins: []
}
