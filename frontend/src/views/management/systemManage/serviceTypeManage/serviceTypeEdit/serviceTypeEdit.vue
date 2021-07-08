<template>
  <div class="detailView" v-if="serviceTypeInfo">
    <div class="item">
      <div class="name">服务类型ID：</div>
      <div class="value">
        <el-input v-model="serviceTypeInfo.serviceTypeId" :disabled="true" style="width: 200px"></el-input>
      </div>
    </div>
    <div class="item">
      <div class="name">服务类型名称：</div>
      <div class="value">
        <el-input v-model="serviceTypeInfo.serviceTypeName" maxlength="4" style="width: 200px"></el-input>
      </div>
    </div>
    <div class="eventView">
      <el-button type="danger" @click="deleteClick">删除</el-button>
      <el-button type="primary" @click="editClick">修改</el-button>
      <el-button type="primary" @click="closeClick">取消</el-button>
    </div>
  </div>
</template>

<script>
  import request from '@/utils/request'
  import {
    MessageBox,
    Message
  } from 'element-ui'

  export default {
    props: {
      serviceTypeId: {
        type: Number,
        required: true
      }
    },
    data() {
      return {
        serviceTypeInfo: null
      }
    },
    mounted() {
      this.queryData()
    },
    methods: {
      editClick: function() {
        this.serviceTypeInfo.serviceTypeName = this.serviceTypeInfo.serviceTypeName.replace(/(^\s*)|(\s*$)/g, "")
        if (!this.serviceTypeInfo.serviceTypeName) {
          return
        }
        let params = {...this.serviceTypeInfo}
        request({
          url: '/management/serviceType/updateServiceTypeDetail',
          method: 'post',
          params
        }).then(response => {
          Message({
            message: '修改成功',
            type: 'success',
            duration: 5 * 1000
          })
          this.closeClick(true)
        }, error => {})
      },

      deleteClick: function() {
        this.$confirm('是否确定删除该服务类型?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          request({
            url: '/management/serviceType/deleteServiceType',
            method: 'post',
            params: {serviceTypeId: this.serviceTypeId}
          }).then(response => {
            Message({
              message: response.msg,
              type: 'success',
              duration: 5 * 1000
            })
            this.closeClick(true)
          }, error => {})
        }).catch(() => {
          
        });
      },
      
      closeClick(isRefresh){
        this.$emit('closeClick', isRefresh);
      },
      
      queryData: function() {
        let params = {}
        params.serviceTypeId = this.serviceTypeId
        request({
          url: 'management/serviceType/getServiceTypeDetail',
          method: 'post',
          params
        }).then(response => {
            this.serviceTypeInfo = response.data
        }, error => {

        })
      }
    }
  }
</script>

<style>
</style>
