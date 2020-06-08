import request from '@/utils/request'

export function getAPILabel() {
    return request({
      url: '/api/label',
      method: 'get',
    })
}

export function setAPILabel(apiVo) {
    console.log(apiVo)
    return request({
      url: '/api/label',
      method: 'post',
      data:{apiVo:JSON.stringify(apiVo)},
    })
}

export function delAPILabel(ids) {
    console.log(ids.join(','))
    return request({
      url: '/api/label',
      method: 'delete',
      params:{ids:ids.join(',')},
    })
}


export function setApiData(api) {
    return request({
      url: '/api/data',
      method: 'post',
      params:{api:JSON.stringify(api)}
    })
}

export function getApiData(id) {
    return request({
      url: '/api/data',
      method: 'get',
      params:{id:id}
    })
}