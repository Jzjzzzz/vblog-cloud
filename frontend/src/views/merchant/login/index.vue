<template>
  <div class="content">
    <div class="background"></div>
    <div class="topView">
      <div class="logo">
        <img src="@/assets/btl_logo/logo2.png" />
      </div>
      <div class="navigation">
        <div class="item">首页</div>
        <div class="item">小程序</div>
        <div class="item">
          商家入驻
          <div class="index"></div>
        </div>
        <div class="item">服务业务</div>
        <div class="item">关于我们</div>
      </div>
    </div>
    <div class="centerView">
      <div class="left">
        <div class="title">商家后台</div>
        <div class="item">
          <img src="@/assets/login_background/img1.png" />
          <div class="text">BTL-车主的全方位汽车生活服务者</div>
        </div>
        <div class="item">
          <img src="@/assets/login_background/img2.png" />
          <div class="text">BTL-汽车售后市场行业资源整合者</div>
        </div>
      </div>
      <div class="right">
        <div class="logo">
          <img src="@/assets/btl_logo/logo2.png" />
        </div>
        <div class="title">网页端后台登录</div>
        <div class="form">
          <div class="item">
            <input placeholder="请输入手机号" v-model="formData.phone" maxlength="11" />
          </div>
          <div class="item">
            <input placeholder="请输入短信码" v-model="formData.code" maxlength="8" />
            <div class="event" @click="getSmsClick">{{smsTips}}</div>
          </div>

          <div class="eventView">
            <div>商家入驻</div>
            <div class="main" @click="login">登录后台</div>
          </div>
        </div>
      </div>
    </div>
    <div class="bottomView">
      <div class="left">
        <div class="title">联系我们</div>
        <div class="item">邮箱：286557247@qq.com</div>
        <div class="item">电话：+86 156 1898 5936</div>
        <div class="item">7X24小时在线</div>
      </div>
      <div class="center">
        <div class="item">网站首页 | 服务条款 | 友情链接 | 关于我们</div>
        <div class="item">©2020 上海谦智汽车服务有限公司 沪ICP备19046602号-1</div>
      </div>
      <div class="right">
        <div class="title">关注公众号</div>
        <div>
          <img src="@/assets/login_background/qrcode.png" />
        </div>
        <div>
          <img src="@/assets/login_background/qrcode.png" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import request from '@/utils/request'
import { MessageBox, Message } from 'element-ui'
import { constantRoutes } from '@/router'
import merchantRouter from '@/router/modules/merchant'

