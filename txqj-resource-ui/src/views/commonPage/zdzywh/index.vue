<template>
  <div class="commonWh">
    <div class="main-layout">
      <TreeSidebar v-if="showTree" ref="treeSidebar" :show-load-button="false" :default-active-path="activePath"
        @node-click="handleTreeNodeClick" @load-data="loadSelectedTreeNodeData" @tip="handleTreeTip"
        @auto-select="handleAutoSelect" />


      <div class="right-content-container" v-if="showRightContent">
        <div v-if="currentComponent === 'zdzywh'">
          <div class="box-card">
            <div class="queryBox">
              <div v-show="showLabelQuery && queryList.length > 0" class="advancedQuery">
                <div v-for="(item, index) in queryList" :key="index">
                  <div>
                    <div class="moreButton">
                      <el-button size="mini" @click.native="item.ifOpen = !item.ifOpen">
                        <span v-if="!item.ifOpen"> 更多<i class="el-icon-arrow-down"></i></span>
                        <span v-else>收起 <i class="el-icon-arrow-up"></i></span>
                      </el-button>
                      <el-button size="mini" @click.native="item.ifMoreChoose = !item.ifMoreChoose">
                        <span v-if="!item.ifMoreChoose"> 多选</span>
                        <span v-else><i class="el-icon-document"></i> 单选</span>
                      </el-button>
                    </div>
                    <div class="queryTitle">
                      <p>{{ item.label }} : </p>
                    </div>
                    <div>
                      <el-radio-group style=" display: none;" @change="getAbcData(index)" v-model="item.initial">
                        <el-radio v-for="opa in abcList" :value="opa.value" :label="opa.value">{{ opa.label }}</el-radio>
                      </el-radio-group>
                      <el-radio-group v-if="!item.ifMoreChoose" v-model="item.radioValue">
                        <el-radio v-for="(opa, opaIndex) in item.listRadio" :value="opa.code" :label="opa.code"
                          @change="getTableData()">{{ opa.name }}</el-radio>
                      </el-radio-group>
                      <el-checkbox-group v-if="item.ifMoreChoose" v-model="item.checkValue">
                        <el-checkbox v-for="(opa, opaIndex) in item.list" :value="opa.code" :label="opa.code"
                          @change="getTableData()">{{ opa.name }}</el-checkbox>
                      </el-checkbox-group>
                    </div>
                  </div>
                </div>
              </div>
              <div class="openTit" @click="showLabelQuery = !showLabelQuery" v-if="queryList.length > 0">
                <p v-if="!showLabelQuery">展开 <i class="el-icon-arrow-down"></i></p>
                <p v-else>收起 <i class="el-icon-arrow-up"></i></p>
              </div>
              <div ref="basicsQuery" class="basicsQuery">
                <el-form :model="searchForm">
                  <el-form-item v-for="(item, index) in searchList" :key="index"
                    :label="item.relateSearchName ? item.relateSearchName : item.attributeNameCn" label-width="100px">
                    <el-select size="mini" v-if="item.attType === 'select'" clearable filterable :loading="selLoading"
                      remote @keydown.enter.native="getTableData()" v-loadmore="loadmore" @focus="focusSelect = item"
                      :remote-method="(val) => remoteMethod(val)" v-model="searchForm[item.attributeNameUnderline]">
                      <el-option v-for="arr in item.list" :label="arr.name" :key="arr.code" :disabled="arr.disabled"
                        :value="arr.code"
                        :title="arr.manageDept ? arr.name + ' （ ' + arr.manageDept + ' ）' : arr.name"></el-option>
                    </el-select>
                    <el-date-picker v-model="searchForm[item.attributeNameUnderline]" @keyup.enter.native="getTableData()"
                      type="datetime" size="mini" v-else-if="item.attType === 'datetime'"
                      value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期时间">
                    </el-date-picker>
                    <el-input size="mini" @keyup.enter.native="getTableData()" v-else
                      v-model="searchForm[item.attributeNameUnderline]"></el-input>
                  </el-form-item>
                  <el-form-item label-width="0" style="float: right; width: 50%; text-align: right">
                    <el-button type="primary" size="mini" @click="getTableData()">查询</el-button>
                    <el-button size="mini" @click="resetSearch()">重置</el-button>
                    <el-button size="mini" @click="openTool" v-hasPermi="['mm:model:isShow']">配置</el-button>
                    <el-button type="primary" size="mini" @click="openImportDialog" v-hasPermi="['metadata:rule:isShow']">
                      导入
                    </el-button>
                  </el-form-item>
                </el-form>
              </div>
            </div>

            <div ref="tableBox" class="tableBox">
              <div class="tabTopButton">
                <slot v-if="tabOperate" name="tabOperate"></slot>
                <el-button v-if="!tabOperate" size="mini" type="primary" @click="openAdd()"
                  v-hasPermi="['system:b:add']">新增</el-button>
                <el-button v-if="!tabOperate" size="mini" type="danger" @click="deleteSta()"
                  v-hasPermi="['system:b:remove']">删除</el-button>
                <el-button v-if="!tabOperate" size="mini" @click="handleExport()">导出</el-button>
                <el-button v-if="!tabOperate" size="mini" @click="openWindow" icon="el-icon-full-screen">全屏</el-button>
              </div>
              <el-table :row-key="getRowKeys" tooltip-effect="light" border highlight-current-row :height="tableHeight"
                style="width: 100%; " :data="tableData" ref="multipleTable" v-bind="$attrs" :row-style="rowStyle"
                :span-method="objectSpanMethod" @sort-change="sortChange" @row-contextmenu="rowContextmenu"
                @select="judgeSelectTick" @select-all="handleSelectAll" v-loading="loading">
                <el-table-column :reserve-selection="true" type="selection" fixed="left" width="50">
                </el-table-column>
                <el-table-column label="序号" width="80" align="center" :formatter="snFormatter">
                  <template slot-scope="scope">
                    <div style="display: flex;justify-content: center; align-items: center">
                      <div style="display: flex;flex-direction:column"
                        v-if="tableName === 'SG_TCCON_TCBUZ_B' && scope.row.ICON">
                        <i v-if="scope.row.ICON && scope.row.ICON.includes('同缆')" title="同缆" class="el-icon-star-on"
                          style="color:#0663ef;cursor:pointer;"></i>
                        <i v-if="scope.row.ICON && scope.row.ICON.includes('三路由')" title="三路由" class="el-icon-star-on"
                          style="color:#d53ce6;cursor:pointer;"></i>
                        <i v-if="scope.row.ICON && scope.row.ICON.includes('路由图')" title="路由图" class="el-icon-star-on"
                          style="color:#47e63c;cursor:pointer;"></i>
                        <i v-if="scope.row.ICON && scope.row.ICON.includes('不可靠路由')" title="不可靠路由" class="el-icon-star-on"
                          style="color:#ff003b;cursor:pointer;"></i>
                      </div>
                      <span>{{ snFormatter(scope) }}</span>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column v-if="tableData[0]" resizable show-overflow-tooltip v-for="item in tableColConfig"
                  :sortable="item.sortable == '1'"
                  :label="item.attributeNameCn === undefined ? item.attributeName : item.attributeNameCn"
                  :prop="item.attributeNameUnderline" :key="item.id" :min-width="getColSize(
                    tableData[0][item.attributeNameUnderline],
                    item.attributeNameCn === undefined ? item.attributeName : item.attributeNameCn
                  )" align="center" :sort-method="sortByDate">
                  <template slot-scope="scope">
                    <span :style="getStyle(item.styleList, scope.row[item.attributeNameUnderline])">
                      <a class="linkColumn" v-if="item.isJumpLink == '1'"
                        @click="getRouterLink(item, scope.row[item.attributeNameUnderline])">{{
                          scope.row[item.attributeNameUnderline] }}</a>
                      <template v-else>
                        {{ scope.row[item.attributeNameUnderline] }}
                      </template>
                    </span>
                  </template>
                </el-table-column>
                <el-table-column label="操作" :width="operate ? operate.width : 180" fixed="right" align="center">
                  <template slot-scope="scope">
                    <slot :scope="scope" v-if="operate" :name="operate.slot"></slot>
                    <el-button v-if="!operate" type="primary" size="mini" @click="openEdit(scope)"
                      v-hasPermi="['system:b:update']">编辑</el-button>
                    <el-button v-if="!operate" type="primary" size="mini" @click="searchStation(scope)"
                      v-hasPermi="['system:b:list']">查看</el-button>
                    <el-button v-if="!operate" type="danger" size="mini" @click="delData(scope)"
                      v-hasPermi="['system:b:remove']">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
              <pagination :total="total" :page.sync="pageIndex" :limit.sync="pageSize" @pagination="getTableData" />
            </div>
          </div>

          <el-dialog v-dialogDrag custom-class="curdBox" title="修改" :modal="false" :visible.sync="editStationDrawer"
            width="1800px" :close-on-click-modal="false">
            <editData ref="child" :modelObj="this.modelObj" :dataObj="this.editForm"></editData>
            <div slot="footer" class="dialog-footer">
              <el-button type="primary" @click="submitEditForm()">确 定</el-button>
              <el-button @click="editStationDrawer = false">取 消</el-button>
            </div>
          </el-dialog>
          <el-dialog v-dialogDrag custom-class="curdBox" title="新增" :modal="false" :close-on-click-modal="false"
            :visible.sync="newStationDrawer" width="1800px">
            <editData ref="child" :modelObj="this.modelObj" :dataObj="this.editForm"></editData>
            <div slot="footer" class="dialog-footer">
              <el-button type="primary" @click="submitForm()">确 定</el-button>
              <el-button @click="newStationDrawer = false">取 消</el-button>
            </div>
          </el-dialog>
          <el-dialog v-dialogDrag custom-class="curdBox" title="查看" :modal="false" :visible.sync="stationDrawer"
            width="1800px" :close-on-click-modal="false">
            <seeData ref="seeChild" :modelObj="this.modelObj" :dataObj="this.editForm"></seeData>
            <div slot="footer" class="dialog-footer">
              <el-button @click="stationDrawer = false">关 闭</el-button>
            </div>
          </el-dialog>
          <el-dialog v-dialogDrag title="展示配置" :modal="false" :close-on-click-modal="false" width="80%"
            :visible.sync="openShow" top="5vh">
            <div style="width: 100%; height: 70vh">
              <zspz :versionObj="this.versionObj"></zspz>
            </div>
          </el-dialog>
          <el-dialog v-dialogDrag title="自定义查询" :modal="false" :close-on-click-modal="false" width="58%"
            :visible.sync="openCustom" top="20vh">
            <el-row :gutter="20">
              <el-col :span="7"><el-card class="box-card" style="width:300px" shadow="never">
                  <div slot="header" class="clearfix">
                    <span>属性列表</span>
                  </div>
                  <div class="left">
                    <el-input v-model="filterText" placeholder="请输入属性名称" clearable size="small"
                      prefix-icon="el-icon-search" style="margin-bottom: 20px" />
                    <el-tree class="filter-tree" :data="deptOptions" highlight-current default-expand-all
                      node-key="data.id" :filter-node-method="filterNode" ref="treeForm"
                      style="font-size: 14px;height:26.5vh;overflow-y: auto;">
                      <span style="width: 100%;height:100%;display: block" class="custom-tree-node"
                        slot-scope="{ node, data }">
                        <span :class="data.isHigh === '1' ? 'red' : ''"
                          style="width: 100%;display: block;line-height: 25px"
                          @click="handleNodeClick(data.columnName, data.columnNameCn)">
                          {{ data.columnNameCn }}
                        </span>
                      </span>
                    </el-tree>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="1">
                <div style="margin-top: 180px;">
                  <img src="../../../assets/images/arrow.png" />
                </div>
              </el-col>
              <el-col :span="7">
                <el-card class="box-card" style="width:300px;height:40vh" shadow="never">
                  <div slot="header" class="clearfix">
                    <span>新增查询条件</span>
                    <el-button style="float: right; padding: 3px 10px;z-index:9999" type="text"
                      @click="resetQuery">重置</el-button>
                  </div>
                  <div class="center">
                    <el-form label-width="100px" :model="addQueryForm" :rules="addQueryRules" ref="addQueryForm">
                      <el-form-item label="属性名称：" prop="name">
                        <el-input v-model="addQueryForm.name" disabled></el-input>
                      </el-form-item>
                      <el-form-item label="逻辑关系：" prop="relationship">
                        <el-select v-model="addQueryForm.relationship">
                          <el-option label="或（or）" value="or"></el-option>
                          <el-option label="与（and）" value="and"></el-option>
                        </el-select>
                      </el-form-item>
                      <el-form-item label="操作符：" prop="operator">
                        <el-select v-model="addQueryForm.operator">
                          <el-option label="不为空" value="不为空"></el-option>
                          <el-option label="大于" value="大于"></el-option>
                          <el-option label="大于等于" value="大于等于"></el-option>
                          <el-option label="小于" value="小于"></el-option>
                          <el-option label="小于等于" value="小于等于"></el-option>
                          <el-option label="等于" value="等于"></el-option>
                          <el-option label="包含" value="包含"></el-option>
                        </el-select>
                      </el-form-item>
                      <el-form-item label="值：" prop="value">
                        <el-input v-model="addQueryForm.value"></el-input>
                      </el-form-item>
                      <el-form-item>
                        <el-button icon="el-icon-plus" @click="addQuery('addQueryForm')">添加</el-button>
                      </el-form-item>
                    </el-form>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="1">
                <div style="margin-top: 180px;">
                  <img src="../../../assets/images/arrow.png" />
                </div>
              </el-col>
              <el-col :span="7">
                <el-card class="box-card" style="width:300px;height:40vh" shadow="never">
                  <div slot="header" class="clearfix">
                    <span>查询条件列表</span>
                    <el-button style="float: right; padding: 3px 10px;z-index:9999" type="text"
                      @click="clearLocal">清空</el-button>
                  </div>
                  <div>
                    <div v-for=" (item, index) in list"
                      style="display:block;margin-bottom:5px;padding-top: 5px;padding-left:5px;border: 1px solid #ECECEC;overflow: hidden;">
                      <span>{{ index + 1 }}.&nbsp;</span>
                      <span style="color:#006A65">{{ item.relationship }}</span>
                      <span>&nbsp;{{ item.nameCn }}{{ item.operator }}{{ item.value }}</span>
                      <el-button type="text" @click="delLocal(index)"
                        style="float:right;position:relative;bottom:8px;right:5px"></el-button>
                    </div>
                  </div>
                </el-card>
              </el-col>
            </el-row>
            <div slot="footer" class="dialog-footer">
              <el-button type="primary" @click="openCustom = false">确 定</el-button>
            </div>
          </el-dialog>
          <el-dialog v-dialogDrag title="数据导入" :modal="true" :visible.sync="importDialog.show" width="500px"
            :close-on-click-modal="false" destroy-on-close>
            <div class="import-box">
              <div class="import-item">
                <label class="import-label">步骤1：下载模板</label>
                <el-button type="primary" icon="el-icon-download" @click="handleDownloadTemplate"
                  :loading="importDialog.downloadLoading">
                  下载系统标准模板
                </el-button>
                <p class="tips-text">点击下载Excel模板，按模板格式填写数据后再上传</p>
              </div>
              <div class="import-item">
                <label class="import-label">步骤2：上传文件</label>
                <el-upload class="upload-demo" drag :action="''" :auto-upload="false" :file-list="importDialog.fileList"
                  :accept="'.xlsx,.xls'" :limit="1" :on-change="handleFileChange" :on-exceed="handleFileExceed"
                  :before-upload="beforeUploadFile">
                  <i class="el-icon-upload"></i>
                  <div class="el-upload__text">将Excel文件拖到此处，或<em>点击上传</em></div>
                  <div class="el-upload__tip" slot="tip">
                    仅支持.xlsx/.xls格式文件，且文件大小不超过10MB
                  </div>
                </el-upload>
              </div>
            </div>
            <div slot="footer" class="dialog-footer">
              <el-button @click="importDialog.show = false">取消</el-button>
              <el-button type="primary" @click="handleSubmitImport" :loading="importDialog.uploadLoading"
                :disabled="importDialog.fileList.length === 0">
                提交导入
              </el-button>
            </div>
          </el-dialog>
          <div id="menu" class="menuDiv">
            <ul class="menuUl">
              <li v-for="(item, index) in rightClickMenus" :key="index" @click.stop="infoClick(item)" @mouseout="mouseout"
                @mouseover="mouseover">
                <i :class="item.icon"></i> {{ item.name }}
              </li>
            </ul>
          </div>
          <el-dialog v-dialogDrag title="批量转换属性" top="5vh" :visible.sync="transitionBox.show" :modal="false"
            :close-on-click-modal="false" width="70%">
            <div style="width: 100%; height: 70vh">
              <el-table v-loading="transitionBox.loading" :data="transitionBox.tableData" height="100%"
                style="width: 100%">
                <el-table-column prop="generateRuleName" label="规则名称" min-width="200" align="center">
                </el-table-column>
                <el-table-column prop="generateRule" label="规则内容" min-width="200" align="center">
                </el-table-column>
                <el-table-column prop="generateRuleExample" label="规则范例" min-width="200" align="center">
                </el-table-column>
                <el-table-column prop="regularExecution" label="是否定期执行" min-width="200" align="center">
                </el-table-column>
                <el-table-column type="index" label="序号" width="50" align="center">
                </el-table-column>
                <el-table-column prop="columnNameCn" label="中文属性名" min-width="150" align="center">
                </el-table-column>
                <el-table-column prop="columnName" label="英文属性名" min-width="150" align="center">
                </el-table-column>
                <el-table-column label="属性使用规则" min-width="120" align="center">
                  <template slot-scope="scope">
                    <span v-if="scope.row.ruleUsed == 0">参数规则</span>
                    <span v-if="scope.row.ruleUsed == 1">SQL转换规则</span>
                  </template>
                </el-table-column>
                <el-table-column label="操作" fixed="right" width="80" align="center">
                  <template slot-scope="scope">
                    <el-button size="mini" type="text" @click="selTransition(scope.row)"
                      icon="el-icon-thumb">转换</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-dialog>
        </div>

        <div v-if="currentComponent === 'nMinusOne'">
          <!-- 直接引入风险分析组件 -->
          <NMinusOne ref="nMinusOne" :tree-node="selectedTreeNode" />
        </div>

        <div v-if="currentComponent === 'fiberManage'">
          <FiberManage ref="fiberManage" :tree-node="selectedTreeNode" />
        </div>


      </div>

      <!-- 未选择节点时的提示 -->
      <div class="right-content-placeholder" v-else>
        <div style="display: flex; justify-content: center; align-items: center; height: 100%; color: #999">
          <div style="text-align: center">
            <i class="el-icon-menu" style="font-size: 48px; margin-bottom: 10px;"></i>
            <p>请在左侧选择数据模型并点击"加载选中数据"按钮</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import TreeSidebar from '@/components/TreeSidebar/index.vue';
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import NMinusOne from '@/views/nMinusOne/index.vue';
// import FiberManage from '@/views/manageFiber/fiber/index.vue';
import zspz from "./zspz";
import {
  tree,
  getModelId,
  submitEdit,
  getData,
  getForm,
  submit,
  errorStation,
  initgriddatasite,
  fileDownload,
  export_excel_file,
  exportExcel,
  deleteStation,
  deleteError,
  tableDropDownBox,
  selectAllRules,
  getColunmId,
  getVerrules,
  getTransitionTableDataApi,
  commitTransitionApi,
  obtainIDBasedOnFilteringCriteria,
  downloadImportTemplate,
  importDataByExcel
} from "@/api/zdwh/zdzywh";
import { mapMessage, conditionalData } from "@/api/zdwh/zdzywh";
import editData from "@/views/publicPage/edit/editData";
import seeData from "@/views/publicPage/edit/seeData";
import { getColSize } from "@/utils/getColSize"

