<template>
  <div class="commonWh">
    <div style="width: 100%; height: 0" class="getSpanSize"></div>
    <div class="box-card">
      <div class="queryBox">
        <!--基础查询-->
        <div class="basicsQuery">
          <el-form label-width="100px" :model="searchForm">
            <el-form-item label="名称 : " prop="fiberLineName">
              <el-input size="mini" v-model="searchForm.fiberLineName"></el-input>
            </el-form-item>
            <el-form-item label="所属光缆 : " prop="fiberId">
              <el-input size="mini" v-model="searchForm.fiberName">
              </el-input>
            </el-form-item>
            <el-form-item style="float: right">
              <el-button type="primary" size="mini" @click="getTableData()">查询</el-button>
              <el-button size="mini" @click="resetSearch()">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
      <div class="tableBox">
        <div class="tabTopButton">
          <el-button size="mini" plain @click="back">返回</el-button>
        </div>
        <el-table
          ref="multipleTable"
          :data="tableData"
          tooltip-effect="light"
          height="calc(100% - 82px)"
          style="width: 100%;"
          stripe
          border
          @selection-change="handleSelectionChange"
          v-loading="loading">
          <el-table-column label="序号" width="80" align="center" :formatter="snFormatter"></el-table-column>
          <el-table-column
            v-if="tableData[0]"
            v-for="item in tableColData"
            :label="item.name"
            :min-width="getColSize(tableData[0][item.prop], item.name)"
            align="center"
            show-overflow-tooltip
            :prop="item.prop"
          ></el-table-column>
          <el-table-column label="操作" width="120" fixed="right" align="center">
            <template slot-scope="scope">
              <el-button size="mini" type="primary" @click="openEdit(scope.row)">编辑</el-button>
              <el-button size="mini" type="danger" @click="lookData(scope.row)">查看</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          :total="total"
          :page.sync="pageNum"
          :limit.sync="pageSize"
          @pagination="getTableData"
        />
      </div>
    </div>

    <!--对话框-->
    <el-dialog
      v-dialogDrag
      title="修改"
      :visible.sync="editStationDrawer"
      custom-class="curdBox"
      width="750px"
      :modal="false"
      :close-on-click-modal="false"
    >
      <awaitEditData ref="child" :modelObj="this.modelObj" :dataObj="this.editForm"></awaitEditData>
      <div slot="footer" class="dialog-footer" style="margin-bottom:20px">
        <el-button type="primary" @click="submitEditForm()">确 定</el-button>
        <el-button @click="editStationDrawer=false">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 查看属性对话框 -->
    <el-dialog
      v-dialogDrag
      title="查看"
      :visible.sync="stationDrawer"
      custom-class="curdBox"
      width="750px"
      :modal="false"
      :close-on-click-modal="false"
    >
      <awaitSeeData ref="seeChild" :modelObj="this.modelObj" :dataObj="editForm"></awaitSeeData>
      <div slot="footer" class="dialog-footer" style="margin-bottom:20px">
        <el-button @click="stationDrawer=false">关 闭</el-button>
      </div>
    </el-dialog>

  </div >
</template>

