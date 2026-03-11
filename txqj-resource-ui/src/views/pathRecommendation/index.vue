<template>
  <div class="commonWh">
    <div class="box-card">
      <div class="queryBox">
        <div class="basicsQuery">
          <el-form :model="queryParams" ref="queryForm" label-width="90px">
            <el-form-item label="资源类型:" style="width: 180px;">
              <el-select size="larger" v-model="queryParams.resType" @change="selceted($event)"
                         placeholder="请选择" :disabled="disabled">
                <el-option v-for="item in resTypes" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="算法类型:" style="width: 220px;">
              <el-select size="larger" v-model="queryParams.algorithmPathType" @change="restFiber($event)"
                         placeholder="请选择" :disabled="disabled">
                <el-option v-for="item in algorithmPathTypes" :key="item.value" :label="item.label"
                           :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="算法规则:" style="width: 220px;">
              <el-select size="larger" v-model="queryParams.algorithmPathRule" placeholder="请选择"
                         :disabled="disabled">
                <el-option v-for="item in algorithmPathRules" :key="item.value" :label="item.label"
                           :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="受损光缆:" style="width: 600px;"
                          v-show="queryParams.algorithmPathType == 0 ? false : true">
              <el-select size="larger" v-model="queryParams.damagedFiber" clearable filterable
                         :filter-method="filterMethod1" v-el-select-loadmore="loadMore(rangeNumber)"
                         @visible-change="visibleChange1" placeholder="请选择">
                <el-option v-for="item in options1.slice(0, rangeNumber)" :key="item.value" :label="item.label"
                           :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="起始资源:" style="width: 300px;"
                          v-show="queryParams.algorithmPathType == 0 ? true : false">
              <el-select size="larger" v-model="queryParams.aResId" clearable filterable
                         :filter-method="filterMethod2" v-el-select-loadmore="loadMore(rangeNumber)"
                         @visible-change="visibleChange2" placeholder="请选择" :disabled="disabled">
                <el-option v-for="item in options2.slice(0, rangeNumber)" :key="item.value" :label="item.label"
                           :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="终止资源:" style="width: 300px;"
                          v-show="queryParams.algorithmPathType == 0 ? true : false">
              <el-select size="larger" v-model="queryParams.zResId" clearable filterable
                         :filter-method="filterMethod3" v-el-select-loadmore="loadMore(rangeNumber)"
                         @visible-change="visibleChange3" placeholder="请选择" :disabled="disabled">
                <el-option v-for="item in options3.slice(0, rangeNumber)" :key="item.value" :label="item.label"
                           :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="输出条数:" style="width: 150px;">
              <input style="text-align: center;" size="larger" type="number" min="1" max="99"
                     v-model="queryParams.outNumber" placeholder="请输入正整数">
            </el-form-item>
            <el-form-item label="最大节点数:" label-width="110px" style="width: 150px;">
              <input style="text-align: center;" size="larger" type="number" min="1" max="99"
                     v-model="queryParams.maxNodeNum" placeholder="请输入正整数">
            </el-form-item>
            <el-form-item label-width="40px" style="width: 300px;">
              <el-button type="primary" size="mini" @click="analysisPath" v-if="analysisbuttonStatu">分析</el-button>
              <el-button type="primary" size="mini" @click="analysisStop" v-if="!analysisbuttonStatu" style="color: thistle;">分析进度{{currentProgress }}%...</el-button>
              <el-button size="mini" @click="resetQuery">重置</el-button>
