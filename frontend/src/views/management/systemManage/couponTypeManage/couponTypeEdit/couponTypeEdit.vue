<template>
  <div class="detailView" v-if="couponTypeInfo">
    <div class="item">
      <div class="name">优惠券类型ID：</div>
      <div class="value">
        <el-input v-model="couponTypeInfo.couponTypeId" :disabled="true" style="width: 200px"></el-input>
      </div>
    </div>
    <div class="item">
      <div class="name">优惠券类型名称：</div>
      <div class="value">
        <el-input v-model="couponTypeInfo.couponName" maxlength="4" style="width: 200px"></el-input>
      </div>
    </div>
    <div class="item">
      <div class="name">优惠券类型图标：</div>
      <div class="value">
        <el-select v-model="couponTypeInfo.couponIcon" style="width:120px">
            <el-option
              v-for="(item,index) in iconType"
              :key="index"
              :label="item.label"
              :value="item.value"
            />
        </el-select>
      </div>
    </div>
    <div class="item">
      
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
      couponTypeId: {
        type: Number,
        required: true
      }
    },
    data() {
      return {
        couponTypeInfo: null,
        iconType: [
          {
            value: '/images/washCar.png',
            label: '洗车图标'
          },
          {
            value: '/images/fineWashCar.png',
            label: '精洗图标'
          },
          {
            value: '/images/maintainCar.png',
            label: '保养图标'
          },
          {
            value: '/images/membraneCar.png',
            label: '车膜图标'
          },
          {
            value: '/images/paintCar.png',
            label: '喷漆图标'
          },
          {
            value: '/images/waxCar.png',
            label: '打蜡图标'
          }
        ],
      }
    },
    mounted() {
      this.queryData()
    },
    methods: {
      editClick: function() {
        this.couponTypeInfo.couponName = this.couponTypeInfo.couponName.replace(/(^\s*)|(\s*$)/g, "")
        if (!this.couponTypeInfo.couponName) {
          return
        }
        let params = {...this.couponTypeInfo}
        request({
          url: '/management/couponType/updateCouponType',
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
        this.$confirm('是否确定删除该优惠券类型?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          request({
            url: '/management/couponType/deleteCouponType',
            method: 'post',
            params: {couponTypeId: this.couponTypeId}
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
        params.couponTypeId = this.couponTypeId
        request({
          url: 'management/couponType/getCouponTypeDetail',
          method: 'post',
          params
        }).then(response => {
            this.couponTypeInfo = response.data
        }, error => {

        })
      }
    }
  }
</script>

<style>
</style>
