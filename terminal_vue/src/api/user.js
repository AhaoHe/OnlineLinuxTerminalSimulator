import request from '@/utils/request'

export function login(data) {
    return request({
      url: '/login',
      method: 'post',
      data
    })
}

export function getInfo(username) {
    return request({
      url: '/user/info',
      method: 'get',
      //params: { username , token }
      params: { username: username }
    })
}
  
export function logout() {
    return request({
        url: '/logout',
        method: 'post',
    })
}

export function validateEMail(mail) {
    return request({
        url: '/user/validateMail',
        method: 'get',
        params: { mail: mail }
    })
}
export function validateName(username) {
    return request({
        url: '/user/validateUsername',
        method: 'get',
        params: { username: username }
    })
}

export function sendMail(emailAddress) {
    return request({
        url: '/user/email',
        method: 'post',
        params: { emailAddress: emailAddress }
    })
}

export function register(userVo) {
    return request({
        url: '/user/register',
        method: 'post',
        params: userVo
    })
}

export function addUser(userVo) {
    return request({
        url: '/user/info',
        method: 'post',
        params: { userVo:JSON.stringify(userVo)}
    })
}


export function findAllUser(current,size) {
    return request({
        url: '/user/AllUser',
        method: 'get',
        params: {current,size}
    })
}

export function findUserBy(userInfo) {
    console.log(JSON.stringify(userInfo))
    return request({
        url: '/user/userInfo',
        method: 'get',
        params: {userInfo: JSON.stringify(userInfo)}
    })
}

/* export function findLikeUser(username,current,size) {
    return request({
        url: '/user/search',
        method: 'get',
        params: {username,current,size}
    })
} */

export function updateState(username,state) {
    return request({
        url: '/user/state',
        method: 'put',
        params: {username:username,state:state}
    })
}

export function updateStates(usernames,state) {
    return request({
        url: '/user/states',
        method: 'put',
        params: {usernames:usernames.join(','),state:state}
    })
}

export function updateUser(userVo) {
    return request({
        url: '/user/info',
        method: 'put',
        params: {userVo:JSON.stringify(userVo)}
    })
}

export function changeUserRole(uid,roleId,groupId) {
    return request({
        url: '/user/role',
        method: 'put',
        params: {uid,roleId,groupId}
    })
}