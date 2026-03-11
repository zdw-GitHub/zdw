<template>
  <div class="commonWh">
    <div style="width: 100%; height: 0" class="getSpanSize"></div>
    <div class="layout" style="width: 100%; height: 100%">
      <div style="height: 100%; width: calc(20% - 10px); float: left; background: #fff; padding: 10px">
        <el-input
          v-model="dbName"
          placeholder="元模型名称"
          clearable
          size="mini"
          prefix-icon="el-icon-search"
          style="margin-bottom: 10px"
        />
        <el-tree
          :data="data"
          highlight-current
          :node-key="id"
          :default-expanded-keys="idArr"
          :filter-node-method="filterNode"
          ref="tree"
          style="height: calc(100% - 38px); overflow-y: auto"
        >
            <span style="width: 100%;height:100%;display: block" class="custom-tree-node" slot-scope="{ node, data }">
              <span style="width: 100%;display: block;line-height: 25px" @click="handleNodeClick(data)">
                  <i :class="data.icon" :style="data.color"></i>{{data.label}}
              </span>
          </span>
        </el-tree>
      </div>
      <div style="height: 100%; width: 80%; float: right; background: #fff; padding: 10px">
        <!--条件查询-->
        <div v-show="showLabelQuery" class="label_query">
          <div class="left_tit">
            <span class="left_tit_con"> 条件查询</span>
            <a class="left_tit_opa" @click="showLabelQueryOpen=!showLabelQueryOpen">
              <span v-if="!showLabelQueryOpen">展开<i class="el-icon-arrow-down"></i></span>
              <span v-else>收起<i class="el-icon-arrow-up"></i></span>
            </a>
          </div>
          <div class="right_con">
            <div class="right_item" v-for="(item,index) in queryList">
              <div class="itemTit" style="width:100px">
                <span>{{item.label}}：</span>
              </div>
              <div class="itemCon" :style="{'height':item.ifOpen?'auto':item.ifQuery==='1'||item.ifQuery===1?'60px':'30px'}">
                <!--字母索引abcList-->
                <div v-show="item.ifQuery==='1'||item.ifQuery===1" style="width: 100%;height: 30px;padding-left: 10px;">
                  <el-radio-group @change="getAbcData(index)" class="queryBox" v-model="item.initial" v-for="opa in abcList">
                    <el-radio :value="opa.value" :label="opa.value">{{opa.label}}</el-radio>
                  </el-radio-group>
                </div>
                <el-radio-group v-if="!item.ifMoreChoose" v-model="item.radioValue" v-for="(opa,opaIndex) in item.listRadio">
                  <el-radio :value="opa.code" :label="opa.code" @change="getTableMidData()">{{opa.name}}</el-radio>
                </el-radio-group>
                <el-checkbox-group v-if="item.ifMoreChoose" v-model="item.checkValue" v-for="(opa,opaIndex) in item.list">
                  <el-checkbox :value="opa.code" :label="opa.code" @change="getTableMidData()">{{opa.name}}</el-checkbox>
                </el-checkbox-group>
              </div>
              <div class="itemOpa">
                <el-button size="mini" @click.native="item.ifOpen=!item.ifOpen">
                  <span v-if="!item.ifOpen">更多<i class="el-icon-arrow-down"></i></span>
                  <span v-else>收起<i class="el-icon-arrow-up"></i></span>
                </el-button>
                <el-button size="mini" @click.native="item.ifMoreChoose=!item.ifMoreChoose">
                  <span v-if="!item.ifMoreChoose"><i class="el-icon-document-copy"></i> 多选</span>
                  <span v-else><i class="el-icon-document"></i> 单选</span>
                </el-button>
              </div>
            </div>
          </div>
        </div>
        <div class="openTit" @click="showLabelQuery=!showLabelQuery">
          <a >
            <span v-if="!showLabelQuery">展开<i class="el-icon-arrow-down"></i></span>
            <span v-else>收起<i class="el-icon-arrow-up"></i></span>
          </a>
        </div>
        <!--基础查询-->
        <div class="label_query">
          <div class="left_tit">
            <span class="left_tit_con"> 基础查询</span>
            <a class="left_tit_opa" @click="showBaseQueryOpen=!showBaseQueryOpen">
              <span v-if="!showBaseQueryOpen">展开<i class="el-icon-arrow-down"></i></span>
              <span v-else>收起<i class="el-icon-arrow-up"></i></span>
            </a>
          </div>
          <div class="right_con" v-show="showBaseQueryOpen">
            <el-form class="searchBox" label-width="100px" :model="searchForm">
              <el-form-item style="float: left" v-for="(item,index) in searchList"
                            :key="index"
                            :label="item.attributeNameCn">
                <el-select size="mini" v-if="item.attType==='select'" clearable filterable v-model="searchForm[item.attributeNameUnderline]">
                  <el-option
                    v-for="arr in item.list"
                    :label="arr.name"
                    :key="arr.code"
                    :value="arr.code"></el-option>
                </el-select>
                <el-date-picker
                  v-model="searchForm[item.attributeNameUnderline]"
                  type="datetime"
                  size="mini"
                  v-else-if="item.attType==='datetime'"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  placeholder="选择日期时间">
                </el-date-picker>
                <el-input size="mini" v-else v-model="searchForm[item.attributeNameUnderline]" ></el-input>
              </el-form-item>
              <el-form-item label="数据模式" prop="dataCategory" style="float: left">
                <el-select v-model="queryParams.dataCategory" clearable filterable @change="$forceUpdate()" size="mini">
                  <el-option label="新增" value="1" key="1"></el-option>
                  <el-option label="更新" value="2" key="2"></el-option>
                  <el-option label="删除" value="3" key="3"></el-option>
                  <!-- <el-option label="无变动" value="4" key="4"></el-option> -->
                </el-select>
              </el-form-item>
              <el-form-item style="float: left;" width="120">
                <template>
                  <el-popover
                    placement="top-start"
                    title="查询条件列表"
                    width="250"
                    trigger="hover"
                  >
                    <el-button type="primary" size="mini"  @click="customQuery()" slot="reference">自定义查询（{{list.length}})</el-button>
                    <div v-for=" (item,index) in list" style="border-radius: 4px;border-color:gray;overflow: hidden;">
                      <span>{{ index+1 }}.&nbsp;</span>
                      <span style="color:#006A65">{{ item.relationship }}</span>
                      <span>&nbsp;{{ item.nameCn }}{{item.operator}}{{item.value}}</span>
                      <el-button icon="el-icon-delete" type="text" @click="delLocal(index)" style="float:right;position:relative;bottom:8px"></el-button>
                    </div>
                  </el-popover>
                </template>
              </el-form-item>

              <el-form-item style="float:right;">
                <el-button type="primary"  size="mini"  @click="getTableMidData()">查询</el-button>
                <el-button size="mini"  @click="resetSearch()">重置</el-button>
                <el-button  size="mini" :disabled="ifRun" plain @click="handleAdd()">发起流程</el-button>
              </el-form-item>
            </el-form>
          </div>
        </div>
        <el-table
          width="100%"
          :data="tableMidList"
          ref="tableMidList"
          :row-key="getRowKey"
          height="80%"
          @selection-change="handleSelectionChange">
          <el-table-column type="selection" :reserve-selection="true"></el-table-column>
          <el-table-column
            label="序号"
            width="80"
            align="center"
            :formatter="snFormatter">
          </el-table-column>
          <el-table-column
            v-for="item in cluList"
            :label="item.attributeNameCn===undefined?item.attributeName:item.attributeNameCn"
            :prop="item.attributeNameUnderline"
            :key="item.id"
            width="150"
            align="center">
            <template slot-scope="scope">
              <el-popover trigger="hover" placement="top">
                <p>{{ scope.row[item.attributeNameUnderline] }}</p>
                <div slot="reference" class="name-wrapper">
                  <p>{{ scope.row[item.attributeNameUnderline] }}</p>
                </div>
              </el-popover>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          :total="total"
          :page.sync="queryParams.pageIndex"
          :limit.sync="queryParams.pageSize"
          @pagination="getTableMidData"
        />
      </div>
    </div>
    <template v-if="false">
      <div id="sjcx" v-loading="loading" class="app-container">
        <el-row :gutter="20">
          <!--数据源数据-->
          <el-col :span="4" :xs="24">
            <div class="head-container">
            </div>
            <div class="head-container">
            </div>
          </el-col>
          <!--表格数据-->
          <el-col :span="20" :xs="24">

            <el-dialog
              v-dialogDrag
              title="自定义查询"
              :close-on-click-modal="false"
              width="58%"
              :visible.sync="openCustom"
              style="margin-top: 19vh !important;"
            >
              <el-row :gutter="20">
                <el-col :span="7"><el-card class="box-card" style="width:300px" shadow="never">
                  <div slot="header" class="clearfix">
                    <span>属性列表</span>
                  </div>
                  <div class="left">
                    <el-input
                      v-model="filterText"
                      placeholder="请输入属性名称"
                      clearable
                      size="small"
                      prefix-icon="el-icon-search"
                      style="margin-bottom: 20px"
                    />
                    <el-tree
                      class="filter-tree"
                      :data="deptOptions"
                      highlight-current
                      default-expand-all
                      node-key="data.id"
                      :filter-node-method="filterNode"
                      ref="treeForm" style="font-size: 14px;height:26.5vh;overflow-y: auto;">
                <span style="width: 100%;height:100%;display: block" class="custom-tree-node" slot-scope="{ node, data }">
                  <span  :class="data.isHigh==='1'?'red':''" style="width: 100%;display: block;line-height: 25px" @click="handleNodeClickCustom(data.columnName,data.columnNameCn)">
                      <!-- <i :class="data.icon" :style="data.color"></i> -->{{ data.columnNameCn }}
                  </span>
                </span>
                    </el-tree>
                  </div>
                </el-card>
                </el-col>
                <el-col :span="1">
                  <div style="margin-top: 180px;">
                    <img src="../../../assets/images/arrow.png">
                  </div>
                </el-col>

                <el-col :span="7">
                  <el-card class="box-card" style="width:300px;height:40vh" shadow="never">
                    <div slot="header" class="clearfix">
                      <span>新增查询条件</span>
                      <el-button style="float: right; padding: 3px 10px;z-index:9999" type="text" @click="resetQuery">重置</el-button>
                    </div>
                    <div class="center">
                      <el-form label-width="100px" :model="addQueryForm" :rules="addQueryRules" ref="addQueryForm">
                        <el-form-item label="属性名称：" prop="name">
                          <el-input v-model="addQueryForm.name" disabled></el-input>
                        </el-form-item>
                        <el-form-item label="逻辑关系：" prop="relationship">
                          <el-select v-model="addQueryForm.relationship">
                            <el-option label="或（or）" value="or"></el-option>
                            <el-option label="与（and）" value="and"></el-option>
                          </el-select>
                        </el-form-item>
                        <el-form-item label="操作符：" prop="operator">
                          <el-select v-model="addQueryForm.operator">
                            <el-option label="不为空" value="不为空"></el-option>
                            <el-option label="大于" value="大于"></el-option>
                            <el-option label="大于等于" value="大于等于"></el-option>
                            <el-option label="小于" value="小于"></el-option>
                            <el-option label="小于等于" value="小于等于"></el-option>
                            <el-option label="等于" value="等于"></el-option>
                            <el-option label="包含" value="包含"></el-option>
                          </el-select>
                        </el-form-item>
                        <el-form-item label="值：" prop="value">
                          <el-input v-model="addQueryForm.value"></el-input>
                        </el-form-item>
                        <el-form-item>
                          <el-button icon="el-icon-plus" @click="addQuery('addQueryForm')">添加</el-button>
                        </el-form-item>
                      </el-form>
                    </div>
                  </el-card>
                </el-col>
                <el-col :span="1">
                  <div style="margin-top: 180px;">
                    <img src="../../../assets/images/arrow.png">
                  </div>
                </el-col>
                <el-col :span="7">

                  <el-card class="box-card" style="width:300px;height:40vh" shadow="never">
                    <div slot="header" class="clearfix">
                      <span>查询条件列表</span>
                      <el-button style="float: right; padding: 3px 10px;z-index:9999" type="text" @click="clearLocal">清空</el-button>
                    </div>
                    <div>
                      <div v-for=" (item,index) in list" style="display:block;margin-bottom:5px;padding-top: 5px;padding-left:5px;border: 1px solid #ECECEC;overflow: hidden;">
                        <span>{{ index+1 }}.&nbsp;</span>
                        <span style="color:#006A65">{{ item.relationship }}</span>
                        <span>&nbsp;{{ item.nameCn }}{{item.operator}}{{item.value}}</span>
                        <el-button icon="el-icon-delete" type="text" @click="delLocal(index)" style="float:right;position:relative;bottom:8px;right:5px"></el-button>
                      </div>
                    </div>
                  </el-card>
                </el-col>
              </el-row>
              <div slot="footer" class="dialog-footer" style="margin-bottom:20px">
                <el-button type="primary" @click="openCustom=false">确 定</el-button>
              </div>
            </el-dialog>
          </el-col>
        </el-row>


      </div>
    </template>
  </div>
