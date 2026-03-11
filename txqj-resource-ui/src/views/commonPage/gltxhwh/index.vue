<template>
  <div class="commonWh">
    <div class="box-card">
      <div class="queryBox">
        <div class="basicsQuery">
          <el-form :model="searchForm">
            <el-form-item label="任务名称 : " label-width="100px">
              <el-input size="mini" v-model="searchForm.name"></el-input>
            </el-form-item>
            <el-form-item label="涉及资源 : " label-width="100px">
              <el-input size="mini" v-model="searchForm.resName"></el-input>
            </el-form-item>
            <el-form-item label="编辑时间 : " label-width="100px">
              <el-date-picker
                size="mini"
                v-model="searchForm.time"
                type="daterange"
                start-placeholder="开始时间"
                end-placeholder="结束时间"
                value-format="yyyy-MM-dd HH:mm:ss"
              >
              </el-date-picker>
            </el-form-item>
            <el-form-item style="float: right" label-width="0">
              <el-button size="mini" type="primary" @click="getCommonData()">查询</el-button>
              <el-button size="mini" @click="searchForm = {}">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>

      <div class="tableBox">
        <div class="tabTopButton">
          <el-button type="primary" size="mini" @click="taskAdd(1)">新建图形化编辑</el-button>
          <el-button type="primary" size="mini" @click="taskAdd(0)">新增光缆引导模式</el-button>
        </div>
        <el-table
          tooltip-effect="light"
          stripe
          border
          height="calc(100% - 82px)"
          style="width: 100%;"
          :data="tableData"
        >
          <el-table-column
            label="序号"
            width="80"
            align="center"
            :formatter="snFormatter"
          ></el-table-column>
          <el-table-column
            show-overflow-tooltip
            align="center"
            prop="taskName"
            width="280"
            label="任务名称">
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            align="center"
            prop="createDate"
            width="150"
            label="创建时间">
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            align="center"
            prop="updateDate"
            width="150"
            label="最近一次编辑时间">
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            align="center"
            prop="owner"
            width="100"
            label="创建人">
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            align="center"
            prop="editType"
            width="100"
            label="主要操作">
          </el-table-column>
          <el-table-column
            prop="resName"
            min-width="250"
            label="涉及资源">
            <template slot-scope="scope">
                <el-tooltip placement="top" effect="light">
                  <div slot="content">
                    <span style="white-space: pre-line">
                      {{lineFeed(scope.row.resName)}}
                    </span>
                  </div>
                  <span
                    style="
                    display: inline-block;
                    width: 100%;
                    overflow:hidden;
                    white-space: nowrap;
                    text-overflow: ellipsis;
                    -o-text-overflow:ellipsis;">
                    {{scope.row.resName}}
                  </span>
                </el-tooltip>
            </template>
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            align="center"
            prop="islead"
            width="100"
            label="引导模式">
            <template slot-scope="scope">
              <span v-if="scope.row.islead == 0">是</span>
              <span v-if="scope.row.islead == 1">否</span>
            </template>
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            align="center"
            label="数据校验"
            width="100">
            <template slot-scope="scope">
              <span v-if="scope.row.checkoutType == 0" style="color: #67C23A">
                正常
              </span>
              <span v-else style="color: #F56C6C">
                {{scope.row.checkout}}
              </span>
            </template>
          </el-table-column>
          <el-table-column
            show-overflow-tooltip
            align="center"
            prop=""
            width="100"
            label="任务状态">
            <template slot-scope="scope">
              <span v-if="scope.row.taskStatus == 0">编制中</span>
              <span v-if="scope.row.taskStatus == 1">已完成</span>
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            width="180"
            fixed="right"
            align="center">
            <template slot-scope="scope">
              <div v-if="scope.row.taskStatus == 0">
                <el-button type="primary" size="mini" @click="openEdit(scope.row.id)">编辑</el-button>
                <el-button type="primary" size="mini" @click="openSee(scope.row.id)">查看</el-button>
                <el-button type="danger" size="mini" @click="deleteXml(scope.row.id)">删除</el-button>
              </div>
              <div v-if="scope.row.taskStatus == 1">
                <el-button type="primary" size="mini" @click="openSee(scope.row.id)">查看</el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          :total="total"
          :page.sync="pageNum"
          :limit.sync="pageSize"
          @pagination="getCommonData"
        />
      </div>
    </div>

    <toDatawarehouse
      ref="toDatawarehouse"
      islead="1"
      :taskId="selRow.taskId"
      :taskName="selRow.taskName"
    />
  </div>
