<template>
  <div>
    <zdzywhPage :showTree="false" ref="zdzywhPage" :tableName="tableName" :operate="{
      slot: 'handle',
      label: '操作',
      width: '200',
    }" :tabOperate="true" :rightClickMenus="[
  { name: '承载光路', icon: 'el-icon-upload2', functionName: 'getCarryingOpticalPath' },
  { name: '承载光路导出', icon: 'el-icon-printer', functionName: 'carryingOpticalPathExport' },
  { name: '承载业务', icon: 'el-icon-paperclip', functionName: 'getCarryingBusiness' },
  { name: '承载业务导出', icon: 'el-icon-printer', functionName: 'carryingBusinessExport' },
  { name: '承载通道链路', icon: 'el-icon-link', functionName: 'getCarryingChannelLink' },
  { name: '断点定位', icon: 'el-icon-circle-close', functionName: 'getAssociatedConnectorBox' },
  { name: '接头盒维护', icon: 'el-icon-aim', functionName: 'getAssociatedConnectorBox' },
]" @getCarryingOpticalPath="getCarryingOpticalPath" @carryingOpticalPathExport="carryingOpticalPathExport"
      @getCarryingBusiness="getCarryingBusiness" @carryingBusinessExport="carryingBusinessExport"
      @getCarryingChannelLink="getCarryingChannelLink" @getAssociatedConnectorBox="getAssociatedConnectorBox"
      @getSearchCriteria="getSearchCriteria">
      <template v-slot:tabOperate>
        <el-button size="mini" @click="toPendingFiber" style=" display: none;">待处理光缆</el-button>
        <el-button size="mini" type="primary" @click="openEditBox('add', {})" v-hasPermi="['system:b:add']">新增</el-button>
        <el-button size="mini" type="danger" @click="deleteSta()" v-hasPermi="['system:b:remove']">删除</el-button>
        <el-button size="mini" @click="handleExport()">导出</el-button>
        <!-- <el-button size="mini" @click="opticalCableLoadBearingLightPath()">导出光缆承载光路</el-button> -->
        <el-button size="mini" @click="openWindow" icon="el-icon-full-screen">全屏</el-button>
      </template>
      <template v-slot:handle="slot">
        <el-button type="primary" size="mini" @click="openFiberInfo(true, slot.scope.row)"
          v-hasPermi="['system:b:update']">编辑</el-button>
        <el-button type="primary" size="mini" @click="openFiberInfo(false, slot.scope.row)"
          v-hasPermi="['system:b:list']">查看</el-button>
        <el-button type="danger" size="mini" @click="delData(slot.scope.row)"
          v-hasPermi="['system:b:remove']">删除</el-button>
        <el-dropdown style="margin-left: 5px" trigger="click">
          <span class="el-dropdown-link"><i class="el-icon-arrow-down el-icon--right"></i></span>
          <el-dropdown-menu slot="dropdown">
            <!-- <el-dropdown-item @click.native="toFiberSeg(slot.scope.row)">光缆段基本信息</el-dropdown-item>
            <el-dropdown-item @click.native="fiberRouter(slot.scope.row)">光缆示意图</el-dropdown-item>
            <el-dropdown-item @click.native="toFiberLine(slot.scope.row)">光缆纤芯</el-dropdown-item>
            <el-dropdown-item @click.native="openFiberCoreDetail(slot.scope.row)">光缆纤芯示意图</el-dropdown-item>
            <el-dropdown-item @click.native="fiberLineRelationShow(slot.scope.row)">对应一次线路</el-dropdown-item> -->
          </el-dropdown-menu>
        </el-dropdown>
      </template>
    </zdzywhPage>
    <!--光缆示意图-->
    <el-dialog v-dialogDrag :modal="false" title="光缆示意图" :visible.sync="FiberRouterDialogShow" width="70%" top="10vh"
      append-to-body :close-on-click-modal="false">
      <div v-if="iframe == 'iframe'" style="width: 100%; height: 70vh">
        <iframe ref="iframe" width="100%" id="iframe1" name="iframe1" style="border: none" height="100%"
          :src="iframeSrc1"></iframe>
      </div>
      <div v-if="iframe == 'iframe2'" style="width: 100%; height: 70vh">
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
                <el-table border stripe size="mini" :data="fiberInfoBox.data.resList" height="100%" style="width: 100%;">
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
      <div slot="footer" class="dialog-footer">
        <el-button v-if="iframe == 'iframe'" :loading="createFiberLineLoading" type="primary"
          @click="createFiberLine()">重新生成光缆纤芯</el-button>
        <el-button v-if="iframe == 'iframe2'" type="primary" @click="saveTemporary">保 存</el-button>
        <el-button @click="FiberRouterDialogShow = false">关 闭</el-button>
      </div>
    </el-dialog>
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
    <!--批量连接纤芯-->
    <el-dialog v-dialogDrag :modal="false" :title="batchConnectionsBox.title + '  批量连接纤芯'" top="15%"
      :visible.sync="batchConnectionsBox.show" :close-on-click-modal="false" width="30%">
      <div style="width: 100%; height: 50vh">
        <el-form :model="batchConnectionsBox.data" :rules="batchConnectionsBox.rules" ref="batchConnectionsBox"
          label-width="150px">
          <el-form-item label="起始端" prop="aSegId">
            <el-select v-model="batchConnectionsBox.data.aSegId"
              @change="segIdChange($event, 'aSegId'), verificationBatchConnectionsBox()">
              <el-option v-for="(item, key) in batchConnectionsBox.selectOption" :label="item.SEGNAME" :value="item.SEGID"
                :disabled="item.SEGID == batchConnectionsBox.data.zSegId" :key="key"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="起始纤芯序号 : " prop="aStartSn">
            <el-input-number v-model="batchConnectionsBox.data.aStartSn" :step="1" step-strictly :min="1"
              :max="getMaxSn('aSegId')" @change="ifRight('aStartSn'), verificationBatchConnectionsBox()" />
          </el-form-item>
          <el-form-item label="终止纤芯序号 : " prop="aEndSn">
            <el-input-number v-model="batchConnectionsBox.data.aEndSn" :step="1" step-strictly :min="1"
              :max="getMaxSn('aSegId')" @change="ifRight('aEndSn'), verificationBatchConnectionsBox()" />
          </el-form-item>
          <el-form-item label="终止端" prop="zSegId">
            <el-select v-model="batchConnectionsBox.data.zSegId"
              @change="segIdChange($event, 'zSegId'), verificationBatchConnectionsBox()">
              <el-option v-for="(item, key) in batchConnectionsBox.selectOption" :label="item.SEGNAME" :value="item.SEGID"
                :disabled="item.SEGID == batchConnectionsBox.data.aSegId" :key="key"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="起始纤芯序号 : " prop="zStartSn">
            <el-input-number v-model="batchConnectionsBox.data.zStartSn" :step="1" step-strictly :min="1"
              :max="getMaxSn('zSegId')" @change="ifRight('zStartSn'), verificationBatchConnectionsBox()" />
          </el-form-item>
          <el-form-item label="终止纤芯序号 : " prop="zEndSn">
            <el-input-number v-model="batchConnectionsBox.data.zEndSn" :step="1" step-strictly :min="1"
              :max="getMaxSn('zSegId')" @change="ifRight('zEndSn'), verificationBatchConnectionsBox()" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="commitbatchConnections">提交</el-button>
            <el-button @click="batchConnectionsBox.show = false">取消</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-dialog>
    <!--批量删除纤芯-->
    <el-dialog v-dialogDrag :modal="false" :title="delMoreBox.title + '  批量删除纤芯'" top="15%"
      :visible.sync="delMoreBox.show" :close-on-click-modal="false" width="30%">
      <div style="width: 100%; height: 30vh">
        <el-form :model="delMoreBox.data" :rules="delMoreBox.rules" ref="delMoreBox" label-width="150px">
          <el-form-item label="起始纤芯序号 : " prop="startSn">
            <el-input-number v-model="delMoreBox.data.startSn" :step="1" step-strictly :min="1" :max="delMoreBox.max" />
          </el-form-item>
          <el-form-item label="终止纤芯序号 : " prop="endSn">
            <el-input-number v-model="delMoreBox.data.endSn" :step="1" step-strictly :min="1" :max="delMoreBox.max" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="delMoreBoxCommit">提交</el-button>
            <el-button @click="delMoreBox.show">取消</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-dialog>
    <!--修改属性对话框-->
    <el-dialog v-dialogDrag custom-class="curdBox" :title="editBox.type == 'add' ? '新增' : '修改'"
      :visible.sync="editBox.show" width="1800px" :modal="false" :close-on-click-modal="false">
      <editData ref="child" :modelObj="this.modelObj" :dataObj="this.editBox.data"></editData>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm()">确 定</el-button>
        <el-button @click="editBox.show = false">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog v-dialogDrag :custom-class="infoBox.class" :visible.sync="infoBox.show" v-if="infoBox.show"
      :width="infoBox.width" :modal="false" :close-on-click-modal="false">
      <info style="width: 100%; height: 80vh;" :isEditMode="infoBox.isEditMode" :dataObj="infoBox.data"
        :tableName="tableName" @switchingModes="switchingModes">
      </info>
    </el-dialog>
    <!--光缆纤芯示意图-->
    <el-dialog v-dialogDrag :title="fiberCoreDetail.name" class="fiberCoreDetail" :visible.sync="fiberCoreDetail.show"
      v-if="fiberCoreDetail.show" :modal="false" :close-on-click-modal="false" width="90%">
      <div style="width: 100%; height: 75vh;" v-loading="fiberCoreDetail.loading">
        <gLFiberCoreDetail ref="fiberCoreDetail" :data="fiberCoreDetail.data" :isEdit="false" @openFiberCoreDetail="openFiberCoreDetail({
          resId: fiberCoreDetail.id,
          name: fiberCoreDetail.name,
        })" @loadingEnd="fiberCoreDetail.loading = false" />
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button :loading="creategTcconFiberlineRFiberseglineLoading" type="primary"
          @click="creategTcconFiberlineRFibersegline()">重新生成光缆纤芯与光缆段纤芯关系</el-button>
        <el-button @click="fiberCoreDetail.show = false">关 闭</el-button>
      </div>
    </el-dialog>
    <!--一次线路关系维护-->
    <el-dialog v-dialogDrag :title="fiberLineRelation.title + '   关联一次线路'" top="5vh"
      :visible.sync="fiberLineRelation.show" v-if="fiberLineRelation.show" :modal="false" :close-on-click-modal="false"
      width="90%">
      <fiberLineRelation style="width: 100%; height: 70vh;" :fiberId="fiberLineRelation.fiberId">
      </fiberLineRelation>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fiberLineRelation.show = false">关 闭</el-button>
      </div>
    </el-dialog>
    <!--承载光路-->
    <el-dialog v-dialogDrag custom-class="curdBox" :title="carryingOpticalPath.title + '   承载光路'"
      v-if="carryingOpticalPath.show" :visible.sync="carryingOpticalPath.show" :modal="false"
      :close-on-click-modal="false" width="90%">
      <carryingOpticalPath style="width: 100%; height: 70vh;" :id="carryingOpticalPath.id" :tableName="tableName">
      </carryingOpticalPath>
      <div slot="footer" class="dialog-footer">
        <el-button @click="carryingOpticalPath.show = false">关 闭</el-button>
      </div>
    </el-dialog>
    <!--承载业务-->
    <el-dialog v-dialogDrag custom-class="curdBox" :title="carryingBusiness.title + '   承载业务'"
      v-if="carryingBusiness.show" :visible.sync="carryingBusiness.show" :modal="false" :close-on-click-modal="false"
      width="90%">
      <carryingBusiness style="width: 100%; height: 70vh;" :id="carryingBusiness.id" :tableName="tableName">
      </carryingBusiness>
      <div slot="footer" class="dialog-footer">
        <el-button @click="carryingBusiness.show = false">关 闭</el-button>
      </div>
    </el-dialog>
    <!--承载通道链路-->
    <el-dialog v-dialogDrag custom-class="curdBox" :title="carryingChannelLink.title + '   承载通道链路'"
      v-if="carryingChannelLink.show" :visible.sync="carryingChannelLink.show" :modal="false"
      :close-on-click-modal="false" width="90%">
      <carryingChannelLink style="width: 100%; height: 70vh;" :id="carryingChannelLink.id" :tableName="tableName">
      </carryingChannelLink>
      <div slot="footer" class="dialog-footer">
        <el-button @click="carryingChannelLink.show = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!--关联接头盒-->
    <el-dialog v-dialogDrag custom-class="curdBox" :title="associatedConnectorBox.title + '   断点定位'"
      v-if="associatedConnectorBox.show" :visible.sync="associatedConnectorBox.show" :modal="false"
      :close-on-click-modal="false" width="90%">
      <fiberRFbrconnect style="width: 100%; height: 70vh;" :fiberId="associatedConnectorBox.id"
        :aResobjid="associatedConnectorBox.aResobjid" :zResobjid="associatedConnectorBox.zResobjid"
        :fiberLength="associatedConnectorBox.fiberLength" :jFlag="true">
      </fiberRFbrconnect>
      <div slot="footer" class="dialog-footer">
        <el-button @click="associatedConnectorBox.show = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { nameTo_, nameToTuoFeng } from "@/utils"
