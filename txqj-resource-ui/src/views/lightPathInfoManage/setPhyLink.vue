<template>
  <div>
    <div style="height: 100%" v-loading="phyLinkShowLoading">
      <iframe
        ref="iframe"
        width="100%"
        id="iframe"
        name="iframe"
        style="border: none"
        height="100%"
        :src="iframeSrc"
      ></iframe>
    </div>
    <el-dialog
      v-dialogDrag
      custom-class="curdBox"
      title="选择光缆"
      :visible.sync="fiberBox.show"
      width="80%"
      :modal="false"
      :close-on-click-modal="false"
    >
      <div style="width: 100%; height: 70vh;" class="commonWh">


        <el-table
          stripe
          border
          height="calc(100% - 60px)"
          :data="fiberBox.tabData"
          style="width: 100%">
          <el-table-column
            type="index"
            label="序号"
            width="80"
            align="center">
          </el-table-column>
          <el-table-column
            prop="snname"
            label="光缆名称"
            min-width="350"
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
            label="操作"
            fixed="right"
            width="100"
            align="center">
            <template slot-scope="scope">
              <el-button @click="selFiber(scope.row)">选择</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fiberBox.show = false">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog
      v-dialogDrag
      title="光缆纤芯选择"
      :modal="false"
      :close-on-click-modal="false"
      :visible.sync="addFiberLineBox.show"
      width="18%"
      top="10%">
      <span style="margin-right: 20px">光缆纤芯</span>
      <el-select
        v-model="addFiberLineBox.selLine"
        filterable
        allow-create
        placeholder="请选择">
        <el-option
          v-for="item in addFiberLineBox.options"
          :label="item.sn"
          :value="item.id"
          :key="item.id"
          :disabled="item.disabled == '1'">
        </el-option>
      </el-select>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addFiberLineBox.show = false;addFiberLineBox.commitLoading = false;addFiberLineBox.selLine = ''">取 消</el-button>
        <el-button type="primary" @click="addFiberLineBoxCommit()" :loading="addFiberLineBox.commitLoading"
                   :disabled="addFiberLineBox.selLine == ''">确 定
        </el-button>
      </div>
    </el-dialog>

    <el-dialog
      v-dialogDrag
      title="收发类型选择"
      :modal="false"
      :close-on-click-modal="false"
      :visible.sync="hierarchy.show"
      width="18%"
      top="10%">
      <span style="margin-right: 20px">收发类型</span>
      <el-select
        v-model="hierarchy.select"
        filterable
        allow-create
        placeholder="请选择">
        <el-option label="发" value="发"></el-option>
        <el-option label="收" value="收"></el-option>
      </el-select>
      <div slot="footer" class="dialog-footer">
        <el-button @click="hierarchy.show = false">取 消</el-button>
        <el-button type="primary" @click="addHierarchyBoxCommit()" :disabled="hierarchy.select == ''">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

import {
  obtainTheOpticalWiringConnectionRelationship,
  obtainODFWiringModuleForOpticalCable,
  obtainTheTerminalsOfTheODFWiringModule,
} from '@/api/lightPathInfoManage/setPhyLink'
import {
  getFiberOptionsApi,
  getFiberLineOptionsApi
} from "@/api/lightPathInfoManage/setLightPath";

