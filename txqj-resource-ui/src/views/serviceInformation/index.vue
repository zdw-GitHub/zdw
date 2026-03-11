<template>
  <div class="serviceInformation">
    <div class="pageBox">
      <!--业务-->
      <zdzywhPage
        ref="zdzywhPage"
        v-show="stepNum == 0"
        :tableName="tableName"
        :operate="{slot: 'handle',label: '操作',width: '250',}"
        :rightClickMenus="[
          { name: '复制路由', icon: 'el-icon-document-copy',functionName: 'copyRoute'},
          { name: '业务路由', icon: 'el-icon-upload2',functionName: 'openBuzRouter'},
        ]"
        @copyRoute="copyRoute"
        @openBuzRouter="openBuzRouter"
        :tabOperate="true"
      >
        <template v-slot:tabOperate>
          <el-button size="mini" type="primary" @click="openAdd()" v-hasPermi="['system:b:add']">新增</el-button>
          <el-button size="mini" type="danger" @click="deleteSta()" v-hasPermi="['system:b:remove']">删除</el-button>
          <el-button size="mini" @click="handleExport()">导出</el-button>
          <el-button size="mini" type="primary" @click="computeBuzRoute()">计算通道路由</el-button>
          <el-button size="mini" type="danger" @click="computeBuzEmergencyRoute()">计算应急路由</el-button>
          <el-button size="mini" @click="openWindow" icon="el-icon-full-screen">全屏</el-button>
        </template>
        <template v-slot:handle="slot">
          <el-button type="primary" size="mini" @click="toLianLu(slot.scope.row)">通道链路</el-button>
          <el-button size="mini" type="primary" @click="openInfo(true,slot.scope.row)" v-hasPermi="['system:b:update']">编辑</el-button>
          <el-button type="primary" size="mini" @click="openInfo(false,slot.scope.row)" v-hasPermi="['system:b:list']">查看</el-button>
          <el-button type="danger" size="mini" @click="delData(slot.scope)" v-hasPermi="['system:b:remove']">删除</el-button>
        </template>
      </zdzywhPage>
      <!--链路-->
      <channel v-if="stepNum == 1" ref="channel" :tcchannelForm="ywData" @goBack="goBack"></channel>
      <channelFiber v-if="stepNum == 2" ref="channel" :tcchannelForm="ywData" @goBack="goBack"></channelFiber>
      <buzTopo :parRow="routerRow" ref="buzTopo"></buzTopo>
      <buzTopoQx :parRow="routerRow" ref="buzTopoQx"></buzTopoQx>
      <el-dialog
        v-dialogDrag
        :custom-class="infoBox.class"
        :visible.sync="infoBox.show"
        v-if="infoBox.show"
        :width="infoBox.width"
        :modal="false"
        :close-on-click-modal="false">
        <info
          style="width: 100%; height: 80vh;"
          :isEditMode="infoBox.isEditMode"
          :dataObj="infoBox.data"
          :tableName="tableName"
          @switchingModes="switchingModes">
        </info>
      </el-dialog>
    </div>
  </div>
</template>

