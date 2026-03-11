<template>
    <div>
     <channeTypePage :tableName="tableName"></channeTypePage>
    </div>
  </template>
  
  <script>
    /* import zdzywhPage from '@/views/commonPage/zdzywh/index'; */
    import channeTypePage from './component/index.vue';
    export default {
      name: "channeType",
      components:{
        channeTypePage
      },
      data() {
        return {
          // 表名
          tableName: 'SG_TCCON_TCPIPESEG_B',
          tableData:[{}]
        }
      },
      watch:{
  
      },
      created() {
        },
      methods: {
  
      },
    };
  </script>
  <style>
  
  </style>
  