<template>
  <div class="jsysj" :style="{ height: height }">
    <div class="qjcx_con">
      <div class="con_right">
        <!-- 顶部表单区域：移除绝对定位，避免遮挡按钮 -->
        <!-- <div class="r_top"> -->
          <!-- 移除绝对定位的按钮（原top:380px会导致按钮脱离文档流被遮挡） -->
          <!-- <div class="table_space" style="height: 70px;display: flex;align-items: center;"> -->
            <!-- <el-form ref="editTableForm" :rules="rules" :model="tableForm" size="small"> -->
              <!-- <div class="el_group"> -->
             
              
              <!-- </div> -->
            <!-- </el-form> -->
          <!-- </div> -->
        <!-- </div> -->

        <!-- 主内容区域：增加内边距，确保按钮不被边缘遮挡 -->
        <div class="r_bot" style="padding: 0 10px;">
          <!-- 搜索与操作按钮区：调整布局，确保按钮横向排列不换行 -->
          <div class="tit cell" style="display: flex;flex-direction: column;margin-bottom: 10px;">
            <div class="cell" style="width: 100%;text-align: right;display: flex;justify-content: flex-end;align-items: center;gap: 8px;">
              <el-form size="small" ref="selForm" :model="selForm" label-width="80px" style="margin-right: 30vh;display: flex;align-items: center;">
                <el-form-item style="margin: 0;" label="实例名">
                  <el-input v-model="tableForm.dbName" readonly disabled style="width:240px"></el-input>
                </el-form-item>
                <el-form-item label="属性名" style="margin: 0;">
                  <el-input style="width: 240px;height: 32px !important;" v-model="selForm.cluName" placeholder="请输入属性名"></el-input>
                </el-form-item>
              </el-form>
              <el-button :disabled="ifSel" size="mini" type="info" plain @click="getData()" icon="el-icon-search" style="padding:5px 15px;width:80px" v-hasPermi="['mm:tech:list']">搜索</el-button>
              <el-button :disabled="ifSel" size="mini" type="primary" style="padding:5px 15px;width:80px" plain @click="addTableClu()" icon="el-icon-plus" v-if="versionShow" v-hasPermi="['mm:tech:add']">新增</el-button>
              <el-button :disabled="ifSel" size="mini" @click="getData" style="padding:5px 15px;width:80px" plain icon="el-icon-refresh">刷新</el-button>
              <!-- 同步按钮单独换行，避免挤压其他按钮 -->
              <!-- <el-button :disabled="ifSel" size="mini" @click="synchronization" style="padding:5px 15px;width:150px;margin-left: 0;margin-top: 5px;" plain icon="el-icon-connection">同步中间库表结构</el-button> -->
            </div>
          </div>

          <!-- 表格容器：动态高度，确保表格不遮挡分页 -->
          <div class="table_space" :style="{ height: tableContainerHeight, overflow: 'hidden' }">
            <!-- 表格：绑定动态高度，开启横向滚动避免列挤压 -->
            <el-table 
              :data="tableData" 
              stripe 
              :height="tableHeight" 
              tooltip-effect="dark" 
              :header-cell-style="{background:'#F5F5F5'}"
              style="width: 100%;overflow-x: auto;"
                  >
              <el-table-column show-overflow-tooltip label="序号" width="100" align="center" :formatter="snFormatter"></el-table-column>
              <el-table-column show-overflow-tooltip prop="columnName" align="center" label="属性名称" width="150">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.columnName" v-show="scope.$index == cellIndex"></el-input>
                  <p v-show="scope.$index != cellIndex">{{ scope.row.columnName }}</p>
                </template>
              </el-table-column>
              <el-table-column show-overflow-tooltip prop="columnNameCn" align="center" label="中文名" width="150">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.columnNameCn" v-show="scope.$index == cellIndex"></el-input>
                  <p v-show="scope.$index != cellIndex">{{ scope.row.columnNameCn }}</p>
                </template>
              </el-table-column>
              <el-table-column show-overflow-tooltip prop="columnType" align="center" label="属性类型" width="200">
                <template slot-scope="scope">
                  <el-select v-model="scope.row.columnType" v-show="scope.$index == cellIndex" style="width:100px" @change="isNoLenth(scope.row.columnType)">
                    <el-option v-for="item in columnTypeList" :key="item" :label="item" :value="item"></el-option>
                  </el-select>
                  <p v-show="scope.$index != cellIndex">{{ scope.row.columnType }}</p>
                </template>
              </el-table-column>
              <el-table-column show-overflow-tooltip prop="columnLength" align="center" label="属性长度" width="180">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.columnLength" size="mini" :disabled="isLength" v-show="scope.$index == cellIndex" style="width:100px"></el-input>
                  <p v-show="scope.$index != cellIndex">{{ scope.row.columnLength }}</p>
                </template>
              </el-table-column>
              <el-table-column show-overflow-tooltip prop="isPrimaryKey" align="center" label="是否主键" width="180">
                <template slot-scope="scope">
                  <el-select v-model="scope.row.isPrimaryKey" v-show="scope.$index == cellIndex" style="width:100px">
                    <el-option value="1" label="是"></el-option>
                    <el-option value="0" label="否"></el-option>
                  </el-select>
                  <p v-show="scope.$index != cellIndex" v-if="scope.row.isPrimaryKey==='1'">是</p>
                  <p v-show="scope.$index != cellIndex" v-if="scope.row.isPrimaryKey==='0'">否</p>
                </template>
              </el-table-column>
              <el-table-column show-overflow-tooltip prop="isNotNull" align="center" label="是否必填" width="160">
                <template slot-scope="scope">
                  <el-select v-model="scope.row.isNotNull" v-show="scope.$index == cellIndex" style="width:100px">
                    <el-option value="1" label="是"></el-option>
                    <el-option value="0" label="否"></el-option>
                  </el-select>
                  <p v-show="scope.$index != cellIndex" v-if="scope.row.isNotNull==='1'">是</p>
                  <p v-show="scope.$index != cellIndex" v-if="scope.row.isNotNull==='0'">否</p>
                </template>
              </el-table-column>
              <el-table-column show-overflow-tooltip prop="productionForm" align="center" label="产生形式" width="180">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.productionForm" size="mini" :disabled="isLength" v-show="scope.$index == cellIndex" style="width:100px"></el-input>
                  <p v-show="scope.$index != cellIndex">{{ scope.row.productionForm }}</p>
                </template>
              </el-table-column>
              <el-table-column show-overflow-tooltip prop="dataSources" align="center" label="数据来源" width="180">
                <template slot-scope="scope">
                  <el-input type="textarea" autosize v-model="scope.row.dataSources" size="mini" :disabled="isLength" v-show="scope.$index == cellIndex" style="width:100px"></el-input>
                  <p v-show="scope.$index != cellIndex">{{ scope.row.dataSources }}</p>
                </template>
              </el-table-column>
              <el-table-column show-overflow-tooltip prop="remark" align="center" label="备注" width="180">
                <template slot-scope="scope">
                  <el-input type="textarea" autosize v-model="scope.row.remark" size="mini" :disabled="isLength" v-show="scope.$index == cellIndex" style="width:100px"></el-input>
                  <p v-show="scope.$index != cellIndex">{{ scope.row.remark }}</p>
                </template>
              </el-table-column>
              <!-- 操作列：固定右侧，确保不被遮挡 -->
              <el-table-column fixed="right" v-if="versionShow" label="操作" width="220" align="center">
                <template slot-scope="scope">
                  <el-button @click="editTable(scope)" size="mini" type="text" icon="el-icon-edit" v-if="scope.$index != cellIndex" v-hasPermi="['mm:tech:edit']">编辑</el-button>
                  <el-button @click="saveTable(scope)" size="small" type="text" v-else icon="el-icon-circle-check">保存</el-button>
                  <el-button type="text" @click="deletTable(scope)" size="small" icon="el-icon-delete" v-if="scope.$index != cellIndex" v-hasPermi="['mm:tech:remove']">删除</el-button>
                  <el-button @click="cancel()" size="small" type="text" v-else icon="el-icon-circle-close">取消</el-button>
                </template>
              </el-table-column>
            </el-table>

            <!-- 分页组件：固定在表格下方，确保不被挤压 -->
            <!-- <div style="padding: 10px 0; text-align: right;">
              <pagination
                v-show="total>0"
                :total="total"
                :page.sync="pageIndex"
                :limit.sync="pageSize"
                @pagination="getData"
              />
            </div> -->
          </div>
        </div>
      </div>
    </div>

    <!-- 导入对话框：调整位置，避免顶部遮挡 -->
    <div class="importBox">
      <el-dialog 
        title="导入元模型属性" 
        :visible.sync="showImport"  
        :close-on-click-modal="false" 
        style="margin-top: 10vh !important;"  
        width="500px"
      >
        <div style="display: flex;align-items: center;margin-bottom: 20px;">
          <div class="hint" style="margin-right: 0.5rem;">选择导入文件:</div>
          <el-button plain type="primary" @click="chooseAddFile" style="width:120px">选择文件</el-button>
          <input 
            id="addFileInput"
            type="file"
            ref="addFileInput"
            @change="chooseAddFileChange"
            style="display:none;"
          />
        </div>
        <div style="height: 30px;line-height: 30px;">
          <div class="file-name" style="font-size: 14px;color: #666;">{{ addFileName || '未选择文件' }}</div>
        </div>
        <div slot="footer" class="dialog-footer" style="text-align: center;">
          <el-button plain style="padding:5px 25px;width:80px" type="primary" @click="importFile()">确认</el-button>
          <el-button plain style="padding:5px 25px;width:80px" @click="showImport = false">取消</el-button>
        </div>
      </el-dialog>
    </div>

    <!-- 新增属性对话框：调整表单布局，避免控件挤压 -->
    <div>
      <el-dialog
        :visible.sync="addTableCluDrawer"
        title="新增表属性"
        :close-on-click-modal="false"
        class="addbox"
        width="500px"
        style="margin-top: 10vh !important;"
      >
        <el-form 
          :model="addTableForm" 
          label-width="100px" 
          style="margin: 0 auto;width: 80%;" 
          ref="addTableForm" 
          :rules="addrules"
        >
          <el-form-item label="属性名称" prop="columnName" style="margin-bottom: 15px;">
            <el-input v-model="addTableForm.columnName" style="width: 100%;"></el-input>
          </el-form-item>
          <el-form-item label="中文名称" prop="columnNameCn" style="margin-bottom: 15px;">
            <el-input v-model="addTableForm.columnNameCn" style="width: 100%;"></el-input>
          </el-form-item>
          <el-form-item label="属性类型" prop="columnType" style="margin-bottom: 15px;">
            <el-select filterable v-model="addTableForm.columnType" placeholder="请选择" @change="ifColumnType(addTableForm.columnType)" style="width: 100%;">
              <el-option v-for="item in columnTypeList" :key="item" :label="item" :value="item"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="属性长度" prop="columnLength" style="margin-bottom: 15px;">
            <el-input v-model="addTableForm.columnLength" :disabled="ifdatetime" style="width: 100%;"></el-input>
          </el-form-item>
          <el-form-item label="是否主键" prop="isPrimaryKey" style="margin-bottom: 15px;">
            <el-select v-model="addTableForm.isPrimaryKey" @change="isNotKeynull()" style="width: 100%;">
              <el-option value="0" label="否"></el-option>
              <el-option value="1" label="是"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="是否必填" prop="isNotNull" style="margin-bottom: 15px;">
            <el-select v-model="addTableForm.isNotNull" :disabled="mustIs" @change="$forceUpdate()" style="width: 100%;">
              <el-option value="0" label="否"></el-option>
              <el-option value="1" label="是"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="产生形式" prop="productionForm" style="margin-bottom: 15px;">
            <el-input v-model="addTableForm.productionForm" style="width: 100%;"></el-input>
          </el-form-item>
          <el-form-item label="数据来源" prop="dataSources" style="margin-bottom: 15px;">
            <el-input type="textarea" v-model="addTableForm.dataSources" style="width: 100%;"></el-input>
          </el-form-item>
          <el-form-item label="备注" prop="remark" style="margin-bottom: 15px;">
            <el-input type="textarea" v-model="addTableForm.remark" style="width: 100%;"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="saveAddCluData('addTableForm')">确 定</el-button>
          <el-button @click="addTableCluDrawer= false">取 消</el-button>
        </div>
      </el-dialog>
    </div>

    <!-- 导入数据预览对话框：调整表格高度，避免内容溢出 -->
    <div class="importDatas">
      <el-dialog
        title="导入表属性"
        v-dialogDrag
        :close-on-click-modal="false"
        :visible.sync="showImportData"
        width="800px"
        style="margin-top: 8vh !important;"
      >
        <el-table 
          :height="300"  
          tooltip-effect="dark"
          :data="importData"
          style="width: 100%;"
        >
          <el-table-column label="序号" width="60" align="center" :formatter="snFormatter"></el-table-column>
          <el-table-column prop="attributeName" align="center" label="属性名称" width="140">
            <template slot-scope="scope">
              <el-input v-model="scope.row.attributeName" style="width: 100%;"></el-input>
            </template>
          </el-table-column>
          <el-table-column prop="attributeNameCn" align="center" label="中文名称" width="140">
            <template slot-scope="scope">
              <el-input v-model="scope.row.attributeNameCn" style="width: 100%;"></el-input>
            </template>
          </el-table-column>
          <el-table-column prop="attributeType" align="center" label="属性类型" width="150">
            <template slot-scope="scope">
              <el-select filterable v-model="scope.row.attributeType" placeholder="请选择" style="width: 100%;">
                <el-option v-for="item in typeList" :key="item.pubCode" :label="item.pubName" :value="item.pubCode"></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column prop="attributeLength" align="center" label="属性长度" width="120">
            <template slot-scope="scope">
              <el-input v-model="scope.row.attributeLength" style="width: 100%;"></el-input>
            </template>
          </el-table-column>
          <el-table-column prop="isPrimaryKey" align="center" label="是否主键" width="150">
            <template slot-scope="scope">
              <el-select v-model="scope.row.isPrimaryKey" style="width: 100%;">
                <el-option value="0" label="否"></el-option>
                <el-option value="1" label="是"></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column prop="isPrimaryKey" align="center" label="操作" width="100">
            <template slot-scope="scope">
              <el-button plain type="primary" @click="delImportData(scope.$index)" size="mini">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div slot="footer" class="dialog-footer" style="text-align: center;">
          <el-button plain type="primary" :disabled="!importData.length>0" @click="saveImportData()">保存</el-button>
          <el-button plain @click="showImportData = false">取消</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import { getTypeList } from '@/api/ysjgl/jsysj'
