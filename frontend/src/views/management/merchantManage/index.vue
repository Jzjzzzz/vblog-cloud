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
        <el-input
          prefix-icon="el-icon-search"
          v-model="formData.searchText"
          :placeholder="'搜索'+searchType[formData.searchTypeIndex].label"
          style="width:200px"
        />
        <el-button icon="el-icon-search" type="primary" @click="queryList">搜索</el-button>
      </div>
      <div class="queryParamView">
        <el-select v-model="formData.sortCriteria" style="width:160px" @change="queryList">
          <el-option
            v-for="item in sortCriteria"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
        <el-select v-model="formData.sortType" style="width:80px" @change="queryList">
          <el-option
            v-for="item in sortType"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </div>
      <div class="queryParamView">
        <el-select v-model="formData.stateType" style="width:120px" @change="queryList">
          <el-option
            v-for="item in stateType"
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
      :page.sync="formData.page"
      :limit.sync="formData.limit"
      @pagination="queryList"
    />

    <el-dialog title="商家详情" :visible.sync="showDetail" :before-close="handleClose">
      <div v-if="showDetail">
        <merchantDetail :merchantId="merchantId" @closeClick="handleClose" />
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request'
import tableView from '@/components/table'
import Pagination from '@/components/Pagination'
import merchantDetail from './merchantDetail/merchantDetail'

export default {
  components: {
    tableView,
    Pagination,
    merchantDetail
  },
  data() {
    return {
      showDetail: false,
      merchantId: null,
      // 搜索类型
      searchType: [
        {
          value: 'name',
          label: '商家名称'
        },
        {
          value: 'phone',
          label: '手机号'
        }
      ],
      // 排序条件
      sortCriteria: [
        {
          value: 'date',
          label: '根据添加时间排序'
        },
        {
          value: 'grade',
          label: '根据评分等级排序'
        },
        {
          value: 'serviceNum',
          label: '根据服务次数排序'
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
          value: 'all',
          label: '全部'
        },
        {
          value: '1',
          label: '营业状态'
        },
        {
          value: '2',
          label: '休息状态'
        },
        {
          value: '0',
          label: '关闭状态'
        }
      ],
      total: 0, //总记录数
      formData: {
        //查询条件
        searchTypeIndex: 0,
        searchText: '',
        sortCriteria: 'date',
        sortType: 'desc',
        stateType: 'all',
        page: 1,
        limit: 20
      },
      showLoading: false, //是否显示table loading
      tableHeadList: [
        {
          //表头
          name: '商家ID',
          value: 'merchantId',
          width: '80'
        },
        {
          name: '商家名称',
          value: 'merchantName',
          width: '300'
        },
        {
          name: '手机号',
          value: 'merchantPhone',
          width: '150'
        },
        {
          name: '商家所属地址',
          value: 'merchantAddress'
        },
        {
          name: '添加时间',
          value: 'addDate',
          width: '200'
        },
        {
          name: '营业时间',
          value: 'businessTime',
          width: '80'
        },
        {
          name: '商家状态',
          value: 'merchantState',
          width: '80'
        },
        {
          name: '商家评分',
          value: 'merchantGrade',
          width: '80'
        },
        {
          name: '服务次数',
          value: 'serviceNumber',
          width: '80'
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
     * 导出Excel文件
     */
    exportExcel: function() {
      let params = JSON.parse(JSON.stringify(this.formData))
      params.searchType = this.searchType[params.searchTypeIndex].value
      params.queryTotalNum = false
      let url = '/api/management/merchant/getMerchantListExcel?'
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
      return request({
        url: '/management/merchant/getMerchantList',
        method: 'post',
        params
      })
    },

    /**
     * @param {Object} row 点击行的数据
     */
    eventClick(row) {
      this.merchantId = row.merchantId
      this.showDetail = true
    },

    /**
     * 商家详情页关闭
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
