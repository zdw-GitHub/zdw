<template>
  <div class="app-container ysjgl">
    <el-row :gutter="20">
      <el-col :span="4" :xs="24">
        <!-- 左侧树区域保持不变 -->
        <div class="head-container">
          <el-input v-model="filterText" placeholder="元模型名称" clearable size="small" prefix-icon="el-icon-search"
            style="margin-bottom: 10px;width: 100%;" />
        </div>
        <div class="tree-container">
          <el-tree class="filter-tree" :data="data" highlight-current accordion node-key="id"
          :default-expand-all="true" :filter-node-method="filterNode" ref="tree"
            style="font-size: 14px; width: 100%;" @current-change="handleTreeCurrentChange">
            <span style="width: 100%; height: 100%; display: block" class="custom-tree-node" slot-scope="{ node, data }">
              <span class="tree-node-label" :class="{ 'tree-node-selected': currentSelectedId === data.id }"
                style="width: 100%; display: block; line-height: 22px; padding: 2px 8px;"
                @click="searchTable(data.id, data.tableName)">
                <i :class="data.icon" :style="data.color"></i>{{ data.label || data.categoryName || data.tableName }}
              </span>
            </span>
          </el-tree>
        </div>
      </el-col>
      <el-col :span="20" :xs="24" class="right-content">
        <el-row :gutter="16" class="top-operate-bar">
          <el-col :span="14">
            <div class="flex items-center">
              <!-- 修复1：调换按钮显示条件，确保状态正确 -->
              <el-button type="text" size="mini" icon="el-icon-chevron-down" @click="toggleCollapse" class="ml-2"
                v-if="isCollapsed"></el-button>
              <el-button type="text" size="mini" icon="el-icon-chevron-up" @click="toggleCollapse" class="ml-2"
                v-else></el-button>
            </div>
          </el-col>
          <el-col :span="10" class="operate-buttons">
            <!-- 操作按钮保持不变 -->
            <!-- <el-button type="primary" plain icon="el-icon-upload2" size="mini" :disabled="ifSel" @click="uploadTable()"
              v-hasPermi="['mm:model:release']" class="btn-compact">发布</el-button> -->
            <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="addTable()" v-if="versionShow"
              v-hasPermi="['mm:model:add']" class="btn-compact">新增</el-button>
            <el-button type="success" plain icon="el-icon-circle-check" size="mini" :disabled="ifSel" v-if="versionShow"
              @click="saveTabData('tableForm')" v-hasPermi="['mm:model:keep']" class="btn-compact">保存</el-button>
            <!-- <el-button type="primary" plain :disabled="ifSel" icon="el-icon-document-copy" size="mini" v-if="versionShow"
              @click="copyTabData()" v-hasPermi="['mm:model:copy']" class="btn-compact">复制</el-button> -->
            <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="ifSel" v-if="versionShow"
              @click="delTable()" v-hasPermi="['mm:model:remove']" class="btn-compact">删除</el-button>
            <el-button plain icon="el-icon-coin" size="mini" @click="classifyDrawer = true" v-if="versionShow"
              class="btn-compact">类目管理</el-button>
          </el-col>
        </el-row>

        <!-- 表单区域保持不变 -->
        <div class="form-container">
          <el-form ref="tableForm" :rules="rules" :model="tableForm" size="small" class="compact-form">
            <div class="el_group compact-group" v-if="!isCollapsed"  :class="{ 'collapsed': isCollapsed }">
              <el-form-item label="表名" prop="tableName" class="form-item-compact">
                <el-input v-model="tableForm.tableName" size="mini" readonly disabled ></el-input>
              </el-form-item>
              <el-form-item label="名称" prop="tableNameCn" class="form-item-compact">
                <el-input v-model="tableForm.tableNameCn" size="mini"></el-input>
              </el-form-item>
              <el-form-item label="类目" prop="categoryName" class="form-item-compact">
                <el-select filterable v-model="tableForm.categoryName" placeholder="请选择" size="mini">
                  <el-option v-for="item in categoryList" :key="item.id" :label="item.categoryNameCn"
                    :value="item.id"></el-option>
                </el-select>
              </el-form-item>
            </div>
            <div class="el_group compact-group">
              <!-- <el-form-item label="标签" prop="labelName" class="form-item-compact">
                <el-select filterable v-model="tableForm.labelName" @change="$forceUpdate()" placeholder="请选择" multiple
                  size="mini">
                  <el-option v-for="item in labelNameList" :key="item.id" :label="item.labelName" :value="item.id"
                    :class="item.remark === 1 || item.remark === '1' ? 'red' : ''"></el-option>
                </el-select>
              </el-form-item> -->
              <!-- <el-form-item label="版本号" class="form-item-compact">
                <el-select filterable v-model="tableForm.modelVersion" placeholder="请选择"
                  @change="ifVersion(tableForm.modelVersion)" size="mini">
                  <el-option v-for="item in tableList" :key="item" :label="item" :value="item"></el-option>
                </el-select>
              </el-form-item> -->
              <!-- <el-form-item label="状态" class="form-item-compact">
                <el-select disabled readonly filterable v-model="tableForm.modelStatus" placeholder="请选择" size="mini">
                  <el-option v-for="item in tableStatusList" :key="item.id" :label="item.statusName"
                    :value="item.id"></el-option>
                </el-select>
              </el-form-item> -->
            </div>
          </el-form>
        </div>

        <!-- 标签栏保持不变 -->
        <el-row :gutter="16" class="tab-bar-container">
          <el-col :span="24">
            <div class="tabBarArea tit">
              <div class="tabBarBtn" :class="[tabName == 'Tab1' ? 'tabBarBtnActive' : '']" @click="tabName = 'Tab1'">表属性
              </div>
              <div class="tabBarBtn" :class="[tabName == 'Tab2' ? 'tabBarBtnActive' : '']" @click="tabName = 'Tab2'">约束条件
              </div>
              <div class="tabBarBtn" :class="[tabName == 'Tab7' ? 'tabBarBtnActive' : '']" @click="tabName = 'Tab7'">
                字段来源树配置</div>
            </div>
          </el-col>
        </el-row>

        <!-- 修复2：调整表格容器高度计算方式 -->
        <div class="table-wrapper">
          <jsysj v-if="tabName === 'Tab1'" :modelId="modelId" :modelName="modelName" :modelVersion="modelVersion"
            :versionShow="versionShow" ref="jsysj" class="full-height-table" :height="tableHeight"></jsysj>
          <ywysj v-if="tabName === 'Tab2'" :modelId="modelId" :modelName="modelName" :modelVersion="modelVersion"
            ref="ywysj" :versionShow="versionShow" class="full-height-table" :height="tableHeight"></ywysj>
          <configTree v-if="tabName === 'Tab7'" :modelId="modelId" class="full-height-table" :height="tableHeight">
          </configTree>
        </div>
      </el-col>
    </el-row>

    <!-- 对话框部分保持不变 -->
    <el-dialog title="发布版本" width="450px" :close-on-click-modal="false" :visible.sync="uploadTableDrawer"
      style="margin-top: 14vh !important;" class="doBox">
      <el-form :model="uploadTableForm" :rules="uploadrules" ref="uploadTableForm" label-width="80px"
        style="text-align: center;margin-left:18px">
        <!-- <el-form-item label="版本号" prop="modelVersion">
          <el-input v-model="uploadTableForm.modelVersion"></el-input>
        </el-form-item> -->
        <el-form-item label="更新内容" style="margin-top:30px">
          <el-input type="textarea" v-model="uploadTableForm.remark"></el-input>
        </el-form-item>
        <el-form-item label="备注" style="margin-top:30px">
          <el-input type="textarea" v-model="uploadTableForm.updateLog"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="saveAddTree()">确 定</el-button>
        <el-button @click="uploadTableDrawer = false">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog title="复制版本" width="450px" :close-on-click-modal="false" :visible.sync="copyTabDataDrawer"
      style="margin-top: 14vh !important;" class="doBox">
      <el-form :model="copyTableForm" :rules="copyrules" ref="copyTableForm" label-width="80px"
        style="text-align: center;margin-left:18px">
        <el-form-item label="名称" prop="columnName">
          <el-input v-model="copyTableForm.columnName"></el-input>
        </el-form-item>
        <el-form-item label="中文名称" style="margin-top:30px">
          <el-input v-model="copyTableForm.columnNameCn"></el-input>
        </el-form-item>
        <el-form-item label="版本号" prop="modelVersion" style="margin-top:30px">
          <el-select v-model="copyTableForm.modelVersion">
            <el-option v-for="item in versionList" :key="item" :label="item" :value="item">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="saveCopyTree()">确 定</el-button>
        <el-button @click="copyTabDataDrawer = false">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog width="450px" title="新增元模型" class="doBox" :close-on-click-modal="false" :visible.sync="addTableDrawer"
      style="margin-top: 14vh !important;">
      <el-form :model="addTableForm" :rules="addrules" ref="addTableForm" label-width="80px"
        style="text-align: center;margin-left:18px">
        <el-form-item label="所属类目" prop="categoryName">
          <el-select filterable v-model="addTableForm.categoryName" placeholder="请选择">
            <el-option v-for="item in categoryList" :key="item.id" :label="item.categoryNameCn" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="数据表名" prop="tableName" style="margin-top:30px">
          <el-input v-model="addTableForm.tableName"></el-input>
        </el-form-item>
        <el-form-item label="中文名称" prop="tableNameCn" style="margin-top:30px">
          <el-input v-model="addTableForm.tableNameCn"></el-input>
        </el-form-item>
        <!-- <el-form-item label="标签" style="margin-top:30px" prop="labelName">
          <el-select filterable v-model="addTableForm.labelName" placeholder="请选择" multiple @change="$forceUpdate()">
            <el-option v-for="item in labelNameList" :key="item.id" :label="item.labelName" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item> -->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="saveAddTable('addTableForm')">确 定</el-button>
        <el-button @click="addTableDrawer = false">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog title="类目管理" width="60%" :close-on-click-modal="false" :visible.sync="classifyDrawer"
      style="margin-top: 2vh !important;" class="doBox" :modal-append-to-body="false" @close='closeClassifyDialog'>
      <lmgl></lmgl>
    </el-dialog>
  </div>
