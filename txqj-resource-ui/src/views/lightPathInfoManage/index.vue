<template>
  <div>
    <zdzywhPage
      ref="zdzywhPage"
      :tableName="tableName"
      :operate="{
            slot: 'handle',
            label: '操作',
            width: '200',
        }"
      :tabOperate="true"
      :rightClickMenus="[
        { name: '复制路由', icon: 'el-icon-document-copy',functionName: 'copyRoute'},
        { name: '路由维护', icon: 'el-icon-upload2',functionName: 'setLightPathBoxShow'},
        { name: '配线连接', icon: 'el-icon-upload',functionName: 'setPhyLinkBoxShow'},
        { name: '承载业务', icon: 'el-icon-paperclip',functionName: 'getCarryingBusiness'},
        { name: '承载业务导出', icon: 'el-icon-printer',functionName: 'carryingBusinessExport'},
        { name: '承载通道链路', icon: 'el-icon-link',functionName: 'getCarryingChannelLink'},
      ]"
      @copyRoute="copyRoute"
      @setLightPathBoxShow="setLightPathBoxShow"
      @setPhyLinkBoxShow="setPhyLinkBoxShow"
      @getCarryingBusiness="getCarryingBusiness"
      @getSearchCriteria="getSearchCriteria"
      @carryingBusinessExport="carryingBusinessExport"
      @getCarryingChannelLink="getCarryingChannelLink"
    >
      <template v-slot:tabOperate>
        <el-button size="mini" type="primary" @click="openAdd()" v-hasPermi="['system:b:add']">新增</el-button>
        <el-button size="mini" type="danger" @click="deleteSta()" v-hasPermi="['system:b:remove']">删除</el-button>
        <el-button size="mini" @click="handleExport()">导出</el-button>
        <el-button size="mini" @click="lightPathListExport()">光路列表导出</el-button>
        <el-button size="mini" type="primary" @click="computeOpticalroadRoute()">计算光路路由</el-button>
        <el-button size="mini" type="danger" @click="computeOpticalroadEmergencyRoute()">计算应急路由</el-button>
        <el-button size="mini" @click="openWindow" icon="el-icon-full-screen">全屏</el-button>
      </template>
      <template v-slot:handle="slot">
        <el-button size="mini" type="primary" @click="openOpticalroadIInfo(true,slot.scope.row)" v-hasPermi="['system:b:update']">编辑</el-button>
        <el-button type="primary" size="mini" @click="openOpticalroadIInfo(false,slot.scope.row)" v-hasPermi="['system:b:list']">查看</el-button>
        <el-button size="mini" type="danger" @click="delData(slot.scope.row)" v-hasPermi="['system:b:remove']">删除</el-button>
        <el-dropdown style="margin-left: 5px" trigger="click" >
          <span class="el-dropdown-link"><i class="el-icon-arrow-down el-icon--right"></i></span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="toFiber(slot.scope.row, '/zdwh/sgtccon/fiberLine')">查看对应光缆纤芯</el-dropdown-item>
            <el-dropdown-item @click.native="toFiber(slot.scope.row, '/zdwh/sgtccon/fiberSegLine')">查看对应光缆段纤芯</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </template>
    </zdzywhPage>
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
    <!--路由维护-->
    <el-dialog
      v-dialogDrag
      custom-class="curdBox"
      title="路由维护"
      :modal="false"
      :close-on-click-modal="false"
      width="90%"
      v-if="setLightPathBox.show"
      :visible.sync="setLightPathBox.show"
    >
      <setLightPath
        ref="setLightPath"
        style="width: 100%; height: 70vh"
        :parId="setLightPathBox.id"
      ></setLightPath>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="commitRouter" :loading="setLightPathBox.commitLoading">确 定</el-button>
        <el-button @click="setLightPathBox.show=false">取 消</el-button>
      </div>
    </el-dialog>
    <!--承载业务-->
    <el-dialog
      v-dialogDrag
      custom-class="curdBox"
      :title="carryingBusiness.title + '   承载业务'"
      v-if="carryingBusiness.show"
      :visible.sync="carryingBusiness.show"
      :modal="false"
      :close-on-click-modal="false"
      width="90%">
      <carryingBusiness style="width: 100%; height: 70vh;" :id="carryingBusiness.id" :tableName="carryingBusiness.tableName">
      </carryingBusiness>
      <div slot="footer" class="dialog-footer">
        <el-button @click="carryingBusiness.show = false">关 闭</el-button>
      </div>
    </el-dialog>
    <!--承载通道链路-->
    <el-dialog
      v-dialogDrag
      custom-class="curdBox"
      :title="carryingChannelLink.title + '   承载通道链路'"
      v-if="carryingChannelLink.show"
      :visible.sync="carryingChannelLink.show"
      :modal="false"
      :close-on-click-modal="false"
      width="90%">
      <carryingChannelLink style="width: 100%; height: 70vh;" :id="carryingChannelLink.id" :tableName="tableName" >
      </carryingChannelLink>
      <div slot="footer" class="dialog-footer">
        <el-button @click="carryingChannelLink.show = false">关 闭</el-button>
      </div>
    </el-dialog>
    <el-dialog
      v-dialogDrag
      custom-class="curdBox"
      title="路由维护"
      :modal="false"
      :close-on-click-modal="false"
      width="90%"
      v-if="setPhyLinkBox.show"
      :visible.sync="setPhyLinkBox.show"
    >
      <setPhyLink
        style="width: 100%; height: 70vh"
        :routerId="setPhyLinkBox.id"
        ref="setPhyLink"
      ></setPhyLink>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="commitPhyLink" :loading="setPhyLinkBox.commitLoading">确 定</el-button>
        <el-button @click="setPhyLinkBox.show=false">取 消</el-button>
      </div>
    </el-dialog>

  </div>

