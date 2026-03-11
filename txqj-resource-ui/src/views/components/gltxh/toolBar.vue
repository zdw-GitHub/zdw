<template>
  <div class="commonWh toolBar">
    <div class="box-card" style="padding: 0">
      <div class="toolBarCon" v-loading="loading">
        <TuLi style="position: absolute;bottom: 10px;right: 10px; z-index:10"></TuLi>
        <!--光缆示意图-->
        <div class="iframeSpaceBorder">
          <div class="guidanceBox" v-if="(islead == 0)">
            <ul>
              <li
                :class="{
                    'accomplishStep': sonlead >= 4,
                    'editStep': sonlead >= 0 && sonlead < 4
                  }"
                style="width: calc(12.5% * 3)">
                <span class="el-icon-share"></span>
                路由编制
              </li>
              <li
                :class="{
                    'accomplishStep': sonlead >= 7,
                    'editStep': sonlead >= 4 && sonlead < 7,
                    'awaitStep': sonlead < 4
                   }"
                style="width: calc(12.5% * 3)">
                <span class="el-icon-edit"></span>
                属性维护
              </li>
              <li
                :class="{
                    'accomplishStep': sonlead >= 8,
                    'editStep': sonlead >= 7 && sonlead < 8,
                    'awaitStep': sonlead < 7
                  }"
                style="width: calc(12.5% * 1)">
                <span class="el-icon-s-order"></span>
                数据校验
              </li>
              <li
                :class="{
                    'accomplishStep': sonlead >= 9,
                    'editStep': sonlead >= 8 && sonlead < 9,
                    'awaitStep': sonlead < 8
                  }"
                style="width: calc(12.5% * 1)">
                <span class="el-icon-s-check"></span>
                生成配线
              </li>
            </ul>
            <div style="padding: 10px 6% 10px 10px;" process-status="wait">
              <el-steps :active="sonlead">
                <el-step title="选择站点"></el-step>
                <el-step title="选择接头盒"></el-step>
                <el-step title="连接光缆段"></el-step>
                <el-step title="纤芯接续"></el-step>
                <el-step title="光缆属性维护"></el-step>
                <el-step title="光缆段属性维护"></el-step>
                <el-step title="光缆纤芯编制"></el-step>
                <el-step title="数据校验"></el-step>
                <el-step title="生成配线"></el-step>
              </el-steps>
            </div>
          </div>
          <!--引入topo-->
          <div class="iframeSpace" :style="{'height': (islead == 0) ? 'calc(100% - 123px)' : '100%'}">
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
                          <span class="Flabel">光缆段名称 : </span><span>{{item.name}}</span>
                        </p>
                        <p class="fiberInfo">
                          <span class="Flabel">已关联纤芯数 : </span><span>{{item.linesn}}</span>
                        </p>
                      </li>
                      <li class="fiberSite" v-if="item.type != 3" style="border-left: 4px solid #fff;">
                        <p class="leftIcon" v-show="item.indexType==='起'" >{{item.indexType}}</p>
                        <p class="leftIcon" v-show="item.indexType==='经'">{{item.indexType}}</p>
                        <p class="leftIcon" v-show="item.indexType==='终'">{{item.indexType}}</p>
                        <p class="fiberInfo">
                          <span class="Flabel">{{item.label}}</span><span  v-if="item.type != 1">{{item.name}}</span>
                        </p>
                      </li>
                    </template>
                  </ul>
                </el-tab-pane>
                <el-tab-pane label="资源" name="1">
                  <div style="padding-top: 10px; height: 100%">
                    <el-table
                      size="mini"
                      stripe
                      border
                      :data="fiberInfoBox.data.resList"
                      height="100%"
                      style="width: 100%;">
                      <el-table-column
                        prop="resName"
                        label="资源名称"
                      >
                      </el-table-column>
                      <el-table-column
                        prop="resType"
                        label="资源类型"
                        width="80">
                      </el-table-column>
                      <el-table-column
                        prop="editType"
                        label="操作类型"
                        width="50">
                      </el-table-column>
                    </el-table>
                  </div>
                </el-tab-pane>
              </el-tabs>
            </div>
            <div class="zyTitle">
              资源展示
            </div>
            <div style="position: absolute;left: calc(15% + (9 * 50px));border-left: 2px solid #eee;padding-left: 15px;">
              <el-button
                size="mini"
                type="primary"
                v-hasPermi="['gltxh:gltxhwh:editTopo']"
                @click="shortSubmit()">保存</el-button>
              <el-button
                size="mini"
                v-if="!isEdit"
                type="primary"
                @click="createData()">
                资源清单
              </el-button>
              <el-button
                size="mini"
                v-if="isEdit && !(islead == 0)"
                v-hasPermi="['gltxh:gltxhwh:editTopo']"
                @click="createFiber()"
                type="primary">
                生成光缆
              </el-button>
            </div>
            <div style="position: absolute;right: 10px;">
              <el-button
                size="mini"
                v-if="isEdit && (!(islead == 0)) || sonlead == 7"
                v-hasPermi="['gltxh:gltxhwh:editTopo']"
                @click="createData()"
                type="primary">
                数据校验
              </el-button>
              <el-button
                size="mini"
                v-if="isEdit && (!(islead == 0) || sonlead >= 8)"
                v-hasPermi="['gltxh:gltxhwh:editTopo']"
                v-loadmore="generateLoadmore"
                :disabled="generateDisabled"
                @click="newFlow()"
                type="primary">
                生成配线
              </el-button>
              <el-button
                size="mini"
                v-if="isEdit && islead == 0"
                v-hasPermi="['gltxh:gltxhwh:editTopo']"
                @click="changeStep('last')"
                :disabled="sonlead == 0"
                :loading="isleadChangeLoading"
                icon="el-icon-back"
                type="primary">
                上一步
              </el-button>
              <el-button
                size="mini"
                v-if="isEdit && islead == 0"
                v-hasPermi="['gltxh:gltxhwh:editTopo']"
                @click="changeStep('next')"
                :disabled="sonlead == 9"
                :loading="isleadChangeLoading"
                type="primary">
                下一步
                <i class="el-icon-right el-icon--right"></i>
              </el-button>
              <el-button
                size="mini"
                v-if="islead == 0"
                @click="stepTipShow = true"
                icon="el-icon-question">
                说明
              </el-button>
              <el-button
                size="mini"
                v-if="isSee"
                @click="$router.back()"
                type="primary">
                返回
              </el-button>
            </div>
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
        </div>
      </div>
    </div>
    <!--点击保存时的弹出框-->
    <el-dialog
      v-dialogDrag
      :modal="false"
      title="保存确认提示"
      top="35vh"
      :close-on-click-modal="false"
      :visible.sync="dialogShortVisible"
      width="25%">
      <span>
        <img src="../../../assets/gltxh_img/警告.png"/>
        保存后数据将保存到临时数据表，从“光缆图形化维护”列表进入，可继续编辑。
      </span>
      <div style="margin-top: 20px;">
        <div>
          <span>任务名称：</span>
          <el-input style="width: 70%" v-model="taskname"></el-input>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogShortVisible = false">取 消</el-button>
        <el-button type="primary" @click="(dialogShortVisible = false),saveTemporary()">确 定</el-button>
      </div>
    </el-dialog>
    <!--批量操作资源-->
    <div class="continuousBox" v-if="continuous" :style="{'width': continuousShow ? '400px' : '0'}">
      <div class="switch" @click="continuousShow = !continuousShow" :class="{'el-icon-d-arrow-left': continuousShow,'el-icon-d-arrow-right': !continuousShow}"></div>
      <div class="continuousBoxCon" v-if="continuousShow">
        <p class="title">选择资源</p>
        <div class="con">
          <ul>
            <li
              v-for="(item, key) in continuousList"
              :class="{'focus': continuousIndex == key}"
              @click="continuousIndex = key"
            >{{item.resName}}<span class="el-icon-edit"></span>
            </li>
          </ul>
        </div>
      </div>
    </div>
    <!--修改资源-->
    <el-dialog
      v-dialogDrag
      custom-class="curdBox"
      :title="editBox.title"
      :modal="false"
      :close-on-click-modal="false"
      :visible.sync="editBox.show"
      width="750px">
      <editData
        ref="child"
        :modelObj="editBox.modelObj"
        :dataObj="editBox.data"
        :type="editBox.editType"
        :dbName="'`ry-middledb`'"
      ></editData>
      <div slot="footer" class="dialog-footer" v-if="isEdit">
        <el-button @click="editBox.show = false">关 闭</el-button>
        <el-button type="primary" @click="editCommit()">保 存</el-button>
      </div>
      <div slot="footer" class="dialog-footer" v-if="!isEdit">
        <el-button @click="editBox.show = false">关 闭</el-button>
      </div>
    </el-dialog>
    <!--选择站点-->
    <el-dialog
      v-dialogDrag
      title="选择站点"
      append-to-body
      :modal="false"
      :close-on-click-modal="false"
      :visible.sync="addSiteBox.show"
      width="50%"
      top="10vh">
      <div style="height: 60vh">
        <el-input
          prefix-icon="el-icon-search"
          style="width: 100%"
          placeholder="请输入站点名称"
          v-model="addSiteBox.searchStation">
        </el-input>
        <div class="siteTree" style="height: calc(60vh - 36px); overflow-y: auto">
          <el-tree
            class="filter-tree"
            :data="addSiteBox.tree"
            :props="{
              children: 'children',
              label: 'label'
            }"
            check-strictly
            highlight-current
            node-key="id"
            @check-change="nodeClick"
            @node-click="nodeClick"
            :filter-node-method="filterNode"
            ref="tree">
          </el-tree>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addSiteBox.show = false">取 消</el-button>
        <el-button type="primary" @click="addSiteBoxCommit()">确 定</el-button>
      </div>
    </el-dialog>
    <!--选择光缆-->
    <el-dialog
      v-dialogDrag
      custom-class="curdBox"
      title="选择光缆"
      append-to-body
      :modal="false"
      :close-on-click-modal="false"
      :visible.sync="addLinkBox.show"
      width="70%">
      <div style="width: 100%; height: 70vh;" class="commonWh">
        <div>
          <el-form label-width="100px" >
            <el-col :span="10">
              <el-form-item label="名称:" >
                <el-input v-model="addLinkBox.searchLinkName" style="width:180px" size="mini"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="10">
              <el-form-item label="起/终站点:" >
                <el-input v-model="addLinkBox.startStation" style="width:180px" size="mini"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-form-item>
                <el-button type="primary" size="mini" @click="getLinkData">查询</el-button>
              </el-form-item>
            </el-col>
          </el-form>
        </div>
        <el-table
          stripe
          border
          height="calc(100% - 60px)"
          style="width: 100%;"
          ref="multipleTable"
          :data="addLinkBox.linkTableData"
          v-loading="addLinkBox.loading"
          @selection-change="handleSelectionChange"
          :row-key="getRowKey">
          <el-table-column
            type="selection"
            :reserve-selection="true"
            width="55"/>
          <el-table-column
            label="序号"
            width="80"
            align="center"
            :formatter="snFormatter">
          </el-table-column>
          <el-table-column
            prop="snname"
            label="省内名称"
            align="center"
          >
          </el-table-column>
          <el-table-column
            prop="name"
            label="光缆名称"
            align="center"
          >
          </el-table-column>
          <el-table-column
            prop="aResobjid"
            label="起始站点"
            align="center">
          </el-table-column>
          <el-table-column
            prop="zResobjid"
            label="终止站点"
            align="center">
          </el-table-column>
        </el-table>
        <pagination
          v-show="addLinkBox.total>0"
          :total="addLinkBox.total"
          :page.sync="addLinkBox.pageIndex"
          :limit.sync="addLinkBox.pageSize"
          @pagination="getLinkData"
        />
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addLinkBox.show = false">取 消</el-button>
        <el-button type="primary" @click="linkCommit()">确 定</el-button>
      </div>
    </el-dialog>
    <!--绘制光缆-->
    <el-dialog
      v-dialogDrag
      title="光缆段芯数选择"
      :modal="false"
      :close-on-click-modal="false"
      :visible.sync="addFiberBox.show"
      width="18%"
      top="10%">
      <span style="margin-right: 20px">芯数</span>
      <el-select
        v-model="addFiberBox.selLineNum"
        filterable
        allow-create
        placeholder="请选择">
        <el-option
          v-for="item in addFiberBox.options"
          :key="item.code"
          :value="item.name">
        </el-option>
      </el-select>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addFiberBox.show = false">取 消</el-button>
        <el-button type="primary" @click="addFiberBoxCommit()" :loading="addFiberBoxCommitLoading" :disabled="addFiberBox.selLineNum == ''">确 定</el-button>
      </div>
    </el-dialog>
    <!--纤芯接续-->
    <el-dialog
      v-dialogDrag
      title="纤芯接续"
      top="5vh"
      :visible.sync="coreConnectBox.show"
      :modal="false"
      :close-on-click-modal="false"
      width="70%">
      <div style="width: 100%; height: 70vh;" v-loading="coreConnectBox.loading">
        <iframe
          ref="coreConnectBoxIframe"
          width="100%"
          height="100%"
          id="coreConnectBoxIframe"
          name="coreConnectBoxIframe"
          style="border: none;"
          :src="coreConnectBox.iframeSrc"
        ></iframe>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="coreConnectBox.show = false">关 闭</el-button>
        <!-- <el-button type="primary" @click="saveAndCreateFiber">保存纤芯接续并生成光缆</el-button> -->
      </div>
    </el-dialog>
    <!--批量连接纤芯-->
    <el-dialog
      v-dialogDrag
      title="批量连接纤芯"
      top="15vh"
      :visible.sync="batchConnectionsBox.show"
      :modal="false"
      :close-on-click-modal="false"
      width="30%">
      <div style="width: 100%; height: 50vh">
        <el-form
          :model="batchConnectionsBox.data"
          :rules="batchConnectionsBox.rules"
          ref="batchConnectionsBox"
          label-width="150px">
          <el-form-item label="起始端" prop="aSegId">
            <el-select
              v-model="batchConnectionsBox.data.aSegId"
              @change="segIdChange($event, 'aSegId'), verificationBatchConnectionsBox()"
            >
              <el-option
                v-for="(item, key) in batchConnectionsBox.selectOption"
                :label="item.SEGNAME"
                :value="item.SEGID"
                :disabled="item.SEGID == batchConnectionsBox.data.zSegId"
                :key="key"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="起始纤芯序号 : " prop="aStartSn">
            <el-input-number
              v-model="batchConnectionsBox.data.aStartSn"
              :step="1"
              step-strictly
              :min="1"
              :max="getMaxSn('aSegId')"
              @change="ifRight('aStartSn'), verificationBatchConnectionsBox()"
            />
          </el-form-item>
          <el-form-item label="终止纤芯序号 : " prop="aEndSn">
            <el-input-number
              v-model="batchConnectionsBox.data.aEndSn"
              :step="1"
              step-strictly
              :min="1"
              :max="getMaxSn('aSegId')"
              @change="ifRight('aEndSn'), verificationBatchConnectionsBox()"
            />
          </el-form-item>
          <el-form-item label="终止端" prop="zSegId">
            <el-select
              v-model="batchConnectionsBox.data.zSegId"
              @change="segIdChange($event, 'zSegId'), verificationBatchConnectionsBox()">
              <el-option
                v-for="(item, key) in batchConnectionsBox.selectOption"
                :label="item.SEGNAME"
                :value="item.SEGID"
                :disabled="item.SEGID == batchConnectionsBox.data.aSegId"
                :key="key"
              ></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="起始纤芯序号 : " prop="zStartSn">
            <el-input-number
              v-model="batchConnectionsBox.data.zStartSn"
              :step="1"
              step-strictly
              :min="1"
              :max="getMaxSn('zSegId')"
              @change="ifRight('zStartSn'), verificationBatchConnectionsBox()"
            />
          </el-form-item>
          <el-form-item label="终止纤芯序号 : " prop="zEndSn">
            <el-input-number
              v-model="batchConnectionsBox.data.zEndSn"
              :step="1"
              step-strictly
              :min="1"
              :max="getMaxSn('zSegId')"
              @change="ifRight('zEndSn'), verificationBatchConnectionsBox()"
            />
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="commitbatchConnections">提交</el-button>
        <el-button @click="batchConnectionsBox.show = false">取消</el-button>
      </div>
    </el-dialog>
    <!--批量删除纤芯-->
    <el-dialog
      v-dialogDrag
      title="批量删除纤芯"
      top="15vh"
      :visible.sync="delMoreBox.show"
      :modal="false"
      :close-on-click-modal="false"
      width="30%">
      <div style="width: 100%; height: 30vh">
        <el-form
          :model="delMoreBox.data"
          :rules="delMoreBox.rules"
          ref="delMoreBox"
          label-width="150px">
          <el-form-item label="起始纤芯序号 : " prop="startSn">
            <el-input-number
              v-model="delMoreBox.data.startSn"
              :step="1"
              step-strictly
              :min="1"
              :max="delMoreBox.max"
            />
          </el-form-item>
          <el-form-item label="终止纤芯序号 : " prop="endSn">
            <el-input-number
              v-model="delMoreBox.data.endSn"
              :step="1"
              step-strictly
              :min="1"
              :max="delMoreBox.max"
            />
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="delMoreBoxCommit">提交</el-button>
        <el-button @click="delMoreBox.show = false">取消</el-button>
      </div>
    </el-dialog>
    <!--生成数据-->
    <el-dialog
      v-dialogDrag
      title="生成数据资源清单"
      top="10vh"
      :visible.sync="createDataBox.show"
      :modal="false"
      :close-on-click-modal="false"
      width="50%">
      <div style="width: 100%; height: 70vh; overflow-y: auto">
        <div style="border-bottom:1px solid black;margin-bottom:10px">
          <span>光缆</span>
        </div>
        <el-table
          :data="createDataBox.data.fiber"
          style="width: 100%">
          <el-table-column
            prop="resName"
            label="资源名称">
          </el-table-column>
          <el-table-column
            prop="resType"
            label="资源类型"
            width="100">
          </el-table-column>
          <el-table-column
            prop="editType"
            label="操作类型"
            width="100">
          </el-table-column>
          <el-table-column
            label="数据校验"
            width="150">
            <template slot-scope="scope">
              <span v-if="scope.row.checkoutType == 0" style="color: #67C23A">
                通过
              </span>
              <span v-else style="color: #F56C6C">
                {{scope.row.checkout}}
              </span>
            </template>
          </el-table-column>
        </el-table>
        <div style="border-bottom:1px solid black;margin-bottom: 10px;margin-top: 20px;">
          <span>光缆段</span>
        </div>
        <el-table
          :data="createDataBox.data.seg"
          style="width: 100%">
          <el-table-column
            prop="resName"
            label="资源名称">
          </el-table-column>
          <el-table-column
            prop="resType"
            label="资源类型"
            width="100">
          </el-table-column>
          <el-table-column
            prop="editType"
            label="操作类型"
            width="100">
          </el-table-column>
          <el-table-column
            label="数据校验"
            width="150">
            <template slot-scope="scope">
              <span v-if="scope.row.checkoutType == 0" style="color: #67C23A">
                通过
              </span>
              <span v-else style="color: #F56C6C">
                {{scope.row.checkout}}
              </span>
            </template>
          </el-table-column>
        </el-table>
        <div style="border-bottom:1px solid black;margin-bottom: 10px;margin-top: 20px;">
          <span>接头盒</span>
        </div>
        <el-table
          :data="createDataBox.data.tcon"
          style="width: 100%">
          <el-table-column
            prop="resName"
            label="资源名称">
          </el-table-column>
          <el-table-column
            prop="resType"
            label="资源类型"
            width="100">
          </el-table-column>
          <el-table-column
            prop="editType"
            label="操作类型"
            width="100">
          </el-table-column>
          <el-table-column
            label="数据校验"
            width="150">
            <template slot-scope="scope">
              <span v-if="scope.row.checkoutType == 0" style="color: #67C23A">
                通过
              </span>
              <span v-else style="color: #F56C6C">
                {{scope.row.checkout}}
              </span>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <span slot="footer" class="dialog-footer">
        <div v-if="isEdit && islead !== 0">
          <el-button type="primary" @click="dataToWarehouse()" v-if="false">同 步</el-button>
          <el-button @click="createDataBox.show = false">取 消</el-button>
        </div>
        <div v-else>
          <el-button @click="createDataBox.show = false">关 闭</el-button>
        </div>
      </span>
    </el-dialog>
    <toDatawarehouse
      ref="toDatawarehouse"
      :islead="islead + ''"
      :taskId="taskId + ''"
      :taskName="taskData.taskName + ''"
    />
    <!--纤芯详情-->
    <el-dialog
      v-dialogDrag
      :title="fiberCoreDetail.name"
      top="5vh"
      class="fiberCoreDetail"
      :visible.sync="fiberCoreDetail.show"
      :modal="false"
      :close-on-click-modal="false"
      width="90%">
      <div style="width: 100%; height: 80vh;" v-loading="fiberCoreDetail.loading">
        <fiberCoreDetail
          ref="fiberCoreDetail"
          :data="fiberCoreDetail.data"
          :isEdit="isEdit"
          @openFiberCoreDetail="openFiberCoreDetail({
            resId: fiberCoreDetail.id,
            name: fiberCoreDetail.name,
          })"
          @loadingEnd="fiberCoreDetail.loading = false"
        />
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fiberCoreDetail.show = false">关 闭</el-button>
        <!-- <el-button type="primary" @click="saveAndCreateFiber">保存纤芯接续并生成光缆</el-button> -->
      </div>
    </el-dialog>
    <!--查看生成光缆与光缆段-->
    <el-dialog
      v-dialogDrag
      title="查看生成光缆与光缆段"
      top="10vh"
      :visible.sync="seeCreateFiber.show"
      :modal="false"
      :close-on-click-modal="false"
      width="50%">
      <div style="width: 100%; height: 70vh;">
        <seeCreateFiber
          ref="seeCreateFiber"
          :segs="seeCreateFiber.segs"
          :fibers="seeCreateFiber.fibers"
        />
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="seeCreateFiber.show = false">关 闭</el-button>
      </div>
    </el-dialog>
    <!--查看步骤说明-->
    <el-dialog
      v-dialogDrag
      title="操作说明"
      top="10vh"
      :visible.sync="stepTipShow"
      :modal="false"
      :close-on-click-modal="false"
      width="90%">
      <div style="width: 100%; height: 70vh;">
        <stepTipImgs :step="sonlead"/>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="stepTipShow = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {nameTo_, nameToTuoFeng} from "@/utils"
  import TuLi from "./tuli"
  import editData from "@/views/publicPage/edit/editData";
  import { getInfo } from '@/api/login'
  import toDatawarehouse from "@/views/components/gltxh/toDatawarehouse.vue"
  import fiberCoreDetail from "@/views/components/gltxh/fiberCoreDetail.vue"
  import seeCreateFiber from "@/views/components/gltxh/seeCreateFiber.vue"
  import stepTipImgs from "@/views/components/gltxh/stepTipImgs.vue"

  import {
    chooseLink,
    selectLink,
    selectSite,
    addSite,
    selectLineNum,
    list,
    delSite,
    addTcon,
    delTcon,
    delSeg,
    delSegData,
    delFiberData,
    addSeg,
    editSite,
    editTcon,
    editSeg,
    xgTcon,
    xgSeg,
    delFiber,
    xgFiber,
    getParaValueByresType,
    tableDropDownBox,
    queryParaValueByRes,
    singleAttributeVerification,
    getCheckDate,
    getTaskName,
    saveFiber,
    getFiberCoreDetailApi,
    updateCoordinates
  } from "@/api/gltxh/gltxhbj";
  import { getOneTask, updataTaskApi } from "@/api/gltxh/gltxhwh";
  import { getModelId } from "@/api/zdwh/zdzywh";
  import {
    coreConnectShowApi,
    addLinkApi,
    delLinkApi,
    commitbatchConnectionsApi,
    delMoreApi,
    saveAndCreateFiberApi,
    getFiberDataApi
  } from '@/api/gltxh/coreConnect'

  import {synchronousOpticalCableRelatedData} from "@/api/gltxh/toDatawarehouse";
  export default {
    components: {TuLi, toDatawarehouse, editData, fiberCoreDetail, seeCreateFiber, stepTipImgs},
    name: "ToolBar",
    props:{
      tipShow:{
        type:Boolean, //类型
        default:false //默认值
      }
    },
    data(){
      var verifySn = (rule, value, callback) => {
        let a = Math.abs(this.batchConnectionsBox.data.aStartSn - this.batchConnectionsBox.data.aEndSn)
        let z = Math.abs(this.batchConnectionsBox.data.zStartSn - this.batchConnectionsBox.data.zEndSn)
        if (a - z != 0) {
          return callback(new Error('纤芯数量不对等'))
        } else {
          return callback()
        }
      };
      return {
        loading: false,
        //是否第一次编辑资源
        ifFirstFiber:true,
        iframeSrc:'/stationWh/html/fiberImaging.html',
        //修改光缆段
        dialogSegVisible:false,
        //修改站点属性框
        dialogTconVisible:false,
        //保存（保存）弹框标识
        dialogShortVisible:false,
        //保存时的任务名称
        taskname:'',
        //保存时的备注
        notes:'',
        //光缆芯数的选择
        options:[],
        //纤芯数量
        value:'',
        //  此任务id
        taskId: this.$route.params.dictId,
        taskData: {},
        //存储修改属性信息
        data1:{},
        //  下拉框数据
        downBox:[],
        //  校验规则的保存
        rules: {},
        // 修改框loadIng
        editBox: {
          title: '',
          data: {},
          type: {},
          editType: '',
          show: false,
          modelObj: {}
        },
        // topo数据
        topoBox: {
          // 点
          nodes: [],
          // 线
          segs: [],
          // 光缆
          fibers: []
        },
        // 新增光缆
        addFiberBox: {
          show: false,
          options: [],
          selLineNum: '',
          aNode: {},
          zNode: {},
        },
        addFiberBoxCommitLoading:false,
        // 选择站点
        addSiteBox: {
          show: false,
          tree: [],
          // 选择站点搜索框输入的值
          searchStation:'',
          defaultProps: {
            children: 'children',
            label: 'label'
          },
          selectNode: {},
          x: '',
          y: '',
        },
        // 选择光缆
        addLinkBox: {
          x: '',
          y: '',
          //多选
          multipleSelection: [],
          //分页
          total:0,
          pageSize: 15,
          pageIndex: 1,
          loading: true,
          show: false,
          // 光缆查询
          searchLinkName: '',//光缆名称
          startStation: '', //起始站点
          endStation: '', // 终止站点
          // 光缆表格
          linkTableData: [],
        },
        // 纤芯接续页面
        coreConnectBox: {
          id: '',
          show: false,
          loading: true,
          iframeSrc:'/stationWh/html/coreConnect.html',
          groupsData: [],
          nodesData: [],
          linksData: [],
          resData: [],
        },
        // 批量连接纤芯
        batchConnectionsBox: {
          title: '',
          show: false,
          aMax: 0,
          zMax: 0,
          data: {
            aSegId: '',
            aStartSn: '',
            aEndSn: '',
            zSegId: '',
            zStartSn: '',
            zEndSn: '',
          },
          selectOption: [],
          rules: {
            aSegId: [{required: true, message: '此项不能为空', trigger: "blur"}],
            aStartSn: [
              {required: true, message: '此项不能为空', trigger: "blur"},
              {validator: verifySn, trigger: "change"},
            ],
            aEndSn: [
              {required: true, message: '此项不能为空', trigger: "blur"},
              {validator: verifySn, trigger: "change"},
            ],
            zSegId: [{required: true, message: '此项不能为空', trigger: "blur"}],
            zStartSn: [
              {required: true, message: '此项不能为空', trigger: "blur"},
              {validator: verifySn, trigger: "change"},
            ],
            zEndSn: [
              {required: true, message: '此项不能为空', trigger: "blur"},
              {validator: verifySn, trigger: "change"},
            ],
          }
        },
        // 批量删除纤芯
        delMoreBox: {
          title: '',
          show: false,
          max: 0,
          data: {
            segId: '',
            startSn: 0,
            endSn: 0,
          },
          rules: {
            startSn: [{required: true, message: '此项不能为空'}],
            endSn: [{required: true, message: '此项不能为空'}],
          }
        },
        // 光缆数据 路由/资源
        fiberInfoBox: {
          index: '0',
          show: false,
          data: {
            topoList: [],
            resList: []
          }
        },
        // 生成数据
        createDataBox:{
          show: false,
          data: {
            fiber: [],
            seg: [],
            tcon: [],
          }
        },
        fiberCoreDetail: {
          loading: false,
          show: false,
          title: '',
          id: '',
          data: {}
        },
        // 是否引导模式
        islead: null,
        // 引导模式子步骤
        sonlead: null,
        // 操作步骤改变时 等待后台存储完成
        isleadChangeLoading: false,
        // 连续操作的list
        continuousList: [],
        // 连续操作的index
        continuousIndex: 0,
        // 是否连续操作
        continuous: false,
        // 连续操作框显示隐藏
        continuousShow: false,
        // 是否连续纤芯接续
        continuousLinkLine: false,
        // 光缆是否连续修改
        continuousEditFiber: false,
        // 光缆段是否连续修改
        continuousEditLink: false,
        // 是否连续查看光缆纤芯
        continuousSeeFiberLink: false,
        // 是否允许topo修改
        isEdit: false,
        isSee: false,
        // 查看生成光缆框
        seeCreateFiber: {
          show: false,
          segs: [],
          fibers: [],
        },
        stepTipShow: this.tipShow,
        generateLoadmore: false,
        generateDisabled: false,
      }
    },
    watch: {
      searchStation(val) {
        this.$refs.tree.filter(val);
      },
      'addSiteBox.searchStation'(val) {
        this.$refs.tree.filter(val);
      },
      // 连续编辑资源 点击事件
      continuousIndex (newVal) {
        if (newVal >= this.continuousList.length) {
          this.continuousIndex = 0
        }
        if (this.continuousLinkLine) {
          this.coreConnectShow(this.continuousList[newVal])
        } else if (this.continuousEditFiber) {
          this.editTopoData(this.continuousList[newVal])
        } else if (this.continuousEditLink) {
          this.editTopoData(this.continuousList[newVal])
        } else if (this.continuousSeeFiberLink) {
          this.openFiberCoreDetail(this.continuousList[newVal])
        }
      },
      'editBox.show' (newVal) {
        if (!newVal && this.continuous) {
          this.continuous = false
        }
      },

      'coreConnectBox.show' (newVal) {
        if (!newVal && this.continuous) {
          this.continuous = false
        }
      },
      'fiberCoreDetail.show'(newVal) {
        if (!newVal && this.continuous) {
          this.continuous = false
        }
      },
      sonlead (newVal, oldVal) {
        this.isleadChangeLoading = true
        updataTaskApi({
          id: this.taskId,
          sonlead: newVal,
        }).then(res => {
          this.isleadChangeLoading = false
        })
      },
    },
    activated () {
      this.isSee = this.$route.query && this.$route.query.isSee == 'true'
      this.getTaskData().then(() => {
        let timeOut = window.setInterval(() => {
          try {
            this.$refs.iframe.contentWindow.reSetTopo()
            window.clearInterval(timeOut)
            this.getIsEdit()
            this.getTopoData()
            this.getSiteTree()
            this.getLinkData()
            this.getFiberNumOptions()
          } catch (e) {
          }
        })
      })
    },
    mounted () {
      this.isSee = this.$route.query && this.$route.query.isSee == 'true'
      this.getTaskData().then(() => {
        let timeOut = window.setInterval(() => {
          try {
            this.$refs.iframe.contentWindow.reSetTopo()
            window.clearInterval(timeOut)
            this.getIsEdit()
            this.getTopoData()
            this.getSiteTree()
            this.getLinkData()
            this.getFiberNumOptions()
          } catch (e) {
          }
        })
      })
    },
    methods:{
      /* 保存 */
      shortSubmit:function () {
        this.taskname= this.taskData.taskName
        this.dialogShortVisible = true
      },
      //树形控件过滤
      filterNode(value, data) {
        if (!value) return true;
        return data.label.indexOf(value) !== -1;
      },
      //获取资源展示的数据
      // 纤芯接续页面展示
      coreConnectShow (data) {
        this.coreConnectBox.show = true
        this.coreConnectBox.loading = true
        coreConnectShowApi({TconId: data.id, TaskId: this.taskId}).then(res => { // +个任务id
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
            }, 100)
          })
        })
      },
      // 获取站点树
      getSiteTree () {
        selectSite().then(res=>{
          this.addSiteBox.tree = res.data
        })
      },
      // 获取光缆表格
      getLinkData() {
        this.addLinkBox.loading = true
        let params = {
          name: this.addLinkBox.searchLinkName,
          aResobjid: this.addLinkBox.startStation,
          pageNum: this.addLinkBox.pageIndex,
          pageSize: this.addLinkBox.pageSize
        }
        selectLink(params).then(res=>{
          this.addLinkBox.linkTableData = res.rows
          this.addLinkBox.total = res.total
          this.addLinkBox.loading = false
        })
      },
      //在树形控件中选中节点
      nodeClick(data, checked, node){
        if (checked.level == 2) {
          this.addSiteBox.selectNode = data
        } else if (checked.level == 3) {
          this.addSiteBox.selectNode = checked.parent.data
          this.addSiteBox.selectNode.odfid = data.id
        } else {
          this.addSiteBox.selectNode = undefined
        }
      },
      async getTopoData (seeCreateFiber) {
        await list({taskId:this.taskId}).then(res=>{
          if(res.code == 200){
            this.topoBox = {
              nodes: res.data.site.concat(res.data.tcon),
              segs: res.data.seg,
              fibers: res.data.fiber,
            }
            if (this.islead == '0') {
              this.$refs.iframe.contentWindow.setStep(this.sonlead)
            }
            this.$refs.iframe.contentWindow.reSetTopo(
              this.topoBox.nodes,
              this.topoBox.segs,
              this.topoBox.fibers,
              this.isEdit
            )
            if (seeCreateFiber) {
              window.setTimeout(() => {
                this.seeCreateFiber = {
                  show: true,
                  segs: this.topoBox.segs,
                  fibers: this.topoBox.fibers,
                }
              }, 1000)
            }
          }
        })
      },
      snFormatter: function (row, col, value, index) {
        return this.addLinkBox.pageSize * (this.addLinkBox.pageIndex - 1) + index + 1;
        /* return index + 1 */
      },
      // 光缆表格多选框
      handleSelectionChange(val) {
        this.addLinkBox.multipleSelection = val;
      },
      getRowKey(row) {
        return row.id
      },
      // 提交选择光缆
      linkCommit() {
        var newLink = []
        for(var i=0;i<this.addLinkBox.multipleSelection.length;i++) {
          newLink.push(this.addLinkBox.multipleSelection[i].id)
        }
        let params = {
          fiberIdListJsonStr: JSON.stringify(newLink),
          taskId: this.taskId
        }
        chooseLink(params).then(res=>{
          res= res.data
          this.getTopoData()
          this.addLinkBox.show = false
        }).catch(e => {
      })
      },
      // 选择光缆
      addLinkShow (x, y) {
        this.addLinkBox.show = true
        this.addLinkBox.x = x
        this.addLinkBox.y = y
        this.addLinkBox.searchLinkName = undefined
        this.addLinkBox.startStation = undefined
        this.getLinkData()
        this.$refs.multipleTable.clearSelection();
      },
      // 选择站点
      addSiteBoxShow (x, y) {
        this.addSiteBox.show = true
        this.addSiteBox.selectNode = {}
        this.addSiteBox.x = x
        this.addSiteBox.y = y
        this.$nextTick(() => {
          this.$refs.tree.setCheckedNodes([])
        })
      },
      // 选择站点 提交
      addSiteBoxCommit (x, y) {
        let params = {
          addresX: this.addSiteBox.x,
          addresY: this.addSiteBox.y,
          editType: '0',
          // owner: window.sessionStorage.getItem('userName'),
          resId: this.addSiteBox.selectNode.id,
          odfid: this.addSiteBox.selectNode.odfid,
          resName: this.addSiteBox.selectNode.label,
          resType: '0',
          taskId: this.taskId,
          voltageclassname: this.addSiteBox.selectNode.VOLTAGECLASSNAME,
        }
        addSite(params).then(res=>{
          if(res.code == 200){
            res.data.voltageclassname = params.voltageclassname
            this.$refs.iframe.contentWindow.addNode(res.data)
            this.addSiteBox.show = false
          }
        })
      },
      //  光缆段添加接头盒 =====>> 原逻辑 删除线+添加接头盒+连线两端 改为====>>新增接头盒 调用后台方法（实现删除+连线）
      addBox(lineName, dataType, taskShowId, id, data, x, y) {
        this.$confirm('是否在"' + lineName + '"光缆段上添加接头盒 ?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let params = { //生成接头盒
            addresX: x,
            addresY: y,
            editType: '1',
            owner: window.sessionStorage.getItem('userName'),
            resType: '1',
            taskId: this.taskId,
            resName: '接头盒',
            name: '接头盒'
          }
          if(data.editType == '1') { //新增状态
            delSeg(data).then(res=>{ // 撤销方法
              if(res.code == 200){
                this.$refs.iframe.contentWindow.delNode(dataType, taskShowId, id)
                addTcon(params).then(ress=>{
                  if(ress.code == 200) {
                    this.$refs.iframe.contentWindow.addNode(ress.data)
                    let params1={
                      aResobjid: data.aResobjid,
                      zResobjid: ress.data.resId,
                      editType: '1',
                      owner: window.sessionStorage.getItem('userName'),
                      resType: '3',
                      taskId: this.taskId,
                      resName: data.resName + '~接头盒',
                      name: data.resName + '~接头盒',
                      lineNumber: data.lineNumber,
                      aresType: '0',
                      zresType: '1',
                    }
                    addSeg(params1).then(resss=>{
                      if(resss.code == 200){
                        this.$refs.iframe.contentWindow.addLink(resss.data)
                        let params2={
                          aResobjid: ress.data.resId,
                          zResobjid: data.zResobjid,
                          editType: '1',
                          owner: window.sessionStorage.getItem('userName'),
                          resType: '3',
                          taskId: this.taskId,
                          resName: '~接头盒'+ data.resName ,
                          name:'~接头盒'+ data.resName ,
                          lineNumber: data.lineNumber,
                          aresType: '1',
                          zresType: '0',
                        }
                        addSeg(params2).then(ressss=>{
                          if(ressss.code == 200){
                            this.$refs.iframe.contentWindow.addLink(ressss.data)
                            this.getTopoData()
                          }
                        })
                      }
                    })
                  }
                })
              }
            })
          } else {
            delSegData(data).then(res=>{  // 修改和查询状态下的删除方法
              if(res.code == 200){
                this.$refs.iframe.contentWindow.delNode(dataType, taskShowId, id)
                addTcon(params).then(ress=>{
                  if(ress.code == 200) {
                    this.$refs.iframe.contentWindow.addNode(ress.data)
                    let params1={
                      aResobjid: data.aResobjid,
                      zResobjid: ress.data.resId,
                      editType: '1',
                      owner: window.sessionStorage.getItem('userName'),
                      resType: '3',
                      taskId: this.taskId,
                      resName: data.resName + '~接头盒',
                      name: data.resName + '~接头盒',
                      lineNumber: data.lineNumber,
                      aresType: '0',
                      zresType: '1',
                    }
                    addSeg(params1).then(resss=>{
                      if(resss.code == 200){
                        this.$refs.iframe.contentWindow.addLink(resss.data)
                        let params2={
                          aResobjid: ress.data.resId,
                          zResobjid: data.zResobjid,
                          editType: '1',
                          owner: window.sessionStorage.getItem('userName'),
                          resType: '3',
                          taskId: this.taskId,
                          resName: '~接头盒'+ data.resName ,
                          name:'~接头盒'+ data.resName ,
                          lineNumber: data.lineNumber,
                          aresType: '1',
                          zresType: '0',
                        }
                        addSeg(params2).then(ressss=>{
                          if(ressss.code == 200){
                            this.$refs.iframe.contentWindow.addLink(ressss.data)
                            this.getTopoData()
                          }
                        })
                      }
                    })
                  }
                })
              }
            })
          }
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消'
          })
        })
      },
      // 新增接头盒提交
      addTconCommit (x, y) {
        let params = {
          addresX: x,
          addresY: y,
          editType: '1',
          owner: window.sessionStorage.getItem('userName'),
          resType: '1',
          taskId: this.taskId,
          resName: '接头盒',
          name: '接头盒'
        }
        addTcon(params).then(res=>{
          if(res.code == 200){
            this.$refs.iframe.contentWindow.addNode(res.data)
          }
        })
      },
      // 获取纤芯数量
      getFiberNumOptions(){
        selectLineNum().then(res => {
          this.addFiberBox.options = res.data
        })
      },
      // 绘制光缆框展示
      addFiberBoxShow (aNode, zNode) {
        this.addFiberBox.show = true
        this.addFiberBox.aNode = aNode
        this.addFiberBox.zNode = zNode
      },
      // 绘制光缆提交
      addFiberBoxCommit () {
        this.addFiberBoxCommitLoading = true
        let params={
          aResobjid: this.addFiberBox.aNode.odfid ? this.addFiberBox.aNode.odfid : this.addFiberBox.aNode.resId,
          zResobjid: this.addFiberBox.zNode.odfid ? this.addFiberBox.zNode.odfid : this.addFiberBox.zNode.resId,
          editType: '1',
          owner: window.sessionStorage.getItem('userName'),
          resType: '3',
          taskId: this.taskId,
          resName: this.addFiberBox.aNode.resName+'~'+this.addFiberBox.zNode.resName+'光缆段',
          name: this.addFiberBox.aNode.resName+'~'+this.addFiberBox.zNode.resName+'光缆段',
          lineNumber: this.addFiberBox.selLineNum,
          aresType: this.addFiberBox.aNode.resType,
          zresType: this.addFiberBox.zNode.resType,
        }
        addSeg(params).then(res=>{
          if(res.code == 200){
            this.$refs.iframe.contentWindow.addLink(res.data)
            this.addFiberBox.show = false
            this.addFiberBoxCommitLoading = false
          }
        })
      },
      // 删除数据 没有站点/（接头盒）
      delData(dataType, taskShowId, id, data) {
        this.$confirm('此操作将永久删除, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
              switch (dataType) {
                case 'links':
                  delSegData(data).then(res=>{
                    if(res.code == 200){
                      this.getTopoData()
                    }
                  })
                  break
                case 'fibers':
                  delFiberData(data).then(res=>{
                    if(res.code == 200){
                       this.getTopoData()
                    }
                  })
                  break
              }
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      },
      delTopoData (dataType, taskShowId, id, data) {
        this.$confirm('操作后数据不可恢复, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
              switch (dataType) {
                case 'nodes':
                  delSite(taskShowId).then(res=>{
                    this.$refs.iframe.contentWindow.delNode(dataType, taskShowId, id)
                  })
                  break
                case 'jths':
                  delTcon(data).then(res=>{
                    if(res.code == 200){
                      this.$refs.iframe.contentWindow.delNode(dataType, taskShowId, id)
                    }
                  })
                  break
                case 'links':
                  delSeg(data).then(res=>{
                    if(res.code == 200){
                      this.$refs.iframe.contentWindow.delNode(dataType, taskShowId, id)
                    }
                  })
                  break
                case 'fibers':
                  delFiber(data).then(res=>{
                    if(res.code == 200){
                      this.$refs.iframe.contentWindow.delNode(dataType, taskShowId, id)
                    }
                  })
                  break
              }
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        });
      },
      saveTemporary () {
        this.taskData.taskName = this.taskname
        this.taskData.remark = this.notes
        this.taskData.id = this.taskId
        this.taskData.owner = window.sessionStorage.getItem('userName')
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
        updataTaskApi(this.taskData).then(res=>{
          this.$message({
            message: '保存完成',
            type: 'success'
          });
        })
      },
      // 修改topo
      async editTopoData (data) {
        let tit = this.isEdit ? '修改' : '查看'
        this.editBox = {
          title: tit + '  ' + JSON.parse(JSON.stringify(data)).resName,
          data: {},
          type: data.resType,
          show: true,
          modelObj: this.editBox.modelObj
        }
        if(data.editType == '0') {
          this.editBox.editType = undefined
        } else {
          this.editBox.editType = "`ry-middledb`"
        }
        getTaskName({resType: this.editBox.type}).then(res1 => {
          getModelId({tableName: res1.msg}).then(res2 => {
            this.editBox.modelObj = res2.data
            for (let key in data) {
              this.editBox.data[nameTo_(key)] = data[key]
            }
            this.$nextTick(()=>{
              this.$refs.child.modelObj = this.editBox.modelObj
              this.$refs.child.dataObj = this.editBox.data
              this.$refs.child.type = this.editBox.editType
              this.$refs.child.beforeLoading()
            })

          })
        })
      },
      seeTopoData (data) {
        this.editBox = {
          title: '查看  ' + JSON.parse(JSON.stringify(data)).name,
          loading: false,
          data: data,
          type: data.resType,
          show: true,
        }
      },
      // 修改topo提交
      editCommit () {
        let data = {}
        for (let key in this.$refs.child.pageForm) {
          this.editBox.data[key] = this.$refs.child.pageForm[key]
        }
        for (let key in this.editBox.data) {
          data[nameToTuoFeng(key)] = this.editBox.data[key]
        }
        if (this.$refs.child.addErrForm.length > 0) {
          data.checkoutType = '1'
        } else {
          data.checkoutType = '0'
        }
        if(this.editBox.type === '1'){
          xgTcon(data).then( res => {
            this.editCommitResInfo(res)
          })
        }
        if(this.editBox.type === '2'){
          xgFiber(data).then( res => {
            this.editCommitResInfo(res)
          })
        }
        if(this.editBox.type === '3'){
          xgSeg(data).then( res => {
            this.editCommitResInfo(res)
          })
        }
      },
      // 更新topo图元素属性值
      editCommitResInfo (res) {
        if(res.code==200){
          this.$message({
            message: '修改成功',
            type: 'success'
          });
          // this.$refs.iframe.contentWindow.editTopoNode(res.data)
          // this.$refs.iframe.contentWindow.setAlarm(res.data)
          this.getTopoData()
          if (!this.continuous) {
            this.editBox.show = false
          } else {
            this.continuousIndex++
          }
        }else{
          this.$message({
            message: '修改失败，请重新操作',
            type: 'warning'
          });
        }
      },
      // 新增纤芯
      addLink (data) {
        addLinkApi({
          tconId: this.coreConnectBox.id,
          aResId: data.aResId,
          zResId: data.zResId
        }).then(res => {
          this.$refs.coreConnectBoxIframe.contentWindow.addLink({
            id: res.msg,
            aResId: data.aResId,
            zResId: data.zResId,
            des: data.des
          })
        })
      },
      // 删除线芯
      delLink (id, link) {
        this.$confirm('删除后无法恢复 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          delLinkApi({id: id}).then(res => {
            if (res.code == '200') {
              this.$refs.coreConnectBoxIframe.contentWindow.removerLinkCommit(link)
              this.$message({
                type: 'success',
                message: '删除成功!'
              });
            }
          })
        }).catch(e => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        })
      },
      // 重新绘制纤芯
      reSetLink (delIds, delLinks, aResId, zResId) {
        this.$confirm('此端口已连接纤芯 继续操作会替换原纤芯 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let ids = [...new Set(delIds)]
          for (let i = 0; i < ids.length; i++) {
            delLinkApi({id: ids[i]}).then(res => {
              if (res.code == '200') {
                this.$refs.coreConnectBoxIframe.contentWindow.removerLinkCommit(delLinks[i])
              }
            })
          }
          this.addLink({aResId: aResId, zResId: zResId})
        }).catch(e => {
          this.$message({
            type: 'info',
            message: '已取消'
          });
        })
      },
      // 批量连接纤芯
      batchConnectionsShow (data, datas, title) {
        this.batchConnectionsBox.title = title
        this.batchConnectionsBox.show = true
        this.batchConnectionsBox.aMax = data.groupLength
        this.batchConnectionsBox.zMax = 0
        this.batchConnectionsBox.data.aSegId = data.SEGID
        this.batchConnectionsBox.data.aStartSn = 0
        this.batchConnectionsBox.data.aEndSn = 0
        this.batchConnectionsBox.data.zSegId = ''
        this.batchConnectionsBox.data.zStartSn = 0
        this.batchConnectionsBox.data.zEndSn = 0
        this.batchConnectionsBox.selectOption = datas
        this.$nextTick(function () { this.$refs['batchConnectionsBox'].clearValidate(); })
      },
      // 批量连接纤芯提交
      commitbatchConnections () {
        this.$refs['batchConnectionsBox'].validate((valid) => {
          if (valid) {
            this.$confirm('此操作可能会影响原纤芯 是否继续?', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              this.batchConnectionsBox.data.TconId = this.coreConnectBox.id
              commitbatchConnectionsApi(this.batchConnectionsBox.data).then(res => {
                this.coreConnectShow(this.coreConnectBox)
                this.batchConnectionsBox.show = false
              })
            })
          }
        })
      },
      // 批量连接纤芯 选择框修改
      segIdChange (val, name) {
        let max = this.batchConnectionsBox.selectOption.find((item) => {
          return item.SEGID == this.batchConnectionsBox.data[name]
        })
        if (name == 'aSegId') {
          this.batchConnectionsBox.data.aStartSn = this.batchConnectionsBox.data.aStartSn > max.groupLength ? max.groupLength : this.batchConnectionsBox.data.aStartSn
          this.batchConnectionsBox.data.aEndSn = this.batchConnectionsBox.data.aEndSn > max.groupLength ? max.groupLength : this.batchConnectionsBox.data.aEndSn
        }
        if (name == 'zSegId') {
          this.batchConnectionsBox.data.zStartSn = this.batchConnectionsBox.data.zStartSn > max.groupLength ? max.groupLength : this.batchConnectionsBox.data.zStartSn
          this.batchConnectionsBox.data.zEndSn = this.batchConnectionsBox.data.zEndSn > max.groupLength ? max.groupLength : this.batchConnectionsBox.data.zEndSn
        }
      },
      // 批量连接纤芯调用校验
      ifRight(name) {
        if(name=='aStartSn') {
          this.$refs.batchConnectionsBox.validateField('zStartSn')
        } else if(name=='aEndSn') {
          this.$refs.batchConnectionsBox.validateField('zEndSn')
        } else if (name=='zStartSn') {
          this.$refs.batchConnectionsBox.validateField('aStartSn')
        } else {
          this.$refs.batchConnectionsBox.validateField('aEndSn')
        }
      },
      // 批量连接纤芯 获取纤芯最大值
      getMaxSn (name) {
        let data = this.batchConnectionsBox.selectOption.find((item) => {
          return item.SEGID == this.batchConnectionsBox.data[name]
        })
        if (data) {
          return data.groupLength
        } else {
          return 1
        }
      },
      // 批量连接验证
      verificationBatchConnectionsBox () {
        this.$nextTick(() => {
          window.setTimeout(() => {
            this.$refs['batchConnectionsBox'].validate()
          }, 100)
        })
      },
      // 批量删除
      delMoreBoxShow (id, max, title) {
        this.delMoreBox.title = title
        this.delMoreBox.max = max
        this.delMoreBox.data.segId = id
        this.delMoreBox.data.startSn = 1
        this.delMoreBox.data.endSn = 1
        this.delMoreBox.show = true
      },
      // 批量删除提交
      delMoreBoxCommit () {
        this.$confirm('删除后无法恢复 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.delMoreBox.data.TconId = this.coreConnectBox.id
          delMoreApi(this.delMoreBox.data).then(res => {
            this.delMoreBox.show = false
            this.coreConnectShow(this.coreConnectBox)
          })
        }).catch(e => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          });
        })
      },
      saveAndCreateFiber () {
        saveAndCreateFiberApi({
          taskId: this.taskId,
          TconId: this.coreConnectBox.id,
        }).then(res => {
          this.coreConnectBox.show = false
          this.getTopoData()
        })
      },
      // 获取光缆数据
      getFiberData (id) {
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
      // 光缆失去焦点关闭光缆详情
      closeFiberBox (id) {
        if (this.fiberInfoBox.fiberId == id) {
          this.fiberInfoBox.show = false
        }
      },
      // 生成数据
      createData () {
        this.createDataBox.show = true
        getCheckDate({taskId:this.taskId}).then(res=>{
          this.createDataBox.data = res.data
        })
      },
      // 生成光缆
      async createFiber () {
        this.loading = true
        let res
        await saveFiber({taskId:this.taskId}).then(res1=>{
          res = res1
        }).catch(e => {
          this.loading = false
        })
        this.$message({
          type: 'success',
          message: res.msg
        });
        await this.getTopoData(true)
        this.loading = false
      },
      // 查看生成光缆
      // 正式入库
      dataToWarehouse () {
        this.createDataBox.show = false
        this.$refs.toDatawarehouse.loadingStart()
        this.$refs.toDatawarehouse.submitDaaVerify()
      },
      async getTaskData () {
        getOneTask(this.taskId).then(res => {
          this.taskData = res.data
          this.islead = res.data.islead
          if (this.islead == '0' && !(this.isSee)) {
            this.islead = parseInt(res.data.islead)
            this.sonlead = parseInt(res.data.sonlead)
          } else {
            this.islead = '1'
            this.sonlead = null
          }
        })
      },
      // 判断是否可修改
      async getIsEdit () {
        await getInfo().then(res => {
          if (
            (
              res.permissions.find((item) => {return item == '*:*:*'}) ||
              res.permissions.find((item) => {return item == 'gltxh:gltxhwh:editTopo'})
            ) &&
            !(this.isSee)
          ) {
            this.isEdit = true
          } else {
            this.isEdit = false
          }
        })
        let islead = this.islead == 0
        let isEdit = this.isEdit
        this.$refs.iframe.contentWindow.setRoul(islead, isEdit, this.isSee)
      },
      // 查看光缆纤芯
      openFiberCoreDetail (data) {
        this.fiberCoreDetail.id = data.resId
        this.fiberCoreDetail.name = data.name
        this.fiberCoreDetail.show = true
        this.fiberCoreDetail.loading = true
        getFiberCoreDetailApi({
          fiberId: data.resId
        }).then(res => {
          this.fiberCoreDetail.data = res.data
          this.$nextTick(() => {
            this.$refs.fiberCoreDetail.locationLine()
          })
        })
      },
      //  上一步下一步
      async changeStep (step) {
        this.continuous = false
        this.continuousShow = false
        this.continuousList = []
        this.continuousLinkLine = false
        this.continuousEditFiber = false
        this.continuousEditLink = false
        this.continuousSeeFiberLink = false
        this.continuousIndex = 0
        if (step == 'next') {
          if (this.sonlead == 3) {
            await this.stepCreateFiber()
          }
          this.sonlead++
        } else if (step == 'last') {
          this.sonlead--
        }
        let resType
        if (this.sonlead == 3) {
          this.continuousList = this.$refs.iframe.contentWindow.getContinuousList('1')
          if (this.continuousList.length > 0) {
            this.continuousLinkLine = true
            this.coreConnectShow(this.continuousList[this.continuousIndex])
          }
        } else if (this.sonlead == 4) {
          this.continuousList = this.$refs.iframe.contentWindow.getContinuousList('2')
          if (this.continuousList.length > 0) {
            this.continuousEditFiber = true
            this.editTopoData(this.continuousList[this.continuousIndex])
          }
        } else if (this.sonlead == 5) {
          this.continuousList = this.$refs.iframe.contentWindow.getContinuousList('3')
          if (this.continuousList.length > 0) {
            this.continuousEditLink = true
            this.editTopoData(this.continuousList[this.continuousIndex])
          }
        } else if (this.sonlead == 6) {
          this.continuousList = this.$refs.iframe.contentWindow.getContinuousList('2')
          if (this.continuousList.length > 0) {
            this.continuousSeeFiberLink = true
            this.openFiberCoreDetail(this.continuousList[this.continuousIndex])
          }
        } else if (this.sonlead == 7) {
          this.createData()
        } else if (this.sonlead == 8) {
          this.dataToWarehouse()
        } else if (this.sonlead == 9) {
          this.newFlow()
        }
        if (this.continuousLinkLine || this.continuousEditFiber || this.continuousEditLink || this.continuousSeeFiberLink) {
          this.continuous = true
          this.continuousShow = true
        }
        this.$refs.iframe.contentWindow.setStep(this.sonlead)
      },
      // 引导模式生成光缆
      async stepCreateFiber () {
        let count = this.$refs.iframe.contentWindow.getFibersCount()
        if (count > 0) {
          let isCreate
          await this.$confirm('已存在光缆是否重新生成 重新生成后 光缆属性数据将会丢失?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            isCreate = true
          }).catch(() => {
            isCreate = false
          })
          if (isCreate) {
            await this.createFiber()
          }
        } else {
          await this.createFiber()
        }
      },
      // 发起流程
      newFlow () {
        this.generateLoadmore = true
        this.$confirm('请确认已维护好所有光缆、光缆段、纤芯接续等数据，该操作不可撤回！', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          synchronousOpticalCableRelatedData({taskId: this.taskId}).then(res => {
            this.$message({ type: 'success', message: '操作成功'});
            this.generateDisabled = true
            this.generateLoadmore = false
          }).catch(() => {
            this.generateLoadmore = false
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消'
          })
        })

      },
    }
  }