import { getModelId, getForm, export_excel_file, fileDownload, getNewIdByOriginalId } from "@/api/zdwh/zdzywh";
import {
  submit,
  submitEdit,
  delData,
  batchDelData,
  getFiberRouter,
  addFiberLineApi,
  delFiberLinkApi,
  getFiberApprovalID,
  exportOpticalCableLoadBearingLightPath,
  generateFiberOpticCableCores,
  generateSgTcconFiberlineRFibersegline,
} from "@/api/manageFiber/fiber";
import {
  getFiberDataApi,
  coreConnectShowApi
} from '@/api/gltxh/coreConnect'
import {
  getFiberCoreDetailApi,
  list,
  getTaskName,
  updateCoordinates
} from "@/api/gltxh/gltxhbj";
import editData from "@/views/publicPage/edit/editData";
import gLFiberCoreDetail from "@/views/components/gltxh/gLFiberCoreDetail.vue"
import zdzywhPage from '@/views/commonPage/zdzywh/index.vue';
import fiberLineRelation from '@/views/manageFiber/fiberLineRelation/index';
import carryingOpticalPath from '@/views/lightPathInfoManage/carryingOpticalPath'
import carryingBusiness from '@/views/serviceInformation/carryingBusiness'
import carryingChannelLink from '@/views/serviceInformation/carryingChannelLink.vue'
import { carryingOpticalPathExport } from '@/api/lightPathInfoManage/carryingOpticalPath'
import { carryingBusinessExport } from '@/api/serviceInformation/carryingBusiness'
import fiberRFbrconnect from '@/views/manageFiber/fiberRFbrconnect/index'
import info from '@/views/commonPage/info/index'
export default {
  name: "index",
  components: { editData, gLFiberCoreDetail, zdzywhPage, fiberLineRelation, carryingOpticalPath, carryingBusiness, carryingChannelLink, fiberRFbrconnect, info },
  data() {
    var verifySn = (rule, value, callback) => {
      let a = Math.abs(this.batchConnectionsBox.data.aStartSn - this.batchConnectionsBox.data.aEndSn)
      let z = Math.abs(this.batchConnectionsBox.data.zStartSn - this.batchConnectionsBox.data.zEndSn)
      if (a - z != 0) {
        return callback(new Error('纤芯数量不对等'))
      } else {
        return callback()
      }
    };
    return {
      tableName: 'SG_TCDEV_FIBER_B',
      iframeSrc1: '/stationWh/manageFiberHtml/fiberImaging.html',
      iframeSrc2: '/stationWh/html/fiberSketchMap.html',
      //表对象信息
      modelObj: {},
      // 修改/新增
      editBox: {
        type: '',
        show: false,
        data: {},
        isFirst: true
      },
      infoBox: {
        isEditMode: false,
        show: false,
        data: {},
        width: '1800px',
        class: 'framePosition',
      },
      //  光缆示意图弹出框遮盖层
      FiberRouterDialogShow: false,
      taskId: '',
      // 光缆数据 路由/资源
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
      FiberRouterData: {},
      createFiberLineLoading: false,
      creategTcconFiberlineRFiberseglineLoading: false,
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
      // 批量连接纤芯
      batchConnectionsBox: {
        title: '',
        show: false,
        aMax: 0,
        zMax: 0,
        data: {
          aSegId: '',
          aStartSn: '',
          aEndSn: '',
          zSegId: '',
          zStartSn: '',
          zEndSn: '',
        },
        selectOption: [],
        rules: {
          aSegId: [{ required: true, message: '此项不能为空', trigger: "blur" }],
          aStartSn: [
            { required: true, message: '此项不能为空', trigger: "blur" },
            { validator: verifySn, trigger: "change" },
          ],
          aEndSn: [
            { required: true, message: '此项不能为空', trigger: "blur" },
            { validator: verifySn, trigger: "change" },
          ],
          zSegId: [{ required: true, message: '此项不能为空', trigger: "blur" }],
          zStartSn: [
            { required: true, message: '此项不能为空', trigger: "blur" },
            { validator: verifySn, trigger: "change" },
          ],
          zEndSn: [
            { required: true, message: '此项不能为空', trigger: "blur" },
            { validator: verifySn, trigger: "change" },
          ],
        }
      },
      // 批量删除纤芯
      delMoreBox: {
        title: '',
        show: false,
        max: 0,
        data: {
          segId: '',
          startSn: 0,
          endSn: 0,
        },
        rules: {
          startSn: [{ required: true, message: '此项不能为空' }],
          endSn: [{ required: true, message: '此项不能为空' }],
        }
      },
      // 查看光缆纤芯
      fiberCoreDetail: {
        loading: false,
        show: false,
        title: '',
        id: '',
        data: {}
      },
      // 光缆与线路关系
      fiberLineRelation: {
        title: '',
        fiberId: '',
        show: false,
      },
      iframe: '',
      //光缆承载光路
      carryingOpticalPath: {
        title: '',
        id: '',
        show: false,
      },
      //光缆承载业务
      carryingBusiness: {
        title: '',
        id: '',
        show: false,
      },
      //光缆承载通道链路
      carryingChannelLink: {
        title: '',
        id: '',
        show: false,
      },
      //关联接头盒
      associatedConnectorBox: {
        title: '',
        id: '',
        fiberLength: '',
        show: false
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
    'fiberLineRelation.show': {
      handler() {
        if (!this.fiberLineRelation.show) {
          this.$refs.zdzywhPage.getTableData()
        }
      },
    },
  },
  created() {
    const { tableName, treeNode } = this.$route.query;
    if (tableName) this.tableName = tableName; // 可能不需要，因为已固定

    if (treeNode) {
      try {
        const node = JSON.parse(treeNode);
        this.initData(node);
      } catch (e) {
        console.error('解析树节点失败', e);
        this.getCommonData(); // 回退到普通加载
      }
    } else {
      this.getCommonData();
    }
    // 纤芯接续
    window['coreConnect'] = (data) => {
      this.coreConnectShow(data)
    }
    window['addFiberLine'] = (data) => {
      this.addFiberLine(data)
    }
    window['reSetLink'] = (objs, delLinks, aResId, zResId) => {
      this.reSetLink(objs, delLinks, aResId, zResId)
    }
    window['delLink'] = (data, link) => {
      this.delLink(data, link)
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
    this.$bus.$on('getTableDataFiber', this.getTableDataFiber)
  },
  beforeDestroy() {
    this.$off('getTableDataFiber')
  },
  methods: {
    initData(treeNode) {
      // 1. 保存树节点数据（供后续扩展使用）
      this.treeNode = treeNode;
      // 2. 重新获取模型信息（确保 tableName 正确）
      this.getCommonData();
      // 3. 触发 zdzywhPage 子组件刷新表格数据
      this.$nextTick(() => {
        if (this.$refs.zdzywhPage && typeof this.$refs.zdzywhPage.getTableData === 'function') {
          this.$refs.zdzywhPage.getTableData();
        }
      });
      // 4. 可选：如果树节点有筛选条件，可在这里传递给 zdzywhPage
      // 示例：如果树节点包含筛选参数
      // if (treeNode.searchParams) {
      //   this.$refs.zdzywhPage.searchParams = treeNode.searchParams;
      // }
    },
    getTableDataFiber() {
      this.$refs.zdzywhPage.getTableData()
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
    getCarryingOpticalPath(data) {
      this.carryingOpticalPath.title = data.rowData.SNNAME || data.rowData.NAME
      this.carryingOpticalPath.id = data.rowData.ID
      this.carryingOpticalPath.show = true
    },
    carryingOpticalPathExport(data) {
      this.$message({
        type: 'success',
        message: '正在导出...'
      })
      carryingOpticalPathExport({}, { id: data.rowData.ID, resType: '光缆' }).then(res => {
        let lastInde = res.msg.lastIndexOf("/") + 1;
        let fileName = res.msg.substring(lastInde);
        fileDownload({ filename: res.msg }).then(res => {
          export_excel_file(res, fileName);
          this.$message({
            type: 'success',
            message: '导出完成'
          })
        })
      })
    },
    carryingBusinessExport(data) {
      this.$message({
        type: 'success',
        message: '正在导出...'
      })
      carryingBusinessExport({}, { id: data.rowData.ID, resType: '光缆' }).then(res => {
        let lastInde = res.msg.lastIndexOf("/") + 1;
        let fileName = res.msg.substring(lastInde);
        fileDownload({ filename: res.msg }).then(res => {
          export_excel_file(res, fileName);
          this.$message({
            type: 'success',
            message: '导出完成'
          })
        })
      })
    },
    getCarryingBusiness(data) {
      this.carryingBusiness.title = data.rowData.SNNAME || data.rowData.NAME
      this.carryingBusiness.id = data.rowData.ID
      this.carryingBusiness.show = true
    },
    getCarryingChannelLink(data) {
      this.carryingChannelLink.title = data.rowData.SNNAME || data.rowData.NAME
      this.carryingChannelLink.id = data.rowData.ID
      this.carryingChannelLink.show = true
    },
    getAssociatedConnectorBox(data) {
      this.associatedConnectorBox.title = data.rowData.NAME
      this.associatedConnectorBox.id = data.rowData.ID
      this.associatedConnectorBox.fiberLength = data.rowData.FIBER_LENGTH
      this.associatedConnectorBox.aResobjid = data.rowData.A_RESOBJID
      this.associatedConnectorBox.zResobjid = data.rowData.Z_RESOBJID
      this.associatedConnectorBox.show = true
    },
    getCommonData() {
      //获取表基本信息
      getModelId({ tableName: this.tableName }).then(res => {
        this.modelObj = res.data
      })
    },
    handleExport() {
      this.$refs.zdzywhPage.handleExport()
    },
    opticalCableLoadBearingLightPath() {
      this.$confirm('是否导出数据?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$refs.zdzywhPage.otherHandleExport()
      })
    },
    getSearchCriteria(params) {
      this.$message({
        type: 'success',
        message: '正在导出...'
      })
      exportOpticalCableLoadBearingLightPath(params).then(res => {
        let lastInde = res.msg.lastIndexOf("/") + 1;
        let fileName = res.msg.substring(lastInde);
        fileDownload({ filename: res.msg }).then(res => {
          export_excel_file(res, fileName);
          this.$message({
            type: 'success',
            message: '导出完成'
          })
        })
      })
    },
    openWindow() {
      this.$refs.zdzywhPage.openWindow()
    },
    //查看
    searchStation(scope) {
      this.$refs.zdzywhPage.searchStation(scope)
    },
    // 新增/修改
    openEditBox(type, row) {
      this.editBox.type = type
      this.editBox.show = true
      this.editBox.data = row
      this.$nextTick(() => {
        this.$refs.child.modelObj = this.modelObj
        this.$refs.child.dataObj = this.editBox.data
        if (this.editBox.isFirst) {
          this.$refs.child.beforeLoading()
          this.editBox.isFirst = false
        } else {
          this.$refs.child.reOpen()
        }
      })
    },
    openFiberInfo(type, row) {
      this.infoBox.isEditMode = type
      this.infoBox.show = true
      this.infoBox.width = '1600px'
      this.infoBox.class = ''
      this.infoBox.data = row
      console.log(this.infoBox.data);
    },
    switchingModes(data) {
      this.infoBox.width = data ? '1800px' : '500px'
      this.infoBox.class = data ? '' : 'framePosition'
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
          if (this.editBox.type == 'add') {
            submit(params).then(response => {
              this.editBox.show = false
              //this.$modal.msgSuccess("操作成功,请在“待处理光缆”中查看。");
              this.$modal.msgSuccess("操作成功");
              this.$refs.zdzywhPage.getTableData()
            })
          } else {
            submitEdit(params).then(response => {
              this.editBox.show = false
              //this.$modal.msgSuccess("操作成功,请在“待处理光缆”中查看。");
              this.$modal.msgSuccess("操作成功");
              this.$refs.zdzywhPage.getTableData()
            })
          }
        })
      }, 1500);
    },
    //  单个删除
    delData(row) {
      this.$confirm('此操作删除的数据将进入带处理数据中, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let params = {
          modelId: this.modelObj.modelId,
          isPage: '0',
          whereAttributes: [{
            attributeName: 'ID',
            attributeValue: row.ID,
            isPrimaryKey: '1'
          }]
        }
        getForm(params).then(res => {
          let data = {}
          for (let key in res.data) {
            data[nameToTuoFeng(key)] = res.data[key]
          }
          delData(data).then(response => {
            //this.$modal.msgSuccess("操作成功,请在“待处理光缆”中查看。");
            this.$modal.msgSuccess("操作成功");
            this.$refs.zdzywhPage.getTableData()
          })
        })


        // delData(row.ID).then(res => {
        //   this.$refs.zdzywhPage.getTableData()
        //   this.$modal.msgSuccess("操作成功,请在“待处理光缆”中查看。");
        // })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    // 多个删除
    deleteSta() {
      if (this.$refs.zdzywhPage.multipleSelection.length === 0) {
        this.$message.error('请选中数据进行删除');
      } else {
        this.$confirm('此操作删除的数据将进入带处理数据中, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let ids = []
          let datas = this.$refs.zdzywhPage.multipleSelection
          for (let i = 0; i < datas.length; i++) {
            ids.push(datas[i].ID)
          }
          batchDelData(ids).then(res => {
            this.$refs.zdzywhPage.getTableData()
            //this.$modal.msgSuccess("操作成功,请在“待处理光缆”中查看。");
            this.$modal.msgSuccess("操作成功");
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
      }
    },
    //  跳转待处理光缆
    toPendingFiber() {
      this.$router.push({
        path: '/zdwh/sgtccon/awaitFiber'
      })
    },
    //  跳转光缆段信息
    toFiberSeg(row) {
      this.$router.push(
        {
          path: '/zdwh/sgtccon/fiberSeg',
          query: {
            search: JSON.stringify({ FIBER_ID: row.ID, }),
            refresh: true,
          }
        }
      )
    },
    //  跳转光缆纤芯
    toFiberLine(row) {
      this.$router.push(
        {
          path: '/zdwh/sgtccon/fiberLine',
          query: {
            search: JSON.stringify({ PAR_FIBER: row.ID, }),
            refresh: true,
          },
        }
      )
    },
    //  光缆路由
    fiberRouter(row) {
      getFiberApprovalID({ id: row.ID }).then(res => {
        if (res.msg) {
          this.taskId = res.msg
          this.FiberRouterDialogShow = true
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
          attributeValue: row.ID,
          isPrimaryKey: '1'
        }]
      }
      getForm(params).then(res => {
        let data = {}
        for (let key in res.data) {
          data[nameToTuoFeng(key)] = res.data[key]
        }
        this.FiberRouterData = data
        getFiberRouter(data).then(res1 => {
          this.FiberRouterDialogShow = true
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
    addFiberLine(data) {
      addFiberLineApi({
        aResId: data.aResId,
        zResId: data.zResId,
        tconId: this.coreConnectBox.tconId,
      }).then(res => {
        this.$refs.coreConnectBoxIframe.contentWindow.addLink(res.data)
      })
    },
    // 重新绘制纤芯
    reSetLink(objs, delLinks, aResId, zResId) {
      this.$confirm('此端口已连接纤芯 继续操作会替换原纤芯 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let obj = [...new Set(objs)]
        for (let i = 0; i < obj.length; i++) {
          delFiberLinkApi(obj[i]).then(res => {
            if (res.code == '200') {
              this.$refs.coreConnectBoxIframe.contentWindow.removerLinkCommit(delLinks[i])
            }
          })
        }
        this.addFiberLine({ aResId: aResId, zResId: zResId })
      }).catch(e => {
        this.$message({
          type: 'info',
          message: '已取消'
        });
      })
    },
    delLink(data, link) {
      this.$confirm('删除后无法恢复 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delFiberLinkApi(data).then(res => {
          if (res.code == '200') {
            this.$refs.coreConnectBoxIframe.contentWindow.removerLinkCommit(link)
            this.$message({
              type: 'success',
              message: '删除成功!'
            });
          }
        })
      }).catch(e => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      })
    },
    //  生成光缆纤芯
    createFiberLine() {
      if (this.FiberRouterData.id) {
        this.$confirm('是否确认重新生成光缆纤芯?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.createFiberLineLoading = true
          generateFiberOpticCableCores({ id: this.FiberRouterData.id }).then(res => {
            this.$modal.msgSuccess("操作成功");
            this.createFiberLineLoading = false
          }).catch(() => {
            this.createFiberLineLoading = false
          })
        })
      }
    },

    saveTemporary() {
      let data = this.$refs.iframe2.contentWindow.getCanvasData()
      for (let i = 0; i < data.length; i++) {
        switch (data[i].resType + '') {
          // 站点
          case '0':
            updateCoordinates(data[i]).then(res => {
            }).catch(e => {
              this.$message({
                message: e,
                type: 'error'
              });
            })
            break;
          // 接头盒
          case '1':
            updateCoordinates(data[i]).then(res => {
            }).catch(e => {
              this.$message({
                message: e,
                type: 'error'
              });
            })
            break;
        }
      }
      this.$message({ message: '保存完成', type: 'success' });
    },
    // 批量连接纤芯 获取纤芯最大值
    getMaxSn(name) {
      let data = this.batchConnectionsBox.selectOption.find((item) => {
        return item.SEGID == this.batchConnectionsBox.data[name]
      })
      if (data) {
        return data.groupLength
      } else {
        return 1
      }
    },
    // 批量连接纤芯提交
    commitbatchConnections() {
      this.$refs['batchConnectionsBox'].validate((valid) => {
        if (valid) {
          this.$confirm('此操作可能会影响原纤芯 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            this.batchConnectionsBox.data.TconId = this.coreConnectBox.id
            commitbatchConnectionsApi(this.batchConnectionsBox.data).then(res => {
              this.coreConnectShow(this.coreConnectBox)
              this.batchConnectionsBox.show = false
            })
          })
        }
      })
    },
    // 批量纤芯删除提交
    delMoreBoxCommit() {
      this.$confirm('删除后无法恢复 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.delMoreBox.data.TconId = this.coreConnectBox.id
        delMoreApi(this.delMoreBox.data).then(res => {
          this.delMoreBox.show = false
          this.coreConnectShow(this.coreConnectBox)
        })
      }).catch(e => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      })
    },
    // 查看光缆纤芯
    openFiberCoreDetail(data) {
      this.fiberCoreDetail.id = data.ID
      this.fiberCoreDetail.name = data.NAME
      this.fiberCoreDetail.show = true
      this.fiberCoreDetail.loading = true
      getFiberCoreDetailApi({
        fiberId: data.ID
      }).then(res => {
        this.fiberCoreDetail.data = res.data
        this.$nextTick(() => {
          this.$refs.fiberCoreDetail.locationLine()
        })
      })
    },
    creategTcconFiberlineRFibersegline() {
      if (this.fiberCoreDetail.id) {
        this.$confirm('是否确认重新生成光缆纤芯与光缆段纤芯?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.creategTcconFiberlineRFiberseglineLoading = true
          generateSgTcconFiberlineRFibersegline({ fiberId: this.fiberCoreDetail.id }).then(res => {
            this.$modal.msgSuccess("操作成功");
            this.creategTcconFiberlineRFiberseglineLoading = false
            getFiberCoreDetailApi({
              fiberId: this.fiberCoreDetail.id
            }).then(res => {
              this.fiberCoreDetail.data = res.data
              this.$nextTick(() => {
                this.$refs.fiberCoreDetail.locationLine()
              })
            })
          }).catch(() => {
            this.creategTcconFiberlineRFiberseglineLoading = false
          })
        })
      }
    },
    fiberLineRelationShow(row) {
      this.fiberLineRelation = {
        title: row.NAME,
        fiberId: row.ID,
        show: true,
      }
    }
  },
}
</script>

<style scoped lang="scss">
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
