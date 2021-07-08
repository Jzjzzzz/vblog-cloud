<template>
  <div class="content">
    <div class="topView">
      <div class="item">
        <div class="name">上月订单数</div>
        <div class="value">{{statisticsData.lastOrder}}</div>
        <div class="tips">
          <div class="left">总订单数：{{statisticsData.totalOrder}}</div>
          <div class="right"></div>
        </div>
      </div>
      <div class="item">
        <div class="name">上月新增用户数</div>
        <div class="value">{{statisticsData.lastUser}}</div>
        <div class="tips">
          <div class="left">总用户数：{{statisticsData.totalUser}}</div>
          <div class="right"></div>
        </div>
      </div>
      <div class="item">
        <div class="name">上月新增商家数</div>
        <div class="value">{{statisticsData.lastMerchant}}</div>
        <div class="tips">
          <div class="left">总商家数：{{statisticsData.totalMerchant}}</div>
          <div class="right"></div>
        </div>
      </div>
      <div class="item">
        <div class="name">本月新增订单数</div>
        <div class="value">{{statisticsData.currentOrder}}</div>
        <div class="tips">
          <div class="left"></div>
          <div class="right"></div>
        </div>
      </div>
      <div class="item">
        <div class="name">本月新增用户数</div>
        <div class="value">{{statisticsData.currentUser}}</div>
        <div class="tips">
          <div class="left"></div>
          <div class="right"></div>
        </div>
      </div>
      <div class="item">
        <div class="name">本月新增商家数</div>
        <div class="value">{{statisticsData.currentMerchant}}</div>
        <div class="tips">
          <div class="left"></div>
          <div class="right"></div>
        </div>
      </div>
    </div>
    <div class="center">
      <line-chart :chart-data="lineChartData" v-if="currentMonthDataShow&&lastMonthDataShow" />
    </div>
    <div class="bottom">
      <div class="left">
        <PieChart :advertClickNum="advertClickNum" v-if="advertClickNumShow" />
      </div>
      <div class="right">
        <BarChart :yearOrderData="yearOrderData" v-if="yearOrderDataShow" />
      </div>
    </div>
  </div>
</template>

<script>
;``
import LineChart from './components/LineChart'
import PieChart from './components/PieChart'
import BarChart from './components/BarChart'
import request from '@/utils/request'

export default {
  components: {
    LineChart,
    PieChart,
    BarChart
  },
  data() {
    return {
      statisticsData: {},
      yearOrderData: {},
      yearOrderDataShow: false,
      lineChartData: {
        lastMonthData: [],
        currentMonthData: []
      },
      advertClickNum: [],
      advertClickNumShow: false,
      currentMonthDataShow: false,
      lastMonthDataShow: false
    }
  },
  mounted() {
    this.queryStatisticsData()
    this.getTotalOrderByYear()
    this.getTotalOrderByCurrentMonth()
    this.getTotalOrderByLastMonth()
    this.getAdvertClickNum()
  },
  methods: {
    getAdvertClickNum() {
      request({
        url: '/management/admin/getAdvertClickNum',
        method: 'get',
        params: null
      }).then(
        response => {
          this.advertClickNum = response.data
          this.advertClickNumShow = true
        },
        error => {}
      )
    },
    queryStatisticsData() {
      request({
        url: '/management/admin/queryStatisticsData',
        method: 'get',
        params: null
      }).then(
        response => {
          this.statisticsData = response.data
        },
        error => {}
      )
    },
    getTotalOrderByYear() {
      let params = {}
      params.year = new Date().getFullYear()
      request({
        url: '/management/admin/getTotalOrderByYear',
        method: 'post',
        params: params
      }).then(
        response => {
          this.yearOrderData = response.data
          this.yearOrderDataShow = true
        },
        error => {}
      )
    },
    getTotalOrderByCurrentMonth() {
      let params = {}
      params.year = new Date().getFullYear()
      params.month = new Date().getMonth() + 1
      request({
        url: '/management/admin/getTotalOrderByMonth',
        method: 'post',
        params: params
      }).then(
        response => {
          let currentMonthData = []
          for (let i = 1; i <= 31; i++) {
            currentMonthData.push(response.data[i + '日'])
          }
          this.lineChartData.currentMonthData = currentMonthData
          this.currentMonthDataShow = true
        },
        error => {}
      )
    },
    getTotalOrderByLastMonth() {
      let params = {}
      params.year = new Date().getFullYear()
      params.month = new Date().getMonth()
      if (params.month === 0) {
        this.lastMonthDataShow = true
      } else {
        request({
          url: '/management/admin/getTotalOrderByMonth',
          method: 'post',
          params: params
        }).then(
          response => {
            let lastMonthData = []
            for (let i = 1; i <= 31; i++) {
              lastMonthData.push(response.data[i + '日'])
            }
            this.lineChartData.lastMonthData = lastMonthData
            this.lastMonthDataShow = true
          },
          error => {}
        )
      }
    }
  }
}
</script>

<style scoped>
.topView {
  display: flex;
  padding: 30px;
  padding-right: 0;
}
.topView .item {
  border: 1px solid #eee;
  width: 250px;
  margin-right: 30px;
}
.topView .item .name {
  padding: 15px;
  border-bottom: 1px solid #eee;
  font-size: 14px;
  font-weight: bold;
  color: #333;
}
.topView .item .value {
  padding: 10px 20px 0px 20px;
  font-weight: bold;
  font-size: 24px;
  color: #666;
}
.topView .item .tips {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  padding: 10px 20px 5px 15px;
  color: #888;
}
.center {
  margin: 10px;
}
.bottom {
  display: flex;
}
.bottom .left {
  width: 30%;
}
.bottom .right {
  width: 70%;
}
</style>
