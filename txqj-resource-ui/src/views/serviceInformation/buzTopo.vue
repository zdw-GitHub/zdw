<template>
    <div>
      <el-dialog
        v-dialogDrag
        custom-class="buzTopo"
        title="业务路由展示"
        :visible.sync="lightPathShow"
        width="100%"
        :modal="false"
        :close-on-click-modal="false"
        append-to-body
      >
        <div style="height: 75vh" v-loading="loading">
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
                  prop="name"
                  label="光路名称"
                  min-width="350"
                  align="center">
                </el-table-column>
                <el-table-column
                  prop="aResId"
                  label="起始设备"
                  width="250"
                  align="center">
                </el-table-column>
                <el-table-column
                  prop="aPort"
                  label="起始端口"
                  width="100"
                  align="center">
                </el-table-column>
                <el-table-column
                  prop="zResId"
                  label="终止设备"
                  width="250"
                  align="center">
                </el-table-column>
                <el-table-column
                  prop="zPort"
                  label="终止端口"
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
      getBuzTopoApi2,
    } from '@/api/serviceInformation/channel'
    export default {
      name: "buzTopo",
      props: {
        parRow: Object,
      },
      data () {
        return {
          iframeSrc: '/stationWh/serve/buzRout.html',
          tabShow: true,
          lightPathShow: false,
          loading: false,
          selLightPathTable: [],
        }
      },
      methods: {
        lightPathBoxShow () {
          this.lightPathShow = true
          this.$nextTick(() => {
            this.getBuzTopoData()
          })
        },
        getBuzTopoData () {
          this.loading = true
          getBuzTopoApi2({id:this.parRow.ID}).then(res => {
            this.selLightPathTable = res.data.optRoadData
            this.$nextTick(() => {
              this.loading = false
              setTimeout(()=>{
                this.$refs.iframe.contentWindow.reSetTopo(
                  res.data.groups,
                  res.data.childGroups,
                  res.data.nodes,
                  res.data.lines
                )
              },500)
            })
          })
        },
      },
    }
  </script>

  <style lang="scss">
    .buzTopo{
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
          padding: 0 10px;
          background-color: #0f9ea2;
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
