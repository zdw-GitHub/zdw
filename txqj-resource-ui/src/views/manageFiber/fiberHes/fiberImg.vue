<template>
    <div>
        <!--光缆示意图-->
        <!-- <el-dialog v-dialogDrag :modal="false" title="光缆示意图" width="70%" top="10vh"
            append-to-body :close-on-click-modal="false"> -->
            <div style="width: 100%; height: 890px">
                <iframe ref="iframe" width="100%" id="iframe" name="iframe" style="border: none" height="100%"
                    :src="iframeSrc"></iframe>
                <!--引入topo-->
                <div class="fiberInfoBox" v-if="fiberInfoBox.show">
                    <div class="fiberTitle">
                        光缆路由详情
                        <p class="boxClose el-icon-circle-close" @click="fiberInfoBox.show = false"></p>
                    </div>
                    <el-tabs v-model="fiberInfoBox.index" type="card">
                        <el-tab-pane label="路由" name="0">
                            <ul style="padding-bottom: 10px">
                                <template v-for="(item) in fiberInfoBox.data.topoList">
                                    <li class="fiberLine" v-if="item.type == 3">
                                        <p class="fiberInfo">
                                            <span class="Flabel">光缆段名称 : </span><span>{{ item.name }}</span>
                                        </p>
                                        <p class="fiberInfo">
                                            <span class="Flabel">已关联纤芯数 : </span><span>{{ item.linesn }}</span>
                                        </p>
                                    </li>
                                    <li class="fiberSite" v-if="item.type != 3" style="border-left: 4px solid #fff;">
                                        <p class="leftIcon" v-show="item.indexType === '起'">{{ item.indexType }}</p>
                                        <p class="leftIcon" v-show="item.indexType === '经'">{{ item.indexType }}</p>
                                        <p class="leftIcon" v-show="item.indexType === '终'">{{ item.indexType }}</p>
                                        <p class="fiberInfo">
                                            <span class="Flabel">{{ item.label }}</span><span v-if="item.type != 1">{{
                                                item.name }}</span>
                                        </p>
                                    </li>
                                </template>
                            </ul>
                        </el-tab-pane>
                        <el-tab-pane label="资源" name="1">
                            <div style="padding-top: 10px; height: 100%">
                                <el-table border stripe size="mini" :data="fiberInfoBox.data.resList" height="100%"
                                    style="width: 100%;">
                                    <el-table-column prop="resName" label="资源名称">
                                    </el-table-column>
                                    <el-table-column prop="resType" label="资源类型" width="80">
                                    </el-table-column>
                                    <el-table-column prop="editType" label="操作类型" width="50">
                                    </el-table-column>
                                </el-table>
                            </div>
                        </el-tab-pane>
                    </el-tabs>
                </div>
            </div>

        <!-- </el-dialog> -->
        <!--光缆示意图查看框-->
        <el-dialog v-dialogDrag custom-class="curdBox" :title="editBox.title" :modal="false" :close-on-click-modal="false"
            :visible.sync="editBox.show" width="750px">
            <editData ref="child" :modelObj="editBox.modelObj" :dataObj="editBox.data" :type="editBox.editType"
                :dbName="'`ry-middledb`'"></editData>
            <div slot="footer" class="dialog-footer">
                <el-button @click="editBox.show = false">关 闭</el-button>
            </div>
        </el-dialog>

        <!--纤芯接续-->
        <el-dialog v-dialogDrag title="纤芯接续" top="5vh" :visible.sync="coreConnectBox.show" :modal="false"
            :close-on-click-modal="false" width="70%">
            <div style="width: 100%; height: 70vh;" v-loading="coreConnectBox.loading">
                <iframe ref="coreConnectBoxIframe" width="100%" height="100%" id="coreConnectBoxIframe"
                    name="coreConnectBoxIframe" style="border: none;" :src="coreConnectBox.iframeSrc"></iframe>
            </div>
            <div slot="footer" class="dialog-footer">
                <el-button @click="coreConnectBox.show = false">关 闭</el-button>
            </div>
        </el-dialog>
    </div>
</template>
  
<script>
import { nameTo_ } from "@/utils"
import {
    generateFiberGraphical
} from "@/api/manageFiber/fiber";
import {
    list,
    getTaskName,
    updateCoordinates
} from "@/api/gltxh/gltxhbj";
import {
    getFiberDataApi,
    coreConnectShowApi,

} from '@/api/gltxh/coreConnect'
import { getModelId, getNewIdByOriginalId } from "@/api/zdwh/zdzywh";
import zdzywhPage from '@/views/commonPage/zdzywh/index';
import editData from "@/views/publicPage/edit/editData";

