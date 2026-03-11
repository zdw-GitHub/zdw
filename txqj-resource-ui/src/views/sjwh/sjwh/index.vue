<template>
  <el-card>
    <div id="sjwh">
      <el-table
        ref="multipleTable"
        :data="tableData"
        tooltip-effect="dark"
        height="660px"
        style="width: 100%;margin-top: 10px;"
        v-loading="loading">
        <el-table-column
          label="序号"
          width="80"
          align="center"
          :formatter="SnFormatter"
        ></el-table-column>
        <el-table-column v-for="item in cluList"
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
        <el-table-column
          v-if="ifShow==true"
          label="操作"
          width="180"
          fixed="right"
          align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-edit" @click="openEdit(scope)" v-hasPermi="['datatask:b:update']">编辑</el-button>
            <!--<el-button size="mini" type="text" icon="el-icon-edit" @click="openEdit(scope)" v-hasPermi="['system:b:update']">编辑</el-button>-->
            <el-button size="mini" type="text" icon="el-icon-s-help" plain @click="searchStation(scope)" v-hasPermi="['system:b:list']">查看</el-button>
            <el-button size="mini" type="text" icon="el-icon-delete" @click="delData(scope)" v-hasPermi="['datatask:b:remove']">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页器 -->
      <div style="text-align: right;margin-top: 5px" v-if="taskId!=='undefined'">
        <pagination
          :total="total"
          :page.sync="pageIndex"
          :limit.sync="pageSize"
          @pagination="getTableData"
        />
      </div>

      <!-- 修改属性对话框 -->
      <el-dialog
        v-dialogDrag
        title="修改"
        :visible.sync="editStationDrawer"
        width="750px"
        append-to-body
        style="margin-top: 5.5vh !important;"
        :close-on-click-modal="false">
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
                  <span style="font-size:10px;color:red;" v-if="showAdd" v-show="rulesForm[item.attributeNameUnderline]!==undefined&&rulesForm[item.attributeNameUnderline]!==''" v-html="rulesForm[item.attributeNameUnderline]"></span>
                  <span style="font-size:10px;color:grey;" v-if="showAdd" v-show="rulesForm[item.attributeNameUnderline]===undefined" v-html="item.rules"></span>
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
                <el-select v-if="item.attType==='select'" clearable filterable @change="checkAdd(item.attributeNameUnderline,editForm[item.attributeName])" v-model="editForm[item.attributeName]"
                           @blur="checkAdd(item.attributeNameUnderline,editForm[item.attributeName])"
                           :class="rulesForm[item.attributeNameUnderline] ===undefined||rulesForm[item.attributeNameUnderline]===''||rulesForm[item.attributeNameUnderline]===null?'':'error'">
                  <el-option
                    v-for="arr in item.list"
                    :label="arr.name"
                    :key="arr.code"
                    :value="arr.code"></el-option>
                </el-select>
                <el-date-picker
                  v-model="editForm[item.attributeName]"
                  @blur="checkAdd(item.attributeNameUnderline,editForm[item.attributeName])"
                  type="datetime"
                  :class="rulesForm[item.attributeNameUnderline] ===undefined||rulesForm[item.attributeNameUnderline]===''||rulesForm[item.attributeNameUnderline]===null?'':'error'"
                  v-else-if="item.attType==='datetime'"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  placeholder="选择日期时间">
                </el-date-picker>
                <el-input @blur="checkAdd(item.attributeNameUnderline,editForm[item.attributeName])" v-else v-model="editForm[item.attributeName]"
                          :class="rulesForm[item.attributeNameUnderline] ===undefined||rulesForm[item.attributeNameUnderline]===''||rulesForm[item.attributeNameUnderline]===null?'':'error'"></el-input>
                <div style="margin-left:210px;margin-top:-30px;width:300px">
                  <span style="font-size:10px;color:red;" v-if="showAdd" v-show="rulesForm[item.attributeNameUnderline]!==undefined&&rulesForm[item.attributeNameUnderline]!==''" v-html="rulesForm[item.attributeNameUnderline]"></span>
                  <span style="font-size:10px;color:grey;" v-if="showAdd" v-show="rulesForm[item.attributeNameUnderline]===undefined" v-html="item.rules"></span>
                </div>
              </el-form-item>
            </div>
          </el-form>
        </div>
        <div slot="footer" class="dialog-footer" style="margin-bottom:20px">
          <el-button type="primary" @click="submitEditForm()">确 定</el-button>
          <el-button @click="editStationDrawer=false">取 消</el-button>
        </div>
      </el-dialog>
      <!-- 查看属性对话框 -->
      <el-dialog
        v-dialogDrag
        title="查看"
        :visible.sync="stationDrawer"
        width="750px"
        append-to-body
        style="margin-top: 5.5vh !important;"
        :close-on-click-modal="false">
        <div class="el-dialog-div">
          <div class="formTitle" v-show="notNullList.length>0">
            <span>* 必填项</span>
          </div>
          <el-form class="seeForm" v-show="notNullList.length>0" ref="form" :model="editForm" label-width="100px" size="mini" style="margin-left:70px">
            <div v-for="item in notNullList">
              <el-form-item style="margin-top:30px"  :label="item.attributeNameCn">
                <el-select placeholder="" disabled v-if="item.attType==='select'" clearable filterable v-model="editForm[item.attributeNameUnderline]">
                  <el-option
                    v-for="arr in item.list"
                    :label="arr.name"
                    :key="arr.code"
                    :value="arr.code"></el-option>
                </el-select>
                <el-date-picker
                  v-model="editForm[item.attributeNameUnderline]"
                  disabled
                  type="datetime"
                  v-else-if="item.attType==='datetime'"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  placeholder="">
                </el-date-picker>
                <el-input disabled v-else v-model="editForm[item.attributeNameUnderline]"></el-input>
              </el-form-item>
            </div>
          </el-form>
          <div class="formTitle" v-show="nullList.length>0">
            <span>非必填</span>
          </div>

          <el-form class="seeForm" v-show="nullList.length>0" style="margin-left:70px" size="mini" label-width="100px" >
            <div v-for="item in nullList">
              <el-form-item style="margin-top:30px"  :label="item.attributeNameCn">
                <el-select placeholder="" disabled v-if="item.attType==='select'" clearable filterable v-model="editForm[item.attributeNameUnderline]" >
                  <el-option
                    v-for="arr in item.list"
                    :label="arr.name"
                    :key="arr.code"
                    :value="arr.code"></el-option>
                </el-select>
                <el-date-picker
                  v-model="editForm[item.attributeNameUnderline]"
                  disabled
                  type="datetime"
                  v-else-if="item.attType==='datetime'"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  placeholder="">
                </el-date-picker>
                <el-input disabled v-else v-model="editForm[item.attributeNameUnderline]"></el-input>
              </el-form-item>
            </div>
          </el-form>
        </div>
        <div slot="footer" class="dialog-footer" style="margin-bottom:20px">
          <el-button @click="stationDrawer=false">关 闭</el-button>
        </div>
      </el-dialog>
    </div>
  </el-card>

