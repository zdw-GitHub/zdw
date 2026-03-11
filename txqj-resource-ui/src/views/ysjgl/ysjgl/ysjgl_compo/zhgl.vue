<template>
    <div class="zhgl">
      <div class="qjcx_con">
        <div class="con_right">
          <div class="r_top">
            <div class="cell" style="width: 50%;text-align: right;position:absolute;top:20px;right:20px">
                <!-- <el-button type="primary" @click="uploadTable()" icon="el-icon-upload2">发布</el-button>
                <el-button type="primary" @click="addTable()" icon="el-icon-plus">新增</el-button> -->
                <!-- <el-button :disabled="ifSel" type="primary" @click="saveTabData()" icon="el-icon-circle-check" style="position:absolute;margin-top:230px;right:20px">保存</el-button> -->
                <!-- <el-button :disabled="ifSel" type="primary" @click="getTabData()" icon="el-icon-circle-close">复制</el-button>
                <el-button :disabled="ifSel" type="primary" @click="delTable()"  icon="el-icon-delete">删除</el-button> -->
              </div>
            <div class="table_space" style="height: 30px;display: flex">
            </div>
          </div>
          <div class="tabBarArea tit">
            <el-form style="display: flex" size="small">
              <el-form-item style="flex: 1" label="关联方式" >
                <el-select v-model="tabName">
                  <el-option value="1" label="字段关联"></el-option>
                  <el-option value="2" label="SQL关联"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item style="text-align: right">
                <el-button @click="saveGlType" type="success" plain icon="el-icon-circle-check" size="mini" v-if="versionShow">保存</el-button>
              </el-form-item>
            </el-form>
                    <!--<div class="tabBarBtn" :class="[tabName=='Tab1'?'tabBarBtnActive':'']"
                        @click="tabName='Tab1'">字段关联</div>
                    <div class="tabBarBtn" :class="[tabName=='Tab2'?'tabBarBtnActive':'']"
                        @click="tabName='Tab2'">SQL关联</div>-->
                </div>
          <div class="r_bot">
            <div  v-if="tabName==='1'">
                <div class="cell" style="float: right;width: 120%;text-align: right;margin-bottom:10px">
                  <el-form style="margin-right:35px;display: inline;" ref="selForm" :model="selForm" label-width="80px">
                    <el-input style="width: 300px;height: 30px" v-model="selForm.cluName" placeholder="请输入属性名"></el-input>
                  </el-form>
                  <el-button :disabled="ifSel" plain style="padding:5px 25px;width:80px" type="info" @click="getData()" icon="el-icon-search" v-hasPermi="['mm:relation:list']">搜索</el-button>
                  <el-button :disabled="ifSel" plain style="padding:5px 25px;width:80px" @click="getData" icon="el-icon-refresh">刷新</el-button>
                </div>
                <div class="table_space">
                  <el-table
                    :data="tableData"
                    stripe
                    height="370px"
                    tooltip-effect="dark"
                    :header-cell-style="{background:'#F5F5F5'}"
                    v-loading="loading"
                    >
                    <el-table-column
                      label="序号"
                      width="80"
                      align="center"
                      :formatter="snFormatter">
                    </el-table-column>
                    <el-table-column
                      prop="columnName"
                      align="center"
                      label="属性名称"
                      width="100">
                    </el-table-column>
                    <el-table-column
                      prop="columnNameCn"
                      align="center"
                      label="中文名"
                      width="100">
                    </el-table-column>
                    <el-table-column
                      prop="columnType"
                      align="center"
                      label="属性类型"
                      width="120">
                    </el-table-column>
                    <el-table-column
                      prop="columnLength"
                      align="center"
                      label="属性长度"
                      width="150">
                    </el-table-column>
                    <el-table-column
                      prop="relationDbId"
                      align="center"
                      label="关联数据源"
                      width="120">
                      <template slot-scope="scope">
                        <!-- scope.$index为当前行下标，cellIndex所点击行下标 -->
                        <el-select v-model="scope.row.relationDbId"  @change="getDbTableList(scope.row,scope.row.relationDbId)"  clearable v-show="scope.$index == cellIndex" style="width:100px">
                          <el-option
                              v-for="item in dbList"
                              :key="item.id"
                              :label="item.dbNameCn"
                              :value="item.id">
                            </el-option>
                        </el-select>
                        <p v-show="scope.$index != cellIndex">{{ tableData[scope.$index].relationDbName }}</p>
                      </template>
                    </el-table-column>
                    <el-table-column
                      prop="relationTableId"
                      align="center"
                      label="关联表"
                      width="120"
                      >
                      <template slot-scope="scope">
                        <!-- scope.$index为当前行下标，cellIndex所点击行下标 -->
                        <el-select v-model="scope.row.relationTableId" filterable @change="getRelationTableAlex(scope.row,scope.row.relationTableId)" :disabled="isRelationTableId"  clearable v-show="scope.$index == cellIndex" style="width:100px;">
                          <el-option
                          v-for="item in tableList"
                          :key="item.id"
                          :label="item.tableName"
                          :value="item.id">
                            </el-option>
                        </el-select>
                        <p v-show="scope.$index != cellIndex">{{ tableData[scope.$index].relationTableName }}</p>
                      </template>
                    </el-table-column>
                    <el-table-column
                      prop="relationTableAlias"
                      align="center"
                      label="关联表别名"
                      width="150">
                      <template slot-scope="scope">
                        <!-- scope.$index为当前行下标，cellIndex所点击行下标 -->
                        <el-input v-model="scope.row.relationTableAlias"  v-show="scope.$index == cellIndex">
                        </el-input>
                        <p v-show="scope.$index != cellIndex">{{ scope.row.relationTableAlias }}</p>
                      </template>
                    </el-table-column>
                    <el-table-column
                      prop="relationColumnId"
                      align="center"
                      label="关联属性"
                      width="150">
                      <template slot-scope="scope">
                        <!-- scope.$index为当前行下标，cellIndex所点击行下标 -->
                        <el-select v-model="scope.row.relationColumnId"  :disabled="isrelationColumnId"  @changer="$forceUpdate()" v-show="scope.$index == cellIndex" style="width:100px;" clearable>
                          <el-option
                          v-for="item in columnList"
                          :key="item.id"
                          :label="item.columnName"
                          :value="item.id">
                            </el-option>
                        </el-select>
                        <p v-show="scope.$index != cellIndex">{{ tableData[scope.$index].relationColumnName }}</p>
                      </template>
                    </el-table-column>
                    <el-table-column
                      prop="isExternalConnection"
                      align="center"
                      label="是否外联"
                      width="150">
                        <template slot-scope="scope">
                        <!-- scope.$index为当前行下标，cellIndex所点击行下标 -->
                        <el-select v-model="scope.row.isExternalConnection"  v-show="scope.$index == cellIndex" style="width:100px" clearable>
                          <el-option :key="1" :value="1" label="是"></el-option>
                          <el-option :key="0" :value="0" label="否"></el-option>
                        </el-select>
                        <p v-show="scope.$index != cellIndex" v-if="scope.row.isExternalConnection===1">是</p>
                        <p v-show="scope.$index != cellIndex" v-if="scope.row.isExternalConnection===0">否</p>
                      </template>
                    </el-table-column>
                    <el-table-column
                      prop="relationCondition"
                      align="center"
                      label="关联条件"
                      width="150">
                      <template slot-scope="scope">
                        <!-- scope.$index为当前行下标，cellIndex所点击行下标 -->
                        <el-input v-model="scope.row.relationCondition"  v-show="scope.$index == cellIndex">
                        </el-input>
                        <p v-show="scope.$index != cellIndex">{{ scope.row.relationCondition }}</p>
                      </template>
                    </el-table-column>
                    <el-table-column
                      label="操作"
                      fixed="right"
                      width="380"
                      align="center"
                      v-if="versionShow">
                      <template slot-scope="scope">
                       <!--  <el-button plain type="primary" disabled="false" @click="glColumn(scope.row)" icon="el-icon-connection">SQL</el-button> -->
                        <el-button size="mini" type="text" @click="glColumn(scope.row)" icon="el-icon-connection" v-hasPermi="['mm:relation:relationEdit']">关联</el-button>
                        <el-button  @click="editTable(scope,scope.row)" size="mini" type="text" icon="el-icon-edit" v-if="scope.$index != cellIndex" v-hasPermi="['mm:relation:edit']">编辑</el-button>
                        <el-button  @click="saveTable(scope)" size="mini" type="text" v-else icon="el-icon-circle-check" v-hasPermi="['mm:relation:edit']">保存</el-button>
                        <el-button  type="text" @click="deletTable(scope)" size="mini" icon="el-icon-delete" v-if="scope.$index != cellIndex"  v-hasPermi="['mm:tech:remove']">删除</el-button>
                        <el-button  @click="cancel()" size="mini" type="text" v-else icon="el-icon-circle-close">取消</el-button>
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
              <div  class="table_space" v-if="tabName==='2'" style="display:flex">
                  <div style="background-color:#F2F2F2;height:450px;flex:1;padding: 4px">
                    <span style="color:#538DC2;height: 40px;">已对应字段</span>
                    <div style="height: 410px;margin-top:10px;overflow-y: auto;">
                      <ul style="margin-left:-20px">
                        <li v-for="item in sqlCluName" :style="{color:item.cluType===0?'#000':'#538DC2'}">{{item.cluName}}</li>
                      </ul>
                    </div>
                  </div>
                  <div style="background-color:#F2F2F2;height:450px;margin-left:20px;flex:3;padding: 4px ">
                    <span style="color:#538DC2;height: 40px;">SQL</span>
                    <el-button @click="saveSql" :disabled="ifSel" style="float: right;" size="mini" plain type="success" icon="el-icon-circle-check" v-if="versionShow">保存</el-button>
                    <div id="sqlSpace" style="margin-top:20px">
                      <el-input :rows="18" type="textarea" v-model="sqlStr"></el-input>
                    </div>
                  </div>
              </div>
          </div>
        </div>
      </div>
      <div>
        <div class="addGl">
          <el-dialog
            :visible.sync="glCluDrawer"
            :title="glTableCluName"
            :close-on-click-modal="false"
          >
            <el-form :model="glTableCluForm" label-width="80px"  ref="glTableCluForm">

              <div style="margin-top: 20px;display: flex;width: 70%">
                <el-form-item style="flex: 6" label="关联数据源" prop="relationDbIdF">
                  <el-select filterable @change="getDbTableList(1,glTableCluForm.relationDbId)" v-model="glTableCluForm.relationDbId" placeholder="请选择" clearable>
                      <el-option
                        v-for="item in dbList"
                        :key="item.id"
                        :label="item.dbNameCn"
                        :value="item.id">
                      </el-option>
                  </el-select>
                </el-form-item>
                <el-form-item style="flex: 6" label="关联表"  prop="relationTableIdF">
                  <el-select @change="getTableClu(glTableCluForm.relationTableId)" filterable v-model="glTableCluForm.relationTableId" placeholder="请选择" clearable>
                    <el-option
                      v-for="item in tableList"
                      :key="item.id"
                      :label="item.tableName"
                      :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </div>
              <div style="display: flex;width: 70%">
                <el-form-item style="flex: 6" label="关联属性" prop="columnName">
                  <el-select disabled placeholder="单击表格进行选择" v-model="glCluObj.columnId" @change="$forceUpdate()" clearable>
                    <el-option :key="columnListA.id" :value="columnListA.id" :label="columnListA.label"></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item style="flex: 6" label="关联表别名" prop="glRelationTableName">
                  <el-input :readonly="seeAlias" v-model="glTableCluForm.glRelationTableName" placeholder="请以字母开头，由字母、数字、下划线组成" ></el-input>
                </el-form-item>
              </div>
              <el-input style="width: 250px;height: 20px;margin-left:520px" v-model="searchGlName" placeholder="请输入属性名称"></el-input>
              <el-button :disabled="ifSel" size="mini" plain style="padding:5px 15px;width:80px;margin-left:30px" type="info" @click="getTableClu(glTableCluForm.relationTableId)" icon="el-icon-search" >搜索</el-button>
              <el-table
                ref="multipleTable"
                :data="tableDataClu"
                height="300px"
                style="margin-top:20px;margin-left:2%;width: 96%"
                tooltip-effect="dark"
                highlight-current-row
                @current-change="handleCurrentChange"
                :header-cell-style="{background:'#F5F5F5'}">
                <el-table-column
                  label="序号"
                  width="80"
                  align="center"
                  :formatter="snFormatter">
                </el-table-column>
                <el-table-column
                  prop="columnName"
                  align="center"
                  label="属性名称"
                  width="120">
                </el-table-column>
                <el-table-column
                  prop="columnNameCn"
                  align="center"
                  label="中文名称"
                  width="120">
                </el-table-column>
                <el-table-column
                  prop="columnType"
                  align="center"
                  label="属性类型"
                  width="120">
                </el-table-column>
                <el-table-column
                  prop="columnLength"
                  align="center"
                  label="属性长度"
                  width="120">
                </el-table-column>
                <el-table-column
                  prop="remark"
                  align="center"
                  label="备注"
                  width="150">
                </el-table-column>
                <el-table-column
                  prop="createTime"
                  align="center"
                  label="创建时间"
                  width="150">
                </el-table-column>
              </el-table>

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
            </el-form>
            <div slot="footer" class="dialog-footer">
              <el-button  type="primary" @click="saveGlData()" >确 定</el-button>
              <el-button @click="glCluDrawer= false">取 消</el-button>
            </div>
          </el-dialog>
        </div>
        <div class="openClu">
          <el-dialog
            title="关联主表属性"
            v-dialogDrag
            :close-on-click-modal="false"
            :visible.sync="mainTableCluVisible" >
            <el-table height="330px"
                      tooltip-effect="dark"
                      :data="mainTableList">
              <el-table-column
                label="序号"
                width="60"
                align="center"
                :formatter="snFormatter">
              </el-table-column>
              <el-table-column
                prop="columnName"
                align="center"
                label="属性名称"
                width="100">
              </el-table-column>
              <el-table-column
                prop="columnNameCn"
                align="center"
                label="中文名称"
                width="120">
              </el-table-column>
              <el-table-column
                prop="columnType"
                align="center"
                label="属性类型"
                width="100">
              </el-table-column>
              <el-table-column
                prop="columnLength"
                align="center"
                label="属性长度"
                width="100">
              </el-table-column>
              <el-table-column
                prop="remark"
                align="center"
                label="备注"
                width="120">
              </el-table-column>
            </el-table>
          </el-dialog>
        </div>
      </div>
    </div>
  </template>
