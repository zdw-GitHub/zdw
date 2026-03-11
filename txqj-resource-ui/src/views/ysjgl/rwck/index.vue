<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--数据源数据-->
      <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-radio-group v-model="type">
            <el-radio label='0' value='0'>全部</el-radio>
            <el-radio label='1'>已分组</el-radio>
            <el-radio label='2'>未分组</el-radio>
          </el-radio-group>
          <el-input
            v-model="dbName"
            placeholder="请输入数据源名称"
            clearable
            size="small"
            prefix-icon="el-icon-search"
            style="margin: 10px 0"
          />
        </div>
        <div class="head-container">
          <el-tree
            class="filter-tree"
            :data="deptOptions"
            highlight-current
            accordion
            node-key="id"
            :default-expanded-keys="[1]"
            :filter-node-method="filterNode"
            ref="tree" style="font-size: 14px;height:70vh;overflow-y: auto;">
          <span style="width: 100%;height:100%;display: block" class="custom-tree-node" slot-scope="{ node, data }">
            <span style="width: 100%;display: block;line-height: 25px" @click="handleNodeClick(data)">
                <i :class="data.icon" :style="data.color"></i>{{ node.label }}
            </span>
        </span>
          </el-tree>
        </div>
      </el-col>
      <el-col :span="20" :xs="24">
        <el-row>
          <!--任务基本信息-->
          <el-col :span="24" :xs="12">
            <el-row :gutter="16" style="border-bottom:1px solid #d3dce6;padding-bottom:5px">
              <el-col :span="18">
                <span>任务信息</span>
              </el-col>
            </el-row>
            <el-form style="margin:10px auto;width: 80%" :model="form" ref="form" size="small" :inline="true" label-width="100px">
              <div style="display: flex">
                <el-form-item label="任务名称" style="margin-right: 260px">
                  <el-input
                    v-model="form.jobName"
                    readonly
                    disabled
                    style="width: 240px"
                  />
                </el-form-item>
                <el-form-item label="任务组名" >
                  <el-input
                    v-model="form.jobGroup"
                    readonly
                    disabled
                    style="width: 240px"
                  />
                </el-form-item>
              </div>
              <div style="display: flex">
                <el-form-item label="所属类目" style="margin-right: 260px">
                  <el-input
                    v-model="form.dbName"
                    readonly
                    disabled
                    style="width: 240px"
                  />
                </el-form-item>
                <el-form-item label="元模型名称">
                  <el-input
                    v-model="form.tableName"
                    readonly
                    disabled
                    style="width: 240px"
                  />
                </el-form-item>
              </div>
              <!--<div style="display: flex">
                <el-form-item label="修改时间" style="margin-right: 260px">
                  <el-input
                    v-model="form.updateTime"
                    readonly
                    disabled
                    style="width: 240px"
                  />
                </el-form-item>
                <el-form-item label="创建时间">
                  <el-input
                    v-model="form.createTime"
                    readonly
                    disabled
                    style="width: 240px"
                  />
                </el-form-item>
              </div>-->
            </el-form>
          </el-col>
          <!--任务日志-->
          <el-col :span="24" :xs="12">
            <el-row :gutter="16" style="border-bottom:1px solid #d3dce6;padding-bottom:5px">
              <el-col :span="18">
                <span>任务日志</span>
              </el-col>
            </el-row>
            <el-form :model="searchParams" ref="searchParams" size="small" :inline="true"  label-width="88px" style="margin-top:10px">
              <el-form-item label="执行状态" >
                <el-select
                  v-model="searchParams.status"
                  placeholder="请选择执行状态"
                  clearable
                  @keyup.enter.native="getLogList">
                  <el-option label="成功" key="0" value="0"></el-option>
                  <el-option label="失败" key="1" value="1"></el-option>
                  </el-select>
              </el-form-item>
              <el-form-item label="异常信息" >
                <el-select
                  v-model="searchParams.exceptionInfo"
                  placeholder="请选择异常信息"
                  clearable
                  @keyup.enter.native="getLogList">
                  <el-option label="正常" key="0" value="0"></el-option>
                  <el-option label="失败" key="1" value="1"></el-option>
                  </el-select>
              </el-form-item>
              <el-form-item label="执行时间" >
                <el-date-picker
                  v-model="searchParams.lastExecutionTime"
                  type="datetimerange"
                  range-separator="至"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  style="width:300px">
                </el-date-picker>
                <!-- <el-input
                  v-model="searchParams.lastExecutionTime"
                  placeholder="请输入执行时间"
                  clearable
                  @keyup.enter.native="getLogList"
                /> -->
              </el-form-item>
            <el-form-item label="耗时(s)" prop="status">
              <el-input
                  v-model="searchParams.startTimeConsuming"
                  clearable
                  style="width:120px;margin-right:5px"
                  @keyup.enter.native="getLogList"
                />
                <span>-</span>
                <el-input
                  v-model="searchParams.stopTimeConsuming"
                  clearable
                  style="width:120px;margin-left:5px"
                  @keyup.enter.native="getLogList"
                />
            </el-form-item>
            <el-form-item style="float: right">
              <el-button type="info" plain icon="el-icon-search" size="mini" @click="getLogList">搜索</el-button>
              <el-button icon="el-icon-refresh" plain size="mini" @click="resetSearchQuery">重置</el-button>
            </el-form-item>
          </el-form>
            <el-table height="520"  :data="logList">
              <el-table-column label="序号" align="center" :formatter="snFormatter" />
              <el-table-column label="任务名称" align="center" prop="jobName">
              </el-table-column>
              <el-table-column label="所属类目" align="center" prop="dbName" />
              <el-table-column label="元模型名称" align="center" prop="tableName" />
              <el-table-column label="总数据" align="center" prop="total" />
              <el-table-column label="已同步数据" align="center" prop="synchronizedQuantity" />
              <el-table-column label="未同步数据" align="center" prop="unsynchronizedQuantity" />
              <el-table-column label="执行状态" align="center" prop="status">
                <template slot-scope="scope">
                  <span v-if="scope.row.status==='0'||scope.row.status===0">成功</span>
                  <span v-else>失败</span>
                </template>
              </el-table-column>
              <el-table-column label="异常信息" align="center"  prop="exceptionInfo" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                  <el-button type="text" size="mini" @click="errorLog(scope.row)">{{ scope.row.exceptionInfo }}</el-button>
                </template>
              </el-table-column>
              <el-table-column label="执行时间" align="center" prop="createTime" width="180">
                <template slot-scope="scope">
                  <span>{{ parseTime(scope.row.createTime) }}</span>
                </template>
              </el-table-column>
              <el-table-column label="耗时（s）" align="center" prop="jobMessage">
              </el-table-column>
            </el-table>
            <pagination
              v-show="total>0"
              :total="total"
              :page.sync="pageNum"
              :limit.sync="pageSize"
              @pagination="getLogList"
            />
          </el-col>
        </el-row>
      </el-col>
    </el-row>
