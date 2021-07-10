<template>
  <div class="login">
    <div class="scene">
      <div class="wrap">
        <div class="wall wall-right" />
        <div class="wall wall-left" />
        <div class="wall wall-top" />
        <div class="wall wall-bottom" />
        <div class="wall wall-back" />
      </div>
      <div class="wrap">
        <div class="wall wall-right" />
        <div class="wall wall-left" />
        <div class="wall wall-top" />
        <div class="wall wall-bottom" />
        <div class="wall wall-back" />
      </div>
    </div>
    <el-form v-loading="loading" :rules="rules" class="login-container" label-position="left" label-width="0px">
      <h3 class="login_title">{{WebName}}</h3>
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="密码登录" name="first">
          <el-form-item prop="account">
            <el-input v-model="loginForm.username" type="text" auto-complete="off" placeholder="账号" />
          </el-form-item>
          <el-form-item prop="checkPass">
            <el-input v-model="loginForm.password" type="password" auto-complete="off" placeholder="密码" />
          </el-form-item>
          <el-checkbox v-model="checked" class="login_remember" label-position="left">记住密码</el-checkbox>
          <el-form-item style="width: 100%">
            <el-button type="primary" style="width: 100%" @click.native.prevent="submitClick">登录</el-button>
          </el-form-item>
        </el-tab-pane>
        <el-tab-pane label="手机登录" name="second">
          <el-form-item >
            <el-input v-model="loginForm.mobile" type="text" auto-complete="off" placeholder="手机号码" />
          </el-form-item>
          <el-form-item >
            <el-button v-if="!sending" type="primary" icon="el-icon-message" @click="sendBtn()">发送验证码</el-button>
            <el-button v-else disabled type="danger" icon="el-icon-check" >
              {{ leftSecond }}秒后重发
            </el-button>
          </el-form-item>

          <el-form-item>
            <el-input v-model="loginForm.code" type="password" auto-complete="off" placeholder="验证码" />
          </el-form-item>
          <el-form-item style="width: 100%">
            <el-button type="primary" style="width: 100%" @click.native.prevent="loginMobile">一键登录</el-button>
          </el-form-item>
        </el-tab-pane>
      </el-tabs>

    </el-form>
  </div>
</template>

<script>
import {
  validUsername
} from '@/utils/validate'
import { getUserList , send} from '@/api/user'
import webConfigApi from '@/api/core/webConfig'

export default {
  name: 'Login',
  data() {
    return {
      WebName: '',
      rules: {
        account: [{
          required: true,
          message: '请输入用户名',
          trigger: 'blur'
        }],
        checkPass: [{
          required: true,
          message: '请输入密码',
          trigger: 'blur'
        }]
      },
      checked: true,
      loginForm: {
        username: 'linghu',
        password: '123',
        mobile: '',
        code: ''
      },
      loading: false,
      activeName: 'second',
      sending: false,
      second: 60, // 倒计时间
      leftSecond: 0 // 剩余时间
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  created() {
    this.getWebName()
  },
  methods: {
    getWebName() {
      webConfigApi.getWebConfig().then(response => {
        console.log(response.data)
        this.WebName = response.data.name
      })
    },
    // 发短信
    sendBtn() {
      if (!this.loginForm.mobile) {
        this.$message.error('请输入手机号')
        return
      }
      // 防止重复提交
      if (this.sending) return
      this.sending = true
      // 倒计时
      this.timeDown()
      // 远程调用发送短信的接口
      send(this.loginForm.mobile).then(response => {
        this.$message.success(response.message)
      })
    },
    // 倒计时
    timeDown() {
      console.log('进入倒计时')
      this.leftSecond = this.second
      // 创建定时器
      const timmer = setInterval(() => {
        // 计数器减一
        this.leftSecond--
        if (this.leftSecond <= 0) {
          // 停止定时器
          clearInterval(timmer)
          // 还原计数器
          this.leftSecond = this.second
          // 还原按钮状态
          this.sending = false
        }
      }, 1000)
    },
    handleClick(tab, event) {
      console.log(tab, event)
    },
    submitClick: function() {
      var _this = this
      this.loading = true
      this.$store.dispatch('user/login', this.loginForm).then(() => {
        // 成功跳转
        _this.$router.replace({
          path: '/dashboard'
        })
      }).catch(err => {
        // 失败
        _this.$alert(err.message)
        this.loading = false
      })
    },

    loginMobile: function() {
      var _this = this
      this.loading = true
      this.$store.dispatch('user/loginMobile', this.loginForm).then(() => {
        // 成功跳转
        _this.$router.replace({
          path: '/dashboard'
        })
      }).catch(err => {
        // 失败
        _this.$alert(err.message)
        this.loading = false
      })
    }
  }
}
</script>

<style scoped="scoped">
  .login {
    height: 100%;
    overflow: auto;
  }

  .login-container {
    border-radius: 15px;
    background-clip: padding-box;
    margin: 180px auto;
    width: 350px;
    padding: 35px 35px 15px 35px;
    background: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
  }

  .login_title {
    margin: 0px auto 40px auto;
    text-align: center;
    color: #505458;
  }

  .login_remember {
    margin: 0px 0px 35px 0px;
    text-align: left;
  }

  .wall {
    background: url(../../assets/img/bg.jpg);
    background-size: cover;
  }

  html,
  body {
    width: 100%;
    height: 100%;
    overflow: hidden;
  }

  body {
    background: #000;
    text-align: center;
  }

  body::before {
    content: "";
    display: inline-block;
    height: 100%;
    vertical-align: middle;
  }

  .scene {
    display: inline-block;
    vertical-align: middle;
    perspective: 5px;
    perspective-origin: 50% 50%;
    position: relative;
  }

  .wrap {
    position: absolute;
    width: 1000px;
    height: 1000px;
    left: -500px;
    top: -500px;
    transform-style: preserve-3d;
    animation: move 15s infinite linear;
  }

  .wrap:nth-child(2) {
    animation: move 12s infinite linear;
  }

  .wall {
    width: 100%;
    height: 100%;
    position: absolute;
    opacity: 0;
    animation: fade 12s infinite linear;
  }

  .wall-right {
    transform: rotateY(90deg) translateZ(500px);
  }

  .wall-left {
    transform: rotateY(-90deg) translateZ(500px);
  }

  .wall-top {
    transform: rotateX(90deg) translateZ(500px);
  }

  .wall-bottom {
    transform: rotateX(-90deg) translateZ(500px);
  }

  .wall-back {
    transform: rotateX(180deg) translateZ(500px);
  }

  @keyframes move {
    0% {
      transform: translateZ(-500px) rotate(0deg);
    }

    100% {
      transform: translateZ(500px) rotate(0deg);
    }
  }

  @keyframes fade {
    0% {
      opacity: 0;
    }

    25% {
      opacity: 1;
    }

    75% {
      opacity: 1;
    }

    100% {
      opacity: 0;
    }
  }
</style>