<script>
  import request from '@/utils/request'
import qs from 'qs'
export default {
  name: 'zhgl',
  props: {
    modelId: String,
    modelName: String,
    modelVersion: String,
    versionShow: Boolean,
    required: true
  },
  data () {
    return {
      //sql关联模块
      sqlStr:'',
      sqlCluData:[],
      sqlCluName:[],
      LArr:[],
      ifSqlUp: false,
      RArr:[],
      attrArr:[],
      idList:[],
      // 当前点击行的下标
      cellIndex: null,
      tabName: '1',
      ifSel: true,
      // 分页
      pageSize: 10,
      pageIndex: 1,
      total: 0,
      ifSave: true,
      glRelationTableName: '',
      glTableCluName: '',
      addTableDrawer: false,
      addTableCluDrawer: false,
      glCluDrawer: false,
      mainTableCluVisible: false,
      selForm: {
        cluName: ''
      },
      columnTypeList: [],
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
      loading: false,
      isRelationTableId: true,
      isrelationColumnId: true,
      tableData: [],
      relationColumnId:'',
      tableDataClu: [],
      glTableCluFormD: {},
      dbName: '',
      searchGlName: '',
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
      ifSaveGl:true,
      // 关联弹框属性(下拉框)
      columnListA: [{
        id: '',
        label: ''
      }],
      // 导入表格属性
      showImport: false,
      ifUpload: true,
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
        relationColumnId: '',
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
        glRelationTableName:'',
        relationTableName: '',
        relationTableNameCn: '',
        relationTableAlias: '',
        relationDbName: '',
        relationDbNameCn: '',
        relationDbId: '',
        relationColumnId: '',
        relationColomnName: '',
        relationColomnNameCn: '',
        relationCondition: '',
        mainTableClu: '',
        dbId: ''
      },
      columnList: [],
      relationId: '',
      glCluObj: {columnName: ''},
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
    sqlStr(){
      for (let i = 0; i < this.sqlCluData.length;i++) {
        this.sqlCluData[i].cluType=0
      }
      let str=this.sqlStr
      this.LArr=[]
      this.RArr=[]
      this.attrArr=[]
      this.idList=[]
      this.sqlCluName=this.sqlCluData.concat([])
      let arr=str.split('select')
      arr=arr[1].split('from')
      str=arr[0]
      this.getIndex(str,"(",0,0)
      this.getIndex(str,")",0,1)
      if(this.LArr.length>0){
        for (let i=this.LArr.length-1;i>=0;i--) {
          let astr=str.substring(this.LArr[i],this.RArr[i]+1)
          str=str.replace(astr,'')
        }
      }
      arr=str.split(',')
      for (let i=0;i<arr.length;i++) {
        if(arr[i].includes('\n')){
          this.repalceN(arr[i])
        }
        arr[i]=arr[i].trim()
        let nerArr=arr[i].split(' ')
        if(nerArr.length>1){
          this.attrArr.push(nerArr[nerArr.length-1])
        }else{
          this.attrArr.push(nerArr[0])
        }
      }
      for (let i = 0; i < this.sqlCluData.length;i++) {
        this.sqlCluData[i].cluType=0
        for (let j = 0; j < this.attrArr.length; j++) {
          if(this.sqlCluData[i].cluName===this.attrArr[j]){
            this.sqlCluName[i].cluType=1
          }
        }
      }
      let newArrClu=[]
      let newArrClu2=[]
      for (let i = 0; i < this.sqlCluName.length;i++) {
        if(this.sqlCluName[i].cluType===1){
          newArrClu.push(this.sqlCluName[i])
          this.idList.push(this.sqlCluName[i].id)
        }else{
          newArrClu2.push(this.sqlCluName[i])
        }
      }
      this.sqlCluName=newArrClu.concat(newArrClu2)
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
    handleSizeChange (val) {
      this.pageSize = val
      this.getData()
    },
    handleCurrentChange1 (val) {
      this.pageIndex = val
      this.getData()
    },
    handleCurrentChange (row) {
      this.columnListA.id = row.id
      this.columnListA.label = row.columnName
      this.glCluObj.columnId = row.id
      this.change()
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
      request.post(localStorage.URL + '/file/upload/', formData, config).then((res) => {
        // 请求成功处理。
        this.toImport(res.data)
      }).catch(e => {
        // 请求失败处理。
        this.$message({
          message: e,
          type: 'error',
          center: true,
            duration: 0,
            showClose: true
        })
      })
    },
    // 获取属性类型下拉框数据
    getcolumnTypeList: function () {
      request.get('tech/type', qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        /* console.log(res) */
        this.columnTypeList = res.data
      }).catch(e => {
        // 请求失败处理。
      })
    },
    // 刷新关联弹框关联属性
    change (e) {
      this.$forceUpdate()
    },
    // 编辑操作
    editTable ({ $index }, row) {
      // 获取当前点击行下标
      this.cellIndex = $index
      this.getDbTableList(row, row.relationDbId)
      this.getTableClu(row.relationTableId)
      this.getRelationTableAlex(row,row.relationTableId)
    },
    // 保存操作
    saveTable ({ row }) {
      var tMetaModelTech = {
        relationCondition: row.relationCondition,
        isExternalConnection: row.isExternalConnection,
        relationColumnId: row.relationColumnId,
        relationDbId: row.relationDbId,
        relationId: row.relationId,
        relationTableId: row.relationTableId,
        relationTableAlias: row.relationTableAlias,
        techId: row.techId,
        modelVersion:this.modelVersion
      }
      request({
        headers: {
          'Content-Type': 'application/json'
        },
        method: 'put',
        url: '/relation?modelId='+ this.modelId,
        data: tMetaModelTech
      }).then((res) => {
        // 请求成功处理。
        this.getData()
        this.$message({
          message: res.msg,
          type: 'success',
          center: true
        })
      }).catch(e => {
        // 请求失败处理。
      })
      this.cellIndex = null
    },
    // 删除操作
    deletTable: function ({ row }) {
      this.$confirm("是否删除此数据？", "提示", {
          confirmButtonText: "确认",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(() => {
            request({
        method: 'delete',
        url: '/tech/' + row.techId + '?tableName=' + this.modelName + '&columnName=' + row.columnName
      }).then((res) => {
        // 请求成功处理。
        this.getData()
        if(res.code===200||res.code==='200'){
        this.$message({
          message: res.msg,
          type: 'success',
          center: true
        })}
      }).catch(e => {
        // 请求失败处理。
        this.$message({
          message: e,
          type: 'error',
          center: true,
            duration: 0,
            showClose: true
        })
      })
          })
          .catch(() => {});

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
          center: true,
            duration: 0,
            showClose: true
        })
        return
      }
      if (flag) {
        this.$message({
          message: '属性名称命名不规范',
          type: 'error',
          center: true,
            duration: 0,
            showClose: true
        })
        return
      }
      let res = this.importData
      for (let i = 0; i < res.length; i++) {
        request.post(localStorage.URL + '/metamodelattribute/create', qs.stringify({
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
            center: true,
            duration: 0,
            showClose: true
          })
        })
      }
    },
    glColumn: function (row) {
      this.glTableCluForm.relationTableId=''
      this.glCluObj.columnId=''
      this.glCluObj = {}
      this.glCluDrawer = this.seeClu = true
      this.glTableCluFormD = row
      this.columnListA.id = row.relationColumnId===undefined?'':row.relationColumnId
      this.columnListA.label = row.relationColumnName===undefined?'':row.relationColumnName
      this.glTableCluForm.relationDbId = row.relationDbId===undefined?'':row.relationDbId
      if(row.relationDbId!==undefined){
        this.getDbTableList(1,row.relationDbId)
      }
      if(row.relationTableId!==undefined){
        this.glTableCluForm.relationTableId = row.relationTableId===undefined?'':row.relationTableId
        this.getTableClu(row.relationTableId,row.relationColumnId)
      }

      this.glTableCluForm.glRelationTableName = row.relationTableAlias===undefined?'':row.relationTableAlias
      this.glTableCluForm.isExternalConnection = row.isExternalConnection===undefined?'':0
      this.glTableCluForm.relationCondition = row.relationCondition===undefined?'':row.relationCondition
      this.tableDataClu = []
      this.relationId = row.relationId===undefined?'':row.relationId
      this.glTableCluName = '关联元模型属性' + row.columnName
      //this.getDbTableList()
    },
    // 获取已关联数据
    getRelationList: function (id) {
      this.relationModelList = this.relationDbList = this.relationTableList = []
      this.relationDbIdF = this.relationTableIdF = this.relationTableAlias = ''
      request.get(localStorage.URL + '/metamodelattribute/relationinfolist?modelId=' + id, qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        this.relationModelList = res.data
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
          center: true,
            duration: 0,
            showClose: true
        })
      })
    },
    // 根据已关联数据源获取已关联表