<!--查看异常信息日志-->
<el-dialog title="查看异常信息" close-on-click-modal="false" :visible.sync="openErrorLog" width="68%" append-to-body style="margin-top:80px">
  <el-form :model="errorQueryParams" ref="errorQueryParams" size="small" :inline="true"  label-width="88px">
      <el-form-item label="异常数据唯一标识" prop="errorId" label-width="130px">
        <el-input
          v-model="errorQueryParams.errorId"
          placeholder="请输入异常数据唯一标识"
          clearable
          @keyup.enter.native="errorLog"
        />
      </el-form-item>
      <el-form-item label="异常类型" prop="errorType">
          <el-select
          v-model="errorQueryParams.errorType"
          placeholder="请选择异常类型"
          clearable
          @keyup.enter.native="errorLog">
          <el-option label="系统" key="0" value="0"></el-option>
          <el-option label="数据" key="1" value="1"></el-option>
          </el-select>
      </el-form-item>
      <el-form-item label="异常信息" prop="errorMessage">
          <el-input
          v-model="errorQueryParams.errorMessage"
          placeholder="请输入异常信息"
          clearable
          @keyup.enter.native="errorLog"
        />
      </el-form-item>

      <el-form-item style="float: right">
        <el-button type="info" plain icon="el-icon-search" size="mini" @click="errorLog">搜索</el-button>
        <el-button icon="el-icon-refresh" plain size="mini" @click="resetQueryError">重置</el-button>
      </el-form-item>
    </el-form>
      <el-table v-loading="errorLogLoading" :data="errorLogList" height="500px">
        <el-table-column label="序号" align="center" :formatter="snFormatter" width="120px"/>
        <el-table-column label="异常类型" align="center" prop="errorType" width="180px">
          <template slot-scope="scope">
            <span v-if="scope.row.errorType==='0'||scope.row.errorType===0">系统</span>
            <span v-if="scope.row.errorType==='1'||scope.row.errorType===1">数据</span>
          </template>
        </el-table-column>
        <el-table-column label="异常数据唯一标识" align="center" prop="errorId" width="180px"></el-table-column>
        <el-table-column label="异常信息" align="center" prop="errorMessage"></el-table-column>
      </el-table>
        <pagination
          v-show="totalError>=0"
          :total="totalError"
          :page.sync="pageNum"
          :limit.sync="pageSize"
          @pagination="errorLog"
          style="margin-bottom:20px"
          />
      <div slot="footer" class="dialog-footer">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>
   </div>
</template>

