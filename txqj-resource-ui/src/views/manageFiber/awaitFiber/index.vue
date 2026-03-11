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
            <el-form-item label="起始站点 : " prop="aResObjid">
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
                v-model="searchForm.aResobjid"
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
            <el-form-item label="终止站点 ; " prop="zResObjid">
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
                v-model="searchForm.zResobjid"
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
              <el-button type="primary" size="mini" @click="getTableData()">查询</el-button>
              <el-button size="mini" @click="resetSearch()">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>

      <div class="tableBox">
        <div class="tabTopButton">
          <el-button type="primary" :disabled="isDisabled" size="mini" @click="applyCommit()">提交</el-button>
          <el-button size="mini" @click="backFiber">返回</el-button>
        </div>


        <el-table
          tooltip-effect="light"
          stripe
          height="calc(100% - 82px)"
          style="width: 100%;"
          ref="multipleTable"
          :data="tableData"
          border
          @selection-change="handleSelectionChange"
          v-loading="loading">
          <el-table-column
            type="selection"
            :selectable="selectable"
            align="center"
            width="50">
          </el-table-column>
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
          <el-table-column prop="isHgs" label="异构光缆" width="100" align="center">
            <template slot-scope="scope">
              <span>{{scope.IS_HGS==='0'?'否':'是'}}</span>
            </template>
          </el-table-column>
          <el-table-column prop="lineNumber" label="芯数" width="80" align="center"> </el-table-column>
          <el-table-column label="操作" width="200"  fixed="right" align="center">
            <template slot-scope="scope">
              <el-button size="mini" type="primary" @click="openEdit(scope.row)">编辑</el-button>
              <el-button size="mini" type="primary" @click="lookData(scope.row)">查看</el-button>
              <el-button size="mini" type="danger" @click="delData(scope.row)">删除</el-button>
              <el-dropdown trigger="click" style="margin-left: 5px">
                <span class="el-dropdown-link"><i class="el-icon-arrow-down el-icon--right"></i></span>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item @click.native="toAWaitFiberSeg(scope.row)">光缆段基本信息</el-dropdown-item>
                  <el-dropdown-item @click.native="fiberRouter(scope.row)">光缆示意图</el-dropdown-item>
                  <el-dropdown-item @click.native="toAWaitFiberLine(scope.row)">光缆纤芯</el-dropdown-item>
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
      custom-class="curdBox"
      :visible.sync="editStationDrawer"
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
      custom-class="curdBox"
      :visible.sync="newStationDrawer"
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
      custom-class="curdBox"
      :visible.sync="stationDrawer"
      width="750px"
      :modal="false"
      :close-on-click-modal="false"
    >
      <awaitSeeData ref="seeChild" :modelObj="this.modelObj" :dataObj="editForm"></awaitSeeData>
      <div slot="footer" class="dialog-footer">
        <el-button @click="stationDrawer=false">关 闭</el-button>
      </div>
    </el-dialog>
    <!--光缆示意图-->
    <el-dialog
      v-dialogDrag
      title="光缆示意图"
      :visible.sync="FiberRouterDialogShow"
      width="70%"
      top="10vh"
      :modal="false"
      :close-on-click-modal="false"
    >
      <div style="width: 100%; height: 70vh">
        <iframe
          ref="iframe"
          width="100%"
          id="iframe1"
          name="iframe1"
          style="border: none"
          height="100%"
          :src="iframeSrc1"
        ></iframe>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="createFiberLine()">生成光缆纤芯</el-button>
        <el-button @click="FiberRouterDialogShow=false">关  闭</el-button>
      </div>
    </el-dialog>
    <!--纤芯接续-->
    <el-dialog
      v-dialogDrag
      title="纤芯接续"
      top="10vh"
      :visible.sync="coreConnectBox.show"
      :close-on-click-modal="false"
      :modal="false"
      width="70%">
      <div style="width: 100%; height: 70vh;" v-loading="coreConnectBox.loading">
        <iframe
          ref="coreConnectBoxIframe"
          width="100%"
          height="100%"
          id="coreConnectBoxIframe"
          name="coreConnectBoxIframe"
          style="border: none;"
          :src="coreConnectBox.iframeSrc"
        ></iframe>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="coreConnectBox.show = false">关 闭</el-button>
        <!-- <el-button type="primary" @click="saveAndCreateFiber">保存纤芯接续并生成光缆</el-button> -->
      </div>
    </el-dialog>
    <!--批量连接纤芯-->
    <el-dialog
      v-dialogDrag
      :title="batchConnectionsBox.title + '  批量连接纤芯'"
      top="15%"
      :visible.sync="batchConnectionsBox.show"
      :close-on-click-modal="false"
      width="30%">
      <div style="width: 100%; height: 50vh">
        <el-form
          :model="batchConnectionsBox.data"
          :rules="batchConnectionsBox.rules"
          ref="batchConnectionsBox"
          label-width="150px">
          <el-form-item label="起始端" prop="aSegId">
            <el-select
              v-model="batchConnectionsBox.data.aSegId"
              @change="segIdChange($event, 'aSegId'), verificationBatchConnectionsBox()"
            >
              <el-option
                v-for="(item, key) in batchConnectionsBox.selectOption"
                :label="item.SEGNAME"
                :value="item.SEGID"
                :disabled="item.SEGID == batchConnectionsBox.data.zSegId"
                :key="key"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="起始纤芯序号 : " prop="aStartSn">
            <el-input-number
              v-model="batchConnectionsBox.data.aStartSn"
              :step="1"
              step-strictly
              :min="1"
              :max="getMaxSn('aSegId')"
              @change="ifRight('aStartSn'), verificationBatchConnectionsBox()"
            />
          </el-form-item>
          <el-form-item label="终止纤芯序号 : " prop="aEndSn">
            <el-input-number
              v-model="batchConnectionsBox.data.aEndSn"
              :step="1"
              step-strictly
              :min="1"
              :max="getMaxSn('aSegId')"
              @change="ifRight('aEndSn'), verificationBatchConnectionsBox()"
            />
          </el-form-item>
          <el-form-item label="终止端" prop="zSegId">
            <el-select
              v-model="batchConnectionsBox.data.zSegId"
              @change="segIdChange($event, 'zSegId'), verificationBatchConnectionsBox()">
              <el-option
                v-for="(item, key) in batchConnectionsBox.selectOption"
                :label="item.SEGNAME"
                :value="item.SEGID"
                :disabled="item.SEGID == batchConnectionsBox.data.aSegId"
                :key="key"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="起始纤芯序号 : " prop="zStartSn">
            <el-input-number
              v-model="batchConnectionsBox.data.zStartSn"
              :step="1"
              step-strictly
              :min="1"
              :max="getMaxSn('zSegId')"
              @change="ifRight('zStartSn'), verificationBatchConnectionsBox()"
            />
          </el-form-item>
          <el-form-item label="终止纤芯序号 : " prop="zEndSn">
            <el-input-number
              v-model="batchConnectionsBox.data.zEndSn"
              :step="1"
              step-strictly
              :min="1"
              :max="getMaxSn('zSegId')"
              @change="ifRight('zEndSn'), verificationBatchConnectionsBox()"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="commitbatchConnections">提交</el-button>
            <el-button @click="batchConnectionsBox.show = false">取消</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-dialog>
    <!--批量删除纤芯-->
    <el-dialog
      v-dialogDrag
      :title="delMoreBox.title + '  批量删除纤芯'"
      top="15%"
      :visible.sync="delMoreBox.show"
      :close-on-click-modal="false"
      width="30%">
      <div style="width: 100%; height: 30vh">
        <el-form
          :model="delMoreBox.data"
          :rules="delMoreBox.rules"
          ref="delMoreBox"
          label-width="150px">
          <el-form-item label="起始纤芯序号 : " prop="startSn">
            <el-input-number
              v-model="delMoreBox.data.startSn"
              :step="1"
              step-strictly
              :min="1"
              :max="delMoreBox.max"
            />
          </el-form-item>
          <el-form-item label="终止纤芯序号 : " prop="endSn">
            <el-input-number
              v-model="delMoreBox.data.endSn"
              :step="1"
              step-strictly
              :min="1"
              :max="delMoreBox.max"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="delMoreBoxCommit">提交</el-button>
            <el-button @click="delMoreBox.show">取消</el-button>
          </el-form-item>
        </el-form>
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
  import{getModelId,tableDropDownBox, getForm} from "@/api/zdwh/zdzywh";
  import {
    submitDataVerifyApi,latestPdConversion
  } from "@/api/gltxh/toDatawarehouse";
  import {
    listDefinition
  } from "@/api/flowable/definition";
  import{
    getFiberData,
    submitEdit,
    delData,
    getFiberRouterAwait,
    coreConnectShowApi,
    creatFiberLineForMiddle,
    getAwaitFiberByIdApi,
    addFiberLineApi,
    delFiberLinkApi,
    creatFiberLineForMeda,
  } from "@/api/manageFiber/fiber";
  import {
    getFiberCoreDetailApi
  } from "@/api/gltxh/gltxhbj";
  import awaitEditData from "@/views/publicPage/edit/awaitEditData";
  import awaitSeeData from "@/views/publicPage/edit/awaitSeeData";
  import fiberCoreDetail from "@/views/components/gltxh/fiberCoreDetail.vue"
  export default {
    name: "index",
    components: {awaitEditData,awaitSeeData,fiberCoreDetail},
    data(){
      var verifySn = (rule, value, callback) => {
        let a = Math.abs(this.batchConnectionsBox.data.aStartSn - this.batchConnectionsBox.data.aEndSn)
        let z = Math.abs(this.batchConnectionsBox.data.zStartSn - this.batchConnectionsBox.data.zEndSn)
        if (a - z != 0) {
          return callback(new Error('纤芯数量不对等'))
        } else {
          return callback()
        }
      };
      return {
        tableColData: [
          {name: '名称', prop: 'name'},
          {name: '审批状态', prop: 'auditStatus'},
          {name: '发起人', prop: 'createUser'},
          {name: '数据校验', prop: 'checkout'},
          {name: '提交时间', prop: 'reportDate'},
          {name: '操作描述', prop: 'editInfo'},
          {name: '所属光缆网', prop: 'parFibergrid'},
          {name: '重要等级', prop: 'fiberLevel'},
          {name: '起点站点', prop: 'aResobjid'},
          {name: '终点站点', prop: 'zResobjid'},
        ],
        iframeSrc1: '/stationWh/manageFiberHtml/fiberImaging.html',
        loading:true,
        //表对象信息
        modelObj: {},
        //基础查询
        showBaseQueryOpen:true,
        //搜索框
        searchForm: {
          name:'',
          aResobjid:'',
          zResobjid: ''
        },
        optionsA:{},
        optionsZ:{},
        optionsALoading: true,
        optionsZLoading: true,
        searchOptionStr: '',
        tableName:'SG_TCDEV_FIBER_B',
        //表的分页
        pageSize: 15,
        pageNum: 1,
        total: 0,
        //  表格数据
        tableData:[],
        //  提交多选
        multipleSelection:[],
        //提交按钮是否可点
        isDisabled:true,

        // 新建、修改光缆弹出框遮盖层
        newStationDrawer:false,
        //对话框题目
        dialogName:'',
        ifFirstEdit: true,
        ifFirstSee: true,
        //  光缆路由弹出框遮盖层
        FiberRouterDialogShow: false,
        FiberRouterData: {},
        //  查看光缆弹出框遮盖层
        stationDrawer:false,
        editStationDrawer: false,
        //  单条数据
        editForm:{},
        addForm: {},
        // 纤芯接续
        coreConnectBox: {
          id: '',
          show: false,
          loading: true,
          iframeSrc:'/stationWh/manageFiberHtml/coreConnect.html',
          groupsData: [],
          nodesData: [],
          linksData: [],
          resData: [],
        },
        // 批量连接纤芯
        batchConnectionsBox: {
          title: '',
          show: false,
          aMax: 0,
          zMax: 0,
          data: {
            aSegId: '',
            aStartSn: '',
            aEndSn: '',
            zSegId: '',
            zStartSn: '',
            zEndSn: '',
          },
          selectOption: [],
          rules: {
            aSegId: [{required: true, message: '此项不能为空', trigger: "blur"}],
            aStartSn: [
              {required: true, message: '此项不能为空', trigger: "blur"},
              {validator: verifySn, trigger: "change"},
            ],
            aEndSn: [
              {required: true, message: '此项不能为空', trigger: "blur"},
              {validator: verifySn, trigger: "change"},
            ],
            zSegId: [{required: true, message: '此项不能为空', trigger: "blur"}],
            zStartSn: [
              {required: true, message: '此项不能为空', trigger: "blur"},
              {validator: verifySn, trigger: "change"},
            ],
            zEndSn: [
              {required: true, message: '此项不能为空', trigger: "blur"},
              {validator: verifySn, trigger: "change"},
            ],
          }
        },
        // 批量删除纤芯
        delMoreBox: {
          title: '',
          show: false,
          max: 0,
          data: {
            segId: '',
            startSn: 0,
            endSn: 0,
          },
          rules: {
            startSn: [{required: true, message: '此项不能为空'}],
            endSn: [{required: true, message: '此项不能为空'}],
          }
        },

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
      // 纤芯接续
      window['coreConnect'] = (data) => {
        this.coreConnectShow(data)
      }
      window['addFiberLine'] = (data) => {
        this.addFiberLine(data)
      }
      window['reSetLink'] = (objs, delLinks, aResId, zResId) => {
        this.reSetLink(objs, delLinks, aResId, zResId)
      }
      window['delLink'] = (data, link) => {
        this.delLink(data, link)
      }
    },
    methods:{
      getColSize (val, titVal) {
        return getColSize(val, titVal)
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
      handleSelectionChange(val){
        this.multipleSelection = val;
        this.isDisabled=!val.length
      },
      getCommonData(){
        //获取表基本信息
        getModelId({tableName:this.tableName}).then(res=>{
          this.modelObj = res.data
            this.getTableData()
          this.getOption('optionsA')
          this.getOption('optionsZ')
        })
      },
      /*重置搜索 */
      resetSearch() {
        this.searchForm={
          name:'',
          aResobjid:'',
          zResobjid: ''
        }
      },
      selectable (row) {
        if (row.auditStatus == '正在审核') {
          return false
        }
        return true
      },
      /*获取表格数据*/
      getTableData(){
          let params={
            name:this.searchForm.name,
            aResobjid:this.searchForm.aResobjid,
            zResobjid:this.searchForm.zResobjid,
            isPage:'1',
            pageIndex: this.pageNum,
            pageSize: this.pageSize,
            flowId: this.$route.query && this.$route.query.procInsId
          }
          getFiberData(params).then(response => {
            this.tableData = response.rows;
            this.total = response.total;
            this.loading = false
          }).catch(() => {
            this.loading = false
          });
      },
      //新建
      openAdd(){
        this.addForm={}
        this.newStationDrawer = true
        this.$nextTick(()=>{
          //this.editForm=row
          this.$refs.child.modelObj=this.modelObj
          this.$refs.child.dataObj=this.addForm
          this.$refs.child.beforeLoading()
        })
      },
      //新增提交
      submitForm(){
        //调用子组件重复校验方法
        this.$refs.child.beforeSubmit()
        //获取子组件表单数据
        this.addForm={}
        this.addForm=this.$refs.child.pageForm
        setTimeout(() => {
          this.$confirm('是否保存此数据?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            let params = {}
            for (let key in this.addForm) {
              params[nameToTuoFeng(key)] = this.addForm[key]
            }
            submit(params).then(response => {
                this.newStationDrawer = false
                this.$modal.msgSuccess("操作成功");
              this.getTableData()
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
            submitEdit(params).then(response => {
                this.editStationDrawer = false
                this.$modal.msgSuccess("操作成功");
                //刷新列表
                this.getTableData()
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
          }).catch(() => {
              this.$message({
                  type: 'info',
                  message: '已取消删除'
              })
          })
      },
      //  跳转光缆段信息
      toAWaitFiberSeg(row){
        this.$router.push(
          {
            path:'/zdwh/sgtccon/awaitFiberSeg',
            query:{
                fiberId: row.id
            }
          }
        )
      },
      //  光缆示意图
      fiberRouter(row){
        getAwaitFiberByIdApi({id: row.id}).then(res => {
          this.FiberRouterData = res.data
          getFiberRouterAwait(res.data).then(res1 => {
            this.FiberRouterDialogShow = true
            let node = []
            let link = []
            for (let i = 0; i < res1.data.length; i++) {
              let item = res1.data[i]
              if (item.type == 3 && item.id != null) {
                item.aId = res1.data[i - 1].id
                item.zId = res1.data[i + 1].id
                link.push(item)
              } else {
                node.push(item)
              }
            }
            this.$nextTick(() => {
              window.setTimeout(() => {
                this.$refs.iframe.contentWindow.reSetTopo(node, link)
              }, 1000)
            })
          })
        })
      },

      coreConnectShow (data) {
        this.coreConnectBox.show = true
        this.coreConnectBox.loading = true
        this.coreConnectBox.tconId = data.id
        coreConnectShowApi({
          ASegId: data.aId,
          ZSegId: data.zId,
          tconId: data.id,
        }).then(res => { // +个任务id
          this.coreConnectBox.groupsData = []
          this.coreConnectBox.nodesData = []
          this.coreConnectBox.linksData = []
          this.coreConnectBox.id = data.id
          this.coreConnectBox.loading = false
          res.data.seg.forEach((item, index, arr) => {
            this.coreConnectBox.groupsData[index] = {
              SEGID: item.SEGID,
              SEGNAME: item.SEGNAME,
              groupLength: item.lines.length,
            }
            this.coreConnectBox.nodesData[index] = item.lines
          })
          this.coreConnectBox.linksData = res.data.link
          this.coreConnectBox.loading = false
          this.$nextTick(() => {
            window.setTimeout(() => {
              this.$refs.coreConnectBoxIframe.contentWindow.reSetTopo(
                this.coreConnectBox.groupsData,
                this.coreConnectBox.nodesData,
                this.coreConnectBox.linksData,
              )
            }, 100)
          })
        })
      },
      addFiberLine (data) {
        addFiberLineApi({
          aResId: data.aResId,
          zResId: data.zResId,
          tconId: this.coreConnectBox.tconId,
        }).then(res => {
          this.$refs.coreConnectBoxIframe.contentWindow.addLink(res.data)
        })
      },
      // 重新绘制纤芯
      reSetLink (objs, delLinks, aResId, zResId) {
        this.$confirm('此端口已连接纤芯 继续操作会替换原纤芯 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let obj = [...new Set(objs)]
          for (let i = 0; i < obj.length; i++) {
            delFiberLinkApi(obj[i]).then(res => {
              if (res.code == '200') {
                this.$refs.coreConnectBoxIframe.contentWindow.removerLinkCommit(delLinks[i])
              }
            })
          }
          this.addFiberLine({aResId: aResId, zResId: zResId})
        }).catch(e => {
          this.$message({
            type: 'info',
            message: '已取消'
          });
        })
      },

      delLink (data, link) {
        this.$confirm('删除后无法恢复 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          delFiberLinkApi(data).then(res => {
            if (res.code == '200') {
              this.$refs.coreConnectBoxIframe.contentWindow.removerLinkCommit(link)
              this.$message({
                type: 'success',
                message: '删除成功!'
              });
            }
          })
        }).catch(e => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        })
      },
      //  生成光缆纤芯
      createFiberLine () {
        creatFiberLineForMiddle(this.FiberRouterData).then(res => {
          this.$modal.msgSuccess("操作成功");
        })
      },

      //  跳转光缆纤芯
      toAWaitFiberLine(row){
        this.$router.push(
          {
            path:'/zdwh/sgtccon/awaitFiberLine',
            query:{
              fiberId:row.id,
              fiberName:row.name
            }
          }
        )
      },
      // 批量连接纤芯 获取纤芯最大值
      getMaxSn (name) {
        let data = this.batchConnectionsBox.selectOption.find((item) => {
          return item.SEGID == this.batchConnectionsBox.data[name]
        })
        if (data) {
          return data.groupLength
        } else {
          return 1
        }
      },
      // 批量连接纤芯提交
      commitbatchConnections () {
        this.$refs['batchConnectionsBox'].validate((valid) => {
          if (valid) {
            this.$confirm('此操作可能会影响原纤芯 是否继续?', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              this.batchConnectionsBox.data.TconId = this.coreConnectBox.id
              commitbatchConnectionsApi(this.batchConnectionsBox.data).then(res => {
                this.coreConnectShow(this.coreConnectBox)
                this.batchConnectionsBox.show = false
              })
            })
          }
        })
      },
      // 批量删除提交
      delMoreBoxCommit () {
        this.$confirm('删除后无法恢复 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.delMoreBox.data.TconId = this.coreConnectBox.id
          delMoreApi(this.delMoreBox.data).then(res => {
            this.delMoreBox.show = false
            this.coreConnectShow(this.coreConnectBox)
          })
        }).catch(e => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        })
      },
      //  跳转光缆纤芯
      toFiberLine(row){
        this.$router.push(
          {
            path:'/zdwh/sgtccon/fiberLine',
            query:{
              fiberId:row.ID,
              type:'await'
            }
          }
        )
      },
    //  返回
      backFiber(){
        this.$tab.closeOpenPage({ path: "/zdwh/sgtcdev/fiber"});
      },
      applyCommit () {
        latestPdConversion({procDefCategory:'process_fiber_list'}).then(res => {
          let arr = []
          for (let i = 0; i < this.multipleSelection.length; i++) {
            arr.push(this.multipleSelection[i].id)
          }
          arr = JSON.stringify(arr)
          this.$nextTick(() => {
            this.$router.push({ path: '/flowable/task/record/index/' + arr,
              query: {
                deployId: res.data.deploymentId,
                procDefId: res.data.id,
                finished: true,
                flowCategory: res.data.category,
                fiberArr: arr
              }
            })
          })
        })
      },

      // 查看光缆纤芯
      openFiberCoreDetail (data) {
        this.fiberCoreDetail.id = data.id
        this.fiberCoreDetail.name = data.name
        this.fiberCoreDetail.show = true
        this.fiberCoreDetail.loading = true
        getFiberCoreDetailApi({
          fiberId: data.id
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
