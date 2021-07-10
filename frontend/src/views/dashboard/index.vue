<template>
  <div class="dashboard-container">
    <el-row :gutter="40" class="panel-group">
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="handleSetLineChartData('newVisitis')">
          <div class="card-panel-icon-wrapper icon-people">
            <i class="el-icon-user-solid" style="font-size:45px" />
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">
              当前会员数
            </div>
            <count-to
              :start-val="0"
              :end-val="102400"
              :duration="3000"
              class="card-panel-num"
            />
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="handleSetLineChartData('messages')">
          <div class="card-panel-icon-wrapper icon-message">
            <i class="el-icon-tickets" style="font-size:45px" />
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">
              当前博客数
            </div>
            <count-to
              :start-val="0"
              :end-val="aCount"
              :duration="3000"
              class="card-panel-num"
            />
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="handleSetLineChartData('purchases')">
          <div class="card-panel-icon-wrapper icon-money">
            <i class="el-icon-chat-line-square" style="font-size:45px" />
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">
              总标签数
            </div>
            <count-to
              :start-val="0"
              :end-val="tCount"
              :duration="3000"
              class="card-panel-num"
            />
          </div>
        </div>
      </el-col>
      <el-col :xs="12" :sm="12" :lg="6" class="card-panel-col">
        <div class="card-panel" @click="handleSetLineChartData('shoppings')">
          <div class="card-panel-icon-wrapper icon-shopping">
            <i class="el-icon-view" style="font-size:45px" />
          </div>
          <div class="card-panel-description">
            <div class="card-panel-text">
              总访问量
            </div>
            <count-to
              :start-val="0"
              :end-val="pvCount"
              :duration="3000"
              class="card-panel-num"
            />
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import tagApi from '@/api/core/tag'
import genreApi from '@/api/core/genre'
import blogApi from '@/api/core/blog'
import { mapGetters } from 'vuex'
import countTo from 'vue-count-to'
import { list } from '@/api/article'
export default {
  name: 'Dashboard',
  components: { countTo },
  data() {
    return {
      everyCount: '',
      uCount: '', // 用户数
      aCount: '', // 文章数
      tCount: '', // 标签数
      pvCount: '' // 浏览数
    }
  },
  computed: {
    ...mapGetters(['name', 'roles'])
  },
  created() {
    this.fetchData()
    // 测试任意接口带token请求
    list(1, 10, '').then(res => {
      console.log('article list', res)
    }).catch(err => {
      console.error(err)
    })
  },
  methods: {
    fetchData() {
      this.gettCount()
      this.getaCount()
      this.getpvCount()
    },
    gettCount() {
      tagApi.list(1, 1, '').then(response => {
        this.tCount = response.data.total
      })
    },
    getaCount() {
      genreApi.list(1, 1, '').then(response => {
        this.aCount = response.data.total
      })
    },
    getpvCount() {
      this.pvCount = 100
    },
    handleSetLineChartData(type) {
      this.$emit('handleSetLineChartData', type)
    }
  }
}
</script>

<style lang="scss" scoped>
.panel-group {
  margin-top: 18px;

  .card-panel-col {
    margin-bottom: 32px;
  }

  .card-panel {
    height: 108px;
    cursor: pointer;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, 0.05);
    border-color: rgba(0, 0, 0, 0.05);

    &:hover {
      .card-panel-icon-wrapper {
        color: #fff;
      }

      .icon-people {
        background: #40c9c6;
      }

      .icon-message {
        background: #36a3f7;
      }

      .icon-money {
        background: #f4516c;
      }

      .icon-shopping {
        background: #34bfa3;
      }
    }

    .icon-people {
      color: #40c9c6;
    }

    .icon-message {
      color: #36a3f7;
    }

    .icon-money {
      color: #f4516c;
    }

    .icon-shopping {
      color: #34bfa3;
    }

    .card-panel-icon-wrapper {
      float: left;
      margin: 14px 0 0 14px;
      padding: 16px;
      transition: all 0.38s ease-out;
      border-radius: 6px;
    }

    .card-panel-icon {
      float: left;
      font-size: 48px;
    }

    .card-panel-description {
      float: right;
      font-weight: bold;
      margin: 26px;
      margin-left: 0px;

      .card-panel-text {
        line-height: 18px;
        color: rgba(0, 0, 0, 0.45);
        font-size: 16px;
        margin-bottom: 12px;
      }

      .card-panel-num {
        font-size: 20px;
      }
    }
  }
}

@media (max-width: 550px) {
  .card-panel-description {
    display: none;
  }

  .card-panel-icon-wrapper {
    float: none !important;
    width: 100%;
    height: 100%;
    margin: 0 !important;

    .svg-icon {
      display: block;
      margin: 14px auto !important;
      float: none !important;
    }
  }
}
</style>
