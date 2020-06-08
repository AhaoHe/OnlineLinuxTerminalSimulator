<template>
    <div id="footerHome" v-text="footer"></div>
</template>
<script>
export default {
    data() {
      return {
        footer: "Author : 何铭浩2016051172",
      }
    }
}
</script>
<style>
#footerHome{
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
</style>