<!--              <el-button size="mini" v-if="queryParams.algorithmPathType == 1" @click="impactSituationExport" :loading="exportLoading">导出</el-button>-->
            </el-form-item>
          </el-form>
        </div>
        <div class="openTit" style="margin-top: 0" @click="openClose=!openClose">
          <p v-if="!openClose">展开  <i class="el-icon-arrow-down"></i></p>
          <p v-else>收起  <i class="el-icon-arrow-up"></i></p>
        </div>
      </div>

      <div class="tableBox" style="height: calc(100% - 80px);">

        <div class="pathRecommendation">
          <el-collapse-transition>
            <div v-if="openClose" class="collapseHeaderBox">
              <el-form :model="queryParams" ref="queryForm" label-width="120px">
                <el-form-item label="传输平面:" style="width: 100%" v-if="queryParams.resType == 0 ? true : false">
                  <el-select size="larger" multiple v-model="queryParams.tcPlane" placeholder="所有">
                    <el-option v-for="item in tcPlanes" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="站点类型:" style="width: 100%">
                  <el-select size="larger" multiple v-model="queryParams.siteType" placeholder="所有">
                    <el-option v-for="item in siteTypes" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="光缆类型:" style="width: 100%">
                  <el-select size="larger" multiple v-model="queryParams.fiberType" placeholder="所有">
                    <el-option v-for="item in fiberTypes" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="电压等级:" style="width: 100%">
                  <el-select size="larger" multiple v-model="queryParams.voltageClass"
                             :placeholder="queryParams.resType == 0 ? '所有电压等级的站点' : '所有电压等级光缆'">
                    <el-option v-for="item in voltageClasss" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                  </el-select>
                </el-form-item>
                <el-form-item :label="queryParams.resType == 0 ? '必不经过设备:' : '必不经过站点:'" style="width: 100%">
                  <el-select size="larger" multiple v-model="queryParams.notPassRes" clearable filterable
                             :filter-method="filterMethod4" v-el-select-loadmore="loadMore(rangeNumber)"
                             @visible-change="visibleChange4" placeholder="无">
                    <el-option v-for="item in options4.slice(0, rangeNumber)" :key="item.value" :label="item.label"
                               :value="item.value">
                    </el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="必不经过光缆:" style="width: 100%">
                  <el-select size="larger" multiple v-model="queryParams.notPassFiber" clearable filterable
                             :filter-method="filterMethod1" v-el-select-loadmore="loadMore(rangeNumber)"
                             @visible-change="visibleChange1" placeholder="无">
                    <el-option v-for="item in options1.slice(0, rangeNumber)" :key="item.value" :label="item.label"
                               :value="item.value">
                    </el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="必须经过资源:" style="width: 100%">
                  <el-select size="larger" multiple v-model="queryParams.mustPassRes" clearable filterable
                             :filter-method="filterMethod5" v-el-select-loadmore="loadMore(rangeNumber)"
                             @visible-change="visibleChange5" placeholder="无">
                    <el-option v-for="item in options5.slice(0, rangeNumber)" :key="item.value" :label="item.label"
                               :value="item.value">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-form>
            </div>
          </el-collapse-transition>
          <!--   光路       -->
          <div class="editForm" v-if="queryParams.resType == 1 && queryParams.algorithmPathType == 1">
            <p class="formTop">影响光路 </p>
            <div class="pathList">
              <el-table
                stripe
                border
                height="100%"
                v-loading="loading"
                :data="impactSituation">
                <el-table-column label="序号" width="80" align="center" :formatter="snFormatter"></el-table-column>
                <el-table-column label="名称" align="center" show-overflow-tooltip prop="name">
                  <template slot-scope="scope">
                    <a
                      class="linkColumn"
                      @click="getRouterLink(scope.row,'SG_TCCON_TCOPTICALROAD_B')"
                    >
                      {{scope.row.name}}
                    </a>
                  </template>
                </el-table-column>
                <el-table-column label="起始资源" width="160" align="center" show-overflow-tooltip
                                 prop="aResName"></el-table-column>
                <el-table-column label="终止资源" width="160" align="center" show-overflow-tooltip
                                 prop="zResName"></el-table-column>
                <el-table-column label="原路由" align="center" show-overflow-tooltip prop="path"></el-table-column>
              </el-table>
            </div>
          </div>
          <div class="editForm" v-if=" queryParams.resType == 1">
            <p class="formTop">路径推荐</p>
            <div class="pathList" v-loading="loading">
              <div v-for="(item, index) in allPaths" :key="index" class="lineIcon">
                <div @click="initEcharts(item)" class="lineIconItem">
                  <span>{{ '路径' + (index + 1) + '.' }}</span>
                  <template v-show="item.pathInfo" v-for="(it,ind ) in item.pathInfo">
                    <span>{{ it.pathName }}</span>
                    <i v-if="ind != item.pathInfo.length - 1" class="el-icon-right"></i>
                    <div v-if="ind != item.pathInfo.length - 1" class="fiberInfo">
                      <span v-if="it.fiberId"
                      >{{ '长度：' + it.fiberLength + 'km   可用芯数：' + it.lineNumberAvailable }}</span>
                      <el-select
                        size="mini"
                        style="width: 200px"
                        v-model="it.fiberId"
                        @change="selectChange($event, index,ind)"
                      >
                        <el-option
                          v-for="i in it.fiberList"
                          :key="i.value"
                          :label="i.label"
                          :value="i.value"
                          :title="i.label"
                        >
                        </el-option>
                      </el-select>
                    </div>
                    <i v-if="ind != item.pathInfo.length - 1" class="el-icon-right"></i>
                  </template>
                  <span style="margin-left: 20px" v-if="item.pathInfo">总长度:{{ item.fiberLength }}</span>
                  <i class="el-icon-document-copy"
                     v-if="item.pathInfo"
                     style="margin: 0 20px; cursor: pointer;"
                     @click.stop="copyFocusedBoxContent(item)"
                  >复制路由</i>
                  <i class="el-icon-circle-check"
                     v-if="item.pathInfo && disabled"
                     style="cursor: pointer;"
                     @click.stop="saveEmergencyRoute(item)"
                  >保存应急路由</i>
                  <span v-if="!item.pathInfo">未寻到合适路径</span>
                </div>
                <span
                  style="color: rgb(255, 0, 191);"
                  v-show="queryParams.algorithmPathRule == 1 && item.pathDetail.pathDetail != null"
                  class="pathClass"
                  @click="initEcharts(item)"
                >{{
                    item.pathDetail.pathDetail + '总长(' +
                    item.pathDetail.totalLength + ')  剩余纤芯(' +
                    item.pathDetail.totalRemainLine + '/' +
                    item.pathDetail.totalLine + ')' + '(' +
                    ((item.pathDetail.totalRemainLine / item.pathDetail.totalLine) * 100).toFixed(2) + '%)'
                  }}</span>
              </div>
            </div>
          </div>
          <!--   业务       -->
          <div class="business" v-show="queryParams.resType == 0 && queryParams.algorithmPathType == 1">
            <div class="business-item" >
              <p class="business-item-top">影响光路</p>
              <div class="business-item-content">
                <el-table
                  stripe
                  border
                  height="100%"
                  v-loading="loading"
                  :data="impactSituation">
                  <el-table-column width="50" label="选择" align="center">
                    <template slot-scope="scope">
                      <el-radio v-model="lightPathId" :label="scope.row.id" @change.native="handleSelectionChange(scope.row)"> <span></span></el-radio>
                    </template>
                  </el-table-column>
                  <el-table-column label="序号" width="80" align="center" :formatter="snFormatter"></el-table-column>
                  <el-table-column label="名称" align="center" show-overflow-tooltip prop="name">
                    <template slot-scope="scope">
                      <a
                        class="linkColumn"
                        @click="getRouterLink(scope.row,'SG_TCCON_TCOPTICALROAD_B')"
                      >
                        {{scope.row.name}}
                      </a>
                    </template>
                  </el-table-column>
                  <el-table-column label="起始资源" width="160" align="center" show-overflow-tooltip
                                   prop="aResName"></el-table-column>
                  <el-table-column label="终止资源" width="160" align="center" show-overflow-tooltip
                                   prop="zResName"></el-table-column>
                </el-table>
              </div>
            </div>
            <div class="business-item">
              <p class="business-item-top">影响继电保护业务</p>
              <div class="business-item-content">
                <el-table
                  v-loading="loading"
                  stripe
                  border
                  height="100%"
                  :data="currentImpactBuz">
                  <el-table-column width="50" label="选择" align="center">
                    <template slot-scope="scope">
                      <el-radio v-model="buzId" :label="scope.row.id" @change.native="handleSelectionChangeBuz(scope.row)"> <span></span></el-radio>
                    </template>
                  </el-table-column>
                  <el-table-column label="序号" width="80" align="center" :formatter="snFormatter"></el-table-column>
                  <el-table-column label="名称" align="center" show-overflow-tooltip prop="name">
                    <template slot-scope="scope">
                      <a
                        class="linkColumn"
                        @click="getRouterLink(scope.row,'SG_TCCON_TCCHANNEL_B')"
                      >
                        {{scope.row.name}}
                      </a>
                    </template>
                  </el-table-column>
                  <el-table-column label="起始资源" width="160" align="center" show-overflow-tooltip
                                   prop="aResName"></el-table-column>
                  <el-table-column label="终止资源" width="160" align="center" show-overflow-tooltip prop="zResName"></el-table-column>
                  <el-table-column align="center" label="业务名称" prop="buzName" show-overflow-tooltip>
                    <template slot-scope="scope">
                      <a
                        class="linkColumn"
                        @click="getRouterLink(scope.row,'SG_TCCON_TCBUZ_B')"
                      >
                        {{scope.row.buzName}}
                      </a>
                    </template>
                  </el-table-column>
                  <el-table-column label="原路由" align="center" show-overflow-tooltip prop="path"></el-table-column>
                </el-table>
              </div>
            </div>
          </div>
          <div class="editForm" v-if="queryParams.resType == 0">
            <p class="formTop">路径推荐</p>
            <div class="pathList" v-loading="loading">
              <div v-for='(item, index) in allPaths' :key="index" class="lineIcon">
                <span class="pathClass" @click="initEcharts(item)">{{ '路径' + (index + 1) + '.' + (item.pathName == null ? '未寻到合适路径' : item.pathName)}}</span>
                <i class="el-icon-circle-check"
                   v-if="item.path != null && disabled"
                   style="margin: 0 20px; cursor: pointer;"
                   @click.stop="saveBuzEmergencyRoute(item.path,index)"
                >保存应急路由</i>
                <span  style="color: rgb(255, 0, 191);" v-show="queryParams.algorithmPathRule == 1&&item.pathDetail!=null" class="pathClass" @click="initEcharts(item)">{{item.pathDetail.pathDetail + '总长(' + item.pathDetail.totalLength + ')  剩余纤芯(' + item.pathDetail.totalRemainLine + '/' + item.pathDetail.totalLine +')' + '(' + ((item.pathDetail.totalRemainLine/item.pathDetail.totalLine)*100).toFixed(2)+"%)"}}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <el-dialog v-dialogDrag custom-class="curdBox" title="路径" :visible.sync="show" width="80%" :modal="false"
               :close-on-click-modal="false">
      <div style="width: 100%; height: 70vh" id="routerEcharts" v-loading="mapLoading"></div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="show = false">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog
      v-dialogDrag
      :custom-class="infoBox.class"
      :visible.sync="infoBox.show"
      v-if="infoBox.show"
      :width="infoBox.width"
      :modal="false"
      :close-on-click-modal="false">
      <info
        style="width: 100%; height: 80vh;"
        :isEditMode="infoBox.isEditMode"
        :dataObj="infoBox.data"
        :tableName="infoBox.tableName"
        @switchingModes="switchingModes">
      </info>
    </el-dialog>
  </div>
