<template>
    <div>
        <div>
            <el-input
            v-model="query.search"
            size="mini"
            style="width:50%;margin-right:20px"
            placeholder="输入用户名搜索"/>
            <el-popover
            placement="right"
            width="200"
            style="float:right"
            trigger="click">
            <el-select v-model="selectVal" placeholder="请选择" @change="currentSel()">
                <el-option
                v-for="item in selection"
                :key="item"
                :label="item+'号服务器'"
                :value="item">
                </el-option>
            </el-select>
            <el-button type="primary" size="mini" slot="reference" :loading="loading">转移至新服务器</el-button>
            </el-popover>
        </div>
        <el-table
        :data="data"
        @selection-change="handleSelectionChange"
        style="width: 100%;margin-buttom=20px;">
        <el-table-column
        type="selection"
        :selectable="disabled"
        width="55">
        </el-table-column>
        <el-table-column
        prop="username"
        label="用户名">
        <template slot-scope="scope">
            <span :style="`color:${scope.row.server_status==1?'green':'red'}`">{{ scope.row.username }}</span>
        </template>
        </el-table-column>
        <el-table-column
        prop="role_name"
        label="角色">
        </el-table-column>
        <el-table-column
        prop="gname"
        label="组名">
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
import { getUsersInfo,getSelection,toNewServer } from '@/api/server'
export default {
    props:{
        SId:{
            type:Number,
            default:0
        }
    },
    mounted(){
        this.getData()
        this.getSelectionData()
    },
    data(){
        return{
            data:[],
            query:{
                serverId:this.SId,
                current:1,
                size:6,
                search:'',
            },
            checkedVal:{},
            total:0,
            selection:[],
            selectVal:'',
            loading:false
        }
    },
    methods:{
        getData(){
            getUsersInfo(this.query).then(r=>{
                console.log(r.object.records)
                this.data=r.object.records
                this.query.current=r.object.current
                this.query.size=r.object.size
                this.total=r.object.total
            }).catch()
        },
        getSelectionData(){
            getSelection(this.query.serverId).then(r=>{
                this.selection = r.object
            }).catch()
        },
        handleSelectionChange(selection){
            console.log(selection)
            this.checkedVal=selection
        },
        currentSel(){
            this.loading=true;
            toNewServer(this.checkedVal,this.query.serverId,this.selectVal).then(r=>{
                if(r.object){
                    this.$message.success('转移成功')
                    this.getData();
                }else{
                    this.$message.error('转移失败！服务器连接失败！')
                }
                this.selectVal=''
                this.loading=false;
            }).catch()
        },
        disabled(row){
            return row.disabled==0
        }
    },
    watch:{
        'SId':function(newVal){
            this.query.serverId=newVal
            this.getData();
            this.getSelectionData();
        },
        'query.search':function(newVal){
            this.getData();
        }
    }
}
</script>
<style scoped>

</style>