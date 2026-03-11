<template>
  <div class="el-dialog-div">
    <div class="formTitle" v-show="notNullList.length>0">
      <span>* 必填项</span>
    </div>
    <el-form class="opaForm" v-show="notNullList.length>0" ref="form" :model="pageForm" label-width="250px" size="mini">
      <el-form-item v-for="(item, key) in notNullListSort" v-if="item.show == null || item.show">
        <template #label>
          <img
            width="28px"
            height="28px"
            style="margin-right: 8px; float: left"
            :src="getIconUrl(item.icon)"
          >
          <span
            style="display: inline-block; width: calc(100% - 36px);">
                {{item.attributeNameCn}}
              </span>
        </template>
        <el-input
          :disabled="true"
          v-model="pageForm[item.attributeNameUnderline]"
        ></el-input>
      </el-form-item>
    </el-form>
    <div class="formTitle" v-show="nullList.length>0">
      <span>非必填</span>
    </div>
    <el-form class="opaForm" v-show="nullList.length>0" size="mini" label-width="250px" >
      <el-form-item v-for="(item, key) in nullListSort"  v-if="item.show == null || item.show">
        <template #label>
          <img
            width="28px"
            height="28px"
            style="margin-right: 8px; float: left"
            :src="getIconUrl(item.icon)"
          >
          <span
            style="display: inline-block; width: calc(100% - 36px);">
                {{item.attributeNameCn}}
              </span>
        </template>
        <el-input
          :disabled="true"
          v-model="pageForm[item.attributeNameUnderline]"
        ></el-input>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  import { getPrimaryKeyName,getForm,initgriddatasite,tableDropDownBox,selectAllRules,getColunmId,getVerrules} from "@/api/zdwh/zdzywh";
  export default {
    name: "seeData",
    props: {
      modelObj:Object,
      dataObj:Object,
      type: String,
      dbName: String,
    },
    data() {
      return {
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
          if(this.modelObj.tableName == 'SG_TCCON_TCBUZ_B'){
            if(this.pageForm.BUZ_TYPE){
              let BUZ_TYPE = this.pageForm.BUZ_TYPE
              this.pageForm.BUZ_TYPE = null
              this.pageForm.BUZ_TYPE = BUZ_TYPE
            }
          }
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
          if(this.modelObj.tableName == 'SG_TCCON_TCBUZ_B'){
            if(this.pageForm.BUZ_TYPE){
              let BUZ_TYPE = this.pageForm.BUZ_TYPE
              this.pageForm.BUZ_TYPE = null
              this.pageForm.BUZ_TYPE = BUZ_TYPE
            }
          }
        }
      },
      pageForm:{
        deep:true,
        handler(newVal){
          if(this.modelObj.tableName == 'SG_TCCON_TCBUZ_B'){
            let show = false
            if(newVal.BUZ_TYPE == '继电保护' ){
              show = true
            }
            for (let i = 0; i < this.notNullList.length; i++) {
              if(
                this.notNullList[i].attributeNameUnderline == 'VOLTAGE_CLASS' ||
                this.notNullList[i].attributeNameUnderline == 'LINE_NAME' ||
                this.notNullList[i].attributeNameUnderline == 'CHANNEL_CODE' ||
                this.notNullList[i].attributeNameUnderline == 'PROTECT_NUM' ||
                this.notNullList[i].attributeNameUnderline == 'PROTECT_PORT'
              ){
                this.notNullList[i].show = show
              }
            }
            for (let i = 0; i < this.nullList.length; i++) {
              if(
                this.nullList[i].attributeNameUnderline == 'VOLTAGE_CLASS' ||
                this.nullList[i].attributeNameUnderline == 'LINE_NAME' ||
                this.nullList[i].attributeNameUnderline == 'CHANNEL_CODE' ||
                this.nullList[i].attributeNameUnderline == 'PROTECT_NUM' ||
                this.nullList[i].attributeNameUnderline == 'PROTECT_PORT'
              ){
                this.nullList[i].show = show

              }
            }
          }
        }
      }
    },
    computed:{
      nullListSort() {
        return this.nullList.sort((a1,a2)=>{
          if (a1.sortNum != a2.sortNum){
            return a1.sortNum - a2.sortNum
          }else {
            return a1.id - a2.id
          }
        })
      },
      notNullListSort() {
        return this.notNullList.sort((a1,a2)=>{
          if (a1.sortNum != a2.sortNum){
            return a1.sortNum - a2.sortNum
          }else {
            return a1.id - a2.id
          }
        })
      }
    },
    methods: {
      getIconUrl (url) {
        let u = process.env.BASE_URL + 'img/formImg/'
        if (url && url!== '') {
          return u + url
        }
        return u + 'default.png'
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
        this.notNullList=[]
        this.nullList=[]
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
              isPage:'0',
              whereAttributes:[{attributeName:this.keyClu,attributeValue:this.dataObj[this.keyClu],isPrimaryKey:'1'}]
            }
            if (
              this.type != undefined
            ) {
              params.type = this.type
              params.dbName =  this.dbName
            }
            getForm(params).then(res=>{
              this.pageForm=res.data
              for (let i = 0; i < this.cluList.length; i++) {
                tableDropDownBox({
                  modelId: this.modelObj.modelId,
                  modeVersion: this.modelObj.modelVersion,
                  attribute: this.cluList[i].attributeNameUnderline,
                  code: this.pageForm[this.cluList[i].attributeNameUnderline]
                }).then((res1) => {
                  if (
                    res1.total > 0 &&
                    this.pageForm[this.cluList[i].attributeNameUnderline] != '' &&
                    this.pageForm[this.cluList[i].attributeNameUnderline] != undefined
                  ) {
                    this.pageForm[this.cluList[i].attributeNameUnderline] = res1.rows[0].name
                  }
                  if(this.cluList[i].isNotNull===1||this.cluList[i].isNotNull==='1'){
                    this.notNullList.push(this.cluList[i])
                  }else{
                    this.nullList.push(this.cluList[i])
                  }
                })
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
            isPage:'0',
            whereAttributes:[{attributeName:this.keyClu,attributeValue:this.dataObj[this.keyClu],isPrimaryKey:'1'}]
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
        this.notNullList = []
        this.nullList = []
        for (let i = 0; i < this.cluList.length; i++) {
          if(this.modelObj.tableName == 'SG_TCDEV_FIBER_B' && this.cluList[i].attributeNameUnderline == 'PAR_LINE'){
            let parLine = []
            for (let j = 0; j < this.pageForm[this.cluList[i].attributeNameUnderline].length; j++) {
              tableDropDownBox({
                modelId: this.modelObj.modelId,
                modeVersion: this.modelObj.modelVersion,
                attribute: this.cluList[i].attributeNameUnderline,
                code: this.pageForm[this.cluList[i].attributeNameUnderline][j]
              }).then((res1) => {
                if (
                  res1.total > 0 &&
                  this.pageForm[this.cluList[i].attributeNameUnderline] != '' &&
                  this.pageForm[this.cluList[i].attributeNameUnderline] != undefined
                ) {
                  parLine.push(res1.rows[0].name)
                  this.pageForm[this.cluList[i].attributeNameUnderline] = parLine.join("、")
                }
                if(j == 0){
                  if(this.cluList[i].isNotNull===1||this.cluList[i].isNotNull==='1'){
                    this.notNullList.push(this.cluList[i])
                  }else{
                    this.nullList.push(this.cluList[i])
                  }
                }
              })
            }
          }else {
            tableDropDownBox({
              modelId: this.modelObj.modelId,
              modeVersion: this.modelObj.modelVersion,
              attribute: this.cluList[i].attributeNameUnderline,
              code: this.pageForm[this.cluList[i].attributeNameUnderline]
            }).then((res1) => {
              if (
                res1.total > 0 &&
                this.pageForm[this.cluList[i].attributeNameUnderline] != '' &&
                this.pageForm[this.cluList[i].attributeNameUnderline] != undefined
              ) {
                this.pageForm[this.cluList[i].attributeNameUnderline] = res1.rows[0].name
              }
              if(this.cluList[i].isNotNull===1||this.cluList[i].isNotNull==='1'){
                this.notNullList.push(this.cluList[i])
              }else{
                this.nullList.push(this.cluList[i])
              }
            })
          }

        }
      },
    }
  }
</script>
