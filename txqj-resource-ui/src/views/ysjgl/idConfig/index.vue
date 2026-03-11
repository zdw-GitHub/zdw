<template>
  <div class="idConfig">
    <el-form inline :model="search" size="mini" label-width="120px">
      <el-form-item label="分类 : ">
        <el-select
          v-model="search.categoryName"
          clearable
        >
          <el-option
            v-for="(item, key) in categoryNameOptions"
            :key="key"
            :label="item.categoryNameCn"
            :value="item.categoryNameCn"
          ></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="中文表名 : ">
        <el-input v-model="search.modelNameCn"></el-input>
      </el-form-item>
      <el-form-item label="英文表名 : ">
        <el-input v-model="search.modelName"></el-input>
      </el-form-item>
      <el-form-item style="float: right">
        <el-button plain type="info" @click="getTableData">查询</el-button>
        <el-button plain type="" @click="reSetTable">重置</el-button>
        <el-button plain type="success" @click="addBoxShow">新增</el-button>
        <el-button plain type="danger" @click="delArr" :disabled="selTableData.length == 0">删除</el-button>
        <el-button plain type="primary" @click="transition" :disabled="selTableData.length == 0">批量转换ID</el-button>
        <el-button plain type="primary" @click="configDefaultShow">默认SQL配置</el-button>
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
          prop="categoryName"
          label="分类"
          min-width="200"
          align="center">
        </el-table-column>
        <el-table-column
          prop="modelNameCn"
          label="中文表名"
          min-width="300"
          align="center">
        </el-table-column>
        <el-table-column
          prop="modelName"
          label="英文表名"
          min-width="300"
          align="center">
        </el-table-column>
        <el-table-column
          prop="idDemo"
          label="ID范例"
          min-width="200"
          align="center">
        </el-table-column>
        <el-table-column
          prop="idGenerationRule"
          label="生成规则"
          min-width="300"
          align="center">
        </el-table-column>
        <el-table-column
          prop="idGenerationRule"
          label="生成规则"
          min-width="300"
          align="center">
        </el-table-column>
        <el-table-column
          prop="lastConversionTime"
          label="最近一次转换时间"
          width="150"
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
              @click="getLogTableData(scope.row.modelName)"
              icon="el-icon-document"
            >日志</el-button>
            <el-button
              size="mini"
              type="text"
              @click="editBoxShow(scope.row.id)"
              icon="el-icon-edit"
            >修改</el-button>
            <el-button
              size="mini"
              type="text"
              @click="delCommit(scope.row.id)"
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
      :title="form.title"
      :visible.sync="form.show"
      :close-on-click-modal="false"
      width="50%">
      <div class="form">
        <el-form ref="form1" :model="form.data" label-width="150px" :rules="form.rules">
          <template v-if="!form.isDefault">
            <el-divider content-position="left">基础配置</el-divider>
            <el-form-item label="分类 : " prop="categoryId">
              <el-select
                :disabled="form.type == 'edit'"
                style="width: 100%"
                v-model="form.data.categoryId"
                @change="selCategory"
              >
                <el-option
                  v-for="(item, key) in categoryNameOptions"
                  :key="key"
                  :label="item.categoryNameCn"
                  :value="item.id"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="表名 : " prop="modelNameCn">
              <el-select
                :disabled="form.type == 'edit'"
                style="width: 100%"
                v-model="form.data.modelNameCn"
                @change="sleModel"
              >
                <el-option
                  v-for="(item, key) in modelNameOptions"
                  :key="key"
                  :label="item.tableNameCn"
                  :value="item.tableNameCn"
                ></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="生成规则 : " prop="idGenerationRule">
              <el-input v-model="form.data.idGenerationRule"></el-input>
            </el-form-item>
            <el-form-item label="范例 : ">
              <el-input v-model="form.data.idDemo">
                <el-button slot="append" @click="ruleConfigChange">生成</el-button>
              </el-input>
            </el-form-item>
          </template>
          <el-divider content-position="left">规则配置</el-divider>
          <el-form-item class="labelTop" label-width="100%" label="前缀编码(属性名) : " prop="prefixEncodingAttributeName">
            <el-input v-model="form.data.prefixEncodingAttributeName"></el-input>
          </el-form-item>
          <el-form-item class="labelTop" label-width="100%" label="数据管理机构编码位数 : ">
            <el-input-number
              v-model="form.data.dataManagementOrgCodeDigit"

              :step="1"
              :min="0"
            ></el-input-number>
          </el-form-item>
          <el-form-item class="labelTop" label="数据管理机构编码默认值 : " prop="dataManagementOrgCodeDefault">
            <el-input v-model="form.data.dataManagementOrgCodeDefault"></el-input>
          </el-form-item>
          <el-form-item class="labelTop" label="序列号位数 : " prop="serial">
            <el-input-number v-model="form.data.serialNumberDigit" :step="1" :min="0"></el-input-number>
          </el-form-item>
          <template v-if="!form.isDefault">
            <el-form-item class="labelTop"></el-form-item>
            <el-form-item class="labelTop" label="是否含有数据字段 : " prop="isHaveDataManagementOrgCode">
              <el-radio-group v-model="form.data.isHaveDataManagementOrgCode" @change="isHaveDataManagementOrgCodeChange">
                <el-radio :label="'0'">否</el-radio>
                <el-radio :label="'1'">是</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item class="labelTop" label="数据字段 : ">
            <el-select
              style="width: 100%"
              v-model="form.data.dataManagementOrgCodeAttributeName"
            >
              <el-option
                v-for="(item, key) in dataManagementOrgCodeAttributeNameOptions"
                :key="key"
                :label="item.columnName + '  (' + item.columnNameCn + ')'"
                :value="item.columnName"
              ></el-option>
            </el-select>
          </el-form-item>
            <el-form-item class="labelTop"></el-form-item>
          </template>
          <el-divider content-position="left">SQL配置</el-divider>
          <el-form-item prop="idInterfaceSql">
            <template slot="label">
              <el-tooltip class="item" effect="light" placement="top" content="
              示例 : call get_max_id_serial( '$tableName$')">
                <span style="color: #409EFF"><i class="el-icon-question"></i>  ID接口SQL : </span>
              </el-tooltip>
              <br/>
              <el-button size="mini" type="primary" @click="form.data.idInterfaceSql = 'call get_max_id_serial( \'$tableName$\')'">自动填写</el-button>
            </template>
            <el-input
              type="textarea"
              :autosize="{minRows: 7}"
              v-model="form.data.idInterfaceSql">
            </el-input>
          </el-form-item>
          <el-form-item prop="batchUpdateSql">
            <template slot="label">
              <el-tooltip class="item" effect="light" placement="top" content="
              示例 : call batch_update_id('$tableName')">
                <span style="color: #409EFF"><i class="el-icon-question"></i>  ID接口SQL : </span>
              </el-tooltip>
              <br/>
              <el-button size="mini" type="primary" @click="form.data.batchUpdateSql = 'call batch_update_id(\'$tableName\')'">自动填写</el-button>
            </template>
            <el-input
              type="textarea"
              :autosize="{minRows: 7}"
              v-model="form.data.batchUpdateSql">
            </el-input>
          </el-form-item>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">保存</el-button>
        <el-button @click="form.show = false">取消</el-button>
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
            <el-button plain type="info" @click="getLogTableData(seeLogBox.search.modelName)">查询</el-button>
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
              prop="modelName"
              label="模型名称"
              min-swidth="200"
              align="center">
            </el-table-column>
            <el-table-column
              prop="modelNameCn"
              label="模型中文名称"
              min-swidth="200"
              align="center">
            </el-table-column>
            <el-table-column
              prop="conversionResult"
              label="转换结果"
              width="100"
              align="center">
              <template slot-scope="scope">
                <el-tooltip  effect="light" content="查看报错信息" placement="top">
                  <span
                  class="conversionResult el-icon-zoom-in"
                  v-if="scope.row.conversionResult == 0"
                  style="background: #F56C6C; cursor: pointer"
                  @click="errorBoxshow(scope.row.errorLog)"
                >  失败</span>
                </el-tooltip>
                <span class="conversionResult" v-if="scope.row.conversionResult == 1" style="background: #67C23A">成功</span>
              </template>
            </el-table-column>
            <el-table-column
              prop="conversionTime"
              label="转换时间"
              swidth="200"
              align="center">
            </el-table-column>
          </el-table>
          <pagination
            :total="seeLogBox.total"
            :page.sync="seeLogBox.pageNum"
            :limit.sync="seeLogBox.pageSize"
            @pagination="getLogTableData(seeLogBox.search.modelName)"
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

    <el-dialog
      title="执行结果"
      :visible.sync="transitionbox.show"
      :close-on-click-modal="false"
      width="70%">
      <div style="height: 70vh" v-loading="transitionbox.loading" element-loading-text="任务正在执行中，请耐心等待，也可关闭弹窗,执行结果也可在日志中查看。">
        <el-table
          v-if="!transitionbox.isOvertime"
          :data="transitionbox.tableData"
          height="100%"
          style="width: 100%"
        >
          <el-table-column
            label="序号"
            width="80"
            type="index"
            align="center">
          </el-table-column>
          <el-table-column
            prop="modelName"
            label="模型名称"
            min-swidth="200"
            align="center">
          </el-table-column>
          <el-table-column
            prop="modelNameCn"
            label="模型中文名称"
            min-swidth="200"
            align="center">
          </el-table-column>
          <el-table-column
            prop="code"
            label="转换结果"
            width="100"
            align="center">
            <template slot-scope="scope">
              <el-tooltip  effect="light" content="查看报错信息" placement="top">
                  <span
                    class="conversionResult el-icon-zoom-in"
                    v-if="scope.row.code == 0"
                    style="background: #F56C6C; cursor: pointer"
                    @click="errorBoxshow(scope.row.msg)"
                  >  失败</span>
              </el-tooltip>
              <span class="conversionResult" v-if="scope.row.code == 1" style="background: #67C23A">成功</span>
            </template>
          </el-table-column>
        </el-table>
        <div v-else style="width: 100%; height: 100%; line-height: 70vh; text-align: center">
          返回数据超时或任务执行失败，请在日志中查看。
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    getCategoryNameOptionsApi,
    getModelNameOptionsApi,
    getDataManagementOrgCodeAttributeNameOptionsApi,
    getTableDataApi,
    getInfoByIdApi,
    addCommitApi,
    editCommitApi,
    delCommitApi,
    getDefaultConfigApi,
    configDefaultCommitApi,
    getLogTableDataApi,
    transitionIdApi
  } from '@/api/idConfig'
  export default {
    name: "index",
    watch: {
    },
    data () {
      return {
        search: {
          categoryName: '',
          modelNameCn: '',
          modelName: '',
        },
        categoryNameOptions: [],
        modelNameOptions: [],
        dataManagementOrgCodeAttributeNameOptions: [],

        loading: false,
        tableData: [],
        pageNum: 1,
        pageSize: 10,
        total: 0,

        selTableData: [],

        form: {
          type: '',
          isDefault: false,
          title: '',
          show: false,
          editId: '',
          data: {
            // 模型类别编号
            categoryId: '',
            // 模型类别名称中文
            categoryName: '',
            // 模型编号
            modelId: '',
            // 模型名称英文
            modelName: '',
            // 模型名称中文  默认配置为 default
            modelNameCn: '',
            // ID范例 文本
            idDemo: '',
            // ID生成规则文本
            idGenerationRule: '',
            // 前缀编码默认
            prefixEncodingDefault: '',
            // 前缀编码属性名称
            prefixEncodingAttributeName: '',
            // 数据管理机构编码位数
            dataManagementOrgCodeDigit: '',
            // 数据管理机构编码默认
            dataManagementOrgCodeDefault: '',
            // 是否含有数据管理机构字段
            isHaveDataManagementOrgCode: '',
            // 数据管理机构字段 默认为TMS_SN
            dataManagementOrgCodeAttributeName: '',
            // number_digit	序列号位数
            serial: '',
            // ID接口SQL
            idInterfaceSql: '',
            // ID批量更新SQL
            batchUpdateSql: '',
          },
          rules: {
            // categoryId: [{required: true, message: '此为必填项', trigger: 'change'}],
            // modelNameCn: [{required: true, message: '此为必填项', trigger: 'change'}],
            // idGenerationRule: [{required: true, message: '此为必填项', trigger: 'change'}],
            // isHaveDataManagementOrgCode: [{required: true, message: '此为必填项', trigger: 'change'}],
            // prefixEncodingAttributeName: [{required: true, message: '此为必填项', trigger: 'change'}],
            // dataManagementOrgCodeDefault: [{required: true, message: '此为必填项', trigger: 'change'}],
            // serial: [{required: true, message: '此为必填项', trigger: 'change'}],
            // idInterfaceSql: [{required: true, message: '此为必填项', trigger: 'change'}],
            // batchUpdateSql: [{required: true, message: '此为必填项', trigger: 'change'}],
          },
        },

        seeLogBox: {
          show: false,
          loading: false,
          search: {
            modelName: '',
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

        transitionbox: {
          show: false,
          loading: false,
          tableData: [],
          isOvertime: false,
        }
      }
    },
    methods: {
      getCategoryNameOptions () {
        getCategoryNameOptionsApi().then(res => {
          this.categoryNameOptions = res.data
        })
      },
      selCategory (val) {
        this.modelNameOptions = [],
        this.dataManagementOrgCodeAttributeNameOptions = []

        this.form.data.categoryId = val
        this.form.data.categoryName = this.categoryNameOptions.find(item => {
          return item.id == val
        }). categoryNameCn

        this.form.data.modelId = ''
        this.form.data.modelName = ''
        this.form.data.modelNameCn = ''
        this.form.data.dataManagementOrgCodeAttributeName = ''

        getModelNameOptionsApi(this.form.data.categoryName).then(res => {
          this.modelNameOptions = res.data
        })
      },
      sleModel (val) {
        this.dataManagementOrgCodeAttributeNameOptions = []
        let data = this.modelNameOptions.find(item => {
          return item.tableNameCn == val
        })
        this.form.data.modelId = data.id
        this.form.data.modelName = data.tableName
        this.form.data.modelNameCn = data.tableNameCn
        this.form.data.dataManagementOrgCodeAttributeName = ''
        getDataManagementOrgCodeAttributeNameOptionsApi({
          modelId: this.form.data.modelId
        }).then(res => {
          this.dataManagementOrgCodeAttributeNameOptions = res.data
        })
      },
      isHaveDataManagementOrgCodeChange (val) {
        if (val == 0) this.form.data.dataManagementOrgCodeAttributeName = ''
      },
      ruleConfigChange () {
        let serial = ''
        for (let i = 0; i < parseInt(this.form.data.serial); i++) {
          serial = serial + 'X'
        }
        this.form.data.idDemo =
          this.form.data.prefixEncodingAttributeName +
          this.form.data.dataManagementOrgCodeDefault +
          serial
      },
      handleSelectionChange (arr) {
        this.selTableData = arr
      },
      reSetTable () {
        this.search = {
          categoryName: '',
          modelNameCn: '',
          modelName: '',
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
        this.form.isDefault = false
        this.form.title = '新增ID生成规则'
        this.form.show = true
        this.$nextTick(() => {
          this.form.data = {
            categoryId: '',
            categoryName: '',
            modelId: '',
            modelName: '',
            modelNameCn: '',
            idDemo: '',
            idGenerationRule: '',
            prefixEncodingDefault: '',
            prefixEncodingAttributeName: '',
            dataManagementOrgCodeDigit: '',
            dataManagementOrgCodeDefault: '',
            isHaveDataManagementOrgCode: '',
            dataManagementOrgCodeAttributeName: '',
            serial: '',
            idInterfaceSql: '',
            batchUpdateSql: '',
          }
          this.$refs.form1.resetFields()
          this.$refs.form2.resetFields()
          this.$refs.form3.resetFields()
        })
        this.modelNameOptions = []
        this.dataManagementOrgCodeAttributeNameOptions = []
      },
      editBoxShow (id) {
        getInfoByIdApi(id).then(res => {
          this.form.type = 'edit'
          this.form.isDefault = false
          this.form.title = '编辑ID生成规则'
          this.form.show = true
          this.form.data = {
            categoryId: '',
            categoryName: '',
            modelId: '',
            modelName: '',
            modelNameCn: '',
            idDemo: '',
            idGenerationRule: '',
            prefixEncodingDefault: '',
            prefixEncodingAttributeName: '',
            dataManagementOrgCodeDigit: '',
            dataManagementOrgCodeDefault: '',
            isHaveDataManagementOrgCode: '',
            dataManagementOrgCodeAttributeName: '',
            serial: '',
            idInterfaceSql: '',
            batchUpdateSql: '',
          }
          for (let key in res.data) {
            this.form.data[key] = res.data[key]
          }
          this.form.editId = res.data.id
          getModelNameOptionsApi(this.form.data.categoryName).then(res => {
            this.modelNameOptions = res.data
          })
          getDataManagementOrgCodeAttributeNameOptionsApi({
            modelId: this.form.data.modelId
          }).then(res => {
            this.dataManagementOrgCodeAttributeNameOptions = res.data
          })
        })
      },
      async configDefaultShow () {
        this.form.isDefault = true
        this.form.title = '配置默认生成规则'
        let data
        await getDefaultConfigApi('default').then(res => {
          data = res.data
        }).catch(e => {
          data = {
            modelName: 'default',
            dataManagementOrgCodeAttributeName: 'TMS_SN'
          }
        })
        for (let key in data) {
          this.form.data[key] = data[key]
        }
        this.form.show = true
      },
      async submitForm () {
        if (!this.form.isDefault) {
          let verify = true
          // await this.$refs.form1.validate((valid) => {verify = valid ? verify : false})
          // await this.$refs.form2.validate((valid) => {verify = valid ? verify : false})
          // await this.$refs.form3.validate((valid) => {verify = valid ? verify : false})
          if (verify) {
            if (this.form.type == 'add') {
              addCommitApi(this.form.data).then(res => {
                this.$modal.msgSuccess("操作成功")
                this.form.show = false
                this.getTableData()
              })
            } else if (this.form.type == 'edit') {
              let data = {
                ...this.form.data,
                id: this.form.editId
              }
              editCommitApi(data).then(res => {
                this.$modal.msgSuccess("操作成功")
                this.form.show = false
                this.getTableData()
              })
            }
          }
        } else {
          // if (this.form.data.params) delete this.form.data.params
          let verify = true
          // await this.$refs.form2.validate((valid) => {verify = valid ? verify : false})
          // await this.$refs.form3.validate((valid) => {verify = valid ? verify : false})
          if (verify) {
            configDefaultCommitApi(this.form.data).then(res => {
              this.$modal.msgSuccess("操作成功")
              this.form.show = false
            })
          }
        }
      },
      delArr () {
        let arr = []
        for (let i = 0; i < this.selTableData.length; i++) {
          arr.push(this.selTableData[i].id)
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
      transition () {
        let arr = []
        for (let i = 0; i < this.selTableData.length; i++) {
          arr.push(this.selTableData[i].modelName)
        }
        this.$confirm('是否批量转换ID?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.transitionbox ={
            show: true,
            loading: true,
            tableData: [],
            isOvertime: false,
          }
          transitionIdApi(arr).then(res => {
            this.transitionbox.loading = false
            this.transitionbox.tableData = res.data
            this.transitionbox.isOvertime = false
          }).catch(() => {
            this.transitionbox.loading = false
            this.transitionbox.tableData = []
            this.transitionbox.isOvertime = true
          })
        }).catch(() => {});
      },
      getLogTableData (modelName) {
        this.seeLogBox.search.modelName = modelName
        let data = {
          pageNum: this.seeLogBox.pageNum,
          pageSize: this.seeLogBox.pageSize,
          modelName: this.seeLogBox.search.modelName,
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
      this.getCategoryNameOptions()
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
      max-height: 70vh;
      overflow-y: auto;
      overflow-x: hidden;
      .el-divider{
        background: #409EFF!important;
        .el-divider__text{
          font-weight: bold!important;
          color: #409EFF;
        }
      }
      .el-form-item{
        width: 50%;
        display: inline-block;
      }
      .labelTop{
        width: 25%;
        display: inline-block;
        height: 100%;
        padding: 0 10px;
        ::v-deep.el-form-item__label{
          width: 100%!important;
          text-align: left!important;
          display: block;
          color: rgba(253, 249, 249, 0.85);
        }
        ::v-deep.el-form-item__content{
          width: 100% !important;
          display: block!important;
          margin: 0!important;
          margin-top: 36px!important;
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
