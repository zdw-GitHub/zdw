<template>
  <div>
    <el-switch style="position: absolute; top: 10px; right: 20px; z-index: 999" v-model="isEditMode" v-if="activeName == '光缆详情'" active-color="#13ce66"></el-switch>
    <el-tabs v-model="activeName" @tab-click="handleClick" @tab-remove="removeTab">
      <el-tab-pane label="光缆详情" name="光缆详情">
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
      <el-tab-pane label="承载光路" name="承载光路">
        <carryingOpticalPath
          v-if="show1"
          style="height: 65vh"
          :id="dataObj.ID"
          :tableName="tableName"
          :pattern="pattern"
          :openNewPage="false"
        >
        </carryingOpticalPath>
      </el-tab-pane>
      <el-tab-pane label="承载业务" name="承载业务">
        <carryingBusiness
          v-if="show2"
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
          v-if="show3"
          style="height: 65vh"
          :id="dataObj.ID"
          :tableName="tableName"
          :pattern="pattern"
          :openNewPage="false"
        >
        </carryingChannelLink>
      </el-tab-pane>
      <el-tab-pane label="光缆段列表" name="光缆段列表">
        <carrying
          v-if="show4"
          v-loading="fiberSeg.loading"
          style="height: 68vh"
          ref="fiberSeg"
          :tableName="fiberSeg.tableName"
          :searchForm="fiberSeg.searchForm"
          :pattern="pattern"
          :jumpLink="[{attributeNameUnderline: 'NAME', menuPath: 'fibersource/zdwh/common/SG_TCDEV_FIBERSEG_B'}]"
          :openNewPage="false"
        ></carrying>
      </el-tab-pane>
      <el-tab-pane label="光缆纤芯" name="光缆纤芯">
        <carrying
          v-if="show5"
          v-loading="fiberLine.loading"
          style="height: 68vh"
          ref="fiberLine"
          :tableName="fiberLine.tableName"
          :searchForm="fiberLine.searchForm"
          :pattern="pattern"
          :jumpLink="[{attributeNameUnderline: 'NAME', menuPath: 'fibersource/zdwh/common/SG_TCDEV_FIBERLINE'}]"
          :openNewPage="false"
        ></carrying>
      </el-tab-pane>
      <el-tab-pane label="光缆纤芯示意图" name="光缆纤芯示意图">
        <gLFiberCoreDetail v-if="show6" ref="fiberCoreDetail" :data="fiberCoreDetail" :isEdit="false">
        </gLFiberCoreDetail>
      </el-tab-pane>
      <el-tab-pane label="关联一次线路" name="关联一次线路">
        <fiberLineRelation
          v-if="show7"
          style="height: 68vh"
          :fiberId="dataObj.ID">
        </fiberLineRelation>
      </el-tab-pane>
      <el-tab-pane label="光缆示意图" name="光缆示意图">
        <div v-if="iframe == 'iframe'" style="width: 100%; height: 60vh">
          <iframe ref="iframe" width="100%" id="iframe1" name="iframe1" style="border: none" height="100%"
            :src="iframeSrc1"></iframe>
        </div>
        <div v-if="iframe == 'iframe2'" style="width: 100%; height: 60vh">
          <iframe ref="iframe2" width="100%" id="iframe2" name="iframe2" style="border: none" height="100%"
            :src="iframeSrc2"></iframe>
          <!--引入topo-->
          <div class="fiberInfoBox" v-if="fiberInfoBox.show">
            <div class="fiberTitle">
              光缆路由详情
              <p class="boxClose el-icon-circle-close" @click="fiberInfoBox.show = false"></p>
            </div>
            <el-tabs v-model="fiberInfoBox.index" type="card">
              <el-tab-pane label="路由" name="0">
                <ul style="padding-bottom: 10px">
                  <template v-for="(item) in fiberInfoBox.data.topoList">
                    <li class="fiberLine" v-if="item.type == 3">
                      <p class="fiberInfo">
                        <span class="Flabel">光缆段名称 : </span><span>{{ item.name }}</span>
                      </p>
                      <p class="fiberInfo">
                        <span class="Flabel">已关联纤芯数 : </span><span>{{ item.linesn }}</span>
                      </p>
                    </li>
                    <li class="fiberSite" v-if="item.type != 3" style="border-left: 4px solid #fff;">
                      <p class="leftIcon" v-show="item.indexType === '起'">{{ item.indexType }}</p>
                      <p class="leftIcon" v-show="item.indexType === '经'">{{ item.indexType }}</p>
                      <p class="leftIcon" v-show="item.indexType === '终'">{{ item.indexType }}</p>
                      <p class="fiberInfo">
                        <span class="Flabel">{{ item.label }}</span><span v-if="item.type != 1">{{ item.name }}</span>
                      </p>
                    </li>
                  </template>
                </ul>
              </el-tab-pane>
              <el-tab-pane label="资源" name="1">
                <div style="padding-top: 10px; height: 100%">
                  <el-table border stripe size="mini" :data="fiberInfoBox.data.resList" height="100%"
                    style="width: 100%;">
                    <el-table-column prop="resName" label="资源名称">
                    </el-table-column>
                    <el-table-column prop="resType" label="资源类型" width="80">
                    </el-table-column>
                    <el-table-column prop="editType" label="操作类型" width="50">
                    </el-table-column>
                  </el-table>
                </div>
              </el-tab-pane>
            </el-tabs>
          </div>
        </div>
      </el-tab-pane>
      <el-tab-pane label="断点定位" name="断点定位">
        <fiberRFbrconnect
          v-if="show9"
          style="height: 68vh;"
          :fiberId="associatedConnectorBox.id"
          :aResobjid="associatedConnectorBox.aResobjid"
          :zResobjid="associatedConnectorBox.zResobjid"
          :fiberLength="associatedConnectorBox.fiberLength"
          :openNewPage="false"
          :jFlag="true"

        >
        </fiberRFbrconnect>
      </el-tab-pane>
      <el-tab-pane label="接头盒维护" name="接头盒维护">
        <fiberRFbrconnect
          v-if="show9"
          style="height: 68vh;"
          :fiberId="associatedConnectorBox.id"
          :aResobjid="associatedConnectorBox.aResobjid"
          :zResobjid="associatedConnectorBox.zResobjid"
          :fiberLength="associatedConnectorBox.fiberLength"
          :openNewPage="false"
          :jFlag="false"
        >
        </fiberRFbrconnect>
      </el-tab-pane>
      <el-tab-pane v-for="(panel, index) in panels" :key="index" :label="panel.label" :name="panel.name" type="card"
        closable>
        <editData :ref="'editData-' + panel.name" :modelObj="curModelObj" :dataObj="curDataObj"
          :customStyle="{ height:'70vh' }"
          :customClass="pattern ? ['el-dialog-div'] : ['el-dialog-div', 'oneLineOne']">
        </editData>
      </el-tab-pane>
    </el-tabs>

    <!--光缆示意图查看框-->
    <el-dialog v-dialogDrag custom-class="curdBox" :title="editBox2.title" :modal="false" :close-on-click-modal="false"
      :visible.sync="editBox2.show" width="750px">
      <editData ref="child2" :modelObj="editBox2.modelObj" :dataObj="editBox2.data" :type="editBox2.editType"
        :dbName="'`ry-middledb`'"></editData>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editBox2.show = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!--纤芯接续-->
    <el-dialog v-dialogDrag title="纤芯接续" top="5vh" :visible.sync="coreConnectBox.show" :modal="false"
      :close-on-click-modal="false" width="70%">
      <div style="width: 100%; height: 70vh;" v-loading="coreConnectBox.loading">
        <iframe ref="coreConnectBoxIframe" width="100%" height="100%" id="coreConnectBoxIframe"
          name="coreConnectBoxIframe" style="border: none;" :src="coreConnectBox.iframeSrc"></iframe>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="coreConnectBox.show = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { nameTo_, nameToTuoFeng } from "@/utils"
