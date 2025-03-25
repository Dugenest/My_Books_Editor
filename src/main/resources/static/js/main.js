import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import './assets/main.css';

// Configuration des feature flags Vue.js
window.__VUE_PROD_HYDRATION_MISMATCH_DETAILS__ = false;
window.__VUE_OPTIONS_API__ = true;
window.__VUE_PROD_DEVTOOLS__ = false;

const app = createApp(App);
app.use(router);
app.use(store);
app.mount('#app'); 