<template>
  <div class="commonWh">
    <div class="box-card">
      <div class="queryBox">
        <!--基础查询-->
        <div class="basicsQuery">
          <el-form :model="queryParams" ref="queryForm" label-width="120px">
            <el-form-item label="光缆名称 : " prop="fiberName">
              <el-input size="mini" v-model="queryParams.fiberName" placeholder="请输入光缆名称"></el-input>
            </el-form-item>
            <el-form-item label="起始站点 : " prop="aResobjid">
              <el-select size="mini" v-model="queryParams.aResobjid" placeholder="请选择" clearable filterable
                :filter-method="filterMethod" v-el-select-loadmore="loadMore(rangeNumber)">
                <el-option v-for="item in options1.slice(0, rangeNumber)" :key="item.value" :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="终止站点 : " prop="zResName">
              <el-input size="mini" v-model="queryParams.zResName" placeholder="请输入站点名称"></el-input>
            </el-form-item>
            <el-form-item style="float: right;">
              <el-button type="primary" size="mini" @click="handleQuery">查询</el-button>
              <el-button size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
        <el-button type="primary" style="margin-top: 10px;" size="mini" @click="openInfo()">新增</el-button>
      </div>

      <div class="tableBox">
        <el-table stripe border height="calc(100% - 30px)" :data="fiberList" v-loading="loading">
          <el-table-column label="序号" width="80" align="center" :formatter="snFormatter" />
          <el-table-column show-overflow-tooltip v-if="false" label="ID" prop="ID" />
          <el-table-column show-overflow-tooltip label="光缆名称" align="center" min-width="300" prop="fiberName" />
          <el-table-column show-overflow-tooltip v-if="false" label="所属光缆" prop="parFiber" />
          <el-table-column show-overflow-tooltip label="起始站点" align="center" min-width="200" prop="aResName" />
          <el-table-column show-overflow-tooltip label="终点站点" align="center" min-width="200" prop="zResName" />
          <el-table-column show-overflow-tooltip label="光缆长度" align="center" min-width="100" prop="fiberLength" />
          <el-table-column show-overflow-tooltip label="芯数" align="center" min-width="100" prop="lineNumber" />
          <el-table-column show-overflow-tooltip label="投运时间" align="center" min-width="200" prop="operateDate" />
          <el-table-column show-overflow-tooltip label="破环插点光缆" align="center" min-width="300" prop="parFiberName">
            <template slot-scope="scope">
              <a class="linkColumn" @click="getRouterLink(scope.row)">
                {{ scope.row.parFiberName }}
              </a>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" min-width="200">
            <template slot-scope="scope">
              <el-button size="mini" type="primary" @click.native="openInfo(scope.row)">详情</el-button>
              <el-button size="mini" type="primary" @click.native="handleDelete(scope.row.parFiber)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
          @pagination="getList" />
      </div>
    </div>
    <el-dialog v-dialogDrag :custom-class="infoBox.class" :visible.sync="infoBox.show" v-if="infoBox.show"
      :width="infoBox.width" :modal="false" :close-on-click-modal="false">
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane label="详情" name="first">
          <el-descriptions title="基本信息" direction="vertical" :column="5" border>
            <template slot="extra">
              <el-button v-if="!isEditMode" type="primary" size="mini" @click="enterEditMode">编辑</el-button>
              <div v-else>
                <el-button type="success" size="mini" @click="saveData">保存</el-button>
                <el-button size="mini" @click="cancelEdit" v-if="!isAddMode">取消</el-button>
              </div>
            </template>
            <el-descriptions-item label="破环插点光缆"><el-select @change="setDefaultMsg" v-if="isAddMode" size="mini"
                style="width: 100%;" v-model="fiberData[0].parFiber" placeholder="请选择" clearable filterable
                :filter-method="filterMethod1" v-el-select-loadmore="loadMore(rangeNumber)">
                <el-option v-for="item in options2.slice(0, rangeNumber)" :key="item.value" :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select><span v-else>{{ fiberData[0].parFiberName }}</span></el-descriptions-item>
            <el-descriptions-item label="插入站点">
              <el-input v-if="isEditMode" v-model="fiberData[0].zResName" size="mini" placeholder="请输入名称" /><span
                v-else>{{ fiberData[0].zResName }}</span>
            </el-descriptions-item>
            <el-descriptions-item :label="`距${fiberData[0].aResName}距离（km）`" :span="3"> <el-input-number
                style="width: 100%;" :precision="2" :step="0.1" v-if="isEditMode" v-model="fiberData[0].fiberLength"
                size="mini" placeholder="请输入名称" />
              <el-tag v-else>{{ fiberData[0].fiberLength }}</el-tag></el-descriptions-item>
            <!-- <el-descriptions-item label="投运时间"><el-date-picker v-if="isEditMode" v-model="infoBox.begTime" type="date"
                size="mini" placeholder="选择日期" value-format="yyyy-MM-dd" />
              <span v-else>{{ infoBox.data.NAME || '--' }}</span></el-descriptions-item> -->
            <el-descriptions-item label="左侧光缆名称"><el-input v-if="isEditMode" v-model="fiberData[0].fiberName" size="mini"
                placeholder="请输入名称" />
              <span v-else>{{ fiberData[0].fiberName }}</span></el-descriptions-item>
            <input type="hidden" v-model="fiberData[0].ID">
            <el-descriptions-item label="光缆长度">
              <el-input-number style="width: 100%;" :precision="2" :step="0.1" v-if="isEditMode"
                v-model="fiberData[0].fiberLength" placeholder="请输入长度（km）" />
              <el-tag v-else>{{ fiberData[0].fiberLength }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="芯数" :span="2">
              <el-input-number style="width: 100%;" v-if="isEditMode" v-model="fiberData[0].lineNumber"
                placeholder="请输入芯数" />
              <el-tag v-else>{{ fiberData[0].lineNumber }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="投运时间"><el-date-picker v-if="isEditMode" v-model="fiberData[0].operateDate"
                type="date" size="mini" placeholder="选择日期" value-format="yyyy-MM-dd" />
              <span v-else>{{ fiberData[0].operateDate }}</span></el-descriptions-item>
            <el-descriptions-item label="右侧光缆名称"><el-input v-if="isEditMode" v-model="fiberData[1].fiberName" size="mini"
                placeholder="请输入名称" />
              <span v-else>{{ fiberData[1].fiberName }}</span></el-descriptions-item>
            <input type="hidden" v-model="fiberData[1].ID">
            <el-descriptions-item label="光缆长度（km）">
              <el-input-number style="width: 100%;" :precision="2" :step="0.1" v-if="isEditMode"
                v-model="fiberData[1].fiberLength" placeholder="请输入长度（km）" />
              <el-tag v-else>{{ fiberData[1].fiberLength }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="芯数" :span="2">
              <el-input-number style="width: 100%;" v-if="isEditMode" v-model="fiberData[1].lineNumber"
                placeholder="请输入芯数" />
              <el-tag v-else>{{ fiberData[1].lineNumber }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="投运时间"><el-date-picker v-if="isEditMode" v-model="fiberData[1].operateDate"
                type="date" size="mini" placeholder="选择日期" value-format="yyyy-MM-dd" />
              <span v-else>{{ fiberData[1].operateDate }}</span></el-descriptions-item>
          </el-descriptions>
          <div style="margin-top: 20px;">
            <el-descriptions title="破环插点示意图" :column="4" direction="vertical" border>
            </el-descriptions>
          </div>

          <div class="diagram-container">
            <svg :width="svgWidth" :height="svgHeight" class="fiber-diagram">
              <line x1="50" y1="75" x2="550" y2="75" stroke="#666" stroke-width="3" stroke-dasharray="5 3" />
              <!-- 起始站点 -->
              <rect x="30" y="50" width="80" height="50" rx="5" fill="#409EFF" />
              <text x="70" y="85" fill="white" text-anchor="middle">
                {{ fiberData[0].aResName }}
              </text>
              <!-- 终止站点 -->
              <rect x="490" y="50" width="80" height="50" rx="5" fill="#409EFF" />
              <text x="530" y="85" fill="white" text-anchor="middle">
                {{ fiberData[1].aResName }}
              </text>
              <circle id="breakPointMarker" :cx="svgWidth * this.breakPosition" :cy="breakPoint.y" r="8" fill="#F56C6C"
                stroke="#fff" stroke-width="2" @click="handleBreakClick" />
              <!-- 断开处动画效果 -->
              <animate href="#breakPointMarker" attributeName="opacity" values="1;0.5;1" dur="1s"
                repeatCount="indefinite" />
              <!-- 文字标注 -->
              <text :x="breakPoint.x" :y="breakPoint.y - 15" fill="#F56C6C" text-anchor="middle">
                破点
              </text>
              <!-- 连接线 -->
              <line :x1="svgWidth * this.breakPosition" y1="75" :x2="svgWidth * this.breakPosition" y2="120"
                stroke="#67C23A" stroke-width="2" />
              <!-- 站点方块 -->
              <rect :x="svgWidth * this.breakPosition - 40" y="120" width="80" height="40" rx="5" fill="#67C23A" />
              <!-- 站点文字 -->
              <text :x="svgWidth * this.breakPosition" y="145" fill="white" text-anchor="middle">
                {{ fiberData[0].zResName }}
              </text>
            </svg>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </div>
</template>

<script>
import { getFiberList, getTySiteList, getTyFiberList, _debounce, getRelateFiberDetail, saveFiberBreak, getDefaultMsg, deleteFiberBreak } from "@/api/manageFiber/fiberBreak";
import { tableDropDownBox, getModelId } from "@/api/zdwh/zdzywh";
import info from '@/views/commonPage/info/index'
export default {
  name: 'fiberYm',
  components: { info },
  data() {
    return {
      breakPosition: 0.5,
      rangeNumber: 10,
      // 示意图尺寸
      svgWidth: 600,
      svgHeight: 150,
      // 示例数据（需替换为真实数据源）
      fiberData: [],
      isEditMode: false, // 控制编辑模式
      isAddMode: false,
      formData: {},      // 表单数据
      originalData: [],  // 原始数据备份
      options1: [],
      options2: [],
      coreOptions: [
        { value: 24, label: '24芯' },
        { value: 48, label: '48芯' }
      ],
      queryParams: {
        pageNum: 1,
        pageSize: 15,
        fiberName: '',
        aResobjid: '',
        zResName: ''
      },
      siteList: [],
      isFaultTypes: [{
        value: '1',
        label: '是'
      }, {
        value: '0',
        label: '否'
      }],
      total: 0,
      fiberList: [],
      tyFiberList: [],
      loading: false,

      tabData: [],
      tabTotal: 0,
      tabLoading: false,
      show: false,
      modelObj: {},
      searchForm: {
        name: '',
        A_RESOBJID: '',
        Z_RESOBJID: '',
        pageSize: 15,
        pageNum: 1,
      },
      A_RESOBJID: {},
      Z_RESOBJID: {},
      A_RESOBJID_Loading: true,
      Z_RESOBJID_Loading: true,
      id: '',
      infoBox: {
        show: false,
        data: {},
        width: '1800px',
        class: 'framePosition',
      },
      activeName: 'first',
    }
  },
  watch: {},
  created() {
    this.handleQuery()
    this.getTySiteList()
    this.getTableData()
    getModelId({ tableName: 'SG_TCDEV_FIBER_B' }).then(res => {
      this.modelObj = res.data
      this.getOption('A_RESOBJID')
      this.getOption('Z_RESOBJID')
    })
  },
  computed: {
    // 计算断开点坐标
    breakPoint() {
      return {
        x: this.svgWidth * this.breakPosition,
        y: this.svgHeight / 2,
      }
    }
  },
  methods: {
    setDefaultMsg(e) {
      getDefaultMsg(e).then(res => {
        this.fiberData[0].parFiberName = res.breakFiberName
        this.fiberData[0].aResobjid = res.aresId
        this.fiberData[0].aResName = res.aresName
        this.fiberData[0].lineNumber = res.leftFiberNumber
        this.fiberData[0].fiberLength = res.leftFiberLength
        this.fiberData[0].operateDate = res.leftFiberOperateDate
        this.fiberData[1].aResobjid = res.zresId
        this.fiberData[1].aResName = res.zresName
        this.fiberData[1].lineNumber = res.rightFiberNumber
        this.fiberData[1].fiberLength = res.rightFiberLength
        this.fiberData[1].operateDate = res.rightFiberOperateDate
        this.breakPosition = 0.5
      })
    },

    filterMethod: _debounce(function (filterVal) {
      if (filterVal) {
        let filterArr = this.siteList.filter((item) => {
          return item.label.toLowerCase().includes(filterVal.toLowerCase())
        })
        this.options1 = filterArr;
      } else {
        this.options1 = this.siteList;
      }
    }, 500),

    filterMethod1: _debounce(function (filterVal) {
      if (filterVal) {
        let filterArr = this.tyFiberList.filter((item) => {
          return item.label.toLowerCase().includes(filterVal.toLowerCase())
        })
        this.options2 = filterArr;
      } else {
        this.options2 = this.tyFiberList;
      }
    }, 500),

    loadMore(n) {
      return () => (this.rangeNumber += 5); //
    },
    // 点击断点处理
    handleBreakClick() {
      this.$confirm('是否要修复此断点?', '提示', {
        confirmButtonText: '立即修复',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 调用修复接口
        this.repairFiberBreak()
      })
    },

    // 示例修复方法
    async repairFiberBreak() {
      try {
        this.$message.success('修复指令已发送')
      } catch (err) {
        this.$message.error('操作失败')
      }
    },
    enterEditMode() {
      this.originalData = [...this.fiberData]; // 深拷贝原始数据
      this.isEditMode = true;
    },
    cancelEdit() {
      this.fiberData = [...this.originalData];    // 恢复原始数据
      this.isEditMode = false;
    },
    async saveData() {
      let FiberBreakVo = {}
      try {
        FiberBreakVo.breakFiberId = this.fiberData[0].parFiber
        FiberBreakVo.breakFiberName = this.fiberData[0].parFiberName
        FiberBreakVo.breakAId = this.fiberData[0].aResobjid
        FiberBreakVo.breakZId = this.fiberData[1].aResobjid
        FiberBreakVo.breakAName = this.fiberData[0].aResName
        FiberBreakVo.breakZName = this.fiberData[1].aResName
        FiberBreakVo.insertSite = this.fiberData[0].zResName
        FiberBreakVo.instanceToBegin = this.fiberData[0].fiberLength
        FiberBreakVo.leftFiberId = this.fiberData[0].ID
        FiberBreakVo.leftFiberName = this.fiberData[0].fiberName
        FiberBreakVo.leftFiberLength = this.fiberData[0].fiberLength
        FiberBreakVo.leftFiberNumber = this.fiberData[0].lineNumber
        FiberBreakVo.leftFiberOperateDate = this.fiberData[0].operateDate
        FiberBreakVo.rightFiberId = this.fiberData[1].ID
        FiberBreakVo.rightFiberName = this.fiberData[1].fiberName
        FiberBreakVo.rightFiberLength = this.fiberData[1].fiberLength
        FiberBreakVo.rightFiberNumber = this.fiberData[1].lineNumber
        FiberBreakVo.rightFiberOperateDate = this.fiberData[1].operateDate
        FiberBreakVo.state = this.isAddMode
        if (FiberBreakVo.breakFiberId != null && FiberBreakVo.breakFiberId != '') {
          // 调用保存API
          saveFiberBreak(FiberBreakVo).then((res) => {
            this.fiberData = res.rows;
            this.breakPosition = this.fiberData[0].fiberLength / (this.fiberData[0].fiberLength + this.fiberData[1].fiberLength)
            this.$message.success('保存成功');
            this.isEditMode = false;
            if (this.isAddMode == true) {
              this.infoBox.show = false
              this.isAddMode = false;
            }
            this.getList();

          })
        } else {
          this.$message.error('请选择光缆');
        }
      } catch (err) {
        this.$message.error('保存失败');
      }
    },
    handleClick(tab, event) {
      console.log(tab, event);
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    getList() {
      this.loading = true
      getFiberList(this.queryParams).then(res => {
        this.fiberList = res.rows;
        this.total = res.total;
        this.loading = false
      })
    },
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    openInfo(row) {
      this.infoBox.width = '1600px'
      this.infoBox.class = ''
      if (row != null && row.parFiber != null) {
        this.isEditMode = false;
        this.isAddMode = false
        getRelateFiberDetail(row.parFiber).then(res => {
          this.fiberData = res.rows;
          this.breakPosition = this.fiberData[0].fiberLength / (this.fiberData[0].fiberLength + this.fiberData[1].fiberLength)
          this.infoBox.show = true
        })
      } else {
        this.enterEditMode()
        this.fiberData = [
          {
            ID: '',
            fiberName: '',
            parFiber: '',
            parFiberName: '',
            aResobjid: '',
            aResName: '起始',
            zResName: '',
            lineNumber: 0,
            fiberLength: 0,
            operateDate: ''
          },
          {
            ID: '',
            fiberName: '',
            parFiber: '',
            parFiberName: '',
            aResobjid: '',
            aResName: '起始',
            zResName: '',
            lineNumber: 0,
            fiberLength: 0,
            operateDate: ''
          }
        ]
        this.isAddMode = true
        this.breakPosition = 0.5
        this.infoBox.show = true
        this.getTyFiberList()
      }
    },
    getOption(data) {
      tableDropDownBox({
        modelId: this.modelObj.modelId,
        modeVersion: this.modelObj.modelVersion,
        attribute: data,
        pageNum: 0,
        pageSize: 20,
      }).then((res) => {
        if (res.total > 0) {
          this[data].list = res.rows
          this[data].total = res.total
          this.$nextTick(() => {
            this[data + '_Loading'] = false
          })
        }
      })
    },
    /** 重置按钮操作 */
    resetTableQuery() {
      this.resetForm("searchForm");
      this.getTableData();
    },
    handleDelete(id) {
      deleteFiberBreak(id).then(res => {
        this.show = false
        this.$confirm('是否要删除相关光缆?', '提示', {
          confirmButtonText: '立即删除',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$message({
            type: 'success',
            message: '操作成功'
          })
          this.getList();
        })

      })
    },



    getTySiteList() {
      this.tabLoading = true
      getTySiteList().then((res) => {
        this.siteList = res
        this.filterMethod()
      })
    },
    getTyFiberList() {
      this.tabLoading = true
      getTyFiberList().then((res) => {
        this.tyFiberList = res
        if (this.fiberData[0].parFiber != null && this.fiberData[0].parFiber != '') {
          let filterArr = this.tyFiberList.filter((item) => {
            return item.value.toLowerCase().includes(this.fiberData[0].parFiber.toLowerCase())
          })
          this.options2 = filterArr;
        } else {
          this.options2 = res
        }

      })
    },
    getTableData() {
      this.tabLoading = true
      getFiberList(this.searchForm).then((res) => {
        this.tabData = res.rows
        this.tabTotal = res.total
        this.tabLoading = false
      })
    },
    remoteMethod2(val, data) {
      this.searchOptionStr = val
      tableDropDownBox({
        modelId: this.modelObj.modelId,
        modeVersion: this.modelObj.modelVersion,
        attribute: data,
        name: this.searchOptionStr,
        pageNum: 0,
        pageSize: 20,
      }).then((res) => {
        this[data + '_Loading'] = true
        this[data].list = res.rows
        this[data].total = res.total
        this.$nextTick(() => {
          this[data + '_Loading'] = false
        })
      })
    },
    /* 格式序列化列 */
    snFormatter: function (row, col, value, index) {
      return this.queryParams.pageSize * (this.queryParams.pageNum - 1) + index + 1;
    },
    //光缆跳转
    getRouterLink(row) {
      this.$router.push({
        path: '/fibersource/fiber',
        query: {
          search: JSON.stringify({ ID: row.parFiber }),
          refresh: true,
        }
      })
    },
    switchingModes(data) {
      this.infoBox.width = data ? '1800px' : '500px'
      this.infoBox.class = data ? '' : 'framePosition'
    },
  },
  directives: {
    'el-select-loadmore': (el, binding) => {
      const SELECTWRAP_DOM = el.querySelector(".el-select-dropdown .el-select-dropdown__wrap");
      if (SELECTWRAP_DOM) {
        SELECTWRAP_DOM.addEventListener("scroll", function () {
          const condition = this.scrollHeight - this.scrollTop <= this.clientHeight;
          if (condition) binding.value();
        });
      }
    },
  }
}
</script>
<style scoped>
.diagram-container {
  border-top: 1px solid #eee;
  padding-top: 5px;
  overflow: auto;
  /* 确保内容溢出时可滚动 */
  height: 200px;
}

.fiber-diagram {
  margin-left: 580px;
  min-width: 600px;
  min-height: 200px;
  /* 保持最小宽度 */
  /* background: #f8f9fa;  */
}

text {
  font-size: 12px;
  /* 统一文字大小 */
  user-select: none;
  /* 禁止文字选中 */
}
</style>
