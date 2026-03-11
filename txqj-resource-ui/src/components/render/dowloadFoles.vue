<template>
  <div id="dowloadFoles">
    <p class="name" v-for="(item, i) in data" :key="i" @click="download(item)">{{item.name}}</p>
  </div>
</template>

<script>
import {downloadOpt} from '@/api/dowloadFiles'
  export default {
    name: "dowloadFoles",
    props: {
      data: Array
    },
    data () {
      return {

      }
    },
    methods: {
      // 参考网页: https://www.cnblogs.com/xianglian/p/15481203.html
      download (file) {
        downloadOpt(file.url).then(res => {
          const link = document.createElement("a");  //创建a标签
          let blob = new Blob([res], { type: "application/vnd.ms-excel" }); // response就是接口返回的文件流
          let objectUrl = URL.createObjectURL(blob);
          link.href = objectUrl;
          link.download = file.name; // 自定义文件名
          link.click(); // 下载文件
          URL.revokeObjectURL(objectUrl); // 释放内存
          window.open(res.config.url)
        })
      }
    },
    mounted () {
    }
  }
</script>

<style scoped>
  .name{
    margin: 0 !important;
    color: #1872FF;
    text-align: left !important;
  }
</style>