import request from '@/utils/request'
import qs from 'qs'
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: 'jsysj',
  props: {
    modelId: String,
    modelName: String,
    modelVersion: String,
    versionShow: Boolean,
    height: {
      type: String,
      default: '600px',
      required: true
    }
  },
  data () {
    return {
      // 动态高度变量
      tableContainerHeight: '50vh', // 表格+分页容器高度
      tableHeight: '300px',          // 表格纯内容高度（容器高度 - 分页高度）
      // 业务数据
      mustIs: false,
      cellIndex: null,
      ifSel: true,
      glRelationTableName: '',
      glTableCluName: '',
      uploadTableDrawer: false,
      addTableCluDrawer: false,
      glCluDrawer: false,
      mainTableCluVisible: false,
      selForm: { cluName: '' },
      // 分页
      pageSize: 250,
      pageIndex: 1,
      total: 0,
      isLength: false,
      dbList: [],
      tableList: [],
      typeList: [],
      categoryList: [],
      mainTableList: [],
      relationModelList: [],
      relationDbList: [],
      relationTableList: [],
      relationTableIdF: '',
      relationTableAlias: '',
      seeClu: true,
      seeAlias: false,
      tableData: [],
      tableDataClu: [],
      glTableCluFormD: {},
      tableForm: { dbName: '' },
      uploadTableForm: {},
      addTableForm: {
        columnName: '',
        columnNameCn: '',
        columnLength: '',
        columnType: '',
        isNotNull: '0',
        isPrimaryKey: '0',
        dataSources: '',
        productionForm: '',
        remark: ''
      },
      // 表单校验
      addrules: {
        columnName: [
          { required: true, message: '请输入属性名称', trigger: 'blur' },
          {
            validator: (rule, value, callback) => {
              if (/^[A-Za-z][A-Za-z0-9_]*$/.test(value) === false) {
                callback(new Error('请输入数字、字母、下划线的组合，必须字母开头'))
              } else {
                callback()
              }
            },
            trigger: 'blur'
          }
        ],
        columnLength: [
          {
            validator: (rule, value, callback) => {
              if (value === '' || value === undefined) {
                callback()
              } else if (/^(\d+,)*\d+$/.test(value) === false) {
                callback(new Error('请输入合法长度（如10或10,2）'))
              } else {
                callback()
              }
            },
            trigger: 'blur'
          }
        ],
        columnNameCn: [
          { required: true, message: '请输入中文名称', trigger: 'blur' }
        ],
        columnType: [
          { required: true, message: '请选择属性类型', trigger: 'blur' }
        ]
      },
      // 导入相关
      showImport: false,
      addFileName: '',
      showImportData: false,
      importData: [],
      editCluForm: {
        id: '',
        attributeName: '',
        attributeNameCn: '',
        attributeType: '',
        attributeLength: '',
        relationTableId: '',
        relationTableName: '',
        relationTableNameCn: '',
        relationTableAlias: '',
        relationDbName: '',
        relationDbNameCn: '',
        relationColomnId: '',
        relationColomnName: '',
        relationColomnNameCn: '',
        relationCondition: '',
        remark: '',
        isPrimaryKey: ''
      },
      glTableCluForm: {
        id: '',
        isExternalConnection: 0,
        relationTableId: '',
        relationTableName: '',
        relationTableNameCn: '',
        relationTableAlias: '',
        relationDbName: '',
        relationDbNameCn: '',
        relationColomnId: '',
        relationColomnName: '',
        relationColomnNameCn: '',
        relationCondition: '',
        mainTableClu: '',
        dbId: ''
      },
      isPrimaryKey: '',
      oldName: '',
      glCluObj: {},
      filterText: '',
      editDrawer: false,
      data: [{
        id: '',
        label: '元模型',
        icon: 'el-icon-s-home',
        color: 'color: #FFC000',
        children: []
      }],
      rules: {
        tableName: [
          { required: true, message: '请输入表名', trigger: 'blur' }
        ]
      },
      columnTypeList: [],
      ifdatetime: false
    }
  },
  watch: {
    // 监听父组件高度变化，实时更新子组件高度
    height: {
      immediate: true,
      handler(newVal) {
        this.calcTableHeight(newVal)
      }
    },
    // 监听窗口大小变化，适配不同屏幕
    '$window.innerHeight': function() {
      this.calcTableHeight(this.height)
    },
    filterText (val) {
      if (this.$refs.tree) this.$refs.tree.filter(val)
    }
  },
  mounted() {
    // 初始化高度计算
    this.calcTableHeight(this.height)
  },
  methods: {

    snFormatter: function (row, column, value, index) {
      // 容错处理：若pageIndex/pageSize未初始化，用默认值1/10
      const currentPage = this.pageIndex || 1;
      const pageSize = this.pageSize || 50;
      // 序号公式：(当前页-1)*每页条数 + 当前行在页面中的索引 + 1
      return (currentPage - 1) * pageSize + index + 1;
    },
    /**
     * 核心方法：计算表格容器和表格高度
     * @param {String} parentHeight - 父组件传递的高度（如"600px"）
     */
    calcTableHeight(parentHeight) {
      const parentH = parseInt(parentHeight)
      if (isNaN(parentH)) return

      // 1. 顶部固定区域高度（r_top:70px + 搜索栏:50px）
      const topFixedHeight = 70 + 50
      // 2. 分页组件高度（固定40px）
      const paginationHeight = 80

      // 3. 表格容器高度 = 父组件高度 - 顶部固定区域高度
      this.tableContainerHeight = (parentH - topFixedHeight) + 'px'
      // 4. 表格高度 = 容器高度 - 分页高度
      this.tableHeight = (parentH - topFixedHeight - paginationHeight) + 'px'

      // 5. 最小高度限制（避免表格过小）
      if (parseInt(this.tableHeight) < 200) {
        this.tableHeight = '200px'
        this.tableContainerHeight = (200 + paginationHeight) + 'px'
      }
    },

    // 同步中间库表结构
    synchronization() {
      request({
        method: 'post',
        url: '/tech/synchronizationTableStructure?tableName=' + this.modelName
      }).then((res) => {
        if (res.code === 200 || res.code === '200') {
          this.$message({
            message: res.msg,
            type: 'success',
            center: true
          })
          this.getData() // 同步后刷新数据
        }
      }).catch(e => {
        this.$message({
          message: e.message || '同步失败',
          type: 'error',
          center: true,
          duration: 0,
          showClose: true
        })
      })
    },

    // 属性类型为datetime时禁用长度输入
    ifColumnType(id) {
      this.ifdatetime = id === 'datetime'
      if (this.ifdatetime) {
        this.addTableForm.columnLength = '' // 清空已输入的长度
      }
    },

    // 编辑表格属性
    editTable({ $index, row }) {
      this.cellIndex = $index
      this.oldName = row.columnName
      this.isPrimaryKey = row.isPrimaryKey === '1' ? 1 : 0
      // datetime类型禁用长度编辑
      this.isLength = row.columnType === 'datetime'
    },

    // 保存编辑的属性
    saveTable({ row }) {
      const tMetaModelTech = {
        columnName: row.columnName,
        columnNameCn: row.columnNameCn,
        productionForm: row.productionForm,
        dataSources: row.dataSources,
        columnType: row.columnType,
        remark: row.remark,
        id: row.id,
        isNotNull: row.isNotNull,
        modelId: this.modelId,
        isPrimaryKey: row.isPrimaryKey,
        columnLength: row.columnType === 'datetime' ? undefined : row.columnLength
      }

      request({
        headers: { 'Content-Type': 'application/json' },
        method: 'put',
        url: `/tech?tableName=${this.modelName}&oldColumnName=${encodeURI(this.oldName)}`,
        data: tMetaModelTech
      }).then((res) => {
        if (res.code === 200 || res.code === '200') {
          this.$message({ message: res.msg, type: 'success', center: true })
          this.cellIndex = null // 退出编辑状态
          this.getData() // 刷新数据
        }
      }).catch(e => {
        this.$message({
          message: e.message || '保存失败',
          type: 'error',
          center: true,
          duration: 0,
          showClose: true
        })
      })
    },

    // 取消编辑
    cancel() {
      this.cellIndex = null
      this.getData() // 刷新数据恢复原始状态
    },

    // 删除属性
    deletTable({ row }) {
      this.$confirm('此操作将永久删除该属性，是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        request({
          method: 'delete',
          url: `/tech/${row.id}?columnName=${row.columnName}&tableName=${this.modelName}&modelId=${this.modelId}`
        }).then((res) => {
          if (res.code === 200 || res.code === '200') {
            this.$message({ message: res.msg, type: 'success', center: true })
            this.getData()
          }
        }).catch(e => {
          this.$message({
            message: e.message || '删除失败',
            type: 'error',
            center: true,
            duration: 0,
            showClose: true
          })
        })
      }).catch(() => {
        this.$message({ type: 'info', message: '已取消删除' })
      })
    },

    // 获取属性类型列表
    getcolumnTypeList() {
      getTypeList().then((res) => {
        this.columnTypeList = res.data || []
      }).catch(e => {
        this.$message({
          message: e.message || '获取属性类型失败',
          type: 'error',
          center: true,
          duration: 0,
          showClose: true
        })
      })
    },

    // 主键必为必填
    isNotKeynull() {
      if (this.addTableForm.isPrimaryKey === '1') {
        this.addTableForm.isNotNull = '1'
        this.mustIs = true
      } else {
        this.addTableForm.isNotNull = '0'
        this.mustIs = false
      }
    },

    // 新增属性
    addTableClu() {
      this.addTableCluDrawer = true
      // 重置表单
      this.$nextTick(() => {
        this.$refs.addTableForm.resetFields()
        this.addTableForm = {
          columnName: '',
          columnNameCn: '',
          columnLength: '',
          columnType: '',
          isNotNull: '0',
          isPrimaryKey: '0',
          dataSources: '',
          productionForm: '',
          remark: ''
        }
        this.ifdatetime = false
        this.mustIs = false
      })
    },

    // 保存新增属性
    saveAddCluData(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const tMetaModelTech = {
            columnName: this.addTableForm.columnName,
            columnNameCn: this.addTableForm.columnNameCn,
            columnLength: this.ifdatetime ? undefined : this.addTableForm.columnLength,
            columnType: this.addTableForm.columnType,
            isNotNull: this.addTableForm.isNotNull,
            dataSources: this.addTableForm.dataSources,
            productionForm: this.addTableForm.productionForm,
            remark: this.addTableForm.remark,
            isPrimaryKey: this.addTableForm.isPrimaryKey
          }

          request({
            headers: { 'Content-Type': 'application/json' },
            method: 'post',
            url: `/tech?modelId=${this.modelId}&tableName=${this.modelName}`,
            data: tMetaModelTech
          }).then((res) => {
            if (res.code === 200 || res.code === '200') {
              this.$message({ message: res.msg, type: 'success', center: true })
              this.addTableCluDrawer = false
              this.getData()
            }
          }).catch(e => {
            this.$message({
              message: e.message || '新增失败',
              type: 'error',
              center: true,
              duration: 0,
              showClose: true
            })
          })
        }
      })
    },

    // 选择导入文件
    chooseAddFile() {
      this.$refs.addFileInput.value = ''
      this.$refs.addFileInput.dispatchEvent(new MouseEvent('click'))
    },

    // 获取导入文件名
    chooseAddFileChange() {
      const files = this.$refs.addFileInput.files
      if (files.length > 0) {
        this.addFileName = files[0].name
      }
    },

    // 导入文件
    importFile() {
      const files = this.$refs.addFileInput.files
      if (files.length === 0) {
        this.$message({ message: '请选择导入文件', type: 'warning', center: true })
        return
      }

      const formData = new FormData()
      formData.append('file', files[0], this.addFileName)
      const config = {
        headers: {
          'Content-Type': 'multipart/form-data',
          'Authorization': localStorage.getItem('token') || ''
        }
      }

      request.post(`${localStorage.URL}/file/upload/`, formData, config).then((res) => {
        this.toImport(res.data)
      }).catch(e => {
        this.$message({
          message: e.message || '文件上传失败',
          type: 'error',
          center: true,
          duration: 0,
          showClose: true
        })
      })
    },

    // 解析导入的Excel数据
    toImport(url) {
      request.get(`${localStorage.URL}/excel/metaModelAttributeByExcel/?path=${url}`).then((res) => {
        if (res.success) {
          this.importData = res.data || []
          this.showImport = false
          this.addFileName = ''
          this.$message({ message: '导入成功', type: 'success', center: true })
          this.showImportData = true
        } else {
          this.$message({ message: res.msg || '解析Excel失败', type: 'error', center: true })
        }
      }).catch(e => {
        this.$message({
          message: e.message || '导入数据失败',
          type: 'error',
          center: true,
          duration: 0,
          showClose: true
        })
      })
    },

    // 删除导入预览中的数据
    delImportData(index) {
      this.importData.splice(index, 1)
    },

    // 保存导入的属性（需根据实际接口完善）
    saveImportData() {
      // 此处需根据后端接口调整参数格式
      const importParams = {
        modelId: this.modelId,
        tableName: this.modelName,
        attributes: this.importData
      }

      request({
        headers: { 'Content-Type': 'application/json' },
        method: 'post',
        url: '/tech/batchAdd',
        data: importParams
      }).then((res) => {
        if (res.code === 200 || res.code === '200') {
          this.$message({ message: res.msg, type: 'success', center: true })
          this.showImportData = false
          this.getData()
        }
      }).catch(e => {
        this.$message({
          message: e.message || '批量保存失败',
          type: 'error',
          center: true,
          duration: 0,
          showClose: true
        })
      })
    },

    // 获取表基本信息
    getTabData() {
      request.get(`/tech/show?modelId=${this.modelId}&modelVersion=${this.modelVersion}`).then((res) => {
        this.tableForm = res.data || { dbName: '' }
      }).catch(e => {
        this.$message({
          message: e.message || '获取表信息失败',
          type: 'error',
          center: true,
          duration: 0,
          showClose: true
        })
      })
    },

    // 获取属性列表（带搜索和分页）
    getData() {
      this.ifSel = !this.modelId
      request.get(`/tech/list?modelId=${this.modelId}&modelVersion=${this.modelVersion}&columnName=${this.selForm.cluName}&pageNum=${this.pageIndex}&pageSize=${this.pageSize}`).then((res) => {
        this.tableData = res.rows || []
        this.total = res.total || 0
      }).catch(e => {
        this.$message({
          message: e.message || '获取属性列表失败',
          type: 'error',
          center: true,
          duration: 0,
          showClose: true
        })
      })
    }
  },
  created () {
    this.getcolumnTypeList()
    this.getData()
    this.getTabData()
  }
}
</script>

