<template>
  <div style="position: relative">
    <el-switch style="position: absolute; top: 10px; right: 20px; z-index: 999" v-model="isEditMode"
               v-if="activeName == '光路详情' || activeName == '路由维护' ||activeName == '配线连接'"
               active-color="#13ce66"></el-switch>

    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="光路详情" name="光路详情">
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
      <el-tab-pane label="承载业务" name="承载业务">
        <carryingBusiness
          v-if="show3"
          style="height: 65vh"
          :id="dataObj.ID"
          :tableName="tableName"
          :pattern="pattern"
          :openNewPage="false"
        >
        </carryingBusiness>
      </el-tab-pane>
      <el-tab-pane label="承载通道" name="承载通道">
        <carryingChannelLink
          v-if="show6"
          style="height: 65vh"
          :id="dataObj.ID"
          :tableName="tableName"
          :pattern="pattern"
          :openNewPage="false"
        >
        </carryingChannelLink>
      </el-tab-pane>
      <el-tab-pane label="光缆列表" name="光缆列表">
        <carrying
          v-if="show7"
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
      <el-tab-pane label="路由维护" name="路由维护">
        <setLightPath
          v-if="show1"
          ref="setLightPath"
          :style="{height:isEditMode ? '60vh' : '65vh',position: 'relative'}"
          :parId="dataObj.ID"
        ></setLightPath>
        <div class="dialog-footer" v-if="isEditMode">
          <el-button style="float: right;margin: 10px" type="primary" :loading="commitLoading1" @click="commitRouter()">
            确 定
          </el-button>
        </div>
      </el-tab-pane>
      <el-tab-pane label="配线连接" name="配线连接">
        <setPhyLink
          v-if="show2"
          :style="{height:isEditMode ? '60vh' : '65vh'}"
          :routerId="dataObj.ID"
          ref="setPhyLink"
        ></setPhyLink>
        <div class="dialog-footer" v-if="isEditMode">
          <el-button style="float: right;margin: 10px" type="primary" :loading="commitLoading2"
                     @click="commitPhyLink()">确 定
          </el-button>
        </div>
      </el-tab-pane>
      <el-tab-pane label="对应光缆纤芯" name="对应光缆纤芯">
        <carrying
          v-if="show4"
          v-loading="fiberLine.loading"
          style="height: 68vh"
          ref="fiberLine"
          :tableName="fiberLine.tableName"
          :searchForm="fiberLine.searchForm"
          :pattern="pattern"
          :openNewPage="false"
        ></carrying>
      </el-tab-pane>
      <el-tab-pane label="对应光缆段纤芯" name="对应光缆段纤芯">
        <carrying
          v-if="show5"
          v-loading="fiberSegLine.loading"
          style="height: 68vh"
          ref="fiberSegLine"
          :tableName="fiberSegLine.tableName"
          :searchForm="fiberSegLine.searchForm"
          :pattern="pattern"
          :openNewPage="false"
        ></carrying>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import {submitEdit} from "@/api/zdwh/zdzywh";
