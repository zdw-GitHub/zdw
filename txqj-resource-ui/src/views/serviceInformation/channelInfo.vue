<template>
  <div style="position: relative">
    <el-switch style="position: absolute; top: 10px; right: 20px; z-index: 999" v-model="isEditMode"
               v-if="activeName == '链路详情'" active-color="#13ce66"
    ></el-switch>

    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="链路详情" name="链路详情">
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
      <el-tab-pane label="通道路由" name="通道路由">
        <div class="buzTopo" v-if="show1 == 2" v-loading="loading">
          <div class="topo" :style="{ height: tabShow ? '50%' : 'calc(100% - 30px)' }">
            <iframe ref="iframe" width="100%" id="iframe1" name="iframe1" style="border: none" height="100%"
                    :src="iframeSrc"></iframe>
          </div>
          <div class="tab" :style="{ height: tabShow ? '50%' : '30px' }">
            <p class="tabTit">
              路由信息
              <span class="tabClose" :class="{
              'el-icon-arrow-down': tabShow,
              'el-icon-arrow-up': !tabShow
            }" @click="tabShow = !tabShow"></span>
            </p>
            <div class="tabCon" v-if="tabShow">
              <el-table stripe border height="100%" :data="selLightPathTable" style="width: 100%">
                <el-table-column type="index" label="序号" width="80" align="center">
                </el-table-column>
                <el-table-column prop="name" label="光路名称" min-width="350" align="center">
                </el-table-column>
                <el-table-column prop="aResId" label="起始设备" width="250" align="center">
                </el-table-column>
                <el-table-column prop="aPort" label="起始端口" width="100" align="center">
                </el-table-column>
                <el-table-column prop="zResId" label="终止设备" width="250" align="center">
                </el-table-column>
                <el-table-column prop="zPort" label="终止端口" width="100" align="center">
                </el-table-column>
              </el-table>
            </div>
          </div>
        </div>
        <div class="channelTopo" v-if="show1 == 1" v-loading="loading" >
          <div class="topo" :style="{height: tabShow ? '50%' : 'calc(100% - 30px)'}">
            <iframe
              ref="iframe"
              width="100%"
              id="iframe1"
              name="iframe1"
              style="border: none"
              height="100%"
              :src="iframeSrc2"
            ></iframe>
          </div>
          <div class="tab" :style="{height: tabShow ? '50%' : '30px'}">
            <p class="tabTit">
              路由信息
              <span
                class="tabClose"
                :class="{
            'el-icon-arrow-down' : tabShow,
            'el-icon-arrow-up' : !tabShow
          }"
                @click="tabShow = !tabShow"
              ></span>
            </p>
            <div class="tabCon" v-if="tabShow">
              <el-table
                stripe
                border
                height="100%"
                :data="selLightPathTable"
                style="width: 100%">
                <el-table-column
                  type="index"
                  label="序号"
                  width="80"
                  align="center">
                </el-table-column>
                <el-table-column
                  prop="aResName"
                  label="起始站点"
                  width="250"
                  align="center">
                </el-table-column>
                <el-table-column
                  prop="zResName"
                  label="终止站点"
                  width="250"
                  align="center">
                </el-table-column>
                <el-table-column
                  prop="fiberName"
                  label="光缆名称"
                  min-width="350"
                  align="center">
                </el-table-column>
                <el-table-column
                  prop="lineName"
                  label="纤芯名称"
                  width="100"
                  align="center">
                </el-table-column>
              </el-table>
            </div>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import editData from '@/views/publicPage/edit/editData'
import carryingOpticalPath from "@/views/lightPathInfoManage/carryingOpticalPath.vue";
import carryingChannelLink from "@/views/serviceInformation/carryingChannelLink.vue";
import carrying from "@/views/commonPage/carrying/index.vue";
import { getModelId, submitEdit, getForm } from '@/api/zdwh/zdzywh'
import { getChannelTopoApi } from '@/api/serviceInformation/channel'
import { getRouterApi } from '@/api/serviceInformation/fiberLine'

