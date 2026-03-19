<template>
  <div class="menuBox">
    <div class="platform-brand" @click="goHome">
      <img class="brand-bg" src="@/assets/txqj/txqj-bg.png" alt="">
      <img class="grid-icon" src="@/assets/txqj/txqj-logo.png" alt="">
      <img class="system-name" src="@/assets/txqj/txqj.png" alt="全时空通信全景平台">
    </div>

    <!-- 中间搜索框 -->
    <div class="search-wrapper">
      <el-input placeholder="搜光缆、设备" prefix-icon="el-icon-search" v-model="searchKeyword" size="medium" clearable
        class="search-input" @keyup.enter="handleSearch">
        <template #append>
          <el-button icon="el-icon-search" @click="handleSearch"></el-button>
        </template>
      </el-input>
    </div>

    <!-- 中间菜单区域 -->
    <div class="menuCon">
      <el-menu :default-active="activeMenu" text-color="#303133" active-text-color="#409EFF" :unique-opened="true"
        :collapse-transition="false" mode="horizontal" :style="{ lineHeight: '58px' }" @select="handleMenuSelect">
        <el-menu-item index="home">首页</el-menu-item>
        <el-menu-item index="comm-map">通信一张图</el-menu-item>
        <el-menu-item index="alert">告警监视</el-menu-item>
        <el-menu-item index="resource">资源管理</el-menu-item>
        <el-menu-item index="toolbox">工具箱</el-menu-item>
        <el-menu-item index="ai-analysis">智能分析</el-menu-item>
      </el-menu>
    </div>

    <!-- 右侧用户信息区域 -->
    <Navbar class="navBox" />
  </div>
</template>

<script>
import { mapGetters, mapState } from "vuex";
import variables from "@/assets/styles/variables.scss";
import Navbar from '../Navbar'

export default {
  data() {
    return {
      searchKeyword: ''
    };
  },
  components: { Navbar },
  computed: {
    ...mapState(["settings"]),
    ...mapGetters(["sidebarRouters", "sidebar"]),
    activeMenu() {
      const route = this.$route;
      const { meta, path } = route;
      if (meta.activeMenu) {
        return meta.activeMenu;
      }
      // 修正：首页路径匹配resourceManagement/index
      if (path.includes('/resourceManagement/index')) return 'home'
      if (path.includes('/comm-map')) return 'comm-map'
      if (path.includes('/alert')) return 'alert'
      if (path.includes('/roomsource')) return 'resource' // 匹配资源管理实际路径
      if (path.includes('/toolbox')) return 'toolbox'
      if (path.includes('/ai')) return 'ai-analysis'
      return path;
    },
    variables() {
      return variables;
    },
    isCollapse() {
      return !this.sidebar.opened;
    }
  },
  methods: {

    handleSearch() {
      if (!this.searchKeyword.trim()) {
        this.$message.warning('请输入搜索内容');
        return;
      }
      // 跳转到资源管理页面，携带tableName和搜索关键词
      this.$router.push({
        path: '/roomsource/zdwh/common/SG_TCCON_TCSITE_B', // 匹配当前可用路径
        query: {
          tableName: 'SG_TCCON_TCSITE_B',
          expandNode: true,
          searchKeyword: this.searchKeyword.trim()
        }
      });
      // 清空搜索框
      this.searchKeyword = '';
    },
    goHome() {
      this.$router.push('/resourceManagement/index')
    },
    handleMenuSelect(key) {
      const routes = {
        'home': '/index',
        'comm-map': '/communication/map',
        'alert': '/alert/monitor',
        'resource': '/roomsource/zdwh/common/SG_TCCON_TCSITE_B?tableName=SG_TCCON_TCSITE_B&refresh=true',
        'toolbox': '/toolbox',
        'ai-analysis': '/ai/analysis'
      }
      if (routes[key]) {
        this.$router.push(routes[key])
      }
    }
  }
};
</script>

