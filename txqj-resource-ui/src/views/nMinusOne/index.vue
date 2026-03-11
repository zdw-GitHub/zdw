<template>
  <div class="commonWh">
    <div class="box-card">
      <div class="queryBox">
        <!--基础查询-->
        <div class="basicsQuery">
          <el-form :model="queryParams" ref="queryForm" label-width="100px">
            <el-form-item label="风险类型 : " prop="riskType">
              <el-select
                size="mini"
                v-model="queryParams.riskType"
                @change="selceted2($event)"
                placeholder="请选择">
                <el-option
                  v-for="item in riskTypes"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="分析类型 : " prop="analysisType" v-if="queryParams.riskType == '1'">
              <el-select
                  size="mini"
                  v-model="queryParams.analysisType"
                  @change="selceted($event)"
                  placeholder="请选择">
                <el-option
                    v-for="item in analysisTypes"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="queryParams.analysisType === '' ? '类型 : ' : queryParams.analysisType+' : '" prop="selectData" v-if="queryParams.riskType == '1'">
              <el-select
                  size="mini"
                  v-model="queryParams.selectData"
                  filterable
                  remote
                  :loading="optionsLoading"
                  :remote-method="remoteMethod"
                  placeholder="请选择">
                <el-option
                    v-for="item in selectDatas"
                    :key="item.id"
                    :label="item.snname"
                    :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="'光缆'" prop="fiber" v-if="queryParams.riskType == '2'">
              <el-select
                size="mini"
                v-model="queryParams.fiber"
                filterable
                remote
                :loading="fiberLoading"
                :remote-method="remoteMethod2"
                @change="change"
                placeholder="请选择">
                <el-option
                  v-for="item in fiberDatas"
                  :key="item.id"
                  :label="item.snname"
                  :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item style="float: right;">
              <el-button type="primary" size="mini" @click="analysis" v-if="queryParams.riskType == '1'">分析</el-button>
              <el-button type="primary" size="mini" @click="analysis2" v-if="queryParams.riskType == '2'">分析</el-button>
              <el-button size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>

      <div class="tableBox">
        <div class="tabTopButton">
          <el-button type="primary" size="mini" @click="saveAnalysis" v-if="queryParams.riskType == '1'">保存</el-button>
        </div>

        <div id="nMinusOne">
          <el-row>
            <el-col :span="14" v-if="queryParams.riskType == '1'">
              <div class="editForm">
                <p class="formTop">
                  <span class="formTit">影响继电保护业务</span>
                </p>

                <div style="height: 35vh;">
                  <el-table
                      stripe
                      border
                      style="width: 100%"
                      height="100%"
                      :data="impactBusiness"
                      v-loading="loading">
                    <el-table-column label="序号" width="80" align="center" :formatter="snFormatter"></el-table-column>
                    <el-table-column
                        label="业务名称"
                        align="center"
                        show-overflow-tooltip
                        prop="name"
                    ></el-table-column>
                  </el-table>
                </div>
              </div>
            </el-col>
            <el-col :span="14" v-if="queryParams.riskType == '2'">
              <div class="editForm">
                <p class="formTop">
                  <span class="formTit">分析光缆</span>
                </p>

                <div style="height: 35vh;">
                  <el-table
                    ref="multipleTable"
                    stripe
                    border
                    style="width: 100%"
                    height="100%"
                    :data="fiberList">
                    <el-table-column label="序号" width="80" align="center" :formatter="snFormatter"></el-table-column>
                    <el-table-column
                      label="光缆名称"
                      align="center"
                      show-overflow-tooltip
                      prop="name"
                    ></el-table-column>
                    <el-table-column
                      label="起始站点"
                      align="center"
                      show-overflow-tooltip
                      prop="aResName"
                    ></el-table-column>
                    <el-table-column
                      label="终止站点"
                      align="center"
                      show-overflow-tooltip
                      prop="zResName"
                    ></el-table-column>
                    <el-table-column
                      label="操作"
                      fixed="right"
                      width="100"
                      align="center">
                      <template slot-scope="scope">
                        <el-button @click="deleteFiber(scope.row.id)">删除</el-button>
                      </template>
                    </el-table-column>
                  </el-table>
                </div>
              </div>
            </el-col>
            <el-col :span="10">
              <div class="editForm">
                <p class="formTop">
                  <span class="formTit">涉及线路</span>
                </p>
                <div style="height: 35vh;">
                  <el-table
                      stripe
                      border
                      style="width: 100%"
                      height="100%"
                      :data="involvingLines"
                      v-loading="loading">
                    <el-table-column width="50" label="选择" align="center">
                      <template slot-scope="scope">
                        <el-radio v-model="protectId" :label="scope.row.id" @change.native="handleSelectionChange(scope.$index,scope.row)"> <span></span></el-radio>
                      </template>
                    </el-table-column>
                    <el-table-column label="序号" width="50" align="center" :formatter="snFormatter"></el-table-column>
                    <el-table-column
                        label="线路名称"
                        align="center"
                        show-overflow-tooltip
                        prop="name"
                    ></el-table-column>
                    <el-table-column
                        v-if="queryParams.riskType == '1'"
                        label="分析结果"
                        align="center"
                        show-overflow-tooltip
                        prop="analysisResults"
                    >
                      <template slot-scope="scope">{{scope.row.analysisResults}}</template>
                    </el-table-column>
                  </el-table>
                </div>
              </div>
            </el-col>

          </el-row>

          <el-row>
            <el-col :span="24">
              <div class="editForm">
                <div style="height: 26vh;margin-top: 20px">
                  <el-table
                    stripe
                    border
                    style="width: 100%"
                    height="100%"
                    :data="protect.business"
                    v-loading="loading">
                    <el-table-column label="序号" width="50" align="center" :formatter="snFormatter"></el-table-column>
                    <el-table-column
                      label="业务名称"
                      align="center"
                      width="600"
                      show-overflow-tooltip
                      prop="name"
                    ></el-table-column>
                    <el-table-column
                      v-if="queryParams.riskType == '1'"
                      label="状态"
                      align="center"
                      width="200"
                      show-overflow-tooltip
                      prop="state">
                    </el-table-column>
                    <el-table-column
                      v-if="queryParams.riskType == '2'"
                      label="经过光缆"
                      align="center"
                      width="200"
                      show-overflow-tooltip
                      prop="fiberName">
                    </el-table-column>
                    <el-table-column
                      label="业务路由"
                      align="center"
                      show-overflow-tooltip
                      prop="channelRoute"
                    ></el-table-column>
                  </el-table>
                </div>
              </div>
            </el-col>

          </el-row>

          <el-row v-if="queryParams.riskType == '1'">
            <el-col :span="12">
              <div class="editForm">
                <p class="formTop">
                  <span class="formTit">重点保护光缆</span>
                </p>
                <div style="height: 37vh;">
                  <el-table
                    stripe
                    border
                    style="width: 100%"
                    height="100%"
                    :data="protect.fibers"
                    v-loading="loading">
                    <el-table-column label="序号" width="50" align="center" :formatter="snFormatter"></el-table-column>
                    <el-table-column
                      label="名称"
                      align="center"
                      show-overflow-tooltip
                      prop="name"
                    >
                      <template slot-scope="scope">
                        <a
                          class="linkColumn"
                          @click="getRouterLink(scope.row)"
                        >
                          {{scope.row.name}}
                        </a>
                      </template>
                    </el-table-column>
                    <el-table-column
                      label="电压等级"
                      align="center"
                      show-overflow-tooltip
                      prop="lineVoltageClass"
                    ></el-table-column>
                    <el-table-column
                      label="光缆类型"
                      align="center"
                      show-overflow-tooltip
                      prop="fiberType"
                    ></el-table-column>
                    <el-table-column
                      label="调度单位"
                      align="center"
                      show-overflow-tooltip
                      prop="authorityUnit"
                    ></el-table-column>
                  </el-table>
                </div>
              </div>
              <div class="editForm">
                <p class="formTop">
                  <span class="formTit">风险分析</span>
                </p>
                <el-input :rows="10" resize="none" type="textarea" v-model="riskAnalysis" v-loading="loading"></el-input>
              </div>

            </el-col>
            <el-col :span="12">
              <div class="editForm">
                <p class="formTop">
                  <span class="formTit">重点保护设备</span>
                </p>
                <div style="height: 37vh;">
                  <el-table
                    stripe
                    border
                    style="width: 100%"
                    height="100%"
                    :data="protect.equipment"
                    v-loading="loading">
                    <el-table-column label="序号" width="50" align="center" :formatter="snFormatter"></el-table-column>
                    <el-table-column
                      label="名称"
                      align="center"
                      show-overflow-tooltip
                      prop="name"
                    >
                      <template slot-scope="scope">
                        <a
                          class="linkColumn"
                          @click="getRouterLink2(scope.row)"
                        >
                          {{scope.row.name}}
                        </a>
                      </template>
                    </el-table-column>
                    <el-table-column
                      label="规格型号"
                      align="center"
                      show-overflow-tooltip
                      prop="devType"
                    ></el-table-column>
                    <el-table-column
                      label="调度单位"
                      align="center"
                      show-overflow-tooltip
                      prop="authorityUnit"
                    ></el-table-column>
                  </el-table>
                </div>
              </div>
              <div class="editForm">
                <p class="formTop">
                  <span class="formTit">对应措施</span>
                </p>
                <el-input :rows="10" resize="none" type="textarea" v-model="correspondingMeasures" v-loading="loading"></el-input>
              </div>
            </el-col>
          </el-row>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
