<template>
  <div class="detailView" v-if="adminInfo">
    <div class="item">
      <div class="name">管理员ID：</div>
      <div class="value">
        <el-input v-model="adminInfo.merchantAdminId" :disabled="true" style="width: 200px"></el-input>
      </div>
    </div>
    <div class="item">
      <div class="name">管理员名称：</div>
      <div class="value">
        <el-input v-model="adminInfo.merchantAdminName" style="width: 200px"></el-input>
      </div>
    </div>
    <div class="item">
      <div class="name">管理员手机号：</div>
      <div class="value">
        <el-input v-model.number="adminInfo.merchantAdminPhone" maxlength="11" style="width: 200px"></el-input>
      </div>
    </div>
    <div class="item">
      <div class="name">添加时间：</div>
      <div class="value">
        <el-input v-model="adminInfo.addDate" :disabled="true" style="width: 200px"></el-input>
      </div>
    </div>
    <div class="item">
      <div class="name">最后登录时间：</div>
      <div class="value">
        <el-input v-model="adminInfo.lastLoginDate" :disabled="true" style="width: 200px"></el-input>
      </div>
    </div>
    <div class="item">
      
    </div>
    <div class="eventView">
      <el-button
        v-if="adminInfo.merchantAdminId!=null"
        type="danger"
        @click="deleteClick"
      >删除该管理员</el-button>
      <el-button type="primary" @click="editClick">保存</el-button>
      <el-button type="primary" @click="closeClick(false)">取消</el-button>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request'
import { Message, MessageBox } from 'element-ui'

export default {
  props: {
    merchantAdminId: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      adminInfo: null
    }
  },
  mounted() {
    if (this.merchantAdminId === 0) {
      this.adminInfo = {
        merchantAdminId: null,
        merchantAdminName: '',
        merchantAdminPhone: ''
      }
    } else {
      this.queryData()
    }
  },
  methods: {
    /**
     * 删除按钮点击
     */
    deleteClick: function() {
      this.$confirm('是否确认删除该管理员?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let params = {}
        params.merchantAdminId = this.adminInfo.merchantAdminId
        request({
          url: '/merchant/admin/deleteMerchantAdmin',
          method: 'post',
          params
        }).then(
          response => {
            Message({
              message: '删除成功',
              type: 'success',
              duration: 5 * 1000
            })
            this.closeClick(true)
          },
          error => {}
        )
      })
    },

    /**
     * 修改按钮点击
     */
    editClick: function() {
      let params = {}
      let adminInfo = this.adminInfo
      params.merchantAdminId = adminInfo.merchantAdminId
      params.merchantAdminName = adminInfo.merchantAdminName
      params.merchantAdminPhone = adminInfo.merchantAdminPhone
      if (params.merchantAdminPhone.toString().length !== 11) {
        Message({
          message: '手机号不正确',
          type: 'error',
          duration: 5 * 1000
        })
      } else {
        request({
          url: '/merchant/admin/updateMerchantAdminDetail',
          method: 'post',
          params
        }).then(
          response => {
            Message({
              message: '保存成功',
              type: 'success',
              duration: 5 * 1000
            })
            this.closeClick(true)
          },
          error => {}
        )
      }
    },

    /**
     * 关闭详情窗口
     */
    closeClick(isRefresh) {
      this.$emit('closeClick', isRefresh)
    },

    /**
     * 查询详情数据
     */
    queryData: function() {
      let params = {}
      params.merchantAdminId = this.merchantAdminId
      request({
        url: '/merchant/admin/getMerchantAdminDetail',
        method: 'post',
        params
      }).then(
        response => {
          let data = response.data
          let merchantAdminId = data.merchantAdminId
          this.adminInfo = data
        },
        error => {}
      )
    }
  }
}
</script>

<style>
</style>