</template>

<script>
import {
  getFiberListApi,
  getResListByTypeApi,
  getTcplanLitsApi,
  getFiberTypeListApi,
  getSiteTypeListApi,
  getVoltageClassListApi,
  pathAlgorithmsApi,
  _debounce,
  impactSituationApi,
  querySiteInformation,
  querySiteInformationByEquipment,
  impactSituationExport,
  getAnalysisResInfo,
  saveEmergencyRoute,
  saveBuzEmergencyRoute
} from "@/api/pathRecommendation";
import * as echarts from 'echarts'
import mapJson from './map.json'
import {
  fileDownload2,
  export_excel_file,
} from "@/api/zdwh/zdzywh";
import info from '@/views/commonPage/info/index'
export default {
  name: "pathRecommendation",
  components: {info},
  data() {
    return {
      disabled: false,
      resInfo: {},
      analysisbuttonStatu: true,
      currentAnalysisRow: 1,
      openClose: false,
      queryParams: {
        resType: 1,
        algorithmPathType: 0,
        damagedFiber: '',
        aResId: '',
        zResId: '',
        algorithmPathRule: 0,
        tcPlane: [],
        siteType: [],
        fiberType: [],
        voltageClass: [],
        notPassRes: [],
        notPassFiber:[],
        mustPassRes: [],
        outNumber: 5,
        maxNodeNum: 10
      },
      options1: [],
      options2: [],
      options3: [],
      options4: [],
      options5: [],
      resTypes: [{
        value: 0,
        label: '业务'
      }, {
        value: 1,
        label: '光路'
      }],
      algorithmPathTypes: [{
        value: 0,
        label: '路径开通'
      }, {
        value: 1,
        label: '路径迂回'
      }],
      damagedFibers: [],
      rangeNumber: 10,
      aResIds: [],
      zResIds: [],
      algorithmPathRules: [{
        value: 0,
        label: '最短路径'
      }, {
        value: 1,
        label: '负载均衡'
      }],
      tcPlanes: [],
      siteTypes: [],
      fiberTypes: [],
      voltageClasss: [],
      notPassRess: [],
      mustPassRess: [],
      outNumbers: [],
      loading: false,
      impactSituation: [],
      //所有影响业务
      impactBuz: [],
      //当前影响业务
      currentImpactBuz: [],
      //分析进度
      currentProgress: 1,
      //光路id为key的影响业务
      allImpactBuzMap: {},
      involvingLines: [],
      protectId: '',
      protect: {},
      allPathMap: {},
      currentKey: '',
      allPaths: [],
      show: false,
      mapLoading: false,
      exportLoading: false,
      lightPathId: '',
      buzId: '',
      infoBox: {
        isEditMode: false,
        show: false,
        data: {},
        width: '1800px',
        class: 'framePosition',
        tableName: '',
      }
    };
  },

  mounted() {
    this.initDropdownBox()
  },
  activated() {
    let gl_id = this.$route.query.gl_id
    let buz_id = this.$route.query.buz_id
    if (gl_id || buz_id){
      //清空query
      this.$router.push({ query: {} })
      this.resetQuery()
      this.disabled = true
      if (gl_id){
        this.queryParams.resType = 1
      }
      if (buz_id){
        this.queryParams.resType = 0
      }
      getAnalysisResInfo({
        gl_id:gl_id,
        buz_id:buz_id,
      }).then(res => {
        this.resInfo = res.data
        this.queryParams.aResId = res.data.aResId
        this.queryParams.zResId = res.data.zResId
        this.options2.push({
          value: res.data.aResId,
          label: res.data.aResName
        })
        this.options3.push({
          value: res.data.zResId,
          label: res.data.zResName
        })
        this.analysisPath()
      })

    }

  },

  methods: {
    initDropdownBox() {
      getFiberListApi().then((res) => {
        this.damagedFibers = res
      })

      getTcplanLitsApi().then((res) => {
        this.tcPlanes = res
      })

      getFiberTypeListApi().then((res) => {
        this.fiberTypes = res
      })

      getVoltageClassListApi().then((res) => {
        this.voltageClasss = res
      })

      getSiteTypeListApi().then((res) => {
        this.siteTypes = res
      })

      getResListByTypeApi().then((res) => {
        this.aResIds = res
        this.zResIds = res
        this.notPassRess = res
        this.mustPassRess = res
      })
    },

    loadMore(n) {
      return () => (this.rangeNumber += 5); //
    },


    // 筛选方法
    filterMethod1: _debounce(function (filterVal) {
      if (filterVal) {
        let filterArr = this.damagedFibers.filter((item) => {
          return item.label.toLowerCase().includes(filterVal.toLowerCase())
        })
        this.options1 = filterArr;
      } else {
        this.options1 = this.damagedFibers;
      }
    }, 500),

    filterMethod2: _debounce(function (filterVal) {
      if (filterVal) {
        let filterArr = this.aResIds.filter((item) => {
          return item.label.toLowerCase().includes(filterVal.toLowerCase())
        })
        this.options2 = filterArr;
      } else {
        this.options2 = this.aResIds;
      }
    }, 500),

    filterMethod3: _debounce(function (filterVal) {
      if (filterVal) {
        let filterArr = this.zResIds.filter((item) => {
          return item.label.toLowerCase().includes(filterVal.toLowerCase())
        })
        this.options3 = filterArr;
      } else {
        this.options3 = this.zResIds;
      }
    }, 500),

    filterMethod4: _debounce(function (filterVal) {
      if (filterVal) {
        let filterArr = this.notPassRess.filter((item) => {
          return item.label.toLowerCase().includes(filterVal.toLowerCase())
        })
        this.options4 = filterArr;
      } else {
        this.options4 = this.notPassRess;
      }
    }, 500),

    filterMethod5: _debounce(function (filterVal) {
      if (filterVal) {
        let filterArr = this.mustPassRess.filter((item) => {
          return item.label.toLowerCase().includes(filterVal.toLowerCase())
        })
        this.options5 = filterArr;
      } else {
        this.options5 = this.mustPassRess;
      }
    }, 500),

    // 下拉框出现时，调用过滤方法
    visibleChange1(flag) {
      if (flag) {
        this.filterMethod1()
      }
    },

    visibleChange2(flag) {
      if (flag) {
        this.filterMethod2()
      }
    },

    visibleChange3(flag) {
      if (flag) {
        this.filterMethod3()
      }
    },

    visibleChange4(flag) {
      if (flag) {
        this.filterMethod4()
      }
    },

    visibleChange5(flag) {
      if (flag) {
        this.filterMethod5()
      }
    },

    analysisPath() {
      if (this.queryParams.algorithmPathType == 0 && (this.queryParams.aResId == '' || this.queryParams.zResId == '')) {
        this.$modal.msgSuccess("请选择起始和终止资源");
      } else if (this.queryParams.algorithmPathType == 1 && this.queryParams.damagedFiber == '') {
        this.$modal.msgSuccess("请选择受损光缆");
      } else {
        this.loading = true
        this.allPathMap = {};
        this.allPaths = [];
        this.impactBuz = [];
        this.allImpactBuzMap = {};
        this.currentImpactBuz = [];
        //如保护算法先查影响光路
        if (this.queryParams.damagedFiber != '') {
          this.analysisbuttonStatu = false
          impactSituationApi(this.queryParams).then((res) => {
            //影响光路
            this.impactSituation = res.data.impactSituation
            //光路业务关系
            this.allImpactBuzMap = res.data;
            //所有影响业务
            this.impactBuz = res.data.impactBuz;
            //如果是业务保护算法，则需要遍历查询业务路径
            if (this.impactSituation.length != null && this.impactSituation.length > 0) {
              if (this.queryParams.resType == 0 && this.queryParams.algorithmPathType == 1) {
                if (this.impactBuz != null && this.impactBuz.length > 0) {
                  this.currentAnalysisRow = 0;
                  this.impactBuz.forEach(element => {
                    this.currentKey = `${element.aResId}_${element.zResId}`;
                    if (this.currentImpactBuz.length == 0) {
                      this.lightPathId = element.opticalId
                      this.currentImpactBuz = this.allImpactBuzMap[element.opticalId]
                    }
                    //先看有没有计算过有的话跳过
                    if (this.currentKey in this.allPathMap) {
                      this.currentProgress = (this.currentAnalysisRow++ / this.impactBuz.length * 100).toFixed(2);
                      return
                    } else {
                      this.currentProgress = (this.currentAnalysisRow++ / this.impactBuz.length * 100).toFixed(2);
                      this.allPathMap[this.currentKey] = []
                      this.queryParams.aResId = element.aResId
                      this.queryParams.zResId = element.zResId
                      pathAlgorithmsApi(this.queryParams).then((res) => {
                        const key = `${element.aResId}_${element.zResId}`
                        if (res && res.length > 0) {
                          if (this.allPaths.length == 0) {
                            this.buzId = element.id
                            this.allPaths = res;
                          }
                          this.allPathMap[key] = res;
                        } else {
                          if (this.allPaths.length == 0) {
                            this.allPaths = [{ 'pathName': '未找到合适路径' }]
                          }
                          this.allPathMap[key] = [{ 'pathName': '未找到合适路径' }]
                        }
                        this.loading = false
                        if (this.currentAnalysisRow == this.impactBuz.length) {
                          this.analysisbuttonStatu = true
                        }
                      })
                    }
                  })
                } else {
                  this.analysisbuttonStatu = true
                }
              } else {
                //如果是光路保护算法，只需分析该受损光缆的迂回路径
                if (this.impactSituation != null && this.impactSituation.length > 0) {
                  this.currentProgress = 100;
                  this.queryParams.aResId = res.data.fiber[0].aResId
                  this.queryParams.zResId = res.data.fiber[0].zResId
                  pathAlgorithmsApi(this.queryParams).then((res) => {
                    if (res && res.length > 0) {
                      this.allPaths = res
                      this.setRouteInfo()
                    } else {
                      this.allPaths = [{pathName: '未找到合适路径', pathDetail: {}}]
                    }
                    this.loading = false
                    this.analysisbuttonStatu = true
                  })
                }
                // //如果是光路保护算法，遍历光路
                // if (this.impactSituation != null && this.impactSituation.length > 0) {
                //   this.currentAnalysisRow = 0;
                //   this.impactSituation.forEach(element => {
                //     this.currentKey = `${element.aResId}_${element.zResId}`;
                //     //先看有没有计算过有的话跳过
                //     if (this.currentKey in this.allPathMap) {
                //       this.currentProgress = (this.currentAnalysisRow++ / this.impactBuz.length * 100).toFixed(2);
                //       return
                //     } else {
                //       this.currentProgress = (this.currentAnalysisRow++ / this.impactBuz.length * 100).toFixed(2);
                //       this.allPathMap[this.currentKey] = []
                //       this.queryParams.aResId = element.aResId
                //       this.queryParams.zResId = element.zResId
                //       pathAlgorithmsApi(this.queryParams).then((res) => {
                //         const key = `${element.aResId}_${element.zResId}`
                //         if (res && res.length > 0) {
                //           if (this.allPaths.length == 0) {
                //             this.allPaths = res;
                //           }
                //           this.allPathMap[key] = res;
                //         } else {
                //           if (this.allPaths.length == 0) {
                //             this.allPaths = [{ 'pathName': '未找到合适路径' }]
                //           }
                //           this.allPathMap[key] = [{ 'pathName': '未找到合适路径' }]
                //         }
                //         this.loading = false
                //         if (this.currentAnalysisRow == this.impactSituation.length) {
                //           this.analysisbuttonStatu = true
                //         }
                //       })
                //     }
                //   })
                // }
              }
            } else {
              this.analysisbuttonStatu = true
              this.loading = false
              this.allPaths = []
            }
          });
        } else {
          //调度算法
          pathAlgorithmsApi(this.queryParams).then((res) => {
            if (res && res.length > 0) {
              this.allPaths = res
              if (this.queryParams.resType == 1){
                this.setRouteInfo()
              }
            } else {
              this.allPaths = [{pathName: '未找到合适路径', pathDetail: {}}]
            }
            this.loading = false
          })
        }
      }
    },
    setRouteInfo(){
      this.allPaths.forEach(item => {
        if(item.path && item.pathName){
          let pathList = item.path.split("->")
          let pathNameList = item.pathName.split("-->")
          let pathInfo = []
          let fiberLen = 0
          for (let i = 0; i < pathList.length; i++) {
            let fiberList = []
            let fiberId = ''
            let fiberName = ''
            let fiberTypeCn = ''
            let lineNumberAvailableStr = ''
            let fiberLength = 0
            let lineNumber = 0
            let lineNumberAvailable = 0
            if (i != pathList.length -1){
              fiberList = this.damagedFibers.filter(item =>
                (item.aResobjid == pathList[i] && item.zResobjid == pathList[i+1])
                || (item.zResobjid == pathList[i] && item.aResobjid == pathList[i+1])
              )
              let data  = fiberList.find(item => item.lineNumberAvailable > 1)
              if (data){
                fiberId = data.value
                fiberName = data.label
                fiberTypeCn = data.fiberTypeCn
                fiberLength = data.fiberLength
                lineNumberAvailable = data.lineNumberAvailable
                lineNumber = data.lineNumber
                lineNumberAvailableStr = data.lineNumberAvailableStr
              }else if (fiberList.length != 0){
                fiberId = fiberList[0].value
                fiberName = fiberList[0].label
                fiberTypeCn = fiberList[0].fiberTypeCn
                fiberLength = fiberList[0].fiberLength
                lineNumberAvailable = fiberList[0].lineNumberAvailable
                lineNumber = fiberList[0].lineNumber
                lineNumberAvailableStr = fiberList[0].lineNumberAvailableStr
              }
            }
            fiberLen += fiberLength
            pathInfo.push({
              path: pathList[i],
              pathName: pathNameList[i],
              fiberId: fiberId,
              fiberName: fiberName,
              fiberTypeCn: fiberTypeCn,
              fiberList: fiberList,
              fiberLength: fiberLength,
              lineNumber: lineNumber,
              lineNumberAvailable: lineNumberAvailable,
              lineNumberAvailableStr: lineNumberAvailableStr
            })
          }
          // item.pathInfo = JSON.parse(JSON.stringify(pathInfo))
          item.pathInfo = pathInfo
          item.fiberLength = fiberLen.toFixed(2)
        }
      })
    },
    impactSituationExport() {
      if (this.queryParams.damagedFiber == ''){
        this.$modal.msgSuccess("请选择受损光缆");
        return
      }else {
        this.$message({type: 'info', message: '正在导出...'})
        this.exportLoading = true;
        impactSituationExport(this.queryParams).then(res => {
          let lastInde = res.msg.lastIndexOf("/") + 1;
          let fileName = res.msg.substring(lastInde);
          fileDownload2({filename: res.msg}).then(res => {
            export_excel_file(res, fileName);
            this.exportLoading = false;
          }).catch(() => {
            this.exportLoading = false;
          });
        }).catch(() => {
          this.exportLoading = false;
        });
      }
    },


    analysisStop() {
      this.$modal.confirm('是否确认终止分析').then(function () {
      }).then(() => {
        this.analysisbuttonStatu = true
        this.loading = false
        this.$modal.msgSuccess("已终止");
      }).catch(() => { });
    },

    resetQuery() {
      this.resetForm("queryForm");
      this.queryParams = {
        resType: 1,
        algorithmPathType: 0,
        damagedFiber: '',
        aResId: '',
        zResId: '',
        algorithmPathRule: 0,
        tcPlane: '',
        siteType: '',
        fiberType: '',
        voltageClass: '',
        notPassRes: '',
        notPassFibers:'',
        mustPassRes: '',
        outNumber: 5,
        maxNodeNum: 10
      };
      this.analysisbuttonStatu = true
      this.allPaths = []
      this.disabled = false
    },


    selceted() {
      getResListByTypeApi(this.queryParams.resType).then((res) => {
        this.aResIds = res
        this.zResIds = res
        this.notPassRess = res
        this.mustPassRess = res
        this.queryParams.notPassRes = ''
        this.queryParams.mustPassRes = ''
        this.queryParams.aResId = ''
        this.queryParams.zResId = ''
      })
    },

    restFiber() {
      this.queryParams.aResId = ''
      this.queryParams.zResId = ''
      this.queryParams.damagedFiber = ''
    },

    /* 格式序列化列 */
    snFormatter: function (row, col, value, index) {
      return index + 1;
    },

    handleSelectionChange(row) {
      this.lightPathId = row.id
      this.currentImpactBuz = this.allImpactBuzMap[row.id]
    },
    handleSelectionChangeBuz(row) {
      this.buzId = row.id
      this.allPaths = this.allPathMap[row.aResId + '_' + row.zResId];
    },

    handleRowClick(row, column, event) {
      //业务保护算法
      if (this.queryParams.resType == 0 && this.queryParams.algorithmPathType == 1) {
        this.currentImpactBuz = this.allImpactBuzMap[row.id]
      } else {
        // this.allPaths = this.allPathMap[row.aResId + '_' + row.zResId];
      }
    },

    buzRowClick(row, column, event) {
      this.allPaths = this.allPathMap[row.aResId + '_' + row.zResId];
    },
    async initEcharts(val) {
      if (!val.path) return
      this.show = true
      this.mapLoading = true
      let ids = val.path.split('->')
      let names = val.pathName.split('-->')
      let node = []
      let link = []
      if (this.queryParams.resType == 1) {
        //查询站点坐标
        await querySiteInformation(ids).then(res => {
          for (let i = 0; i < res.data.length; i++) {
            let item = res.data[i]
            let data = {
              id: item.id,
              name: item.name,
              value: [item.longitude, item.latitude],
              type: 'node',
              symbolSize: 16,
              data: item,
              symbol: 'image://' + require('./site.svg')
            }
            node.push(data)
          }
        })
        for (let i = 0; i < ids.length - 1; i++) {
          let data = {
            id: +new Date(),
            type: 'link',
            source: ids[i],
            target: ids[i + 1],
            name: names[i] + '-->' + names[i + 1],
            lineStyle: {
              normal: {
                width: 2,
                opacity: 1,
                color: '#68b7f9',
              }
            }
          }
          link.push(data)
        }
      } else {
        //查询站点坐标
        await querySiteInformationByEquipment(ids).then(res => {
          for (let i = 0; i < res.data.length; i++) {
            let item = res.data[i]
            let data = {
              id: item.id,
              name: item.name,
              value: [item.longitude, item.latitude],
              type: 'node',
              symbolSize: 30,
              data: item,
              symbol: 'image://' + require('./ne.png')
            }
            if (!node.find(ii => ii.id == item.id)) {
              node.push(data)
            }
          }
          for (let i = 0; i < ids.length - 1; i++) {
            let item = res.data.find(item => item.sbId == ids[i])
            let item2 = res.data.find(item => item.sbId == ids[i + 1])
            if (item && item2) {
              let lName = item.name + '-->' + item2.name
              let data = {
                id: +new Date(),
                type: 'link',
                source: item.id,
                target: item2.id,
                name: lName,
                lineStyle: {
                  normal: {
                    width: 2,
                    opacity: 1,
                    color: '#000000',
                  }
                }
              }
              link.push(data)
            } else {
              if (!item) {
                //this.$message({type: 'info', message: names[i]+'未关联站点'})
              } else {
                //this.$message({type: 'info', message: names[i+1]+'未关联站点'})
              }

            }

          }
        })
      }



      // 数据变更时 销毁原模型
      var myChart = echarts.getInstanceByDom(document.getElementById('routerEcharts'))
      let zoom = 1.2
      let center = [119.56712417687297, 29.147780225220433]
      if (myChart !== undefined) {
        zoom = myChart.getOption().series[0].zoom
        center = [myChart.getOption().series[0].center[0], myChart.getOption().series[0].center[1]]
        echarts.dispose(myChart)
      }
      myChart = echarts.init(document.getElementById('routerEcharts'))
      myChart.clear()
      // 注册地图json
      echarts.registerMap('zhejiang', mapJson)
      let margin = 0.05
      let option = {
        tooltip: {
          borderColor: "#fff",
          extraCssText: 'box-shadow: 0 0 3px rgba(0, 0, 0, 0.3);',
          padding: [5, 10],
          formatter: function (val) {
            if (val.seriesType == 'graph' || val.valseriesType == 'scatter') {
              if (val.data.type == 'node') {
                return `站点名称 : ${val.data.name}`
              } else if (val.data.type == 'link') {
                return `名称 : ${val.data.name}`
              }
            }
          }
        },
        geo: [
          {
            map: 'zhejiang',
            type: 'map',
            zoom: zoom,
            aspectScale: 1,
            label: {
              normal: {
                show: false, // 是否显示标签。
              },
              emphasis: {
                show: false
              }
            },
            animationDurationUpdate: 0,
            roam: false,
            center: [center[0], center[1] + margin],
            zlevel: 1,
            itemStyle: {
              normal: {
                opacity: 1,
                areaColor: this.queryParams.resType == 1 ? '#bcc0cb' : '#fff',
                borderColor: this.queryParams.resType == 1 ? '#bcc0cb' : '#fff', // 线
                borderWidth: 0.5,
                shadowBlur: 20,
                shadowColor: this.queryParams.resType == 1 ? '#c8c9cd' : '#fff',
              },
              emphasis: {
                // 悬浮区背景
                areaColor: this.queryParams.resType == 1 ?  '#bcc0cb' : '#fff',
                label: {
                  show: false,
                },
              },
            }
          }
        ],
        series: [
          {
            map: 'zhejiang',
            type: 'map',
            zoom: zoom,
            aspectScale: 1,
            label: {
              normal: {
                show: false, // 是否显示标签。
              },
              emphasis: {
                show: false
              }
            },
            animationDurationUpdate: 0,
            roam: true,
            center: center,
            zlevel: 2,
            itemStyle: {
              normal: {
                opacity: 1,
                areaColor: '#fff',
                borderColor: this.queryParams.resType == 1 ? '#bdd0de' : '#fff', // 线
                borderWidth: 0.5
              },
              emphasis: {
                // 悬浮区背景
                areaColor: '#fff',
                label: {
                  show: false,
                },
              }
            }
          },
          {
            type: 'graph',
            coordinateSystem: 'geo',
            zlevel: 4,
            data: node,
            links: link,
            roam: true, // 是否开启鼠标缩放和平移漫游
            large: false,
            hoverAnimation: false,
            animation: false,
            // 站点名称显示
            label: {
              normal: {
                show: true,
                fontSize: 12,
                formatter: function (params) {
                  return params.data.name
                },
                position: 'bottom',
                color: this.queryParams.resType == 1 ? '#68b7f9' : '#6c758d',
              },
              emphasis: {
                show: true,
                fontSize: 12,
                formatter: function (params) {
                  return params.data.name
                },
                position: 'bottom',
                color: this.queryParams.resType == 1 ? '#68b7f9' : '#6c758d',
              }
            },
          },
        ]
      }
      myChart.on("georoam", function (params) {
        var option = myChart.getOption()
        if (params.zoom != null && params.zoom != undefined) {
          option.geo[0].zoom = option.series[0].zoom
        }
        //捕捉到拖曳时
        option.geo[0].center = [option.series[0].center[0], parseFloat(option.series[0].center[1] + margin)]
        myChart.setOption(option);
      });

      myChart.setOption(option)
      this.mapLoading = false
    },
    switchingModes(data){
      this.infoBox.width = data ? '1800px' : '500px'
      this.infoBox.class = data ? '' : 'framePosition'
    },
    getRouterLink(row,tableName){
      this.infoBox.isEditMode = false
      this.infoBox.show = true
      this.infoBox.width = '1600px'
      this.infoBox.class = ''
      this.infoBox.tableName = tableName
      let id
      if (tableName == 'SG_TCCON_TCCHANNEL_B'){
        id = row.channelId
      }else {
        id = row.id
      }
      this.infoBox.data = {ID: id}
    },
    selectChange(val,index,ind){
      let fiberList = this.allPaths[index].pathInfo[ind].fiberList
      let data = fiberList.find(item => item.value == val)
      this.allPaths[index].pathInfo[ind].fiberId = String(data.value)
      this.allPaths[index].pathInfo[ind].lineNumber = data.lineNumber
      this.allPaths[index].pathInfo[ind].lineNumberAvailable = data.lineNumberAvailable
      this.allPaths[index].pathInfo[ind].lineNumberAvailableStr = data.lineNumberAvailableStr
      this.allPaths[index].pathInfo[ind].fiberTypeCn = data.fiberTypeCn
      this.allPaths[index].pathInfo[ind].fiberLength = data.fiberLength
      this.allPaths[index].pathInfo[ind].fiberName = data.label
      let fiberLength = 0
      let pathInfo = this.allPaths[index].pathInfo
      pathInfo.forEach(item => {
        fiberLength += parseFloat(item.fiberLength)
      })
      this.allPaths[index].fiberLength = fiberLength.toFixed(2)
      this.$forceUpdate()
    },
    copyFocusedBoxContent(item){
      let pathInfo = item.pathInfo
      let text = ''
      for (let i = 0; i < pathInfo.length; i++) {
        let data = pathInfo[i]
        let pathNameA = data.pathName
        let fiberName = data.fiberName
        if (i != pathInfo.length - 1){
          let pathNameZ = pathInfo[i+1].pathName
          if (!data.fiberId){
            this.$message({ type: 'info', message: pathNameA + '-->' + pathNameZ + '未选择光缆' })
            return
          }
          if (data.lineNumberAvailable < 2){
            this.$message({ type: 'info', message: fiberName + '可用芯数不足2条' })
            return
          }
          let line = data.lineNumberAvailableStr.split(",").sort((a1,a2)=> a1 - a2)

          let lineName;
          let number = fiberName.indexOf("OPGW光缆")
          if( number !== -1){
            lineName = fiberName.substring(0,number)
          }else {
            lineName = pathNameA + '-' + pathNameZ
          }

          text += pathNameA + "-" + data.fiberTypeCn + '/' + lineName + '/' + line[0] + '&' + line[1] + '/' + data.lineNumber + '/' + data.fiberLength + 'km-'
        }else {
          text += pathNameA
        }
      }
      this.$copyText(text).then((e) => {
        this.$message({
          type: 'info',
          message: '复制成功'
        })
      }, (e) => {
        this.$message({
          type: 'info',
          message: '复制失败'
        })
      })
    },
    saveEmergencyRoute(item){
      let pathInfo = item.pathInfo
      let text = ''
      for (let i = 0; i < pathInfo.length; i++) {
        let data = pathInfo[i]
        let pathNameA = data.pathName
        let fiberName = data.fiberName
        if (i != pathInfo.length - 1){
          let pathNameZ = pathInfo[i+1].pathName
          if (!data.fiberId){
            this.$message({ type: 'info', message: pathNameA + '-->' + pathNameZ + '未选择光缆' })
            return
          }
          if (data.lineNumberAvailable < 2){
            this.$message({ type: 'info', message: fiberName + '可用芯数不足2条' })
            return
          }
          let line = data.lineNumberAvailableStr.split(",").sort((a1,a2)=> a1 - a2)

          let resobjnamn
          if (i == 0){
            resobjnamn = pathNameA + '/' + this.resInfo.aNeName + '/' + this.resInfo.aPortName
          }else {
            resobjnamn = pathNameA
          }

          let lineName;
          let number = fiberName.indexOf("OPGW光缆")
          if( number !== -1){
            lineName = fiberName.substring(0,number)
          }else {
            lineName = pathNameA + '-' + pathNameZ
          }

          text += resobjnamn + "-" + data.fiberTypeCn + '/' + lineName + '/' + line[0] + '&' + line[1] + '/' + data.lineNumber + '/' + data.fiberLength + 'km-'
        }else {
          text += pathNameA + '/' + this.resInfo.zNeName + '/' + this.resInfo.zPortName
        }
      }
      saveEmergencyRoute({
        id: this.resInfo.resId,
        emergencyRouting: text
      }).then(res => {
        this.$message({
          type: 'success',
          message: '保存成功'
        })
      })
    },
    saveBuzEmergencyRoute(path,index){
      this.loading = true
      saveBuzEmergencyRoute({
        id: this.resInfo.resId,
        path: path,
      }).then(res =>{
        this.loading = false
        this.$message({
          type: 'success',
          message: '保存成功'
        })
      }).catch(() => {
        this.loading = false
      })
    },
  },

  directives: {
    'el-select-loadmore': (el, binding) => {
      const SELECTWRAP_DOM = el.querySelector(".el-select-dropdown .el-select-dropdown__wrap");
      if (SELECTWRAP_DOM) {
        SELECTWRAP_DOM.addEventListener("scroll", function () {
          const condition = this.scrollHeight - this.scrollTop <= this.clientHeight;
          if (condition) binding.value();
        });
      }
    },
  }

};


