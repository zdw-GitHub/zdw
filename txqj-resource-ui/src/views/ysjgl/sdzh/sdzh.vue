<template>
  <div class="app-container">
  <!--树形结构-->
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
        </div>
      </el-col>
      <!--同步选择-->
      <el-col :span="20" :xs="24">
      <div style="display: flex;justify-content: space-between;">
        <div>
          <el-form :model="synSearchParams" ref="synSearchParams" size="small" :inline="true" >
            <el-form-item label="条件组名称" label-width="100px" >
              <el-input
                style="width: 250px"
                v-model="synSearchParams.conditionGroupName"
                placeholder="请输入转换同步条件组名称"
                clearable
                class="syn-input"
              ></el-input>
            </el-form-item>
          </el-form>
        </div>
        <div>
          <el-button type="info" plain icon="el-icon-search" size="mini" @click="getTableData">搜索</el-button>
          <el-button icon="el-icon-refresh" plain size="mini" @click="restSynSearch">重置</el-button>
          <el-button :disabled="addIsDisable" type="primary" plain icon="el-icon-plus" size="mini" @click="addSynCondition">新增</el-button>
          <el-button icon="el-icon-refresh-right" type="success" plain size="mini" :disabled="selectCode==''" @click="returnSynSearch">转换</el-button>
        </div>
      </div>
      <el-table height="60vh" v-loading="synLoading" :data="synList" ref="synList">
        <el-table-column label="选择" type="index" width="70" header-align="center" align="center">
          <template slot-scope="scope">
            <el-radio class="radio" v-model="selectCode" :label="scope.row.id"
                      @click.native.stop.prevent="getCurrentRow(scope.row)">{{&nbsp;}}</el-radio>
          </template>
        </el-table-column>
        <el-table-column label="序号" align="center" width="70" :formatter="synFormatter" />
        <el-table-column label="条件组名称" align="center" prop="conditionGroupName"/>
        <el-table-column label="创建时间" align="center" prop="createTime"/>
        <el-table-column label="更新时间" align="center" prop="updateTime"/>
        <el-table-column label="备注" align="center" prop="remark" />
        <el-table-column
          label="操作"
          width="230"
          align="center"
        >
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-s-help" @click="seeHandleForm(scope)">查看</el-button>
            <el-button size="mini" type="text" plain icon="el-icon-edit" @click="editHandleForm(scope)">编辑</el-button>
            <el-button size="mini" type="text" icon="el-icon-delete" @click="delsynData(scope)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
        <pagination
          v-show="total>0"
          :total="total"
          :page.sync="synSearchParams.pageIndex"
          :limit.sync="synSearchParams.pageSize"
          @pagination="getTableData"
        />
    </el-col>
    </el-row>
