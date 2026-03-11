<template>
  <div class="seeCreateFiber">
    <div class="box">
      <p class="title">生成光缆</p>
      <el-table
        :data="fibers"
        height="calc(100% - 70px)"
        style="width: 100%">
        <el-table-column
          prop="resName"
          label="光缆名"
          width="">
        </el-table-column>
        <el-table-column
          prop="lineNumber"
          label="纤芯数"
          width="100">
        </el-table-column>
      </el-table>
    </div>
    <div class="box">
      <p class="title">光缆段</p>
      <el-table
        :data="segs"
        height="calc(100% - 70px)"
        style="width: 100%">
        <el-table-column
          prop="resName"
          label="光缆段名"
          width="">
        </el-table-column>
        <el-table-column
          prop="lineNumber"
          label="纤芯数"
          width="100">
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
export default {
  name: "seeCreateFiber",
  props: {
    segs: Array,
    fibers: Array,
  },
  data () {
    return {

    }
  }
}
</script>

<style scoped>
  .seeCreateFiber{
    width: 100%;
    height: 100%;
  }
  .box{
    height: 50%;
  }
  .title{
    width: 100%;
    height: 40px;
    border-bottom:1px solid black;
    margin-bottom: 10px !important;
    margin-top: 20px !important;
  }
</style>