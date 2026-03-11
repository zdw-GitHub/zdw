<template>
  <div>
    <el-dialog v-dialogDrag title="配置路由" :visible.sync="lightPathShow" width="100%" :modal="false"
      :close-on-click-modal="false" custom-class="channelTopo">
      <div style="height: 75vh">
        <div class="topo" :style="{ height: tabShow ? '50%' : 'calc(100% - 30px)' }">
          <iframe ref="iframe" width="100%" id="iframe1" name="iframe1" style="border: none" height="100%"
            :src="iframeSrc"></iframe>
        </div>
        <div class="tab" :style="{ height: tabShow ? '50%' : '30px' }">
          <p class="tabTit">
            路由信息
            <span class="tabClose" :class="{
              'el-icon-arrow-down': tabShow,
              'el-icon-arrow-up': !tabShow
            }" @click="tabShow = !tabShow"></span>
          </p>
          <div class="tabCon" v-if="tabShow">
            <el-table stripe border height="100%" :data="selLightPathTable" style="width: 100%">
              <el-table-column type="index" label="序号" width="80" align="center">
              </el-table-column>
              <el-table-column prop="name" label="光路名称" min-width="350" align="center">
              </el-table-column>
              <el-table-column prop="aResId" label="起始设备" width="250" align="center">
              </el-table-column>
              <el-table-column prop="aPort" label="起始端口" width="100" align="center">
              </el-table-column>
              <el-table-column prop="zResId" label="终止设备" width="250" align="center">
              </el-table-column>
              <el-table-column prop="zPort" label="终止端口" width="100" align="center">
              </el-table-column>
              <el-table-column label="操作" fixed="right" width="100" align="center">
                <template slot-scope="scope">
                  <el-button @click="deleteSelLightPathTable(scope.row.id)"
                    v-hasPermi="['resources:sgTcconTcchannelsegB:saveChannelsegTopo']">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </div>
      <div v-if="pastedCollection">
        <h3>粘贴的集合：</h3>
        <pre>{{ pastedCollection }}</pre>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="cobyRout">复制路由</el-button>
        <el-button type="primary" @click="pasteRout">粘贴路由</el-button>
        <el-button type="primary" @click="sort">自动布局</el-button>
        <el-button type="primary" @click="submitLightPath()" v-if="parRow.isCollection != '1'"
          :loading="submitLightPathLoading" v-hasPermi="['resources:sgTcconTcchannelsegB:saveChannelsegTopo']">保
          存</el-button>
        <el-button @click="lightPathShow = false">关 闭</el-button>
      </div>
    </el-dialog>

    <el-dialog v-dialogDrag custom-class="curdBox" title="选择光路" :visible.sync="lightPathBox.show" width="80%"
      :modal="false" :close-on-click-modal="false">
      <div style="width: 100%; height: 70vh;" class="commonWh">
        <el-form label-width="80px" :inline="true">
          <el-form-item label="光路名称 : " prop="name">
            <el-input v-model="glName" placeholder="请输入光路名称"></el-input>
          </el-form-item>
        </el-form>
        <el-table stripe border height="calc(100% - 60px)" :data="tabData" style="width: 100%">
          <el-table-column type="index" label="序号" width="80" align="center">
          </el-table-column>
          <el-table-column prop="name" label="光路名称" min-width="350" align="center">
          </el-table-column>
          <el-table-column prop="aResId" label="起始设备" width="250" align="center">
          </el-table-column>
          <el-table-column prop="aPort" label="起始端口" width="100" align="center">
          </el-table-column>
          <el-table-column prop="zResId" label="终止设备" width="250" align="center">
          </el-table-column>
          <el-table-column prop="zPort" label="终止端口" width="100" align="center">
          </el-table-column>
          <el-table-column label="操作" fixed="right" width="100" align="center">
            <template slot-scope="scope">
              <el-button @click="selLightPath(scope.row)">选择</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="lightPathBox.show = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getChannelTopoApi,
  getLightPathByResIdApi,
  submitLightPathApi
} from '@/api/serviceInformation/channel'
export default {
  name: "channelTopo",
  props: {
    parRow: Object,
  },
  computed: {
    tabData() {
      return this.lightPathBox.tabData.filter(a => {
        if (a.name) {
          return a.name.indexOf(this.glName) !== -1
        } else {
          return true
        }

      })
    }
  },
  data() {
    return {
      iframeSrc: '/stationWh/serve/channel.html',
      tabShow: true,
      lightPathShow: false,
      pastedCollection: null,
      loading: true,
      submitLightPathLoading: false,
      synchronizationLoading: false,
      selLightPathTable: [],
      topoData: {},
      lightPathBox: {
        tabData: [],
        selData: {},
        show: false,
      },
      aPortId: '',
      zPortId: '',
      glName: '',
    }
  },
  methods: {
    async cobyRout() {
      try {
        // 将集合转换为 JSON 字符串
        const text = JSON.stringify(this.selLightPathTable);
        // 写入剪贴板
        await navigator.clipboard.writeText(text);
        this.$modal.msgSuccess("复制成功");
      } catch (err) {
        console.error("复制失败:", err);
      }
    },
    async pasteRout() {
      try {
        // 从剪贴板读取文本
        const text = await navigator.clipboard.readText();
        // 将数据解析为集合
        const collection = JSON.parse(text);
        // 检查是否是数组
        if (!Array.isArray(collection)) {
          throw new Error("剪贴板中的数据不是有效的集合！");
        }
        if (collection.length > 0) {
          console.log(collection);
          // 循环调用 selLightPath 方法
          for (const row of collection) {
            this.selLightPath(row);
          }
          this.submitLightPathLoading = true;
          let data = this.$refs.iframe.contentWindow.returnTopoData()
          data.channelId = this.parRow.id
          data.optRoadData = data.optRoadData.filter(p => p.deleteFlag != 1);
          data = JSON.stringify(data)
          this.sort();
          submitLightPathApi(data).then(res => {
            this.submitLightPathLoading = false;
          }).catch(e => {
            this.submitLightPathLoading = false
          })
        }
      } catch (err) {
        console.error("粘贴失败:", err);
      }
    },
    lightPathBoxShow() {
      this.lightPathShow = true
      this.$nextTick(() => {
        this.getTopoData()
      })
    },
    getTopoData() {
      this.loading = true
      this.selLightPathTable = []
      this.topoData = {}
      this.lightPathBox = {
        tabData: [],
        selData: {},
        show: false
      }
      getChannelTopoApi({ channelId: this.parRow.id }).then(res => {
        this.topoData.group = res.data.groupsData
        this.topoData.node = res.data.nodesData
        this.topoData.link = res.data.optRoadData
        this.selLightPathTable = res.data.optRoadData.filter(p => p.deleteFlag != 1);
        this.aPortId = res.data.aPortId
        this.zPortId = res.data.zPortId
        this.$nextTick(() => {
          this.loading = false
          setTimeout(() => {
            this.$refs.iframe.contentWindow.reSetTopo(
              this.topoData.group,
              this.topoData.node,
              this.topoData.link,
              res.data.aPortId,
              res.data.zPortId,
            )
          }, 500)
        })
      })
    },
    sort() {
      let data = this.$refs.iframe.contentWindow.returnTopoData()
      let nodesData = [...data.nodesData]
      let newNodesData = []
      let optRoadData = [...data.optRoadData]
      let newOptRoadData = [...data.optRoadData.filter(p => p.deleteFlag != 1)];
      let groupData = [...data.groupData]
      if (nodesData.length == 0) {
        return
      }

      let x = 100
      let aIndex = nodesData.findIndex(a => {
        return a.nodeId == this.aPortId
      })
      if (aIndex != -1) {
        nodesData[aIndex].positionX = x
        nodesData[aIndex].positionY = 100
        x += 50
        newNodesData.push(nodesData[aIndex])
        nodesData.splice(aIndex, 1);
      }

      let zIndex = nodesData.findIndex(a => {
        return a.nodeId == this.zPortId
      })
      if (zIndex != -1) {
        newNodesData.push(nodesData[zIndex])
        nodesData.splice(zIndex, 1);
      }


      let aGroup = groupData.filter(a => {
        if (a.groupFlag) {
          return a.groupFlag == 'A'
        } else {
          return false
        }
      })

      let zGroup = groupData.filter(a => {
        if (a.groupFlag) {
          return a.groupFlag == 'Z'
        } else {
          return false
        }
      })

      let aNode = nodesData.filter(a => {
        return a.groupId == aGroup[0].groupId
      })

      let num = newOptRoadData.length
      //路由一条直线
      if (aNode.length == 1) {
        let nodeId = aNode[0].nodeId
        for (let k = 0; k < num; k++) {
          let index = nodesData.findIndex(a => {
            return a.nodeId == nodeId
          })
          nodesData[index].positionX = x
          nodesData[index].positionY = 100
          x += 200
          newNodesData.push(nodesData[index])
          nodesData.splice(index, 1);

          let nId = '';
          let indexOptRoad = newOptRoadData.findIndex(a => {
            return a.aPortId == nodeId || a.zPortId == nodeId
          })
          if (newOptRoadData[indexOptRoad].aPortId == nodeId) {
            nId = newOptRoadData[indexOptRoad].zPortId;
          }
          if (newOptRoadData[indexOptRoad].zPortId == nodeId) {
            nId = newOptRoadData[indexOptRoad].aPortId;
          }
          newOptRoadData.splice(indexOptRoad, 1);

          let index2 = nodesData.findIndex(a => {
            return a.nodeId == nId
          })
          nodesData[index2].positionX = x
          nodesData[index2].positionY = 100
          x += 50
          newNodesData.push(nodesData[index2])
          let groupId = nodesData[index2].groupId
          nodesData.splice(index2, 1);

          let index3 = nodesData.findIndex(a => {
            return a.groupId == groupId
          })

          if (nodesData[index3]) {
            nodeId = nodesData[index3].nodeId
          }
        }
        zIndex = newNodesData.findIndex(a => {
          return a.nodeId == this.zPortId
        })
        if (zIndex != -1) {
          newNodesData[zIndex].positionX = x
          newNodesData[zIndex].positionY = 100
        }
      }




      let lowerNId = '';
      let topNId = '';
      //路由环型

      if (aNode.length == 2) {
        let topNodeId = aNode[0].nodeId
        let lowerNodeId = aNode[1].nodeId

        let top = true
        let lower = true

        for (let k = 0; k < num; k++) {
          if (top) {
            let topIndex = nodesData.findIndex(a => {
              return a.nodeId == topNodeId
            })
            nodesData[topIndex].positionX = x
            nodesData[topIndex].positionY = 100
            newNodesData.push(nodesData[topIndex])
            nodesData.splice(topIndex, 1);
          }
          if (lower) {
            let lowerIndex = nodesData.findIndex(a => {
              return a.nodeId == lowerNodeId
            })
            nodesData[lowerIndex].positionX = x
            if (k == 0) {
              nodesData[lowerIndex].positionY = 150
            } else {
              nodesData[lowerIndex].positionY = 300
            }

            newNodesData.push(nodesData[lowerIndex])
            nodesData.splice(lowerIndex, 1);
          }

          x += 200


          zIndex = newNodesData.findIndex(a => {
            return a.nodeId == this.zPortId
          })
          let topGroupId = ''
          let lowerGroupId = ''
          if (top) {
            let topIndexOptRoad = newOptRoadData.findIndex(a => {
              return a.aPortId == topNodeId || a.zPortId == topNodeId
            })
            if (newOptRoadData[topIndexOptRoad].aPortId == topNodeId) {
              topNId = newOptRoadData[topIndexOptRoad].zPortId;
            }
            if (newOptRoadData[topIndexOptRoad].zPortId == topNodeId) {
              topNId = newOptRoadData[topIndexOptRoad].aPortId;
            }
            newOptRoadData.splice(topIndexOptRoad, 1);

            let topIndex2 = nodesData.findIndex(a => {
              return a.nodeId == topNId
            })
            topGroupId = nodesData[topIndex2].groupId
            if (zGroup[0].groupId != topGroupId) {
              nodesData[topIndex2].positionX = x
              nodesData[topIndex2].positionY = 100
              newNodesData.push(nodesData[topIndex2])
              nodesData.splice(topIndex2, 1);
            }
          }
          if (lower) {
            let lowerIndexOptRoad = newOptRoadData.findIndex(a => {
              return a.aPortId == lowerNodeId || a.zPortId == lowerNodeId
            })
            if (newOptRoadData[lowerIndexOptRoad].aPortId == lowerNodeId) {
              lowerNId = newOptRoadData[lowerIndexOptRoad].zPortId;
            }
            if (newOptRoadData[lowerIndexOptRoad].zPortId == lowerNodeId) {
              lowerNId = newOptRoadData[lowerIndexOptRoad].aPortId;
            }
            newOptRoadData.splice(lowerIndexOptRoad, 1);

            let lowerIndex2 = nodesData.findIndex(a => {
              return a.nodeId == lowerNId
            })
            lowerGroupId = nodesData[lowerIndex2].groupId

            if (zGroup[0].groupId != lowerGroupId) {
              nodesData[lowerIndex2].positionX = x
              nodesData[lowerIndex2].positionY = 300
              newNodesData.push(nodesData[lowerIndex2])
              nodesData.splice(lowerIndex2, 1);
            }
          }

          x += 50

          if (zGroup[0].groupId == topGroupId && zGroup[0].groupId == lowerGroupId) {
            top = false
            lower = false
            topGroupId = ''
            lowerGroupId = ''

            let topIndex = nodesData.findIndex(a => {
              return a.nodeId == topNId
            })
            nodesData[topIndex].positionX = x - 50
            nodesData[topIndex].positionY = 100
            newNodesData.push(nodesData[topIndex])
            nodesData.splice(topIndex, 1);

            if (zIndex != -1) {
              aIndex = newNodesData.findIndex(a => {
                return a.nodeId == this.aPortId
              })
              newNodesData[aIndex].positionX = 100
              newNodesData[aIndex].positionY = 200

              newNodesData[zIndex].positionX = x
              newNodesData[zIndex].positionY = 200
            }

            let lowerIndex = nodesData.findIndex(a => {
              return a.nodeId == lowerNId
            })
            nodesData[lowerIndex].positionX = x - 50
            nodesData[lowerIndex].positionY = 200
            newNodesData.push(nodesData[lowerIndex])
            nodesData.splice(lowerIndex, 1);


          } else if (zGroup[0].groupId == topGroupId) {
            if (!lower) {
              let topIndex = nodesData.findIndex(a => {
                return a.nodeId == topNId
              })
              nodesData[topIndex].positionX = x - 250
              nodesData[topIndex].positionY = 300
              newNodesData.push(nodesData[topIndex])
              nodesData.splice(topIndex, 1);

              if (zIndex != -1) {
                newNodesData[zIndex].positionX = x - 300
                newNodesData[zIndex].positionY = 300
              }

              let lowerIndex = nodesData.findIndex(a => {
                return a.nodeId == lowerNId
              })

              if (zIndex != -1) {
                nodesData[lowerIndex].positionX = x - 350
              } else {
                nodesData[lowerIndex].positionX = x - 300
              }
              nodesData[lowerIndex].positionY = 300
              newNodesData.push(nodesData[lowerIndex])
              nodesData.splice(lowerIndex, 1);

            }
            top = false
            topGroupId = ''
          } else if (zGroup[0].groupId == lowerGroupId) {
            if (!top) {
              let topIndex = nodesData.findIndex(a => {
                return a.nodeId == topNId
              })

              if (zIndex != -1) {
                nodesData[topIndex].positionX = x - 350
              } else {
                nodesData[topIndex].positionX = x - 300
              }
              nodesData[topIndex].positionY = 100
              newNodesData.push(nodesData[topIndex])
              nodesData.splice(topIndex, 1);

              if (zIndex != -1) {
                newNodesData[zIndex].positionX = x - 300
                newNodesData[zIndex].positionY = 100
              }

              let lowerIndex = nodesData.findIndex(a => {
                return a.nodeId == lowerNId
              })
              nodesData[lowerIndex].positionX = x - 250
              nodesData[lowerIndex].positionY = 100
              newNodesData.push(nodesData[lowerIndex])
              nodesData.splice(lowerIndex, 1);
            }
            lower = false
            lowerGroupId = ''
          }

          if (top) {
            let topIndex3 = nodesData.findIndex(a => {
              return a.groupId == topGroupId
            })

            if (nodesData[topIndex3]) {
              topNodeId = nodesData[topIndex3].nodeId
            }
          }

          if (lower) {
            let lowerIndex3 = nodesData.findIndex(a => {
              return a.groupId == lowerGroupId
            })

            if (nodesData[lowerIndex3]) {
              lowerNodeId = nodesData[lowerIndex3].nodeId
            }
          }

        }
      }







      this.$nextTick(() => {
        this.loading = false
        setTimeout(() => {
          this.$refs.iframe.contentWindow.reSetTopo(
            groupData,
            newNodesData,
            optRoadData,
            this.aPortId,
            this.zPortId,
          )
        }, 500)
      })


    },
    getLightPath(data) {
      getLightPathByResIdApi({
        pageSize: 999,
        resId: data.groupId
      }).then(res => {
        this.lightPathBox.tabData = res.rows
        this.lightPathBox.show = true
      })
    },
    selLightPath(row) {
      this.$refs.iframe.contentWindow.addLightPath(row)
      this.lightPathBox.show = false
    },
    getSelLightPathTable(data) {
      this.selLightPathTable = data
    },
    deleteSelLightPathTable(id) {
      for (let index = 0; index < this.selLightPathTable.length; index++) {
        if (this.selLightPathTable[index] && this.selLightPathTable[index].id === id) {
          this.selLightPathTable.splice(index, 1)
        }
      }
      this.$nextTick(() => {
        this.loading = false
        setTimeout(() => {
          this.$refs.iframe.contentWindow.removeLinkById(id)
        }, 500)
      })
    },
    submitLightPath() {
      this.submitLightPathLoading = true;
      let data = this.$refs.iframe.contentWindow.returnTopoData()
      data.channelId = this.parRow.id
      data.optRoadData = data.optRoadData.filter(p => p.deleteFlag != 1);
      data = JSON.stringify(data)
      submitLightPathApi(data).then(res => {
        this.$modal.msgSuccess("操作成功");
        this.lightPathShow = false
        this.submitLightPathLoading = false;
      }).catch(e => {
        this.submitLightPathLoading = false
      })
    },
    delTopoData(functionName, data) {
      let that = this
      this.$confirm('是否确认删除数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
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
    window['getLightPath'] = (data) => {
      this.getLightPath(data)
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
.channelTopo {
  .topo {
    height: 50%;
  }

  .tab {
    height: 50%;
    box-shadow: 0px -2px 0px #eee;

    .tabTit {
      width: 100%;
      height: 30px;
      line-height: 30px;
      font-size: 15px;
      padding: 0 10px;

      .tabClose {
        font-size: 24px;
        line-height: 30px;
        float: right;
        cursor: pointer;

        &:hover {
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
