<template>
  <div class="toDatawarehouse">
    <el-dialog
      v-dialogDrag
      :title="islead == 0 ? '数据检验未通过' : '数据检验未通过是否继续提交'"
      :visible.sync="errDataBox.show"
      :close-on-click-modal="false"
      width="50%">
      <div style="height: 70vh" v-loading="errDataBox.loading">
        <div style="border-bottom:1px solid black;margin-bottom:10px">
          <span>不合法数据</span>
        </div>
        <div style="height: calc(100% - 30px)">
          <el-table
            :data="errDataBox.data"
            height="100%"
            style="width: 100%">
            <el-table-column
              prop="resName"
              label="资源名称">
            </el-table-column>
            <el-table-column
              prop="resType"
              label="资源类型"
              width="100">
            </el-table-column>
            <el-table-column
              label="不合法原因"
              width="150">
              <template slot-scope="scope">
              <span v-if="scope.row.checkoutType == 0" style="color: #67C23A">
                通过
              </span>
                <span v-else style="color: #F56C6C">
                {{scope.row.checkout}}
              </span>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button
          v-if="islead != 0"
          type="primary"
          @click="errDataBox.show = false, newFlow()"
        >提 交</el-button>
        <el-button @click="errDataBox.show = false">关 闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import {
  submitDataVerifyApi,latestPdConversion,synchronousOpticalCableRelatedData
  } from "@/api/gltxh/toDatawarehouse";
  import {
    listDefinition
  } from "@/api/flowable/definition";
  export default {
    name: "ToDatawarehouse",
    props: {
      islead: String,
      taskId: String,
      taskName: String,
    },
    data () {
      return {
        errDataBox: {
          show: false,
          loading: false,
          data: []
        },
      }
    },
    methods: {
      // 进入等待
      loadingStart () {
        this.errDataBox.loading = true
      },
      // 提交校验
      submitDaaVerify () {
        submitDataVerifyApi({taskId: this.taskId}).then(res => {
          this.errDataBox.show = true
          this.errDataBox.loading = false
          if (res.data.length > 0) {
            this.errDataBox.loading = false
            this.errDataBox.data = res.data
          } else {
            this.errDataBox.show = false
            if (this.islead != '0') {
              this.newFlow()
            } else {
              this.$message({
                type: 'success',
                message: '校验通过 请点击下一步 生成配线'
              });
            }
          }
        }).catch(e => {
          this.$message({
            type: 'error',
            message: e
          });
        })
      },
      //最新流程
      newFlow(){
        // latestPdConversion({procDefCategory:'process_fiber_graphical'}).then(res=>{
        //   this.flowStar(res.data)
        // })
        //同步光缆相关数据
        synchronousOpticalCableRelatedData({taskId: this.taskId}).then(res => {
          this.$message({ type: 'success', message: '同步完成'});
        })
      },
      // 发起流程申请
      flowStar (row) {
        this.$nextTick(() => {
          this.$router.push({ path: '/flowable/task/record/index/'+this.taskId,
           query: {
              deployId: row.deploymentId,
              procDefId: row.id,
              finished: true,
              topoTaskId: this.taskId,
              topoTaskName: this.taskName,
              flowCategory: row.category,
            }
          })
        })
      }
    },
  }
</script>

<style scoped>

</style>