function buildTreeData(list, idKey = 'ID', parentIdKey = 'PARENT_ID', labelKey = 'NAME') {
  const treeData = [];
  const map = {};
  list.forEach(item => {
    map[item[idKey]] = {
      id: item[idKey],
      label: item[labelKey],
      parentId: item[parentIdKey] || 0,
      ...item
    };
  });
  list.forEach(item => {
    const node = map[item[idKey]];
    if (node.parentId === 0 || !map[node.parentId]) {
      treeData.push(node);
    } else {
      if (!map[node.parentId].children) {
        map[node.parentId].children = [];
      }
      map[node.parentId].children.push(node);
    }
  });
  return treeData;
}

export default {
  name: "Zdzywh",
  components: {
    zspz, editData, seeData, TreeSidebar, NMinusOne,
    FiberManage: () => import('@/views/manageFiber/fiber/index.vue')
  },
  props: {
    showTree: {
      type: Boolean,
      default: true
    },
    tableName: {
      type: String,
      default: 'SG_TCCON_TCSITE_B' // 默认模型
    },
    tabOperate: {
      type: Boolean,
      default: () => false
    },
    isSearch: {
      type: Boolean,
      default: () => false
    },
    operate: {
      type: Object,
      default: () => { }
    },
    searchData: {
      type: Object,
      default: () => { return {} }
    },
    rightClickMenus: {
      type: Array,
      default: () => { return [] }
    }
  },
  data() {
    return {
      innerTableName: this.innerTableName,
      tableHeight: 'auto',
      currentComponent: 'zdzywh',
      activePath: '',
      globalSearchKeyword: '',
      // 控制右侧内容显示
      showRightContent: true, // 默认显示右侧内容
      // 记录选中的树节点
      selectedTreeNode: null,


      currentTreeNode: null,
      importDialog: {
        show: false,
        downloadLoading: false,
        uploadLoading: false,
        fileList: []
      },
      selLoading: false,
      focusSelect: {},
      searchStr: '',
      addQueryForm: {
        name: '',
        relationship: '',
        operator: '',
        value: ''
      },
      addQueryRules: {
        name: [{ required: true, message: '请选择属性名称', trigger: ['change', 'blur'] }],
        relationship: [{ required: true, message: '请选择逻辑关系', trigger: ['change', 'blur'] }],
        operator: [{ required: true, message: '请选择操作符', trigger: ['change', 'blur'] }]
      },
      openCustom: false,
      versionObj: {},
      openShow: false,
      loading: false,
      showLabelQuery: false,
      showLabelQueryOpen: false,
      radio: '',
      radioList: [],
      abcList: [
        { label: '全部', value: '' }, { label: 'A', value: 'A' }, { label: 'B', value: 'B' },
        { label: 'C', value: 'C' }, { label: 'D', value: 'D' }, { label: 'E', value: 'E' },
        { label: 'F', value: 'F' }, { label: 'G', value: 'G' }, { label: 'H', value: 'H' },
        { label: 'I', value: 'I' }, { label: 'J', value: 'J' }, { label: 'K', value: 'K' },
        { label: 'L', value: 'L' }, { label: 'M', value: 'M' }, { label: 'N', value: 'N' },
        { label: 'O', value: 'O' }, { label: 'P', value: 'P' }, { label: 'Q', value: 'Q' },
        { label: 'R', value: 'R' }, { label: 'S', value: 'S' }, { label: 'T', value: 'T' },
        { label: 'U', value: 'U' }, { label: 'V', value: 'V' }, { label: 'W', value: 'W' },
        { label: 'X', value: 'X' }, { label: 'Y', value: 'Y' }, { label: 'Z', value: 'Z' },
      ],
      queryList: [],
      modelObj: {},
      cluList: [],
      tableColConfig: [],
      searchList: [],
      nullList: [],
      notNullList: [],
      searchForm: {},
      pageSize: 15,
      pageIndex: 1,
      total: 0,
      tableData: [],
      filterText: '',
      multipleSelection: [],
      deptOptions: [],
      allRulesForm: {},
      newStationDrawer: false,
      addForm: {},
      rulesForm: {},
      showAdd: true,
      addErrForm: [],
      stationDrawer: false,
      editForm: {},
      editStationDrawer: false,
      list: [],
      index: '',
      ifFirstEdit: true,
      ifFirstSee: true,
      column: {},
      rightClikcRow: {},
      transitionBox: {
        show: false,
        loading: false,
        tableData: [],
      }
    }
  },
  watch: {
    '$route'(to) {
    this.activePath = to.path;
    },
    tableName: {
      immediate: true,
      handler(newVal) {
        this.innerTableName = newVal;
      }
    },
    filterText(val) {
      this.$refs.treeForm.filter(val);
    },
    'tableData': {
      handler() {
        this.$nextTick(() => {
          this.$refs.multipleTable.doLayout()
        })
      },
      deep: true
    },
    'list.length': {
      handler(newValue, oldValue) {
        if (newValue !== oldValue) {
          for (let j = 0; j < this.deptOptions.length; j++) {
            this.$set(this.deptOptions[j], 'isHigh', '0')
          }
          this.ifHight()
        }
      }
    },
    openShow() {
      if (!this.openShow) {
        this.versionObj = {}
        this.clearLocal()
        this.getCluList()
        this.getQueryList()
      }
    },
  },
  activated() {
    if (this.$route.query.refresh) {
      this.$nextTick(() => {
        setTimeout(() => {
          this.$nextTick(() => {
            this.refresh()
          })
        }, 100)
      })
    }
  },
  mounted() {
    this.initTreeData();
    this.activePath = this.$route.path; 
    this.calcTableHeight();
    window.addEventListener('resize', this.calcTableHeight);

    this.$nextTick(async () => {
      this.globalSearchKeyword = this.$route.query.searchKeyword || '';

      if (this.globalSearchKeyword) {
        this.handleGlobalSearch(this.globalSearchKeyword);
      }

      // 根据 tableName 加载数据（如果有）
      if (this.innerTableName) {
        this.showRightContent = true;
        await this.loadTreeNodeData();
      } else {
        this.showRightContent = false;
      }
    });
  },
  methods: {


    initTreeData() {
      this.treeData = []
      // 增加空值判断
      const topbarRouters = this.$store.state.permission?.topbarRouters || [];
      let arr = topbarRouters.filter(item => item && !item.hidden); // 过滤空值和隐藏项

      for (let k = 0; k < arr.length; k++) {
        const parentNode = arr[k];
        if (!parentNode?.children || parentNode.children.length === 0) continue;

        const groupBasePath = parentNode.path;
        const treeNode = {
          path: groupBasePath,
          label: parentNode.meta?.title || '未命名', // 增加默认值
          isLeaf: false,
          children: []
        }

        let sArr = parentNode.children.filter(item => item && !item.hidden);
        for (let v = 0; v < sArr.length; v++) {
          const childNode = sArr[v];
          const fullPath = `${groupBasePath}/${childNode.path}`.replace(/\/+/g, '/');

          treeNode.children.push({
            path: fullPath,
            label: childNode.meta?.title || '未命名', // 增加默认值
            query: childNode.query,
            tableName: childNode.query ? (JSON.parse(childNode.query).tableName || '') : '',
            isLeaf: true
          })
        }
        this.treeData.push(treeNode)
      }
    },
    beforeDestroy() {
      // 清理右键菜单
      const menu = document.querySelector("#menu");
      if (menu) {
        menu.style.display = "none";
      }
      // 清理 localStorage
      // 如果不需要持久化查询条件，可以清空
      // localStorage.removeItem('cmts');

      // 取消所有未完成的请求（如果使用 axios）
      if (this.cancelTokenSource) {
        this.cancelTokenSource.cancel('组件销毁，取消请求');
      }
      window.removeEventListener('resize', this.calcTableHeight);
    },
    handleGlobalSearch(keyword) {
      if (!keyword) {
        this.getTableData(); // 无关键词则重新加载全部数据
        return;
      }

      // 方案A：前端筛选（适合数据量小）
      if (this.tableData.length > 0) {
        const filteredData = this.tableData.filter(row => {
          // 遍历所有列，匹配关键词（忽略大小写）
          return Object.values(row).some(value => {
            if (value === null || value === undefined) return false;
            return String(value).toLowerCase().includes(keyword.toLowerCase());
          });
        });
        this.tableData = filteredData;
        this.total = filteredData.length;
      }
      // 方案B：后端筛选（适合数据量大，推荐）
      else {
        // 修改getTableData的参数，增加全局搜索条件
        this.searchForm.globalSearch = keyword;
        this.getTableData();
      }

      this.$message.info(`搜索到 ${this.tableData.length} 条相关数据`);
    },
    calcTableHeight() {
      this.$nextTick(() => {
        try {
          const tableBox = this.$refs.tableBox;
          if (tableBox) {
            const tabTopButton = tableBox.querySelector('.tabTopButton');
            const buttonHeight = tabTopButton ? tabTopButton.offsetHeight : 40;
            const pagination = tableBox.querySelector('.pagination');
            const paginationHeight = pagination ? pagination.offsetHeight : 30;
            const containerHeight = tableBox.clientHeight;
            this.tableHeight = containerHeight - buttonHeight - paginationHeight - 20;
            if (this.tableHeight < 440) {
              this.tableHeight = 440;
            }
          }
        } catch (e) {
          console.error('计算表格高度失败：', e);
          // 关键修改：默认高度从 400px 改为 600px
          this.tableHeight = 400;
        }
      });
    },


    handleTreeNodeClick(data, node) {
      if (!data || !data.isLeaf) return;

      // // 风险分析页面
      // if (data.path.includes('/analysis/nMinusOne')) {
      //   this.$router.push({
      //     path: '/resourceManagement/analysis/nMinusOne',
      //     query: { treeNode: JSON.stringify(data) }
      //   });
      //   return;
      // }

      // // 光缆管理页面
      // if (data.path.includes('fibersource/fiber')) {
      //   this.$router.push({
      //     path: '/fibersource/fiber',
      //     query: { treeNode: JSON.stringify(data) }
      //   });
      //   return;
      // }

      // // 业务信息页面
      // if (data.path.includes('/businesssource/serviceInformation')) {
      //   this.$router.push({
      //     path: '/businesssource/serviceInformation',
      //     query: { treeNode: JSON.stringify(data) }
      //   });
      //   return;
      // }

      // // 光路信息页面
      // if (data.path.includes('/transsource/lightPathInfoManage')) {
      //   this.$router.push({
      //     path: '/transsource/lightPathInfoManage',
      //     query: { treeNode: JSON.stringify(data) }
      //   });
      //   return;
      // }

      this.$router.push({
        path: data.path,
      });
    },
    handleTreeTip({ type, message }) {
      this.$message[type](message);
    },

    // 新增：处理树组件自动选中事件
    handleAutoSelect(node) {
      this.selectedTreeNode = node;
      // 自动选中后加载数据（和原逻辑一致）
      const nodeTableName = node.tableName || (() => {
        const pathSegments = node.path.split('/').filter(seg => seg);
        return pathSegments.pop() || '';
      })();
      if (nodeTableName && node.path.includes('/zdzy/')) {
        this.innerTableName = nodeTableName;
        this.loadTreeNodeData();
      }
    },

    async loadTreeNodeData() {
      if (!this.innerTableName) {
        // 非表模型节点，直接返回
        return;
      }

      if (!this.loading) {
        this.loading = true;
      }

      try {

        // 1. 校验tableName是否有效
        if (!this.innerTableName) {
          throw new Error('tableName为空，无法加载数据');
        }

        // 2. 获取模型ID（增加超时处理）
        const modelRes = await Promise.race([
          getModelId({ tableName: this.innerTableName }),
          new Promise((_, reject) => setTimeout(() => reject(new Error('接口请求超时')), 10000))
        ]);

        if (!modelRes || !modelRes.data) {
          throw new Error('模型ID接口返回空数据');
        }
        this.modelObj = modelRes.data;
        this.versionObj.label = modelRes.data.modelVersion;
        this.versionObj.parentId = modelRes.data.modelId;

        // 3. 获取列配置
        await this.getCluListSync();

        // 4. 获取查询列表
        this.getQueryList();

        // 5. 强制触发表格数据加载
        this.getTableData();

        console.log('数据加载完成');
      } catch (error) {
        console.error('加载节点数据失败：', error);
        // 仅表模型节点提示错误，非表模型不提示
        if (this.innerTableName) {
          this.$message.error(`加载【${this.innerTableName}】数据失败：${error.message || error.msg || '服务器错误'}`);
        }
        this.showRightContent = true;
        this.tableData = [];
        this.total = 0;
      } finally {
        this.loading = false;
      }
    },

    getCluListSync() {
      return new Promise((resolve, reject) => {
        this.cluList = [];
        this.searchList = [];
        this.notNullList = [];
        this.nullList = [];
        initgriddatasite(this.modelObj).then(res => {
          this.cluList = res.data.columns;
          this.tableColConfig = JSON.parse(JSON.stringify(res.data.columns));
          this.searchList = res.data.searchAttributes;
          this.$nextTick(() => {
            // 重新计算表格高度
            this.calcTableHeight();
          });
          // 处理下拉框数据（并行加载，不阻塞主流程）
          this.loadSearchListOptions();
          resolve(res);
        }).catch(err => {
          reject(err);
        });
      });
    },

    // 新增：单独加载搜索列表下拉框数据
    loadSearchListOptions() {
      for (let i = 0; i < this.searchList.length; i++) {
        let data = {
          modelId: this.modelObj.modelId,
          modeVersion: this.modelObj.modelVersion,
          attribute: this.searchList[i].attributeNameUnderline,
          name: '',
          pageNum: 0,
          pageSize: 20,
        };
        let attribute = this.searchList[i].attributeNameUnderline;
        if (this.$route.query.search) {
          const routeSearch = JSON.parse(this.$route.query.search);
          if (routeSearch[attribute]) {
            data.code = routeSearch[attribute];
          }
        }
        tableDropDownBox(data).then(res1 => {
          if (res1.total > 0) {
            this.$set(this.searchList[i], 'attType', 'select');
            this.$set(this.searchList[i], 'list', res1.rows);
            this.$set(this.searchList[i], 'total', parseInt(res1.total));
          }
        }).catch(e => {
          console.error(`加载下拉框数据失败（${attribute}）：`, e);
        });
      }
    },

    // 修复 getCommonData 方法（保持兼容）
    getCommonData() {
      this.loadTreeNodeData(); // 复用新增的异步加载方法
    },
    // 加载选中节点的数据（备用按钮逻辑）
    loadSelectedTreeNodeData() {
      if (!this.selectedTreeNode) {
        this.$message.warning('请先选择左侧的模型节点');
        return;
      }

      // 重复点击时提示，避免重复加载
      if (this.innerTableName === this.selectedTreeNode.tableName) {
        this.$message.info(`【${this.selectedTreeNode.label}】数据已加载`);
        return;
      }

      // 执行加载逻辑（和节点点击一致）
      this.showRightContent = true;
      this.searchForm = {};
      this.queryList = [];
      this.tableData = [];
      this.total = 0;
      this.innerTableName = this.selectedTreeNode.tableName;
      this.refresh();
      this.$message.success(`已加载【${this.selectedTreeNode.label}】模型数据`);
    },

    autoSelectTreeNodeByTableName(tableName) {
      if (!this.treeData || this.treeData.length === 0) return;

      const findNode = (nodes, parentNodes = []) => {
        for (const node of nodes) {
          // 匹配规则：支持tableName或纯路由path匹配
          const pathSegments = node.path.split('/').filter(seg => seg);
          const pathTableName = pathSegments.pop() || '';

          // 新增：如果是纯路由节点，直接匹配path
          if (node.tableName === tableName || pathTableName === tableName || node.path === tableName) {
            return {
              node,
              parentNodes: [...parentNodes, node]
            };
          }

          if (node.children && node.children.length > 0) {
            const found = findNode(node.children, [...parentNodes, node]);
            if (found) return found;
          }
        }
        return null;
      };

      const target = findNode(this.treeData);
      if (target) {
        this.selectedTreeNode = target.node;
        this.$nextTick(() => {
          try {
            if (this.$refs.treeSidebar) {
              this.$refs.treeSidebar.setCurrentKey(target.node.path);
              // 展开父节点
              target.parentNodes.forEach(parentNode => {
                if (parentNode.path) {
                  this.$refs.treeSidebar.expandNode(parentNode);
                }
              });
              if (target.node.children && target.node.children.length > 0) {
                this.$refs.treeSidebar.expandNode(target.node);
              }
            }
          } catch (e) {
            console.warn('自动选中/展开节点失败：', e);
          }
        });

        // 仅表模型节点加载数据，非表模型不处理
        const nodeTableName = target.node.tableName || (() => {
          const pathSegments = target.node.path.split('/').filter(seg => seg);
          return pathSegments.pop() || '';
        })();
        if (nodeTableName && target.node.path.includes('/zdzy/')) {
          this.innerTableName = nodeTableName;
          this.loadTreeNodeData();
        }
      }
    },

    // 树节点过滤方法
    filterTreeNode(value, data) {
      if (!value) return true;
      return data.label.toLowerCase().includes(value.toLowerCase());
    },

    buildTree(list) {
      this.treeData = buildTreeData(list, 'ID', 'PARENT_ID', 'NAME');
    },

    transitionBoxShow() {
      if (this.multipleSelection.length === 0) {
        this.$confirm('未选择数据是否根据筛选条件转换数据', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.transitionBox.loading = true
          this.transitionBox.show = true
          let params = {
            tableId: this.modelObj.modelId,
            pageNum: 1,
            pageSize: 10000,
          }
          getTransitionTableDataApi(params).then(res => {
            this.transitionBox.loading = false
            this.transitionBox.tableData = res.rows
          })
        })
      } else {
        this.transitionBox.loading = true
        this.transitionBox.show = true
        let params = {
          tableId: this.modelObj.modelId,
          pageNum: 1,
          pageSize: 10000,
        }
        getTransitionTableDataApi(params).then(res => {
          this.transitionBox.loading = false
          this.transitionBox.tableData = res.rows
        })
      }
    },

    selTransition(row) {
      this.$confirm('是否执行该转换规则?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let params = {
          tableId: this.modelObj.modelId,
          ruleId: row.ID,
          dataIds: this.multipleSelection.map(item => item.ID).join(',')
        }
        commitTransitionApi(params).then(res => {
          this.$message.success(res.msg || '转换成功')
          this.transitionBox.show = false
          this.getTableData()
        }).catch(err => {
          this.$message.error(err.msg || '转换失败')
          this.transitionBox.loading = false
        })
      }).catch(() => {
        this.transitionBox.loading = false
      })
    },

    openImportDialog() {
      this.importDialog.fileList = []
      this.importDialog.show = true
    },
    handleFileChange(file, fileList) {
      const validFiles = fileList.filter(item => item.raw && (item.raw.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' || item.raw.type === 'application/vnd.ms-excel'))
      this.importDialog.fileList = validFiles.slice(-1)
    },
    handleFileExceed() {
      this.$message.warning('仅支持单次上传1个Excel文件，请先删除已选择文件')
    },
    beforeUploadFile(file) {
      const isExcel = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' || file.type === 'application/vnd.ms-excel'
      const isLt10M = file.size / 1024 / 1024 < 10
      if (!isExcel) {
        this.$message.error('仅支持上传.xlsx/.xls格式的Excel文件')
        return false
      }
      if (!isLt10M) {
        this.$message.error('文件大小不能超过10MB，请压缩后再上传')
        return false
      }
      return true
    },
    async handleSubmitImport() {
      try {
        this.importDialog.uploadLoading = true
        const file = this.importDialog.fileList[0].raw
        const formData = new FormData()
        formData.append('file', file)
        formData.append('modelId', this.modelObj.modelId)
        formData.append('tableName', this.innerTableName)
        const res = await importDataByExcel(formData)
        this.$message.success(res.msg || '数据导入成功')
        this.importDialog.show = false
        this.getTableData()
      } catch (error) {
        this.$message.error(error.msg || '数据导入失败，请检查文件格式或联系管理员')
        console.error('导入错误：', error)
      } finally {
        this.importDialog.uploadLoading = false
      }
    },
    getRowKeys(row) {
      return row.ID
    },
    objectSpanMethod({ row, column, rowIndex, columnIndex }) {
      let data = this.tableColConfig.find(item => {
        return item.attributeNameUnderline == column.property
      })
      if (data && data.isMergeRows == 1) {
        let colName = column.property
        if (rowIndex > 0 && this.tableData[rowIndex - 1][colName] == this.tableData[rowIndex][colName]) {
          return { rowspan: 0, colspan: 0 }
        }
        let rowSpan = 1
        for (let i = rowIndex; i < this.tableData.length; i++) {
          if (this.tableData[i + 1] && this.tableData[i + 1][colName] == this.tableData[i][colName]) {
            rowSpan++
          } else {
            return { rowspan: rowSpan, colspan: 1 }
          }
        }
      } else {
        return { rowspan: 1, colspan: 1 }
      }
    },
    getRouterLink(item, data) {
      this.$router.push({
        path: '/' + item.menuPath,
        query: {
          search: JSON.stringify({ [item.linkReColumnName]: data, }),
          refresh: true,
        }
      })
    },
    getStyle(arr, val) {
      let style = {}
      let obj = arr.find(item => { return item.value == val })
      if (obj) {
        let str = obj.style
        str = str.replace(/\r?\n|\r/g, "")
        str = str.replace(/\s/g, "")
        let styleArr = str.split(';')
        for (let i = 0; i < styleArr.length; i++) {
          let s = styleArr[i].split(':')
          style[s[0]] = s[1]
        }
      }
      return style
    },
    refresh() {
      this.searchForm = {}
      if (this.$route.query.search) {
        for (let key in JSON.parse(this.$route.query.search)) {
          this.searchForm[key] = JSON.parse(this.$route.query.search)[key]
        }
      }
      this.loadTreeNodeData();
    },
    loadmore() {
      if (this.focusSelect.total > this.focusSelect.list.length) {
        let pageNum = parseInt(this.focusSelect.list.length / 20) * 20
        tableDropDownBox({
          modelId: this.modelObj.modelId,
          modeVersion: this.modelObj.modelVersion,
          attribute: this.focusSelect.attributeNameUnderline,
          name: this.searchStr,
          pageNum: pageNum,
          pageSize: 20,
        }).then((res) => {
          this.selLoading = true
          if (res.rows.length > 0) {
            this.focusSelect.list.push.apply(
              this.focusSelect.list,
              res.rows
            )
          }
          this.$nextTick(() => {
            this.selLoading = false
          })
        }).catch(e => {
        })
      } else {
        if (!this.focusSelect.list[this.focusSelect.list.length - 1].disabled) {
          this.selLoading = true
          this.focusSelect.list.push({
            code: 'aaabbbccc',
            name: '没有更多了',
            disabled: true
          })
          this.$nextTick(() => {
            this.selLoading = false
          })
        }
      }
    },
    remoteMethod(val) {
      this.searchStr = val
      tableDropDownBox({
        modelId: this.modelObj.modelId,
        modeVersion: this.modelObj.modelVersion,
        attribute: this.focusSelect.attributeNameUnderline,
        name: this.searchStr,
        pageNum: 0,
        pageSize: 20,
      }).then((res) => {
        this.loading = true
        this.focusSelect.list = res.rows
        this.focusSelect.total = parseInt(res.total)
        this.$nextTick(() => {
          this.loading = false
        })
      }).catch(e => {
      })
    },
    openWindow: function () {
      var arr = []
      for (let i = 0; i < this.queryList.length; i++) {
        var listValue = {}
        listValue.radioValue = this.queryList[i].radioValue
        listValue.checkValue = this.queryList[i].checkValue
        listValue.ifOpen = this.queryList[i].ifOpen
        listValue.ifMoreChoose = this.queryList[i].ifMoreChoose
        arr.push(listValue)
      }
      this.$confirm('是否进行全屏展示?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const routeOne = this.$router.resolve({
          path: '/zdzy/zdzywh',
          query: {
            queryData: JSON.stringify(arr),
            searchForm: JSON.stringify(this.searchForm),
            tableName: JSON.stringify(this.innerTableName)
          }
        })
        window.open(routeOne.href, '_blank')
      }).catch(() => {
        this.$message({ type: 'info', message: '已取消全屏' })
      })
    },
    updateScrollTop() {
      let x = document.getElementsByClassName("card")[0]
      x.scrollTop = x.scrollHeight
    },
    filterNode(value, data) {
      if (!value) return true
      return (data.columnNameCn).indexOf(value) !== -1
    },
    resetQuery() {
      this.addQueryForm.name = ''
      this.addQueryForm.relationship = ''
      this.addQueryForm.operator = ''
      this.addQueryForm.value = ''
    },
    handleNodeClick(name, nameCn) {
      this.addQueryForm.name = nameCn
      this.addQueryForm.cluName = name
    },
    getTree() {
      let params = {
        modelId: this.modelObj.modelId,
        isCustomQuery: '1'
      }
      tree(params).then(res => {
        res = res.data
        this.deptOptions = res
        this.ifHight()
      });
    },
    delLocal(index) {
      this.$delete(this.list, index);
      localStorage.setItem("cmts", JSON.stringify(this.list));
    },
    clearLocal() {
      localStorage.removeItem('cmts')
      var list = JSON.parse(localStorage.getItem("cmts") || "[]");
      this.list = list;
    },
    addQuery(forName) {
      this.$refs[forName].validate((valid) => {
        if (valid) {
          var comment = {
            name: this.addQueryForm.cluName, nameCn: this.addQueryForm.name, relationship: this.addQueryForm.relationship,
            operator: this.addQueryForm.operator, value: this.addQueryForm.value
          }
          var list = JSON.parse(localStorage.getItem("cmts") || "[]");
          list.unshift(comment);
          localStorage.setItem("cmts", JSON.stringify(list));
          this.list = list;
          this.resetQuery()
          this.$nextTick(() => {
            this.$refs[forName].clearValidate();
          });
        } else {
          return false;
        }
      });
    },
    ifHight() {
      for (let i = 0; i < this.list.length; i++) {
        for (let j = 0; j < this.deptOptions.length; j++) {
          if (this.list[i].name === this.deptOptions[j].columnName) {
            this.$set(this.deptOptions[j], 'isHigh', '1')
          }
        }
      }
    },
    customQuery() {
      this.openCustom = true
      this.getTree()
      this.resetQuery()
      for (let j = 0; j < this.deptOptions.length; j++) {
        this.$set(this.deptOptions[j], 'isHigh', '0')
      }
      this.ifHight()
      this.$nextTick(() => {
        this.$refs['addQueryForm'].clearValidate();
      });
    },
    openTool() {
      getModelId({ tableName: this.innerTableName }).then(res => {
        this.modelObj = res.data
        this.versionObj.label = res.data.modelVersion
        this.versionObj.parentId = res.data.modelId
      })
      this.openShow = true
    },

    getQueryList() {
      mapMessage(this.modelObj).then(res => {
        for (let i = 0; i < res.data.length; i++) {
          res.data[i].radioValue = ''
          res.data[i].checkValue = []
          res.data[i].initial = ''
          if (res.data[i].list.length > 0) {
            res.data[i].listRadio = [{ code: '', name: '全部' }].concat(res.data[i].list)
          }
        }
        this.queryList = res.data
      })
    },
    getAbcData(index) {
      this.index = index
      this.queryList[index].radioValue = ''
      this.queryList[index].checkValue = []
      this.queryList[index].list = []
      this.queryList[index].listRadio = []
      conditionalData(this.queryList[index]).then(res => {
        this.queryList[index].list = res.data
        if (this.queryList[index].list.length > 0) {
          this.queryList[index].listRadio = [{ code: '', name: '全部' }].concat(this.queryList[index].list)
        }
      })
    },
    getCluList() {
      this.cluList = []
      this.searchList = []
      this.notNullList = []
      this.nullList = []
      initgriddatasite(this.modelObj).then(res => {
        this.cluList = res.data.columns
        this.tableColConfig = JSON.parse(JSON.stringify(res.data.columns))
        this.searchList = res.data.searchAttributes
        this.$nextTick(() => {
          if (this.$refs.tableBox && this.$refs.basicsQuery) {
            this.$refs.tableBox.style.height = 'calc(100% - ' + this.$refs.basicsQuery.offsetHeight + 'px)'
          }
        })
        for (let i = 0; i < this.searchList.length; i++) {
          let data = {
            modelId: this.modelObj.modelId,
            modeVersion: this.modelObj.modelVersion,
            attribute: this.searchList[i].attributeNameUnderline,
            name: '',
            pageNum: 0,
            pageSize: 20,
          };
          let attribute = this.searchList[i].attributeNameUnderline;
          if (this.$route.query.search) {
            for (let key in JSON.parse(this.$route.query.search)) {
              if (attribute == key) {
                data.code = JSON.parse(this.$route.query.search)[key]
              }
            }
          }
          tableDropDownBox(data).then(res1 => {
            if (res1.total > 0) {
              this.searchList[i].attType = 'select'
              this.searchList[i].list = res1.rows
              this.searchList[i].total = parseInt(res1.total)
            }
          })
        }
        this.getTableData()
      })
    },
    getColSize(val, titVal) {
      return getColSize(val, titVal)
    },
    resetSearch() {
      this.globalSearchKeyword = '';
      this.searchForm = {}
      for (let i = 0; i < this.queryList.length; i++) {
        this.queryList[i].radioValue = ''
        this.queryList[i].checkValue = []
      }
      this.clearLocal()
      this.column = {}
      this.getTableData()
      this.$router.push({ query: {} })
      this.getCluList()
    },
    snFormatter: function (scope) {
      return this.pageSize * (this.pageIndex - 1) + scope.$index + 1;
    },
    getTableData() {
      this.loading = true;
      let params = {
        modelId: this.modelObj.modelId,
        isPage: '1',
        pageIndex: this.pageIndex,
        pageSize: this.pageSize,
        whereAttributes: [],
        orderAttributes: [],
        globalSearch: this.globalSearchKeyword || ''
      }
      if (this.isSearch) {
        this.$emit('getTable', params)
      } else {
        getData(params).then(response => {
          this.tableData = response.rows;
          this.total = parseInt(response.total);
          this.loading = false;
          this.$nextTick(() => {
            // 重新计算表格高度
            this.calcTableHeight();
            if (this.$refs.multipleTable) {
              // 重新布局表格
              this.$refs.multipleTable.doLayout();
              for (let i = 0; i < this.multipleSelection.length; i++) {
                let id = this.multipleSelection[i].ID
                let row = this.tableData.find(item => { return item.ID == id })
                if (row) {
                  this.$refs.multipleTable.toggleRowSelection(row, true)
                }
              }
            }
          })
        }).catch(() => {
          this.loading = false
        });
      }
    },
    judgeSelectTick(selection, row) {
      let selected = selection.length && selection.indexOf(row) !== -1
      if (!selected) {
        const res = this.multipleSelection.findIndex((item) => item.ID === row.ID)
        this.multipleSelection.splice(res, 1)
      } else {
        this.multipleSelection.push(row)
      }
      if (this.$refs.multipleTable) {
        this.$refs.multipleTable.setCurrentRow()
      }
    },
    handleSelectAll(selection) {
      if (selection.length) {
        this.multipleSelection.splice(0, this.multipleSelection.length)
        for (let i = 0; i < selection.length; i++) {
          this.multipleSelection.push(selection[i])
        }
      } else {
        this.multipleSelection.splice(0, this.multipleSelection.length)
      }
      if (this.$refs.multipleTable) {
        this.$refs.multipleTable.setCurrentRow()
      }
    },
    rowStyle({ row }) {
      let arr = this.multipleSelection;
      let style = {
        backgroundColor: '#aae9ec'
      }
      for (let i = 0; i < arr.length; i++) {
        if (row.ID && row.ID === arr[i].ID) {
          return style
        } else if (row.CODE && row.CODE === arr[i].CODE) {
          return style
        }
      }
    },
    deleteSta() {
      if (this.multipleSelection.length === 0) {
        this.$message.error('请选中数据进行删除');
      } else {
        this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let params = {
            modelId: this.modelObj.modelId,
            isPage: '0',
            valueAttributes: []
          }
          let selArr = JSON.parse(JSON.stringify(this.multipleSelection))
          this.multipleSelection = []
          if (this.$refs.multipleTable) {
            this.$refs.multipleTable.clearSelection();
          }
          for (let i = 0; i < selArr.length; i++) {
            let obj = {
              attributeName: 'ID',
              attributeValue: selArr[i].ID,
              isPrimaryKey: '1'
            }
            params.valueAttributes = [obj]
            deleteStation(params).then(response => {
              if (i === selArr.length - 1) {
                this.getTableData()
                this.deleteErrorData(selArr)
                this.$message({
                  message: response.msg,
                  type: 'success',
                  center: true
                })
              }
            }).catch(() => {
              this.getTableData()
            })
          }
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
      }
    },
    deleteErrorData(selArr) {
      for (let i = 0; i < selArr.length; i++) {
        deleteError(selArr[i].ID).then(response => {
        })
      }
    },
    delData(scope) {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let params = {
          modelId: this.modelObj.modelId,
          isPage: '0',
          valueAttributes: [{
            attributeName: 'ID',
            attributeValue: scope.row.ID,
            isPrimaryKey: '1'
          }]
        }
        deleteStation(params).then(response => {
          this.getTableData()
          deleteError(scope.row.ID).then(res => {
          })
          this.$message({
            message: response.msg,
            type: 'success',
            center: true
          })
        }).catch(() => {
          this.getTableData()
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    getAllRules() {
      selectAllRules().then(res => {
        res = res.data
        for (let i = 0; i < res.length; i++) {
          for (let j = 0; j < this.cluList.length; j++) {
            if (res[i].errorColumnNameCn === this.cluList[j].attributeNameCn) {
              if (this.cluList[j].rules === undefined) {
                this.cluList[j].rules = res[i].ruleDescription + '<br/>'
              } else {
                this.cluList[j].rules += res[i].ruleDescription + '<br/>'
              }
            }
          }
        }
        for (let i = 0; i < this.cluList.length; i++) {
          if (this.cluList[i].isNotNull === 1 || this.cluList[i].isNotNull === '1') {
            this.notNullList.push(this.cluList[i])
          } else {
            this.nullList.push(this.cluList[i])
          }
        }
      })
    },
    openAdd() {
      this.editForm = {}
      this.$nextTick(() => {
        if (this.$refs.child) {
          this.$refs.child.modelObj = this.modelObj
          this.$refs.child.dataObj = this.editForm
          this.$refs.child.beforeLoading()
        }
      })
      this.newStationDrawer = true
    },
    checkAdd(column, value) {
      let param = {
        colunmName: column,
        modelId: this.modelObj.modelId
      }
      getColunmId(param).then(response => {
        this.columnId = response.msg
        this.getRules(column, this.columnId, value)
      })
    },
    getRules(column, columnId, value) {
      let arr = []
      for (let i = 0; i < this.addErrForm.length; i++) {
        if (this.addErrForm[i].errorColumnName !== column) {
          arr.push(this.addErrForm[i])
        }
      }
      this.addErrForm = arr
      this.rulesForm[column] = ''
      let param = {
        commId: columnId,
        modelId: this.modelObj.modelId,
        param: value
      }
      getVerrules(param).then(response => {
        this.showAdd = false
        if (response.data !== []) {
          let str = ''
          for (var i = 0; i < response.data.length; i++) {
            this.addErrForm.push(response.data[i])
            str += response.data[i].ruleDescription + '<br/>'
          }
          this.rulesForm[column] = str
          this.$set(this.rulesForm, column, this.rulesForm[column])
        } else {
          this.rulesForm[column] = undefined
        }
        this.showAdd = true
      }).catch(e => {
      })
    },
    submitForm() {
      if (this.$refs.child) {
        this.$refs.child.beforeSubmit()
        this.addForm = {}
        this.addForm = this.$refs.child.pageForm
        setTimeout(() => {
          this.$confirm('是否保存此数据?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            let params = {
              modelId: this.modelObj.modelId,
              isPage: '0',
              valueAttributes: [{ attributeName: this.$refs.child.keyClu, attributeValue: '1', isPrimaryKey: '1' }]
            }
            for (let key in this.addForm) {
              let obj = {}
              if (this.addForm[key] !== '' && this.addForm[key] !== undefined) {
                obj = {
                  attributeName: key,
                  attributeValue: this.addForm[key]
                }
                params.valueAttributes.push(obj)
              }
            }
            submit(params).then(response => {
              this.newStationDrawer = false
              this.saveErrorStation(response.msg)
              this.$modal.msgSuccess("操作成功");
              this.getTableData()
            }).catch((e) => {
            })
          })
        }, 1500);
      }
    },
    saveErrorStation(id) {
      if (this.$refs.child) {
        this.addErrForm = this.$refs.child.addErrForm
        if (this.addErrForm.length > 0) {
          for (var i = 0; i < this.addErrForm.length; i++) {
            this.addErrForm[i].errorId = id
          }
          errorStation(this.addErrForm).then(response => {
          })
        }
      }
    },
    searchStation({ row }) {
      this.editForm = row
      this.stationDrawer = true
      this.$nextTick(() => {
        if (this.$refs.seeChild) {
          this.$refs.seeChild.modelObj = this.modelObj
          this.$refs.seeChild.dataObj = this.editForm
          if (this.ifFirstSee) {
            this.$refs.seeChild.beforeLoading()
            this.ifFirstSee = false
          } else {
            this.$refs.seeChild.reOpen()
          }
        }
      })
    },
    handleExport() {
      this.$confirm('是否导出数据?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let params = this.getSearchCriteria();
        exportExcel(params).then(res => {
          let lastInde = res.msg.lastIndexOf("/") + 1;
          let fileName = res.msg.substring(lastInde);
          fileDownload({ filename: res.msg }).then(res => {
            export_excel_file(res, fileName);
          })
        })
      })
    },
    handleDownloadTemplate() {
      this.$confirm('是否下载模板?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let params = this.getSearchCriteria();
        downloadImportTemplate(params).then(res => {
          let lastInde = res.msg.lastIndexOf("/") + 1;
          let fileName = res.msg.substring(lastInde);
          fileDownload({ filename: res.msg }).then(res => {
            export_excel_file(res, fileName);
          })
        })
      })
    },
    getSearchCriteria() {
      let params = {
        modelId: this.modelObj.modelId,
        isPage: '0',
        whereAttributes: []
      }
      for (let key in this.searchForm) {
        let obj = {}
        if (key !== 'pageSize' && key !== 'pageNum') {
          if (this.searchForm[key] !== '' && this.searchForm[key] !== undefined) {
            obj = {
              attributeName: key,
              attributeValue: this.searchForm[key]
            }
            let data = this.searchList.find(item => {
              return item.attributeNameUnderline == key
            })
            if (data && data.relateSearchAttributeId) {
              obj.relateSearchAttribute = data.relateSearchAttribute
              obj.relateSearchCondition = data.relateSearchCondition
            }
            params.whereAttributes.push(obj)
          }
        }
      }
      for (let i = 0; i < this.queryList.length; i++) {
        let obj = {}
        if (this.queryList[i].ifMoreChoose) {
          if (this.queryList[i].checkValue !== undefined && this.queryList[i].checkValue.length > 0) {
            obj = {
              attributeName: this.queryList[i].name,
              attributeValue: this.queryList[i].checkValue.toString()
            }
            params.whereAttributes.push(obj)
          }
        } else {
          obj = {
            attributeName: this.queryList[i].name,
            attributeValue: this.queryList[i].radioValue
          }
          if (this.queryList[i].radioValue !== '') {
            params.whereAttributes.push(obj)
          }
        }
      }
      return params;
    },
    otherHandleExport() {
      let params = this.getSearchCriteria();
      this.$emit('getSearchCriteria', params)
    },
    openEdit({ row }) {
      this.editForm = row
      this.editStationDrawer = true
      this.$nextTick(() => {
        if (this.$refs.child) {
          this.$refs.child.modelObj = this.modelObj
          this.$refs.child.dataObj = this.editForm
          if (this.ifFirstEdit) {
            this.$refs.child.beforeLoading()
            this.ifFirstEdit = false
          } else {
            this.$refs.child.reOpen()
          }
        }
      })
    },
    submitEditForm() {
      if (this.$refs.child) {
        this.$refs.child.beforeSubmit()
        this.editForm = {}
        // 修复：原代码缺少 .pageForm，导致赋值不完整
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
                if (key === this.$refs.child.keyClu) { obj.isPrimaryKey = 1 }
                params.valueAttributes.push(obj)
              }
            }
            submitEdit(params).then(response => {
              this.editStationDrawer = false
              this.saveErrorStation(this.editForm[this.$refs.child.keyClu])
              this.$modal.msgSuccess("操作成功");
              this.getTableData()
            }).catch((e) => {
              this.$message.error("编辑失败：" + (e.msg || "服务器错误"));
            })
          }).catch(() => {
            this.$message.info("已取消编辑");
          })
        }, 1500);
      }
    },
    sortChange(column, prop, order) {
      this.column.prop = column.prop
      if (column.order === 'descending') {
        this.column.order = 'desc'
      } else if (column.order === 'ascending') {
        this.column.order = 'asc'
      } else {
        this.column = {}
      }
      this.getTableData()
    },
    sortByDate(obj1, obj2) {
      let o1 = 0;
      let o2 = 0;
      try {
        o1 = parseInt(obj1.NAME);
        o2 = parseInt(obj2.NAME);
      } catch (e) {
        return 0;
      }
      return o1 - o2
    },
    rowContextmenu(row, column, event) {
      this.$refs.multipleTable.setCurrentRow(row)
      if (this.rightClickMenus[0]) {
        event.preventDefault();
        let menu = document.querySelector("#menu");
        menu.style.display = "block";
        menu.style.zIndex = 1000;
        let width = menu.getElementsByClassName('menuUl')[0].offsetWidth + 20
        let left = window.screen.width - event.x < width ? window.screen.width - width : event.x
        let height = this.rightClickMenus.length * 35
        let top = window.screen.height - event.y - 150 < height ? window.screen.height - height - 150 : event.y
        top = top - 120
        left = left - 30
        menu.style.left = left + 'px'
        menu.style.top = top + 'px'
        this.rightClikcRow = row
      }
    },
    mouseout() {
      let menu = document.querySelector("#menu");
      menu.style.display = "none";
    },
    mouseover() {
      let menu = document.querySelector("#menu");
      menu.style.display = "block";
    },
    infoClick(item) {
      let menu = document.querySelector("#menu");
      menu.style.display = "none";
      this.$emit(item.functionName, {
        rowData: this.rightClikcRow,
        menu: item,
      })
    }
  }
}
</script>

