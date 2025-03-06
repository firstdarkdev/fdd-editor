import './assets/css/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import vSelect from 'vue-select'
import { install as DiscordMessageComponents } from '@discord-message-components/vue'

import App from './App.vue'
import router from './router'
import 'animate.css/animate.css'
import 'vue-select/dist/vue-select.css'
import 'sweetalert2/dist/sweetalert2.css'
import 'highlight.js/styles/stackoverflow-dark.css'
import '@discord-message-components/vue/dist/style.css'
import '../node_modules/flowbite-vue/dist/index.css'

import hljs from 'highlight.js/lib/core';
import yaml from 'highlight.js/lib/languages/yaml';
import hljsVuePlugin from "@highlightjs/vue-plugin";

hljs.registerLanguage('toml', yaml);

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(hljsVuePlugin)
app.use(DiscordMessageComponents, {
  defaultTheme: 'light'
})
app.component('v-select', vSelect)

app.mount('#app')
