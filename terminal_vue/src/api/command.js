import request from '@/utils/request'

export function getCommandInfo(info) {
    return request({
      url: '/command/info',
      method: 'get',
      params:{
          info:JSON.stringify(info)
      }
    })
}
export function addCommandInfo(info) {
    return request({
      url: '/command/info',
      method: 'post',
      params:{
          info:JSON.stringify(info)
      }
    })
}
export function updateCommandInfo(info) {
    return request({
      url: '/command/info',
      method: 'put',
      params:{
          info:JSON.stringify(info)
      }
    })
}
export function delCommandInfo(id) {
    return request({
      url: '/command/info',
      method: 'delete',
      params:{
          id
      }
    })
}

export function changeStatus(selection,unselection) {
    return request({
      url: '/command/change',
      method: 'post',
      params:{
        selection,unselection
      }
    })
}

export function getGroupCommand(groupId) {
    return request({
      url: '/command/groupcommand',
      method: 'get',
      params:{
        groupId
      }
    })
}

export function changeGroupCommand(groupId,selection,unselection) {
    return request({
      url: '/command/command',
      method: 'post',
      params:{
        groupId,selection,unselection
      }
    })
}