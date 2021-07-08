<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import resize from './mixins/resize'

const animationDuration = 6000

export default {
  mixins: [resize],
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
      default: '300px'
    },
    yearOrderData: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      chart: null,
      months: []
    }
  },
  mounted() {
    let monthArr = []
    for (let i = 1; i <= 12; i++) {
      monthArr.push(i + '月')
    }
    this.months = monthArr
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
      let datas = []
      let i = 0
      for (let temp in this.yearOrderData) {
        datas.push(this.yearOrderData[temp])
        i++
        if (i >= 12) {
          break
        }
      }
      datas.reverse()
      this.chart.setOption({
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            // 坐标轴指示器，坐标轴触发有效
            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
          }
        },
        grid: {
          top: 10,
          left: '2%',
          right: '2%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: [
          {
            type: 'category',
            data: this.months,
            axisTick: {
              alignWithLabel: true
            }
          }
        ],
        yAxis: [
          {
            type: 'value',
            axisTick: {
              show: false
            }
          }
        ],
        series: [
          {
            name: '订单量',
            type: 'bar',
            stack: 'vistors',
            barWidth: '60%',
            data: datas,
            label: {
              normal: {
                show: true,
                position: 'top',
                textStyle: {
                  fontSize: 15
                }
              }
            },
            animationDuration
          }
        ]
      })
    }
  }
}
</script>
