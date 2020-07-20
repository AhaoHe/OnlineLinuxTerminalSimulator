<template>
    <div>
    <el-page-header @back="goBack" class="resiger-title" content="返回首页" />
      页面不存在，请重试！
    </div>
</template>

<script>
    export default {
        name: "NotFount",
        methods:{
            goBack(){
                this.$router.replace('/login')
            }
        }
    }
</script>

<style scoped>
</style>