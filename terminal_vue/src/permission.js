import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from '@/utils/auth' // get token from cookie
import getPageTitle from '@/utils/get-page-title'

NProgress.configure({ showSpinner: false }) // NProgress Configuration

//const whiteList = ['/login', '/resgister']

router.beforeEach(async(to, from, next) => {
    // 开始进度条
    NProgress.start()
    //设置标题
    document.title = getPageTitle(to.meta.title);
    // 验证是否含token、是否含角色权限信息，
    // 没有则返回登录页，有但无页面则返回404页面。
    const hasToken = getToken();
    let flag=0;
    if(hasToken){
        if (to.path === '/login') {
            // if is logged in, redirect to the home page
            next({ path: '/' })
            NProgress.done()
        } else {
            // determine whether the user has obtained his permission roles through getInfo
            const hasRoles = store.getters.roles && store.getters.roles.length > 0
            const hasRoute = store.getters.permission_routes && store.getters.permission_routes.length > 0
            if(hasRoles && hasRoute){
                next()
            }else{
                try{
                    const menus = await store.dispatch('user/getInfo');
                    await store.dispatch('permission/GenerateRoutes',menus)
                    flag++
                    /* router.addRoutes(menu_list);
                    router.addRoutes(asyncRoutes); */ 
    
                    // hack method to ensure that addRoutes is complete
                    // set the replace: true, so the navigation will not leave a history record
                    next({ ...to, replace: true })
                }catch(error){
                    // remove token and go to login page to re-login
                    await store.dispatch('user/resetToken')
                    Message.error(error || 'Has Error');
                    next({ path: '/login'})
                    NProgress.done()
                }
            }
        }
    }else if(to.meta.requireAuth!==undefined && !to.meta.requireAuth){
        //如果requireAuth 为false 则直接进入路由
        next();
    }else{
        //如果路径不是白名单内的,而且又没有登录,就跳转登录页面
        /* if (whiteList.indexOf(to.path) !== -1) {
            // in the free login whitelist, go directly
            next()
        }else{
            next('/login')
            NProgress.done() // 结束Progress
        }  */
        next('/login')
        NProgress.done() 
    }
});

router.afterEach(() => {
    // finish progress bar
    NProgress.done()
});