</template>

<script>
  import{getInfo,update,deltask,updateByTaskNo,listMiddle,getModeId} from "@/api/sjwh/sjwh";
  import {tableDropDownBox,selectAllRules,getColunmId,getVerrules,initgriddatasite} from "@/api/zdwh/zdzywh";
  import { getPrimaryKeyName} from "@/api/sj/audit";
  export default {
    name: "sjwh",
    data(){
      return {
        //表对象信息
        modelObj: {},
        // 遮罩层
        loading: false,
        //动态查询条件与列,必填与非必填
        cluList:[],
        searchList:[],
        nullList:[],
        notNullList:[],
        //表的分页
        pageSize: 15,
        pageIndex: 1,
        total: 0,
        tableData: [],
        filterText:'',
        multipleSelection: [],
        //查看
        stationDrawer:false,
        //修改
        editForm:{},
        editStationDrawer:false,
      //
        rulesForm:{},
        showAdd:true,
        addErrForm:[],
        list: [],
        taskId:'',
        keyName:'',
        ifShow:true
      }
    },
    updated() {
      this.$nextTick(() => {
        this.$refs.multipleTable.doLayout()
      })
    },
    watch: {
      //解决操作栏固定后表格错位问题
      'tableData':{
        handler(){
          this.$nextTick(()=>{
            this.$refs.multipleTable.doLayout();
          })
        },
        deep:true
      },
    },
    activated(){
      this.getCommonData()
      this.taskId=this.$route.query.taskId
      // this.getCommonData()

      // if(this.taskId) {
      //   this.getCommonData()
      // } else {
      //   this.getCommonData()
        // this.tableData =JSON.parse(localStorage.getItem('selectionList'))
        // this.list=this.tableData
        // this.total=this.tableData.length
      // }
    },
    created(){
      this.taskId=this.$route.query.taskId
      this.getCommonData()


      // console.log(this.$route.query.taskId)
      // if(this.taskId) {
      //   this.getCommonData()
      // } else {
      //   this.getCommonData()
      //
      // }
    },
    methods:{
      getKey() {
        let params = {modeId: this.modelObj.modelId}
        getPrimaryKeyName(params).then(res=>{
          res=res.msg
          this.keyName = res
        });
      },
      //动态获取页面搜索条件与列名
      getCluList(){
        this.cluList = []
        // this.searchList = []
        this.notNullList=[]
        this.nullList=[]
          //获取表格下拉框数据
        initgriddatasite(this.modelObj).then(res=>{
          this.cluList = res.data.columns
          this.searchList = res.data.searchAttributes
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
           this.getTableData()
        })
      },
      getCommonData(){
      //   //获取表基本信息
        if(this.taskId=="undefined"){
          this.ifShow=false
          this.modelObj.modelId=localStorage.getItem('modelId')
          this.modelObj.modelVersion='edit'
          this.tableData =JSON.parse(localStorage.getItem('selectionList'))
          this.list=this.tableData
          this.total=this.tableData.length
          this.getCluList()
          // this.getKey()

        }else{
          this.ifShow=true
          getModeId(this.taskId).then(res=>{
            this.modelObj.modelId = res.msg
            this.modelObj.modelVersion='edit'
            this.getCluList()
            this.getKey()
          })
        }
      },
      //获取表格数据
      getTableData() {
        let params={
          modelId:this.modelObj.modelId,
          isPage:'1',
          taskNo:this.$route.query.taskId,
          pageIndex: this.pageIndex,
          pageSize: this.pageSize,
          whereAttributes:[]
        }
        if(this.taskId=="undefined"){
          this.ifShow=false
          this.tableData =JSON.parse(localStorage.getItem('selectionList'))
          this.list=this.tableData
          this.total=this.tableData.length
        }else{
          this.loading = true;
          this.ifShow=true
          listMiddle(params).then(res=>{
            this.tableData = res.rows;
            this.total = res.total;
            this.loading = false;
          }).catch(() => {
            this.loading = false
          });
        }
      },
      /* 格式序列化列 */
      SnFormatter: function (row, col, value, index) {
        return this.pageSize * (this.pageIndex - 1) + index + 1;
      },
      //获取所有属性的规则
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
          this.notNullList=[]
          this.nullList=[]
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
      //校验名字
      checkAdd(column,value) {
        let param = {
          colunmName: column,
          modelId: this.modelObj.modelId
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
          modelId: this.modelObj.modelId,
          param: value
        }
        getVerrules(param).then(response=>
        {
          this.showAdd=false
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
          this.showAdd=true
        }).catch(e => {

        })
      },
      //打开修改
      openEdit({row}){
        this.rulesForm={}
        this.editForm={}
        this.addErrForm=[]
        let params={
          modelId:this.modelObj.modelId,
          isPage:'0',
          taskNo:this.$route.query.taskId,
          whereAttributes:[{attributeName:this.keyName,attributeValue:row[this.keyName],isPrimaryKey:'1'}]
        }
          getInfo(params).then(res=>{
            this.editForm=res.data
            // console.log(this.editForm)
          })
        for (let i = 0; i < this.notNullList.length; i++) {
          this.checkAdd(this.notNullList[i].attributeNameUnderline,this.editForm[this.notNullList[i].attributeNameUnderline])
        }
        for (let i = 0; i < this.nullList.length; i++) {
          this.checkAdd(this.nullList[i].attributeNameUnderline,this.editForm[this.nullList[i].attributeNameUnderline])
        }
        this.editStationDrawer = true

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
          this.$confirm('是否保存此数据?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            let params={
              modelId:this.modelObj.modelId,
              isPage:'0',
              taskNo:this.$route.query.taskId,
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
            update(params).then(response => {
                this.editStationDrawer = false
                this.$modal.msgSuccess("操作成功");
                this.getTableData()
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
      //查看
      searchStation({row}){
        this.stationDrawer = true
        this.editForm = []
        let params={
          modelId:this.modelObj.modelId,
          isPage:'0',
          taskNo:this.$route.query.taskId,
          whereAttributes:[{attributeName:this.keyName,attributeValue:row[this.keyName],isPrimaryKey:'1'}]
        }
        getInfo(params).then(res=>{
          this.editForm=res.data
        })
      },
      //删除单条数据
      delData(scope){
        this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
            let params = {
              modelId: this.modelObj.modelId,
              valueAttributes:[{
                attributeName:'AUDIT_STATUS',
                attributeValue:'0'
              }],
              whereAttributes: [{
                attributeName: this.keyName,
                attributeValue: scope.row[this.keyName],
              }]
            }
            update(params).then(response =>{
              let str='?taskNo='+this.taskId+'&id='+scope.row[this.keyName]
                updateByTaskNo(str).then(res =>{

                })
                this.$message({
                  message: response.msg,
                  type: 'success',
                  center: true
                })
                this.getTableData()
              }
            ).catch(() => {
              this.getTableData()
            })

        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
      },
    }

  }
</script>

<style>
  #sjwh .label_query{
    width: 100%;
    margin-bottom: 10px;
  }
  #sjwh .label_query .left_tit{
    width: 100%;
    height:24px;
    display: flex;
    border-bottom:1px solid #E9E9E9;
  }
  #sjwh .label_query .left_tit .left_tit_con{
    flex: 1;
    font-size:14px;
    font-weight: 600;
    color:rgba(0,0,0,0.85) ;
    line-height: 16px;
  }
  #sjwh .label_query .left_tit .left_tit_opa{
    font-size:12px;
    font-weight: 600;
    color:#047E78 ;
    line-height: 20px;
  }
  #sjwh .label_query .right_con{
    width: 100%;
    margin-top: 15px;
  }
  #sjwh .label_query .right_con .el-form-item__label{
    font-size: 14px;
    font-weight: 400;
    color: rgba(0,0,0,0.85);
  }
  #sjwh .label_query .right_con .right_item{
    display: flex;
    border-bottom:2px solid #fff;
    padding: 2px 5px;
  }
  #sjwh .label_query .right_con .right_item .itemTit{
    font-size: 14px;
    font-weight: 400;
    color: rgba(0,0,0,0.65);
    line-height: 30px;
  }
  #sjwh .label_query .right_con .right_item .itemCon{
    flex:1;
    margin: 0 10px 0 20px;
    overflow: hidden;

  }
  #sjwh .label_query .right_con .right_item .itemCon .el-radio__input{
    display: none
  }
  #sjwh .label_query .right_con .right_item .itemCon .el-radio__label,
  #sjwh .label_query .right_con .right_item .itemCon .el-checkbox__label{
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
    color: #047E78;
    background: #e6f2f2;
    border: 1px solid #9bcbc9
  }
  #sjwh .label_query .right_con .right_item .itemCon .el-checkbox-group,
  #sjwh .label_query .right_con .right_item .itemCon .el-radio-group
  {
    float: left;
  }
  #sjwh .label_query .right_con .right_item .itemOpa{
    float: left;
    margin-top: 5px;
  }
  #sjwh .searchBox .el-form-item .el-form-item__content .el-input,
  #sjwh .searchBox .el-form-item .el-form-item__content .el-input .el-input_inner{
    width: 15rem;
  }

  .error .el-input__inner {
    border: red 1px solid;
  }
  .center .el-input {
    width:170px
  }
  .center .el-select {
    width:170px
  }
  .red{
    color:#047E78;
  }
  .box-card {
    overflow-y:auto
  }
</style>