</template>

<script>
  import { tree,customTree,getTable,getMiddleTableClu,getMidTable,tableDropDownBox,initgriddatasite ,getPrimaryKeyName} from "@/api/sj/audit";
  import { getToken } from "@/utils/auth";
  import {listDefinition,latestPdConversion} from "@/api/flowable/definition";
  import { mapMessage,conditionalData } from "@/api/zdwh/zdzywh";
  import Treeselect from "@riophae/vue-treeselect";
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";

  export default {
    name: "Sj",
    components: { Treeselect },
    data() {
      return {
        // 判定是否全选
        allDataFlag: false,
        // 存储选择的数据
        multipleSelection: [],
        // 表格数据长度
        dataLength: '',
        // 自定义查询表单
        addQueryForm: {
          name:'',
          relationship: '',
          operator: '',
          value: ''
        },
        // 资源树搜索
        filterText:'',
        // 显示搜索条件
        showSearch: false,
        // 自定义查询规则
        addQueryRules: {
          name: [ {
            required: true, message: '请选择属性名称',trigger: ['change', 'blur']
          }],
          relationship:[ {
            required: true, message: '请选择逻辑关系', trigger: ['change', 'blur']
          }],
          operator: [ {
            required: true, message: '请选择操作符' , trigger: ['change', 'blur']
          }]
        },
        // 自定义查询
        openCustom: false,
        versionObj: {},
        openShow:false,
        // 遮罩层
        processLoading: true,
        loading: false,
        //显示条件查询
        showLabelQuery:false,
        //查询条件展开,基础条件展开
        showLabelQueryOpen:false,
        showBaseQueryOpen:true,
        //字母索引 条件查询列表
        radio:'',
        radioList:[],
        abcList:[
          {label:'全部',value:''},{label:'A',value:'A'},{label:'B',value:'B'},
          {label:'C',value:'C'},{label:'D',value:'D'},{label:'E',value:'E'},
          {label:'F',value:'F'},{label:'G',value:'G'},{label:'H',value:'H'},
          {label:'I',value:'I'},{label:'J',value:'J'},{label:'K',value:'K'},
          {label:'L',value:'L'},{label:'M',value:'M'},{label:'N',value:'N'},
          {label:'O',value:'O'},{label:'P',value:'P'},{label:'Q',value:'Q'},
          {label:'R',value:'R'},{label:'S',value:'S'},{label:'T',value:'T'},
          {label:'U',value:'U'},{label:'V',value:'V'},{label:'W',value:'W'},
          {label:'X',value:'X'},{label:'Y',value:'Y'},{label:'Z',value:'Z'},
        ],
        queryList:[],
         //动态查询条件与列,必填与非必填
        cluList:[],
        nullList:[],
        notNullList:[],
        // 查询条件modelId
        modelId: '',
        // 中间库列信息
        tableMidClu: [],
        // 中间库id
        rlModelId:'',
        // 单选框
        radio: '1',
        // 遮罩层
        loading: false,
        // 弹出层标题
        title: "",
        // 选中数据
        ids: [],
        versionObj:{},
        dbName:'',
        // 树选项
        deptOptions: [],
        // 总条数
        total: 0,
        // 主键名称
        keyName: '',
        //搜索框
        searchForm: {},
        // 树选项
        data: [{
          id: '0',
          label: '元模型',
          icon: 'el-icon-s-home',
          color: 'color: #FFC000',
          children: []
        }],
        idArr:[],
        //动态查询条件与列,必填与非必填
        searchList:[],
        // 查询参数
        queryParams: {
          pageIndex: 1,
          pageSize: 15,
          dataCategory: ''
        },
        // 列信息
        tableClu: [],
        searchClu: [],
        tableMidList:[],
        //展示配置
        openShow:false,
        //自定义
        list:[],
        // 发起流程
        ifRun:true,
        open:false,
        processTotal:0,
        definitionList:[],
        // 查询参数
        queryProcessParams: {
          pageNum: 1,
          pageSize: 15,
          name: null,
          category: null,
          key: null,
          tenantId: null,
          deployTime: null,
          derivedFrom: null,
          derivedFromRoot: null,
          parentDeploymentId: null,
          engineVersion: null
        },
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
            pageSize: 15
          }
            this.getCluList()
        }
      },
      // 根据名称筛选树
      dbName(val) {
        this.$refs.tree.filter(val);
      },
      'list.length': {
        handler(newValue, oldValue) {

            if (newValue !== oldValue) {
                // 操作
              for(let j=0;j<this.deptOptions.length;j++){
                this.$set(this.deptOptions[j],'isHigh','0')
              }
                this.ifHight()
            }

        }
      }
    },
    created() {
      this.getSjyTree();
    },
    methods: {
      // 表格多选id
      getRowKey(row) {
        return row.id
      },
      /** 重置按钮操作 */
    resetProcessQuery() {
      this.resetForm("queryProcessForm");
      this.handleProcessQuery();
    },
    /** 搜索按钮操作 */
    handleProcessQuery() {
      this.queryProcessParams.pageNum = 1;
      this.listDefinition();
    },
      /**  发起流程申请 */
      handleStartProcess(row){
        // console.log(row)
        // this.open=false
        this.$router.push({ path: '/flowable/task/record/index/'+row.id,
          query: {
            deployId: row.deploymentId,
            procDefId:row.id,
            finished: true,
            flowCategory: row.category,
            procInsId:row.id
            }
        })
        /* alert(row.id) */
      },
      /** 流程操作 */
      handleAdd() {
        var selectionData = this.multipleSelection
        // console.log('测试',selectionData)
        localStorage.removeItem('selectionList')
        localStorage.removeItem('newArr')
        localStorage.removeItem('newArr1')
        localStorage.setItem('selectionList',JSON.stringify(selectionData))
        localStorage.setItem('modelId',this.modelId)

        let newArr1={}
        newArr1=this.multipleSelection
        let newArr=[]
        for(let i=0;i<this.multipleSelection.length;i++){
          /* multipleSelection[i][res] */
          newArr.push(this.multipleSelection[i][this.keyName])
        }
          localStorage.setItem('newArr',JSON.stringify(newArr))
        localStorage.setItem('newArr1',JSON.stringify(newArr1))
        // this.open = true;
            // this.title = "发起流程";
            // this.listDefinition();
        this.new()
      },
      //最新流程
      new(){
        latestPdConversion().then(res=>{
          this.handleStartProcess(res.data)
          console.log('最新',res.data)
        })
      },
      listDefinition(){
        listDefinition(this.queryProcessParams).then(response => {
          this.definitionList = response.data.records;
          this.processTotal = response.data.total;
          this.processLoading = false;
        });
    },
      toggleSelection(rows) {
        if (rows) {
          rows.forEach(row => {
            this.$refs.multipleTable.toggleRowSelection(row);
          });
        } else {
          this.$refs.multipleTable.clearSelection();
        }
      },
      handleSelectionChange(val) {
        this.multipleSelection = val;
        this.ifRun = false
        console.log(this.multipleSelection)
      },
      /*重置搜索 */
      resetSearch() {
          //重置基础搜索
          this.searchForm={}
          this.queryParams.dataCategory = ''
          //重置条件查询
          for (let i = 0; i < this.queryList.length; i++) {
            this.queryList[i].radioValue=''
            this.queryList[i].checkValue=[]
          }
          //重置自定义查询
          this.clearLocal()
        },
      // 重置自定义查询
      resetQuery() {
        this.addQueryForm.name = ''
        this.addQueryForm.relationship = ''
        this.addQueryForm.operator = ''
        this.addQueryForm.value = ''
      },
      // 删除localStorage
      delLocal(index) {
        this.$delete(this.list,index);
        /*  list = this.list; //把获取到的数据赋值给list  */
        localStorage.setItem("cmts", JSON.stringify(this.list)); //再次调用JOSN.stringify转为数组字符串，然后调用localStorage.setItem()
      },
      // 清空localStorage
      clearLocal() {
        localStorage.removeItem('cmts')
        var list = JSON.parse(localStorage.getItem("cmts") || "[]"); //从localStorage中获取之前发布过的数据
        this.list = list; //把获取到的数据赋值给this.list
      },
      // 添加localStorage
      addQuery(forName) {
        this.$refs[forName].validate((valid) => {
          if (valid) {
              var comment = { name:this.addQueryForm.cluName ,nameCn:this.addQueryForm.name , relationship: this.addQueryForm.relationship ,
                          operator: this.addQueryForm.operator, value:this.addQueryForm.value }
              //从localStorage中获取之前发布过的数据
              list.unshift(comment); //重新保存最新的数据（之前的+添加的）
              localStorage.setItem("cmts", JSON.stringify(list)); //再次调用JOSN.stringify转为数组字符串，然后调用localStorage.setItem()
              this.list = list; //把获取到的数据赋值给this.list
              this.resetQuery()
              this.$nextTick(() => {
                this.$refs[forName].clearValidate();
              });
          } else {
            /* console.log('error submit!!'); */
            return false;
          }
        });

      },
      //获取条件查询列表
      getQueryList(){
        var modelObj = {
            modelId: this.modelId,
            modelVersion: 'edit'
          }
          mapMessage(modelObj).then(res=>{
            for (let i = 0; i < res.data.length; i++) {
              res.data[i].radioValue=''
              res.data[i].checkValue=[]
              res.data[i].initial=''
              if(res.data[i].list.length>0){
                res.data[i].listRadio=[{code:'',name:'全部'}].concat(res.data[i].list)
              }
            }
            this.queryList=res.data
          })

        },
      //按首字母索引
      getAbcData(index){
        this.index = index
        /* alert(this.index) */
        this.queryList[index].radioValue=''
        this.queryList[index].checkValue=[]
        this.queryList[index].list=[]
        this.queryList[index].listRadio=[]
        conditionalData(this.queryList[index]).then(res=>{
          this.queryList[index].list=res.data
          if(this.queryList[index].list.length>0){
            this.queryList[index].listRadio=[{code:'',name:'全部'}].concat(this.queryList[index].list)
          }
        })
        /* console.log(this.queryList) */
      },
       //动态获取页面搜索条件与列名
       getCluList(){
          this.searchList = []
          var modelObj = {
            modelId: this.modelId,
            modelVersion: 'edit'
          }
          initgriddatasite(modelObj).then(res=>{
            this.cluList = res.data.columns
            this.searchList = res.data.searchAttributes
            this.tableMidClu = res.data
            this.queryParams.dataCategory = ''
            this.getTableMidData()
            //获取表格下拉框数据
            tableDropDownBox(modelObj).then(res=>{
              res=res.data
              for (let i = 0; i < this.cluList.length; i++) {
                this.cluList[i].rules=''
                for (let j = 0; j < res.length; j++) {
                  if(this.cluList[i].attributeName===res[j].name&&res[j].list.length>0){
                    this.cluList[i].attType='select'
                    this.cluList[i].list=res[j].list
                  }
                }
              }
              for (let i = 0; i < this.searchList.length; i++) {
                for (let j = 0; j < res.length; j++) {
                  if(this.searchList[i].attributeName===res[j].name&&res[j].list.length>0){
                    this.searchList[i].attType='select'
                    this.searchList[i].list=res[j].list
                  }
                }
              }
              //获取所有属性初始化规则
             /*  this.getAllRules() */
            })
            this.getTableMidData()
          })
        },
      /* 格式序列化列 */
      snFormatter: function (row, col, value, index) {
        return this.queryParams.pageSize * (this.queryParams.pageIndex - 1) + index + 1;
        //return index + 1
      },
      /** 查询数据源下拉树结构 */
      getSjyTree() {
        this.idArr=[]
        tree().then(res => {
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
      // 自定义查询节点单击事件
      handleNodeClickCustom(name,nameCn) {
        this.addQueryForm.name = nameCn
        this.addQueryForm.cluName = name
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
          pageSize: 15
        }
        if (res.type === 'metadata') {
          this.queryParams.dataCategory = ''
          this.rlModelId = res.rlModelId
          this.modelId = res.id
          this.getCluList()
          /* this.getCommon() */
          this.getQueryList()
          this.getKey( )
        }

      },
      getKey() {
        let params = {modeId: this.modelId}
        getPrimaryKeyName(params).then(res=>{
          res=res.msg
          this.keyName = res
          });
      },
      //查询数据源与表格属性数据获取表格动态属性
       getCommon() {
        var modelObj = {
            modelId: this.modelId,
            modelVersion: 'edit'
          }
          initgriddatasite(modelObj).then(res=>{
              this.tableMidClu = res.data
              this.queryParams.dataCategory = ''
              this.getTableMidData()
           /*  this.tableClu = res.data.columns;
            this.searchClu = res.data.searchAttributes;
            this.getTableData() */
          });
      },
      // 判定高亮
      ifHight() {
        for(let i=0;i<this.list.length;i++) {
          for(let j=0;j<this.deptOptions.length;j++){
            if(this.list[i].name === this.deptOptions[j].columnName){
              this.$set(this.deptOptions[j],'isHigh','1')
            }
          }
        }
      },
      //打开自定义查询页面
      customQuery() {
        this.openCustom = true
        this.getTree()
        this.resetQuery()
        for(let j=0;j<this.deptOptions.length;j++){
          this.$set(this.deptOptions[j],'isHigh','0')
        }
        this.ifHight()
        this.$nextTick(() => {
                this.$refs['addQueryForm'].clearValidate();
              });
      },
       /** 查询数据源下拉树结构 */
       getTree() {
        let params={
            modelId:this.modelId,
            isCustomQuery: '1'
          }
        customTree(params).then(res => {
          res=res.data
          this.deptOptions = res
          this.ifHight()
        });
      },
      // 获取中间库表格数据
      getTableMidData() {
        /* this.loading = true; */
        let params={
          modelId: this.modelId,
          isPage:'1',
          pageIndex:this.queryParams.pageIndex,
          pageSize:this.queryParams.pageSize,
          whereAttributes:[]
        }

        //基础查询
        for(let key in this.searchForm){
            let obj={}
              if(this.searchForm[key]!==''&&this.searchForm[key]!==undefined){
                obj={
                  attributeName:key,
                  attributeValue:this.searchForm[key]
                }
                params.whereAttributes.push(obj)
              }
          }
          //条件查询
          for (let i = 0; i < this.queryList.length; i++) {
            //多选
            let obj={}
            if(this.queryList[i].ifMoreChoose){
              if(this.queryList[i].checkValue!==undefined&&this.queryList[i].checkValue.length>0){
                obj={
                  attributeName:this.queryList[i].name,
                  attributeValue:this.queryList[i].checkValue.toString()
                }
                params.whereAttributes.push(obj)
              }
            }else{
              //单选
              obj={
                attributeName:this.queryList[i].name,
                attributeValue:this.queryList[i].radioValue
              }
              if(this.queryList[i].radioValue!==''){
                params.whereAttributes.push(obj)
              }
            }
          }
          //自定义查询
          if(this.list.length > 0) {
            for(let j = 0; j < this.list.length ;j++) {
            let obj={}
            obj={
                  attributeName:this.list[j].name,
                  attributeValue:this.list[j].value,
                  logicalRelationSymbol:this.list[j].relationship,
                  operator: this.list[j].operator
                }
            params.whereAttributes.push(obj)
          }
        }
        for (let i = 0; i < this.searchClu.length; i++) {
          if(this.queryParams[this.searchClu[i].attributeName]!==''&&this.queryParams[this.searchClu[i].attributeName]!==undefined){
            params.whereAttributes.push({attributeName:this.searchClu[i].attributeName,attributeValue:this.queryParams[this.searchClu[i].attributeName]})
          }
        }
        if(this.queryParams.dataCategory !== '') {
          params.whereAttributes.push({attributeName:'data_category',attributeValue:this.queryParams.dataCategory})
        }
        params.whereAttributes.push({attributeName:'AUDIT_STATUS',attributeValue:'0'})
        if(this.multipleSelection.length>0) {
          this.$confirm('跳转页面会取消已选择项，是否跳转?', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              getMidTable(params).then(response => {
                this.tableMidList = response.rows;
                this.dataLength = this.tableMidList.length;
                this.total = response.total;
                this.loading = false;

              }
            ).catch(() => {
              this.loading = false
            });
            }).catch(() => {
              this.$message({
                type: 'info',
                message: '已取消跳转'
              })
            })
        } else {
          getMidTable(params).then(response => {
                this.tableMidList = response.rows;
                this.dataLength = this.tableMidList.length;
                this.total = response.total;
                this.loading = false;

              }
           )}

      },
    }
  };