/*     getRelationTableList: function (type) {
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
    }, */
    // 获取已关联表别名与属性
    getRelationTableAlex: function (row, id) {
      if (id === '' || id === undefined) {
        row.relationColumnId = ''
        this.isrelationColumnId = true
      } else {
        request.get('/relation/getColumns?tableId=' + id, qs.stringify({
        })).then((res) => {
          // 请求成功处理。
          this.columnList = res.data
          this.isrelationColumnId = false
        }).catch(e => {
          // 请求失败处理。
        })
      }
    },
    // 获取公共数据
    getCommonList: function () {
      // 获取数据源
      request.get('/relation/getDbs', qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        this.dbList = res.data
      }).catch(e => {
        // 请求失败处理。
      })
    },
    // 根据数据源获取表列表
    getDbTableList: function (row, id) {
      if (id === '' || id === undefined) {
        row.relationTableId = ''
        this.isRelationTableId = true
      } else {
        this.glTableCluForm.relationTableId = ''
        request.get('/relation/getTables?dbId=' + id, qs.stringify({
        })).then((res) => {
          // 请求成功处理。
          this.tableList = res.data
          this.isRelationTableId = false
          this.getTableClu(this.glTableCluForm.relationTableId)
        }).catch(e => {
          // 请求失败处理。
        })
      }
    },
    getTableList: function (type) {
      this.tableList = []
      if (type === 0 || type === '0') {
        this.tableForm.relationTableId = ''
      }
      if (this.tableForm.relationDbId !== undefined) {
        request.get(localStorage.URL + '/dbtable/list?dbId=' + this.tableForm.relationDbId, qs.stringify({
        })).then((res) => {
          // 请求成功处理。
          this.tableList = res.data
        }).catch(e => {
          // 请求失败处理。
          this.$message({
            message: e,
            type: 'error',
            center: true,
            duration: 0,
            showClose: true
          })
        })
      }
    },
    // 删除表
    delTable: function () {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        request.post(localStorage.URL + '/metamodel/delete', qs.stringify({
          id: this.modelId
        })).then((res) => {
          // 请求成功处理。
          this.modelId = ''
          for (var a in this.tableForm) {
            this.tableForm[a] = ''
          }
          this.getTree()
          this.$message({
            message: res.msg,
            type: 'success',
            center: true
          })
        }).catch(e => {
          // 请求失败处理。
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
    // 获取表基本信息
    getTabData: function () {
      this.tableForm = {}
      request.get('/relation/getColumnInfoList?modelId=' + this.modelId, qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        res = res.data
        this.tableForm = res
        this.getTableList()
      }).catch(e => {
        // 请求失败处理。
        this.$message({
          message: e,
          type: 'error',
          center: true,
            duration: 0,
            showClose: true
        })
      })
    },
    // 获取表属性列表
    getData: function () {
      this.tableData = []
      this.loading = true
      request.get('/relation/list?modelId=' + this.modelId + '&modelVersion=' + this.modelVersion  + '&columnName=' + this.selForm.cluName + '&pageNum=' + this.pageIndex + '&pageSize=' + this.pageSize, qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        this.loading = false
        this.tableData = res.rows
        this.total = res.total
        this.ifUploada()
        /* console.log(this.tableData) */
        this.ifSel = false

      }).catch(e => {
        // 请求失败处理。
      })

    },
    ifUploada: function () {
      for(var i=0;i<this.tableData.length;i++) {
        /* console.log(this.tableData[i].relationDbId) */
        if(this.tableData[i].relationDbId===''||this.tableData[i].relationDbId===undefined) {
          this.ifUpload = false
        } else {
          this.ifUpload = true
        }
      }
    },
    // 取消操作
    cancel () {
      this.cellIndex = null
      this.getData()
    },
    // 删除表属性
    delTableClu: function (id) {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        request.post(localStorage.URL + '/metamodelattribute/delete', qs.stringify({
          id: id
        })).then((res) => {
          // 请求成功处理。
          this.getData()
          this.$message({
            message: res.msg,
            type: 'success',
            center: true
          })
        }).catch(e => {
          // 请求失败处理。
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
    // 获取关联表所有属性,
    getTableClu: function (id,cluId) {
      this.glCluObj = {}
      this.tableDataClu = []
      this.glCluObj.columnId = ''
      request.get('/relation/getColumnInfoList?tableId=' + id + '&columnName=' + this.searchGlName, qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        this.tableDataClu = res.data
        if(cluId!==''&&cluId!==undefined){
          this.glCluObj.columnId = cluId
        }
        this.checkAlias()
      }).catch(e => {
        // 请求失败处理。
        this.$message({
          message: e,
          type: 'error',
          center: true,
            duration: 0,
            showClose: true
        })
      })
    },
    // 判断别名是否存在,是否可修改
    checkAlias: function () {
      this.seeAlias = false
      this.glTableCluForm.glRelationTableName = ''
      var tableId = this.glTableCluForm.relationTableId
      var dbId = this.glTableCluForm.relationDbId
      if (tableId === this.tableForm.relationTableId && dbId === this.tableForm.relationDbId) {
        this.glTableCluForm.glRelationTableName = this.tableForm.relationTableAlias
        this.seeAlias = true
      } else {
        var tableSel = {}
        for (var i = 0; i < this.tableData.length; i++) {
          if (tableId === this.tableData[i].relationTableId && dbId === this.tableData[i].relationDbId) {
            tableSel = this.tableData[i]
          }
        }
        if (tableSel.relationTableName === undefined) {
          this.glTableCluForm.glRelationTableName = ''
        } else {
          this.glTableCluForm.glRelationTableName = tableSel.relationTableAlias
          if (tableSel.id !== this.glTableCluForm.id) {
            this.seeAlias = true
          }
        }
      }
    },
    // 保存关联表信息
    saveGlData: function () {
      this.saveGlTypee()
      console.log(this.glTableCluForm)
      request.put('/relation/relationEdit?relationTableAlias=' + this.glTableCluForm.glRelationTableName + '&isExternalConnection=' +
      this.glTableCluForm.isExternalConnection + '&relationColumnId='+ this.columnListA.id + '&relationDbId=' + this.glTableCluForm.relationDbId
      + '&relationCondition=' + this.glTableCluForm.relationCondition + '&relationId=' + this.relationId + '&relationTableId='+ this.glTableCluForm.relationTableId+'&modelId='+this.modelId+'&modelVersion='+this.modelVersion, qs.stringify({})).then((res) => {
        // 请求成功处理。
        this.glCluDrawer = false
        this.getData()
        if(res.code===200 || res.code==='200'){
        this.$message({
          message: res.msg,
          type: 'success',
          center: true
        })}
      }).catch(e => {
        // 请求失败处理。
        this.$message({
          message: e,
          type: 'error',
          center: true,
            duration: 0,
            showClose: true
        })
      })
    },
    //sql关联模块
    //获取表格属性数组
    getSqlClu(){
      this.sqlCluData=[]
      request.get('/relation/list?modelId=' + this.modelId + '&modelVersion=' + this.modelVersion, qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        res=res.rows
        for (let i = 0; i < res.length; i++) {
          this.sqlCluName.push({cluName:res[i].columnName,cluType:0,id:res[i].techId})
          this.sqlCluData.push({cluName:res[i].columnName,cluType:0,id:res[i].techId})
        }
        request.get('/relation/sql?modelId=' + this.modelId + '&modelVersion=' + this.modelVersion, qs.stringify({
        })).then((res) => {
          // 请求成功处理。
          this.sqlStr=res.data


        }).catch(e => {
          // 请求失败处理。
        })
      }).catch(e => {
        // 请求失败处理。
      })
    },
    //替换sql语句中换行符
    repalceN(str){
    str=str.replace('\n','')
    if(str.includes('\n')){
      this.repalceN(str)
    }
  },
    //获取sql语句中括号的索引
  getIndex(str,meta,nindex,type){
    let index=str.indexOf(meta,nindex+1)
    if(index!==-1){
      if(type===0){
        this.LArr.push(index)
      }else{
        this.RArr.push(index)
      }

      this.getIndex(str,meta,index,type)
    }
  },
//保存sql
    saveSql(){
      this.saveGlTypee()
      request.put('/relation/sqlEdit?modelId='+this.modelId+'&sqlContent='+this.sqlStr+'&columnIdList='+this.idList, qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        this.ifSqlUp = true
        this.$message({
          message: res.msg,
          type: 'success',
          center: true
        })
      }).catch(e => {
        // 请求失败处理。
      })
    },
    // 保存sql/字段时 保存关联类型
    saveGlTypee(){
      request.put('/relation/choose?modelId='+this.modelId+'&relationType='+this.tabName, qs.stringify({
      })).then((res) => {
        // 请求成功处理。
      }).catch(e => {
        // 请求失败处理。
      })
    },
    //保存关联类型
    saveGlType(){
      request.put('/relation/choose?modelId='+this.modelId+'&relationType='+this.tabName, qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        this.$message({
          message: res.msg,
          type: 'success',
          center: true
        })
      }).catch(e => {
        // 请求失败处理。
      })
    },
    //初始化relationType
    getRelationType(){
      this.tabName='1'
      request.post('/relation/relationtype', {
        modelId:this.modelId,
        modelVersion:this.modelVersion
      }).then((res) => {
        // 请求成功处理。
        if(res.msg!==undefined){
        this.tabName=res.msg+''}

      }).catch(e => {
        // 请求失败处理。
      })
    }

  },
  created () {
    this.getRelationType()
    this.getCommonList()
    if(this.modelVersion !== ''){
    this.getData()}
    this.getSqlClu()

  }
}
</script>
<style lang="stylus" rel="stylesheet/stylus" scoped>
    ul,li{list-style:none;}
    .cell >.el-button
      padding 5px 10px !important
    .zhgl
      width 100%
      height 100%
      .cell .el-input__inner
        height 30px
      .aa .el-input,.aa .el-form-item__content,.aa .el-input__inner
        width  3rem !important
      .el-dialog
        width 10rem
        height 10rem
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
        .tabBarArea:after{
            display: inline;
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
    #sqlSpace .el-textarea,#sqlSpace .el-textarea__inner{
      height 400px
    }

  </style>