</template>

<script>
  import zdzywhPage from '@/views/commonPage/zdzywh/index';

  import {
    delDataApi,
    commitRouterApi,
    exportOpticalPath,
    computeOpticalroadEmergencyRoute,
    serialOpticalRouting
  } from "@/api/lightPathInfoManage/index";
  import{export_excel_file,fileDownload} from "@/api/zdwh/zdzywh";
  import carryingBusiness from '@/views/serviceInformation/carryingBusiness';
  import setPhyLink from '@/views/lightPathInfoManage/setPhyLink';
  import {carryingBusinessExport} from '@/api/serviceInformation/carryingBusiness'
  import {saveTheOpticalWiringConnectionRelationship} from "@/api/lightPathInfoManage/setPhyLink";
  import carryingChannelLink from '@/views/serviceInformation/carryingChannelLink.vue'
  import setLightPath from "@/views/lightPathInfoManage/setLightPath";
  import info from '@/views/commonPage/info/index'
    export default {
    name: "lightPathInfoManage",
    components: {info, zdzywhPage,setLightPath,carryingBusiness,setPhyLink,carryingChannelLink},
    watch: {
      //解决操作栏固定后表格错位问题
      'tableData': {
        handler() {
          this.$nextTick(() => {
            this.$refs.multipleTable.doLayout();
          })
        },
        deep: true
      },
    },
    data(){
      return{
        tableName:'SG_TCCON_TCOPTICALROAD_B',
        rateOption: [],
        selectName: '',
        optionsA:{},
        optionsZ:{},
        optionsALoading: true,
        optionsZLoading: true,
        searchOptionStr: '',
        //  查看光缆弹出框遮盖层
        stationDrawer:false,
        editStationDrawer: false,
        newStationDrawer: false,
        ifFirstEdit: true,
        ifFirstSee: true,
        //  单条数据
        editForm:{},
        addForm: {},
        setLightPathBox: {
          id: '',
          show: false,
          commitLoading:false,
        },

        carryingBusiness:{
          title: '',
          id: '',
          show: false,
          tableName:'',
        },

        //光缆承载通道链路
        carryingChannelLink:{
          title: '',
          id: '',
          show: false,
        },
        setPhyLinkBox: {
          id: '',
          show: false,
          commitLoading:false,
        },
        infoBox: {
          isEditMode: false,
          show: false,
          data: {},
          width: '1800px',
          class: 'framePosition',
        },
      }
    },
    activated () {
    },
      mounted() {
        this.$bus.$on('getTableDataGL', this.getTableDataGL)
      },
      beforeDestroy() {
        this.$off('getTableDataGL')
      },
    methods:{
      getTableDataGL(){
        this.$refs.zdzywhPage.getTableData()
      },
      getCarryingBusiness(data){
        this.carryingBusiness.title = data.rowData.SNNAME || data.rowData.NAME
        this.carryingBusiness.id = data.rowData.ID
        this.carryingBusiness.show = true
        this.carryingBusiness.tableName = this.tableName
      },
      openAdd () {
        this.$refs.zdzywhPage.openAdd()
      },
      //编辑
      openEdit(scope){
        this.$refs.zdzywhPage.openEdit(scope)
      },
      //查看
      searchStation(scope){
        this.$refs.zdzywhPage.searchStation(scope)
      },
      handleExport(){
        this.$refs.zdzywhPage.handleExport()
      },
      lightPathListExport(){
        this.$confirm('是否导出数据?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$refs.zdzywhPage.otherHandleExport()
        })
      },
      getSearchCriteria(params){
        this.$message({
          type: 'success',
          message: '正在导出...'
        })
        exportOpticalPath(params).then(res =>{
          let lastInde = res.msg.lastIndexOf("/")+1;
          let fileName = res.msg.substring(lastInde);
          fileDownload({filename:res.msg}).then(res =>{
            export_excel_file(res, fileName);
            this.$message({
              type: 'success',
              message: '导出完成'
            })
          })
        })
      },
      getCarryingChannelLink(data){
        this.carryingChannelLink.title = data.rowData.SNNAME || data.rowData.NAME
        this.carryingChannelLink.id = data.rowData.ID
        this.carryingChannelLink.show = true
      },
      carryingBusinessExport(data){
        this.$message({
          type: 'success',
          message: '正在导出...'
        })
        carryingBusinessExport({},{id:data.rowData.ID,resType:'光路'}).then(res =>{
          let lastInde = res.msg.lastIndexOf("/")+1;
          let fileName = res.msg.substring(lastInde);
          fileDownload({filename:res.msg}).then(res =>{
            export_excel_file(res, fileName);
            this.$message({
              type: 'success',
              message: '导出完成'
            })
          })
        })
      },
      openWindow(){
        this.$refs.zdzywhPage.openWindow()
      },
      delData(row){
        this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          delDataApi(row.ID).then(res => {
            this.$refs.zdzywhPage.getTableData()
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
      },
      deleteSta() {
        if(this.$refs.zdzywhPage.multipleSelection.length===0){
          this.$message.error('请选中数据进行删除');
        } else{
          this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            let ids = []
            let datas = this.$refs.zdzywhPage.multipleSelection
            for (let i = 0; i < datas.length; i++) {
              ids.push(datas[i].ID)
            }
            delDataApi(ids).then(res => {
              this.$refs.zdzywhPage.getTableData()
            })
          }).catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除'
            })
          })
        }
      },
      computeOpticalroadEmergencyRoute(){
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
                query:{gl_id: ids[0]}
              })
              return
            }
            this.$message({ type: 'success', message: '开始计算'})
            let idstr = ids.map(item => "'" + item + "'").join(",")
            computeOpticalroadEmergencyRoute({idstr:idstr}).then(res=>{
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
      computeOpticalroadRoute(){
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
            this.$message({ type: 'success', message: '开始计算'})
            let idstr = ids.map(item => "'" + item + "'").join(",")
            serialOpticalRouting({idstr:idstr}).then(res=>{
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

      setLightPathBoxShow (row) {
        this.setLightPathBox = {
          id: row.rowData.ID,
          show: true,
          commitLoading: false,
        }
      },
      setPhyLinkBoxShow(row){
        this.setPhyLinkBox = {
          id: row.rowData.ID,
          show: true,
          commitLoading: false,
        }
      },
      commitPhyLink () {
        let data = this.$refs.setPhyLink.commitPhyLink()
        if(data) {
          this.setPhyLinkBox.commitLoading=true
          saveTheOpticalWiringConnectionRelationship(data,{routerId:this.setPhyLinkBox.id}).then(res =>{
            this.setPhyLinkBox = {
              id: '',
              show: false,
              commitLoading: false,
            }
            this.$modal.msgSuccess("操作成功");
          }).catch(e =>{
            this.setPhyLinkBox.commitLoading=false
          })
        }
      },
      copyRoute (row) {
        if(row.rowData.OPTICAL_ROUTING){
          this.$copyText(row.rowData.OPTICAL_ROUTING).then((e) => {
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
      commitRouter () {
        let data = this.$refs.setLightPath.tableCommit()
        if(data) {
          this.setLightPathBox.commitLoading=true
          commitRouterApi({
            routerId: data.routerId,
            tableData: data.tableData,
            isJumpFiber: data.isJumpFiber,
            llId: data.llId,
          }).then(res => {
            this.setLightPathBox = {
              id: '',
              show: false,
              commitLoading: false,
            }
            this.$modal.msgSuccess(res.msg);
          }).catch(e =>{
            this.setLightPathBox.commitLoading = false
          })
        }
      },
      toFiber (row, url) {
        this.$router.push(
          {
            path: url,
            query:{
              search: JSON.stringify({OPTICALROAD_ID: row.ID}),
              refresh: true
            }
          }
        )
      },
      openOpticalroadIInfo(type,row){
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
    }
  }
</script>

<style>
  .el-message{
    z-index: 1000;
  }
</style>
