<template>
    <el-card>
        <el-table
        ref="filterTable"
        :data="data"
        @filter-change="filterChange"
        style="width: 100%;">
            <el-table-column 
            type="expand" v-if="isSuper">
                <template slot-scope="props">
                    <el-form label-position="left" inline class="demo-table-expand">
                        <el-form-item label="服务器">
                        <span>{{ `${props.row.serverId}号服务器` }}</span>
                        </el-form-item>
                        <el-form-item label="所属组ID">
                        <span>{{ props.row.groupId }}</span>
                        </el-form-item>
                        <el-form-item label="所属组">
                        <span>{{ props.row.gname }}</span>
                        </el-form-item>
                        <el-form-item label="所属系统组">
                        <span>{{ props.row.groupName }}</span>
                        </el-form-item>
                    </el-form>
               </template>
            </el-table-column>
            <el-table-column 
            prop="username"
            label="用户名"
            width="150">
                <template slot-scope="scope">
                    {{ scope.row.username }}
                    <el-tag v-if="type=='online'&&scope.row.online==true" type="success" size="mini">在线</el-tag>
                </template>
            </el-table-column>
            <!-- <el-table-column 
            prop="gname"
            label="所在组">
            </el-table-column>
            <el-table-column 
            prop="serverId"
            label="服务器">
                <template slot-scope="scope">
                    {{ scope.row.serverId +'号服务器' }}        
                </template>
            </el-table-column> -->
            <el-table-column 
            prop="count"
            width="280"
            label="历史代码量（总量/危险数）">
                <template slot-scope="scope">
                    {{ `${scope.row.count} / ${scope.row.dangercount}` }}        
                </template>
            </el-table-column>
            <el-table-column 
            prop="count"
            width="280"
            label="今日代码量（总量/危险数）">
                <template slot-scope="scope">
                    {{ `${scope.row.countNow} / ${scope.row.dangercountNow}` }}        
                </template>
            </el-table-column>
            <el-table-column 
            prop="status"
            width="100"
            filter-placement="bottom-end"
            :filter-multiple="false"
            :filters="statusFilter"
            column-key = 'status'
            label="风险评估">
                <template slot-scope="scope">
                    <el-tag :type="getType(scope.row.level)" v-text="getLevel(scope.row.level)"></el-tag>       
                </template>
            </el-table-column>
            <el-table-column label="操作" width="260">
                <template slot="header" slot-scope="scope">
                    <el-input
                    v-model="query.search"
                    size="mini"
                    style="width:100%"
                    placeholder="输入用户名搜索"/>
                </template>
                <template slot-scope="scope">
                    <el-button :disabled="scope.row.disabled" type="info" size="mini" @click="dialogVisible=true;checked=scope.row.username">统计查看</el-button>
                    <el-popconfirm style="margin-left:10px"
                        v-if="type=='online'&&query.level>scope.row.power" :disabled="scope.row.disabled"
                        placement="top"
                        :title="`确定封禁${scope.row.username}的Linux账户吗？`"
                        @onConfirm="ban(scope.row.username)"
                    >
                        <el-button slot="reference" type="danger" size="mini">封禁</el-button>
                    </el-popconfirm>
                    <el-popconfirm style="margin-left:10px"
                        v-if="type=='online'&&scope.row.online==true&&query.level>scope.row.power" :disabled="scope.row.disabled"
                        placement="top"
                        :title="`确定下线${scope.row.username}吗？`"
                        @onConfirm="line(scope.row.username)"
                    >
                        <el-button slot="reference" type="danger" size="mini">下线</el-button>
                    </el-popconfirm>
                    <el-popconfirm style="margin-left:10px"
                        v-if="type=='ban'" :disabled="scope.row.disabled"
                        placement="top"
                        :title="`确定解禁${scope.row.username}吗？`"
                        @onConfirm="unban(scope.row.username)"
                    >
                        <el-button v-if="type=='ban'"  slot="reference" type="danger" size="mini">解禁</el-button>
                    </el-popconfirm>
                </template>
            </el-table-column>
        </el-table>
        <div class="block" style="text-align: center;">
            <el-pagination
            @current-change="getData"
            :current-page.sync="query.current"
            :page-size="query.size"
            layout="total, prev, pager, next, jumper"
            :total="total">
            </el-pagination>
        </div>

        <el-dialog title="统计查看" :visible.sync="dialogVisible" style="width:100%">
            <get-charts :user-name="checked"></get-charts>
        </el-dialog>
    </el-card>
</template>
<script>
import { getBanUserList , getUserList ,offline,banUser,UnbanUser} from '@/api/online'
import GetCharts from './charts'
export default {
    props:{
        isSuper: {
            type: Boolean,
            default: false
        },
        type:{
            type: String,
            default: 'online'
        },
    },
    mounted(){
        this.getData();
    },
    data(){
        return{
            dialogVisible:false,
            checked:'',
            data:[],
            total:0,
            query:{
                current:1,
                size:6,
                level:this.$store.getters.level,
                search:'',
                groupId:this.$store.getters.groupId,
                levelquery:0,
            },
            statusFilter:[{text: '低风险', value: 1},{text: '中风险', value: 2},{text: '高风险', value: 3}],
        }
    },
    methods:{
        filterChange(filters){
            console.log(filters)
            this.query.levelquery = filters['status'][0];
            this.getData()
        },
        getData(){
            console.log(this.type)
            if(this.type=='ban'){
                getBanUserList(this.query).then(r=>{
                    this.query.current=r.object.current
                    this.query.size=r.object.size
                    this.total=r.object.total
                    this.data=r.object.records
                    console.log(this.data)
                }).catch()
            }
            if(this.type=='online'){
                getUserList(this.query).then(r=>{
                    this.query.current=r.object.current
                    this.query.size=r.object.size
                    this.total=r.object.total
                    this.data=r.object.records
                    console.log(this.data)
                }).catch()
            }
        },
        getLevel(data){
            let level='error';
            switch(data){
                case 1: 
                    level= '低风险'
                    break;
                case 2: 
                    level= '中风险'
                    break;
                case 3: 
                    level= '高风险'
                    break;
                default: 
                    level='error'
            }
            return level
        },
        getType(data){
            let type='info';
            switch(data){
                case 1: 
                    type= 'success'
                    break;
                case 2: 
                    type= 'warning'
                    break;
                case 3: 
                    type= 'danger'
                    break;
                default: 
                    type='info'
            }
            return type
        },
        line(username){
            offline(username).then(r=>{
                this.getData()
                this.$emit('getcount')
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
        },
        unban(username){
            UnbanUser(username).then(r=>{
               this.getData()
               this.$message.success("已解禁"+username)
            }).catch()
        }
    },
    watch:{
        'query.search':function(newVal){
            this.query.search = newVal
            this.getData()
        },
        onlineCount:function(newVal){
            this.getData()
        }
    },
    components:{
        GetCharts
    }
}
</script>
<style scoped>
  .demo-table-expand {
    font-size: 0;
  }
  .demo-table-expand label {
    width: 90px;
    color: #99a9bf;
  }
  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }
</style>