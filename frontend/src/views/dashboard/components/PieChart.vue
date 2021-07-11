<template>
  <div :class="className" :style="{height:height,width:width}" />
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import resize from '@/views/dashboard/mixins/resize.js'
import genreApi from '@/api/core/genre'
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
      default: '320px'
    }
  },
  data() {
    return {
      chart: null,
      list:[],
      list1:[],
      chartData: {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} ({d}%)'
        },
        legend: {
          left: 'center',
          bottom: '10',
          data: ['Industries', 'Technology', 'Forex', 'Gold', 'Forecasts']
        },
        series: [
          {
            name: '热门分类',
            type: 'pie',
            roseType: 'radius',
            radius: [15, 95],
            center: ['50%', '38%'],
            data: [
              { value: 320, name: 'Industries' },
              { value: 240, name: 'Technology' },
              { value: 149, name: 'Forex' },
              { value: 100, name: 'Gold' },
              { value: 59, name: 'Forecasts' }
            ],
            animationEasing: 'cubicInOut',
            animationDuration: 2600
          }
        ]
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
    getCategory(page, limit) {
      genreApi.getCategory(page, limit).then(resp => {
        // this.list = resp.data.records
        console.log(resp)

        resp.data.records.forEach(item=>{
          this.list.push({value:item.sort,name:item.cateName});
          this.list1.push(item.cateName);
        })
        Object.assign(this.chartData.series[0].data, this.list)
        Object.assign(this.chartData.legend.data, this.list1)
        console.log('this.list1', this.list1)
        // console.log('this.list', this.list)
        // console.log('this.chartData', this.chartData)
        console.log('this.chartData.legend.data', this.chartData.legend.data)
        this.initChart();
      })
    },
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')

      this.chart.setOption(this.chartData)
    },
  },
  created() {
    this.getCategory(1, 20)

  }

}
</script>
