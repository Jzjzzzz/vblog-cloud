<template>
  <div class="content">
    <div class="top">
      <div class="queryParamView">
        <el-input prefix-icon="el-icon-edit" v-model="addServiceTypeName" :placeholder="'填写欲添加的服务类型名称'" maxlength="4" style="width:250px" />
        <el-button type="primary" @click="addServiceType">添加</el-button>
      </div>
    </div>
    <div>
      <tableView :table-head-list="tableHeadList" :data-list="dataList" :show-loading="showLoading" @eventClick="eventClick"/>
    </div>
    <div v-if="showEdit">
    <el-dialog title="编辑服务类型" :visible.sync="showEdit" :before-close="handleClose">
      <serviceTypeEdit :serviceTypeId="serviceTypeId" @closeClick="handleClose" />
    </el-dialog>
    </div>
  </div>
</template>

<script>
  import request from '@/utils/request'
  import tableView from '@/components/table'
  import serviceTypeEdit from './serviceTypeEdit/serviceTypeEdit'

  export default {
    components: {
      tableView,
      serviceTypeEdit
    },

    data() {
      return {
        showEdit: false,
        serviceTypeId: null,
        addServiceTypeName: null,
        showLoading: false,
        tableHeadList: [{
          name: '服务类型ID',
          value: 'serviceTypeId'
        },{
          name: '服务类型显示名称',
          value: 'serviceTypeName'
        }],
        dataList: []  //数据列表
      }
    },

    mounted() {
      this.queryList()  //查询列表
    },

    methods: {
      /**
       * 查询列表
       */
      queryList:function() { 
        const _this = this
        this.showLoading = true
        request({
          url: '/management/serviceType/getAllServiceType',
          method: 'post',
        }).then(response => {
          _this.showLoading = false
          _this.dataList = response.data
        }, error => {
          _this.showLoading = false
        })
      },
      
      /**
       * @param {Object} row 点击行的数据
       */
      eventClick(row) {
        this.serviceTypeId = row.serviceTypeId
        this.showEdit=true
      },

      /**
       * 用户详情页关闭
       * @param {Object} done
       */
      handleClose(isRefresh) {
        this.showEdit = false
        if (isRefresh) {
          this.queryList()
        }
      },

      /**
       * 添加服务类型
       */
      addServiceType: function() {
        this.addServiceTypeName = this.addServiceTypeName.replace(/(^\s*)|(\s*$)/g, "")
        if (!this.addServiceTypeName) {
          return
        }
        const _this = this
        this.$confirm('是否添加服务类型：【' + this.addServiceTypeName + '】?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          request({
            url: '/management/serviceType/addServiceType',
            method: 'post',
            params: {serviceTypeName: this.addServiceTypeName}
          }).then(response => {
            _this.addServiceTypeName = ""
            _this.$message({
              message: '添加成功',
              type: 'success',
              duration: 3 * 1000
            })
            _this.queryList()
          }, error => {
            Message({
              message: '添加失败',
              type: 'error',
              duration: 5 * 1000
            })
          })
        }).catch(() => {
          
        });
      },
    }
  }
</script>