</template>

<script>
  import { getList,addTask,delTask,getOneTask } from "@/api/gltxh/gltxhwh";
  import toDatawarehouse from "@/views/components/gltxh/toDatawarehouse.vue"
  export default {
    name: "Gltxhwh",
    components: {toDatawarehouse},
    data(){
        return{
          //搜索框的值
          searchForm:{},
          //表的分页
          pageSize: 15,
          pageNum: 1,
          total: 0,
          //表中存放的数据
          index:1,
          tableData: [],
          selRow: {}
        }
    },
    created(){
      this.getCommonData()
    },
    activated () {
      this.getCommonData()
    },
    methods:{
      lineFeed (str) {
        if (str != '' && str != undefined) {
          str = str.split(',')
          return str.join('\n')
        }
        return ''
      },
        /*新建图形化编辑*/
      taskAdd (islead) {
        let data
        let stepTipShow = false
        if (islead == 0) {
          data = {
            owner:window.sessionStorage.getItem('userName'),
            islead: 0,
            parlead: 0,
            sonlead: 0,
          }
          stepTipShow = true
        } else if (islead == 1) {
          data = {
            owner:window.sessionStorage.getItem('userName'),
            islead: 1,
          }
        }
        addTask(data).then(res=>{
          this.$router.push({
            path:'/commonPage/gltxhwh-data/index/'+res.msg,
            query:{
              stepTipShow
            }
          })
        })
      },
      /* 获取表的基本信息 */
      getCommonData(){
        let createDate=''
        let updateDate=''
        if(this.searchForm.time){
          createDate=this.searchForm.time[0]
          updateDate=this.searchForm.time[1]
        }
        let params={
          pageSize: this.pageSize,
          pageNum: this.pageNum,
          taskName:this.searchForm.name,
          resName:this.searchForm.resName,
          createDate:createDate,
          updateDate:updateDate,
        }
        getList(params).then(res=>{
          this.tableData = res.rows
          this.total=res.total
        })
      },
      //动态获取表的信息

      /* 格式序列化列 */
      snFormatter: function (row, col, value, index) {
        return this.pageSize * (this.pageNum - 1) + index + 1;
      },


      /* 启用编辑，打开图形化编辑 */
      openSee:function (id) {
        getOneTask(id).then(res=>{
          if(res.code==200){
            this.$router.push({
              path:'/commonPage/gltxhwh-data/index/' + id,
              query: {isSee: true}
            })
          }
        })
      },/* 启用编辑，打开图形化编辑 */
      openEdit:function (id) {
        getOneTask(id).then(res=>{
          if(res.code==200){
            this.$router.push({path:'/commonPage/gltxhwh-data/index/'+id})
          }
        })
      },
      /* 删除 */
      deleteXml:function (id) {
        this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消'
        })
          .then(() => {
            delTask(id).then(res=>{
              if(res.code==200){
                this.$message({
                  type: 'success',
                  message: '删除成功'
                });
              }
              this.getCommonData()
            })
          })
          .catch(action => {
            this.$message({
              type: 'info',
              message: '已取消删除'
            })
          });
      },
      dataToWarehouse (id, taskName) {
        this.selRow = {
          taskId: id,
          taskName: taskName
        }
        this.$refs.toDatawarehouse.submitDaaVerify()
      }
    }
  }
</script>