</template>

<script>
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import qs from 'qs'
import ywysj from '../ysjgl/ysjgl_compo/ywysj.vue'
import jsysj from '../ysjgl/ysjgl_compo/jsysj.vue'
import czysj from '../ysjgl/ysjgl_compo/czysj.vue'
import zhgl from '../ysjgl/ysjgl_compo/zhgl.vue'
import aqysj from '../ysjgl/ysjgl_compo/aqysj.vue'
import request from '@/utils/request'
import Lmgl from "../sjbz/lmgl";
import configTree from './ysjgl_compo/configTree.vue'
export default {
  name: 'Ysjgl',
  data() {
    return {
      isCollapsed: false,
      tableHeight: '600px',
      currentSelectedId: '',
      isExpandedArray: [1],
      tabName: 'Tab1',
      ifSel: true,
      glRelationTableName: '',
      glTableCluName: '',
      uploadTableDrawer: false,
      copyTabDataDrawer: false,
      addTableDrawer: false,
      addTableCluDrawer: false,
      glCluDrawer: false,
      mainTableCluVisible: false,
      classifyDrawer: false,
      selForm: {
        cluName: ''
      },
      dbList: [],
      tableList: [],
      tableStatusList: [
        { id: '0', statusName: '未发布' },
        { id: '1', statusName: '发布' }
      ],
      modelVersion: '',
      versionList: [],
      seeClu: true,
      seeAlias: false,
      modelId: '',
      modelName: '',
      tableData: [],
      tableDataClu: [],
      glTableCluFormD: {},
      tableForm: {
        tableName: '',
        tableNameCn: '',
        categoryaName: '',
        labelNameList: [{ id: '', labelName: '' }],
        modelVersion: '',
        modelStatus: '',
        updateLog: ''
      },
      labelNameList: {
      },
      categoryList: {
      },
      uploadTableForm: {
        modelVersion: '',
        remark: '',
        updateLog: ''
      },
      copyTableForm: {
        columnName: '',
        columnNameCn: '',
        modelId: '',
        modelVersion: ''
      },
      addTableForm: {
        tableName: '',
        tableNameCn: '',
        categoryName: '',
        labelNameList: [{ id: '', labelName: '' }],
        modelVersion: '',
        modelStatus: '',
        updateLog: ''
      },
      versionShow: true,
      glCluObj: {},
      filterText: '',
      editDrawer: false,
      data: [{
        id: '1',
        label: '元模型',
        icon: 'el-icon-s-home',
        color: 'color: #FFC000',
        children: []
      }],
      uploadrules: {
        modelVersion: [
          {
            required: true,
            message: '请输入版本号',
            trigger: 'blur'
          }
        ]
      },
      copyrules: {
        modelVersion: [
          {
            required: true,
            message: '请输入版本号',
            trigger: 'blur'
          }
        ],
        columnName: [
          {
            required: true,
            message: '请输入名称',
            trigger: 'blur'
          }
        ]
      },
      rules: {
        tableName: [
          { required: true, message: '请输入元模型名称', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              if (/^\w+$/.test(value) === false) {
                callback(new Error('请输入字母/数字/下划线'))
              } else {
                callback()
              }
            },
            trigger: 'blur'
          }
        ],
        tableNameCn: [
          { required: true, message: '请输入元模型中文名称', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              if (/^[\a-\z\A-\Z0-9\u4e00-\u9fe5]+$/.test(value) === false) {
                callback(new Error('请输入中文名称'))
              } else {
                callback()
              }
            },
            trigger: 'blur'
          }
        ]
      },
      addrules: {
        tableName: [
          { required: true, message: '请输入元模型名称', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              if (/^\w+$/.test(value) === false) {
                callback(new Error('请输入字母/数字/下划线'))
              } else {
                callback()
              }
            },
            trigger: 'blur'
          }
        ],
        tableNameCn: [
          { required: true, message: '请输入元模型中文名称', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              if (/^[\a-\z\A-\Z0-9\u4e00-\u9fe5]+$/.test(value) === false) {
                callback(new Error('请输入中文名称'))
              } else {
                callback()
              }
            },
            trigger: 'blur'
          }
        ]
      }
    }
  },

  mounted() {
    // 修复5：初始化时计算高度，且监听窗口 resize
    this.calcTableHeight();
    window.addEventListener('resize', () => {
      this.calcTableHeight();
    });
  },
  beforeDestroy() {
    // 移除窗口监听，避免内存泄漏
    window.removeEventListener('resize', () => {
      this.calcTableHeight();
    });
  },
  watch: {
    isCollapsed: {
      immediate: true,  // 初始化时立即执行
      handler(newVal) {
        this.calcTableHeight();  // 计算高度
        // 折叠时滚动到表格区域（可选优化）
        if (newVal) {
          setTimeout(() => {
            document.querySelector('.table-wrapper').scrollIntoView({ behavior: 'smooth' });
          }, 100);
        }
      }
    },
    // 监听窗口大小变化，适配不同屏幕（可选优化）
    '$window.innerHeight': function () {
      this.calcTableHeight();
    },

    filterText(val) {
    this.$refs.tree.filter(val)
    if (val == '') {
      this.getTree()
    }
  }
  },

