<template>
  <div class="commonWh">
    <div style="width: 100%; height: 0" class="getSpanSize"></div>
    <div class="box-card">
      <div class="queryBox">
        <!--条件查询-->
        <div v-show="showLabelQuery && queryList.length > 0" class="advancedQuery">
          <div v-for="(item, index) in queryList">
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
                <!--字母索引-->
                <el-radio-group style=" display: none;" @change="getAbcData(index)" v-model="item.initial">
                  <el-radio v-for="opa in abcList" :value="opa.value" :label="opa.value">{{ opa.label }}</el-radio>
                </el-radio-group>
                <!--字母索引-->
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
        <!--基础查询-->
        <div ref="basicsQuery" class="basicsQuery">
          <el-form :model="searchForm">
            <el-form-item v-for="(item, index) in searchList" :key="index"
              :label="item.relateSearchName ? item.relateSearchName : item.attributeNameCn" label-width="100px">
              <el-select size="mini" v-if="item.attType === 'select'" clearable filterable :loading="selLoading" remote
                @keydown.enter.native="getTableData()" v-loadmore="loadmore" @focus="focusSelect = item"
                :remote-method="(val) => remoteMethod(val)" v-model="searchForm[item.attributeNameUnderline]">
                <el-option v-for="arr in item.list" :label="arr.name" :key="arr.code" :disabled="arr.disabled"
                  :value="arr.code"
                  :title="arr.manageDept ? arr.name + ' （ ' + arr.manageDept + ' ）' : arr.name"></el-option>
              </el-select>
              <el-date-picker v-model="searchForm[item.attributeNameUnderline]" @keyup.enter.native="getTableData()"
                type="datetime" size="mini" v-else-if="item.attType === 'datetime'" value-format="yyyy-MM-dd HH:mm:ss"
                placeholder="选择日期时间">
              </el-date-picker>
              <el-input size="mini" @keyup.enter.native="getTableData()" v-else
                v-model="searchForm[item.attributeNameUnderline]"></el-input>
            </el-form-item>
            <el-form-item label-width="0" style="float: right; width: 50%; text-align: right">
              <el-button type="primary" size="mini" @click="getTableData()">查询</el-button>
              <el-button size="mini" @click="resetSearch()">重置</el-button>
              <el-button size="mini" @click="openTool" v-hasPermi="['mm:model:isShow']">配置</el-button>
              <!-- <el-button type="primary"  size="mini" @click="transitionBoxShow()" v-hasPermi="['metadata:rule:isShow']">批量转换属性</el-button> -->
              <el-button type="primary" size="mini" @click="openImportDialog" v-hasPermi="['metadata:rule:isShow']">
                导入
              </el-button>
              <!-- <template>
                <el-popover
                  placement="top-start"
                  title="查询条件列表"
                  width="250"
                  trigger="hover"
                >
                  <el-button type="primary" size="mini" @click="customQuery()" slot="reference" style="margin-left: 10px">自定义查询（{{list.length}})</el-button>
                  <div v-for=" (item,index) in list" style="border-radius: 4px;border-color:gray;overflow: hidden;">
                    <span>{{ index+1 }}.&nbsp;</span>
                    <span style="color:#006A65">{{ item.relationship }}</span>
                    <span>&nbsp;{{ item.nameCn }}{{item.operator}}{{item.value}}</span>
                    <el-button  type="text" @click="delLocal(index)" style="float:right;position:relative;bottom:8px"></el-button>
                  </div>
                </el-popover>
              </template> -->
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
        <el-table :row-key="getRowKeys" tooltip-effect="light" border highlight-current-row height="calc(100% - 82px)"
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
                  <i v-if="scope.row.ICON.includes('同缆')" title="同缆" class="el-icon-star-on"
                    style="color:#0663ef;cursor:pointer;"></i>
                  <i v-if="scope.row.ICON.includes('三路由')" title="三路由" class="el-icon-star-on"
                    style="color:#d53ce6;cursor:pointer;"></i>
                  <i v-if="scope.row.ICON.includes('路由图')" title="路由图" class="el-icon-star-on"
                    style="color:#47e63c;cursor:pointer;"></i>
                  <i v-if="scope.row.ICON.includes('不可靠路由')" title="不可靠路由" class="el-icon-star-on"
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
    <!-- 修改属性对话框 -->
    <el-dialog v-dialogDrag custom-class="curdBox" title="修改" :modal="false" :visible.sync="editStationDrawer"
      width="1800px" :close-on-click-modal="false">
      <editData ref="child" :modelObj="this.modelObj" :dataObj="this.editForm"></editData>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitEditForm()">确 定</el-button>
        <el-button @click="editStationDrawer = false">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 新增属性对话框 -->
    <el-dialog v-dialogDrag custom-class="curdBox" title="新增" :modal="false" :close-on-click-modal="false"
      :visible.sync="newStationDrawer" width="1800px">
      <editData ref="child" :modelObj="this.modelObj" :dataObj="this.editForm"></editData>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm()">确 定</el-button>
        <el-button @click="newStationDrawer = false">取 消</el-button>
      </div>
    </el-dialog>
    <!-- 查看属性对话框 -->
    <el-dialog v-dialogDrag custom-class="curdBox" title="查看" :modal="false" :visible.sync="stationDrawer" width="1800px"
      :close-on-click-modal="false">
      <seeData ref="seeChild" :modelObj="this.modelObj" :dataObj="this.editForm"></seeData>
      <div slot="footer" class="dialog-footer">
        <el-button @click="stationDrawer = false">关 闭</el-button>
      </div>
    </el-dialog>
    <el-dialog v-dialogDrag title="展示配置" :modal="false" :close-on-click-modal="false" width="80%" :visible.sync="openShow"
      top="5vh">
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
              <el-input v-model="filterText" placeholder="请输入属性名称" clearable size="small" prefix-icon="el-icon-search"
                style="margin-bottom: 20px" />
              <el-tree class="filter-tree" :data="deptOptions" highlight-current default-expand-all node-key="data.id"
                :filter-node-method="filterNode" ref="treeForm" style="font-size: 14px;height:26.5vh;overflow-y: auto;">
                <span style="width: 100%;height:100%;display: block" class="custom-tree-node" slot-scope="{ node, data }">
                  <span :class="data.isHigh === '1' ? 'red' : ''" style="width: 100%;display: block;line-height: 25px"
                    @click="handleNodeClick(data.columnName, data.columnNameCn)">
                    <!-- <i :class="data.icon" :style="data.color"></i> -->{{ data.columnNameCn }}
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
    <!-- 导入数据弹窗：模板下载+文件上传 -->
    <el-dialog v-dialogDrag title="数据导入" :modal="true" :visible.sync="importDialog.show" width="500px"
      :close-on-click-modal="false" destroy-on-close>
      <div class="import-box">
        <!-- 模板下载区域 -->
        <div class="import-item">
          <label class="import-label">步骤1：下载模板</label>
          <el-button type="primary" icon="el-icon-download" @click="handleDownloadTemplate"
            :loading="importDialog.downloadLoading">
            下载系统标准模板
          </el-button>
          <p class="tips-text">点击下载Excel模板，按模板格式填写数据后再上传</p>
        </div>

        <!-- 文件上传区域 -->
        <div class="import-item">
          <label class="import-label">步骤2：上传文件</label>
          <el-upload class="upload-demo" drag :action="''" action :auto-upload="false" :file-list="importDialog.fileList"
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

      <!-- 弹窗底部按钮 -->
      <div slot="footer" class="dialog-footer">
        <el-button @click="importDialog.show = false">取消</el-button>
        <el-button type="primary" @click="handleSubmitImport" :loading="importDialog.uploadLoading"
          :disabled="importDialog.fileList.length === 0">
          提交导入
        </el-button>
      </div>
    </el-dialog>

    <!--rightClickMenus: [{name: '选项名', icon: 'element-ui-icon', functionName: '调用父组件方法名'}]-->
    <div id="menu" class="menuDiv">
      <ul class="menuUl">
        <li v-for="(item, index) in rightClickMenus" :key="index" @click.stop="infoClick(item)" @mouseout="mouseout"
          @mouseover="mouseover">
          <i :class="item.icon"></i> {{ item.name }}
        </li>
      </ul>
    </div>
    <!--批量转换-->
    <el-dialog v-dialogDrag title="批量转换属性" top="5vh" :visible.sync="transitionBox.show" :modal="false"
      :close-on-click-modal="false" width="70%">
      <div style="width: 100%; height: 70vh">
        <el-table v-loading="transitionBox.loading" :data="transitionBox.tableData" height="100%" style="width: 100%">
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
              <el-button size="mini" type="text" @click="selTransition(scope.row)" icon="el-icon-thumb">转换</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
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
  obtainIDBasedOnFilteringCriteria, downloadImportTemplate,
  importDataByExcel
} from "@/api/zdwh/zdzywh";
import { mapMessage, conditionalData } from "@/api/zdwh/zdzywh";
import editData from "@/views/publicPage/edit/editData";
import seeData from "@/views/publicPage/edit/seeData";
import { getColSize } from "@/utils/getColSize"
export default {
  name: "Zdzywh",
  components: { zspz, editData, seeData },
  props: {
    tableName: String,
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
      importDialog: {
        show: false, // 弹窗显隐
        downloadLoading: false, // 模板下载加载状态
        uploadLoading: false, // 文件上传加载状态
        fileList: [] // 上传文件列表（仅存1个Excel文件）
      },
      selLoading: false,
      focusSelect: {},
      searchStr: '',
      // 自定义查询表单
      addQueryForm: {
        name: '',
        relationship: '',
        operator: '',
        value: ''
      },
      // 自定义查询规则
      addQueryRules: {
        name: [{
          required: true, message: '请选择属性名称', trigger: ['change', 'blur']
        }],
        relationship: [{
          required: true, message: '请选择逻辑关系', trigger: ['change', 'blur']
        }],
        operator: [{
          required: true, message: '请选择操作符', trigger: ['change', 'blur']
        }]
      },
      // 自定义查询
      openCustom: false,
      versionObj: {},
      openShow: false,
      // 遮罩层
      loading: false,
      //显示条件查询
      showLabelQuery: false,
      //查询条件展开,基础条件展开
      showLabelQueryOpen: false,
      //字母索引 条件查询列表
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
      //表对象信息
      modelObj: {},
      //动态查询条件与列,必填与非必填
      cluList: [],
      // 表格展示属性
      tableColConfig: [],
      searchList: [],
      nullList: [],
      notNullList: [],
      //搜索框
      searchForm: {},
      //表的分页
      pageSize: 15,
      pageIndex: 1,
      total: 0,
      tableData: [],
      filterText: '',
      multipleSelection: [],
      // 树选项
      deptOptions: [],
      //所有属性规则
      allRulesForm: {},
      //新增
      newStationDrawer: false,
      addForm: {},
      rulesForm: {},
      showAdd: true,
      addErrForm: [],
      //查看
      stationDrawer: false,
      //修改
      editForm: {},
      editStationDrawer: false,
      // localStorage存储
      list: [],
      index: '',
      //是否第一次点击编辑按钮
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
  watch: {
    // 根据名称筛选树
    filterText(val) {
      this.$refs.treeForm.filter(val);
    },
    //解决操作栏固定后表格错位问题
    'tableData': {
      handler() {
        this.$nextTick(() => {
          this.$refs.multipleTable.doLayout()
          // this.multipleSelection.forEach(row => {
          //   this.$refs.multipleTable.toggleRowSelection(row)
          // })
        })
      },
      deep: true
    },
    'list.length': {
      handler(newValue, oldValue) {

        if (newValue !== oldValue) {
          // 操作
          for (let j = 0; j < this.deptOptions.length; j++) {
            this.$set(this.deptOptions[j], 'isHigh', '0')
          }
          this.ifHight()
        }

      }
    },
    //关闭配置页面后刷新列表查询条件
    openShow() {
      if (!this.openShow) {
        this.versionObj = {}
        this.clearLocal()
        this.getCluList()
        this.getQueryList()
      }
    },
  },
  mounted() {
    this.refresh()
  },
  methods: {
    openImportDialog() {
      // 每次打开重置文件列表和状态
      this.importDialog.fileList = []
      this.importDialog.show = true
    },



    // 上传文件变化时触发（仅保留最新的1个Excel文件）
    handleFileChange(file, fileList) {
      // 过滤仅保留Excel文件，且仅存最后1个
      const validFiles = fileList.filter(item => item.raw && (item.raw.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' || item.raw.type === 'application/vnd.ms-excel'))
      this.importDialog.fileList = validFiles.slice(-1)
    },

    // 文件超出数量限制
    handleFileExceed() {
      this.$message.warning('仅支持单次上传1个Excel文件，请先删除已选择文件')
    },

    // 文件上传前的格式/大小校验
    beforeUploadFile(file) {
      const isExcel = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' || file.type === 'application/vnd.ms-excel'
      const isLt10M = file.size / 1024 / 1024 < 10 // 限制10MB内

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

    // 提交导入文件
    async handleSubmitImport() {
      try {
        this.importDialog.uploadLoading = true
        const file = this.importDialog.fileList[0].raw // 获取原生File对象
        // 构建formData表单（后端要求multipart/form-data提交）
        const formData = new FormData()
        formData.append('file', file) // 'file'为后端接收文件的参数名，需与后端一致
        formData.append('modelId', this.modelObj.modelId) // 传表ID等额外参数（根据业务调整）
        formData.append('tableName', this.tableName)

        // 调用导入接口
        const res = await importDataByExcel(formData)
        this.$message.success(res.msg || '数据导入成功')
        this.importDialog.show = false
        this.getTableData() // 导入成功后刷新表格
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
    /* 合并单元格 rowIndex:行，columnIndex：列*/
    objectSpanMethod({ row, column, rowIndex, columnIndex }) {
      let data = this.tableColConfig.find(item => {
        return item.attributeNameUnderline == column.property
      })
      if (data && data.isMergeRows == 1) {
        let colName = column.property
        if (rowIndex > 0 && this.tableData[rowIndex - 1][colName] == this.tableData[rowIndex][colName]) {
          return {
            rowspan: 0,
            colspan: 0
          }
        }
        let rowSpan = 1
        for (let i = rowIndex; i < this.tableData.length; i++) {
          if (this.tableData[i + 1] && this.tableData[i + 1][colName] == this.tableData[i][colName]) {
            rowSpan++
          } else {
            return {
              rowspan: rowSpan,
              colspan: 1
            }
          }
        }
      } else {
        return {
          rowspan: 1,
          colspan: 1
        }
      }
    },
    //跳转
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
      this.getCommonData()
      var list = JSON.parse(localStorage.getItem("cmts") || "[]");
      this.list = list;
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
        if (
          !this.focusSelect.list[this.focusSelect.list.length - 1].disabled
        ) {
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
    /*弹出页面*/
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
            tableName: JSON.stringify(this.tableName)
          }
        })
        window.open(routeOne.href, '_blank')
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消全屏'
        })
      })
    },
    updateScrollTop() {
      let x = document.getElementsByClassName("card")[0]
      x.scrollTop = x.scrollHeight  //将滚轮置底
    },
    // 过滤节点
    filterNode(value, data) {
      if (!value) return true
      return (data.columnNameCn).indexOf(value) !== -1
    },
    // 重置查询
    resetQuery() {
      this.addQueryForm.name = ''
      this.addQueryForm.relationship = ''
      this.addQueryForm.operator = ''
      this.addQueryForm.value = ''
    },
    // 节点单击事件
    handleNodeClick(name, nameCn) {
      this.addQueryForm.name = nameCn
      this.addQueryForm.cluName = name
    },
    // 查询数据源下拉树结构
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
    // 删除localStorage
    delLocal(index) {
      this.$delete(this.list, index);
      /*  list = this.list; //把获取到的数据赋值给list  */
      localStorage.setItem("cmts", JSON.stringify(this.list)); //再次调用JOSN.stringify转为数组字符串，然后调用localStorage.setItem()
    },
    // 清空localStorage
    clearLocal() {
      localStorage.removeItem('cmts')
      var list = JSON.parse(localStorage.getItem("cmts") || "[]"); //从localStorage中获取之前发布过的数据
      this.list = list; //把获取到的数据赋值给this.list
    },
    // 添加localStorage
    addQuery(forName) {
      this.$refs[forName].validate((valid) => {
        if (valid) {
          var comment = {
            name: this.addQueryForm.cluName, nameCn: this.addQueryForm.name, relationship: this.addQueryForm.relationship,
            operator: this.addQueryForm.operator, value: this.addQueryForm.value
          }
          var list = JSON.parse(localStorage.getItem("cmts") || "[]"); //从localStorage中获取之前发布过的数据
          list.unshift(comment); //重新保存最新的数据（之前的+添加的）
          localStorage.setItem("cmts", JSON.stringify(list)); //再次调用JOSN.stringify转为数组字符串，然后调用localStorage.setItem()
          this.list = list; //把获取到的数据赋值给this.list
          this.resetQuery()
          this.$nextTick(() => {
            this.$refs[forName].clearValidate();
          });
        } else {
          return false;
        }
      });

    },
    // 判定高亮
    ifHight() {
      for (let i = 0; i < this.list.length; i++) {
        for (let j = 0; j < this.deptOptions.length; j++) {
          if (this.list[i].name === this.deptOptions[j].columnName) {
            this.$set(this.deptOptions[j], 'isHigh', '1')
          }
        }
      }
    },
    //打开自定义查询页面
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
    //打开配置页面
    openTool() {
      getModelId({ tableName: this.tableName }).then(res => {
        this.modelObj = res.data
        this.versionObj.label = res.data.modelVersion
        this.versionObj.parentId = res.data.modelId
      })
      this.openShow = true
    },
    getCommonData() {
      //获取表基本信息
      getModelId({ tableName: this.tableName }).then(res => {
        this.modelObj = res.data
        this.versionObj.label = res.data.modelVersion
        this.versionObj.parentId = res.data.modelId
        this.getCluList()
        this.getQueryList()
      })
    },
    //获取条件查询列表
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
    //按首字母索引
    getAbcData(index) {
      this.index = index
      /* alert(this.index) */
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
    //动态获取页面搜索条件与列名
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
          this.$refs.tableBox.style.height = 'calc(100% - ' + this.$refs.basicsQuery.offsetHeight + 'px)'
        })
        //获取表格下拉框数据
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
    // 重置搜索
    resetSearch() {
      //重置基础搜索
      this.searchForm = {}
      //重置条件查询
      for (let i = 0; i < this.queryList.length; i++) {
        this.queryList[i].radioValue = ''
        this.queryList[i].checkValue = []
      }
      //重置自定义查询
      this.clearLocal()
      //  重置列表排序
      this.column = {}
      this.getTableData()

      //清空query
      this.$router.push({ query: {} })
      //重置动态获取页面搜索条件与列名
      this.getCluList()
    },
    // 格式序列化列
    snFormatter: function (row, col, value, index) {
      return this.pageSize * (this.pageIndex - 1) + row.$index + 1;
      //return this.pageSize * (this.pageIndex - 1) + index + 1;
    },
    //获取表格数据
    getTableData() {
      this.loading = true;
      let params = {
        modelId: this.modelObj.modelId,
        isPage: '1',
        pageIndex: this.pageIndex,
        pageSize: this.pageSize,
        whereAttributes: [],
        orderAttributes: [],
      }
      //排序
      if (Object.keys(this.column).length != 0) {
        let obj = {
          attributeName: this.column.prop,
          attributeValue: this.column.order
        }
        params.orderAttributes.push(obj)
      }
      //基础查询
      for (let key in this.searchForm) {
        let obj = {}
        if (this.searchForm[key] !== '' && this.searchForm[key] !== undefined) {
          // attributeNameUnderline
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
      //条件查询
      for (let i = 0; i < this.queryList.length; i++) {
        //多选
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
          //单选
          obj = {
            attributeName: this.queryList[i].name,
            attributeValue: this.queryList[i].radioValue
          }
          if (this.queryList[i].radioValue !== '') {
            params.whereAttributes.push(obj)
          }
        }
      }
      //自定义查询
      if (this.list.length > 0) {
        for (let j = 0; j < this.list.length; j++) {
          let obj = {}
          obj = {
            attributeName: this.list[j].name,
            attributeValue: this.list[j].value,
            logicalRelationSymbol: this.list[j].relationship,
            operator: this.list[j].operator
          }
          params.whereAttributes.push(obj)
        }
      }

      if (this.isSearch) {
        this.$emit('getTable', params)
      } else {
        getData(params).then(response => {
          this.tableData = response.rows;
          this.total = parseInt(response.total);
          this.loading = false;
          this.$nextTick(() => {
            for (let i = 0; i < this.multipleSelection.length; i++) {
              let id = this.multipleSelection[i].ID
              let row = this.tableData.find(item => { return item.ID == id })
              if (row) {
                this.$refs.multipleTable.toggleRowSelection(row, true)
              }
            }
          })
        }
        ).catch(() => {
          this.loading = false
        });
      }
    },
    //获取多选框数据
    judgeSelectTick(selection, row) {
      // true为选中, 0或false为取消选中
      let selected = selection.length && selection.indexOf(row) !== -1
      if (!selected) {
        const res = this.multipleSelection.findIndex((item) => item.ID === row.ID)
        this.multipleSelection.splice(res, 1)
      } else {
        this.multipleSelection.push(row)
      }
      //高亮取消
      this.$refs.multipleTable.setCurrentRow()
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
      //高亮取消
      this.$refs.multipleTable.setCurrentRow()
    },
    // 当前数据行背景样式
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

    // 删除
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
          this.$refs.multipleTable.clearSelection();
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
            }
            ).catch(() => {
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
    //删除数据对应的问题数据
    deleteErrorData(selArr) {
      for (let i = 0; i < selArr.length; i++) {
        deleteError(selArr[i].ID).then(response => {

        })
      }
    },
    //删除单条数据
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
        }
        ).catch(() => {
          this.getTableData()
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    //获取所有属性的规则
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
        //循环获取必填与非必填数组，是否必填1是，0否
        for (let i = 0; i < this.cluList.length; i++) {
          if (this.cluList[i].isNotNull === 1 || this.cluList[i].isNotNull === '1') {
            this.notNullList.push(this.cluList[i])
          } else {
            this.nullList.push(this.cluList[i])
          }
        }
      })
    },
    //打开新增
    openAdd() {
      this.editForm = {}
      this.$nextTick(() => {
        //this.editForm=row
        this.$refs.child.modelObj = this.modelObj
        this.$refs.child.dataObj = this.editForm
        this.$refs.child.beforeLoading()
      })
      this.newStationDrawer = true
    },
    //校验名字
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
      //删除addErrForm中原来的错误数据，
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
            //将错误数据保存到addErrForm中
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
    // 新增保存
    submitForm() {
      //调用子组件重复校验方法
      this.$refs.child.beforeSubmit()
      //获取子组件表单数据
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
      }, 1500);

    },
    // 保存错误传回
    saveErrorStation(id) {
      this.addErrForm = this.$refs.child.addErrForm
      if (this.addErrForm.length > 0) {
        for (var i = 0; i < this.addErrForm.length; i++) {
          this.addErrForm[i].errorId = id
        }
        errorStation(this.addErrForm).then(response => {
        })
      }
    },
    //查看
    searchStation({ row }) {
      this.editForm = row
      this.stationDrawer = true
      this.$nextTick(() => {
        this.$refs.seeChild.modelObj = this.modelObj
        this.$refs.seeChild.dataObj = this.editForm
        if (this.ifFirstSee) {
          this.$refs.seeChild.beforeLoading()
          this.ifFirstSee = false
        } else {
          this.$refs.seeChild.reOpen()
        }
      })
    },
    // 导出按钮操作
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
          console.log('导出');
          console.log(fileName);
          console.log(res.msg);
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
          console.log('模板');
          console.log(fileName);
          console.log(res.msg);
          fileDownload({ filename: res.msg }).then(res => {
            export_excel_file(res, fileName);
          })
        })
      })
    },
    //获取搜索条件
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

            if (data.relateSearchAttributeId) {
              obj.relateSearchAttribute = data.relateSearchAttribute
              obj.relateSearchCondition = data.relateSearchCondition
            }
            params.whereAttributes.push(obj)
          }
        }
      }
      //条件查询
      for (let i = 0; i < this.queryList.length; i++) {
        //多选
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
          //单选
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
    //打开修改
    openEdit({ row }) {
      this.editForm = row
      console.log(11111111111111);
      console.log(row);
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
    //修改提交
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
              if (key === this.$refs.child.keyClu) { obj.isPrimaryKey = 1 }
              params.valueAttributes.push(obj)
            }
          }
          submitEdit(params).then(response => {
            this.editStationDrawer = false
            this.saveErrorStation(this.editForm[this.$refs.child.keyClu])
            this.$modal.msgSuccess("操作成功");
            this.getTableData()
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
      }, 1500);
    },

    //  列表排序
    sortChange(column, prop, order) {
      this.column.prop = column.prop
      //descending降序、ascending升序
      if (column.order === 'descending') {
        this.column.order = 'desc'
      }
      else if (column.order === 'ascending') {
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
    // 表格右击的功能
    rowContextmenu(row, column, event) {
      //高亮
      this.$refs.multipleTable.setCurrentRow(row)
      if (this.rightClickMenus[0]) {
        event.preventDefault();
        let menu = document.querySelector("#menu");
        menu.style.display = "block";
        menu.style.zIndex = 1000;
        // 根据事件对象中鼠标点击的位置，进行定位
        let width = menu.getElementsByClassName('menuUl')[0].offsetWidth + 20
        let left = window.screen.width - event.x < width ? window.screen.width - width : event.x
        let height = this.rightClickMenus.length * 35
        let top = window.screen.height - event.y - 150 < height ? window.screen.height - height - 150 : event.y
        top = top - 120
        left = left - 30
        menu.style.left = left + 'px'
        menu.style.top = top + 'px'
        // 改变自定义菜单的隐藏与显示
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
      // 调用方法 然后返回行信息和点击菜单信息
      this.$emit(item.functionName, {
        rowData: this.rightClikcRow,
        menu: item,
      })
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
      let msg = ''
      if (this.multipleSelection.length === 0) {
        msg = '未选择数据 是否根据筛选条件开始转换?'
      } else {
        msg = '已选择' + this.multipleSelection.length + '条数据 是否开始转换?'
      }
      this.$confirm(msg, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        if (this.multipleSelection.length === 0) {
          let params = this.getSearchCriteria();
          obtainIDBasedOnFilteringCriteria(params).then(res => {
            if (res.data) {
              let data = {
                metaDataIdList: res.data,
                ruleId: row.ruleId,
                tableId: row.tableId,
              }
              this.transitionBox.loading = true
              commitTransitionApi(data).then(res => {
                this.transitionBox.loading = false
                this.transitionBox.show = false
                this.$modal.msgSuccess("转换完成");
                this.multipleSelection = []
                this.$refs.multipleTable.clearSelection()
                this.getTableData()
              })
            } else {
              this.$message({
                type: 'info',
                message: '没有需要转换的数据'
              })
            }
          })
        } else {
          let arr = []
          for (let i = 0; i < this.multipleSelection.length; i++) {
            arr.push(this.multipleSelection[i].ID)
          }
          let data = {
            metaDataIdList: arr,
            ruleId: row.ruleId,
            tableId: row.tableId,
          }
          this.transitionBox.loading = true
          commitTransitionApi(data).then(res => {
            this.transitionBox.loading = false
            this.transitionBox.show = false
            this.$modal.msgSuccess("转换完成");
            this.multipleSelection = []
            this.$refs.multipleTable.clearSelection()
            this.getTableData()
          })
        }
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消转换'
        })
      })
    },
  },
};
</script>
<style scoped lang="scss">
// 菜单样式（原样保留）
.menuDiv {
  display: none;
  position: absolute;

  .menuUl {
    height: auto;
    width: auto;
    font-size: 14px;
    text-align: left;
    border-radius: 4px;
    background-color: #ffffff;
    color: #606266;
    list-style: none;
    border: 1px solid #ebeef5;
    position: fixed;
    padding-inline-start: 0px !important;

    li {
      height: 35px;
      line-height: 35px;
      padding: 0 10px;
      cursor: pointer;
      border-bottom: 1px solid rgba(255, 255, 255, 0.47);

      &:hover {
        display: block;
        background-color: #ecf5ff;
        color: #079b9f;
      }
    }
  }
}

// 导入框样式（原样保留）
.import-box {
  padding: 20px 0;

  .import-item {
    margin-bottom: 30px;

    &:last-child {
      margin-bottom: 0;
    }

    .import-label {
      display: block;
      font-weight: 700;
      margin-bottom: 10px;
      color: #333;
    }

    .tips-text {
      margin-top: 8px;
      font-size: 12px;
      color: #666;
    }
  }
}

// 修复上传组件拖拽样式（原样保留）
::v-deep .el-upload--drag {
  border: 1px dashed #dcdcdc;
  border-radius: 6px;
  padding: 40px 0;
  text-align: center;
}

// 新增：页面整体背景渐变（仅添加，不修改原有属性）
.commonWh {
  background: linear-gradient(180deg, #Dffff6 0%, #ffffff 100%);
}

// 新增：搜索区域背景渐变（仅添加，不修改原有属性）
.queryBox {
  background: linear-gradient(180deg, #Dffff6 0%, #ffffff 100%);
}
</style>