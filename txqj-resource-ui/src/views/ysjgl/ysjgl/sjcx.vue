<template>
  <div id="sjcx"  class="app-container">
    <el-row :gutter="20">
      <!--数据源数据-->
      <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-input
            v-model="dbName"
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
            node-key="id"
            :default-expanded-keys="idArr"
            :filter-node-method="filterNode"
            ref="tree" style="font-size: 14px;height: 80vh;overflow-y: auto">
            <span style="width: 100%;height:100%;display: block" class="custom-tree-node" slot-scope="{ node, data }">
              <span style="width: 100%;display: block;line-height: 25px" @click="handleNodeClick(data)">
                  <i :class="data.icon" :style="data.color"></i>{{data.label}}
              </span>
          </span>
          </el-tree>
          <!--<el-tree
            class="filter-tree"
            :data="data"
            highlight-current
            node-key="id"
            :default-expanded-keys="[0]"
            :filter-node-method="filterNode"
            ref="tree" style="font-size: 14px;height: 80vh;overflow-y: auto">
            <span style="width: 100%;height:100%;display: block" class="custom-tree-node" slot-scope="{ node, data }">
              <span style="width: 100%;display: block;line-height: 25px" @click="handleNodeClick(data)">
                  <i :class="data.icon" :style="data.color"></i>{{data.label}}
              </span>
          </span>
          </el-tree>-->
        </div>
      </el-col>
      <!--表格数据-->
      <el-col :span="20" :xs="24">
        <el-row :gutter="16" style="border-bottom:1px solid #d3dce6;padding-bottom:5px;margin-bottom:10px">
          <el-col :span="24">
            <span>表数据</span>
            <div style="display:inline-block;margin-left:20px">
              <el-radio-group v-model="radio" @change="ifMiddle">
                <el-radio label="1">正式库</el-radio>
                <el-radio label="2" v-if="showAudit">中间库</el-radio>
              </el-radio-group>
            </div>
            <div style="float:right;margin-right:10px">
              <!--<el-button :disabled="versionObj.id===''||versionObj.id===undefined" type="primary" plain icon="el-icon-magic-stick" size="mini" v-hasPermi="['dataaccess:datatransfer:view']" @click="refreshData">同步</el-button>-->
              <el-button :disabled="versionObj.id===''||versionObj.id===undefined" plain icon="el-icon-setting" size="mini" @click="openTool" v-hasPermi="['mm:model:isShow']">配置</el-button>
            </div>
          </el-col>
        </el-row>
        <el-form id="sjcxForm" :model="queryParams" style="width: 100%;" ref="queryForm" size="small" :inline="true" label-width="68px">
          <el-form-item v-for="(item,index) in searchClu"
                        :key="index"
                        :label="item.attributeNameCn">
            <el-input v-model="queryParams[item.attributeName]" style="width:200px"></el-input>
          </el-form-item>
          <el-form-item label="数据模式" prop="dataCategory">
            <el-select v-model="queryParams.dataCategory" clearable filterable @change="$forceUpdate()">
              <el-option label="新增" value="1" key="1"></el-option>
              <el-option label="更新" value="2" key="2"></el-option>
              <el-option label="删除" value="3" key="3"></el-option>
              <el-option label="无变动" value="4" key="4"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item style="flex: 1"></el-form-item>
          <el-form-item style="float:right" v-if="radio==='1'">
            <el-button v-show="searchClu.length>0" :disabled="versionObj.id===''||versionObj.id===undefined" type="info" plain icon="el-icon-search" size="mini" v-hasPermi="['dataaccess:table:edit']" @click="getTableData">搜索</el-button>

            <el-button :disabled="versionObj.id===''||versionObj.id===undefined" plain icon="el-icon-refresh" size="mini" v-hasPermi="['dataaccess:datatransfer:view']" @click="getTableData">刷新</el-button>

          </el-form-item>
          <el-form-item style="float:right" v-if="radio==='2'">
            <el-button v-show="searchClu.length>0" :disabled="versionObj.id===''||versionObj.id===undefined" type="info" plain icon="el-icon-search" size="mini" v-hasPermi="['dataaccess:table:edit']" @click="getTableMidData">搜索</el-button>
            <el-button :disabled="versionObj.id===''||versionObj.id===undefined" plain icon="el-icon-refresh" size="mini" v-hasPermi="['dataaccess:datatransfer:view']" @click="getTableMidData">刷新</el-button>
          </el-form-item>
        </el-form>
        <!--正式库-->
          <div v-if="ifShowTable">
            <el-table width="100%" max-height="620" v-loading="loading" :data="tableList">

              <el-table-column
                label="序号"
                width="100"
                align="center"
                :formatter="snFormatter">
              </el-table-column>
              <el-table-column v-for="item in tableClu"
                              :label="item.attributeNameCn===undefined?item.attributeName:item.attributeNameCn+'('+item.attributeName+')'"
                              :prop="item.attributeName"
                              :key="item.id"
                              width="150"
                              align="center">
                <template slot-scope="scope">
                  <el-popover trigger="hover" placement="top">
                    <p>{{ scope.row[item.attributeName] }}</p>
                    <div slot="reference" class="name-wrapper">
                      <p>{{ scope.row[item.attributeName] }}</p>
                    </div>
                  </el-popover>
                </template>
              </el-table-column>
            </el-table>
            <pagination
              v-show="total>0"
              :total="total"
              :page.sync="queryParams.pageIndex"
              :limit.sync="queryParams.pageSize"
              @pagination="getTableData"
            />
        </div>
      <!--中间库-->
      <div v-if="ifShowTableMid">
            <el-table width="100%" max-height="620" :data="tableMidList">

              <el-table-column
                v-loading="loading"
                label="序号"
                width="100"
                align="center"
                :formatter="snFormatter">
              </el-table-column>
              <el-table-column v-for="item in tableMidClu"
                              :label="item.attributeNameCn===undefined?item.attributeName:item.attributeNameCn+'('+item.attributeName+')'"
                              :prop="item.attributeName"
                              :key="item.id"
                              width="150"
                              align="center">
                <template slot-scope="scope">
                  <el-popover trigger="hover" placement="top">
                    <p>{{ scope.row[item.attributeNamUnderlinee] }}</p>
                    <div slot="reference" class="name-wrapper">
                      <p>{{ scope.row[item.attributeNameUnderline] }}</p>
                    </div>
                  </el-popover>
                </template>
              </el-table-column>
            </el-table>
            <pagination
              v-show="total>0"
              :total="total"
              :page.sync="queryParams.pageIndex"
              :limit.sync="queryParams.pageSize"
              @pagination="getTableMidData"
            />
        </div>
      </el-col>
    </el-row>

    <el-dialog
      title="展示配置"
      :close-on-click-modal="false"
      width="85%"
      :visible.sync="openShow"
    >
      <zspz :versionObj="versionObj"></zspz>
    </el-dialog>



  </div>
