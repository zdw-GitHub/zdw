<template>
  <div>
    <el-dialog
      v-dialogDrag
      title="配置路由"
      :visible.sync="lightPathShow"
      width="100%"
      :modal="false"
      :close-on-click-modal="false"
      custom-class="channelTopo"
    >
      <div style="height: 75vh">
        <div class="topo" :style="{height: tabShow ? '50%' : 'calc(100% - 30px)'}">
          <iframe
            ref="iframe"
            width="100%"
            id="iframe1"
            name="iframe1"
            style="border: none"
            height="100%"
            :src="iframeSrc"
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

              <el-table-column
              label="操作"
              fixed="right"
              width="100"
              align="center">
              <template slot-scope="scope">
              <el-button @click="deleteSelLightPathTable(scope.row.ID)" v-hasPermi="['resources:SgTcconTcchannelFiberB:saveChannelLineTopo']">删除</el-button>
              </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
<!--        <el-button type="primary" @click="submitLightPath()" :loading="submitLightPathLoading" v-hasPermi="['resources:SgTcconTcchannelFiberB:saveChannelLineTopo']">保 存</el-button>-->
        <el-button @click="lightPathShow = false;submitLightPathLoading = false">关 闭</el-button>
      </div>
    </el-dialog>

    <el-dialog
      v-dialogDrag
      title="选择纤芯"
      :visible.sync="lightPathBox.show"
      width="40%"
      :modal="false"
      :close-on-click-modal="false"
    >
      <div style="height: 40vh">
        <el-form label-width="100px">
          <el-form-item label="选择光缆 : " style="margin-bottom: 20px!important;">
            <el-select
              filterable
              remote
              style="width: 100%"
              :loading="fiberLoading"
              v-loadmore="loadmore"
              :remote-method="(val) => remoteMethod(val)"
              v-model="selFiberId"
              @focus="remoteMethod('')"
              @change="fiberSelected($event)"
            >
              <el-option
                v-for="item in fiberOptions.list"
                :key="item.id"
                :label="item.name"
                :value="item.id"
                :disabled="item.disabled">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="选择纤芯 : ">
            <el-select
              style="width: 100%"
              :loading="fiberLineLoading"
              v-model="selFiberLineId"
              @change="fiberLineSelected($event)"
            >
              <el-option
                v-for="item in fiberLineOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id"
                :disabled="item.disabled == '1'">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="selLightPath">保 存</el-button>
        <el-button @click="lightPathBox.show = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    getRouterApi,
    saveRouterApi,
    getFiberOptionsApi,
    getFiberLineOptionsApi
  } from '@/api/serviceInformation/fiberLine'
  export default {
    name: "fiberLineTopo",
    props: {
      parRow: Object,
    },
    data () {
      return {
        iframeSrc: '/stationWh/serve/fiberLine.html',
        tabShow: true,
        lightPathShow: false,

        selLightPathTable: [],
        topoData: {},
        lightPathBox: {
          tabData: [],
          selData: {},
          show: false,
        },

        searchOptionStr: '',
        fiberOptions: {
          total: 0,
          list: [],
        },
        fiberLoading: false,
        selFiberId: '',
        selFiber: {},
        fiberLineOptions: [],
        fiberLineLoading: true,
        selFiberLineId: '',
        selFiberLine: {},
        submitLightPathLoading:false
      }
    },
    watch:{
      'lightPathBox.show'(newVal,oldVal){
        if(!newVal){
          this.selFiberId = ''
          this.selFiberLineId = ''
        }
      }
    },
    methods: {
      remoteMethod (val) {
        this.fiberLoading = true
        this.searchOptionStr = val
        getFiberOptionsApi({
          snname: this.searchOptionStr,
          pageNum: 0,
          pageSize: 20,
        }).then((res) => {
          this.fiberOptions = {
            list: res.rows,
            total: res.total
          }
          this.$nextTick(() => {
            this.fiberLoading = false
          })
        })
      },
      loadmore () {
        if (this.fiberLoading) return
        if (this.fiberOptions.total > this.fiberOptions.list.length) {
          let pageNum = parseInt(this.fiberOptions.list.length / 20) * 20
          if (!this.fiberOptions.list[this.fiberOptions.list.length - 1].disabled) {
            this.fiberOptions.list.push({
              id: 'aaabbbccc',
              name: '加载中',
              disabled: true
            })
          }
          getFiberOptionsApi({
            name: this.searchOptionStr,
            siteId: '',
            pageNum: pageNum,
            pageSize: 20,
          }).then((res) => {
            this.fiberLoading = true
            this.fiberOptions.list.pop()
            if (res.rows.length > 0) {
              this.fiberOptions.list.push.apply(
                this.fiberOptions.list,
                res.rows
              )
            } else {
              if (!this.fiberOptions.list[this.fiberOptions.list.length - 1].disabled) {
                this.fiberOptions.list.push({
                  id: 'aaabbbccc',
                  name: '没有更多了',
                  disabled: true
                })
              }
            }
            this.$nextTick(() => {
              this.fiberLoading = false
            })
          })
        }
      },
      fiberSelected (data) {
        this.selFiber = this.fiberOptions.list.find(item => {
          return item.id == this.selFiberId
        })
        this.fiberLineOptions = []
        this.selFiberLine = {}
        getFiberLineOptionsApi({
          parFiber: data,
          pageSize: 9999
        }).then(res => {
          this.fiberLineLoading = true
          this.fiberLineOptions = res.rows
          this.$nextTick(() => {
            this.fiberLineLoading = false
          })
        })
      },
      fiberLineSelected (data) {
        this.selFiberLine = this.fiberLineOptions.find(item => {
          return item.id == this.selFiberLineId
        })
      },

      lightPathBoxShow () {
        this.lightPathShow = true
        this.$nextTick(() => {
          this.getTopoData()
        })
      },
      getTopoData () {
        this.selLightPathTable = []
        this.topoData = {}
        this.lightPathBox = {
          tabData: [],
          selData: {},
          show: false
        }
        getRouterApi({ChannelsegId: this.parRow.id}).then(res => {
          this.topoData.node = res.data.nodesData
          this.topoData.link = res.data.optRoadData
          this.selLightPathTable = res.data.optRoadData
          for (let i = 0; i < this.topoData.link.length; i++) {
            this.topoData.link[i].name = this.topoData.link[i].fiberName + ' ' + this.topoData.link[i].lineName
          }
          this.$nextTick(() => {
            setTimeout(() => {
              this.$refs.iframe.contentWindow.reSetTopo(
                this.topoData.node,
                this.topoData.link,
              )
            }, 500)
          })
        })
      },
      getFiberLine () {
        this.searchOptionStr = ''
        this.fiberOptions = {
          total: 0,
          list: [],
        }
        this.fiberLoading = false
        this.selFiber = {}
        this.fiberLineOptions = []
        this.fiberLineLoading = true
        this.selFiberLine = {}
        this.lightPathBox.show = true
      },
      selLightPath () {
        if (this.selFiberId != '' && this.selFiberLineId != '') {
          let data = {
            ID: this.selFiberLine.id,
            name: this.selFiber.name + ' ' + this.selFiberLine.name,
            aResId: this.selFiber.aResid,
            zResId: this.selFiber.zResid,
            aResName: this.selFiber.aResName,
            zResName: this.selFiber.zResName,
        }
          this.$refs.iframe.contentWindow.addLightPath(data)
          this.lightPathBox.show = false
          this.selFiberId = ''
          this.selFiberLineId = ''
        } else {
          this.$modal.msgError("请选择完整数据")
        }
      },
      getSelLightPathTable (data) {
        this.selLightPathTable = data
      },
      deleteSelLightPathTable (id) {
        console.log(id)
        for(let index = 0; index < this.selLightPathTable.length; index++) {
          if(this.selLightPathTable[index] && this.selLightPathTable[index].id === id) {
            this.selLightPathTable.splice(index, 1)
          }
        }
        this.$nextTick(() => {
          this.loading = false
          setTimeout(()=>{
            this.$refs.iframe.contentWindow.removeLinkById(id)
          },500)
        })
      },
      submitLightPath () {
        this.submitLightPathLoading = true
        let data = this.$refs.iframe.contentWindow.returnTopoData()
        data.channelsegId = this.parRow.id
        data = JSON.stringify(data)
        saveRouterApi(data).then(res => {
          this.$modal.msgSuccess("操作成功");
          this.submitLightPathLoading = false
          this.lightPathShow = false
        }).catch((e) => {
          this.submitLightPathLoading = false
        })
      },
      delTopoData (functionName, data) {
        let that = this
        this.$confirm('是否确认删除数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function() {
          that.$refs.iframe.contentWindow[functionName](data)
        }).catch((e) => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        })
      },
    },
    created() {
      window['getFiberLine'] = (data) => {
        this.getFiberLine(data)
      }
      window['getSelLightPathTable'] = (data) => {
        this.getSelLightPathTable(data)
      }
      window['delTopoData'] = (functionName, data) => {
        this.delTopoData(functionName, data)
      }
    }
  }
</script>

<style lang="scss">
  .channelTopo{
    .topo{
      height: 50%;
    }
    .tab{
      height: 50%;
      box-shadow: 0px -2px 0px #eee;
      .tabTit{
        width: 100%;
        height: 30px;
        line-height: 30px;
        font-size: 15px;
        padding: 0 10px;
        .tabClose{
          font-size: 24px;
          line-height: 30px;
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