<style lang="stylus" rel="stylesheet/stylus" scoped>
/* 根容器：100%高度，溢出隐藏 */
.jsysj {
  width: 100%;
  height: 100%;
  overflow: hidden;
  box-sizing: border-box;
}

/* 主内容容器：横向布局 */
.qjcx_con {
  width: 100%;
  height: 100%;
  display: flex;
  box-sizing: border-box;
}

/* 右侧内容区：占满剩余宽度，溢出隐藏 */
.con_right {
  flex: 1;
  height: 100%;
  overflow: hidden;
  box-sizing: border-box;
}

/* 顶部表单区：固定高度，避免挤压 */
.r_top {
  width: 100%;
  height: 70px;
  border-bottom: 1px solid #e5e7eb;
  padding: 0 10px;
  box-sizing: border-box;
}

/* 表格容器：动态高度，溢出控制 */
.table_space {
  width: 100%;
  box-sizing: border-box;
}

/* 操作按钮区：避免换行挤压 */
.tit {
  width: 100%;
  box-sizing: border-box;
}

/* 对话框样式优化：避免控件挤压 */
.addbox .el-form-item {
  margin-bottom: 15px;
}
.addbox .el-input__inner,
.addbox .el-select {
  width: 100% !important;
}
.addbox .el-textarea__inner {
  width: 100% !important;
  min-height: 80px;
}

/* 导入对话框样式 */
.importBox .el-dialog {
  width: 500px !important;
}
.importDatas .el-dialog {
  width: 800px !important;
}

/* 表格样式优化：避免列挤压 */
.el-table {
  width: 100%;
  box-sizing: border-box;
}
.el-table__header-wrapper,
.el-table__body-wrapper {
  overflow-x: auto;
}

/* 分页样式：固定位置 */
.pagination {
  display: inline-block;
  margin-right: 10px;
}

/* 标签栏样式（冗余清理） */
.tabBarArea, .tabBarBtn {
  display: none; /* 原标签栏未使用，隐藏避免干扰 */
}
</style>