<template>
  <div class="detailView" v-if="orderInfo">
    <div class="item">
      <div class="name">订单ID：</div>
      <div class="value">
        <el-input v-model="orderInfo.orderId" :disabled="true" style="width: 200px"></el-input>
      </div>
    </div>
    <div class="item">
      <div class="name">用户名称：</div>
      <div class="value">
        <el-input v-model="orderInfo.userName" :disabled="true" style="width: 200px"></el-input>
      </div>
    </div>
    <div class="item">
      <div class="name">用户手机号：</div>
      <div class="value">
        <el-input v-model="orderInfo.userPhone" :disabled="true" style="width: 200px"></el-input>
      </div>
    </div>
    <div class="item">
      <div class="name">用户车型：</div>
      <div class="value">
        <el-input v-model="orderInfo.carType" :disabled="true" style="width: 200px"></el-input>
      </div>
    </div>
    <div class="item">
      <div class="name">用户车牌号：</div>
      <div class="value">
        <el-input v-model="orderInfo.carNum" :disabled="true" style="width: 200px"></el-input>
      </div>
    </div>
    <div class="item">
      <div class="name">服务名称：</div>
      <div class="value">
        <el-input v-model="orderInfo.serviceName" :disabled="true" style="width: 200px"></el-input>
      </div>
    </div>
    <div class="item">
      <div class="name">提交时间：</div>
      <div class="value">
        <el-input v-model="orderInfo.submitDate" :disabled="true" style="width: 200px"></el-input>
      </div>
    </div>
    <div class="item">
      <div class="name">预约时间：</div>
      <div class="value">
        <el-input v-model="orderInfo.bookedDate" :disabled="true" style="width: 200px"></el-input>
      </div>
    </div>
    <div class="item">
      <div class="name">完成/取消时间：</div>
      <div class="value">
        <el-input v-model="orderInfo.completeDate" :disabled="true" style="width: 200px"></el-input>
      </div>
    </div>
    <div class="item">
      <div class="name">订单状态：</div>
      <div class="value">
        <el-select v-model="orderInfo.orderState" :disabled="true" style="width:100px">
          <el-option
            v-for="item in stateList"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </div>
    </div>

    <div class="item" v-if="orderInfo.completeImage">
      <div class="name">完成图片1：</div>
      <div class="value">
        <el-image
          class="completeImage"
          :src="orderInfo.imageArr[0]"
          :preview-src-list="orderInfo.imageArr"
        />
      </div>
    </div>
    <div class="item" v-if="orderInfo.completeImage">
      <div class="name">完成图片2：</div>
      <div class="value">
        <el-image
          class="completeImage"
          :src="orderInfo.imageArr[1]"
          :preview-src-list="orderInfo.imageArr"
        />
      </div>
    </div>

    <div class="eventView">
      <el-button type="primary" @click="closeClick(false)">关闭</el-button>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request'
import { MessageBox, Message } from 'element-ui'

export default {
  props: {
    orderId: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      orderInfo: null,
      stateList: [
        {
          value: 0,
          label: '已预约'
        },
        {
          value: 1,
          label: '已完成'
        },
        {
          value: 2,
          label: '已评价'
        },
        {
          value: 3,
          label: '已取消'
        },
        {
          value: 4,
          label: '已删除'
        },
        {
          value: 5,
          label: '已接受'
        }
      ]
    }
  },
  mounted() {
    this.queryData()
  },
  methods: {

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
      params.orderId = this.orderId
      request({
        url: '/merchant/order/getOrderDetail',
        method: 'post',
        params
      }).then(
        response => {
          let data = response.data
          let orderId = data.orderId

          if (data.completeImage) {
            //解析完成图片路径
            let imageArr = data.completeImage.split(';')
            for (let i = 0; i < imageArr.length; i++) {
              if (imageArr[i] != '') {
                imageArr[i] = '/api/image/order/' + orderId + imageArr[i]
              } else {
                imageArr.splice(i, 1)
              }
            }
            data.imageArr = imageArr
          }
          this.orderInfo = data
        },
        error => {}
      )
    }
  }
}
</script>

<style>
.completeImage {
  width: 30%;
  height: 30%;
  border: 1px solid #f0f0f0;
  background-color: #fff;
}
</style>
