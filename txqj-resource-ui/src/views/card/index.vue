<template>
  <div>
    <zdzywhPage
      ref="zdzywhPage"
      :tableName="tableName"
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
    </zdzywhPage>
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
  export default {
    name: "index",
    components: {zdzywhPage,carryingOpticalPath,carryingBusiness},
    data(){
      return {
        tableName:'SG_TCDEV_TCCARD_B',
        //承载光路
        carryingOpticalPath:{
          title: '',
          id: '',
          show: false,
        },
        //承载业务
        carryingBusiness:{
          title: '',
          id: '',
          show: false,
        },
      }
    },
    watch: {
    },
    methods:{
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
        carryingOpticalPathExport({},{id:data.rowData.ID,resType:'板卡'}).then(res =>{
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
        carryingBusinessExport({},{id:data.rowData.ID,resType:'板卡'}).then(res =>{
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
