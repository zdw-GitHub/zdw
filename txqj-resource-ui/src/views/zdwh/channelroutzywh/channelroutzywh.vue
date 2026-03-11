<template>
  <div>
   <zdzywhPage :tableName="tableName"></zdzywhPage>
  </div>
</template>

<script>
  import zdzywhPage from '@/views/commonPage/zdzywh/index';
  export default {
    name: "zdzywh",
    components:{
      zdzywhPage
    },
    data() {
      return {
        tableName: 'SG_TCCON_TCOPTICALROAD_B',
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
