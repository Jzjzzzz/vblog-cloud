<template>
  <div class="login-container">
    <el-form
      ref="loginForm"
      :model="loginForm"
      :rules="loginRules"
      class="login-form"
      auto-complete="on"
      label-position="left"
    >
      <div class="title-container">
        <h3 class="title">平台管理员登录</h3>
      </div>

      <el-form-item prop="phone">
        <span class="svg-container">
          <svg-icon icon-class="user" />
        </span>
        <el-input
          ref="phone"
          v-model="loginForm.phone"
          placeholder="请输入手机号"
          name="phone"
          type="text"
          tabindex="1"
          auto-complete="on"
        />
      </el-form-item>

      <el-form-item prop="password">
        <span class="svg-container">
          <svg-icon icon-class="password" />
        </span>
        <el-input
          :key="passwordType"
          ref="password"
          v-model="loginForm.password"
          :type="passwordType"
          placeholder="请输入验证码"
          name="password"
          tabindex="2"
          auto-complete="on"
          @keyup.enter.native="handleLogin"
        />
        <div class="event" @click="getSmsClick">{{smsTips}}</div>
      </el-form-item>

      <el-button
        :loading="loading"
        type="primary"
        style="width:100%;margin-bottom:30px;"
        @click.native.prevent="handleLogin"
      >登录管理平台</el-button>
    </el-form>
  </div>
</template>

<script>
import { validphone } from '@/utils/validate'
import { constantRoutes } from '@/router'
import managementRouter from '@/router/modules/management'
import request from '@/utils/request'

export default {
  name: 'Login',

  data() {
    const validatephone = (rule, value, callback) => {
      if (!validphone(value)) {
        callback(new Error('请输入管理员手机号'))
      } else {
        callback()
      }
    }
    const validatePassword = (rule, value, callback) => {
      if (value.length < 4) {
        callback(new Error('请输入验证码'))
      } else {
        callback()
      }
    }
    return {
      loginForm: {
        phone: '',
        password: ''
      },
      loginRules: {
        phone: [{ required: true, trigger: 'blur', validator: validatephone }],
        password: [
          { required: true, trigger: 'blur', validator: validatePassword }
        ]
      },
      loading: false,
      passwordType: 'password',
      redirect: undefined,
      smsTips: '获取短信码',
      getSmsState: true,
      getSmsInterval: null,
      getSmsTime: 0
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
  methods: {
    getSmsClick() {
      if (this.getSmsState) {
        if (this.loginForm.phone.toString().length === 11) {
          this.getSmsState = false
          this.getSmsTime = 59
          this.smsTips = '重新获取(' + this.getSmsTime + ')'
          this.getSmsInterval = setInterval(() => {
            if (this.getSmsTime <= 1) {
              this.getSmsState = true
              this.getSmsTime = 1
              this.smsTips = '重新获取'
              clearInterval(this.getSmsInterval)
            } else {
              this.getSmsTime = this.getSmsTime - 1
              this.smsTips = '重新获取(' + this.getSmsTime + ')'
            }
          }, 1000)

          let params = {}
          params.phone = this.loginForm.phone
          params.type = 'merchant'
          request({
            url: '/sms/sendSmsCode',
            method: 'post',
            params: params
          }).then(
            response => {},
            error => {}
          )
        } else {
          Message({
            message: '手机号不正确',
            type: 'error',
            duration: 5 * 1000
          })
        }
      }
    },
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          let params = this.loginForm
          request({
            url: '/management/admin/login',
            method: 'post',
            params
          }).then(
            Response => {
              console.log(22)
              this.$router.addRoutes(managementRouter)
              this.$router.options.routes = this.$router.options.routes
                .slice(0, constantRoutes.length)
                .concat(managementRouter)
              sessionStorage.setItem('managementInfo', true)
              sessionStorage.setItem('merchantInfo', null)
              this.loading = false

              this.$router.push({ path: this.redirect || '/' })
              this.loading = false
            },
            error => {
              this.loading = false
            }
          )
        } else {
          console.log('error submit!!')
          return false
        }
      })
    }
  }
}
</script>

<style lang="scss">
/* 修复input 背景不协调 和光标变色 */
/* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

$bg: #283443;
$light_gray: #fff;
$cursor: #fff;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}

/* reset element-ui css */
.login-container {
  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;

    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
      caret-color: $cursor;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }

  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.1);
    border-radius: 5px;
    color: #454545;
  }
}
</style>

<style lang="scss" scoped>
$bg: #2d3a4b;
$dark_gray: #889aa4;
$light_gray: #eee;

.login-container {
  min-height: 100%;
  width: 100%;
  background-color: $bg;
  overflow: hidden;

  .login-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 160px 35px 0;
    margin: 0 auto;
    overflow: hidden;
  }

  .tips {
    font-size: 14px;
    color: #fff;
    margin-bottom: 10px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $dark_gray;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
  }

  .title-container {
    position: relative;

    .title {
      font-size: 26px;
      color: $light_gray;
      margin: 0px auto 40px auto;
      text-align: center;
      font-weight: bold;
    }
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $dark_gray;
    cursor: pointer;
    user-select: none;
  }
}

.event {
  position: absolute;
  right: 20px;
  top: 5px;
  color: #409eff;
  font-weight: bold;
  cursor: pointer;
}
</style>
