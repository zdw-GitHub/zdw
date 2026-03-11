<template>
  <div class="app-container ysjgl">
    <el-row :gutter="20">
      <!--数据源数据-->
      <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-input
            v-model="filterText"
            placeholder="元模型名称"
            clearable
            size="small"
            prefix-icon="el-icon-search"
            style="margin-bottom: 20px"
          />
        </div>
        <div class="head-container">
          <el-tree
            class="filter-tree"
            :data="data"
            highlight-current
            :default-expand-all="true"
            accordion
            node-key="id"
            :default-expanded-keys="[1]"
            :filter-node-method="filterNode"
            ref="tree" style="font-size: 14px;height:100%">
            <span style="width: 100%;height:100%;display: block" class="custom-tree-node" slot-scope="{ node, data }">
              <span style="width: 100%;display: block;line-height: 25px" @click="searchTable(data.id,data.tableName)">
                  <i :class="data.icon" :style="data.color"></i>{{data.label||data.categoryName||data.tableName}}
              </span>
          </span>
          </el-tree>
        </div>
      </el-col>
      <el-col :span="20" :xs="24">
        <el-row>
          <!--数据源基本信息-->
          <el-col :span="24" :xs="12">
            <el-row :gutter="16" style="border-bottom:1px solid #d3dce6;padding-bottom:5px">
              <el-col :span="16">
                <span>搜索结果</span>
              </el-col>
              <el-col :span="1.5" style="padding: 0 4px">
                <!-- <el-button
                  type="primary"
                  plain
                  icon="el-icon-upload2"
                  size="mini"
                  :disabled="ifSel"
                  @click="uploadTable()"
                  v-hasPermi="['mm:model:release']"
                >发布</el-button> -->
              </el-col>
              <el-col :span="1.5" style="padding: 0 4px">
                <el-button
                  type="primary"
                  plain
                  icon="el-icon-plus"
                  size="mini"
                  @click="addTable()"
                  v-if="versionShow"
                  v-hasPermi="['mm:model:add']"
                >新增</el-button>
              </el-col>
              <el-col :span="1.5" style="padding: 0 4px">
                <el-button
                  type="success"
                  plain
                  icon="el-icon-circle-check"
                  size="mini"
                  :disabled="ifSel"
                  v-if="versionShow"
                  @click="saveTabData('tableForm')"
                  v-hasPermi="['mm:model:keep']"
                >保存</el-button>
              </el-col>
              <el-col :span="1.5" style="padding: 0 4px">
                <el-button
                  type="primary"
                  plain
                  :disabled="ifSel"
                  icon="el-icon-document-copy"
                  size="mini"
                  v-if="versionShow"
                  @click="copyTabData()"
                  v-hasPermi="['mm:model:copy']"
                >复制</el-button>
              </el-col>
              <el-col :span="1.5" style="padding: 0 4px">
                <el-button
                  type="danger"
                  plain
                  icon="el-icon-delete"
                  size="mini"
                  :disabled="ifSel"
                  v-if="versionShow"
                  @click="delTable()"
                  v-hasPermi="['mm:model:remove']"
                >删除</el-button>
              </el-col>

            </el-row>
            <div style="display:flex">
              <el-form ref="tableForm" :rules="rules" :model="tableForm" >
                <div class="el_group" style="margin-top: 20px">
                  <el-form-item style="flex: 8" label="名称" prop="tableName">
                    <el-input disabled  v-model="tableForm.tableName"></el-input>
                  </el-form-item>
                  <el-form-item style="flex: 8" label="中文名称" prop="tableNameCn">
                    <el-input disabled v-model="tableForm.tableNameCn"></el-input>
                  </el-form-item>
                  <el-form-item style="flex: 8" label="所属类目" prop="categoryName">
                    <!--<el-input readonly v-model="tableForm.relationDbNameCn"></el-input>-->
                    <el-select disabled filterable  v-model="tableForm.categoryName" placeholder="请选择">
                      <el-option
                        v-for="item in categoryList"
                        :key="item.id"
                        :label="item.categoryNameCn"
                        :value="item.id">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </div>
                <div class="el_group">
                  <el-form-item style="flex: 8" label="标签" prop="labelName">
                    <el-select disabled filterable v-model="tableForm.labelName" @change="$forceUpdate()" placeholder="请选择" multiple >
                      <el-option
                        v-for="item in labelNameList"
                        :key="item.id"
                        :label="item.labelName"
                        :value="item.id"
                        :class="item.remark===1||item.remark==='1'?'red':''"
                      >
                      </el-option>
                    </el-select>
                  </el-form-item>
                  <el-form-item style="flex: 8" label="版本号">
                    <!--<el-input readonly v-model="tableForm.relationTableName"></el-input>-->
                    <el-select disabled filterable v-model="tableForm.modelVersion" placeholder="请选择" @change="ifVersion(tableForm.modelVersion)">
                      <el-option
                        v-for="item in tableList"
                        :key="item"
                        :label="item"
                        :value="item">
                      </el-option>
                    </el-select>
                  </el-form-item>
                  <el-form-item style="flex: 8" label="状态">
                    <el-select  disabled  readonly filterable v-model="tableForm.modelStatus" placeholder="请选择">
                      <el-option
                        v-for="item in tableStatusList"
                        :key="item.id"
                        :label="item.statusName"
                        :value="item.id">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </div>
                <div class="el_group">
                  <el-form-item style="flex: 5" label="审计模式" prop="auditMode">
                    <el-select  clearable filterable  placeholder="请选择" v-model="tableForm.auditMode" @change="ifAudit(tableForm.auditMode)">
                      <el-option label="无审计" key="0" value="0"></el-option>
                      <el-option label="手动审计" key="1" value="1"></el-option>
                      <el-option label="自动审计" key="2" value="2" disabled="true"></el-option>
                    </el-select>
                  </el-form-item>
                  <el-form-item style="flex: 8" label="接入模式" prop="accessMode">
                    <el-select multiple clearable filterable  placeholder="请选择" v-model="tableForm.accessMode" @change="$forceUpdate()" :disabled="ifAccess">
                      <el-option label="新增" key="1" value="1"></el-option>
                      <el-option label="更新" key="2" value="2"></el-option>
                      <el-option label="删除" key="3" value="3"></el-option>
                    </el-select>
                  </el-form-item>
                  <el-form-item style="flex: 8" label="关联方式" prop="dataRelationType">
                    <el-select placeholder="请选择" v-model="tableForm.dataRelationType">
                      <el-option label="字段关联" key="1" value="1"></el-option>
                      <el-option label="SQL关联" key="2" value="2"></el-option>
                      <el-option label="混合关联" key="3" value="3"></el-option>
                    </el-select>
                  </el-form-item>
                </div>
                <div class="el_group">
                  <el-form-item style="flex: 8" label="同步方式" prop="dataStorageMode">
                    <el-select  placeholder="请选择" v-model="tableForm.dataStorageMode">
                      <el-option label="增量同步" key="0" value="0"></el-option>
                      <el-option label="清除同步" key="1" value="1"></el-option>
                    </el-select>
                  </el-form-item>
                </div>
                <div class="el_group">
                  <el-form-item style="flex: 12" label="更新日志">
                    <el-input disabled  type="textarea" v-model="tableForm.updateLog" style="width:960px"></el-input>
                  </el-form-item>
                </div>
              </el-form>
            </div>
            <el-row :gutter="16" style="border-bottom:1px solid #d3dce6;padding-bottom:5px">
              <el-col :span="16">
                <div class="tabBarArea tit" style="z-index:98;font-size:16px">

                  <!-- div class="tabBarBtn" :class="[tabName=='Tab4'?'tabBarBtnActive':'']"
                      @click="tabName='Tab4'">血缘关系</div> -->
                  <div class="tabBarBtn" :class="[tabName=='Tab5'?'tabBarBtnActive':'']"
                       @click="tabName='Tab5'">数据关联配置</div>
                  <!-- <div class="tabBarBtn" :class="[tabName=='Tab6'?'tabBarBtnActive':'']"
                      @click="tabName='Tab6'">安全元数据</div> -->
                  <div class="tabBarBtn" :class="[tabName=='Tab3'?'tabBarBtnActive':'']"
                       @click="tabName='Tab3'">数据更新配置</div>
                  <!-- <div class="tabBarBtn" :class="[tabName=='Tab1'?'tabBarBtnActive':'']"
                      @click="tabName='Tab1'">表属性</div> -->
                </div>
              </el-col>
            </el-row>
            <jsysj v-if="tabName==='Tab1'" :modelId="modelId" :modelName="modelName"  :modelVersion="modelVersion" :versionShow="versionShow" ref="jsysj"></jsysj>
            <ywysj v-if="tabName==='Tab2'" :modelId="modelId" :modelName="modelName" :modelVersion="modelVersion" ref="ywysj" :versionShow="versionShow"></ywysj>
            <gxpz v-if="tabName==='Tab3'" :modelId="modelId" :modelName="modelName" :modelVersion="modelVersion" ref="gxpz"></gxpz>
            <zhgl v-if="tabName==='Tab5'" :modelId="modelId" :modelName="modelName" :modelVersion="modelVersion" :versionShow="versionShow"  ref="zhgl"></zhgl>
            <aqysj v-if="tabName==='Tab6'"></aqysj>
          </el-col>
          <!--数据源数据-->
        </el-row>
      </el-col>
    </el-row>

    <!-- 添加或修改用户配置对话框 -->
    <el-dialog
      title="发布版本"
      width="500px"
      class="doBox"
      :close-on-click-modal="false"
      :visible.sync="uploadTableDrawer"
      style="margin-top: 14vh !important;"
    >
      <el-form :model="uploadTableForm" :rules="uploadrules" ref="uploadTableForm" label-width="80px" style="text-align: center;margin-left:30px" size="small">
        <el-form-item label="版本号" prop="modelVersion">
          <el-input v-model="uploadTableForm.modelVersion"></el-input>
        </el-form-item>
        <el-form-item label="更新内容" style="margin-top:30px">
          <el-input type="textarea" v-model="uploadTableForm.remark"></el-input>
        </el-form-item>
        <el-form-item label="备注" style="margin-top:30px">
          <el-input type="textarea" v-model="uploadTableForm.updateLog"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button  type="primary" @click="saveAddTree()" >确 定</el-button>
        <el-button @click="uploadTableDrawer= false">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog
      title="复制版本"
      width="500px"
      :close-on-click-modal="false"
      :visible.sync="copyTabDataDrawer"
      style="margin-top: 14vh !important;"
    >
      <el-form :model="copyTableForm"  :rules="copyrules" ref="copyTableForm" label-width="80px" style="text-align: center;margin-left:30px">
        <el-form-item label="名称" prop="columnName">
          <el-input v-model="copyTableForm.columnName"></el-input>
        </el-form-item>
        <el-form-item label="中文名称" style="margin-top:30px">
          <el-input  v-model="copyTableForm.columnNameCn"></el-input>
        </el-form-item>
        <el-form-item label="版本号" prop="modelVersion" style="margin-top:30px">
          <el-select v-model="copyTableForm.modelVersion">
            <el-option
              v-for="item in versionList"
              :key="item"
              :label="item"
              :value="item">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button  type="primary" @click="saveCopyTree()" >确 定</el-button>
        <el-button @click="copyTabDataDrawer= false">取 消</el-button>
      </div>
    </el-dialog>
    <div class="doBox">
      <el-dialog width="500px" title="新增元模型"  :close-on-click-modal="false" :visible.sync="addTableDrawer" style="margin-top: 14vh !important;">
        <el-form :model="addTableForm" :rules="addrules" ref="addTableForm" label-width="80px" style="text-align: center;margin-left:30px">
          <el-form-item label="所属类目"  prop="categoryName">
            <el-select filterable  v-model="addTableForm.categoryName" placeholder="请选择">
              <el-option
                v-for="item in categoryList"
                :key="item.id"
                :label="item.categoryNameCn"
                :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="名称" prop="tableName" style="margin-top:30px">
            <el-input v-model="addTableForm.tableName" ></el-input>
          </el-form-item>
          <el-form-item label="中文名称" prop="tableNameCn" style="margin-top:30px">
            <el-input v-model="addTableForm.tableNameCn" ></el-input>
          </el-form-item>
          <el-form-item label="标签" style="margin-top:30px" prop="labelName" >
            <el-select filterable v-model="addTableForm.labelName" placeholder="请选择" multiple  @change="$forceUpdate()">
              <el-option
                v-for="item in labelNameList"
                :key="item.id"
                :label="item.labelName"
                :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <!--  <el-form-item label="版本号">
             <el-select filterable v-model="addTableForm.modelVersion" placeholder="请选择">
               <el-option v-for="item in categoryList" :key="item.id" :label="item.modelVersion" :value="item.id">
               </el-option>
             </el-select>
           </el-form-item>
           <el-form-item label="备注">
             <el-input type="textarea" v-model="addTableForm.updateLog"></el-input>
           </el-form-item> -->
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button  type="primary" @click="saveAddTable('addTableForm')" >确 定</el-button>
          <el-button @click="addTableDrawer= false">取 消</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>

  import "@riophae/vue-treeselect/dist/vue-treeselect.css";
  import qs from 'qs'
  import ywysj from '../sjzh/ysjgl_compo/ywysj.vue'
  import jsysj from '../sjzh/ysjgl_compo/jsysj.vue'
  import gxpz from '../sjzh/ysjgl_compo/gxpz.vue'
  import zhgl from '../sjzh/ysjgl_compo/zhgl.vue'
  import aqysj from '../sjzh/ysjgl_compo/aqysj.vue'
  import request from '@/utils/request'

  export default {
    name: 'Sjzh',
    data () {
      return {
        // 审计模式为无审计时 接入模式禁用
        ifAccess: false,
        tabName: 'Tab5',
        ifSel: true,
        glRelationTableName: '',
        glTableCluName: '',
        uploadTableDrawer: false,
        copyTabDataDrawer: false,
        addTableDrawer: false,
        addTableCluDrawer: false,
        glCluDrawer: false,
        mainTableCluVisible: false,
        selForm: {
          cluName: ''
        },
        dbList: [],
        tableList: [],
        tableStatusList: [
          {id: '0', statusName: '未发布'},
          {id: '1', statusName: '发布'}
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
          labelNameList: [{id: '', labelName: ''}],
          modelVersion: '',
          modelStatus: '',
          updateLog: '',
          auditMode: '',
          accessMode: [],
          dataRelationType: '',
          dataStorageMode: ''
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
          labelNameList: [{id: '', labelName: ''}],
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
          auditMode: [
            { required: true, message: '请选择接审计模式', trigger: 'change' }
          ],
          accessMode: [
            { required: true, message: '请选择接入模式', trigger: 'change' }
          ],
          dataRelationType: [
            { required: true, message: '请选择关联方式', trigger: 'change' }
          ],
          dataStorageMode: [
            { required: true, message: '请选择同步方式', trigger: 'change' }
          ],
          tableNameCn: [
            { required: true, message: '请输入元模型中文名称', trigger: 'blur' },
            // {
            //   validator: (rule, value, callback) => {
            //     if (/^[\a-\z\A-\Z0-9\u4e00-\u9fe5]+$/.test(value) === false) {
            //       callback(new Error('请输入中文名称'))
            //     } else {
            //       callback()
            //     }
            //   },
            //   trigger: 'blur'
            // }
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
    watch: {
      filterText (val) {
        this.$refs.tree.filter(val)
      }
    },
    methods: {
      // 审计模式判断
      ifAudit(value) {
        if(value=='0') {
          /* this.tableForm.accessMode = ['1','2','3'] */
          this.$set(this.tableForm,'accessMode',['1','2','3'])
          this.ifAccess = true
        } else {
          this.$set(this.tableForm,'accessMode',[])
          this.ifAccess = false
        }
      },
      // 下拉版本
      ifVersion(id) {

        this.modelVersion = id
        this.getTabData()
        if(id !== 'edit') {
          this.versionShow = false
          if (this.tabName === 'Tab1') {
            setTimeout(() => {
              this.$refs.jsysj.getData()
              this.$refs.jsysj.getTabData()
            }, 100)
          }
          if (this.tabName === 'Tab2') {
            setTimeout(() => {
              this.$refs.ywysj.getData()
            }, 100)
          }
          if (this.tabName === 'Tab3') {
            setTimeout(() => {
              this.$refs.gxpz.getData()
            }, 100)
          }
          if (this.tabName === 'Tab5') {
            setTimeout(() => {
              this.$refs.zhgl.getData()
            }, 200)
          }
        } else {
          this.versionShow = true
        }

        this.$forceUpdate()
      },
      filterNode (value, data) {
        if (!value) return true
        return (data.label||data.categoryName||data.tableName).indexOf(value) !== -1
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
        request.get(localStorage.URL + '/dbtablecolumn/' + row.id, qs.stringify({
        })).then((res) => {
          // 请求成功处理。
          this.glCluObj = res.data
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
        this.uploadTableDrawer = true
        this.uploadTableForm.modelVersion = ''
        if(this.tableForm.updateLog === '' || this.tableForm.updateLog == undefined) {
          this.uploadTableForm.updateLog = ''
        } else {
          this.uploadTableForm.updateLog = this.tableForm.updateLog}
        this.tabName='Tab5'
      },
      copyTabData: function () {
        this.copyTableForm.columnName = this.tableForm.tableName
        this.copyTableForm.columnNameCn = this.tableForm.tableNameCn
        this.getversionList()
        this.copyTabDataDrawer = true
      },
      // 添加表格属性
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
        this.$set(this.addTableForm, "labelName",  [])

        /*  this.addTableForm.labelName = '' */
        this.addTableDrawer = true
      },
      // 保存添加元模型
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
              // 请求成功处理。
              this.addTableDrawer = false
              this.getTree()
              if(res.code===200||res.code==='200'){
                this.$message({
                  message: res.msg,
                  type: 'success',
                  center: true
                })}
            }).catch(e => {
              // 请求失败处理。
              /*  this.$message({
                 message: e,
                 type: 'error',
                 center: true
               }) */
            })
          } else {
            return false
          }
        })
      },
      searchTable: function (id, name) {
        if (id !== undefined) {
          this.modelId = id
          /* this.modelName = name */
          this.modelVersion = 'edit'
          this.getlabelNameList()
          this.getTabData()
          this.$refs.tableForm.resetFields()
          if (this.tabName === 'Tab1') {
            setTimeout(() => {
              this.$refs.jsysj.getData()
              this.$refs.jsysj.getTabData()
            }, 100)
          } else if (this.tabName === 'Tab2') {
            setTimeout(() => {
              this.$refs.ywysj.getData()
              this.$refs.ywysj.getrulesData()
            }, 100)
          }
          if (this.tabName === 'Tab3') {
            setTimeout(() => {
              this.$refs.gxpz.getData()
            }, 100)
          }
          if (this.tabName === 'Tab5') {
            setTimeout(() => {
              this.$refs.zhgl.getData()
              this.$refs.zhgl.getSqlClu()
              this.$refs.zhgl.getRelationType()
            }, 200)
          }
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
          request.delete('mm/model/' + this.modelId + '?tableName=' + this.modelName, qs.stringify({
          })).then((res) => {
            // 请求成功处理。

            this.modelId = ''
            this.getTabData()
            this.getTree()
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
              duration:0,
              showClose:true
            })
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
      },
      getlabelNameList () {
        request.get('/label/selectNameId', qs.stringify({
        })).then((res) => {
          /* console.log(res) */
          // 请求成功处理。
          this.labelNameList = res.data
        }).catch(e => {
          // 请求失败处理。
        })
        request.get('/category/list', qs.stringify({
        })).then((res) => {
          // 请求成功处理。
          this.categoryList = res.rows
        }).catch(e => {
          // 请求失败处理。
        })
        request.get('/model/versions?modelId=' + this.modelId, qs.stringify({
        })).then((res) => {
          // 请求成功处理。
          this.tableList = res.data
        }).catch(e => {
          // 请求失败处理。
        })
      },
      getTree: function () {
        request.get('/model/tree', qs.stringify({
        })).then((res) => {
          // 请求成功处理。\
          /*  console.log(res) */
          res = res.data
          this.data[0].children = res
          for (var i = 0; i < this.data[0].children.length; i++) {
            this.data[0].children[i].children = res[i].tmetaModels
            /* console.log(this.data[0].children[i].children.tableName) */
          }

        }).catch(e => {
          // 请求失败处理。
        })
      },
// 保存修改元模型信息
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
              auditMode: this.tableForm.auditMode,
              accessMode: this.tableForm.accessMode.join(','),
              modelVersion: this.tableForm.modelVersion,
              dataRelationType: this.tableForm.dataRelationType,
              dataStorageMode: this.tableForm.dataStorageMode,
            }
            request({
              headers: {
                'Content-Type': 'application/json'
              },
              method: 'post',
              url: '/model/keep?modelId=' + this.modelId + '&oldTableName=' + this.modelName,
              data: params
            }).then((res) => {
              // 请求成功处理。
              this.getTree()

              this.getTabData()
              if(res.code===200||res.code==='200'){
                this.$message({
                  message: res.msg,
                  type: 'success',
                  center: true
                })}
            }).catch(e => {
            })
          } else {
            console.log('error submit!!');
            return false;
          }
        })

      },
