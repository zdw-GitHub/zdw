<template>
  <div class="el-dialog-div">
    <div class="formTitle" v-show="notNullList.length>0">
      <span>* 必填项</span>
    </div>
    <el-form class="opaForm" v-show="notNullList.length>0" ref="form" :model="pageForm" label-width="100px" size="mini">
      <div>
        <el-form-item v-for="(item, key) in notNullList" :label="item.attributeNameCn">
          <el-input v-model="pageForm[item.attributeNameUnderline]"></el-input>
        </el-form-item>
      </div>
    </el-form>
    <div class="formTitle" v-show="nullList.length>0">
      <span>非必填</span>
    </div>
    <el-form class="opaForm" v-show="nullList.length>0" size="mini" label-width="100px" >
      <el-form-item v-for="(item, key) in nullList" :label="item.attributeNameCn">
        <el-input v-model="pageForm[item.attributeNameUnderline]"></el-input>
      </el-form-item>
    </el-form>
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
        //主键
        keyClu:'',
        //数据的属性列
        cluList:[],
        //非空与空属性
        notNullList:[],
        nullList:[],
        //不符合规范的属性数据
        //表单数据
        pageForm:{},
      }
    },
    methods: {
      //页面初始化
      beforeLoading(){
        this.pageForm={}
        this.notNullList = []
        this.nullList = []
        this.getCluList()
      },
      //第二次进入页面
      reOpen(){
        this.notNullList=[]
        this.nullList=[]
        this.pageForm={}
        this.getFormData()
      },
      //根据modelId获取主键
      getFormData(){
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
        getForm(params).then(res=>{
          this.pageForm=res.data
        })
        //循环获取必填与非必填数组，是否必填1是，0否
        let notNullName=[]
        let nullName=[]
        this.notNullList = []
        this.nullList = []
        for (let i = 0; i < this.cluList.length; i++) {
          awaitTableDropDownBox({
            modelId: this.modelObj.modelId,
            modeVersion: this.modelObj.modelVersion,
            attribute: this.cluList[i].attributeNameUnderline,
            code: this.pageForm[this.cluList[i].attributeNameUnderline]
          }).then((res) => {
            if (res.total > 0 && this.pageForm[this.cluList[i].attributeNameUnderline]) {
              this.pageForm[this.cluList[i].attributeNameUnderline] = res.rows[0].name
            }
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
      //根据modelId获取属性，区分必填与非必填字段
      getCluList(){
        this.cluList = []
        initgriddatasite(this.modelObj).then(res=>{
          this.cluList = res.data.columns
          this.getAllRules()
        })
      },
      getAllRules(){
        getPrimaryKeyName({modeId:this.modelObj.modelId}).then(response=>{
          this.keyClu = response.msg

          this.getFormData()
        })
      },
    }
  }
</script>

<style>

</style>