import { submitEdit, getFiberApprovalID, getFiberRouter } from "@/api/manageFiber/fiber";
import { getSegIdsByFiberId } from "@/api/manageFiber/fiberSeg";
import { getFiberCoreDetailApi, list, getTaskName } from "@/api/gltxh/gltxhbj";
import editData from "@/views/publicPage/edit/editData";
import carryingOpticalPath from "@/views/lightPathInfoManage/carryingOpticalPath.vue";
import carryingBusiness from "@/views/serviceInformation/carryingBusiness.vue";
import carryingChannelLink from "@/views/serviceInformation/carryingChannelLink.vue";
import carrying from "@/views/commonPage/carrying/index.vue";
import gLFiberCoreDetail from "@/views/components/gltxh/gLFiberCoreDetail.vue"
import fiberLineRelation from "@/views/manageFiber/fiberLineRelation/index.vue";
import fiberRFbrconnect from '@/views/manageFiber/fiberRFbrconnect/index'
import { getModelId, getForm, getNewIdByOriginalId, getData } from "@/api/zdwh/zdzywh";
import { getFiberDataApi, coreConnectShowApi } from '@/api/gltxh/coreConnect'

export default {
  name: "index",
  components: {
    fiberLineRelation, carrying, carryingChannelLink, carryingBusiness, carryingOpticalPath, editData, gLFiberCoreDetail, fiberRFbrconnect
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

      panels: [

      ],


      modelObj: {},
      tableName: 'SG_TCDEV_FIBER_B',
      activeName: '光缆详情',
      fiberSeg: {
        loading: false,
        tableName: 'SG_TCDEV_FIBERSEG_B',
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
      fiberCoreDetail: {},

      iframe: '',
      taskId: '',
      iframeSrc1: '/stationWh/manageFiberHtml/fiberImaging.html',
      iframeSrc2: '/stationWh/html/fiberSketchMap.html',
      // 纤芯接续
      coreConnectBox: {
        id: '',
        show: false,
        loading: true,
        iframeSrc: '/stationWh/html/coreConnect.html',
        groupsData: [],
        nodesData: [],
        linksData: [],
        resData: [],
      },
      fiberInfoBox: {
        index: '0',
        show: false,
        data: {
          topoList: [],
          resList: []
        }
      },
      editBox2: {
        title: '',
        data: {},
        type: {},
        editType: '',
        show: false,
        modelObj: {}
      },

      //关联接头盒
      associatedConnectorBox: {
        title: '',
        id: '',
        fiberLength: '',
        show: false
      },
      show1: false,
      show2: false,
      show3: false,
      show4: false,
      show5: false,
      show6: false,
      show7: false,
      show8: false,
      show9: false,
    }
  },
  watch: {},
  created() {
    // 纤芯接续
    window['coreConnect'] = (data) => {
      this.coreConnectShow(data)
    }
    window['getFiberData'] = (id) => {
      this.getFiberData(id)
    }
    // 查看topo元素
    window['editTopoData'] = (data) => {
      this.editTopoData(data)
    }
  },
  mounted() {
    this.getData()
  },
  methods: {
    addPanel(payload) {
      console.log(payload);
      this.tableName = 'SG_TCCON_TCSITE_B'
      this.curDataObj = { ID: '602013000000004394' }
      this.curModelObj = {
        "modelId": "322327895743934464",
        "modelVersion": "edit",
        "tableName": "SG_TCCON_TCSITE_B"
      }
      const existingTab = this.panels.find(tab => tab.name === payload.rowData);
      // this.curModelId = {mode==========lObj:'322364301111472128'}
      if (existingTab) {
        this.activeName = payload.rowData;
      } else {
        this.panels.push({
          name: payload.rowData,
          label: payload.rowData,
          isShow: true
        });
        this.activeName = payload.rowData;
        getModelId({ tableName: this.tableName }).then(res => {
            this.modelObj = res.data
            this.$nextTick(() => {
            const refName = 'editData-' + payload.rowData;
            console.log(this.$refs[refName]);
            this.$refs[refName].beforeLoading()
          })

      })
      }
    },

    removeTab(targetName) {
      let tabs = this.panels;
      let curActiveName = this.activeName;
      if (curActiveName === targetName) {
        tabs.forEach((tab, index) => {
          if (tab.name === targetName) {
            let nextTab = tabs[index + 1] || tabs[index - 1];
            if (nextTab) {
              curActiveName = nextTab.name;
            }
          }
        });
      }
      this.activeName = curActiveName;
      this.panels = tabs.filter(tab => tab.name !== targetName);
    },

    handleClick() {
      if (this.activeName == '承载光路' && this.show1 == false) {
        this.show1 = true
      }
      if (this.activeName == '承载业务' && this.show2 == false) {
        this.show2 = true
      }
      if (this.activeName == '承载通道' && this.show3 == false) {
        this.show3 = true
      }
      if (this.activeName == '光缆段列表' && this.show4 == false) {
        this.show4 = true
        getSegIdsByFiberId({ fiberId: this.dataObj.ID }).then(res => {
          this.fiberSeg.searchForm.ID = res.msg
          this.$nextTick(() => {
            this.$refs.fiberSeg.getTableData()
          })
        })
      }

      if (this.activeName == '光缆纤芯' && this.show5 == false) {
        this.show5 = true
        this.fiberLine.searchForm.PAR_FIBER = this.dataObj.ID
        this.$nextTick(() => {
          this.$refs.fiberLine.getTableData()
        })
      }

      if (this.activeName == '光缆纤芯示意图' && this.show6 == false) {
        this.show6 = true
        getFiberCoreDetailApi({ fiberId: this.dataObj.ID }).then(res => {
          this.fiberCoreDetail = res.data
          this.$nextTick(() => {
            this.$refs.fiberCoreDetail.locationLine()
          })
        })
      }
      if (this.activeName == '关联一次线路' && this.show7 == false) {
        this.show7 = true
      }
      if (this.activeName == '光缆示意图' && this.show8 == false) {
        this.show8 = true
        getFiberApprovalID({ id: this.dataObj.ID }).then(res => {
          if (res.msg) {
            this.taskId = res.msg
            this.iframe = 'iframe2'
            list({ taskId: res.msg }).then(res => {
              if (res.code == 200) {
                setTimeout(() => {
                  this.$refs.iframe2.contentWindow.reSetTopo(
                    res.data.site.concat(res.data.tcon),
                    res.data.seg,
                    res.data.fiber,
                    false
                  )
                }, 1000)
              }
            })
            return
          }
        })
        this.iframe = 'iframe'
        let params = {
          modelId: this.modelObj.modelId,
          isPage: '0',
          whereAttributes: [{
            attributeName: 'ID',
            attributeValue: this.dataObj.ID,
            isPrimaryKey: '1'
          }]
        }
        getForm(params).then(res => {
          let data = {}
          for (let key in res.data) {
            data[nameToTuoFeng(key)] = res.data[key]
          }
          getFiberRouter(data).then(res1 => {
            let node = []
            let link = []
            for (let i = 0; i < res1.data.length; i++) {
              let item = res1.data[i]
              if (item.type == 3 && item.id != null) {
                item.aId = res1.data[i - 1].id
                item.zId = res1.data[i + 1].id
                link.push(item)
              } else {
                node.push(item)
              }
            }
            this.$nextTick(() => {
              window.setTimeout(() => {
                this.$refs.iframe.contentWindow.reSetTopo(node, link)
              }, 1000)
            })
          })
        })
      }
      if (this.activeName == '断点定位') {
        this.show9 = false
        let params = {
          modelId: this.modelObj.modelId,
          isPage: "1",
          pageIndex: 1,
          pageSize: 15,
          orderAttributes: [],
          whereAttributes: [
            { attributeName: 'ID', attributeValue: this.dataObj.ID }
          ]
        }
        getData(params).then(res => {
          this.associatedConnectorBox.id = this.dataObj.ID
          this.associatedConnectorBox.fiberLength = res.rows[0].FIBER_LENGTH
          this.associatedConnectorBox.aResobjid = res.rows[0].A_RESOBJID
          this.associatedConnectorBox.zResobjid = res.rows[0].Z_RESOBJID
          this.show9 = true
        })
      }

      if (this.activeName == '接头盒维护') {
        this.show9 = false
        let params = {
          modelId: this.modelObj.modelId,
          isPage: "1",
          pageIndex: 1,
          pageSize: 15,
          orderAttributes: [],
          whereAttributes: [
            { attributeName: 'ID', attributeValue: this.dataObj.ID }
          ]
        }
        getData(params).then(res => {
          this.associatedConnectorBox.id = this.dataObj.ID
          this.associatedConnectorBox.fiberLength = res.rows[0].FIBER_LENGTH
          this.associatedConnectorBox.aResobjid = res.rows[0].A_RESOBJID
          this.associatedConnectorBox.zResobjid = res.rows[0].Z_RESOBJID
          this.show9 = true
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
          submitEdit(params).then(response => {
            this.$modal.msgSuccess("操作成功");
            this.$bus.$emit('getTableDataFiber')
          })
        })
      }, 1500);
    },

    coreConnectShow(data) {
      this.coreConnectBox.show = true
      this.coreConnectBox.loading = true
      coreConnectShowApi({ TconId: data.id, TaskId: this.taskId }).then(res => { // +个任务id
        this.coreConnectBox.groupsData = []
        this.coreConnectBox.nodesData = []
        this.coreConnectBox.linksData = []
        this.coreConnectBox.id = data.id
        this.coreConnectBox.loading = false
        res.data.seg.forEach((item, index, arr) => {
          this.coreConnectBox.groupsData[index] = {
            SEGID: item.SEGID,
            SEGNAME: item.SEGNAME,
            groupLength: item.lines.length,
          }
          this.coreConnectBox.nodesData[index] = item.lines
        })
        this.coreConnectBox.linksData = res.data.link
        this.coreConnectBox.loading = false
        this.$nextTick(() => {
          window.setTimeout(() => {
            this.$refs.coreConnectBoxIframe.contentWindow.reSetTopo(
              this.coreConnectBox.groupsData,
              this.coreConnectBox.nodesData,
              this.coreConnectBox.linksData,
              this.isEdit
            )
          }, 500)
        })
      })
    },
    getFiberData(id) {
      getFiberDataApi({
        fiberId: id,
        taskId: this.taskId
      }).then(res => {
        this.$refs.iframe2.contentWindow.topoShowFiber(res.data.topoList)
        this.fiberInfoBox.data.resList = res.data.resList
        this.fiberInfoBox.fiberId = id
        let topoList = res.data.topoList
        for (let i = 0; i < topoList.length; i++) {
          if (topoList[i].type != 3) {
            topoList[i].label = '接头盒'
            topoList[i].indexType = '经'
          }
        }
        topoList[0].label = '起始站点 : '
        topoList[0].indexType = '起'
        topoList[topoList.length - 1].indexType = '终'
        topoList[topoList.length - 1].label = '终止站点 : '
        this.fiberInfoBox.data.topoList = topoList
        this.fiberInfoBox.show = true
      })
    },
    async editTopoData(data) {
      let tit = '查看'
      this.editBox2 = {
        title: tit + '  ' + JSON.parse(JSON.stringify(data)).resName,
        data: {},
        type: data.resType,
        show: true,
        modelObj: this.editBox2.modelObj
      }
      if (data.editType == '0') {
        this.editBox2.editType = undefined
      } else {
        this.editBox2.editType = "`ry-middledb`"
      }
      getTaskName({ resType: this.editBox2.type }).then(res1 => {
        getModelId({ tableName: res1.msg }).then(res2 => {
          getNewIdByOriginalId({ tableName: res1.msg, originalId: data.id }).then(res3 => {
            if (res3.msg) {
              this.editBox2.editType = undefined
              data.id = res3.msg
            }
            this.editBox2.modelObj = res2.data
            for (let key in data) {
              this.editBox2.data[nameTo_(key)] = data[key]
            }
            this.$nextTick(() => {
              this.$refs.child2.modelObj = this.editBox2.modelObj
              this.$refs.child2.dataObj = this.editBox2.data
              this.$refs.child2.type = this.editBox2.editType
              this.$refs.child2.beforeLoading()
            })
          })


        })
      })
    },
  },
}
</script>

