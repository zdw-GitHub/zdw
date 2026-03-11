<template>
  <div class="idConfig">
    <el-form inline :model="search" size="mini" label-width="120px">
      <el-form-item label="分类 : ">
        <el-select
          v-model="search.categoryName"
          clearable
        >
          <el-option
            v-for="(item, key) in categoryOptions"
            :key="key"
            :label="item.categoryNameCn"
            :value="item.categoryNameCn"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="中文表名 : ">
        <el-input v-model="search.tableNameCn"></el-input>
      </el-form-item>
      <el-form-item label="英文表名 : ">
        <el-input v-model="search.tableName"></el-input>
      </el-form-item>
      <el-form-item style="float: right">
        <el-button plain type="info" @click="getTableData">查询</el-button>
        <el-button plain type="" @click="reSetTable">重置</el-button>
        <el-button plain type="success" @click="addBoxShow">新增</el-button>
        <el-button plain type="danger" @click="delArr" :disabled="selTableData.length == 0">删除</el-button>
        <!--<el-button plain type="primary" @click="configDefaultShow">默认SQL配置</el-button>-->
      </el-form-item>
    </el-form>
    <div>
      <el-table
        v-loading="loading"
        :data="tableData"
        height="600px"
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="50" align="center"/>
        <el-table-column
          label="序号"
          width="80"
          align="center">
          <template slot-scope="scope">
            <span>{{(pageNum - 1) * pageSize + scope.$index + 1}}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="generateRuleName"
          label="规则名称"
          min-width="200"
          align="center">
        </el-table-column>
        <el-table-column
          prop="generateRule"
          label="规则内容"
          min-width="200"
          align="center">
        </el-table-column>
        <el-table-column
          prop="generateRuleExample"
          label="规则范例"
          min-width="200"
          align="center">
        </el-table-column>
        <el-table-column
            prop="regularExecution"
            label="是否定期执行"
            min-width="200"
            align="center">
          </el-table-column>
        <el-table-column
          prop="categoryName"
          label="分类"
          min-width="150"
          align="center">
        </el-table-column>
        <el-table-column
          prop="tableNameCn"
          label="中文表名"
          min-width="150"
          align="center">
        </el-table-column>
        <el-table-column
          prop="tableName"
          label="英文表名"
          min-width="200"
          align="center">
        </el-table-column>
        <el-table-column
          prop="columnNameCn"
          label="中文属性名"
          min-width="150"
          align="center">
        </el-table-column>
        <el-table-column
          prop="columnName"
          label="英文属性名"
          min-width="150"
          align="center">
        </el-table-column>
        <el-table-column
          label="属性使用规则"
          min-width="120"
          align="center">
          <template slot-scope="scope">
            <span v-if="scope.row.ruleUsed == 0">参数规则</span>
            <span v-if="scope.row.ruleUsed == 1">SQL转换规则</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="lastConversionTime"
          label="最近一次转换时间"
          width="200"
          align="center">
        </el-table-column>
        <el-table-column
          prop="lastConversionResult"
          label="最近一次转换结果"
          width="150"
          align="center">
          <template slot-scope="scope">
            <span class="conversionResult" v-if="scope.row.lastConversionResult == 0" style="background: #F56C6C">失败</span>
            <span class="conversionResult" v-if="scope.row.lastConversionResult == 1" style="background: #67C23A">成功</span>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          fixed="right"
          width="180"
          align="center">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              @click="getLogTableData(scope.row.tableId)"
              icon="el-icon-document"
            >日志</el-button>
            <el-button
              size="mini"
              type="text"
              @click="editBoxShow(scope.row.ruleId)"
              icon="el-icon-edit"
            >修改</el-button>
            <el-button
              size="mini"
              type="text"
              @click="delCommit(scope.row.ruleId)"
              icon="el-icon-delete"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination
        :total="total"
        :page.sync="pageNum"
        :limit.sync="pageSize"
        @pagination="getTableData"
      />
    </div>

    <el-dialog
      :title="(form.type == 'add' ? '新增' : '修改') + '属性转换规则'"
      :visible.sync="form.show"
      :close-on-click-modal="false"
      width="70vw">
      <div class="form">
        <el-row>
          <el-form ref="form" :model="form.data" label-width="150px" :rules="form.rules">
            <el-divider content-position="left">基础配置</el-divider>
            <el-col :span="24">
              <el-form-item label="规则名称 : " prop="generateRuleName">
                <el-input v-model="form.data.generateRuleName"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="规则内容 : " prop="generateRule">
                <el-input v-model="form.data.generateRule"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="规则范例 : " prop="generateRuleExample">
                <el-input v-model="form.data.generateRuleExample"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="是否定期执行 : " prop="regularExecution">
                <el-select  style="width: 100%"
                  @change="regularExecution"
                  v-model="form.data.regularExecution" placeholder="请选择">
                  <el-option
                    v-for="item in regularExecutionOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="分类 : " prop="categoryId">
                <el-select
                  style="width: 100%"
                  @change="formCategoryIdSel"
                  v-model="form.data.categoryId"
                >
                  <el-option
                    v-for="(item, key) in categoryOptions"
                    :key="key"
                    :label="item.categoryNameCn"
                    :value="item.id"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="属性使用规则 : " prop="ruleUsed">
                <el-radio-group v-model="form.data.ruleUsed">
                  <el-radio label="0">参数规则</el-radio>
                  <el-radio label="1">SQL转换规则</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="中文表名 : " prop="tableId">
                <el-select
                  style="width: 100%"
                  @change="formTableSel"
                  v-model="form.data.tableId"
                >
                  <el-option
                    v-for="(item, key) in formTableOptions"
                    :key="key"
                    :label="item.tableNameCn"
                    :value="item.id"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="英文表名 : " prop="tableId">
                <el-select
                  style="width: 100%"
                  @change="formTableSel"
                  v-model="form.data.tableId"
                >
                  <el-option
                    v-for="(item, key) in formTableOptions"
                    :key="key"
                    :label="item.tableName"
                    :value="item.id"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="中文属性名 : " prop="columnId">
                <el-select
                  style="width: 100%"
                  @change="formColumnSel"
                  v-model="form.data.columnId"
                >
                  <el-option
                    v-for="(item, key) in formColumnOptions"
                    :key="key"
                    :label="item.columnNameCn"
                    :value="item.id"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="英文属性名 : " prop="columnId">
                <el-select
                  style="width: 100%"
                  @change="formColumnSel"
                  v-model="form.data.columnId"
                >
                  <el-option
                    v-for="(item, key) in formColumnOptions"
                    :key="key"
                    :label="item.columnName"
                    :value="item.id"
                  ></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="指定目标表 : " prop="targetTable">
                <el-input v-model="form.data.targetTable"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24" v-if="form.data.ruleUsed == 0">
              <div style="height: 50px; width:100%">
                <div style="float: left; width: calc(100% - 66px)">
                  <el-divider content-position="left">参数规则</el-divider>
                </div>
                <el-button plain type="success" size="mini" style="margin: 11px 0; float: right" @click="addRuleShow">新增</el-button>
              </div>
              <el-table
                tooltip-effect="light"
                :data="form.data.ruleParamVoList"
                height="300px"
                style="width: 100%; margin-bottom: 22px">
                <el-table-column
                  type="index"
                  label="序号"
                  width="50"
                  align="center">
                </el-table-column>
                <el-table-column
                  prop="paramNameCn"
                  label="参数中文名"
                  width="180"
                  align="center">
                </el-table-column>
                <el-table-column
                  prop="paramName"
                  label="参数英文名"
                  width="250"
                  align="center">
                </el-table-column>
                <el-table-column
                  prop="paramTableNameCn"
                  label="参数来源表中文名"
                  width="180"
                  align="center">
                </el-table-column>
                <el-table-column
                  prop="paramTableName"
                  label="参数来源表英文名"
                  min-width="250"
                  align="center">
                </el-table-column>
                <el-table-column
                  prop="paramTableNameCn"
                  label="参数来源属性中文"
                  width="180"
                  align="center">
                </el-table-column>
                <el-table-column
                  prop="paramTableName"
                  label="参数来源属性英文"
                  width="250"
                  align="center">
                </el-table-column>
                <el-table-column
                  show-overflow-tooltip
                  prop="paramCondition"
                  label="参数来源条件"
                  width="180"
                  align="center">
                </el-table-column>
                <el-table-column
                  fixed="right"
                  label="操作"
                  width="120"
                  align="center">
                  <template slot-scope="scope">
                    <el-button
                      size="mini"
                      type="text"
                      @click="editRuleShow(scope.row, scope.$index)"
                      icon="el-icon-edit"
                    >修改</el-button>
                    <el-button
                      size="mini"
                      type="text"
                      @click="delRuleCommit(scope.$index)"
                      icon="el-icon-delete"
                    >删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
              <el-form-item prop="paramRuleSet">
                <template slot="label">
                  <el-tooltip class="item" effect="light" placement="top" content="示例 : $参数名称$">
                    <span style="color: #409EFF"><i class="el-icon-question"></i>  规则设置 : </span>
                  </el-tooltip>
                </template>
                <el-input v-model="form.data.paramRuleSet" placeholder="示例 : $参数名称$"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="24" v-if="form.data.ruleUsed == 1">
              <el-divider content-position="left">SQL转换规则</el-divider>
              <el-form-item prop="paramRuleSet">
                <template slot="label">
                  <el-tooltip class="item" effect="light" placement="top">
                    <div slot="content">
                      SQL规则 : 输入sql时，查询字段应满足别名”id”（元数据id）、” ruleData”（符合需要的规则的字段数据）。<br/>
                      例如有以下规则场景：<br/>
                      通信站基本信息的名称，需要符合规则：所属网络简称_站点名称，<br/>
                      如：杭州通信网_110kV前进变。则sql转换规则sql如下：<br/>
                      select s.id as id, concat(n.NAME_ABBREVIATION, '_', s.name) as ruleData<br/>
                      from SG_TCCON_TCSITE_B s<br/>
                      join SG_TCCON_TCGRID_B n on s.PAR_GRID = n.ID<br/>
                    </div>
                    <span style="color: #409EFF"><i class="el-icon-question"></i>  转换SQL : </span>
                  </el-tooltip>
                </template>
                <el-input
                  type="textarea"
                  :autosize="{minRows: 7}"
                  v-model="form.data.convertRuleSql"
                  placeholder="
