<template>
  <div class="commonWh">
    <carrying
      v-loading="loading"
      ref="carryingPage"
      :tableName="listTableName"
      :jumpLink="jumpLink"
      :searchForm="searchForm"
    ></carrying>
  </div>
</template>

<script>
import carrying from '@/views/commonPage/carrying/index';
import {cabinetAssociatedSites} from '@/api/crack/associatedSites'
export default {
  name: "associatedSites",
  props: {
    id: String,
    tableName: String
  },
  components: {carrying},
  data() {
    return {
      loading: true,
      listTableName: 'SG_TCCON_TCSITE_B',
      jumpLink: [
        {attributeNameUnderline: 'NAME', menuPath: 'roomsource/zdwh/common/SG_TCCON_TCSITE_B'}
      ],
      ids: '',
      searchForm: {
        ID: '',
      },
    }
  },
  methods: {
    getTable() {
      this.loading = true
      cabinetAssociatedSites({resId: this.id}).then(res => {
        this.ids = res.msg
        this.getTableData();
        this.loading = false
      })

    },
    getTableData() {
      this.searchForm.ID = this.ids
      this.$refs.carryingPage.getTableData()
    },
  },
  created() {
    this.getTable()
  }
}
</script>

<style scoped>
</style>
