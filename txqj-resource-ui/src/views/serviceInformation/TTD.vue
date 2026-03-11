<template>
  <div class="commonWh">
    <div style="margin: 10px">
      <el-button type="primary" size="mini" @click="openAdd()" v-hasPermi="['system:b:add']">新建通道段</el-button>
      <el-button type="primary" size="mini" @click="bindTTDShow()" v-hasPermi="['system:b:add']">绑定通道段</el-button>
    </div>

    <el-table
      stripe
      border
      style="width: 100%"
      height="calc(100% - 60px)"
      ref="multipleTable"
      :data="tableData"
      v-loading="loading">
      <el-table-column label="序号" width="80" align="center" :formatter="snFormatter" ></el-table-column>
      <template
        v-if="tableData[0]"
        v-for="item in tableColData"
      >
        <el-table-column
          :label="item.name"
          :min-width="getColSize(tableData[0][item.prop], item.name)"
          align="center"
          show-overflow-tooltip
          :prop="item.prop"
        ></el-table-column>
      </template>
      <el-table-column label="操作" width="380"  fixed="right" align="center">
        <template slot-scope="scope">
          <el-button size="mini" type="primary"  @click="openEdit(scope.row)" v-hasPermi="['system:b:update']">编辑</el-button>
          <el-button size="mini" type="primary" @click="lookData(scope.row)" v-hasPermi="['system:b:list']">查看</el-button>
          <el-button size="mini" type="danger"  @click="delData(scope.row)" v-hasPermi="['system:b:remove']">删除</el-button>
          <el-button size="mini" type="danger" @click="unbindTDDCommit(scope.row)" v-hasPermi="['system:b:remove']">解除绑定</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination
      :total="total"
      :page.sync="pageNum"
      :limit.sync="pageSize"
      @pagination="getTableData"
    />
    <!--对话框-->
    <el-dialog
      v-dialogDrag
      custom-class="curdBox"
      title="修改"
      :visible.sync="editStationDrawer"
      width="1800px"
      :modal="false"
      :close-on-click-modal="false"
    >
      <editData ref="child" :modelObj="this.modelObj" :dataObj="this.editForm"></editData>
      <div slot="footer" class="dialog-footer" style="margin-bottom:20px">
        <el-button type="primary" @click="submitEditForm()">确 定</el-button>
        <el-button @click="editStationDrawer=false">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 新增属性对话框 -->
    <el-dialog
      v-dialogDrag
      custom-class="curdBox"
      title="新增"
      :modal="false"
      :visible.sync="newStationDrawer"
      width="1800px"
      :close-on-click-modal="false"
    >
      <editData ref="child" :modelObj="this.modelObj" :dataObj="this.editForm" :form="commonData"></editData>
      <div slot="footer" class="dialog-footer" style="margin-bottom:20px">
        <el-button type="primary" @click="submitForm()">确 定</el-button>
        <el-button @click="newStationDrawer=false">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 查看属性对话框 -->
    <el-dialog
      v-dialogDrag
      custom-class="curdBox"
      title="查看"
      :modal="false"
      :visible.sync="stationDrawer"
      width="1800px"
      :close-on-click-modal="false"
    >
      <seeData ref="seeChild" :modelObj="this.modelObj" :dataObj="editForm"></seeData>
      <div slot="footer" class="dialog-footer" style="margin-bottom:20px">
        <el-button @click="stationDrawer=false">关 闭</el-button>
      </div>
    </el-dialog>
    <!-- 绑定通道段 -->
    <el-dialog
      v-dialogDrag
      custom-class="curdBox"
      title="绑定通道段"
      :visible.sync="bindTTDBox.show"
      width="80%"
      :modal="false"
      :close-on-click-modal="false"
    >
      <div style="height: 70vh" class="commonWh">
        <el-form ref="form" label-width="100px" :inline="true">
          <el-form-item label="名称 : ">
            <el-input size="mini" v-model="bindTTDBox.searchForm.name"></el-input>
          </el-form-item>
          <el-form-item label="A设备 : " prop="ares">
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
              v-model="bindTTDBox.searchForm.aResId"
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
          <el-form-item label="Z设备" prop="zres">
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
              v-model="bindTTDBox.searchForm.zResId"
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
          <el-form-item style="float: right;" label-width="0">
            <el-button size="mini" type="primary" style="margin-left: 10px"  @click="bindTTDShow()">查询</el-button>
            <el-button size="mini" @click="bindTTDBox.searchForm = {}">重置</el-button>
          </el-form-item>
        </el-form>
        <el-table
          ref="multipleTable2"
          stripe
          border
          height="calc(100% - 60px)"
          style="width: 100%;"
          :data="bindTTDBox.data"
          v-loading="bindTTDBox.tableLoadIng"
        >
          <el-table-column
            label="序号"
            width="80"
            align="center"
            :formatter="snFormatter"
          ></el-table-column>
          <el-table-column
            v-if="bindTTDBox.data[0]"
            v-for="item in bindTTDBox.tableColData"
            :label="item.name"
            :min-width="getColSize(bindTTDBox.data[0][item.prop], item.name)"
            align="center"
            show-overflow-tooltip
            :prop="item.prop"
          ></el-table-column>
          <el-table-column
            label="操作"
            fixed="right"
            width="80"
            align="center"
          >
            <template slot-scope="scope">
              <el-button size="mini" type="primary" @click="bindTDDCommit(scope.row)" v-if="!scope.row.llId">选择</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          :total="bindTTDBox.total"
          :page.sync="bindTTDBox.pageNum"
          :limit.sync="bindTTDBox.pageSize"
          @pagination="bindTTDShow"
        />
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="bindTTDBox.show = false">取 消</el-button>
      </div>
    </el-dialog>
  </div >
