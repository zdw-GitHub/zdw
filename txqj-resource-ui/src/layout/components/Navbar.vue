<template>
  <div class="navbar">
    用户 : {{ userName }}
    <!-- <el-button
      class="right-menu-item system-btn"
      style="background-color: transparent; color: #fff; border: none;"
      icon="el-icon-s-tools"
      @click.native="toSystemManage"
    >
      
    </el-button> -->
    <!-- 原有退出按钮 -->
    <el-button
      class="right-menu-item logout-btn"
      style="background-color: transparent; color: #303133; border: none;"
      icon="el-icon-switch-button"
      @click.native="logout"
    >
      退出
    </el-button>
  </div>
</template>

<script>
import { getUserType, getIscUserId, removeIscUserId } from "@/utils/auth";

export default {
  data() {
    return {
      year: '',
      day: '',
      userName: sessionStorage.nickName,
      userType: '当前'
    }
  },
  created() {
    if (getUserType()) {
      this.userType = getUserType()
    }
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async logout() {
      this.$confirm('确定注销并退出系统吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('LogOut').then(() => {
          let iscUserId = getIscUserId()
          if (iscUserId) {
            removeIscUserId()
            window.location = `${process.env.VUE_APP_ISO_LOGOUT}?iscUserId=${iscUserId}&redirect_uri=${process.env.VUE_APP_PROJECT_URL}`
          } else {
            location.href = '/resourceManagement/index';
          }
        })
      }).catch(() => {});
    },
    getTime: function () {
      var myDate = new Date()
      var weeks = ['日', '一', '二', '三', '四', '五', '六']
      this.day = '星期' + weeks[myDate.getDay()]
      this.year = myDate.getFullYear() + '年' + (myDate.getMonth() + 1) + '月' + myDate.getDate() + '日'
    },
    // 新增：模型配置导航方法
    toModelConfig() {
      // 替换为模型配置页面的实际路由路径
      this.$router.push('/model/config')
    },
    // 新增：系统管理导航方法
    toSystemManage() {
      // 替换为系统管理页面的实际路由路径
      this.$router.push('/system/manage')
    }
  },
}
</script>

<style lang="scss" scoped>
.navbar {
  margin: 0 !important;
  width: 100%;
  height: 100%;
  padding-right: 20px;
  line-height: 60px;
  font-size: 14px;
  color: #303133;
  text-align: right;
  // 统一按钮样式：间距、hover效果，覆盖原有背景图（避免与退出按钮冲突）
  .el-button {
    margin-left: 15px; // 调整间距，比原有20px更紧凑，适配多按钮
    &:hover {
      background: linear-gradient(180deg, #Dffff6 0%, #ffffff 100%);
      border-color: transparent !important;
    }
    // 图标与文字间距（可选，优化视觉）
   .el-icon {
      margin-right: 4px;
    }
  }
  // 单独给退出按钮加稍大间距，与功能按钮区分
  .logout-btn {
    margin-left: 20px;
  }
}
</style>