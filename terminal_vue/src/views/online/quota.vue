<template>
    <div>
        <div style="margin-bottom:10px;text-align:center">  
            <el-button v-if="$store.getters.level==10" type="text" slot="reference" style="margin-right:10px" @click="getDFH">点击查看总磁盘使用情况</el-button>
        </div>
        <el-card>
            <el-table
            ref="filterTable"
            :data="data"
            style="width: 100%;">
                <el-table-column 
                prop="username"
                label="用户名">
                    <template slot="header">
                        用户名
                        <el-tooltip class="item" effect="dark" content="红色为系统封禁用户，绿色为正常用户" placement="top">
                            <i class="el-icon-warning-outline"></i>
                        </el-tooltip>
                    </template>
                    <template slot-scope="scope">
                        <span :style="`color:${scope.row.serverStatus==1?'green':'red'}`">{{ scope.row.username }}</span>
                    </template>
                </el-table-column>
                <el-table-column 
                prop="gname"
                label="所在组">
                </el-table-column>
                <el-table-column 
                prop="serverId"
                label="服务器">
                    <template slot-scope="scope">
                        {{ scope.row.serverId +'号服务器' }}        
                    </template>
                </el-table-column>
                <el-table-column 
                prop="disk"
                label="磁盘额度">
                <template slot-scope="scope">
                    <el-popover
                    placement="right"
                    width="200"
                    trigger="click">
                    <el-select v-model="selectVal" placeholder="请选择" @change="currentSel(scope.row.uid,scope.row.username,selectVal)">
                        <el-option
                        v-for="item in selection"
                        :key="item.value"
                        :label="item.text"
                        :value="item.value">
                        </el-option>
                    </el-select>
                    <el-tag size="medium"
                        slot="reference"
                        @click="selectVal=scope.row.disk;"
                        :type="'未分配'==(scope.row.disk) ? 'danger' : 'primary'">{{scope.row.disk}}</el-tag>
                    </el-popover>
                </template>
                </el-table-column>
                <el-table-column label="操作" width="200%">
                    <template slot="header" slot-scope="scope">
                        <el-input
                        v-model="search"
                        size="mini"
                        style="width:100%"
                        placeholder="输入用户名搜索"/>
                    </template>
                    <template slot-scope="scope">
                        <el-button @click="quota(scope.row.uid,scope.row.username)" type="text" size="mini">查看个人磁盘情况</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </el-card>
        <div class="block" style="text-align: center;">
            <el-pagination
            :current-page.sync="current"
            @current-change="getData"
            :page-size="size"
            layout="total, prev, pager, next, jumper"
            :total="total">
            </el-pagination>
        </div>

        <el-dialog
            title="详情"
            :visible.sync="dialogVisible"
            width="30%">
            <!-- <span>{{ '总量  使用量  剩余量  使用率  挂载' }}</span> -->
            <span v-for=" (o,index) in query" :key="index">{{ list[index] }} : {{ o }}<br></span>
            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>
<script>
import { dfh,disk,diskuser,updateDisk } from '@/api/online'
import store from '@/store'
export default {
    mounted(){
        this.getData()
    },
    data(){
        return {
            dialogVisible:false,
            current:1,
            size:6,
            search:'',
            total: 0,
            data:[],
            query:'',
            list:[],
            selectVal:'',
            selection:[{text:'10M',value:'10M'},{text:'20M',value:'20M'},{text:'50M',value:'50M'},{text:'100M',value:'100M'}]
        }
    },
    methods:{
        getDFH(){
            // console.log(store.getters.username)
            dfh(store.getters.username).then(r=>{
                let val = r.object[5]
                val = val.substring(val.indexOf(' '));
                val = val.split(" ").filter(e=>{return e!=""})
               // val = val.join('<br\>')
               console.log(val)
                this.query = val
                this.list = ['总量','使用量','剩余量','使用率','挂载']
                this.dialogVisible=true;
            }).catch()
        },
        getData(){
            disk(this.current,this.size,this.search,store.getters.level,store.getters.groupId).then(r=>{
                console.log(r.object.records)
                this.current = r.object.current
                this.size = r.object.size
                this.total = r.object.total
                this.data = r.object.records
            }).catch()
        },
        quota(uid,username){
            diskuser(uid,username).then(r=>{
                let val = r.object
                val=val.split(" ").filter(e=>{return e!=""})
                this.query=val
                console.log(val)
                if(val.length==7){
                    this.list=['文件系统','磁盘使用情况','警告阈值','最大阈值','存在文件数','警告文件数','最大文件数']
                }else if(val.length==9){
                    this.list=['文件系统','磁盘使用情况','警告阈值','最大阈值','宽限剩余时间','存在文件数','警告文件数','最大文件数','宽限剩余时间']
                }
                this.dialogVisible=true
            }).catch()
        },
        currentSel(uid,username,disk){
            updateDisk(uid,username,disk).then(r=>{
                if(r.object){
                    this.data.forEach(e=>{
                        if(e.uid==uid){
                            e.disk=disk
                        }
                    })
                    this.$message.success('修改成功')
                }else{
                    this.$message.error('修改失败!可能原因：系统错误或超级管理员无法修改');
                }
            }).catch()
        }
    },
    watch:{
        'search':function(newVal){
            this.search=newVal
            this.getData()
        }
    }
    
}
</script>
<style scoped>

</style>