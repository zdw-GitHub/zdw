<template>
  <div class="stepTipImgs">
    <div class="stepTip" v-if="step == 0">
      <p class="title">操作步骤</p>
      <p>1 在画布上方工具栏中 拖拽站点图标致画布上</p>
      <p>2 拖动到合适位置所开鼠标 在弹出的对话框中选择站点,ODF,接头盒</p>
    </div>
    <div class="stepTip" v-if="step == 1">
      <p class="title">操作步骤</p>
      <p>1.在画布上方工具栏中 拖拽接头盒图标致画布上 在合适的位置松开鼠标</p>
    </div>
    <div class="stepTip" v-if="step == 2">
      <p class="title">新增光缆段</p>
      <p>1 进入此步骤时自动切换到新建光缆段模式</p>
      <p>2 在新建光缆段模式中 点击站点/接头盒 确认光缆段起点 再次点击其他光缆段/接头盒 确认终点(点击空白处可重新选择起点)</p>
      <p>3 确认终点后 在弹出的对话框中选择纤芯数量</p>
      <p class="title">删除光缆段</p>
      <p>1 点击画布工具栏左侧光标 切换至选择模式</p>
      <p>2 右键左侧树或画布上的光缆段 点击删除/撤销</p>
      <p class="title">注 : </p>
      <p>删除 : 为数据打上删除标记 如果审批通过 数据将会从正式库删除</p>
      <p>撤销 : 仅在画布上删除 不影响正式库</p>
      <p>删除和撤销对新增的数据来说没有区别 对选择的数据来说删除会从正式库删除撤销对正式库无影响</p>
    </div>
    <div class="stepTip" v-if="step == 3">
      <p class="title">操作步骤</p>
      <p>1 在左侧栏中选择要编辑的接头盒</p>
      <p>2 通过点击纤芯接口连接 也可以批量连接/删除</p>
      <p>3 完成后点击线芯连接框的关闭按钮 关闭批量操作模式 想再次编辑纤芯接续 可右键接头盒 选择纤芯接续</p>
    </div>
    <div class="stepTip" v-if="step == 4">
      <p class="title">操作步骤</p>
      <p>生成光缆后会自动弹出生成资源</p>
      <p>1 在左侧栏中选择要编辑的光缆</p>
      <p>2 编辑完成后点击保存按钮 自动跳到下一个光缆</p>
      <p>3 全部编辑完成后点击关闭按钮批量操作模式 关闭批量操作模式 想再次编辑光缆 可右键画布左侧树中的光缆 选择修改属性</p>
      <p class="title">注 : </p>
      <p>也可暂不完善光缆信息 数据审批通过后 再到光缆基本信息页面中进行完善</p>
    </div>
    <div class="stepTip" v-if="step == 5">
      <p class="title">操作步骤</p>
      <p>1 在左侧栏中选择要编辑的光缆段</p>
      <p>2 编辑完成后点击保存按钮 自动跳到下一个光缆段</p>
      <p>3 全部编辑完成后点击关闭按钮批量操作模式 关闭批量操作模式 想再次编辑光缆段 可右键画布左侧树中的光缆段 选择修改属性</p>
      <p class="title">注 : </p>
      <p>也可暂不完善光缆段信息 数据审批通过后 再到光缆段基本信息页面中进行完善</p>
    </div>
    <div class="stepTip" v-if="step == 6">
      <p class="title">操作步骤</p>
      <p>1 在左侧栏中选择要编辑的光缆</p>
      <p>2 点击纤芯 对应纤芯关系会高亮</p>
      <p>3 点击纤芯路由详情中的修改按钮 可修改光缆纤芯与光缆段纤芯对应关系 解决错芯问题</p>
      <p>4 右键光缆纤芯/光缆段纤芯 可对纤芯进行信息完善(可暂不完善纤芯信息)</p>
    </div>
    <div class="stepTip" v-if="step == 7">
      <p class="title">操作步骤</p>
      <p>可在此步骤查看生成数据资源清单以及数据校验是否通过</p>
      <p>可暂不完善数据 数据审批通过后 再到资源对应页面进行完善</p>
      <p>点击画布上方数据校验按钮可再次查看</p>
    </div>
    <div class="stepTip" v-if="step == 8">
      <p class="title">操作步骤</p>
      <p>确认资源无误后 点击提交审核按钮进行提交</p>
    </div>
    <el-image
      :src="require('@/assets/images/step/' +  + (step + 1) + '.gif')"
      :preview-src-list="[require('@/assets/images/step/' +  + (step + 1) + '.gif')]"
      style="width: 80%; float: right"
    />
  </div>
</template>

<script>
  export default {
    name: "stepTipImgs",
    props: {
      step: Number
    }
  }
</script>

<style scoped>
  .stepTipImgs{
    width: 100%;
    height: 100%;
  }
  .stepTip{
    width: calc(20% - 10px);
    height: 100%;
    padding-right: 10px;
    float: left;
    border-right: 1px solid #ccc;
  }
  .stepTip p{
    width: 100%;
    line-height: 30px;
    font-size: 14px;
    margin: 0!important;
    padding: 0!important;
    text-indent: 2rem;
  }
  .stepTip .title{
    font-size: 16px;
    font-weight: bold;
    text-indent: 0!important;
    margin-top: 20px!important;
  }
</style>