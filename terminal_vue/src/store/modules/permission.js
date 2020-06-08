import { constantRoutes , asyncRoutes } from '@/router'
import Layout from '@/layout/index'
import router from '@/router'
import store from '@/store'
import { getRouter , setRouter} from '@/utils/auth'

export const filterMenus = (routers) => { // 遍历后台传来的路由字符串，转换为组件对象
    const accessedRouters = routers.filter(router => {
      if (router.component) {
        if (router.component === 'Layout') { // Layout组件特殊处理
          router.component = Layout
        } else {
          const component = router.component
          router.component = loadView(component)
        }
      }
      if (router.children && router.children.length) {
        router.children = filterMenus(router.children)
      }
      return true
    })
    return accessedRouters
  }
export const loadView = (view) => { // 路由懒加载
    return () => import(`@/views/${view}`)
  }

const state = {
    routes: [],
    addRoutes: []
}

const mutations = {
    SET_ROUTES: (state, routes) => {
      state.addRoutes = routes
      state.routes = constantRoutes.concat(routes)
      setRouter(routes)
    }
}

const actions = {
    GenerateRoutes({ commit }, data){
        return new Promise(resolve => {
            console.log(data)
            let routers = []
            //routers = generaMenu(data)
            routers = filterMenus(data)
            //routers = MenuTree(null,routers,data)
            //data = initDataNew(data)
            //routers = MenuTree(0,routers,data)
            console.log(routers);
            store.commit('user/SET_MENULIST', routers)
            store.commit('permission/SET_ROUTES',routers)
            router.addRoutes(routers);
            router.addRoutes(asyncRoutes);
            
            resolve()
        })
    }
}

export default {
    namespaced: true,
    state,
    mutations,
    actions
}




/*以下为废弃方案*/ 
function generaMenu(data){
    let routers = [];
    try{
        data.forEach((item) => {
            let menu = {
                name: item.menuName,
                path: item.url,
                component: !item.url ?  Layout : () => import(`@/views${item.component}`),
                meta:{
                    title: item.menuName,
                    icon: item.icon,
                    pid: item.pid
                },
                children: [],
            }
            routers.push(menu);
        });
        routers.forEach(item => {
            if(item.pid !== null){
                var index = data.findIndex(item1 => item1.id === item.meta.pid)
                if(index !== -1){
                    routers[index].children = routers[index].children || []
                    routers[index].children.push(item)
                    //为什么前面推入的数据会随着 后继续同样位置推入的数据变化
                }
            }
        })
        return routers.filter(item => item.meta.pid === null)
    }catch(error){
        console.log(error);
    }
}

//获得菜单树
function MenuTree(id,routers,data){
    //树顶点的pid为null
    try{
        let newArr = data.filter(item=> item.meta.pid === id );
        newArr.forEach(item=>{
            let arr = [];
            item.children= item.children.concat(MenuTree(item.meta.id,arr,data))
            routers.push(item);
        });
    }catch(error){
        console.log(error);
    }
    return routers;
}

//将菜单数据初始化为router格式的对象
function initData(data){
    let routers = []
    data.forEach((item) => {
        let menu ={};
        if(item.component!=null && item.pid==null){
            menu={
                name: item.menuName,
                path: item.url,
                redirect: item.url+'/index',
                component: Layout,
                meta: {
                    pid: item.pid,
                    id: item.id,
                },
                children: [{
                    path: 'index',
                    name: item.menuName+'Child',
                    component: () => import(`@/views${item.component}`), 
                    meta:{
                        title : item.menuName,
                        icon : item.icon
                    },
                }],
            }
        }else{
            menu={
                name: item.menuName,
                path: item.url,
                component: !item.component ?  Layout : () => import(`@/views${item.component}`),
                meta:{
                    pid: item.pid,
                    id: item.id,
                    title : item.menuName,
                    icon : item.icon
                },
                children: [],
            }
        }
        if(item.redirect && item.redirect !== null)
            menu.redirect = item.redirect;
        if(item.hidden && item.hidden !== true)
            menu.hidden = item.hidden            
        routers.push(menu);
    });
    //console.log(routers)
    return routers
}

function initDataOld(data){
    let routers = []
    data.forEach((item) => {
        let menu = {
            
            path: item.url,
            component: item.component == 'Layout' ?  Layout : () => import(`@/views${item.component}`),
            meta:{
                pid: item.pid,
                id: item.id,
                title : item.menuName,
                icon : item.icon
            },
            children: [],
        }
        if(item.menuName && item.menuName !== '')
            menu.name = item.menuName
        if(item.redirect && item.redirect !== null)
            menu.redirect = item.redirect;
        if(item.hidden && item.hidden == true)
            menu.hidden = item.hidden            
        if(item.affix && item.affix == true)
            menu.affix = item.affix            
        routers.push(menu);
    });
    return routers
}