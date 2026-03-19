<template>
  <div class="ywysj">
    <div class="qjcx_con">
      <div class="con_right">
        <div class="r_top">
          <div class="cell" style="width: 100%;text-align: right;position:absolute;top:250px;right:35px">
            </div>

          <div class="table_space" style="height: 20px;display: flex" >
          </div>
          <div class="tabBarArea tit">
                  <div class="tabBarBtn" :class="[tabName=='Tab1'?'tabBarBtnActive':'']"
                      @click="tabName='Tab1'">属性标准</div>
                  <div class="tabBarBtn" :class="[tabName=='Tab2'?'tabBarBtnActive':'']"
                      @click="tabName='Tab2'">关联性标准</div>
              </div>
        </div>
        <div class="r_bot" v-if="tabName==='Tab1'">
          <div class="tit cell">
            <div class="cell" style="float: right;width: 120%;text-align: right;margin-bottom: 10px;">
              <el-form style="margin-right:30px;display: inline;" ref="selForm" :model="selForm" label-width="80px" label="">
                  <el-input style="width: 300px;height: 28px !important" size="mini" v-model="selForm.cluName" placeholder="请输入属性名"></el-input>
              </el-form>
              <el-button plain size="mini" style="padding:5px 15px;width:80px" :disabled="ifSel" type="info" @click="getData()" icon="el-icon-search">搜索</el-button>
               <el-button :disabled="ifSel" plain size="mini" style="padding:5px 15px;width:80px" type="primary" @click="addNewRules()" icon="el-icon-plus">新增</el-button>
              <el-button plain size="mini" style="padding:5px 15px;width:80px"  :disabled="ifSel" @click="getData" icon="el-icon-refresh">刷新</el-button>
            </div>
          </div>
          <div class="table_space">
            <el-table
              ref="tableData"
              :data="tableData"
              stripe
              height="370px"
              v-loading="loading"
              tooltip-effect="dark"
              :header-cell-style="{background:'#F5F5F5'}">
              <el-table-column
                label="序号"
                width="100"
                align="center"
                :formatter="snFormatter">
              </el-table-column>
              <el-table-column
                prop="columnName"
                align="center"
                label="属性名称"
                width="130">
              </el-table-column>
              <el-table-column
                prop="columnNameCn"
                align="center"
                label="属性中文名称"
                width="130">
              </el-table-column>
              <el-table-column
                prop="columnType"
                align="center"
                label="属性类型"
                width="130">
              </el-table-column>
              <el-table-column
                prop="columnLength"
                align="center"
                label="属性长度"
                width="130">
              </el-table-column>
              <el-table-column
              label="校验方式"
              align="center"
              width="200">
                <template slot-scope="scope">
                  <el-select filterable   v-model="scope.row.ruleId" v-show="scope.$index == cellIndex" @change="getDesciption(scope.row.ruleId,scope)" placeholder="请选择" style="width:130px">
                    <el-option v-for="item in rulesList"
                    :label="item.ruleName"
                    :value="item.id"
                    :key="item.id"></el-option>
                  </el-select>
                  <p v-show="scope.$index != cellIndex">{{scope.row.ruleName}}</p>
                </template>
              </el-table-column>
              <el-table-column
                prop="ruleDescription"
                align="center"
                label="规则描述"
                width="140"
                >
                <template slot-scope="scope">
                  <el-input v-show="scope.$index == cellIndex" v-model="addRulesForm.ruleDescription" ></el-input>
                  <el-tooltip v-show="scope.$index != cellIndex" class="item" effect="light" placement="top">
                    <div v-html="scope.row.ruleDescription" slot="content" style="max-width:300px"></div>
                    <div class="oneLine">{{scope.row.ruleDescription}}</div>
                  </el-tooltip>
                  </template>
              </el-table-column>
              <el-table-column
              label="比较方式"
              align="center"
              width="200">
                <template slot-scope="scope">
                  <el-select :disabled="ifSymbol" v-show="scope.$index == cellIndex" style="width:150px" v-model="scope.row.symbol" placeholder="请选择" filterable @change="upchange()">
                    <el-option v-for="item in signList"
                    :label="item.selectName"
                    :value="item.selectName"
                    :key="item.selectName"></el-option>
                  </el-select>
                  <p v-show="scope.$index != cellIndex">{{scope.row.symbol}}</p>
                </template>
              </el-table-column>
              <el-table-column
                prop="selectType"
                align="center"
                label="比较内容类别"
                width="220">
                <template slot-scope="scope">
                  <el-row :gutter="20">
                  <el-col :span="18">
                    <el-select filterable   v-model="scope.row.selectType" v-show="scope.$index == cellIndex" placeholder="请选择" style="width:150px;margin-left:10px">
                  <el-option v-for="item in saveConditions"
                    :label="item.query_name"
                    :value="item.id"
                    :key="item.id">
                  </el-option>
                  </el-select>
                </el-col>
                <p v-show="scope.$index != cellIndex" v-if="scope.row.selectType==='0'">自定义sql</p>
                  <p v-show="scope.$index != cellIndex" v-if="scope.row.selectType==='2'">表属性选择</p>
                  <p v-show="scope.$index != cellIndex" v-if="scope.row.selectType==='1'">填报</p>
                <el-col :span="6">
                  <el-button circle plain  v-show="scope.$index == cellIndex" style="width:30px ;height:30px ;padding:5px 5px " class="el-icon-edit-outline" @click="editLevel(scope)" :type="savelevelForm.ifSave ? 'success' : ''"  autofocus></el-button></el-col>
                </el-row>
                  </template>
              </el-table-column>
              <el-table-column
                prop="alarmLevel"
                align="center"
                label="告警级别"
                width="180">
                <template slot-scope="scope">
                  <div v-for="item in alarmList" v-if="scope.$index !== cellIndex" v-hasPermi="['system:dict:list']">
                    <p v-if="scope.row.alarmLevel===item.dictValue">{{item.dictLabel}}</p>
                  </div>
                  <el-select filterable  v-model="scope.row.alarmLevel" v-show="scope.$index == cellIndex" placeholder="请选择" style="width:100px" v-hasPermi="['system:dict:list']">
                    <el-option v-for="item in alarmList"
                    :label="item.dictLabel"
                    :value="item.dictValue"
                    :key="item.dictValue"></el-option>
                  </el-select>
                  </template>
              </el-table-column>
              <el-table-column
                prop="modificationSuggestions"
                align="center"
                label="治理建议"
                width="260">
                <template slot-scope="scope">
                  <el-input
                      v-model="scope.row.modificationSuggestions"
                      style="width:230px;"
                      v-show="scope.$index == cellIndex">
                  </el-input>
                  <el-tooltip v-show="scope.$index != cellIndex" class="item" effect="light" placement="top">
                    <div v-html="scope.row.modificationSuggestions" slot="content" style="max-width:300px"></div>
                    <div class="oneLine">{{scope.row.modificationSuggestions}}</div>
                  </el-tooltip>
                  </template>
              </el-table-column>
              <el-table-column
                prop="state"
                align="center"
                label="状态"
                width="60"
                fixed="right">
                <template slot-scope="scope">
                  <el-tooltip :content="scope.row.state==='1'?'禁用':'启用'" placement="top">
                  <el-switch
                    v-show="scope.$index == cellIndex"
                    v-model="scope.row.state"
                    active-color="#13ce66"
                    inactive-color="#ff4949"
                    active-value="0"
                    inactive-value="1"
                    >
                  </el-switch>
                </el-tooltip>
                <el-tooltip :content="scope.row.state==='1'?'禁用':'启用'" placement="top">
                  <el-switch
                  v-show="scope.$index != cellIndex"
                    v-model="scope.row.state"
                    active-color="#13ce66"
                    inactive-color="#ff4949"
                    active-value="0"
                    inactive-value="1"
                    @change="ifRun(scope)">
                  </el-switch>
                </el-tooltip>
                  </template>
              </el-table-column>
              <el-table-column
                label="操作"
                width="210"
                align="center"
                fixed="right"
                v-if="versionShow">
                  <template slot-scope="scope">
                    <el-button
                      @click="editTable(scope)"
                      size="mini"
                      type="text"
                      icon="el-icon-edit"
                      v-if="scope.$index != cellIndex"
                      >编辑</el-button>
                      <el-button
                      @click="saveTable(scope)"
                      size="mini"
                      type="text"
                      v-else
                      icon="el-icon-circle-check"
                      >保存</el-button>
                      <el-button type="text" size="mini" icon="el-icon-delete" v-if="scope.$index != cellIndex" @click="deleteRules(scope)">删除</el-button>
                  <el-button
                      @click="cancel()"
                      size="mini"
                      type="text"
                      v-else
                      icon="el-icon-circle-close"
                      >取消</el-button>
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
        <div class="r_bot" v-if="tabName==='Tab2'">
          <div class="tit cell">
            <div class="cell" style="float: right;width: 120%;text-align: right;margin-bottom: 10px;">
              <el-form style="margin-right:30px;display: inline;" ref="selForm" :model="selForm" label-width="80px" label="">
                  <el-input style="width: 300px;height: 30px" v-model="selForm.cluName" placeholder="请输入属性名"></el-input>
              </el-form>
              <el-button plain size="mini" style="padding:5px 15px;width:80px" :disabled="ifSel" type="info" @click="getRulesData()" icon="el-icon-search">搜索</el-button>
              <el-button plain size="mini" :disabled="ifSel" style="padding:5px 15px;width:80px" type="primary" @click="addTableClu()" icon="el-icon-plus" v-if="versionShow">新增</el-button>
              <el-button plain size="mini" style="padding:5px 15px;width:80px"  :disabled="ifSel" @click="getRulesData" icon="el-icon-refresh">刷新</el-button>
            </div>
          </div>
          <div >
            <el-table
              :data="rulestableData"
              ref="rulestableData"
              stripe
              height="400px"
              v-loading="loading"
              tooltip-effect="dark"
              :header-cell-style="{background:'#F5F5F5'}">
              <el-table-column
                label="序号"
                width="100"
                align="center"
                :formatter="snFormatter">
              </el-table-column>
              <el-table-column
              label="关联校验规则类型"
              prop="verificationRuleType"
              align="center"
              width="150">
                <template slot-scope="scope">
                  <el-select v-show="scope.$index == cellIndex1" v-model="scope.row.verificationRuleType" style="width:120px" @change="ifChange(scope)">
                    <el-option label="关联规则" key="0" value="0"></el-option>
                    <el-option label="自定义sql校验" key="1" value="1"></el-option>
                  </el-select>
                  <p v-show="scope.$index !== cellIndex1" v-if="scope.row.verificationRuleType==='0'">关联规则</p>
                  <p v-show="scope.$index !== cellIndex1" v-if="scope.row.verificationRuleType==='1'">自定义sql校验</p>
                </template>
              </el-table-column>
              <el-table-column
              label="规则名称"
              prop="ruleName"
              align="center"
              width="190">
                <template slot-scope="scope">
                  <el-select v-show="scope.$index == cellIndex1" v-model="scope.row.verificationRuleId" style="width:120px" :disabled="ifRuleName" @change="getNewDes(scope.row.verificationRuleId)">
                    <el-option v-for="item in ruleNameList"
                    :label="item.ruleName"
                    :key="item.id"
                    :value="item.id"></el-option>
                  </el-select>
                  <div v-for="item in ruleNameList">
                  <el-tooltip v-show="scope.$index != cellIndex1" class="item" effect="light" placement="top" v-if="scope.row.verificationRuleId===item.id">
                    <div v-html="item.ruleName" slot="content" style="max-width:300px"></div>
                    <div class="oneLine">{{item.ruleName}}</div>
                  </el-tooltip>
                </div>
                </template>
              </el-table-column>
              <el-table-column
                prop="ruleDescription"
                align="center"
                label="规则描述"
                width="140"
                >
                <template slot-scope="scope">
                  <el-input v-show="scope.$index == cellIndex1" v-model="relationForm.ruleDescription"></el-input>
                  <el-tooltip v-show="scope.$index != cellIndex1" class="item" effect="light" placement="top">
                    <div v-html="scope.row.ruleDescription" slot="content" style="max-width:300px"></div>
                    <div class="oneLine">{{scope.row.ruleDescription}}</div>
                  </el-tooltip>
                  </template>
              </el-table-column>
              <el-table-column
                prop="useDefinedVerSql"
                align="center"
                label="校验sql"
                width="360">
                <template slot-scope="scope">
                  <el-button circle plain  style="width:35px ;height:35px ;" v-show="scope.$index == cellIndex1" :disabled="ifSql" class="el-icon-edit-outline" @click="editSql(scope)" :type="saveRelationForm.ifSave ? 'success' : ''"  autofocus></el-button>
                  <el-tooltip v-show="scope.$index != cellIndex1" class="item" effect="light" placement="top">
                    <div v-html="scope.row.useDefinedVerSql" slot="content" style="max-width:300px"></div>
                    <div class="oneLine">{{scope.row.useDefinedVerSql}}</div>
                  </el-tooltip>
                  </template>
              </el-table-column>
              <el-table-column
                prop="alarmId"
                align="center"
                label="告警级别"
                width="140">
                <template slot-scope="scope">
                  <el-select filterable   v-model="scope.row.alarmId" v-show="scope.$index == cellIndex1" @change="getDesciption(scope.row.ruleId)" v-hasPermi="['system:dict:list']" placeholder="请选择" style="width:130px">
                    <el-option v-for="item in alarmList"
                    :label="item.dictLabel"
                    :value="item.dictValue"
                    :key="item.dictValue"></el-option>
                  </el-select>
                  <div v-for="item in alarmList" v-if="scope.$index !== cellIndex1" v-hasPermi="['system:dict:list']">
                    <p v-if="scope.row.alarmId===item.dictValue">{{item.dictLabel}}</p>
                  </div>
                  </template>
              </el-table-column>
              <el-table-column
                prop="modificationSuggestions"
                align="center"
                label="治理建议"
                width="240"
                >
                <template slot-scope="scope">
                  <el-input
                      v-model="scope.row.modificationSuggestions"
                      style="width:230px;"
                      v-show="scope.$index == cellIndex1">
                  </el-input>
                  <el-tooltip v-show="scope.$index != cellIndex1" class="item" effect="light" placement="top">
                    <div v-html="scope.row.modificationSuggestions" slot="content" style="max-width:300px"></div>
                    <div class="oneLine">{{scope.row.modificationSuggestions}}</div>
                  </el-tooltip>
                  </template>
              </el-table-column>

              <el-table-column
                label="操作"
                width="210"
                align="center"
                fixed="right"
                v-if="versionShow">
                  <template slot-scope="scope">
                    <el-button
                      @click="editTable1(scope)"
                      size="mini"
                      type="text"
                      icon="el-icon-edit"
                      v-if="scope.$index != cellIndex1"
                      >编辑</el-button>
                      <el-button
                      @click="saveTable1(scope)"
                      size="mini"
                      type="text"
                      v-else
                      icon="el-icon-circle-check"
                      >保存</el-button>
                      <el-button type="text" size="mini" @click="deleteRule(scope)" icon="el-icon-delete" v-if="scope.$index != cellIndex1" >删除</el-button>
                    <el-button
                      @click="cancel1()"
                      size="mini"
                      type="text"
                      v-else
                      icon="el-icon-circle-close"
                      >取消</el-button>
                </template>
              </el-table-column>
            </el-table>
            <pagination
              v-show="total1>0"
              :total="total1"
              :page.sync="pageIndex1"
              :limit.sync="pageSize1"
              @pagination="getrulesData"
            />
          </div>
        </div>
      </div>
    </div>
    <div>
    </div>
    <el-dialog
        class="relation"
        title="新增关联性标准"
        :close-on-click-modal="false"
        :visible.sync="addRelationDrawer"
        style="margin-top: 7vh !important;"
        width="420px"
      >
        <el-form label-width="auto" style="text-align: center" >
          <el-form-item label="关联校验规则类型"  >
            <el-select v-model="relationForm.verificationRuleType" style="width:200px" @change="ifChange(relationForm)" clearable>
                    <el-option label="关联规则" key="0" value="0"></el-option>
                    <el-option label="自定义sql校验" key="1" value="1"></el-option>
                  </el-select>
          </el-form-item>
          <el-form-item label="规则名称"  >
            <el-select  v-model="relationForm.verificationRuleId" style="width:200px" :disabled="ifRuleName" clearable @change="getNewDes(relationForm.verificationRuleId)">
                    <el-option v-for="item in ruleNameList"
                    :label="item.ruleName"
                    :key="item.id"
                    :value="item.id"></el-option>
                  </el-select>
          </el-form-item>
          <el-form-item label="规则描述"  >
            <el-input  v-model="relationForm.ruleDescription" :disabled="ifRuleName" style="width:200px"></el-input>
          </el-form-item>
          <el-form-item label="校验sql"  >
            <el-button circle plain  style="width:35px ;height:35px ;"  class="el-icon-edit-outline" @click="editSql(relationForm)" :disabled="ifSql"  :type="saveRelationForm.ifSave"  autofocus></el-button>
          </el-form-item>
          <el-form-item label="告警级别"  >
            <el-select filterable   v-model="relationForm.alarmId"  @change="getDesciption(relationForm.ruleId)"  placeholder="请选择" style="width:200px">
                    <el-option v-for="item in alarmList"
                    :label="item.dictLabel"
                    :value="item.dictValue"
                    :key="item.dictValue"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="治理建议"  >
            <el-input
                      v-model="relationForm.modificationSuggestions"
                      style="width:200px;">
                  </el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button  type="primary" @click="saveRelationRules()" >确 定</el-button>
          <el-button @click="addRelationDrawer = false">取 消</el-button>
        </div>
      </el-dialog>
      <el-dialog
        title="新增属性标准"
        :close-on-click-modal="false"
        :visible.sync="nextNewRulesDrawer"
        style="margin-top: 7vh !important;"
        width="450px"
        class="addBox"
      >
        <el-form label-width="80px" style="text-align: center" >
          <el-form-item label="选择表属性"  >
            <el-select v-model="newRulesId" style="width:200px" filterable>
              <el-option  v-for="item in selectData"
              :label="item.columnNameCn"
              :key="item.id"
              :value="item.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="校验方式"  >
            <el-select filterable   v-model="addRulesForm.ruleId" @change="getDesciption(addRulesForm.ruleId)" placeholder="请选择" style="width:200px">
                    <el-option v-for="item in rulesList"
                    :label="item.ruleName"
                    :value="item.id"
                    :key="item.id"></el-option>
                  </el-select>
          </el-form-item>
          <el-form-item label="规则描述"  >
                    <el-input v-model="addRulesForm.ruleDescription" style="width:200px"></el-input>
          </el-form-item>
          <el-form-item label="比较方式"  >
            <el-select :disabled="ifSymbol"  style="width:200px" v-model="addRulesForm.symbol" placeholder="请选择" filterable @change="upchange()">
                    <el-option v-for="item in signList"
                    :label="item.selectName"
                    :value="item.selectName"
                    :key="item.selectName"></el-option>
                  </el-select>
          </el-form-item>
          <el-form-item label="比较内容类别"  >
            <el-select filterable   v-model="addRulesForm.selectType" placeholder="请选择" style="width:200px;margin-left:0px" @change="$forceUpdate()" clearable>
                  <el-option v-for="item in saveConditions"
                    :label="item.query_name"
                    :value="item.id"
                    :key="item.id">
                  </el-option>
                  </el-select>
                  <el-button circle plain  style="width:30px ;height:30px ;padding:5px 5px;position:absolute;right:-25px;top:4px" class="el-icon-edit-outline" @click="editLevel1(addRulesForm)" :type="savelevelForm.ifSave ? 'success' : ''"  autofocus></el-button>
          </el-form-item>
          <el-form-item label="告警级别"  >
            <el-select filterable  v-model="addRulesForm.alarmLevel" placeholder="请选择" style="width:200px" @change="$forceUpdate()" clearable>
                    <el-option v-for="item in alarmList"
                    :label="item.dictLabel"
                    :value="item.dictValue"
                    :key="item.dictValue"></el-option>
                  </el-select>
          </el-form-item>
          <el-form-item label="治理建议"  >
            <el-input
                      type="textarea"
                      v-model="addRulesForm.modificationSuggestions"
                      style="width:200px;"
                      >
                  </el-input>
          </el-form-item>
          <el-form-item label="状态"  >
            <el-tooltip :content="addRulesForm.state==='1'?'禁用':'启用'" placement="top">
                  <el-switch
                    v-model="addRulesForm.state"
                    active-color="#13ce66"
                    inactive-color="#ff4949"
                    active-value="0"
                    inactive-value="1"
                    >
                  </el-switch>
                </el-tooltip>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button  type="primary" @click="saveNewRules()" >确 定</el-button>
          <el-button @click="nextNewRulesDrawer = false">取 消</el-button>
        </div>
      </el-dialog>
      
    <div class="levelBox">
      <el-dialog
        title="sql"
        :close-on-click-modal="false"
        :visible.sync="sqlDrawer"
        style="margin-top: 7vh !important;"
      >
      <div slot="title">
        <span>sql</span>
        <span style="color:#BEBEBE;margin-left:20px">select  code as  code ,name as  name from sg_dic_tcstationlevel   备注id 别名为code ，名称  别名为name</span>
      </div>
      
        <el-form label-width="80px" style="text-align: center" >
          <el-form-item label="SQL" prop="sql" >
            <el-input v-model="levelForm.customizedSql" type="textarea" :rows="20" style="width:800px;"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button  type="primary" @click="saveLevleBox()" >确 定</el-button>
          <el-button @click="cancelLevelBox()">取 消</el-button>
        </div>
      </el-dialog>
      <el-dialog
        title="sql"
        :close-on-click-modal="false"
        :visible.sync="sqlEditDrawer"
        style="margin-top: 7vh !important;"
      >
        <el-form label-width="80px" style="text-align: center" >
          <el-form-item label="SQL" prop="sql" >
            <el-input v-model="relationForm.useDefinedVerSql" type="textarea" ></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button  type="primary" @click="saveRelation()" >确 定</el-button>
          <el-button @click="cancelLevelBox()">取 消</el-button>
        </div>
      </el-dialog>
    </div>
    <div class="levelBox1">
      <el-dialog
        title="填报"
        :close-on-click-modal="false"
        :visible.sync="chooseDrawer"
        style="margin-top: 19vh !important;"
      >
        <el-form :model="uploadTableForm" :rules="rules" ref="uploadTableForm" label-width="80px" style="text-align: center">
          <el-form-item label="填报" >
            <el-input type="textarea"  placeholder="多个参数请用逗号隔开" v-model="levelForm.relParam" style="width:750px"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button  type="primary" @click="saveLevleBoxsign()" >确 定</el-button>
          <el-button @click="cancelLevelBox()">取 消</el-button>
        </div>
      </el-dialog>
    </div>
    <div class="levelBox2" >
      <el-dialog
        title="表属性选择"
        :close-on-click-modal="false"
        :visible.sync="writeDrawer"
        width="780px"
      >
      <el-form>
            <div >
              <el-form-item style="flex: 6" label="关联元模型">
                <el-select v-model="levelForm.relMetaModeId" @change="getTableClu(levelForm.relMetaModeId)" filterable placeholder="请选择" style="width:200px">
                  <el-option v-for="item in modelIdList"
                  :label="item.tableNameCn"
                  :key="item.id"
                  :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </div>
            <div style="display: flex;width: 70%">
              <el-form-item style="flex: 6" label="关联编码属性" prop="columnName">
               <el-select  placeholder="请选择"  v-model="levelForm.relIdMetaModelColumnId" @change="$forceUpdate()" style="width:200px">
                  <el-option v-for="item in metatableData"
                  :label="item.columnNameCn"
                  :key="item.id"
                  :value="item.id">
                </el-option>
                </el-select>
              </el-form-item>
              <el-form-item style="flex: 6" label="关联名称属性" prop="columnName">
               <el-select    v-model="levelForm.relNameMetaModelColumnId" @change="changeUp()" style="width:200px">
                <el-option v-for="item in metatableData"
                  :label="item.columnNameCn"
                  :key="item.id"
                  :value="item.id">
                </el-option>
                </el-select>
              </el-form-item>
            </div>
            <el-input style="width: 250px;height: 20px;margin-left:360px !important" v-model="searchmetaName" placeholder="请输入属性名称"></el-input>
            <el-button :disabled="ifSel" size="mini" plain style="padding:5px 15px;width:80px;margin-left:20px" type="info" @click="getmetaData(levelForm.relMetaModeId)" icon="el-icon-search" >搜索</el-button>
            <el-table
              ref="multipleTable"
              height="300px"
              :data="metatableData"
              style="margin-top:20px;margin-left:2%;width: 96%"
              tooltip-effect="dark"
              highlight-current-row
              :header-cell-style="{background:'#F5F5F5'}">
              <el-table-column
                label="序号"
                width="80"
                align="center"
                :formatter="snFormatter1">
              </el-table-column>
              <el-table-column
                align="center"
                label="属性名称"
                width="120"
                prop="columnName">
              </el-table-column>
              <el-table-column
                align="center"
                label="中文名称"
                width="120"
                prop="columnNameCn">
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
                prop="createDate"
                align="center"
                label="创建时间"
                width="150">
              </el-table-column>
              </el-table>
          </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button  type="primary" @click="saveRelMeta()" >确 定</el-button>
          <el-button @click="cancelLevelBox()">取 消</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>