<script>
  import zdzywhPage from '@/views/commonPage/zdzywh/index';
  import channel from '@/views/serviceInformation/channel';
  import channelFiber from '@/views/serviceInformation/channelFiber';
  import buzTopoQx from "@/views/serviceInformation/buzTopoQx.vue";
  import buzTopo from "@/views/serviceInformation/buzTopo.vue";
  import {ywDelCommitApi,computeBuzEmergencyRoute,serialServiceRouting} from '@/api/serviceInformation'
  import info from '@/views/commonPage/info/index'
  export default {
    name: "index",
    components: {zdzywhPage, channel,channelFiber,buzTopo, buzTopoQx,info},
    data () {
      return {
        tableName:'SG_TCCON_TCBUZ_B',
        // 步骤
        stepNum: 0,
        ywData: {},
        routerRow:{},
        infoBox: {
          isEditMode: false,
          show: false,
          data: {},
          width: '1800px',
          class: 'framePosition',
        },
      }
    },
    mounted() {
      this.$bus.$on('getTableDataBuz', this.getTableDataBuz)
    },
    beforeDestroy() {
      this.$off('getTableDataBuz')
    },
    methods: {
      getTableDataBuz(){
        this.$refs.zdzywhPage.getTableData()
      },
      openBuzRouter (data) {
        this.routerRow = data.rowData
        if(data.rowData.IS_FIBER_BUZ == '是'){
          this.$refs.buzTopoQx.parRow = data.rowData
          this.$refs.buzTopoQx.lightPathBoxShow()
        }else {
          this.$refs.buzTopo.parRow = data.rowData
          this.$refs.buzTopo.lightPathBoxShow()
        }
      },
      copyRoute (row) {
        if(row.rowData.CHANNEL_ROUTE){
          this.$copyText(row.rowData.CHANNEL_ROUTE).then((e) => {
            this.$message({
              type: 'info',
              message: '复制成功'
            })
          }, (e) => {
            this.$message({
              type: 'info',
              message: '复制失败'
            })
          })
        }else {
          this.$message({
            type: 'info',
            message: '路由为空'
          })
        }

      },
      toLianLu(row) {
        this.ywData = row
        if(row.IS_FIBER_BUZ == '否'){
          this.stepNum = 1
        }
        if(row.IS_FIBER_BUZ == '是'){
          this.stepNum = 2
        }
      },
      goBack () {
        this.stepNum = 0
      },
      openInfo(type,row){
        this.infoBox.isEditMode = type
        this.infoBox.show = true
        this.infoBox.width = '1600px'
        this.infoBox.class = ''
        this.infoBox.data = row
      },
      switchingModes(data){
        this.infoBox.width = data ? '1800px' : '500px'
        this.infoBox.class = data ? '' : 'framePosition'
      },
      delData (scope) {
        this.$confirm('此操作将删除该数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          ywDelCommitApi(scope.row.ID).then(response => {
            this.$modal.msgSuccess("操作成功");
            this.$refs.zdzywhPage.getTableData();
          })
        }).catch((e) => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
      },
      openAdd(){
        this.$refs.zdzywhPage.openAdd()
      },
      deleteSta(){
        this.$refs.zdzywhPage.deleteSta()
      },
      openWindow(){
        this.$refs.zdzywhPage.openWindow()
      },
      handleExport(){
        this.$refs.zdzywhPage.handleExport()
      },
      computeBuzEmergencyRoute(){
        if(this.$refs.zdzywhPage.multipleSelection.length===0){
          this.$message.error('请选中数据进行计算');
        } else{
          this.$confirm('是否确认计算应急路由?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            let ids = []
            let datas = this.$refs.zdzywhPage.multipleSelection
            for (let i = 0; i < datas.length; i++) {
              ids.push(datas[i].ID)
            }
            this.$refs.zdzywhPage.$refs.multipleTable.clearSelection()
            this.$refs.zdzywhPage.multipleSelection = []
            if (ids.length===1){
              this.$router.push({
                path:'/businesssource/pathRecommendation',
                query:{buz_id: ids[0]}
              })
              return
            }
            this.$message({ type: 'success', message: '开始计算'})
            let idstr = ids.map(item => "'" + item + "'").join(",")
            computeBuzEmergencyRoute({idstr:idstr}).then(res=>{
              this.$message({ type: 'success', message: '计算完成'})
            })
          }).catch(() => {
            this.$message({
              type: 'info',
              message: '已取消计算'
            })
          })
        }
      },
      computeBuzRoute(){
        if(this.$refs.zdzywhPage.multipleSelection.length===0){
          this.$message.error('请选中数据进行计算');
        } else{
          this.$confirm('是否确认计算通道路由?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            let ids = []
            let datas = this.$refs.zdzywhPage.multipleSelection
            for (let i = 0; i < datas.length; i++) {
              ids.push(datas[i].ID)
            }
            this.$refs.zdzywhPage.$refs.multipleTable.clearSelection()
            this.$refs.zdzywhPage.multipleSelection = []
            this.$message({ type: 'success', message: '开始计算'})
            let idstr = ids.map(item => "'" + item + "'").join(",")
            serialServiceRouting({idstr:idstr}).then(res=>{
              this.$message({ type: 'success', message: '计算完成'})
            })
          }).catch(() => {
            this.$message({
              type: 'info',
              message: '已取消计算'
            })
          })
        }
      },
    },
  }
</script>

<style scoped>
  .pageBox{
    width: 100%;
    height: 100%;
  }
</style>