<script>
  import { sjyTree,getById,listLog  } from "@/api/sjygl/rwck";
  import { getErrorLogList } from "@/api/sjygl/rwzgl";
  export default {
    name: "Rwck",
    data() {
      return {
        //数据源树
        type:'0',
        dbName:'',
        deptOptions: [{
          id: '1',
          label: '数据源',
          icon: 'el-icon-s-home',
          color: 'color: #FFC000',
          children: []
        }],
        // 错误日志id
        logId:'',
        // 错误日志查询
        errorQueryParams: {
          errorId: '',
          errorMessage: '',
          errorType: ''
        },
        // 搜索列表
        searchParams: {
          status: '',
          exceptionInfo: '',
          lastExecutionTime: '',
          startTimeConsuming: '',
          stopTimeConsuming: ''
        },
        // 异常信息日志
        errorLogList: [],
        logTotal: 0,
        totalError: 0,
        // 遮罩层
        errorLogLoading: true,
        // 是否显示异常信息弹出层
        openErrorLog: false,
        //选中任务
        jobObj:{},
        //任务基本信息
        form:{},
        //任务日志列表
        logList:[],
        pageSize:10,
        pageNum:1,
        total:0,
      };
    },
    watch: {
      // 根据名称筛选树，节点过滤
      dbName(val) {
        this.$refs.tree.filter(val);
      },
      type(val) {
        this.getSjyTree();
      },
    },
    created() {
      this.getSjyTree();
    },
    methods: {
      /** 重置按钮操作 */
      resetQueryError() {
        this.errorQueryParams.errorId = undefined;
        this.errorQueryParams.errorMessage = undefined;
        this.errorQueryParams.errorType = undefined;
        this.getErrorLog();
      },
       /** 查询异常信息日志 */
      errorLog(row) {
        this.openErrorLog = true;
        this.errorLogLoading = true;
        if(row.jobLogId!==undefined) {
           this.logId = row.jobLogId
          }
        this.getErrorLog()
        /* console.log(row) */
      },
      getErrorLog() {
        var paramsHis = {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            logId: this.logId,
            errorId: this.errorQueryParams.errorId,
            errorMessage: this.errorQueryParams.errorMessage,
            errorType: this.errorQueryParams.errorType
          }
        getErrorLogList(paramsHis).then(response => {
            this.errorLogList = response.rows;
            this.errorLogLoading = false;
            this.totalError = response.total;
          });
      },
      /** 查询数据源下拉树结构 */
      getSjyTree() {
        sjyTree({type:this.type}).then(res => {
          res=res.data
          for (let i = 0; i < res.length; i++) {
            for (let j = 0; j < res[i].children.length; j++) {
              if(res[i].children[j].jobGroupName){
                res[i].children[j].label=res[i].children[j].label+"（"+res[i].children[j].jobGroupName+"）"
              }
            }
          }
          this.deptOptions[0].children = res
        });
      },
      //获取任务基本信息
      getBaseMsg(){
        getById(this.jobObj.jobId).then(response => {
          this.form = response.data
        });
      },
      // 重置任务日志查询
      resetSearchQuery() {
          this.searchParams.status = '';
          this.searchParams.exceptionInfo = '';
          this.searchParams.lastExecutionTime = '';
          this.searchParams.startTimeConsuming = '';
          this.searchParams.stopTimeConsuming = '';
          this.getLogList();
      },
      //获取任务日志
      getLogList(){
        console.log(this.searchParams.lastExecutionTime)
        let params={
          pageSize:this.pageSize,
          pageNum:this.pageNum,
          jobId:this.jobObj.jobId,
          status: this.searchParams.status,
          exceptionInfo: this.searchParams.exceptionInfo,
          startTimeConsuming: this.searchParams.startTimeConsuming,
          stopTimeConsuming: this.searchParams.stopTimeConsuming
        }
        if(this.searchParams.lastExecutionTime.length>0) {
          params.startTime = this.searchParams.lastExecutionTime[0];
          params.stopTime = this.searchParams.lastExecutionTime[1]
        }
        listLog(params).then(response => {
          this.logList = response.rows;
         /*  this.logList[1].exceptionInfo = '123'
          alert(1) */
          this.total = response.total;
        });
      },
      // 筛选节点
      filterNode(value, data) {
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      },
      // 节点单击事件
      handleNodeClick(node) {
        //清空右侧页面数据
        this.jobObj={}
        this.form={}
        this.logList=[]
          this.pageSize=10
          this.pageNum=1
          this.total=0
        //获取右侧页面数据
        console.log(node)
        if(node.jobId!==undefined&&node.jobId!==''){
          this.jobObj=node
          this.getBaseMsg()
          this.getLogList()
        }
      },
      snFormatter: function (row, col, value, index) {
        return this.pageSize * (this.pageNum - 1) + index + 1;
      },
    }
  };
</script>
