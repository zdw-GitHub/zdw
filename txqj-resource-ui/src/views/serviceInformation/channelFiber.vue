<template>
  <div class="commonWh">
    <div id="tcconTcchannel">
      <div class="editForm">
        <p class="formTop">
          <span class="formTit">纤芯通道</span>
          <span class="formBut">
            <el-button size="mini" @click="back()">返回</el-button>
          </span>
        </p>
        <GL
          ref="GL"
          @obtainChannelLinks="obtainChannelLinks"
          :llId="llId" :ywId="this.tcchannelForm.ID"
          style="height: 37vh"></GL>
        <p class="formTop">
          <span class="formTit">通道链路</span>
        </p>
        <div style="height: 38vh" class="commonWh">
          <div style="margin: 10px">
<!--            <el-button type="primary" size="mini" @click="openAdd()" v-hasPermi="['system:b:add']">新建通道链路</el-button>-->
            <el-button type="primary" size="mini" @click="bindLLShow()" v-hasPermi="['system:b:add']">绑定通道链路</el-button>
          </div>
          <el-table
            stripe
            border
            style="width: 100%"
            height="calc(100% - 60px)"
            ref="multipleTable"
            :data="link.tableData"
            v-loading="link.loading">
            <el-table-column width="80" label="选择" align="center">
              <template slot-scope="scope">
                <el-radio v-model="llId" :label="scope.row.id" @change.native="handleSelectionChange(scope.$index,scope.row)"> <span></span></el-radio>
              </template>
            </el-table-column>
            <el-table-column label="序号" width="80" align="center" :formatter="snFormatter"></el-table-column>
            <template
              v-if="link.total != 0"
              v-for="item in link.tableColData"
            >
              <el-table-column
                :label="item.name"
                :min-width="getColSize(link.tableData[0][item.prop], item.name)"
                align="center"
                show-overflow-tooltip
                :prop="item.prop"
              ></el-table-column>
            </template>
            <el-table-column label="操作" width="350" fixed="right" align="center">
              <template slot-scope="scope">
                <el-button size="mini" type="primary" @click="openRouter(scope.row)">路由</el-button>
                <el-button size="mini" type="primary" @click="openEdit(scope.row)" v-hasPermi="['system:b:update']">编辑</el-button>
                <el-button size="mini" type="primary" @click="lookData(scope.row)" v-hasPermi="['system:b:list']">查看</el-button>
                <el-button size="mini" type="danger"  @click="delData(scope.row)" v-hasPermi="['system:b:remove']">删除</el-button>
                <el-button size="mini" type="danger"  @click="unbindLL(scope.row)" v-hasPermi="['system:b:remove']">解除绑定</el-button>
              </template>
            </el-table-column>
          </el-table>
          <pagination
            :total="link.total"
            :page.sync="link.pageNum"
            :limit.sync="link.pageSize"
            @pagination="getCommonData"
          />
        </div>
      </div>

    </div>
    <!--绑定通道链路-->
    <el-dialog
      v-dialogDrag
      custom-class="curdBox"
      title="绑定链路"
      :visible.sync="bindLLBox.show"
      width="80%"
      :modal="false"
      :close-on-click-modal="false"
    >
      <div style="height: 70vh" class="commonWh">
        <el-form ref="form" label-width="100px" :inline="true">
          <el-form-item label="名称 : ">
            <el-input size="mini" v-model="bindLLBox.searchForm.name"></el-input>
          </el-form-item>
          <el-form-item label="A设备 : " prop="ares">
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
              v-model="bindLLBox.searchForm.aResId"
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
          <el-form-item label="Z设备" prop="zres">
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
              v-model="bindLLBox.searchForm.zResId"
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
          <el-form-item style="float: right;" label-width="0">
            <el-button size="mini" type="primary" @click="bindLLShow()">查询</el-button>
            <el-button size="mini" @click="bindLLBox.searchForm = {}">重置</el-button>
          </el-form-item>
        </el-form>
        <el-table
          ref="multipleTable"
          stripe
          border
          height="calc(100% - 60px)"
          style="width: 100%;"
          :data="bindLLBox.tableData"
          v-loading="bindLLBox.tableLoadIng"
        >
          <el-table-column
            label="序号"
            width="80"
            align="center"
            :formatter="snFormatter"
          ></el-table-column>
          <el-table-column
            v-if="bindLLBox.tableData[0]"
            v-for="item in bindLLBox.tableColData"
            :label="item.name"
            :min-width="getColSize(bindLLBox.tableData[0][item.prop], item.name)"
            align="center"
            show-overflow-tooltip
            :prop="item.prop"
          ></el-table-column>
          <el-table-column
            label="操作"
            fixed="right"
            width="80"
            align="center"
          >
            <template slot-scope="scope">
              <el-button size="mini" type="primary" @click="bindLLCommit(scope.row)" v-if="!scope.row.buzId">选择</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          :total="bindLLBox.total"
          :page.sync="bindLLBox.pageNum"
          :limit.sync="bindLLBox.pageSize"
          @pagination="bindLLShow"
        />
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="bindLLBox.show = false">取 消</el-button>
      </div>
    </el-dialog>
    <!--修改对话框-->
    <el-dialog
      v-dialogDrag
      custom-class="curdBox"
      title="修改"
      :visible.sync="editStationDrawer"
      width="1800px"
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
      width="1800px"
      :close-on-click-modal="false"
    >
      <editData ref="child" :modelObj="this.modelObj" :dataObj="this.editForm"></editData>
      <div slot="footer" class="dialog-footer" style="margin-bottom:20px">
        <el-button type="primary" @click="submitForm()">确 定</el-button>
        <el-button @click="newStationDrawer=false">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 查看属性对话框 -->
    <el-dialog
      v-dialogDrag
      custom-class="curdBox"
      title="查看"
      :modal="false"
      :visible.sync="stationDrawer"
      width="1800px"
      :close-on-click-modal="false"
    >
      <seeData ref="seeChild" :modelObj="this.modelObj" :dataObj="editForm"></seeData>
      <div slot="footer" class="dialog-footer" style="margin-bottom:20px">
        <el-button @click="stationDrawer=false">关 闭</el-button>
      </div>
    </el-dialog>
    <fiberLineTopo :parRow="editForm" ref="fiberLineTopo"></fiberLineTopo>
  </div>
