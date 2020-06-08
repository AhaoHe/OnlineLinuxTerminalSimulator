<template>
    <div>
        <div style="margin-bottom:10px;text-align:center">  
            <el-button type="text" style="margin-right:10px" @click="getData">刷新</el-button>
            <span v-text="`在线人数：${total}`"></span>
        </div>
        <el-card>
            <el-table
            ref="filterTable"
            :data="data"
            style="width: 100%;">
                <el-table-column 
                prop="username"
                label="在线人员">
                </el-table-column>
                <el-table-column label="操作" width="200%">
                    <template slot="header" slot-scope="">
                        <el-input
                        v-model="request.search"
                        size="mini"
                        style="width:100%"
                        placeholder="输入用户名搜索"/>
                    </template>
                    <template slot-scope="scope">
                        <el-popconfirm style="margin-left:10px"
                            placement="top"
                            :title="`确定封禁${scope.row.username}的Linux账户吗？`"
                            @onConfirm="ban(scope.row.username)"
                        >
                            <el-button slot="reference" type="danger" size="mini">封禁</el-button>
                        </el-popconfirm>
                        <el-popconfirm style="margin-left:10px"
                            placement="top"
                            :title="`确定下线${scope.row.username}吗？`"
                            @onConfirm="line(scope.row.username)"
                        >
                            <el-button slot="reference" type="danger" size="mini">下线</el-button>
                        </el-popconfirm>
                    </template>
                </el-table-column>
            </el-table>
        </el-card>
        <div class="block" style="text-align: center;">
            <el-pagination
            :current-page.sync="request.current"
            :page-size="request.pageSize"
            layout="total, prev, pager, next, jumper"
            :total="total">
            </el-pagination>
        </div>
    </div>
</template>
<script>
import { online,onlineCount,offline,banUser } from '@/api/online'
export default {
    mounted(){
        this.getData()
    },
    data(){
        return {
            request:{
                current:1,
                pageSize:1,
            },
            search:'',
            total: 0,
            data:[],
        }
    },
    methods:{
        getData(){
            online().then(r=>{
                const val = r.object
                this.data = []
                for(let i=0;i<val.length;i++){
                    this.data.push({'username':val[i]})
                }
                console.log(this.data);
            }).catch()
            onlineCount().then(r=>{
                this.total= r.object
            }).catch()
        },
        line(username){
            offline(username).then(r=>{
                this.getData()
                this.$message.success(username+"下线成功！")
            }).catch(e=>{
                console.log(e)
            })
        },
        ban(username){
           banUser(username).then(r=>{
               this.getData()
               this.$message.success("已封禁"+username)
           }).catch()
        }
    },
    watch:{
        'request.search':function(newVal){
            
        }
    }
    
}
</script>
<style scoped>

</style>