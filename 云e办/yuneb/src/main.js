import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import store from './store/index.js'
import { initMenu } from './utils/menus.js'
import  {getRequest} from './utils/api.js'
//图标样式
import 'font-awesome/css/font-awesome.css'
import { ElMessage } from 'element-plus'
import vuex from 'vuex'

const app = createApp(App)
app.use(router)
app.use(store)
app.use(vuex)
app.use(ElementPlus)
app.mount('#app')


//全局导航守卫 (路由跳转时加载菜单)
router.beforeEach((to,from,next)=>{
    if(window.sessionStorage.getItem('tokenStr')){
      //加载左侧菜单
      initMenu(router,store);
      //判断用户信息是否存在
      if(!window.sessionStorage.getItem('user')){
        return getRequest('/info').then(resp => {
          if(resp){
            //存入用户信息
            window.sessionStorage.setItem('user',JSON.stringify(resp.data.details));
            store.commit('initadmin',resp.data.details);
            next();
          }
        })
      }
      next();
    }else {
      if(to.path == '/'){
        next();
      }else {
        next('/?redirect='+to.path);
      alert("请先登录");
      }
    }
  })