</script>
<style scoped lang="scss">
.pathRecommendation {
  width: 100%;
  height: 100%;
  background-color: #fff;
  overflow: auto;
  .collapseHeaderBox {
    width: 100%;
    padding: 10px;
    border-radius: 3px;
    background-color: #f9f9fb;
    border: 1px solid #f1f1f2;
    .el-form {
      width: 100%;
      .el-form-item {
        display: inline-block;
        width: 25%;
        margin: 0;
        .el-form-item__content {
          .el-input__inner,
          .el-input,
          .el-input_inner,
          .el-input .el-input_inner,
          .el-select {
            width: 100%;
          }
        }
      }
    }
  }

  .editForm {
    width: 100%;
    height: 50%;
    .formTop {
      width: 100%;
      height: 40px;
      line-height: 40px;
      font-size: 16px;
      font-weight: bold;
      color: #079b9f;
    }
    .pathList{
      width: 100%;
      height: calc(100% - 40px);
      border: 2px solid #f1f1f2;
      overflow: auto;
    }
  }
  .business{
    width: 100%;
    height: 50%;
    display: flex;
    justify-content: space-between;
    .business-item{
      width: 49.5%;
      height: 100%;
      .business-item-top {
        width: 100%;
        height: 40px;
        line-height: 40px;
        font-size: 16px;
        font-weight: bold;
        color: #079b9f;
      }
      .business-item-content{
        width: 100%;
        height: calc(100% - 40px);
        border: 2px solid #f1f1f2;
        overflow: auto;
      }
    }
  }
}

.lineIcon {
  margin: 10px;
  padding-left: 50px;
  width: 100%;
  color: #000;
  line-height: 40px;
  background-image: url("../../assets/images/path.png");
  background-size: 30px 30px;
  background-repeat: no-repeat;
  background-position: 10px center;
  cursor: pointer;
  white-space: nowrap;
  .lineIconItem{
    display: flex;
    align-items: center;
    .fiberInfo{
      display: flex;
      flex-direction: column;
      align-items: center;
      font-size: 12px;
      line-height: 20px;
    }

  }

}
</style>
