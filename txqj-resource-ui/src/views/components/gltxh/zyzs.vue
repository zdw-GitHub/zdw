<template>
  <div>
    <el-menu class="el-menu-vertical-demo" :collapse="isCollapse">
      <i class="el-icon-s-operation" @click="isCollapse=!isCollapse"></i>
      <span style="font-weight: bold" v-show="isCollapse==false">资源展示</span>
      <hr/>
      <div v-show="isCollapse==false">
        <el-card class="box-card"  v-show="selectData.length>0">
          <div slot="header" class="clearfix">
            <span style="float: left;">已选择资源</span>
          </div>
          <div>
            <el-form>
              <el-form-item v-for="item in selectData"  :label="item">
                <div style="float: right;">
                  <el-button icon="el-icon-edit" size="mini" title="编辑" plain></el-button>
                  <el-button icon="el-icon-delete" size="mini" title="删除" plain></el-button>
                </div>
              </el-form-item>
            </el-form>
          </div>
        </el-card>
        <el-card class="box-card"  v-show="addData.length>0">
          <div slot="header" class="clearfix">
            <span style="float: left;">新增资源</span>
          </div>
          <div>
            <el-form>
              <el-form-item v-for="item in addData"  :label="item">
                <div style="float: right;">
                  <el-button icon="el-icon-edit" size="mini" title="编辑" plain></el-button>
                  <el-button icon="el-icon-delete" size="mini" title="删除" plain></el-button>
                </div>
              </el-form-item>
            </el-form>
          </div>
        </el-card>

      </div>
    </el-menu>
  </div>
</template>

<script>
  export default {
    name: "Zyzs",
    data(){
      return {
        isCollapse:true,
        //选择资源存储
        selectData:[],
        //    新增资源存储
        addData:[]
      }
    }
  }
</script>

<style scoped>
  .el-menu-vertical-demo:not(.el-menu--collapse) {
    width: 250px;
    min-height: 650px;
  }
</style>
