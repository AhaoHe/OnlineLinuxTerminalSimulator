<template>
    <div>
        <el-input
            v-model="query.search"
            size="mini"
            style="width:50%"
            placeholder="输入用户名搜索"/>
        <el-table
        :data="data"
        style="width: 100%;margin-buttom=20px;"
        >
        <el-table-column
        prop="username"
        label="用户名">
        </el-table-column>
        <el-table-column
        prop="role_name"
        label="角色">
        </el-table-column>
        <el-table-column
        prop="server_username"
        label="系统名/文件名">
        </el-table-column>
        </el-table>

        <el-pagination
        @current-change="getData"
        :current-page.sync="query.current"
        :page-size="query.size"
        layout="total, prev, pager, next, jumper"
        :total="total">
        </el-pagination>
    </div>
</template>
<script>
import { getGroupUsers } from '@/api/terminal'
export default {
    mounted(){
        this.getData()
    },
    data(){
        return{
            data:[],
            query:{
                groupId:this.$store.getters.groupId,
                level:this.$store.getters.level,
                current:1,
                size:6,
                search:'',
            },
            total:0
        }
    },
    methods:{
        getData(){
            getGroupUsers(this.query).then(r=>{
                console.log(r)
                this.data=r.object.records
                this.query.current=r.object.current
                this.query.size=r.object.size
                this.total=r.object.total
            }).catch()
        }
    },
    watch:{
        'query.search':function(newVal){
            this.getData()
        }
    }
}
</script>
<style scoped>

</style>