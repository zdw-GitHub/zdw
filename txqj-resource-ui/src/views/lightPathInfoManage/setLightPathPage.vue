<template>
  <div class="setLightPath">
    <el-table
      v-loading="loading"
      :data="tableData"
      height="50%"
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
              style="margin-bottom: 10px">
              <el-option
                v-for="item in scope.row.lineOptions"
                :label="item.sn"
                :value="item.id"
                :key="item.id"
                :disabled="item.disabled == '1'"
              ></el-option>
            </el-select>
            <el-select
              v-model="scope.row.line[1].lineId"
              placeholder="请选择"
              filterable
              @change="lineSelceted($event, scope.$index, 1)"
              clearable>
              <el-option
                v-for="item in scope.row.lineOptions"
                :label="item.sn"
                :value="item.id"
                :key="item.id"
                :disabled="item.disabled == '1'"
              ></el-option>
            </el-select>
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
    <el-button
      icon="el-icon-plus"
      type="primary"
      style="position: absolute; right: 200px; top: 51%; background: #079b9f !important;"
      v-if="parId"
      @click="addJumpers"
    >添加站内跳纤</el-button>
    <el-button
      icon="el-icon-plus"
      type="primary"
      style="position: absolute; right: 70px; top: 51%; background: #079b9f !important;"
      v-if="parId"
      @click="addTable"
    >新增光缆</el-button>
    <div style="height: 50%; border-top: 1px solid #999;">
      <iframe
        ref="iframe"
        width="100%"
        id="iframe"
        name="iframe"
        style="border: none"
        height="100%"
        src="/stationWh/html/lightPath.html"
      ></iframe>
    </div>
    <el-button
      type="primary"
      style="position: absolute; right: 70px; top: 90%; background: #079b9f !important;"
      v-if="parId"
      :loading="tableCommitLoading"
      @click="tableCommit"
    >保存</el-button>
  </div>
</template>