export default {
  data() {
    return {
      smsTips: '获取短信码',
      getSmsTime: 0,
      getSmsState: true,
      getSmsInterval: null,
      formData: {
        phone: '',
        code: ''
      }
    }
  },
  methods: {
    getSmsClick() {
      if (this.getSmsState) {
        if (this.formData.phone.toString().length === 11) {
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
          params.phone = this.formData.phone
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
    login() {
      if (
        true
        // this.formData.phone.toString().length === 11 &&
        // this.formData.code.toString().length >= 4
      ) {
        this.loading = true
        const params = this.formData
        params.type = 'merchant'
        request({
          url: '/article/list/1/10',
          method: 'get',
          params
        }).then(
          response => {
            this.loading = false
            console.log(response)

            this.$router.addRoutes(merchantRouter)
            this.$router.options.routes = this.$router.options.routes
              .slice(0, constantRoutes.length)
              .concat(merchantRouter)
            sessionStorage.setItem('managementInfo', null)
            sessionStorage.setItem('merchantInfo', true)
            this.loading = false
            this.$router.push({ path: this.redirect || '/' })
          },
          error => {
            this.loading = false
            console.log(error)
          }
        )
      } else {
        Message({
          message: '请检查手机号或验证码',
          type: 'error',
          duration: 5 * 1000
        })
      }
    }
  }
}
</script>

<style scoped>
.content {
  width: 1920px;
  height: 937px;
  background-color: rgba(0, 0, 0, 0.6);
  padding: 0 7.3%;
  font-family: Source Han Serif SC;
  color: #fff;
  position: relative;
}
.content:before {
  content: '';
  background: url('~@/assets/login_background/background.png');
  background-repeat: no-repeat;
  background-size: cover;
  position: absolute;
  width: 1920px;
  height: 937px;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: -1;
}
.content .topView {
  padding-top: 68px;
  display: flex;
  justify-content: space-between;
}
.content .topView .logo img {
  width: 50%;
}
.content .topView .navigation {
  display: flex;
  font-weight: 500;
  font-size: 30px;
  align-items: center;
}
.content .topView .navigation .item {
  margin: 0 40px;
  position: relative;
}

.content .topView .navigation .item:hover {
  transform: scale(1.5);
  transition: transform 0.5s;
  cursor: pointer;
}
.content .topView .navigation .item .index {
  height: 5px;
  background-color: #fc0c1b;
  width: 50px;
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  margin-top: 20px;
  border-radius: 10px;
}

.content .centerView {
  margin-top: 5%;
  display: flex;
  justify-content: space-between;
}
.content .centerView .left .title {
  font-size: 40px;
  font-weight: bold;
}
.content .centerView .left .item {
  width: 300px;
  margin-top: 30px;
  position: relative;
}
.content .centerView .left .item img {
  width: inherit;
}
.content .centerView .left .item .text {
  position: absolute;
  bottom: 20px;
  left: 30px;
}

.content .centerView .right {
  width: 420px;
  height: 420px;
  margin-right: 100px;
  background-color: rgba(0, 0, 0, 0.5);
  border-radius: 10px;
}
.content .centerView .right .logo {
  margin: auto;
  width: 200px;
  padding: 30px 0;
}
.content .centerView .right .logo img {
  width: inherit;
}
.content .centerView .right .title {
  text-align: center;
  color: #999;
  font-size: 12px;
  letter-spacing: 8px;
}
.content .centerView .right .form {
  margin-top: 30px;
}

.content .centerView .right .form .item {
  width: 330px;
  height: 45px;
  line-height: 45px;
  background-color: rgba(255, 255, 255, 0.2);
  margin: auto;
  margin-bottom: 20px;
  border-radius: 30px;
  position: relative;
}
.content .centerView .right .form .item input {
  background-color: rgba(0, 0, 0, 0);
  border: 0;
  margin-left: 30px;
  color: #eee;
  font-weight: bold;
  font-size: 16px;
  outline: none;
}
input::-webkit-input-placeholder {
  color: #eee;
  font-weight: bold;
}
.content .centerView .right .form .item .event {
  position: absolute;
  right: 20px;
  top: 0;
  color: #fc0c1b;
  cursor: pointer;
}

.eventView {
  display: flex;
  width: 300px;
  margin: auto;
  margin-top: 50px;
  justify-content: space-between;
  align-items: center;
}
.eventView  div{
cursor: pointer;
}
.eventView .main {
  width: 120px;
  height: 40px;
  line-height: 40px;
  text-align: center;
  border-radius: 20px;
  background-color: #fc0c1b;

}

.bottomView {
  position: absolute;
  bottom: 0;
  right: 0;
  left: 0;
  padding: 20px 150px;
  display: flex;
  justify-content: space-between;
  background: linear-gradient(
    10deg,
    rgba(150, 150, 150, 0.8),
    rgba(0, 0, 0, 0.5),
    rgba(150, 150, 150, 0.8),
    rgba(0, 0, 0, 0.5),
    rgba(150, 150, 150, 0.8)
  );
}
.bottomView .title {
  font-size: 30px;
  font-weight: bold;
  padding-bottom: 20px;
}
.bottomView .left .item {
  margin-bottom: 10px;
}
.bottomView .center {
  margin-top: 80px;
  text-align: center;
}
.bottomView .center .item {
  margin-bottom: 10px;
}
.bottomView .right {
  width: 200px;
}
.bottomView .right img {
  width: 80px;
  float: left;
  margin-right: 20px;
}
</style>
