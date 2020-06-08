import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
// 导入 ElementUI
import ElementUI from 'element-ui'

//import 'normalize.css/normalize.css' // a modern alternative to CSS resets
import './styles/element-variables.scss'
import '@/styles/index.scss'// global css

import './icons' // icon
import './permission'

Vue.config.productionTip = false
// 安装 ElementUI
Vue.use(ElementUI);

var axios = require('axios');
axios.defaults.baseURL = 'http://localhost:8080/'
//将API方法绑定到全局
Vue.prototype.$axios = axios

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
