<template>
    <el-container>
        <el-header>
            <header-menu></header-menu>
        </el-header>
        <el-main>
            Main
            <router-view></router-view>
        </el-main>
    </el-container>
</template>>

<script>
    import HeaderMenu from '@/components/headerMenu'
    export default{
        name: "Main",
        data() {
            return{}
        },
        components:{
            HeaderMenu
        }
    }
</script>