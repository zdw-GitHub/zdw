<template>
    <div class="zhgl">
      <div class="qjcx_con">
        <div class="con_right">
          <div class="r_top">
            <div class="cell" style="width: 50%;text-align: right;position:absolute;top:20px;right:20px">
                <!-- <el-button type="primary" @click="uploadTable()" icon="el-icon-upload2">发布</el-button>
                <el-button type="primary" @click="addTable()" icon="el-icon-plus">新增</el-button> -->
                <!-- <el-button :disabled="ifSel" type="primary" @click="saveTabData()" icon="el-icon-circle-check" style="position:absolute;margin-top:230px;right:20px">保存</el-button> -->
                <!-- <el-button :disabled="ifSel" type="primary" @click="getTabData()" icon="el-icon-circle-close">复制</el-button>
                <el-button :disabled="ifSel" type="primary" @click="delTable()"  icon="el-icon-delete">删除</el-button> -->
              </div>
            <div class="table_space" style="height: 10px;display: flex">
            </div>
          </div>
          <div class="tabBarArea tit">
            <el-form style="display: flex" size="small">
              <el-form-item label="关联方式" style="flex: 1">
                <el-select v-model="tabName" class="relationType">
                  <el-option value="1" label="字段关联"></el-option>
                  <el-option value="2" label="SQL关联"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item style="text-align: right">
                <el-button type="primary" plain icon="el-icon-setting" :disabled="ifSel" size="mini" @click="openMarkDialog">唯一标识配置</el-button>
                <el-button @click="saveGlType" type="success" plain icon="el-icon-circle-check" size="mini" v-if="versionShow">保存</el-button>
              </el-form-item>
            </el-form>
                    <!--<div class="tabBarBtn" :class="[tabName=='Tab1'?'tabBarBtnActive':'']"
                        @click="tabName='Tab1'">字段关联</div>
                    <div class="tabBarBtn" :class="[tabName=='Tab2'?'tabBarBtnActive':'']"
                        @click="tabName='Tab2'">SQL关联</div>-->
                </div>
          <div class="r_bot">
            <div  v-if="tabName==='1'">
                <div class="cell" style="float: right;width: 120%;text-align: right;margin-bottom:10px">
                  <span style="position:absolute;left:20px">已关联主库： {{ modelData.relationMainDbNameCn }}</span>
                <span style="position:absolute;left:250px;">已关联主表： {{ modelData.relationMainTableName }}</span>
                <span style="position:absolute;left:510px">已关联主表别名： {{ modelData.relationMainTableAlias }}</span>
                  <el-button  :disabled="ifSel" plain style="width:115px;margin-right:10px" icon="el-icon-s-operation" @click="mainTable()">关联主表</el-button>
                  <el-form style="margin-right:35px;display: inline;" ref="selForm" :model="selForm" label-width="80px">
                    <el-input style="width: 300px;height: 30px" v-model="selForm.cluName" placeholder="请输入属性名"></el-input>
                  </el-form>
                  <el-button :disabled="ifSel" plain style="padding:5px 25px;width:80px" type="info" @click="getData()" icon="el-icon-search" v-hasPermi="['mm:relation:list']">搜索</el-button>
                  <el-button :disabled="ifSel" plain style="padding:5px 25px;width:80px" @click="getData" icon="el-icon-refresh">刷新</el-button>
                </div>
                <div class="table_space">
                  <el-table
                    :data="tableData"
                    stripe
                    height="320px"
                    tooltip-effect="dark"
                    :header-cell-style="{background:'#F5F5F5'}"
                    v-loading="loading"
                    >
                    <el-table-column
                      label="序号"
                      width="80"
                      align="center"
                      :formatter="snFormatter">
                    </el-table-column>
                    <el-table-column
                      prop="columnName"
                      align="center"
                      label="属性名称"
                      width="100">
                    </el-table-column>
                    <el-table-column
                      prop="columnNameCn"
                      align="center"
                      label="中文名"
                      width="100">
                    </el-table-column>
                    <el-table-column
                      prop="columnType"
                      align="center"
                      label="属性类型"
                      width="120">
                    </el-table-column>
                    <el-table-column
                      prop="columnLength"
                      align="center"
                      label="属性长度"
                      width="150">
                    </el-table-column>
                    <el-table-column
                      prop="relationDbId"
                      align="center"
                      label="关联数据源"
                      width="120">
                      <template slot-scope="scope">
                        <!-- scope.$index为当前行下标，cellIndex所点击行下标 -->
                        <el-select v-model="scope.row.relationDbId"  @change="getDbTableList(scope.row,scope.row.relationDbId)"  clearable v-show="scope.$index == cellIndex" style="width:100px">
                          <el-option
                              v-for="item in dbList"
                              :key="item.id"
                              :label="item.dbNameCn"
                              :value="item.id">
                            </el-option>
                        </el-select>
                        <p v-show="scope.$index != cellIndex">{{ tableData[scope.$index].relationDbName }}</p>
                      </template>
                    </el-table-column>
                    <el-table-column
                      prop="relationTableId"
                      align="center"
                      label="关联表"
                      width="120"

                      >
                      <template slot-scope="scope">
                        <!-- scope.$index为当前行下标，cellIndex所点击行下标 -->
                        <el-select v-model="scope.row.relationTableId" filterable @change="getRelationTableAlex(scope.row,scope.row.relationTableId)" :disabled="isRelationTableId"  clearable v-show="scope.$index == cellIndex" style="width:100px;">
                          <el-option
                          v-for="item in tableList"
                          :key="item.id"
                          :label="item.tableName"
                          :value="item.id">
                            </el-option>
                        </el-select>
                        <p v-show="scope.$index != cellIndex">{{ tableData[scope.$index].relationTableName }}</p>
                      </template>
                    </el-table-column>
                    <el-table-column
                      prop="relationTableAlias"
                      align="center"
                      label="关联表别名"
                      width="150">
                      <template slot-scope="scope">
                        <!-- scope.$index为当前行下标，cellIndex所点击行下标 -->
                        <el-input v-model="scope.row.relationTableAlias"  v-show="scope.$index == cellIndex" @input="$forceUpdate()">
                        </el-input>
                        <p v-show="scope.$index != cellIndex">{{ scope.row.relationTableAlias }}</p>
                      </template>
                    </el-table-column>
                    <el-table-column
                      prop="relationColumnId"
                      align="center"
                      label="关联属性"
                      width="150">
                      <template slot-scope="scope">
                        <!-- scope.$index为当前行下标，cellIndex所点击行下标 -->
                        <el-select v-model="scope.row.relationColumnId"  :disabled="isrelationColumnId"  @change="$forceUpdate()" v-show="scope.$index == cellIndex" style="width:100px;" clearable>
                          <el-option
                          v-for="item in columnList"
                          :key="item.id"
                          :label="item.columnName"
                          :value="item.id">
                            </el-option>
                        </el-select>
                        <p v-show="scope.$index != cellIndex">{{ tableData[scope.$index].relationColumnName }}</p>
                      </template>
                    </el-table-column>
                    <!-- <el-table-column
                      prop="updateOrNot"
                      align="center"
                      label="是否更新"
                      width="150">
                        <template slot-scope="scope">
                        <el-select v-model="scope.row.updateOrNot"  v-show="scope.$index == cellIndex" style="width:100px" clearable>
                          <el-option :key="1" :value="1" label="是"></el-option>
                          <el-option :key="0" :value="0" label="否"></el-option>
                        </el-select>
                        <p v-show="scope.$index != cellIndex" v-if="scope.row.updateOrNot==='1'">是</p>
                        <p v-show="scope.$index != cellIndex" v-if="scope.row.updateOrNot==='0'">否</p>
                      </template>
                    </el-table-column> -->
                    <el-table-column
                      prop="isExternalConnection"
                      align="center"
                      label="是否外联"
                      width="150">
                        <template slot-scope="scope">
                        <!-- scope.$index为当前行下标，cellIndex所点击行下标 -->
                        <el-select v-model="scope.row.isExternalConnection"  v-show="scope.$index == cellIndex" style="width:100px" clearable>
                          <el-option :key="1" :value="1" label="是"></el-option>
                          <el-option :key="0" :value="0" label="否"></el-option>
                        </el-select>
                        <p v-show="scope.$index != cellIndex" v-if="scope.row.isExternalConnection===1">是</p>
                        <p v-show="scope.$index != cellIndex" v-if="scope.row.isExternalConnection===0">否</p>
                      </template>
                    </el-table-column>
                    <el-table-column
                      prop="relationCondition"
                      align="center"
                      label="关联条件"
                      width="150">
                      <template slot-scope="scope">
                        <!-- scope.$index为当前行下标，cellIndex所点击行下标 -->
                        <el-input v-model="scope.row.relationCondition"  v-show="scope.$index == cellIndex">
                        </el-input>
                        <p v-show="scope.$index != cellIndex">{{ scope.row.relationCondition }}</p>
                      </template>
                    </el-table-column>
                    <el-table-column
                      label="操作"
                      fixed="right"
                      width="380"
                      align="center"
                      v-if="versionShow">
                      <template slot-scope="scope">
                       <!--  <el-button plain type="primary" disabled="false" @click="glColumn(scope.row)" icon="el-icon-connection">SQL</el-button> -->
                        <el-button size="mini" type="text" @click="glColumn(scope.row)" icon="el-icon-connection" v-hasPermi="['mm:relation:relationEdit']">关联</el-button>
                        <el-button  @click="editTable(scope,scope.row)" size="mini" type="text" icon="el-icon-edit" v-if="scope.$index != cellIndex" v-hasPermi="['mm:relation:edit']">编辑</el-button>
                        <el-button  @click="saveTable(scope)" size="mini" type="text" v-else icon="el-icon-circle-check" v-hasPermi="['mm:relation:edit']">保存</el-button>
                        <el-button  type="text" @click="deletTable(scope)" size="mini" icon="el-icon-delete" v-if="scope.$index != cellIndex" v-show="scope.row.relationDbId!==undefined"  v-hasPermi="['mm:tech:remove']">删除</el-button>
                        <el-button  @click="cancel()" size="mini" type="text" v-else icon="el-icon-circle-close">取消</el-button>
                      </template>
                    </el-table-column>
                  </el-table>
                  <pagination
                    v-show="total>0"
                    :total="total"
                    :page.sync="pageIndex"
                    :limit.sync="pageSize"
                    @pagination="getData"
                  />
                </div>
           </div>
              <div  class="table_space" v-if="tabName==='2'" style="display:flex">
                  <div style="background-color:#F2F2F2;height:450px;flex:1;padding: 4px">
                    <span style="color:#538DC2;height: 40px;">已对应字段</span>
                    <div style="height: 410px;margin-top:10px;overflow-y: auto;">
                      <ul style="margin-left:-20px">
                        <li v-for="item in sqlCluName" :style="{color:item.cluType===0?'#000':'#538DC2'}">{{item.cluName}}</li>
                      </ul>
                    </div>
                  </div>
                  <div style="background-color:#F2F2F2;height:450px;margin-left:20px;flex:3;padding: 4px ">
                    <span style="color:#538DC2;height: 40px;">SQL</span>
                    <el-button @click="saveSql" :disabled="ifSel" style="float: right;" size="mini" plain type="success" icon="el-icon-circle-check" v-if="versionShow">保存</el-button>
                    <div id="sqlSpace" style="margin-top:20px">
                      <el-input :rows="18" type="textarea" v-model="sqlStr"></el-input>
                    </div>
                  </div>
              </div>
          </div>
          <!--唯一标识配置对话框-->
          <el-dialog
            title="唯一标识配置"
            append-to-body
            :close-on-click-modal="false"
            :visible.sync="markVisible">
            <div>
              <el-row :gutter="20">
                <el-col :span="2">
                  <div
                    style="writing-mode: vertical-lr;
                    width: 40px;
                    height:260px;
                    background-color:#f5f7fa;margin-top: 150px;
                    font-size: larger;
                    font-weight: bolder;
                    letter-spacing:15px;
                    text-indent:0.5cm;
                    padding: 20px 5px;">唯一标识组成</div>
                </el-col>
                <div style="display: flex;overflow-x: auto;">
                  <!--<el-col :span="22">-->
                  <div class="draggable1" style="margin-right: 10px">
                    <el-table
                      v-if="metaData[0]"
                      ref="metaData"
                      row-key="columnId"
                      :data="metaData[0].uidColumnList"
                      height="500"
                      style="width: 100%">
                      <el-table-column
                        type="index"
                        label="序号"
                        align="center"
                        width="60">
                      </el-table-column>
                      <el-table-column
                        prop="date"
                        align="center"
                        :label="`tms2.0元模型-${metaData[0].dbName}`">
                        <el-table-column
                          prop="name"
                          align="center"
                        >
                          <template slot="header">
                            <span>{{metaData[0].tableName+`(`+metaData[0].tableNameCn+`)`}}</span>
                            <el-button type="text" size="mini" @click="config(metaData[0].modelId,0)">配置</el-button>
                          </template>
                          <el-table-column
                            prop="columnName"
                            label="属性名称"
                            width="180">
                          </el-table-column>
                          <el-table-column
                            prop="columnNameCn"
                            label="中文名称"
                            :show-overflow-tooltip="true"
                            width="180">
                          </el-table-column>
                        </el-table-column>
                      </el-table-column>
                    </el-table>
                  </div>
                  <!--</el-col>-->
                  <!--<el-col :span="11">-->
                  <div v-for="item,i in sourceData" :id="`draggable${i+2}`" class="draggable2"  style="margin-right: 10px">
                    <el-table
                      ref="tableData2"
                      row-key="columnId"
                      :data="item.uidColumnList"
                      v-model="item.uidColumnList"
                      height="500"
                      style="width: 100%">
                      <el-table-column
                        type="index"
                        label="序号"
                        align="center"
                        width="60">
                      </el-table-column>
                      <el-table-column
                        prop="date"
                        align="center"
                        :label="`源数据-${item.dbName}`">
                        <el-table-column
                          prop="name"
                          align="center"
                        >
                          <template slot="header">
                            <span>{{item.tableName}}</span>
                            <el-button type="text" size="mini" @click="config(item.tableId,i+1)">配置</el-button>
                          </template>
                          <el-table-column
                            label="属性名称"
                            prop="columnName"
                            width="175">
                            <template slot-scope="scope">
                              <el-select v-model="scope.row.columnName" clearable placeholder="请选择" @change="selectCn(scope.row.columnName,i,scope.$index)">
                                <el-option
                                  v-for="a in option[i]"
                                  :key="a.id"
                                  :label="a.columnName"
                                  :value="a.id">
                                </el-option>
                              </el-select>
                            </template>
                          </el-table-column>
                          <el-table-column
                            prop="columnNameCn"
                            label="中文名称"
                            :show-overflow-tooltip="true"
                            width="175">
                          </el-table-column>
                        </el-table-column>
                      </el-table-column>
                    </el-table>
                  </div>
                  <!--</el-col>-->
                </div>

              </el-row>
            </div>


            <div slot="footer" class="dialog-footer">
              <el-button @click="markVisible = false">取 消</el-button>
              <el-button type="primary" @click="queding">确 定</el-button>
            </div>
          </el-dialog>
          <!--元模型、源数据唯一标识对话框-->
          <el-dialog
            :title="marktitle"
            append-to-body
            :close-on-click-modal="false"
            :visible.sync="modelMarkVisible"
            @close="reset"
            destroy-on-close
            width="52%">
            <div style="margin-bottom: 10px">
              <el-row>
                <el-col :span="2">已配置：</el-col>
                  <el-col :span="22">
                    <draggable v-model="item" animation="300">
                    <span v-for="tag,i in item" style="margin-right: 10px;">
                      <el-tag v-if="tag.columnNameCn"  style="cursor: move;margin-bottom: 5px;" closable @close="removeList(item,tag)">
                        {{i+1}}--{{tag.columnNameCn}}
                      </el-tag>
                      <el-tag v-else  style="cursor: move;margin-bottom: 5px;" closable @close="removeList(item,tag)">
                        {{i+1}}--{{tag.columnName}}
                      </el-tag>
                    </span>
                    </draggable>
                  </el-col>
              </el-row>
            </div>
            <div>
              <el-table
                ref="multipleTable"
                height="500px"
                :data="markTableData"
                style="margin-top:20px;margin-left:2%;width: 96%"
                tooltip-effect="dark"
                highlight-current-row
                :header-cell-style="{background:'#F5F5F5'}"
                loading="markLoading"
                row-key="id"
                @selection-change="handleSelectionChange">
                <el-table-column
                  type="selection"
                  :reserve-selection="true"
                  width="55">
                </el-table-column>
                <el-table-column
                  label="序号"
                  width="80"
                  type="index"
                  align="center">
                </el-table-column>
                <el-table-column
                  align="center"
                  label="属性名称"
                  prop="columnName"
                  width="180">
                </el-table-column>
                <el-table-column
                  align="center"
                  label="中文名称"
                  prop="columnNameCn"
                  width="150">
                </el-table-column>
                <el-table-column
                  align="center"
                  label="属性类型"
                  prop="columnType"
                  width="120">
                </el-table-column>
                <el-table-column
                  align="center"
                  label="属性长度"
                  prop="columnLength"
                  width="120">
                </el-table-column>
                <el-table-column
                  align="center"
                  label="是否主键"
                  width="100"
                  prop="isPrimaryKey">
                </el-table-column>
                <el-table-column
                  align="center"
                  label="是否必填"
                  width="100"
                  prop="isNotNull">
                </el-table-column>
              </el-table>
            </div>

            <div slot="footer" class="dialog-footer">
              <el-button @click="modelMarkVisible = false">取 消</el-button>
              <el-button type="primary" @click="saveSeleteMark">确 定</el-button>
            </div>
          </el-dialog>
        </div>
      </div>
      <div>
        <div >
          <el-dialog
            :visible.sync="mainTableDrawer"
            title="选择关联主表"
            :close-on-click-modal="false"
            style="margin-top: 18vh !important;"
            class="cluTable"
          >
            <el-form :model="mainTableForm" label-width="80px"  ref="mainTableForm" style="margin-left:30px">


                <el-form-item style="flex: 6" label="关联数据源" prop="relationDbId"
                :rules="[
                        { required: true, message: '不能为空',trigger:'blur'}
                  ]">
                  <el-select filterable @change="getDbTableList(mainTableForm,mainTableForm.relationDbId)" v-model="mainTableForm.relationDbId" placeholder="请选择" clearable>
                      <el-option
                        v-for="item in dbList"
                        :key="item.id"
                        :label="item.dbNameCn"
                        :value="item.id">
                      </el-option>
                  </el-select>
                </el-form-item>
                <el-form-item style="flex: 6" label="关联表"  prop="relationTableId"
                :rules="[
                        { required: true, message: '不能为空',trigger:'blur'}
                  ]">
                  <el-select @change="getTableClu(mainTableForm.relationTableId)" filterable v-model="mainTableForm.relationTableId" placeholder="请选择" clearable>
                    <el-option
                      v-for="item in tableList"
                      :key="item.id"
                      :label="item.tableName"
                      :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>


                <el-form-item style="flex: 6" label="关联表别名" prop="relationTableAlias"
                  :rules="[
                        { required: true, message: '不能为空',trigger:'blur'}
                  ]">
                  <el-input  v-model="mainTableForm.relationTableAlias" placeholder="请以字母开头，由字母、数字、下划线组成"  @input="$forceUpdate()"></el-input>
                </el-form-item>

            </el-form>
            <div slot="footer" class="dialog-footer">
              <el-button  type="primary" @click="saveMainTable()" >确 定</el-button>
              <el-button @click="mainTableDrawer = false">取 消</el-button>
            </div>
          </el-dialog>
        </div>
        <div class="addGl">
          <el-dialog
            :visible.sync="glCluDrawer"
            :title="glTableCluName"
            :close-on-click-modal="false"

          >
            <el-form :model="glTableCluForm" label-width="80px"  ref="glTableCluForm">

              <div style="margin-top: 20px;display: flex;width: 70%">
                <el-form-item style="flex: 6" label="关联数据源" prop="relationDbIdF">
                  <el-select filterable @change="getDbTableList(glTableCluForm,glTableCluForm.relationDbId,1)" v-model="glTableCluForm.relationDbId" placeholder="请选择" clearable>
                      <el-option
                        v-for="item in dbList"
                        :key="item.id"
                        :label="item.dbNameCn"
                        :value="item.id">
                      </el-option>
                  </el-select>
                </el-form-item>
                <el-form-item style="flex: 6" label="关联表"  prop="relationTableIdF">
                  <el-select @change="getTableClu(glTableCluForm.relationTableId)" filterable v-model="glTableCluForm.relationTableId" placeholder="请选择" clearable>
                    <el-option
                      v-for="item in tableList"
                      :key="item.id"
                      :label="item.tableName"
                      :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </div>
              <div style="display: flex;width: 70%">
                <el-form-item style="flex: 6" label="关联属性" prop="columnName">
                  <el-select disabled placeholder="单击表格进行选择" v-model="glCluObj.columnId" @change="$forceUpdate()" clearable>
                    <el-option :key="columnListA.id" :value="columnListA.id" :label="columnListA.label"></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item style="flex: 6" label="关联表别名" prop="glRelationTableName">
                  <el-input  v-model="glTableCluForm.glRelationTableName" placeholder="请以字母开头，由字母、数字、下划线组成" ></el-input>
                </el-form-item>
              </div>
              <el-input style="width: 250px;height: 20px;margin-left:520px" v-model="searchGlName" placeholder="请输入属性名称"></el-input>
              <el-button :disabled="ifSel" size="mini" plain style="padding:5px 15px;width:80px;margin-left:30px" type="info" @click="getTableClu(glTableCluForm.relationTableId)" icon="el-icon-search" >搜索</el-button>
              <el-table
                ref="multipleTable"
                :data="tableDataClu"
                height="300px"
                style="margin-top:20px;margin-left:2%;width: 96%"
                tooltip-effect="dark"
                highlight-current-row
                @current-change="handleCurrentChange"
                :header-cell-style="{background:'#F5F5F5'}">
                <el-table-column
                  label="序号"
                  width="80"
                  align="center"
                  :formatter="snFormatter1">
                </el-table-column>
                <el-table-column
                  prop="columnName"
                  align="center"
                  label="属性名称"
                  width="120">
                </el-table-column>
                <el-table-column
                  prop="columnNameCn"
                  align="center"
                  label="中文名称"
                  width="120">
                </el-table-column>
                <el-table-column
                  prop="columnType"
                  align="center"
                  label="属性类型"
                  width="120">
                </el-table-column>
                <el-table-column
                  prop="columnLength"
                  align="center"
                  label="属性长度"
                  width="120">
                </el-table-column>
                <el-table-column
                  prop="remark"
                  align="center"
                  label="备注"
                  width="150">
                </el-table-column>
                <el-table-column
                  prop="createTime"
                  align="center"
                  label="创建时间"
                  width="150">
                </el-table-column>
              </el-table>

              <div style="margin-top: 20px;display:flex;">
                <el-form-item class="aa" style="margin-left: -10px;" label="是否外联">
                  <template>
                    <el-radio v-model="glTableCluForm.isExternalConnection" :label="1">是</el-radio>
                    <el-radio v-model="glTableCluForm.isExternalConnection" :label="0">否</el-radio>
                  </template>
                </el-form-item>
                <el-form-item style="margin-left: 10px;" label="关联条件">
                  <el-input style="border: none;width: 300px" v-model="glTableCluForm.relationCondition">
                  </el-input>
                </el-form-item>
              </div>
            </el-form>
            <div slot="footer" class="dialog-footer">
              <el-button  type="primary" @click="saveGlData()" >确 定</el-button>
              <el-button @click="glCluDrawer= false">取 消</el-button>
            </div>
          </el-dialog>
        </div>
        <div class="openClu">
          <el-dialog
            title="关联主表属性"
            v-dialogDrag
            :close-on-click-modal="false"
            :visible.sync="mainTableCluVisible" >
            <el-table height="330px"
                      tooltip-effect="dark"
                      :data="mainTableList">
              <el-table-column
                label="序号"
                width="60"
                align="center"
                :formatter="snFormatter">
              </el-table-column>
              <el-table-column
                prop="columnName"
                align="center"
                label="属性名称"
                width="100">
              </el-table-column>
              <el-table-column
                prop="columnNameCn"
                align="center"
                label="中文名称"
                width="120">
              </el-table-column>
              <el-table-column
                prop="columnType"
                align="center"
                label="属性类型"
                width="100">
              </el-table-column>
              <el-table-column
                prop="columnLength"
                align="center"
                label="属性长度"
                width="100">
              </el-table-column>
              <el-table-column
                prop="remark"
                align="center"
                label="备注"
                width="120">
              </el-table-column>
            </el-table>
          </el-dialog>
        </div>
      </div>
    </div>
  </template>
