<template>
  <div class="commonWh">
    <div style="width: 100%; height: 0" class="getSpanSize"></div>
    <div class="box-card">
      <div class="queryBox">
        <!--基础查询-->
        <div class="basicsQuery">
          <el-form label-width="100px" :model="searchForm">
            <el-form-item label="名称 : " prop="fiberName">
              <el-input size="mini" v-model="searchForm.name"></el-input>
            </el-form-item>
            <el-form-item label="所属光缆 : " prop="siteName">
              <el-select
                size="mini"
                placeholder="请选择"
                v-model="searchForm.fiberId"
              >
                <el-option
                  v-for="item in fiberIdOptions"
                  :key="item.code"
                  :label="item.name"
                  :value="item.code"
                  :disabled="item.disabled">
                </el-option>
              </el-select>
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
          <el-button type="primary"  size="mini" @click="openAdd()">新建</el-button>
          <el-button size="mini"  @click="selectFiberSegs">选择光缆段</el-button>
          <el-button size="mini" @click="back">返回</el-button>
        </div>
        <el-table
          tooltip-effect="light"
          stripe
          height="calc(100% - 82px)"
          style="width: 100%;"
          ref="multipleTable"
          :data="tableData"
          border
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
          <el-table-column label="操作" width="200"  fixed="right" align="center">
            <template slot-scope="scope">
              <el-button type="primary" size="mini" @click="openEdit(scope.row)">编辑</el-button>
              <el-button type="primary" size="mini" @click="lookData(scope.row)">查看</el-button>
              <el-button type="danger" size="mini" @click="delData(scope.row)">删除</el-button>
              <el-dropdown style="margin-left: 5px">
                <span class="el-dropdown-link"><i class="el-icon-arrow-down el-icon--right"></i></span>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item @click.native="toFiberSegLine(scope.row)">光缆段纤芯</el-dropdown-item>
                  <el-dropdown-item @click.native="openFiberCoreDetail(scope.row)">光缆纤芯示意图</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
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
    <!--选择光缆段-->
    <el-dialog
      v-dialogDrag
      title="选择光缆段"
      :visible.sync="selectFiberSegDialogShow"
      top="5vh"
      width="80vw"
      :modal="false"
      :close-on-click-modal="false"
    >
      <div style="height: 80vh">
        <!--搜索-->
        <el-form label-width="100px" :model="searchFiberSegForm" :inline="true">
          <el-form-item label="名称 : " prop="fiberSegName">
            <el-input size="mini" v-model="searchFiberSegForm.name" placeholder="请输入光缆段名称"></el-input>
          </el-form-item>
          <el-form-item label="起始接续装置 : " prop="aResobjid">
            <el-select
              size="mini"
              placeholder="请选择"
              clearable
              filterable
              remote
              @focus="selectName = 'optionsA'"
              :loading="optionsALoading"
              v-loadmore="loadmore"
              :remote-method="(val) => remoteMethod(val, 'optionsA')"
              v-model="searchFiberSegForm.aResobjid"
            >
              <el-option
                v-for="item in optionsA.list"
                :key="item.code"
                :label="item.name"
                :value="item.code"
                :disabled="item.disabled">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="终止接续装置" prop="zResobjid">
            <el-select
              size="mini"
              placeholder="请选择"
              clearable
              filterable
              remote
              @focus="selectName = 'optionsZ'"
              :loading="optionsZLoading"
              v-loadmore="loadmore"
              :remote-method="(val) => remoteMethod(val, 'optionsZ')"
              v-model="searchFiberSegForm.zResobjid"
            >
              <el-option
                v-for="item in optionsZ.list"
                :key="item.code"
                :label="item.name"
                :value="item.code"
                :disabled="item.disabled">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item style="float: right;">
            <el-button type="primary" size="mini" @click="getFiberSegTable()">查询</el-button>
            <el-button size="mini" @click="resetFiberSegSearch()">重置</el-button>
            <el-button type="primary" :disabled="addDisabled" plain  size="mini" @click="getFiberSegData()">添加</el-button>
          </el-form-item>
        </el-form>
        <!--表格-->
        <el-table
          ref="fiberSegTable"
          :data="fiberSegTable"
          tooltip-effect="light"
          height="calc(100% - 100px)"
          style="width: 100%;"
          border
          @selection-change="SelectionFiberSegChange"
          v-loading="loading">
          <el-table-column
            type="selection"
            align="center"
            width="50">
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            label="序号"
            width="80"
            align="center"
            :formatter="snFormatter"
          ></el-table-column>
          <el-table-column
            show-overflow-tooltip
            prop="name"
            label="名称"
            min-width="400"
            align="center"
          ></el-table-column>
          <el-table-column
            show-overflow-tooltip
            prop="fiberTypeName"
            label="光缆段类型"
            min-width="100"
            align="center"
          ></el-table-column>
          <el-table-column
            show-overflow-tooltip
            prop="aResobjidName"
            label="起点接续装置"
            min-width="300"
            align="center"
          ></el-table-column>
          <el-table-column
            show-overflow-tooltip
            prop="zResobjidName"
            label="终点接续装置"
            min-width="300"
            align="center"
          ></el-table-column>
          <el-table-column
            show-overflow-tooltip
            prop="lineNumber"
            label="芯数"
            min-width="80"
            align="center"
          ></el-table-column>
        </el-table>
        <pagination
          :total="fiberSegTableTotal"
          :page.sync="fiberSegTablePageNum"
          :limit.sync="fiberSegTablePageSize"
          @pagination="getFiberSegTable"
        />
      </div>
    </el-dialog>

    <el-dialog
      v-dialogDrag
      :title="fiberCoreDetail.name"
      top="5vh"
      class="fiberCoreDetail"
      :visible.sync="fiberCoreDetail.show"
      :modal="false"
      :close-on-click-modal="false"
      width="90%">
      <div style="width: 100%; height: 80vh;" v-loading="fiberCoreDetail.loading">
        <fiberCoreDetail
          ref="fiberCoreDetail"
          :data="fiberCoreDetail.data"
          :isEdit="true"
          @openFiberCoreDetail="openFiberCoreDetail({
            resId: fiberCoreDetail.id,
            name: fiberCoreDetail.name,
          })"
          @loadingEnd="fiberCoreDetail.loading = false"
        />
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fiberCoreDetail.show = false">关 闭</el-button>
        <!-- <el-button type="primary" @click="saveAndCreateFiber">保存纤芯接续并生成光缆</el-button> -->
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {nameTo_, nameToTuoFeng} from "@/utils"
  import {getColSize} from "@/utils/getColSize"
  import{getModelId, tableDropDownBox, getForm} from "@/api/zdwh/zdzywh";
  import{getFiberSegByFiberData, submit, submitEdit, delData, getFiberOptionsAll, getSelFiberAwait,addFiberRFiberSeg} from "@/api/manageFiber/fiberSeg";
  import awaitEditData from "@/views/publicPage/edit/awaitEditData";
  import awaitSeeData from "@/views/publicPage/edit/awaitSeeData";
  import {
    getFiberCoreDetailApi
  } from "@/api/gltxh/gltxhbj";
  import fiberCoreDetail from "@/views/components/gltxh/fiberCoreDetail.vue"
  export default {
    name: "index",
      components: {awaitEditData,awaitSeeData,fiberCoreDetail},
    data(){
      return {
        tableColData: [
          {name: '名称', prop: 'name'},
          {name: '光缆段类型', prop: 'fiberTypeName'},
          {name: '起点接续装置', prop: 'aResobjidName'},
          {name: '终点接续装置', prop: 'zResobjidName'},
          {name: '生产厂家', prop: 'producerNameLabel'},
          {name: '芯数', prop: 'lineNumber'},
        ],
        loading:false,
        tableName:'SG_TCDEV_FIBERSEG_B',
        //表对象信息
        modelObj: {},
        //基础查询
        showBaseQueryOpen:true,
        //搜索框
        searchForm: {
          name:'',
          fiberId:this.$route.query.fiberId
        },
        //表的分页
        pageSize: 15,
        pageNum: 1,
        total: 0,
        //  表格数据
        tableData:[],
        //所属光缆下拉框
        fiberIdOptions:[],


        optionsA:{},
        optionsZ:{},
        optionsALoading: true,
        optionsZLoading: true,
        searchOptionStr: '',

        selectFiberSegDialogShow:false,//遮盖层
        //搜索框
        searchFiberSegForm:{
          name: '',
          aResobjid:'',
          zResobjid:'',
          fiberId:this.$route.query.fiberId
        },
        newStationDrawer:false,
      //  选择光缆段表格
        fiberSegTable:[],
        fiberSegTablePageSize: 15,
        fiberSegTablePageNum: 1,
        fiberSegTableTotal: 0,
      //  多选光缆段关联光缆
        selectFiberSeg:[],
        addDisabled:true,

        stationDrawer:false,
        editStationDrawer: false,
        editForm:{},
        ifFirstEdit: true,
        ifFirstSee: true,

        fiberCoreDetail: {
          loading: false,
          show: false,
          title: '',
          id: '',
          data: {}
        },
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
      geFiberIdtOption () {
        getFiberOptionsAll({
          code: this.$route.query.fiberId,
          pageNum: 0,
          pageSize: 20,
        }).then((res) => {
          this.fiberIdOptions = res.rows
        })
      },
      getOption (data) {
        tableDropDownBox({
          modelId: this.modelObj.modelId,
          modeVersion: this.modelObj.modelVersion,
          attribute: 'A_RESOBJID',
          pageNum: 0,
          pageSize: 20,
        }).then((res) => {
          if (res.total > 0) {
            this[data].list = res.rows
            this[data].total = res.total
            this.$nextTick(() => {
              this[data + 'Loading'] = false
            })
          }
        })
      },
      remoteMethod (val, data) {
        this.searchOptionStr = val
        tableDropDownBox({
          modelId: this.modelObj.modelId,
          modeVersion: this.modelObj.modelVersion,
          attribute: 'A_RESOBJID',
          name: this.searchOptionStr,
          pageNum: 0,
          pageSize: 20,
        }).then((res) => {
          this[data + 'Loading'] = true
          this[data].list = res.rows
          this[data].total = res.total
          this.$nextTick(() => {
            this[data + 'Loading'] = false
          })
        })
      },
      loadmore (val) {
        if (this[this.selectName].total > this[this.selectName].list.length) {
          let pageNum = parseInt(this[this.selectName].list.length / 20) * 20
          tableDropDownBox({
            modelId: this.modelObj.modelId,
            modeVersion: this.modelObj.modelVersion,
            attribute: 'A_RESOBJID',
            name: this.searchOptionStr,
            pageNum: pageNum,
            pageSize: 20,
          }).then((res) => {
            this[this.selectName + 'Loading'] = true
            if (res.rows.length > 0) {
              this[this.selectName].list.push.apply(
                this[this.selectName].list,
                res.rows
              )
            }
            this.$nextTick(() => {
              this[this.selectName + 'Loading'] = false
            })
          })
        } else {
          if (
            !this[this.selectName].list[this[this.selectName].list.length - 1].disabled
          ) {
            this[data + 'Loading'] = true
            this[this.selectName].list.push({
              code: 'aaabbbccc',
              name: '没有更多了',
              disabled: true
            })
            this.$nextTick(() => {
              this[data + 'Loading'] = false
            })
          }
        }
      },
      /* 格式序列化列 */
      snFormatter: function (row, col, value, index) {
        return this.pageSize * (this.pageNum - 1) + index + 1;
      },
      SelectionFiberSegChange(val){
        this.selectFiberSeg=val;
        this.addDisabled=!val.length
      },
      getCommonData(){
        //获取表基本信息
        getModelId({tableName:this.tableName}).then(res=>{
          this.modelObj = res.data
          this.getTableData()
          this.getOption('optionsA')
          this.getOption('optionsZ')
          this.geFiberIdtOption()
          this.getFiberSegTable()
        })
      },
      /*重置搜索 */
      resetSearch() {
        this.searchForm={
          name:'',
          fiberId:this.$route.query.fiberId
        }
        this.getTableData()
      },
      /*获取表格数据*/
      getTableData(){
          let params={
              name:this.searchForm.name,
              fiberId:this.searchForm.fiberId,
              isPage:'1',
              pageIndex: this.pageNum,
              pageSize: this.pageSize,
          }
          getFiberSegByFiberData(params).then(response => {
              this.tableData = response.rows;
              this.total = response.total;
          }).catch(() => {
              this.loading = false
          });
      },
      //返回
      back(){
        if(this.$route.query.type){
          this.$tab.closeOpenPage({
            path: "/zdwh/sgtccon/awaitFiber",
          });
        }else{
          this.$tab.closeOpenPage({
            path: "/zdwh/sgtccon/awaitFiber",
          });
        }
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
            params.fiberId = this.$route.query.fiberId
            submit(params).then(response => {
                this.editStationDrawer = false
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
            params.fiberId = this.$route.query.fiberId
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

      //选择光缆段
      selectFiberSegs(){
        this.selectFiberSegDialogShow=true
      },
      //获取选择光缆段中的表格
      getFiberSegTable(){
        let params = this.searchFiberSegForm
        params.pageNum = this.fiberSegTablePageNum
        params.pageSize = this.fiberSegTablePageSize
        if(params.name==""){
            params.name = '1000kV'
        }
        getSelFiberAwait(params).then(response => {
          this.fiberSegTable = response.rows;
          this.fiberSegTableTotal = response.total;
          this.loading = false;
        })
      },
      //重置选择光缆段中的搜索
      resetFiberSegSearch(){
        this.searchFiberSegForm={
          name: '',
          aResobjid:'',
          zResobjid:'',
          fiberId:this.$route.query.fiberId
        }
        this.getFiberSegTable()
      },
      //添加光缆段
      getFiberSegData(){
          let params=[]
          for(let i = 0; i < this.selectFiberSeg.length; i++) {
              let obj = {
                  fibersegId:this.selectFiberSeg[i].id,
                  fiberId:this.$route.query.fiberId
              }
              params.push(obj)
          }
          addFiberRFiberSeg(params).then(response => {
              this.selectFiberSegDialogShow=false
              this.$message({
                  type: 'info',
                  message: '绑定成功!请去待处理数据提交!'
              })
          })

      },

      /**/
      //  跳转光缆段纤芯
      toFiberSegLine(row){
        this.$router.push(
          {
            path:'/zdwh/sgtccon/awaitFiberSegLine',
            query:{
              fiberId:this.searchForm.fiberId,
              fiberSegId:row.id,
              fiberSegName:row.name,
              type:this.$route.query.type
            }
          }
        )
      },


      // 查看光缆纤芯
      openFiberCoreDetail (data) {
        let name = this.fiberIdOptions.find(item => {
          return item.code == this.searchForm.fiberId
        })
        name = name.name
        this.fiberCoreDetail.id = this.searchForm.fiberId
        this.fiberCoreDetail.name = name
        this.fiberCoreDetail.show = true
        this.fiberCoreDetail.loading = true
        getFiberCoreDetailApi({
          fiberId: this.searchForm.fiberId
        }).then(res => {
          this.fiberCoreDetail.data = res.data
          this.$nextTick(() => {
            this.$refs.fiberCoreDetail.locationLine()
          })
        })
      },
    },
  }
</script>

<style>
</style>
