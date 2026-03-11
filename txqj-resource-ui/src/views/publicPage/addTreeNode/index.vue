<template>
  <div>
    <el-dialog
      v-dialogDrag
      append-to-body
      @close="closeAddTreeNodeBox"
      custom-class="curdBox"
      :title="'新增' + title"
      :visible.sync="show"
      width="750px"
      :modal="false"
      :close-on-click-modal="false"
      top="0"
    >
      <editData1
        ref="child1"
        :modelObj="modelObj"
        :dataObj="data"
        :form="parData"
        name="editData1"
        :isAddTreeNode="false"
        style="background: #fff"
      ></editData1>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm()">确 定</el-button>
        <el-button @click="closeAddTreeNodeBox()">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import{getModelId, submit} from "@/api/zdwh/zdzywh";
  // import editData1 from "@/views/publicPage/edit/editData";
  export default {
    name: "addTreeNode",
    components: {editData1: () => import('@/views/publicPage/edit/editData')},
    props: {
      parObj: Object
    },
    watch: {
      //解决操作栏固定后表格错位问题
      'parObj': {
        handler() {
          this.show = this.parObj.show
          // this.data = {}
          if (this.parObj.show) {
            this.title = this.parObj.title
            this.tableName = this.parObj.tableName
            this.parData = {}
            if (this.parObj.parentLabel) {
              this.parData[this.parObj.parentLabel] = this.parObj.parentValue
            }
            if (this.parObj.parentLabel2) {
              this.parData[this.parObj.parentLabel2] = this.parObj.parentValue2
            }
            this.getCommonData()
          }
        },
        deep: true
      },
    },
    data () {
      return {
        title: '',
        tableName: '',
        parData: {},
        show: false,
        data: {},
        modelObj: {}
      }
    },
    methods: {
      getCommonData(){
        //获取表基本信息
        getModelId({tableName:this.tableName}).then(res=>{
          this.modelObj = res.data
          this.reSetForm()
        })
      },
      reSetForm () {
        this.data = {}
        this.$refs.child1.modelObj = this.modelObj
        this.$refs.child1.dataObj = this.data
        this.$refs.child1.form = this.parData
        this.$refs.child1.beforeLoading()
      },
      //新增提交
      submitForm(){
        //调用子组件重复校验方法
        this.$refs.child1.beforeSubmit()
        //获取子组件表单数据
        let data = {}
        data=this.$refs.child1.pageForm
        setTimeout(() => {
          this.$confirm('是否保存此数据?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            let params={
              modelId:this.modelObj.modelId,
              isPage:'0',
              valueAttributes:[{attributeName:this.$refs.child1.keyClu,attributeValue:'1',isPrimaryKey:'1'}]
            }
            for(let key in data){
              let obj={}
              if(data[key]!==''&&data[key]!==undefined) {
                obj = {
                  attributeName: key,
                  attributeValue: data[key]
                }
                params.valueAttributes.push(obj)
              }
            }
            submit(params).then(response => {
                this.$modal.msgSuccess("操作成功");
                this.closeAddTreeNodeBox()
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
      closeAddTreeNodeBox () {
        this.$emit("closeAddTreeNodeBox");
      }
    },
  }
</script>

<style scoped>

</style>
