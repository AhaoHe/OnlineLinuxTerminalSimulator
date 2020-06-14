<template>
    <div>分配命令行（选中：{{ `${GName}-${serverId}` }}号）
        <div style="margin-bottom:10px;margin-top:10px">
        <el-button size="small" type="primary" @click="submit" style="margin-right:10px" :disabled="groupId==0">提交</el-button>
        <el-input
            v-model="request.search"
            size="mini"
            style="width:50%"
            placeholder="输入命令行搜索"/>
        </div>
        <el-card>
            <el-table
            ref="Table"
            :data="data"
            @selection-change="selectChange"
            style="width: 100%;">
                <el-table-column
                type="selection"
                :selectable="selectable"
                width="55">
                </el-table-column>
                <el-table-column type="expand">
                    <template slot-scope="props">
                    <el-form label-position="left" inline class="demo-table-expand">
                        <el-form-item label="描述">
                        <span>{{ props.row.description }}</span>
                        </el-form-item>
                    </el-form>
                    </template>
                </el-table-column>
                <el-table-column 
                prop="command"
                label="命令行">
                </el-table-column>
            </el-table>
        </el-card>
        <div class="block" style="text-align: center;">
            <el-pagination
            @current-change="getData"
            :current-page.sync="request.current"
            :page-size="request.pageSize"
            layout="total, prev, pager, next, jumper"
            :total="total">
            </el-pagination>
        </div>
    </div>
</template>
<script>
import store from '@/store'
import { getCommandInfo,addCommandInfo,updateCommandInfo
,delCommandInfo,changeStatus,getGroupCommand,changeGroupCommand } from '@/api/command'
export default {
    props: {
        groupId: {
          type: Number,
          default: 0
        },
        GName: {
          type: String,
          default: '无'
        },
        serverId: {
          type: Number,
          default: 0
        }
    },
    mounted(){
        this.getData()
    },
    data(){
        return {
            level:store.getters.level,
            request:{
                search:'',
                current:1,
                pageSize:6,
            },
            total: 0,
            data:[],
            selection:[],
            unselection:[],
            gid:0,
        }
    },
    methods:{
        getData(){
            new Promise((resolve,reject)=>{
                getGroupCommand(this.groupId).then(r=>{
                    resolve(r.object)
                }).catch()
            }).then(resolve=>{
                getCommandInfo(this.request).then(r=>{
                    this.total=r.object.total
                    this.data = r.object.records
                    this.request.current = r.object.current
                    this.request.pageSize = r.object.size
                    this.$nextTick(() => {
                        this.data.forEach(e=>{
                            if(e.status==0){
                                this.$refs.Table.toggleRowSelection(e,true);
                            }else if(resolve.length>0&&resolve.indexOf(e.id)!=-1){
                                this.$refs.Table.toggleRowSelection(e,true);
                            }
                        })
                    })
                }).catch()
            }).catch()
        },
        selectChange(selection){
            this.selection=[]
            this.unselection=[]
            selection.forEach(e=>{
                if(e.status!=0){
                    this.selection.push(e.id)
                }
            })
            this.data.forEach(e=>{
                if(this.selection.indexOf(e.id)==-1&&e.status!=0){
                    this.unselection.push(e.id)
                }
            })
        },
        submit(){
            console.log(this.groupId,this.selection,this.unselection)
            changeGroupCommand(this.groupId,this.selection.join(','),this.unselection.join(',')).then(r=>{
                this.$message.success("修改成功")
                this.getData()
            }).catch
        },
        selectable(row){
            return row.status!=0
        }
    },
    watch:{
        'request.search':function(newVal){
            this.getData();
        },
        'groupId':function(newVal){
            this.gid=newVal
        }
    }
    
}
</script>
<style scoped>

</style>