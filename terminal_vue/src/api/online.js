import request from '@/utils/request'



export function onlineCount() {
    return request({
      url: '/home/onlinecount',
      method: 'get'
    })
}

export function offline(username) {
    return request({
      url: '/home/online',
      method: 'delete',
      params:{
        username
      }
    })
}

export function banUser(username) {
    return request({
      url: '/home/online',
      method: 'put',
      params:{
        username
      }
    })
}
export function UnbanUser(username) {
    return request({
      url: '/home/ban',
      method: 'put',
      params:{
        username
      }
    })
}

export function dfh(username) {
    return request({
      url: '/home/dfh',
      method: 'get',
      params:{
        username
      }
    })
}

export function disk(current,size,search,level,groupId) {
    return request({
      url: '/home/disk',
      method: 'get',
      params:{
        current,size,search,level,groupId
      }
    })
}

export function diskuser(uid,username) {
    return request({
      url: '/home/diskuser',
      method: 'get',
      params:{
        uid,username
      }
    })
}

export function updateDisk(uid,username,disk) {
    return request({
      url: '/home/disk',
      method: 'put',
      params:{
        uid,username,disk
      }
    })
}

export function getUserList(query) {   
    return request({
      url: '/home/online',
      method: 'get',
      params:{
        query:JSON.stringify(query)
      }
    })
}
export function getBanUserList(query) {
    return request({
      url: '/home/ban',
      method: 'get',
      params:{
        query:JSON.stringify(query)
      }
    })
}