// 获取表基本信息
      getTabData: function () {
        request.get('model/show' + '?modelId=' + this.modelId + '&modelVersion=' + this.modelVersion, qs.stringify({
        })).then((res) => {
          // 请求成功处理。
          /* console.log(res) */
          res = res.data
          this.tableForm = res
          this.tableForm.labelName = res.labelId
          this.tableForm.categoryName = res.categoryNameId
          this.tableForm.accessMode = res.accessMode.split(',')
          /* console.log(this.tableForm.accessMode) */
          this.modelVersion = this.tableForm.modelVersion
          this.modelName = this.tableForm.tableName
          if (res.modelStatus === undefined) {
            this.tableForm.modelStatus = '0'
          }
          /* this.getTableList()
          this.tableForm = res */
        }).catch(e => {
          // 请求失败处理。
        })
      },
// 获取表属性列表
      getData: function () {
        this.tableData = []
        request.get(localStorage.URL + '/metamodelattribute/list?modelId=' + this.modelId, qs.stringify({
        })).then((res) => {
          // 请求成功处理。
          this.tableData = res.rows
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
// 发布版本保存
      saveAddTree: function () {
        var ifAddVersion = true
        if (this.uploadTableForm.modelVersion === '' || this.uploadTableForm.modelVersion === undefined) {
          this.$message({
            message: '请输入版本号',
            type: 'error',
            center: true,
            duration:0,
            showClose:true
          })
          ifAddVersion = false
        }
        this.$refs.zhgl.getData()
        /*   if(this.$refs.zhgl.ifUpload === false&&this.$refs.zhgl.ifSqlUp === false) {
            ifAddVersion = false
            this.$message({
              message: '数据组合关联未填写！',
              type: 'error',
              center: true,
                    duration:0,
                    showClose:true
            })
          } */
        if (ifAddVersion === true) {
          request.post('/model/release?modelId='+ this.modelId +'&modelVersion=' + this.uploadTableForm.modelVersion+ '&remark=' + this.uploadTableForm.remark
            + '&updateLog=' + this.uploadTableForm.updateLog + '&tableName=' + this.modelName, qs.stringify({})).then((res) => {
            // 请求成功处理。
            this.uploadTableDrawer = false
            this.uploadCopy(this.uploadTableForm.modelVersion)
            this.getlabelNameList()
          }).catch(e => {
            // 请求失败处理。
          })
        }
      },
// 发布后复制
      uploadCopy: function (version) {
        request.post('/model/copy?modelId=' + this.modelId + '&modelVersion=' + version + '&tableName=' +
          this.tableForm.tableName + '&tableNameCn=' + this.tableForm.tableNameCn, qs.stringify({})).then((res) => {
          // 请求成功处理。
          this.uploadTableDrawer = false
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
            duration:0,
            showClose:true
          })
        })
      },
      // 复制版本号下拉框
      getversionList: function () {
        request.get('/model/versions?modelId=' + this.modelId, qs.stringify({
        })).then((res) => {
          this.versionList = res.data
        }).catch(e => {
          // 请求失败处理。
        })
      },
      // 复制版本保存
      saveCopyTree: function () {
        var ifAdd = true
        if (this.copyTableForm.modelVersion === '' || this.copyTableForm.modelVersion === undefined) {
          this.$message({
            message: '请输入版本号',
            type: 'error',
            center: true,
            duration:0,
            showClose: true
          })
          ifAdd = false
        }
        if (this.copyTableForm.columnNameCn !== '' && this.copyTableForm.columnNameCn !== undefined) {
          var re =/^[\a-\z\A-\Z0-9\u4e00-\u9fe5]+$/
          if (!re.test(this.copyTableForm.columnNameCn)) {
            this.$message({
              message: '请输入中文名称',
              type: 'error',
              center: true,
              duration:0,
              showClose:true
            })
            ifAdd = false
          }
        }
        if (ifAdd) {
          request.post('/model/copy?modelId=' + this.modelId + '&modelVersion=' + this.copyTableForm.modelVersion
            + '&tableName=' + this.copyTableForm.columnName + '&tableNameCn=' + this.copyTableForm.columnNameCn, qs.stringify({})).then((res) => {
            // 请求成功处理。
            this.uploadTableDrawer = false
            this.tabName='Tab5'
            /* this.$refs.jsysj.getData() */
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
              duration:0,
              showClose:true
            })
          })
          this.copyTabDataDrawer = false
        }
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
              duration:0,
              showClose:true
            })
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
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
      }
    },
    created () {

    },
    components: {
      ywysj,
      jsysj,
      gxpz,
      zhgl,
      aqysj
    },
    beforeMount () {
      this.getlabelNameList()
      this.getTree()
      this.ifSel = true
    }
  }
</script>
<style lang="stylus" rel="stylesheet/stylus" scoped>
  .el_group
    display flex
    width 80%
    margin auto
  .el-dialog
    width:30rem
  .el-input__inner
    width:240px !important
  .el_group .el-form-item
    margin-right:20px
  /deep/.el-form-item__label{
    width:100px
  }
  /deep/.el-form-item__content{
    margin-left:100px;
    width:240px
  }
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
    height:25px
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
  .importDatas .el-dialog
    width 18rem
    margin auto
  .el-select
    width 240px
  .openClu .el-dialog
    width 14rem
    height 9rem
    margin 2rem 11rem
  .red
    color: red
  .el-select__tags {
    margin-left:5px !important
    flex-wrap: nowrap !important
    overflow: hidden !important
  }
  .levelBox .el-dialog
    width 53rem
    margin-left:28%
  .levelBox .el-textarea__inner
    width:620px
    height: 400px !important
  .levelBox2 .el-dialog
    width 53rem
    margin-left:28%
  .addGl .el-dialog
    width:60rem
    height: auto
    margin-left:25%
    margin-top: 10vh !important
  .addGl .el-select
    width:180px
  .addGl .el-input
    width:180px
  .el-dialog
    width:30rem

</style>
