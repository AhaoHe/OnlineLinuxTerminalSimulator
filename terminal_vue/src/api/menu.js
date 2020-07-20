import request from '@/utils/request'

export function getMenuLabel() {
    return request({
      url: '/menu/label',
      method: 'get',
    })
}

export function setMenuData(menu) {
    return request({
      url: '/menu/data',
      method: 'post',
      params:{menu:JSON.stringify(menu)}
    })
}

export function getMenuData(id) {
    return request({
      url: '/menu/data',
      method: 'get',
      params:{id:id}
    })
}

export function setMenuLabel(menuVo) {
    console.log(menuVo)
    return request({
      url: '/menu/label',
      method: 'post',
      data:{menuVo:JSON.stringify(menuVo)},
    })
}

export function delMenuLabel(ids) {
    console.log(ids.join(','))
    return request({
      url: '/menu/label',
      method: 'delete',
      params:{ids:ids.join(',')},
    })
}