<script>
import {
  getFiberOptionsApi,
  getFiberLineOptionsApi,
  getLightPathRouterApi,
  getLightPathAZSite,

} from "@/api/lightPathInfoManage/setLightPath";
import {
  commitRouterApi
} from "@/api/lightPathInfoManage/index";
export default {
  name: "setLightPath",
  data () {
    return {
      parId: '',
      tableData: [],
      topoData: {},
      optionsLoading: true,
      getOptionsLoading: false,
      searchOptionStr: '',
      siteId: '',
      selectIndex: undefined,
      isJumpFiber:false,
      loading: false,
      prompts:true,
      tableCommitLoading: false,
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
    },
    addJumpers(){
      this.tableData.splice(0,this.tableData.length)
      this.isJumpFiber = true;
    },
    delTable (index) {
      this.tableData.splice(index, 1)
    },
    async getTable () {
      this.parId = this.$route.query.id
      if (!this.parId){
        this.$message({
          type: 'info',
          message: 'id不能为空'
        })
        return
      }
      this.loading = true
      let tableData
      await getLightPathRouterApi({routerId: this.parId}).then(res => {
        this.topoData = res.data
        tableData = res.data.tableData
        this.isJumpFiber = res.data.isJumpFiber == '1'
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
        this.remoteMethod(tableData[i].fiberName, i)
      }
      this.loading = false
    },
    tableCommit () {
      this.tableCommitLoading = true
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
      commitRouterApi({
        routerId: this.parId,
        tableData: this.tableData,
        isJumpFiber: this.isJumpFiber
      }).then(res => {
        this.$modal.msgSuccess("保存成功");
        this.tableCommitLoading = false
      }).catch(e =>{
        this.$modal.msgSuccess("保存失败");
        this.tableCommitLoading = false
      })
    },
    async reDrawTopo () {
      //A端站点
      let aZD = ''
      //Z端站点
      let zZD = ''
      //临时记录站点
      let zd = ''
      //a端设备名称
      let aSBName = ''
      //Z端设备名称
      let zSBName = ''
      //a端端口名称
      let aPortName = ''
      //Z端端口名称
      let zPortName = ''
      await getLightPathAZSite({id:this.parId}).then(res=>{
        if(res.data){
          aZD = res.data.aSiteId;
          zZD = res.data.zSiteId;
          zd  = res.data.aSiteId;
          aSBName = res.data.aSiteName;
          zSBName = res.data.zSiteName;
          aPortName = res.data.aPortName;
          zPortName = res.data.zPortName;
        }else {
          this.$message({
            type: 'info',
            message: '请确认光缆A端端口或Z端端口是否选择'
          })
          this.$refs.iframe.contentWindow.cleanUp();
          return;
        }
      })
      let data = {
        aSite:aZD,
        zSite:zZD,
        aNodeName:aSBName,
        zNodeName:zSBName,
        aPortName:aPortName,
        zPortName:zPortName,
      }
      if(this.isJumpFiber){
        this.$refs.iframe.contentWindow.setJumpFiberTopo(aZD+'A',aSBName,aPortName,zZD+'Z',zSBName,zPortName)
        return
      }
      if (!this.topoData.tableData[0]) {
        this.$refs.iframe.contentWindow.reSetTopo([],[],[], data)
        return
      }
      let group = []
      let node = []
      let line = []
      let linshi = JSON.parse(JSON.stringify(this.topoData.tableData))
      for (let i = 0; i < this.topoData.tableData.length; i++) {
        for (let i = 0; i < linshi.length; i++) {
          let item = linshi[i]
          await getFiberOptionsApi({
            id: item.fiberId,
            pageNum: 0,
            pageSize: 20,
          }).then(res => {
            if(aZD == res.rows[0].aResid ){
              linshi.splice(i, 1);
              zd = res.rows[0].zResid
              group.push({
                id: res.rows[0].aResid,
                name: res.rows[0].aResName,
              })
              group.push({
                id: res.rows[0].zResid,
                name: res.rows[0].zResName,
              })
              for (let k = 0; k < item.line.length; k++) {
                let itemK = item.line[k]
                if(!itemK.lineId){
                  itemK.lineId = '单芯:'+new Date().getTime()
                }
                line.push({
                  id: itemK.lineId,
                  name: itemK.lineName
                })
                node.push({
                  id: itemK.lineId + 'A',
                  parId: res.rows[0].aResid
                })
                node.push({
                  id: itemK.lineId + 'Z',
                  parId: res.rows[0].zResid
                })
              }
            }
            if(aZD == res.rows[0].zResid ){
              linshi.splice(i, 1);
              zd = res.rows[0].aResid
              group.push({
                id: res.rows[0].zResid,
                name: res.rows[0].zResName,
              })
              group.push({
                id: res.rows[0].aResid,
                name: res.rows[0].aResName,
              })
              for (let k = 0; k < item.line.length; k++) {
                let itemK = item.line[k]
                if(!itemK.lineId){
                  itemK.lineId = '单芯:'+new Date().getTime()
                }
                line.push({
                  id: itemK.lineId,
                  name: itemK.lineName
                })
                node.push({
                  id: itemK.lineId + 'A',
                  parId: res.rows[0].zResid
                })
                node.push({
                  id: itemK.lineId + 'Z',
                  parId: res.rows[0].aResid
                })
              }
            }
            aZD = zd
          })
        }
      }
      if(zZD != zd || linshi.length != 0){
        if(this.prompts){
          this.$message({
            type: 'info',
            message: '请确认光缆是否选择正确'
          })
        }
        this.prompts = false
        this.$refs.iframe.contentWindow.cleanUp();
        return;
      }
      this.$refs.iframe.contentWindow.reSetTopo(group, node, line, data)
    }
  },
  mounted () {
    this.getTable()
  },
  activated () {
    this.getTable()
  },
}
</script>

<style lang="scss" scoped>
  .setLightPath{
    padding: 10px;
    height: 100%;
  }
</style>
