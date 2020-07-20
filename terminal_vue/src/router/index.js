import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router);

import Layout from '@/layout/index'
/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar
    noCache: true                if set true, the page will no be cached(default is false)
    affix: true                  if set true, the tag will affix in the tags-view
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

export const constantRoutes = [
    {
        path: '/redirect',
        component: Layout,
        hidden: true,
        children: [
          {
            path: '/redirect/:path(.*)',
            component: () => import('@/views/redirect/index')
          }
        ]
    },
    {
        // 登录页
        path: '/login',
        name: 'Login',
        component: () => import('@/views/login'),
        hidden: true,
        meta: {
            requireAuth: false
        }
    },
    {
        // 注册页
        path: '/resgister',
        name: 'Resgister',
        component: () => import('@/views/login/register'),
        hidden: true,
        meta: {
            requireAuth: false
        }
    }/* ,
    {
        //权限测试
        path: '/test',
        name: 'TEST',
        redirect: '/child',
        component: Layout,
        children:[
            {
                path: '/child',
                name: 'Child',
                //component: () => import('@/views/main/index'),
                component: () => import('@/views/home/admin/index'),
                meta: {
                    title: '权限测试',
                    icon: 'dashboard',
                },
            }
        ]
    } *//* ,
    {
        //权限测试
        path: '/ws',
        name: 'WebSocket',
        redirect: '/ws/index',
        component: Layout,
        children:[
            {
                path: 'index',
                name: 'websocket',
                component: () => import('@/views/terminal/index'),
                meta: {
                    title: 'WebSocket',
                    icon: 'dashboard',
                },
            }
        ]
    } */,
    {
      path: '/404',
      component: () => import('@/views/NotFound'),
      meta: {
          requireAuth: false
      },
      hidden: true
    }
];

export const asyncRoutes = [
    /* {
        //后台主页
        path: '/main',
        name: 'Main',
        component: () => import('@/views/main/Main'),
        meta: {
            title: '服务器中心',
            roles: [ role[0] ],
            icon: 'lock'
        },
        children:[
            {
                path : '/severcenter',
                name : 'SeverCenter',
                component : () => import('@/views/main/admin/ServerCenter'),
                meta: {
                    title: '服务器中心',
                    roles: [ role[0] ]
                  }
            },
            {
                path : '/servermanage',
                name : 'ServerManage',
                component : () => import('@/views/main/admin/ServerCenter')
            },
            {
                path : '/usermanage',
                name : 'UserManage',
                component : () => import('@/views/main/admin/UserManage')
            }
        ]
    }, */
    // 404 page must be placed at the end !!!
    {
        path: '*',
        redirect: '/404'
    }
]

const createRouter = () => new Router({
    // mode: 'history', // require service support
    scrollBehavior: () => ({ y: 0 }),
    //路由模式
    //hash：路径带 # 符号，如 http://localhost/#/login
    // history：路径不带 # 符号，如 http://localhost/login
    mode: 'history',
    routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
