<template>
  <div class="commonWh">
    <el-form label-width="150px" :model="breakpointLocationParams" :inline="true">
      <el-form-item>
        <el-button style="" type="primary" @click="openAddConnectorBox()" size="mini">关联接头盒
        </el-button>
      </el-form-item>
      <el-form-item>
        <el-button style="margin: 0 20px" type="primary" @click="openAdd()" size="mini">新增接头盒
        </el-button>
      </el-form-item>
      <el-form-item v-if="jFlag" label="光缆总长度（公里） : ">
        <el-input v-model="fiberLength" disabled></el-input>
      </el-form-item>
      <el-form-item v-if="jFlag" label="方向 : " prop="analysisType">
        <el-select
          size="mini"
          v-model="breakpointLocationParams.direction"
          placeholder="请选择">
          <el-option :label="aResobjid + ' ---> ' + zResobjid" value="起点到终点"></el-option>
          <el-option :label="zResobjid + ' ---> ' + aResobjid" value="终点到起点"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item v-if="jFlag" label="断点位置（公里） : " prop="length">
        <el-input-number v-model="breakpointLocationParams.length" :precision="2" :step="0.01" :max="999" :min="0"
                         placeholder="请输入断点距离" controls-position="right"></el-input-number>
      </el-form-item>
      <el-form-item style="float: right;margin: 10px 40px!important;">
        <el-button style="margin-bottom: 10px" type="primary" @click="analysis()" size="mini" v-if="jFlag">分析</el-button>
      </el-form-item>
    </el-form>


    <el-table
      tooltip-effect="light"
      ref="checkUpdateMultipleTable"
      :data="tableData"
      border
      v-loading="loading"
      height="calc(90%)">
      <el-table-column label="序号" width="50" align="center" type="index"></el-table-column>
      <el-table-column
        prop="name"
        label="接头盒名称"
        align="center"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <a
            class="linkColumn"
            @click="getRouterLink(scope.row)"
          >
            {{scope.row.name}}
          </a>
        </template>
      </el-table-column>
      <el-table-column
        prop="devType"
        label="装置类型"
        align="center"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="aDistanceLength"
        :label="'距离起点 '+ aResobjid +' 长度（公里）'"
        align="center"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        prop="zDistanceLength"
        :label="'距离终点 '+ zResobjid +' 长度（公里）'"
        align="center"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column label="操作" width="200" fixed="right" align="center">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="openEdit(scope)">编辑</el-button>
          <el-button type="danger" size="mini" @click="delcheckData(scope)">删除关联</el-button>
          <el-button type="danger" size="mini" @click="delSgTcconFbrconnectB(scope)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!--关联接头盒-->
    <el-dialog
      v-dialogDrag
      title="关联接头盒"
      :visible.sync="newCheckUpdateDialog"
      :modal="false"
      :close-on-click-modal="false"
      width="60%">
      <div style="width: 100%; height: 55vh;" class="commonWh">
        <el-form label-width="100px" :model="queryParams" :inline="true">
          <el-form-item label="接头盒名称 : ">
            <el-input size="mini" v-model="queryParams.name"></el-input>
          </el-form-item>
          <el-form-item style="float: right;">
            <el-button size="mini" type="primary" @click="getJointBoxList()">查询</el-button>
          </el-form-item>
        </el-form>
        <el-table
          tooltip-effect="light"
          :data="jointBoxList"
          height="calc(100% - 80px)"
          border>
          <el-table-column label="序号" width="50" align="center" type="index"></el-table-column>
          <el-table-column
            prop="name"
            label="接头盒名称"
            align="center"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="devType"
            label="装置类型"
            align="center"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="aDistanceLength"
            label="距离起点站点长度（公里）"
            align="center"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="zDistanceLength"
            label="距离终点站点长度（公里）"
            align="center"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column label="操作" width="80" fixed="right" align="center">
            <template slot-scope="scope">
              <el-button size="mini" @click="addData(scope)">选择</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getJointBoxList"
        />
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="newCheckUpdateDialog = false">关 闭</el-button>
      </div>
    </el-dialog>
    <el-dialog
      v-dialogDrag
      title="光缆断点定位"
      :visible.sync="breakpointLocationDialog"
      v-if="breakpointLocationDialog"
      :modal="false"
      :close-on-click-modal="false"
      width="100%">
      <div style="width: 100%; height: 58vh;">
        <iframe
          ref="iframe"
          width="100%"
          id="iframe"
          name="iframe"
          style="border: none"
          height="100%"
          :src="iframeSrc"
        ></iframe>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="breakpointLocationDialog = false">关 闭</el-button>
      </div>
    </el-dialog>

    <el-dialog
      v-dialogDrag
      title="接头盒维护"
      :visible.sync="breakpointLocationDialog"
      v-if="breakpointLocationDialog"
      :modal="false"
      :close-on-click-modal="false"
      width="100%">
      <div style="width: 100%; height: 58vh;">
        <iframe
          ref="iframe"
          width="100%"
          id="iframe"
          name="iframe"
          style="border: none"
          height="100%"
          :src="iframeSrc"
        ></iframe>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="breakpointLocationDialog = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!--修改对话框-->
    <el-dialog
      v-dialogDrag
      custom-class="curdBox"
      title="修改"
      :visible.sync="editStationDrawer"
      width="750px"
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
      width="750px"
      :close-on-click-modal="false"
    >
      <editData ref="child" :modelObj="this.modelObj" :dataObj="this.editForm"></editData>
      <div slot="footer" class="dialog-footer" style="margin-bottom:20px">
        <el-button type="primary" @click="submitForm()">确 定</el-button>
        <el-button @click="newStationDrawer=false">取 消</el-button>
      </div>
    </el-dialog>


  </div>
