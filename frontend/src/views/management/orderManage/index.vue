<template>
  <div class="content">
    <div class="top">
      <div class="queryParamView">
        <el-select v-model="formData.searchTypeIndex" style="width:120px" @change="queryList">
          <el-option
            v-for="(item,index) in searchType"
            :key="item.value"
            :label="item.label"
            :value="index"
          />
        </el-select>
        <el-select
          v-model="formData.searchText"
          filterable
          remote
          :placeholder="'搜索'+searchType[formData.searchTypeIndex].label"
          :remote-method="remoteMethod"
          :loading="searchLoading"
          loading-text="正在搜索"
          :clearable="true"
          style="width:200px"
          @change="queryList"
        >
          <el-option v-for="item in searchList" :key="item.id" :label="item.name" :value="item.id"></el-option>
        </el-select>
      </div>
      <!--<div class="queryParamView">
        <el-select v-model="formData.sortCriteria" style="width:160px" @change="queryList">
          <el-option v-for="(item,index) in sortCriteria" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
        <el-select v-model="formData.sortType" style="width:80px" @change="queryList">
          <el-option v-for="(item,index) in sortType" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </div>-->
      <div class="queryParamView">
        <el-select v-model="formData.stateType" style="width:120px" @change="queryList">
          <el-option
            v-for="(item,index) in stateType"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </div>
      <div class="queryParamView">
        <el-date-picker
          v-model="formData.dateFrame"
          type="daterange"
          align="right"
          unlink-panels
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          :picker-options="pickerOptions"
          value-format="yyyy-MM-dd"
          :clearable="false"
          @change="queryList"
        ></el-date-picker>
      </div>
      <div class="queryParamView">
        <el-button type="primary" @click="exportExcel">导出Excel</el-button>
      </div>
    </div>
    <div>
      <tableView
        :table-head-list="tableHeadList"
        :data-list="dataList"
        :show-loading="showLoading"
        @eventClick="eventClick"
      />
    </div>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="formData.page"
      :limit.sync="formData.limit"
      @pagination="queryList"
    />

    <div v-if="showDetail">
      <el-dialog title="订单详情" :visible.sync="showDetail" :before-close="handleClose">
        <orderDetail :orderId="orderId" @closeClick="handleClose" />
      </el-dialog>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request'
import tableView from '@/components/table'
import Pagination from '@/components/Pagination'
import orderDetail from './orderDetail/orderDetail'
import { parseTime } from '@/utils/index'

