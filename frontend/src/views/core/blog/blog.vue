<template>
  <div class="app-container">
    <!--查询表单-->
    <el-form v-loading="loading" :inline="true" class="demo-form-inline">
      <el-form-item label="博客标题">
        <el-input v-model="title" placeholder="博客标题" />
      </el-form-item>
      <el-form-item
        label="状态"

        prop="cid"
      >
        <el-select
          v-model="state"
          size="small"
          placeholder="全部"
          @change="listByState(state)"
        >
          <el-option
            v-for="item in options"
            :key="item.id"
            :label="item.label"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-button type="primary" icon="el-icon-search" @click="fetchData1()">
        查询
      </el-button>
      <el-button
        class="grid-content bg-purple"
        type="primary"
        icon="el-icon-edit"
        size="medium"
        @click="dialogFormVisible = true"
      >
        新增
      </el-button>

      <el-button type="default" @click="resetData()">清空</el-button>



    </el-form>

    <!-- 表格 -->
    <el-table :data="list" border stripe>
      <el-table-column type="index" width="50" align="center" />
      <el-table-column prop="title" label="博客标题" align="center" />
      <el-table-column prop="author.nickname" label="作者" align="center" />
      <el-table-column prop="category" label="博客分类" align="center">
        <!--      <el-table-column prop="pageView" label="博客标签" align="center">-->
        <template slot-scope="scope">
          <el-tag type="success">{{ scope.row.category.cateName }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="tags" label="博客标签" align="center">
        <template slot-scope="scope">
          <el-tag v-for="item in scope.row.tags" effect="success">{{ item.tagName }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="pageView" label="博客点击数" align="center" />
      <el-table-column prop="publishDate" label="创建时间" align="center" />
      <el-table-column prop="editTime" label="修改时间" align="center" />
      <el-table-column prop="state" width="100" label="状态" align="center">
        <template slot-scope="scope">
          <template v-if="scope.row.state == 0">
            <span>草稿箱</span>
          </template>
          <template v-if="scope.row.state == 1">
            <span>已发表</span>
          </template>
          <template v-if="scope.row.state == 2">
            <span>已删除</span>
          </template>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="300" align="center">
        <template slot-scope="scope">
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-edit"
            @click="approvalShow(scope.row.id)"
          >
            修改
          </el-button>
          <el-button
            type="danger"
            size="mini"
            icon="el-icon-delete"
            @click="removeById(scope.row.id)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <el-pagination
      :current-page="page"
      :total="total"
      :page-size="limit"
      :page-sizes="[5, 10]"
      style="padding: 30px 0; "
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="changePageSize"
      @current-change="changeCurrentPage"
    />

    <!-- 图片缩略图弹出框 -->
    <el-dialog :visible.sync="dialogVisible">
      <img width="100%" :src="dialogImageUrl" alt="">
    </el-dialog>
    <!-- 添加或修改对话框 -->
    <el-dialog
      title="新增博客"
      :visible.sync="dialogFormVisible"
      fullscreen
      @close="closeDialog"
    >
      <el-form ref="form" :model="form" :rules="rules">
        <el-row>
          <el-col :span="12">
            <el-form-item
              label="标题"
              :label-width="formLabelWidth"
              prop="title"
            >
              <el-input v-model="form.title" type="textarea" />
            </el-form-item>
            <el-form-item
              style="margin-top:39px"
              label="简介"
              :label-width="formLabelWidth"
            >
              <el-input v-model="form.summary" type="textarea" />
            </el-form-item>
          </el-col>

        </el-row>

        <el-row>
          <el-col :span="4">
            <el-form-item
              label="分类"
              :label-width="formLabelWidth"
              prop="cid"
            >
              <el-select
                v-model="form.cid"
                size="small"
                placeholder="请选择"
                @change="blogSortValue(genreList)"
              >
                <el-option
                  v-for="item in genreList"
                  :key="item.id"
                  :label="item.cateName"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="4">
            <el-form-item label="标签" label-width="80px" prop="tags">
              <el-select
                v-model="form.tags"
                multiple
                size="small"
                placeholder="请选择"
                filterable
              >
                <el-option
                  v-for="item in tagList"
                  :key="item.id"
                  :label="item.tagName"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
          </el-col>

        </el-row>
        <el-form-item label="内容" :label-width="formLabelWidth" prop="mdContent">
          <mavon-editor v-model="form.mdContent" />
        </el-form-item>

        <el-form-item
          style="float: right; margin-right: 20px; margin-top: 20px;"
        >
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="submitForm(0)">存为草稿</el-button>
          <el-button type="primary" @click="submitForm(1)">确 定</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>
<script>
import webConfigApi from '@/api/core/webConfig'
import blogApi from '@/api/core/blog'
import MarkdownEditor from '@/components/MarkdownEditor'

export default {
  components: {
    MarkdownEditor
  },
  // 定义数据模型
  data() {
    return {
      srcList: [],
      list: [],
      loading: false,
      total: 0, // 数据库中的总记录数
      page: 1, // 默认页码
      limit: 5, // 每页记录数
      dialogImageUrl: '',
      dialogVisible: false, // 图片缩略图弹出框状态
      dialogFormVisible: false, // 新增博客弹出框状态
      formLabelWidth: '120px',
      state: null,
      options: [{
        id: '10',
        label: '全部'
      }, {
        id: '0',
        label: '草稿箱'
      }, {
        id: '1',
        label: '已发表'
      }, {
        id: '2',
        label: '已删除'
      }],
      form: {},
      rules: {
        title: [
          { required: true, message: '博客标题不能为空', trigger: 'blur' },
          { min: 1, max: 30, message: '长度在1到30个字符' }
        ],
        cid: [
          { required: true, message: '分类不能为空', trigger: 'blur' }
        ],
        tags: [{ required: true, message: '标签不能为空', trigger: 'blur' }],
        original: [
          { required: true, message: '是否原创不能为空', trigger: 'blur' }
        ],
        openComment: [
          { required: true, message: '是否开启评论不能为空', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '是否发布不能为空', trigger: 'blur' }
        ],
        mdContent: [{ required: true, message: '内容不能为空', trigger: 'blur' }]
      },
      tagList: {}, // 标签列表
      genreList: {}, // 分类列表
      starsDict: {}, // 推荐等级字典数据
      orderList: {}, // 是否原创字典数据
      commentList: {}, // 是否开启评论字典数据
      dataList: {}, // 是否上架字典数据
      uploadUrl: '/api/oss/file/upload', // 图片上传地址
      BASE_API: process.env.VUE_APP_BASE_API, // 获取后端接口地址
      fileList: 0,
      fileListurl: [],
      limitCount: 1,
      hideUpload: false,
      title: '' // 查询
    }
  },

  // 页面渲染成功后获取数据
  created() {
    this.fetchData()
  },
  // 定义方法
  methods: {
    approvalShow(row) {
      this.dialogFormVisible = true
      blogApi.getById(row).then(response => {
        this.form = response.data
        // 表单数据类型转换
        // var tag = new Array()
        // tag = this.form.tags.split(',')
        // tag = tag.map(function(data) {
        //   return +data
        // })
        // this.form.tags = response.data.tags.id
        const xx = response.data.tags.map(value => value.id)
        console.log(xx)
        this.form.tags = response.data.tags.map(value => value.id)
        this.form.cid = response.data.category.id
      })
    },
    // 根据id删除数据
    removeById(id) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          blogApi.removeById(id).then(response => {
            this.$message.success(response.message)
            this.fetchData()
          })
        })
        .catch(error => {
          this.$message.info('取消删除')
        })
    },
    // 重置表单
    resetData() {
      this.searchObj = {}
      this.fetchData()
    },
    srclistBig(url) {
      this.srcList.push(url)
    },
    // 选中分类下拉后给表单赋值
    blogSortValue(item) {
      console.log(item)
      this.form.cid = item.getid()
      this.form.blogSortName = item.getcateName()
    },
    // 选中状态下拉
    listByState(item) {
      if (item === '10') {
        blogApi.list(this.page, this.limit, this.title,this.state).then(response => {
          this.list = response.data.records
        })
      } else {
        blogApi.listByState(this.page, this.limit, item).then(response => {
          this.list = response.data.records
        })
      }
    },
    // 关闭dialog时清空数据
    closeDialog() {
      location.reload()
    },
    // 新增表单提交
    submitForm(state) {
      this.$refs.form.validate(valid => {
        if (!valid) {
          console.log('校验出错')
        } else {
          this.form.state = state
          if (this.form.id != null) {
            const tags = []
            this.form.tags.toString().split(',').map(value => {
              tags.push({ id: value })
            })
            Object.assign(this.form.tags, tags)
            blogApi.updateById(this.form).then(response => {
              this.dialogFormVisible = false
              this.$message.success(response.message)
              this.fetchData()
            })
          } else {
            const tags = []
            this.form.tags.toString().split(',').map(value => {
              tags.push({ id: value })
            })
            // console.log(tags)
            Object.assign(this.form.tags, tags)
            // console.log(this.form)
            blogApi.save(this.form).then(response => {
              this.dialogFormVisible = false
              this.$message.success(response.message)
              this.fetchData()
            })
          }
        }
      })
    },
    // handleChange() {
    //   this.hideUpload = this.fileList >= this.limitCount // this.limitCount就是图片数量
    // },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url
      this.dialogVisible = true
    },
    onUploadSuccessImg(response, file) {
      this.onUploadSuccess(response, file, 'blogTitleImg')
    },
    onUploadSuccess(response, file, type) {
      // debugger
      if (response.code !== 0) {
        this.$message.error(response.message)
        return
      }
      // 填充上传文件
      this.form.fileId = response.data.url
      this.fileList = 1
    },
    onUploadRemove(file, fileList) {
      // 删除oss服务器上的内容
      this.url = file.response.data.url
      webConfigApi.delete(this.url).then(response => {
        this.$message.success(response.message)
        this.fileList = 0
        this.hideUpload = false
      })
    },
    // 拼接图片上传路径
    handleBeforeUploadImg: function() {
      return this.BASE_API + this.uploadUrl
    },
    changePageSize(size) {
      this.limit = size
      this.fetchData()
    },
    changeCurrentPage(page) {
      this.page = page
      this.fetchData()
    },
    fetchData() {
      const that = this
      this.loading = true
      // 获取标签和分类的数据
      blogApi.getCategoryLabels().then(response => {
        this.tagList = response.data.tags
        this.genreList = response.data.categories
      })
      blogApi.list(this.page, this.limit, this.title,this.state).then(response => {
        this.list = response.data.records
        this.total = response.data.total
        that.loading = false
      }).catch(error => that.loading = false)
    },
    fetchData1() {
      blogApi.list(1, this.limit, this.title,this.state).then(response => {
        this.list = response.data.records
        this.total = response.data.total
      })
    }

  }
}
</script>
<style>
.hide .el-upload--picture-card {
  display: none;
}
</style>
