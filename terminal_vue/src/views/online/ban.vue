<template>
    <div>
        <user-list :is-super="isSuper" :type="'ban'"></user-list>
    </div>
</template>
<script>
import userList from './components/userList'
export default {
    data(){
        return{
            isSuper:this.$store.getters.level==10
        }
    },
    components:{
        userList
    }
}
</script>
<style scoped>

</style>