export default {
    name: "index",
    components: { zdzywhPage, editData },
    data() {
        return {
            tableName: 'SG_TCDEV_FIBERHGS',
            iframeSrc: '/stationWh/html/fiberSketchMap.html',
            //任务id
            taskId: '',

            //光缆示意图 路由/资源
            fiberInfoBox: {
                index: '0',
                show: false,
                data: {
                    topoList: [],
                    resList: []
                }
            },
            //光缆示意图资源查看
            editBox: {
                title: '',
                data: {},
                type: {},
                editType: '',
                show: false,
                modelObj: {}
            },
            // 纤芯接续页面
            coreConnectBox: {
                id: '',
                show: false,
                loading: true,
                iframeSrc: '/stationWh/html/coreConnect.html',
                groupsData: [],
                nodesData: [],
                linksData: [],
                resData: [],
            },
        }
    },
    watch: {
        //解决操作栏固定后表格错位问题
        'tableData': {
            handler() {
                this.$nextTick(() => {
                    this.$refs.multipleTable.doLayout();
                })
            },
            deep: true
        },
    },
    activated() {
        // 查看topo元素
        window['editTopoData'] = (data) => {
            this.editTopoData(data)
        }
        window['getFiberData'] = (id) => {
            this.getFiberData(id)
        }
        // 纤芯接续
        window['coreConnect'] = (data) => {
            this.coreConnectShow(data)
        }
    },
    methods: {
        relatedOpticalCables(data) {
            this.$router.push({
                path: '/fibersource/fiber',
                query: {
                    search: JSON.stringify({ ID: data.rowData.FIBER_ID }),
                    refresh: true,
                }
            })
        },

        generateChart(data) {
            this.$confirm('是否重新生成?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.$message({
                    type: 'success',
                    message: '生成中!'
                });
                if (data.rowData.ID) {
                    generateFiberGraphical({ id: data.rowData.ID }).then(res => {
                        this.$message({
                            type: 'success',
                            message: '生成完成!'
                        });
                    })
                }
            })

        },
        async editTopoData(data) {
            let tit = '查看'
            this.editBox = {
                title: tit + '  ' + JSON.parse(JSON.stringify(data)).resName,
                data: {},
                type: data.resType,
                show: true,
                modelObj: this.editBox.modelObj
            }
            if (data.editType == '0') {
                this.editBox.editType = undefined
            } else {
                this.editBox.editType = "`ry-middledb`"
            }
            getTaskName({ resType: this.editBox.type }).then(res1 => {
                getModelId({ tableName: res1.msg }).then(res2 => {
                    getNewIdByOriginalId({ tableName: res1.msg, originalId: data.id }).then(res3 => {
                        if (res3.msg) {
                            this.editBox.editType = undefined
                            data.id = res3.msg
                        }
                        this.editBox.modelObj = res2.data
                        for (let key in data) {
                            this.editBox.data[nameTo_(key)] = data[key]
                        }
                        this.$nextTick(() => {
                            this.$refs.child.modelObj = this.editBox.modelObj
                            this.$refs.child.dataObj = this.editBox.data
                            this.$refs.child.type = this.editBox.editType
                            this.$refs.child.beforeLoading()
                        })
                    })


                })
            })
        },
        getFiberData(id) {
            getFiberDataApi({
                fiberId: id,
                taskId: this.taskId
            }).then(res => {
                this.$refs.iframe.contentWindow.topoShowFiber(res.data.topoList)
                this.fiberInfoBox.data.resList = res.data.resList
                this.fiberInfoBox.fiberId = id
                let topoList = res.data.topoList
                for (let i = 0; i < topoList.length; i++) {
                    if (topoList[i].type != 3) {
                        topoList[i].label = '接头盒'
                        topoList[i].indexType = '经'
                    }
                }
                topoList[0].label = '起始站点 : '
                topoList[0].indexType = '起'
                topoList[topoList.length - 1].indexType = '终'
                topoList[topoList.length - 1].label = '终止站点 : '
                this.fiberInfoBox.data.topoList = topoList
                this.fiberInfoBox.show = true
            })
        },
        //获取资源展示的数据
        // 纤芯接续页面展示
        coreConnectShow(data) {
            this.coreConnectBox.show = true
            this.coreConnectBox.loading = true
            coreConnectShowApi({ TconId: data.id, TaskId: this.taskId }).then(res => { // +个任务id
                this.coreConnectBox.groupsData = []
                this.coreConnectBox.nodesData = []
                this.coreConnectBox.linksData = []
                this.coreConnectBox.id = data.id
                this.coreConnectBox.loading = false
                res.data.seg.forEach((item, index, arr) => {
                    this.coreConnectBox.groupsData[index] = {
                        SEGID: item.SEGID,
                        SEGNAME: item.SEGNAME,
                        groupLength: item.lines.length,
                    }
                    this.coreConnectBox.nodesData[index] = item.lines
                })
                this.coreConnectBox.linksData = res.data.link
                this.coreConnectBox.loading = false
                this.$nextTick(() => {
                    window.setTimeout(() => {
                        this.$refs.coreConnectBoxIframe.contentWindow.reSetTopo(
                            this.coreConnectBox.groupsData,
                            this.coreConnectBox.nodesData,
                            this.coreConnectBox.linksData,
                            this.isEdit
                        )
                    }, 500)
                })
            })
        },

        saveTemporary() {
            let data = this.$refs.iframe.contentWindow.getCanvasData()
            for (let i = 0; i < data.length; i++) {
                switch (data[i].resType + '') {
                    // 站点
                    case '0':
                        updateCoordinates(data[i]).then(res => {
                        }).catch(e => {
                            this.$message({
                                message: e,
                                type: 'error'
                            });
                        })
                        break;
                    // 接头盒
                    case '1':
                        updateCoordinates(data[i]).then(res => {
                        }).catch(e => {
                            this.$message({
                                message: e,
                                type: 'error'
                            });
                        })
                        break;
                }
            }
            this.$message({ message: '保存完成', type: 'success' });
        },
    },
    created() {
        if (this.$route.query.task_id) {
                this.taskId = this.$route.query.task_id
                list({ taskId: this.taskId }).then(res => {
                    if (res.code == 200) {
                        setTimeout(() => {
                            this.$refs.iframe.contentWindow.reSetTopo(
                                res.data.site.concat(res.data.tcon),
                                res.data.seg,
                                res.data.fiber,
                                false
                            )
                        }, 1000)
                    }
                })

        }
    }


}
</script>
  
