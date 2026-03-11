<template>
    <div id="commonWh" class="app-container">
      <el-card v-show="searchList.length>0" class="box-card">
        <!--条件查询-->
        <div v-show="showLabelQuery" class="label_query">
          <div class="left_tit">
            <span class="left_tit_con"> 条件查询</span>
            <!--<a class="left_tit_opa" @click="showLabelQueryOpen=!showLabelQueryOpen">
              <span v-if="!showLabelQueryOpen">展开<i class="el-icon-arrow-down"></i></span>
              <span v-else>收起<i class="el-icon-arrow-up"></i></span>
            </a>-->
          </div>
          <div class="right_con">
            <div class="right_item" v-for="(item,index) in queryList">
              <div class="itemTit" style="width:100px">
                <span>{{item.label}}：</span>
              </div>
              <div class="itemCon" :style="{'height':item.ifOpen?'auto':item.ifQuery==='1'||item.ifQuery===1?'60px':'30px'}">
                <!--字母索引abcList-->
               <div v-show="item.ifQuery==='1'||item.ifQuery===1" style="width: 100%;height: 30px;padding-left: 10px;">
                <el-radio-group @change="getAbcData(index)" class="queryBox" v-model="item.initial" v-for="opa in abcList">
                  <el-radio :value="opa.value" :label="opa.value">{{opa.label}}</el-radio>
                </el-radio-group>
                </div>
                <el-radio-group v-if="!item.ifMoreChoose" v-model="item.radioValue" v-for="(opa,opaIndex) in item.listRadio">
                  <el-radio :value="opa.code" :label="opa.code" @change="getTableData()">{{opa.name}}</el-radio>
                </el-radio-group>
                <el-checkbox-group v-if="item.ifMoreChoose" v-model="item.checkValue" v-for="(opa,opaIndex) in item.list">
                  <el-checkbox :value="opa.code" :label="opa.code" @change="getTableData()">{{opa.name}}</el-checkbox>
                </el-checkbox-group>
              </div>
              <div class="itemOpa">
                <el-button size="mini" @click.native="item.ifOpen=!item.ifOpen">
                  <span v-if="!item.ifOpen">更多<i class="el-icon-arrow-down"></i></span>
                  <span v-else>收起<i class="el-icon-arrow-up"></i></span>
                  </el-button>
                <el-button size="mini" @click.native="item.ifMoreChoose=!item.ifMoreChoose">
                  <span v-if="!item.ifMoreChoose"><i class="el-icon-document-copy"></i> 多选</span>
                  <span v-else><i class="el-icon-document"></i> 单选</span>
                </el-button>
              </div>
            </div>
          </div>
        </div>
        <div class="openTit" @click="showLabelQuery=!showLabelQuery">
          <a >
            <span v-if="!showLabelQuery">展开<i class="el-icon-arrow-down"></i></span>
            <span v-else>收起<i class="el-icon-arrow-up"></i></span>
          </a>
        </div>
        <!--基础查询-->
        <div class="label_query">
          <div class="left_tit">
            <span class="left_tit_con"> 基础查询</span>
            <a class="left_tit_opa" @click="showBaseQueryOpen=!showBaseQueryOpen">
              <span v-if="!showBaseQueryOpen">展开<i class="el-icon-arrow-down"></i></span>
              <span v-else>收起<i class="el-icon-arrow-up"></i></span>
            </a>
          </div>
          <div class="right_con" v-show="showBaseQueryOpen">
            <el-form class="searchBox" label-width="100px" :model="searchForm">
              <el-form-item style="float: left" v-for="(item,index) in searchList"
                            :key="index"
                            :label="item.attributeNameCn">
                <el-select size="mini" v-if="item.attType==='select'" clearable filterable v-model="searchForm[item.attributeNameUnderline]">
                  <el-option
                    v-for="arr in item.list"
                    :label="arr.name"
                    :key="arr.code"
                    :value="arr.code"></el-option>
                </el-select>
                <el-date-picker
                  v-model="searchForm[item.attributeNameUnderline]"
                  type="datetime"
                  size="mini"
                  v-else-if="item.attType==='datetime'"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  placeholder="选择日期时间">
                </el-date-picker>
                <el-input size="mini" v-else v-model="searchForm[item.attributeNameUnderline]" ></el-input>
              </el-form-item>
              <el-form-item style="float: left;margin-left: -80px;">
                <template>
            <el-popover
              placement="top-start"
              title="查询条件列表"
              width="250"
              trigger="hover"
              >
                <el-button type="primary" size="mini" plain @click="customQuery()" slot="reference">自定义查询（{{list.length}})</el-button>
                <div v-for=" (item,index) in list" style="border-radius: 4px;border-color:gray;overflow: hidden;">
                  <span>{{ index+1 }}.&nbsp;</span>
                  <span style="color:#006A65">{{ item.relationship }}</span>
                  <span>&nbsp;{{ item.nameCn }}{{item.operator}}{{item.value}}</span>
                  <el-button icon="el-icon-delete" type="text" @click="delLocal(index)" style="float:right;position:relative;bottom:8px"></el-button>
                </div>
            </el-popover>
            </template>
              </el-form-item>
              <el-form-item style="float: right;margin-left: 0!important;">
                <el-button type="info" icon="el-icon-search" size="mini" plain @click="getTableData()">查询</el-button>
                <el-button size="mini" icon="el-icon-refresh-right" @click="resetSearch()">重置</el-button>
                <el-button plain icon="el-icon-setting" size="mini" @click="openTool">配置</el-button>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </el-card>
      <el-card class="box-card" style="margin-top:15px">
        <el-button type="success" plain icon="el-icon-circle-check" size="mini" @click="openAdd()">新增</el-button>
        <el-button type="danger" plain icon="el-icon-delete" size="mini" @click="deleteSta()">删除</el-button>
        <el-button type="primary" plain icon="el-icon-top-right" size="mini"  @click="handleExport()">导出</el-button>
        <!-- <el-button
                    type="primary"
                    plain
                    icon="el-icon-s-platform"
                    size="mini"
                    @click="openWindow"
                  >全屏</el-button> -->
        <el-table
          ref="multipleTable"
          :data="tableData"
          tooltip-effect="dark"
          stripe
          height="590px"
          style="width: 100%;margin-top: 30px;"
          @selection-change="handleSelectionChange"
          v-loading="loading">
          <el-table-column
            type="selection"
            width="55">
          </el-table-column>
          <el-table-column
            label="序号"
            width="80"
            align="center"
            :formatter="snFormatter"
          ></el-table-column>
          <el-table-column v-for="item in cluList"
                           :label="item.attributeNameCn===undefined?item.attributeName:item.attributeNameCn"
                           :prop="item.attributeNameUnderline"
                           :key="item.id"
                           width="150"
                           align="center">
            <template slot-scope="scope">
              <el-popover trigger="hover" placement="top">
                <p>{{ scope.row[item.attributeNameUnderline] }}</p>
                <div slot="reference" class="name-wrapper">
                  <p>{{ scope.row[item.attributeNameUnderline] }}</p>
                </div>
              </el-popover>
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            width="180"
            fixed="right"
            align="center">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-edit" @click="openEdit(scope)" >编辑</el-button>
              <el-button size="mini" type="text" icon="el-icon-s-help" plain @click="searchStation(scope)" >查看</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div style="text-align: right;margin-top: 5px">
          <pagination
            :total="total"
            :page.sync="pageIndex"
            :limit.sync="pageSize"
            @pagination="getTableData"
          />
          <!--@pagination="getTableData"-->
        </div>
      </el-card>
      <!-- 修改属性对话框 -->
      <el-dialog
        v-dialogDrag title="修改" :visible.sync="editStationDrawer" width="750px" append-to-body style="margin-top: 5.5vh !important;" :close-on-click-modal="false">
        <div class="el-dialog-div">
          <div class="formTitle" v-show="notNullList.length>0">
            <span>* 必填项</span>
          </div>
          <el-form class="opaForm" v-show="notNullList.length>0" ref="form" :model="editForm" label-width="100px" size="mini" style="margin-left:70px">
            <div v-for="item in notNullList">
              <el-form-item style="margin-top:30px"  :label="item.attributeNameCn">
                <el-select v-if="item.attType==='select'" clearable filterable @change="checkAdd(item.attributeNameUnderline,editForm[item.attributeNameUnderline])" v-model="editForm[item.attributeNameUnderline]"
                           @blur="checkAdd(item.attributeNameUnderline,editForm[item.attributeNameUnderline])"
                           :class="rulesForm[item.attributeNameUnderline] ===undefined||rulesForm[item.attributeNameUnderline]===''||rulesForm[item.attributeNameUnderline]===null?'':'error'">
                  <el-option
                    v-for="arr in item.list"
                    :label="arr.name"
                    :key="arr.code"
                    :value="arr.code"></el-option>
                </el-select>
                <el-date-picker
                  v-model="editForm[item.attributeNameUnderline]"
                  @blur="checkAdd(item.attributeNameUnderline,editForm[item.attributeNameUnderline])"
                  type="datetime"
                  :class="rulesForm[item.attributeNameUnderline] ===undefined||rulesForm[item.attributeNameUnderline]===''||rulesForm[item.attributeNameUnderline]===null?'':'error'"
                  v-else-if="item.attType==='datetime'"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  placeholder="选择日期时间">
                </el-date-picker>
                <el-input @blur="checkAdd(item.attributeNameUnderline,editForm[item.attributeNameUnderline])" v-else v-model="editForm[item.attributeNameUnderline]"
                          :class="rulesForm[item.attributeNameUnderline] ===undefined||rulesForm[item.attributeNameUnderline]===''||rulesForm[item.attributeNameUnderline]===null?'':'error'"></el-input>
                <div style="margin-left:210px;margin-top:-30px;width:300px">
                  <span style="font-size:10px;color:red;" v-if="showAdd" v-show="rulesForm[item.attributeNameUnderline]!==undefined&&rulesForm[item.attributeNameUnderline]!==''" v-html="rulesForm[item.attributeNameUnderline]"></span>
                  <span style="font-size:10px;color:grey;" v-if="showAdd" v-show="rulesForm[item.attributeNameUnderline]===undefined" v-html="item.rules"></span>
                </div>
              </el-form-item>
            </div>
          </el-form>
          <div class="formTitle" v-show="nullList.length>0">
            <span>非必填</span>
          </div>

          <el-form class="opaForm" v-show="nullList.length>0" style="margin-left:70px" size="mini" label-width="100px" >
            <div v-for="item in nullList">
              <el-form-item style="margin-top:30px"  :label="item.attributeNameCn">
                <el-select v-if="item.attType==='select'" clearable filterable @change="checkAdd(item.attributeNameUnderline,editForm[item.attributeName])" v-model="editForm[item.attributeName]"
                           @blur="checkAdd(item.attributeNameUnderline,editForm[item.attributeName])"
                           :class="rulesForm[item.attributeNameUnderline] ===undefined||rulesForm[item.attributeNameUnderline]===''||rulesForm[item.attributeNameUnderline]===null?'':'error'">
                  <el-option
                    v-for="arr in item.list"
                    :label="arr.name"
                    :key="arr.code"
                    :value="arr.code"></el-option>
                </el-select>
                <el-date-picker
                  v-model="editForm[item.attributeName]"
                  @blur="checkAdd(item.attributeNameUnderline,editForm[item.attributeName])"
                  type="datetime"
                  :class="rulesForm[item.attributeNameUnderline] ===undefined||rulesForm[item.attributeNameUnderline]===''||rulesForm[item.attributeNameUnderline]===null?'':'error'"
                  v-else-if="item.attType==='datetime'"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  placeholder="选择日期时间">
                </el-date-picker>
                <el-input @blur="checkAdd(item.attributeNameUnderline,editForm[item.attributeName])" v-else v-model="editForm[item.attributeName]"
                          :class="rulesForm[item.attributeNameUnderline] ===undefined||rulesForm[item.attributeNameUnderline]===''||rulesForm[item.attributeNameUnderline]===null?'':'error'"></el-input>
                <div style="margin-left:210px;margin-top:-30px;width:300px">
                  <span style="font-size:10px;color:red;" v-if="showAdd" v-show="rulesForm[item.attributeNameUnderline]!==undefined&&rulesForm[item.attributeNameUnderline]!==''" v-html="rulesForm[item.attributeNameUnderline]"></span>
                  <span style="font-size:10px;color:grey;" v-if="showAdd" v-show="rulesForm[item.attributeNameUnderline]===undefined" v-html="item.rules"></span>
                </div>
              </el-form-item>
            </div>
          </el-form>
        </div>
        <div slot="footer" class="dialog-footer" style="margin-bottom:20px">
          <el-button type="primary" @click="submitEditForm()">确 定</el-button>
          <el-button @click="editStationDrawer=false">取 消</el-button>
        </div>
      </el-dialog>
      <!-- 新增属性对话框 -->
      <el-dialog title="新增"  :close-on-click-modal="false" :visible.sync="newStationDrawer" width="750px" append-to-body style="margin-top: 5.5vh !important;">
        <div class="el-dialog-div">
          <div class="formTitle" v-show="notNullList.length>0">
            <span>* 必填项</span>
          </div>
          <el-form class="opaForm" v-show="notNullList.length>0" ref="form" :model="addForm" label-width="100px" size="mini" style="margin-left:70px">
            <div v-for="item in notNullList">
              <el-form-item style="margin-top:30px"  :label="item.attributeNameCn">
                <el-select v-if="item.attType==='select'" clearable filterable @change="checkAdd(item.attributeNameUnderline,addForm[item.attributeNameUnderline])" v-model="addForm[item.attributeNameUnderline]"
                           @blur="checkAdd(item.attributeNameUnderline,addForm[item.attributeNameUnderline])"
                           :class="rulesForm[item.attributeNameUnderline] ===undefined||rulesForm[item.attributeNameUnderline]===''||rulesForm[item.attributeNameUnderline]===null?'':'error'">
                  <el-option
                    v-for="arr in item.list"
                    :label="arr.name"
                    :key="arr.code"
                    :value="arr.code"></el-option>
                </el-select>
                <el-date-picker
                  v-model="addForm[item.attributeNameUnderline]"
                  @blur="checkAdd(item.attributeNameUnderline,addForm[item.attributeNameUnderline])"
                  type="datetime"
                  :class="rulesForm[item.attributeNameUnderline] ===undefined||rulesForm[item.attributeNameUnderline]===''||rulesForm[item.attributeNameUnderline]===null?'':'error'"
                  v-else-if="item.attType==='datetime'"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  placeholder="选择日期时间">
                </el-date-picker>
                <el-input @blur="checkAdd(item.attributeNameUnderline,addForm[item.attributeNameUnderline])" v-else v-model="addForm[item.attributeNameUnderline]"
                          :class="rulesForm[item.attributeNameUnderline] ===undefined||rulesForm[item.attributeNameUnderline]===''||rulesForm[item.attributeNameUnderline]===null?'':'error'"></el-input>
                <div style="margin-left:210px;margin-top:-30px;width:300px">
                  <span style="font-size:10px;color:red;" v-if="showAdd" v-show="rulesForm[item.attributeNameUnderline]!==undefined&&rulesForm[item.attributeNameUnderline]!==''" v-html="rulesForm[item.attributeNameUnderline]"></span>
                  <span style="font-size:10px;color:grey;" v-if="showAdd" v-show="rulesForm[item.attributeNameUnderline]===undefined" v-html="item.rules"></span>
                </div>
              </el-form-item>
            </div>
          </el-form>
          <div class="formTitle" v-show="nullList.length>0">
            <span>非必填</span>
          </div>

          <el-form class="opaForm" v-show="nullList.length>0" style="margin-left:70px" size="mini" label-width="100px" >
            <div v-for="item in nullList">
              <el-form-item style="margin-top:30px"  :label="item.attributeNameCn">
                <el-select v-if="item.attType==='select'" clearable filterable @change="checkAdd(item.attributeNameUnderline,addForm[item.attributeNameUnderline])" v-model="addForm[item.attributeNameUnderline]"
                           @blur="checkAdd(item.attributeNameUnderline,addForm[item.attributeNameUnderline])"
                           :class="rulesForm[item.attributeNameUnderline] ===undefined||rulesForm[item.attributeNameUnderline]===''||rulesForm[item.attributeNameUnderline]===null?'':'error'">
                  <el-option
                    v-for="arr in item.list"
                    :label="arr.name"
                    :key="arr.code"
                    :value="arr.code"></el-option>
                </el-select>
                <el-date-picker
                  v-model="addForm[item.attributeNameUnderline]"
                  @blur="checkAdd(item.attributeNameUnderline,addForm[item.attributeNameUnderline])"
                  type="datetime"
                  :class="rulesForm[item.attributeNameUnderline] ===undefined||rulesForm[item.attributeNameUnderline]===''||rulesForm[item.attributeNameUnderline]===null?'':'error'"
                  v-else-if="item.attType==='datetime'"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  placeholder="选择日期时间">
                </el-date-picker>
                <el-input @blur="checkAdd(item.attributeNameUnderline,addForm[item.attributeNameUnderline])" v-else v-model="addForm[item.attributeNameUnderline]"
                          :class="rulesForm[item.attributeNameUnderline] ===undefined||rulesForm[item.attributeNameUnderline]===''||rulesForm[item.attributeNameUnderline]===null?'':'error'"></el-input>
                <div style="margin-left:210px;margin-top:-30px;width:300px">
                  <span style="font-size:10px;color:red;" v-if="showAdd" v-show="rulesForm[item.attributeNameUnderline]!==undefined&&rulesForm[item.attributeNameUnderline]!==''" v-html="rulesForm[item.attributeNameUnderline]"></span>
                  <span style="font-size:10px;color:grey;" v-if="showAdd" v-show="rulesForm[item.attributeNameUnderline]===undefined" v-html="item.rules"></span>
                </div>
              </el-form-item>
            </div>
          </el-form>
        </div>
        <div slot="footer" class="dialog-footer" style="margin-bottom:20px">
          <el-button type="primary" @click="submitForm()">确 定</el-button>
          <el-button @click="newStationDrawer=false">取 消</el-button>
        </div>
      </el-dialog>
      <!-- 查看属性对话框 -->
      <el-dialog title="查看" :visible.sync="stationDrawer" width="750px" append-to-body style="margin-top: 5.5vh !important;" :close-on-click-modal="false">
        <div class="el-dialog-div">
          <div class="formTitle" v-show="notNullList.length>0">
            <span>* 必填项</span>
          </div>
          <el-form class="seeForm" v-show="notNullList.length>0" ref="form" :model="editForm" label-width="100px" size="mini" style="margin-left:70px">
            <div v-for="item in notNullList">
              <el-form-item style="margin-top:30px"  :label="item.attributeNameCn">
                <el-select placeholder="" disabled v-if="item.attType==='select'" clearable filterable v-model="editForm[item.attributeNameUnderline]">
                  <el-option
                    v-for="arr in item.list"
                    :label="arr.name"
                    :key="arr.code"
                    :value="arr.code"></el-option>
                </el-select>
                <el-date-picker
                  v-model="editForm[item.attributeNameUnderline]"
                  disabled
                  type="datetime"
                  v-else-if="item.attType==='datetime'"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  placeholder="">
                </el-date-picker>
                <el-input disabled v-else v-model="editForm[item.attributeNameUnderline]"></el-input>
              </el-form-item>
            </div>
          </el-form>
          <div class="formTitle" v-show="nullList.length>0">
            <span>非必填</span>
          </div>

          <el-form class="seeForm" v-show="nullList.length>0" style="margin-left:70px" size="mini" label-width="100px" >
            <div v-for="item in nullList">
              <el-form-item style="margin-top:30px"  :label="item.attributeNameCn">
                <el-select placeholder="" disabled v-if="item.attType==='select'" clearable filterable v-model="editForm[item.attributeNameUnderline]" >
                  <el-option
                    v-for="arr in item.list"
                    :label="arr.name"
                    :key="arr.code"
                    :value="arr.code"></el-option>
                </el-select>
                <el-date-picker
                  v-model="editForm[item.attributeNameUnderline]"
                  disabled
                  type="datetime"
                  v-else-if="item.attType==='datetime'"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  placeholder="">
                </el-date-picker>
                <el-input disabled v-else v-model="editForm[item.attributeNameUnderline]"></el-input>
              </el-form-item>
            </div>
          </el-form>
        </div>
        <div slot="footer" class="dialog-footer" style="margin-bottom:20px">
          <el-button @click="stationDrawer=false">关 闭</el-button>
        </div>
      </el-dialog>
      <el-dialog
        title="展示配置"
        :close-on-click-modal="false"
        width="82%"
        :visible.sync="openShow"
        >
        <zspz v-if="openShow" :versionObj="this.versionObj"></zspz>
      </el-dialog>
      <el-dialog
        title="自定义查询"
        :close-on-click-modal="false"
        width="58%"
        :visible.sync="openCustom"
        style="margin-top: 19vh !important;"
        >
        <el-row :gutter="20">
          <el-col :span="7"><el-card class="box-card" style="width:300px" shadow="never">
            <div slot="header" class="clearfix">
              <span>属性列表</span>
            </div>
            <div class="left">
              <el-input
                v-model="filterText"
                placeholder="请输入属性名称"
                clearable
                size="small"
                prefix-icon="el-icon-search"
                style="margin-bottom: 20px"
              />
              <el-tree
                class="filter-tree"
                :data="deptOptions"
                highlight-current
                default-expand-all
                node-key="data.id"
                :filter-node-method="filterNode"
                ref="treeForm" style="font-size: 14px;height:26.5vh;overflow-y: auto;">
                  <span style="width: 100%;height:100%;display: block" class="custom-tree-node" slot-scope="{ node, data }">
                    <span  :class="data.isHigh==='1'?'red':''" style="width: 100%;display: block;line-height: 25px" @click="handleNodeClick(data.columnName,data.columnNameCn)">
                        <!-- <i :class="data.icon" :style="data.color"></i> -->{{ data.columnNameCn }}
                    </span>
                  </span>
              </el-tree>
            </div>
          </el-card>
        </el-col>
          <el-col :span="1">
            <div style="margin-top: 180px;">
              <img src="../../../assets/images/arrow.png"/>
            </div>
          </el-col>

          <el-col :span="7">
            <el-card class="box-card" style="width:300px;height:40vh" shadow="never">
              <div slot="header" class="clearfix">
                <span>新增查询条件</span>
                <el-button style="float: right; padding: 3px 10px;z-index:9999" type="text" @click="resetQuery">重置</el-button>
              </div>
              <div class="center">
                <el-form label-width="100px" :model="addQueryForm" :rules="addQueryRules" ref="addQueryForm">
                  <el-form-item label="属性名称：" prop="name">
                    <el-input v-model="addQueryForm.name" disabled></el-input>
                  </el-form-item>
                  <el-form-item label="逻辑关系：" prop="relationship">
                    <el-select v-model="addQueryForm.relationship">
                      <el-option label="或（or）" value="or"></el-option>
                      <el-option label="与（and）" value="and"></el-option>
                    </el-select>
                  </el-form-item>
                  <el-form-item label="操作符：" prop="operator">
                    <el-select v-model="addQueryForm.operator">
                      <el-option label="不为空" value="不为空"></el-option>
                      <el-option label="大于" value="大于"></el-option>
                      <el-option label="大于等于" value="大于等于"></el-option>
                      <el-option label="小于" value="小于"></el-option>
                      <el-option label="小于等于" value="小于等于"></el-option>
                      <el-option label="等于" value="等于"></el-option>
                      <el-option label="包含" value="包含"></el-option>
                    </el-select>
                  </el-form-item>
                  <el-form-item label="值：" prop="value">
                    <el-input v-model="addQueryForm.value"></el-input>
                  </el-form-item>
                  <el-form-item>
                    <el-button icon="el-icon-plus" @click="addQuery('addQueryForm')">添加</el-button>
                  </el-form-item>
                </el-form>
              </div>
            </el-card>
          </el-col>
          <el-col :span="1">
            <div style="margin-top: 180px;">
              <img src="../../../assets/images/arrow.png"/>
            </div>
          </el-col>
          <el-col :span="7">

            <el-card class="box-card" style="width:300px;height:40vh" shadow="never">
              <div slot="header" class="clearfix">
                <span>查询条件列表</span>
                <el-button style="float: right; padding: 3px 10px;z-index:9999" type="text" @click="clearLocal">清空</el-button>
              </div>
              <div>
                <div v-for=" (item,index) in list" style="display:block;margin-bottom:5px;padding-top: 5px;padding-left:5px;border: 1px solid #ECECEC;overflow: hidden;">
                  <span>{{ index+1 }}.&nbsp;</span>
                  <span style="color:#006A65">{{ item.relationship }}</span>
                  <span>&nbsp;{{ item.nameCn }}{{item.operator}}{{item.value}}</span>
                    <el-button icon="el-icon-delete" type="text" @click="delLocal(index)" style="float:right;position:relative;bottom:8px;right:5px"></el-button>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
        <div slot="footer" class="dialog-footer" style="margin-bottom:20px">
          <el-button type="primary" @click="openCustom=false">确 定</el-button>
        </div>


      </el-dialog>
    </div>
  </template>

  <script>
    import "@riophae/vue-treeselect/dist/vue-treeselect.css";
    import { getToken } from "@/utils/auth";
    import zspz from "./zspz";
    import { tree,getModelId,submitEdit,getData,getForm,submit,errorStation,initgriddatasite,fileDownload,export_excel_file,exportExcel,deleteStation,deleteError,tableDropDownBox,selectAllRules,getColunmId,getVerrules} from "@/api/zdwh/zdzywh";
    import{mapMessage,conditionalData} from "@/api/zdwh/zdzywh";
    export default {
      name: "Zdzywh",
      computed:{
      },
      components: { zspz },
      props:{
      },
      data() {
        return {
          /* tableName: this.$route.query.tableName, */
          // 接收高级查询参数
          queryData: [],
          // 自定义查询表单
          addQueryForm: {
            name:'',
            relationship: '',
            operator: '',
            value: ''
          },
          // 自定义查询规则
          addQueryRules: {
            name: [ {
              required: true, message: '请选择属性名称',trigger: ['change', 'blur']
            }],
            relationship:[ {
              required: true, message: '请选择逻辑关系', trigger: ['change', 'blur']
            }],
            operator: [ {
              required: true, message: '请选择操作符' , trigger: ['change', 'blur']
            }]
          },
          // 自定义查询
          openCustom: false,
          versionObj: {},
          openShow:false,
          // 遮罩层
          loading: false,
          //显示条件查询
          showLabelQuery:false,
          //查询条件展开,基础条件展开
          showLabelQueryOpen:false,
          showBaseQueryOpen:true,
          //字母索引 条件查询列表
          radio:'',
          radioList:[],
          abcList:[
            {label:'全部',value:''},{label:'A',value:'A'},{label:'B',value:'B'},
            {label:'C',value:'C'},{label:'D',value:'D'},{label:'E',value:'E'},
            {label:'F',value:'F'},{label:'G',value:'G'},{label:'H',value:'H'},
            {label:'I',value:'I'},{label:'J',value:'J'},{label:'K',value:'K'},
            {label:'L',value:'L'},{label:'M',value:'M'},{label:'N',value:'N'},
            {label:'O',value:'O'},{label:'P',value:'P'},{label:'Q',value:'Q'},
            {label:'R',value:'R'},{label:'S',value:'S'},{label:'T',value:'T'},
            {label:'U',value:'U'},{label:'V',value:'V'},{label:'W',value:'W'},
            {label:'X',value:'X'},{label:'Y',value:'Y'},{label:'Z',value:'Z'},
          ],
          queryList:[],
          //表对象信息
          modelObj: {},
          //动态查询条件与列,必填与非必填
          cluList:[],
          searchList:[],
          nullList:[],
          notNullList:[],
          //搜索框
          searchForm: {},
          //表的分页
          pageSize: 15,
          pageIndex: 1,
          total: 0,
          tableData: [],
          filterText:'',
          multipleSelection: [],
          // 树选项
          deptOptions: [],
          //所有属性规则
          allRulesForm:{},
          //新增
          newStationDrawer: false,
          addForm:{},
          rulesForm:{},
          showAdd:true,
          addErrForm:[],
          //查看
          stationDrawer:false,
          //修改
          editForm:{},
          editStationDrawer:false,
          // localStorage存储
          list: [],
          index: ''
        }
      },
      watch:{
        // 根据名称筛选树
        filterText(val) {
          this.$refs.treeForm.filter(val);
        },
        //解决操作栏固定后表格错位问题
        'tableData':{
          handler(){
            this.$nextTick(()=>{
              this.$refs.multipleTable.doLayout();
            })
          },
          deep:true
        },
         'list.length': {
          handler(newValue, oldValue) {

              if (newValue !== oldValue) {
                  // 操作
                for(let j=0;j<this.deptOptions.length;j++){
                  this.$set(this.deptOptions[j],'isHigh','0')
                }
                  this.ifHight()
              }

          }
          /* ,
          queryList(newVal, oldVal) {  //对数组进行监听
              console.log(newVal)
              console.log(oldVal)
          }, */



     /*      'queryList[1].radioValue':function( newValue,oldValue ) {
            alert(1)
              if(newValue !== oldValue) {
                alert(1)
                this.getTableData()
              }
          },
          'queryList[1].checkValue.length':function ( newValue, oldValue ) {
            alert(2)
              if(newValue !== oldValue ) {
                alert(2)
                this.getTableData()
              }
            } */
        },
        //关闭配置页面后刷新列表查询条件
        openShow(){
          if(!this.openShow){
            this.clearLocal()
            this.getCluList()
            this.getQueryList()
          }
        },

      },
      created() {
          this.getCommonData()
          var list = JSON.parse(localStorage.getItem("cmts") || "[]"); //从localStorage中获取之前发布过的数据
          this.list = list; //把获取到的数据赋值给this.list
          /* console.log(this.list); //获取到的数据 */

            this.searchForm = JSON.parse(this.$route.query.searchForm);
            /* console.log(this.searchForm)
            console.log(this.queryList) */
        },
      methods: {
        /*弹出页面*/
        openWindow: function () {
          /* this.$alert('是否全屏显示', '弹屏提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            showCancelButton: true,
            callback: (action) => {
              //打开新窗口展示
              const routeOne = this.$router.resolve({
                path: '/zdzy/zdzywh',
              })
              window.open(routeOne.href, '_blank')
            },
          });
   */

        },
        updateScrollTop(){
            let x=document.getElementsByClassName("card")[0]
            x.scrollTop=x.scrollHeight  //将滚轮置底
         },
        // 过滤节点
        filterNode (value, data) {
          if (!value) return true
          return (data.columnNameCn).indexOf(value) !== -1
        },
        // 重置查询
        resetQuery() {
          this.addQueryForm.name = ''
          this.addQueryForm.relationship = ''
          this.addQueryForm.operator = ''
          this.addQueryForm.value = ''
        },
        // 节点单击事件
        handleNodeClick(name,nameCn) {
          this.addQueryForm.name = nameCn
          this.addQueryForm.cluName = name
        },
         /** 查询数据源下拉树结构 */
         getTree() {
          let params={
              modelId:this.modelObj.modelId,
              isCustomQuery: '1'
            }
          tree(params).then(res => {
            res=res.data
            this.deptOptions = res
            this.ifHight()
          });
        },
        // 删除localStorage
        delLocal(index) {
          this.$delete(this.list,index);
          /*  list = this.list; //把获取到的数据赋值给list  */
          localStorage.setItem("cmts", JSON.stringify(this.list)); //再次调用JOSN.stringify转为数组字符串，然后调用localStorage.setItem()
        },
        // 清空localStorage
        clearLocal() {
          localStorage.removeItem('cmts')
          var list = JSON.parse(localStorage.getItem("cmts") || "[]"); //从localStorage中获取之前发布过的数据
          this.list = list; //把获取到的数据赋值给this.list
        },
        // 添加localStorage
        addQuery(forName) {
          this.$refs[forName].validate((valid) => {
            if (valid) {
                var comment = { name:this.addQueryForm.cluName ,nameCn:this.addQueryForm.name , relationship: this.addQueryForm.relationship ,
                            operator: this.addQueryForm.operator, value:this.addQueryForm.value }
                var list = JSON.parse(localStorage.getItem("cmts") || "[]"); //从localStorage中获取之前发布过的数据
                list.unshift(comment); //重新保存最新的数据（之前的+添加的）
                localStorage.setItem("cmts", JSON.stringify(list)); //再次调用JOSN.stringify转为数组字符串，然后调用localStorage.setItem()
                this.list = list; //把获取到的数据赋值给this.list
                this.resetQuery()
                this.$nextTick(() => {
                  this.$refs[forName].clearValidate();
                });
            } else {
              /* console.log('error submit!!'); */
              return false;
            }
          });

        },
        // 判定高亮
        ifHight() {
          for(let i=0;i<this.list.length;i++) {
            for(let j=0;j<this.deptOptions.length;j++){
              if(this.list[i].name === this.deptOptions[j].columnName){
                this.$set(this.deptOptions[j],'isHigh','1')
              }
            }
          }
        },
        //打开自定义查询页面
        customQuery() {
          this.openCustom = true
          this.getTree()
          this.resetQuery()
          for(let j=0;j<this.deptOptions.length;j++){
            this.$set(this.deptOptions[j],'isHigh','0')
          }
          this.ifHight()
          this.$nextTick(() => {
                  this.$refs['addQueryForm'].clearValidate();
                });
        },
        //打开配置页面
          openTool(){
            this.openShow=true
          },
          getCommonData(){
            //获取表基本信息
            /* alert(this.$route.query.tableName) */
            getModelId({tableName:JSON.parse(this.$route.query.tableName)}).then(res=>{
              this.modelObj = res.data
              /* console.log(this.modelObj) */
              this.versionObj.label = res.data.modelVersion
              this.versionObj.parentId = res.data.modelId
              this.getQueryList()
              this.getCluList()


            })

          },
          //获取条件查询列表
          getQueryList(){
            mapMessage(this.modelObj).then(res=>{
              for (let i = 0; i < res.data.length; i++) {
                res.data[i].radioValue=''
                res.data[i].checkValue=[]
                res.data[i].initial=''
                if(res.data[i].list.length>0){
                  res.data[i].listRadio=[{code:'',name:'全部'}].concat(res.data[i].list)
                }
              }
              this.queryList=res.data
              this.queryData = JSON.parse(this.$route.query.queryData);
              for( let i = 0; i<this.queryData.length; i++) {
              this.queryList[i].radioValue = this.queryData[i].radioValue
              this.queryList[i].checkValue = this.queryData[i].checkValue
              this.queryList[i].ifOpen = this.queryData[i].ifOpen
              this.queryList[i].ifMoreChoose = this.queryData[i].ifMoreChoose
            }
            })

          },
        //按首字母索引
        getAbcData(index){
          this.index = index
          /* alert(this.index) */
          this.queryList[index].radioValue=''
          this.queryList[index].checkValue=[]
          this.queryList[index].list=[]
          this.queryList[index].listRadio=[]
          conditionalData(this.queryList[index]).then(res=>{
            this.queryList[index].list=res.data
            if(this.queryList[index].list.length>0){
              this.queryList[index].listRadio=[{code:'',name:'全部'}].concat(this.queryList[index].list)
            }
          })
          /* console.log(this.queryList) */
        },
          //动态获取页面搜索条件与列名
          getCluList(){
            this.cluList = []
            this.searchList = []
            this.notNullList=[]
            this.nullList=[]
            initgriddatasite(this.modelObj).then(res=>{
              this.cluList = res.data.columns
              this.searchList = res.data.searchAttributes
              //获取表格下拉框数据
              tableDropDownBox(this.modelObj).then(res=>{
                res=res.data
                for (let i = 0; i < this.cluList.length; i++) {
                  this.cluList[i].rules=''
                  for (let j = 0; j < res.length; j++) {
                    if(this.cluList[i].attributeName===res[j].name&&res[j].list.length>0){
                      this.cluList[i].attType='select'
                      this.cluList[i].list=res[j].list
                    }
                  }
                }
                for (let i = 0; i < this.searchList.length; i++) {
                  for (let j = 0; j < res.length; j++) {
                    if(this.searchList[i].attributeName===res[j].name&&res[j].list.length>0){
                      this.searchList[i].attType='select'
                      this.searchList[i].list=res[j].list
                    }
                  }
                }
                //获取所有属性初始化规则
                this.getAllRules()
              })
              this.getTableData()
            })
          },
          /*重置搜索 */
          resetSearch() {
            //重置基础搜索
            this.searchForm={}
            //重置条件查询
            for (let i = 0; i < this.queryList.length; i++) {
              this.queryList[i].radioValue=''
              this.queryList[i].checkValue=[]
            }
            //重置自定义查询
            this.clearLocal()
          },
          /* 格式序列化列 */
          snFormatter: function (row, col, value, index) {
            return this.pageSize * (this.pageIndex - 1) + index + 1;
          },
          //获取表格数据
          getTableData() {
            this.loading = true;
            let params={
              modelId:this.modelObj.modelId,
              isPage:'1',
              pageIndex: this.pageIndex,
              pageSize: this.pageSize,
              whereAttributes:[]
            }
            //基础查询
            for(let key in this.searchForm){
              let obj={}
                if(this.searchForm[key]!==''&&this.searchForm[key]!==undefined){
                  obj={
                    attributeName:key,
                    attributeValue:this.searchForm[key]
                  }
                  params.whereAttributes.push(obj)
                }
            }
            //条件查询
            for (let i = 0; i < this.queryList.length; i++) {
              //多选
              let obj={}
              if(this.queryList[i].ifMoreChoose){
                if(this.queryList[i].checkValue!==undefined&&this.queryList[i].checkValue.length>0){
                  obj={
                    attributeName:this.queryList[i].name,
                    attributeValue:this.queryList[i].checkValue.toString()
                  }
                  params.whereAttributes.push(obj)
                }
              }else{
                //单选
                obj={
                  attributeName:this.queryList[i].name,
                  attributeValue:this.queryList[i].radioValue
                }
                if(this.queryList[i].radioValue!==''){
                  params.whereAttributes.push(obj)
                }
              }
            }
            //自定义查询
            if(this.list.length > 0) {
              for(let j = 0; j < this.list.length ;j++) {
              let obj={}
              obj={
                    attributeName:this.list[j].name,
                    attributeValue:this.list[j].value,
                    logicalRelationSymbol:this.list[j].relationship,
                    operator: this.list[j].operator
                  }
              params.whereAttributes.push(obj)
            }
          }
            getData(params).then(response => {
                this.tableData = response.rows;
                this.total = response.total;
                this.loading = false;
              }
            ).catch(() => {
              this.loading = false
            });
          },
          //获取多选框数据
          handleSelectionChange(val) {
            this.multipleSelection = val;
          },
          // 删除
          deleteSta() {
            if(this.multipleSelection.length===0){
              this.$message.error('请选中数据进行删除');
            } else{
              this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              }).then(() => {
                let params={
                  modelId:this.modelObj.modelId,
                  isPage:'0',
                  valueAttributes:[]
                }
                for(let i = 0; i < this.multipleSelection.length; i++) {
                  let obj = {
                    attributeName: 'ID',
                    attributeValue: this.multipleSelection[i].ID,
                    isPrimaryKey:'1'
                  }
                  params.valueAttributes=[obj]
                  deleteStation(params).then(response =>{
                    if(i===this.multipleSelection.length-1){
                      this.getTableData()
                      this.deleteErrorData()
                      this.$message({
                        message: response.msg,
                        type: 'success',
                        center: true
                      })
                    }
                    }
                  ).catch(() => {
                    this.getTableData()
                  })
                }

              }).catch(() => {
                this.$message({
                  type: 'info',
                  message: '已取消删除'
                })
              })
            }
          },
          //删除数据对应的问题数据
          deleteErrorData() {
            for(let i = 0; i < this.multipleSelection.length; i++) {
              deleteError(this.multipleSelection[i].ID).then(response =>{

              })
            }
          },
          //获取所有属性的规则
          getAllRules(){
            selectAllRules().then(res=>{
              res = res.data
              for (let i = 0; i < res.length; i++) {
                for (let j = 0; j < this.cluList.length; j++) {
                  if(res[i].errorColumnNameCn===this.cluList[j].attributeNameCn){
                    if(this.cluList[j].rules===undefined){
                      this.cluList[j].rules=res[i].ruleDescription + '<br/>'
                    }else{
                      this.cluList[j].rules+=res[i].ruleDescription + '<br/>'
                    }
                  }
                }
              }
              //循环获取必填与非必填数组，是否必填1是，0否
              for (let i = 0; i < this.cluList.length; i++) {
                if(this.cluList[i].isNotNull===1||this.cluList[i].isNotNull==='1'){
                  this.notNullList.push(this.cluList[i])
                }else{
                  this.nullList.push(this.cluList[i])
                }
              }
            })
          },
          //打开新增
          openAdd(){
            this.newStationDrawer = true
            this.rulesForm={}
            this.addForm={}
            this.addErrForm=[]
          },
          //校验名字
          checkAdd(column,value) {
            let param = {
              colunmName: column,
              modelId: this.modelObj.modelId
            }
            getColunmId(param).then(response=>{
              this.columnId = response.msg
              this.getRules(column,this.columnId,value)
            })

          },
          getRules(column,columnId,value) {
            //删除addErrForm中原来的错误数据，
            let arr=[]
            for (let i = 0; i < this.addErrForm.length; i++) {
              if(this.addErrForm[i].errorColumnName!==column){
                arr.push(this.addErrForm[i])
              }
            }
            this.addErrForm=arr
            this.rulesForm[column]=''
            let param={
              commId: columnId,
              modelId: this.modelObj.modelId,
              param: value
            }
            getVerrules(param).then(response=>
            {
              this.showAdd=false
              if(response.data !== []) {
                let str = ''
                for(var i = 0;i<response.data.length;i++){
                  //将错误数据保存到addErrForm中
                  this.addErrForm.push(response.data[i])
                  str += response.data[i].ruleDescription + '<br/>'
                }
                this.rulesForm[column] = str
                this.$set(this.rulesForm,column,this.rulesForm[column])
              }else{
                this.rulesForm[column] = undefined
              }
              this.showAdd=true
            }).catch(e => {

            })
          },
          // 新增保存
          submitForm() {
            for (let i = 0; i < this.notNullList.length; i++) {
              this.checkAdd(this.notNullList[i].attributeNameUnderline,this.addForm[this.notNullList[i].attributeNameUnderline])
            }
            for (let i = 0; i < this.nullList.length; i++) {
              this.checkAdd(this.nullList[i].attributeNameUnderline,this.addForm[this.nullList[i].attributeNameUnderline])
            }
            setTimeout(() => {
              this.$confirm('是否保存此数据?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              }).then(() => {
                let params={
                  modelId:this.modelObj.modelId,
                  isPage:'0',
                  valueAttributes:[{attributeName:'ID',attributeValue:'1',isPrimaryKey:'1'}]
                }
                for(let key in this.addForm){
                  let obj={}
                  if(this.addForm[key]!==''&&this.addForm[key]!==undefined) {
                    obj = {
                      attributeName: key,
                      attributeValue: this.addForm[key]
                    }
                    params.valueAttributes.push(obj)
                  }
                }
                submit(params).then(response => {
                    this.newStationDrawer = false
                    this.saveErrorStation(response.msg)
                    this.$modal.msgSuccess("操作成功");
                    this.getTableData()
                  }
                ).catch((e) => {
                  this.$message({
                    message: e,
                    type: 'error',
                    center: true,
                    duration:0,
                    showClose:true
                  })
                })
              })
            }, 1500);

          },
          // 保存错误传回
          saveErrorStation(id) {
            if(this.addErrForm.length >0) {
              for(var i=0;i<this.addErrForm.length;i++){
                this.addErrForm[i].errorId=id
              }
              errorStation(this.addErrForm).then(response =>{
              })
            }
          },
          //查看
          searchStation({row}){
            this.stationDrawer = true
            this.editForm = []
            let params={
              modelId:this.modelObj.modelId,
              isPage:'0',
              whereAttributes:[{attributeName:'ID',attributeValue:row.ID,isPrimaryKey:'1'}]
            }
            getForm(params).then(res=>{
              this.editForm=res.data
            })
          },
          /** 导出按钮操作 */
          handleExport(){
            this.$confirm('是否导出数据?', '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              let params={
                modelId:this.modelObj.modelId,
                isPage:'0',
                whereAttributes:[]
              }
              for(let key in this.searchForm){
                let obj={}
                if(key!=='pageSize'&&key!=='pageNum'){
                  if(this.searchForm[key]!==''&&this.searchForm[key]!==undefined){
                    obj={
                      attributeName:key,
                      attributeValue:this.searchForm[key]
                    }
                    params.whereAttributes.push(obj)
                  }
                }
              }
              //条件查询
              for (let i = 0; i < this.queryList.length; i++) {
                //多选
                let obj={}
                if(this.queryList[i].ifMoreChoose){
                  if(this.queryList[i].checkValue!==undefined&&this.queryList[i].checkValue.length>0){
                    obj={
                      attributeName:this.queryList[i].name,
                      attributeValue:this.queryList[i].checkValue.toString()
                    }
                    params.whereAttributes.push(obj)
                  }
                }else{
                  //单选
                  obj={
                    attributeName:this.queryList[i].name,
                    attributeValue:this.queryList[i].radioValue
                  }
                  if(this.queryList[i].radioValue!==''){
                    params.whereAttributes.push(obj)
                  }
                }
              }
              //自定义查询
              exportExcel(params).then(res =>{
                fileDownload({filename:res.msg}).then(res =>{
                  export_excel_file(res, '文件名 ');
                  // window.open(res.config.baseURL + res.config.url)
                })
              })
            })
          },
          //打开修改
          openEdit({row}){
            this.rulesForm={}
            this.editForm={}
            this.addErrForm=[]
            let params={
              modelId:this.modelObj.modelId,
              isPage:'0',
              whereAttributes:[{attributeName:'ID',attributeValue:row.ID,isPrimaryKey:'1'}]
            }
            getForm(params).then(res=>{
              this.editForm=res.data
              for (let i = 0; i < this.notNullList.length; i++) {
                this.checkAdd(this.notNullList[i].attributeNameUnderline,this.editForm[this.notNullList[i].attributeNameUnderline])
              }
              for (let i = 0; i < this.nullList.length; i++) {
                this.checkAdd(this.nullList[i].attributeNameUnderline,this.editForm[this.nullList[i].attributeNameUnderline])
              }
              this.editStationDrawer = true
            })
          },
          //修改提交
          submitEditForm() {
            for (let i = 0; i < this.notNullList.length; i++) {
              this.checkAdd(this.notNullList[i].attributeNameUnderline,this.editForm[this.notNullList[i].attributeNameUnderline])
            }
            for (let i = 0; i < this.nullList.length; i++) {
              this.checkAdd(this.nullList[i].attributeNameUnderline,this.editForm[this.nullList[i].attributeNameUnderline])
            }
            setTimeout(() => {
              this.$confirm('是否保存此数据?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
              }).then(() => {
                let params={
                  modelId:this.modelObj.modelId,
                  isPage:'0',
                  valueAttributes:[]
                }
                for(let key in this.editForm){
                  let obj={}
                  if(this.editForm[key]!==''&&this.editForm[key]!==undefined){
                    obj={
                      attributeName:key,
                      attributeValue:this.editForm[key]
                    }
                    if(key==='ID'){obj.isPrimaryKey=1}
                    params.valueAttributes.push(obj)
                  }
                }
                submitEdit(params).then(response => {
                  this.editStationDrawer = false
                  this.saveErrorStation(this.editForm.ID)
                  this.$modal.msgSuccess("操作成功");
                  this.getTableData()
                  }
                ).catch((e) => {
                  this.$message({
                    message: e,
                    type: 'error',
                    center: true,
                    duration:0,
                    showClose:true
                  })
                })
              })
            }, 1500);
          },
      },
    };
  </script>
  <style>
    #commonWh .label_query{
      width: 100%;
      margin-bottom: 10px;
    }
    #commonWh .label_query .left_tit{
      width: 100%;
      height:24px;
      display: flex;
      border-bottom:1px solid #E9E9E9;
    }
    #commonWh .label_query .left_tit .left_tit_con{
      flex: 1;
      font-size:14px;
      font-weight: 600;
      color:rgba(0,0,0,0.85) ;
      line-height: 16px;
    }
    #commonWh .label_query .left_tit .left_tit_opa{
      font-size:12px;
      font-weight: 600;
      color:#047E78 ;
      line-height: 20px;
    }
    #commonWh .label_query .right_con{
      width: 100%;
      margin-top: 15px;
    }
    #commonWh .label_query .right_con .el-form-item__label{
      font-size: 14px;
      font-weight: 400;
      color: rgba(0,0,0,0.85);
    }
    #commonWh .label_query .right_con .right_item{
      display: flex;
      border-bottom:2px solid #fff;
      padding: 2px 5px;
    }
    #commonWh .label_query .right_con .right_item .itemTit{
      font-size: 14px;
      font-weight: 400;
      color: rgba(0,0,0,0.65);
      line-height: 30px;
    }
    #commonWh .label_query .right_con .right_item .itemCon{
      flex:1;
      margin: 0 10px 0 20px;
      overflow: hidden;

    }
    #commonWh .label_query .right_con .right_item .itemCon .el-radio__input{
      display: none
    }
    #commonWh .label_query .right_con .right_item .itemCon .el-radio__label,
    #commonWh .label_query .right_con .right_item .itemCon .el-checkbox__label{
      display: inline-block;
      margin-left:-5px;
      margin-right:20px;
      line-height: 30px;
      font-size: 14px;
      font-weight: 400;
    }
    .queryBox .el-radio__label{
      height: 23px;
      text-align: center;
      line-height: 20px !important;
      padding: 0px 5px !important;
    }
    .queryBox .el-radio__input.is-checked + .el-radio__label{
      color: #047E78;
      background: #e6f2f2;
      border: 1px solid #9bcbc9
    }
    #commonWh .label_query .right_con .right_item .itemCon .el-checkbox-group,
    #commonWh .label_query .right_con .right_item .itemCon .el-radio-group
    {
      float: left;
    }
    #commonWh .label_query .right_con .right_item .itemOpa{
      float: left;
      margin-top: 5px;
    }
    #commonWh .searchBox .el-form-item .el-form-item__content .el-input,
    #commonWh .searchBox .el-form-item .el-form-item__content .el-input .el-input_inner{
      width: 15rem;
    }
    .error .el-input__inner {
      border: red 1px solid;
    }
    .center .el-input {
      width:170px
    }
    .center .el-select {
      width:170px
    }
    .red{
      color:#047E78;
    }
    .box-card {
      overflow-y:auto
    }
  </style>
