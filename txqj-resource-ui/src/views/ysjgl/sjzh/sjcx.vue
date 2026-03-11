<template>
  <div v-loading="loading" class="app-container">
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
          <el-col :span="18">
            <span>表数据</span>
          </el-col>
        </el-row>
        <el-form id="sjcxForm" :model="queryParams" style="width: 100%;display: flex" ref="queryForm" size="small" :inline="true" label-width="68px">
          <el-form-item v-for="(item,index) in searchClu"
                        :key="index"
                        :label="item.attributeNameCn">
            <el-input v-model="queryParams[item.attributeName]" ></el-input>
          </el-form-item>
          <el-form-item style="flex: 1"></el-form-item>
          <el-form-item>
            <el-button v-show="searchClu.length>0" :disabled="versionObj.id===''||versionObj.id===undefined" type="info" plain icon="el-icon-search" size="mini" v-hasPermi="['dataaccess:table:edit']" @click="getTableData">搜索</el-button>
            <el-button :disabled="versionObj.id===''||versionObj.id===undefined" type="primary" plain icon="el-icon-magic-stick" size="mini" v-hasPermi="['dataaccess:datatransfer:view']" @click="refreshData">同步</el-button>
            <el-button :disabled="versionObj.id===''||versionObj.id===undefined" plain icon="el-icon-refresh" size="mini" v-hasPermi="['dataaccess:datatransfer:view']" @click="getTableData">刷新</el-button>
          </el-form-item>
        </el-form>

        <el-table width="100%" max-height="620" :data="tableList">
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
      </el-col>
    </el-row>



  </div>
</template>

<script>
  import { sjyTree,getTableClu,getTable,updateTable,tbData } from "@/api/ysjgl/sjcx";
  import { getToken } from "@/utils/auth";
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";

  export default {
    name: "sjcx",
    components: { Treeselect },
    data() {
      return {
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
          pageSize: 10
        },
        // 列信息
        tableClu: [],
        searchClu: [],
        tableList:[]
      };
    },
    watch: {
      // 根据名称筛选树
      dbName(val) {
        this.$refs.tree.filter(val);
      }
    },
    created() {
      this.getSjyTree();
    },
    methods: {
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
      // 筛选节点
      filterNode(value, data) {
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
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
          this.versionObj=res
          this.getCommon()
        }
      },
      //查询数据源与表格属性数据获取表格动态属性
      getCommon() {
        getTableClu({modelId: this.versionObj.parentId,modelVersion:this.versionObj.label}).then(res => {
          this.tableClu = res.data.columns;
          this.searchClu = res.data.searchAttributes;
          this.getTableData()
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
              /* if (response.msg=='同步成功') {
                this.$modal.msgSuccess(response.msg);
              } else {
                alert(1)
                this.$modal.msgError(response.msg);
              } */
              this.getTableData()
            }
          ).catch(() => {
            this.loading = false
          });
        }).then(() => {

        }).catch(() => {
        });
      }
    }
  };
</script>
<style>
  #tbtj .el-form-item__content{
    width: 92%;
  }
  #sjcxForm .el-form-item__content{
    width: auto !important;
    margin: auto !important;
  }
  #sjcxForm .el-input{
    /*width:2rem;*/
  }
</style>
