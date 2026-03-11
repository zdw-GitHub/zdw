<template>
  <div class="commonWh">
    <div style="width: 100%; height: 0" class="getSpanSize"></div>
    <div class="box-card">
      <div class="queryBox">
        <!--基础查询-->
        <div class="basicsQuery">
          <el-form label-width="100px" :model="searchForm" ref="searchForm">
            <el-form-item label="名称：" >
              <el-input size="mini" v-model="searchForm.siteName"></el-input>
            </el-form-item>
            <el-form-item label="资源类型：">
              <el-select size="mini" filterable v-model="modelId">
                <el-option
                  v-for="item in resourceList"
                  :label="item.name"
                  :key="item.code"
                  :value="item.code"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="维护单位：">
              <el-select size="mini" clearable filterable v-model="searchForm.ownerId" >
                <el-option
                  v-for="item in ownerList"
                  :label="item.name"
                  :key="item.code"
                  :value="item.code"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="问题种类：">
              <el-select size="mini" clearable filterable v-model="searchForm.bRuleId">
                <el-option
                  v-for="item in typeList"
                  :label="item.name"
                  :key="item.code"
                  :value="item.code"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item style="float: right">
              <el-button type="primary" size="mini" @click="getTableData()">查询</el-button>
              <el-button size="mini" @click="resetSearch()">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
      <div class="tableBox">
        <div class="tabTopButton">
          <el-button @click="saveRule()" size="mini" type="primary">手动校验</el-button>
        </div>
        <el-table
          stripe
          border
          :span-method="objectSpanMethod"
          ref="multipleTable"
          :data="tableData"
          tooltip-effect="light"
          height="calc(100% - 82px)"
          v-loading="loading"
        >
          <el-table-column label="序号" width="80" align="center" :formatter="snFormatter"></el-table-column>
          <el-table-column
            v-if="tableData[0]"
            v-for="item in tableColData"
            :label="item.name"
            :min-width="getColSize(tableData[0][item.prop], item.name)"
            align="center"
            show-overflow-tooltip
            :prop="item.prop"
          ></el-table-column>
          <el-table-column prop="" label="操作" align="center" width="120">
            <template slot-scope="scope" >
              <el-button size="mini" type="primary" @click="editStation(scope)">编辑</el-button>
              <el-button size="mini" type="primary" @click="getHelpById(scope.row.siteId,scope.row.name)">引导</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          :total="total"
          :page.sync="pageIndex"
          :limit.sync="pageSize"
          @pagination="getTableData"
        />
      </div>
    </div>
    <el-dialog
      v-dialogDrag
      :title="title"
      :close-on-click-modal="false"
      :visible.sync="openGuide"
      width="700px"
      :modal="false"
    >
      <el-form ref="guideForm" :model="guideForm" label-width="100px">
        <el-row v-for="(item,index) in guideList">
          <el-col :span="12">
            <el-form-item style="margin-top:30px"  :label="item.attributeNameCn">
              <el-select v-if="item.attType==='select'" clearable filterable @change="checkAdd(item.attributeNameUnderline,guideForm[item.attributeNameUnderline])" v-model="guideForm[item.attributeNameUnderline]"
                         @blur="checkAdd(item.attributeNameUnderline,guideForm[item.attributeNameUnderline])"
                         :class="rulesForm[item.attributeNameUnderline] ===undefined||rulesForm[item.attributeNameUnderline]===''||rulesForm[item.attributeNameUnderline]===null?'':'error'">
                <el-option
                  v-for="arr in item.list"
                  :label="arr.name"
                  :key="arr.code"
                  :value="arr.code"></el-option>
              </el-select>
              <el-date-picker
                v-model="guideForm[item.attributeNameUnderline]"
                @blur="checkAdd(item.attributeNameUnderline,guideForm[item.attributeNameUnderline])"
                type="datetime"
                :class="rulesForm[item.attributeNameUnderline] ===undefined||rulesForm[item.attributeNameUnderline]===''||rulesForm[item.attributeNameUnderline]===null?'':'error'"
                v-else-if="item.attType==='datetime'"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="选择日期时间">
              </el-date-picker>
              <el-input @blur="checkAdd(item.attributeNameUnderline,guideForm[item.attributeNameUnderline])" v-else v-model="guideForm[item.attributeNameUnderline]"
                        :class="rulesForm[item.attributeNameUnderline] ===undefined||rulesForm[item.attributeNameUnderline]===''||rulesForm[item.attributeNameUnderline]===null?'':'error'"></el-input>
              <div style="margin-left:240px;margin-top:-40px;width:300px">
                <span style="font-size:10px;color:red;" v-if="seeErr" v-show="rulesForm[item.attributeNameUnderline]!==undefined&&rulesForm[item.attributeNameUnderline]!==''" v-html="rulesForm[item.attributeNameUnderline]"></span>
                <span style="font-size:10px;color:grey;" v-if="seeErr" v-show="rulesForm[item.attributeNameUnderline]===undefined" v-html="item.rules"></span>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <div style="float: left">
          <el-button @click="openGuide=false">取 消</el-button>
          <el-button type="primary" plain>帮 助</el-button>
        </div>
        <el-button type="primary" v-show="guideCurrebtPage===guidePageTotal" @click="nextGuide(1)">完 成</el-button>
        <el-button type="primary" v-show="guideCurrebtPage!==guidePageTotal" @click="nextGuide(2)">下一步</el-button>
      </div>
    </el-dialog>
    <!-- 修改属性对话框 -->
    <el-dialog
      v-dialogDrag
      title="站点修改"
      :visible.sync="editStationDrawer"
      width="750px"
      :close-on-click-modal="false"
      :modal="false"
    >
      <div class="el-dialog-div">
        <div class="formTitle" v-show="notNullList.length>0">
          <span>* 必填项</span>
        </div>
        <el-form class="opaForm" v-show="notNullList.length>0" ref="form" :model="editForm" label-width="100px" size="mini" style="margin-left:70px">
          <div v-for="item in notNullList">
            <el-form-item style="margin-top:30px"  :label="item.attributeNameCn">
              <el-select v-if="item.attType==='select'" clearable filterable @change="checkAdd(item.attributeNameUnderline,editForm[item.attributeNameUnderline])" v-model="editForm[item.attributeNameUnderline]"
                         @blur="checkAdd(item.attributeNameUnderline,editForm[item.attributeNameUnderline])"
                         :class="rulesForm[item.attributeNameUnderline] ===undefined||rulesForm[item.attributeNameUnderline]===''||rulesForm[item.attributeNameUnderline]===null?'':'error'">
                <el-option
                  v-for="arr in item.list"
                  :label="arr.name"
                  :key="arr.code"
                  :value="arr.code"></el-option>
              </el-select>
              <el-date-picker
                v-model="editForm[item.attributeNameUnderline]"
                @blur="checkAdd(item.attributeNameUnderline,editForm[item.attributeNameUnderline])"
                type="datetime"
                :class="rulesForm[item.attributeNameUnderline] ===undefined||rulesForm[item.attributeNameUnderline]===''||rulesForm[item.attributeNameUnderline]===null?'':'error'"
                v-else-if="item.attType==='datetime'"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="选择日期时间">
              </el-date-picker>
              <el-input @blur="checkAdd(item.attributeNameUnderline,editForm[item.attributeNameUnderline])" v-else v-model="editForm[item.attributeNameUnderline]"
                        :class="rulesForm[item.attributeNameUnderline] ===undefined||rulesForm[item.attributeNameUnderline]===''||rulesForm[item.attributeNameUnderline]===null?'':'error'"></el-input>
              <div style="margin-left:210px;margin-top:-30px;width:300px">
                <span style="font-size:10px;color:red;" v-if="seeErr" v-show="rulesForm[item.attributeNameUnderline]!==undefined&&rulesForm[item.attributeNameUnderline]!==''" v-html="rulesForm[item.attributeNameUnderline]"></span>
                <span style="font-size:10px;color:grey;" v-if="seeErr" v-show="rulesForm[item.attributeNameUnderline]===undefined" v-html="item.rules"></span>
              </div>
            </el-form-item>
          </div>
        </el-form>
        <div class="formTitle" v-show="nullList.length>0">
          <span>非必填</span>
        </div>

        <el-form class="opaForm" v-show="nullList.length>0" style="margin-left:70px" size="mini" label-width="100px" >
          <div v-for="item in nullList">
            <el-form-item style="margin-top:30px"  :label="item.attributeNameCn">
              <el-select v-if="item.attType==='select'" clearable filterable @change="checkAdd(item.attributeNameUnderline,editForm[item.attributeNameUnderline])" v-model="editForm[item.attributeNameUnderline]"
                         @blur="checkAdd(item.attributeNameUnderline,editForm[item.attributeNameUnderline])"
                         :class="rulesForm[item.attributeNameUnderline] ===undefined||rulesForm[item.attributeNameUnderline]===''||rulesForm[item.attributeNameUnderline]===null?'':'error'">
                <el-option
                  v-for="arr in item.list"
                  :label="arr.name"
                  :key="arr.code"
                  :value="arr.code"></el-option>
              </el-select>
              <el-date-picker
                v-model="editForm[item.attributeName]"
                @blur="checkAdd(item.attributeNameUnderline,editForm[item.attributeNameUnderline])"
                type="datetime"
                :class="rulesForm[item.attributeNameUnderline] ===undefined||rulesForm[item.attributeNameUnderline]===''||rulesForm[item.attributeNameUnderline]===null?'':'error'"
                v-else-if="item.attType==='datetime'"
                value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="选择日期时间">
              </el-date-picker>
              <el-input @blur="checkAdd(item.attributeNameUnderline,editForm[item.attributeNameUnderline])" v-else v-model="editForm[item.attributeNameUnderline]"
                        :class="rulesForm[item.attributeNameUnderline] ===undefined||rulesForm[item.attributeNameUnderline]===''||rulesForm[item.attributeNameUnderline]===null?'':'error'"></el-input>
              <div style="margin-left:210px;margin-top:-30px;width:300px">
                <span style="font-size:10px;color:red;" v-if="seeErr" v-show="rulesForm[item.attributeNameUnderline]!==undefined&&rulesForm[item.attributeNameUnderline]!==''" v-html="rulesForm[item.attributeNameUnderline]"></span>
                <span style="font-size:10px;color:grey;" v-if="seeErr" v-show="rulesForm[item.attributeNameUnderline]===undefined" v-html="item.rules"></span>
              </div>
            </el-form-item>
          </div>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitEditForm()">确 定</el-button>
        <el-button @click="editStationDrawer=false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {getColSize} from "@/utils/getColSize"
  import { getList,selectType, selectCompany,guide,saveSta,deleteRule,handRules,handRules1,selectResource} from "@/api/zdwh/errData";
  import{getModelId,initgriddatasite,tableDropDownBox,submitEdit,selectAllRules,getForm,getVerrules,getColunmId,errorStation} from "@/api/zdwh/zdzywh";

  export default {
    name: "ErrData",
    data() {
       return {
         tableColData: [
           {name: '名称', prop: 'name'},
           {name: '资源类型', prop: 'resourceType'},
           {name: '维护单位', prop: 'tmsName'},
           {name: '问题属性', prop: 'columnNameCn'},
           {name: '问题种类', prop: 'ruleName'},
           {name: '问题描述', prop: 'ruleDescription'},
           {name: '治理建议', prop: 'modificationSuggestions'},
           {name: '', prop: 'toolsKinds'},
         ],
         tableData: [],
         //元模型对象
         modelObj:{},
         //元模型动态属性
         cluList:[],
         notNullList:[],
         nullList:[],
         ownerList:[],//维护单位列表
         typeList:[],//问题类型列表
         resourceList:[],//资源类型列表
         modelId:'',//资源类型
         //问题数据列表
         pageSize: 15,
         pageIndex: 1,
         total: 0,
         cellList: [], // 单元格数组
         count: null, // 计数
         // 遮罩层
         loading: false,
         //搜索条件
         searchForm: {
           siteName:'',
           ownerId:'',
           bRuleId:'',
           type:'',
           quesType:''
         },
         //编辑后选中数据
         siteId:'',
         siteName:'',
         editForm:{},
         addErrForm:[],
         rulesForm:{},
         //引导模块
         //引导的页数
         guidePageTotal:1,
         guideCurrebtPage:1,
         title:'',
         openGuide:false,
         guideForm:{},
         guideList:[],
         seeErr:true,
          //修改
         editStationDrawer:false,
      }
    },
    props:{
      tableName:String
    },
    watch: {
      //解决操作栏固定后表格错位问题
      'tableData': {
        handler() {
          this.$nextTick(() => {
            this.$refs.multipleTable.doLayout();
          })
        },
        deep: true
      },
    },
    created() {
      this.getCommonData()
      this.getTableData()
    },
    methods: {
      getColSize (val, titVal) {
        return getColSize(val, titVal)
      },
      //获取下拉框公共数据
      getCommonData(){
        //获取站点表基本信息
        getModelId({tableName:this.tableName}).then(res=>{
          this.modelObj = res.data
          //获取动态属性
          this.getCluList()
        })
        //获取维护单位
        selectCompany().then(res => {
          this.ownerList=res.data

        });
        //获取问题种类
        selectType().then(res => {
          this.typeList=res.data
        });
        //资源类型
        selectResource().then(res => {
          this.resourceList=res.data
          this.modelId=this.resourceList[0].code
        });
      },
      //动态获取站点页面搜索条件与列名
      getCluList(){
        this.cluList = []
        this.notNullList=[]
        this.nullList=[]
        initgriddatasite(this.modelObj).then(res=>{
          this.cluList = res.data.columns
          //获取表格下拉框数据
          tableDropDownBox(this.modelObj).then(res=>{
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
            //获取所有属性初始化规则
            this.getAllRules()
          })
        })
      },
      //获取所有属性初始化规则
      getAllRules(){
        selectAllRules().then(res=>{
          res = res.data
          for (let i = 0; i < res.length; i++) {
            for (let j = 0; j < this.cluList.length; j++) {
              if(res[i].errorColumnNameCn===this.cluList[j].attributeNameCn){
                if(this.cluList[j].rules===undefined){
                  this.cluList[j].rules=res[i].ruleDescription + '<br/>'
                }else{
                  this.cluList[j].rules+=res[i].ruleDescription + '<br/>'
                }
              }
            }
          }
          //循环获取必填与非必填数组，是否必填1是，0否
          for (let i = 0; i < this.cluList.length; i++) {
            if(this.cluList[i].isNotNull===1||this.cluList[i].isNotNull==='1'){
              this.notNullList.push(this.cluList[i])
            }else{
              this.nullList.push(this.cluList[i])
            }
          }
        })
      },
      //获取表格数据
      getTableData() {
        this.loading = true
        this.tableData = []
        this.cellList=[] // 单元格数组
        this.count=null// 计数
        let param={
          siteName:this.searchForm.siteName,
          ownerId:this.searchForm.ownerId,
          bRuleId:this.searchForm.bRuleId,
          columnName:this.searchForm.quesType,
          pageSize:this.pageSize,
          pageNum:this.pageIndex,
          modelId:this.modelId
        }
        if(this.searchForm.bRuleId!==''){
          for(let i=0;i<this.typeList.length;i++){
            if(this.searchForm.bRuleId===this.typeList[i].code){
              param.type=this.typeList[i].type
            }
          }
        }
        //请求方法获取tableData数据
        getList(param).then(res => {
          this.tableData = res.rows
          this.total = parseInt(res.total)
          //对表格数据进行合并
          this.computeCell(this.tableData);
          this.loading = false
        });
      },
      computeCell(tableBody) {
        // 循环遍历表体数据
        for (let i = 0; i < tableBody.length; i++) {
          if (i == 0) {
            // 先设置第一项
            this.cellList.push(1); // 初为1，若下一项和此项相同，就往cellList数组中追加0
            this.count = 0; // 初始计数为0
          } else {
            // 判断当前项与上项的设备类别是否相同，因为是合并这一列的单元格
            if (tableBody[i].siteId == tableBody[i - 1].siteId) {
              // 如果相等
              this.cellList[this.count] += 1; // 增加计数
              this.cellList.push(0); // 相等就往cellList数组中追加0
            } else {
              this.cellList.push(1); // 不等就往cellList数组中追加1
              this.count = i; // 将索引赋值为计数
            }
          }
        }
      },
      // 第2步，将计算好的结果返回给el-table，这样的话表格就会根据这个结果做对应合并列渲染
      objectSpanMethod({row, column, rowIndex, columnIndex}) {
        // 给第二列做单元格合并。0是第一列，1是第二列。
        if (columnIndex === 1||columnIndex === 2||columnIndex === 3||columnIndex === 4) {
          const rowCell = this.cellList[rowIndex];
          if (rowCell > 0) {
            const colCell = 1;
            return {
              rowspan: rowCell,
              colspan: colCell,
            };
          } else {
            // 清除原有的单元格，必须要加，否则就会出现单元格会被横着挤到后面了！！！
            // 本例中数据是写死的不会出现，数据若是动态后端获取的，就会出现了！！！
            return {
              rowspan: 0,
              colspan: 0,
            };
          }
        }
      },
      /* 格式序列化列 */
      snFormatter: function (row, col, value, index) {
        return this.pageSize * (this.pageIndex - 1) + index+1;
      },
      /*搜索条件重置*/
      resetSearch(){
        this.searchForm = {
          siteName:'',
          ownerId:'',
          bRuleId:'',
          type:'',
        }
      },
      //根据id
      /*引导，获取引导数据*/
      getHelpById(id,name){
        if(id===null||id===undefined){
          this.$message({
            message: '无法获取站点id，请核实数据！',
            type: 'danger',
            center: true
          })
        }else{
          guide({siteId:id}).then(res => {
            this.guideCurrebtPage=1
            this.siteId=id
            this.siteName=name
            this.getObjById(id)
            this.guidePageTotal=parseInt(res.total % 2 == 0 ? res.total / 2 : res.total / 2 + 1 );
            this.title=name+" 维护引导，步骤"+this.guideCurrebtPage+'（共'+ this.guidePageTotal+'步）'
            this.getGuideForm(1)
          });
        }

      },
      /*上一步，下一步，完成*/
      nextGuide(type){
        //type:0--上一步；1--完成；2--下一步
        if(type===0){
          this.getGuideForm(this.guideCurrebtPage-1)
        }
        else if(type===1) {
          this.saveGuide(1)
        }
        else if(type===2) {
          this.saveGuide(2)

        }
      },
      //根据id获取单个对象
      getObjById(id){
        this.editForm={}
        let params={
          modelId:this.modelId,
          isPage:'0',
          whereAttributes:[{attributeName:'ID',attributeValue:id,isPrimaryKey:'1'}]
        }
        getForm(params).then(res=>{
          this.editForm=res.data
        })
      },
      /* 获取引导信息*/
      getGuideForm(page){
        this.guideForm={}
        this.guideList=[]
        this.seeErr = false
        this.guideCurrebtPage=page
        this.addErrForm=[]
        this.rulesForm={}
        guide({siteId:this.siteId,pageNum:page}).then(res => {
          res=res.rows
          for (let i = 0; i < res.length; i++) {
            for (let j = 0; j < this.cluList.length; j++) {
              if(res[i].name===this.cluList[j].attributeNameUnderline){
                this.guideList.push(this.cluList[j])
                this.$set(this.guideForm,this.cluList[j].attributeName,this.editForm[this.cluList[j].attributeName]===null?'':this.editForm[this.cluList[j].attributeName])
              }
            }
          }
          for (let i = 0; i < this.guideList.length; i++) {
            this.checkAdd(this.guideList[i].attributeNameUnderline,this.guideForm[this.guideList[i].attributeName])
          }
          this.seeErr = true
          this.openGuide = true
          this.title=this.siteName+" 维护引导，步骤"+page+'（共'+ this.guidePageTotal+'步）'
        });

      },
      /*保存引导信息*/
      saveGuide(type) {
        for (let i = 0; i < this.guideList.length; i++) {
          this.checkAdd(this.guideList[i].attributeNameUnderline,this.guideForm[this.guideList[i].attributeNameUnderline])
        }
        setTimeout(() => {
          if(this.addErrForm.length>0){
            this.$message({
              message: '请修改错误信息',
              type: 'error',
              center: true,
              duration:0,
              showClose:true
            })
            return
          }
          this.guideForm.ID=this.editForm.ID
          let params={
            modelId:this.modelId,
            isPage:'0',
            valueAttributes:[]
          }
          for(let key in this.guideForm){
            let obj={}
            if(this.guideForm[key]!==''&&this.guideForm[key]!==undefined){
              obj={
                attributeName:key,
                attributeValue:this.guideForm[key]
              }
              if(key==='ID'){obj.isPrimaryKey=1}
              params.valueAttributes.push(obj)
            }
          }

          submitEdit(params).then(res=>{
            this.deletGui()
            if(type===1){
              this.openGuide = false
              this.$message({
                message: res.msg,
                type: 'success',
                center: true
              })
            }else{
              this.getGuideForm(this.guideCurrebtPage+1)
            }
          })
        }, 1000);

        /**/
      },
      //校验名字
      checkAdd(column,value) {
        let param={
          colunmName:column,
          modelId:this.modelId
        }
          getColunmId(param).then(response=>{
            this.columnId = response.msg
            this.getRules(column,this.columnId,value)
          })

      },
      getRules(column,columnId,value) {
        //删除addErrForm中原来的错误数据，
        let arr=[]
        for (let i = 0; i < this.addErrForm.length; i++) {
          if(this.addErrForm[i].errorColumnName!==column){
            arr.push(this.addErrForm[i])
          }
        }
        this.addErrForm=arr
        this.rulesForm[column]=''
        let param={
          commId: columnId,
          modelId: this.modelId,
          param: value
        }
        getVerrules(param).then(response=>
        {
          this.seeErr=false
          if(response.data !== []) {
            let str = ''
            for(var i = 0;i<response.data.length;i++){
              //将错误数据保存到addErrForm中
              this.addErrForm.push(response.data[i])
              str += response.data[i].ruleDescription + '<br/>'
            }
            this.rulesForm[column] = str
            this.$set(this.rulesForm,column,this.rulesForm[column])
          }else{
            this.rulesForm[column] = undefined
          }
          this.seeErr=true
        }).catch(e => {

        })
      },
      //打开修改
      editStation({row}){
        if(row.siteId===null||row.siteId===undefined){
          this.$message({
            message: '无法获取站点id，请核实数据！',
            type: 'danger',
            center: true
          })
          return
        }
        this.rulesForm={}
        this.editForm={}
        this.addErrForm=[]
        let params={
          modelId:this.modelId,
          isPage:'0',
          whereAttributes:[{attributeName:'ID',attributeValue:row.siteId,isPrimaryKey:'1'}]
        }
        getForm(params).then(res=>{
          this.editForm=res.data
          for (let i = 0; i < this.notNullList.length; i++) {
            this.checkAdd(this.notNullList[i].attributeNameUnderline,this.editForm[this.notNullList[i].attributeNameUnderline])
          }
          for (let i = 0; i < this.nullList.length; i++) {
            this.checkAdd(this.nullList[i].attributeNameUnderline,this.editForm[this.nullList[i].attributeNameUnderline])
          }
          this.editStationDrawer = true
        })
      },
      //修改提交
      submitEditForm() {
        for (let i = 0; i < this.notNullList.length; i++) {
          this.checkAdd(this.notNullList[i].attributeNameUnderline,this.editForm[this.notNullList[i].attributeNameUnderline])
        }
        for (let i = 0; i < this.nullList.length; i++) {
          this.checkAdd(this.nullList[i].attributeNameUnderline,this.editForm[this.nullList[i].attributeNameUnderline])
        }
        setTimeout(() => {
          this.$confirm('是否保存此站点?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            let params={
              modelId:this.modelId,
              isPage:'0',
              valueAttributes:[]
            }
            for(let key in this.editForm){
              let obj={}
              if(this.editForm[key]!==''&&this.editForm[key]!==undefined){
                obj={
                  attributeName:key,
                  attributeValue:this.editForm[key]
                }
                if(key==='ID'){obj.isPrimaryKey=1}
                params.valueAttributes.push(obj)
              }
            }
            submitEdit(params).then(response => {
                this.editStationDrawer = false
                this.saveErrorStation(this.editForm.ID)
                this.$modal.msgSuccess("操作成功");

              }
            ).catch((e) => {
              this.$message({
                message: e,
                type: 'error',
                center: true,
                duration:0,
                showClose:true
              })
            })
          })
        }, 1500);
      },
      // 保存错误站点传回
      saveErrorStation(id) {
        if(this.addErrForm.length >0) {
          for(var i=0;i<this.addErrForm.length;i++){
            this.addErrForm[i].errorId=id
          }
          errorStation(this.addErrForm).then(response =>{
            this.getTableData()
          })
        }
      },
      deletGui(){
        let names=[]
        for(let i=0;i<this.guideList.length;i++){
          names.push(this.guideList[i].attributeNameUnderline)
        }
        let str='?siteId='+this.editForm.ID+'&names='+names
        deleteRule(str).then(res => {
          this.getTableData()
        })
      },
      saveRule() {
        this.$confirm('是否进行手动校验', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          handRules().then(res=>{
            this.$message({
              message: res.msg,
              type: 'success',
              center: true
            })
          })
          this.handRule()
        })
      },
      handRule(){
        handRules1().then(res=>{
          this.$message({
            message: res.msg,
            type: 'success',
            center: true
          })
        })
      },
    }
  };
</script>
<style>
  #errData.searchBox .el-input{
    width: 12rem;
  }
  .error .el-input__inner {
      border: red 1px solid;
   }
</style>
