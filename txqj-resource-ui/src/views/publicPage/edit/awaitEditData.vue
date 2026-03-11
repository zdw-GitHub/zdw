<template>
  <div class="el-dialog-div">
    <template v-if="formLoading">
      <div class="formTitle" v-show="notNullList.length>0">
        <span>* 必填项</span>
      </div>
      <el-form class="opaForm" v-show="notNullList.length>0" ref="form" :model="pageForm" label-width="100px" size="mini">
        <el-form-item v-for="(item, key) in notNullList" :label="item.attributeNameCn">
          <el-select
            v-if="item.attType==='select'"
            clearable
            filterable
            remote
            :loading="loading"
            v-loadmore="loadmore"
            @focus="focusSelect = notNullList[key]"
            :remote-method="(val) => remoteMethod(val, 'notNullList', key, item.list)"
            @change="checkAdd(item.attributeNameUnderline,pageForm[item.attributeNameUnderline])"
            v-model="pageForm[item.attributeNameUnderline]"
            :class="
                rulesForm[item.attributeNameUnderline] === undefined ||
                rulesForm[item.attributeNameUnderline] === '' ||
                rulesForm[item.attributeNameUnderline] === null
                ? '' : 'error'"
          >
            <el-option
              v-for="(arr, index) in notNullList[key].list"
              :label="arr.name"
              :key="arr.code + '' + index"
              :value="arr.code"
              :disabled="arr.disabled"
            ></el-option>
          </el-select>
          <el-date-picker
            v-model="pageForm[item.attributeNameUnderline]"
            @blur="checkAdd(item.attributeNameUnderline,pageForm[item.attributeNameUnderline])"
            type="datetime"
            :class="rulesForm[item.attributeNameUnderline] ===undefined||rulesForm[item.attributeNameUnderline]===''||rulesForm[item.attributeNameUnderline]===null?'':'error'"
            v-else-if="item.attType==='datetime'"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="选择日期时间">
          </el-date-picker>
          <el-input @blur="checkAdd(item.attributeNameUnderline,pageForm[item.attributeNameUnderline])" v-else v-model="pageForm[item.attributeNameUnderline]"
                    :class="rulesForm[item.attributeNameUnderline] ===undefined||rulesForm[item.attributeNameUnderline]===''||rulesForm[item.attributeNameUnderline]===null?'':'error'"></el-input>
          <!--表单验证信息 别删-->
          <!--<div style="margin-left:210px;margin-top:-30px;width:300px">-->
          <!--<span style="font-size:10px;color:red;" v-if="showRules" v-show="rulesForm[item.attributeNameUnderline]!==undefined&&rulesForm[item.attributeNameUnderline]!==''" v-html="rulesForm[item.attributeNameUnderline]"></span>-->
          <!--<span style="font-size:10px;color:grey;" v-if="showRules" v-show="rulesForm[item.attributeNameUnderline]===undefined" v-html="item.rules"></span>-->
          <!--</div>-->
        </el-form-item>
      </el-form>
      <div class="formTitle" v-show="nullList.length>0">
        <span>非必填</span>
      </div>
      <el-form class="opaForm" v-show="nullList.length>0" size="mini" label-width="100px" >
      <el-form-item v-for="(item, key) in nullList" :label="item.attributeNameCn">
        <el-select
          v-if="item.attType==='select'"
          clearable
          filterable
          :loading="loading"
          remote
          v-loadmore="loadmore"
          @focus="focusSelect = nullList[key]"
          :remote-method="(val) => remoteMethod(val)"
          @change="checkAdd(item.attributeNameUnderline,pageForm[item.attributeNameUnderline])"
          v-model="pageForm[item.attributeNameUnderline]"
          :class="
              rulesForm[item.attributeNameUnderline] === undefined ||
              rulesForm[item.attributeNameUnderline] === '' ||
              rulesForm[item.attributeNameUnderline] === null
              ? '' : 'error'"
        >
          <el-option
            v-for="(arr, index) in nullList[key].list"
            :label="arr.name"
            :key="arr.code + '' + index"
            :value="arr.code"
            :disabled="arr.disabled"
          ></el-option>
        </el-select>
        <el-date-picker
          v-model="pageForm[item.attributeNameUnderline]"
          @blur="checkAdd(item.attributeNameUnderline,pageForm[item.attributeNameUnderline])"
          type="datetime"
          :class="rulesForm[item.attributeNameUnderline] ===undefined||rulesForm[item.attributeNameUnderline]===''||rulesForm[item.attributeNameUnderline]===null?'':'error'"
          v-else-if="item.attType==='datetime'"
          value-format="yyyy-MM-dd HH:mm:ss"
          placeholder="选择日期时间">
        </el-date-picker>
        <el-input @blur="checkAdd(item.attributeNameUnderline,pageForm[item.attributeNameUnderline])" v-else v-model="pageForm[item.attributeNameUnderline]"
                  :class="rulesForm[item.attributeNameUnderline] ===undefined||rulesForm[item.attributeNameUnderline]===''||rulesForm[item.attributeNameUnderline]===null?'':'error'"></el-input>
        <!--<div style="margin-left:210px;margin-top:-30px;width:300px">-->
        <!--<span style="font-size:10px;color:red;" v-if="showRules" v-show="rulesForm[item.attributeNameUnderline]!==undefined&&rulesForm[item.attributeNameUnderline]!==''" v-html="rulesForm[item.attributeNameUnderline]"></span>-->
        <!--<span style="font-size:10px;color:grey;" v-if="showRules" v-show="rulesForm[item.attributeNameUnderline]===undefined" v-html="item.rules"></span>-->
        <!--</div>-->
      </el-form-item>
    </el-form>
    </template>
  </div>
