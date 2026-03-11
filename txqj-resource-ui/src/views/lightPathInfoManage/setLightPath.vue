<template>
  <div class="setLightPath">
    <div style="width:100%;height:5%;color: #079b9f; font-size: 16px;text-align: center" v-if="activeStandbyShow">
      {{name}}
      <el-button
        size="mini"
        v-if="activeStandbyShow"
        type="primary"
        style="float: right"
        @click="bindGLBox.show = true"
      >{{'绑定' + activeStandbyName + '光路'}}</el-button>
      <el-button
        size="mini"
        v-if="activeStandbyShow"
        type="primary"
        style="float: right;margin-right: 10px"
        @click="correspondsMsp"
      >{{'切换' + activeStandbyName + '光路'}}</el-button>
    </div>
    <el-table
      v-if="llRow.length ==0"
      v-loading="loading"
      :data="tableData"
      :height="activeStandbyShow ? '45%' : '50%'"
      border=""
      style="width: 100%;"
    >
      <el-table-column
        label="序号"
        width="80"
        align="center"
        type="index">
      </el-table-column>
      <el-table-column
        label="光缆名称"
        min-width="300"
        align="center"
      >
        <template slot-scope="scope">
          <el-select
            filterable
            remote
            style="width: 90%"
            :loading="optionsLoading"
            v-loadmore="loadmore"
            :remote-method="(val) => remoteMethod(val, scope.$index)"
            @focus="selectIndex = scope.$index, remoteMethod('', scope.$index)"
            v-model="scope.row.fiberId"
            @change="selceted($event, scope.$index)"
          >
            <el-option
              v-for="item in scope.row.fiberOptions.list"
              :key="item.id"
              :label="item.snname"
              :value="item.id"
              :disabled="item.disabled"
              :title="item.snname"
            >
            </el-option>
          </el-select>
          <i class="el-icon-document-copy"
             style="margin-left: 5px; cursor: pointer;"
             @click="copyFocusedBoxContent(scope.row)"
          ></i>
        </template>
      </el-table-column>
      <el-table-column
        prop="aResName"
        label="起点站点"
        width="150"
        align="center">
      </el-table-column>
      <el-table-column
        prop="zResName"
        label="终点站点"
        width="150"
        align="center">
      </el-table-column>
      <el-table-column
        label="纤芯"
        width="300"
        align="center"
        prop="line"
      >
        <template slot-scope="scope">
          <div style=" height: 82px">
            <el-select
              v-model="scope.row.line[0].lineId"
              placeholder="请选择"
              filterable
              clearable
              @change="lineSelceted($event, scope.$index, 0)"
              style="margin: 0 10px 10px 0;">
              <el-option
                v-for="item in scope.row.lineOptions"
                :label="item.sn"
                :value="item.id"
                :key="item.id"
                :disabled="item.disabled == '1'"
              ></el-option>
            </el-select>收
            <el-select
              v-model="scope.row.line[1].lineId"
              placeholder="请选择"
              filterable
              @change="lineSelceted($event, scope.$index, 1)"
              clearable
              style="margin: 0 10px 0 0;">
              <el-option
                v-for="item in scope.row.lineOptions"
                :label="item.sn"
                :value="item.id"
                :key="item.id"
                :disabled="item.disabled == '1'"
              ></el-option>
            </el-select>发
          </div>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        width="100"
        align="center"
      >
        <template slot-scope="scope">
          <el-button size="mini" type="danger" @click="delTable(scope.$index)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-table
      v-else
      stripe
      border
      :height="activeStandbyShow ? '45%' : '50%'"
      style="width: 100%;"
      :data="llRow"
    >
      <el-table-column
        label="序号"
        width="80"
        align="center"
        :formatter="snFormatter"
      ></el-table-column>
      <el-table-column
        v-if="llRow[0]"
        v-for="item in bindLLBox.tableColData"
        :label="item.name"
        :min-width="getColSize(llRow[0][item.prop], item.name)"
        align="center"
        show-overflow-tooltip
        :prop="item.prop"
      ></el-table-column>
    </el-table>
    <div style="color: #ff003b; position: absolute; left: 25%; top: 50%;" v-if="llRow.length ==0">
      提示：若光路路由的光缆纤芯错误，可以删除全部光缆纤芯配线数据，保存后重新打开页面，即可获取TMS2.0配线数据。
    </div>
    <el-button
      icon="el-icon-plus"
      type="primary"
      style="position: absolute; right: 310px; top: 50%; background: #079b9f !important;"
      @click="bindLLBox.show = true"
    >承载OTN通道</el-button>
    <el-button
      icon="el-icon-plus"
      type="primary"
      style="position: absolute; right: 150px; top: 50%; background: #079b9f !important;"
      @click="addJumpers"
    >添加站内跳纤</el-button>
    <el-button
      icon="el-icon-plus"
      type="primary"
      style="position: absolute; right: 20px; top: 50%; background: #079b9f !important;"
      @click="addTable"
    >新增光缆</el-button>
    <div style="height: 50%; border-top: 1px solid #999;">
      <iframe
        v-if="llRow.length == 0"
        ref="iframe"
        width="100%"
        id="iframe"
        name="iframe"
        style="border: none"
        height="100%"
        src="/stationWh/html/lightPath.html"
      ></iframe>
      <iframe
        v-else
        ref="iframe1"
        width="100%"
        id="iframe1"
        name="iframe1"
        style="border: none"
        height="100%"
        src="/stationWh/serve/channel.html"
      ></iframe>
    </div>
    <!--绑定通道链路-->
    <el-dialog
      v-dialogDrag
      custom-class="curdBox"
      title="绑定链路"
      :visible.sync="bindLLBox.show"
      width="100%"
      :modal="false"
      :close-on-click-modal="false"
    >
      <div style="height: 58vh" class="commonWh">
        <el-form ref="form" label-width="100px" :inline="true">
          <el-form-item label="名称 : ">
            <el-input size="mini" v-model="bindLLBox.searchForm.name"></el-input>
          </el-form-item>
          <el-form-item label="起始设备 : " prop="ares">
            <el-select
              size="mini"
              placeholder="请选择"
              clearable
              filterable
              remote
              :loading="optionsALoading"
              :remote-method="(val) => remoteMethodLL(val, 'optionsA')"
              v-model="bindLLBox.searchForm.aResId"
            >
              <el-option
                v-for="item in optionsA.list"
                :key="item.code"
                :label="item.name"
                :value="item.code"
                :disabled="item.disabled">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="终止设备" prop="zres">
            <el-select
              size="mini"
              placeholder="请选择"
              clearable
              filterable
              remote
              :loading="optionsZLoading"
              :remote-method="(val) => remoteMethodLL(val, 'optionsZ')"
              v-model="bindLLBox.searchForm.zResId"
            >
              <el-option
                v-for="item in optionsZ.list"
                :key="item.code"
                :label="item.name"
                :value="item.code"
                :disabled="item.disabled">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item style="float: right;" label-width="0">
            <el-button size="mini" type="primary" @click="getbindLl()">查询</el-button>
            <el-button size="mini" @click="bindLLBox.searchForm = {}">重置</el-button>
          </el-form-item>
        </el-form>
        <el-table
          ref="multipleTable2"
          stripe
          border
          height="calc(100% - 60px)"
          style="width: 100%;"
          :data="bindLLBox.tableData"
          v-loading="bindLLBox.tableLoadIng"
        >
          <el-table-column
            label="序号"
            width="80"
            align="center"
            :formatter="snFormatter"
          ></el-table-column>
          <el-table-column
            v-if="bindLLBox.tableData[0]"
            v-for="item in bindLLBox.tableColData"
            :label="item.name"
            :min-width="getColSize(bindLLBox.tableData[0][item.prop], item.name)"
            align="center"
            show-overflow-tooltip
            :prop="item.prop"
          ></el-table-column>
          <el-table-column
            label="操作"
            fixed="right"
            width="80"
            align="center"
          >
            <template slot-scope="scope">
              <el-button size="mini" type="primary" @click="bindLLCommit(scope.row)">选择</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          :total="bindLLBox.total"
          :page.sync="bindLLBox.pageNum"
          :limit.sync="bindLLBox.pageSize"
          @pagination="getbindLl"
        />
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="bindLLBox.show = false">取 消</el-button>
      </div>
    </el-dialog>
    <el-dialog
      v-dialogDrag
      custom-class="curdBox"
      :title="'绑定' + activeStandbyName + '光路'"
      :visible.sync="bindGLBox.show"
      width="100%"
      :modal="false"
      :close-on-click-modal="false"
    >
      <div style="height: 58vh" class="commonWh">
        <el-form ref="form" label-width="100px" :inline="true">
          <el-form-item label="名称 : " prop="name">
            <el-input size="mini" v-model="bindGLBox.searchForm.name" placeholder="请输入名称名称"></el-input>
          </el-form-item>
          <el-form-item label="A端设备 : " prop="A_RES_ID">
            <el-select
              size="mini"
              placeholder="请选择"
              clearable
              filterable
              remote
              :loading="A_RES_ID_Loading"
              :remote-method="(val) => remoteMethod2(val, 'A_RES_ID')"
              v-model="bindGLBox.searchForm.A_RES_ID"
            >
              <el-option
                v-for="item in A_RES_ID.list"
                :key="item.code"
                :label="item.name"
                :value="item.code"
                :disabled="item.disabled">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="Z端设备" prop="Z_RES_ID">
            <el-select
              size="mini"
              placeholder="请选择"
              clearable
              filterable
              remote
              :loading="Z_RES_ID_Loading"
              :remote-method="(val) => remoteMethod2(val, 'Z_RES_ID')"
              v-model="bindGLBox.searchForm.Z_RES_ID"
            >
              <el-option
                v-for="item in Z_RES_ID.list"
                :key="item.code"
                :label="item.name"
                :value="item.code"
                :disabled="item.disabled">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item style="float: right;margin-left: 0!important;">
            <el-button type="primary" size="mini" @click="getTableData()">查询</el-button>
            <el-button size="mini" @click="resetTableQuery">重置</el-button>
          </el-form-item>
        </el-form>
        <el-table
          stripe
          border
          style="width: 100%"
          height="calc(100% - 60px)"
          :data="bindGLBox.tableData">
          <el-table-column label="序号" width="50" align="center" :formatter="snFormatter2"></el-table-column>
          <el-table-column
            label="光路名称"
            align="center"
            show-overflow-tooltip
            prop="name"
          ></el-table-column>
          <el-table-column
            label="A端设备"
            align="center"
            width="150"
            show-overflow-tooltip
            prop="aResId">
          </el-table-column>
          <el-table-column
            label="A端端口"
            align="center"
            width="150"
            show-overflow-tooltip
            prop="aPort">
          </el-table-column>
          <el-table-column
            label="Z端设备"
            align="center"
            width="150"
            show-overflow-tooltip
            prop="zResId">
          </el-table-column>
          <el-table-column
            label="Z端端口"
            align="center"
            width="150"
            show-overflow-tooltip
            prop="zPort">
          </el-table-column>
          <el-table-column
            label="光路路由"
            align="center"
            show-overflow-tooltip
            prop="opticalRouting"
          ></el-table-column>
          <el-table-column
            label="操作"
            fixed="right"
            width="100"
            align="center">
            <template slot-scope="scope">
              <el-button @click="saveMsp(scope.row.id)">选择</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          :total="bindGLBox.total"
          :page.sync="bindGLBox.pageNum"
          :limit.sync="bindGLBox.pageSize"
          @pagination="getTableData"
        />
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="bindGLBox.show = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { commitRouterApi,saveMsp } from "@/api/lightPathInfoManage/index";
import {
  getFiberOptionsApi,
  getFiberLineOptionsApi,
  getLightPathRouterApi,
  getLightPathAZSite,
} from "@/api/lightPathInfoManage/setLightPath";
import {getbindLlApi} from "@/api/serviceInformation";
import {getColSize} from "@/utils/getColSize"
import{getModelId, tableDropDownBox} from "@/api/zdwh/zdzywh";
import {getChannelTopoApi} from "@/api/serviceInformation/channel";
import {
  queryRouterByCondition
} from "@/api/RouterIdenticalFiber";
export default {
  name: "setLightPath",
  props: {
    parId: String
  },
  data () {
    return {
      tableData: [],
      optionsLoading: true,
      getOptionsLoading: false,
      searchOptionStr: '',
      siteId: '',
      selectIndex: undefined,
      isJumpFiber:false,
      loading: true,
      prompts:true,
      forTheFirstTime:false,
      glInfo: {},
      llRow: [],
      bindLLBox: {
        show: false,
        searchForm: {},
        pageSize: 15,
        pageNum: 1,
        total: 0,
        tableData: [],
        tableColData: [
          {name: '名称', prop: 'name'},
          {name: '起始设备', prop: 'aResId'},
          {name: '起始端口', prop: 'aPort'},
          {name: '终止设备', prop: 'zResId'},
          {name: '终止端口', prop: 'zPort'},
          {name: '链路类型', prop: 'channelTypeName'},
        ],
        tableLoadIng: false,
      },
      optionsA:{},
      optionsZ:{},
      optionsALoading: true,
      optionsZLoading: true,


      activeStandbyShow: false,
      isActiveStandby: '',
      activeStandbyName: '',
      mspOpticalroadId: '',
      name: '',


      bindGLBox:{
        searchForm: {
          name: '',
          A_RES_ID: '',
          Z_RES_ID: '',
        },
        show: false,
        pageSize: 15,
        pageNum: 1,
        total: 0,
        tableData: [],
      },
      A_RES_ID: {},
      Z_RES_ID: {},
      A_RES_ID_Loading: true,
      Z_RES_ID_Loading: true,
    }
  },
  watch: {
    tableData: {
      handler () {
        this.$nextTick(() => {
          window.setTimeout(() => {
            this.$nextTick(() => {
              this.reDrawTopo()
            })
          },1000)
        })
      },
      deep: true
    },
    isJumpFiber(newVal){
      if(newVal){
        this.$nextTick(() => {
          window.setTimeout(() => {
            this.$nextTick(() => {
              this.reDrawTopo()
            })
          },1000)
        })
      }
    }
  },
  methods: {
    copyFocusedBoxContent(data){
      if(data){
        this.$copyText(data.fiberName).then((e) => {
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
      }


    },
    selceted (val, index) {
      let data = this.tableData[index].fiberOptions.list.find(item => {
        return item.id == val
      })
      this.tableData[index].fiberId = data.id
      this.tableData[index].fiberName = data.snname
      this.tableData[index].aResName = data.aResName
      this.tableData[index].zResName = data.zResName
      this.tableData[index].aResid = data.aResid
      this.tableData[index].zResid = data.zResid

      this.tableData[index].line = [{}, {}]
      this.tableData[index].lineOptions = []
      getFiberLineOptionsApi({
        parFiber: val,
        routerId: this.parId
      }).then(res => {
        this.tableData[index].lineOptions = res.rows
      })
      this.tableData.splice(index,1,{
        ...this.tableData[index],
      })
    },
    lineSelceted (val, indexT, index) {
      if(!val){
        this.tableData[indexT].line[index].lineName = ''
        this.tableData[indexT].line[index].lineId = ''
        this.tableData[indexT].line[index].sn = ''
        return
      }
      let data = this.tableData[indexT].lineOptions.find(item => {
        return item.id == val
      })
      let sn = '('+this.tableData[indexT].aResName + ' ~ ' + this.tableData[indexT].zResName+')'
      if(data.sn < 10){
        sn += '光缆纤芯F00' + data.sn
      }else {
        sn += '光缆纤芯F0' + data.sn
      }
      this.tableData[indexT].line[index].lineName = sn
      this.tableData[indexT].line[index].lineId = data.id
      this.tableData[indexT].line[index].sn = data.sn
    },
    remoteMethod (val, index) {
      this.searchOptionStr = val
      getFiberOptionsApi({
        snname: this.searchOptionStr,
        pageNum: 0,
        pageSize: 20,
      }).then((res) => {
        this.tableData[index].fiberOptions.list = res.rows
        this.tableData[index].fiberOptions.total = res.total
        this.optionsLoading = true
        this.$nextTick(() => {
          this.optionsLoading = false
        })
      })
    },
    loadmore () {
      let index = this.selectIndex
      if (this.getOptionsLoading) return
      this.getOptionsLoading = true
      if (this.tableData[index].fiberOptions.total > this.tableData[index].fiberOptions.list.length) {
        let pageNum = parseInt(this.tableData[index].fiberOptions.list.length / 20) * 20
        if (!this.tableData[index].fiberOptions.list[this.tableData[index].fiberOptions.list.length - 1].disabled) {
          this.tableData[index].fiberOptions.list.push({
            id: 'aaabbbccc',
            name: '加载中',
            disabled: true
          })
        }
        getFiberOptionsApi({
          snname: this.searchOptionStr,
          pageNum: pageNum,
          pageSize: 20,
        }).then((res) => {
          this.optionsLoading = true
          this.tableData[index].fiberOptions.list.pop()
          if (res.rows.length > 0) {
            this.tableData[index].fiberOptions.list.push.apply(
              this.tableData[index].fiberOptions.list,
              res.rows
            )
          } else {
            if (!this.tableData[index].fiberOptions.list[this.tableData[index].fiberOptions.list.length - 1].disabled) {
              this.tableData[index].fiberOptions.list.push({
                id: 'aaabbbccc',
                name: '没有更多了',
                disabled: true
              })
            }
          }
          this.$nextTick(() => {
            this.optionsLoading = false
            this.getOptionsLoading = false
          })
        })
      }
    },
    addTable () {
      this.tableData.push({
        fiberId: '',
        fiberName: '',
        aResName: '',
        zResName: '',
        line: [{}, {}],
        lineOptions: [],
        fiberOptions: {
          list: [],
          total: 0,
        },
      })
      this.isJumpFiber = false;
      this.llRow = [];
    },
    addJumpers(){
      this.tableData.splice(0,this.tableData.length)
      this.isJumpFiber = true;
      this.llRow = [];
    },
    delTable (index) {
      this.tableData.splice(index, 1)
    },
    async getTable () {
      this.loading = true
      let tableData
      await getLightPathRouterApi({routerId: this.parId}).then(res => {
        tableData = res.data.tableData
        this.isJumpFiber = res.data.isJumpFiber == '1'
        this.llRow = res.data.llRow
        if (this.llRow.length){
          this.getTopoData()
        }
        if(res.data.isActiveStandby == '1' || res.data.isActiveStandby == '0'){
          this.activeStandbyShow = true
          this.isActiveStandby = res.data.isActiveStandby
          this.activeStandbyName = res.data.isActiveStandby == '1' ? '备用' : '主用'
          this.mspOpticalroadId = res.data.mspOpticalroadId
          this.name = res.data.name
        }
      })

      for (let i = 0; i < tableData.length; i++) {
        tableData[i].lineOptions = []
        tableData[i].fiberOptions = {
          list: [],
          total: 0,
        }
        if(tableData[i].fiberId){
          await getFiberLineOptionsApi({
            parFiber: tableData[i].fiberId,
            routerId: this.parId
          }).then(res => {
            tableData[i].lineOptions = res.rows
          })
        }

      }
      this.tableData = tableData
      for (let i = 0; i < this.tableData.length; i++) {
        await getFiberOptionsApi({
          id: tableData[i].fiberId,
          pageNum: 0,
          pageSize: 20,
        }).then(res => {
          this.tableData[i].fiberOptions.list = res.rows
          this.tableData[i].fiberOptions.total = res.total
          this.optionsLoading = true
          this.$nextTick(() => {
            this.optionsLoading = false
          })
        })
      }
      this.loading = false
      await getLightPathAZSite({id:this.parId}).then(res=>{
        if(res.data){
          this.glInfo = res.data
        }else {
          this.$message({type: 'info',message: '请确认光缆A端端口或Z端端口是否选择'})
        }
      })
    },
    tableCommit () {
      for (let i = 0; i < this.tableData.length; i++) {
        if(!this.tableData[i].fiberId){
          this.$message({
            type: 'info',
            message: '请填写完整'
          })
          return
        }
        this.tableData[i].sn = i
        delete this.tableData[i].fiberOptions
        delete this.tableData[i].lineOptions
        for (let k = 0; k < this.tableData[i].line.length; k++) {
          this.tableData[i].line[k].hierarchy = k
        }
      }
      let data = {
        routerId:this.parId,
        tableData:this.tableData,
        isJumpFiber:this.isJumpFiber,
        llId:this.llRow.length != 0 ? this.llRow[0].id : ''
      }
      return data
    },
    async reDrawTopo () {
      if (this.llRow.length) return;
      if (!this.glInfo.ID) return;
      //A端站点
      let aZD = this.glInfo.aSiteId
      //Z端站点
      let zZD = this.glInfo.zSiteId
      //临时记录站点
      let zd = aZD
      //a端设备名称
      let aSBName = this.glInfo.aSiteName
      //Z端设备名称
      let zSBName = this.glInfo.zSiteName
      //a端端口名称
      let aPortName = this.glInfo.aPortName
      //Z端端口名称
      let zPortName = this.glInfo.zPortName

      //站内跳纤
      if(this.isJumpFiber){
        this.$refs.iframe.contentWindow.setJumpFiberTopo(aZD+'A',aSBName,aPortName,zZD+'Z',zSBName,zPortName)
        return
      }

      //默认路由
      let data = {
        aSite:aZD,
        zSite:zZD,
        aNodeName:aSBName,
        zNodeName:zSBName,
        aPortName:aPortName,
        zPortName:zPortName,
      }
      if (!this.tableData[0]) {
        this.$refs.iframe.contentWindow.reSetTopo([],[],[], data)
        return
      }


      let group = []
      let node = []
      let line = []
      let linshi = JSON.parse(JSON.stringify(this.tableData))
      let tableData = []
      let frequency = this.tableData.length;
      let id = 1;
      for (let i = 0; i < frequency; i++) {
        for (let i = 0; i < linshi.length; i++) {
          let item = linshi[i]
          if(aZD == item.aResid ){
            tableData.push(JSON.parse(JSON.stringify(item)))
            linshi.splice(i, 1);
            zd = item.zResid
            group.push({
              id: item.aResid,
              name: item.aResName,
            })
            group.push({
              id: item.zResid,
              name: item.zResName,
            })
            for (let k = 0; k < item.line.length; k++) {
              let itemK = item.line[k]
              if(!itemK.lineId){
                itemK.lineId = '单芯:'+ id
                id ++;
              }
              line.push({
                id: itemK.lineId,
                name: itemK.lineName
              })
              node.push({
                id: itemK.lineId + 'A',
                parId: item.aResid
              })
              node.push({
                id: itemK.lineId + 'Z',
                parId: item.zResid
              })
            }
          }
          if(aZD == item.zResid ){
            tableData.push(JSON.parse(JSON.stringify(item)))
            linshi.splice(i, 1);
            zd = item.aResid
            group.push({
              id: item.zResid,
              name: item.zResName,
            })
            group.push({
              id: item.aResid,
              name: item.aResName,
            })
            for (let k = 0; k < item.line.length; k++) {
              let itemK = item.line[k]
              if(!itemK.lineId){
                itemK.lineId = '单芯:'+ id
                id ++;
              }
              line.push({
                id: itemK.lineId,
                name: itemK.lineName
              })
              node.push({
                id: itemK.lineId + 'A',
                parId: item.zResid
              })
              node.push({
                id: itemK.lineId + 'Z',
                parId: item.aResid
              })
            }
          }
          aZD = zd
        }
      }
      if(zZD != zd || linshi.length != 0){
        let flag = false
        for (let i = 0; i < this.tableData.length; i++) {
          let item = this.tableData[i]
          if (item.aResName == this.glInfo.aZDName && item.aResid != this.glInfo.aSiteId) flag = true
          if (item.aResName == this.glInfo.zZDName && item.aResid != this.glInfo.zSiteId) flag = true
          if (item.zResName == this.glInfo.aZDName && item.zResid != this.glInfo.aSiteId) flag = true
          if (item.zResName == this.glInfo.zZDName && item.zResid != this.glInfo.zSiteId) flag = true
        }

        if (flag){
          this.$message({ type: 'info', message: '存在起始终止设备的站点和光缆站点不一致'})
        }

        if(this.prompts){
          this.$message({ type: 'info', message: '请确认光缆是否选择正确'})
        }
        this.prompts = false
        this.$refs.iframe.contentWindow.cleanUp();
        return;
      }
      if(this.forTheFirstTime){
        this.tableData = tableData
        this.forTheFirstTime = false

        //打开页面保存路由
        let data = JSON.parse(JSON.stringify(tableData))
        for (let i = 0; i < data.length; i++) {
          data[i].sn = i
          delete data[i].fiberOptions
          delete data[i].lineOptions
          for (let k = 0; k < data[i].line.length; k++) {
            data[i].line[k].hierarchy = k
          }
        }
        commitRouterApi({
          routerId: this.parId,
          tableData: data,
          isJumpFiber: this.isJumpFiber,
          llId: this.llRow.length != 0 ? this.llRow[0].id : ''
        })
      }
      this.$refs.iframe.contentWindow.reSetTopo(group, node, line, data)
    },
    getbindLl(){
      this.bindLLBox.tableLoadIng = true
      let params = this.bindLLBox.searchForm
      params.channelType = '1400'
      params.pageSize = this.bindLLBox.pageSize
      params.pageNum = this.bindLLBox.pageNum
      getbindLlApi(params).then(res => {
        this.bindLLBox.tableLoadIng = false
        this.bindLLBox.tableData = res.rows
        this.bindLLBox.total = res.total
      })
    },
    //绑定链路提交
    bindLLCommit(row) {
      this.tableData.splice(0,this.tableData.length)
      this.isJumpFiber = false
      this.llRow = [row]
      this.getTopoData()
      this.bindLLBox.show = false
    },
    getTopoData () {
      getChannelTopoApi({channelId: this.llRow[0].id}).then(res => {
        this.$nextTick(() => {
          setTimeout(()=>{
            this.$refs.iframe1.contentWindow.reSetTopo(
              res.data.groupsData,
              res.data.nodesData,
              res.data.optRoadData,
              res.data.aPortId,
              res.data.zPortId,
            )
          },500)
        })
      })
    },
    remoteMethodLL (val, data) {
      this.searchOptionStr = val
      tableDropDownBox({
        modelId: this.modelObj.modelId,
        modeVersion: this.modelObj.modelVersion,
        attribute: 'A_RES_ID',
        name: this.searchOptionStr,
        pageNum: 0,
        pageSize: 20,
      }).then((res) => {
        this[data + 'Loading'] = true
        this[data].list = res.rows
        this[data].total = res.total
        this.$nextTick(() => {
          this[data + 'Loading'] = false
        })
      })
    },
    getOption (data) {
      tableDropDownBox({
        modelId: this.modelObj.modelId,
        modeVersion: this.modelObj.modelVersion,
        attribute: 'A_RES_ID',
        pageNum: 0,
        pageSize: 20,
      }).then((res) => {
        if (res.total > 0) {
          this[data].list = res.rows
          this[data].total = res.total
          this.$nextTick(() => {
            this[data + 'Loading'] = false
          })
        }
      })
    },
    correspondsMsp(){
      if (this.mspOpticalroadId){
        this.parId = this.mspOpticalroadId
        this.activeStandbyShow = false
        this.isActiveStandby = ''
        this.activeStandbyName = ''
        this.mspOpticalroadId = ''
        this.name = ''
        this.prompts = false
        this.forTheFirstTime = false
        this.getTable()
      }else {
        this.$message({ type: 'info', message: '请绑定对应' + this.activeStandbyName + '光路'})
        this.bindGLBox.show = true
      }

    },
    saveMsp(id){
      this.mspOpticalroadId = id
      saveMsp({
        routerId: this.parId,
        isActiveStandby: this.isActiveStandby,
        mspOpticalroadId: this.mspOpticalroadId,
      }).then(res => {
        this.bindGLBox.show = false
        this.correspondsMsp()
      })
    },
    getTableData(){
      queryRouterByCondition({
        name: this.bindGLBox.searchForm.name,
        aResId: this.bindGLBox.searchForm.A_RES_ID,
        zResId: this.bindGLBox.searchForm.Z_RES_ID,
        pageNum: this.bindGLBox.pageNum,
        pageSize: this.bindGLBox.pageSize,
      }).then((res) => {
        this.bindGLBox.tableData = res.rows
        this.bindGLBox.total = res.total
      })
    },
    remoteMethod2(val, data) {
      this.searchOptionStr = val
      tableDropDownBox({
        modelId: this.modelObj2.modelId,
        modeVersion: this.modelObj2.modelVersion,
        attribute: data,
        name: this.searchOptionStr,
        pageNum: 0,
        pageSize: 20,
      }).then((res) => {
        this[data + '_Loading'] = true
        this[data].list = res.rows
        this[data].total = res.total
        this.$nextTick(() => {
          this[data + '_Loading'] = false
        })
      })
    },
    resetTableQuery() {
      this.bindGLBox.searchForm = {
        name: '',
        A_RES_ID: '',
        Z_RES_ID: '',
      }
    },
    getOption2(data) {
      tableDropDownBox({
        modelId: this.modelObj2.modelId,
        modeVersion: this.modelObj2.modelVersion,
        attribute: data,
        pageNum: 0,
        pageSize: 20,
      }).then((res) => {
        if (res.total > 0) {
          this[data].list = res.rows
          this[data].total = res.total
          this.$nextTick(() => {
            this[data + '_Loading'] = false
          })
        }
      })
    },

    /* 格式序列化列 */
    snFormatter: function (row, col, value, index) {
      return this.bindLLBox.pageSize * (this.bindLLBox.pageNum - 1) + index + 1;
    },
    /* 格式序列化列 */
    snFormatter2: function (row, col, value, index) {
      return this.bindGLBox.pageSize * (this.bindLLBox.pageNum - 1) + index + 1;
    },
    getColSize(val, titVal) {
      return getColSize(val, titVal)
    },
    getCommonData() {
      //获取表基本信息
      getModelId({tableName: 'SG_TCCON_TCCHANNEL_B'}).then(res => {
        this.modelObj = res.data
        this.getbindLl()
        this.getOption('optionsA')
        this.getOption('optionsZ')
      })
      getModelId({tableName: 'SG_TCCON_TCOPTICALROAD_B'}).then(res => {
        this.modelObj2 = res.data
        this.getTableData()
        this.getOption2('A_RES_ID')
        this.getOption2('Z_RES_ID')
      })
    },
  },
  mounted () {
    this.getTable()
    this.getCommonData()
  }
}
</script>

<style lang="scss" scoped>
  .setLightPath{
  }
</style>
