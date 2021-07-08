<template>
  <div class="content">
    <div class="topView">
      <div class="item">
        <div class="name">上月订单数</div>
        <div class="value">{{orderTotalData.lastMonthOrder}}</div>
        <div class="tips">
          <div class="left">总订单数：{{totalOrder}}</div>
          <div class="right"></div>
        </div>
      </div>
      <div class="item">
        <div class="name">上月浏览数</div>
        <div class="value">{{lastMonthBrowse}}</div>
        <div class="tips">
          <div class="left">总浏览数：{{totalBrowse}}</div>
          <div class="right"></div>
        </div>
      </div>
      <div class="item">
        <div class="name">本月新增订单数</div>
        <div class="value">{{orderTotalData.currentMonthOrder}}</div>
        <div class="tips">
          <div class="left"></div>
          <div class="right"></div>
        </div>
      </div>
      <div class="item">
        <div class="name">本月浏览数</div>
        <div class="value">{{currentMonthBrowse}}</div>
        <div class="tips">
          <div class="left"></div>
          <div class="right"></div>
        </div>
      </div>
    </div>
    <div class="center">
      <line-chart :chart-data="lineChartData" />
    </div>
    <div class="bottom">
      <div class="left">
        <PieChart :serviceTotalOrder="serviceTotalOrder" v-if="serviceTotalOrderShow" />
      </div>
      <div class="right">
        <BarChart :yearOrderData="yearOrderData" v-if="yearOrderDataShow" />
      </div>
    </div>
  </div>
</template>

<script>
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
      totalOrder:'',
      lastMonthBrowse:'',
      currentMonthBrowse: '',
      totalBrowse: '',
      orderTotalData: {},
      yearOrderData: {},
      yearOrderDataShow: false,
      lineChartData: {
        lastMonthData: [],
        currentMonthData: []
      },
      currentMonthDataShow: false,
      lastMonthDataShow: false,
      serviceTotalOrder: [],
      serviceTotalOrderShow: false
    }
  },
  mounted() {
    this.getTotalOrder()
    this.getTotalOrderByYearGroupService()
    this.getTotalOrderByYear()
    this.getTotalOrderByCurrentMonth()
    this.getTotalOrderByLastMonth()
    this.getTotalBrowse()
    this.getBrowseByCurrentMonth()
    this.getBrowseByLastMonth()
  },
  methods: {
    
    getTotalOrder() {
      let params = {}
      params.year = new Date().getFullYear()
      request({
        url: '/merchant/merchant/getTotalOrder',
        method: 'post',
        params: params
      }).then(
        response => {
          this.totalOrder = response.data.total
        },
        error => {}
      )
    },
    getTotalOrderByYearGroupService() {
      let params = {}
      params.year = new Date().getFullYear()
      request({
        url: '/merchant/merchant/getTotalOrderByYearGroupService',
        method: 'post',
        params: params
      }).then(
        response => {
          this.serviceTotalOrder = response.data
          this.serviceTotalOrderShow = true
        },
        error => {}
      )
    },
    getTotalOrderByYear() {
      let params = {}
      params.year = new Date().getFullYear()
      request({
        url: '/merchant/merchant/getTotalOrderByYear',
        method: 'post',
        params: params
      }).then(
        response => {
          this.yearOrderData = response.data
          this.yearOrderDataShow = true

          let lastMonth = new Date().getMonth()
          this.orderTotalData.lastMonthOrder = response.data[lastMonth + '月']
          this.orderTotalData.currentMonthOrder =
            response.data[lastMonth + 1 + '月']
        },
        error => {}
      )
    },
    getTotalOrderByCurrentMonth() {
      let params = {}
      params.year = new Date().getFullYear()
      params.month = new Date().getMonth() + 1
      request({
        url: '/merchant/merchant/getTotalOrderByMonth',
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
      request({
        url: '/merchant/merchant/getTotalOrderByMonth',
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
    },
    getTotalBrowse() {
      request({
        url: '/merchant/merchant/getTotalBrowse',
        method: 'get',
        params: null
      }).then(
        response => {
          this.totalBrowse = response.data.total
        },
        error => {}
      )
    },
    getBrowseByCurrentMonth() {
      let params = {}
      let currentMonth = new Date().getMonth() + 1
      if (currentMonth < 10) {
        currentMonth = '0' + currentMonth
      }
      params.month = new Date().getFullYear() + '-' + currentMonth
      request({
        url: '/merchant/merchant/getBrowseByMonth',
        method: 'post',
        params: params
      }).then(
        response => {
          this.currentMonthBrowse = response.data.browse_number
        },
        error => {}
      )
    },
    getBrowseByLastMonth() {
      let params = {}
      let lastMonth = new Date().getMonth()
      if (lastMonth === 0) {
        this.lastMonthBrowse = 0
      } else {
        if (lastMonth < 10) {
          lastMonth = '0' + lastMonth
        }
        params.month = new Date().getFullYear() + '-' + lastMonth
        request({
          url: '/merchant/merchant/getBrowseByMonth',
          method: 'post',
          params: params
        }).then(
          response => {
            this.lastMonthBrowse = response.data.browse_number ? response.data.browse_number : 0
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