</template>

<script>
  import { getPrimaryKeyName,getForm,initgriddatasite,awaitTableDropDownBox,selectAllRules,getColunmId,getVerrules} from "@/api/zdwh/zdzywh";
  export default {
    name: "editData",
    props: {
      modelObj:Object,
      dataObj:Object,
      type: String,
      dbName: String,
    },
    data() {
      return {
        formLoading: false,
        //主键
        keyClu:'',
        //数据的属性列
        cluList:[],
        //非空与空属性
        notNullList:[],
        nullList:[],
        //校验规则
        rulesForm:{},
        //不符合规范的属性数据
        addErrForm:[],
        //页面展示校验结果
        showRules:false,
        //表单数据
        pageForm:{},
        listMap:[],
        loading: false,
        focusSelect: {},
        searchStr: '',
      }
    },
    created() {
      // this.beforeLoading()
    },
    watch:{
      modelObj(newVal,oldVal){
        /* if(newVal.modelId===oldVal.modelId){
           this.reOpen()
         }else{
           this.beforeLoading()
         }*/
      },
      notNullList:{
        deep:true,
        handler(newVal){
          window.setTimeout(() => {
            this.formLoading = false
            this.$nextTick(() => {
              this.formLoading = true
            })
          }, 1000)
        }
      },
      nullList:{
        deep:true,
        handler(newVal){
          window.setTimeout(() => {
            this.formLoading = false
            this.$nextTick(() => {
              this.formLoading = true
            })
          }, 1000)
        }
      }
    },
    methods: {
      loadmore () {
        if (this.focusSelect.total > this.focusSelect.list.length) {
          let pageNum = parseInt(this.focusSelect.list.length / 20) * 20
          awaitTableDropDownBox({
            modelId: this.modelObj.modelId,
            modeVersion: this.modelObj.modelVersion,
            attribute: this.focusSelect.attributeNameUnderline,
            name: this.searchStr,
            pageNum: pageNum,
            pageSize: 15,
          }).then((res) => {
            this.loading = true
            if (res.rows.length > 0) {
              this.focusSelect.list.push.apply(
                this.focusSelect.list,
                res.rows
              )
            }
            this.$nextTick(() => {
              this.loading = false
            })
          }).catch(e => {
          })
        } else {
          if (
            !this.focusSelect.list[this.focusSelect.list.length - 1].disabled
          ) {
            this.loading = true
            this.focusSelect.list.push({
              code: 'aaabbbccc',
              name: '没有更多了',
              disabled: true
            })
            this.$nextTick(() => {
              this.loading = false
            })
          }
        }
      },
      remoteMethod (val) {
        this.searchStr = val
        awaitTableDropDownBox({
          modelId: this.modelObj.modelId,
          modeVersion: this.modelObj.modelVersion,
          attribute: this.focusSelect.attributeNameUnderline,
          name: this.searchStr,
          pageNum: 0,
          pageSize: 15,
        }).then((res) => {
          this.loading = true
          this.focusSelect.list = res.rows
          this.focusSelect.total = res.total
          this.$nextTick(() => {
            this.loading = false
          })
        }).catch(e => {
        })
      },
      //页面初始化
      beforeLoading(){
        this.rulesForm={}
        this.pageForm={}
        this.addErrForm=[]
        this.notNullList = []
        this.nullList = []
        this.getCluList()
      },
      //第二次进入页面
      reOpen(){
        this.rulesForm={}
        this.pageForm={}
        this.addErrForm=[]
        this.getKey()
      },
      //根据modelId获取主键
      getKey(){
        getPrimaryKeyName({modeId:this.modelObj.modelId}).then(response=>{
          this.keyClu = response.msg
          if(this.dataObj[this.keyClu]!==undefined&&this.dataObj[this.keyClu]!==''){
            let params={
              modelId:this.modelObj.modelId,
              type:"`ry-middledb`",
              dbName:"`ry-middledb`",
              isPage:'0',
              whereAttributes:[{attributeName:this.keyClu,attributeValue:this.dataObj[this.keyClu],isPrimaryKey:'1'}],
            }
            if (
              this.type != undefined
            ) {
              params.type = this.type
              params.dbName =  this.dbName
            }
            getForm(params).then(res=>{
              this.pageForm=res.data
              for (let i = 0; i < this.notNullList.length; i++) {
                this.checkAdd(this.notNullList[i].attributeNameUnderline,this.pageForm[this.notNullList[i].attributeNameUnderline])
                if (
                  this.notNullList[i].attType == 'select' &&
                  this.notNullList[i].list &&
                  !this.notNullList[i].list.find(item => {
                    return this.pageForm[this.notNullList[i].attributeNameUnderline] == item.code
                  }) &&
                  this.pageForm[this.cluList[i].attributeNameUnderline]
                ) {
                  awaitTableDropDownBox({
                    modelId: this.modelObj.modelId,
                    modeVersion: this.modelObj.modelVersion,
                    attribute: this.notNullList[i].attributeNameUnderline,
                    code: this.pageForm[this.notNullList[i].attributeNameUnderline]
                  }).then((res1) => {
                    if (res1.total > 0) {
                      this.notNullList[i].list = [
                        res1.rows[0],
                        ...this.notNullList[i].list
                      ]
                      this.notNullList = JSON.parse(JSON.stringify(this.notNullList))
                    }
                  }).catch(e => {
                  })
                }
              }

              for (let i = 0; i < this.nullList.length; i++) {
                this.checkAdd(this.nullList[i].attributeNameUnderline,this.pageForm[this.nullList[i].attributeNameUnderline])
                if (
                  this.nullList[i].attType == 'select' &&
                  this.nullList[i].list &&
                  !this.nullList[i].list.find(item => {
                    return this.pageForm[this.nullList[i].attributeNameUnderline] == item.code
                  }) &&
                  this.pageForm[this.cluList[i].attributeNameUnderline]
                ) {
                  awaitTableDropDownBox({
                    modelId: this.modelObj.modelId,
                    modeVersion: this.modelObj.modelVersion,
                    attribute: this.nullList[i].attributeNameUnderline,
                    code: this.pageForm[this.nullList[i].attributeNameUnderline]
                  }).then((res1) => {
                    if (res1.total > 0) {
                      this.nullList[i].list = [
                        res1.rows[0],
                        ...this.nullList[i].list
                      ]
                      this.nullList = JSON.parse(JSON.stringify(this.nullList))
                    }
                  }).catch(e => {
                  })
                }
              }
            })
          }else{
            //新增
            this.pageForm={}
          }
        })
      },
      //根据modelId获取属性，区分必填与非必填字段
      getCluList(){
        this.cluList = []
        initgriddatasite(this.modelObj).then(res=>{
          this.cluList = res.data.columns
          for (let i = 0; i < this.cluList.length; i++) {
            this.cluList[i].rules=''
          }
          //获取所有属性初始化规则
          this.getAllRules()
        })
      },
      //获取所有属性的规则
      async getAllRules(){
        await selectAllRules().then(res=>{
          res = res.data
          for (let i = 0; i < res.length; i++) {
            for (let j = 0; j < this.cluList.length; j++) {
              if(res[i].errorColumnNameCn === this.cluList[j].attributeNameCn){
                if(this.cluList[j].rules===undefined){
                  this.cluList[j].rules=res[i].ruleDescription + '<br/>'
                }else{
                  this.cluList[j].rules+=res[i].ruleDescription + '<br/>'
                }
              }
            }
          }
        })
        await getPrimaryKeyName({modeId:this.modelObj.modelId}).then(response=>{
          this.keyClu = response.msg
        })
        if(this.dataObj[this.keyClu]!==undefined&&this.dataObj[this.keyClu]!==''){
          let params={
            modelId:this.modelObj.modelId,
            type:"`ry-middledb`",
            dbName:"`ry-middledb`",
            isPage:'0',
            whereAttributes:[
              {
                attributeName: this.keyClu,
                attributeValue:this.dataObj[this.keyClu],
                isPrimaryKey:'1'
              }
            ]
          }
          if (
            this.type != undefined
          ) {
            params.type = this.type
            params.dbName =  this.dbName
          }
          await getForm(params).then(res=>{
            this.pageForm=res.data
          })
        }else{
          //新增
          this.pageForm={}
        }
        //循环获取必填与非必填数组，是否必填1是，0否
        let notNullName=[]
        let nullName=[]
        this.notNullList = []
        this.nullList = []
        for (let i = 0; i < this.cluList.length; i++) {
          this.checkAdd(
            this.cluList[i].attributeNameUnderline,
            this.pageForm[this.cluList[i].attributeNameUnderline]
          )
          awaitTableDropDownBox({
            modelId: this.modelObj.modelId,
            modeVersion: this.modelObj.modelVersion,
            attribute: this.cluList[i].attributeNameUnderline,
            pageNum: 0,
            pageSize: 15,
          }).then((res1) => {
            if (res1.total > 0) {
              this.cluList[i].attType = 'select'
              this.cluList[i].list = res1.rows
              this.cluList[i].total = res1.total
              if (
                this.pageForm[this.cluList[i].attributeNameUnderline] &&
                !this.cluList[i].list.find(item => {
                  return this.pageForm[this.cluList[i].attributeNameUnderline] == item.code
                })
              ) {
                awaitTableDropDownBox({
                  modelId: this.modelObj.modelId,
                  modeVersion: this.modelObj.modelVersion,
                  attribute: this.cluList[i].attributeNameUnderline,
                  code: this.pageForm[this.cluList[i].attributeNameUnderline]
                }).then((res1) => {
                  if (res1.total > 0) {
                    this.cluList[i].list = [
                      res1.rows[0],
                      ...this.cluList[i].list
                    ]
                    this.cluList = JSON.parse(JSON.stringify(this.cluList))
                  }
                })
              }
            }
            if(this.cluList[i].isNotNull===1||this.cluList[i].isNotNull==='1'){
              this.notNullList.push(this.cluList[i])
              notNullName.push(this.cluList[i].attributeName)
            }else{
              this.nullList.push(this.cluList[i])
              nullName.push(this.cluList[i].attributeName)
            }
          }).catch(e => {
            if(this.cluList[i].isNotNull===1||this.cluList[i].isNotNull==='1'){
              this.notNullList.push(this.cluList[i])
              notNullName.push(this.cluList[i].attributeName)
            }else{
              this.nullList.push(this.cluList[i])
              nullName.push(this.cluList[i].attributeName)
            }
          })
        }
      },
      //校验名字
      checkAdd(column,value) {
        return
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
          this.showRules=false
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
          this.showRules=true
        }).catch(e => {

        })
      },
      //提交前校验所有属性
      beforeSubmit(){
        for (let i = 0; i < this.notNullList.length; i++) {
          this.checkAdd(this.notNullList[i].attributeNameUnderline,this.pageForm[this.notNullList[i].attributeNameUnderline])
        }
        for (let i = 0; i < this.nullList.length; i++) {
          this.checkAdd(this.nullList[i].attributeNameUnderline,this.pageForm[this.nullList[i].attributeNameUnderline])
        }
      }
    }
  }
</script>

<style>

</style>