<script>
  import {nameTo_, nameToTuoFeng} from "@/utils"
  import {getColSize} from "@/utils/getColSize"
  import{getModelId,getData,selectAllRules,getVerrules,getColunmId,errorStation,fileDownload} from "@/api/zdwh/zdzywh";
  import{submitEdit,getFiberlineAwaitData} from "@/api/manageFiber/fiberline";
  import awaitEditData from "@/views/publicPage/edit/awaitEditData";
  import awaitSeeData from "@/views/publicPage/edit/awaitSeeData";
  export default {
    name: "index",
    components: {awaitEditData,awaitSeeData},
    data(){
      return {
        tableColData: [
          {name: '名称', prop: 'name'},
          {name: '纤芯序号', prop: 'sn'},
          {name: '所属光缆', prop: 'parFiber'},
          {name: '纤芯类型', prop: 'fiberlineType'},
          {name: '使用状态', prop: 'serviceState'},
          {name: '对应光路', prop: 'opticalroadId'},
          {name: '对应纤芯通道', prop: 'channelId'},
          {name: '描述', prop: 'des'},
        ],
        loading:false,
        tableName:'SG_TCDEV_FIBERLINE',
        //表对象信息
        modelObj: {},
        //基础查询
        showBaseQueryOpen:true,
        //搜索框
        searchForm: {
          fiberLineName:'',
          fiberId:this.$route.query.fiberId,
          fiberName:this.$route.query.fiberName
        },
        //表的分页
        pageSize: 15,
        pageNum: 1,
        total: 0,
        //  表格数据
        tableData:[],
        //  删除多选
        multipleSelection:[],
        //所属光缆段下拉框
        options:[],
        //  新建、修改光缆弹出框遮盖层
        editFiberDialogShow:false,
        //  查看光缆弹出框遮盖层
        lookFiberDialogShow:false,
        //  单条数据
        editForm:{},
        stationDrawer:false,
        editStationDrawer: false,
          ifFirstEdit: true,
          ifFirstSee: true
      }
    },
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
    created(){
      this.getCommonData()
    },
    methods:{
      getColSize (val, titVal) {
        return getColSize(val, titVal)
      },
      /* 格式序列化列 */
      snFormatter: function (row, col, value, index) {
        return this.pageSize * (this.pageNum - 1) + index + 1;
      },
      handleSelectionChange(val){
        this.multipleSelection = val;
      },
      getCommonData(){
          this.loading = true
        //获取表基本信息
        getModelId({tableName:this.tableName}).then(res=>{
          this.modelObj = res.data
          this.getTableData()

        })
      },
      /*重置搜索 */
      resetSearch() {
        this.searchForm={
          fiberLineName:'',
          fiberId:this.$route.query.fiberId,
          fiberName:this.$route.query.fiberName
        }
        this.getTableData()
      },
      /*获取表格数据*/
      getTableData(){
          let params={
              isPage:'1',
              pageIndex: this.pageNum,
              pageSize: this.pageSize,
              fiberId:this.$route.query.fiberId,
              name:this.searchForm.fiberLineName
          }
          getFiberlineAwaitData(params).then(response => {
              this.tableData = response.rows;
              this.total = response.total;
              this.loading = false;
          }).catch(() => {
              this.loading = false
          });
      },
      //返回
      back(){
        if(this.$route.query.type){
          this.$tab.closeOpenPage({ path: "/zdwh/sgtccon/awaitFiber"});
        }else{
          this.$tab.closeOpenPage({ path: "/zdwh/sgtccon/fiber"});
        }

      },
      //编辑
      openEdit(row){
        this.editForm={}
        this.editStationDrawer = true
        for (let key in row) {
          this.editForm[nameTo_(key)] = row[key]
        }
        this.$nextTick(()=>{
          this.$refs.child.modelObj=this.modelObj
          this.$refs.child.dataObj=this.editForm
          if(this.ifFirstEdit){
            this.$refs.child.beforeLoading()
            this.ifFirstEdit=false
          }else{
            this.$refs.child.reOpen()
          }
        })
      },
      //编辑提交
      submitEditForm(){
          //调用子组件重复校验方法
          this.$refs.child.beforeSubmit()
          //获取子组件表单数据
          this.editForm={}
          this.editForm=this.$refs.child.pageForm
          setTimeout(() => {
              this.$confirm('是否保存此数据?', '提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
              }).then(() => {
                  let params = {}
                  for (let key in this.editForm) {
                      params[nameToTuoFeng(key)] = this.editForm[key]
                  }
                  params.parFiber = this.$route.query.fiberId
                  submitEdit(params).then(response => {
                    this.editStationDrawer = false
                    this.$modal.msgSuccess("操作成功");
                    this.getTableData()
                  })
              })
          }, 1500);
      },
      //查看
      lookData(row){
        this.editForm=row
        this.stationDrawer = true
        for (let key in row) {
          this.editForm[nameTo_(key)] = row[key]
        }
        this.$nextTick(()=>{
          this.$refs.seeChild.modelObj=this.modelObj
          this.$refs.seeChild.dataObj=this.editForm
          if(this.ifFirstSee){
            this.$refs.seeChild.beforeLoading()
            this.ifFirstSee=false
          }else{
            this.$refs.seeChild.reOpen()
          }
        })
      },
      //批量删除
      deleteSta(){
        if(this.multipleSelection.length===0){
          this.$message.error('请选中数据进行删除');
        } else {

        }
      },
      //  单个删除
      delData(scope){

      },


    },
  }
</script>

<style>
</style>
