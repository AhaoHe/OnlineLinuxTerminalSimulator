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

