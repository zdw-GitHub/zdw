<template>
  <div>
    <zdzywhPage
      ref="zdzywhPage"
      :tableName="tableName"
      :operate="{slot: 'handle',label: '操作',width: '200'}"
      :rightClickMenus="[
        { name: '承载光路', icon: 'el-icon-upload2',functionName: 'getCarryingOpticalPath'},
        { name: '承载光路导出', icon: 'el-icon-printer',functionName: 'carryingOpticalPathExport'},
        { name: '承载业务', icon: 'el-icon-paperclip',functionName: 'getCarryingBusiness'},
        { name: '承载业务导出', icon: 'el-icon-printer',functionName: 'carryingBusinessExport'},
      ]"
      @getCarryingOpticalPath="getCarryingOpticalPath"
      @getCarryingBusiness="getCarryingBusiness"
      @carryingOpticalPathExport="carryingOpticalPathExport"
      @carryingBusinessExport="carryingBusinessExport"
    >
      <template v-slot:handle="slot">
        <el-button size="mini" type="primary" @click="openInfo(true,slot.scope.row)" v-hasPermi="['system:b:update']">编辑</el-button>
        <el-button type="primary" size="mini" @click="openInfo(false,slot.scope.row)" v-hasPermi="['system:b:list']">查看</el-button>
        <el-button size="mini" type="danger" @click="delData(slot.scope.row)" v-hasPermi="['system:b:remove']">删除</el-button>
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
    <!--承载光路-->
    <el-dialog
      v-dialogDrag
      custom-class="curdBox"
      :title="carryingOpticalPath.title + '   承载光路'"
      v-if="carryingOpticalPath.show"
      :visible.sync="carryingOpticalPath.show"
      :modal="false"
      :close-on-click-modal="false"
      width="90%">
      <carryingOpticalPath style="width: 100%; height: 70vh;" :id="carryingOpticalPath.id" :tableName="carryingOpticalPath.tableName">
      </carryingOpticalPath>
      <div slot="footer" class="dialog-footer">
        <el-button @click="carryingOpticalPath.show = false">关 闭</el-button>
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
  </div>
</template>

<script>
  import zdzywhPage from '@/views/commonPage/zdzywh/index';
  import carryingOpticalPath from '@/views/lightPathInfoManage/carryingOpticalPath'
  import carryingBusiness from '@/views/serviceInformation/carryingBusiness'
  import {carryingOpticalPathExport} from '@/api/lightPathInfoManage/carryingOpticalPath'
  import {carryingBusinessExport} from '@/api/serviceInformation/carryingBusiness'
  import{export_excel_file,fileDownload} from "@/api/zdwh/zdzywh";
  import info from '@/views/commonPage/info/index'
  export default {
    name: "index",
    components: {zdzywhPage,carryingOpticalPath,carryingBusiness,info},
    data(){
      return {
        tableName:'SG_TCDEV_TCNESDH_B',
        //SDH承载光路
        carryingOpticalPath:{
          title: '',
          id: '',
          show: false,
        },
        //SDH承载业务
        carryingBusiness:{
          title: '',
          id: '',
          show: false,
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
    watch: {
    },
    methods:{
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
        this.$refs.zdzywhPage.delData(scope)
      },
      getCarryingOpticalPath(data){
        this.carryingOpticalPath.title = data.rowData.SNNAME || data.rowData.NAME
        this.carryingOpticalPath.id = data.rowData.ID
        this.carryingOpticalPath.show = true
        this.carryingOpticalPath.tableName = this.tableName
      },
      getCarryingBusiness(data){
        this.carryingBusiness.title = data.rowData.SNNAME || data.rowData.NAME
        this.carryingBusiness.id = data.rowData.ID
        this.carryingBusiness.show = true
        this.carryingBusiness.tableName = this.tableName
      },
      carryingOpticalPathExport(data){
        this.$message({
          type: 'success',
          message: '正在导出...'
        })
        carryingOpticalPathExport({},{id:data.rowData.ID,resType:'SDH'}).then(res =>{
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
      carryingBusinessExport(data){
        this.$message({
          type: 'success',
          message: '正在导出...'
        })
        carryingBusinessExport({},{id:data.rowData.ID,resType:'SDH'}).then(res =>{
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
    },
    mounted () {
    },
  }
</script>

<style scoped lang="scss">
</style>
