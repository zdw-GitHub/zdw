<template>
  <div>
    <ToolBar ref="child" :tipShow="stepTipShow"></ToolBar>
  </div>
</template>

<script>
    import ToolBar from "../../components/gltxh/toolBar";
    export default {
        name: "data",
      components: {ToolBar},

      data(){
        return {
          iframeSrc:'/html/one.html',
          stepTipShow:false,
          toolBarShow:false
        }
      },
      created(){
          if(this.$route.query.stepTipShow && this.$route.query.stepTipShow == 'true'){
            this.stepTipShow = true
          }

        // 添加接头盒
        window['addBox'] = (lineName, dataType, taskShowId, id, data, x, y) => {
          this.$refs.child.addBox(lineName, dataType, taskShowId, id, data, x, y)
        }
        // 选择光缆
        window['addLinkShow'] = (x, y) => {
          this.$refs.child.addLinkShow(x, y)

        }
        // 选择站点
        window['addSiteBoxShow'] = (x, y) => {
          this.$refs.child.addSiteBoxShow(x, y)

        }
        // 新增接头盒
        window['addTconCommit'] = (x, y) => {
          this.$refs.child.addTconCommit(x, y)
        }
        // 绘制光缆
        window['addFiberBoxShow'] = (aNode, zNode) => {
          this.$refs.child.addFiberBoxShow(aNode, zNode)
        }
        // 删除topo数据
        window['delData'] = (dataType, taskShowId, id, data) => {
          this.$refs.child.delData(dataType, taskShowId, id, data)
        }
        // 删除topo元素
        window['delTopoData'] = (dataType, taskShowId, id, data) => {
          this.$refs.child.delTopoData(dataType, taskShowId, id, data)
        }
        // 修改topo元素
        window['editTopoData'] = (data) => {
          this.$refs.child.editTopoData (data)
        }
        // 查看topo元素
        window['seeTopoData'] = (data) => {
          this.$refs.child.seeTopoData (data)
        }
        // 纤芯接续
        window['coreConnect'] = (data) => {
          this.$refs.child.coreConnectShow(data)
        }
        // 新增纤芯
        window['addLink'] = (data) => {
          this.$refs.child.addLink(data)
        }
        // 删除纤芯
        window['delLink'] = (id, link) => {
          this.$refs.child.delLink(id, link)
        }
        // 重新绘制纤芯
        window['reSetLink'] = (delIds, delLinks, aResId, zResId) => {
          this.$refs.child.reSetLink(delIds, delLinks, aResId, zResId)
        }
        // 批量修改纤芯
        window['batchConnectionsShow'] = (data, datas, title) => {
          this.$refs.child.batchConnectionsShow(data, datas, title)
        }
        // 批量删除纤芯
        window['delMoreBoxShow'] = (id, max, title) => {
          this.$refs.child.delMoreBoxShow(id, max, title)
        }
        // 批量删除纤芯
        window['getFiberData'] = (id) => {
          this.$refs.child.getFiberData(id)
        }
        // 批量删除纤芯
        window['closeFiberBox'] = (id) => {
          this.$refs.child.closeFiberBox(id)
        }

        // 查看光缆纤芯
        window['openFiberCoreDetail'] = (data) => {
          this.$refs.child.openFiberCoreDetail(data)
        }

      },

      deactivated() {
          this.$destroy()
      },
    }
</script>

<style scoped>
  .iframeSpace{
    width: 100%;
    height:75vh;
  }
</style>
