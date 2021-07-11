<template>
  <el-table :data="list" style="width: 100%;margin: 1rem">
    <el-table-column
      prop="title"
      label="文章"
      fixed="left"
      min-width="300"
    >
    </el-table-column>
    <el-table-column
      prop="pageView"
      label="热度"
      fixed="right">
    </el-table-column>
  </el-table>
</template>

<script>
import genreApi from '@/api/core/genre'

export default {
  name: 'TransactionTable',
  data() {
    return {
      list: [],
      page: 1,
      limit: 5,
      field: 'pageView'
    }
  },
  created() {
    this.getlistOrderByDesc(this.page, this.limit, this.field)
  },
  methods: {
    getlistOrderByDesc(page, limit, field) {
      genreApi.listOrderByDesc(page, limit, field).then(resp => {
        this.list = resp.data.records
        // console.log(resp)
        // console.log(this.list)
      })
    }
  }
}
</script>