<!--    </el-dialog>-->
    <!--新增转换同步条件组-->
    <el-dialog
      :close-on-click-modal="false"
      :title="synTitle"
      :visible.sync="synShow"
      width="65%"
      @open="open"
      @close="close"
      append-to-body>
      <div>
        <el-form :inline="true" :model="addSynForm" ref="form" :rules="rules">
          <el-form-item label="条件组名称" label-width="100px" prop="conditionGroupName">
            <el-input :disabled="isDisabled" style="width: 300px;" v-model="addSynForm.conditionGroupName" placeholder="请输入条件组名称"></el-input>
          </el-form-item>
          <el-form-item label="备注" label-width="100px">
            <el-input :disabled="isDisabled" type="textarea" style="width: 540px" v-model="addSynForm.remark" :autosize="{ minRows: 3, maxRows: 3}"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <div class="content">
        <el-radio-group v-model="selection">
          <el-radio-button :disabled="noType" v-if="dataRelationType=='1'||dataRelationType=='3'" label="2">字段关联条件</el-radio-button>
          <el-radio-button :disabled="noType" v-if="dataRelationType=='2'||dataRelationType=='3'" label="3">sql关联条件</el-radio-button>
          <el-radio-button label="1">元模型同步条件</el-radio-button>
        </el-radio-group>
        <div v-if="selection=='1'" class="content_item">
          <div class="item">
            <el-col :span="9" >
              <div class="item_border">
                <el-container>
                  <el-header style="padding-left:10px; padding-right: 10px">
                    <el-tooltip class="item" effect="dark" :content="baseData.metaBase.content" placement="top">
                      <span class="tableName tableNameSpan">{{baseData.metaBase.content}}</span>
                    </el-tooltip>
                    <!--<span class="tableName">{{baseData.metaBase.tableName}}({{baseData.metaBase.tableNameCn}})</span>-->
                  </el-header>
                  <el-main>
                    <el-input v-model="baseData.filterate" placeholder="请输入属性名称进行过滤"></el-input>
                    <el-table
                      :data="filteredTableData"
                      height="380"
                      border
                      style="width: 100%">
                      <el-table-column
                        label="序号"
                        align="center"
                        width="50">
                        >
                        <template slot-scope="scop">
                          {{scop.$index+1}}
                        </template>
                      </el-table-column>
                      <el-table-column
                        prop="columnName"
                        label="属性名称"
                        >
                      </el-table-column>
                      <el-table-column
                        prop="columnNameCn"
                        label="中文名">
                      </el-table-column>
                    </el-table>
                  </el-main>
                </el-container>
              </div>
            </el-col>
            <el-col :span="14">
              <div class="item_border">
                <el-container>
                  <el-header>
                    <span class="tableName">元模型同步查询条件 (where)</span>
                    <span>
                      <el-button type="success" @click="testSql">测试SQL</el-button>
                      <el-button :disabled="isDisabled" type="text" @click="clearTextarea">清 空</el-button>
                    </span>
                  </el-header>
                  <el-main>
                    <el-input
                      type="textarea"
                      :rows="18"
                      :disabled="isDisabled"
                      placeholder="请输入元模型同步查询sql语句where"
                      v-model="sqlGroup.conditon_type_meta_model"
                      clearable
                      >
                    </el-input>

                  </el-main>
                </el-container>
              </div>
            </el-col>
          </div>
        </div>
        <div v-if="selection=='2'" class="content_item">
          <div class="item">
            <el-col :span="9" >
              <div class="item_border">
                <el-container>
                  <el-header style="padding-left:10px; padding-right: 10px">
                    <span class="tableName">属性关联表</span>
                  </el-header>
                  <el-main>

                    <el-collapse v-model="activeNames" @change="activeNamesChange">
                      <!--表格1-->
                        <el-collapse-item :name="item.relationId" v-for="item in baseData.fieldBase">
                          <div slot="title" class="floadStyle">
                            <span class="floadStyleLeft">{{item.relationTableName}}({{item.relationTableAlias}})</span>
                          </div>
                        <el-input v-model="keyword" placeholder="请输入属性名称进行过滤"></el-input>
                        <el-table
                          :data="filterField(item.data)"
                          border
                          height="255"
                          style="width: 100%">
                          <el-table-column
                            label="序号"
                            align="center"
                            width="50">
                          >
                            <template slot-scope="scop">
                              {{scop.$index+1}}
                            </template>
                          </el-table-column>
                          <el-table-column
                            prop="columnName"
                            label="属性名称"
                          >
                          </el-table-column>
                          <el-table-column
                            prop="columnNameCn"
                            label="中文名">
                          </el-table-column>
                        </el-table>
                      </el-collapse-item>
                    </el-collapse>

                  </el-main>
                </el-container>
              </div>
            </el-col>
            <el-col :span="14">
              <div class="item_border">
                <el-container>
                  <el-header>
                    <span class="tableName">字段关联查询条件 (where)</span>
                    <span>
                      <el-button type="success" @click="testSql">测试SQL</el-button>
                      <el-button :disabled="isDisabled" type="text" @click="clearTextarea">清 空</el-button>
                    </span>
                  </el-header>
                  <el-main>
                    <el-input
                      type="textarea"
                      :rows="18"
                      :disabled="isDisabled"
                      placeholder="请输入字段关联条件sql语句where"
                      v-model="sqlGroup.conditon_type_attribute_transformation"
                      clearable
                    >
                    </el-input>
                  </el-main>
                </el-container>
              </div>
            </el-col>
          </div>
        </div>
        <div v-if="selection=='3'" class="content_item">
          <div class="item" style="height: 51%">
            <el-col :span="24" >
              <div class="item_border">
                <el-container>
                  <el-header>
                    <span class="tableName">元数据关联sql</span>
                  </el-header>
                  <el-main style="height:21vh">
                    <el-input
                      class="inputcolor"
                      type="textarea"
                      :rows="18"
                      placeholder=""
                      v-model="baseData.sql"
                      clearable
                      disabled
                    >
                    </el-input>
                  </el-main>
                </el-container>
              </div>
            </el-col>
          </div>
          <div class="item" style="height: 51%">
            <el-col :span="24" >
              <div class="item_border">
                <el-container>
                  <el-header>
                    <span class="tableName">sql关联查询条件</span>
                    <span>
                      <el-button type="success" @click="testSql">测试SQL</el-button>
                      <el-button :disabled="isDisabled" type="text" @click="clearTextarea">清 空</el-button>
                    </span>
                  </el-header>
                  <el-main style="height:21vh">
                    <el-input
                      type="textarea"
                      :rows="18"
                      placeholder="请输入sq关联查询sql语句"
                      v-model="sqlGroup.conditon_type_sql_transformation"
                      clearable
                      :disabled="isDisabled"
                    >
                    </el-input>
                  </el-main>
                </el-container>
              </div>
            </el-col>
          </div>
        </div>

      </div>
      <div v-if="synTitle=='条件组查看'" slot="footer" class="dialog-footer">
        <el-button @click="close">关 闭</el-button>
      </div>
      <div v-else slot="footer" class="dialog-footer">
        <el-button @click="close">取 消</el-button>
        <el-button type="primary" @click="determine">确 定</el-button>
      </div>
    </el-dialog>
