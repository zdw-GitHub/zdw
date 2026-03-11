<template>
  <div style="position: relative">
    <el-switch style="position: absolute; top: 10px; right: 20px; z-index: 999" v-model="isEditMode"
               v-if="activeName == '站点详情'" active-color="#13ce66"
    ></el-switch>
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="站点详情" name="站点详情">
        <editData
          ref="child"
          :modelObj="modelObj"
          :dataObj="dataObj"
          :customStyle="{height:isEditMode ? '60vh' : '65vh'}"
          :customClass="pattern ? ['el-dialog-div'] : ['el-dialog-div','oneLineOne']"
        >
        </editData>
        <div class="dialog-footer" v-if="isEditMode">
          <el-button style="float: right;margin: 10px" type="primary" @click="submitForm()">确 定</el-button>
        </div>
      </el-tab-pane>
      <el-tab-pane label="SDH设备列表" name="SDH设备列表">
        <carrying
          v-if="show1"
          v-loading="sdh.loading"
          style="height: 68vh"
          ref="sdh"
          :tableName="sdh.tableName"
          :searchForm="sdh.searchForm"
          :pattern="pattern"
          :openNewPage="false"
          :jumpLink="[{attributeNameUnderline: 'NAME', menuPath: 'transsource/zdwh/common/SG_TCDEV_TCNESDH_B'}]"
        ></carrying>
      </el-tab-pane>
      <el-tab-pane label="OTN设备列表" name="OTN设备列表">
        <carrying
          v-if="show2"
          v-loading="otn.loading"
          style="height: 68vh"
          ref="otn"
          :tableName="otn.tableName"
          :searchForm="otn.searchForm"
          :pattern="pattern"
          :openNewPage="false"
          :jumpLink="[{attributeNameUnderline: 'NAME', menuPath: 'transsource/zdwh/common/SG_TCDEV_TCNEOTN_B'}]"
        ></carrying>
      </el-tab-pane>
      <el-tab-pane label="光缆列表" name="光缆列表">
        <carrying
          v-if="show3"
          v-loading="fiber.loading"
          style="height: 68vh"
          ref="fiber"
          :tableName="fiber.tableName"
          :searchForm="fiber.searchForm"
          :pattern="pattern"
          :openNewPage="false"
          :jumpLink="[{attributeNameUnderline: 'NAME', menuPath: 'fibersource/fiber'}]"
        ></carrying>
      </el-tab-pane>
      <el-tab-pane label="机房" name="机房">
        <roomViewer
          v-if="show4"
          style="height: 68vh"
          :siteId="dataObj.ID"
        ></roomViewer>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import editData from '@/views/publicPage/edit/editData'
import carrying from "@/views/commonPage/carrying/index.vue";
import { getModelId, submitEdit } from '@/api/zdwh/zdzywh'
import { getSdhBySiteId, getOtnBySiteId, getFiberBySiteId } from '@/api/info/index'
import roomViewer from '@/views/site/roomViewer.vue'

export default {
  name: 'index',
  components: {
    editData,carrying,roomViewer
  },
  props: {
    dataObj: Object,
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
      tableName: 'SG_TCCON_TCSITE_B',
      activeName: '站点详情',
      sdh: {
        loading: false,
        tableName: 'SG_TCDEV_TCNESDH_B',
        searchForm: {
          ID: ''
        }
      },
      otn: {
        loading: false,
        tableName: 'SG_TCDEV_TCNEOTN_B',
        searchForm: {
          ID: ''
        }
      },
      fiber: {
        loading: false,
        tableName: 'SG_TCDEV_FIBER_B',
        searchForm: {
          ID: ''
        }
      },
      show1: false,
      show2: false,
      show3: false,
      show4: false
    }
  },
  watch: {},
  created() {
  },
  mounted() {
    this.getData()
  },
  methods: {
    handleClick() {
      if (this.activeName == 'SDH设备列表' && this.show1 == false) {
        this.show1 = true
        getSdhBySiteId({ resId: this.dataObj.ID }).then(res => {
          this.sdh.searchForm.ID = res.msg
          this.$nextTick(() => {
            this.$refs.sdh.getTableData()
          })
        })
      }
      if (this.activeName == 'OTN设备列表' && this.show2 == false) {
        this.show2 = true
        getOtnBySiteId({ resId: this.dataObj.ID }).then(res => {
          this.otn.searchForm.ID = res.msg
          this.$nextTick(() => {
            this.$refs.otn.getTableData()
          })
        })
      }
      if (this.activeName == '光缆列表' && this.show3 == false) {
        this.show3 = true
        getFiberBySiteId({ resId: this.dataObj.ID }).then(res => {
          this.fiber.searchForm.ID = res.msg
          this.$nextTick(() => {
            this.$refs.fiber.getTableData()
          })
        })
      }
      if (this.activeName == '机房' && this.show4 == false) {
        this.show4 = true
      }
    },
    getData() {
      getModelId({ tableName: this.tableName }).then(res => {
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
            //if(this.editForm[key]!==''&&this.editForm[key]!==undefined){
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
            this.$modal.msgSuccess('操作成功')
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
      }, 1500)
    }
  }
}
</script>

<style scoped lang="scss">
</style>