export default {
  name: 'index',
  components: {
    editData,carryingOpticalPath,carryingChannelLink,carrying
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
      tableName: 'SG_TCCON_TCCHANNEL_B',
      activeName: '链路详情',
      iframeSrc: '/stationWh/serve/channel.html',
      iframeSrc2: '/stationWh/serve/fiberLine.html',
      loading: true,
      tabShow: true,
      selLightPathTable: [],
      topoData: {},
      show1: 0
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
      if (this.activeName ==  '通道路由' && this.show1 == 0){
        let params={
          modelId:this.modelObj.modelId,
          isPage:'0',
          whereAttributes:[
            {
              attributeName: 'ID',
              attributeValue:this.dataObj.ID,
              isPrimaryKey:'1'
            }
          ]
        }
        getForm(params).then(res=>{
          this.loading = true
          if(res.data.CHANNEL_TYPE == '1800'){
            this.show1 = 1
            this.selLightPathTable = []
            this.topoData = {}
            this.lightPathBox = {
              tabData: [],
              selData: {},
              show: false
            }
            getRouterApi({ChannelsegId: this.dataObj.ID}).then(res => {
              this.topoData.node = res.data.nodesData
              this.topoData.link = res.data.optRoadData
              this.selLightPathTable = res.data.optRoadData
              for (let i = 0; i < this.topoData.link.length; i++) {
                this.topoData.link[i].name = this.topoData.link[i].fiberName + ' ' + this.topoData.link[i].lineName
              }
              this.$nextTick(() => {
                this.loading = false
                setTimeout(() => {
                  this.$refs.iframe.contentWindow.reSetTopo(
                    this.topoData.node,
                    this.topoData.link,
                  )
                }, 500)
              })
            })
          }else {
            this.show1 = 2
            this.selLightPathTable = []
            this.topoData = {}
            getChannelTopoApi({ channelId: this.dataObj.ID }).then(res => {
              this.topoData.group = res.data.groupsData
              this.topoData.node = res.data.nodesData
              this.topoData.link = res.data.optRoadData
              this.selLightPathTable = res.data.optRoadData.filter(p => p.deleteFlag != 1);
              this.$nextTick(() => {
                this.loading = false
                setTimeout(() => {
                  this.$refs.iframe.contentWindow.reSetTopo(
                    this.topoData.group,
                    this.topoData.node,
                    this.topoData.link,
                    res.data.aPortId,
                    res.data.zPortId,
                  )
                }, 500)
              })
            })
          }
        })
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
          })
        })
      }, 1500)
    }
  }
}
</script>

<style scoped lang="scss">
.buzTopo{
  height: 65vh;
  .topo{
    height: 50%;
  }
  .tab{
    height: 50%;
    box-shadow: 0px -2px 0px #eee;
    .tabTit{
      color: #f0f0f0;
      width: 100%;
      height: 40px;
      line-height: 40px;
      font-size: 16px;
      padding: 0 10px;
      background-color: #0f9ea2;
      .tabClose{
        font-size: 24px;
        line-height: 40px;
        float: right;
        cursor: pointer;
        &:hover{
          color: #409EFF;
        }
      }
    }
    .tabCon {
      width: 100%;
      height: calc(100% - 40px);
    }
  }
}
.channelTopo{
  height: 65vh;
  .topo{
    height: 50%;
  }
  .tab{
    height: 50%;
    box-shadow: 0px -2px 0px #eee;
    .tabTit{
      color: #f0f0f0;
      width: 100%;
      height: 40px;
      line-height: 40px;
      font-size: 16px;
      background-color: #0f9ea2;
      padding: 0 10px;
      .tabClose{
        font-size: 24px;
        line-height: 40px;
        float: right;
        cursor: pointer;
        &:hover{
          color: #409EFF;
        }
      }
    }
    .tabCon {
      width: 100%;
      height: calc(100% - 40px);
    }
  }
}
</style>
