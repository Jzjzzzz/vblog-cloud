<template>
  <div class="content">
    <div class="top">
      <!--<div class="queryParamView">
        <el-select
          v-model="formData.searchText"
          filterable
          remote
          :placeholder="'搜索管理员名称'"
          :remote-method="remoteMethod"
          :loading="searchLoading"
          loading-text="正在搜索"
          :clearable="true"
          style="width:200px"
          @change="queryList"
        >
          <el-option v-for="item in searchList" :key="item.id" :label="item.name" :value="item.id"></el-option>
        </el-select>
      </div>-->
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
        :show-event="false"
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
  </div>
</template>

<script>
import request from '@/utils/request'
import tableView from '@/components/table'
import Pagination from '@/components/Pagination'
import { parseTime } from '@/utils/index'

export default {
  components: {
    tableView,
    Pagination
  },
  data() {
    return {
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
      total: 0, // 总记录数
      searchLoading: false, // 是否显示 搜索框 loading
      searchList: [], // 搜索结果列表
      formData: {
        // 查询条件
        searchText: '',
        dateFrame: [],
        page: 1,
        limit: 20
      },
      showLoading: false, // 是否显示table loading
      tableHeadList: [
        {
          // 表头
          name: '记录ID',
          value: 'merchantOperateId',
          width: '80'
        },
        {
          name: '操作内容',
          value: 'operateContent'
        },
        {
          name: '操作时间',
          value: 'operateDate',
          width: '200'
        }
      ],
      dataList: [] // 数据列表
    }
  },
  mounted() {
    // 默认查询一周内数据
    let end = new Date()
    let start = new Date()
    start.setTime(start.getTime() - 3600 * 1000 * 24 * 7)
    this.formData.dateFrame.push(parseTime(start, '{y}-{m}-{d}'))
    this.formData.dateFrame.push(parseTime(end, '{y}-{m}-{d}'))

    this.queryTotalNum() // 查询总记录数
    this.queryList() // 查询列表
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
        let url = '/merchant/admin/searchMerchantAdminList'
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
      params.queryTotalNum = false
      params.startDate = params.dateFrame[0] + ' 00:00:00'
      params.stopDate = params.dateFrame[1] + ' 23:59:59'
      params.dateFrame = null
      params.typeId = 1
      if (params.searchText === '') {
        params.searchText = 0
      }
      let url = '/api/merchant/opRecord/getRecordExcel?'
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
      params.queryTotalNum = isTotal
      params.startDate = params.dateFrame[0] + ' 00:00:00'
      params.stopDate = params.dateFrame[1] + ' 23:59:59'
      params.dateFrame = null
      params.typeId = 1
      if (params.searchText === '') {
        params.searchText = 0
      }
      return request({
        url: '/merchant/opRecord/getRecordList',
        method: 'post',
        params
      })
    }
  }
}
</script>

<style>
</style>
