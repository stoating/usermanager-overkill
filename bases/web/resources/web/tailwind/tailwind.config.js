module.exports = {
  content: ["./bases/web/resources/web/**/*"],
  theme: {
    extend: {},
  },
  plugins: [
    require('@tailwindcss/forms'),
    require('@tailwindcss/typography')
  ],
}