<script>
  import request from '@/utils/request'
import qs from 'qs'
  import draggable from 'vuedraggable'
  import Sortable from 'sortablejs'
  import { disposeMetaDataUid,metaUid,sourceUid,sourcelist,modifyMetaUid,modify } from '@/api/ysjgl/zhgl'
  import options from "../../../../utils/dict/DictOptions";
export default {
  name: 'zhgl',
  //注册draggable组件
  components: {
    draggable
  },
  props: {
    modelId: String,
    modelName: String,
    modelVersion: String,
    versionShow: Boolean,
    required: true
  },
  data () {
    return {
      value:'',
      multipleSelection:[],
      //唯一标识元模型列表
      metaData: [],
      option:[
      //   {
      //   id:'',
      //   columnName:''
      // }
      ],
      modelData:[],
      mainTableDrawer:false,
      //sql关联模块
      sqlStr:'',
      sqlCluData:[],
      sqlCluName:[],
      LArr:[],
      ifSqlUp: false,
      RArr:[],
      attrArr:[],
      idList:[],
      // 当前点击行的下标
      cellIndex: null,
      tabName: '1',
      ifSel: true,
      // 分页
      pageSize: 10,
      pageIndex: 1,
      total: 0,
      ifSave: true,
      glRelationTableName: '',
      glTableCluName: '',
      addTableDrawer: false,
      addTableCluDrawer: false,
      glCluDrawer: false,
      mainTableCluVisible: false,
      selForm: {
        cluName: ''
      },
      techId:'',
      columnTypeList: [],
      dbList: [],
      tableList: [],
      typeList: [],
      categoryList: [],
      mainTableList: [],
      relationModelList: [],
      relationDbList: [],
      relationTableList: [],
      relationDbIdF: '',
      relationTableIdF: '',
      relationTableAlias: '',
      seeClu: true,
      loading: false,
      isRelationTableId: true,
      isrelationColumnId: true,
      tableData: [],
      relationColumnId:'',
      tableDataClu: [],
      glTableCluFormD: {},
      dbName: '',
      searchGlName: '',
      tableForm: {
        dbId: '',
        id: '',
        modelName: '',
        modelNameCn: '',
        relationTableAlias: '',
        remark: '',
        relationDbName: '',
        relationDbNameCn: '',
        relationDbId: '',
        relationTableName: '',
        relationTableNameCn: '',
        relationTableId: '',
        categoryId: '',
        categoryName: '',
        categoryNameCn: ''
      },
      addTableForm: {
        modelName: '',
        modelNameCn: '',
        relationTableAlias: '',
        remark: '',
        relationDbName: '',
        relationDbNameCn: '',
        relationDbId: '',
        relationTableName: '',
        relationTableNameCn: '',
        relationTableId: '',
        categoryId: '',
        categoryName: '',
        categoryNameCn: ''
      },
      addTableCluForm: {
        attributeName: '',
        attributeNameCn: '',
        attributeType: '',
        attributeLength: '',
        remark: '',
        isPrimaryKey: ''
      },
      ifSaveGl:true,
      // 关联弹框属性(下拉框)
      columnListA: [{
        id: '',
        label: ''
      }],
      // 导入表格属性
      showImport: false,
      ifUpload: true,
      addFileName: '',
      // 导入后的数据列表
      showImportData: false,
      importData: [],
      editCluForm: {
        id: '',
        attributeName: '',
        attributeNameCn: '',
        attributeType: '',
        attributeLength: '',
        relationTableId: '',
        relationTableName: '',
        relationTableNameCn: '',
        relationTableAlias: '',
        relationDbName: '',
        relationDbNameCn: '',
        relationDbId: '',
        relationColumnId: '',
        relationColomnName: '',
        relationColomnNameCn: '',
        relationCondition: '',
        remark: '',
        isPrimaryKey: ''
      },
      mainTableForm: {
        relationTableAlias:''
      },
      glTableCluForm: {
        id: '',
        isExternalConnection: 0,
        relationTableId: '',
        glRelationTableName:'',
        relationTableName: '',
        relationTableNameCn: '',
        relationTableAlias: '',
        relationDbName: '',
        relationDbNameCn: '',
        relationDbId: '',
        relationColumnId: '',
        relationColomnName: '',
        relationColomnNameCn: '',
        relationCondition: '',
        mainTableClu: '',
        dbId: ''
      },
      mainTableName: '',
      columnList: [],
      relationId: '',
      glCluObj: {columnName: ''},
      filterText: '',
      editDrawer: false,
      data: [{
        id: '1',
        label: '元模型',
        icon: 'el-icon-s-home',
        color: 'color: #FFC000',
        children: []
      }],
      rules: {
        modelNameCn: [
          { required: true, message: '.', trigger: 'blur' }
        ]
      },
      //唯一标识配置对话框
      markVisible:false,
      //元模型源数据唯一标识对话框
      modelMarkVisible:false,
      //配置选择时的唯一标识列表
      markTableData:[],
      //选中的数据
      item:[],
      drawBodyWrapper:'',
    //  唯一标识源数据列表
      sourceData:[],
      //唯一标识配置title
      marktitle:'',
    //  保存返回的数据
      saveList:[]
    }
  },
  watch: {
    sqlStr(){
      for (let i = 0; i < this.sqlCluData.length;i++) {
        this.sqlCluData[i].cluType=0
      }
      let str=this.sqlStr
      this.LArr=[]
      this.RArr=[]
      this.attrArr=[]
      this.idList=[]
      this.sqlCluName=this.sqlCluData.concat([])
      let arr=str.split('select')
      if(arr[1].indexOf('from')!==-1){
        arr=arr[1].split('from')
      }else{
        arr=arr[1].split('FROM')
      }
      str=arr[0]
      this.getIndex(str,"(",0,0)
      this.getIndex(str,")",0,1)
      if(this.LArr.length>0){
        for (let i=this.LArr.length-1;i>=0;i--) {
          let astr=str.substring(this.LArr[i],this.RArr[i]+1)
          str=str.replace(astr,'')
        }
      }
      arr=str.split(',')
      for (let i=0;i<arr.length;i++) {
        if(arr[i].includes('\n')){
          this.repalceN(arr[i])
        }
        arr[i]=arr[i].trim()
        let nerArr=arr[i].split(' ')
        if(nerArr.length>1){
          this.attrArr.push(nerArr[nerArr.length-1])
        }else{
          this.attrArr.push(nerArr[0])
        }
      }
      for (let i = 0; i < this.sqlCluData.length;i++) {
        this.sqlCluData[i].cluType=0
        for (let j = 0; j < this.attrArr.length; j++) {
          if(this.sqlCluData[i].cluName===this.attrArr[j]){
            this.sqlCluName[i].cluType=1
          }
        }
      }
      let newArrClu=[]
      let newArrClu2=[]
      /* console.log(this.sqlCluName) */
      for (let i = 0; i < this.sqlCluName.length;i++) {
        if(this.sqlCluName[i].cluType===1){
          newArrClu.push(this.sqlCluName[i])
          this.idList.push(this.sqlCluName[i].id)
        }else{
          newArrClu2.push(this.sqlCluName[i])
        }
      }
      this.sqlCluName=newArrClu.concat(newArrClu2)
    },
    modelId(){
      this.mark()
    }
  },
  methods: {

    //元模型/源数据局唯一标识对话框关闭时的回调
    reset(){
      // this.item=[]
    },
    //打开配置
    async config(id,i){
      // this.item=[]
      if(i=='0'){
        await metaUid({modelId:id}).then(res=>{
          this.markTableData=res.data
          for(let i=0;i<this.markTableData.length;i++){
            this.markTableData[i].isPrimaryKey=this.markTableData[i].isPrimaryKey==='1' ? '是' : '否'
            this.markTableData[i].isNotNull=this.markTableData[i].isNotNull==='1' ? '是' : '否'
          }
          this.marktitle='元模型唯一标识配置'
          this.item=this.markTableData.filter(item=>item.columnOrder!='not_uid')
        })
        this.modelMarkVisible=true
      }else {
        await sourceUid({tableId:id}).then(res=>{
          this.markTableData=res.data
          for(let i=0;i<this.markTableData.length;i++){
            this.markTableData[i].isPrimaryKey=this.markTableData[i].isPrimaryKey==='1' ? '是' : '否'
            this.markTableData[i].isNotNull=this.markTableData[i].isNotNull==='1' ? '是' : '否'
          }
          this.marktitle='源数据唯一标识配置'
          this.item=this.markTableData.filter(item=>item.columnOrder!='not_uid')
          this.saveList=this.sourceData[i-1]
        })
        this.modelMarkVisible=true
      }
      //选中回显
      // if(this.$refs.multipleTable){
        this.$nextTick(function(){
          this.$refs.multipleTable.clearSelection()
          this.item.forEach(key=>{
            this.markTableData.forEach(row=>{
              if(key.id==row.id){
                this.$refs.multipleTable.toggleRowSelection(row, true);
              }
            })
          })
        })
      // }
    },
    //源数据的下拉框选择后的中文名称显示
    selectCn(name,i,index){
      const item=this.option[i].filter(item1=>item1.id==name)
      this.sourceData[i].uidColumnList[index].columnName=item[0].columnName
      this.sourceData[i].uidColumnList[index].columnNameCn=item[0].columnNameCn
      this.sourceData[i].uidColumnList[index].columnId=item[0].id
    },
    //唯一标识配置多选
    handleSelectionChange(val) {
      // this.multipleSelection = val;
      this.item=val
    },
    //保存唯一标识选择
    saveSeleteMark(){
      // this.markForm = this.item
      if(this.marktitle=='元模型唯一标识配置'){
        let data={
          dbId:this.metaData[0].dbId,
          dbName:this.metaData[0].dbName,
          modelId:this.metaData[0].modelId,
          modelType:this.metaData[0].modelType,
          tableName:this.metaData[0].tableName,
          tableNameCn:this.metaData[0].tableNameCn,
          uidColumnList:[]
        }
        for(let i=0;i<this.item.length;i++){
          let a={
            columnId:this.item[i].id,
            columnName: this.item[i].columnName,
            columnNameCn:this.item[i].columnNameCn,
            dbId:this.metaData[0].dbId,
            modelId:this.metaData[0].modelId,
            modelType:this.metaData[0].modelType,
            modelVersion:'edit'
          }
          data.uidColumnList.push(a)
        }
        modifyMetaUid(data).then(res=>{
          if(res.code==200){
            this.$message.success('操作成功')
            this.openMarkDialog()
          }
        })
      }else{
        let data={
          dbId:this.saveList.dbId,
          dbName:this.saveList.dbName,
          modelId:this.saveList.modelId,
          modelType:this.saveList.modelType,
          tableName:this.saveList.tableName,
          tableNameCn:this.saveList.tableNameCn,
          tableId:this.saveList.tableId,
          uidColumnList:[]
        }
        for(let i=0;i<this.item.length;i++){
          let a={
            columnId:this.item[i].id,
            columnName: this.item[i].columnName,
            columnNameCn:this.item[i].columnNameCn,
            dbId:this.saveList.dbId,
            modelId:this.saveList.modelId,
            modelType:this.saveList.modelType,
            modelVersion:'edit',
            tableId:this.saveList.tableId,
          }
          data.uidColumnList.push(a)
        }
        modifyMetaUid(data).then(res=>{
          if(res.code==200){
            this.$message.success('操作成功')
            this.openMarkDialog()
          }
        })
      }

      this.modelMarkVisible = false
    },
    //获取唯一标识数据
    async mark(){
      this.metaData=[]
      this.sourceData=[]
      await disposeMetaDataUid({modelId:this.modelId}).then(res=>{
        this.metaData=res.data.metaUidTableVoList.filter(item=>item.modelType=='meta')
        this.sourceData=res.data.metaUidTableVoList.filter(item=>item.modelType=='source')
        for(let i=0;i<this.sourceData.length;i++){
          sourcelist({tableId:this.sourceData[i].tableId}).then(res=>{
            // this.sourceData[i].options=res.data
            this.option[i]=res.data
          })
        }
      })
    },
    //打开唯一标识配置
    async openMarkDialog(){
      await this.mark()
      if(this.sourceData.length==0){
        this.$message({
          showClose: true,
          message: '请关联数据源，关联表',
          type: 'warning'
        });
      }else{
        if(this.metaData.length>0){
          this.markVisible=true
          this.$nextTick(() => {
            this.rowDrop()
            this.rowDrop1()
          })
        }
      }
    },
    // 行拖拽
    rowDrop() {
      // 此时找到的元素是要拖拽元素的父容器
      let tbody = document.querySelector('.draggable1 .el-table__body-wrapper tbody')
      let _this = this
      Sortable.create(tbody, {
        animation: 300, // ms, number 单位：ms，定义排序动画的时间
        onEnd ({ newIndex, oldIndex }) { // 结束拖拽
          let currRow = _this.metaData[0].uidColumnList.splice(oldIndex, 1)[0]
          _this.metaData[0].uidColumnList.splice(newIndex, 0, currRow)
        }
      })
    },
    rowDrop1() {
      // 此时找到的元素是要拖拽元素的父容器2,3,4
      let y=this.sourceData.length
      for(let i=2;i<y+2;i++){
        let tbody = document.querySelector(`#draggable${i} .el-table__body-wrapper tbody`)
        let _this = this
        Sortable.create(tbody, {
          animation: 300, // ms, number 单位：ms，定义排序动画的时间
          onEnd ({ newIndex, oldIndex }) { // 结束拖拽
            let currRow = _this.sourceData[i-2].uidColumnList.splice(oldIndex, 1)[0]
            _this.sourceData[i-2].uidColumnList.splice(newIndex, 0, currRow)
          }
        })
      }

    },
    queding(){
      let data={
        modelId:this.modelId,
        metaUidTableVoList:[],
      }
      data.metaUidTableVoList.push(this.metaData[0])
      for(let i=0;i<this.sourceData.length;i++){
        data.metaUidTableVoList.push(this.sourceData[i])
      }
      modify(data).then(res=>{
        if(res.code==200){
          this.$message.success('操作成功')
        }
      })
      this.markVisible=false

    },
    //选择唯一标识配置标签关闭
    removeList: function (list, v) {
      this.item.splice(this.item.indexOf(v), 1);
      this.$refs.multipleTable.toggleRowSelection(v)
    },
    //
    getModelData() {
      request.get('model/getOne?id='+ this.modelId, qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        /* console.log(res) */
        this.modelData = res.data
        this.mainTableName = this.modelData.relationMainTableName
        /* console.log(this.modelData) */
      }).catch(e => {
        // 请求失败处理。
      })
    },
    saveMainTable() {
      var tMetaModelTech = {
        id: this.modelId,
        relationMainDbId: this.mainTableForm.relationDbId,
        relationMainTableAlias: this.mainTableForm.relationTableAlias,
        relationMainTableId: this.mainTableForm.relationTableId
      }
      request({
        headers: {
          'Content-Type': 'application/json'
        },
        method: 'put',
        url: '/model',
        data: tMetaModelTech
      }).then((res) => {
        // 请求成功处理。
        this.getModelData()
        this.mainTableDrawer = false
        this.$message({
          message: res.msg,
          type: 'success',
          center: true
        })
      }).catch(e => {
        // 请求失败处理。
      })
    },
    mainTable(){
      this.mainTableForm={}
      this.$nextTick(()=>{
        this.$refs['mainTableForm'].clearValidate()
      })
      this.getModelData()
      this.mainTableDrawer = true
      if(this.modelData.relationMainTableId !==''||this.modelData.relationMainTableId !== null || this.modelData.relationMainTableId !==undefined) {
        this.mainTableForm.relationTableId = this.modelData.relationMainTableId
      }
      if(this.modelData.relationMainDbId !== '' || this.modelData.relationMainDbId !== null || this.modelData.relationMainDbId !==undefined) {
        this.mainTableForm.relationDbId = this.modelData.relationMainDbId
        this.getDbTableList(1,this.mainTableForm.relationDbId,1,1)
      }
      if(this.modelData.relationMainTableAlias !== '' || this.modelData.relationMainTableAlias !== null || this.modelData.relationMainTableAlias !==undefined){
        /* this.mainTableForm.relationTableAlias = this.modelData.relationMainTableAlias */
        this.$set(this.mainTableForm,'relationTableAlias',this.modelData.relationMainTableAlias)
      } else {
        this.$set(this.mainTableForm,'relationTableAlias','')
      }


    },
    filterNode (value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    },
    /* 格式序列化列 */
    snFormatter: function (row, col, value, index) {
      return this.pageSize * (this.pageIndex - 1) + index + 1;
      /* return index + 1 */
    },
    /* 格式序列化列 */
    snFormatter1: function (row, col, value, index) {
      /* return this.pageSize * (this.pageIndex - 1) + index + 1; */
       return index + 1
    },
    /* 格式所属表列 */
    tableIdFormatter: function (row, col, value, index) {
      for (var i = 0; i < this.columnTableList.length; i++) {
        if (this.columnTableList[i].id === value) {
          return this.columnTableList[i].tableName
        }
      }
      return value
    },
    handleSizeChange (val) {
      this.pageSize = val
      this.getData()
    },
    handleCurrentChange1 (val) {
      this.pageIndex = val
      this.getData()
    },
    handleCurrentChange (row) {
      this.columnListA.id = row.id
      this.columnListA.label = row.columnName
      this.glCluObj.columnId = row.id
      this.change()
    },
    addTable: function () {
      for (var a in this.addTableForm) {
        this.addTableForm[a] = ''
      }
      this.addTableForm.modelName = 't_meta_'
      this.addTableDrawer = true
    },
    // 添加表格属性
    addTableClu: function () {
      for (var a in this.addTableCluForm) {
        this.addTableCluForm[a] = ''
      }
      this.addTableCluForm.isPrimaryKey = '0'
      this.addTableCluDrawer = true
    },
    // 导入表格属性
    importTableClu: function () {
      this.addFileName = ''
      this.showImport = true
    },
    // 获取导入文件名称
    chooseAddFileChange: function () {
      let files = this.$refs.addFileInput.files
      this.addFileName = files[0].name
    },
    // 接收导入文件
    chooseAddFile: function () {
      this.$refs.addFileInput.value = ''
      this.$refs.addFileInput.dispatchEvent(new MouseEvent('click'))
    },
    // 确认导入
    importFile: function () {
      let files = this.$refs.addFileInput.files
      this.addFileName = files[0].name
      let formData = new FormData()
      formData.append('file', files[0], this.addFileName)
      let config = {
        headers: {
          'Content-Type': 'multipart/form-data',
          'Authorization': this.token
        }
      }
      // 向后台请求区域查询
      request.post(localStorage.URL + '/file/upload/', formData, config).then((res) => {
        // 请求成功处理。
        this.toImport(res.data)
      }).catch(e => {
        // 请求失败处理。
        this.$message({
          message: e,
          type: 'error',
          center: true,
            duration: 0,
            showClose: true
        })
      })
    },
    // 获取属性类型下拉框数据
    getcolumnTypeList: function () {
      request.get('tech/type', qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        /* console.log(res) */
        this.columnTypeList = res.data
      }).catch(e => {
        // 请求失败处理。
      })
    },
    // 刷新关联弹框关联属性
    change (e) {
      this.$forceUpdate()
    },
    // 编辑操作
    editTable ({ $index }, row) {
      // 获取当前点击行下标
      /* alert(row.relationDbId) */
      this.cellIndex = $index
      this.getDbTableList(row, row.relationDbId,1)
      this.getTableClu(row.relationTableId,1)
      this.getRelationTableAlex(row,row.relationTableId,1)
    },
    // 保存操作
    saveTable ({ row }) {

      if(row.relationId) {
      var tMetaModelTech = {
        relationCondition: row.relationCondition,
        isExternalConnection: row.isExternalConnection,
        relationColumnId: row.relationColumnId,
        relationDbId: row.relationDbId,
        relationId: row.relationId,
        relationTableId: row.relationTableId,
        relationTableAlias: row.relationTableAlias,
        techId: row.techId,
        modelVersion:this.modelVersion,
        updateOrNot: row.updateOrNot
      }
      request({
        headers: {
          'Content-Type': 'application/json'
        },
        method: 'put',
        url: '/relation?modelId='+ this.modelId,
        data: tMetaModelTech
      }).then((res) => {
        // 请求成功处理。
        /* this.getData() */
        this.$message({
          message: res.msg,
          type: 'success',
          center: true
        })
      }).catch(e => {
        // 请求失败处理。
      })
    } else {
      var tMetaModelTech = {
        relationCondition: row.relationCondition,
        isExternalConnection: row.isExternalConnection,
        relationColumnId: row.relationColumnId,
        relationDbId: row.relationDbId,
        relationId: row.relationId,
        relationTableId: row.relationTableId,
        relationTableAlias: row.relationTableAlias,
        techId: row.techId,
        modelVersion:this.modelVersion
      }
      request({
        headers: {
          'Content-Type': 'application/json'
        },
        method: 'put',
        url: '/relation/add?modelId='+ this.modelId,
        data: tMetaModelTech
      }).then((res) => {
        // 请求成功处理。
        this.getData()
        this.$message({
          message: res.msg,
          type: 'success',
          center: true
        })
      }).catch(e => {
        // 请求失败处理。
      })
    }
      this.cellIndex = null
    },
    // 删除操作
    deletTable: function ({ row }) {
      this.$confirm("是否删除此数据？", "提示", {
          confirmButtonText: "确认",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(() => {
            request.post('/relation/delete?relationId=' + row.relationId, qs.stringify({
        })).then((res) => {
          // 请求成功处理。
          this.getData()
          this.$message({
            message: res.msg,
            type: 'success',
            center: true
          })
        }).catch(e => {
          // 请求失败处理。
          this.$message({
            message: e,
            type: 'error',
            center: true,
            duration: 0,
            showClose: true
          })
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })


    },
    // 保存所有批量导入属性
    saveImportData: function () {
      let a = 0
      let flag = false
      let reg = /^[a-zA-Z_]{1}[a-zA-Z\d_]*/
      for (let i = 0; i < this.importData.length; i++) {
        if (parseInt(this.importData[i].isPrimaryKey) === 1) {
          a += 1
        }
        if (!reg.test(this.importData[i].attributeName)) {
          flag = true
        }
      }
      if (a > 1) {
        this.$message({
          message: '请保持主键唯一性',
          type: 'error',
          center: true,
            duration: 0,
            showClose: true
        })
        return
      }
      if (flag) {
        this.$message({
          message: '属性名称命名不规范',
          type: 'error',
          center: true,
            duration: 0,
            showClose: true
        })
        return
      }
      let res = this.importData
      for (let i = 0; i < res.length; i++) {
        request.post(localStorage.URL + '/metamodelattribute/create', qs.stringify({
          attributeName: res[i].attributeName,
          attributeNameCn: res[i].attributeNameCn,
          attributeType: res[i].attributeType,
          attributeLength: res[i].attributeLength,
          isPrimaryKey: res[i].isPrimaryKey,
          isRequired: res[i].isRequired,
          isSearch: res[i].isSearch,
          isShow: res[i].isShow,
          modelId: this.modelId,
          remark: ''
        })).then((ress) => {
          // 请求成功处理。
          if (i === res.length - 1) {
            this.$message({
              message: '保存成功',
              type: 'success',
              center: true
            })
            this.showImportData = false
            window.setTimeout(() => {
              this.getData()
            }, 3000)
          }
        }).catch(e => {
          // 请求失败处理。
          this.$message({
            message: e,
            type: 'error',
            center: true,
            duration: 0,
            showClose: true
          })
        })
      }
    },
    glColumn: function (row) {
      this.glTableCluName = '关联元模型属性' + row.columnName
      if(row.relationId) {
        this.relationId = row.relationId
      } else if(row.relationId === undefined) {
        this.relationId = undefined
      }
      if(row.techId) {
       this.techId = row.techId
      } else if(row.techId === undefined) {
        this.techId = undefined
      }
      this.glTableCluForm.relationTableId=''
      this.glCluObj.columnId=''
      this.glCluObj = {}
      this.glCluDrawer = this.seeClu = true
      this.glTableCluFormD = row
      this.columnListA.id = row.relationColumnId===undefined?'':row.relationColumnId
      this.columnListA.label = row.relationColumnName===undefined?'':row.relationColumnName
      this.glTableCluForm.relationDbId = row.relationDbId===undefined?'':row.relationDbId
      if(row.relationDbId!==undefined){
        this.getDbTableList(row,row.relationDbId,1)
      } else {
        this.glTableCluForm.relationDbId = this.modelData.relationMainDbId
        this.getDbTableList(this.glTableCluForm,this.glTableCluForm.relationDbId,1)
      }
      if(row.relationTableId!==undefined){
        this.glTableCluForm.relationTableId = row.relationTableId===undefined?'':row.relationTableId
        this.getTableClu(row.relationTableId,row.relationColumnId,1)

      } else {
        if(this.modelData.relationMainTableId!==undefined){
          this.glTableCluForm.relationTableId = this.modelData.relationMainTableId
          /* this.getTableClu(this.glTableCluForm.relationTableId,'',1) */
          this.tableDataClu = []
        } else {
          /* this.getTableClu(this.glTableCluForm.relationTableId,'',1) */
          this.tableDataClu = []
        }
      }
      if(row.relationTableAlias!==undefined) {
        this.glTableCluForm.glRelationTableName = row.relationTableAlias
      } else {
        this.glTableCluForm.glRelationTableName = this.modelData.relationMainTableAlias
        for(var a = 0 ; a < this.tableList.length ; a++) {
        if(id===this.tableList[a].id) {
          this.glTableCluForm.glRelationTableName = this.tableList[a].alias
         /*  this.mainTableForm.relationTableAlias = this.tableList[a].alias */
         if( this.tableList[a].alias!==''||this.tableList[a].alias!==null||this.tableList[a].alias!==undefined) {
          this.$set(this.mainTableForm, 'relationTableAlias',this.tableList[a].alias)
        } else {
          this.$set(this.mainTableForm,'relationTableAlias','')
        }
        }
      }
      }
      this.glTableCluForm.glRelationTableName = row.relationTableAlias===undefined?'':row.relationTableAlias
      this.glTableCluForm.isExternalConnection = row.isExternalConnection===undefined?'':0
      this.glTableCluForm.relationCondition = row.relationCondition===undefined?'':row.relationCondition

      //this.getDbTableList()
    },
    // 获取已关联数据
    getRelationList: function (id) {
      this.relationModelList = this.relationDbList = this.relationTableList = []
      this.relationDbIdF = this.relationTableIdF = this.relationTableAlias = ''
      request.get(localStorage.URL + '/metamodelattribute/relationinfolist?modelId=' + id, qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        this.relationModelList = res.data
        for (var i = 0; i < this.relationModelList.length; i++) {
          var ifPush = true
          for (var j = 0; j < this.relationDbList.length; j++) {
            if (this.relationDbList[j].relationDbId === this.relationModelList[i].relationDbId) {
              ifPush = false
            }
          }
          if (ifPush) {
            this.relationDbList.push(this.relationModelList[i])
          }
          this.relationDbIdF = this.tableForm.relationDbId
          this.getRelationTableList(1)
        }
      }).catch(e => {
        // 请求失败处理。
        this.$message({
          message: e,
          type: 'error',
          center: true,
            duration: 0,
            showClose: true
        })
      })
    },
    // 根据已关联数据源获取已关联表
/*     getRelationTableList: function (type) {
      this.relationTableList = []
      this.relationTableIdF = this.relationTableAlias = ''
      this.seeClu = true
      for (var i = 0; i < this.relationModelList.length; i++) {
        if (this.relationModelList[i].relationDbId === this.relationDbIdF) {
          this.relationTableList.push(this.relationModelList[i])
        }
      }
      if (type === 1) {
        this.relationTableIdF = this.tableForm.relationTableId
        this.getRelationTableAlex()
      }
    }, */
    // 获取已关联表别名与属性
    getRelationTableAlex: function (row, id) {
      /* row.relationColumnId = '' */
      /* this.$set(row,'relationColumnId', '') */
      if (id === '' || id === undefined) {
        this.$set(row,'relationColumnId', '')
        this.isrelationColumnId = true
      } else {
        /* this.$set(row,'relationColumnId', '') */
        request.get('/relation/getColumns?tableId=' + id, qs.stringify({
        })).then((res) => {
          // 请求成功处理。
          this.columnList = res.data
          this.isrelationColumnId = false
        }).catch(e => {
          // 请求失败处理。
        })
      }
    },
    // 获取公共数据
    getCommonList: function () {
      // 获取数据源
      request.get('/relation/getDbs', qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        this.dbList = res.data
      }).catch(e => {
        // 请求失败处理。
      })
    },
    // 根据数据源获取表列表
    getDbTableList: function (row, id, type) {
      if(type===1||type==='1') {
      if (id === '' || id === undefined) {
        this.$set(row,'relationTableId', '')
        this.isRelationTableId = true
      } else {
        this.glTableCluForm.relationTableId = ''
        request.get('/relation/getTables?dbId=' + id, qs.stringify({
        })).then((res) => {
          // 请求成功处理。
          this.tableList = res.data
          this.isRelationTableId = false
          this.getTableClu(this.glTableCluForm.relationTableId)
        }).catch(e => {
          // 请求失败处理。
        })
      }
    } else {
     /*  row.relationTableId = ''
      row.relationTableAlias = ''
      row.relationColumnId = ''
 */
      this.$set(row,'relationColumnId', '')
      this.$set(row,'relationTableAlias', '')
      this.$set(row,'relationTableId', '')
      if (id === '' || id === undefined) {
        row.relationTableId = ''
        this.isRelationTableId = true
      } else {
        this.glTableCluForm.relationTableId = ''
        request.get('/relation/getTables?dbId=' + id, qs.stringify({
        })).then((res) => {
          // 请求成功处理。
          this.tableList = res.data
          this.isRelationTableId = false
          this.getTableClu(this.glTableCluForm.relationTableId)
        }).catch(e => {
          // 请求失败处理。
        })
      }
    }
    },
    // 删除表
    delTable: function () {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        request.post(localStorage.URL + '/metamodel/delete', qs.stringify({
          id: this.modelId
        })).then((res) => {
          // 请求成功处理。
          this.modelId = ''
          for (var a in this.tableForm) {
            this.tableForm[a] = ''
          }
          this.getTree()
          this.$message({
            message: res.msg,
            type: 'success',
            center: true
          })
        }).catch(e => {
          // 请求失败处理。
          this.$message({
            message: e,
            type: 'error',
            center: true,
            duration: 0,
            showClose: true
          })
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    // 获取表基本信息
    getTabData: function () {
      this.tableForm = {}
      request.get('/relation/getColumnInfoList?modelId=' + this.modelId, qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        res = res.data
        this.tableForm = res
      }).catch(e => {
        // 请求失败处理。
        this.$message({
          message: e,
          type: 'error',
          center: true,
            duration: 0,
            showClose: true
        })
      })
    },
    // 获取表属性列表
    getData: function () {
      this.tableData = []
      this.loading = true
      request.get('/relation/list?modelId=' + this.modelId + '&modelVersion=' + this.modelVersion  + '&columnName=' + this.selForm.cluName + '&pageNum=' + this.pageIndex + '&pageSize=' + this.pageSize, qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        this.loading = false
        this.tableData = res.rows
        this.getModelData()
        this.total = res.total
        this.ifUploada()
        /* console.log(this.tableData) */
        this.ifSel = false

      }).catch(e => {
        // 请求失败处理。
      })

    },
    ifUploada: function () {
      for(var i=0;i<this.tableData.length;i++) {
        /* console.log(this.tableData[i].relationDbId) */
        if(this.tableData[i].relationDbId===''||this.tableData[i].relationDbId===undefined) {
          this.ifUpload = false
        } else {
          this.ifUpload = true
        }
      }
    },
    // 取消操作
    cancel () {
      this.cellIndex = null
      this.getData()
    },
    // 删除表属性
    delTableClu: function (id) {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        request.post(localStorage.URL + '/metamodelattribute/delete', qs.stringify({
          id: id
        })).then((res) => {
          // 请求成功处理。
          this.getData()
          this.$message({
            message: res.msg,
            type: 'success',
            center: true
          })
        }).catch(e => {
          // 请求失败处理。
          this.$message({
            message: e,
            type: 'error',
            center: true,
            duration: 0,
            showClose: true
          })
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    // 获取关联表所有属性,
    getTableClu: function (id,cluId,type) {
      if(type===1||type==='1') {
      this.glCluObj = {}
      this.tableDataClu = []
      this.glCluObj.columnId = ''
      request.get('/relation/getColumnInfoList?tableId=' + id + '&columnName=' + this.searchGlName, qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        this.tableDataClu = res.data
        if(cluId!==''&&cluId!==undefined){
          this.glCluObj.columnId = cluId
        }
        /* this.checkAlias() */
      }).catch(e => {
        // 请求失败处理。
        this.$message({
          message: e,
          type: 'error',
          center: true,
            duration: 0,
            showClose: true
        })
      })

    } else {
      this.glCluObj = {}
      this.tableDataClu = []
      this.glCluObj.columnId = ''
      request.get('/relation/getColumnInfoList?tableId=' + id + '&columnName=' + this.searchGlName, qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        this.tableDataClu = res.data
        if(cluId!==''&&cluId!==undefined){
          this.glCluObj.columnId = cluId
        }
        /* this.checkAlias() */
      }).catch(e => {
        // 请求失败处理。
        this.$message({
          message: e,
          type: 'error',
          center: true,
            duration: 0,
            showClose: true
        })
      })
    }

    },
    // 判断别名是否存在,是否可修改
    checkAlias: function () {
      this.seeAlias = false
      this.glTableCluForm.glRelationTableName = ''
      var tableId = this.glTableCluForm.relationTableId
      var dbId = this.glTableCluForm.relationDbId
      if (tableId === this.tableForm.relationTableId && dbId === this.tableForm.relationDbId) {
        this.glTableCluForm.glRelationTableName = this.tableForm.relationTableAlias
        this.seeAlias = true
      } else {
        var tableSel = {}
        for (var i = 0; i < this.tableData.length; i++) {
          if (tableId === this.tableData[i].relationTableId && dbId === this.tableData[i].relationDbId) {
            tableSel = this.tableData[i]
          }
        }
        if (tableSel.relationTableName === undefined) {
          this.glTableCluForm.glRelationTableName = ''
        } else {
          this.glTableCluForm.glRelationTableName = tableSel.relationTableAlias
          if (tableSel.id !== this.glTableCluForm.id) {
            this.seeAlias = true
          }
        }
      }
    },
    // 保存关联表信息
    saveGlData: function () {
      this.saveGlTypee()
      if(this.relationId) {
      var tMetaModelTech = {
        relationCondition: this.glTableCluForm.relationCondition,
        isExternalConnection: this.glTableCluForm.isExternalConnection,
        relationColumnId: this.columnListA.id,
        relationDbId: this.glTableCluForm.relationDbId,
        relationId: this.relationId,
        relationTableId: this.glTableCluForm.relationTableId,
        relationTableAlias: this.glTableCluForm.glRelationTableName,
        techId: this.techId,
        modelVersion:this.modelVersion
      }
      request({
        headers: {
          'Content-Type': 'application/json'
        },
        method: 'put',
        url: '/relation?modelId='+ this.modelId,
        data: tMetaModelTech
      }).then((res) => {
        // 请求成功处理。
        this.getData()
        this.glCluDrawer= false
        this.$message({
          message: res.msg,
          type: 'success',
          center: true
        })
      }).catch(e => {
        // 请求失败处理。
      })
    } else {
      var tMetaModelTech = {
        relationCondition: this.glTableCluForm.relationCondition,
        isExternalConnection: this.glTableCluForm.isExternalConnection,
        relationColumnId: this.columnListA.id,
        relationDbId: this.glTableCluForm.relationDbId,
        relationId: this.relationId,
        relationTableId: this.glTableCluForm.relationTableId,
        relationTableAlias: this.glTableCluForm.glRelationTableName,
        techId: this.techId,
        modelVersion:this.modelVersion
      }
      request({
        headers: {
          'Content-Type': 'application/json'
        },
        method: 'put',
        url: '/relation/add?modelId='+ this.modelId,
        data: tMetaModelTech
      }).then((res) => {
        // 请求成功处理。
        this.getData()
        this.glCluDrawer= false
        this.$message({
          message: res.msg,
          type: 'success',
          center: true
        })
      }).catch(e => {
        // 请求失败处理。
      })
    }

/*
      request.put('/relation/relationEdit?relationTableAlias=' + this.glTableCluForm.glRelationTableName + '&isExternalConnection=' +
      this.glTableCluForm.isExternalConnection + '&relationColumnId='+ this.columnListA.id + '&relationDbId=' + this.glTableCluForm.relationDbId
      + '&relationCondition=' + this.glTableCluForm.relationCondition + '&relationId=' + this.relationId + '&relationTableId='+ this.glTableCluForm.relationTableId+'&modelId='+this.modelId+'&modelVersion='+this.modelVersion, qs.stringify({})).then((res) => {
        // 请求成功处理。
        this.glCluDrawer = false
        this.getData()
        if(res.code===200 || res.code==='200'){
        this.$message({
          message: res.msg,
          type: 'success',
          center: true
        })}
      }).catch(e => {
        // 请求失败处理。
        this.$message({
          message: e,
          type: 'error',
          center: true,
            duration: 0,
            showClose: true
        })
      }) */
    },
    //sql关联模块
    //获取表格属性数组
    getSqlClu(){
      this.sqlCluData=[]
      this.sqlCluName=[]
      request.get('/relation/sqlList?modelId=' + this.modelId + '&modelVersion=' + this.modelVersion, qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        res=res.data
        for (let i = 0; i < res.length; i++) {
          this.sqlCluName.push({cluName:res[i].columnName,cluType:0,id:res[i].techId})
          this.sqlCluData.push({cluName:res[i].columnName,cluType:0,id:res[i].techId})
        }
        request.get('/relation/sql?modelId=' + this.modelId + '&modelVersion=' + this.modelVersion, qs.stringify({
        })).then((res) => {
          // 请求成功处理。
          this.sqlStr=res.data


        }).catch(e => {
          // 请求失败处理。
        })
      }).catch(e => {
        // 请求失败处理。
      })
    },
    //替换sql语句中换行符
    repalceN(str){
    str=str.replace('\n','')
    if(str.includes('\n')){
      this.repalceN(str)
    }
  },
    //获取sql语句中括号的索引
  getIndex(str,meta,nindex,type){
    let index=str.indexOf(meta,nindex+1)
    if(index!==-1){
      if(type===0){
        this.LArr.push(index)
      }else{
        this.RArr.push(index)
      }

      this.getIndex(str,meta,index,type)
    }
  },
//保存sql
    saveSql(){
      this.saveGlTypee()
      request.put('/relation/sqlEdit?modelId='+this.modelId+'&sqlContent='+this.sqlStr+'&columnIdList='+this.idList, qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        this.ifSqlUp = true
        this.$message({
          message: res.msg,
          type: 'success',
          center: true
        })
      }).catch(e => {
        // 请求失败处理。
      })
    },
    // 保存sql/字段时 保存关联类型
    saveGlTypee(){
      request.put('/relation/choose?modelId='+this.modelId+'&relationType='+this.tabName, qs.stringify({
      })).then((res) => {
        // 请求成功处理。
      }).catch(e => {
        // 请求失败处理。
      })
    },
    //保存关联类型
    saveGlType(){
      request.put('/relation/choose?modelId='+this.modelId+'&relationType='+this.tabName, qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        this.$message({
          message: res.msg,
          type: 'success',
          center: true
        })
      }).catch(e => {
        // 请求失败处理。
      })
    },
    //初始化relationType
    getRelationType(){
      this.tabName='1'
      request.post('/relation/relationtype', {
        modelId:this.modelId,
        modelVersion:this.modelVersion
      }).then((res) => {
        // 请求成功处理。
        if(res.msg!==undefined){
        this.tabName=res.msg+''}

      }).catch(e => {
        // 请求失败处理。
      })
    }

  },
  created () {
    this.getModelData()
    this.getRelationType()
    this.getCommonList()
    this.getData()
    this.getSqlClu()
  }
}
</script>
<style lang="stylus" rel="stylesheet/stylus" >
    ul,li{list-style:none;}
    .cell >.el-button
      padding 5px 10px !important
    .zhgl
      width 100%
      height 100%
      .cluTable .el-input__inner
        width:240px !important
      .addGl .el-input__inner
        width:240px !important
      .cell .el-input__inner
        height 30px
      .aa .el-input,.aa .el-form-item__content,.aa .el-input__inner
        width  3rem !important
      .el-form-item__label{
          width:100px
        }
      .el-form-item__content{
          margin-left:100px;
          width:300px
        }
      .cluTable .el-dialog
        width:30rem
        height:23rem
        margin-left:35% !important
      .el-dialog
        margin 5% 25%
      .importBox .el-dialog
        width 10rem
        height 5rem
        margin 11% 36%
      .importDatas .el-dialog
        width 18rem
        margin auto
        .el-select
          width 2.5rem
      .openClu .el-dialog
        width 14rem
        height 9rem
        margin 2rem 11rem
      input[readonly='readonly']
        background #F5F7FA
        color #CAC4CC
      .el-select input[readonly='readonly']
        background #fff
        color #000
      .qjcx_con
        width 100%
        height 100%
        display flex
        .con_left
          flex 6
          width:0
          overflow auto
          flex-shrink: 0
          height 100%
          border-right 1px solid lightgrey
          .left_con
            padding .2rem 0
            font-size: 14px
            line-height 20px
            border-bottom 1px solid lightgrey
            .l_con
              margin-top: 10px
              text-indent 4px
        .con_right
          flex 18
          flex-shrink: 0
          width:0
          .table_space
            width 98%
            margin auto
            .el_group
              display flex
              width 80%
              margin auto
        .tit
          font-size 16px
          height 40px
          line-height 40px
          text-indent 1%
          margin-bottom 10px
          border-bottom 1px solid lightgrey
        .tabBarArea:after{
            display: inline;
            content: '';
            clear: both;
          }
        .tabBarBtn {
            float: left;
            margin-left: 20px;
            cursor: pointer;
            white-space:nowrap;
            width: auto;
            height:35px
            padding: 0px;
          }
        .tabBarBtn:hover {
            color: #326BE8;
            border-bottom: 2px solid #326BE8;
            border-top:0px;
            border-left:0px;
            border-right:0px;
          }
        .tabBarBtnActive {
            color: #326BE8;
            border-bottom: 2px solid #326BE8;
            border-top:0px;
            border-left:0px;
            border-right:0px;
          }
        .tabBarBtnActive:hover {
            color: #326BE8;
          }
    #sqlSpace .el-textarea,#sqlSpace .el-textarea__inner{
      height 400px
    }
    .draggable1 .el-table--enable-row-hover .el-table__body tr:hover > td {
      background: #fff !important;
      cursor:move;
    }
  .draggable2 .el-table--enable-row-hover .el-table__body tr:hover > td{
    background: #fff !important;
    cursor:move;
  }

  </style>
