<template>
  <div class="detailView" v-if="userInfo">
    <div class="item">
      <div class="name">用户ID：</div>
      <div class="value">
        <el-input v-model="userInfo.userId" :disabled="true" style="width: 200px"></el-input>
      </div>
    </div>
    <div class="item">
      <div class="name">姓名：</div>
      <div class="value">
        <el-input v-model="userInfo.userName" style="width: 200px"></el-input>
      </div>
    </div>
    <div class="item">
      <div class="name">手机号：</div>
      <div class="value">
        <el-input v-model="userInfo.userPhone" style="width: 200px"></el-input>
      </div>
    </div>
    <div class="item">
      <div class="name">车型：</div>
      <div class="value">
        <el-input v-model="userInfo.userCarType" style="width: 200px"></el-input>
      </div>
    </div>
    <div class="item">
      <div class="name">车牌号：</div>
      <div class="value">
        <el-input v-model="userInfo.userCarNum" style="width: 200px"></el-input>
      </div>
    </div>
    <div class="item">
      <div class="name">微信昵称：</div>
      <div class="value">
        <el-input v-model="userInfo.wechatNikeName" :disabled="true" style="width: 200px"></el-input>
      </div>
    </div>

    <div class="item">
      <div class="name">注册时间：</div>
      <div class="value">
        <el-input v-model="userInfo.registerDate" :disabled="true" style="width: 200px"></el-input>
      </div>
    </div>
    <div class="item">
      <div class="name">上次登录时间：</div>
      <div class="value">
        <el-input v-model="userInfo.lastLoginDate" :disabled="true" style="width: 200px"></el-input>
      </div>
    </div>
    <div class="item">
      <div class="name">微信openId：</div>
      <div class="value">
        <el-input v-model="userInfo.wechatOpenid" :disabled="true" style="width: 200px"></el-input>
      </div>
    </div>
    <div class="item">
      <div class="name">微信头像：</div>
      <div class="value">
        <el-image class="signImg" :src="userInfo.wechatAvatarUrl" />
      </div>
    </div>
    <div class="eventView">
      <el-button type="danger" @click="editClick">修改</el-button>
      <el-button type="primary" @click="closeClick">取消</el-button>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request'
import { MessageBox, Message } from 'element-ui'

export default {
  props: {
    userId: {
      type: Number,
      required: true
    }
  },
  data() {
    return {
      userInfo: null
    }
  },
  mounted() {
    this.queryData()
  },
  methods: {
    editClick: function() {
      let params = { ...this.userInfo }
      request({
        url: '/management/user/updateUserDetail',
        method: 'post',
        params
      }).then(
        response => {
          Message({
            message: '修改成功',
            type: 'success',
            duration: 5 * 1000
          })
          this.closeClick(true)
        },
        error => {}
      )
    },

    closeClick() {
      this.$emit('closeClick')
    },

    queryData: function() {
      let params = {}
      params.userId = this.userId
      request({
        url: 'management/user/getUserDetail',
        method: 'post',
        params
      }).then(
        response => {
          this.userInfo = response.data
        },
        error => {}
      )
    }
  }
}
</script>

<style>
</style>