<style scoped lang="scss">
.fiberInfoBox {
    position: absolute;
    top: 92px;
    right: 10px;
    border: 2px solid #eeeeee;
    width: 300px;
    height: 50vh;
    background-color: #fff;

    .fiberTitle {
        width: 100%;
        height: 45px;
        line-height: 45px;
        background-color: #fff;
        padding-left: 10px;

        .boxClose {
            width: 25px;
            height: 25px;
            line-height: 45px;
            font-size: 25px;
            float: right;
            margin: 0 10px 0 0;
            cursor: pointer;
        }

        .boxClose:hover {
            color: #047E78;
        }
    }

    .el-tabs {
        width: 100%;
        height: calc(100% - 45px);
        padding: 10px;

        .el-tabs__header,
        .el-tabs__nav-wrap,
        .el-tabs__nav-scroll,
        .el-tabs__nav,
        .el-tabs__item {
            height: 30px;
            line-height: 30px;
            margin: 0;
        }

        .is-active {
            color: #047E78 !important;
        }

        .el-tabs__content {
            height: calc(100% - 30px);
            overflow-y: auto;
        }

        .el-tab-pane {
            height: 100%
        }

        ul {
            margin: 0;
            list-style: none;
            padding: 10px 0 0 20px;

            li {
                width: 100%;
                position: relative;
                border-left: 4px solid #67C23A;
                padding: 0px 0 10px 20px;

                p {
                    margin: 0;
                }

                .leftIcon {
                    position: absolute;
                    left: -17px;
                    width: 30px;
                    height: 30px;
                    background-color: #67C23A;
                    border-radius: 50%;
                    text-align: center;
                    line-height: 30px;
                    color: #fff;
                    z-index: 10;
                }

                .fiberInfo {
                    width: 100%;
                    line-height: 20px;
                    font-size: 14px !important;

                    .Flabel {
                        font-weight: bold;
                        display: inline-block;
                        width: 6rem;
                    }
                }
            }

            .fiberSite .fiberInfo {
                line-height: 20px !important;
            }

            li:last-child {
                padding-bottom: 0;
            }
        }
    }
}
</style>
  