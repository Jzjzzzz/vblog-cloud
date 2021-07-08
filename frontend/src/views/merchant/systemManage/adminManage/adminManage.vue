<template>
  <div class="content">
    <div class="top">
      <div class="queryParamView">
        <el-input
          v-model="formData.searchText"
          prefix-icon="el-icon-search"
          placeholder="搜索管理员姓名"
          style="width:200px"
        />
        <el-button icon="el-icon-search" type="primary" @click="queryList">搜索</el-button>
      </div>
      <div class="queryParamView">
        <el-button type="primary" @click="addAdmin">新增管理员</el-button>
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

    <el-dialog title="管理员详情" :visible.sync="showDetail" :before-close="handleClose">
      <div v-if="showDetail">
        <adminDetail :merchantAdminId="adminId" @closeClick="handleClose" />
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request'
import tableView from '@/components/table'
import Pagination from '@/components/Pagination'
import adminDetail from './adminDetail/adminDetail'

export default {
  components: {
    tableView,
    Pagination,
    adminDetail
  },
  data() {
    return {
      showDetail: false,
      adminId: null,
      total: 0, //总记录数
      formData: {
        //查询条件
        searchText: '',
        page: 1,
        limit: 20
      },
      showLoading: false, //是否显示table loading
      tableHeadList: [
        {
          //表头
          name: '管理员ID',
          value: 'adminId',
          width: '80'
        },
        {
          name: '管理员姓名',
          value: 'adminName'
        },
        {
          name: '管理员手机号',
          value: 'adminPhone'
        },
        {
          name: '添加时间',
          value: 'addDate',
          width: '200'
        },
        {
          name: '最后登录时间',
          value: 'lastLoginDate',
          width: '200'
        }
      ],
      dataList: [] //数据列表
    }
  },
  mounted() {
    this.queryTotalNum() //查询总记录数
    this.queryList() //查询列表
  },
  methods: {
    /**
     * 新增管理员
     */
    addAdmin: function() {
      this.adminId = 0
      this.showDetail = true
    },

    /**
     * 导出Excel文件
     */
    exportExcel: function() {
      let params = JSON.parse(JSON.stringify(this.formData))
      params.queryTotalNum = false
      let url = '/api/merchant/admin/getAllMerchantAdminExcel?'
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
      return request({
        url: '/merchant/admin/getAllMerchantAdminList',
        method: 'post',
        params
      })
    },

    /**
     * @param {Object} row 点击行的数据
     */
    eventClick(row) {
      this.adminId = row.adminId
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
