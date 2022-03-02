<template>
  <div class="user">
    <div style="margin-top: 10px; display: flex; justify-content: center">
      <el-input
        v-model="keywords"
        placeholder="默认展示部分用户，可以通过用户名搜索用户..."
        prefix-icon="el-icon-search"
        style="width: 400px"
        size="small"
      />
      <el-button
        type="primary"
        icon="el-icon-search"
        size="small"
        style="margin-left: 3px"
        @click="searchClick"
      >
        搜索
      </el-button>
      <el-button
        type="primary"
        icon="el-icon-circle-plus-outline"
        size="small"
        style="margin-left: 3px"
        @click="addClick"
      >
        新增
      </el-button>
    </div>
    <!-- 添加或修改对话框 -->
    <el-dialog
      title="新增用户"
      :visible.sync="dialogVisible"
      width="30%"
      @close="closeDialog"
    >
      <el-form ref="form" :model="form" :rules="rules">
        <el-form-item
          label="头像"
          :label-width="formLabelWidth"
          prop="userface"
        >
          <el-upload
            :action="handleBeforeUploadImg()"
            :on-success="onUploadSuccessUserface"
            :on-remove="onUploadRemove"
            :multiple="false"
            :on-change="handleEditChange"
            :class="{ hide: hideUpload }"
            :data="{ module: 'UserFace' }"
            :limit="1"
            :file-list="fileList"
            list-type="picture-card"
          >
            <i class="el-icon-plus" />
          </el-upload>
          <!--          <el-upload-->
          <!--            class="avatar-uploader"-->
          <!--            :action="handleBeforeUploadImg()"-->
          <!--            :on-success="onUploadSuccessUserface"-->
          <!--            :on-remove="onUploadRemove"-->
          <!--            :multiple="false"-->
          <!--            :on-change="handleEditChange"-->
          <!--            :class="{ hide: hideUpload }"-->
          <!--            :data="userface"-->
          <!--            :limit="1"-->
          <!--            :file-list="fileList"-->
          <!--            list-type="picture-card"-->
          <!--          >-->
          <!--            <i class="el-icon-plus"></i>-->
          <!--          </el-upload>-->
        </el-form-item>
        <el-form-item
          label="用户名"
          :label-width="formLabelWidth"
          prop="username"
        >
          <el-input v-model="form.username" auto-complete="off" />
        </el-form-item>
        <el-form-item label="邮箱" :label-width="formLabelWidth" prop="email">
          <el-input v-model="form.email" auto-complete="off" />
        </el-form-item>
        <el-form-item
          label="手机号码"
          :label-width="formLabelWidth"
          prop="mobile"
        >
          <el-input v-model="form.mobile" auto-complete="off" />
        </el-form-item>
        <el-form-item
          label="昵称"
          :label-width="formLabelWidth"
          prop="nickname"
        >
          <el-input v-model="form.nickname" auto-complete="off" />
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="approvalSubmit">确定</el-button>
      </div>
    </el-dialog>

    <div style="display: flex; justify-content: space-around; flex-wrap: wrap">
      <el-card
        v-for="(user, index) in users"
        :key="index"
        v-loading="cardloading[index]"
        style="width: 330px; margin-top: 10px"
      >
        <div slot="header" style="text-align: left">
          <span>{{ user.nickname }}</span>
          <el-button
            style="float: right; padding: 3px 0; color: #ff0509"
            type="text"
            icon="el-icon-delete"
            @click="deleteUser(user.id)"
          >
            删除
          </el-button>
        </div>
        <div>
          <div>
            <img
              :src="user.userface"
              :alt="user.nickname"
              style="width: 70px; height: 70px"
            >
          </div>
          <div
            style="
              text-align: left;
              color: #20a0ff;
              font-size: 12px;
              margin-top: 13px;
            "
          >
            <span>用户名:</span>
            <span>{{ user.username }}</span>
          </div>
          <div
            style="
              text-align: left;
              color: #20a0ff;
              font-size: 12px;
              margin-top: 13px;
            "
          >
            <span>电子邮箱:</span>
            <span>{{ user.email }}</span>
          </div>
          <div
            style="
              text-align: left;
              color: #20a0ff;
              font-size: 12px;
              margin-top: 13px;
            "
          >
            <span>注册时间:</span>
            <span>{{ user.regTime }}</span>
          </div>
          <div
            style="
              text-align: left;
              color: #20a0ff;
              font-size: 12px;
              margin-top: 13px;
              display: flex;
              align-items: center;
            "
          >
            <span>用户状态:</span>
            <el-switch
              v-model="user.enabled"
              active-text="启用"
              inactive-text="禁用"
              style="font-size: 12px"
              @change="enabledChange(user.enabled, user.id, index)"
            />
          </div>
        </div>
      </el-card>
    </div>
    <!-- 分页组件 -->
    <el-pagination
      :current-page="page"
      :total="total"
      :page-size="limit"
      :page-sizes="[6, 12]"
      style="padding: 30px 0"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="changePageSize"
      @current-change="changeCurrentPage"
    />
  </div>
