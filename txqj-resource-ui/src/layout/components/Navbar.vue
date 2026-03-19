<template>
  <div class="navbar">
    <!-- 修改用户信息部分 -->
    <div class="user-wrapper">

      <!-- 用户名称 -->
      <span class="user-name">{{ userName }}</span>
      <!-- 用户头像 -->
      <el-avatar :size="40" :src="avatarUrl" class="user-avatar" fit="cover">
        <!-- 如果头像加载失败，显示用户名首字母 -->
        <img src="@/assets/txqj/user-login.png" alt="用户头像" />
      </el-avatar>


      <!-- 退出按钮 - 放大图标 -->
      <el-button class="right-menu-item logout-btn"
        style="background-color: transparent; color: #303133; border: none; padding: 0 8px;" @click.native="logout">
        <i class="el-icon-switch-button" style="font-size: 18px;"></i>
        <span style="font-size: 14px; margin-left: 4px;">退出</span>
      </el-button>
    </div>
  </div>
</template>

<script>
import { getUserType, getIscUserId, removeIscUserId } from "@/utils/auth";
import { mapGetters } from 'vuex'
import defaultAvatar from '@/assets/txqj/user-login.png'

export default {
  data() {
    return {
      year: '',
      day: '',
      userName: sessionStorage.nickName || '调度员',
      userType: '当前',
      defaultAvatar: defaultAvatar
    }
  },
  computed: {
    ...mapGetters([
      'avatar'  // 从vuex获取头像
    ]),
    avatarUrl() {
      // 如果有上传的头像，使用上传的；否则使用默认头像
      return this.avatar || defaultAvatar
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
      }).catch(() => { });
    },
    getTime: function () {
      var myDate = new Date()
      var weeks = ['日', '一', '二', '三', '四', '五', '六']
      this.day = '星期' + weeks[myDate.getDay()]
      this.year = myDate.getFullYear() + '年' + (myDate.getMonth() + 1) + '月' + myDate.getDate() + '日'
    },
    toSystemManage() {
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
  line-height: 60px;
  font-size: 14px;
  color: #303133;
  text-align: right;

  .user-wrapper {
    display: flex;
    align-items: center;
    justify-content: flex-end;
    height: 100%;
    gap: 8px; // 统一间距

    .user-avatar {
      background-color: #409EFF;
      border: 2px solid #fff;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
      flex-shrink: 0;

      :deep(img) {
        object-fit: cover;
        width: 100%;
        height: 100%;
      }
    }

    .user-name {
      font-size: 14px;
      color: #303133;
      margin-right: 0; // 移除右边距
      white-space: nowrap;
    }

    .logout-btn {
      margin-left: 0; // 移除左边距，用gap控制
      display: inline-flex;
      align-items: center;
      justify-content: center;
      height: 40px;
      border-radius: 20px;
      padding: 0 12px !important;
      transition: all 0.3s;

      &:hover {
        background: linear-gradient(180deg, #Dffff6 0%, #ffffff 100%);
        border-color: transparent !important;
        transform: scale(1.05);
      }

      i {
        font-size: 24px;
        margin-right: 4px;
      }

      span {
        font-size: 14px;
      }
    }
  }

  .el-button {
    margin-left: 0; // 移除默认的左边距

    &:hover {
      background: linear-gradient(180deg, #Dffff6 0%, #ffffff 100%);
      border-color: transparent !important;
    }
  }
}
</style>