</template>

<script>
  import {nameTo_, nameToTuoFeng} from "@/utils"
  import {getColSize} from "@/utils/getColSize"
  import{getModelId, tableDropDownBox} from "@/api/zdwh/zdzywh";
  import editData from "@/views/publicPage/edit/editData";
  import seeData from "@/views/publicPage/edit/seeData";
  import {
    getTtdTableApi,
    ttdAddCommitApi,
    ttdEditCommitApi,
    ttdDelCommitApi,
    bindingTTDList,
    unbindTTDApi,
    bindChannelSegments
  } from '@//api/serviceInformation'
  export default {
    name: "TTD",
    components: {editData,seeData},
    props: {
      llId: String,
      ywId: String,
    },
    data(){
      return {
        tableColData: [
          {name: '名称', prop: 'name'},
          {name: '描述', prop: 'des'},
          {name: 'A端设备', prop: 'aResName'},
          {name: 'A端端口', prop: 'aPort'},
          {name: 'Z端设备', prop: 'zResName'},
          {name: 'Z端端口', prop: 'zPort'},
          {name: '创建时间', prop: 'createDate'},
          {name: '变更时间', prop: 'updateDate'},
        ],
        loading:false,
        tableName:'SG_TCCON_TCCHANNELSEG_B',
        //表对象信息
        modelObj: {},
        //表的分页
        pageSize: 15,
        pageNum: 1,
        total: 0,
        //  表格数据
        tableData:[],
        newStationDrawer:false,
        stationDrawer:false,
        editStationDrawer: false,
        //  单条数据
        editForm:{},
        ifFirstEdit: true,
        ifFirstSee: true,
        //传递的数据
        commonData:{},

        bindTTDBox: {
          show: false,
          searchForm: {},
          pageSize: 15,
          pageNum: 1,
          total: 0,
          data: [],
          tableColData: [
            {name: '名称', prop: 'name'},
            {name: 'A端设备', prop: 'aResName'},
            {name: 'A端端口', prop: 'aPort'},
            {name: 'Z端设备', prop: 'zResName'},
            {name: 'Z端端口', prop: 'zPort'},
            {name: '绑定链路', prop: 'llName'},
            {name: '绑定业务', prop: 'ywName'},
          ],
          tableLoadIng: false,
        },
        // 下拉框框配置
        selectName: '',
        optionsA:{},
        optionsZ:{},
        optionsALoading: true,
        optionsZLoading: true,
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
      /*绑定通道段*/
      bindTTDShow(){
        this.bindTTDBox.show = true
        this.bindTTDBox.tableLoadIng = true
        this.bindTTDBox.data = []
        let params = this.bindTTDBox.searchForm
        params.pageSize = this.bindTTDBox.pageSize
        params.pageNum = this.bindTTDBox.pageNum
        bindingTTDList(params).then(res=>{
          this.bindTTDBox.data = res.rows
          this.bindTTDBox.total=res.total
          this.$nextTick(() => {
            // 初始化表格
            this.$refs.multipleTable2.doLayout();
          })
          this.bindTTDBox.tableLoadIng = false
          this.getOption('optionsA')
          this.getOption('optionsZ')

        })
      },
      bindTDDCommit(row){
        bindChannelSegments({
          segId:row.id,
          channelId:this.llId,
          ywId:this.ywId
        }).then(response => {
            this.bindTTDBox.show = false
            this.$modal.msgSuccess("操作成功");
            this.$emit('obtainChannelLinks')
            this.getTableData();
          }
        )
      },
      unbindTDDCommit(row){
        this.$confirm('是否解除绑定?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          unbindTTDApi({
            segId:row.id
          }).then(response => {
              this.bindTTDBox.show = false
              this.$modal.msgSuccess("操作成功");
              this.getTableData();
            }
          )
        }).catch((e) => {
          this.$message({
            type: 'info',
            message: '已取消解除绑定'
          })
        })
      },
      getColSize (val, titVal) {
        return getColSize(val, titVal)
      },
      /* 格式序列化列 */
      snFormatter: function (row, col, value, index) {
        return this.pageSize * (this.pageNum - 1) + index + 1;
      },
      getCommonData(){
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
        this.loading = true;
        let params={
          pageIndex: this.pageNum,
          pageSize: this.pageSize,
        }
        if(this.llId){
          params.llId = this.llId
        }else {
          params.llId = '空'
        }
        getTtdTableApi(params).then(response => {
          this.tableData = response.rows;
          this.total = response.total;
          this.loading = false;
        }).catch(() => {
          this.loading = false
        });
      },
      //新建
      openAdd(){
        this.editForm={}
        this.newStationDrawer = true
        this.commonData.CHANNELSEG_TYPE = '1100'
        this.$nextTick(()=>{
          this.$refs.child.form=this.commonData
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
            params.llId = this.llId
            params.ywId = this.ywId
            ttdAddCommitApi(params).then(response => {
              this.newStationDrawer = false
              this.$modal.msgSuccess("操作成功")
              if(this.llId){
                this.getTableData();
              }else {
                this.$emit('obtainChannelLinks')
              }
              }
            )
          })
        }, 1500);
      },
      //编辑
      openEdit(row){
        this.editForm = row
        for (let key in row) {
            this.editForm[nameTo_(key)] = row[key]
        }
        this.editStationDrawer = true
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
            params.llId = this.llId
            ttdEditCommitApi(params).then(response => {
                this.editStationDrawer = false
                this.$modal.msgSuccess("操作成功");
                this.$emit('obtainChannelLinks')
                this.getTableData();
              }
            )
          })
        }, 1500);
      },
      //查看
      lookData(row){
        this.editForm = row
        for (let key in row) {
            this.editForm[nameTo_(key)] = row[key]
        }
        this.stationDrawer = true
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
          ttdDelCommitApi(row.id).then(response => {
            this.$modal.msgSuccess("操作成功");
            this.getTableData();
          })
        }).catch((e) => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })

      },
      /*搜索*/
      loadmore (val) {
        if (this[this.selectName].total > this[this.selectName].list.length) {
          let pageNum = parseInt(this[this.selectName].list.length / 20) * 20
          tableDropDownBox({
            modelId: this.modelObj.modelId,
            modeVersion: this.modelObj.modelVersion,
            attribute: 'A_TCSITE_ID',
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
      remoteMethod (val, data) {
        this.searchOptionStr = val
        tableDropDownBox({
          modelId: this.modelObj.modelId,
          modeVersion: this.modelObj.modelVersion,
          attribute: 'A_RES_ID',
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
      getOption (data) {
        tableDropDownBox({
          modelId: this.modelObj.modelId,
          modeVersion: this.modelObj.modelVersion,
          attribute: 'A_RES_ID',
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
    },
  }
</script>

