<template>
    <div>
        <div style="margin-bottom:10px;text-align:center">  
            <el-button type="text" style="margin-right:10px" @click="refresh">刷新</el-button>
            <span v-text="`在线人数：${onlinecount}`"></span>
        </div>
        <div>
            <user-list ref="userlist" v-on:getcount="getCount" :is-super="isSuper" :type="'online'" ></user-list>
        </div>
    </div>
</template>
<script>
import { onlineCount } from '@/api/online'
import userList from './components/userList'
export default {
    mounted(){
        this.getCount()
    },
    data(){
        return {
            onlinecount: 0,
            isSuper:this.$store.getters.level==10
        }
    },
    methods:{
        getCount(){
            onlineCount().then(r=>{
                this.onlinecount= r.object
                console.log(r.object)
            }).catch()
        },
        refresh(){
            this.getCount()
            this.$refs.userlist.getData()
        }
    },
    components:{
        userList
    }
    
}
</script>
<style scoped>

</style>