export default {
  name: "setPhyLink",
  props: {
    //604533000000000001
    routerId: String,
  },
  data() {
    return {
      iframeSrc: '/stationWh/html/phyLink.html',
      phyLinkShowLoading: false,
      //路由信息
      router: {},
      //光缆
      fiberList: [],
      //ODF配线模块
      fbrconnectList: [],
      //端子
      tceqptermList: [],
      //配线
      tcphylinkRList: [],

      fiberBox: {
        tabData: [],
        selData: {},
        show: false,
      },

      addFiberLineBox: {
        show: false,
        commitLoading: false,
        options: [],
        selLine: '',
        aNode: {},
        zNode: {},
        fiber: {}
      },
      hierarchy: {
        show: false,
        select: '',
        aNode: {},
        zNode: {},
      },
    }
  },
  mounted () {
    this.getPhyLinkData()
  },
  methods: {
    getPhyLinkData() {
      this.phyLinkShowLoading = true
      this.router = {}
      this.fiberList = []
      this.fbrconnectList = []
      this.tceqptermList = []
      this.tcphylinkRList = []
      obtainTheOpticalWiringConnectionRelationship({routerId: this.routerId}).then(res => {
        this.router = res.router || {}
        this.fiberList = res.fiberList || []
        this.fbrconnectList = res.fbrconnectList || []
        this.tceqptermList = res.tceqptermList || []
        this.tcphylinkRList = res.tcphylinkRList || []

        this.reDrawTopo();
        this.phyLinkShowLoading = false
      })
    },
    reDrawTopo() {
      let group = []
      let childGroup = []
      let node = []
      let line = []

      this.addFiberTopo(group)

      this.addFbrconnectTopo(childGroup)

      this.addTcphylinkRTopo(line)

      this.addNodeTopo(node)

      setTimeout(() => {
        this.$refs.iframe.contentWindow.reSetTopo(group, childGroup, node, line);
      }, 500)
    },
    addFiberTopo(group) {
      //光路站点
      group.push({
        id: this.router.aResobjid,
        name: this.router.aResobj,
      })
      group.push({
        id: this.router.zResobjid,
        name: this.router.zResobj,
      })

      for (let i = 0; i < this.fiberList.length; i++) {
        let fiber = this.fiberList[i]
        let aIndex = group.findIndex(item => {
          return item.id == fiber.aResid
        })
        if (aIndex == -1) {
          group.push({
            id: fiber.aResid,
            name: fiber.aResName
          })
        }
        let zIndex = group.findIndex(item => {
          return item.id == fiber.zResid
        })
        if (zIndex == -1) {
          group.push({
            id: fiber.zResid,
            name: fiber.zResName
          })
        }

      }
    },
    addFbrconnectTopo(childGroup) {
      //光路设备
      childGroup.push({
        id: this.router.aId,
        name: this.router.aResId,
        parId: this.router.aResobjid,
      })
      childGroup.push({
        id: this.router.zId,
        name: this.router.zResId,
        parId: this.router.zResobjid,
      })

      for (let i = 0; i < this.fbrconnectList.length; i++) {
        childGroup.push({
          id: this.fbrconnectList[i].id,
          name: this.fbrconnectList[i].name,
          parId: this.fbrconnectList[i].parStation,
        })
      }
    },
    addTcphylinkRTopo(line) {
      //配线中包含的端子取消禁用
      for (let i = 0; i < this.tcphylinkRList.length; i++) {
        let aResId = this.tcphylinkRList[i].aResId
        let zResId = this.tcphylinkRList[i].zResId
        let obj = {
          id: this.tcphylinkRList[i].id,
          from: aResId,
          to: zResId,
        }
        if (this.tcphylinkRList[i].name) obj.name = this.tcphylinkRList[i].name
        line.push(obj)


        let indexA = this.tceqptermList.findIndex(item => item.id == aResId);
        if(indexA != -1) this.tceqptermList[indexA].disabled = '2'
        let indexZ = this.tceqptermList.findIndex(item => item.id == zResId);
        if(indexZ != -1) this.tceqptermList[indexZ].disabled = '2'
      }
    },
    addNodeTopo(node) {
      //otf排序
      let otf = []
      let aResid = this.router.aResobjid
      let fiberList = [...this.fiberList]

      let resid = ''

      for (let i = 0; fiberList.length > 0; i) {
        let index = fiberList.findIndex(item => {
          return item.aResid == aResid || item.zResid == aResid
        })
        if (index != -1) {

          if (aResid == fiberList[index].aResid) {
            let list1 = this.fbrconnectList.filter(item => {
              return item.parStation == fiberList[index].aResid && item.parFiber == fiberList[index].id
            })
            for (let j = 0; j < list1.length; j++) {
              if(otf.findIndex(item => item == list1[j].id) == -1) otf.push(list1[j].id)
            }
            let list2 = this.fbrconnectList.filter(item => {
              return item.parStation == fiberList[index].zResid && item.parFiber == fiberList[index].id
            })
            for (let j = 0; j < list2.length; j++) {
              if(otf.findIndex(item => item == list2[j].id) == -1) otf.push(list2[j].id)
            }

            resid = fiberList[index].zResid
          }
          if (aResid == fiberList[index].zResid) {
            let list2 = this.fbrconnectList.filter(item => {
              return item.parStation == fiberList[index].zResid && item.parFiber == fiberList[index].id
            })
            for (let j = 0; j < list2.length; j++) {
              if(otf.findIndex(item => item == list2[j].id) == -1) otf.push(list2[j].id)
            }
            let list1 = this.fbrconnectList.filter(item => {
              return item.parStation == fiberList[index].aResid && item.parFiber == fiberList[index].id
            })
            for (let j = 0; j < list1.length; j++) {
              if(otf.findIndex(item => item == list1[j].id) == -1) otf.push(list1[j].id)
            }
            resid = fiberList[index].aResid
          }
          aResid = resid
          fiberList.splice(index, 1)
        } else {
          let list1 = this.fbrconnectList.filter(item => {
            return item.parStation == fiberList[i].aResid && item.parFiber == fiberList[i].id
          })
          for (let j = 0; j < list1.length; j++) {
            otf.push(list1[j].id)
          }
          let list2 = this.fbrconnectList.filter(item => {
            return item.parStation == fiberList[i].zResid && item.parFiber == fiberList[i].id
          })
          for (let j = 0; j < list2.length; j++) {
            otf.push(list2[j].id)
          }

          fiberList.splice(i, 1)
        }
      }

      let positionX = 300
      //添加端子
      for (let i = 0; i < otf.length; i++) {
        let tceqptermList = this.tceqptermList.filter(item => item.parFbrconnecttype == otf[i]);
        //排序
        tceqptermList = tceqptermList.sort((item1, item2) => {
          return item1.name - item2.name
        })
        let positionY = 100
        for (let j = 0; j < tceqptermList.length; j++) {
          node.push({
            id: tceqptermList[j].id,
            name: tceqptermList[j].name,
            parId: tceqptermList[j].parFbrconnecttype,
            type: '1300',
            disabled:tceqptermList[j].disabled,
            img: 'duanZi',
            positionX: positionX,
            positionY: positionY
          })
          positionY += 30;
        }
        positionX += 200;
      }


      //光路端口
      node.push({
        id: this.router.aPortId,
        name: this.router.aPort,
        parId: this.router.aId,
        type: '1200',
        img: 'duanKou',
        positionX: 150,
        positionY: 300
      })

      node.push({
        id: this.router.zPortId,
        name: this.router.zPort,
        parId: this.router.zId,
        type: '1200',
        img: 'duanKou',
        positionX: positionX,
        positionY: 300
      })
    },

    commitPhyLink() {
      //校验数据完整性
      let fiberList = [...this.fiberList]
      let fIndex = fiberList.findIndex(item => item.aResid == this.router.aResobjid || item.zResid == this.router.aResobjid)
      if (fIndex == -1) {
        this.$message({type: 'info', message: '路由不完整'})
        return
      }
      let resid = fiberList[fIndex].aResid == this.router.aResobjid ? fiberList[fIndex].zResid : fiberList[fIndex].aResid
      fiberList.splice(fIndex, 1)
      for (let i = 0; fiberList.length > 0; i) {
        let index = fiberList.findIndex(item => {
          return item.aResid == resid || item.zResid == resid
        })
        if (index != -1) {
          resid = fiberList[index].aResid == resid ? fiberList[index].zResid : fiberList[index].aResid
          fiberList.splice(index, 1)
        } else {
          this.$message({type: 'info', message: '路由不完整'})
          return
        }
      }
      if (resid != this.router.zResobjid) {
        this.$message({type: 'info', message: '路由不完整'})
        return
      }

      //去除az资源不存在的配线
      let node = this.tceqptermList.map(item => item.id);
      node.push(this.router.aPortId)
      node.push(this.router.zPortId)
      let tcphylinkRS = [];
      for (let i = 0; i < this.tcphylinkRList.length; i++) {
        let indexA = node.findIndex(item => item == this.tcphylinkRList[i].aResId);
        let indexZ = node.findIndex(item => item == this.tcphylinkRList[i].zResId);
        if(indexA != -1 && indexZ != -1){
          tcphylinkRS.push(this.tcphylinkRList[i])
        }
      }
      this.tcphylinkRList = tcphylinkRS

      //校验配线完整性
      let tcphylinkRList = [...this.tcphylinkRList]
      let findIndex = tcphylinkRList.findIndex(item => item.aResId == this.router.aPortId || item.zResId == this.router.aPortId)
      if (findIndex == -1) {
        this.$message({type: 'info', message: '配线不完整'})
        return
      }
      let hierarchy = tcphylinkRList[findIndex].hierarchy
      let resId = tcphylinkRList[findIndex].aResId == this.router.aPortId ? tcphylinkRList[findIndex].zResId : tcphylinkRList[findIndex].aResId
      tcphylinkRList.splice(findIndex, 1)
      for (let i = 0; tcphylinkRList.length > 0; i) {
        let index = tcphylinkRList.findIndex(item => {
          return item.aResId == resId || item.zResId == resId
        })
        if (index != -1) {
          if (resId == this.router.zPortId) {
            hierarchy = tcphylinkRList[index].hierarchy
          } else {
            let ind = this.tcphylinkRList.findIndex(item => item.id == tcphylinkRList[index].id);
            this.tcphylinkRList[ind].hierarchy = hierarchy
          }
          resId = tcphylinkRList[index].aResId == resId ? tcphylinkRList[index].zResId : tcphylinkRList[index].aResId
          tcphylinkRList.splice(index, 1)
        } else {
          this.$message({type: 'info', message: '配线不完整'})
          return
        }
      }
      if (resId != this.router.aPortId && resId != this.router.zPortId) {
        this.$message({type: 'info', message: '配线不完整'})
        return
      }
      return this.tcphylinkRList
    },

    addTcphylinkR(aNode, zNode) {
      if (aNode.type == '1200' || zNode.type == '1200') {
        this.hierarchy.show = true
        this.hierarchy.aNode = aNode
        this.hierarchy.zNode = zNode
      }
      if (aNode.type == '1300' && zNode.type == '1300') {
        let aIndex = this.fbrconnectList.findIndex(item => item.id == aNode.parId);
        let zIndex = this.fbrconnectList.findIndex(item => item.id == zNode.parId);
        if (aIndex != -1 && zIndex != -1) {

          if (this.fbrconnectList[aIndex].parStation == this.fbrconnectList[zIndex].parStation) {
            let linkId = Date.now()
            this.tcphylinkRList.push({
              id: linkId,
              aResId: aNode.id,
              aResType: '1300',
              zResId: zNode.id,
              zResType: '1300',
            })
            setTimeout(() => {
              this.$refs.iframe.contentWindow.addLink({
                id: linkId,
                from: aNode.id,
                to: zNode.id,
              });
            }, 500)
          } else {
            let index = this.fiberList.findIndex(item => {
              return (item.aResid == this.fbrconnectList[aIndex].parStation || item.aResid == this.fbrconnectList[zIndex].parStation) && (item.zResid == this.fbrconnectList[aIndex].parStation || item.zResid == this.fbrconnectList[zIndex].parStation)
            })
            if (index != -1) {
              getFiberLineOptionsApi({
                parFiber: this.fiberList[index].id,
                routerId: this.routerId
              }).then(res => {
                this.addFiberLineBox.options = res.rows
              })
              this.addFiberLineBox.show = true
              this.addFiberLineBox.aNode = aNode
              this.addFiberLineBox.zNode = zNode
              this.addFiberLineBox.fiber = this.fiberList[index]
            }

          }
        }
      }
    },
    addHierarchyBoxCommit() {
      this.hierarchy.show = false
      let linkId =  Date.now();
      this.tcphylinkRList.push({
        id: linkId,
        aResId: this.hierarchy.aNode.id,
        aResType: this.hierarchy.aNode.type,
        zResId: this.hierarchy.zNode.id,
        zResType: this.hierarchy.zNode.type,
        name: this.hierarchy.select,
        hierarchy: this.hierarchy.select == '发' ? '0' : '1'
      })
      setTimeout(() => {
        this.$refs.iframe.contentWindow.addLink({
          id: linkId,
          from: this.hierarchy.aNode.id,
          to: this.hierarchy.zNode.id,
          name: this.hierarchy.select,
        });
      }, 500)
    },
    addFiberLineBoxCommit() {
      this.addFiberLineBox.commitLoading = true
      let linkId =  Date.now();
      let name = "（" + this.addFiberLineBox.fiber.aResName + " ~ " + this.addFiberLineBox.fiber.zResName + ")光缆纤芯F0"
      let index = this.addFiberLineBox.options.findIndex(item => item.id == this.addFiberLineBox.selLine)
      name += this.addFiberLineBox.options[index].sn > 9 ? this.addFiberLineBox.options[index].sn : "0" + this.addFiberLineBox.options[index].sn
      this.tcphylinkRList.push({
        id: linkId,
        aResId: this.addFiberLineBox.aNode.id,
        aResType: '1300',
        zResId: this.addFiberLineBox.zNode.id,
        zResType: '1300',
        fiberLineId: this.addFiberLineBox.selLine,
        parFiberId: this.addFiberLineBox.fiber.id,
        parFiberName: this.addFiberLineBox.fiber.snname,
        name: name
      })
      this.addFiberLineBox.selLine = ''
      this.addFiberLineBox.show = false
      this.addFiberLineBox.commitLoading = false
      setTimeout(() => {
        this.$refs.iframe.contentWindow.addLink({
          id: linkId,
          from: this.addFiberLineBox.aNode.id,
          to: this.addFiberLineBox.zNode.id,
          name: name
        });
      }, 500)
    },
    addFiberBoxShow(data) {
      getFiberOptionsApi({
        pageSize: 999,
        siteId: data.id
      }).then(res => {
        this.fiberBox.tabData = res.rows
        this.fiberBox.show = true
      })
    },
    selFiber(row) {
      this.phyLinkShowLoading = true
      this.fiberBox.show = false
      this.fiberList.push({
        id: row.id,
        snname: row.snname,
        aResid: row.aResid,
        zResid: row.zResid,
        aResName: row.aResName,
        zResName: row.zResName,
      })
      obtainODFWiringModuleForOpticalCable([row.id]).then(res => {
        if (res.length > 0) {
          this.fbrconnectList = [...this.fbrconnectList, ...res]
          let ids = res.map(item => item.id)
          obtainTheTerminalsOfTheODFWiringModule(ids).then(r => {
            this.tceqptermList = [...this.tceqptermList, ...r]
            this.reDrawTopo()
            this.phyLinkShowLoading = false
          })
        }
      })
    },
    delTcphylinkR(data) {
      let index = this.tcphylinkRList.findIndex(item => {
        return item.id == data.id
      })
      if (index != -1) this.tcphylinkRList.splice(index, 1)
    },
    delFiber(data) {
      //删除光缆两端站点包含data的光缆
      let fiberList = this.fiberList.filter(item => item.aResid == data.id || item.zResid == data.id);
      this.fiberList = this.fiberList.filter(item => item.aResid != data.id && item.zResid != data.id);

      //删除光缆中的otf
      let fbrconnectList = []
      for (let i = 0; i < fiberList.length; i++) {
        let fbrconnect = this.fbrconnectList.filter(item => item.parFiber == fiberList[i].id);
        this.fbrconnectList = this.fbrconnectList.filter(item => item.parFiber != fiberList[i].id);
        fbrconnectList = [...fbrconnectList, ...fbrconnect]
      }

      //删除otf中的端子
      let tceqptermList = []
      for (let i = 0; i < fbrconnectList.length; i++) {
        let tceqpterm = this.tceqptermList.filter(item => item.parFbrconnecttype == fbrconnectList[i].id);
        tceqptermList = [...tceqptermList, ...tceqpterm]
        this.tceqptermList = this.tceqptermList.filter(item => item.parFbrconnecttype != fbrconnectList[i].id);
      }

      //删除配线两端资源包含端子的配线
      for (let i = 0; i < tceqptermList.length; i++) {
        this.tcphylinkRList = this.tcphylinkRList.filter(item => item.aResId != tceqptermList[i].id && item.zResId != tceqptermList[i].id)
      }
      this.reDrawTopo()

    },
  },
  created() {
    window['addFiberBoxShow'] = (data) => {
      this.addFiberBoxShow(data)
    }
    window['delTcphylinkR'] = (data) => {
      this.delTcphylinkR(data)
    }
    window['delFiber'] = (data) => {
      this.delFiber(data)
    }
    window['addTcphylinkR'] = (aNode, zNode) => {
      this.addTcphylinkR(aNode, zNode)
    }
  },
}
</script>

<style lang="scss">
</style>