import editData from "@/views/publicPage/edit/editData";
import carryingBusiness from "@/views/serviceInformation/carryingBusiness.vue";
import carryingChannelLink from "@/views/serviceInformation/carryingChannelLink.vue";
import carrying from "@/views/commonPage/carrying/index.vue";
import {getModelId} from "@/api/zdwh/zdzywh";
import setLightPath from "@/views/lightPathInfoManage/setLightPath.vue";
import setPhyLink from "@/views/lightPathInfoManage/setPhyLink.vue";
import {saveTheOpticalWiringConnectionRelationship} from "@/api/lightPathInfoManage/setPhyLink";
import {commitRouterApi} from "@/api/lightPathInfoManage";
import { getFiberByGlId } from '@/api/info/index'
export default {
  name: "index",
  components: {
    setPhyLink,
    setLightPath,
    carrying,
    carryingBusiness,
    carryingChannelLink,
    editData
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
      tableName: 'SG_TCCON_TCOPTICALROAD_B',
      activeName: '光路详情',
      fiberSegLine: {
        loading: false,
        tableName: 'SG_TCDEV_FIBERLINE_B',
        searchForm: {
          ID: '',
        },
      },
      fiberLine: {
        loading: false,
        tableName: 'SG_TCDEV_FIBERLINE',
        searchForm: {
          PAR_FIBER: '',
        },
      },
      fiber: {
        loading: false,
        tableName: 'SG_TCDEV_FIBER_B',
        searchForm: {
          ID: ''
        }
      },
      fiberCoreDetail: {},
      show1: false,
      show2: false,
      show3: false,
      show4: false,
      show5: false,
      show6: false,
      show7: false,
      commitLoading1: false,
      commitLoading2: false,
    }
  },
  watch: {},
  mounted() {
    this.getData()
  },
  methods: {
    handleClick() {
      if (this.activeName == '路由维护' && this.show1 == false) {
        this.show1 = true
      }
      if (this.activeName == '配线连接' && this.show2 == false) {
        this.show2 = true
      }
      if (this.activeName == '承载业务' && this.show3 == false) {
        this.show3 = true
      }
      if (this.activeName == '对应光缆纤芯' && this.show4 == false) {
        this.show4 = true
        this.fiberLine.searchForm.OPTICALROAD_ID = this.dataObj.ID
        this.$nextTick(() => {
          this.$refs.fiberLine.getTableData()
        })
      }
      if (this.activeName == '对应光缆段纤芯' && this.show5 == false) {
        this.show5 = true
        this.fiberSegLine.searchForm.OPTICALROAD_ID = this.dataObj.ID
        this.$nextTick(() => {
          this.$refs.fiberSegLine.getTableData()
        })
      }
      if (this.activeName ==  '承载通道' && this.show6 == false){
        this.show6 = true
      }
      if (this.activeName == '光缆列表' && this.show7 == false) {
        this.show7 = true
        getFiberByGlId({ resId: this.dataObj.ID }).then(res => {
          this.fiber.searchForm.ID = res.msg
          this.$nextTick(() => {
            this.$refs.fiber.getTableData()
          })
        })
      }
    },
    getData() {
      getModelId({tableName: this.tableName}).then(res => {
        this.modelObj = res.data
        this.$refs.child.modelObj = this.modelObj
        this.$refs.child.dataObj = this.dataObj
        this.$refs.child.beforeLoading()
      })
    },
    //修改提交
    submitForm() {
      //调用子组件重复校验方法
      this.$refs.child.beforeSubmit()
      let editForm = this.$refs.child.pageForm
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
          for (let key in editForm) {
            let obj = {}
            if (editForm[key] !== undefined) {
              if (editForm[key] == '') {
                obj = {
                  attributeName: key,
                  attributeValue: null
                }
              } else {
                obj = {
                  attributeName: key,
                  attributeValue: editForm[key]
                }
              }
              if (key === this.$refs.child.keyClu) {
                obj.isPrimaryKey = 1
              }
              params.valueAttributes.push(obj)
            }
          }
          submitEdit(params).then(response => {
            this.$modal.msgSuccess("操作成功");
            this.$bus.$emit('getTableDataGL')
          })
        })
      }, 1500);
    },
    commitRouter() {
      let data = this.$refs.setLightPath.tableCommit()
      if (data) {
        this.commitLoading1 = true
        commitRouterApi({
          routerId: this.dataObj.ID,
          tableData: data.tableData,
          isJumpFiber: data.isJumpFiber,
          llId: data.llId,
        }).then(res => {
          this.$modal.msgSuccess(res.msg);
          this.commitLoading1 = false
        }).catch(e => {
          this.commitLoading1 = false
        })
      }
    },
    commitPhyLink() {
      let data = this.$refs.setPhyLink.commitPhyLink()
      if (data) {
        this.commitLoading2 = true
        saveTheOpticalWiringConnectionRelationship(data, {routerId: this.dataObj.ID}).then(res => {
          this.commitLoading2 = false
          this.$modal.msgSuccess("操作成功");
        }).catch(e => {
          this.commitLoading2 = false
        })
      }
    }
  },
}
</script>

<style scoped lang="scss">
</style>
