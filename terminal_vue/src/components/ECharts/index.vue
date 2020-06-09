<template>
    <div :class="className" :style="{height:height,width:width}" />
</template>
<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme

export default {
    props: {
        className: {
        type: String,
        default: 'chart'
        },
        width: {
        type: String,
        default: '100%'
        },
        height: {
        type: String,
        default: '350px'
        },
        autoResize: {
        type: Boolean,
        default: true
        },
        chartData: {
        type: Object,
        required: true
        }
    },
    data() {
        return {
        chart: null
        }
    },
    watch: {
        chartData: {
            deep: true,
            handler(val) {
                this.setOptions(val)
            }
        }
    },
    mounted() {
        this.$nextTick(() => {
        this.initChart()
        })
    },
    beforeDestroy() {
        if (!this.chart) {
        return
        }
        this.chart.dispose()
        this.chart = null
    },
    methods: {
        initChart() {
        this.chart = echarts.init(this.$el, 'macarons')
        this.setOptions(this.chartData)
        },
        setOptions(val) {
            console.log(val)
            this.chart.setOption({
                xAxis: {
                    data: val.dateData,
                    boundaryGap: true,
                    axisTick: {
                        show: false
                    }
                },
                grid: {
                    left: 10,
                    right: 10,
                    bottom: 20,
                    top: 30,
                    containLabel: true
                },
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'cross'
                    },
                    padding: [5, 10]
                },
                yAxis: {
                    axisTick: {
                        show: false
                    }
                },
                legend: {
                    data: val.xData
                },
                series: [{
                    name: val.xData[0], itemStyle: {
                        normal: {
                            color: '#FF005A',
                            lineStyle: {
                                color: '#FF005A',
                                width: 2
                            }
                        }
                    },
                    smooth: true,
                    type: 'line',
                    data: val.firstData,
                    animationDuration: 2800,
                    animationEasing: 'cubicInOut'
                },
                {
                    name: val.xData[0],
                    smooth: true,
                    type: 'bar',
                    barWidth : '20%',
                    itemStyle: {
                        normal: {
                            color: '#3888fa'
                        }
                    },
                    data: val.firstData,
                    animationDuration: 1000,
                    animationEasing: 'quadraticOut'
                },
                {
                    name: val.xData[1], 
                    itemStyle: {
                        normal: {
                            normal: {
                                color: '#3888fa'
                            }
                        }
                    },
                    smooth: true,
                    type: 'bar',
                    barWidth : '20%',
                    data: val.secondData,
                    animationDuration: 1000,
                    animationEasing: 'cubicInOut'
                },]
            })
        }
    }
}
</script>

<style scoped>

</style>