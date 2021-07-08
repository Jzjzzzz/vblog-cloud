<template>
  <div class="detailView" v-if="merchantInfo">
    <div class="item">
      <div class="name">商家ID：</div>
      <div class="value">
        <el-input v-model="merchantInfo.merchantId" :disabled="true" style="width: 200px"></el-input>
      </div>
    </div>
    <div class="item">
      <div class="name">商家名称：</div>
      <div class="value">
        <el-input v-model="merchantInfo.merchantName" style="width: 200px"></el-input>
      </div>
    </div>
    <div class="item">
      <div class="name">商家手机号：</div>
      <div class="value">
        <el-input v-model="merchantInfo.merchantPhone" maxlength="11" style="width: 200px"></el-input>
      </div>
    </div>
    <div class="item">
      <div class="name">商家地址：</div>
      <div class="value">
        <el-input v-model="merchantInfo.merchantAddress" :disabled="true" style="width:100%;"></el-input>
      </div>
    </div>
    <div class="item">
      <div class="name">服务次数：</div>
      <div class="value">
        <el-input v-model="merchantInfo.serviceNumber" style="width: 50px"></el-input>
      </div>
    </div>
    <div class="item">
      <div class="name">营业时间：</div>
      <div class="value">
        <el-input v-model="merchantInfo.businessTime" :disabled="true" style="width: 100px"></el-input>
      </div>
    </div>
    <div class="item">
      <div class="name">联系电话1：</div>
      <div class="value">
        <el-input v-model="merchantInfo.contactPhone1" maxlength="11" style="width: 200px"></el-input>
      </div>
    </div>
    <div class="item">
      <div class="name">联系电话2：</div>
      <div class="value">
        <el-input v-model="merchantInfo.contactPhone2" maxlength="11" style="width: 200px"></el-input>
      </div>
    </div>
    <div class="item">
      <div class="name">商家状态：</div>
      <div class="value">
        <el-select v-model="merchantInfo.merchantState" style="width:80px">
          <el-option v-for="item in stateList" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </div>
    </div>
    <div class="item">
      <div class="name">添加时间：</div>
      <div class="value">
        <el-input v-model="merchantInfo.addDate" :disabled="true" style="width: 200px"></el-input>
      </div>
    </div>
    <div class="item">
      <div class="name">商家签名：</div>
      <div class="value">
        <el-image class="signImg" :src="merchantInfo.signImage[0]" :preview-src-list="merchantInfo.signImage" />
      </div>
    </div>
    <div class="item">
      <div class="name">商家身份证：</div>
      <div class="value">
        <el-image class="signImg" :src="merchantInfo.idcardImage" :preview-src-list="merchantInfo.idcardImage" />
      </div>
    </div>
    <div class="item">
      <div class="name">商家银行卡：</div>
      <div class="value">
        <el-image class="signImg" :src="merchantInfo.bankcardImage" :preview-src-list="merchantInfo.bankcardImage" />
      </div>
    </div>
    <div class="item">
      <div class="name">商家图片：</div>
      <div class="value">
        <el-carousel height="200px" indicator-position="outside">
          <el-carousel-item v-for="item in merchantInfo.imageArr" :key="item">
            <el-image style="width:100%; height:100%;" :src="item" />
          </el-carousel-item>
        </el-carousel>
      </div>
    </div>
    <div class="eventView">
      <el-button type="danger" @click="editClick">修改</el-button>
      <el-button type="primary" @click="closeClick(false)">取消</el-button>
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
      merchantId: {
        type: Number,
        required: true
      }
    },
    data() {
      return {
        merchantInfo: null,
        stateList: [{
          value: 0,
          label: "关闭"
        }, {
          value: 1,
          label: "营业"
        }, {
          value: 2,
          label: "休息"
        }]
      }
    },
    mounted() {
      this.queryData()
    },
    methods: {
      /**
       * 修改按钮点击
       */
      editClick: function() {
        let params = {}
        let merchantInfo = this.merchantInfo
        params.merchantId = merchantInfo.merchantId
        params.merchantName = merchantInfo.merchantName
        params.merchantPhone = merchantInfo.merchantPhone
        params.serviceNumber = merchantInfo.serviceNumber
        params.contactPhone1 = merchantInfo.contactPhone1
        params.contactPhone2 = merchantInfo.contactPhone2
        params.merchantState = merchantInfo.merchantState
        request({
          url: '/management/merchant/updateMerchantDetail',
          method: 'post',
          params
        }).then(response => {
          Message({
            message: '修改成功',
            type: 'success',
            duration: 5 * 1000
          })
          this.closeClick(true)
        }, error => {

        })
      },

      /**
       * 关闭详情窗口
       */
      closeClick(isRefresh) {
        this.$emit('closeClick',isRefresh);
      },

      /**
       * 查询详情数据
       */
      queryData: function() {
        let params = {}
        params.merchantId = this.merchantId
        request({
          url: '/management/merchant/getMerchantDetail',
          method: 'post',
          params
        }).then(response => {
          let data = response.data
          let merchantId = data.merchantId

          //解析商家身份证图片路径
          data.idcardImage = "/api/image/idcard/" + merchantId + data.idcardImage

          //解析商家银行卡图片路径
          data.bankcardImage = "/api/image/bankcard/" + merchantId + data.bankcardImage

          //解析签名图片路径
          let signImage = []
          signImage.push("/api/image/sign/" + merchantId + data.signImage)
          data.signImage = signImage

          //解析商家图片路径
          let imageArr = data.merchantImage.split(";")
          for (let i = 0; i < imageArr.length; i++) {
            if (imageArr[i] != "") {
              imageArr[i] = "/api/image/merchant/" + merchantId + imageArr[i]
            } else {
              imageArr.splice(i, 1)
            }
          }
          //添加logo图片
          if (data.merchantLogo) {
            imageArr.unshift("/api/image/merchant/" + merchantId + data.merchantLogo)
          }
          data.imageArr = imageArr
          this.merchantInfo = data
        }, error => {

        })
      }
    }
  }
</script>

<style>
  .signImg {
    width: 30%;
    height: 30%;
    border: 1px solid #f0f0f0;
    background-color: #fff;
  }
</style>