</template>

<script>
  import { sjyTree,getTableClu,getTable,updateTable,tbData,getMiddleTableClu,getMidTable } from "@/api/ysjgl/sjcx";
  import { getToken } from "@/utils/auth";
  import Treeselect from "@riophae/vue-treeselect";
  import zspz from "./zspz";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";

  export default {
    name: "Sjcx",
    components: { Treeselect,zspz },
    data() {
      return {
        // 中间库id
        rlModelId: '',
        // 显示正式库表格
        ifShowTable: true,
        // 显示中间库表格
        ifShowTableMid: false,
        // 是否显示中间库
        showAudit: false,
        // 单选框
        radio: '1',
        // 遮罩层
        loading: false,
        // 选中数据
        ids: [],
        versionObj:{},
        dbName:'',
        // 总条数
        total: 0,
        // 树选项
        data: [{
          id: '0',
          label: '元模型',
          icon: 'el-icon-s-home',
          color: 'color: #FFC000',
          children: []
        }],
        idArr:[],
        // 查询参数
        queryParams: {
          pageIndex: 1,
          pageSize: 10,
          dataCategory: ''
        },
        // 中间库列信息
        tableMidClu: [],
        // 列信息
        tableClu: [],
        searchClu: [],
        tableList:[],
        //展示配置
        openShow:false,
      };
    },
    watch: {
      openShow(val){
        if(!val){
          this.tableList=[]
          this.tableClu=[]
          this.total=0
          this.searchClu=[]
          this.queryParams= {
            pageIndex: 1,
            pageSize: 10
          }
            this.getCommon()
        }
      },
      // 根据名称筛选树
      dbName(val) {
        this.$refs.tree.filter(val);
        if(val==''){
          this.getSjyTree();
        }
      }
    },
    created() {
      this.getSjyTree();
    },
    methods: {
      // 正式库中间库radio事件
      ifMiddle:function (val) {
        /* let that = this */
        if(val === '1') {
          this.ifShowTable = true;
          this.ifShowTableMid = false;
          this.getTableData();
        } else if(val === '2') {
            this.ifShowTable = false;
            this.ifShowTableMid  = true;
            getMiddleTableClu({modelId: this.rlModelId,modelVersion:'edit'}).then(res => {
              this.tableMidClu = res.data.columns
              this.getTableMidData()
           /*  this.tableClu = res.data.columns;
            this.searchClu = res.data.searchAttributes;
            this.getTableData() */
          });
        }
      },
      /* 格式序列化列 */
      snFormatter: function (row, col, value, index) {
        return this.queryParams.pageSize * (this.queryParams.pageIndex - 1) + index + 1;
        //return index + 1
      },

      /** 查询数据源下拉树结构 */
      getSjyTree() {
        this.idArr=[]
        sjyTree().then(res => {
          res = res.data
          this.data[0].children = res
          this.idArr=['0']
        });

      },
      // 筛选节点,不会返回匹配的node的子节点
      // filterNode(value, data) {
      //   if (!value) return true;
      //   return data.label.indexOf(value) !== -1;
      // },
      // 筛选节点,返回匹配的node的子节点
      filterNode(value,data,node) {
        if(!value){
          return true;
        }
        let level = node.level;
        let _array = [];//这里使用数组存储 只是为了存储值。
        this.getReturnNode(node,_array,value);
        let result = false;
        _array.forEach((item)=>{
          result = result || item;
        });
        return result;
      },
      getReturnNode(node,_array,value){
        let isPass = node.data &&  node.data.label && node.data.label.indexOf(value) !== -1;
        isPass?_array.push(isPass):'';
        // this.index++;
        if(!isPass && node.level!=1 && node.parent){
          this.getReturnNode(node.parent,_array,value);
        }
      },
      // 节点单击事件
      handleNodeClick(res) {
        this.versionObj={}
        this.tableList=[]
        this.tableClu=[]
        this.total=0
        this.searchClu=[]
        this.queryParams= {
          pageIndex: 1,
          pageSize: 10
        }
        if (res.type === 'versionT') {
          console.log(res)
          this.versionObj=res
          this.radio = '1';
          this.ifShowTable = true;
          this.ifShowTableMid = false;
          this.getCommon()
          this.getTableData();
        }
        if(res.auditMode === '1') {
          this.showAudit = true
          this.rlModelId = res.parentId
        } else {
          this.showAudit = false
        }
      },
      //查询数据源与表格属性数据获取表格动态属性
      getCommon() {
        getTableClu({modelId: this.versionObj.parentId,modelVersion:this.versionObj.label}).then(res => {
          this.tableClu = res.data.columns;
          this.searchClu = res.data.searchAttributes;
          this.queryParams.dataCategory = ''
          this.getTableData()
        });
      },
      // 获取中间库表格数据
      getTableMidData() {
        this.loading = true;
        let params={
          modelId: this.rlModelId,
          modelVersion:this.versionObj.label,
          pageIndex:this.queryParams.pageIndex,
          pageSize:this.queryParams.pageSize,
          whereAttributes:[]
        }
        for (let i = 0; i < this.searchClu.length; i++) {
          if(this.queryParams[this.searchClu[i].attributeName]!==''&&this.queryParams[this.searchClu[i].attributeName]!==undefined){
            params.whereAttributes.push({attributeName:this.searchClu[i].attributeName,attributeValue:this.queryParams[this.searchClu[i].attributeName]})
          }
        }
        if(this.queryParams.dataCategory !== '') {
          params.whereAttributes.push({attributeName:'data_category',attributeValue:this.queryParams.dataCategory})
        }
        getMidTable(params).then(response => {
            this.tableMidList = response.rows;
            this.total = response.total;
            this.loading = false;
          }
        ).catch(() => {
          this.loading = false
        });
      },
      //获取表格数据
      getTableData() {
        this.loading = true;
        let params={
          modelId: this.versionObj.parentId,
          modelVersion:this.versionObj.label,
          pageIndex:this.queryParams.pageIndex,
          pageSize:this.queryParams.pageSize,
          whereAttributes:[]
        }
        for (let i = 0; i < this.searchClu.length; i++) {
          if(this.queryParams[this.searchClu[i].attributeName]!==''&&this.queryParams[this.searchClu[i].attributeName]!==undefined){
            params.whereAttributes.push({attributeName:this.searchClu[i].attributeName,attributeValue:this.queryParams[this.searchClu[i].attributeName]})
          }
        }
        /* alert(this.queryParams.dataCategory) */
        if(this.queryParams.dataCategory!=='') {
          params.whereAttributes.push({attributeName:'data_category',attributeValue:this.queryParams.dataCategory})
        }
        getTable(params).then(response => {
            this.tableList = response.rows;
            this.total = response.total;
            this.loading = false;
          }
        ).catch(() => {
          this.loading = false
        });
      },
     /* /!** 保存同步条件操作 *!/
      updateSearchCondition() {
        let params = {
          id: this.tableId,
          searchCondition: this.queryParams.tbparma
        }
        updateTable(params).then(response => {
          this.$modal.msgSuccess("保存成功");
        });
      },*/
      /** 同步数据操作 */
      refreshData() {
        this.$modal.confirm('是否进行表格数据同步操作？').then(() => {
          this.loading = true
          this.params = {
            modelId: this.versionObj.parentId,
            modelVersion:this.versionObj.label,
          }
          tbData(this.params).then(response => {
              this.loading = false
              if (response.msg=='同步成功') {
                this.$modal.msgSuccess(response.msg);
              } else {
                this.$modal.msgError(response.msg);
              }
              this.getTableData()
            }
          ).catch(() => {
            this.loading = false
          });
        }).then(() => {

        }).catch(() => {
        });
      },
      //打开配置页面
      openTool(){
        this.openShow=true
      },


    }
  };
</script>
<style lang="scss">
  #tbtj .el-form-item__content{
    width: 92%;
  }
  #sjcxForm .el-form-item__content{
    width: auto !important;
    margin: auto !important;
  }
  #sjcx .el-dialog__body{
    padding:0px !important;
  }
  #sjcxForm .el-form-item__label {
    width:80px !important
  }

</style>
