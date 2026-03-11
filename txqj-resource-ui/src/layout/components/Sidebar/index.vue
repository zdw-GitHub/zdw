<template>
  <div class="menuBox" style="{ background: linear-gradient(180deg, #Dffff6 0%, #ffffff 100%) }">
    <!-- 优化后的平台标题 - 包含通信图标和文字 -->
    <div class="platform-title">
      <img class="communication-icon" src="@/assets/logo/logo.svg" alt="">
      <!-- <span>通信一体化平台</span> -->
    </div>

    <div class="menuCon">
      <!-- <el-menu :default-active="activeMenu" text-color="#fff" active-text-color="#fff" :unique-opened="true"
        :collapse-transition="false" mode="horizontal" :style="{ lineHeight: '58px' }">
        <sidebar-item class="centre-menu" v-for="(route, index) in sidebarRouters" :key="route.path + index"
          :item="route" :base-path="route.path" />
      </el-menu> -->
    </div>
    <Navbar class="navBox" />
  </div>
</template>

<script>
import { mapGetters, mapState } from "vuex";
import SidebarItem from "./SidebarItem";
import variables from "@/assets/styles/variables.scss";
import Navbar from '../Navbar'

export default {
  data() {
    return {
    };
  },
  components: { SidebarItem, Navbar },
  computed: {
    ...mapState(["settings"]),
    ...mapGetters(["sidebarRouters", "sidebar"]),
    activeMenu() {
      const route = this.$route;
      const { meta, path } = route;
      if (meta.activeMenu) {
        return meta.activeMenu;
      }
      return path;
    },
    showLogo() {
      return this.$store.state.settings.sidebarLogo;
    },
    variables() {
      return variables;
    },
    isCollapse() {
      return !this.sidebar.opened;
    }
  }
};
</script>

<style lang="scss">
.menuBox {
  width: 100%;
  height: 60px;
  background-position: 0 0;
  background-size: 100% 60px;
  background-repeat: no-repeat;
  padding-left: 27%;
  position: relative;
  display: flex;
  align-items: center;

  /* 通信图标与平台标题 */
  .platform-title {
    position: absolute;
    top: 0;
    left: 0;
    height: 60px;
    display: flex;
    align-items: center;
    width: 27%;
    /* 与左侧padding一致 */
    padding-left: 20px;
    z-index: 5;

    .communication-icon {
      width: 30px;
      height: 30px;
      margin-right: 10px;
      /* 如果需要白色，添加滤镜 */
      filter: brightness(0) invert(1);
    }

    span {
      color: white;
      font-size: 22px;
      font-weight: bold;
      font-family: 'Microsoft YaHei', sans-serif;
      text-shadow: 0 0 5px rgba(0, 0, 0, 0.5);
      letter-spacing: 2px;
      position: relative;
      top: 1px;
      /* 微调垂直位置 */
    }
  }

  /* 菜单区域对齐优化 */
  .menuCon {
    width: calc(100% - 250px);
    height: 60px !important;
    overflow: hidden;
    float: left;

    .el-menu {
      display: flex;
      align-items: center;
      height: 100% !important;
      background-color: transparent !important;
      border: none !important;
      overflow: hidden;

      /* 解决菜单项不对齐问题 */
      .centre-menu,
      .el-submenu,
      .el-menu-item {
        height: 58px !important;
        line-height: 58px !important;
        background-color: transparent;
        display: flex;
        align-items: center;
        justify-content: center;
        box-sizing: border-box;
        padding: 0 !important;
        margin: 0 5px !important;
      }

      /* 确保所有菜单项具有相同的垂直对齐 */
      &>* {
        vertical-align: middle;
      }

      .is-active {
        color: #fff !important;
        font-weight: bold;
        border-bottom: 2px solid #fff;
      }

      .el-submenu,
      .el-menu-item {
        margin: 0 5px !important;
        background-color: transparent;
        background-size: 100% 100%;
        background-repeat: no-repeat;

        &:hover,
        &:focus {
          color: #fff !important;
          font-weight: bold;
          border-bottom: 2px solid #fff;
        }
      }

      .el-submenu__title {
        padding: 0 !important;
        height: 100% !important;
        display: flex;
        align-items: center;
      }
    }
  }

  .navBox {
    display: inline-block !important;
    height: 100%;
    width: 250px !important;
  }
}

/* 修复下拉菜单样式 */
.el-menu--horizontal {
  border-radius: 5px;
  overflow: auto;
  max-height: 70vh;

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
        color: #000 !important;

        &:hover,
        &:focus {
          color: #67C23A !important;
          background-color: #E1F3D8;
          border-radius: 5px;
        }
      }

      .is-active {
        color: #67C23A !important;
        background-color: #E1F3D8;
        border-radius: 5px;
      }
    }
  }
}
</style>