export default {
  components: {
    tableView,
    Pagination,
    orderDetail
  },
  data() {
    return {
      showDetail: false,
      orderId: null,
      searchType: [
        {
          //搜索类型
          value: 'merchantName',
          label: '商家名称'
        },
        {
          value: 'userName',
          label: '用户名称'
        }
      ],
      /*sortCriteria: [{ //排序条件
          value: 'date',
          label: '根据订单时间排序'
        }],
        sortType: [{ //排序类型
          value: 'asc',
          label: '升序'
        }, {
          value: 'desc',
          label: '降序'
        }],*/
      stateType: [
        {
          //状态类型
          value: 'all',
          label: '全部'
        },
        {
          value: '0',
          label: '已预约'
        },
        {
          value: '1',
          label: '已完成'
        },
        {
          value: '3',
          label: '已取消'
        },
        {
          value: '4',
          label: '已删除'
        }
      ],
      pickerOptions: {
        shortcuts: [
          {
            text: '最近一周',
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
              picker.$emit('pick', [start, end])
            }
          },
          {
            text: '最近一个月',
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30)
              picker.$emit('pick', [start, end])
            }
          },
          {
            text: '最近三个月',
            onClick(picker) {
              const end = new Date()
              const start = new Date()
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90)
              picker.$emit('pick', [start, end])
            }
          }
        ]
      },
      total: 0, //总记录数
      searchLoading: false, //是否显示 搜索框 loading
      searchList: [], //搜索结果列表
      formData: {
        //查询条件
        searchTypeIndex: 0,
        searchText: '',
        sortCriteria: 'date',
        sortType: 'desc',
        stateType: 'all',
        dateFrame: [],
        page: 1,
        limit: 20
      },
      showLoading: false, //是否显示table loading
      tableHeadList: [
        {
          //表头
          name: '订单ID',
          value: 'orderId',
          width: '80'
        },
        {
          name: '商家名称',
          value: 'merchantName'
        },
        {
          name: '用户名称',
          value: 'userName'
        },
        {
          name: '服务名称',
          value: 'serviceName'
        },
        {
          name: '消耗劵类型',
          value: 'couponName',
          width: '100'
        },
        {
          name: '提交时间',
          value: 'submitDate',
          width: '200'
        },
        {
          name: '预约时间',
          value: 'bookedDate',
          width: '200'
        },
        {
          name: '完成/取消时间',
          value: 'completeDate',
          width: '200'
        },
        {
          name: '订单状态',
          value: 'orderState',
          width: '100'
        }
      ],
      dataList: [] //数据列表
    }
  },
  mounted() {
    //默认查询一周内数据
    let end = new Date()
    let start = new Date()
    start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
    this.formData.dateFrame.push(parseTime(start, '{y}-{m}-{d}'))
    this.formData.dateFrame.push(parseTime(end, '{y}-{m}-{d}'))

    this.queryTotalNum() //查询总记录数
    this.queryList() //查询列表
  },
  methods: {
    /**
     * 搜索框输入改变事件
     * @param {Object} query 搜索文本
     */
    remoteMethod(query) {
      if (query !== '') {
        let params = {}
        params.searchText = query
        this.searchLoading = true
        let url
        switch (this.formData.searchTypeIndex) {
          case 0:
            url = '/management/merchant/searchMerchantList'
            break

          case 1:
            url = '/management/user/searchUserList'
            break
        }
        request({
          url: url,
          method: 'post',
          params
        }).then(
          response => {
            this.searchLoading = false
            this.searchList = response.data
          },
          error => {
            this.searchLoading = false
          }
        )
      }
    },

    /**
     * 导出Excel文件
     */
    exportExcel: function() {
      let params = JSON.parse(JSON.stringify(this.formData))
      params.searchType = this.searchType[params.searchTypeIndex].value
      params.queryTotalNum = false
      params.startDate = params.dateFrame[0] + ' 00:00:00'
      params.stopDate = params.dateFrame[1] + ' 23:59:59'
      params.dateFrame = null
      if (params.searchText === '') {
        params.searchText = 0
      }
      let url = '/api/management/order/getOrderListExcel?'
      for (let param in params) {
        url += param + '=' + params[param] + '&'
      }
      url = url.substring(0, url.length - 1)
      window.open(url)
    },
    /**
     * 查询列表
     */
    queryList: function() {
      this.showLoading = true
      this.queryData(false).then(
        response => {
          this.showLoading = false
          this.dataList = response.data
        },
        error => {
          this.showLoading = false
        }
      )
    },

    /**
     * 查询总记录数
     */
    queryTotalNum: function() {
      this.queryData(true).then(
        response => {
          this.total = response.data[0].totalNumber
        },
        error => {}
      )
    },

    /**
     * 查询数据
     * @param {bool} isTotal :是否查询总数(否则查询数据)
     */
    queryData(isTotal) {
      let params = JSON.parse(JSON.stringify(this.formData))
      params.searchType = this.searchType[params.searchTypeIndex].value
      params.queryTotalNum = isTotal
      params.startDate = params.dateFrame[0] + ' 00:00:00'
      params.stopDate = params.dateFrame[1] + ' 23:59:59'
      params.dateFrame = null
      if (params.searchText === '') {
        params.searchText = 0
      }
      return request({
        url: '/management/order/getOrderList',
        method: 'post',
        params
      })
    },

    /**
     * @param {Object} row 点击行的数据
     */
    eventClick(row) {
      this.orderId = row.orderId
      this.showDetail = true
    },

    /**
     * 详情页关闭
     * @param {Object} done
     */
    handleClose(isRefresh) {
      this.showDetail = false
      if (isRefresh && isRefresh == true) {
        this.queryList()
      }
    }
  }
}
</script>

<style>
</style>
