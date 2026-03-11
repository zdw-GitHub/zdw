<template>
  <div class="aqysj">
    <div class="qjcx_con">
      <div class="con_right">
        <div class="r_top">
          <div class="cell" style="width: 10%;text-align: right;position:absolute;top:250px;right:35px">
            <!-- <el-button type="primary" @click="uploadTable()" icon="el-icon-upload2">发布</el-button>
            <el-button type="primary" @click="addTable()" icon="el-icon-plus">新增</el-button> -->
            <el-button :disabled="ifSel" type="primary" @click="saveTabData()" icon="el-icon-circle-check">保存</el-button>
            <!-- <el-button :disabled="ifSel" type="primary" @click="getTabData()" icon="el-icon-circle-close">复制</el-button>
            <el-button :disabled="ifSel" type="primary" @click="delTable()" icon="el-icon-delete">删除</el-button> -->
          </div>

          <div class="table_space" style="height: 130px;display: flex">
            <el-form ref="editTableForm" :rules="rules" :model="tableForm">
              <div class="el_group" style="margin-top: 20px">
                <el-form-item style="flex: 12" label="数据权限">
                  <!--<el-input readonly v-model="tableForm.relationDbNameCn"></el-input>-->
                  <el-select filterable @change="getTableList(0)" v-model="tableForm.relationDbId" placeholder="请选择">
                    <el-option v-for="item in dbList" :key="item.id" :label="item.dbNameCn" :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
                <el-form-item style="flex: 12" label="安全等级">
                  <el-select filterable v-model="tableForm.categoryId" placeholder="请选择">
                    <el-option v-for="item in categoryList" :key="item.id" :label="item.categoryNameCn"
                      :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </div>
            </el-form>
          </div>
        </div>
        <div class="r_bot">
          <div class="tit cell">
            <div class="cell" style="float: right;width: 120%;text-align: right">
              <span style="margin-right:425px;color:#0070C0">数据权限字段管理</span>
              <el-form style="margin-right:65px;display: inline;" ref="selForm" :model="selForm" label-width="80px"
                label="">
                属性名称：<el-input style="width: 300px;height: 30px" v-model="selForm.cluName" placeholder="请输入属性名">
                </el-input>
              </el-form>
              <el-button :disabled="ifSel" type="primary" @click="getData()" icon="el-icon-search">搜索</el-button>
              <!-- <el-button :disabled="ifSel||tableData.length>0" type="primary" @click="importTableClu()" icon="el-icon-plus">导入</el-button> -->
              <el-button :disabled="ifSel" type="primary" @click="addTableClu()" icon="el-icon-plus">新增</el-button>
              <el-button :disabled="ifSel" @click="getData" type="primary" icon="el-icon-refresh">刷新</el-button>
            </div>
          </div>
          <div class="table_space">
            <el-table :data="tableData" stripe height="450px" tooltip-effect="dark"
              :header-cell-style="{background:'#F5F5F5'}">
              <el-table-column label="序号" width="110" align="center" :formatter="snFormatter">
              </el-table-column>
              <el-table-column prop="attributeName" align="center" label="属性名称" width="105">
              </el-table-column>
              <el-table-column prop="attributeNameCn" align="center" label="中文名" width="105">
              </el-table-column>
              <el-table-column prop="attributeType" align="center" label="属性类型" width="110">
              </el-table-column>
              <el-table-column prop="attributeType" align="center" label="属性长度" width="112">
              </el-table-column>
              <el-table-column prop="attributeType" align="center" label="关联数据源" width="120">
              </el-table-column>
              <el-table-column prop="attributeType" align="center" label="关联表" width="110">
              </el-table-column>
              <el-table-column prop="relationCondition" align="center" label="关联表别名" width="110">
              </el-table-column>
              <el-table-column prop="remark" align="center" label="关联属性" width="110">
              </el-table-column>
              <el-table-column label="操作" width="240" align="center">
                <template slot-scope="scope">
                  <el-button type="primary" @click="editColumn(scope.row)" icon="el-icon-edit">关联</el-button>
                  <el-button type="primary" @click="editColumn(scope.row)" icon="el-icon-edit">编辑</el-button>
                  <el-button type="primary" @click="editColumn(scope.row)" icon="el-icon-edit">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </div>
    </div>
    <div>
      <el-dialog title="发布版本" :close-on-click-modal="false" :visible.sync="uploadTableDrawer" v-if="tabName==='Tab1'">
        <el-form :model="uploadTableForm" :rules="rules" ref="uploadTableForm" label-width="80px"
          style="text-align: center">
          <el-form-item label="版本号">
            <el-input></el-input>
          </el-form-item>
          <el-form-item label="更新内容">
            <el-input type="textarea"></el-input>
          </el-form-item>
          <el-form-item label="备注">
            <el-input type="textarea"></el-input>
          </el-form-item>
          <el-form-item style="margin-top: 30px;margin-left: -25px">
            <el-button type="primary" @click="saveAddTable()" icon="el-icon-circle-check">保存</el-button>
            <el-button type="primary" @click="uploadTableDrawer= false" icon="el-icon-circle-close">取消</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>
      <el-dialog title="新增业务元数据" :close-on-click-modal="false" :visible.sync="addTableDrawer" v-if="tabName==='Tab1'">
        <el-form :model="addTableForm" :rules="rules" ref="addTableForm" label-width="80px" style="text-align: center">
          <el-form-item label="名称">
            <el-input v-model="addTableForm.modelName"></el-input>
          </el-form-item>
          <el-form-item label="中文名称" prop="modelNameCn">
            <el-input v-model="addTableForm.modelNameCn"></el-input>
          </el-form-item>
          <el-form-item label="所属类目">
            <el-select filterable @change="getDbTableList(0)" v-model="addTableForm.relationDbId" placeholder="请选择">
              <el-option v-for="item in dbList" :key="item.id" :label="item.dbNameCn" :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="标签">
            <el-select filterable v-model="addTableForm.relationTableId" placeholder="请选择">
              <el-option v-for="item in tableList" :key="item.id" :label="item.tableName" :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="版本号">
            <el-select filterable v-model="addTableForm.categoryId" placeholder="请选择">
              <el-option v-for="item in categoryList" :key="item.id" :label="item.categoryNameCn" :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="备注">
            <el-input type="textarea" v-model="addTableForm.remark"></el-input>
          </el-form-item>
          <el-form-item style="margin-top: 30px;margin-left: -25px">
            <el-button type="primary" @click="saveAddTable()" icon="el-icon-circle-check">保存</el-button>
            <el-button type="primary" @click="addTableDrawer= false" icon="el-icon-circle-close">取消</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>
      <el-dialog :visible.sync="addTableCluDrawer" title="新增元模型属性" :close-on-click-modal="false">
        <el-form :model="addTableCluForm" label-width="80px" style="text-align: center">
          <el-form-item label="属性名称">
            <el-input v-model="addTableCluForm.attributeName"></el-input>
          </el-form-item>
          <el-form-item label="中文名称">
            <el-input v-model="addTableCluForm.attributeNameCn"></el-input>
          </el-form-item>
          <el-form-item label="属性类型">
            <el-select filterable v-model="addTableCluForm.attributeType" placeholder="请选择">
              <el-option v-for="item in typeList" :key="item.pubCode" :label="item.pubName" :value="item.pubCode">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="属性长度">
            <el-input v-model="addTableCluForm.attributeLength"></el-input>
          </el-form-item>
          <el-form-item label="是否主键">
            <el-select v-model="addTableCluForm.isPrimaryKey">
              <el-option value="0" label="否"></el-option>
              <el-option value="1" label="是"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="备注">
            <el-input v-model="addTableCluForm.remark"></el-input>
          </el-form-item>
          <el-form-item style="position: absolute;bottom: 20px;left: -50px">
            <el-button type="primary" @click="saveAddCluData()" icon="el-icon-circle-check">保存</el-button>
            <el-button type="primary" @click="addTableCluDrawer= false" icon="el-icon-circle-close">取消</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>
      <div class="addGl">
        <el-dialog :visible.sync="glCluDrawer" :title="glTableCluName" :close-on-click-modal="false">
          <el-form :model="glTableCluForm" label-width="80px">
            <div style="display: flex;width: 70%">
              <el-form-item style="flex: 6" label="关联数据源">
                <el-select filterable @change="getDbTableList(1)" v-model="glTableCluForm.relationDbId"
                  placeholder="请选择">
                  <el-option v-for="item in dbList" :key="item.id" :label="item.dbNameCn" :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item style="flex: 6" label="关联表">
                <el-select @change="getTableClu" filterable v-model="glTableCluForm.relationTableId" placeholder="请选择">
                  <el-option v-for="item in tableList" :key="item.id" :label="item.tableName" :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </div>
            <div style="display: flex;width: 70%">
              <el-form-item style="flex: 6" label="关联属性">
                <el-input readonly placeholder="单击表格进行选择" v-model="glCluObj.columnName"></el-input>
              </el-form-item>
              <el-form-item style="flex: 6" label="关联表别名">
                <el-input :readonly="seeAlias" v-model="glRelationTableName" placeholder="请以字母开头，由字母、数字、下划线组成">
                </el-input>
              </el-form-item>
            </div>
            <el-table ref="multipleTable" :data="tableDataClu" height="300px"
              style="margin-top:20px;margin-left:2%;width: 96%" tooltip-effect="dark" highlight-current-row
              @current-change="handleCurrentChange" :header-cell-style="{background:'#F5F5F5'}">
              <el-table-column label="序号" width="80" align="center" :formatter="snFormatter">
              </el-table-column>
              <el-table-column prop="columnName" align="center" label="属性名称" width="120">
              </el-table-column>
              <el-table-column prop="columnNameCn" align="center" label="中文名称" width="120">
              </el-table-column>
              <el-table-column prop="columnType" align="center" label="属性类型" width="120">
              </el-table-column>
              <el-table-column prop="columnLength" align="center" label="属性长度" width="120">
              </el-table-column>
              <el-table-column prop="remark" align="center" label="备注" width="150">
              </el-table-column>
              <el-table-column prop="createTime" align="center" label="创建时间" width="150">
              </el-table-column>
            </el-table>
            <div class="aa" style="margin-top: 20px;width: 70%;display: flex">
              <el-form-item style="" label="已关联数据源">
                <el-select filterable @change="getRelationTableList()" v-model="relationDbIdF" placeholder="请选择">
                  <el-option v-for="item in relationDbList" :key="item.relationDbId" :label="item.relationDbNameCn"
                    :value="item.relationDbId">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item style="" label="已关联表">
                <el-select filterable @change="getRelationTableAlex()" v-model="relationTableIdF" placeholder="请选择">
                  <el-option v-for="item in relationTableList" :key="item.relationTableId"
                    :label="item.relationTableName" :value="item.relationTableId">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item style="" label="关联表别名">
                <el-input readonly v-model="relationTableAlias">
                </el-input>
              </el-form-item>
              <el-form-item style="text-align: left;">
                <el-button :disabled="seeClu" @click="mainTableCluVisible = true"
                  style="padding: 8px 12px;margin-left: -80px !important;" type="primary" icon="el-icon-help">查看表属性
                </el-button>
              </el-form-item>
            </div>
            <div style="margin-top: 20px;display:flex;">
              <el-form-item class="aa" style="margin-left: -10px;" label="是否外联">
                <template>
                  <el-radio v-model="glTableCluForm.isExternalConnection" :label="1">是</el-radio>
                  <el-radio v-model="glTableCluForm.isExternalConnection" :label="0">否</el-radio>
                </template>
              </el-form-item>
              <el-form-item style="margin-left: 10px;" label="关联条件">
                <el-input style="border: none;width: 300px" v-model="glTableCluForm.relationCondition">
                </el-input>
              </el-form-item>
            </div>
            <el-form-item style="position: absolute;bottom: 20px;left: -50px">
              <el-button type="primary" @click="saveGlData()" icon="el-icon-circle-check">保存</el-button>
              <el-button type="primary" @click="glCluDrawer = false" icon="el-icon-circle-close">取消</el-button>
            </el-form-item>
          </el-form>
        </el-dialog>
      </div>
      <el-dialog :visible.sync="editDrawer" style="height: 80%;margin-top: 5%;" title="修改属性"
        :close-on-click-modal="false">
        <el-form ref="form" :model="editCluForm" label-width="80px" style="text-align: center">
          <el-form-item label="属性名称">
            <el-input v-model="editCluForm.attributeName"></el-input>
          </el-form-item>
          <el-form-item label="中文名称">
            <el-input v-model="editCluForm.attributeNameCn"></el-input>
          </el-form-item>
          <el-form-item label="属性类型">
            <el-select filterable v-model="editCluForm.attributeType" placeholder="请选择">
              <el-option v-for="item in typeList" :key="item.pubCode" :label="item.pubName" :value="item.pubCode">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="属性长度">
            <el-input v-model="editCluForm.attributeLength"></el-input>
          </el-form-item>
          <el-form-item label="是否主键">
            <el-select v-model="editCluForm.isPrimaryKey">
              <el-option value="0" label="否"></el-option>
              <el-option value="1" label="是"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="备注">
            <el-input v-model="editCluForm.remark"></el-input>
          </el-form-item>
          <el-form-item style="position: absolute;bottom: 20px;left: -50px">
            <el-button type="primary" @click="saveEditCluData()" icon="el-icon-circle-check">保存</el-button>
            <el-button type="primary" @click="editDrawer= false" icon="el-icon-circle-close">取消</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>
      <div class="openClu">
        <el-dialog title="关联主表属性" v-dialogdrag :close-on-click-modal="false" :visible.sync="mainTableCluVisible">
          <el-table height="330px" tooltip-effect="dark" :data="mainTableList">
            <el-table-column label="序号" width="60" align="center" :formatter="snFormatter">
            </el-table-column>
            <el-table-column prop="columnName" align="center" label="属性名称" width="100">
            </el-table-column>
            <el-table-column prop="columnNameCn" align="center" label="中文名称" width="120">
            </el-table-column>
            <el-table-column prop="columnType" align="center" label="属性类型" width="100">
            </el-table-column>
            <el-table-column prop="columnLength" align="center" label="属性长度" width="100">
            </el-table-column>
            <el-table-column prop="remark" align="center" label="备注" width="120">
            </el-table-column>
          </el-table>
        </el-dialog>
      </div>
    </div>
    <div class="importBox">
      <el-dialog title="导入元模型属性" :visible.sync="showImport" width="9.5rem" :close-on-click-modal="false">
        <div> <input id="addFileInput" type="file" ref="addFileInput" @change="chooseAddFileChange"
            style="display:none" />
          <div class="hint" style="float: left;margin-right: 0.2rem">选择导入文件:</div>
          <el-button type="primary" @click="chooseAddFile" style="float: left">选择文件</el-button>
        </div>
        <div style="height: 50px">
          <div class="file-name" style="font-size: 16px;line-height: 24px">{{addFileName}}</div>
        </div>
        <div slot="footer" class="dialog-footer" style="text-align: center">
          <el-button type="primary" @click="importFile()">确认</el-button>
          <el-button @click="showImport = false">取消</el-button>
        </div>
      </el-dialog>
    </div>
    <div class="importDatas">
      <el-dialog title="导入表属性" v-dialogdrag :close-on-click-modal="false" :visible.sync="showImportData">
        <el-table height="330px" tooltip-effect="dark" :data="importData">
          <el-table-column label="序号" width="60" align="center" :formatter="snFormatter">
          </el-table-column>
          <el-table-column prop="attributeName" align="center" label="属性名称" width="140">
            <template slot-scope="scope">
              <el-input v-model="scope.row.attributeName">{{scope.row.attributeName}}</el-input>
            </template>
          </el-table-column>
          <el-table-column prop="attributeNameCn" align="center" label="中文名称" width="140">
            <template slot-scope="scope">
              <el-input v-model="scope.row.attributeNameCn">{{scope.row.attributeNameCn}}</el-input>
            </template>
          </el-table-column>
          <el-table-column prop="attributeType" align="center" label="属性类型" width="150">
            <template slot-scope="scope">
              <el-select filterable v-model="scope.row.attributeType" placeholder="请选择">
                <el-option v-for="item in typeList" :key="item.pubCode" :label="item.pubName" :value="item.pubCode">
                </el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column prop="attributeLength" align="center" label="属性长度" width="120">
            <template slot-scope="scope">
              <el-input v-model="scope.row.attributeLength">{{scope.row.attributeLength}}</el-input>
            </template>
          </el-table-column>
          <el-table-column prop="isPrimaryKey" align="center" label="是否主键" width="150">
            <template slot-scope="scope">
              <el-select v-model="scope.row.isPrimaryKey">
                <el-option value="0" label="否"></el-option>
                <el-option value="1" label="是"></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column prop="isPrimaryKey" align="center" label="操作" width="100">
            <template slot-scope="scope">
              <el-button type="primary" @click="delImportData(scope.$index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div slot="footer" class="dialog-footer" style="text-align: center">
          <el-button type="primary" :disabled="!importData.length>0" @click="saveImportData()">保存</el-button>
          <el-button @click="showImportData = false">取消</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>