</template>

<script>
import {
  listByFiberId,
  jointBoxList,
  fiberBreakpointLocation,
  delSgTcconFiberRFbrconnect,
  addSgTcconFiberRFbrconnect,
  addSgTcconFbrconnectB,
  updateSgTcconFbrconnectB,
  delSgTcconFbrconnectB
} from '@/api/manageFiber/fiberRFbrconnect'
import {nameToTuoFeng} from "@/utils"
import{getModelId} from "@/api/zdwh/zdzywh";
import editData from "@/views/publicPage/edit/editData";
export default {
  name: "index",
  props: {
    fiberId: String,
    aResobjid: String,
    zResobjid: String,
    fiberLength: Number,
    openNewPage: {
      type: Boolean,
      default: () => true
    },
    jFlag: true
  },
  components: {editData},
  data() {
    return {
      //光缆关联接头盒数据
      tableData: [],
      loading: false,
      //新增接头盒弹窗
      newCheckUpdateDialog: false,
      // 新增接头盒总条数
      total: 0,
      // 新增接头盒查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: '',
      },
      //新增接头盒列表
      jointBoxList: [],
      breakpointLocationDialog: false,
      breakpointLocationParams: {
        direction: '起点到终点',
        length: null
      },
      iframeSrc: '/stationWh/manageFiberHtml/fiberBreakpointLocation.html',
      tableName: 'SG_TCCON_FBRCONNECT_B',
      //  单条数据
      editForm: {},
      //编辑是否第一次打开
      ifFirstEdit: true,
      newStationDrawer: false,
      //表对象信息
      modelObj: {},
      editStationDrawer: false,
      //传递的数据
      commonData:{},
    }
  },
  watch: {
    //解决操作栏固定后表格错位问题
    'jointBoxList': {
      handler() {
        this.$nextTick(() => {
          this.$refs.multipleTable.doLayout();
        })
      },
      deep: true
    },
  },
  activated() {
    this.getTableData()
  },
  methods: {

    getCommonData() {
      //获取表基本信息
      getModelId({tableName: this.tableName}).then(res => {
        this.modelObj = res.data
        this.getTableData()
      })
    },
    getTableData() {
      this.loading = true
      listByFiberId({fiberId: this.fiberId}).then((res) => {
        this.tableData = res.rows
        this.loading = false
      })
    },
    getJointBoxList() {
      jointBoxList(this.queryParams).then(res => {
        this.jointBoxList = res.rows
        this.total = res.total
      })
    },
    openAddConnectorBox() {
      this.newCheckUpdateDialog = true
      this.getJointBoxList()
    },
    addData(scope) {
      if (this.tableData.find(item => {
        return item.jthId == scope.row.id
      })) {
        this.$message.success('已绑定该接头盒')
        return
      }
      addSgTcconFiberRFbrconnect({
        parFiber: this.fiberId,
        parFbrconnect: scope.row.id
      }).then(res => {
        this.getTableData()
        this.newCheckUpdateDialog = false
        this.$message.success('保存成功')
      })
    },
    delcheckData(scope) {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delSgTcconFiberRFbrconnect(scope.row.id).then((res) => {
          this.getTableData()
          this.$message.success('删除成功')
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已删除解除'
        })
      })
    },
    delSgTcconFbrconnectB(scope){
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delSgTcconFbrconnectB(scope.row.jthId).then((res) => {
          delSgTcconFiberRFbrconnect(scope.row.id).then((r) => {
            this.getTableData()
            this.$message.success('删除成功')
          })
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已删除解除'
        })
      })
    },
    analysis() {
      if (this.breakpointLocationParams.direction && this.breakpointLocationParams.length) {
        fiberBreakpointLocation({
          fiberId: this.fiberId,
          ...this.breakpointLocationParams
        }).then(res => {
          this.breakpointLocationDialog = true
          this.$nextTick(() => {
            window.setTimeout(() => {
              this.$refs.iframe.contentWindow.reSetTopo(res.data.nodes, res.data.links, res.data.groups)
            }, 1000)
          })
        })

      } else {
        this.$message({
          type: 'info',
          message: '请填写断点位置'
        })
      }

    },
    //跳转
    getRouterLink(row){
      if (this.openNewPage){
        this.$router.push({
          path:'/fibersource/zdwh/common/SG_TCCON_FBRCONNECT_B',
          query:{
            search: JSON.stringify({ID:row.jthId}),
            refresh:true,
          }
        })
      }else {
        this.$bus.$emit('openNewTab',{
          tableName: this.tableName,
          name: row.name,
          title: '接续装置详情',
          ID: row.jthId,
        })
      }

    },
    //新建
    openAdd() {
      this.editForm = {}
      this.newStationDrawer = true
      this.$nextTick(() => {
        this.$refs.child.form = this.commonData
        this.$refs.child.modelObj = this.modelObj
        this.$refs.child.dataObj = this.editForm
        this.$refs.child.beforeLoading()
      })
    },
    //编辑
    openEdit({row}) {
      this.editForm.ID = row.jthId
      this.editStationDrawer = true
      this.$nextTick(() => {
        this.$refs.child.modelObj = this.modelObj
        this.$refs.child.dataObj = this.editForm
        if (this.ifFirstEdit) {
          this.$refs.child.beforeLoading()
          this.ifFirstEdit = false
        } else {
          this.$refs.child.reOpen()
        }
      })
    },
    //编辑提交
    submitEditForm() {
      //调用子组件重复校验方法
      this.$refs.child.beforeSubmit()
      //获取子组件表单数据
      this.editTcconForm = {}
      this.editTcconForm = this.$refs.child.pageForm
      setTimeout(() => {
        this.$confirm('是否保存此数据?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let params = {}
          for (let key in this.editTcconForm) {
            params[nameToTuoFeng(key)] = this.editTcconForm[key]
          }
          updateSgTcconFbrconnectB(params).then(response => {
              this.editStationDrawer = false
              this.$modal.msgSuccess("操作成功");
              this.getTableData()
            }
          )
        })
      }, 1500);
    },
    //新增提交
    submitForm() {
      //调用子组件重复校验方法
      this.$refs.child.beforeSubmit()
      //获取子组件表单数据
      this.editTcconForm = {}
      this.editTcconForm = this.$refs.child.pageForm
      setTimeout(() => {
        this.$confirm('是否保存此数据?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let params = {}
          for (let key in this.editTcconForm) {
            params[nameToTuoFeng(key)] = this.editTcconForm[key]
          }
          params.fiberId = this.fiberId
          addSgTcconFbrconnectB(params).then(response => {
                this.newStationDrawer = false
                this.$modal.msgSuccess("操作成功");
                this.getTableData()
              }
            )
        })
      }, 1500);
    },
  },
  created() {
    this.getCommonData()
  }
}
</script>

<style scoped>

</style>
