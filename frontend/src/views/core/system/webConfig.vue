<template>
  <div class="app-container">
    <el-tabs type="border-card">
      <el-tab-pane label="网站信息">
        <span slot="label">
          <i class="el-icon-date" />
          网站信息
        </span>

        <el-form ref="form" :model="form" label-width="80px">
          <el-form-item label="LOGO">
            <el-upload
              :action="handleBeforeUploadImg()"
              :on-success="onUploadSuccessLOGO"
              :on-remove="onUploadRemove"
              :multiple="false"
              :on-change="handleEditChange"
              :class="{ hide: hideUpload }"
              :data="{ module: 'LOGO' }"
              :limit="1"
              :file-list="fileList"
              list-type="picture-card"
            >
              <i class="el-icon-plus"></i>
            </el-upload>
          </el-form-item>
          <el-row :gutter="24">
            <el-col :span="10">
              <el-form-item label="网站名称" prop="oldPwd">
                <el-input v-model="form.name" style="width: 400px" />
              </el-form-item>
            </el-col>
            <el-col :span="10">
              <el-form-item label="标题" prop="newPwd1">
                <el-input v-model="form.title" style="width: 400px" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="24">
            <el-col :span="10">
              <el-form-item label="关键字" prop="newPwd2">
                <el-input
                  v-model="form.keyword"
                  style="width: 400px"
                />
              </el-form-item>
            </el-col>
            <el-col :span="10">
              <el-form-item label="描述" prop="newPwd1">
                <el-input
                  v-model="form.summary"
                  style="width: 400px"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="24">
            <el-col :span="10">
              <el-form-item label="作者" prop="newPwd2">
                <el-input v-model="form.author" style="width: 400px" />
              </el-form-item>
            </el-col>
            <el-col :span="10">
              <el-form-item label="备案号" prop="newPwd2">
                <el-input
                  v-model="form.recordnum"
                  style="width: 400px"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item>
            <el-button type="primary" @click="submitForm()">
              保 存
            </el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane v-permission="'/webConfig/getWebConfig'">
        <span slot="label">
          <i class="el-icon-date" />
          关注我们
        </span>
        <el-form
          ref="form"
          style="margin-left: 20px;"
          label-position="left"
          :model="form"
          label-width="80px"
        >
          <el-checkbox-group v-model="form.showList">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" style="width: 400px" />
              <el-checkbox label="1" style="margin-left: 10px">
                在首页显示
              </el-checkbox>
            </el-form-item>

            <el-form-item label="QQ号" prop="qqnumber">
              <el-input v-model="form.qqnumber" style="width: 400px" />
              <el-checkbox label="2" style="margin-left: 10px">
                在首页显示
              </el-checkbox>
            </el-form-item>

            <el-form-item label="QQ群" prop="qqGroup">
              <el-input v-model="form.qqgroup" style="width: 400px" />
              <el-checkbox label="3" style="margin-left: 10px">
                在首页显示
              </el-checkbox>
            </el-form-item>
          </el-checkbox-group>
          <el-form-item>
            <el-button
              v-permission="'/webConfig/editWebConfig'"
              type="primary"
              @click="submitForm()"
            >
              保 存
            </el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<style>
.hide .el-upload--picture-card {
  display: none;
}
</style>
<script>
import webConfigApi from '@/api/core/webConfig'
export default {
  // 定义数据模型
  data() {
    return {
      form: {
        name: '',
        title: '',
        keyword: '',
        summary: '',
        author: '',
        logo: '',
        qqnumber: '',
        qqgroup: '',
        recordnum: '',
        openComment: '1',
        showList: [],
        loginTypeList: []
      },
      hideUpload: false,
      limitCount: 1,
      uploadUrl: '/file/upload', // 图片上传地址
      BASE_API: process.env.VUE_APP_BASE_API, // 获取后端接口地址
      fileList: []
    }
  },
  // 页面渲染成功后获取数据
  created() {
    this.fetchData()
  },

  // 定义方法
  methods: {
    handleBeforeUploadImg: function() {
      console.log(this.BASE_API + this.uploadUrl)
      return this.BASE_API + this.uploadUrl
    },
    handleEditChange(file, fileList) {
      console.log(file)
      this.hideUpload = fileList.length >= this.limitCount
    },
    handleChange(file, fileList) {
      webConfigApi.uploadImage(file).then(response => {
        this.$message.success(response.message)
      })
    },
    onUploadRemove(file, fileList) {
      // 删除oss服务器上的内容
      this.url = file.url
      webConfigApi.delete(this.url).then(response => {
        this.$message.success(response.message)
        this.hideUpload = fileList.length >= this.limitCount
      })
    },
    onUploadSuccessLOGO(response, file) {
      this.onUploadSuccess(response, file, 'LOGO')
    },
    onUploadSuccess(response, file, type) {
      // debugger
      if (response.code !== 200) {
        this.$message.error(response.message)
        return
      }
      // 填充上传文件
      console.log(response)
      this.form.logo = response.data
    },
    fetchData() {
      // 调用api
      webConfigApi.getWebConfig().then(response => {
        this.form = response.data
        this.fileList.push({
          name: 'logo',
          url: this.form.logo
        })
      })
    },

    submitForm() {
      const form = this.form
      webConfigApi.editWebConfig(form).then(response => {
        this.$message.success('保存成功')
        this.fileList.splice(0)
        this.fetchData()
      })
    }
  }
}
</script>
