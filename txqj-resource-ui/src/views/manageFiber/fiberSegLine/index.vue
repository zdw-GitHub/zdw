<template>
  <div>

    <zdzywhPage
      ref="zdzywhPage"
      :tableName="'SG_TCDEV_FIBERLINE_B'"
      :operate="{
            slot: 'handle',
            label: '操作',
            width: '180',
        }"
      :tabOperate="true"
    >
      <template v-slot:tabOperate>
        <el-button size="mini" plain @click="back">返回</el-button>
        <el-button size="mini" type="primary" @click="openEditBox('add', {})" v-hasPermi="['system:b:add']">新增</el-button>
        <!--<el-button size="mini" type="danger" @click="deleteSta()">删除</el-button>-->
        <el-button size="mini" @click="handleExport()">导出</el-button>
        <el-button size="mini" @click="openWindow" icon="el-icon-full-screen">全屏</el-button>
      </template>
      <template v-slot:handle="slot">
        <el-button size="mini" type="primary" @click="openEditBox('edit', slot.scope.row)" v-hasPermi="['system:b:update']">编辑</el-button>
        <el-button type="primary" size="mini" @click="searchStation(slot.scope)" v-hasPermi="['system:b:list']">查看</el-button>
        <el-button size="mini" type="danger" @click="delData(slot.scope.row)" v-hasPermi="['system:b:remove']">删除</el-button>
      </template>
    </zdzywhPage>

    <!-- 新增属性对话框 -->
    <el-dialog
      v-dialogDrag
      custom-class="curdBox"
      :title="editBox.type == 'add' ? '新增' : '修改'"
      :modal="false"
      :visible.sync="editBox.show"
      width="750px"
      :close-on-click-modal="false"
    >
      <editData ref="child" :modelObj="modelObj" :dataObj="editBox.data"></editData>
      <div slot="footer" class="dialog-footer" style="margin-bottom:20px">
        <el-button type="primary" @click="submitForm()">确 定</el-button>
        <el-button @click="editBox.show = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {nameTo_, nameToTuoFeng} from "@/utils"
  import{getModelId,getData, getForm} from "@/api/zdwh/zdzywh";
  import{submit, submitEdit, delData} from "@/api/manageFiber/fiberSegLine";
  import editData from "@/views/publicPage/edit/editData";
  import zdzywhPage from '@/views/commonPage/zdzywh/index';
  export default {
    name: "index",
    components: {editData,zdzywhPage},
    data(){
      return {
        tableName:'SG_TCDEV_FIBERLINE_B',
        //表对象信息
        modelObj: {},
        editBox: {
          type: '',
          show: false,
          data: {},
          isFirst: true,
        },

      }
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
    created(){
      this.getCommonData()
    },
    methods:{
      //返回
      back(){
        this.$router.back()
      },
      getCommonData(){
        //获取表基本信息
        getModelId({tableName:this.tableName}).then(res=>{
          this.modelObj = res.data
        })
      },
      handleExport(){
        this.$refs.zdzywhPage.handleExport()
      },
      // 新增/修改
      openEditBox(type, row){
        this.editBox.type = type
        this.editBox.show = true
        this.editBox.data = row
        this.$nextTick(()=>{
          this.$refs.child.modelObj=this.modelObj
          this.$refs.child.dataObj=this.editBox.data
          if(this.editBox.isFirst){
            this.$refs.child.beforeLoading()
            this.editBox.isFirst = false
          }else{
            this.$refs.child.reOpen()
          }
        })
      },
      //查看
      searchStation(scope){
        this.$refs.zdzywhPage.searchStation(scope)
      },
      //新增提交
      submitForm(){
        //调用子组件重复校验方法
        this.$refs.child.beforeSubmit()
        setTimeout(() => {
          this.$confirm('是否保存此数据?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            let params1 = this.$refs.child.pageForm
            let params = {}
            for (let key in params1) {
              params[nameToTuoFeng(key)] = params1[key]
            }
            params.parFiber = JSON.parse(this.$route.query.search).PAR_FIBER
            params.parFiberSeg = JSON.parse(this.$route.query.search).PAR_FIBER_SEG
            if (this.editBox.type == 'add') {
              submit(params).then(response => {
                this.editBox.show = false
                this.$modal.msgSuccess("操作成功");
                this.$refs.zdzywhPage.getTableData()
              })
            } else {
              submitEdit(params).then(response => {
                this.editBox.show = false
                this.$modal.msgSuccess("操作成功");
                this.$refs.zdzywhPage.getTableData()
              })
            }
          })
        }, 1500);
      },

      openWindow(){
        this.$refs.zdzywhPage.openWindow()
      },

      //  单个删除
      delData(row){
        this.$confirm('此操作删除的数据将进入带处理数据中, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let data = {}
          for (let key in row) {
            data[nameToTuoFeng(key)] = row[key]
          }
          data.fiberId = JSON.parse(this.$route.query.search).PAR_FIBER
          delData(data).then(response => {
            this.$modal.msgSuccess("操作成功");
            this.$refs.zdzywhPage.getTableData()
          })
        }).catch((e) => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
      },
      deleteSta() {
        if(this.$refs.zdzywhPage.multipleSelection.length===0){
          this.$message.error('请选中数据进行删除');
        } else{
          this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            let ids = []
            let datas = this.$refs.zdzywhPage.multipleSelection
            for (let i = 0; i < datas.length; i++) {
              ids.push(datas[i].ID)
            }
            delDataApi(ids).then(res => {
              this.$refs.zdzywhPage.getTableData()
            })
          }).catch(() => {
            this.$message({
              type: 'info',
              message: '已取消删除'
            })
          })
        }
      },




      toLightPath (row) {
        let data = {
          modelId: this.modelObj.modelId,
          isPage: 0,
          whereAttributes: [
            {
              attributeName: 'ID',
              attributeValue: row.ID,
              isPrimaryKey: 1,
            }
          ]
        }
        getForm(data).then(res => {
          this.$router.push(
            {
              path: getUrlByPagePath('lightPathInfoManage', this.$store.state.permission.topbarRouters, ''),
              query:{
                id: res.data.ID,
                name: row.OPTICALROAD_ID,
              }
            }
          )
        })
      }
    },
  }
</script>

<style scoped lang="scss">
  .toLightPath{
    border-bottom: 1px solid #409EFF;
    color: #409EFF;
    cursor: pointer;
  }
</style>