<script>
import qs from 'qs'
import request from '@/utils/request'

export default {
name: 'ywysj',
props: {
  modelId: String,
  modelVersion: {  // 【修改1】给props设置默认值
    type: String,
    default: 'edit',  // 默认值设为edit
    required: true
  },
  versionShow: Boolean,
  required: true
},
data () {
  return {
    levelOp:'',
    addRelationDrawer: false,
    cellIndex1: null,
    cellIndex: null,
    loading: true,
    tabName: 'Tab1',
    ifSel: true,
    glRelationTableName: '',
    glTableCluName: '',
    uploadTableDrawer: false,
    addTableDrawer: false,
    addTableCluDrawer: false,
    glCluDrawer: false,
    mainTableCluVisible: false,
    sqlEditDrawer:false,
    // 分页
    pageSize: 10,
    pageIndex: 1,
    total: 0,
    addNewRulesDrawer: false,
    nextNewRulesDrawer: false,
    // 分页
    pageSize1: 10,
    pageIndex1: 1,
    total1: 0,
    // 基础规则下拉框
    rulesList:[],
    ruleNameList:[],
    signList: [],
    modelIdList:[],
    searchmetaName:'',
    metaIdList: [{id:'',metaName:''}],
    // 选择类型弹框表单
    levelForm: {
      relParam: '',
      relNameMetaModelColumnId: '',
      relIdMetaModelColumnId:'',
      relMetaModeId:'',
      param: '',
      symbol: '',
      customizedSql: '',
      relMetaModeId: '',
      relIdMetaModelColumnId: '',
      symbolCategory: ''
    },
    index: '',
    newRulesId: '',
    saveRelationForm: {
      useDefinedVerSql: '',
      ifSave:false
    },
    oldlevelForm: {},
    savelevelForm: {
      relParam: '',
      relIdMetaModelColumnId: '',
      relNameMetaModelColumnId: '',
      relMetaModeId: '',
      symbolCategory: '',
      customizedSql: '',
      relMetaModeId: '',
      relIdMetaModelColumnId: '',
      symbol: '',
      param: '',
      ifSave: false
    },
    DescriptionList: [{id:'',ruleDescription:'',modificationSuggestions:''}],
    DescriptionList1:[{id:'',ruleDescription:''}],
    selForm: {
      cluName: ''
    },
    seeClu: true,
    seeAlias: false,
    tableData: [],
    selectData:[],
    metatableData: [],
    tableDataClu: [],
    glTableCluFormD: {},
    signcategoryList:[],
    alarmList: [],
    ifSql: true,
    ifRuleName: true,
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
    uploadTableForm: {},
    saveConditions: [
      {id:'0', query_name: '自定义sql'},
      {id:'1', query_name: '填报'},
      {id:'2', query_name: '表属性选择'}
    ],
    // 导入表格属性
    showImport: false,
    addFileName: '',
    // 导入后的数据列表
    showImportData: false,
    importData: [],
    value: '100',
    glCluObj: {},
    addRulesForm: {
      relParam: '',
      relNameMetaModelColumnId: '',
      relIdMetaModelColumnId:'',
      relMetaModeId:'',
      param: '',
      symbol: '',
      customizedSql: '',
      relMetaModeId: '',
      relIdMetaModelColumnId: '',
      symbolCategory: '',
      // 【修改2】新增属性默认值
      alarmLevel: '',
      ruleId: '',
      ruleDescription: '',
      selectType: '',
      modificationSuggestions: '',
      state: '0'  // 默认启用
    },
    filterText: '',
    editDrawer: false,
    data: [{
      id: '',
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
    sqlRules: {
      sql: [
      { required: true, message: '.', trigger: 'blur' }
      ]
    },
    sqlDrawer: false,
    ifSymbol: false,
    chooseDrawer: false,
    writeDrawer: false,
    symboNameId: '',
    rulestableData: [],
    relationForm: {
      verificationRuleType: '',
      useDefinedVerSql: '',
      // 【修改3】新增关联表单默认值
      verificationRuleId: '',
      ruleDescription: '',
      alarmId: '',
      modificationSuggestions: ''
    }
  }
},
watch: {
},
methods: {
  saveRule({row}) {
    request.get('/site/b/'+row.id).then(res=>{
      this.$message({
            message: res.msg,
            type: 'success',
            center: true
          })
    })
  },
  addNewRules() {
    this.newRulesId = ''
    this.DescriptionList.ruleDescription = ''
    this.getSelectData()
    this.getRulesList()
    this.getalarmList()
    this.addRulesForm = {
      relParam: '',
      relNameMetaModelColumnId: '',
      relIdMetaModelColumnId:'',
      relMetaModeId:'',
      param: '',
      symbol: '',
      customizedSql: '',
      relMetaModeId: '',
      relIdMetaModelColumnId: '',
      symbolCategory: '',
      alarmLevel: '',
      ruleId: '',
      ruleDescription: '',
      selectType: '',
      modificationSuggestions: '',
      state: '0'  // 默认启用
    }
    this.nextNewRulesDrawer = true
  },
  ifChange(row) {
    if(row.verificationRuleType==='0') {
      this.ifSql = true
      row.useDefinedVerSql = ''
    } else {
      this.ifSql = false
    }
    if(row.verificationRuleType==='1') {
      this.relationForm.ruleDescription = ''
      row.verificationRuleId = ''
      this.DescriptionList1.ruleDescription = ''
      this.ifRuleName = true
    } else {
      this.ifRuleName = false
    }

  },
  // 新增关联性标准
  addTableClu() {
    this.addRelationDrawer = true
    this.relationForm = {
      verificationRuleType: '',
      useDefinedVerSql: '',
      verificationRuleId: '',
      ruleDescription: '',
      alarmId: '',
      modificationSuggestions: ''
    }
  },
  // 存储关联性标准新增
  saveRelationRules() {
    // 【修改4】确保modelVersion有值，优先用props的值，没有则用默认值edit
    const currentModelVersion = this.modelVersion || 'edit'
    
    var tMetaModelTech = {
          verificationRuleType: this.relationForm.verificationRuleType,
          ruleDescription: this.relationForm.ruleDescription,
          metaModeId: this.modelId,
          modelVersion: currentModelVersion,  // 使用处理后的默认值
          verificationRuleId:this.relationForm.verificationRuleId,
          useDefinedVerSql: this.saveRelationForm.useDefinedVerSql,
          alarmLevel: this.relationForm.alarmId,
          modificationSuggestions: this.relationForm.modificationSuggestions
        }
        request({
          headers: {
            'Content-Type': 'application/json'
          },
          method: 'post',
          url: '/verification',
          data: tMetaModelTech
        }).then((res) => {
          // 请求成功处理。
          this.addRelationDrawer = false
          this.getRulesData()
          this.getruleNameList()
          this.getalarmList()
          if (res.code===200||res.code==='200'){
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
        })
  },
  upchange() {
    this.$forceUpdate()
  },
  getRulesData () {
    this.rulestableData = []
    // 【修改5】请求参数中确保modelVersion有默认值
    const currentModelVersion = this.modelVersion || 'edit'
    
    request.get('/verification/list?metaModeId=' + this.modelId  + '&modelVersion=' + currentModelVersion
    + '&columnName=' + this.selForm.cluName + '&pageNum=' + this.pageIndex + '&pageSize=' + this.pageSize, qs.stringify({
    })).then((res) => {
      // 请求成功处理。
      this.rulestableData = res.rows
      this.loading = false
      this.total1 = parseInt(res.total)

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
  //删除属性规则
  deleteRules({row}) {
    this.$confirm("是否删除此数据？", "提示", {
        confirmButtonText: "确认",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          request({
      method: 'delete',
      url: '/verrules/' + row.id}).then((res) => {
      // 请求成功处理。
      this.getData()
      if (res.code === 200||res.code==='200'){
      this.$message({
        message: res.msg,
        type: 'success',
        center: true
      })}
    }).catch(e => {
      this.$message({
        message: e,
        type: 'error',
        center: true,
          duration:0,
          showClose:true
      })
    })
        })
        .catch(() => {});

  },
  //编辑sql
  editSql({row}) {
   this.sqlEditDrawer = true
   if(this.saveRelationForm.useDefinedVerSql===''||this.saveRelationForm.useDefinedVerSql===undefined) {
    this.relationForm.useDefinedVerSql = row.useDefinedVerSql
   } else {
    this.relationForm.useDefinedVerSql = this.saveRelationForm.useDefinedVerSql
   }
  }, 
  // 新增属性规则
  saveNewRules() {
    // 【修改6】新增属性规则时添加modelVersion
    const currentModelVersion = this.modelVersion || 'edit'
    
    var tQualityverrules = {
          alarmLevel: this.addRulesForm.alarmLevel,
          metaModeId: this.modelId,
          modelVersion: currentModelVersion,  // 新增这行，传递modelVersion
          ruleId:this.addRulesForm.ruleId,
          ruleDescription: this.addRulesForm.ruleDescription,
          symbol: this.addRulesForm.symbol,
          selectType:this.addRulesForm.selectType,
          modificationSuggestions: this.addRulesForm.modificationSuggestions,
          state: this.addRulesForm.state,
          metaModelColumnId: this.newRulesId,
        }
        
        if(this.addRulesForm.selectType==='0'){
          tQualityverrules.customizedSql = this.savelevelForm.customizedSql
        }
        
        if(this.addRulesForm.selectType==='1') {
          tQualityverrules.param = this.savelevelForm.param
        }
        if(this.addRulesForm.selectType==='2') {
          tQualityverrules.relMetaModeId = this.savelevelForm.relMetaModeId
          tQualityverrules.relIdMetaModelColumnId = this.savelevelForm.relIdMetaModelColumnId
          tQualityverrules.relNameMetaModelColumnId = this.savelevelForm.relNameMetaModelColumnId

        }
        request({
          headers: {
            'Content-Type': 'application/json'
          },
          method: 'post',
          url: '/verrules',
          data: tQualityverrules
        }).then((res) => {
          // 请求成功处理。
          this.getData()
          this.getRulesData()
          this.getruleNameList()
          this.getalarmList()
          this.addNewRulesDrawer = false
            this.nextNewRulesDrawer = false
          if (res.code===200||res.code==='200'){
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
        })
  },
  // 删除规则
  deleteRule({row}) {
    this.$confirm("是否删除此规则？", "提示", {
        confirmButtonText: "确认",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          request({
      method: 'delete',
      url: '/verification/' + row.id
    }).then((res) => {
      // 请求成功处理。
      this.getRulesData()
      if (res.code === 200||res.code==='200'){
      this.$message({
        message: res.msg,
        type: 'success',
        center: true
      })}
    }).catch(e => {
      this.getRulesData()
      // 请求失败处理。
      this.$message({
        message: e,
        type: 'error',
        center: true,
          duration: 0,
          showClose: true
      })
    })
        })
        .catch(() => {});
    
  },
  // 是否启用
  ifRun({row}) {
    // 【修改7】修改状态时添加modelVersion
    const currentModelVersion = this.modelVersion || 'edit';
    
  var params = {
      id: row.id,
      metaModeId: this.modelId,
      modelVersion: currentModelVersion,  // 新增这行
      ruleId: row.ruleId,
      ruleDescription: this.DescriptionList.ruleDescription,
      alarmLevel: row.alarmLevel,
      modificationSuggestions: row.modificationSuggestions,
      state: row.state,
      selectType: row.selectType,
      symbol: row.symbol
    }
    request({
      headers: {
        'Content-Type': 'application/json'
      },
      method: 'put',
      url: '/verrules',
      data: params
    }).then((res) => {
      // 请求成功处理。
      this.getData()
      if(res.code===200||res.code==='200'){
      this.$message({
        message: '修改成功',
        type: 'success',
        center: true
      })}
    }).catch(e => {
      // 请求失败处理。
      this.getData()
      this.$message({
        message: e,
        type: 'error',
        center: true,
          duration: 0,
          showClose: true
      })
    })
  },
  // 根据元模型获取技术表格
  getTableClu(id) {
    this.levelForm.relIdMetaModelColumnId = ''
    this.levelForm.relNameMetaModelColumnId = ''
    this.getmetaData(id)
    this.$forceUpdate()
  },
  getmetaData(id) {
    this.metatableData = []
    if(id!==undefined){
    request.get('/tech/listDownDrop?modelId=' + id + '&columnName=' + this.searchmetaName, qs.stringify({
    })).then((res) => {
      // 请求成功处理。
      this.metatableData = res.data
    }).catch(e => {
      // 请求失败处理。
      this.$message({
        message: e,
        type: 'error',
        center: true,
          duration: 0,
          showClose: true
      })
    })}
  },
   // 取消操作
   cancel () {
    this.getData()
    this.cellIndex = null

  },
      // 取消操作
   cancel1 () {
    this.cellIndex1 = null
    this.getRulesData()
  },
  //获取元模型下拉选项
  getmodelIdList() {

    request.get('/model/listDownDrop?isDelete=' + '0', qs.stringify({
    })).then((res) => {
      // 请求成功处理。
      this.modelIdList = res.data
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
  // 根据校验方式获取比较方式
  getSign(id) {

    request.get('/verrules/symbolList?englishName=' + id, qs.stringify({
    })).then((res) => {
      // 请求成功处理。
      this.signList = res.rows
      if(res.rows.length===0){
        this.ifSymbol = true
      } else {
        this.ifSymbol = false
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
  // 校验方式下拉框
  getsigncategoryList() {
    request.get('/verrules/symbolCategoryList', qs.stringify({
    })).then((res) => {
      // 请求成功处理。
      this.signcategoryList = res.rows
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
  saveRelation() {
    this.saveRelationForm.useDefinedVerSql =  this.relationForm.useDefinedVerSql
    this.sqlEditDrawer = false
  },
  // 保存选择类型弹框
  saveLevleBox () {
      this.savelevelForm.customizedSql = this.levelForm.customizedSql
      this.savelevelForm.ifSave = true
      this.sqlDrawer= false
      this.sqlEditDrawer = false
  },
  // 取消
  cancelLevelBox() {
    this.levelForm = this.oldlevelForm
    this.$set(this.levelForm, this.oldlevelForm)
    this.sqlEditDrawer = false
    this.sqlDrawer= false
    this.chooseDrawer = false
    this.writeDrawer = false
  },
  saveLevleBoxsign () {
    this.savelevelForm.param = this.levelForm.param
    this.chooseDrawer = false

  },
  saveRelMeta() {
    this.savelevelForm.relMetaModeId = this.levelForm.relMetaModeId
    this.savelevelForm.relIdMetaModelColumnId = this.levelForm.relIdMetaModelColumnId
    this.savelevelForm.relNameMetaModelColumnId = this.levelForm.relNameMetaModelColumnId
    this.writeDrawer = false
  },
  //获取规则名称
  getruleNameList() {
    request.get('/assrules/list', qs.stringify({
    })).then((res) => {
      // 请求成功处理。
      this.ruleNameList = res.rows
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
  getNewDes(id) {
    this.$forceUpdate()
    for(var i=0;i<this.ruleNameList.length;i++) {
      if(id===this.ruleNameList[i].id) {
        this.DescriptionList1.ruleDescription = this.ruleNameList[i].ruleDescription
      }
      this.$set(this.relationForm,'ruleDescription',this.DescriptionList1.ruleDescription)
    }
    
  },
  // 获取规则描述和比较方式
  getDesciption(id) {

    for(var i=0;i<this.rulesList.length;i++) {
      if(id===this.rulesList[i].id) {
        this.DescriptionList.ruleDescription = this.rulesList[i].ruleDescription
        this.getSign(this.rulesList[i].englishName)
      }
      this.$set(this.addRulesForm,'ruleDescription',this.DescriptionList.ruleDescription) 
    }

  },
  // 获取基础规则下拉框
  getRulesList () {
    request.get('/basicRules/list', qs.stringify({
    })).then((res) => {
      // 请求成功处理。
      this.rulesList = res.rows
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
  // 编辑操作
  editTable ({ $index }) {
    // 获取当前点击行下标
    this.cellIndex = $index
    this.getRulesList()
    this.getDesciption(this.tableData[this.cellIndex].ruleId)
    this.savelevelForm = {}
    this.$set(this.addRulesForm,'ruleDescription',this.tableData[$index].ruleDescription)

  },
   // 编辑操作
   editTable1 ({ $index }) {
    // 获取当前点击行下标
    this.cellIndex1 = $index
    
    this.getruleNameList()
    this.getalarmList()
    if(this.rulestableData[$index].verificationRuleId!==undefined) {
    this.getNewDes(this.rulestableData[$index].verificationRuleId)
  }
    if(this.tableData[$index].verificationRuleType==='0'){
      this.ifSql = false
      this.ifRuleName = true

    } else {
      this.ifRuleName = false
      this.ifSql = true
    }
    this.relationForm.ruleDescription = this.rulestableData[$index].ruleDescription
  },
  //告警下拉框
  getalarmList() {
    request.get('/dict/data/list?dictType=alarm_level', qs.stringify({
    })).then((res) => {
      // 请求成功处理。
      this.alarmList = res.rows
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
  // 保存表属性操作
  saveTable ({ row }) {
    // 【修改8】保存编辑时添加modelVersion
    const currentModelVersion = this.modelVersion || 'edit';
    
    var params = {
      id: row.id,
      metaModeId: this.modelId,
      modelVersion: currentModelVersion,  // 新增这行
      ruleId: row.ruleId,
      ruleDescription: this.addRulesForm.ruleDescription,
      alarmLevel: row.alarmLevel,
      modificationSuggestions: row.modificationSuggestions,
      state: row.state,
      selectType: row.selectType,
      symbol: row.symbol

    }
    
    if(row.selectType==='0') {
      params.customizedSql = this.savelevelForm.customizedSql
    }
    if(row.selectType==='1') {
      params.param = this.savelevelForm.param
    }
    if(row.selectType==='2') {
      params.relMetaModeId = this.savelevelForm.relMetaModeId
      params.relIdMetaModelColumnId = this.savelevelForm.relIdMetaModelColumnId
      params.relNameMetaModelColumnId = this.savelevelForm.relNameMetaModelColumnId

    }
    request({
      headers: {
        'Content-Type': 'application/json'
      },
      method: 'put',
      url: '/verrules',
      data: params
    }).then((res) => {
      // 请求成功处理。
      this.getData()
      if(res.code===200||res.code==='200'){
      this.$message({
        message: res.msg,
        type: 'success',
        center: true
      })}
    }).catch(e => {
      // 请求失败处理。
      this.getData()
      this.$message({
        message: e,
        type: 'error',
        center: true,
          duration: 0,
          showClose: true 
      })
    })
    this.cellIndex = null
  },
  changeUp() {
    this.$forceUpdate()
  },
  saveTable1({row}) {
    // 【修改9】保存关联规则时确保modelVersion有默认值
    const currentModelVersion = this.modelVersion || 'edit';
    
    var params = {
      alarmLevel: row.alarmId,
      metaModeId: this.modelId,
      modelVersion: currentModelVersion,  // 使用处理后的默认值
      ruleDescription: this.relationForm.ruleDescription,
      modificationSuggestions: row.modificationSuggestions,
      verificationRuleId: row.verificationRuleId,
      verificationRuleType: row.verificationRuleType,
      useDefinedVerSql: this.saveRelationForm.useDefinedVerSql,
      id: row.id

    }
    request({
      headers: {
        'Content-Type': 'application/json'
      },
      method: 'put',
      url: '/verification',
      data: params
    }).then((res) => {
      // 请求成功处理。
      this.getRulesData()
      if(res.code===200||res.code==='200'){
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
    })
    this.tabName = 'Tab2'
    this.cellIndex1 = null
  },
  filterNode (value, data) {
    if (!value) return true
    return data.label.indexOf(value) !== -1
  },
  /* 格式序列化列 */
  snFormatter: function (row, col, value, index) {
    return this.pageSize * (this.pageIndex - 1) + index + 1;
  },
  /* 格式序列化列 */
  snFormatter1: function (row, col, value, index) {
    return index + 1
  },
  handleSizeChange (val) {
    this.pageSize = val
    this.getData()
  },
  handleCurrentChange (val) {
    this.pageNum = val
    this.getData()
  },
  handleCurrentChangeA (row) {
    this.metaIdList.id = row.id
    this.metaIdList.metaName = row.columnNameCn
    this.levelForm.relNameMetaModelColumnId = row.id
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
      // 选择类型弹框
  editLevel1: function (row) {
    this.savelevelForm.ifSave = 'success'
    
    if (row.selectType === '0'){
        this.sqlDrawer = true
        this.levelForm.customizedSql = ''
    } else if (row.selectType === '1') {
        this.chooseDrawer = true
          this.levelForm.param = ''
    } else if (row.selectType === '2') {
        this.writeDrawer = true
        this.getmodelIdList()
          this.levelForm.relMetaModeId = ''
          this.levelForm.relIdMetaModelColumnId = ''
          this.levelForm.relNameMetaModelColumnId = ''
          this.metatableData = []
    }
    this.oldlevelForm = Object.assign({}, this.levelForm)
  },
  // 选择类型弹框
  editLevel: function ({row}) {
    this.savelevelForm.ifSave = 'success'
    if (row.selectType === '0'){
        this.sqlDrawer = true
        if(this.savelevelForm.customizedSql===''||this.savelevelForm.customizedSql ===undefined){
          this.levelForm.customizedSql = row.customizedSql
        } else {
          this.levelForm.customizedSql = this.savelevelForm.customizedSql
        }

    } else if (row.selectType === '1') {
        this.chooseDrawer = true
        if(this.savelevelForm.param === '' || this.savelevelForm.param === undefined) {
          this.levelForm.param = row.param
        }

    } else if (row.selectType === '2') {
        this.writeDrawer = true
        this.getmodelIdList()
        
        if(this.savelevelForm.relMetaModeId === '' || this.savelevelForm.relMetaModeId ===undefined) {
          this.getTableClu(row.relMetaModeId)
          this.levelForm.relNameMetaModelColumnId = row.relNameMetaModelColumnId
          this.levelForm.relMetaModeId = row.relMetaModeId
          this.levelForm.relIdMetaModelColumnId = row.relIdMetaModelColumnId
        } else {
          this.levelForm.relMetaModeId = this.savelevelForm.relMetaModeId
          this.levelForm.relIdMetaModelColumnId = this.savelevelForm.relIdMetaModelColumnId
          this.levelForm.relNameMetaModelColumnId = this.savelevelForm.relNameMetaModelColumnId
        }

    }
    this.oldlevelForm = Object.assign({}, this.levelForm)
  },
  // 填报元模型下拉框
  getModelList () {
    request.get('/basicRules/list', qs.stringify({
    })).then((res) => {
      // 请求成功处理。
      this.rulesList = res.rows
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
  uploadTable: function () {
    for (var a in this.uploadTableForm) {
      this.uploadTableForm[a] = ''
    }
    this.uploadTableForm.modelName = 't_meta_'
    this.uploadTableDrawer = true
  },
  addTable: function () {
    for (var a in this.addTableForm) {
      this.addTableForm[a] = ''
    }
    this.addTableForm.modelName = 't_meta_'
    this.addTableDrawer = true
  },

  // 根据已关联数据源获取已关联表
  getRelationTableList: function (type) {
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
  },
  // 获取已关联表别名与属性
  getRelationTableAlex: function () {
    this.mainTableList = []
    this.relationTableAlias = ''
    this.seeClu = false
    for (var i = 0; i < this.relationTableList.length; i++) {
      if (this.relationTableList[i].relationTableId === this.relationTableIdF) {
        this.relationTableAlias = this.relationTableList[i].relationTableAlias
      }
    }
    request.get(localStorage.URL + '/dbtablecolumn/list?tableId=' + this.relationTableIdF, qs.stringify({
    })).then((res) => {
      // 请求成功处理。
      this.mainTableList = res.data
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
  // 根据数据源获取表列表
  getDbTableList: function (type) {
    let ids = type === 0 ? this.addTableForm.relationDbId : this.glTableCluForm.relationDbId
      this.tableList = []
      this.glCluObj = {}
      this.addTableForm.relationTableId = ''
      this.glTableCluForm.relationTableId = this.glTableCluForm.relationTableAlias = ''
      this.tableDataClu = []
      this.seeAlias = false
      if (ids !== undefined) {
        request.get(localStorage.URL + '/dbtable/list?dbId=' + ids, qs.stringify({
        })).then((res) => {
          // 请求成功处理。
          this.tableList = res.data
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
    getTableList: function (type) {
      this.tableList = []
      if (type === 0 || type === '0') {
        this.tableForm.relationTableId = ''
      }
      if (this.tableForm.relationDbId !== undefined) {
        request.get(localStorage.URL + '/dbtable/list?dbId=' + this.tableForm.relationDbId, qs.stringify({
        })).then((res) => {
          // 请求成功处理。
          this.tableList = res.data
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
    searchTable: function (id, nodeType) {
      if (nodeType === 'MODEL') {
        this.modelId = id
        this.selForm.cluName = ''
        this.getData()
        this.getTabData()
        this.ifSel = false
      }
    },
    // 获取表基本信息
    getTabData: function () {
      this.tableForm = {}
      request.get('metamodel/' + this.modelId, qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        res = res.data
        this.tableForm = res
        this.getTableList()
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
    getSelectData:function() {
      this.selectData = []
      request.get('/verrules/attrListOptions?modelId=' + this.modelId + '&modelVersion=' + this.modelVersion , qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        /* console.log(res) */
        this.total = parseInt(res.total)
        this.selectData = res.rows
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
      if (this.modelId === '') { this.ifSel = true } else { this.ifSel = false }
      this.tableData = []
      request.get('/verrules/listVo?metaModeId=' + this.modelId + '&modelVersion=' + this.modelVersion +
      '&columnNameCn=' + this.selForm.cluName + '&pageNum=' + this.pageIndex + '&pageSize=' + this.pageSize, qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        /* console.log(res) */
        this.loading = false
        this.total = parseInt(res.total)
        this.tableData = res.rows
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
    // 保存添加元模型
    saveAddTable: function () {
      for (var i = 0; i < this.tableList.length; i++) {
        if (this.tableList[i].id === this.addTableForm.relationTableId) {
          this.addTableForm.relationTableName = this.tableList[i].tableName
          this.addTableForm.relationTableNameCn = this.tableList[i].tableNameCn
        }
      }
      for (var j = 0; j < this.categoryList.length; j++) {
        if (this.categoryList[j].id === this.addTableForm.categoryId) {
          this.addTableForm.categoryName = this.categoryList[j].categoryName
          this.addTableForm.categoryNameCn = this.categoryList[j].categoryNameCn
        }
      }
      for (var z = 0; z < this.dbList.length; z++) {
        if (this.dbList[z].id === this.addTableForm.relationDbId) {
          this.addTableForm.relationDbName = this.dbList[z].dbName
          this.addTableForm.relationDbNameCn = this.dbList[z].dbNameCn
        }
      }
      // 判断别名是否合法
      var ifAdd = true
      if (this.addTableForm.relationTableId !== '' && this.addTableForm.relationTableId !== undefined) {
        var re = /^[a-zA-Z][a-zA-Z0-9_]*$/
        if (!re.test(this.addTableForm.relationTableAlias)) {
          this.$message({
            message: '表别名不合法，请以字母开头，由字母、数字、下划线组成',
            type: 'error',
            center: true,
            duration: 0,
            showClose: true
          })
          ifAdd = false
        }
      }
      this.$refs['addTableForm'].validate((valid) => {
        if (valid) {} else {
          ifAdd = false
          this.$message({
            message: '请输入中文名称',
            type: 'error',
            center: true,
            duration: 0,
            showClose: true
          })
          return false
        }
      })
      if (ifAdd) {
        request.post(localStorage.URL + '/metamodel/create', qs.stringify({
          modelName: this.addTableForm.modelName,
          modelNameCn: this.addTableForm.modelNameCn,
          relationTableAlias: this.addTableForm.relationTableAlias,
          remark: this.addTableForm.remark,
          relationTableName: this.addTableForm.relationTableName,
          relationTableNameCn: this.addTableForm.relationTableNameCn,
          relationTableId: this.addTableForm.relationTableId,
          relationDbId: this.addTableForm.relationDbId,
          relationDbName: this.addTableForm.relationDbName,
          relationDbNameCn: this.addTableForm.relationDbNameCn,
          categoryId: this.addTableForm.categoryId,
          categoryName: this.addTableForm.categoryName,
          categoryNameCn: this.addTableForm.categoryNameCn
        })).then((res) => {
          // 请求成功处理。
          this.addTableDrawer = false
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
              duration:0,
              showClose:true
          })
        })
      }
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
    }
  },
  beforeMount () {
    this.getData()
    this.getalarmList()
    this.getRulesData()
    this.getruleNameList()
  }
}
</script>
<style lang="stylus" rel="stylesheet/stylus" scoped>
    .cell >.el-button
      padding 5px 10px !important
    .ywysj
      width 100%
      height 100%
      .cell .el-input__inner
        height 30px
      .aa .el-input,.aa .el-form-item__content,.aa .el-input__inner
        width  3rem !important
      .el-dialog
        height auto
        margin 11% 36%
      .importDatas .el-dialog
        width 18rem
        margin auto
        .el-select
          width 2.5rem
      .addGl .el-dialog
        width 19rem
        height 14rem
        margin 2rem 11rem
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
      .el-input__inner
        height 40px
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

        .oneLine {
          overflow: hidden;
          white-space: nowrap;
          text-overflow: ellipsis;
        }
        .green {
          background-color:green
        }
        
    </style>
