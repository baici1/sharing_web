<template>
  <div>
    <Header></Header>
    <!-- 个人信息内容 -->
<router-view></router-view>
  </div>
</template>

<script>
import Header from '../../components/Header'
export default {
  components: { Header },

  methods: {}
}
</script>

<style scoped>

</style>
