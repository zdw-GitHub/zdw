<template>
  <div class="jsysj">
    <div class="qjcx_con">
      <div class="con_right">
        <div class="r_top">
          <div  style="width: 100%;text-align: right;position:absolute;top:380px;right:270px">
              <!-- <el-button  type="primary" plain @click="saveTabData()" icon="el-icon-circle-check">保存</el-button> -->
            </div>

          <div class="table_space" style="height: 90px;display: flex">
            <el-form ref="editTableForm" :rules="rules" :model="tableForm" >
              <div class="el_group" style="margin-top: 20px">
<!--                 <el-form-item  style="flex: 12" label="数据库类型">
                  <el-input v-model="tableForm.dbType"></el-input>
                </el-form-item>
                <el-form-item  style="flex: 12" label="数据库连接名" >
                  <el-input v-model="tableForm.dbJdbc"></el-input>
                </el-form-item> -->
              </div>
              <div class="el_group" >
                  <el-form-item style="flex: 12" label="实例名">
                  <el-input v-model="tableForm.dbName"></el-input>
                </el-form-item>
                <el-form-item style="flex: 12" label="表名" prop="tableName">
                  <el-input v-model="tableForm.tableName"></el-input>
                </el-form-item>
              </div>
     <!--          <div class="el_group">
                <el-form-item style="flex: 12" label="数据接口">
                  <el-input  v-model="tableForm.modelName"></el-input>
                </el-form-item>
              </div> -->
            </el-form>
          </div>
        </div>
        <div class="r_bot">
          <div class="tit cell">
            <div class="cell" style="float: right;width: 100%;text-align: right;margin-bottom:10px">
              <el-form size="small" style="margin-right:30px;display: inline;" ref="selForm" :model="selForm" label-width="80px" >
                  <el-input style="width: 300px;height: 30px" v-model="selForm.cluName" placeholder="请输入属性名"></el-input>
              </el-form>
              <el-button :disabled="ifSel" size="mini" type="info" plain @click="getData()" icon="el-icon-search" v-hasPermi="['mm:tech:list']">搜索</el-button>
              <!-- <el-button :disabled="ifSel" size="mini" type="primary" plain @click="importTableClu()" icon="el-icon-download" v-if="versionShow" >导入</el-button> -->
              <el-button :disabled="ifSel" size="mini" type="primary" plain @click="addTableClu()" icon="el-icon-plus" v-if="versionShow" v-hasPermi="['mm:tech:add']">新增</el-button>
              <el-button :disabled="ifSel" size="mini" @click="getData" plain icon="el-icon-refresh">刷新</el-button>
            </div>
          </div>
          <div class="table_space">
            <el-table :data="tableData" stripe height="400px" tooltip-effect="dark" :header-cell-style="{background:'#F5F5F5'}">
              <el-table-column :show-overflow-tooltip="true" label="序号" width="100" align="center" :formatter="snFormatter">
              </el-table-column>
              <el-table-column :show-overflow-tooltip="true" prop="columnName" align="center" label="属性名称" width="150">
                <template slot-scope="scope">
                  <!-- scope.$index为当前行下标，cellIndex所点击行下标 -->
                  <el-input v-model="scope.row.columnName"  v-show="scope.$index == cellIndex">
                  </el-input>
                  <p v-show="scope.$index != cellIndex">{{ scope.row.columnName }}</p>
                </template>
              </el-table-column>
              <el-table-column :show-overflow-tooltip="true" prop="columnNameCn" align="center" label="中文名" width="150">
                <template slot-scope="scope">
                  <!-- scope.$index为当前行下标，cellIndex所点击行下标 -->
                  <el-input v-model="scope.row.columnNameCn"  v-show="scope.$index == cellIndex">
                  </el-input>
                  <p v-show="scope.$index != cellIndex">{{ scope.row.columnNameCn }}</p>
                </template>
              </el-table-column>
              <el-table-column :show-overflow-tooltip="true" prop="columnType" align="center" label="属性类型" width="200">
                <template slot-scope="scope">
                  <!-- scope.$index为当前行下标，cellIndex所点击行下标 -->
                  <el-select v-model="scope.row.columnType"  v-show="scope.$index == cellIndex" style="width:100px" @change="isNoLenth(scope.row.columnType)">
                    <el-option
                        v-for="item in columnTypeList"
                        :key="item"
                        :label="item"
                        :value="item">
                      </el-option>
                  </el-select>
                  <p v-show="scope.$index != cellIndex">{{ scope.row.columnType }}</p>
                </template>
              </el-table-column>
              <el-table-column :show-overflow-tooltip="true" prop="columnLength" align="center" label="属性长度" width="180" >
                <template slot-scope="scope">
                  <!-- scope.$index为当前行下标，cellIndex所点击行下标 -->
                  <el-input v-model="scope.row.columnLength" size="mini" @change="handleChange"  :disabled="isLength"
                    label="描述文字" v-show="scope.$index == cellIndex" style="width:100px"></el-input>
                  <p v-show="scope.$index != cellIndex">{{ scope.row.columnLength }}</p>
                </template>
              </el-table-column>
              <el-table-column :show-overflow-tooltip="true" prop="isPrimaryKey" align="center" label="是否主键" width="180">
                <template slot-scope="scope">
                  <!-- scope.$index为当前行下标，cellIndex所点击行下标 -->
                  <el-select v-model="scope.row.isPrimaryKey"  v-show="scope.$index == cellIndex" style="width:100px">
                    <el-option value="1" label="是"></el-option>
                    <el-option value="0" label="否"></el-option>
                  </el-select>
                  <p v-show="scope.$index != cellIndex" v-if="scope.row.isPrimaryKey==='1'">是</p>
                  <p v-show="scope.$index != cellIndex" v-if="scope.row.isPrimaryKey==='0'">否</p>
                </template>
              </el-table-column>
              <el-table-column :show-overflow-tooltip="true" prop="isNotNull" align="center" label="是否必填" width="160">
                <template slot-scope="scope">
                  <!-- scope.$index为当前行下标，cellIndex所点击行下标 -->
                  <el-select v-model="scope.row.isNotNull"  v-show="scope.$index == cellIndex" style="width:100px">
                    <el-option value="1" label="是"></el-option>
                    <el-option value="0" label="否"></el-option>
                  </el-select>
                  <p v-show="scope.$index != cellIndex" v-if="scope.row.isNotNull==='1'">是</p>
                  <p v-show="scope.$index != cellIndex" v-if="scope.row.isNotNull==='0'">否</p>
                </template>
              </el-table-column>
              <el-table-column :show-overflow-tooltip="true" prop="columnLength" align="center" label="产生形式" width="180" >
                <template slot-scope="scope">
                  <!-- scope.$index为当前行下标，cellIndex所点击行下标 -->
                  <el-input v-model="scope.row.productionForm" size="mini"  :disabled="isLength"
                            v-show="scope.$index == cellIndex" style="width:100px"></el-input>
                  <p v-show="scope.$index != cellIndex">{{ scope.row.productionForm }}</p>
                </template>
              </el-table-column>
              <el-table-column :show-overflow-tooltip="true" prop="columnLength" align="center" label="数据来源 " width="180" >
                <template slot-scope="scope">
                  <!-- scope.$index为当前行下标，cellIndex所点击行下标 -->
                  <el-input type="textarea" autosize v-model="scope.row.dataSources" size="mini"  :disabled="isLength"
                            v-show="scope.$index == cellIndex" style="width:100px"></el-input>
                  <p v-show="scope.$index != cellIndex">{{ scope.row.dataSources }}</p>
                </template>
              </el-table-column>
              <el-table-column :show-overflow-tooltip="true" prop="columnLength" align="center" label="备注" width="180" >
                <template slot-scope="scope">
                  <!-- scope.$index为当前行下标，cellIndex所点击行下标 -->
                  <el-input type="textarea" autosize v-model="scope.row.remark" size="mini" :disabled="isLength"
                            v-show="scope.$index == cellIndex" style="width:100px"></el-input>
                  <p v-show="scope.$index != cellIndex">{{ scope.row.remark }}</p>
                </template>
              </el-table-column>
              <el-table-column fixed="right" v-if="versionShow" label="操作" width="220" align="center">
                <template slot-scope="scope">
                  <el-button @click="editTable(scope)" size="mini" type="text" icon="el-icon-edit"
                    v-if="scope.$index != cellIndex"  v-hasPermi="['mm:tech:edit']">编辑</el-button>
                  <el-button @click="saveTable(scope)" size="small" type="text" v-else icon="el-icon-circle-check">保存</el-button>
                  <el-button type="text" @click="deletTable(scope)" size="small" icon="el-icon-delete" v-if="scope.$index != cellIndex" v-hasPermi="['mm:tech:remove']">删除</el-button>
                  <el-button @click="cancel()" size="small" type="text" v-else icon="el-icon-circle-close">取消</el-button>
                </template>
                </el-table-column>
            </el-table>
            <pagination
              v-show="total>0"
              :total="total"
              :page.sync="pageIndex"
              :limit.sync="pageSize"
              @pagination="getData"
            />
          </div>
        </div>
      </div>
    </div>
    <div>
    </div>
    <div class="importBox">
      <el-dialog title="导入元模型属性" :visible.sync="showImport"  :close-on-click-modal="false" style="margin-top: 19vh !important;">
        <div> <input id="addFileInput"
                     type="file"
                     ref="addFileInput"
                     @change="chooseAddFileChange"
                     style="display:none;"
        />
          <div class="hint" style="float: left;margin-right: 0.2rem">选择导入文件:</div>
          <el-button plain type="primary" @click="chooseAddFile" style="float: left;width:120px">选择文件</el-button>
        </div>
        <div style="height: 50px">
          <div class="file-name" style="font-size: 16px;line-height: 24px">{{addFileName}}</div>
        </div>
        <div slot="footer" class="dialog-footer" style="text-align: center">
          <el-button plain style="padding:5px 25px;width:80px" type="primary" @click="importFile()">确认</el-button>
          <el-button plain style="padding:5px 25px;width:80px" @click="showImport = false">取消</el-button>
        </div>
      </el-dialog>
    </div>
    <div>
      <el-dialog
        :visible.sync="addTableCluDrawer"
        title="新增表属性"
        :close-on-click-modal="false"
      >
        <el-form :model="addTableForm" label-width="80px" style="text-align: center" ref="addTableForm" :rules="addrules" >
          <el-form-item label="属性名称" prop="columnName">
            <el-input v-model="addTableForm.columnName"></el-input>
          </el-form-item>
          <el-form-item label="中文名称" prop="columnNameCn" style="margin-top:30px">
            <el-input v-model="addTableForm.columnNameCn"></el-input>
          </el-form-item>
          <el-form-item label="属性类型" prop="columnType" style="margin-top:30px">
            <el-select filterable v-model="addTableForm.columnType" placeholder="请选择" @change="ifColumnType(addTableForm.columnType)">
              <el-option
                        v-for="item in columnTypeList"
                        :key="item"
                        :label="item"
                        :value="item">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="属性长度" prop="columnLength" style="margin-top:30px">
            <el-input v-model="addTableForm.columnLength" :disabled="ifdatetime"></el-input>
          </el-form-item>
          <el-form-item label="是否主键" prop="isPrimaryKey"  style="margin-top:30px">
            <el-select v-model="addTableForm.isPrimaryKey" @change="isNotKeynull()">
              <el-option value="0" label="否"></el-option>
              <el-option value="1" label="是"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="是否必填" prop="isNotNull"  style="margin-top:30px">
            <el-select v-model="addTableForm.isNotNull" :disabled="mustIs" @change="$forceUpdate()">
              <el-option value="0" label="否"></el-option>
              <el-option value="1" label="是"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="产生形式" prop="productionForm" style="margin-top:30px">
            <el-input v-model="addTableForm.productionForm"></el-input>
          </el-form-item>
          <el-form-item label="数据来源" prop="dataSources" style="margin-top:30px">
            <el-input type="textarea" v-model="addTableForm.dataSources"></el-input>
          </el-form-item>
          <el-form-item label="备注" prop="remark" style="margin-top:30px">
            <el-input type="textarea" v-model="addTableForm.remark"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button  type="primary" @click="saveAddCluData('addTableForm')" >确 定</el-button>
          <el-button @click="addTableCluDrawer= false">取 消</el-button>
        </div>
      </el-dialog>
    </div>
    <div class="importDatas">
      <el-dialog
        title="导入表属性"
        v-dialogDrag
        :close-on-click-modal="false"
        :visible.sync="showImportData" >
        <el-table height="330px"
                  tooltip-effect="dark"
                  :data="importData">
          <el-table-column
            label="序号"
            width="60"
            align="center"
            :formatter="snFormatter">
          </el-table-column>
          <el-table-column
            prop="attributeName"
            align="center"
            label="属性名称"
            width="140">
            <template slot-scope="scope">
              <el-input v-model="scope.row.attributeName">{{scope.row.attributeName}}</el-input>
            </template>
          </el-table-column>
          <el-table-column
            prop="attributeNameCn"
            align="center"
            label="中文名称"
            width="140">
            <template slot-scope="scope">
              <el-input v-model="scope.row.attributeNameCn">{{scope.row.attributeNameCn}}</el-input>
            </template>
          </el-table-column>
          <el-table-column
            prop="attributeType"
            align="center"
            label="属性类型"
            width="150">
            <template slot-scope="scope">
              <el-select filterable v-model="scope.row.attributeType" placeholder="请选择">
                <el-option
                  v-for="item in typeList"
                  :key="item.pubCode"
                  :label="item.pubName"
                  :value="item.pubCode">
                </el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column
            prop="attributeLength"
            align="center"
            label="属性长度"
            width="120">
            <template slot-scope="scope">
              <el-input v-model="scope.row.attributeLength">{{scope.row.attributeLength}}</el-input>
            </template>
          </el-table-column>
          <el-table-column
            prop="isPrimaryKey"
            align="center"
            label="是否主键"
            width="150">
            <template slot-scope="scope">
              <el-select v-model="scope.row.isPrimaryKey">
                <el-option value="0" label="否"></el-option>
                <el-option value="1" label="是"></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column
            prop="isPrimaryKey"
            align="center"
            label="操作"
            width="100">
            <template slot-scope="scope">
              <el-button plain type="primary" @click="delImportData(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <div slot="footer" class="dialog-footer" style="text-align: center">
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
    required: true
  },
  data () {
    return {
      mustIs: false,
      // 当前点击行的下标
      cellIndex: null,
      tabName: 'Tab1',
      ifSel: true,
      glRelationTableName: '',
      glTableCluName: '',
      uploadTableDrawer: false,
      addTableDrawer: false,
      addTableCluDrawer: false,
      glCluDrawer: false,
      mainTableCluVisible: false,
      selForm: {
        cluName: ''
      },
      // 分页
      pageSize: 10,
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
      tableData: [
      ],

      tableDataClu: [],
      glTableCluFormD: {},
      tableForm: {
      },
      uploadTableForm: {

      },
      addTableForm: {
        columnName: '',
        columnNameCn: '',
        columnLength: '',
        columnType: '',
        isNotNull: '',
        isPrimaryKey: '',
        dataSources:'',
        productionForm:'',
        remark:''
      },
      addrules: {
        columnName: [
          {required: true, message: '请输入属性名称', trigger: 'blur'},
          {
            validator: (rule, value, callback) => {
              if (/^(^[A-Za-z]+$)|(?![^A-Za-z]+$)(?![^0-9]+$)[0-9A-Za-z_]|(?![^A-Za-z]+$)[0-9A-Za-z_]/.test(value) === false) {
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
              } else {
                if (/^(\d+,)*\d+$/.test(value) === false) {
                  callback(new Error('请输入合法长度'))
                } else {
                  callback()
                }
                callback()
              }
            },
            trigger: 'blur'
          }
        ],
        columnNameCn: [
          {required: true, message: '请输入中文名称', trigger: 'blur'}
        ],
        columnType: [
          {required: true, message: '请选择属性类型', trigger: 'blur'}
        ],
        isPrimaryKey: [
/*           {
            validator: (rule, value, callback) => {
                for (let a = 0; a < this.tableData.length; a++) {
                  if (value === '1' && this.tableData[a].isPrimaryKey === '1') {
                    console.log('已存在')
                    console.log(this.tableData[a].isPrimaryKey)
                  } else{
                    console.log('222')
                  }
                }
    }
  } */
        ]

      },
      // 导入表格属性
      showImport: false,
      addFileName: '',
      // 导入后的数据列表
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
          {required: true, message: '请输入表名', trigger: 'blur'}
        ]
      },
      columnTypeList: [],
      ifdatetime: false
    }
  },
  watch: {
    filterText (val) {
      this.$refs.tree.filter(val)
    }
  },
  methods: {
    // 新增类型为datetime
    ifColumnType(id) {
      if(id==='datetime'){
        this.ifdatetime = true
      } else {
        this.ifdatetime = false
      }
    },
    // 编辑操作
    editTable ({ $index }) {
      // 获取当前点击行下标
      this.cellIndex = $index
      this.oldName = this.tableData[$index].columnName
      if (this.tableData[$index].isPrimaryKey === '1' || this.tableData[$index].isPrimaryKey === 1) {
        this.isPrimaryKey = 1
      }
      if (this.tableData[$index].columnType === 'datetime') {
        this.isLength = true
      }
    },
    // 保存操作
    saveTable ({ row }) {
      var tMetaModelTech = {
        columnName: row.columnName,
        columnNameCn: row.columnNameCn,
        productionForm: row.productionForm,
        dataSources: row.dataSources,
        columnType: row.columnType,
        remark: row.remark,
        id: row.id,
        isNotNull: row.isNotNull,
        modelId: this.modelId
      }
      if (row.columnType==='datetime') {
        tMetaModelTech.columnLength = undefined
      } else {
        tMetaModelTech.columnLength = row.columnLength
      }
      if (this.isPrimaryKey === 1 && row.isPrimaryKey === '1') {
        tMetaModelTech.isPrimaryKey = undefined
      } else {
        tMetaModelTech.isPrimaryKey = row.isPrimaryKey
      }
      request({
        headers: {
          'Content-Type': 'application/json'
        },
        method: 'put',
        url: '/tech?tableName=' + this.modelName + '&oldColumnName=' + encodeURI(this.oldName),
        data: tMetaModelTech
      }).then((res) => {
        // 请求成功处理。
        this.getData()
        if (res.code === 200||res.code === '200'){
        this.$message({
          message: res.msg,
          type: 'success',
          center: true
        })}
      }).catch(e => {
        /* this.getData() */
        // 请求失败处理。
        this.$message({
          message: e,
          type: 'error',
          center: true,
            duration:0,
            showClose:true
        })
      })
      this.cellIndex = null
    },
    isNoLenth(type) {
      if (type==='datetime') {
        this.isLength = true
      } else {
        this.isLength = false
      }
    },
    handleSizeChange (val) {
      this.pageSize = val
      this.getData()
    },
    handleCurrentChange (val) {
      this.pageIndex = val
      this.getData()
    },
    // 取消操作
    cancel () {
      this.cellIndex = null
      this.getData()
    },
    // 删除操作
    deletTable ({ row }) {
      this.$confirm("是否删除此数据？", "提示", {
          confirmButtonText: "确认",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(() => {
            request({
        method: 'delete',
        url: '/tech/' + row.id + '?columnName=' + row.columnName + '&tableName=' + this.modelName
      }).then((res) => {
        // 请求成功处理。
        this.getData()
        if (res.code === 200||res.code==='200'){
        this.$message({
          message: res.msg,
          type: 'success',
          center: true
        })}
      }).catch(e => {
        /* this.getData() */
        // 请求失败处理。
        this.$message({
          message: e,
          type: 'error',
          center: true,
            duration:0,
            showClose:true
        })
      })
          })
          .catch(() => {});

    },
    filterNode (value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    },
    /* 格式序列化列 */
    snFormatter: function (row, col, value, index) {
      // return this.pageNumber * (this.pageSize - 1) + index + 1;
      return index + 1
    },
    /* 格式所属表列 */
    tableIdFormatter: function (row, col, value, index) {
      for (var i = 0; i < this.columnTableList.length; i++) {
        if (this.columnTableList[i].id === value) {
          return this.columnTableList[i].tableName
        }
      }
      return value
    },
    // 获取属性类型下拉框数据
    getcolumnTypeList: function () {
      getTypeList().then((res) => {
        // 请求成功处理。
        this.columnTypeList = res.data
      }).catch(e => {
        // 请求失败处理。
        this.$message({
          message: e,
          type: 'error',
          center: true,
            duration:0,
            showClose:true
        })
      })

    },
    uploadTable: function () {
      for (var a in this.uploadTableForm) {
        this.uploadTableForm[a] = ''
      }
      this.uploadTableForm.modelName = 't_meta_'
      this.uploadTableDrawer = true
    },
    // 当新增为主键 则必填
    isNotKeynull: function () {
      if (this.addTableForm.isPrimaryKey === 1 || this.addTableForm.isPrimaryKey === '1') {
        this.addTableForm.isNotNull = '1'
        this.mustIs = true
      } else {
        this.addTableForm.isNotNull = ''
        this.mustIs = false
      }
    },
    // 添加表格属性
    addTableClu: function () {
      this.addTableCluDrawer = true
      this.addTableForm = {}
      this.addTableForm.isPrimaryKey = '0'
      this.addTableForm.isNotNull = '0'
      this.mustIs = false
      this.$refs["addTableForm"].resetFields()
    },
    saveAddCluData: function (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          var tMetaModelTech = {
            columnName: this.addTableForm.columnName,
            columnNameCn: this.addTableForm.columnNameCn,
            columnLength: this.addTableForm.columnLength,
            columnType: this.addTableForm.columnType,
            isNotNull: this.addTableForm.isNotNull,
            dataSources:this.addTableForm.dataSources,
            productionForm:this.addTableForm.productionForm,
            remark:this.addTableForm.remark,
            isPrimaryKey: this.addTableForm.isPrimaryKey
          }
          request({
            headers: {
              'Content-Type': 'application/json'
            },
            method: 'post',
            url: '/tech?modelId=' + this.modelId + '&tableName=' + this.modelName,
            data: tMetaModelTech
          }).then((res) => {
            // 请求成功处理。
            this.addTableCluDrawer = false
            this.getData()
            if (res.code===200||res.code==='200'){
            this.$message({
              message: res.msg,
              type: 'success',
              center: true
            })}
          }).catch(e => {
             /* this.getData() */
            // 请求失败处理。
            this.$message({
              message: e,
              type: 'error',
              center: true,
            duration:0,
            showClose:true
            })
          })
        } else {
          return false
        }
      })
    },
    // 导入表格属性
    importTableClu: function () {
      this.addFileName = ''
      this.showImport = true
    },
    // 获取导入文件名称
    chooseAddFileChange: function () {
      let files = this.$refs.addFileInput.files
      this.addFileName = files[0].name
    },
    // 接收导入文件
    chooseAddFile: function () {
      this.$refs.addFileInput.value = ''
      this.$refs.addFileInput.dispatchEvent(new MouseEvent('click'))
    },
    // 确认导入
    importFile: function () {
      let files = this.$refs.addFileInput.files
      this.addFileName = files[0].name
      let formData = new FormData()
      formData.append('file', files[0], this.addFileName)
      let config = {
        headers: {
          'Content-Type': 'multipart/form-data',
          'Authorization': this.token
        }
      }
      // 向后台请求区域查询
      request.post(localStorage.URL + '/file/upload/', formData, config).then((res) => {
        // 请求成功处理。
        this.toImport(res.data)
      }).catch(e => {
        // 请求失败处理。
        this.$message({
          message: e,
          type: 'error',
          center: true,
            duration:0,
            showClose:true
        })
      })
    },
    toImport: function (url) {
      this.importData = []
      request.get(localStorage.URL + '/excel/metaModelAttributeByExcel/?path=' + url, qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        if (res.success) {
          this.importData = res.data
          this.showImport = false
          this.addFileName = ''
          this.$message({
            message: '导入成功',
            type: 'success',
            center: true
          })
          this.showImportData = true
        } else {
          this.$message({
            message: res.msg,
            type: 'error',
            center: true,
            duration:0,
            showClose:true
          })
        }
      }).catch(e => {
        // 请求失败处理。
      })
    },
    // 删除导入属性
    delImportData (index) {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.importData.splice(index, 1)
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    // 保存所有批量导入属性
    saveImportData: function () {
    },
    // 获取已关联表别名与属性
    getRelationTableAlex: function () {
      this.mainTableList = []
      this.relationTableAlias = ''
      this.seeClu = false
      for (var i = 0; i < this.relationTableList.length; i++) {
        if (this.relationTableList[i].relationTableId === this.relationTableIdF) {
          this.relationTableAlias = this.relationTableList[i].relationTableAlias
        }
      }
      request.get(localStorage.URL + '/dbtablecolumn/list?tableId=' + this.relationTableIdF, qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        this.mainTableList = res.data
      }).catch(e => {
        // 请求失败处理。
        this.$message({
          message: e,
          type: 'error',
          center: true,
            duration:0,
            showClose:true
        })
      })
    },
    // 获取表基本信息
    getTabData: function () {
      this.tableForm = {}
      request.get('/tech/show?modelId=' + this.modelId + '&modelVersion=' + this.modelVersion, qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        res = res.data
        this.tableForm = res
        /* this.getData() */
      }).catch(e => {
        // 请求失败处理。
      })
    },
    // 获取表属性列表
    getData: function () {
      if (this.modelId === '') { this.ifSel = true } else { this.ifSel = false }
      this.tableData = []
      request.get('/tech/list?modelId=' + this.modelId + '&modelVersion=' + this.modelVersion + '&columnName=' + this.selForm.cluName + '&pageNum=' + this.pageIndex + '&pageSize=' + this.pageSize, qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        this.tableData = res.rows
        this.total = res.total
        this.loading = false
      }).catch(e => {
        /* this.getData() */
        // 请求失败处理。
      })
    },
    // 获取关联表所有属性,
    getTableClu: function () {
      this.glCluObj = {}
      this.tableDataClu = []
      request.get(localStorage.URL + '/dbtablecolumn/list?tableId=' + this.glTableCluForm.relationTableId, qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        this.tableDataClu = res.data
        this.checkAlias()
      }).catch(e => {
        /* this.getData() */
        // 请求失败处理。
        this.$message({
          message: e,
          type: 'error',
          center: true,
            duration:0,
            showClose:true
        })
      })
    },
    // 判断别名是否存在,是否可修改
    checkAlias: function () {
      this.seeAlias = false
      this.glRelationTableName = ''
      var tableId = this.glTableCluForm.relationTableId
      var dbId = this.glTableCluForm.ahhahahaa
      if (tableId === this.tableForm.relationTableId && dbId === this.tableForm.ahhahahaa) {
        this.glRelationTableName = this.tableForm.relationTableAlias
        this.seeAlias = true
      } else {
        var tableSel = {}
        for (var i = 0; i < this.tableData.length; i++) {
          if (tableId === this.tableData[i].relationTableId && dbId === this.tableData[i].ahhahahaa) {
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
  created () {
    this.ifSel = true
    this.getcolumnTypeList()
    this.getData()
    this.getTabData()
  }
}
</script>
<style lang="stylus" rel="stylesheet/stylus" scoped>
    .jsysj
      width 100%
      height 100%
      .cell .el-input__inner
        height 30px
      .aa .el-input,.aa .el-form-item__content,.aa .el-input__inner
        width  3rem !important
      /deep/.el_group .el-form-item__label{
          width:100px !important
        }
      /deep/.el_group .el-form-item__content{
          margin-left:100px !important;
          width:300px
        }
      .el-dialog
        width 30rem !important
        height auto !important
        margin 11% 36%
      .importDatas .el-dialog
        width 18rem
        margin auto
      .el-select
          width 18.7rem
      input[readonly='readonly']
        background #F5F7FA
        color #CAC4CC
      .el-select input[readonly='readonly']
        background #fff
        color #000
      .el-input__inner
        height 40px
      .qjcx_con
        width 100%
        height 100%
        display flex
        .con_left
          flex 6
          width:0
          overflow auto
          flex-shrink: 0
          height 100%
          border-right 1px solid lightgrey
          .left_con
            padding .2rem 0
            font-size: 14px
            line-height 20px
            border-bottom 1px solid lightgrey
            .l_con
              margin-top: 10px
              text-indent 4px
        .con_right
          flex 18
          flex-shrink: 0
          width:0
          .table_space
            width 100%
            margin auto
            .el_group
              display flex
              width 90%
              margin auto
        .tit
          font-size 16px
          height 40px
          line-height 40px
          text-indent 1%
          margin-bottom 10px
          border-bottom 1px solid lightgrey
        .tabBarArea {
            position: relative;
          }
        .tabBarArea:after{
            display: block;
            content: '';
            clear: both;
          }
        .tabBarBtn {
            float: left;
            margin-left: 20px;
            cursor: pointer;
            white-space:nowrap;
            width: auto;
            height:35px
            padding: 0px;
          }
        .tabBarBtn:hover {
            color: #326BE8;
            border-bottom: 2px solid #326BE8;
            border-top:0px;
            border-left:0px;
            border-right:0px;
          }
        .tabBarBtnActive {
            color: #326BE8;
            border-bottom: 2px solid #326BE8;
            border-top:0px;
            border-left:0px;
            border-right:0px;
          }
        .tabBarBtnActive:hover {
            color: #326BE8;
          }

  </style>
