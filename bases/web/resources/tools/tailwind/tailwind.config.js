module.exports = {
  // check resources path
  // it should be relative to the root of the project
  // but... this certainly seems relative to me!
  content: ["../../../resources/**/*.{html,js,clj,cljs}"],
  theme: {
    extend: {},
  },
  plugins: [
    require('@tailwindcss/forms'),
    require('@tailwindcss/typography')
  ],
}