</script>
<style>
  .label_query{
    width: 100%;
    margin-bottom: 10px;
  }
  .label_query .left_tit{
    width: 100%;
    height:24px;
    display: flex;
    border-bottom:1px solid #E9E9E9;
  }
  .label_query .left_tit .left_tit_con{
    flex: 1;
    font-size:14px;
    font-weight: 600;
    color:rgba(0,0,0,0.85) ;
    line-height: 16px;
  }
  .label_query .left_tit .left_tit_opa{
    font-size:12px;
    font-weight: 600;
    color:#457eff ;
    line-height: 20px;
  }
  .label_query .right_con{
    width: 100%;
    margin-top: 15px;
  }
  .label_query .right_con .right_item{
    display: flex;
    border-bottom:2px solid #fff;
    padding: 2px 5px;
  }
  .label_query .right_con .right_item .itemTit{
    font-size: 14px;
    font-weight: 400;
    color: rgba(0,0,0,0.65);
    line-height: 30px;
  }
  .label_query .right_con .right_item .itemCon{
    flex:1;
    margin: 0 10px 0 20px;
    overflow: hidden;

  }
  .label_query .right_con .right_item .itemCon .el-radio__input{
    display: none
  }
  .label_query .right_con .right_item .itemCon .el-radio__label,
  .label_query .right_con .right_item .itemCon .el-checkbox__label{
    display: inline-block;
    margin-left:-5px;
    margin-right:20px;
    line-height: 30px;
    font-size: 14px;
    font-weight: 400;
  }
  .queryBox .el-radio__label{
    height: 23px;
    text-align: center;
    line-height: 20px !important;
    padding: 0px 5px !important;
  }
  .queryBox .el-radio__input.is-checked + .el-radio__label{
    color: #457eff;
    background: #ecf2ff;
    border: 1px solid #b5cbff
  }
  .label_query .right_con .right_item .itemCon .el-checkbox-group,
  .label_query .right_con .right_item .itemCon .el-radio-group
  {
    float: left;
  }
  .label_query .right_con .right_item .itemOpa{
    float: left;
    margin-top: 5px;
  }
  .opaForm .el-input{
    width: 12rem;
  }
  .seeForm .el-input{
    width: 18rem;
  }
  .error .el-input__inner {
    border: red 1px solid;
  }
  .red{
    color:#047E78;
  }
</style>