<!--点击测试SQL出来的弹窗-->
    <el-dialog
      title="测试SQL"
      :visible.sync="testSQLshow"
      width="60%"
      :close-on-click-modal="false"
      append-to-body
     >
      <div class="test_container">
        <div class="testSql">
          <el-input disabled type="textarea" v-model="testData.sql" :autosize="{minRows: 2, maxRows: 6}" class="input-with-scroll"></el-input>
        </div>
        <div class="table" style="margin-top: 10px" v-if="testData.code=='success'">
          <el-table
            :data="testData.data"
            height="95%"
            style="width: 100%">
            <el-table-column
              v-for="item in tableColumns"
              :label="item.label"
              :prop="item.prop"
              min-width="100"
            >
            </el-table-column>
          </el-table>
          <!--<pagination-->
            <!--v-show="testtotal>0"-->
            <!--:total="testtotal"-->
            <!--:page.sync="test.pageIndex"-->
            <!--:limit.sync="test.pageSize"-->
            <!--@pagination="testSql"-->
          <!--/>-->
        </div>
        <div class="table" v-else style="margin-top: 10px">
          <div style="color: red">{{testData.error}}</div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="testSQLshow = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
    import {sjyTree} from "@/api/ysjgl/sjcx";
    import {relationtype,groupList,modelShow,metaData,getSql,getRetable,columnList, addConditionGroup,addCondition,delConditionGroup,
      ConditionList,delCondition,editConditionGroup,editCondition,syncData,testSQL} from "@/api/ysjgl/sdzh"

    export default {
        name: "sdzh",
      data(){
          return {
            addIsDisable:true,
            //元模型信息
            versionObj:{},
            /**转换同步 */
            //  同步条件选择遮盖层
            selectSynShow:true,
            //搜索
            synSearchParams:{
              conditionGroupName:'',
              pageSize:10,
              pageIndex:1
            },
            total:0,
            //加载
            synLoading:false,
            //同步选择表格
            synList:[],
            //  单选code
            selectCode:'',
            //  选择存储数据
            selectData:{},
            //  新增、查看、修改对话框标题
            synTitle:'',
            synShow:false,
            //关联方式 1字段关联 2sql关联 3混合关联
            dataRelationType:'3',
            noType:false,
            //按钮选项
            selection:"1",
            addSynForm:{
              conditionGroupName: '',
              remark: '',
            },
            //shuju
            baseData:{
              metaBase:{},//元模型的基本信息（中文名，表名）
              metaTableData: [],
              metaFilterate:'',//过滤数据
              sql:'',
              fieldBase:[],
            },
            keyword:'',
            //填写的sql
            sqlGroup:{
              conditon_type_meta_model:'',
              conditon_type_attribute_transformation:'',
              conditon_type_sql_transformation:''
            },
            rules: {
              conditionGroupName: [
                {required: true, message: '请输入条件组名称', trigger: 'change'}
              ],
            },
            //选中数据
            dbName:'',
            // 树选项
            data: [{
              id: '0',
              label: '元模型',
              icon: 'el-icon-s-home',
              color: 'color: #FFC000',
              children: []
            }],
            idArr:[],
            //元模型同步条件
            textarea: '',
            //字段关联条件
            textareaMatch: '',
            //SQL关联条件
            textareaSQL: '',
            //点击测试SQL出来的弹窗
            testSQLshow:false,
            //字段关联条件的折叠表格
            activeNames: [],
          //条件组下的条件
            oneGroup:[],
            ids:'',
          //  不可更改
            isDisabled:false,
            test:{
              pageIndex:1,
              pageSize:10
            },
            testtotal:0,
            testData:[],
            tableColumns:[]
          }
      },
      watch:{
        // 根据名称筛选树
        dbName(val) {
          this.$refs.tree.filter(val);
          if(val==''){
            this.getSjyTree();
          }
        }
      },
      computed: {
          //元模型同步条件的表格
        filteredTableData() {
          // 根据输入框的值进行过滤
          return this.baseData.metaTableData.filter(item =>{
              return item.columnName.includes(this.baseData.metaFilterate) || item.columnNameCn.includes(this.baseData.metaFilterate)
            }
          ) ;
        },
        filterField(){
          return data => {
            if (!this.keyword) {
              return data // 没有关键字直接返回原数据
            }
            return data.filter(row => {
              return Object.values(row).some(val => {
                return String(val).includes(this.keyword)
              })
            })
          }
        },
      },
      created() {
        this.activeNames = [];
        this.synLoading=true
        this.getSjyTree();
      },
      methods:{
        //  格式化
        synFormatter: function (row, col, value, index) {
          return this.synSearchParams.pageSize * (this.synSearchParams.pageIndex - 1) + index + 1;
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
          this.synList=[]
          this.dataRelationType='3'
          this.synSearchParams={
            conditionGroupName:'',
              pageSize:10,
              pageIndex:1
          }
          this.total=0
          if (res.type === 'versionT') {
            this.addIsDisable=false
            this.versionObj=res
            this.getRelationType();
            this.getTableData();
            this.modelShow()
          }
        },
        //获取表的关联方式
         getRelationType(){
           relationtype({modelId: this.versionObj.parentId,modelVersion:this.versionObj.label}).then(res=>{
            if(res.msg){
              this.dataRelationType=res.msg
              if(this.dataRelationType=='1'){
                this.selection="2"
              } else if(this.dataRelationType=='2'){
                this.selection="3"
              }else{
                this.selection="2"
              }
              this.noType=false
            }else{
              this.selection="1"
              this.noType=true
            }
          })
        },
        //获取表的所有条件组
        getTableData(){
          this.synLoading=true
          this.selectCode=""
          this.selectData={}
          this.synSearchParams.metaModelId=this.versionObj.parentId
          this.synSearchParams.metaModelVersion=this.versionObj.label
          groupList(this.synSearchParams).then(res=>{
            if(res.code==200){
              this.total=res.total
              this.synList=res.rows
              this.synLoading=false
            }
          })
        },
        //获取元数据的基本信息（表名，中文名等）
        modelShow(){
          modelShow({modelId: this.versionObj.parentId,modelVersion:this.versionObj.label}).then(res=>{
            this.baseData.metaBase.tableName=res.data.tableName
            this.baseData.metaBase.tableNameCn=res.data.tableNameCn
            this.baseData.metaBase.content=res.data.tableName+'('+res.data.tableNameCn+')'
          })
        },
        //对话框打开时调取的事件
        open(){
          this.metaDataTable()
          this.getSql()
          this.getRetable()
        },
        //获取元模型的属性表
        metaDataTable(){
          metaData({modelId: this.versionObj.parentId}).then(res=>{
            this.baseData.metaTableData=res.data
          })
        },
        //获取sql关联
        getSql(){
          getSql({modelId: this.versionObj.parentId,modelVersion:this.versionObj.label}).then(res=>{
            this.baseData.sql=res.data
          })
        },
        //获取字段关联表
        getRetable(){
          getRetable({modelId: this.versionObj.parentId,modelVersion:this.versionObj.label}).then(res=>{
            if(res.length>0){
              this.baseData.fieldBase=res.filter(item => item.hasOwnProperty('relationId'))
              this.columnList()
            }
          })
        },
        //根据字段关联表id获取表属性
        columnList(){
          for(let i=0;i<this.baseData.fieldBase.length;i++){
            columnList({tableId:this.baseData.fieldBase[i].relationTableId}).then(res=>{
              this.baseData.fieldBase[i].data=res.data
            })
          }
        },
        /** 转换同步*/
        //  重置
        restSynSearch(){
          this.synSearchParams={
            metaModelId:this.versionObj.parentId,
            metaModelVersion:this.versionObj.label,
            conditionGroupName:'',
            pageSize:10,
            pageIndex:1
          }
          this.selectCode=""
          this.selectData={}
          this.getTableData()
        },
        //转换
        returnSynSearch(){
          this.$modal.confirm('是否进行表格数据转换同步操作？').then(() => {
            this.synLoading = true
            this.params = {
              modelId: this.versionObj.parentId,
              modelVersion:this.versionObj.label,
              conditionGroupId:this.selectCode,
            }
            syncData(this.params).then(response => {
                this.synLoading = false
                if (response.msg=='同步成功') {
                  this.$modal.msgSuccess(response.msg);
                } else {
                  this.$modal.msgError(response.msg);
                }
                this.getTableData()
              }
            ).catch(() => {
              this.synLoading = false
            });
          }).then(() => {

          }).catch(() => {
          });
        },
        //单选
        getCurrentRow(row){
          if (this.selectCode == row.id) {
            this.selectCode = ''
            this.selectData={}
          } else {
            this.selectCode = row.id
            this.selectData=row
          }
        },
        //  新增
        addSynCondition(){
          this.synTitle="条件组新增"
          this.addSynForm={
            conditionGroupName: '',
            remark: '',
          }
          this.sqlGroup={
            conditon_type_meta_model:'',
            conditon_type_attribute_transformation:'',
            conditon_type_sql_transformation:''
          }
          this.synShow=true
        },
        //获取条件列表
        ConditionList(id){
          let params={groupId:id}
          ConditionList(params).then(res=>{
            this.oneGroup=res.rows
            let meta = this.oneGroup.find(item => item.conditionType === 'conditon_type_meta_model')
            if (meta) {
              this.sqlGroup.conditon_type_meta_model = meta.conditionContent
            }
            let att = this.oneGroup.find(item => item.conditionType === 'conditon_type_attribute_transformation')
            if (att) {
              this.sqlGroup.conditon_type_attribute_transformation = att.conditionContent
            }
            let sql = this.oneGroup.find(item => item.conditionType === 'conditon_type_sql_transformation')
            if (sql) {
              this.sqlGroup.conditon_type_sql_transformation = sql.conditionContent
            }
            this.ids=this.oneGroup.map(obj => obj.id).join(',');
          })
        },
        //单个条件组
        oneGroupList(scope){
          let params={
            metaModelId:this.versionObj.parentId,
            metaModelVersion:this.versionObj.label,
            id:scope.row.id,
            conditionGroupName:scope.row.conditionGroupName
          }
          groupList(params).then(res=>{
            this.addSynForm.id=res.rows[0].id
            this.addSynForm.conditionGroupName=res.rows[0].conditionGroupName
            this.addSynForm.remark=res.rows[0].remark
          })
        },
        //查看
        seeHandleForm(scope){
          this.synTitle='条件组查看'
          this.isDisabled=true
          this.ConditionList(scope.row.id)
          let params={
            metaModelId:this.versionObj.parentId,
            metaModelVersion:this.versionObj.label,
            id:scope.row.id,
            conditionGroupName:scope.row.conditionGroupName
          }
          groupList(params).then(res=>{
            this.addSynForm.id=res.rows[0].id
            this.addSynForm.conditionGroupName=res.rows[0].conditionGroupName
            this.addSynForm.remark=res.rows[0].remark
          })

          this.synShow=true
        },
        //编辑
        editHandleForm(scope){
          this.synTitle='条件组修改'
          this.isDisabled=false
          this.ConditionList(scope.row.id)
          this.oneGroupList(scope)
          this.synShow=true
        },
        //  删除
        async delsynData(scope){
          this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            //删除条件组
            this.ConditionList(scope.row.id)
            delConditionGroup(scope.row.id).then(res=>{
              if(this.ids!=''){
                delCondition(this.ids).then(res=>{
                  this.$message({
                          message: res.msg,
                          type: 'success',
                          center: true
                        })
                  this.getTableData()
                })
              }
            })
          }).catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除'
            })
          })
        },
        //清空文本域
        clearTextarea() {
          if(this.selection=='1'){
            this.sqlGroup.metaSql=''
          }
          if(this.selection=='2'){
            this.sqlGroup.attSql=''
          }
          if(this.selection=='3'){
            this.sqlGroup.sql=''
          }
        },
        //条件组确定（提交）
        determine(){
          this.$refs['form'].validate((valid) => {
            if (valid) {
              this.addSynForm.metaModelId=this.versionObj.parentId
              this.addSynForm.metaModelVersion=this.versionObj.label
              if(this.synTitle=='条件组修改'){
                editConditionGroup(this.addSynForm).then(res=>{
                  this.editGroup(this.addSynForm)
                  this.getTableData()
                  this.$message({
                    showClose: true,
                    message: '修改成功',
                    type: 'success'
                  });
                })

              }else{
                addConditionGroup(this.addSynForm).then(res=>{
                  if(res.code=='200'){
                    this.addSynForm.groupId=res.data
                    this.addGroup()
                    this.getTableData()
                    this.$message({
                      showClose: true,
                      message: '新增成功',
                      type: 'success'
                    });
                  }else{
                    this.$message({
                      showClose: true,
                      message: '失败',
                      type: 'error'
                    });
                  }
                })
                this.synShow=false
              }
              this.synShow=false
            } else {
              this.$message({
                showClose: true,
                message: '失败',
                type: 'error'
              });
              return false;
            }
          });

        },
        //新增条件
        async addGroup(){
          for (let key in this.sqlGroup) {
            let params={
              groupId:this.addSynForm.groupId,
              conditionType:key,
              conditionContent:this.sqlGroup[key]
            }
            await addCondition(params).then(res=>{
            })
          }
        },
        //修改条件
        async editGroup(){
          for (let key in this.sqlGroup) {
            let a=this.oneGroup.find(item => item.conditionType === key)
            let params={
              groupId:this.addSynForm.id,
              conditionType:key,
              conditionContent:this.sqlGroup[key],
              id:a.id
            }
            await editCondition(params).then(res=>{
            })
          }
        },
        //关闭窗口
        close(){
          this.activeNames=[]
          this.isDisabled=false
          this.$refs['form'].resetFields();
          this.keyword=''
          if(this.dataRelationType){
            if(this.dataRelationType=='1'){
              this.selection="2"
            } else if(this.dataRelationType=='2'){
              this.selection="3"
            }else{
              this.selection="2"
            }
          }else{
            this.selection="1"
            this.noType=true
          }
          this.synShow=false
        },
        //字段关联条件的折叠表格
        activeNamesChange(activeNames) {
          if (activeNames.length > 1) {
            // 如果同时展开了多个折叠面板，则只保留最后一个
            this.activeNames = [activeNames[activeNames.length - 1]];
          }
        },
      //  测试sql语句
        async testSql(){
          this.test.metaModelId=this.versionObj.parentId
          this.test.modelVersion=this.versionObj.label
          if(this.selection=='1'){
            this.test.conditionContent=this.sqlGroup.conditon_type_meta_model
            this.test.conditionType='conditon_type_meta_model'
          }else if(this.selection=='2'){
            this.test.conditionContent=this.sqlGroup.conditon_type_attribute_transformation
            this.test.conditionType='conditon_type_attribute_transformation'
          }else{
            this.test.conditionContent=this.sqlGroup.conditon_type_sql_transformation
            this.test.conditionType='conditon_type_sql_transformation'
          }
          await testSQL(this.test).then(res=>{
            this.testData=res.data
            if(res.data.code=='success'){
              if(res.data.data.length>0) {
                let column = Object.keys(res.data.data[0])
                this.tableColumns = column.map(column => {
                  return {
                    prop: column,
                    label: column
                  };
                });
              }
            }
          })
          this.testSQLshow=true
        },

      }
    }
