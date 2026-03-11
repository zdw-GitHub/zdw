<template>
  <div>
    <zdzywhPage
      ref="zdzywhPage"
      :isSearch="true"
      :tableName="'SG_TCDEV_FIBERSEG_B'"
      @getTable="getTable"
      :operate="{
            slot: 'handle',
            label: '操作',
            width: '250',
        }"
      :tabOperate="true"
    >
      <template v-slot:tabOperate>
        <el-button size="mini" plain @click="back">返回</el-button>
        <el-button size="mini" type="primary" @click="openEditBox('add', {})" v-hasPermi="['system:b:add']">新增</el-button>
        <el-button size="mini" @click="getFiberSegTable" v-hasPermi="['system:b:add']">选择光缆段</el-button>
        <el-button size="mini" type="danger" @click="deleteSta()" v-hasPermi="['system:b:remove']">删除</el-button>
        <el-button size="mini" @click="handleExport()">导出</el-button>
        <el-button size="mini" @click="openWindow" icon="el-icon-full-screen">全屏</el-button>
      </template>
      <template v-slot:handle="slot">

        <el-button size="mini" type="primary" @click="openEditBox('edit', slot.scope.row)" v-hasPermi="['system:b:update']">编辑</el-button>
        <el-button type="primary" size="mini" @click="searchStation(slot.scope)" v-hasPermi="['system:b:list']">查看</el-button>
        <el-button size="mini" type="danger" @click="delData(slot.scope.row)" v-hasPermi="['system:b:remove']">删除</el-button>
        <el-button size="mini" type="danger" @click="delFiberRFiberSeg(slot.scope.row)" v-hasPermi="['system:b:remove']">解绑</el-button>
        <el-dropdown style="margin-left: 5px">
          <span class="el-dropdown-link"><i class="el-icon-arrow-down el-icon--right"></i></span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="toFiberSegLine(slot.scope.row)">光缆段纤芯</el-dropdown-item>
            <!--<el-dropdown-item @click.native="openFiberCoreDetail(slot.scope.row)">光缆纤芯示意图</el-dropdown-item>-->
          </el-dropdown-menu>
        </el-dropdown>
      </template>
    </zdzywhPage>

    <!-- 新增属性对话框 -->
    <el-dialog
      v-dialogDrag
      custom-class="curdBox"
      :title="editBox.type == 'add' ? '新增' : '修改'"
      :modal="false"
      :visible.sync="editBox.show"
      width="750px"
      :close-on-click-modal="false"
    >
      <editData ref="child" :modelObj="modelObj" :dataObj="editBox.data"></editData>
      <div slot="footer" class="dialog-footer" style="margin-bottom:20px">
        <el-button type="primary" @click="submitForm()">确 定</el-button>
        <el-button @click="editBox.show = false">取 消</el-button>
      </div>
    </el-dialog>
    <!--选择光缆段-->
    <el-dialog
      v-dialogDrag
      custom-class="curdBox"
      title="选择光缆段"
      :visible.sync="selectFiberSeg.show"
      width="80%"
      :modal="false"
      :close-on-click-modal="false"
    >
      <div style="height: 70vh" class="commonWh">
        <!--搜索-->
        <el-form label-width="120px" :model="selectFiberSeg.searchForm" :inline="true">
          <el-form-item label="名称 : " prop="fiberSegName">
            <el-input size="mini" v-model="selectFiberSeg.searchForm.name" placeholder="请输入光缆段名称"></el-input>
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
              v-model="selectFiberSeg.searchForm.aResobjid"
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
              v-model="selectFiberSeg.searchForm.zResobjid"
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
          <el-form-item style="float: right;margin-left: 0!important;">
            <el-button type="primary"  size="mini" @click="getFiberSegTable()">查询</el-button>
            <el-button size="mini"  @click="resetFiberSegSearch()">重置</el-button>
          </el-form-item>
        </el-form>
        <!--表格-->
        <el-table
          stripe
          border
          ref="fiberSegTable"
          :data="selectFiberSeg.tableData"
          height="calc(100% - 60px)"
          style="width: 100%;"
          @selection-change="SelectionFiberSegChange"
          v-loading="selectFiberSeg.loading">
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
          :total="selectFiberSeg.total"
          :page.sync="selectFiberSeg.pageNum"
          :limit.sync="selectFiberSeg.pageSize"
          @pagination="getFiberSegTable"
        />
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :disabled="selectFiberSeg.selectedArr[0] == undefined" @click="getFiberSegData">添 加</el-button>
        <el-button @click="selectFiberSeg.show=false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { nameToTuoFeng} from "@/utils"
  import{getData,getModelId, tableDropDownBox} from "@/api/zdwh/zdzywh";
  import{submit, submitEdit, delDataApi, getSelFiber,addFiberRFiberSeg,delFiberRFiberSeg,getSegIdsByFiberId} from "@/api/manageFiber/fiberSeg";
  import editData from "@/views/publicPage/edit/editData";
  import fiberCoreDetail from "@/views/components/gltxh/fiberCoreDetail.vue"
  import zdzywhPage from '@/views/commonPage/zdzywh/index';
  export default {
    name: "index",
    components: {editData,fiberCoreDetail,zdzywhPage},
    data(){
      return {
        tableName:'SG_TCDEV_FIBERSEG_B',
        modelObj: {},
        editBox: {
          type: '',
          show: false,
          data: {},
          isFirst: true,
        },


        // 下拉框框配置
        selectName: '',
        optionsA:{},
        optionsZ:{},
        optionsALoading: true,
        optionsZLoading: true,
        searchOptionStr: '',
        // 选择光缆
        selectFiberSeg: {
          show: false,
          loading:false,
          searchForm: {
            name: '',
            aResobjid:'',
            zResobjid:'',
            fiberId:JSON.parse(this.$route.query.search).FIBER_ID,
          },

          tableData: [],
          pageSize: 15,
          pageNum: 1,
          total: 0,
          selectedArr: []
        },
        // 光缆纤芯示意图
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
      'selectFiberSeg.tableData': {
        handler() {
          this.$nextTick(() => {
            this.$refs.fiberSegTable.doLayout();
          })
        },
        deep: true
      },
    },
    created(){
      this.getCommonData()
    },
    methods:{
      /* 格式序列化列 */
      snFormatter: function (row, col, value, index) {
        return this.selectFiberSeg.pageSize * (this.selectFiberSeg.pageNum - 1) + index + 1;
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
      getCommonData(){
        //获取表基本信息
        getModelId({tableName:this.tableName}).then(res=>{
          this.modelObj = res.data
          this.getOption('optionsA')
          this.getOption('optionsZ')
        })
      },

      getTable (params) {
        getSegIdsByFiberId({fiberId: JSON.parse(this.$route.query.search).FIBER_ID}).then(res =>{
          let sign = true
          let paramsArr = params.whereAttributes
          for (let i = 0; i < paramsArr.length; i++) {
            if(paramsArr[i].attributeName == 'ID'){
              sign = false
              paramsArr[i].attributeValue = res.msg
            }
            if(paramsArr[i].attributeName == 'FIBER_ID'){
              paramsArr.splice(i,1)
            }
          }
          if(sign){
            params.whereAttributes.push({
              attributeName:'ID',
              attributeValue:res.msg,
            })
          }
          getData(params).then(response => {
            this.$refs.zdzywhPage.tableData = response.rows;
            this.$refs.zdzywhPage.total = parseInt(response.total);
            this.$refs.zdzywhPage.loading = false;
          }).catch(() => {
            this.$refs.zdzywhPage.loading = false
          });
        })
      },
      //返回
      back(){
        this.$router.back()
      },
      handleExport(){
        this.$refs.zdzywhPage.handleExport()
      },
      // 新增/修改
      openEditBox(type, row){
        this.editBox.type = type
        this.editBox.show = true
        this.editBox.data = row
        this.$nextTick(()=>{
          this.$refs.child.modelObj=this.modelObj
          this.$refs.child.dataObj=this.editBox.data
          if(this.editBox.isFirst){
            this.$refs.child.beforeLoading()
            this.editBox.isFirst = false
          }else{
            this.$refs.child.reOpen()
          }
        })
      },
      //新增提交
      submitForm(){
        //调用子组件重复校验方法
        this.$refs.child.beforeSubmit()
        setTimeout(() => {
          this.$confirm('是否保存此数据?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            let params1 = this.$refs.child.pageForm
            let params = {}
            for (let key in params1) {
              params[nameToTuoFeng(key)] = params1[key]
            }
            params.fiberId = JSON.parse(this.$route.query.search).FIBER_ID
            if (this.editBox.type == 'add') {
              submit(params).then(response => {
                this.editBox.show = false
                this.$modal.msgSuccess("操作成功");
                this.$refs.zdzywhPage.getTableData()
              })
            } else {
              submitEdit(params).then(response => {
                this.editBox.show = false
                this.$modal.msgSuccess("操作成功");
                this.$refs.zdzywhPage.getTableData()
              })
            }
          })
        }, 1500);
      },

      //查看
      searchStation(scope){
        this.$refs.zdzywhPage.searchStation(scope)
      },
      openWindow(){
        this.$refs.zdzywhPage.openWindow()
      },

      //  单个删除
      delData(row){
        this.$confirm('此操作删除的数据将进入带处理数据中, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          delDataApi(row.ID).then(response => {
            this.$modal.msgSuccess("操作成功");
            this.$refs.zdzywhPage.getTableData()
          })
        }).catch((e) => {
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
              this.$modal.msgSuccess("操作成功");
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
      SelectionFiberSegChange(val){
        this.selectFiberSeg.selectedArr = val;
      },
      //选择光缆段表格展示
      getFiberSegTable(){
        this.selectFiberSeg.show = true
        this.selectFiberSeg.loading = true
        let params = this.selectFiberSeg.searchForm
        params.pageNum = this.selectFiberSeg.pageNum
        params.pageSize = this.selectFiberSeg.pageSize
        getSelFiber(params).then(response => {
          this.selectFiberSeg.tableData = response.rows
          this.selectFiberSeg.total = response.total
          this.selectFiberSeg.loading = false
        })
      },
      //重置选择光缆段中的搜索
      resetFiberSegSearch(){
        this.selectFiberSeg.searchForm = {
          name: '',
          aResobjid:'',
          zResobjid:'',
          fiberId:JSON.parse(this.$route.query.search).FIBER_ID,
        }
      },
      //添加光缆段
      getFiberSegData(){
        let params=[]
        for(let i = 0; i < this.selectFiberSeg.selectedArr.length; i++) {
            let obj = {
                fibersegId: this.selectFiberSeg.selectedArr[i].id,
                fiberId: JSON.parse(this.$route.query.search).FIBER_ID
            }
            params.push(obj)
        }
        addFiberRFiberSeg(params).then(response => {
          this.selectFiberSeg.show = false
          this.$modal.msgSuccess("操作成功");
          this.$refs.zdzywhPage.getTableData()
        })

      },
      delFiberRFiberSeg(row){
        this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          delFiberRFiberSeg({
            fiberId: JSON.parse(this.$route.query.search).FIBER_ID,
            fibersegId: row.ID
          }).then(response => {
            this.$modal.msgSuccess("操作成功");
            this.$refs.zdzywhPage.getTableData()
          })
        })

      },
      //  跳转光缆段纤芯
      toFiberSegLine(row){
        this.$router.push(
          {
            path:'/zdwh/sgtccon/fiberSegLine',
            query:{
              search: JSON.stringify({
                PAR_FIBER_SEG: row.ID
              }),
              refresh:true,
            }
          }
        )
      },
    },
  }
</script>

<style>
</style>
