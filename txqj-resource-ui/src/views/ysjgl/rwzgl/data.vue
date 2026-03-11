<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="68px">
      <el-form-item label="任务组名">
        <el-input v-model="queryParams.jobGroup" disabled></el-input>
      </el-form-item>
      <el-form-item label="任务名称" prop="jobName">
        <el-input v-model="queryParams.jobName"></el-input>
      </el-form-item>

      <el-form-item style="float: right">
        <el-button type="info" icon="el-icon-search" size="mini" plain @click="handleQuery">搜索</el-button>
        <el-button plain icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        <el-button
        type="warning"
        plain
        icon="el-icon-close"
        size="mini"
        @click="handleClose"
      >返回</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:dict:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:dict:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:dict:export']"
        >导出</el-button>
      </el-col>
      <!--<el-col :span="1.5">-->
        <!--<el-button-->
          <!--type="warning"-->
          <!--plain-->
          <!--icon="el-icon-close"-->
          <!--size="mini"-->
          <!--@click="handleClose"-->
        <!--&gt;返回</el-button>-->
      <!--</el-col>-->
    </el-row>

    <el-table v-loading="loading" :data="dataList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" :formatter="snFormatter" />
      <el-table-column label="任务名称" align="center" prop="jobName">
      </el-table-column>
      <el-table-column label="任务组名" align="center" prop="jobGroup" />
      <el-table-column label="所属类目" align="center" prop="dbName" />
      <el-table-column label="元模型名称" align="center" prop="tableName" />
      <el-table-column label="下次执行时间" align="center" prop="nextValidTime" />
      <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
      <el-table-column label="修改时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="日志" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-s-help"
            @click="handleLog(scope.row)"
            v-hasPermi="['system:dict:edit']"
          >查看</el-button>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:dict:edit']"
          >编辑</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:dict:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog @close="close" :close-on-click-modal="false" :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="130px">
        <el-form-item label="任务名称" prop="jobName">
          <el-input v-model="form.jobName" placeholder="请输入任务名称" />
        </el-form-item>
        <el-form-item label="任务组名">
          <el-input v-model="form.jobGroup" :disabled="true" />
        </el-form-item>
        <el-form-item label="所属类目" prop="dbId" >
          <el-select v-model="form.dbId" placeholder="请选择" @change="getTableList" filterable :disabled="isEdit">
            <el-option
              v-for="item in dbList"
              :key="item.id"
              :label="item.dbNameCn"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="元模型名称" prop="tableId" >
          <el-select v-model="form.tableId" placeholder="请选择" @change="getConditionList" filterable :disabled="isEdit">
            <!--<el-option
              v-for="item in tableList"
              :key="item.id"
              :label="item.tableName"
              :value="item.id">
            </el-option>-->
            <el-option-group
              v-for="group in tableList"
              :key="group.label"
              :label="group.label">
              <el-option
                v-for="item in group.list"
                :key="item.id"
                :label="item.tableName"
                :value="item.id"
                :disabled="item.disabled"
                >
              </el-option>
            </el-option-group>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 查看日志 -->
    <el-dialog :close-on-click-modal="false" title="查看日志" :visible.sync="openLog" width="65%" append-to-body>
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
              <el-form-item label="异常信息" style="margin-left:35px">
                <el-select
                  v-model="searchParams.exceptionInfo"
                  placeholder="请选择异常信息"
                  clearable
                  @keyup.enter.native="getLogList">
                  <el-option label="正常" key="0" value="0"></el-option>
                  <el-option label="失败" key="1" value="1"></el-option>
                  </el-select>
              </el-form-item>
              <el-form-item label="未同步数据" style="margin-left:35px">
                <el-select
                  v-model="searchParams.unsynchronizedQuantity"
                  placeholder="请选择未同步数据"
                  clearable
                  @keyup.enter.native="getLogList">
                  <el-option label="正常" key="0" value="0"></el-option>
                  <el-option label="失败" key="1" value="1"></el-option>
                  </el-select>
              </el-form-item>
            <el-form-item label="耗时(s)" prop="status" >
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
            <el-form-item style="float: right">
              <el-button type="info" plain icon="el-icon-search" size="mini" @click="getLogList">搜索</el-button>
              <el-button icon="el-icon-refresh" plain size="mini" @click="resetSearchQuery">重置</el-button>
              <el-button type="info" icon="el-icon-refresh" size="mini" plain @click="getLogList" >刷新</el-button>
            </el-form-item>
          </el-form>
      <el-table height="500" v-loading="loading" :data="logList">
        <el-table-column label="序号" align="center" :formatter="snLogFormatter" />
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
        v-show="logTotal>0"
        :total="logTotal"
        :page.sync="logParams.pageNum"
        :limit.sync="logParams.pageSize"
        @pagination="getLogList"
      />
      <div slot="footer" class="dialog-footer">
        <el-button @click="openLog=false">关 闭</el-button>
      </div>
    </el-dialog>
