import { getToken, setToken, removeToken, getRole, setRole ,
     removeRole , getUsername , setUsername , removeUsername,getGName,setGName,removeGName,
     getLevel,setLevel,removeLevel ,getGroupId,setGroupId,removeGroupId,removeRouter} from '@/utils/auth'
import { login, logout, getInfo } from '@/api/user'
import router , { resetRouter } from '@/router'

//import { generaMenu } from '@/utils'

export default {
    namespaced: true,
    state:{
        username: getUsername(),
        roles: getRole(),
        token: getToken(),
        groupId: getGroupId(),
        gname:getGName(),
        level:getLevel(),
        menu_list: []
    },
    mutations:{
        setLoginMsg(state,payload){
            state.username = payload.username;
            state.role = payload.auth;
            state.token = payload.token;
            state.menu_list = payload.menus
        },
        SET_USERNAME: (state, username) => {
            state.username = username
        },
        SET_ROLES: (state, roles) => {
            state.roles = roles
        },
        SET_TOKEN: (state, token) => {
            state.token = token
        },
        SET_MENULIST: (state, menu_list) => {
            state.menu_list = menu_list
        },
        SET_LEVEL: (state, level) => {
            state.level = level
        },
        SET_GOUPID: (state, groupId) => {
            state.groupId = groupId
        },
        SET_GNAME: (state, gname) => {
            state.gname = gname
        },
    },
    actions:{
        // user login
        login({ commit }, userInfo) {
            const { username, password } = userInfo
            return new Promise((resolve, reject) => {
            login({ username: username.trim(), password: password }).then(response => {
                const data = response
            
                if (!data) {
                    reject('验证失败，请重新登录!')
                }
                if(data.code == 200){
                    const { username , token } = data.object;
                    commit('SET_TOKEN', token);
                    commit('SET_USERNAME', username);
                    setToken(token);
                    setUsername(username);
                }
                resolve()
            }).catch(error => {
                reject(error)
            })
            })
        },

                // get user info
        getInfo({ commit, state }) {
            return new Promise((resolve, reject) => {
            getInfo(state.username).then(response => {
                    const data = response
                    
                    if (!data) {
                        reject('未获得数据，请重新登录')
                    }

                    const { roles, menus,groupId,gname,level } = data.object

                    // roles must be a non-empty array
                    if (!roles) {
                        reject('getInfo: 角色不能为空!')
                    }

                     //转化为vue-router可用菜单
                    /* let routers = [];
                    generaMenu(routers,menus)
                    console.log(routers)
                    let menu_list = generaMenu(routers,menus);
                    let menu_list = routers; */

                    commit('SET_ROLES', roles)
                    commit('SET_LEVEL', level)
                    commit('SET_GOUPID', groupId)
                    commit('SET_GNAME', gname)
                    setRole(roles);
                    setLevel(level);
                    setGroupId(groupId);
                    setGName(gname);
                    resolve(menus)
                }).catch(error => {
                    reject(error)
                })
            })
        },

        // user logout
        logout({ commit, state, dispatch }) {
            return new Promise((resolve, reject) => {
            logout(state.token).then(() => {
                dispatch('resetToken')
                resetRouter()

                // reset visited views and cached views
                // to fixed https://github.com/PanJiaChen/vue-element-admin/issues/2485
                dispatch('tagsView/delAllViews', null, { root: true })

                resolve("退出成功")
            }).catch(error => {
                reject(error)
            })
            })
        },

        // remove token
        //初始化信息
        resetToken({ commit }) {
            return new Promise(resolve => {
                commit('SET_TOKEN', '')
                commit('SET_ROLES', [])
                commit('SET_LEVEL', 0)
                commit('SET_GOUPID', 0)
                commit('SET_GNAME', '')
                removeUsername()
                removeToken()
                removeRole()
                removeLevel()
                removeGroupId()
                removeGName()
                removeRouter()
                resolve()
            })
        },

    }
}