<style scoped lang="scss" >
.fiberInfoBox {
  position: absolute;
  top: 92px;
  right: 10px;
  border: 2px solid #eeeeee;
  width: 300px;
  height: 50vh;
  background-color: #fff;

  .fiberTitle {
    width: 100%;
    height: 45px;
    line-height: 45px;
    background-color: #fff;
    padding-left: 10px;

    .boxClose {
      width: 25px;
      height: 25px;
      line-height: 45px;
      font-size: 25px;
      float: right;
      margin: 0 10px 0 0;
      cursor: pointer;
    }

    .boxClose:hover {
      color: #047E78;
    }
  }

  .el-tabs {
    width: 100%;
    height: calc(100% - 45px);
    padding: 10px;

    .el-tabs__header,
    .el-tabs__nav-wrap,
    .el-tabs__nav-scroll,
    .el-tabs__nav,
    .el-tabs__item {
      height: 30px;
      line-height: 30px;
      margin: 0;
    }

    .is-active {
      color: #047E78 !important;
    }

    .el-tabs__content {
      height: calc(100% - 30px);
      overflow-y: auto;
    }

    .el-tab-pane {
      height: 100%
    }

    ul {
      margin: 0;
      list-style: none;
      padding: 10px 0 0 20px;

      li {
        width: 100%;
        position: relative;
        border-left: 4px solid #67C23A;
        padding: 0px 0 10px 20px;

        p {
          margin: 0;
        }

        .leftIcon {
          position: absolute;
          left: -17px;
          width: 30px;
          height: 30px;
          background-color: #67C23A;
          border-radius: 50%;
          text-align: center;
          line-height: 30px;
          color: #fff;
          z-index: 10;
        }

        .fiberInfo {
          width: 100%;
          line-height: 20px;
          font-size: 14px !important;

          .Flabel {
            font-weight: bold;
            display: inline-block;
            width: 6rem;
          }
        }
      }

      .fiberSite .fiberInfo {
        line-height: 20px !important;
      }

      li:last-child {
        padding-bottom: 0;
      }
    }
  }
}
</style>