</template>
<script>
import { getUserList, deleteRequest, userEnabled, saveUser } from '@/api/user'
import webConfigApi from '@/api/core/webConfig'

export default {
  data() {
    return {
      loading: false,
      eploading: [],
      cardloading: [],
      keywords: '',
      users: [],
      allRoles: [],
      roles: [],
      cpRoles: [],
      total: 0, // 数据库中的总记录数
      page: 1, // 默认页码
      limit: 12, // 每页记录数
      dialogVisible: false,
      formLabelWidth: '120px',
      hideUpload: false,
      limitCount: 1,
      uploadUrl: '/api/oss/fileoss/upload', // 图片上传地址
      BASE_API: process.env.VUE_APP_BASE_API, // 获取后端接口地址
      fileList: [],
      form: { roles: [{ id: 0, name: '' }] }, // 新增,
      rules: {
        username: [
          { required: true, message: '用户名不能为空', trigger: 'blur' },
          { min: 1, max: 20, message: '长度在1到20个字符' }
        ],
        nickname: [
          { required: true, message: '昵称不能为空', trigger: 'blur' },
          { min: 1, max: 20, message: '长度在1到20个字符' }
        ],
        // status: [{ required: true, message: '状态不能为空', trigger: 'blur' }],
        email: [
          {
            type: 'email',
            required: true,
            message: '请填写正确邮箱地址',
            trigger: 'blur'
          }
        ],
        mobile: [{ required: true, message: '号码不能为空', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getUserList(this.page, this.limit)
  },
  methods: {
    deleteUser(id) {
      var _this = this
      this.$confirm('删除该用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          _this.loading = true
          deleteRequest(id).then(
            (resp) => {
              if (resp.code == 200) {
                _this.$message({ type: 'success', message: '删除成功!' })
                _this.getUserList(this.page, this.limit)
                return
              }
              _this.loading = false
              _this.$message({ type: 'error', message: '删除失败!' })
            },
            (resp) => {
              _this.loading = false
              _this.$message({ type: 'error', message: '删除失败!' })
            }
          )
        })
        .catch(() => {
          _this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
    },
    // 关闭dialog时清空数据
    closeDialog() {
      this.form = {}
      this.onUploadRemove() // 删除图片
      this.$refs.form.clearValidate() // 移除该表单项的校验结果
    },
    handleBeforeUploadImg() {
      return this.BASE_API + this.uploadUrl
    },
    onUploadSuccessUserface(response, file) {
      this.onUploadSuccess(response, file, 'Userface')
    },
    onUploadSuccess(response, file, type) {
      // debugger
      if (response.code !== 200) {
        this.$message.error(response.message)
        return
      }
      // 填充上传文件
      this.form.userface = response.data
    },
    onUploadRemove(file, fileList) {
      // 删除oss服务器上的内容
      this.url = file.url
      webConfigApi.delete(this.url).then((response) => {
        this.$message.success(response.message)
        this.hideUpload = fileList.length >= this.limitCount
      })
    },
    handleEditChange(file, fileList) {
      console.log(file)
      this.hideUpload = fileList.length >= this.limitCount
    },
    // 提交表单
    approvalSubmit() {
      // this.from.concat({"roles":[{"id": 0,"name": ""}]})

      this.$refs.form.validate((valid) => {
        if (!valid) {
          console.log('校验出错')
        } else {
          if (this.form.id != null) {
            // 修改
          } else {
            // 新增
            saveUser(this.form).then((res) => {
              this.dialogVisible = false
              this.$message({ type: 'success', message: '新增成功!' })
              this.getUserList(this.page, this.limit)
            })
          }
        }
      })
    },
    addClick() {
      this.dialogVisible = true
    },
    enabledChange(enabled, id, index) {
      var _this = this
      // _this.cardloading.splice(index, 1, true)
      userEnabled(id).then(
        (resp) => {
          if (resp.code != 200) {
            _this.$message({ type: 'error', message: '更新失败!' })
            _this.getUserList(this.page, this.limit)
            return
          }
          // _this.cardloading.splice(index, 1, false)
          _this.$message({ type: 'success', message: '更新成功!' })
        },
        (resp) => {
          _this.$message({ type: 'error', message: '更新失败!' })
          _this.getUserList(this.page, this.limit)
        }
      )
    },
    searchClick() {
      this.loading = true
      console.log(this.keywords)
      getUserList(1, this.limit, this.keywords).then((response) => {
        this.users = response.data.records
        this.total = response.data.total
        console.log(response)
      })
    },
    getUserList(page, limit) {
      getUserList(page, limit, '').then((response) => {
        this.users = response.data.records
        this.total = response.data.total
        console.log(response)
      })
    },
    changePageSize(size) {
      this.limit = size
      this.getUserList()
    },
    changeCurrentPage(page) {
      this.page = page
      this.getUserList(page, this.limit)
    }
  }
}
</script>
