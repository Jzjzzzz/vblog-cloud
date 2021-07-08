<template>
  <div class="content">
    <div class="top">
      <div class="queryParamView">
        <el-input prefix-icon="el-icon-edit" v-model="addCouponTypeName" :placeholder="'填写欲添加的优惠券类型名称'" maxlength="4" style="width:250px" />
        <el-select v-model="couponIcon" style="width:120px">
          <el-option
            v-for="(item,index) in iconType"
            :key="index"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
        <el-button type="primary" @click="addCouponType">添加</el-button>
      </div>
    </div>
    <div>
      <tableView :table-head-list="tableHeadList" :data-list="dataList" :show-loading="showLoading" @eventClick="eventClick"/>
    </div>
    <div v-if="showEdit">
    <el-dialog title="编辑优惠券类型" :visible.sync="showEdit" :before-close="handleClose">
      <couponTypeEdit :couponTypeId="couponTypeId" @closeClick="handleClose" />
    </el-dialog>
    </div>
  </div>
</template>

<script>
  import request from '@/utils/request'
  import tableView from '@/components/table'
  import couponTypeEdit from './couponTypeEdit/couponTypeEdit'

  export default {
    components: {
      tableView,
      couponTypeEdit
    },

    data() {
      return {
        showEdit: false,
        couponTypeId: null,
        addCouponTypeName: null,
        showLoading: false,
        couponIcon: null,
        tableHeadList: [{
          name: '优惠券类型ID',
          value: 'couponTypeId'
        },{
          name: '优惠券类型显示名称',
          value: 'couponName'
        }],
        iconType: [
          {
            value: '/images/washCar.png',
            label: '洗车图标'
          },
          {
            value: '/images/fineWashCar.png',
            label: '精洗图标'
          },
          {
            value: '/images/maintainCar.png',
            label: '保养图标'
          },
          {
            value: '/images/membraneCar.png',
            label: '车膜图标'
          },
          {
            value: '/images/paintCar.png',
            label: '喷漆图标'
          },
          {
            value: '/images/waxCar.png',
            label: '打蜡图标'
          }
        ],
        dataList: []  //数据列表
      }
    },

    mounted() {
      this.queryList()  //查询列表
    },

    methods: {
      /**
       * 查询列表
       */
      queryList:function() { 
        const _this = this
        this.showLoading = true
        request({
          url: '/management/couponType/getAllCouponType',
          method: 'post',
        }).then(response => {
          _this.showLoading = false
          _this.dataList = response.data
        }, error => {
          _this.showLoading = false
        })
      },
      
      /**
       * @param {Object} row 点击行的数据
       */
      eventClick(row) {
        this.couponTypeId = row.couponTypeId
        this.showEdit=true
      },

      /**
       * 详情页关闭
       * @param {Object} done
       */
      handleClose(isRefresh) {
        this.showEdit = false
        if (isRefresh) {
          this.queryList()
        }
      },

      /**
       * 添加服务类型
       */
      addCouponType: function() {
        this.addCouponTypeName = this.addCouponTypeName.replace(/(^\s*)|(\s*$)/g, "")
        this.couponIcon = this.couponIcon.replace(/(^\s*)|(\s*$)/g, "")
        if (!this.addCouponTypeName || !this.couponIcon) {
          return
        }
        const _this = this
        this.$confirm('是否添加优惠券类型：【' + this.addCouponTypeName + '】?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          request({
            url: '/management/couponType/addCouponType',
            method: 'post',
            params: {couponName: this.addCouponTypeName, couponIcon: this.couponIcon}
          }).then(response => {
            _this.addCouponTypeName = ""
            _this.couponIcon = ""
            _this.$message({
              message: '添加成功',
              type: 'success',
              duration: 3 * 1000
            })
            _this.queryList()
          }, error => {
            Message({
              message: '添加失败',
              type: 'error',
              duration: 5 * 1000
            })
          })
        }).catch(() => {
          
        });
      },
    }
  }
</script>
