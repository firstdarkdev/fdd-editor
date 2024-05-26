/** @type {import('tailwindcss').Config} */
export default {
  darkMode: 'class',
  content: ['./index.html', './src/**/*.{vue,js,ts,jsx,tsx}', './node_modules/flowbite/**/*.js'],
  theme: {
    extend: {
      colors: {
        'ct-dark-primary': '#04002A',
        'ct-dark-secondary': 'rgba(112, 125, 155, .05)',
        'ct-light-primary': '#ECEDF0',
        'ct-light-secondary': '#FAFAFA',
        'ct-card-dark': 'rgba(112, 125, 155, .1)',
        'ct-card-light': 'rgba(255, 255, 255, .7)'
      },
      borderColor: {
        'ct-card': 'rgba(255, 255, 255, .1)'
      }
    }
  },
  plugins: [require('flowbite/plugin')]
}