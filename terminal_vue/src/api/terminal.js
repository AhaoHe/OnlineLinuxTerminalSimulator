import request from '@/utils/request'

export function getCommandDesc(command) {
    return request({
      url: '/command/findLikeCommand',
      method: 'get',
      //params: { username , token }
      params: { command }
    })
}
export function getGroupUsers(query) {
    return request({
      url: '/user/groupusers',
      method: 'get',
      params: { query:JSON.stringify(query) }
    })
}