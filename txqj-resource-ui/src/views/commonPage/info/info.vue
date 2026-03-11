<template>
  <div style="position: relative">
    <el-switch style="position: absolute; top: 10px; right: 20px; z-index: 999" v-model="isEditMode" active-color="#13ce66"></el-switch>

    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane :label="title" name="详情">
        <editData
          ref="child"
          :modelObj="modelObj"
          :dataObj="dataObj"
          :customStyle="{height:isEditMode ? '60vh' : '65vh'}"
          :customClass="pattern ? ['el-dialog-div'] : ['el-dialog-div','oneLineOne']">
        </editData>
        <div class="dialog-footer" v-if="isEditMode">
          <el-button style="float: right;margin: 10px" type="primary" @click="submitForm()">确 定</el-button>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import editData from "@/views/publicPage/edit/editData";
import {getModelId,submitEdit} from "@/api/zdwh/zdzywh";

export default {
  name: "index",
  components: { editData},
  props: {
    dataObj: Object,
    tableName: String,
    title: String,
    isEditMode: {
      type: Boolean,
      default: () => false
    },
    pattern: {
      type: Boolean,
      default: () => true
    }
  },
  data() {
    return {
      modelObj: {},
      activeName: '详情'
    }
  },
  watch: {},
  created(){
  },
  mounted() {
    this.getData()
  },
  methods: {
    handleClick(){
    },
    getData() {
      getModelId({tableName:this.tableName}).then(res=>{
        this.modelObj = res.data
        this.$refs.child.modelObj = this.modelObj
        this.$refs.child.dataObj = this.dataObj
        this.$refs.child.beforeLoading()
      })
    },
    //新增提交
    submitForm() {
      //调用子组件重复校验方法
      this.$refs.child.beforeSubmit()
      //获取子组件表单数据
      this.editForm={}
      this.editForm=this.$refs.child.pageForm
      setTimeout(() => {
        this.$confirm('是否保存此数据?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let params={
            modelId:this.modelObj.modelId,
            isPage:'0',
            valueAttributes:[]
          }
          for(let key in this.editForm){
            let obj={}
            //if(this.editForm[key]!==''&&this.editForm[key]!==undefined){
            if(this.editForm[key]!==undefined){
              if(this.editForm[key] ==''){
                obj={
                  attributeName:key,
                  attributeValue:null
                }
              }else {
                obj={
                  attributeName:key,
                  attributeValue:this.editForm[key]
                }
              }
              if(key===this.$refs.child.keyClu){obj.isPrimaryKey=1}
              params.valueAttributes.push(obj)
            }
          }
          submitEdit(params).then(response => {
              this.$modal.msgSuccess("操作成功");
            }
          ).catch((e) => {
            // this.$message({
            //   message: e,
            //   type: 'error',
            //   center: true,
            //   duration:0,
            //   showClose:true
            // })
          })
        })
      }, 1500);
    },
    switchingModes(){
      this.pattern = !this.pattern
      this.$emit('switchingModes', this.pattern)
    },
  },
}
</script>
