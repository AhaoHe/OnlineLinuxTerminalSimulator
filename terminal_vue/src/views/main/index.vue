<template>
    <el-card>
        <p v-text="url"></p>
        <p v-text="gname"></p>
        <p v-text="groupId"></p>
        <p v-text="level"></p>
        <button v-text="{roles}"></button>
    </el-card>
</template>
<script>
import store from '@/store'
import * as api from '@/api/server'
export default {
    data(){
        return{
            roles: store.getters.roles,
            url: this.$axios.defaults.baseURL,
            gname:this.$store.getters.gname,
            level:this.$store.getters.level,
            groupId:this.$store.getters.groupId,
        }
    },
    methods:{

    }
}
</script>