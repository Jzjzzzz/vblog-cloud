<template>
  <div class="login">
    <div class="scene">
      <div class="wrap">
        <div class="wall wall-right"></div>
        <div class="wall wall-left"></div>
        <div class="wall wall-top"></div>
        <div class="wall wall-bottom"></div>
        <div class="wall wall-back"></div>
      </div>
      <div class="wrap">
        <div class="wall wall-right"></div>
        <div class="wall wall-left"></div>
        <div class="wall wall-top"></div>
        <div class="wall wall-bottom"></div>
        <div class="wall wall-back"></div>
      </div>
    </div>
    <el-form :rules="rules" class="login-container" label-position="left" label-width="0px" v-loading="loading">
      <h3 class="login_title">系统登录</h3>
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="密码登录" name="first">
          <el-form-item prop="account">
            <el-input type="text" v-model="loginForm.username" auto-complete="off" placeholder="账号"></el-input>
          </el-form-item>
          <el-form-item prop="checkPass">
            <el-input type="password" v-model="loginForm.password" auto-complete="off" placeholder="密码"></el-input>
          </el-form-item>
          <el-checkbox class="login_remember" v-model="checked" label-position="left">记住密码</el-checkbox>
          <el-form-item style="width: 100%">
            <el-button type="primary" @click.native.prevent="submitClick" style="width: 100%">登录</el-button>
          </el-form-item>
        </el-tab-pane>
        <el-tab-pane label="手机登录" name="second">
          <el-form-item prop="account">
            <el-input type="text" v-model="loginForm.phone" auto-complete="off" placeholder="手机号码"></el-input>
          </el-form-item>
          <el-form-item prop="checkPass">
            <el-input type="password" v-model="loginForm.code" auto-complete="off" placeholder="验证码"></el-input>
          </el-form-item>
          <el-form-item style="width: 100%">
            <el-button type="primary" @click.native.prevent="submitClick" style="width: 100%">一键登录</el-button>
          </el-form-item>
        </el-tab-pane>
        </el-tab-pane>
      </el-tabs>

    </el-form>
  </div>
</template>

<script>
  import {
    validUsername
  } from '@/utils/validate'
  import request from '@/utils/request'
  import { getInfo, login } from '@/api/user'
  import { setToken } from '@/utils/auth'
  import store from '@/store'

  export default {
    name: 'Login',
    data() {
      return {
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
          phone: '',
          code: ''
        },
        loading: false,
        activeName: 'second'
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
      handleClick(tab, event) {
        console.log(tab, event);
      },
      submitClick: function() {
        var _this = this;
        this.loading = true;
        login({
          username: _this.loginForm.username,
          password: _this.loginForm.password,
        }).then(res => {
          // 成功
          // 设置Token
          setToken(res.data)
          // 拿用户信息
          getInfo().then(res=>{
            console.log('userinfo', res.data)
          })
          // 跳转
          _this.$router.replace({
            path: '/dashboard'
          });
        }).catch(err => {
          // 失败
          _this.$alert('失败!', err.message);
        });
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
