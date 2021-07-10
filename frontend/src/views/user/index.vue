<template>
  <div class="user">
    <div style="margin-top: 10px;display: flex;justify-content: center">
      <el-input
        placeholder="默认展示部分用户，可以通过用户名搜索用户..."
        prefix-icon="el-icon-search"
        v-model="keywords" style="width: 400px" size="small">
      </el-input>
      <el-button type="primary" icon="el-icon-search" size="small" style="margin-left: 3px" @click="searchClick">搜索
      </el-button>

    </div>
    <div style="display: flex;justify-content: space-around;flex-wrap: wrap">
      <el-card style="width:330px;margin-top: 10px;" v-for="(user,index) in users" :key="index"
               v-loading="cardloading[index]">
        <div slot="header" style="text-align: left">
          <span>{{user.nickname}}</span>
          <el-button style="float: right; padding: 3px 0;color: #ff0509" type="text" icon="el-icon-delete"
                     @click="deleteUser(user.id)">删除
          </el-button>
        </div>
        <div>
          <div><img :src="user.userface" :alt="user.nickname" style="width: 70px;height: 70px"></div>
          <div style="text-align: left;color:#20a0ff;font-size: 12px;margin-top: 13px">
            <span>用户名:</span><span>{{user.username}}</span>
          </div>
          <div style="text-align: left;color:#20a0ff;font-size: 12px;margin-top: 13px">
            <span>电子邮箱:</span><span>{{user.email}}</span>
          </div>
          <div style="text-align: left;color:#20a0ff;font-size: 12px;margin-top: 13px">
            <span>注册时间:</span><span>{{user.regTime}}</span>
          </div>
          <div
            style="text-align: left;color:#20a0ff;font-size: 12px;margin-top: 13px;display: flex;align-items: center">
            <span>用户状态:</span>
            <el-switch
              v-model="user.enabled"
              active-text="启用"
              active-color="#13ce66"
              @change="enabledChange(user.enabled,user.id,index)"
              inactive-text="禁用" style="font-size: 12px">
            </el-switch>

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
      style="padding: 30px 0; "
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="changePageSize"
      @current-change="changeCurrentPage"
    />
  </div>
</template>
<script>
import {getUserList, deleteRequest, userEnabled} from "@/api/user";

export default{
  methods: {
    // saveRoles(id, index){
    //   var selRoles = this.roles;
    //   if (this.cpRoles.length == selRoles.length) {
    //     for (var i = 0; i < this.cpRoles.length; i++) {
    //       for (var j = 0; j < selRoles.length; j++) {
    //         if (this.cpRoles[i].id == selRoles[j]) {
    //           selRoles.splice(j, 1);
    //           break;
    //         }
    //       }
    //     }
    //     if (selRoles.length == 0) {
    //       return;
    //     }
    //   }
    //   var _this = this;
    //   _this.cardloading.splice(index, 1, true)
    //   putRequest("/admin/user/role", {rids: this.roles, id: id}).then(resp=> {
    //     if (resp.status == 200 && resp.data.status == 'success') {
    //       _this.$message({type: resp.data.status, message: resp.data.msg});
    //       _this.loadOneUserById(id, index);
    //     } else {
    //       _this.cardloading.splice(index, 1, false)
    //       _this.$message({type: 'error', message: '更新失败!'});
    //     }
    //   }, resp=> {
    //     _this.cardloading.splice(index, 1, false)
    //     if (resp.response.status == 403) {
    //       var data = resp.response.data;
    //       _this.$message({type: 'error', message: data});
    //     }
    //   });
    // },
    // showRole(aRoles, id, index){
    //   this.cpRoles = aRoles;
    //   this.roles = [];
    //   this.loadRoles(index);
    //   for (var i = 0; i < aRoles.length; i++) {
    //     this.roles.push(aRoles[i].id);
    //   }
    // },
    deleteUser(id){
      var _this = this;
      this.$confirm('删除该用户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        _this.loading = true;
        deleteRequest(id).then(resp=> {
          if (resp.code == 200) {
            _this.$message({type: 'success', message: '删除成功!'})
            _this.getUserList(this.page,this.limit);
            return;
          }
          _this.loading = false;
          _this.$message({type: 'error', message: '删除失败!'})
        }, resp=> {
          _this.loading = false;
          _this.$message({type: 'error', message: '删除失败!'})
        });
      }).catch(() => {
        _this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    enabledChange(enabled, id, index){
      var _this = this;
      // _this.cardloading.splice(index, 1, true)
      userEnabled(id).then(resp=> {
        if (resp.code != 200) {
          _this.$message({type: 'error', message: '更新失败!'})
          _this.getUserList(this.page,this.limit);
          return;
        }
        // _this.cardloading.splice(index, 1, false)
        _this.$message({type: 'success', message: '更新成功!'})
      }, resp=> {
        _this.$message({type: 'error', message: '更新失败!'})
        _this.getUserList(this.page,this.limit);
      });
    },
    searchClick(){
      this.loading = true;
      console.log(this.keywords)
      getUserList(1,this.limit,this.keywords).then(response=>{

        this.users=response.data.records;
        this.total=response.data.total;
        console.log(response);
      })
    },
    getUserList(page, limit) {
      getUserList(page,limit,'').then(response=>{

        this.users=response.data.records;
        this.total=response.data.total;
        console.log(response);
      })
    },
    changePageSize(size) {
      this.limit = size
      this.getUserList()
    },
    changeCurrentPage(page) {
      this.page = page
      this.getUserList(page,this.limit)
    },
  },
  data(){
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
    }
  },
  created() {
    this.getUserList(this.page,this.limit);
  }
}
</script>
