<template>
    <div class="legend">
      <p class="title" @click="isShow = !isShow">
        图例
        <span :class="{'el-icon-caret-top': !isShow, 'el-icon-caret-bottom': isShow}"></span>
      </p>
      <ul v-show="isShow" class="con">
        <li class="imgs">
          <p><span :style="{'backgroundImage': 'url(' + require('@/assets/gltxh_img/gltxh/fiber.png') + ')'}"></span>光缆</p>
        </li>
        <li class="imgs">
          <p><span :style="{'backgroundImage': 'url(' + require('@/assets/gltxh_img/gltxh/link.png') + ')'}"></span>光缆段</p>
        </li>
        <li class="imgs">
          <p><span :style="{'backgroundImage': 'url(' + require('@/assets/gltxh_img/gltxh/site.png') + ')'}"></span>站点</p>
        </li>
        <li class="imgs">
          <p><span :style="{'backgroundImage': 'url(' + require('@/assets/gltxh_img/gltxh/tbox.png') + ')'}"></span>接头盒</p>
        </li>
        <li class="line">
          <p><span style="background: #67C23A"></span>光缆段数据校验通过</p>
        </li>
        <li class="line">
          <p><span style="background: #0084FF"></span>光缆段数据校验未通过</p>
        </li>
      </ul>
    </div>
</template>

<script>
    export default {
        name: "Tuli",
      data(){
          return {
            isShow:false
          }
      }
    }
</script>

<style lang="scss">
  .legend{
    border: 1px solid #eee;
    width: 230px;
    .title{
      width: 100%;
      height: 40px;
      line-height: 40px;
      padding: 0 10px;
      font-size: 16px;
      cursor: pointer;
    }
    .con {
      border-top: 1px solid #eee;
      width: 100%;
      padding: 10px;
      margin: 0;
      .line span{
        width: 40px;
        height: 12px;
        margin: 14px 10px 14px 0;
      }
      li{
        width: 100%;
        height: 40px;
        line-height: 40px;
        margin: 0!important;
        list-style: none;
        font-size: 14px;
        span{
          display: inline-block;
          float: left;
          width: 50px;
          height: 40px;
          background-repeat: no-repeat;
          background-size: 30px;
          background-position: 0;
        }
      }
    }
  }
</style>
