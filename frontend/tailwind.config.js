/* tailwind.config.js */
/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/**/*.{js,jsx,ts,tsx}"],
  theme: {
    extend: {
      boxShadow: {
        'inner-tb': 'inset 0 5px 5px -5px rgba(0, 0, 0, 0.3), inset 0 -5px 5px -5px rgba(0, 0, 0, 0.3)',
        'inner-t': 'inset 0 5px 5px -5px rgba(0, 0, 0, 0.3)', // Inner shadow top only
        'inner-b': 'inset 0 -5px 5px -5px rgba(0, 0, 0, 0.3)', // Inner shadow bottom only
        'inner-tb-strong': 'inset 0 10px 10px -8px rgba(0, 0, 0, 0.5), inset 0 -10px 10px -8px rgba(0, 0, 0, 0.5)',
        'inner-t-strong': 'inset 0 10px 10px -8px rgba(0, 0, 0, 0.5)', // Inner shadow top only
        'inner-b-strong': 'inset 0 -10px 10px -8px rgba(0, 0, 0, 0.5)', // Inner shadow bottom only
      },
    },
  },
  plugins: [],
}