import {
  getEquipmentOptionsApi,
  getAnalysisData,
  saveAnalysis,
  analysisExport,
  manyFiberAnalysis
} from "@/api/nMinusOne";
import {
  getFiberOptionsApi,
} from "@/api/lightPathInfoManage/setLightPath";
import {
  fileDownload,
  export_excel_file,
} from "@/api/zdwh/zdzywh";
export default {
  name: "NMinusOne",
  data() {
    return {
      queryParams: {
        riskType: '1',
        analysisType: '',
        selectData: '',
        fiber: '',
      },
      optionsLoading: false,
      fiberLoading: false,
      analysisTypes: [{
        value: '设备',
        label: '设备'
      }, {
        value: '光缆',
        label: '光缆'
      }],
      riskTypes: [{
        value: '1',
        label: 'N-1风险分析'
      }, {
        value: '2',
        label: '多光缆风险分析'
      }],
      selectDatas: [],
      fiberDatas: [],
      fiberList: [],
      loading: false,
      impactBusiness: [],
      involvingLines:[],
      protectId: '',
      protect:{},
      riskAnalysis:'',
      correspondingMeasures:'',
    };
  },
  watch: {
    //解决操作栏固定后表格错位问题
    'fiberList': {
      handler() {
        this.$nextTick(() => {
          this.$refs.multipleTable.doLayout();
        })
      },
      deep: true
    },
  },
  created() {
  },
  activated() {
  },
  methods: {
    handleSelectionChange(index,row) {
      this.protect = row
      this.protectId = this.protect.id

    },
    selceted(val) {
      this.queryParams.selectData = ''
      if (val === '光缆') {
        this.optionsLoading = true
        getFiberOptionsApi({
          snname: '',
          pageNum: 0,
          pageSize: 20,
        }).then((res) => {
          this.selectDatas = res.rows
          this.optionsLoading = false
        })
      }
      if (val === '设备') {
        getEquipmentOptionsApi({
          snname: '',
          pageNum: 0,
          pageSize: 20,
        }).then((res) => {
          this.selectDatas = res.rows
          this.optionsLoading = false
        })
      }
    },
    selceted2(val) {
      this.resetQuery()
      if (val === '2') {
        this.remoteMethod2()
      }
    },
    remoteMethod(query) {
      if (query !== '') {
        if (this.queryParams.analysisType === '光缆') {
          this.optionsLoading = true
          getFiberOptionsApi({
            snname: query,
            pageNum: 0,
            pageSize: 20,
          }).then((res) => {
            this.selectDatas = res.rows
            this.optionsLoading = false
          })
        }
        if (this.queryParams.analysisType === '设备') {
          this.optionsLoading = true
          getEquipmentOptionsApi({
            snname: query,
            pageNum: 0,
            pageSize: 20,
          }).then((res) => {
            this.selectDatas = res.rows
            this.optionsLoading = false
          })
        }
      }

    },
    remoteMethod2(query) {
      this.fiberLoading = true
      getFiberOptionsApi({
        snname: query,
        pageNum: 0,
        pageSize: 20,
      }).then((res) => {
        this.fiberDatas = res.rows
        this.fiberLoading = false
      })
    },
    change(val){
      let index = this.fiberList.findIndex(item => item.id == val)
      if (index == -1){
        let fiber = this.fiberDatas.find(item => item.id == val)
        // if (fiber.snname){
        //   fiber.name = fiber.snname
        // }
        this.fiberList.push(fiber)
      }else {
        this.$message({type: 'info', message: '光缆已存在'})
      }
    },
    deleteFiber(id){
      let index = this.fiberList.findIndex(item => item.id == id)
      if (index != -1){
        this.fiberList.splice(index,1)
      }
    },
    analysis() {
      // this.resetQuery2()
      if (this.queryParams.selectData && this.queryParams.analysisType) {
        this.loading = true
        getAnalysisData({
          type: this.queryParams.analysisType,
          id: this.queryParams.selectData
        }).then((res) => {
          this.impactBusiness = res.data.impactBusiness
          this.involvingLines = res.data.involvingLines || this.involvingLines.splice(0, this.involvingLines.length)
          if(this.involvingLines[0]){
            this.protect = this.involvingLines[0]
            this.protectId = this.protect.id
          }
          this.riskAnalysis = res.data.riskAnalysis
          this.correspondingMeasures = res.data.correspondingMeasures
          this.loading = false
        })
      } else {
        this.$message({
          type: 'info',
          message: '请选择分析数据'
        })
      }
    },
    analysis2() {
      // this.resetQuery2()
      if (this.fiberList.length != 0) {
        this.loading = true
        let ids = this.fiberList.map(item => item.id)
        manyFiberAnalysis(ids).then((res) => {
          this.involvingLines = res.data || this.involvingLines.splice(0, this.involvingLines.length)
          if(this.involvingLines[0]){
            this.protect = this.involvingLines[0]
            this.protectId = this.protect.id
          }
          this.loading = false
          this.$message({type: 'success',message: '分析完成'})
        })
      } else {
        this.$message({
          type: 'info',
          message: '请选择分析数据'
        })
      }
    },
    analysisExport() {
      if (this.queryParams.selectData) {
        this.$message({
          type: 'info',
          message: '正在导出...'
        })
        analysisExport({
          type: this.queryParams.analysisType,
          id: this.queryParams.selectData
        }).then((res) => {
          let lastInde = res.msg.lastIndexOf("/") + 1;
          let fileName = res.msg.substring(lastInde);
          fileDownload({filename: res.msg}).then(res => {
            export_excel_file(res, fileName);
          })
        })
      } else {
        this.$message({
          type: 'info',
          message: '请选择分析数据'
        })
      }
    },
    saveAnalysis() {
      if (this.queryParams.selectData && this.queryParams.analysisType) {
        saveAnalysis({
          resId: this.queryParams.selectData,
          risk: this.riskAnalysis,
          measure: this.correspondingMeasures,
        }).then((res) => {
          if (res.code === 200) {
            this.$message({
              type: 'success',
              message: '修改成功'
            })
          }

        })
      } else {
        this.$message({
          type: 'info',
          message: '请选择分析数据'
        })
      }

    },
    /** 重置按钮操作 */
    resetQuery() {
      this.queryParams.analysisType = ''
      this.queryParams.selectData = ''
      this.queryParams.fiber = ''
      this.involvingLines = []
      this.impactBusiness = []
      this.protect = {}
      this.riskAnalysis = ''
      this.correspondingMeasures = ''
      this.fiberList = []
    },
    /** 重置按钮操作 */
    resetQuery2() {
      this.involvingLines = []
      this.impactBusiness = []
      this.protect = {}
      this.riskAnalysis = ''
      this.correspondingMeasures = ''
      this.fiberList = []
    },
    /* 格式序列化列 */
    snFormatter: function (row, col, value, index) {
      return index + 1;
    },
    //光缆跳转
    getRouterLink(row){
      this.$router.push({
        path:'/fibersource/fiber',
        query:{
          search: JSON.stringify({ID:row.id}),
          refresh:true,
        }
      })
    },
    //设备跳转
    getRouterLink2(row){
      let path
      if(row.type == 'SDH'){
        path = '/transsource/zdwh/common/SG_TCDEV_TCNESDH_B'
      }else {
        path = '/transsource/zdwh/common/SG_TCDEV_TCNEOTN_B'
      }
      this.$router.push({
        path:path,
        query:{
          search: JSON.stringify({ID:row.id}),
          refresh:true,
        }
      })
    },
  }
};
</script>
<style scoped lang="scss">
#nMinusOne {
  width: 100%;
  height: calc(100% - 70px);
  background-color: #fff;
  overflow: auto;
  .editForm {
    width: 100%;
    padding-right: 20px;

    .formTop {
      width: 100%;
      height: 40px;
      line-height: 40px;

      .formTit {
        font-size: 16px;
        font-weight: bold;
        color: #079b9f;
      }
    }
  }
}
</style>