</template>

<script>
import {getColSize} from "@/utils/getColSize"
import {nameTo_, nameToTuoFeng} from "@/utils"
import{getModelId, tableDropDownBox} from "@/api/zdwh/zdzywh";
import {
  deleteRemovetcchannelB,
  queryTheBoundLinkBasedOnTheBusinessID,
  tdllAddCommitApi,
  tdllEditCommitApi,
  getbindLlApi,
  bindLlCommitApi,
  unbindLlCommitApi
} from "@/api/serviceInformation";
import editData from "@/views/publicPage/edit/editData";
import GL from "@/views/serviceInformation//GL";
import seeData from "@/views/publicPage/edit/seeData";
import fiberLineTopo from "@/views/serviceInformation/fiberLineTopo.vue";
export default {
  name: "channelFiber",
  props: {
    tcchannelForm: Object,
  },
  watch: {
    //解决操作栏固定后表格错位问题
    'bindLLBox.tableData': {
      handler() {
        this.$nextTick(() => {
          this.$refs.multipleTable.doLayout();
        })
      },
      deep: true
    },
  },
  components: {fiberLineTopo, editData, seeData, GL},
  data() {
    return {
      tableName: 'SG_TCCON_TCCHANNEL_B',
      //链路列表
      link: {
        loading: false,
        pageSize: 15,
        pageNum: 1,
        total: 0,
        tableData: [],
        tableColData: [
          {name: '名称', prop: 'name'},
          {name: '起始设备', prop: 'aResId'},
          {name: '终止设备', prop: 'zResId'},
          {name: '链路类型', prop: 'channelTypeName'},
          {name: '用状态', prop: 'serviceStateName'},
          {name: '其他调度等级', prop: 'otherDspLevelName'},
        ],
      },
      //  单条数据
      editForm: {},
      //编辑是否第一次打开
      ifFirstEdit: true,
      //查看是否第一次打开
      ifFirstSee: true,
      editStationDrawer: false,
      //表对象信息
      modelObj: {},
      //传递的数据
      commonData:{},
      newStationDrawer: false,
      stationDrawer: false,

      bindLLBox: {
        show: false,
        searchForm: {},
        pageSize: 15,
        pageNum: 1,
        total: 0,
        tableData: [],
        tableColData: [
          {name: '名称', prop: 'name'},
          {name: '起始设备', prop: 'aResId'},
          {name: '起始端口', prop: 'aPort'},
          {name: '终止设备', prop: 'zResId'},
          {name: '终止端口', prop: 'zPort'},
          {name: '链路类型', prop: 'channelTypeName'},
          {name: '已绑定业务', prop: 'buzName'},
        ],
        tableLoadIng: false,
      },
      llId: '',
      editTcconForm:{},

      // 下拉框框配置
      selectName: '',
      optionsA:{},
      optionsZ:{},
      optionsALoading: true,
      optionsZLoading: true,
      modelObj2: {},
    }
  },
  methods: {
    getCommonData() {
      //获取表基本信息
      getModelId({tableName: this.tableName}).then(res => {
        this.modelObj = res.data
        this.obtainChannelLinks()
      })

      //获取表基本信息
      getModelId({tableName: 'SG_TCDEV_FIBERLINE_B'}).then(res => {
        this.modelObj2 = res.data
      })
    },

    openRouter (row) {
      this.editForm = row
      this.$refs.fiberLineTopo.parRow = row
      this.$refs.fiberLineTopo.lightPathBoxShow()
    },
    //获取通道链路
    obtainChannelLinks() {
      this.link.loading = true
      queryTheBoundLinkBasedOnTheBusinessID({
        ywId: this.tcchannelForm.ID,
        pageSize: this.link.pageSize,
        pageNum: this.link.pageNum,
      }).then(res => {
        this.link.tableData = res.rows
        this.link.total = res.total
        this.$nextTick(() => {
          // 初始化表格
          this.$refs.multipleTable.doLayout();
        })
        if (this.link.tableData.length) {
          this.llId = this.link.tableData[0].id
          this.$refs.GL.llId = this.llId
          this.$refs.GL.getTableData()
        }
        this.link.loading = false
      })
    },
    //编辑
    openEdit(row) {
      this.editForm = row
      for (let key in row) {
        this.editForm[nameTo_(key)] = row[key]
      }
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
          tdllEditCommitApi(params).then(response => {
              this.editStationDrawer = false
              this.$modal.msgSuccess("操作成功");
              this.obtainChannelLinks()
            }
          )
        })
      }, 1500);
    },
    //新建
    openAdd() {
      this.editForm = {}
      this.newStationDrawer = true
      this.commonData.CHANNEL_TYPE = 1800
      this.$nextTick(() => {
        this.$refs.child.form = this.commonData
        this.$refs.child.modelObj = this.modelObj
        this.$refs.child.dataObj = this.editForm
        this.$refs.child.beforeLoading()
      })
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
          params.ywId = this.tcchannelForm.ID,
            tdllAddCommitApi(params).then(response => {
                this.newStationDrawer = false
                this.$modal.msgSuccess("操作成功");
                this.obtainChannelLinks()
              }
            )
        })
      }, 1500);
    },
    //查看
    lookData(row) {
      this.editForm = row
      for (let key in row) {
        this.editForm[nameTo_(key)] = row[key]
      }
      this.stationDrawer = true
      this.$nextTick(() => {
        this.$refs.seeChild.modelObj = this.modelObj
        this.$refs.seeChild.dataObj = this.editForm
        if (this.ifFirstSee) {
          this.$refs.seeChild.beforeLoading()
          this.ifFirstSee = false
        } else {
          this.$refs.seeChild.reOpen()
        }
      })
    },
    //单个删除
    delData(row) {
      this.$confirm('此操作将删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteRemovetcchannelB(row.id).then((res) => {
          if (res.code === 200) {
            this.obtainChannelLinks()
            this.$message.success('删除成功')
          } else {
            this.$message.error('删除失败')
          }
        })
      }).catch((e) => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })

    },
    /*绑定链路*/
    bindLLShow() {
      this.bindLLBox.show = true
      this.bindLLBox.tableLoadIng = true
      this.bindLLBox.tableData = []
      let params = this.bindLLBox.searchForm
      params.channelType = '1800'
      params.pageSize = this.bindLLBox.pageSize
      params.pageNum = this.bindLLBox.pageNum
      getbindLlApi(params).then(res => {
        this.bindLLBox.tableLoadIng = false
        this.bindLLBox.tableData = res.rows
        this.bindLLBox.total = res.total
        this.getOption('optionsA')
        this.getOption('optionsZ')
      })
    },
    //绑定链路提交
    bindLLCommit(row) {
      bindLlCommitApi({
        buzId: this.tcchannelForm.ID,
        channelId: row.id
      }).then(res => {
        this.bindLLBox.show = false
        this.$modal.msgSuccess("操作成功");
        this.obtainChannelLinks()
      })
    },
    /*解绑链路*/
    unbindLL(row) {
      this.$confirm('是否解除绑定?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        unbindLlCommitApi({
          channelId: row.id,
          buzId: this.tcchannelForm.ID,
        }).then(response => {
          this.$modal.msgSuccess("操作成功");
          this.obtainChannelLinks()
        })
      })
    },
    /*返回*/
    back(){
      this.$emit('goBack')
    },
    handleSelectionChange(index,row) {
      this.llId = row.id
      this.$refs.GL.llId = this.llId
      this.$refs.GL.getTableData()
    },

    /*搜索*/
    loadmore (val) {
      if (this[this.selectName].total > this[this.selectName].list.length) {
        let pageNum = parseInt(this[this.selectName].list.length / 20) * 20
        tableDropDownBox({
          modelId: this.modelObj2.modelId,
          modeVersion: this.modelObj2.modelVersion,
          attribute: 'PAR_FIBER_SEG',
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
    remoteMethod (val, data) {
      this.searchOptionStr = val
      tableDropDownBox({
        modelId: this.modelObj2.modelId,
        modeVersion: this.modelObj2.modelVersion,
        attribute: 'PAR_FIBER_SEG',
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
    getOption (data) {
      tableDropDownBox({
        modelId: this.modelObj2.modelId,
        modeVersion: this.modelObj2.modelVersion,
        attribute: 'PAR_FIBER_SEG',
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
    /* 格式序列化列 */
    snFormatter: function (row, col, value, index) {
      return this.bindLLBox.pageSize * (this.bindLLBox.pageNum - 1) + index + 1;
    },
    getColSize(val, titVal) {
      return getColSize(val, titVal)
    },
  },
  created() {
    this.getCommonData()
  }
}
</script>

<style scoped lang="scss">
#tcconTcchannel {
  width: 100%;
  padding: 10px;
  background-color: #fff;

  .editForm {
    width: 100%;
    margin-bottom: 10px;

    .editTccon {
      width: 100%;
      overflow-y: auto;
      padding: 40px 40px 20px 40px;
      border-bottom: 1px solid #C0C4CC;
      margin-bottom: 20px;
    }

    .formTop {
      width: 100%;
      height: 40px;
      line-height: 40px;

      .formTit {
        font-size: 16px;
        font-weight: bold;
        color: #079b9f;
      }

      .formBut {
        float: right;
      }
    }
  }

  .tcchannelsegTable {
    height: calc(100vh - 90px);
    width: 100%;
    background-color: #fff;
  }

  /*路由弹窗*/
  .graphModule {
    border: 1px solid red;
    height: 60vh;
  }
}
</style>