<script>
import axios from 'axios'
import qs from 'qs'

export default {
  name: 'aqysj',
  data () {
    return {
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
      dbList: [],
      tableList: [],
      typeList: [],
      categoryList: [],
      mainTableList: [],
      relationModelList: [],
      relationDbList: [],
      relationTableList: [],
      relationDbIdF: '',
      relationTableIdF: '',
      relationTableAlias: '',
      seeClu: true,
      seeAlias: false,
      modelId: '',
      tableData: [{ columnName: '2' }],
      tableDataClu: [],
      glTableCluFormD: {},
      tableForm: {
        dbId: '',
        id: '',
        modelName: '',
        modelNameCn: '',
        relationTableAlias: '',
        remark: '',
        relationDbName: '',
        relationDbNameCn: '',
        relationDbId: '',
        relationTableName: '',
        relationTableNameCn: '',
        relationTableId: '',
        categoryId: '',
        categoryName: '',
        categoryNameCn: ''
      },
      uploadTableForm: {

      },
      addTableForm: {
        modelName: '',
        modelNameCn: '',
        relationTableAlias: '',
        remark: '',
        relationDbName: '',
        relationDbNameCn: '',
        relationDbId: '',
        relationTableName: '',
        relationTableNameCn: '',
        relationTableId: '',
        categoryId: '',
        categoryName: '',
        categoryNameCn: ''
      },
      addTableCluForm: {
        attributeName: '',
        attributeNameCn: '',
        attributeType: '',
        attributeLength: '',
        remark: '',
        isPrimaryKey: ''
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
        relationDbId: '',
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
        relationDbId: '',
        relationColomnId: '',
        relationColomnName: '',
        relationColomnNameCn: '',
        relationCondition: '',
        mainTableClu: '',
        dbId: ''
      },
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
        modelNameCn: [
          { required: true, message: '.', trigger: 'blur' }
        ]
      }
    }
  },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val)
    },
    'tableForm.modelName'(val) {
      if (typeof val === 'undefined' || val === null || val === '') {
        console.log()
      } else {
        if (val.length > 0) {
          if (val.indexOf('t_meta_') === -1) {
            this.tableForm.modelName = 't_meta_'
          }
        }
      }
    },
    'addTableForm.modelName'(val) {
      if (val.indexOf('t_meta_') === -1) {
        this.addTableForm.modelName = 't_meta_'
      }
    }
  },
  methods: {
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
    handleCurrentChange (row) {
      axios.get(localStorage.URL + '/dbtablecolumn/' + row.id, qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        this.glCluObj = res.data.data
      }).catch(e => {
        // 请求失败处理。
        this.$message({
          message: e,
          type: 'error',
          center: true
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
    addTable: function () {
       for (var a in this.addTableForm) {
        this.addTableForm[a] = ''
      }
      this.addTableForm.modelName = 't_meta_'
      this.addTableDrawer = true
    },
    // 添加表格属性
    addTableClu: function () {
      for (var a in this.addTableCluForm) {
        this.addTableCluForm[a] = ''
      }
      this.addTableCluForm.isPrimaryKey = '0'
      this.addTableCluDrawer = true
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
      axios.post(localStorage.URL + '/file/upload/', formData, config).then((res) => {
        // 请求成功处理。
        this.toImport(res.data.data)
      }).catch(e => {
        // 请求失败处理。
        this.$message({
          message: e,
          type: 'error',
          center: true
        })
      })
    },
    toImport: function (url) {
      this.importData = []
      axios.get(localStorage.URL + '/excel/metaModelAttributeByExcel/?path=' + url, qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        if (res.data.success) {
          this.importData = res.data.data
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
            message: res.data.msg,
            type: 'error',
            center: true
          })
        }
      }).catch(e => {
        // 请求失败处理。
      })
    },
    // 删除导入属性
    delImportData(index) {
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
      let a = 0
      let flag = false
      let reg = /^[a-zA-Z_]{1}[a-zA-Z\d_]*/
      for (let i = 0; i < this.importData.length; i++) {
        if (parseInt(this.importData[i].isPrimaryKey) === 1) {
          a += 1
        }
        if (!reg.test(this.importData[i].attributeName)) {
          flag = true
        }
      }
      if (a > 1) {
        this.$message({
          message: '请保持主键唯一性',
          type: 'error',
          center: true
        })
        return
      }
      if (flag) {
        this.$message({
          message: '属性名称命名不规范',
          type: 'error',
          center: true
        })
        return
      }
      let res = this.importData
      for (let i = 0; i < res.length; i++) {
        axios.post(localStorage.URL + '/metamodelattribute/create', qs.stringify({
          attributeName: res[i].attributeName,
          attributeNameCn: res[i].attributeNameCn,
          attributeType: res[i].attributeType,
          attributeLength: res[i].attributeLength,
          isPrimaryKey: res[i].isPrimaryKey,
          isRequired: res[i].isRequired,
          isSearch: res[i].isSearch,
          isShow: res[i].isShow,
          modelId: this.modelId,
          remark: ''
        })).then((ress) => {
          // 请求成功处理。
          if (i === res.length - 1) {
            this.$message({
              message: '保存成功',
              type: 'success',
              center: true
            })
            this.showImportData = false
            window.setTimeout(() => {
              this.getData()
            }, 3000)
          }
        }).catch(e => {
          // 请求失败处理。
          this.$message({
            message: e,
            type: 'error',
            center: true
          })
        })
      }
    },
    glColumn: function (row) {
      this.getTabData()
      this.glTableCluFormD = row
      this.glTableCluName = '关联元模型属性' + row.attributeName
      axios.get(localStorage.URL + '/metamodelattribute/' + row.id, qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        res = res.data.data
        // this.glTableCluForm = res
        if (res.relationDbId === undefined) {
          // 第一次关联
          this.glTableCluForm = res
          this.glTableCluForm.relationDbId = this.tableForm.relationDbId
          this.getDbTableList()
          this.glTableCluForm.relationTableId = this.tableForm.relationTableId
          this.getTableClu()
          this.glRelationTableName = this.tableForm.relationTableAlias
        } else {
          this.glTableCluForm.relationDbId = res.relationDbId
          this.getDbTableList()
          this.glTableCluForm.relationTableId = res.relationTableId
          this.getTableClu()
          this.glTableCluForm = res
          this.glRelationTableName = this.glTableCluForm.relationTableAlias
          this.glCluObj.columnName = this.glTableCluForm.relationColomnName
        }
        this.glTableCluForm.id = row.id
        this.relationDbIdF = this.relationTableIdF = this.relationTableAlias = ''

        this.getRelationList(row.modelId)
        this.glCluDrawer = this.seeClu = true
      }).catch(e => {
        // 请求失败处理。
        this.$message({
          message: e,
          type: 'error',
          center: true
        })
      })
    },
    // 获取已关联数据
    getRelationList: function (id) {
      this.relationModelList = this.relationDbList = this.relationTableList = []
      this.relationDbIdF = this.relationTableIdF = this.relationTableAlias = ''
      axios.get(localStorage.URL + '/metamodelattribute/relationinfolist?modelId=' + id, qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        this.relationModelList = res.data.data
        for (var i = 0; i < this.relationModelList.length; i++) {
          var ifPush = true
          for (var j = 0; j < this.relationDbList.length; j++) {
            if (this.relationDbList[j].relationDbId === this.relationModelList[i].relationDbId) {
              ifPush = false
            }
          }
          if (ifPush) {
            this.relationDbList.push(this.relationModelList[i])
          }
          this.relationDbIdF = this.tableForm.relationDbId
          this.getRelationTableList(1)
        }
      }).catch(e => {
        // 请求失败处理。
        this.$message({
          message: e,
          type: 'error',
          center: true
        })
      })
    },
    // 根据已关联数据源获取已关联表
    getRelationTableList: function (type) {
      this.relationTableList = []
      this.relationTableIdF = this.relationTableAlias = ''
      this.seeClu = true
      for (var i = 0; i < this.relationModelList.length; i++) {
        if (this.relationModelList[i].relationDbId === this.relationDbIdF) {
          this.relationTableList.push(this.relationModelList[i])
        }
      }
      if (type === 1) {
        this.relationTableIdF = this.tableForm.relationTableId
        this.getRelationTableAlex()
      }
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
      axios.get(localStorage.URL + '/dbtablecolumn/list?tableId=' + this.relationTableIdF, qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        this.mainTableList = res.data.data
      }).catch(e => {
        // 请求失败处理。
        this.$message({
          message: e,
          type: 'error',
          center: true
        })
      })
    },
    // 获取公共数据
    getCommonList: function () {
      // 获取数据源
      axios.get(localStorage.URL + '/dbconfig/list', qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        this.dbList = res.data.data
      }).catch(e => {
        // 请求失败处理。
        this.$message({
          message: e,
          type: 'error',
          center: true
        })
      })
      // 获取类目列表
      axios.get(localStorage.URL + '/metamodelcategory/list', qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        this.categoryList = res.data.data
      }).catch(e => {
        // 请求失败处理。
        this.$message({
          message: e,
          type: 'error',
          center: true
        })
      })
      // 获取数据类型列表
      axios.get(localStorage.URL + '/pubcode/listByType?pubTypeCode=mysql_field_type', qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        this.typeList = res.data.data
      }).catch(e => {
        // 请求失败处理。
        this.$message({
          message: e,
          type: 'error',
          center: true
        })
      })
    },
    // 根据数据源获取表列表
    getDbTableList: function (type) {
      let ids = type === 0 ? this.addTableForm.relationDbId : this.glTableCluForm.relationDbId
      this.tableList = []
      this.glCluObj = {}
      this.addTableForm.relationTableId = ''
      this.glTableCluForm.relationTableId = this.glTableCluForm.relationTableAlias = ''
      this.tableDataClu = []
      this.seeAlias = false
      if (ids !== undefined) {
        axios.get(localStorage.URL + '/dbtable/list?dbId=' + ids, qs.stringify({
        })).then((res) => {
          // 请求成功处理。
          this.tableList = res.data.data
        }).catch(e => {
          // 请求失败处理。
          this.$message({
            message: e,
            type: 'error',
            center: true
          })
        })
      }
    },
    getTableList: function (type) {
      this.tableList = []
      if (type === 0 || type === '0') {
        this.tableForm.relationTableId = ''
      }
      if (this.tableForm.relationDbId !== undefined) {
        axios.get(localStorage.URL + '/dbtable/list?dbId=' + this.tableForm.relationDbId, qs.stringify({
        })).then((res) => {
          // 请求成功处理。
          this.tableList = res.data.data
        }).catch(e => {
          // 请求失败处理。
          this.$message({
            message: e,
            type: 'error',
            center: true
          })
        })
      }
    },
    searchTable: function (id, nodeType) {
      if (nodeType === 'MODEL') {
        this.modelId = id
        this.selForm.cluName = ''
        this.getData()
        this.getTabData()
        this.ifSel = false
      }
    },
    // 删除表
    delTable: function () {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        axios.post(localStorage.URL + '/metamodel/delete', qs.stringify({
          id: this.modelId
        })).then((res) => {
          // 请求成功处理。
          this.modelId = ''
          for (var a in this.tableForm) {
            this.tableForm[a] = ''
          }
          this.getTree()
          this.$message({
            message: res.data.msg,
            type: 'success',
            center: true
          })
        }).catch(e => {
          // 请求失败处理。
          this.$message({
            message: e,
            type: 'error',
            center: true
          })
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    getTree: function () {
      axios.get(localStorage.URL + '/metamodelcategory/tree', qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        res = res.data.data
        this.data[0].children = res
      }).catch(e => {
        // 请求失败处理。
        this.$message({
          message: e,
          type: 'error',
          center: true
        })
      })
    },
    // 保存修改元模型信息
    saveTabData: function () {
      for (var i = 0; i < this.tableList.length; i++) {
        if (this.tableList[i].id === this.tableForm.relationTableId) {
          this.tableForm.relationTableName = this.tableList[i].tableName
          this.tableForm.relationTableNameCn = this.tableList[i].tableNameCn
        }
      }
      for (var j = 0; j < this.categoryList.length; j++) {
        if (this.categoryList[j].id === this.tableForm.categoryId) {
          this.tableForm.categoryName = this.categoryList[j].categoryName
          this.tableForm.categoryNameCn = this.categoryList[j].categoryNameCn
        }
      }
      for (var z = 0; z < this.dbList.length; z++) {
        if (this.dbList[z].id === this.tableForm.relationDbId) {
          this.tableForm.relationDbName = this.dbList[z].dbName
          this.tableForm.relationDbNameCn = this.dbList[z].dbNameCn
        }
      }
      // 判断表别名是否存在
      var ifHave = true
      for (var q = 0; q < this.tableData.length; q++) {
        if (this.tableForm.relationTableAlias === this.tableData[q].relationTableAlias) {
          if (this.tableForm.relationTableId === this.tableData[q].relationTableId) {
            ifHave = true
          } else {
            this.$message({
              message: '表别名已存在',
              type: 'error',
              center: true
            })
            ifHave = false
            return
          }
        }
        if (this.tableForm.relationTableId === this.tableData[q].relationTableId) {
          if (this.tableForm.relationTableAlias === this.tableData[q].relationTableAlias) {
            ifHave = true
          } else {
            this.$message({
              message: '关联主表已有属性被关联，表别名暂不可修改',
              type: 'error',
              center: true
            })
            ifHave = false
            return
          }
        }
      }
      // 判断别名是否合法
      var ifAdd2 = true
      if (this.tableForm.relationTableId !== '' && this.tableForm.relationTableId !== undefined) {
        var re = /^[a-zA-Z][a-zA-Z0-9_]*$/
        if (!re.test(this.tableForm.relationTableAlias)) {
          this.$message({
            message: '表别名不合法，请以字母开头，由字母、数字、下划线组成',
            type: 'error',
            center: true
          })
          ifAdd2 = false
        }
      }
      var ifAdd = false
      this.$refs['editTableForm'].validate((valid) => {
        if (valid) {
          ifAdd = true
        } else {
          this.$message({
            message: '请输入中文名称',
            type: 'error',
            center: true
          })
          return false
        }
      })
      if (ifAdd && ifAdd2 && ifHave) {
        axios.post(localStorage.URL + '/metamodel/update', qs.stringify({
          id: this.tableForm.id,
          modelName: this.tableForm.modelName,
          modelNameCn: this.tableForm.modelNameCn,
          relationTableAlias: this.tableForm.relationTableAlias,
          remark: this.tableForm.remark,
          relationTableName: this.tableForm.relationTableName,
          relationTableNameCn: this.tableForm.relationTableNameCn,
          relationTableId: this.tableForm.relationTableId,
          relationDbId: this.tableForm.relationDbId,
          relationDbName: this.tableForm.relationDbName,
          relationDbNameCn: this.tableForm.relationDbNameCn,
          categoryId: this.tableForm.categoryId,
          categoryName: this.tableForm.categoryName,
          categoryNameCn: this.tableForm.categoryNameCn
        })).then((res) => {
          // 请求成功处理。
          this.getTree()
          this.getTabData()
          this.$message({
            message: res.data.msg,
            type: 'success',
            center: true
          })
        }).catch(e => {
          // 请求失败处理。
          this.$message({
            message: e,
            type: 'error',
            center: true
          })
        })
      }
    },
    // 获取表基本信息
    getTabData: function () {
      this.tableForm = {}
      axios.get(localStorage.URL + '/metamodel/' + this.modelId, qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        res = res.data.data
        this.tableForm = res
        this.getTableList()
        this.tableForm = res
      }).catch(e => {
        // 请求失败处理。
        this.$message({
          message: e,
          type: 'error',
          center: true
        })
      })
    },
    // 获取表属性列表
    getData: function () {
      this.tableData = []
      axios.get(localStorage.URL + '/metamodelattribute/list?modelId=' + this.modelId + '&attributeName=' + this.selForm.cluName, qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        this.tableData = res.data.data
      }).catch(e => {
        // 请求失败处理。
        this.$message({
          message: e,
          type: 'error',
          center: true
        })
      })
    },
    // 保存添加元模型
    saveAddTable: function () {
      for (var i = 0; i < this.tableList.length; i++) {
        if (this.tableList[i].id === this.addTableForm.relationTableId) {
          this.addTableForm.relationTableName = this.tableList[i].tableName
          this.addTableForm.relationTableNameCn = this.tableList[i].tableNameCn
        }
      }
      for (var j = 0; j < this.categoryList.length; j++) {
        if (this.categoryList[j].id === this.addTableForm.categoryId) {
          this.addTableForm.categoryName = this.categoryList[j].categoryName
          this.addTableForm.categoryNameCn = this.categoryList[j].categoryNameCn
        }
      }
      for (var z = 0; z < this.dbList.length; z++) {
        if (this.dbList[z].id === this.addTableForm.relationDbId) {
          this.addTableForm.relationDbName = this.dbList[z].dbName
          this.addTableForm.relationDbNameCn = this.dbList[z].dbNameCn
        }
      }
      // 判断别名是否合法
      var ifAdd = true
      if (this.addTableForm.relationTableId !== '' && this.addTableForm.relationTableId !== undefined) {
        var re = /^[a-zA-Z][a-zA-Z0-9_]*$/
        if (!re.test(this.addTableForm.relationTableAlias)) {
          this.$message({
            message: '表别名不合法，请以字母开头，由字母、数字、下划线组成',
            type: 'error',
            center: true
          })
          ifAdd = false
        }
      }
      this.$refs['addTableForm'].validate((valid) => {
        if (valid) { } else {
          ifAdd = false
          this.$message({
            message: '请输入中文名称',
            type: 'error',
            center: true
          })
          return false
        }
      })
      if (ifAdd) {
        axios.post(localStorage.URL + '/metamodel/create', qs.stringify({
          modelName: this.addTableForm.modelName,
          modelNameCn: this.addTableForm.modelNameCn,
          relationTableAlias: this.addTableForm.relationTableAlias,
          remark: this.addTableForm.remark,
          relationTableName: this.addTableForm.relationTableName,
          relationTableNameCn: this.addTableForm.relationTableNameCn,
          relationTableId: this.addTableForm.relationTableId,
          relationDbId: this.addTableForm.relationDbId,
          relationDbName: this.addTableForm.relationDbName,
          relationDbNameCn: this.addTableForm.relationDbNameCn,
          categoryId: this.addTableForm.categoryId,
          categoryName: this.addTableForm.categoryName,
          categoryNameCn: this.addTableForm.categoryNameCn
        })).then((res) => {
          // 请求成功处理。
          this.addTableDrawer = false
          this.getTree()
          this.$message({
            message: res.data.msg,
            type: 'success',
            center: true
          })
        }).catch(e => {
          // 请求失败处理。
          this.$message({
            message: e,
            type: 'error',
            center: true
          })
        })
      }
    },
    // 保存添加表属性
    saveAddCluData: function () {
      axios.post(localStorage.URL + '/metamodelattribute/create', qs.stringify({
        attributeName: this.addTableCluForm.attributeName,
        attributeNameCn: this.addTableCluForm.attributeNameCn,
        attributeType: this.addTableCluForm.attributeType,
        attributeLength: this.addTableCluForm.attributeLength,
        modelId: this.modelId,
        isPrimaryKey: this.addTableCluForm.isPrimaryKey,
        remark: this.addTableCluForm.remark
      })).then((res) => {
        // 请求成功处理。
        this.addTableCluDrawer = false
        this.getData()
        this.$message({
          message: res.data.msg,
          type: 'success',
          center: true
        })
      }).catch(e => {
        // 请求失败处理。
        this.$message({
          message: e,
          type: 'error',
          center: true
        })
      })
    },
    // 修改某属性
    editColumn: function (row) {
      axios.get(localStorage.URL + '/metamodelattribute/' + row.id, qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        this.editCluForm = res.data.data
        this.editDrawer = true
      }).catch(e => {
        // 请求失败处理。
        this.$message({
          message: e,
          type: 'error',
          center: true
        })
      })
    },
    // 保存修改表属性
    saveEditCluData: function () {
      axios.post(localStorage.URL + '/metamodelattribute/update', qs.stringify({
        attributeName: this.editCluForm.attributeName,
        attributeNameCn: this.editCluForm.attributeNameCn,
        attributeType: this.editCluForm.attributeType,
        attributeLength: this.editCluForm.attributeLength,
        relationTableId: this.editCluForm.relationTableId,
        relationTableName: this.editCluForm.relationTableName,
        relationTableNameCn: this.editCluForm.relationTableNameCn,
        relationTableAlias: this.editCluForm.relationTableAlias,
        relationDbId: this.editCluForm.relationDbId,
        relationDbName: this.editCluForm.relationDbName,
        relationDbNameCn: this.editCluForm.relationDbNameCn,
        relationColomnId: this.editCluForm.relationColomnId,
        relationColomnName: this.editCluForm.relationColomnName,
        relationColomnNameCn: this.editCluForm.relationColomnNameCn,
        relationCondition: this.editCluForm.relationCondition,
        modelId: this.editCluForm.modelId,
        id: this.editCluForm.id,
        remark: this.editCluForm.remark,
        isPrimaryKey: this.editCluForm.isPrimaryKey,
        isRequired: this.editCluForm.isRequired,
        orderNum: this.editCluForm.orderNum,
        isShow: this.editCluForm.isShow,
        isSearch: this.editCluForm.isSearch,
        dicDbId: this.editCluForm.dicDbId,
        dicDbName: this.editCluForm.dicDbName,
        dicDbNameCn: this.editCluForm.dicDbNameCn,
        dicTableId: this.editCluForm.dicTableId,
        dicTableName: this.editCluForm.dicTableName,
        dicTableNameCn: this.editCluForm.dicTableNameCn,
        dicTableAlias: this.editCluForm.dicTableNameCn,
        dicColomnId: this.editCluForm.dicTableNameCn,
        dicColomnName: this.editCluForm.columnName,
        dicColomnNameCn: this.editCluForm.columnNameCn,
        dicGroupType: this.editCluForm.dicGroupType
      })).then((res) => {
        // 请求成功处理。
        this.editDrawer = false
        this.getData()
        this.$message({
          message: res.data.msg,
          type: 'success',
          center: true
        })
      }).catch(e => {
        // 请求失败处理。
        this.$message({
          message: e,
          type: 'error',
          center: true
        })
      })
    },
    // 删除表属性
    delTableClu: function (id) {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        axios.post(localStorage.URL + '/metamodelattribute/delete', qs.stringify({
          id: id
        })).then((res) => {
          // 请求成功处理。
          this.getData()
          this.$message({
            message: res.data.msg,
            type: 'success',
            center: true
          })
        }).catch(e => {
          // 请求失败处理。
          this.$message({
            message: e,
            type: 'error',
            center: true
          })
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    // 获取关联表所有属性,
    getTableClu: function () {
      this.glCluObj = {}
      this.tableDataClu = []
      axios.get(localStorage.URL + '/dbtablecolumn/list?tableId=' + this.glTableCluForm.relationTableId, qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        this.tableDataClu = res.data.data
        this.checkAlias()
      }).catch(e => {
        // 请求失败处理。
        this.$message({
          message: e,
          type: 'error',
          center: true
        })
      })
    },
    // 判断别名是否存在,是否可修改
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
    },
    // 保存关联表信息
    saveGlData: function () {
      for (var i = 0; i < this.tableList.length; i++) {
        if (this.tableList[i].id === this.glTableCluForm.relationTableId) {
          this.glTableCluForm.relationTableName = this.tableList[i].tableName
          this.glTableCluForm.relationTableNameCn = this.tableList[i].tableNameCn
        }
      }
      for (var z = 0; z < this.dbList.length; z++) {
        if (this.dbList[z].id === this.glTableCluForm.relationDbId) {
          this.glTableCluForm.relationDbName = this.dbList[z].dbName
          this.glTableCluForm.relationDbNameCn = this.dbList[z].dbNameCn
        }
      }
      // 判断别名是否存在
      var ifAdd = true
      if (!this.seeAlias) {
        for (var j = 0; j < this.tableData.length; j++) {
          if (this.tableData[j].id !== this.glTableCluForm.id && this.glRelationTableName === this.tableData[j].relationTableAlias) {
            this.$message({
              message: '表别名已存在',
              type: 'error',
              center: true
            })
            ifAdd = false
          }
        }
      }
      // 判断别名是否合法
      var re = /^[a-zA-Z][a-zA-Z0-9_]*$/
      if (!re.test(this.glRelationTableName)) {
        this.$message({
          message: '表别名不合法，请以字母开头，由字母、数字、下划线组成',
          type: 'error',
          center: true
        })
        ifAdd = false
      }
      if (ifAdd) {
        axios.post(localStorage.URL + '/metamodelattribute/update', qs.stringify({
          id: this.glTableCluForm.id,
          modelId: this.glTableCluForm.modelId,
          isExternalConnection: this.glTableCluForm.isExternalConnection,
          attributeName: this.glTableCluForm.attributeName,
          attributeNameCn: this.glTableCluForm.attributeNameCn,
          attributeType: this.glTableCluForm.attributeType,
          attributeLength: this.glTableCluForm.attributeLength,
          relationDbId: this.glTableCluForm.relationDbId,
          relationDbName: this.glTableCluForm.relationDbName,
          relationDbNameCn: this.glTableCluForm.relationDbNameCn,
          relationTableId: this.glTableCluForm.relationTableId,
          relationTableName: this.glTableCluForm.relationTableName,
          relationTableNameCn: this.glTableCluForm.relationTableNameCn,
          relationTableAlias: this.glRelationTableName,
          relationColomnId: this.glCluObj.id,
          relationColomnName: this.glCluObj.columnName,
          relationColomnNameCn: this.glCluObj.columnNameCn,
          relationCondition: this.glTableCluForm.relationCondition,
          dicDbId: this.glTableCluForm.dicDbId,
          dicDbName: this.glTableCluForm.dicDbName,
          dicDbNameCn: this.glTableCluForm.dicDbNameCn,
          dicTableId: this.glTableCluForm.dicTableId,
          dicTableName: this.glTableCluForm.dicTableName,
          dicTableNameCn: this.glTableCluForm.dicTableNameCn,
          dicTableAlias: this.glTableCluForm.dicTableAlias,
          dicColomnId: this.glTableCluForm.dicColomnId,
          dicColomnName: this.glTableCluForm.dicColomnName,
          dicColomnNameCn: this.glTableCluForm.dicColomnNameCn,
          dicGroupType: this.glTableCluForm.dicGroupType,
          isPrimaryKey: this.glTableCluForm.isPrimaryKey,
          isRequired: this.glTableCluForm.isRequired,
          orderNum: this.glTableCluForm.orderNum,
          isShow: this.glTableCluForm.isShow,
          isSearch: this.glTableCluForm.isSearch
        })).then((res) => {
          // 请求成功处理。
          this.glCluDrawer = false
          this.getData()
          this.$message({
            message: res.data.msg,
            type: 'success',
            center: true
          })
        }).catch(e => {
          // 请求失败处理。
          this.$message({
            message: e,
            type: 'error',
            center: true
          })
        })
      }
    }
  },
  beforeMount() {
    this.ifSel = true
    this.getCommonList()
    this.getTree()
  }
}
</script>
<style lang="stylus" rel="stylesheet/stylus" scoped>
  .cell >.el-button
    padding 5px 10px !important
  .aqysj
    width 100%
    height 100%
    .cell .el-input__inner
      height 30px
    .aa .el-input,.aa .el-form-item__content,.aa .el-input__inner
      width  3rem !important
    .el-dialog
      width 10rem
      height auto
      margin 11% 36%
    .importBox .el-dialog
      width 10rem
      height 5rem
      margin 11% 36%
    .importDatas .el-dialog
      width 18rem
      margin auto
      .el-select
        width 2.5rem
    .addGl .el-dialog
      width 19rem
      height 14rem
      margin 2rem 11rem
    .openClu .el-dialog
      width 14rem
      height 9rem
      margin 2rem 11rem
    input[readonly='readonly']
      background #F5F7FA
      color #CAC4CC
    .el-select input[readonly='readonly']
      background #fff
      color #000
    .el-button
      padding: 5px 30px
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
          width 98%
          margin auto
          .el_group
            display flex
            width 80%
            margin auto
      .tit
        font-size 16px
        height 40px
        line-height 40px
        text-indent 1%
        margin-bottom 10px
        border-bottom 1px solid lightgrey
</style>
