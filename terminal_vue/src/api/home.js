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


