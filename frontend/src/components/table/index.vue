<template>
  <div id="tableView" class="app-container">
    <el-table
      v-loading="showLoading"
      :data="dataList"
      element-loading-text="Loading"
      :height="tableHeight"
      border
      fit
      highlight-current-row
    >
      <el-table-column
        v-for="item in tableHeadList"
        :key="item.name"
        align="center"
        :label="item.name"
        :width="item.width"
      >
        <template slot-scope="scope">{{ scope.row[item.value] }}</template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="100" fixed="right" v-if="showEvent">
        <template slot-scope="scope">
          <el-button icon="el-icon-edit" type="primary" @click="eventClick(scope.row)" />
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  props: {
    tableHeadList: {
      type: Array,
      required: true
    },
    dataList: {
      type: Array,
      required: true
    },
    showLoading: {
      type: Boolean,
      required: true
    },
    showEvent: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      list: null,
      tableHeight: null
    }
  },
  mounted() {
    this.tableHeight =
      document.body.clientHeight -
      document.getElementById('tableView').getBoundingClientRect().top -
      80 +
      'px'
  },
  methods: {
    eventClick(row) {
      this.$emit('eventClick', row)
    }
  }
}
</script>
