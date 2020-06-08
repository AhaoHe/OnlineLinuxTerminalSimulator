import request from '@/utils/request'

export function getGroupList(level,groupId) {
    return request({
      url: '/group/info',
      method: 'get',
      params:{
        level,groupId
      }
    })
}
export function SaveOrUpdate(info) {
    return request({
      url: '/group/info',
      method: 'post',
      params:{
        info:JSON.stringify(info)
      }
    })
}
export function delGroup(groupId) {
    return request({
      url: '/group/info',
      method: 'delete',
      params:{
        groupId
      }
    })
}

export function serverList() {
    return request({
      url: '/group/server',
      method: 'get',
    })
}
// 验证组名
export function validateName(groupName,serverId) {
    return request({
      url: '/group/exist',
      method: 'get',
      params:{
        groupName,serverId
      }
    })
}

export function UnChecked(groupVo) {
    return request({
      url: '/group/userunchecked',
      method: 'get',
      params:{
        groupVo
      }
    })
}
export function ApplyUnChecked(groupId,search) {
    return request({
      url: '/group/applyunchecked',
      method: 'get',
      params:{
        groupId,search
      }
    })
}
export function Checked(groupVo) {
    return request({
      url: '/group/userchecked',
      method: 'get',
      params:{
        groupVo
      }
    })
}


export function AddTo(strArr,groupId) {
    let formData = new FormData();
    formData.append("strArr",JSON.stringify(strArr))
    formData.append("groupId",groupId)
    return request({
      url: '/group/addUser',
      method: 'post',
      data:formData
    })
}
export function RemoveFrom(strArr,groupId) {
    let formData = new FormData();
    formData.append("strArr",JSON.stringify(strArr))
    formData.append("groupId",groupId)
    return request({
      url: '/group/removeUser',
      method: 'post',
      data:formData
    })
}


//申请组的API
export function applyGroupList(username,groupId) {
    return request({
      url: '/group/apply',
      method: 'get',
      params:{username,groupId}
    })
}
export function applyGroup(groupId,username) {
    return request({
      url: '/group/apply',
      method: 'post',
      params:{groupId,username}
    })
}

export function delApply(groupId,usernames) {
    return request({
      url: '/group/apply',
      method: 'delete',
      params:{groupId,usernames}
    })
}