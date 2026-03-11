<template>
  <div class="app-container">
    <!--搜索行-->
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
        <el-form-item label="任务组名称" prop="jobGroupName">
          <el-input
            v-model="queryParams.jobGroupName"
            placeholder="请输入任务组名称"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="任务状态" prop="status">
          <el-select v-model="queryParams.status" placeholder="请选择任务状态" clearable>
            <el-option
              v-for="item in statusList"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item style="float: right">
          <el-button type="info" plain icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" plain size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    <!--按钮行-->
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              icon="el-icon-plus"
              size="mini"
              @click="handleAdd"
              v-hasPermi="['monitor:job:add']"
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
              v-hasPermi="['monitor:job:remove']"
            >删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="warning"
              plain
              icon="el-icon-download"
              size="mini"
              @click="handleExport"
              v-hasPermi="['monitor:job:export']"
            >导出</el-button>
          </el-col>
        </el-row>
    <!--任务组管理的表格-->
        <el-table v-loading="loading" :data="jobList" @selection-change="handleSelectionChange" height="750px">
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="序号" align="center" :formatter="snFormatter1"/>
          <el-table-column label="任务组名" align="center" prop="jobGroupName">
            <template slot-scope="scope">
              <router-link :to="('/ysjgl/rwzgl-data/index/' + scope.row.jobGroupId)" class="link-type">
                <span>{{ scope.row.jobGroupName }}</span>
              </router-link>
            </template>
          </el-table-column>
          <el-table-column label="状态" align="center">
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.status"
                active-value="0"
                inactive-value="1"
                @change="handleStatusChange(scope.row)"
              ></el-switch>
            </template>
          </el-table-column>
          <el-table-column label="cron执行表达式" align="center" prop="cronExpression" :show-overflow-tooltip="true" />
          <el-table-column label="最近一次执行情况" align="center">
            <el-table-column label="任务总数" align="center" prop="total"></el-table-column>
            <el-table-column label="成功数" align="center" prop="synchronizedQuantity"></el-table-column>
            <el-table-column label="失败数" align="center" prop="numberFailures"></el-table-column>
            <el-table-column label="执行时间" align="center" prop="lastRunTime"></el-table-column>
          </el-table-column>

          <el-table-column label="修改时间" align="center" prop="updateTime"></el-table-column>
          <el-table-column label="创建时间" align="center" prop="createTime"></el-table-column>
          <el-table-column label="备注" align="center" prop="remark" show-overflow-tooltip></el-table-column>
          <el-table-column label="日志" align="center">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-s-help"
                @click="handleView(scope.row,'recent')"
                v-hasPermi="['monitor:job:edit']"
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
                v-hasPermi="['monitor:job:edit']"
              >编辑</el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['system:dict:remove']"
              >删除</el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-s-platform"
                @click="handleRun(scope.row)"
                v-hasPermi="['monitor:job:edit']"
              >执行一次</el-button>
              <!-- <el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)" v-hasPermi="['monitor:job:changeStatus', 'monitor:job:query']">
                <span class="el-dropdown-link">
                  <i class="el-icon-d-arrow-right el-icon--right"></i>更多
                </span>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="handleRun" icon="el-icon-caret-right"
                    v-hasPermi="['monitor:job:changeStatus']">执行一次</el-dropdown-item>
                  <el-dropdown-item command="handleView" icon="el-icon-view"
                    v-hasPermi="['monitor:job:query']">任务详细</el-dropdown-item>
                  <el-dropdown-item command="handleJobLog" icon="el-icon-s-operation"
                    v-hasPermi="['monitor:job:query']">调度日志</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown> -->
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


    <!-- 添加或修改定时任务对话框 -->
    <el-dialog :title="title" close-on-click-modal="false" :visible.sync="open" width="800px" append-to-body style="margin-top:140px" :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="任务组名称" prop="jobName">
              <el-input v-model="form.jobGroupName" placeholder="请输入任务组名称" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="状态">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="item in statusList"
                  :key="item.value"
                  :label="item.value"
                >{{item.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
          </el-col>
          <el-col :span="24">
            <el-form-item label="cron表达式" prop="cronExpression">
              <el-input v-model="form.cronExpression" placeholder="请输入cron执行表达式">
                <template slot="append">
                  <el-button type="primary" @click="handleShowCron">
                    生成表达式
                    <i class="el-icon-time el-icon--right"></i>
                  </el-button>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input type="textarea" v-model="form.remark"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="Cron表达式生成器" close-on-click-modal="false" :visible.sync="openCron" append-to-body destroy-on-close class="scrollbar" :close-on-click-modal="false" >
      <crontab @hide="openCron=false" @fill="crontabFill" :expression="expression"></crontab>
    </el-dialog>

    <!-- 查看任务组日志 -->
    <el-dialog title="查看任务组日志" close-on-click-modal="false" :visible.sync="openView" width="68%" append-to-body :close-on-click-modal="false" class="rwzDialog">
      <el-tabs v-model="logView" type="card" @tab-click="handleView">
        <el-tab-pane label="最近一次" name="recent">
          <el-form :model="searchParams" ref="searchParams" size="small" :inline="true"  label-width="88px" style="margin-top:10px">
              <el-form-item label="执行状态" >
                <el-select
                  v-model="searchParams.status"
                  placeholder="请选择执行状态"
                  clearable
                  @keyup.enter.native="handleView">
                  <el-option label="成功" key="0" value="0"></el-option>
                  <el-option label="失败" key="1" value="1"></el-option>
                  </el-select>
              </el-form-item>
              <el-form-item label="异常信息" style="margin-left:42px">
                <el-select
                  v-model="searchParams.exceptionInfo"
                  placeholder="请选择异常信息"
                  clearable
                  @keyup.enter.native="handleView">
                  <el-option label="正常" key="0" value="0"></el-option>
                  <el-option label="失败" key="1" value="1"></el-option>
                  </el-select>
              </el-form-item>
              <el-form-item label="未同步数据" style="margin-left:42px">
                <el-select
                  v-model="searchParams.unsynchronizedQuantity"
                  placeholder="请选择未同步数据"
                  clearable
                  @keyup.enter.native="handleView">
                  <el-option label="正常" key="0" value="0"></el-option>
                  <el-option label="失败" key="1" value="1"></el-option>
                  </el-select>
              </el-form-item>
            <el-form-item label="耗时(s)" prop="status" >
              <el-input
                  v-model="searchParams.startTimeConsuming"
                  clearable
                  style="width:120px;margin-right:5px"
                  @keyup.enter.native="handleView"
                />
                <span>-</span>
                <el-input
                  v-model="searchParams.stopTimeConsuming"
                  clearable
                  style="width:120px;margin-left:5px"
                  @keyup.enter.native="handleView"
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
              <el-button type="info" plain icon="el-icon-search" size="mini" @click="handleView">搜索</el-button>
              <el-button icon="el-icon-refresh" plain size="mini" @click="resetSearchQuery">重置</el-button>
              <el-button type="info" icon="el-icon-refresh" size="mini" plain @click="handleView" >刷新</el-button>
            </el-form-item>
          </el-form>
          <el-table v-loading="recentloading" :data="recentJobGroupList" height="450px">
            <el-table-column label="序号" align="center" :formatter="snFormatter"/>
            <el-table-column label="任务名称" align="center" prop="jobName"></el-table-column>
            <el-table-column label="所属类目" align="center" prop="dbName" />
            <el-table-column label="元模型名称" align="center" prop="tableName" />
            <el-table-column label="总数据" align="center" prop="total"></el-table-column>
            <el-table-column label="已同步数据" align="center" prop="synchronizedQuantity"></el-table-column>
            <el-table-column label="未同步数据" align="center" prop="unsynchronizedQuantity"></el-table-column>
            <el-table-column label="执行状态" align="center" prop="status">
              <template slot-scope="scope">
                <span v-if="scope.row.status==='0'||scope.row.status===0">成功</span>
                <span v-if="scope.row.status==='1'||scope.row.status===1">失败</span>
              </template>
            </el-table-column>
            <el-table-column label="异常信息" align="center"  prop="exceptionInfo" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <el-button type="text" size="mini" @click="errorLog(scope.row)">{{ scope.row.exceptionInfo }}</el-button>
                 </template>
            </el-table-column>
            <el-table-column label="执行时间" align="center" prop="createTime"></el-table-column>
            <el-table-column label="耗时（s）" align="center" prop="jobMessage"></el-table-column>
          </el-table>
          <pagination
            v-show="totalRecent>=0"
            :total="totalRecent"
            :page.sync="pageNum"
            :limit.sync="pageSize"
            @pagination="handleView"
            style="margin-bottom:20px"
          />
        </el-tab-pane>
        <el-tab-pane label="历史" name="history">
          <el-row :gutter="20">
            <el-col :span="6" >
              <div class="head-container">
                <el-input
                  v-model="dbName"
                  placeholder="请输入任务名称"
                  clearable
                  size="small"
                  prefix-icon="el-icon-search"
                  style="margin-bottom: 20px"
                />
              </div>
              <div class="head-container">
                <el-tree
                  class="filter-tree"
                  :data="deptOptions"
                  highlight-current
                  default-expand-all
                  :filter-node-method="filterNode"
                  ref="tree" style="font-size: 14px;height:54vh;overflow-y: auto;">
                        <span style="width: 100%;height:100%;display: block" class="custom-tree-node" slot-scope="{ node, data }">
                            <span style="width: 100%;display: block;line-height: 25px" @click="handleNodeClick(data.jobId , data.jobName)">
                                <i :class="data.icon" :style="data.color"></i>{{data.label }}
                            </span>
                        </span>
                </el-tree>
              </div>
            </el-col>
            <el-col :span="18" >
              <el-form :model="searchParams" ref="searchParams" size="small" :inline="true"  label-width="88px" style="margin-top:10px">
              <el-form-item label="执行状态" >
                <el-select
                  v-model="searchParams.status"
                  placeholder="请选择执行状态"
                  clearable
                  @keyup.enter.native="handleView">
                  <el-option label="成功" key="0" value="0"></el-option>
                  <el-option label="失败" key="1" value="1"></el-option>
                  </el-select>
              </el-form-item>
              <el-form-item label="异常信息" style="margin-left:42px">
                <el-select
                  v-model="searchParams.exceptionInfo"
                  placeholder="请选择异常信息"
                  clearable
                  @keyup.enter.native="handleView">
                  <el-option label="正常" key="0" value="0"></el-option>
                  <el-option label="失败" key="1" value="1"></el-option>
                  </el-select>
              </el-form-item>
              <el-form-item label="未同步数据" style="margin-left:42px">
                <el-select
                  v-model="searchParams.unsynchronizedQuantity"
                  placeholder="请选择未同步数据"
                  clearable
                  @keyup.enter.native="handleView">
                  <el-option label="正常" key="0" value="0"></el-option>
                  <el-option label="失败" key="1" value="1"></el-option>
                  </el-select>
              </el-form-item>
            <el-form-item label="耗时(s)" prop="status" >
              <el-input
                  v-model="searchParams.startTimeConsuming"
                  clearable
                  style="width:120px;margin-right:5px"
                  @keyup.enter.native="handleView"
                />
                <span>-</span>
                <el-input
                  v-model="searchParams.stopTimeConsuming"
                  clearable
                  style="width:120px;margin-left:5px"
                  @keyup.enter.native="handleView"
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
              <el-button type="info" plain icon="el-icon-search" size="mini" @click="handleView">搜索</el-button>
              <el-button icon="el-icon-refresh" plain size="mini" @click="resetSearchQuery">重置</el-button>
              <el-button type="info" icon="el-icon-refresh" size="mini" plain @click="handleView" >刷新</el-button>
            </el-form-item>
              </el-form>

              <el-table v-loading="historyloading" :data="historyJobGroupList" height="400px">
                <el-table-column label="序号" align="center" :formatter="snFormatter"/>
                <el-table-column label="任务名称" align="center" prop="jobName"></el-table-column>
                <el-table-column label="所属类目" align="center" prop="dbName" />
                <el-table-column label="元模型名称" align="center" prop="tableName" />
                <el-table-column label="总数据" align="center" prop="total"></el-table-column>
                <el-table-column label="已同步数据" align="center" prop="synchronizedQuantity"></el-table-column>
                <el-table-column label="未同步数据" align="center" prop="unsynchronizedQuantity"></el-table-column>
                <el-table-column label="执行状态" align="center" prop="status">
                  <template slot-scope="scope">
                    <span v-if="scope.row.status==='0'||scope.row.status===0">成功</span>
                    <span v-if="scope.row.status==='1'||scope.row.status===1">失败</span>
                  </template>
                </el-table-column>
                <el-table-column label="异常信息" align="center"  prop="exceptionInfo" :show-overflow-tooltip="true">
                  <template slot-scope="scope">
                    <el-button type="text" size="mini" @click="errorLog(scope.row)">{{ scope.row.exceptionInfo }}</el-button>
                  </template>
                </el-table-column>
                <el-table-column label="执行时间" align="center" prop="lastExecutionTime"></el-table-column>
                <el-table-column label="耗时（s）" align="center" prop="jobMessage"></el-table-column>
              </el-table>
              <pagination
                v-show="totalHistory>=0"
                :total="totalHistory"
                :page.sync="pageNum"
                :limit.sync="pageSize"
                @pagination="handleView"
                style="margin-bottom:20px"
              />
            </el-col>
          </el-row>
        </el-tab-pane>

      </el-tabs>
      <div slot="footer" class="dialog-footer" style="height:20px">
        <el-button @click="openView = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!--查看异常信息日志-->
    <el-dialog title="查看异常信息" close-on-click-modal="false" :visible.sync="openErrorLog" width="68%" append-to-body  :close-on-click-modal="false">
      <el-form :model="errorQueryParams" ref="errorQueryParams" size="small" :inline="true" v-show="showSearch" label-width="88px">
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
  import { listGroupJob, changeStatus,runJobGroup, addJob, updateJob, getRecentGroup, getHistoryGroup, getGroupId, getJobList, delJobGroup, getErrorLogList, getGroupInfo } from "@/api/sjygl/rwzgl";
  import { listJob, getJob, delJob, runJob, changeJobStatus } from "@/api/monitor/job";
  import Crontab from '@/components/Crontab'

  export default {
    components: { Crontab },
    name: "Rwzgl",
    data() {
      return {
        dbName:'',
        //状态搜索
        statusList: [
          {label:'正常',value:'0'},
          {label:'暂停',value:'1'}
        ],
        // 树选项
        deptOptions: [{
          id: '',
          label: '',
          icon: 'el-icon-s-home',
          color: 'color: #FFC000',
          children: []
        }],
        // 日志搜索列表
        searchParams: {
          status: '',
          exceptionInfo: '',
          lastExecutionTime: '',
          startTimeConsuming: '',
          stopTimeConsuming: '',
          unsynchronizedQuantity: ''
        },
        //查看任务组日志tabs页
        logView: '',
        // 遮罩层
        errorLogLoading: true,
        loading: true,
        historyloading: true,
        recentloading: true,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        // 总条数
        total: 0,
        totalRecent: 0,
        totalHistory: 0,
        totalError: 0,
        pageNum:1,
        pageSize:10,
        // 定时任务表格数据
        jobList: [
          {jobGroup:'aaa'}
        ],
        //资源树任务组id
        groupId: '',
        // 异常信息日志
        errorLogList: [],
        //最近一次任务组日志
        recentJobGroupList:[],
        //任务组日志历史
        historyJobGroupList:[],
        // 弹出层标题
        title: "",
        // 是否显示异常信息弹出层
        openErrorLog: false,
        // 是否显示弹出层
        open: false,
        // 是否显示详细弹出层
        openView: false,
        // 是否显示Cron表达式弹出层
        openCron: false,
        // 传入的表达式
        expression: "",
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          jobGroupName: undefined,
          status: undefined
        },
        logId:'',
        //异常信息
        errorQueryParams: {
          errorId: '',
          errorMessage: '',
          errorType: ''
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          jobGroupName: [
            { required: true, message: "任务名称不能为空", trigger: "blur" }
          ],
          cronExpression: [
            { required: true, message: "cron执行表达式不能为空", trigger: "change" }
          ]
        }
      };
    },
    watch: {
      // 根据名称筛选树
      dbName(val) {
        this.$refs.tree.filter(val);
      }
    },
    created() {
      this.getList();
    },
    methods: {
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
      getSjyTree(id) {
        getGroupId(id).then(res =>{
          res = res.data
          this.deptOptions[0].id = res.jobGroupId
          this.deptOptions[0].label = res.jobGroupName
          var jobParams = {
            jobGroupId: id
          }
          getJobList(jobParams).then(res =>{
            res = res.data
            for (let i = 0; i < res.length; i++) {
              res[i].label=res[i].jobName+"（"+res[i].dbName+" . "+res[i].tableName+"）"
            }
            this.deptOptions[0].children = res
          })
        })

      },
      // 筛选节点
      filterNode(value, data) {
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      },
      // 节点单击事件
      handleNodeClick(id, name) {
        this.handleView('','',id)
      },
      /* 格式序列化列 */
      snFormatter: function (row, col, value, index) {
        return this.pageSize * (this.pageNum - 1) + index + 1;
        /* return index + 1 */
      },
      snFormatter1: function (row, col, value, index) {
        return this.queryParams.pageSize * (this.queryParams.pageNum - 1) + index + 1;
        /* return index + 1 */
      },
      /** 查询定时任务列表 */
      getList() {
        this.loading = true;
        listGroupJob(this.queryParams).then(response => {
          this.jobList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      // 任务组名字典翻译
      /* jobGroupFormat(row, column) {
        return this.selectDictLabel(this.dict.type.sys_job_group, row.jobGroup);
      }, */
      // 取消按钮
      cancel() {
        this.open = false;
        this.reset();
        this.getList()
      },
      // 表单重置
      reset() {
        this.form = {
          jobGroupId: undefined,
          jobGroupName: undefined,
          cronExpression: undefined,
          status: "0"
        };
        this.resetForm("form");
      },
      /** 搜索按钮操作 */
      handleQuery() {
        this.queryParams.pageNum = 1;
        this.getList();
      },
      /** 重置按钮操作 */
      resetQuery() {
        this.resetForm("queryForm");
        this.handleQuery();
      },
      /** 重置按钮操作 */
      resetQueryError() {
        this.errorQueryParams.errorId = undefined;
        this.errorQueryParams.errorMessage = undefined;
        this.errorQueryParams.errorType = undefined;
        this.getErrorLog();
      },
      // 多选框选中数据
      handleSelectionChange(selection) {
        this.ids = selection.map(item => item.jobGroupId);
        this.single = selection.length != 1;
        this.multiple = !selection.length;
      },
      // 更多操作触发
      /* handleCommand(command, row) {
        switch (command) {
          case "handleRun":
            this.handleRun(row);
            break;
          case "handleView":
            this.handleView(row);
            break;
          case "handleJobLog":
            this.handleJobLog(row);
            break;
          default:
            break;
        }
      }, */
      // 任务状态修改
      handleStatusChange(row) {
        let text = row.status === "0" ? "启用" : "停用";
        this.$modal.confirm('确认要"' + text + '""' + row.jobGroupName + '"任务吗？').then(function() {
          return changeStatus(row.jobGroupId, row.status);
        }).then(() => {
          this.$modal.msgSuccess(text + "成功");
        }).catch(function() {
          row.status = row.status === "0" ? "1" : "0";
        });
      },
      /* 立即执行一次 */
      handleRun(row) {
        this.$modal.confirm('确认要立即执行一次"' + row.jobGroupName + '"任务吗？').then(function() {
          return runJobGroup(row.jobGroupId);
        }).then(() => {
          this.$modal.msgSuccess("执行成功");
        }).catch(() => {});
      },
      // 重置任务日志查询
      resetSearchQuery() {
          this.searchParams.status = '';
          this.searchParams.exceptionInfo = '';
          this.searchParams.lastExecutionTime = '';
          this.searchParams.startTimeConsuming = '';
          this.searchParams.stopTimeConsuming = '';
          this.searchParams.unsynchronizedQuantity = '';
          this.handleView('','recent','')
      },
      /** 任务详细信息 */
      handleView(row, type, id) {
        if(row.jobGroupId !== undefined) {
          this.groupId = row.jobGroupId
        }
        this.recentJobGroupList = []
        this.historyJobGroupList = []
        if(type==='recent'||this.logView==='recent'){
          this.logView = 'recent';
          this.recentloading = true;
          var paramsRecent = {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            jobGroupId: this.groupId,
            status: this.searchParams.status,
            exceptionInfo: this.searchParams.exceptionInfo,
            unsynchronizedQuantity: this.searchParams.unsynchronizedQuantity,
            startTimeConsuming: this.searchParams.startTimeConsuming,
            stopTimeConsuming: this.searchParams.stopTimeConsuming,
          }
          if(this.searchParams.lastExecutionTime.length>0) {
            paramsRecent.startTime = this.searchParams.lastExecutionTime[0];
            paramsRecent.stopTime = this.searchParams.lastExecutionTime[1]
          }
          getRecentGroup(paramsRecent).then(response => {
            this.recentJobGroupList = response.rows;
            this.recentloading = false;
            /* this.recentJobGroupList[0].exceptionInfo = '12313' */
            this.totalRecent = response.total;
          });
        } else {
          /* this.logView = 'history'; */
          this.getSjyTree(this.groupId)
          this.historyloading = true;
          var paramsHis = {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            jobGroupId: this.groupId,
            jobId: id,
            status: this.searchParams.status,
            exceptionInfo: this.searchParams.exceptionInfo,
            startTimeConsuming: this.searchParams.startTimeConsuming,
            stopTimeConsuming: this.searchParams.stopTimeConsuming,
            unsynchronizedQuantity: this.searchParams.unsynchronizedQuantity,
          }
          if(this.searchParams.lastExecutionTime.length>0) {
            params.startTime = this.searchParams.lastExecutionTime[0];
            params.stopTime = this.searchParams.lastExecutionTime[1]
          }
          getHistoryGroup(paramsHis).then(response => {
            this.historyJobGroupList = response.rows;
            this.historyloading = false;
            /* this.historyJobGroupList[0].exceptionInfo = '123' */
            this.totalHistory = response.total;
          });
        }
        this.openView = true;
        /* getJob(row.jobId).then(response => {
          this.form = response.data;

        }); */
      },
      /** cron表达式按钮操作 */
      handleShowCron() {
        this.expression = this.form.cronExpression;
        this.openCron = true;
      },
      /** 确定后回传值 */
      crontabFill(value) {
        this.form.cronExpression = value;
      },
      /** 任务日志列表查询 */
      /* handleJobLog(row) {
        const jobId = row.jobId || 0;
        this.$router.push({ path: '/monitor/job-log/index', query: { jobId: jobId } })
      }, */
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "任务组新增";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        /* console.log(row) */
        this.reset();
        getGroupInfo(row.jobGroupId).then(response => {
          this.form = response.data
          this.open = true;
          this.title = "修改任务";
        });
      },
      /** 提交按钮 */
      submitForm: function() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.jobGroupId != undefined) {
              updateJob(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addJob(this.form).then(response => {
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
        const jobIds = row.jobGroupId || this.ids;
        this.$modal.confirm('是否确认删除定时任务编号为"' + jobIds + '"的数据项？').then(function() {
          return delJobGroup(jobIds)
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {});
      },
      /** 导出按钮操作 */
      handleExport() {
        this.$modal.confirm('是否确认导出数据？').then(function() {

        }).then(() => {
          this.download('schedule/group/export', {
            ...this.queryParams
          }, `data_${new Date().getTime()}.xlsx`)
        }).then(() => {
          this.$modal.msgSuccess("导出成功");
        }).catch(() => {});

      }
    }
  };
</script>
<style lang="stylus" rel="stylesheet/stylus"  scoped>
  .el-input-group__append button.el-button, .el-input-group__append div.el-select .el-input__inner, .el-input-group__append div.el-select:hover .el-input__inner, .el-input-group__prepend button.el-button, .el-input-group__prepend div.el-select .el-input__inner, .el-input-group__prepend div.el-select:hover .el-input__inner{
    border-color:#46a6ff !important;
    background-color:#46a6ff !important;
    color: #fff;
  }

  .el-form-item__inner{
    width:100px !important
  }
  .rwzDialog .el-dialog {
    padding:0 !important
  }

</style>
