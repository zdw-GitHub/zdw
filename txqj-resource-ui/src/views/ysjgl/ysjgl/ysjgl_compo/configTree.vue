<template>
  <div id="configTree">
    <div class="butBox">
      <el-form style="width: 100%">
        <el-form-item label="树名称" style="width: 420px; margin: 0; display: inline-block">
          <el-input v-model="searchForm.name" size="mini"></el-input>
        </el-form-item>
        <el-form-item style="width: 500px; margin: 0; float: right; text-align: right">
          <el-button icon="el-icon-search" size="mini" @click="getTableData">搜索</el-button>
          <el-button icon="el-icon-plus" type="primary" plain size="mini" @click="reSetTreeForm()">新增</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="searchForm.name = ''">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="tableBox">
      <el-table
        ref="tableData"
        :data="tableData"
        stripe
        height="430px"
        v-loading="loading"
        tooltip-effect="dark"
        :header-cell-style="{background:'#F5F5F5'}"
        row-key="showId"
        :tree-props="{children: 'children'}"
        default-expand-all>
        <!--<el-table-column-->
          <!--label="序号"-->
          <!--width="150"-->
          <!--prop="showId"-->
        <!--&gt;</el-table-column>-->
        <el-table-column
          label="树名称"
          width="200"
          prop="treeName"
        ></el-table-column>
        <!--<el-table-column-->
          <!--label="是否启用"-->
          <!--align="center"-->
          <!--width="80"-->
        <!--&gt;-->
         <!--<template slot-scope="scope">-->
           <!--<span v-if="scope.row.state == 0">启用</span>-->
           <!--<span v-if="scope.row.state == 1">暂停</span>-->
         <!--</template>-->
        <!--</el-table-column>-->
        <el-table-column
          label="节点层级"
          align="center"
          width="100"
          prop="nodeLevel"
        ></el-table-column>
        <el-table-column
          label="节点名称"
          align="center"
          width="200"
          prop="name"
        ></el-table-column>
        <el-table-column
          label="数据查询SQL"
          align="center"
          width=""
          prop="nodeDataSql"
        ></el-table-column>
        <el-table-column
          label="说明"
          align="center"
          width="200"
          prop="remark"
        ></el-table-column>
        <el-table-column
          label="操作"
          align="center"
          width="140"
          fixed="right"
        >
          <template slot-scope="scope">
            <template v-if="!scope.row.treeId">
              <el-button
                size="mini"
                type="text"
                @click="reSetNodeForm(scope.row.id)"
              >操作节点</el-button>
              <el-dropdown>
                <span class="el-dropdown-link">
                  更多<i class="el-icon-arrow-down el-icon--right"></i>
                </span>
                <el-dropdown-menu slot="dropdown">
                  <!--<el-dropdown-item><el-button-->
                    <!--v-if="scope.row.state == 0"-->
                    <!--size="mini"-->
                    <!--type="text"-->
                    <!--@click="playAndSuspend(scope.row.id, 1)"-->
                    <!--icon="el-icon-video-play"-->
                  <!--&gt;启用</el-button></el-dropdown-item>-->
                  <!--<el-dropdown-item><el-button-->
                    <!--v-if="scope.row.state == 1"-->
                    <!--size="mini"-->
                    <!--type="text"-->
                    <!--@click="playAndSuspend(scope.row.id, 0)"-->
                    <!--icon="el-icon-video-pause"-->
                  <!--&gt;暂停</el-button></el-dropdown-item>-->
                  <el-dropdown-item><el-button
                    size="mini"
                    type="text"
                    icon="el-icon-edit"
                    @click="editTreeBoxShow(scope.row.id)"
                  >修改树</el-button></el-dropdown-item>
                  <el-dropdown-item><el-button
                    size="mini"
                    type="text"
                    icon="el-icon-delete"
                    @click="delTreeCommit(scope.row.id)"
                  >删除树</el-button></el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </template>
            <template v-if="scope.row.treeId">
              <el-dropdown-item><el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="delNodeCommit(scope.row.id)"
              >删除</el-button></el-dropdown-item>
            </template>
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="total>0"
        :total="total"
        :page.sync="pageIndex"
        :limit.sync="pageSize"
        @pagination="getTableData"
      />
    </div>
    <el-dialog
      :title="treeBox.type == 'add' ? '新增树' : '修改树'"
      :close-on-click-modal="false"
      :visible.sync="treeBox.show"
      width="30%">
      <div style="height: 20vh">
        <el-form ref="treeBox" :model="treeBox.form" label-width="100px" :rules="treeBox.rules">
          <el-form-item label="树名称 : " prop="name">
            <el-input v-model="treeBox.form.name"></el-input>
          </el-form-item>
          <el-form-item label="说明 : ">
            <el-input v-model="treeBox.form.remark"></el-input>
          </el-form-item>
          <!--<el-form-item label="是否启用 : ">-->
            <!--<el-switch-->
              <!--v-model="treeBox.form.state"-->
              <!--active-text="启用"-->
              <!--active-value="0"-->
              <!--inactive-value="1"-->
              <!--inactive-text="暂停">-->
            <!--</el-switch>-->
          <!--</el-form-item>-->
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer" v-if="treeBox.type == 'add'">
        <el-button type="primary" @click="treeFormCommit">确 定</el-button>
        <el-button @click="reSetTreeForm()">重 置</el-button>
      </span>
      <span slot="footer" class="dialog-footer" v-if="treeBox.type == 'edit'">
        <el-button type="primary" @click="treeFormCommit(false)">确 定</el-button>
        <el-button @click="treeBox.show = false">取 消</el-button>
      </span>
    </el-dialog>
    <el-dialog
      title="操作节点"
      :close-on-click-modal="false"
      :visible.sync="nodeBox.show"
      top="20%"
      width="70%">
      <el-form
        ref="nodeBox"
        :model="nodeBox.form"
        label-width="120px"
        :rules="nodeBox.rules"
        style="height: 50vh"
      >
        <el-row :gutter="20">
          <el-col :span="7" style="height: 100%">
            <el-form-item label="检索树 : ">
              <el-input v-model="nodeBox.treeSearch"></el-input>
            </el-form-item>
            <el-tree
              :props="{
                children: 'children',
                label: 'label'
              }"
              @node-click="nodeClick"
              :data="nodeBox.treeData"
              node-key="showId"
              default-expand-all
              highlight-current
              :expand-on-click-node="false"
              :filter-node-method="filterNode"
              ref="nodeBoxTree"
              style="height: calc(100% - 58px); overflow-y: auto"
            >
              <span class="custom-tree-node" slot-scope="{ node, data }">
                <span>{{ data.label }}</span>
                <span style="float: right;">
                  <el-button type="text" size="mini" @click="selectNode(data, node)" v-if="node.level > 1">修改</el-button>
                  <el-button type="text" size="mini" @click="selectParNode(data, node)">添加</el-button>
                </span>
              </span>
            </el-tree>
          </el-col>
          <el-col :span="17" style="height: 100%">
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="节点名称 : " prop="name">
                  <el-input v-model="nodeBox.form.name"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="节点说明 : " prop="remark">
                  <el-input v-model="nodeBox.form.remark"></el-input>
                </el-form-item>
              </el-col><el-col :span="12">
              <el-form-item label="上级节点 : ">
                <el-select v-model="nodeBox.form.parentName" disabled>
                  <el-option
                    v-for="item in tableArr"
                    :key="item.showId"
                    :label="item.name"
                    :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
              <el-col :span="12">
                <el-form-item label="节点层级 : " prop="nodeLevel">
                  <el-input disabled v-model="nodeBox.form.nodeLevel"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" style="height: calc(100% - 58px * 2)">
                <el-form-item label="数据查询SQL : " prop="nodeDataSql" style="height: 100%">
                  <el-button size="mini" @click="nodeBox.form.nodeDataSql = ''">清空</el-button>

                  <el-tooltip placement="bottom" effect="light">
                    <div slot="content">
                      <div class="sqlExplainInfo">
                        sql格式说明：<br/>
                        1、sql查询字段，需要有作为树节点数据的编码和名称字段（如：id、name），且字段必须使用别名“code”、"name"；<br/>
                        2、当前节点不是根节点时，sql查询字段还需要有一个字段，用于标识其所属的上级节点数据，且必须使用别名“parent_column”。<br/>
                    例如：当前节点数据为端口，数据由 SG_TCDEV_TCPORT_B 表中查询，查询字段需要有 par_card 字段来标识数据属于上级节点（板卡）的哪一条数据；
                      </div>
                    </div>
                    <p class="sqlExplain">sql格式说明<span class="el-icon-question"></span></p>
                  </el-tooltip>

                  <el-input
                    type="textarea"
                    style="height: calc(100% - 36px)"
                    placeholder="请输入内容"
                    v-model="nodeBox.form.nodeDataSql">
                  </el-input>
                </el-form-item>
              </el-col>
              <el-col :span="12" style="float: right">
                <el-form-item label="是否最底层节点 : ">
                  <el-switch
                    v-model="nodeBox.form.isLastNode"
                    active-text="是"
                    active-value="1"
                    inactive-text="否"
                    inactive-value="0"
                  ></el-switch>
                </el-form-item>
              </el-col>
              <el-col :span="12" style="float: right">
                <el-form-item label="是否多级搜索 : ">
                  <el-switch
                    v-model="nodeBox.form.isMultistageSearch"
                    active-text="是"
                    active-value="1"
                    inactive-text="否"
                    inactive-value="0"
                  ></el-switch>
                </el-form-item>
              </el-col>
              <el-col :span="12" style="float: right">
                <el-form-item label="对应字段 : ">
                  <el-select v-model="nodeBox.form.metaModelTechId" placeholder="请选择">
                    <el-option
                      v-for="item in this.nodeBox.tableOptions"
                      :key="item.id"
                      :label="item.columnName"
                      :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="nodeFormCommit">确 定</el-button>
        <el-button @click="reSetNodeForm(nodeBox.treeId)">重 置</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import {
    getTableDataApi,
    treeCommitApi,
    getTreeDataByIdApi,
    delTreeCommitApi,
    getTableTreeOptionsApi,
    nodeFormCommitApi,
    delNodeCommitApi,


  } from '@/api/ysjgl/configTree'
  export default {
    name: "configTree",
    props: {
      modelId: String
    },
    data () {
      var fieldConfig = (rule, value, callback) => {
        if (value == '') {
          callback(new Error('此为必填项'))
        }
        let code = this.nodeBox.fieldData.findIndex(item => {
          return item.nodeQueryColumnAlias == 'code'
        })
        let name = this.nodeBox.fieldData.findIndex(item => {
          return item.nodeQueryColumnAlias == 'name'
        })
        let parent_column = this.nodeBox.fieldData.findIndex(item => {
          return item.nodeQueryColumnAlias == 'parent_column'
        })
        if (code == -1 || name == -1) {
          callback(new Error('映射名未包含code与name'))
          if (this.nodeBox.form.nodeLevel != 1 && parent_column == -1) {
            callback(new Error('如果不是根节点 映射名必须包含parent_column'))
          }
        }
        callback()
      }
      return {
        searchForm: {
          name: ''
        },
        tableArr: [],
        tableData: [],
        loading: false,
        total: 0,
        pageIndex: 1,
        pageSize: 10,
        treeBox: {
          show: false,
          type: '',
          id: '',
          form: {
            name: '',
            remark: '',
            state: '0'
          },
          rules: {name: [{required: true, message: '此为必填项', trigger: 'change'}],}
        },
        nodeBox: {
          show: false,
          treeId: '',
          treeData: [],
          treeSearch: '',
          selectedShowId: '',
          tableOptions: [],
          form: {
            id: '',
            treeId: '',
            name: '',
            remark: '',
            nodeLevel: '',
            parentId: '',
            parentName: '',
            nodeDataSql: '',
            parentColumn: '',
            isLastNode: '0',
            isMultistageSearch: '0',
            metaModelId: '',
            metaModelTechId: '',
          },
          fieldData: [],
          rules: {
            name: [{required: true, message: '此为必填项', trigger: 'blur'}],
            nodeLevel: [{required: true, message: '请在左侧树选择节点', trigger: 'blur'}],
            nodeDataSql: [{validator: fieldConfig, trigger: 'blur'}],
            parentColumn: [{required: true, message: '此为必填项', trigger: 'blur'}],
          }
        }
      }
    },
    watch: {
      //解决操作栏固定后表格错位问题
      'tableData': {
        handler() {
          this.$nextTick(() => {
            this.$refs.tableData.doLayout();
          })
        },
        deep: true
      },
      modelId () {
        this.getTableData()
      },
      'nodeBox.treeSearch' (val) {
        this.$refs.nodeBoxTree.filter(val)
      },
      'nodeBox.form.nodeDataSql' (val) {
        this.nodeBox.fieldData = []
        // sql可能有大小写 全部转小写获取 第一个select和from的index
        let str = val.toLowerCase()
        let aIndex = str.indexOf('select')
        let zIndex = str.indexOf('from')
        if (aIndex != -1 && zIndex != -1) {
          // 起点index + 6 因为 "select" 有六个字母
          aIndex += 6
          // 截取select和from之间的字符串 并通过 , 分割
          val = val.substring(aIndex,zIndex)
          let selArr = val.split(',')
          // 清空右侧表格
          for (let i = 0; i < selArr.length; i++) {
            let nodeQueryColumn = ''
            let nodeQueryColumnAlias = ''
            // 判断字段是否有别名 有别名的话就取别名
            if (selArr[i].indexOf(' as ') != -1 || selArr[i].indexOf(' AS ') != -1) {
              nodeQueryColumn = selArr[i].split(' as ')[0]
              nodeQueryColumnAlias = selArr[i].split(' as ')[1]
            } else {
              nodeQueryColumn = selArr[i]
            }
            // 去除两端空格
            nodeQueryColumn = nodeQueryColumn.trim()
            nodeQueryColumnAlias = nodeQueryColumnAlias.trim()
            // 添加到右侧表格中
            if (selArr[i] != '') {
              let item = {
                nodeQueryColumn: nodeQueryColumn,
                nodeQueryColumnAlias: nodeQueryColumnAlias,
              }
              this.nodeBox.fieldData.push(item)
            }
          }
        }
      },
    },
    methods: {
      async getTableData () {
        if (!this.modelId) {
          this.total = 0
          this.pageIndex = 1
          this.pageSize = 10
          this.tableData = []
          return
        }
        this.loading = true
        await getTableDataApi({
          metaModeId: this.modelId,
          name: this.searchForm.name,
        }).then(res => {
          this.tableData = res.rows
          this.total = res.total
          for (let i = 0; i < this.tableData.length; i++) {
            this.tableData[i].treeInfo.treeName = this.tableData[i].treeInfo.name
            delete this.tableData[i].treeInfo.name
            if (this.tableData[i].treeNodeInfo) {
              this.tableData[i].treeInfo.children = [this.tableData[i].treeNodeInfo]
            }
            this.tableData[i] = this.tableData[i].treeInfo
          }
          this.setTableDataIndex('', this.tableData)
          this.loading = false
        })
      },
      async getTableTreeOptions () {
        getTableTreeOptionsApi({
          modelId: this.modelId,
          modelVersion: 'edit',
          pageSize: 999,
        }).then(res => {
          console.log(res.rows)
          this.nodeBox.tableOptions = res.rows
        })
      },
      setTableDataIndex (head, arr) {
        for (let i = 0; i < arr.length; i++) {
          if (arr[i] !== null && arr[i] !== undefined) {
            arr[i].showId = head + '' + (i + 1)
            arr[i].label = arr[i].name ? arr[i].name : arr[i].treeName
            this.tableArr.push({
              id: arr[i].id,
              name: arr[i].label
            })
            if (arr[i].children) [
              this.setTableDataIndex(arr[i].showId + '-', arr[i].children)
            ]
          }
        }
      },

      reSetTreeForm () {
        this.treeBox.show = true
        this.treeBox.form = {
          name: '',
          remark: '',
          state: '0'
        }
        this.treeBox.type = 'add'
        this.$nextTick(() => {
          this.$refs.treeBox.resetFields()
        })
      },
      editTreeBoxShow (id) {
        this.treeBox.id = id
        getTreeDataByIdApi(id).then(res => {
          this.treeBox.show = true
          this.treeBox.type = 'edit'
          this.$nextTick(() => {
            this.$refs.treeBox.resetFields()
          })
          this.treeBox.form = res.data
        })
      },
      treeFormCommit () {
        this.$refs.treeBox.validate((valid) => {
          if (valid) {
            let params = this.treeBox.form
            let type = ''
            if (this.treeBox.type == 'add') {
              type = 'post'
            } else {
              params.id = this.treeBox.id
              type = 'put'
            }
            params.metaModeId = this.modelId
            treeCommitApi(type, params).then(res => {
              this.$modal.msgSuccess("操作成功")
              this.treeBox.show = false
              this.getTableData()
            })
          } else {
            return false
          }
        })
      },
      delTreeCommit (id) {
        this.$confirm('删除数据后无法恢复 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          delTreeCommitApi(id).then(() => {
            this.$modal.msgSuccess("操作成功")
            this.getTableData()
          })
        }).catch(() => {});
      },
      playAndSuspend (id, val) {
        getTreeDataByIdApi(id).then(res => {
          let data = res.data
          data.state = val
          treeCommitApi('put', data).then(res => {
            this.$modal.msgSuccess("操作成功")
            this.getTableData()
          })
        })
      },

      nodeClick (data, node) {
        this.$refs.nodeBoxTree.setCurrentKey(this.nodeBox.selectedShowId)
      },
      filterNode(value, data) {
        if (!value) return true
        return data.label.indexOf(value) !== -1
      },
      async reSetNodeForm (treeId) {
        this.getTableTreeOptions().then(() => {
          this.nodeBox.show = true
          this.nodeBox.treeId = treeId
          this.nodeBox.treeData = [this.tableData.find(item => {
            return this.nodeBox.treeId == item.id
          })]
          this.nodeBox.form = {
            id: '',
            treeId: '',
            name: '',
            nodeLevel: '',
            parentId: '',
            parentName: '',
            nodeDataSql: '',
            parentColumn: '',
            isLastNode: '0',
            isMultistageSearch: '0',
            metaModelId: '',
            metaModelTechId: '',
          }
          this.$nextTick(() => {
            this.$refs.nodeBox.resetFields()
          })
        })
      },
      selectNode (data, node) {
        this.nodeBox.form = {
          id: data.id,
          remark: data.remark,
          treeId: data.treeId,
          name: data.label,
          nodeLevel: data.nodeLevel,
          parentId: node.parent.data.id,
          parentName: data.parentName,
          nodeDataSql: data.nodeDataSql,
          parentColumn: data.parentColumn,
          isLastNode: data.isLastNode,
          isMultistageSearch: data.isMultistageSearch,
          metaModelId: data.metaModelId,
          metaModelTechId: data.metaModelTechId,
        }
        this.nodeBox.selectedShowId = data.showId
      },
      selectParNode (data, node) {
        this.nodeBox.form = {
          treeId: '',
          name: '',
          remark: '',
          nodeLevel: '',
          parentId: '',
          parentName: '',
          nodeDataSql: '',
          parentColumn: '',
          metaModelId: '',
          metaModelTechId: '',
        }

        this.nodeBox.form.nodeLevel = node.level
        if (this.nodeBox.form.nodeLevel > 1) {
          this.nodeBox.form.parentId = data.id
          this.nodeBox.form.parentName = data.label
        }
        this.nodeBox.selectedShowId = data.showId
      },
      nodeFormCommit () {
        this.$refs.nodeBox.validate((valid) => {
          if (valid) {
            nodeFormCommitApi(this.packageNodeData()).then(res => {
              this.$modal.msgSuccess("操作成功")
              this.getTableData().then(() => {
                this.reSetNodeForm(this.nodeBox.treeId)
              })
            })
          } else {
            return false
          }
        })
      },
      packageNodeData () {
        for (let i = 0; i < this.nodeBox.fieldData.length; i++) {
          this.nodeBox.fieldData[i].treeId = this.nodeBox.treeId
          if (this.nodeBox.form.id) {
            this.nodeBox.fieldData[i].nodeId = this.nodeBox.form.id
          }
          if (this.nodeBox.form.nodeLevel > 1 && this.nodeBox.fieldData[i].nodeQueryColumnAlias == 'parent_column') {
            this.nodeBox.form.parentColumn = this.nodeBox.fieldData[i].nodeQueryColumn
          }
        }
        let data = {
          ...this.nodeBox.form,
          treeId: this.nodeBox.treeId,
          metaModelId: this.modelId,
          nodeColumnList: this.nodeBox.fieldData,
        }
        return data
      },
      delNodeCommit (id) {
        this.$confirm('删除数据后无法恢复 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          delNodeCommitApi(id).then(() =>{
            this.$modal.msgSuccess("操作成功")
            this.getTableData()
          })
        }).catch(() => {});
      }
    },
    mounted () {
      this.getTableData()
    }
  }
</script>

<style scoped lang="scss">
  #configTree{
    .butBox{
      width: 100%;
      height: 40px;
      line-height: 40px;
      padding: 0 10px;
    }
    .sqlExplain{
      float: right;
      display: inline-block;
      height: 40px;
      line-height: 40px;
      margin: 0!important;
      color: #409EFF;
    }
  }
</style>
<style lang="scss">
  #configTree{
    .el-row{
      height: 100% !important;
    }
    /*style="height: 100%;width: 100%; margin: 0*/
    .el-form-item{
      .el-form-item__content{
        width: calc(100% - 120px);
        height: 100%;
        .el-textarea__inner{
          height: 100%;
        }
      }
    }
    .custom-tree-node {
      flex: 1;
      display: flex;
      align-items: center;
      justify-content: space-between;
      font-size: 14px;
      padding-right: 8px;
    }
  }
  .sqlExplainInfo{
    line-height: 22px;
    width: 500px!important;
    font-size: 14px;
  }
</style>
