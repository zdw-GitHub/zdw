<template>
  <div>
    <zdzywhPage
      ref="zdzywhPage"
      :tableName="'SG_TCDEV_FIBERLINE'"
      :operate="{
            slot: 'handle',
            label: '操作',
            width: '150',
        }"
      :tabOperate="true"
    >
      <template v-slot:tabOperate>
        <el-button size="mini" @click="back">返回</el-button>
        <el-button size="mini" @click="handleExport()">导出</el-button>
        <el-button size="mini" @click="openWindow" icon="el-icon-full-screen">全屏</el-button>
      </template>
      <template v-slot:handle="slot">
        <el-button size="mini" type="primary" @click="openEdit(slot.scope.row)" v-hasPermi="['system:b:update']">编辑</el-button>
        <el-button type="primary" size="mini" @click="searchStation(slot.scope)" v-hasPermi="['system:b:list']">查看</el-button>
        <el-dropdown
          trigger="click"
          v-if="slot.scope.row.OPTICALROAD_ID"
          style="margin-left: 5px">
          <span class="el-dropdown-link"><i class="el-icon-arrow-down el-icon--right"></i></span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="toLightPath(slot.scope.row)">查看对应光路</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </template>
    </zdzywhPage>
    <!--修改对话框-->
    <el-dialog
      v-dialogDrag
      custom-class="curdBox"
      title="修改"
      :visible.sync="editStationDrawer"
      width="750px"
      :modal="false"
      :close-on-click-modal="false"
    >
      <editData ref="child" :modelObj="this.modelObj" :dataObj="this.editForm"></editData>
      <div slot="footer" class="dialog-footer" style="margin-bottom:20px">
        <el-button type="primary" @click="submitEditForm()">确 定</el-button>
        <el-button @click="editStationDrawer=false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

  import editData from "@/views/publicPage/edit/editData";
  import zdzywhPage from '@/views/commonPage/zdzywh/index';
  import { getModelId, submitEdit } from '@/api/zdwh/zdzywh'
  import {updateFiberLineNumber} from "@/api/manageFiber/fiber";
  export default {
    name: "index",
    components: {zdzywhPage,editData},
    data(){
      return {
        tableName: 'SG_TCDEV_FIBERLINE',
        //  单条数据
        editForm: {},
        //表对象信息
        modelObj: {},
        editStationDrawer: false,
        //编辑是否第一次打开
        ifFirstEdit: true,
      }
    },
    created() {
      this.getCommonData()
    },
    methods:{
      getCommonData() {
        //获取表基本信息
        getModelId({tableName: this.tableName}).then(res => {
          this.modelObj = res.data
        })
      },
      handleExport(){
        this.$refs.zdzywhPage.handleExport()
      },
      openWindow(){
        this.$refs.zdzywhPage.openWindow()
      },
      //编辑
      openEdit(scope){
        this.editForm.ID = scope.ID
        this.editStationDrawer = true
        this.$nextTick(() => {
          this.$refs.child.modelObj = this.modelObj
          this.$refs.child.dataObj = this.editForm
          if (this.ifFirstEdit) {
            this.$refs.child.beforeLoading()
            this.ifFirstEdit = false
          } else {
            this.$refs.child.reOpen()
          }
        })
      },
      //编辑提交
      submitEditForm() {
        //调用子组件重复校验方法
        this.$refs.child.beforeSubmit()
        //获取子组件表单数据
        this.editForm = {}
        this.editForm = this.$refs.child.pageForm
        setTimeout(() => {
          this.$confirm('是否保存此数据?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            let params = {
              modelId: this.modelObj.modelId,
              isPage: '0',
              valueAttributes: []
            }
            for (let key in this.editForm) {
              let obj = {}
              if (this.editForm[key] !== undefined) {
                if (this.editForm[key] == '') {
                  obj = {
                    attributeName: key,
                    attributeValue: null
                  }
                } else {
                  obj = {
                    attributeName: key,
                    attributeValue: this.editForm[key]
                  }
                }
                if (key === this.$refs.child.keyClu) {
                  obj.isPrimaryKey = 1
                }
                params.valueAttributes.push(obj)
              }
            }
            submitEdit(params).then(response => {
              this.editStationDrawer = false
              this.$modal.msgSuccess('操作成功')
              this.$refs.zdzywhPage.getTableData()
              let PAR_FIBER = JSON.parse(this.$route.query.search).PAR_FIBER
              if (PAR_FIBER){
                updateFiberLineNumber({id:PAR_FIBER})
              }

            }).catch((e) => {})
          })
        }, 1500)
      },
      //查看
      searchStation(scope){
        this.$refs.zdzywhPage.searchStation(scope)
      },
      //返回
      back(){
        this.$router.back()
      },
      toLightPath (row) {
        this.$router.push(
          {
            path: '/zdwh/sgtccon/lightPathInfoManage',
            query:{
              search: JSON.stringify({
                NAME: row.OPTICALROAD_ID,
              }),
              refresh: true
            }
          }
        )
      }
    },
  }
</script>
