<template>
    <div>
        <div style="margin-bottom:10px">
        <el-button size="small" v-if="level==10" type="primary" @click="submit" style="margin-right:10px">提交（选中则所有角色不能使用该代码）</el-button>
        <el-button size="small" v-if="level>8" type="primary" @click="add" style="margin-right:10px">新增命令行</el-button>
        <el-input
            v-model="request.search"
            size="mini"
            style="width:20%"
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
                v-if="level==10"
                width="55">
                </el-table-column>
                <el-table-column type="expand">
                    <template slot-scope="props">
                    <el-form label-position="left" inline class="demo-table-expand" style="width:100%">
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
                <el-table-column label="操作" width="200%" v-if="level>8">
                    <template slot-scope="scope" v-if="scope.row.status!=0&&level>8">
                        <el-button type="info" @click="edit(scope.row)" size="mini">编辑</el-button>
                        <el-popconfirm style="margin-left:10px"
                            placement="top"
                            :title="`确定删除${scope.row.command}吗？`"
                            @onConfirm="del(scope.row.id)"
                        >
                            <el-button slot="reference" type="danger" size="mini">删除</el-button>
                        </el-popconfirm>
                    </template>
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

        <el-dialog :title="info=='add'?'新增':'编辑'" :visible.sync="visable" width="400px">
            <el-form ref="registerForm" :model="editData" :rules="rules" label-width="100px" class="resiger-box">
                <el-form-item label="ID" v-if="info!='add'">
                    <span type="text" style="width: 80%;" readonly  v-text="editData.id"/>
                </el-form-item>
                <el-form-item label="命令" prop="command">
                    <el-input  v-model="editData.command" style="width: 80%;"/>
                </el-form-item>
                <el-form-item label="描述" prop="description">
                    <el-input type="textarea" v-model="editData.description" style="width: 80%;"/>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
            <el-button @click="visable = false">取 消</el-button>
            <el-button @click="editData={
                id:'',
                command:'',
                description:''
            }">重 置</el-button>
            <el-button :loading="loading" type="primary" @click="upload">{{ info=='add'?'添 加':'修 改' }}</el-button>
            </div>
        </el-dialog>
    </div>
</template>
<script>
import store from '@/store'
import { getCommandInfo,addCommandInfo,updateCommandInfo,delCommandInfo,changeStatus } from '@/api/command'
export default {
    mounted(){
        this.getData()
    },
    data(){
        return {
            level:store.getters.level,
            visable:false,
            request:{
                search:'',
                current:1,
                pageSize:6,
            },
            total: 0,
            data:[],
            editData:{
                id:0,
                command:'',
                description:''
            },
            loading:false,
            info:'add',
            rules: {
                command: [
                    {required: true, message: '不可为空',trigger: 'blur'},
                ],
                description: [
                    {required: true,message: '不可为空', trigger: 'blur'},
                ],
            },
            selection:[],
            unselection:[]
        }
    },
    methods:{
        getData(){
            getCommandInfo(this.request).then(r=>{
                console.log(r)
                this.total=r.object.total
                this.data = r.object.records
                this.request.current = r.object.current
                this.request.pageSize = r.object.size
                this.$nextTick(() => {
                    this.data.forEach(e=>{
                        if(e.status==0){
                            this.$refs.Table.toggleRowSelection(e,true);
                        }
                    })
                })
            }).catch()
        },
        del(id){
            delCommandInfo(id).then(r=>{
                this.getData();
                this.$message.success("删除成功");
            }).catch()
        },
        edit(row){
            this.editData = row
            this.info='edit';
            this.visable = true
        },
        add(){
            this.editData={
                id:0,
                command:'',
                description:''
            },
            this.info='add';
            this.visable=true;
        },
        upload(){
            if(this.info=='add'){
                this.loading=true;
                addCommandInfo(this.editData).then(r=>{
                    this.getData()
                    this.visable=false;
                    this.loading=false;
                    this.$message.success("添加成功");
                }).catch(()=>{this.loading=false;})
            }else{
                this.loading=true;
                updateCommandInfo(this.editData).then(r=>{
                    this.getData()
                    this.visable=false;
                    this.loading=false;
                    this.$message.success("修改成功");
                }).catch(()=>{this.loading=false;})
            }
        },
        selectChange(selection){
            this.selection=[]
            this.unselection=[]
            selection.forEach(e=>{
                this.selection.push(e.id)
            })
            this.data.forEach(e=>{
                if(this.selection.indexOf(e.id)==-1){
                    this.unselection.push(e.id)
                }
            })
        },
        submit(){
            changeStatus(this.selection.join(','),this.unselection.join(',')).then(r=>{
                this.$message.success("修改成功")
                this.getData()
            }).catch
        }
    },
    watch:{
        'request.search':function(newVal){
            this.getData();
        }
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
    width: 1000%;
  }
</style>