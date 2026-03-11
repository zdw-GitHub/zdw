<template>
  <div class="commonWh">
    <div style="width: 100%; height: 0" class="getSpanSize"></div>
    <div class="box-card">
      <div class="queryBox">
        <!--基础查询-->
        <div class="basicsQuery">
          <el-form :model="queryParams" ref="queryForm" label-width="100px">
            <el-form-item label="名称 : " prop="taskName">
              <el-input
                v-model="queryParams.taskName"
                placeholder="请输入节点名称"
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
            size="mini"
            type="danger"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['system:deployment:remove']"
          >删除</el-button>
        </div>
        <el-table
          tooltip-effect="light"
          stripe
          height="calc(100% - 82px)"
          style="width: 100%;"
          v-loading="loading"
          :data="finishedList"
          border
          @selection-change="handleSelectionChange">
          <el-table-column
            type="selection"
            width="50"
            align="center"
          />
          <el-table-column
            label="序号"
            width="80"
            align="center"
            :formatter="snFormatter"
          ></el-table-column>
          <!--<el-table-column label="任务编号" align="center" prop="taskId" :show-overflow-tooltip="true"/>-->
          <el-table-column
            label="流程名称"
            align="center"
            prop="procDefName"
            show-overflow-tooltip
            min-width="300"
          />
          <el-table-column
            label="任务节点"
            align="center"
            prop="taskName"
            min-width="300"
            show-overflow-tooltip
          />
          <el-table-column
            label="流程发起人"
            align="center"
            min-width="200"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              <label>{{scope.row.startUserName}} <el-tag type="info" size="mini">{{scope.row.startDeptName}}</el-tag></label>
            </template>
          </el-table-column>
          <el-table-column
            label="接收时间"
            align="center"
            prop="createTime"
            min-width="160"
            show-overflow-tooltip
          />
          <el-table-column
            label="审批时间"
            align="center"
            prop="finishTime"
            min-width="160"
            show-overflow-tooltip
          />
          <el-table-column
            label="耗时"
            align="center"
            prop="duration"
            min-width="150"
            show-overflow-tooltip
          />
          <el-table-column
            label="操作"
            align="center"
            width="150"
          >
            <template slot-scope="scope">
              <el-button size="mini" type="primary" @click="handleFlowRecord(scope.row)">流转记录</el-button>
              <el-button size="mini" type="primary" @click="handleRevoke(scope.row)">撤回</el-button>
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
  </div>
</template>

<script>
import { finishedList, getDeployment, delDeployment, addDeployment, updateDeployment, exportDeployment, revokeProcess } from "@/api/flowable/finished";

export default {
  name: "/finished",
  components: {
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      selectData:[],
      // 总条数
      total: 0,
      // 已办任务列表数据
      finishedList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      src: "",
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 15,
        taskName: null,
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
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询流程定义列表 */
    getList() {
      this.loading = true;
      finishedList(this.queryParams).then(response => {
        this.finishedList = response.data.records;
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
    setIcon(val){
      if (val){
        return "el-icon-check";
      }else {
        return "el-icon-time";
      }

    },
    setColor(val){
      if (val){
        return "#2bc418";
      }else {
        return "#b3bdbb";
      }

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
      this.reset();
      this.open = true;
      this.title = "添加流程定义";
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
    /** 撤回任务 */
    handleRevoke(row){
      const params = {
        instanceId: row.procInsId
      }
      revokeProcess(params).then( res => {
        this.msgSuccess(res.msg);
        this.getList();
      });
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
      const ids = row.id || this.ids;
      const datas=this.selectData
      this.$confirm('是否确认删除数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        if(row.id){
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

