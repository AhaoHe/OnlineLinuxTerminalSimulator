import request from '@/utils/request'


export function getRoleInfo() {
    return request({
      url: '/role/info',
      method: 'get',
    })
}
export function addRole(info) {
    return request({
      url: '/role/info',
      method: 'post',
      params:{info:JSON.stringify(info)}
    })
}
export function updateRole(info) {
    return request({
      url: '/role/info',
      method: 'put',
      params:{info:JSON.stringify(info)}
    })
}
export function deleteRole(id) {
    return request({
      url: '/role/info',
      method: 'delete',
      params:{id}
    })
}

export function getRoleMenuChecked(roleId) {
  return request({
    url: '/role/rolemenu',
    method: 'get',
    params:{
      roleId
    }
  })
}

export function addRoleMenu(menuIds,roleId) {
    console.log(menuIds.join(','))
    return request({
      url: '/role/rolemenu',
      method: 'post',
      params:{
        menuIds:menuIds.join(','),
        roleId:roleId
      }
    })
}


export function getRoleAPIChecked(roleId) {
  return request({
    url: '/role/roleapi',
    method: 'get',
    params:{roleId:roleId}
  })
}

export function addRoleAPI(apiIds,roleId) {
  return request({
    url: '/role/roleapi',
    method: 'post',
    params:{
      apiIds:apiIds.join(','),
      roleId:roleId
    }
  })
}