activated() {
  let id = this.$route.query.id
  if (id) {
    this.searchTable(id)
    this.isExpandedArray.pop()
    this.isExpandedArray.push(0, 1, id)
    this.getTree()
    delete this.$route.query.id;
  }
},
methods: {

  calcTableHeight() {
      // 1. 获取顶部固定区域的总高度（操作栏 + 标签栏 + 表单（若显示））
      const topBar = document.querySelector('.top-operate-bar');
      const tabBar = document.querySelector('.tab-bar-container');
      const form = document.querySelector('.form-container');

      let topTotalHeight = 0;
      if (topBar) topTotalHeight += topBar.offsetHeight;  // 操作栏高度（约30px）
      if (tabBar) topTotalHeight += tabBar.offsetHeight;  // 标签栏高度（约30px）
      // 表单显示时，加上表单高度（约80px）；隐藏时加0
      if (form && !this.isCollapsed) topTotalHeight += form.offsetHeight;

      // 2. 表格高度 = 浏览器视口高度 - 左侧树区域的上下边距 - 顶部固定区域高度 - 底部预留间距（20px）
      const windowHeight = window.innerHeight;
      const tableHeight = windowHeight - 40 - topTotalHeight - 20;  // 40=页面整体边距，20=底部预留

      // 3. 避免高度过小（最小设为300px，防止表格消失）
      this.tableHeight = Math.max(tableHeight, 300) + 'px';

      // 4. 强制子组件更新高度（关键：确保子组件同步高度）
      this.$nextTick(() => {
        if (this.tabName === 'Tab1' && this.$refs.jsysj) {
          this.$refs.jsysj.$emit('update-height', this.tableHeight);  // 给子组件发事件
        } else if (this.tabName === 'Tab2' && this.$refs.ywysj) {
          this.$refs.ywysj.$emit('update-height', this.tableHeight);
        } else if (this.tabName === 'Tab7' && this.$refs.configTree) {
          this.$refs.configTree.$emit('update-height', this.tableHeight);
        }
      });
    },

    toggleCollapse() {
      this.isCollapsed = !this.isCollapsed;
    },
  handleTreeCurrentChange(data) {
    this.currentSelectedId = data ? data.id : '';
  },
  ifVersion(id) {
    this.modelVersion = id
    this.getTabData()
    if (id !== 'edit') {
      this.versionShow = false
      if (this.tabName === 'Tab1') {
        setTimeout(() => {
          this.$refs.jsysj.getData()
          this.$refs.jsysj.getTabData()
        }, 100)
      } else if (this.tabName === 'Tab2') {
        setTimeout(() => {
          this.$refs.ywysj.getData()
        }, 100)
      } else if (this.tabName === 'Tab3') {
        setTimeout(() => {
          this.$refs.czysj.getData()
        }, 100)
      } else if (this.tabName === 'Tab5') {
        setTimeout(() => {
          this.$refs.zhgl.getData()
        }, 200)
      }
    } else {
      this.versionShow = true
    }
    this.$forceUpdate()
  },
  filterNode(value, data) {
    if (!value) return true
    return (data.label || data.categoryName || data.tableName).indexOf(value) !== -1
  },
  snFormatter: function (row, col, value, index) {
    return index + 1
  },
  tableIdFormatter: function (row, col, value, index) {
    for (var i = 0; i < this.columnTableList.length; i++) {
      if (this.columnTableList[i].id === value) {
        return this.columnTableList[i].tableName
      }
    }
    return value
  },
  handleCurrentChange(row) {
    request.get(localStorage.URL + '/dbtablecolumn/' + row.id, qs.stringify({
    })).then((res) => {
      this.glCluObj = res.data
    }).catch(e => {
      this.$message({
        message: e,
        type: 'error',
        center: true,
        duration: 0,
        showClose: true
      })
    })
  },
  uploadTable: function () {
    this.uploadTableDrawer = true
    this.uploadTableForm.modelVersion = ''
    if (this.tableForm.updateLog === '' || this.tableForm.updateLog == undefined) {
      this.uploadTableForm.updateLog = ''
    } else {
      this.uploadTableForm.updateLog = this.tableForm.updateLog
    }
    this.tabName = 'Tab5'
  },
  copyTabData: function () {
    this.copyTableForm.columnName = this.tableForm.tableName
    this.copyTableForm.columnNameCn = this.tableForm.tableNameCn
    this.getversionList()
    this.copyTabDataDrawer = true
  },
  addTableClu: function () {
    for (var a in this.addTableCluForm) {
      this.addTableCluForm[a] = ''
    }
    this.addTableCluForm.isPrimaryKey = '0'
    this.addTableCluDrawer = true
  },
  addTable: function () {
    this.addTableForm.tableName = ''
    this.addTableForm.tableNameCn = ''
    this.addTableForm.categoryName = ''
    this.$set(this.addTableForm, "labelName", [])
    this.addTableDrawer = true
  },
  saveAddTable: function (formName) {
    this.$refs[formName].validate((valid) => {
      if (valid) {
        var metadataBuzInfoVo = {
          tableName: this.addTableForm.tableName,
          tableNameCn: this.addTableForm.tableNameCn,
          categoryNameId: this.addTableForm.categoryName,
          labelId: this.addTableForm.labelName
        }
        request({
          headers: {
            'Content-Type': 'application/json'
          },
          method: 'post',
          url: '/model',
          data: metadataBuzInfoVo
        }).then((res) => {
          this.addTableDrawer = false
          this.getTree()
          if (res.code === 200 || res.code === '200') {
            this.$message({
              message: res.msg,
              type: 'success',
              center: true
            })
          }
        }).catch(e => { })
      } else {
        return false
      }
    })
  },
  searchTable: function (id, name) {
    if (id !== undefined) {
      this.currentSelectedId = id;
      this.modelId = id;
      this.modelVersion = 'edit';
      this.getlabelNameList();
      this.getTabData();
      this.$refs.tableForm.resetFields();
      if (this.tabName === 'Tab1') {
        setTimeout(() => {
          this.$refs.jsysj.getData()
          this.$refs.jsysj.getTabData()
        }, 100)
      } else if (this.tabName === 'Tab2') {
        setTimeout(() => {
          this.$refs.ywysj.getData()
          this.$refs.ywysj.getRulesData()
        }, 100)
      } else if (this.tabName === 'Tab3') {
        setTimeout(() => {
          this.$refs.czysj.getData()
        }, 100)
      } else if (this.tabName === 'Tab5') {
        setTimeout(() => {
          this.$refs.zhgl.getData()
          this.$refs.zhgl.getSqlClu()
          this.$refs.zhgl.getRelationType()
        }, 200)
      }
      this.ifSel = false
    }
  },
  delTable: function () {
    this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      request.delete('mm/model/' + this.modelId + '?tableName=' + this.modelName, qs.stringify({
      })).then((res) => {
        this.modelId = ''
        this.getTabData()
        this.getTree()
        if (res.code === 200 || res.code === '200') {
          this.$message({
            message: res.msg,
            type: 'success',
            center: true
          })
        }
      }).catch(e => {
        this.$message({
          message: e,
          type: 'error',
          center: true,
          duration: 0,
          showClose: true
        })
      })
    }).catch(() => {
      this.$message({
        type: 'info',
        message: '已取消删除'
      })
    })
  },
  closeClassifyDialog() {
    request.get('/category/list', qs.stringify({
    })).then((res) => {
      this.categoryList = res.rows
    }).catch(e => { })
  },
  getlabelNameList() {
    request.get('/label/selectNameId', qs.stringify({
    })).then((res) => {
      this.labelNameList = res.data
    }).catch(e => { })
    request.get('/category/list', qs.stringify({
    })).then((res) => {
      this.categoryList = res.rows
    }).catch(e => { })
    request.get('/model/versions?modelId=' + this.modelId, qs.stringify({
    })).then((res) => {
      this.tableList = res.data
    }).catch(e => { })
  },
  getTree: function () {
    request.get('/model/tree', qs.stringify({
    })).then((res) => {
      res = res.data
      this.data[0].children = res
      for (var i = 0; i < this.data[0].children.length; i++) {
        this.data[0].children[i].children = res[i].tmetaModels
      }
    }).catch(e => { })
  },
  saveTabData: function (formName) {
    this.$refs[formName].validate((valid) => {
      if (valid) {
        var params = {
          tableName: this.tableForm.tableName,
          tableNameCn: this.tableForm.tableNameCn,
          categoryNameId: this.tableForm.categoryName,
          labelId: this.tableForm.labelName,
          modelStatus: this.tableForm.modelStatus,
          updateLog: this.tableForm.updateLog,
          modelVersion: this.tableForm.modelVersion
        }
        request({
          headers: {
            'Content-Type': 'application/json'
          },
          method: 'post',
          url: '/model/keep?modelId=' + this.modelId + '&oldTableName=' + this.modelName,
          data: params
        }).then((res) => {
          this.getTree()
          this.getTabData()
          if (res.code === 200 || res.code === '200') {
            this.$message({
              message: res.msg,
              type: 'success',
              center: true
            })
          }
        }).catch(e => { })
      } else {
        console.log('error submit!!');
        return false;
      }
    })
  },
  getTabData: function () {
    request.get('model/show' + '?modelId=' + this.modelId + '&modelVersion=' + this.modelVersion, qs.stringify({
    })).then((res) => {
      res = res.data
      this.tableForm = res
      this.tableForm.labelName = res.labelId
      this.tableForm.categoryName = res.categoryNameId
      this.modelName = this.tableForm.tableName
      if (res.modelStatus === undefined) {
        this.tableForm.modelStatus = '0'
      }
    }).catch(e => { })
  },
  getData: function () {
    this.tableData = []
    request.get(localStorage.URL + '/metamodelattribute/list?modelId=' + this.modelId, qs.stringify({
    })).then((res) => {
      this.tableData = res.rows
    }).catch(e => {
      this.$message({
        message: e,
        type: 'error',
        center: true,
        duration: 0,
        showClose: true
      })
    })
  },
  saveAddTree: function () {
    var ifAddVersion = true
    if (this.uploadTableForm.modelVersion === '' || this.uploadTableForm.modelVersion === undefined) {
      this.$message({
        message: '请输入版本号',
        type: 'error',
        center: true,
        duration: 0,
        showClose: true
      })
      ifAddVersion = false
    }
    this.$refs.zhgl.getData()
    if (ifAddVersion === true) {
      request.post('/model/release?modelId=' + this.modelId + '&modelVersion=' + this.uploadTableForm.modelVersion + '&remark=' + this.uploadTableForm.remark
        + '&updateLog=' + this.uploadTableForm.updateLog + '&tableName=' + this.modelName, qs.stringify({})).then((res) => {
          this.uploadTableDrawer = false
          this.uploadCopy(this.uploadTableForm.modelVersion)
          this.getlabelNameList()
        }).catch(e => { })
    }
  },
  uploadCopy: function (version) {
    request.post('/model/copy?modelId=' + this.modelId + '&modelVersion=' + version + '&tableName=' +
      this.tableForm.tableName + '&tableNameCn=' + this.tableForm.tableNameCn, qs.stringify({})).then((res) => {
        this.uploadTableDrawer = false
        if (res.code === 200 || res.code === '200') {
          this.$message({
            message: res.msg,
            type: 'success',
            center: true
          })
        }
      }).catch(e => {
        this.$message({
          message: e,
          type: 'error',
          center: true,
          duration: 0,
          showClose: true
        })
      })
  },
  getversionList: function () {
    request.get('/model/versions?modelId=' + this.modelId, qs.stringify({
    })).then((res) => {
      this.versionList = res.data
    }).catch(e => { })
  },
  saveCopyTree: function () {
    var ifAdd = true
    if (this.copyTableForm.modelVersion === '' || this.copyTableForm.modelVersion === undefined) {
      this.$message({
        message: '请输入版本号',
        type: 'error',
        center: true,
        duration: 0,
        showClose: true
      })
      ifAdd = false
    }
    if (this.copyTableForm.columnNameCn !== '' && this.copyTableForm.columnNameCn !== undefined) {
      var re = /^[\a-\z\A-\Z0-9\u4e00-\u9fe5]+$/
      if (!re.test(this.copyTableForm.columnNameCn)) {
        this.$message({
          message: '请输入中文名称',
          type: 'error',
          center: true,
          duration: 0,
          showClose: true
        })
        ifAdd = false
      }
    }
    if (ifAdd) {
      request.post('/model/copy?modelId=' + this.modelId + '&modelVersion=' + this.copyTableForm.modelVersion
        + '&tableName=' + this.copyTableForm.columnName + '&tableNameCn=' + this.copyTableForm.columnNameCn, qs.stringify({})).then((res) => {
          this.uploadTableDrawer = false
          this.tabName = 'Tab1'
          this.$message({
            message: res.msg,
            type: 'success',
            center: true
          })
        }).catch(e => {
          this.$message({
            message: e,
            type: 'error',
            center: true,
            duration: 0,
            showClose: true
          })
        })
      this.copyTabDataDrawer = false
    }
  },
  checkAlias: function () {
    this.seeAlias = false
    this.glRelationTableName = ''
    var tableId = this.glTableCluForm.relationTableId
    var dbId = this.glTableCluForm.relationDbId
    if (tableId === this.tableForm.relationTableId && dbId === this.tableForm.relationDbId) {
      this.glRelationTableName = this.tableForm.relationTableAlias
      this.seeAlias = true
    } else {
      var tableSel = {}
      for (var i = 0; i < this.tableData.length; i++) {
        if (tableId === this.tableData[i].relationTableId && dbId === this.tableData[i].relationDbId) {
          tableSel = this.tableData[i]
        }
      }
      if (tableSel.relationTableName === undefined) {
        this.glRelationTableName = ''
      } else {
        this.glRelationTableName = tableSel.relationTableAlias
        if (tableSel.id !== this.glTableCluForm.id) {
          this.seeAlias = true
        }
      }
    }
  }
},
components: {
  Lmgl,
    ywysj,
    jsysj,
    czysj,
    zhgl,
    aqysj,
    configTree
},
beforeMount() {
  this.getlabelNameList()
  this.getTree()
  this.ifSel = true
}
  }