<style scoped>
.commonWh {
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.main-layout {
  display: flex;
  width: 100%;
  height: 100%;
}

/* 左侧树容器：缩小宽度 + 绿色渐变背景 */
.left-tree-container {
  width: 220px;
  /* 从260px缩小到220px */
  height: 100%;
  border-right: 1px solid #e6e6e6;
  padding: 10px;
  box-sizing: border-box;
  /* 绿色渐变背景（和顶部导航一致） */
  background:
    linear-gradient(180deg, rgba(223, 255, 246, 0.9) 0%, rgba(255, 255, 255, 0.9) 100%),
    url('~@/assets/txqj/Mask group.png') no-repeat center center / cover;
}

.tree-header {
  font-size: 14px;
  font-weight: bold;
  margin-bottom: 10px;
  color: #006A65;
  /* 改为绿色系文字 */
}

.tree-search-input {
  margin-bottom: 10px;
  width: 100%;
}

.right-content-container {
  flex: 1;
  height: 100%;
  /* 关键修改：把 overflow: auto 改为 overflow: hidden */
  overflow: hidden;
  padding: 10px;
  box-sizing: border-box;
  background: linear-gradient(180deg, rgba(223, 255, 246, 0.9) 0%, rgba(255, 255, 255, 0.9) 100%),
    url('~@/assets/txqj/Mask group.png') no-repeat center center / cover;
}

.box-card {
  width: 100%;
  height: 100%;
  /* 关键：设置box-card高度为100% */
  display: flex;
  flex-direction: column;
  /* 改为flex布局，保障子元素高度计算 */
  box-sizing: border-box;
}

/* 右侧占位容器：绿色渐变背景 */
.right-content-placeholder {
  flex: 1;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background:
    linear-gradient(180deg, rgba(223, 255, 246, 0.9) 0%, rgba(255, 255, 255, 0.9) 100%),
    url('~@/assets/txqj/Mask group.png') no-repeat center center / cover;
}

.queryBox {
  flex-shrink: 0;
  /* 防止查询框被压缩 */
  margin-bottom: 10px;
}


.advancedQuery {
  margin-bottom: 10px;
  padding: 10px;
  border: 1px solid #d6e9e6;
  border-radius: 4px;
  background: linear-gradient(135deg, #e6f7f5 0%, #f0fcf9 100%);
}

.moreButton {
  margin-bottom: 5px;
}

.queryTitle {
  display: inline-block;
  margin-right: 10px;
  font-weight: bold;
  color: #006A65;
  /* 改为绿色系文字 */
}

.openTit {
  cursor: pointer;
  color: #009688;
  /* 改为绿色系文字 */
  margin-bottom: 10px;
}

/* 基础查询区 - 绿色渐变背景 */
.basicsQuery {
  padding: 10px;
  border: 1px solid #d6e9e6;
  /* 改为绿色系边框 */
  border-radius: 4px;
  margin-bottom: 10px;
  /* 替换原有背景为绿色渐变 */
  background: linear-gradient(135deg, #e6f7f5 0%, #f0fcf9 100%);
}

.tableBox {
  flex: 1;
  width: 100%;
  /* 关键修改：提高最小高度，比如从 300px 改为 600px */
  min-height: 440px;
  border: 1px solid #d6e9e6;
  border-radius: 4px;
  padding: 10px;
  box-sizing: border-box;
  background: rgba(255, 255, 255, 0.7);
  overflow: hidden;
}

/* 按钮靠左排列（默认就是这个，但可以更规范） */
.tabTopButton {
  margin-bottom: 10px;
  padding: 8px 10px;
  border-radius: 4px;
  border: 1px solid #d6e9e6;
  background: linear-gradient(135deg, #e6f7f5 0%, #f0fcf9 100%);
  /* 关键：左对齐 */
  display: flex;
  justify-content: flex-start;
  /* 靠左 */
  gap: 8px;
  /* 按钮之间的间距 */
}

.menuDiv {
  position: absolute;
  display: none;
  background: #fff;
  border: 1px solid #e6e6e6;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.menuUl {
  list-style: none;
  margin: 0;
  padding: 0;
}

.menuUl li {
  padding: 5px 20px;
  cursor: pointer;
  white-space: nowrap;
}

.menuUl li:hover {
  background: #e6f7f5;
  /* 改为绿色系 hover 背景 */
}

.import-box {
  padding: 10px 0;
}

.import-item {
  margin-bottom: 20px;
}

.import-label {
  display: block;
  margin-bottom: 10px;
  font-weight: bold;
  color: #006A65;
  /* 改为绿色系文字 */
}

.tips-text {
  color: #666;
  font-size: 12px;
  margin-top: 5px;
}

.linkColumn {
  color: #009688;
  /* 改为绿色系链接色 */
  cursor: pointer;
}

.red {
  color: red;
}

/* 树节点样式优化 */
.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 13px;
}

/* 根节点图标样式 */
.tree-root-icon {
  font-size: 16px;
}

/* 树组件背景透明，继承容器背景 */
::v-deep .el-tree {
  background: transparent;
}

/* 树节点 hover/选中 样式改为绿色系 */
::v-deep .el-tree-node__content:hover {
  background-color: #e6f7f5 !important;
}

::v-deep .el-tree-node.is-current>.el-tree-node__content {
  background-color: #b9eae4 !important;
  color: #006A65 !important;
}
</style>