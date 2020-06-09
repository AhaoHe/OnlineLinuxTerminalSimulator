import request from '@/utils/request'
import {dateFormat} from '@/utils/format'

export function getChartData(days) {
    return request({
      url: '/home/echarts',
      method: 'get',
      params:{
          time:dateFormat('YYYY-mm-dd',new Date()),
          days
      }
    })
}

export function getUserChart(days,username) {
    return request({
      url: '/home/usercharts',
      method: 'get',
      params:{
          time:dateFormat('YYYY-mm-dd',new Date()),
          days:days,
          username:username
      }
    })
}

export function getHistoryCommand(query) {
    return request({
      url: '/home/usercommand',
      method: 'get',
      params:{
          query:JSON.stringify(query)
      }
    })
}


