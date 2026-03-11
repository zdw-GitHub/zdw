<template>
  <div class="zspz">
    <div class="infoBox">
      <p class="title">
        基本信息
      </p>
      <div class="con">
        <el-row>
          <el-col :span="2">
            <p class="label">名称 : </p>
          </el-col>
          <el-col :span="4">
            <p class="value">{{essentialInfo.tableName}}</p>
          </el-col>
          <el-col :span="2">
            <p class="label">中文名称 : </p>
          </el-col>
          <el-col :span="4">
            <p class="value">{{essentialInfo.tableNameCn}}</p>
          </el-col>
          <el-col :span="2">
            <p class="label">所属类目 : </p>
          </el-col>
          <el-col :span="4">
            <p class="value">{{essentialInfo.categoryName}}</p>
          </el-col>
          <el-col :span="2">
            <p class="label">创建时间 : </p>
          </el-col>
          <el-col :span="4">
            <p class="value">{{essentialInfo.createDate}}</p>
          </el-col>
        </el-row>
      </div>
    </div>
    <div class="tabBox">
      <p class="title">
        详细信息
      </p>
      <div class="con">
        <el-table
          :data="tableData"
          stripe
          height="100%"
          tooltip-effect="dark"
        >
          <el-table-column
            label="序号"
            width="50"
            align="center"
            fixed="left"
            type="index"
          >
          </el-table-column>
          <el-table-column
            prop="columnName"
            align="center"
            label="属性名称"
            fixed="left"
            width="150"
          >
          </el-table-column>
          <el-table-column
            prop="columnNameCn"
            align="center"
            label="中文名"
            fixed="left"
            width="150"
          >
          </el-table-column>
          <el-table-column
            prop="columnType"
            align="center"
            label="属性类型"
            width="80"
          >
          </el-table-column>
          <el-table-column
            prop="columnLength"
            align="center"
            label="属性长度"
            width="80"
          >
          </el-table-column>
          <el-table-column
            prop="isVisible"
            align="center"
            label="是否显示"
            width="100"
            sortable
          >
            <template slot-scope="scope">
              <span class="whether" :style="{
                background: scope.row.isVisible == '1' ? '#67C23A' : '#F56C6C'
              }">
                {{translation(scope.row.isVisible)}}
              </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="sortNum"
            align="center"
            label="显示顺序"
            width="100"
            sortable
          >
          </el-table-column>
          <el-table-column
            prop="sortable"
            align="center"
            label="是否支持排序"
            width="140"
            sortable
          >
            <template slot-scope="scope">
              <span class="whether" :style="{
                background: scope.row.sortable == '1' ? '#67C23A' : '#F56C6C'
              }">
                {{translation(scope.row.sortable)}}
              </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="isMergeRows"
            align="center"
            label="是否合并行"
            width="120"
            sortable
          >
            <template slot-scope="scope">
              <span class="whether" :style="{
                background: scope.row.isMergeRows == '1' ? '#67C23A' : '#F56C6C'
              }">
                {{translation(scope.row.isMergeRows)}}
              </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="isQuery"
            align="center"
            label="是否查询"
            width="100"
            sortable
          >
            <template slot-scope="scope">
              <span class="whether" :style="{
                background: scope.row.isQuery == '1' ? '#67C23A' : '#F56C6C'
              }">
                {{translation(scope.row.isQuery)}}
              </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="relateSearchCondition"
            align="center"
            label="关联查询条件"
            width="130"
            sortable
          >
          </el-table-column>
          <el-table-column
            prop="relateSearchAttribute"
            align="center"
            label="关联查询属性"
            width="130"
            sortable
          >
          </el-table-column>
          <el-table-column
            prop="relateSearchName"
            align="center"
            label="关联查询命名"
            width="130"
            sortable
          >
          </el-table-column>
          <el-table-column
            prop="isCustomQuery"
            align="center"
            label="是否自定义查询"
            width="150"
            sortable
          >
            <template slot-scope="scope">
              <span class="whether" :style="{
                background: scope.row.isCustomQuery == '1' ? '#67C23A' : '#F56C6C'
              }">
                {{translation(scope.row.isCustomQuery)}}
              </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="isAdvancedQuery"
            align="center"
            label="是否高级查询"
            width="130"
            sortable
          >
            <template slot-scope="scope">
              <span class="whether" :style="{
                background: scope.row.isAdvancedQuery == '1' ? '#67C23A' : '#F56C6C'
              }">
                {{translation(scope.row.isAdvancedQuery)}}
              </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="isAdvancedQueryIndex"
            align="center"
            label="是否支持高级查询索引"
            width="190"
            sortable
          >
            <template slot-scope="scope">
              <span class="whether" :style="{
                background: scope.row.isAdvancedQueryIndex == '1' ? '#67C23A' : '#F56C6C'
              }">
                {{translation(scope.row.isAdvancedQueryIndex)}}
              </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="advancedQueryOrder"
            align="center"
            label="高级查询顺序"
            width="130"
            sortable
          >
          </el-table-column>
          <el-table-column
            prop="isJumpLink"
            align="center"
            label="是否跳转链接"
            width="130"
            sortable
          >
            <template slot-scope="scope">
              <span class="whether" :style="{
                background: scope.row.isJumpLink == '1' ? '#67C23A' : '#F56C6C'
              }">
                {{translation(scope.row.isJumpLink)}}
              </span>
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            width="200"
            align="center"
            fixed="right"
          >
            <template slot-scope="scope">
              <el-button @click="linkConfig(scope)" size="mini"
                         v-if="scope.row.isJumpLink=='1'">跳转配置</el-button>
              <el-button @click="editBoxShow(scope.row)" size="mini">编辑</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <el-dialog
      v-dialogDrag
      custom-class="curdBox"
      :close-on-click-modal="false"
      :modal="false"
      title="修改"
      :visible.sync="editBox.show"
      width="30%">
      <el-form ref="form" :model="editBox.data" label-width="200px" class="opaForm">
        <el-form-item label="是否显示 : ">
          <el-radio size="mini" v-model="editBox.data.isVisible" label="1">是</el-radio>
          <el-radio size="mini" v-model="editBox.data.isVisible" label="0">否</el-radio>
        </el-form-item>
        <el-form-item label="显示顺序 : ">
          <el-input-number size="mini" s v-model="editBox.data.sortNum" :min="0" :step="1"></el-input-number>
        </el-form-item>
        <el-form-item label="是否支持排序 : ">
          <el-radio size="mini" v-model="editBox.data.sortable" label="1">是</el-radio>
          <el-radio size="mini" v-model="editBox.data.sortable" label="0">否</el-radio>
        </el-form-item>
        <el-form-item label="是否合并行 : ">
          <el-radio size="mini" v-model="editBox.data.isMergeRows" label="1">是</el-radio>
          <el-radio size="mini" v-model="editBox.data.isMergeRows" label="0">否</el-radio>
        </el-form-item>
        <el-form-item label="是否查询 : ">
          <el-radio @change="mutual('isQuery', $event)" size="mini" v-model="editBox.data.isQuery" label="1">是</el-radio>
          <el-radio @change="mutual('isQuery', $event)" size="mini" v-model="editBox.data.isQuery" label="0">否</el-radio>
        </el-form-item>
        <div v-if="editBox.data.isQuery == 1" style="background: rgb(244,244,245)">
          <el-form-item label="关联查询条件 : ">
            <el-radio size="mini" v-model="editBox.data.relateSearchCondition" label="or">or</el-radio>
            <el-radio size="mini" v-model="editBox.data.relateSearchCondition" label="and">and</el-radio>
            <el-button size="mini" icon="el-icon-delete" @click="clearClumn"></el-button>
          </el-form-item>
          <el-form-item label="关联查询属性 : ">
            <el-select
              size="mini"
              v-model="editBox.data.relateSearchAttributeId"
              @change="selectedClumn"
            >
              <el-option
                v-for="item in tableData"
                :key="item.columnId"
                :label="item.columnNameCn + '  ' + item.columnName"
                :value="item.columnId"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="关联查询命名 : ">
            <el-input style="width: 193px" size="mini" v-model="editBox.data.relateSearchName"></el-input>
          </el-form-item>
        </div>
        <el-form-item label="是否自定义查询 : ">
          <el-radio @change="mutual('isCustomQuery', $event)" size="mini" v-model="editBox.data.isCustomQuery" label="1">是</el-radio>
          <el-radio @change="mutual('isCustomQuery', $event)" size="mini" v-model="editBox.data.isCustomQuery" label="0">否</el-radio>
        </el-form-item>
        <el-form-item label="是否高级查询 : ">
          <el-radio @change="mutual('isAdvancedQuery', $event)" size="mini" v-model="editBox.data.isAdvancedQuery" label="1">是</el-radio>
          <el-radio @change="mutual('isAdvancedQuery', $event)" size="mini" v-model="editBox.data.isAdvancedQuery" label="0">否</el-radio>
        </el-form-item>
        <el-form-item label="是否支持高级查询索引 : ">
          <el-radio size="mini" v-model="editBox.data.isAdvancedQueryIndex" label="1">是</el-radio>
          <el-radio size="mini" v-model="editBox.data.isAdvancedQueryIndex" label="0">否</el-radio>
        </el-form-item>
        <el-form-item label="高级查询顺序 : ">
          <el-input-number size="mini" v-model="editBox.data.advancedQueryOrder" :min="0" :step="1"></el-input-number>
        </el-form-item>
        <el-form-item label="是否跳转链接 : ">
          <el-radio size="mini" v-model="editBox.data.isJumpLink" label="1">是</el-radio>
          <el-radio size="mini" v-model="editBox.data.isJumpLink" label="0">否</el-radio>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editBox.show = false">取 消</el-button>
        <el-button type="primary" @click="editCommit">确 定</el-button>
      </span>
    </el-dialog>
    <!--跳转配置对话框-->
    <el-dialog
      title="跳转配置"
      width="30%"
      class="dialog"
      :close-on-click-modal="false"
      :visible.sync="isLinkConfig"
      style="margin-top: 2vh !important;"
      modal-append-to-body
      :append-to-body="true"
    >
      <div>
        <el-form ref="linkForm" :rules="rules" :model="linkForm" label-width="100px">
          <el-row>
            <el-col :span="24">
              <el-form-item label="跳转菜单" prop="menuId">
                <treeselect
                  v-model="linkForm.menuId"
                  :default-expand-level="2"
                  :options="menuOptions"
                  :normalizer="normalizer"
                  :show-count="true"
                  @select="selectMenu"
                  placeholder="请选择跳转菜单"
                  style="width: 400px"
                />
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="关联元模型" prop="text">
                <el-input ref="input" clearable v-model="linkForm.text" placeholder="请输入" style="width: 400px" @focus="showTreeSelector"></el-input>
                <el-tree
                  class="filter-tree"
                  :data="ReMetaOptions"
                  v-if="showSelector==true"
                  highlight-current
                  node-key="ReMetaOptions.id"
                  default-expand-all
                  :filter-node-method="filterMetaNode"
                  ref="metaTree" style="font-size: 14px;height: 30vh;overflow-y: auto">
            <span style="width: 100%;height:100%;display: block" class="custom-tree-node" slot-scope="{ node, data }">
              <span style="width: 100%;display: block;line-height: 25px" @click="handleNodeClick(data)">
                  <i :class="data.icon" :style="data.color"></i>{{data.label||data.categoryName||data.tableName}}
              </span>
          </span>
                </el-tree>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="关联属性" prop="linkReColumnId">
                <el-select clearable v-model="linkForm.linkReColumnId" filterable placeholder="请选择" style="width: 400px">
                  <el-option
                    v-for="item in ReColumnOptions"
                    :key="item.id"
                    :label="item.columnNameCn"
                    :value="item.id">
                    <span style="float: left">{{ item.columnNameCn }}</span>
                    <span style="float: right; color: #8492a6; font-size: 13px">{{ item.columnName }}</span>
                  </el-option>

                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button  type="primary" @click="saveConfigData()" >确 定</el-button>
        <el-button @click="colseConfig">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";
  import{tabData, editCommit, getInfo, getInfo1} from "@/api/ysjgl/sjcx";
  import {listMenu,sjyTree,column,editShow} from "@/api/ysjgl/zspz";
  export default {
    name: "zspz",
    components: { Treeselect },
    props: {
      versionObj: Object,
    },
    data () {
      return {
        essentialInfo: {
          tableName: '',
          tableNameCn: '',
          categoryName: '',
          createDate: '',
        },
        tableData: [],
        editBox: {
          show: false,
          data: {},
        },
      //  跳转配置
        isLinkConfig:false,
        linkForm:{
          menuId:'',
          text:'',
          linkReColumnId:'',
        },
        rules:{
          menuId: [
            { required: true, message: '请选择跳转菜单', trigger: 'change' },
          ],
          text: [
            { required: true, message: '请选择关联元模型', trigger: 'change' },
          ],
          linkReColumnId: [
            { required: true, message: '请选择关联属性', trigger: 'change' },
          ]
        },
        menuOptions:[],//跳转菜单选择
        ReMetaOptions:[
          {
            id: '0',
            label: '元模型',
            icon: 'el-icon-s-home',
            color: 'color: #FFC000',
            children: []
          }
        ],
        ReColumnOptions:[],//关联属性
        showSelector:false,
        columnConfigData:{},
        linkConfigForm:{},
      }
    },
    // id	编号
    // modelId	元模型编号
    // columnId	技术元数据属性编号
    // isQuery	是否查询1是，0否
    // isAdvancedQueryIndex	是否支持高级查询索引 1是，0否
    // isCustomQuery	是否自定义查询1是，0否
    // advancedQueryOrder	高级查询顺序
    // isAdvancedQuery	是否高级查询1是，0否
    // isVisible	是否显示1是，0否
    // sortNum	显示顺序
    // sortable	是否排序  1是，0否
    // modelVersion	版本号
    // createDate	创建时间
    // updateDate	修改时间
    // isJumpLink	是否跳转链接  1是，0否
    // menuId	菜单编号
    // menuPath	菜单路由
    // linkReMetaModelId	元模型编号
    // linkReMetaModel	元模型名称
    // linkReMetaModelCn	元模型名称中文
    // linkReColumnId	属性编号
    // linkReColumnName	属性名称
    // linkReColumnNameCn	属性名称中文
    // isMergeRows	是否合并行 1是，0否
    watch:{
      versionObj(newValue, oldValue) {
        this.getInfo()
        this.getTabData()
        this.getTreeselect();
        this.getSjyTree();
        },


      'linkForm.text':function(newval){
        this.$refs.metaTree.filter(newval)
      },
      'linkForm.linkReColumnId':function (newval,oldval) {
        if(newval){
          let arr=this.ReColumnOptions.find(item=>item.id==newval)
          this.linkConfigForm.linkReColumnId=arr.id
          this.linkConfigForm.linkReColumnName=arr.columnName
          this.linkConfigForm.linkReColumnNameCn=arr.columnNameCn
        }
      },
    },
    methods: {
      /** 元模型下拉菜单*/
      getSjyTree(params) {
        sjyTree({tableName:params}).then(res => {
          res = res.data
          this.ReMetaOptions[0].children = res
          for (var i = 0; i < this.ReMetaOptions[0].children.length; i++) {
            this.ReMetaOptions[0].children[i].children = res[i].tmetaModels
          }
        });

      },
      /** 查询菜单下拉树结构 */
      getTreeselect() {
        listMenu({menuName:'通信站维护与统计展示'}).then(response => {
          this.menuOptions = [];
          const menu = { menuId:1203 , menuName: '通信站维护与统计展示', children: [] };
          let data=response.rows.filter(item=>item.menuId!=1203 && item.menuType!='F')
          menu.children = this.handleTree(data, "menuId");
          this.menuOptions.push(menu);
        });
      },
      /** 转换菜单数据结构 */
      normalizer(node) {
        if (node.children && !node.children.length) {
          delete node.children;
        }
        return {
          id: node.menuId,
          label: node.menuName,
          children: node.children
        };
      },
      //点击跳转配置
      linkConfig(scope){
        this.columnConfigData=scope.row
        this.linkForm.menuId=this.columnConfigData.menuId
        this.getColumn(this.columnConfigData.linkReMetaModelId)
        this.linkForm.linkReColumnId=this.columnConfigData.linkReColumnId
        this.linkForm.text=this.columnConfigData.linkReMetaModel
        this.isLinkConfig=true
      },
      //  点击配置确定
      saveConfigData(){
        this.$refs['linkForm'].validate((valid) => {
          if (valid) {
            delete this.columnConfigData.params
            delete this.columnConfigData.updateDate
            delete this.columnConfigData.createDate
            let params={...this.columnConfigData,...this.linkConfigForm}
            editShow(params).then(res=>{
              // 请求成功处理。
              this.getTabData()
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
            this.isLinkConfig=false
            this.linkForm={
              menuId:'',
              text:'',
              linkReColumnId:'',
            }
            this.linkConfigForm={}
          } else {
            return false;
          }
        });

      },
      colseConfig(){
        this.isLinkConfig=false
        this.$refs['linkForm'].resetFields()
      },
      //  元数据模型过滤
      filterMetaNode(value, data) {
        if (!value) return true
        return (data.label||data.categoryName||data.tableName).indexOf(value) !== -1
      },
      selectMenu(value){
        if(value.menuType=='C'){
          this.linkConfigForm.menuId=value.menuId
          this.linkConfigForm.menuPath=value.path
          let arr=value.path.split('/')
          let tableName=arr[arr.length-1]
          if(arr[arr.length-2]=='common'){
            this.getSjyTree(tableName)
          }
        }else{
          this.linkConfigForm.menuId=value.menuId
          this.linkConfigForm.menuPath=null
        }
      },
      // 节点单击事件
      handleNodeClick(res) {
        if(!res.tmetaModels){
          this.linkForm.text=res.tableName
          this.linkConfigForm.linkReMetaModelId=res.id
          this.linkConfigForm.linkReMetaModel=res.tableName
          this.linkConfigForm.linkReMetaModelCn=res.tableNameCn
          this.getColumn(res.id)
          this.showSelector=false
        }
      },
      //获取属性列表
      getColumn(tableId){
        column({modelId:tableId}).then(res=>{
          this.ReColumnOptions=res.data
        })
      },
      showTreeSelector(){
        this.showSelector=true
      },



      /*之前的*/
      translation (str) {
        return str == '1' ? '是' : '否'
      },
      getInfo () {
        getInfo(this.versionObj.parentId).then(res1 => {
          this.essentialInfo.tableName = res1.data.tableName
          this.essentialInfo.tableNameCn = res1.data.tableNameCn
          this.essentialInfo.createDate = res1.data.createDate
          getInfo1(res1.data.categoryNameId).then(res2 => {
            this.essentialInfo.categoryName = res2.data.categoryName
          })
        })
      },
      getTabData: function () {
        let params = {
          modelId:this.versionObj.parentId,
          modelVersion: this.versionObj.label,
          pageNum:1,
          pageSize:999,
        }
        tabData(params).then((res) => {
          this.tableData = res.rows
        })
      },
      editBoxShow (row) {
        this.editBox = {
          show: true,
          data: JSON.parse(JSON.stringify(row))
        }
      },
      /* 查询互斥 */
      mutual(name, value) {
        if(value == '1') {
          this.editBox.data.isAdvancedQuery = '0'
          this.editBox.data.isCustomQuery = '0'
          this.editBox.data.isQuery = '0'
          this.editBox.data[name] = value
        }
      },
      selectedClumn (val) {
        let data = this.tableData.find(item => {
          return item.columnId == val
        })
        this.editBox.data.relateSearchAttributeId = val
        this.editBox.data.relateSearchAttribute = data.columnName
      },
      clearClumn () {
        this.$set(this.editBox.data, 'relateSearchCondition', '')
        this.$set(this.editBox.data, 'relateSearchAttributeId', '')
        this.$set(this.editBox.data, 'relateSearchAttribute', '')
        this.$set(this.editBox.data, 'relateSearchName', '')
      },
      editCommit () {
        let data = {
          ...this.editBox.data,
          modelVersion:this.versionObj.label,
          modelId:this.versionObj.parentId,
        }
        if (data.isQuery != 1) {
          data.relateSearchCondition = ''
          data.relateSearchAttributeId = ''
          data.relateSearchAttribute = ''
          data.relateSearchName = ''
        }
        editCommit(data).then(res => {
          this.editBox.show = false
          this.getTabData()
          this.$modal.msgSuccess("操作成功");
        })
      }
    },
    created () {
      console.log(this.versionObj)
      this.getInfo()
      this.getTabData()
      this.getTreeselect();
      this.getSjyTree();
    }
  }
</script>

<style lang="scss" scoped>
  .zspz{
    padding: 10px;
    p{
      margin: 0 !important;
    }
    width: 100%;
    height: 70vh;
    .infoBox{
      width: 100%;
      height: 100px;
    }
    .tabBox{
      width: 100%;
      height: calc(100% - 100px);
    }
    .infoBox,
    .tabBox{
      .title{
        width: 100%;
        height: 24px;
        line-height: 24px;
        font-weight: bold;
        border-bottom: 1px solid #000;
      }
      .con{
        width: 100%;
        height: calc(100% - 24px);
        padding: 10px;
      }
    }
    .infoBox .con{
      padding: 10px 30px;
      p{
        width: 100%;
        height: 56px;
        line-height: 36px;
        padding: 10px;
      }
      .label{
        text-align: right;
        font-weight: bold;
      }
    }
    .tabBox .whether {
      padding: 5px;
      border-radius: 3px;
      color: #fff;
    }
  }
</style>
