<template>
  <div class="commonWh">
    <div style="width: 100%; height: 0" class="getSpanSize"></div>
    <div class="box-card">
      <div class="queryBox">
        <div class="basicsQuery">
          <el-form label-width="100px" :model="searchForm">
            <el-form-item label="名称 : " prop="fiberSegLineName">
              <el-input size="mini" v-model="searchForm.fiberSegLineName"></el-input>
            </el-form-item>
            <el-form-item label="所属光缆段 : " prop="fiberSegId">
              <el-input size="mini" v-model="searchForm.fiberSegName"></el-input>
            </el-form-item>
            <el-form-item style="float: right;">
              <el-button type="primary" size="mini" @click="getTableData()">查询</el-button>
              <el-button size="mini" @click="resetSearch()">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>

      <div class="tableBox">
        <div class="tabTopButton">
          <el-button type="primary" size="mini" @click="openAdd()">新建</el-button>
          <el-button type="danger" :disabled="isDisabled" plain  size="mini" v-show="false" @click="deleteSta()">删除</el-button>
          <el-button size="mini" plain @click="back">返回</el-button>
        </div>
        <el-table
          tooltip-effect="light"
          ref="multipleTable"
          :data="tableData"
          height="calc(100% - 82px)"
          style="width: 100%;"
          border
          @selection-change="handleSelectionChange"
          v-loading="loading">
          <el-table-column label="序号" width="80" align="center" :formatter="snFormatter" ></el-table-column>
          <el-table-column
            v-if="tableData[0]"
            v-for="item in tableColData"
            :label="item.name"
            :min-width="getColSize(tableData[0][item.prop], item.name)"
            align="center"
            show-overflow-tooltip
            :prop="item.prop"
          ></el-table-column>
          <el-table-column label="操作" width="180"  fixed="right" align="center">
            <template slot-scope="scope">
              <el-button type="primary" size="mini" @click="openEdit(scope.row)">编辑</el-button>
              <el-button type="primary" size="mini" @click="lookData(scope.row)">查看</el-button>
              <el-button type="danger" size="mini" @click="delData(scope.row)">删除</el-button>
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
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitEditForm()">确 定</el-button>
        <el-button @click="editStationDrawer=false">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 新增属性对话框 -->
    <el-dialog
      v-dialogDrag
      title="新增"
      :visible.sync="newStationDrawer"
      custom-class="curdBox"
      width="750px"
      :modal="false"
      :close-on-click-modal="false"
    >
      <awaitEditData ref="child" :modelObj="this.modelObj" :dataObj="this.editForm"></awaitEditData>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm()">确 定</el-button>
        <el-button @click="newStationDrawer=false">取 消</el-button>
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
      <div slot="footer" class="dialog-footer">
        <el-button @click="stationDrawer=false">关 闭</el-button>
      </div>
    </el-dialog>
  </div >
</template>

<script>
  import {getColSize} from "@/utils/getColSize"
  import{getModelId,getData} from "@/api/zdwh/zdzywh";
  import{getFiberSegLineByFiberSegData,submit, submitEdit, delData} from "@/api/manageFiber/fiberSegLine";
  import awaitEditData from "@/views/publicPage/edit/awaitEditData";
  import awaitSeeData from "@/views/publicPage/edit/awaitSeeData";
  import {nameTo_, nameToTuoFeng} from "@/utils"
  export default {
    name: "index",
   components: {awaitEditData,awaitSeeData},
    data(){
      return {
        tableColData: [
          {name: '名称', prop: 'name'},
          {name: '描述', prop: 'des'},
          {name: '纤芯序号', prop: 'sn'},
          {name: '所属光缆段', prop: 'fiberSegName'},
          {name: '所属光缆', prop: 'fiberName'},
          {name: '纤芯类型', prop: 'fiberlineTypeName'},
          {name: '使用状态', prop: 'serviceStateName'},
          {name: '对应光路', prop: 'roadName'},
          {name: '对应纤芯通道', prop: 'channelName'},
        ],
        loading:false,
        tableName:'SG_TCDEV_FIBERLINE_B',
        //表对象信息
        modelObj: {},
        //基础查询
        showBaseQueryOpen:true,
        //搜索框
        searchForm: {
          fiberSegLineName:'',
          fiberSegId:this.$route.query.fiberSegId,
          fiberSegName:this.$route.query.fiberSegName
        },
        //表的分页
        pageSize: 15,
        pageNum: 1,
        total: 0,
        //  表格数据
        tableData:[],
        //  删除多选
        multipleSelection:[],
        newStationDrawer:false,
        isDisabled:true,
        //所属光缆段下拉框
        options:[],
        stationDrawer:false,
        editStationDrawer: false,
        //对话框题目
        dialogName:'',
        //  查看光缆弹出框遮盖层
        lookFiberDialogShow:false,
        //  单条数据
        editForm:{},
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
        this.isDisabled=!val.length
      },
      getCommonData(){
          this.getTableData()
        //获取表基本信息
        getModelId({tableName:this.tableName}).then(res=>{
          this.modelObj = res.data
        })
      },
      /*重置搜索 */
      resetSearch() {
        this.searchForm={
            fiberSegLineName:'',
            fiberSegId:this.$route.query.fiberSegId,
            fiberSegName:this.$route.query.fiberSegName
        }
        this.getTableData();
      },
      /*获取表格数据*/
      getTableData(){
          let params={
              name:this.searchForm.fiberSegLineName,
              parFiberSeg:this.searchForm.fiberSegId,
              isPage:'1',
              pageIndex: this.pageNum,
              pageSize: this.pageSize,
          }
          getFiberSegLineByFiberSegData(params).then(response => {
                  this.tableData = response.rows;
                  this.total = response.total;
              }
          ).catch(() => {
              this.loading = false
          });
      },
      //返回
      back(){
        this.$tab.closeOpenPage({
          path: "/zdwh/sgtccon/awaitFiberSeg",
          query:{
            fiberId:this.$route.query.fiberId,
            type:this.$route.query.type
          }
        });
      },
      //新建
      openAdd(){
          this.editForm={}
          this.newStationDrawer = true
          this.$nextTick(()=>{
              //this.editForm=row
              this.$refs.child.modelObj=this.modelObj
              this.$refs.child.dataObj=this.editForm
              this.$refs.child.beforeLoading()
          })
      },
      //新增提交
      submitForm(){
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
                  params.parFiberSeg = this.$route.query.fiberSegId
                  params.parFiber = this.$route.query.fiberId
                  submit(params).then(response => {
                          this.newStationDrawer = false
                          this.$modal.msgSuccess("操作成功");
                          this.getTableData();
                      }
                  )
              })
          }, 1500);
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
                  params.parFiberSeg = this.$route.query.fiberSegId
                  params.parFiber = this.$route.query.fiberId
                  submitEdit(params).then(response => {
                          this.editStationDrawer = false
                          this.$modal.msgSuccess("操作成功");
                          this.getTableData();
                      }
                  )
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
      delData(row){
          this.$confirm('此操作将删除该数据, 是否继续?', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
          }).then(() => {
              let data = {}
              data.id=row.id
              data.editstatus=row.editstatus
              delData(data).then(response => {
                  this.$modal.msgSuccess("操作成功");
                  this.getTableData();
              })
          }).catch((e) => {
              console.log(e)
              this.$message({
                  type: 'info',
                  message: '已取消删除'
              })
          })

      },
    },
  }
</script>

<style>
</style>
