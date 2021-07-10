<template>
  <div class="app-container">
    <!--查询表单-->
    <el-form :inline="true" class="demo-form-inline">
      <el-form-item label="日志管理">
      </el-form-item>
    </el-form>

    <!-- 表格 -->
    <el-table
      :data="list"
      border
      stripe
    >
      <el-table-column type="index" width="50" align="center" />
      <el-table-column prop="hostip" label="请求ip" align="center" />
      <el-table-column prop="requestmethod" label="请求的url" align="center" />
      <el-table-column prop="date" label="创建时间" align="center" />
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
  </div>
</template>
<script>
import journalApi from '@/api/core/journal'
export default {
  // 定义数据模型
  data() {
    return {
      list: [], // 数据列表
      total: 0, // 数据库中的总记录数
      page: 1, // 默认页码
      limit: 5, // 每页记录数
      tagName: '', // 查询条件
      formLabelWidth: '120px'
    }
  },
  // 页面渲染成功后获取数据
  created() {
    this.fetchData()
  },
  // 定义方法
  methods: {
    changePageSize(size) {
      this.limit = size
      this.fetchData()
    },
    changeCurrentPage(page) {
      this.page = page
      this.fetchData()
    },
    fetchData() {
      // 调用api
      journalApi.list(this.page, this.limit).then(response => {
        this.list = response.data.records
        this.total = response.data.total
      })
    }
  }
}
</script>