<!--查看异常信息日志-->
<el-dialog title="查看异常信息" close-on-click-modal="false" :visible.sync="openErrorLog" width="68%" append-to-body style="margin-top:20px">
  <el-form :model="errorQueryParams" ref="errorQueryParams" size="small" :inline="true" label-width="88px">
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
        <el-button @click="openErrorLog = false">关 闭</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
  import { getTableById,getDbById,getGroup,listJob,addJob,getById,updateJob,delJob,getDbTree,getTableTree,listLog,jobTableConditionList } from "@/api/rwzgl/rwgl";
  import { conditionadd } from "@/api/sjygl/cjysjgl";
  import { getErrorLogList } from "@/api/sjygl/rwzgl";

  export default {
    name: "Data",
    data() {
      return {
        totalError:'',
        // 修改是否禁填
        isEdit: false,
        // 错误日志id
        logId:'',
        // 错误日志查询
        errorQueryParams: {
          errorId: '',
          errorMessage: '',
          errorType: ''
        },
        // 遮罩层
        errorLogLoading: true,
        loading: true,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 总条数
        total: 0,
        logTotal: 0,
        // 任务表格数据
        dataList: [],
        // 默认任务组
        defaultGroup: {},
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        addopen:false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          jobGroup: undefined,
          jobName: undefined
        },
        jobId: '',
        // 日志搜索列表
        searchParams: {
          status: '',
          exceptionInfo: '',
          lastExecutionTime: '',
          startTimeConsuming: '',
          stopTimeConsuming: '',
          unsynchronizedQuantity: ''
        },
        // 表单参数
        form: {},
        //所属类目，元模型名称
        dbList:[],
        tableList:[],
        // 获取同步名称选项
        conditionList: [],
        treeList:[],
        // 表单校验
        rules: {
          jobName: [
            { required: true, message: "数据名称不能为空", trigger: "blur" }
          ],
          dbId: [
            { required: true, message: "请选择所属类目", trigger: "change" }
          ],
          tableId: [
            { required: true, message: "请选择元模型名称", trigger: "change" }
          ],
        },
        // 异常信息日志
        errorLogList: [],
        // 是否显示异常信息弹出层
        openErrorLog: false,
        //查看日志
        logList:[],
        openLog:false,
        logParams:{
          pageNum: 1,
          pageSize: 10
        },
        logTotal:0,
        addOneSet:{
          conditionName:undefined,
          searchCondition:undefined,
          deleteFlag:undefined
        },
        dbId:'',
        //添加任务中的新增按钮
        addCondition:true,
      //  添加任务同步名称title提示
        conditionName:'',
        tableRemark:'',
        selectDisabled:false
      };
    },
    created() {
      const groupId = this.$route.params && this.$route.params.jobGroupId;
      this.getGroupBy(groupId);

    },
    methods: {
      /** 根据条件备注获取条件配置 */
      setCondition() {
        /** 传回备注 获取数据 this.同步条件=xx this.是否原有数据=xx */
      },
     /** 重置按钮操作 */
     resetQueryError() {
        this.errorQueryParams.errorId = undefined;
        this.errorQueryParams.errorMessage = undefined;
        this.errorQueryParams.erroType = undefined;
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
      /** 查询任务类型详细 */
      getGroupBy(groupId) {
        getGroup(groupId).then(response => {
          this.queryParams.jobGroup = response.data.jobGroupName;
          this.defaultGroup = response.data;
          this.getList();
        });
      },
      /** 查询任务数据列表 */
      getList() {
        this.loading = true;
        listJob(this.queryParams).then(response => {
          this.dataList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      /* 格式序列化列 */
      snFormatter: function (row, col, value, index) {
        return this.queryParams.pageSize * (this.queryParams.pageNum - 1) + index + 1;
      },
      snLogFormatter: function (row, col, value, index) {
        return this.logParams.pageSize * (this.logParams.pageNum - 1) + index + 1;
      },
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
      },
      // 表单重置
      reset() {
        this.form = {
          jobName: undefined,
          invokeTarget: undefined,
          remark: undefined,
          dbId:undefined,
          dbName:undefined,
          tableId:undefined,
          tableName:undefined,
          conditionId:undefined,
          deleteFlag:undefined
        };
        this.tableRemark=''
        getDbTree().then(response => {
          this.dbList=response.data
          this.tableList=[]
        });
        this.resetForm("form");
        this.form.jobGroup=this.defaultGroup.jobGroupName
        this.form.jobGroupId=this.defaultGroup.jobGroupId
        this.form.cronExpression=this.defaultGroup.cronExpression
        this.form.status=this.defaultGroup.status
      },
      //选择数据源后获取其子集
      getTableList(){
        // this.form.tableId=undefined
        getTableTree({dbId:this.form.dbId}).then(response => {
          this.tableList=response.data
        });
        for (let i = 0; i < this.dbList.length; i++) {
          if(this.form.dbId===this.dbList[i].id){
            this.form.dbName=this.dbList[i].dbNameCn
          }
        }
      },
      /*选择源模型后*/
      getConditionList(){
        getTableById(this.form.tableId).then(res => {
          res=res.data
          this.tableRemark=res.remark
          this.dbId=res.dbId+'L'
          jobTableConditionList({tableId:res.id}).then(res=>{
            this.conditionList=res.data
          })
        });
        this.addCondition=false
        for(let j=0;j<this.tableList.length;j++){
          for (let i = 0; i < this.tableList[j].list.length; i++) {
            if(this.form.tableId===this.tableList[j].list[i].id){
              this.form.tableName=this.tableList[j].list[i].tableName
            }
          }
        }
      },
      /*选择源模型后填充字符串*/
      setParams(){
        for (let i=0;i<this.conditionList.length;i++) {
          if(this.form.conditionId==this.conditionList[i].id){
            this.form.deleteFlag=this.conditionList[i].deleteFlag
            this.form.searchCondition=this.conditionList[i].searchCondition
            this.conditionName=this.conditionList[i].conditionName
          }
        }
            getDbById(this.form.dbId).then(ress => {
              ress=ress.data
              let dbName=ress.dbUsername+'.'+this.form.tableName
              this.form.invokeTarget="ryTask.dataSynchronization("+this.dbId+",'"+dbName+"'"+",'"+this.form.searchCondition+"'"+",'"+this.form.deleteFlag+"')"
            });
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 返回按钮操作 */
      handleClose() {
        const obj = { path: "/ysjgl/rwzgl" };
        this.$tab.closeOpenPage(obj);
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.queryParams.jobGroup = this.defaultGroup.jobGroupName;
        this.handleQuery();
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.isEdit = false;
        this.title = "添加任务";
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.jobId)
        this.single = selection.length!=1
        this.multiple = !selection.length
      },
      /*查看日志操作*/
      handleLog(row){
          if(row.jobId !== undefined) {
            this.jobId = row.jobId
          }
        this.openLog = true;
        this.getLogList()
      },
      // 重置任务日志查询
      resetSearchQuery() {
          this.searchParams.status = '';
          this.searchParams.exceptionInfo = '';
          this.searchParams.lastExecutionTime = '';
          this.searchParams.startTimeConsuming = '';
          this.searchParams.stopTimeConsuming = '';
          this.searchParams.unsynchronizedQuantity = '';
          this.getLogList()
      },
      getLogList(){
        /* alert(this.jobId) */
        this.loading = true;
        this.logParams={
            pageNum: 1,
            pageSize: 10,
            jobId:this.jobId,
            status: this.searchParams.status,
            exceptionInfo: this.searchParams.exceptionInfo,
            unsynchronizedQuantity: this.searchParams.unsynchronizedQuantity,
            startTimeConsuming: this.searchParams.startTimeConsuming,
            stopTimeConsuming: this.searchParams.stopTimeConsuming,

          }
          if(this.searchParams.lastExecutionTime.length>0) {
            logParams.startTime = this.searchParams.lastExecutionTime[0];
            logParams.stopTime = this.searchParams.lastExecutionTime[1]
          }
        listLog(this.logParams).then(response => {
          this.logList = response.rows;
          /* this.logList[1].exceptionInfo = '123' */
          this.logTotal = response.total;
          this.loading = false;
        });
      },
      /** 修改按钮操作 */
      async handleUpdate(row) {
        this.reset();
        getById(row.jobId).then(response => {
          this.form.dbId = response.data.dbId;
          this.form.tableId=response.data.tableId
          // this.isEdit = true;
         this.getTableList()
          this.getConditionList()
          // this.setParams()
          setTimeout(()=>{
            this.form=response.data
          },1000)

          this.open = true;
          this.title = "修改任务";
        });
      },
      close(){
        this.addCondition=true
        this.conditionList=[]
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.jobId != undefined) {
              updateJob(this.form).then(response => {
                //this.$store.dispatch('dict/removeDict', this.queryParams.dictType);
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addJob(this.form).then(response => {
                //this.$store.dispatch('dict/removeDict', this.queryParams.dictType);
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              });
            }
          }
        });
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        const dictCodes = row.jobId || this.ids;
        this.$modal.confirm('是否确认删除选择的的数据项？').then(function() {
          return delJob(dictCodes);
        }).then(() => {
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
          //this.$store.dispatch('dict/removeDict', this.queryParams.dictType);
        }).catch(() => {});
      },
      /** 导出按钮操作 */
      handleExport() {
        this.$modal.confirm('是否确认导出数据？').then(function() {

        }).then(() => {
          this.download('schedule/job/export', {
            ...this.queryParams
          }, `data_${new Date().getTime()}.xlsx`)
        }).then(() => {
          this.$modal.msgSuccess("导出成功");
        }).catch(() => {});

      },
      /** 新增任务新增同步条件*/
      receive(data){
        this.addopen=data
      },
      openadd(){
        if(this.tableRemark=='table_no_key'){
          this.selectDisabled=true
        }
        this.addopen=true
      },
      submitOneSet(data){
        this.addOneSet=data
          let params={
            conditionName:this.addOneSet.conditionName,
            deleteFlag:this.addOneSet.deleteFlag,
            searchCondition:this.addOneSet.searchCondition,
            tableId:this.form.tableId
          }
          conditionadd(params).then(res=>{
            if(res.code=='200'){
              this.$message.success('新增成功');
              this.getConditionList()
              this.addopen=false
            }
          })
      },
    }
  };
</script>
<style scoped>
  #chooseMeta .el-input,#chooseMeta .el-input_inner{
    width: 340px;
  }
</style>
