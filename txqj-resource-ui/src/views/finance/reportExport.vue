<template>
  <div class="finance-page">
    <div class="container">
      <!-- 功能切换头部：新增功能入口，不影响原始功能 -->
      <div class="function-switch-header" v-if="showFunctionSwitch">
        <div class="switch-card">
          <h2>财务数据多功能生成平台</h2>
          <div class="function-tabs">
            <el-radio-group v-model="activeFunction" @change="handleFunctionChange">
              <el-radio-button label="wordReport">1. 月快报数据分析Word导出</el-radio-button>
              <el-radio-button label="unitExcel">2. 各单位主要指标Excel导出</el-radio-button>
              <el-radio-button label="reportExcel">3. 财务报表指标Excel导出</el-radio-button>
            </el-radio-group>
          </div>
        </div>
      </div>

      <!-- 原始Word功能区域：完全保留，未做任何修改 -->
      <div v-if="activeFunction === 'wordReport'">
        <!-- 页面头部：更新文件说明 -->
        <div class="page-header">
          <div class="header-card">
            <h1>「快报数据分析报告生成器」</h1>
            <p class="desc">基于「xxxx年×月快报数据分析模板（新）.docx」结构，上传Excel动态生成</p>
            <div class="file-tip-card">
              <div class="tip-title">
                <i class="el-icon-info-circle"></i>
                <span>必传文件说明</span>
              </div>
              <ul class="file-list">
                <li>
                  <span class="file-tag">1. 划入文件</span>
                  <span class="file-desc">例：国网浙江综合能源服务有限公司（划入）-2025-09-30.xlsx，含资产负债表/损益表/现金流量表/其他指标表</span>
                </li>
              </ul>
            </div>
          </div>
        </div>

        <!-- 上传区域：仅保留划入文件上传 -->
        <div class="upload-section">
          <div class="upload-card-group">
            <div class="upload-card">
              <div class="card-header">
                <i class="el-icon-folder-add"></i>
                <span>国网浙江综合能源服务有限公司（划入）-20xx-xx-xx文件上传</span>
              </div>
              <el-upload class="upload-container" drag action="#" :auto-upload="false" :on-change="handleTransferFileChange"
                :file-list="transferFiles" accept=".xlsx,.xls,.XLSX,.XLS" :limit="1"
                :on-exceed="() => this.$message.warning('仅允许上传1个划入文件')">
                <i class="el-icon-cloud-upload upload-icon"></i>
                <div class="upload-text">
                  <p class="main-text">拖拽文件至此处，或点击选择</p>
                  <p class="sub-text">示例：国网浙江综合能源服务有限公司（划入）-2025-09-30.xlsx</p>
                </div>
              </el-upload>
            </div>
          </div>

          <!-- 已上传文件列表 -->
          <div class="file-info-card" v-if="transferFiles.length">
            <div class="card-header">
              <i class="el-icon-document"></i>
              <span>已上传文件列表</span>
            </div>
            <el-table :data="getFileInfoList()" border size="small" class="file-table">
              <el-table-column label="文件名" prop="name" width="400"></el-table-column>
              <el-table-column label="文件类型" prop="typeText">
                <template #default="scope">
                  <el-tag type="success">{{ scope.row.typeText }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column label="大小" prop="sizeText"></el-table-column>
              <el-table-column label="操作" width="100">
                <template #default="scope">
                  <el-button type="text" icon="el-icon-delete" @click="removeTransferFile" class="delete-btn">
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>

        <!-- 解析按钮 -->
        <div class="btn-section" v-if="transferFiles.length">
          <div class="btn-card">
            <el-button type="primary" @click="parseExcel" :loading="isParsing" icon="el-icon-search" class="parse-btn">
              <i class="el-icon-loading" v-if="isParsing"></i>
              解析数据（匹配「2.2025年×月快报数据分析模板（新）.docx」字段）
            </el-button>
          </div>
        </div>

        <!-- 数据预览 -->
        <div class="preview-section" v-if="showPreview">
          <div class="section-header">
            <h2 class="section-title">预览模板字段数据</h2>
            <p class="section-desc">对应「2.2025年×月快报数据分析模板（新）.docx」</p>
          </div>
          <el-tabs v-model="activeTab" type="border-card" class="preview-tabs">
            <!-- 第一章：损益情况 -->
            <el-tab-pane label="一、损益情况" name="income">
              <el-table :data="preview.income" border size="mini" class="preview-table">
                <el-table-column label="模板字段" prop="field"></el-table-column>
                <el-table-column label="Excel数据" prop="value">
                  <template #default="scope">
                    <div class="data-value">{{ scope.row.value || '-' }}</div>
                  </template>
                </el-table-column>
                <el-table-column label="单位" prop="unit"></el-table-column>
              </el-table>
            </el-tab-pane>
            <!-- 第二章：资产负债情况 -->
            <el-tab-pane label="二、资产负债情况" name="balance">
              <el-table :data="preview.balance" border size="mini" class="preview-table">
                <el-table-column label="模板字段" prop="field"></el-table-column>
                <el-table-column label="Excel数据" prop="value">
                  <template #default="scope">
                    <div class="data-value">{{ scope.row.value || '-' }}</div>
                  </template>
                </el-table-column>
                <el-table-column label="单位" prop="unit"></el-table-column>
              </el-table>
            </el-tab-pane>
            <!-- 第三章：现金流量情况 -->
            <el-tab-pane label="三、现金流量情况" name="cashFlow">
              <el-table :data="preview.cashFlow" border size="mini" class="preview-table">
                <el-table-column label="模板字段" prop="field"></el-table-column>
                <el-table-column label="Excel数据" prop="value">
                  <template #default="scope">
                    <div class="data-value">{{ scope.row.value || '-' }}</div>
                  </template>
                </el-table-column>
                <el-table-column label="单位" prop="unit"></el-table-column>
              </el-table>
            </el-tab-pane>
            <!-- 第四章：其他指标 -->
            <el-tab-pane label="四、其他指标" name="other">
              <el-table :data="preview.other" border size="mini" class="preview-table">
                <el-table-column label="模板字段" prop="field"></el-table-column>
                <el-table-column label="Excel数据" prop="value">
                  <template #default="scope">
                    <div class="data-value">{{ scope.row.value || '-' }}</div>
                  </template>
                </el-table-column>
                <el-table-column label="单位" prop="unit"></el-table-column>
              </el-table>
            </el-tab-pane>
          </el-tabs>
        </div>

        <!-- 生成按钮 -->
        <div class="btn-section" v-if="showGenerate">
          <div class="btn-card">
            <el-button type="success" @click="generateReport" :loading="isGenerating" icon="el-icon-download"
              class="generate-btn">
              <i class="el-icon-loading" v-if="isGenerating"></i>
              生成「{{ targetReportName }}」
            </el-button>
          </div>
        </div>
      </div>

      <!-- 新增功能1：各单位主要指标Excel导出 -->
      <div v-if="activeFunction === 'unitExcel'" class="new-function-card">
        <div class="detail-header">
          <h2>各单位主要指标Excel导出</h2>
          <p class="detail-desc">汇总各地市“国网（XX）综合能源服务有限公司（划入）”文件，需至少1个地市文件</p>
        </div>

        <!-- 1. 文件上传区 -->
        <div class="upload-section">
          <div class="upload-card-group">
            <div class="upload-card">
              <div class="card-header">
                <i class="el-icon-folder-add"></i>
                <span>地市划入文件上传（支持多文件）</span>
              </div>
              <el-upload class="upload-container" drag action="#" :auto-upload="false"
                :on-change="(file) => handleNewFileUpload(file, 'unitExcel')" :file-list="newFileGroups.unitExcel"
                accept=".xlsx,.xls,.XLSX,.XLS">
                <i class="el-icon-cloud-upload upload-icon"></i>
                <div class="upload-text">
                  <p class="main-text">拖拽文件至此处，或点击选择（支持多文件）</p>
                  <p class="sub-text">示例：国网（杭州）综合能源服务有限公司（划入）-2025-10-31.xlsx</p>
                </div>
              </el-upload>
            </div>
          </div>

          <!-- 已上传文件列表 -->
          <div class="file-info-card" v-if="newFileGroups.unitExcel.length">
            <div class="card-header">
              <i class="el-icon-document"></i>
              <span>已上传地市文件列表</span>
            </div>
            <el-table :data="getNewFileInfoList('unitExcel')" border size="small" class="file-table">
              <el-table-column label="文件名" prop="name" width="400"></el-table-column>
              <el-table-column label="所属地市" prop="cityName">
                <template #default="scope">
                  <el-tag type="info">{{ scope.row.cityName }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column label="大小" prop="sizeText"></el-table-column>
              <el-table-column label="操作" width="100">
                <template #default="scope">
                  <el-button type="text" icon="el-icon-delete" 
                    @click="removeNewFile(scope.row.uid, 'unitExcel')" class="delete-btn">
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>

        <!-- 解析&生成按钮 -->
        <div class="btn-section" v-if="newFileGroups.unitExcel.length">
          <div class="btn-card">
            <el-button type="primary" @click="parseNewFile('unitExcel')" :loading="newLoading.parse.unitExcel" 
              icon="el-icon-search" class="parse-btn" :disabled="newFileGroups.unitExcel.length < 1">
              <i class="el-icon-loading" v-if="newLoading.parse.unitExcel"></i>
              解析地市文件（共{{ newFileGroups.unitExcel.length }}个）
            </el-button>
            <el-button type="success" @click="generateNewFile('unitExcel')" :loading="newLoading.generate.unitExcel" 
              icon="el-icon-download" class="generate-btn ml-4" :disabled="!newShowGenerate.unitExcel">
              <i class="el-icon-loading" v-if="newLoading.generate.unitExcel"></i>
              生成「{{ newTargetFileName.unitExcel }}」
            </el-button>
          </div>
        </div>

        <!-- 预览区域 -->
        <div class="preview-section" v-if="newShowPreview.unitExcel">
          <div class="section-header">
            <h2 class="section-title">地市指标汇总预览</h2>
            <p class="section-desc">汇总各地市核心财务指标</p>
          </div>
          <div class="preview-card">
            <el-table :data="newPreview.unitExcel" border size="mini" class="preview-table">
              <el-table-column label="所属地市" prop="cityName" width="120"></el-table-column>
              <el-table-column label="营业收入（亿元）" prop="revenue"></el-table-column>
              <el-table-column label="利润总额（亿元）" prop="profit"></el-table-column>
              <el-table-column label="资产总额（亿元）" prop="totalAssets"></el-table-column>
              <el-table-column label="资产负债率（%）" prop="debtRatio"></el-table-column>
            </el-table>
          </div>
        </div>
      </div>

      <!-- 新增功能2：财务报表主要指标Excel导出 -->
      <div v-if="activeFunction === 'reportExcel'" class="new-function-card">
        <div class="detail-header">
          <h2>财务报表主要指标Excel导出</h2>
          <p class="detail-desc">需上传2类文件：1个主文件（三公及会议费报表）+ 多个支撑文件（各地市/子公司文件）</p>
        </div>

        <!-- 1. 文件上传区（分2类） -->
        <div class="upload-section">
          <div class="upload-card-group">
            <!-- 主文件上传 -->
            <div class="upload-card">
              <div class="card-header">
                <i class="el-icon-folder-add"></i>
                <span>主文件（三公及会议费报表）上传</span>
              </div>
              <el-upload class="upload-container" drag action="#" :auto-upload="false"
                :on-change="(file) => handleNewFileUpload(file, 'reportExcel', 'main')" 
                :file-list="newFileGroups.reportExcel.main" accept=".xlsx,.xls,.XLSX,.XLS" :limit="1"
                :on-exceed="() => this.$message.warning('仅允许上传1个主文件')">
                <i class="el-icon-cloud-upload upload-icon"></i>
                <div class="upload-text">
                  <p class="main-text">拖拽文件至此处，或点击选择</p>
                  <p class="sub-text">示例：公司2025年1-10月财务报表主要指标-三公及会议费.xls</p>
                </div>
              </el-upload>
            </div>

            <!-- 支撑文件上传 -->
            <div class="upload-card">
              <div class="card-header">
                <i class="el-icon-folder-add"></i>
                <span>支撑文件（地市/子公司划入文件）上传</span>
              </div>
              <el-upload class="upload-container" drag action="#" :auto-upload="false"
                :on-change="(file) => handleNewFileUpload(file, 'reportExcel', 'support')" 
                :file-list="newFileGroups.reportExcel.support" accept=".xlsx,.xls,.XLSX,.XLS">
                <i class="el-icon-cloud-upload upload-icon"></i>
                <div class="upload-text">
                  <p class="main-text">拖拽文件至此处，或点击选择（支持多文件）</p>
                  <p class="sub-text">示例：国网浙江综合能源服务有限公司本部（划入）-2025-10-31.xlsx</p>
                </div>
              </el-upload>
            </div>
          </div>

          <!-- 已上传文件列表 -->
          <div class="file-info-card" v-if="newFileGroups.reportExcel.main.length || newFileGroups.reportExcel.support.length">
            <div class="card-header">
              <i class="el-icon-document"></i>
              <span>已上传文件列表</span>
            </div>
            <el-table :data="getNewFileInfoList('reportExcel')" border size="small" class="file-table">
              <el-table-column label="文件名" prop="name" width="400"></el-table-column>
              <el-table-column label="文件类型" prop="typeText">
                <template #default="scope">
                  <el-tag :type="scope.row.type === 'main' ? 'success' : 'info'">{{ scope.row.typeText }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column label="大小" prop="sizeText"></el-table-column>
              <el-table-column label="操作" width="100">
                <template #default="scope">
                  <el-button type="text" icon="el-icon-delete" 
                    @click="removeNewFile(scope.row.uid, 'reportExcel', scope.row.type)" class="delete-btn">
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>

        <!-- 解析&生成按钮 -->
        <div class="btn-section" v-if="newFileGroups.reportExcel.main.length || newFileGroups.reportExcel.support.length">
          <div class="btn-card">
            <el-button type="primary" @click="parseNewFile('reportExcel')" :loading="newLoading.parse.reportExcel" 
              icon="el-icon-search" class="parse-btn" 
              :disabled="!(newFileGroups.reportExcel.main.length === 1 && newFileGroups.reportExcel.support.length >= 1)">
              <i class="el-icon-loading" v-if="newLoading.parse.reportExcel"></i>
              解析文件（1主+{{ newFileGroups.reportExcel.support.length }}支撑）
            </el-button>
            <el-button type="success" @click="generateNewFile('reportExcel')" :loading="newLoading.generate.reportExcel" 
              icon="el-icon-download" class="generate-btn ml-4" :disabled="!newShowGenerate.reportExcel">
              <i class="el-icon-loading" v-if="newLoading.generate.reportExcel"></i>
              生成「{{ newTargetFileName.reportExcel }}」
            </el-button>
          </div>
        </div>

        <!-- 预览区域 -->
        <div class="preview-section" v-if="newShowPreview.reportExcel">
          <div class="section-header">
            <h2 class="section-title">三公及会议费指标预览</h2>
            <p class="section-desc">1-10月累计数据汇总</p>
          </div>
          <el-tabs v-model="newActiveTab.reportExcel" type="border-card" class="preview-tabs">
            <el-tab-pane label="三公经费" name="publicFund">
              <el-table :data="newPreview.reportExcel.publicFund" border size="mini" class="preview-table">
                <el-table-column label="指标名称" prop="indicator" width="200"></el-table-column>
                <el-table-column label="1-10月累计（万元）" prop="totalAmount"></el-table-column>
                <el-table-column label="月度均值（万元）" prop="monthlyAvg"></el-table-column>
                <el-table-column label="同比增减（万元）" prop="yoyAmount"></el-table-column>
              </el-table>
            </el-tab-pane>
            <el-tab-pane label="会议费" name="meetingFee">
              <el-table :data="newPreview.reportExcel.meetingFee" border size="mini" class="preview-table">
                <el-table-column label="指标名称" prop="indicator" width="200"></el-table-column>
                <el-table-column label="1-10月累计（万元）" prop="totalAmount"></el-table-column>
                <el-table-column label="大型会议次数" prop="meetingCount"></el-table-column>
                <el-table-column label="同比增减（%）" prop="yoyRate"></el-table-column>
              </el-table>
            </el-tab-pane>
          </el-tabs>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
// 保留原始API导入
import { parseExcelForTemplate, generateReportForTarget } from '@/api/finance/targetReportApi'

export default {
  name: 'FinanceMultiFunctionGenerate',
  data() {
    return {
      // ====================== 原始Word功能数据（完全保留，未修改）======================
      transferFiles: [],
      transferRaw: null,
      isParsing: false,
      isGenerating: false,
      showPreview: false,
      showGenerate: false,
      activeTab: 'income',
      targetReportName: '2025年×月快报数据分析.docx',
      preview: {
        income: [
          { field: '营业收入（当年累计）', value: '', unit: '亿元' },
          { field: '营业收入同比增减', value: '', unit: '亿元' },
          { field: '营业收入同比增长率', value: '', unit: '%' },
          { field: '营业成本（当年累计）', value: '', unit: '亿元' },
          { field: '营业成本同比增减', value: '', unit: '亿元' },
          { field: '营业成本同比增长率', value: '', unit: '%' },
          { field: '毛利率同比变化', value: '', unit: '%' },
          { field: '营业费用', value: '', unit: '亿元' },
          { field: '营业费用同比增减率', value: '', unit: '%' },
          { field: '管理费用', value: '', unit: '亿元' },
          { field: '管理费用同比增减率', value: '', unit: '%' },
          { field: '财务费用', value: '', unit: '亿元' },
          { field: '财务费用同比增减率', value: '', unit: '%' },
          { field: '投资收益', value: '', unit: '亿元' },
          { field: '投资收益同比增减率', value: '', unit: '%' },
          { field: '营业外收入', value: '', unit: '亿元' },
          { field: '营业外收入同比增减率', value: '', unit: '%' },
          { field: '营业外支出', value: '', unit: '亿元' },
          { field: '营业外支出同比增减率', value: '', unit: '%' },
          { field: '利润总额（当年累计）', value: '', unit: '亿元' },
          { field: '利润总额同比增减', value: '', unit: '亿元' },
          { field: '利润总额同比增减率', value: '', unit: '%' }
        ],
        balance: [
          { field: '资产总额', value: '', unit: '亿元' },
          { field: '资产总额较年初增减', value: '', unit: '亿元' },
          { field: '资产总额较年初增长率', value: '', unit: '%' },
          { field: '权益总额', value: '', unit: '亿元' },
          { field: '权益总额较年初增减', value: '', unit: '亿元' },
          { field: '权益总额较年初增长率', value: '', unit: '%' },
          { field: '应收账款净额', value: '', unit: '亿元' },
          { field: '应收账款较年初增减', value: '', unit: '亿元' },
          { field: '应收账款较年初增长率', value: '', unit: '%' },
          { field: '系统外应收账款净额', value: '', unit: '亿元' },
          { field: '系统外应收账款较年初增减', value: '', unit: '亿元' },
          { field: '系统外应收账款较年初增长率', value: '', unit: '%' },
          { field: '存货净额', value: '', unit: '亿元' },
          { field: '存货较年初增减', value: '', unit: '亿元' },
          { field: '存货较年初增长率', value: '', unit: '%' },
          { field: '带息负债余额', value: '', unit: '亿元' },
          { field: '带息负债较年初增减', value: '', unit: '亿元' },
          { field: '带息负债较年初增长率', value: '', unit: '%' }
        ],
        cashFlow: [
          { field: '经营活动现金流量净额', value: '', unit: '亿元' },
          { field: '营业收现率', value: '', unit: '%' }
        ],
        other: [
          { field: '净资产收益率', value: '', unit: '%' },
          { field: '净资产收益率同比变动', value: '', unit: '%' },
          { field: 'EVA', value: '', unit: '亿元' },
          { field: 'EVA同比增减', value: '', unit: '亿元' },
          { field: '资产负债率', value: '', unit: '%' },
          { field: '资产负债率较年初变动', value: '', unit: '%' },
          { field: '人工成本总额', value: '', unit: '万元' },
          { field: '全民员工人数', value: '', unit: '人' },
          { field: '全民员工人工成本', value: '', unit: '万元' },
          { field: '直管外包员工人数', value: '', unit: '人' },
          { field: '成本费用占收入比', value: '', unit: '%' },
          { field: '成本费用占收入比较年初变动', value: '', unit: '%' }
        ]
      },

      // ====================== 新增功能数据（独立命名空间，不影响原始功能）======================
      showFunctionSwitch: true, // 是否显示功能切换 tabs
      activeFunction: 'wordReport', // 当前激活的功能：wordReport/unitExcel/reportExcel
      // 新增功能-文件分组
      newFileGroups: {
        unitExcel: [], // 各单位Excel-地市文件
        reportExcel: { main: [], support: [] } // 财务报表Excel-主文件+支撑文件
      },
      // 新增功能-原始文件存储
      newFileRaw: {
        unitExcel: [],
        reportExcel: { main: null, support: [] }
      },
      // 新增功能-加载状态
      newLoading: {
        parse: { unitExcel: false, reportExcel: false },
        generate: { unitExcel: false, reportExcel: false }
      },
      // 新增功能-显示状态
      newShowPreview: { unitExcel: false, reportExcel: false },
      newShowGenerate: { unitExcel: false, reportExcel: false },
      // 新增功能-标签页
      newActiveTab: { reportExcel: 'publicFund' },
      // 新增功能-目标文件名
      newTargetFileName: {
        unitExcel: '2025年×月各单位主要指标汇总.xlsx',
        reportExcel: '2025年1-×月财务报表主要指标（三公及会议费）.xlsx'
      },
      // 新增功能-预览数据
      newPreview: {
        unitExcel: [
          { cityName: '杭州', revenue: '', profit: '', totalAssets: '', debtRatio: '' },
          { cityName: '宁波', revenue: '', profit: '', totalAssets: '', debtRatio: '' },
          { cityName: '温州', revenue: '', profit: '', totalAssets: '', debtRatio: '' },
          { cityName: '嘉兴', revenue: '', profit: '', totalAssets: '', debtRatio: '' },
          { cityName: '湖州', revenue: '', profit: '', totalAssets: '', debtRatio: '' }
        ],
        reportExcel: {
          publicFund: [
            { indicator: '因公出国（境）费用', totalAmount: '', monthlyAvg: '', yoyAmount: '' },
            { indicator: '公务用车购置及运行费', totalAmount: '', monthlyAvg: '', yoyAmount: '' },
            { indicator: '公务接待费', totalAmount: '', monthlyAvg: '', yoyAmount: '' }
          ],
          meetingFee: [
            { indicator: '会议费总支出', totalAmount: '', meetingCount: '', yoyRate: '' },
            { indicator: '大型会议支出', totalAmount: '', meetingCount: '', yoyRate: '' }
          ]
        }
      }
    }
  },
  methods: {
    // ====================== 原始Word功能方法（完全保留，未修改）======================
    handleTransferFileChange(file) {
      if (file.size > 10 * 1024 * 1024) {
        this.$message.error(`文件 "${file.name}" 超过10MB，请压缩后上传`);
        return;
      }
      this.transferFiles = [file];
      this.transferRaw = file.raw;
      this.extractDateFromFileName(file.name);
    },
    extractDateFromFileName(fileName) {
      let year = '2025';
      let month = '';
      const yearMatch = fileName.match(/20\d{2}/);
      const monthMatch = fileName.match(/-(\d{1,2})-/);
      if (yearMatch) year = yearMatch[0];
      if (monthMatch) month = parseInt(monthMatch[1]);
      if (month) {
        this.targetReportName = `${year}年${month}月快报数据分析.docx`;
      }
    },
    getFileInfoList() {
      const list = [];
      if (this.transferFiles[0]) {
        list.push({
          name: this.transferFiles[0].name,
          type: 'transfer',
          typeText: '划入文件',
          sizeText: this.formatFileSize(this.transferFiles[0].size)
        });
      }
      return list;
    },
    formatFileSize(size) {
      if (size < 1024) return `${size}B`;
      if (size < 1024 * 1024) return `${(size / 1024).toFixed(2)}KB`;
      return `${(size / (1024 * 1024)).toFixed(2)}MB`;
    },
    removeTransferFile() {
      this.transferFiles = [];
      this.transferRaw = null;
      this.showPreview = false;
      this.showGenerate = false;
      this.$message.success('划入文件已删除');
    },
    parseExcel() {
      this.isParsing = true;
      const formData = new FormData();
      formData.append('transferFile', this.transferRaw);

      parseExcelForTemplate(formData).then(res => {
        this.isParsing = false;
        if (res.code === 200) {
          this.fillPreviewData(res.data);
          this.showPreview = true;
          this.showGenerate = true;
          this.$message.success(`数据解析完成，已匹配「2.2025年×月快报数据分析模板（新）.docx」`);
        } else {
          this.$message.error(`解析失败：${res.msg || '未获取到模板所需数据'}`);
        }
      }).catch(err => {
        this.isParsing = false;
        const errMsg = err.response?.data?.msg || err.message || '接口请求失败';
        this.$message.error(`解析异常：${errMsg}`);
      });
    },
    fillPreviewData(parseResult) {
      // 填充损益情况
      this.preview.income = this.preview.income.map(item => {
        switch (item.field) {
          case '营业收入（当年累计）': return { ...item, value: parseResult.income.revenue };
          case '营业收入同比增减': return { ...item, value: parseResult.income.revenueYoYAmount };
          case '营业收入同比增长率': return { ...item, value: parseResult.income.revenueYoYRate };
          case '营业成本（当年累计）': return { ...item, value: parseResult.income.cost };
          case '营业成本同比增减': return { ...item, value: parseResult.income.costYoYAmount };
          case '营业成本同比增长率': return { ...item, value: parseResult.income.costYoYRate };
          case '毛利率同比变化': return { ...item, value: parseResult.income.grossMarginChange };
          case '营业费用': return { ...item, value: parseResult.income.businessCost };
          case '营业费用同比增减率': return { ...item, value: parseResult.income.businessCostYoYRate };
          case '管理费用': return { ...item, value: parseResult.income.managementFee };
          case '管理费用同比增减率': return { ...item, value: parseResult.income.managementFeeYoYRate };
          case '财务费用': return { ...item, value: parseResult.income.financeFee };
          case '财务费用同比增减率': return { ...item, value: parseResult.income.financeFeeYoYRate };
          case '投资收益': return { ...item, value: parseResult.income.investmentIncome };
          case '投资收益同比增减率': return { ...item, value: parseResult.income.investmentIncomeYoYRate };
          case '营业外收入': return { ...item, value: parseResult.income.nonOperatingIncome };
          case '营业外收入同比增减率': return { ...item, value: parseResult.income.nonOperatingIncomeYoYRate };
          case '营业外支出': return { ...item, value: parseResult.income.nonOperatingExpense };
          case '营业外支出同比增减率': return { ...item, value: parseResult.income.nonOperatingExpenseYoYRate };
          case '利润总额（当年累计）': return { ...item, value: parseResult.income.totalProfit };
          case '利润总额同比增减': return { ...item, value: parseResult.income.totalProfitYoYAmount };
          case '利润总额同比增减率': return { ...item, value: parseResult.income.totalProfitYoYRate };
          default: return item;
        }
      });

      // 填充资产负债情况
      this.preview.balance = this.preview.balance.map(item => {
        switch (item.field) {
          case '资产总额': return { ...item, value: parseResult.balance.totalAssets };
          case '资产总额较年初增减': return { ...item, value: parseResult.balance.totalAssetsYoYAmount };
          case '资产总额较年初增长率': return { ...item, value: parseResult.balance.totalAssetsYoYRate };
          case '权益总额': return { ...item, value: parseResult.balance.equity };
          case '权益总额较年初增减': return { ...item, value: parseResult.balance.equityYoYAmount };
          case '权益总额较年初增长率': return { ...item, value: parseResult.balance.equityYoYRate };
          case '应收账款净额': return { ...item, value: parseResult.balance.receivables };
          case '应收账款较年初增减': return { ...item, value: parseResult.balance.receivablesYoYAmount };
          case '应收账款较年初增长率': return { ...item, value: parseResult.balance.receivablesYoYRate };
          case '系统外应收账款净额': return { ...item, value: parseResult.balance.externalReceivables };
          case '系统外应收账款较年初增减': return { ...item, value: parseResult.balance.externalReceivablesYoYAmount };
          case '系统外应收账款较年初增长率': return { ...item, value: parseResult.balance.externalReceivablesYoYRate };
          case '存货净额': return { ...item, value: parseResult.balance.inventory };
          case '存货较年初增减': return { ...item, value: parseResult.balance.inventoryYoYAmount };
          case '存货较年初增长率': return { ...item, value: parseResult.balance.inventoryYoYRate };
          case '带息负债余额': return { ...item, value: parseResult.balance.interestDebt };
          case '带息负债较年初增减': return { ...item, value: parseResult.balance.interestDebtYoYAmount };
          case '带息负债较年初增长率': return { ...item, value: parseResult.balance.interestDebtYoYRate };
          default: return item;
        }
      });

      // 填充现金流量情况
      this.preview.cashFlow = this.preview.cashFlow.map(item => {
        switch (item.field) {
          case '经营活动现金流量净额': return { ...item, value: parseResult.cashFlow.operatingCashFlow };
          case '营业收现率': return { ...item, value: parseResult.cashFlow.cashCollectionRate };
          default: return item;
        }
      });

      // 填充其他指标
      this.preview.other = this.preview.other.map(item => {
        switch (item.field) {
          case '净资产收益率': return { ...item, value: parseResult.other.roe };
          case '净资产收益率同比变动': return { ...item, value: parseResult.other.roeYoYChange };
          case 'EVA': return { ...item, value: parseResult.other.eva };
          case 'EVA同比增减': return { ...item, value: parseResult.other.evaYoYChange };
          case '资产负债率': return { ...item, value: parseResult.other.debtRatio };
          case '资产负债率较年初变动': return { ...item, value: parseResult.other.debtRatioYoYChange };
          case '人工成本总额': return { ...item, value: parseResult.other.laborCost };
          case '全民员工人数': return { ...item, value: parseResult.other.staffCount };
          case '全民员工人工成本': return { ...item, value: parseResult.other.staffLaborCost };
          case '成本费用占收入比': return { ...item, value: parseResult.other.costIncomeRatio };
          case '成本费用占收入比较年初变动': return { ...item, value: parseResult.other.costIncomeRatioYoYChange };
          default: return item;
        }
      });
    },
    generateReport() {
      this.isGenerating = true;
      const formData = new FormData();
      formData.append('transferFile', this.transferRaw);
      formData.append('targetReportName', this.targetReportName);

      generateReportForTarget(formData).then(res => {
        this.isGenerating = false;
        const blob = new Blob([res], {
          type: 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'
        });
        const url = URL.createObjectURL(blob);
        const aLink = document.createElement('a');
        aLink.href = url;
        aLink.download = this.targetReportName;
        document.body.appendChild(aLink);
        aLink.click();
        URL.revokeObjectURL(url);
        document.body.removeChild(aLink);
        this.$message.success(`「${this.targetReportName}」生成并下载成功`);
      }).catch(err => {
        this.isGenerating = false;
        const errMsg = err.response?.data?.msg || err.message || '接口请求失败';
        this.$message.error(`生成失败：${errMsg}`);
      });
    },

    // ====================== 新增功能方法（独立命名，不影响原始功能）======================
    /**
     * 切换功能
     */
    handleFunctionChange() {
      // 切换功能时重置新增功能的状态，避免数据残留
      this.newShowPreview = { unitExcel: false, reportExcel: false };
      this.newShowGenerate = { unitExcel: false, reportExcel: false };
    },

    /**
     * 新增功能-文件上传
     */
    handleNewFileUpload(file, functionKey, fileType = 'default') {
      // 校验文件大小
      if (file.size > 20 * 1024 * 1024) {
        this.$message.error(`文件 "${file.name}" 超过20MB，请压缩后上传`);
        return;
      }

      // 校验重复上传
      const fileList = functionKey === 'reportExcel' ? this.newFileGroups[functionKey][fileType] : this.newFileGroups[functionKey];
      const isDuplicate = fileList.some(item => item.name === file.name);
      if (isDuplicate) {
        this.$message.warning(`文件 "${file.name}" 已上传，无需重复上传`);
        return;
      }

      // 存储文件列表
      if (functionKey === 'reportExcel') {
        this.newFileGroups[functionKey][fileType].push(file);
        // 存储原始文件
        this.newFileRaw[functionKey][fileType] = fileType === 'main' ? file.raw : [...this.newFileRaw[functionKey][fileType], file.raw];
      } else {
        this.newFileGroups[functionKey].push(file);
        this.newFileRaw[functionKey].push(file.raw);
        // 提取地市名称并更新目标文件名
        this.extractNewFileDate(file.name, functionKey);
      }

      this.$message.success(`文件 "${file.name}" 上传成功`);
    },

    /**
     * 新增功能-提取文件名日期
     */
    extractNewFileDate(fileName, functionKey) {
      const yearMatch = fileName.match(/20\d{2}/);
      const monthMatch = fileName.match(/-(\d{1,2})-/);
      const year = yearMatch ? yearMatch[0] : '2025';
      const month = monthMatch ? parseInt(monthMatch[1]) : '';

      if (month) {
        if (functionKey === 'unitExcel') {
          this.newTargetFileName.unitExcel = `${year}年${month}月各单位主要指标汇总.xlsx`;
        } else if (functionKey === 'reportExcel') {
          this.newTargetFileName.reportExcel = `${year}年1-${month}月财务报表主要指标（三公及会议费）.xlsx`;
        }
      }
    },

    /**
     * 新增功能-获取文件列表（用于表格展示）
     */
    getNewFileInfoList(functionKey) {
      const list = [];
      if (functionKey === 'unitExcel') {
        // 地市文件列表
        list.push(...this.newFileGroups.unitExcel.map(file => {
          const cityMatch = file.name.match(/国网（([^）]+)）/);
          const cityName = cityMatch ? cityMatch[1] : '未知地市';
          return {
            id: file.uid,
            name: file.name,
            cityName,
            sizeText: this.formatFileSize(file.size)
          };
        }));
      } else if (functionKey === 'reportExcel') {
        // 主文件列表
        list.push(...this.newFileGroups.reportExcel.main.map(file => ({
          id: file.uid,
          name: file.name,
          type: 'main',
          typeText: '主文件（三公及会议费报表）',
          sizeText: this.formatFileSize(file.size)
        })));
        // 支撑文件列表
        list.push(...this.newFileGroups.reportExcel.support.map(file => ({
          id: file.uid,
          name: file.name,
          type: 'support',
          typeText: '支撑文件（地市/子公司）',
          sizeText: this.formatFileSize(file.size)
        })));
      }
      return list;
    },

    /**
     * 新增功能-删除文件
     */
    removeNewFile(fileId, functionKey, fileType = 'default') {
      if (functionKey === 'reportExcel') {
        const fileList = this.newFileGroups[functionKey][fileType];
        const deleteIndex = fileList.findIndex(file => file.uid === fileId);
        if (deleteIndex === -1) return;
        fileList.splice(deleteIndex, 1);
        // 删除原始文件
        if (fileType === 'main') {
          this.newFileRaw[functionKey][fileType] = null;
        } else {
          this.newFileRaw[functionKey][fileType].splice(deleteIndex, 1);
        }
      } else {
        const fileList = this.newFileGroups[functionKey];
        const deleteIndex = fileList.findIndex(file => file.uid === fileId);
        if (deleteIndex === -1) return;
        fileList.splice(deleteIndex, 1);
        this.newFileRaw[functionKey].splice(deleteIndex, 1);
      }

      // 重置状态
      this.newShowPreview[functionKey] = false;
      this.newShowGenerate[functionKey] = false;
      this.$message.success('文件已删除');
    },

    /**
     * 新增功能-解析文件
     */
    parseNewFile(functionKey) {
      this.newLoading.parse[functionKey] = true;
      const formData = new FormData();

      // 组装参数（与后端接口匹配）
      if (functionKey === 'unitExcel') {
        // 各单位Excel：多个地市文件
        this.newFileRaw[functionKey].forEach((file, index) => {
          formData.append(`transferFile${index}`, file);
        });
      } else if (functionKey === 'reportExcel') {
        // 财务报表Excel：1主文件+多支撑文件
        formData.append('mainFile', this.newFileRaw[functionKey].main);
        this.newFileRaw[functionKey].support.forEach((file, index) => {
          formData.append(`supportFile${index}`, file);
        });
      }

      // 调用API解析
      parseExcelForTemplate(formData).then(res => {
        this.newLoading.parse[functionKey] = false;
        if (res.code === 200) {
          this.fillNewPreviewData(functionKey, res.data);
          this.newShowPreview[functionKey] = true;
          this.newShowGenerate[functionKey] = true;
          this.$message.success(`${functionKey === 'unitExcel' ? '地市指标' : '财务报表指标'}解析完成`);
        } else {
          this.$message.error(`解析失败：${res.msg || '数据格式错误'}`);
        }
      }).catch(err => {
        this.newLoading.parse[functionKey] = false;
        const errMsg = err.response?.data?.msg || err.message || '接口请求失败';
        this.$message.error(`解析异常：${errMsg}`);
      });
    },

    /**
     * 新增功能-填充预览数据
     */
    fillNewPreviewData(functionKey, parseResult) {
      if (functionKey === 'unitExcel') {
        // 填充地市指标预览
        this.newPreview.unitExcel = this.newPreview.unitExcel.map(cityItem => {
          const matchData = parseResult.cityData.find(item => item.cityName === cityItem.cityName) || {};
          return {
            ...cityItem,
            revenue: matchData.revenue || '',
            profit: matchData.profit || '',
            totalAssets: matchData.totalAssets || '',
            debtRatio: matchData.debtRatio || ''
          };
        });
      } else if (functionKey === 'reportExcel') {
        // 填充三公经费预览
        this.newPreview.reportExcel.publicFund = this.newPreview.reportExcel.publicFund.map(item => {
          switch (item.indicator) {
            case '因公出国（境）费用': return { ...item, totalAmount: parseResult.publicFund.overseas, monthlyAvg: parseResult.publicFund.overseasAvg, yoyAmount: parseResult.publicFund.overseasYoY };
            case '公务用车购置及运行费': return { ...item, totalAmount: parseResult.publicFund.vehicle, monthlyAvg: parseResult.publicFund.vehicleAvg, yoyAmount: parseResult.publicFund.vehicleYoY };
            case '公务接待费': return { ...item, totalAmount: parseResult.publicFund.reception, monthlyAvg: parseResult.publicFund.receptionAvg, yoyAmount: parseResult.publicFund.receptionYoY };
            default: return item;
          }
        });
        // 填充会议费预览
        this.newPreview.reportExcel.meetingFee = this.newPreview.reportExcel.meetingFee.map(item => {
          switch (item.indicator) {
            case '会议费总支出': return { ...item, totalAmount: parseResult.meetingFee.total, meetingCount: parseResult.meetingFee.totalCount, yoyRate: parseResult.meetingFee.totalYoY };
            case '大型会议支出': return { ...item, totalAmount: parseResult.meetingFee.large, meetingCount: parseResult.meetingFee.largeCount, yoyRate: parseResult.meetingFee.largeYoY };
            default: return item;
          }
        });
      }
    },

    /**
     * 新增功能-生成文件
     */
    generateNewFile(functionKey) {
      this.newLoading.generate[functionKey] = true;
      const formData = new FormData();
      const fileName = this.newTargetFileName[functionKey];
      formData.append('targetReportName', fileName);

      // 组装参数
      if (functionKey === 'unitExcel') {
        this.newFileRaw[functionKey].forEach((file, index) => {
          formData.append(`transferFile${index}`, file);
        });
      } else if (functionKey === 'reportExcel') {
        formData.append('mainFile', this.newFileRaw[functionKey].main);
        this.newFileRaw[functionKey].support.forEach((file, index) => {
          formData.append(`supportFile${index}`, file);
        });
      }

      // 调用API生成
      generateReportForTarget(formData).then(res => {
        this.newLoading.generate[functionKey] = false;
        // 处理Excel下载
        const blob = new Blob([res], {
          type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
        });
        const url = URL.createObjectURL(blob);
        const aLink = document.createElement('a');
        aLink.href = url;
        aLink.download = fileName;
        document.body.appendChild(aLink);
        aLink.click();
        URL.revokeObjectURL(url);
        document.body.removeChild(aLink);
        this.$message.success(`「${fileName}」生成并下载成功`);
      }).catch(err => {
        this.newLoading.generate[functionKey] = false;
        const errMsg = err.response?.data?.msg || err.message || '接口请求失败';
        this.$message.error(`生成失败：${errMsg}`);
      });
    }
  }
}
</script>

<style scoped>
/* 基础布局：保留原始样式，新增功能样式独立 */
.container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 20px;
}

.finance-page {
  padding: 32px 0;
  background: #f5f7fa;
  min-height: 100vh;
}

/* 功能切换头部样式 */
.function-switch-header {
  margin-bottom: 32px;
}

.switch-card {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  text-align: center;
}

.switch-card h2 {
  font-size: 20px;
  color: #2c3e50;
  margin-bottom: 16px;
  font-weight: 700;
}

.function-tabs .el-radio-group {
  display: inline-flex;
  background: #f8fafc;
  border-radius: 8px;
  padding: 8px;
}

.function-tabs .el-radio-button {
  margin: 0 4px;
}

/* 原始样式完全保留 */
.page-header {
  margin-bottom: 40px;
  text-align: center;
}

.header-card {
  background: #fff;
  border-radius: 12px;
  padding: 32px 24px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.page-header h1 {
  font-size: 24px;
  color: #2c3e50;
  margin-bottom: 12px;
  font-weight: 700;
}

.page-header .desc {
  font-size: 14px;
  color: #666;
  margin-bottom: 24px;
  line-height: 1.6;
}

.file-tip-card {
  background: #f8fafc;
  border-radius: 8px;
  padding: 16px;
  text-align: left;
  max-width: 800px;
  margin: 0 auto;
  border-left: 4px solid #409eff;
}

.tip-title {
  display: flex;
  align-items: center;
  color: #409eff;
  font-weight: 600;
  margin-bottom: 12px;
}

.tip-title i {
  margin-right: 8px;
  font-size: 16px;
}

.file-list {
  padding-left: 24px;
}

.file-list li {
  display: flex;
  margin-bottom: 8px;
  font-size: 13px;
  color: #333;
  line-height: 1.6;
}

.file-tag {
  min-width: 80px;
  color: #409eff;
  font-weight: 500;
  margin-right: 8px;
}

.file-desc {
  flex: 1;
  color: #666;
}

.upload-section {
  margin-bottom: 40px;
}

.upload-card-group {
  display: flex;
  gap: 24px;
  margin-bottom: 24px;
  flex-wrap: wrap;
}

.upload-card {
  flex: 1;
  min-width: 300px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

.card-header {
  background: #f8fafc;
  padding: 16px 24px;
  display: flex;
  align-items: center;
  border-bottom: 1px solid #f0f0f0;
}

.card-header i {
  font-size: 18px;
  color: #409eff;
  margin-right: 8px;
}

.card-header span {
  font-size: 15px;
  font-weight: 600;
  color: #333;
}

.upload-container {
  padding: 36px 20px;
  border: none !important;
}

.upload-icon {
  font-size: 40px;
  color: #409eff;
  margin-bottom: 16px;
}

.upload-text .main-text {
  font-size: 16px;
  color: #333;
  margin-bottom: 8px;
  font-weight: 500;
}

.upload-text .sub-text {
  font-size: 13px;
  color: #999;
}

.file-info-card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

.file-table {
  width: 100%;
  margin-bottom: 0;
}

.el-table th {
  background: #f8fafc !important;
  font-weight: 600 !important;
  color: #333 !important;
}

.el-tag {
  font-size: 12px !important;
  padding: 2px 8px !important;
}

.delete-btn {
  color: #f56c6c;
  transition: color 0.3s;
}

.delete-btn:hover {
  color: #f03838;
}

.btn-section {
  text-align: center;
  margin-bottom: 40px;
}

.btn-card {
  display: inline-block;
  background: #fff;
  padding: 24px 32px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.parse-btn,
.generate-btn {
  font-size: 15px;
  padding: 12px 32px;
  border-radius: 8px;
}

.preview-section {
  margin-bottom: 40px;
}

.section-header {
  margin-bottom: 24px;
  text-align: left;
}

.section-title {
  font-size: 18px;
  color: #333;
  font-weight: 700;
  margin-bottom: 8px;
}

.section-desc {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
}

.preview-tabs {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

.el-tabs__header {
  background: #f8fafc;
  border-bottom: 1px solid #f0f0f0 !important;
}

.el-tabs__item {
  font-size: 14px;
  font-weight: 500;
  color: #666;
  padding: 16px 24px !important;
}

.el-tabs__item.is-active {
  color: #409eff !important;
  font-weight: 600;
}

.el-tabs__content {
  padding: 24px;
}

.preview-table {
  width: 100%;
}

.preview-table th,
.preview-table td {
  padding: 8px !important;
  font-size: 12px !important;
}

.preview-table th {
  background: #f8fafc !important;
  font-weight: 600 !important;
  color: #333 !important;
}

.data-value {
  color: #333;
  font-weight: 500;
}

.remark-content {
  font-size: 12px;
  color: #666;
  line-height: 1.4;
}

/* 新增功能卡片样式 */
.new-function-card {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  padding: 24px;
}

/* 响应式适配 */
@media (max-width: 768px) {
  .upload-card-group {
    flex-direction: column;
  }

  .btn-card {
    width: 100%;
    padding: 16px;
  }

  .parse-btn,
  .generate-btn {
    width: 100%;
  }

  .function-tabs .el-radio-group {
    flex-direction: column;
  }

  .function-tabs .el-radio-button {
    width: 100%;
    margin: 4px 0;
  }

  .el-table-column {
    font-size: 12px !important;
  }
}
</style>