</script>

<style scoped lang="scss">
  .content{
    height: 60vh;
    border: 1px solid #dcdfe6;
    padding: 10px;
  .content_item{
    border: 1px solid #dcdfe6;
    height: 92%;
    padding: 20px;
    .el-col-24{
      height: 25vh;
      .el-main{
        ::v-deep .el-textarea__inner{
          height: 19vh;
          padding-bottom: 0;
        }
      }
    }
    .el-col-14{
      height: 49vh;
      .el-main{
        ::v-deep .el-textarea__inner{
          height: 42.5vh;
          padding-bottom: 0;
        }
      }
    }

  .item{
    display: flex;
    justify-content: space-between;
    height: 100%;
  .item_border{
    border: 1px solid #dcdfe6;
    height: 100%;

    .el-header {
      //background-color: pink;
      height: 40px !important;
      color: #333;
      text-align: center;
      line-height: 30px;
      padding: 5px;
      display: inline-flex;
      justify-content: space-between;
      border-bottom: 1px solid #dcdfe6;
      .tableNameSpan{
        width: 100%;
        overflow: hidden;
      }
      span{
        height: 100%;
        .el-button{
        padding: 6px 10px;
        }
        .tableName{
          font-weight: bold;
          width: 100%;
          overflow: hidden;
        }
        .tableWord{
          font-size: 12px;
          margin-right: 5px;
        }
        .fieldStatistics{
          color: blue;
          font-weight: bold;
        }
      }
    }

    .el-aside {
      //background-color: #D3DCE6;
      color: #333;
      text-align: center;
      line-height: 200px;
    }

    .el-main {
      //background-color: #E9EEF3;
      color: #333;
      text-align: center;
      height: 45vh;
      padding: 10px;
      .el-table{
        /*height:39vh !important;*/
      }
      ::v-deep .el-collapse{

        ::v-deep .el-collapse-item__content{
          padding-bottom: 0;
          border-bottom: none !important;
        }
        .floadStyle{
          width: 350px;
          display: flex;
          .floadStyleLeft{
            width: 200px;
            text-align: left;
            margin-left: 10px;
          }
          .floadStyleRight{
            width: 150px;
            text-align: right;
            margin-right: 10px;
            span{
              color: blue;
              font-weight: bold;
            }
          }
        }
      }

    }

    body > .el-container {
      margin-bottom: 40px;
    }

    .el-container:nth-child(5) .el-aside,
    .el-container:nth-child(6) .el-aside {
      line-height: 260px;
    }

    .el-container:nth-child(7) .el-aside {
      line-height: 320px;
    }
  }
  }
  }
  }
  .card{
    height: 100%;
    border: 1px solid #dcdfe6;
    border-top: none;
  }
  .test_container{
    height: 680px;
    display: flex;
    flex-direction: column;
    .testSql .input-with-scroll .el-textarea__inner {
      height: auto !important;
      max-height: 250px;
      overflow: auto;
    }
    .table{
      flex:1;
      border:1px solid #dcdfe6;
    }
  }

</style>