<style lang="scss">
.menuBox {
  width: 100%;
  height: 60px;
  // 整体背景：Mask group.png + 绿色渐变叠加
  background:
    linear-gradient(180deg, rgba(223, 255, 246, 0.9) 0%, rgba(255, 255, 255, 0.9) 100%),
    url('~@/assets/txqj/Mask group.png') no-repeat center center / cover;
  position: relative;
  display: flex;
  align-items: center;
  padding: 0;

  /* 左侧品牌区域 - 使用txqj-bg.png作为局部背景 */
  .platform-brand {
    position: relative;
    width: 300px;
    height: 60px;
    flex-shrink: 0;

    // 局部背景图 - txqj-bg.png
    .brand-bg {
      position: absolute;
      top: 0;
      left: 0;
      width: 160%;
      height: 90%;
      opacity: 0.3; // 半透明，让整体背景也能透出来
      object-fit: cover;
      z-index: 1;
    }

    .grid-icon {
      position: absolute;
      top: 50%;
      left: 20px;
      transform: translateY(-50%);
      width: 40px;
      height: 40px;
      z-index: 2;
      object-fit: contain;
    }

    .system-name {
      position: absolute;
      top: 50%;
      left: 70px;
      width: 80%;
      height: 80%;
      transform: translateY(-50%);
      height: 30px;
      z-index: 2;
      object-fit: contain;
    }
  }

  /* 搜索框样式 - 调整左边距 */
  .search-wrapper {
    width: 350px;
    flex-shrink: 0;
    margin-left: 180px; // 减小左边距，让布局更紧凑

    .search-input {
      :deep(.el-input__inner) {
        border-radius: 20px;
        border: 1px solid #dcdfe6;
        background: #fff;
        color: #303133;
        height: 36px;
        line-height: 36px;
        transition: all 0.3s;

        &::placeholder {
          color: #909399;
        }

        &:focus {
          border-color: #409EFF;
          box-shadow: 0 0 8px rgba(64, 158, 255, 0.3);
        }
      }

      :deep(.el-input__prefix) {
        display: flex;
        align-items: center;

        .el-input__icon {
          line-height: 36px;
          color: #909399;
        }
      }
    }
  }

  /* 菜单区域 */
  .menuCon {
    flex: 1;
    min-width: 0;
    height: 60px !important;
    overflow: hidden;
    margin-left: 20px;

    .el-menu {
      display: flex;
      align-items: center;
      height: 100% !important;
      background-color: transparent !important;
      border: none !important;
      overflow: hidden;

      .el-menu-item {
        height: 58px !important;
        line-height: 58px !important;
        background-color: transparent;
        display: flex;
        align-items: center;
        justify-content: center;
        box-sizing: border-box;
        padding: 0 15px !important;
        margin: 0 5px !important;
        font-size: 15px;
        color: #303133 !important;
        border-bottom: none !important;
        transition: all 0.3s;

        &:hover {
          color: #409EFF !important;
          background: linear-gradient(180deg, #Dffff6 0%, #ffffff 100%) !important;
        }

        &.is-active {
          color: #409EFF !important;
          font-weight: 500;
          border-bottom: 3px solid #409EFF !important;
          background: transparent !important;
        }
      }

      &>* {
        vertical-align: middle;
      }
    }
  }

  .navBox {
    flex-shrink: 0;
    width: auto !important;
    min-width: 100px;
    height: 100%;
    margin-right: 20px;
  }
}

/* 修复下拉菜单样式 */
.el-menu--horizontal {
  border-radius: 5px;
  overflow: auto;
  max-height: 70vh;
  border: 1px solid #e4e7ed !important;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

  .el-menu {
    width: 140px !important;
    margin: 0 !important;
    padding: 10px 15px !important;

    .nest-menu {
      width: 100%;
      height: 100%;
      display: block !important;

      .el-menu-item {
        padding: 0 !important;
        color: #303133 !important;

        &:hover,
        &:focus {
          color: #409EFF !important;
          background-color: #ecf5ff;
          border-radius: 5px;
        }
      }

      .is-active {
        color: #409EFF !important;
        background-color: #ecf5ff;
        border-radius: 5px;
      }
    }
  }
}
</style>