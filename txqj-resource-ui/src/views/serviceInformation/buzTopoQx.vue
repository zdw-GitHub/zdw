<template>
  <div>
    <el-dialog
      v-dialogDrag
      custom-class="channelTopo"
      title="配置路由"
      :visible.sync="lightPathShow"
      width="100%"
      :modal="false"
      :close-on-click-modal="false"
      append-to-body
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
            </el-table>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="lightPathShow = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    getLineTopoByTDId,
    saveRouterApi,
    getFiberOptionsApi,
    getFiberLineOptionsApi
  } from '@/api/serviceInformation/fiberLine'
  export default {
    name: "buzTopoQx",
    props: {
      parRow: Object,
      parCahnnel: String
    },
    data () {
      return {
        iframeSrc: '/stationWh/serve/buzRoutQx.html',
        tabShow: true,
        lightPathShow: false,
        selLightPathTable: [],
        topoData: {},
      }
    },
    methods: {

      lightPathBoxShow () {
        this.lightPathShow = true
        this.$nextTick(() => {
          this.getTopoData()
        })
      },
      getTopoData () {
        getLineTopoByTDId({id: this.parRow.ID}).then(res => {
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
        color: #f0f0f0;
        width: 100%;
        height: 40px;
        line-height: 40px;
        font-size: 16px;
        background-color: #0f9ea2;
        padding: 0 10px;
        .tabClose{
          font-size: 24px;
          line-height: 40px;
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