</script>
<style scoped lang="scss">
  .iframeSpaceBorder {
    width: 100%;
    height: 100%;
  }
  .guidanceBox{
    height: 123px;
    background-color: rgb(245, 247, 250);
  }
  .guidanceBox .el-step__title{
    font-size: 12px;
  }
  .guidanceBox ul{
    width: 100%;
    height: calc(100% - 77px);
    margin: 0!important;
    padding: 10px 40px 10px 6%!important;
  }
  .guidanceBox ul li{
    list-style: none;
    display: inline-block;
    height: 100%;
    text-align: center;
    position: relative;
  }
  .guidanceBox ul li::after {
    width: 14px;
    height: 14px;
    border-top: 3px solid;
    border-right: 3px solid;
    border-color: #ccc;
    content: '';
    position: absolute;
    top: 6px;
    right: 10px;
    transform: rotate(45deg);
  }
  .accomplishStep{
    color: #409EFF;
  }
  .accomplishStep::after {
    border-color: #409EFF !important;
  }
  .editStep{
    color: #303133;
  }
  .editStep::after {
    border-color: #303133 !important;
  }
  .awaitStep{
    color: #C0C4CC;
  }
  .awaitStep::after {
    border-color: #C0C4CC !important;
  }
  .iframeSpace{
    width: 100%;
    padding-top: 10px;
    position: relative;
  }
  .toolBarCon{
    width: 100%;
    height: 100%;
    position: relative;
  }
  .zyTitle{
    width: calc(15% + 9px);
    line-height: 43px;
    padding: 0 10px 0 15px;
    position: absolute;
    border-right: 2px solid #eeeeee;
    z-index: 100;
    height: 45px;
    left: -7px;
    top: 3px;
    border-bottom: 2px solid #eeeeee;
  }
  .fiberInfoBox{
    position: absolute;
    top: 46px;
    right: 0px;
    border: 2px solid #eeeeee;
    width: 300px;
    height: 50vh;
    background-color: #fff;
    .fiberTitle{
      width: 100%;
      height: 45px;
      line-height: 45px;
      background-color: #fff;
      padding-left: 10px;
      .boxClose{
        width: 25px;
        height: 25px;
        line-height: 45px;
        font-size: 25px;
        float: right;
        margin: 0 10px 0 0;
        cursor: pointer;
      }
      .boxClose:hover{
        color: #047E78;
      }
    }
    .el-tabs{
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
      .is-active{
        color: #047E78!important;
      }
      .el-tabs__content{
        height: calc(100% - 30px);
        overflow-y: auto;
      }
      .el-tab-pane{
        height: 100%
      }
      ul{
        margin: 0;
        list-style: none;
        padding: 10px 0 0 20px;
        li{
          width: 100%;
          position: relative;
          border-left: 4px solid #67C23A;
          padding: 0px 0 10px 20px;
          p{margin: 0;}
          .leftIcon{
            position: absolute;
            left:  -17px;
            width: 30px;
            height: 30px;
            background-color: #67C23A;
            border-radius: 50%;
            text-align: center;
            line-height: 30px;
            color: #fff;
            z-index: 10;
          }
          .fiberInfo{
            width: 100%;
            line-height: 20px;
            font-size: 14px!important;
            .Flabel{
              font-weight: bold;
              display: inline-block;
              width: 6rem;
            }
          }
        }
        .fiberSite .fiberInfo{
          line-height: 20px!important;
        }
        li:last-child{
          padding-bottom: 0;
        }
      }
    }
  }
  .rulesAlarm{
    margin: 0;
    padding-left: 2rem;
    color: #F56C6C;
  }
  .el-message{
    z-index: 1000;
  }
  .siteTree .el-tree--highlight-current .el-tree-node.is-current > .el-tree-node__content{
    background-color: #409EFF !important;
    color: #fff;
  }
  .toolBar .fiberCoreDetail .el-dialog:not(.is-fullscreen) {
    margin-top: 5vh !important;
  }
  .toolBar .continuousBox{
    width: 400px;
    height: 100vh;
    z-index: 3000;
    position: fixed;
    left: 0;
    top: 0vh;
    background-color: #fff;
    border: 1px solid #ccc;
    border-radius: 5px;
  }
  .toolBar .continuousBox .switch{
    width: 30px;
    height: 100px;
    line-height: 100px;
    text-align: center;
    font-size: 18px;
    position: absolute;
    top: calc(50% - 50px);
    border-radius: 0 25px 25px 0;
    background-color: #fff;
    border: 1px solid #ccc;
    border-left: none;
    cursor: pointer;
    right: -29px;
  }
  .toolBar .continuousBox .switch:hover{
    border-color: #aaa;
    color: #409EFF;
  }
  .toolBar .continuousBox .continuousBoxCon{
    width: 100%;
    height: 100%;
  }
  .toolBar .continuousBox .continuousBoxCon .title{
    width: 100%;
    height: 40px;
    font-size: 16px;
    line-height: 40px;
    margin: 0px !important;
    background-color: rgb(220, 243, 252);
    padding: 0 10px;
  }
  .toolBar .continuousBox .continuousBoxCon .con{
    width: 100%;
    height: calc(100% - 40px);
    padding: 10px;
  }
  .toolBar .continuousBox .continuousBoxCon ul{
    width: 100%;
    height: 100%;
    margin: 0!important;
    padding: 0!important;
    overflow-y: auto;
  }
  .toolBar .continuousBox .continuousBoxCon li{
    list-style: none;
    width: 100%;
    line-height: 30px;
    margin: 3px 0;
    padding: 0 10px;
    cursor: pointer;
  }
  .toolBar .continuousBox .continuousBoxCon li:hover{
    background-color: rgb(220, 243, 252);
  }
  .toolBar .continuousBox .continuousBoxCon li span{
    line-height: 30px;
    float: right;
  }
  .toolBar .continuousBox .continuousBoxCon .focus{
    background-color: rgb(220, 243, 252);
  }
</style>
