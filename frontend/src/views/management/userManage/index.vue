<template>
  <div class="content">
    <div class="top">
      <div class="queryParamView">
        <el-select v-model="formData.searchType" style="width:120px" @change="queryList">
          <el-option
            v-for="(item,index) in searchType"
            :key="item.value"
            :label="item.label"
            :value="index"
          />
        </el-select>
        <el-input
          prefix-icon="el-icon-search"
          v-model="formData.searchContent"
          :placeholder="'搜索'+searchType[formData.searchType].label"
          style="width:200px"
        />
        <el-button icon="el-icon-search" type="primary" @click="queryList">搜索</el-button>
      </div>
      <div class="queryParamView">
        <el-select v-model="formData.sortCriteria" style="width:160px" @change="queryList">
          <el-option
            v-for="(item,index) in sortCriteria"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
        <el-select v-model="formData.sortType" style="width:80px" @change="queryList">
          <el-option
            v-for="(item,index) in sortType"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
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
      :page.sync="formData.pageNum"
      :limit.sync="formData.pageSize"
      @pagination="queryList"
    />
    <div v-if="showDetail">
      <el-dialog title="用户详情" :visible.sync="showDetail" :before-close="handleClose">
        <userDetail :userId="userId" @closeClick="handleClose" />
      </el-dialog>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request'
import tableView from '@/components/table'
import Pagination from '@/components/Pagination'
import userDetail from './userDetail/userDetail'

export default {
  components: {
    tableView,
    Pagination,
    userDetail
  },
  data() {
    return {
      showDetail: false,
      userId: null,
      searchType: [
        {
          //搜索类型
          value: '0',
          label: '用户名称'
        },
        {
          value: '1',
          label: '手机号'
        }
      ],
      sortCriteria: [
        {
          //排序条件
          value: 'register_date',
          label: '根据注册时间排序'
        },
        {
          value: 'last_login_date',
          label: '根据登录时间排序'
        }
      ],
      sortType: [
        {
          //排序类型
          value: 'asc',
          label: '升序'
        },
        {
          value: 'desc',
          label: '降序'
        }
      ],
      stateType: [
        {
          //状态类型
          value: 0,
          label: '全部'
        },
        {
          value: 1,
          label: '已注册'
        },
        {
          value: 2,
          label: '未注册'
        }
      ],
      total: 0, //总记录数
      formData: {
        //查询条件
        searchType: 0,
        searchContent: '',
        sortCriteria: 'register_date',
        sortType: 'desc',
        stateType: 0,
        pageNum: 1,
        pageSize: 20
      },
      showLoading: false, //是否显示table loading
      tableHeadList: [
        {
          name: '用户ID',
          value: 'userId',
          width: '100'
        },
        {
          name: '用户名称',
          value: 'userName',
          width: '150'
        },
        {
          name: '手机号',
          value: 'userPhone'
        },
        {
          name: '车型',
          value: 'userCarType'
        },
        {
          name: '车牌号',
          value: 'userCarNum'
        },
        {
          name: '微信昵称',
          value: 'wechatNikeName',
          width: '200'
        },
        {
          name: '注册时间',
          value: 'registerDate',
          width: '200'
        },
        {
          name: '上次登录时间',
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
     * 查询列表
     */
    queryList: function() {
      const params = this.formData
      const _this = this
      this.showLoading = true
      request({
        url: '/management/user/getAllUserList',
        method: 'get',
        params
      }).then(
        response => {
          _this.showLoading = false
          _this.dataList = response.data
        },
        error => {
          _this.showLoading = false
        }
      )
    },

    /**
     * 查询总记录数
     */
    queryTotalNum: function() {
      const _this = this
      const params = { queryTotalNum: true }
      request({
        url: '/management/user/getAllUserList',
        method: 'get',
        params
      }).then(response => {
        _this.total = response.data[0].totalNumber
      })
    },

    /**
     * @param {Object} row 点击行的数据
     */
    eventClick(row) {
      this.userId = row.userId
      this.showDetail = true
    },

    /**
     * 用户详情页关闭
     * @param {Object} done
     */
    handleClose() {
      this.showDetail = false
    },

    /**
     * 导出Excel文件
     */
    exportExcel: function() {
      let params = JSON.parse(JSON.stringify(this.formData))
      let url = '/api/management/user/getAllUserList?'
      for (let param in params) {
        url += param + '=' + params[param] + '&'
      }
      url = url.substring(0, url.length - 1)
      url += '&exportExcel=true'
      window.open(url)
    }
  }
}
</script>
