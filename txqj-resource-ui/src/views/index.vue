<template>
  <div class="dashboard-container">
    <div class="main-content">
      <div class="group-card" v-for="(group, groupIndex) in router" :key="groupIndex">
        <div class="group-header">
          <div class="header-decoration"></div>
          <h2 class="group-title">{{ group.name }}</h2>
        </div>
        
        <div class="items-grid">
          <div 
            class="function-item" 
            v-for="(item, itemIndex) in group.children" 
            :key="itemIndex"
            @click="toPage(group.url, item)"
          >
            <div class="item-icon">
              <img 
                :src="publicPath + 'img/homeImg/' + item.name + '.png'" 
                :alt="item.name"
              >
            </div>
            <div class="item-info">
              <p class="item-name">{{ item.name }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
// import { getRouters } from '@/api/menu'
export default {
  name: "Index",
  data() {
    return {
      router: [],
      publicPath: process.env.BASE_URL
    };
  },
  mounted() {
    this.getRouters()
  },
  methods: {
    toPage(itemUrl, sitem) {
      this.$router.push({
        path: itemUrl + sitem.url,
        query: sitem.query ? JSON.parse(sitem.query) : {}
      })
    },
    getRouters() {
      let arr = this.$store.state.permission.topbarRouters
      for (let k = 0; k < arr.length; k++) {
        if (!arr[k].children || arr[k].hidden) continue
        this.router.push({
          name: arr[k].meta.title,
          url: arr[k].path,
          children: []
        })
        let sArr = arr[k].children
        for (let v = 0; v < sArr.length; v++) {
          this.router[this.router.length - 1].children.push({
            name: sArr[v].meta.title,
            url: '/' + sArr[v].path,
            query: sArr[v].query,
          })
        }
      }
    }
  },
};
</script>

<style scoped lang="scss">
.dashboard-container {
  padding: 20px;
  min-height: 120vh;
  position: relative;

  &::before {
    content: '';
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-image: radial-gradient(rgba(160, 174, 192, 0.05) 1px, transparent 1px);
    background-size: 20px 20px;
    z-index: 0;
    pointer-events: none; 
  } 
}

.main-content {
  max-width: 100%;
  margin: 0 auto;
  position: relative;
  z-index: 1; 
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.group-card {

  backdrop-filter: blur(8px);
  border-radius: 10px;
  box-shadow: 0 4px 20px rgba(148, 163, 184, 0.12);
  margin-bottom: 30px;
  overflow: hidden;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  width: 100%;
  
  &:hover {
    transform: translateY(-3px);
    box-shadow: 0 8px 28px rgba(64, 158, 255, 0.18);
    background-color: rgba(255, 255, 255, 0.95);
  }
}


@media (max-width: 768px) {
  .main-content {
    grid-template-columns: 1fr;
  }
}

.group-header {
  display: flex;
  align-items: center;
  // background: linear-gradient(90deg, #409EFF 0%, #5ba6ff 50%, #7bb8ff 100%);
  padding: 18px 24px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.2); 
}

.header-decoration {
  width: 4px;
  height: 20px;
  background-color: #686565;
  border-radius: 2px;
  margin-right: 12px;
  box-shadow: 0 0 8px rgba(255, 255, 255, 0.5); 
}

.group-title {
  font-size: 17px;
  color: #111010;
  margin: 0;
  font-weight: 500;
  text-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  letter-spacing: 0.3px;
}

/* 网格布局：保持原有适配逻辑 */
.items-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 18px;
  padding: 24px;
}

/* 功能项：优化 hover 反馈，与整体风格统一 */
.function-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 22px 18px;

  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid rgb(20, 20, 20);
  
  &:hover {
    background-color: #646464;
    border-color: #c6e2ff;
    box-shadow: 0 4px 12px rgba(64, 158, 255, 0.15);
    transform: translateY(-3px);
  }
}

.item-icon {
  width: 52px;
  height: 52px;
  margin-bottom: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #ffffff;
  border-radius: 50%;
  box-shadow: 0 3px 10px rgba(148, 163, 184, 0.1);
  transition: box-shadow 0.3s ease;
  
  &:hover {
    box-shadow: 0 4px 15px rgba(64, 158, 255, 0.2);
  }
  
  img {
    max-width: 38px;
    max-height: 38px;
    object-fit: contain;
  }
}

.item-info {
  text-align: center;
  width: 100%;
}

.item-name {
  font-size: 14px;
  color: #05367a; /* 加深文字颜色，提升可读性 */
  margin: 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  padding: 0 8px;
  letter-spacing: 0.2px;
}

/* 响应式调整：优化小屏幕体验 */
@media (max-width: 768px) {
  .dashboard-container {
    padding: 15px;
  }
  
  .items-grid {
    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
    gap: 12px;
    padding: 18px;
  }
  
  .function-item {
    padding: 18px 12px;
  }
  
  .item-icon {
    width: 46px;
    height: 46px;
    
    img {
      max-width: 32px;
      max-height: 32px;
    }
  }
  
  .group-header {
    padding: 15px 20px;
  }
  
  .group-title {
    font-size: 16px;
  }
}
</style>