<template>
  <div class="commonWh">
    <div style="width: 100%; height: 0" class="getSpanSize"></div>
    <div class="box-card">
      <div class="queryBox">
        <!--基础查询-->
        <div class="basicsQuery">
          <el-form :model="queryParams" ref="queryForm" label-width="100px">
            <el-form-item label="名称 : " prop="name">
              <el-input
                v-model="queryParams.name"
                placeholder="请输入名称"
                clearable
                size="mini"
                @keyup.enter.native="handleQuery"
              />
            </el-form-item>
            <el-form-item label="开始时间 : " prop="deployTime">
              <el-date-picker
                clearable
                size="mini"
                v-model="queryParams.deployTime"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="选择时间">
              </el-date-picker>
            </el-form-item>
            <el-form-item style="float: right;">
              <el-button type="primary" size="mini" @click="handleQuery">搜索</el-button>
              <el-button size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
      <div class="tableBox">
        <div class="tabTopButton">
          <el-button
            type="primary"
            size="mini"
            @click="handleAdd"
            v-hasPermi="['system:deployment:add']"
          >新增流程</el-button>
          <el-button
            size="mini"
            :disabled="multiple"
            type="danger"
            @click="handleDelete"
            v-hasPermi="['system:deployment:remove']"
          >删除</el-button>
          <!--<el-button-->
            <!--type="warning"-->
            <!--plain-->
            <!--size="mini"-->
            <!--@click="handleExport"-->
            <!--v-hasPermi="['system:deployment:export']"-->
          <!--&gt;导出</el-button>-->
        </div>
        <el-table
          ref="multipleTable"
          tooltip-effect="light"
          height="calc(100% - 82px)"
          v-loading="loading"
          :data="myProcessList"
          border
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column
            label="序号"
            width="80"
            align="center"
            :formatter="snFormatter"
          ></el-table-column>
          <!--<el-table-column label="流程编号" align="center" prop="procInsId" :show-overflow-tooltip="true"/>-->
          <el-table-column
            show-overflow-tooltip
            label="流程名称"
            align="center"
            min-width="300"
            prop="procDefName"/>
          <el-table-column
            show-overflow-tooltip
            label="流程类别"
            align="center"
            prop="category"
            min-width="150" />
          <el-table-column
            show-overflow-tooltip
            label="流程版本"
            align="center"
            min-width="100">
            <template slot-scope="scope">
              <el-tag size="medium" >v{{ scope.row.procDefVersion }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            label="提交时间"
            align="center"
            prop="createTime"
            min-width="160"/>
          <el-table-column
            show-overflow-tooltip
            label="流程状态"
            align="center"
            min-width="100">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.finishTime == null" size="mini">进行中</el-tag>
              <el-tag type="success" v-if="scope.row.finishTime != null" size="mini">已完成</el-tag>
            </template>
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            label="耗时"
            align="center"
            prop="duration"
            min-width="150"/>
          <el-table-column
            show-overflow-tooltip
            label="当前节点"
            align="center"
            min-width="150"
            prop="taskName"/>
          <el-table-column
            show-overflow-tooltip
            label="办理 "
            min-width="150"
            align="center">
            <template slot-scope="scope">
              <label v-if="scope.row.assigneeName">{{scope.row.assigneeName}}<el-tag type="info" size="mini">{{scope.row.deptName}}</el-tag></label>
              <label v-if="scope.row.candidate">{{scope.row.candidate}}</label>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" min-width="200">
            <template slot-scope="scope">
              <el-button size="mini" type="primary" @click.native="handleFlowRecord(scope.row)">详情</el-button>
              <el-button size="mini" type="primary" @click.native="handleStop(scope.row)">取消申请</el-button>
              <el-button size="mini" type="danger" @click.native="handleDelete(scope.row)" v-hasPermi="['system:deployment:remove']">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </div>
    </div>
    <!-- 发起流程 -->
    <el-dialog
      v-dialogDrag
      :title="title"
      :visible.sync="open"
      width="60vw"
      :modal="false"
      :close-on-click-modal="false"
    >
      <div style="height: 60vh">
        <el-form
          :model="queryProcessParams"
          ref="queryProcessForm"
          :inline="true"
          label-width="100px">
          <el-form-item label="名称 : " prop="name">
            <el-input
              v-model="queryProcessParams.name"
              placeholder="请输入名称"
              clearable
              size="mini"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item style="float: right">
            <el-button type="primary" size="mini" @click="handleProcessQuery">搜索</el-button>
            <el-button size="mini" @click="resetProcessQuery">重置</el-button>
          </el-form-item>
        </el-form>
        <el-table
          height="calc(100% - 80px)"
          v-loading="processLoading"
          fit
          tooltip-effect="light"
          :data="definitionList"
          border >
          <el-table-column label="流程名称" align="center" prop="name" min-width="300"/>
          <el-table-column label="流程版本" align="center" min-width="100">
            <template slot-scope="scope">
              <el-tag size="medium" >v{{ scope.row.version }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="流程分类" align="center" prop="category" min-width="300"/>
          <el-table-column label="操作" align="center" min-width="120">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="primary"
                @click="handleStartProcess(scope.row)"
              >发起流程</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          v-show="processTotal>0"
          :total="processTotal"
          :page.sync="queryProcessParams.pageNum"
          :limit.sync="queryProcessParams.pageSize"
          @pagination="listDefinition"
        />
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {
  getDeployment,
  delDeployment,
  addDeployment,
  updateDeployment,
  exportDeployment,
  flowRecord
} from "@/api/flowable/finished";
import { myProcessList,stopProcess } from "@/api/flowable/process";
import {listDefinition} from "@/api/flowable/definition";
export default {
  name: "/process",
  components: {
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      processLoading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      selectData:[],
      // 总条数
      total: 0,
      processTotal:0,
      // 我发起的流程列表数据
      myProcessList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      src: "",
      definitionList:[],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 15,
        name: null,
        category: null,
        key: null,
        tenantId: null,
        deployTime: null,
        derivedFrom: null,
        derivedFromRoot: null,
        parentDeploymentId: null,
        engineVersion: null
      },
      // 查询参数
      queryProcessParams: {
        pageNum: 1,
        pageSize: 15,
        name: null,
        category: null,
        key: null,
        tenantId: null,
        deployTime: null,
        derivedFrom: null,
        derivedFromRoot: null,
        parentDeploymentId: null,
        engineVersion: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      },
    };
  },
  watch: {
    //解决操作栏固定后表格错位问题
    'myProcessList': {
      handler() {
        this.$nextTick(() => {
          this.$refs.multipleTable.doLayout();
        })
      },
      deep: true
    },
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询流程定义列表 */
    getList() {
      this.loading = true;
      myProcessList(this.queryParams).then(response => {
        this.myProcessList = response.data.records;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        name: null,
        category: null,
        key: null,
        tenantId: null,
        deployTime: null,
        derivedFrom: null,
        derivedFromRoot: null,
        parentDeploymentId: null,
        engineVersion: null
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
    /** 搜索按钮操作 */
    handleProcessQuery() {
      this.queryProcessParams.pageNum = 1;
      this.listDefinition();
    },
    /** 重置按钮操作 */
    resetProcessQuery() {
      this.resetForm("queryProcessForm");
      this.handleProcessQuery();
    },
    /* 格式序列化列 */
    snFormatter: function (row, col, value, index) {
      return this.queryParams.pageSize * (this.queryParams.pageNum - 1) + index + 1;
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.selectData=selection
      this.ids = selection.map(item => item.procInsId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.open = true;
      this.title = "发起流程";
      this.listDefinition();
    },
    listDefinition(){
      listDefinition(this.queryProcessParams).then(response => {
        this.definitionList = response.data.records;
        this.processTotal = response.data.total;
        this.processLoading = false;
      });
    },
    /**  发起流程申请 */
    handleStartProcess(row){
      this.$router.push({ path: '/flowable/task/record/index/'+row.id,
        query: {
          deployId: row.deploymentId,
          procDefId:row.id,
          finished: true,
          flowCategory: row.category
          }
      })
    },
    /**  取消流程申请 */
    handleStop(row){
      const params = {
        instanceId: row.taskId,
        taskId: row.procInsId,
        type:row.category
      }
      stopProcess(params).then( res => {
        this.msgSuccess(res.msg);
        this.getList();
      });
    },
    /** 流程流转记录 */
    handleFlowRecord(row){
      this.$router.push({ path: '/flowable/task/record/index/'+row.taskId,
        query: {
          procInsId: row.procInsId,
          deployId: row.deployId,
          taskId: row.taskId,
          finished: false
      }})
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getDeployment(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改流程定义";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateDeployment(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDeployment(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.procInsId || this.ids;// 暂不支持删除多个流程
      const datas=this.selectData
      this.$confirm('是否确认删除数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        //return delDeployment(ids);
        if(row.procInsId){
          return delDeployment(ids);
        }else{
          for (let i = datas.length-1; i >=0; i--) {
            if(i===0){
              return delDeployment(datas[i].procInsId)
            }else{
              delDeployment(datas[i].procInsId)
            }
          }
        }
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有流程定义数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return exportDeployment(queryParams);
      }).then(response => {
        this.download(response.msg);
      })
    }
  }
};
</script>