</script>
<style lang="stylus" rel="stylesheet/stylus" scoped>
.app-container.ysjgl {
  overflow: hidden;
  height: 90vh;  /* 页面占满视口 */
  margin: 0;
  padding: 20px;  /* 整体页面边距（可选，可调整） */
  box-sizing: border-box;  /* 关键：padding不影响高度计算 */
}

.right-content {
  height: 100%;  /* 右侧内容区占满父容器（100vh - 页面边距） */
  overflow: hidden;  /* 禁止右侧整体滚动 */
  padding: 0;  /* 清除右侧内边距，避免影响高度 */
  box-sizing: border-box;
}

.table-wrapper {
  position: relative;  /* 改为relative，依赖父容器高度 */
  width: 100%;
  height: calc(100% - 60px);  /* 100%=right-content高度，60=操作栏+标签栏高度（初始值，后续会被js覆盖） */
  overflow: hidden;  /* 禁止容器滚动，仅表格内部滚动 */
}

/deep/ .table-component {  /* 假设子组件根元素有.table-component类，若没有则替换为子组件实际根类 */
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.tree-container {
  width: 100%;
  height: calc(100vh - 80px);  /* 树高度=视口-页面边距-输入框高度 */
  overflow-y: auto;
  box-sizing: border-box;
}
.tree-node-label {
  cursor: pointer;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 13px;
}

.tree-node-selected {
  background-color: #e6f7ff;
  color: #1890ff;
  font-weight: 500;
}

.top-operate-bar {
  border-bottom: 1px solid #d3dce6;
  padding: 4px 0;
  margin-bottom: 6px;
  height: auto;  /* 自动高度，避免固定高度导致计算错误 */
}

.operate-buttons {
  display: flex;
  flex-wrap: nowrap;
  gap: 4px;
  justify-content: flex-end;
  padding-left: 8px;
}

.btn-compact {
  padding: 2px 6px;
  font-size: 12px;
  min-width: auto;
  height: 24px;
}

.form-container {
  margin-bottom: 6px;
  height: auto;  /* 表单自动高度，避免固定高度 */
}
.compact-form {
  width: 100%;
  margin: 0;
  padding: 0;
}

.compact-group {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin-bottom: 4px;
}

.form-item-compact {
  flex: 1;
  min-width: 160px;
  margin-bottom: 0;
}

.form-item-compact /deep/ .el-form-item__label {
  width: 55px !important;
  padding-right: 4px !important;
  font-size: 12px !important;
  line-height: 24px !important;
}

.form-item-compact /deep/ .el-form-item__content {
  margin-left: 60px !important;
  font-size: 12px !important;
}

.form-item-compact /deep/ .el-input__inner,
.form-item-compact /deep/ .el-select {
  height: 24px !important;
  font-size: 12px !important;
}

.tab-bar-container {
  border-bottom: 1px solid #d3dce6;
  padding: 2px 0;
  margin-bottom: 6px;
  height: auto;
}

.tabBarArea {
  position: relative;
  z-index: 98;
  font-size: 13px !important;
}

.tabBarArea:after {
  display: block;
  content: '';
  clear: both;
}

.tabBarBtn {
  float: left;
  margin-left: 12px !important;
  cursor: pointer;
  white-space: nowrap;
  height: 24px !important;
  line-height: 24px !important;
  padding: 0 !important;
  font-size: 13px !important;
}

.tabBarBtn:hover,
.tabBarBtnActive {
  border-bottom: 2px solid #326BE8 !important;
  border-top: 0 !important;
  border-left: 0 !important;
  border-right: 0 !important;
  color: #326BE8 !important;
}

.table-content-container {
  width: 100%;
  overflow: hidden;
  transition: height 0.2s ease;  /* 新增：平滑过渡高度变化 */
}

.full-height-table {
  width: 100%;
  height: 100%;
  overflow-y: auto;
  overflow-x: auto;
}

.collapsed {
  display: none !important;
}
</style>