SQL规则 : 输入sql时，查询字段应满足别名”id”（元数据id）、” ruleData”（符合需要的规则的字段数据）。
例如有以下规则场景：
通信站基本信息的名称，需要符合规则：所属网络简称_站点名称，
如：杭州通信网_110kV前进变。则sql转换规则sql如下：
select s.id as id, concat(n.NAME_ABBREVIATION, '_', s.name) as ruleData
from SG_TCCON_TCSITE_B s
join SG_TCCON_TCGRID_B n on s.PAR_GRID = n.ID
                "
                ></el-input>
              </el-form-item>
            </el-col>
          </el-form>
        </el-row>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">保存</el-button>
        <el-button @click="form.show = false">取消</el-button>
      </span>
    </el-dialog>
    <el-dialog
      :title="addRule.type == 'add' ? '新增配置规则' : '修改配置规则'"
      :visible.sync="addRule.show"
      :close-on-click-modal="false"
      width="30%">
      <div style="height: 50vh">
        <el-form ref="addRule" :model="addRule.data" label-width="150px" :rules="addRule.rules">
          <el-form-item label="参数中文名 : " prop="paramNameCn" >
            <el-input v-model="addRule.data.paramNameCn"></el-input>
          </el-form-item>
            <el-form-item label="参数英文名 : " prop="paramName">
              <el-input v-model="addRule.data.paramName"></el-input>
            </el-form-item>
            <el-form-item label="表名 : " prop="paramTableId">
              <el-select
                filterable
                style="width: 100%"
                @change="tableTableSel"
                v-model="addRule.data.paramTableId"
              >
                <el-option
                  v-for="(item, key) in tableTableOptions"
                  :key="key"
                  :label="item.tableNameCn + ' ' + item.tableName"
                  :value="item.id"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="属性名 : " prop="paramColumnId">
              <el-select
                style="width: 100%"
                @change="tableColumnSel"
                v-model="addRule.data.paramColumnId"
              >
                <el-option
                  v-for="(item, key) in tableColumnOptions"
                  :key="key"
                  :label="item.columnNameCn + ' ' + item.columnName"
                  :value="item.id"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="参数来源条件 : " prop="paramCondition">
              <el-input type="textarea" :autosize="{minRows: 7}" v-model="addRule.data.paramCondition"></el-input>
            </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addRuleCommit">保存</el-button>
        <el-button @click="addRule.show = false">关闭</el-button>
      </span>
    </el-dialog>
    <el-dialog
      title="查看日志"
      :visible.sync="seeLogBox.show"
      :close-on-click-modal="false"
      width="70%">
      <div style="height: 70vh">
        <el-form inline :model="seeLogBox.search" size="mini" label-width="120px">
          <el-form-item label="转换结果 : ">
            <el-select
              v-model="seeLogBox.search.conversionResult"
              clearable
            >
              <el-option label="失败" value="0"></el-option>
              <el-option label="成功" value="1"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="转换时间 : ">
            <el-date-picker
              v-model="seeLogBox.search.time"
              type="daterange"
              format="yyyy-MM-dd"
              value-format="yyyy-MM-dd"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期">
            </el-date-picker>
          </el-form-item>
          <el-form-item style="float: right">
            <el-button plain type="info" @click="getLogTableData(seeLogBox.search.tableId)">查询</el-button>
            <el-button plain type="" @click="reSetLogTable">重置</el-button>
          </el-form-item>
        </el-form>
        <div style="height: calc(100% - 100px)">
          <el-table
            v-loading="seeLogBox.loading"
            :data="seeLogBox.tableData"
            height="100%"
            style="width: 100%"
          >
            <el-table-column
              label="序号"
              width="80"
              align="center">
              <template slot-scope="scope">
                <span>{{(seeLogBox.pageNum - 1) * seeLogBox.pageSize + scope.$index + 1}}</span>
              </template>
            </el-table-column>
            <el-table-column
              prop="tableNameCn"
              label="中文表名"
              min-width="150"
              align="center">
            </el-table-column>
            <el-table-column
              prop="tableName"
              label="英文表名"
              min-width="200"
              align="center">
            </el-table-column>
            <el-table-column
              prop="columnNameCn"
              label="中文属性名"
              min-width="150"
              align="center">
            </el-table-column>
            <el-table-column
              prop="columnName"
              label="英文属性名"
              min-width="200"
              align="center">
            </el-table-column>
            <el-table-column
              label="属性使用规则"
              min-width="120"
              align="center">
              <template slot-scope="scope">
                <span v-if="scope.row.ruleUsed == 0">参数规则</span>
                <span v-if="scope.row.ruleUsed == 1">SQL转换规则</span>
              </template>
            </el-table-column>
            <el-table-column
              label="是否批量转换"
              min-width="120"
              align="center">
              <template slot-scope="scope">
                <span v-if="scope.row.isBatch == 0">否</span>
                <span v-if="scope.row.isBatch == 1">是</span>
              </template>
            </el-table-column>
            <el-table-column
              label="转换结果 "
              min-width="80"
              align="center">
              <template slot-scope="scope">
                <el-tooltip  effect="light" content="查看报错信息" placement="top">
                  <span
                    class="conversionResult el-icon-zoom-in"
                    v-if="scope.row.conversionResult == 0"
                    style="background: #F56C6C; cursor: pointer"
                    @click="errorBoxshow(scope.row.convertLog)"
                  >  失败</span>
                </el-tooltip>
                <span class="conversionResult" v-if="scope.row.conversionResult == 1" style="background: #67C23A">成功</span>
              </template>
            </el-table-column>
            <el-table-column
              prop="conversionTime"
              label="转换时间"
              min-width="200"
              align="center">
            </el-table-column>
          </el-table>
          <pagination
            :total="seeLogBox.total"
            :page.sync="seeLogBox.pageNum"
            :limit.sync="seeLogBox.pageSize"
            @pagination="getLogTableData(seeLogBox.search.tableId)"
          />
        </div>
      </div>
    </el-dialog>
    <el-dialog
      title="报错信息"
      :visible.sync="errorBox.show"
      :close-on-click-modal="false"
      width="40%">
      <div style="height: 70vh">
        {{errorBox.text}}
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import {
    getCategoryOptionsApi,
    getTableOptionsApi,
    getColumnOptionsApi,
    getTableDataApi,
    addAndEditCommitApi,
    getInfoByIdApi,
    delCommitApi,
    getLogTableDataApi
  } from '@/api/fieldConfig'
  export default {
    name: "index",
    watch: {
    },
    data () {
      return {
        regularExecutionOptions: [{
          value: '0',
          label: '否'
        }, {
          value: '1',
          label: '是'
        }],

        search: {
          categoryName: '',
          tableNameCn: '',
          tableName: '',
        },

        loading: false,
        tableData: [],
        pageNum: 1,
        pageSize: 10,
        total: 0,
        selTableData: [],

        form: {
          type: '',
          show: false,
          editId: '',
          data: {},
          rules: {
            categoryId: [{required: true, message: '此为必填项', trigger: 'change'}],
            ruleUsed: [{required: true, message: '此为必填项', trigger: 'change'}],
            tableId: [{required: true, message: '此为必填项', trigger: 'change'}],
            columnId: [{required: true, message: '此为必填项', trigger: 'change'}],
            generateRuleName: [{required: true, message: '此为必填项', trigger: 'change'}],
          },
        },
        addRule: {
          type: '',
          show: false,
          index: '',
          data: {
            // 参数名称
            paramName: '',
            // 参数中文名称
            paramNameCn: '',
            // 参数来源表id
            paramTableId: '',
            // 参数来源表名
            paramTableName: '',
            // 参数来源表中文名
            paramTableNameCn: '',
            // 参数来源字段id
            paramColumnId: '',
            // 参数来源字段名
            paramColumnName: '',
            // 参数来源字段中文名
            paramColumnNameCn: '',
            // 参数来源条件
            paramCondition: '',
          },
          rules: {
            paramName: [{required: true, message: '此为必填项', trigger: 'change'}],
            paramNameCn: [{required: true, message: '此为必填项', trigger: 'change'}],
            paramTableId: [{required: true, message: '此为必填项', trigger: 'change'}],
            paramColumnId: [{required: true, message: '此为必填项', trigger: 'change'}],
          },
        },
        categoryOptions: [],
        formTableOptions: [],
        formColumnOptions: [],
        tableTableOptions: [],
        tableColumnOptions: [],
        seeLogBox: {
          show: false,
          loading: false,
          search: {
            tableId: '',
            conversionResult: '',
            time: [],
          },
          tableData: [],
          pageNum: 1,
          pageSize: 10,
          total: 0,
        },
        errorBox: {
          show: false,
          text: '',
        },
      }
    },
    methods: {
      getCategoryOptions () {
        getCategoryOptionsApi({
          pageNum: 1,
          pageSize: 10000,
        }).then(res => {
          this.categoryOptions = res.rows
        })
      },
      getTableOptions (data, id) {
        getTableOptionsApi({
          categoryNameId: id,
          pageNum: 1,
          pageSize: 10000,
        }).then(res => {
          console.log(res)
          this[data] = res.rows
        })
      },
      getColumnOptions (data, id) {
        getColumnOptionsApi({
          modelId: id,
          pageNum: 1,
          pageSize: 10000,
        }).then(res => {
          this[data] = res.rows
        })
      },
      regularExecution (val) {
        this.form.data.regularExecution = val
      },
      formCategoryIdSel (val) {
        let data = this.categoryOptions.find(item => {
          return item.id == val
        })
        this.form.data.categoryName = data.categoryNameCn

        this.form.data.tableId = ''
        this.form.data.tableName = ''
        this.form.data.tableNameCn = ''

        this.form.data.columnId = ''
        this.form.data.columnName = ''
        this.form.data.columnNameCn = ''

        this.formTableOptions = []
        this.formColumnOptions = []
        this.getTableOptions('formTableOptions', val)
      },
      formTableSel (val) {
        let data = this.formTableOptions.find(item => {
          return item.id == val
        })
        this.form.data.tableName = data.tableName
        this.form.data.tableNameCn = data.tableNameCn

        this.form.data.columnId = ''
        this.form.data.columnName = ''
        this.form.data.columnNameCn = ''

        this.formColumnOptions = []
        this.getColumnOptions('formColumnOptions', val)
      },
      formColumnSel (val) {
        let data = this.formColumnOptions.find(item => {
          return item.id == val
        })
        this.form.data.columnName = data.columnName
        this.form.data.columnNameCn = data.columnNameCn
      },
      tableTableSel (val) {
        let data = this.tableTableOptions.find(item => {
          return item.id == val
        })
        this.addRule.data.paramTableName = data.tableName
        this.addRule.data.paramTableNameCn = data.tableNameCn

        this.addRule.data.paramColumnNameId = ''
        this.addRule.data.paramColumnName = ''
        this.addRule.data.paramColumnNameCn = ''

        this.tableColumnOptions = []
        this.getColumnOptions('tableColumnOptions', val)
      },
      tableColumnSel (val) {
        let data = this.tableColumnOptions.find(item => {
          return item.id == val
        })
        this.addRule.data.paramColumnName = data.columnName
        this.addRule.data.paramColumnNameCn = data.columnNameCn
      },
      handleSelectionChange (arr) {
        this.selTableData = arr
      },
      reSetTable () {
        this.search = {
          categoryName: '',
          tableNameCn: '',
          tableName: '',
        }
      },
      getTableData () {
        let params = {
          ...this.search,
          pageNum: this.pageNum,
          pageSize: this.pageSize,
        }
        this.loading = true
        getTableDataApi(params).then(res => {
          this.loading = false
          this.tableData = res.rows
          this.total = res.total
        })
      },
      addBoxShow () {
        this.form.type = 'add'
        this.form.show = true
        this.$nextTick(() => {
          this.form.data = {
            // 类型
            categoryId: '',
            // 类型名称
            categoryName: '',
            // 表id
            tableId: '',
            // 表名
            tableName: '',
            // 表中文名
            tableNameCn: '',
            // 属性id
            columnId: '',
            // 属性名
            columnName: '',
            // 属性中文名
            columnNameCn: '',
            // 配置规则-名称
            generateRuleName: '',
            // 配置规则-内容
            generateRule: '',
            // 配置规则-范例
            generateRuleExample: '',
            // 是否定期执行
            regularExecution: '',
            paramRuleSet: '',
            // 转换规则-sql
            convertRuleSql: '',
            // 转换规则-批量sql
            convertRuleBatchSql: '',
            // 属性使用规则。0：配置规则；1：转换规则
            ruleUsed: '0',
            ruleParamVoList: [],
          }
          this.$refs.form.resetFields()
        })
        this.formTableOptions = []
        this.formColumnOptions = []
        // this.tableTableOptions = []
        this.tableColumnOptions = []
      },
      editBoxShow (id) {
        getInfoByIdApi({ruleId: id}).then(res => {
          console.log(res)
          this.form.type = 'edit'
          this.form.show = true
          this.form.data = res.data
          this.getTableOptions('formTableOptions', this.form.data.categoryId)
          this.getColumnOptions('formColumnOptions', this.form.data.tableId)
        })
      },
      submitForm () {
        this.$refs.form.validate((valid) => {
          if (valid) {
            addAndEditCommitApi(this.form.data)
            this.$modal.msgSuccess("操作成功")
            this.getTableData()
            this.form.show = false
          } else {
            return false;
          }
        })
      },
      addRuleShow () {
        this.addRule.type = 'add'
        this.addRule.show = true
        this.$nextTick(() => {
          this.addRule.data = {
            paramName: '',
            paramNameCn: '',
            paramTableId: '',
            paramTableName: '',
            paramTableNameCn: '',
            paramColumnId: '',
            paramColumnName: '',
            paramColumnNameCn: '',
            paramCondition: '',
          }
          this.$refs.addRule.resetFields()
        })
        this.tableColumnOptions = []
      },
      addRuleCommit () {
        this.$refs.addRule.validate((valid) => {
          if (valid) {
            if (this.addRule.type == 'add') {
              this.$modal.msgSuccess("添加成功 请继续添加")
              this.form.data.ruleParamVoList.push(JSON.parse(JSON.stringify(this.addRule.data)))
              this.addRuleShow()
            } else {
              this.$modal.msgSuccess("修改成功成功")
              let arr = JSON.parse(JSON.stringify(this.form.data.ruleParamVoList))
              arr[this.addRule.index] = JSON.parse(JSON.stringify(this.addRule.data))
              this.form.data.ruleParamVoList = arr
              this.addRule.show = false
            }
          } else {
            return false;
          }
        })
      },
      editRuleShow (row, index) {
        this.addRule.type = 'edit'
        this.addRule.show = true
        this.addRule.index = index
        this.addRule.data = JSON.parse(JSON.stringify(row))
        this.getColumnOptions('tableColumnOptions', this.addRule.data.paramTableId)
      },
      delRuleCommit (index) {
        this.$confirm('删除数据后无法恢复 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.form.data.ruleParamVoList.splice(index, 1)
        }).catch(() => {});
      },
      delArr () {
        let arr = []
        for (let i = 0; i < this.selTableData.length; i++) {
          arr.push(this.selTableData[i].ruleId)
        }
        this.delCommit(arr)
      },
      delCommit (id) {
        this.$confirm('删除数据后无法恢复 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          delCommitApi(id).then(res => {
            this.$modal.msgSuccess("操作成功")
            this.getTableData()
          })
        }).catch(() => {});
      },
      getLogTableData (tableId) {
        this.seeLogBox.search.tableId = tableId
        let data = {
          pageNum: this.seeLogBox.pageNum,
          pageSize: this.seeLogBox.pageSize,
          tableId: this.seeLogBox.search.tableId,
          conversionResult: this.seeLogBox.search.conversionResult,
          startTIme: this.seeLogBox.search.time != null && this.seeLogBox.search.time[0] ? this.seeLogBox.search.time[0] + ' 00:00:00' : '',
          endTime: this.seeLogBox.search.time != null && this.seeLogBox.search.time[1] ? this.seeLogBox.search.time[1] + ' 23:59:50' : '',
        }
        this.seeLogBox.loading = true
        getLogTableDataApi(data).then(res => {
          this.seeLogBox.loading = false
          this.seeLogBox.show = true
          this.seeLogBox.tableData = res.rows
          this.seeLogBox.total = res.total
        })
      },
      reSetLogTable () {
        this.seeLogBox.search.conversionResult = ''
        this.seeLogBox.search.time = []
      },
      errorBoxshow (text) {
        this.errorBox = {
          show: true,
          text: text
        }
      }
    },
    mounted () {
      this.getCategoryOptions()
      this.getTableOptions('tableTableOptions')
      this.getTableData()
    }
  }
</script>

<style lang="scss" scoped>
  .idConfig{
    width: 100%;
    height: 100%;
    padding: 10px;
    .form{
      width: 100%;
      height: 70vh;
      overflow-y: auto;
      overflow-x: hidden;
      .el-divider{
        background: #409EFF!important;
        .el-divider__text{
          font-weight: bold!important;
          color: #409EFF;
        }
      }
    }
    .conversionResult{
      padding: 5px;
      border-radius: 5px;
      font-weight: bold;
      color: #fff;
    }
  }
</style>