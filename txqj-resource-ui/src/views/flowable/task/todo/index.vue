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
                size="mini"
                clearable
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
          >删除
          </el-button>
        </div>

        <el-table
          v-loading="loading"
          :data="todoList"
          border
          style="width: 100%;"
          height="calc(100% - 82px)"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="50" align="center"/>
          <el-table-column
            label="序号"
            width="80"
            align="center"
            :formatter="snFormatter"
          ></el-table-column>
          <!--<el-table-column label="任务编号" align="center" prop="taskId" :show-overflow-tooltip="true"/>-->
          <el-table-column
            show-overflow-tooltip
            label="流程名称"
            align="center"
            min-width="300"
            prop="procDefName"/>
          <el-table-column
            label="任务节点"
            show-overflow-tooltip
            align="center"
            min-width="300"
            prop="taskName"/>
          <el-table-column
            show-overflow-tooltip
            label="流程版本"
            min-width="100"
            align="center">
            <template slot-scope="scope">
              <el-tag size="medium" >v{{scope.row.procDefVersion}}</el-tag>
            </template>
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            align="center"
            min-width="200"
            label="流程发起人"
          >
            <template slot-scope="scope">
              <label>{{scope.row.startUserName}} <el-tag type="info" size="mini">{{scope.row.startDeptName}}</el-tag></label>
            </template>
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            align="center"
            min-width="160"
            label="接收时间"
            prop="createTime"/>
          <el-table-column
            label="操作" align="center" width="100">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="primary"
                @click="handleProcess(scope.row)"
              >处理
              </el-button>
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
import {
  todoList,
  complete,
  returnList,
  returnTask,
  rejectTask,
  getDeployment,
  delDeployment,
  exportDeployment
} from "@/api/flowable/todo";

export default {
  name: "/todo",
  components: {},
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
      // 流程待办任务表格数据
      todoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 15,
        name: null,
        category: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {}
    };
  },
  created() {
    this.getList();
  },
  activated() {
    this.getList();
  },
  methods: {
    /** 查询流程定义列表 */
    getList() {
      this.loading = true;
      todoList(this.queryParams).then(response => {
        this.todoList = response.data.records;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    // 跳转到处理页面
    handleProcess(row){
      this.$router.push({ path: '/flowable/task/record/index/'+row.taskId,
        query: {
          procInsId: row.procInsId,
          deployId: row.deployId,
          taskId: row.taskId,
          finished: true
        }})
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
    /* 格式序列化列 */
    snFormatter: function (row, col, value, index) {
      return this.queryParams.pageSize * (this.queryParams.pageNum - 1) + index + 1;
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.selectData=selection
      this.ids = selection.map(item => item.procInsId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加流程定义";
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
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = this.ids;
      const datas=this.selectData
      this.$confirm('是否确认删除数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
          for (let i = datas.length-1; i >=0; i--) {
            if(i===0){
              return delDeployment(datas[i].procInsId)
            }else{
              delDeployment(datas[i].procInsId)
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
      }).then(function () {
        return exportDeployment(queryParams);
      }).then(response => {
        this.download(response.msg);
      })
    }
  }
};
</script>

