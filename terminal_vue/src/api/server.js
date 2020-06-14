import request from '@/utils/request'

export function getServerInfo() {
    return request({
      url: '/server/info',
      method: 'get',
    })
}

export function addServerInfo(server) {
    return request({
      url: '/server/info',
      method: 'post',
      params: { server: JSON.stringify(server) }
    })
}

export function updateServerInfo(server) {
    return request({
      url: '/server/info',
      method: 'put',
      params: { server: JSON.stringify(server) }
    })
}

export function deleteServerInfo(id) {
    return request({
      url: '/server/info',
      method: 'delete',
      params: { id: id }
    })
}

export function setRegister(id) {
    return request({
      url: '/server/register',
      method: 'put',
      params: { id: id }
    })
}


export function getUsersInfo(query) {
    return request({
      url: '/server/severusers',
      method: 'get',
      params: { query: JSON.stringify(query) }
    })
}

export function getSelection(serverId) {
    return request({
      url: '/server/selection',
      method: 'get',
      params: { serverId }
    })
}


export function toNewServer(users,serverIdOld,serverIdNew) {
    let formData = new FormData();
    formData.append("users",JSON.stringify(users))
    formData.append("serverIdOld",serverIdOld)
    formData.append("serverIdNew",parseInt(serverIdNew))
    return request({
      url: '/server/toNewServer',
      method: 'post',
      data : formData
    })
}

