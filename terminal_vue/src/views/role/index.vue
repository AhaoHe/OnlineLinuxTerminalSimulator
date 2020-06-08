<template>
    <el-container>
        <el-main style="width:30%">
            <el-tabs type="border-card">
                <el-tab-pane label="菜单分配">
                    <menu-info :role-name="roleName" :role-id="roleId"></menu-info>
                </el-tab-pane>
                <el-tab-pane label="API分配">
                    <api-info :role-name="roleName" :role-id="roleId"></api-info>
                </el-tab-pane>
            </el-tabs>
        </el-main>
        <el-main style="width:70%">
            <el-card>
                <el-table
                @row-click="getRoleId"
                :highlight-current-row="true"
                :data="data">
                    <el-table-column type="expand">
                        <template slot-scope="props">
                        <el-form label-position="left" inline class="demo-table-expand">
                            <el-form-item label="ID">
                            <span>{{ props.row.roleId }}</span>
                            </el-form-item>
                            <el-form-item label="描述">
                            <span>{{ props.row.description }}</span>
                            </el-form-item>
                        </el-form>
                        </template>
                    </el-table-column>
                    <el-table-column
                        prop="roleName"
                        label="角色名">
                    </el-table-column>
                    <el-table-column
                        prop="level"
                        label="权限等级">
                    </el-table-column>
                    <el-table-column
                        label="操作">
                        <template slot="header" slot-scope="">
                            <el-button size="mini" type="primary" @click="add" round>添加</el-button>
                        </template>
                        <template slot-scope="scope">
                            <el-button size="mini" type="success" @click="edit(scope.row)" round>编辑</el-button>
                            <el-popconfirm style="margin-left:10px"
                                placement="top"
                                :title="`确定删除${scope.row.roleName}吗？`"
                                @onConfirm="deleteThis(scope.row.roleId)">
                                <el-button slot="reference" type="danger" size="mini" v-if="scope.row.level!=1&&scope.row.level!=10" round>删除</el-button>
                            </el-popconfirm>
                        </template>
                    </el-table-column>
                </el-table>
            </el-card>
        </el-main>
        
        <el-dialog
        :title="title==0?'添加角色':'编辑角色'"
        :visible.sync="visable"
        width="50%"
        center>
        <el-form label-width="80px" :model="form">
        <el-form-item v-if="form.roleId!=null" label="ID">
            <span v-text="form.roleId"></span>
        </el-form-item>
        <el-form-item label="角色名">
            <el-input v-model="form.roleName"></el-input>
        </el-form-item>
        <el-form-item label="权限等级">
            <div class="block" v-if="form.level!=10&&form.level!=100">
                <el-slider
                v-model="form.level"
                :step="10"
                :format-tooltip="formatTooltip"
                :min="20"
                :max="90"
                show-stops>
                </el-slider>
            </div>
            <div class="block" v-if="form.level==10||form.level==100" v-text="form.level/10"></div>
        </el-form-item>
        <el-form-item label="描述">
            <el-input v-model="form.description"></el-input>
        </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
            <el-button @click="visable = false">取 消</el-button>
            <el-button type="primary" @click="AddOrUpdate" v-text="title==0?'添加':'更新'"></el-button>
        </span>
        </el-dialog>

    </el-container>
</template>
<script>
import { getRoleInfo,addRole,updateRole,deleteRole } from '@/api/role'
import MenuInfo from './components/menu'
import ApiInfo from './components/api'
export default {
    mounted(){
        this.getInfo();
    },
    data(){
        return {
            data:[],
            form:{
                roleId:null,
                roleName:'',
                level:20,
                description:''
            },
            title:0,
            roleId:0,
            roleName:'无',
            visable:false,
        }
    },
    methods:{
        getInfo(){
            getRoleInfo().then(r=>{
                console.log(r.object)
                this.data=r.object;
            }).catch(()=>{})
        },
        getRoleId(row){
            this.roleId = row.roleId
            this.roleName = row.roleName
        },
        formatTooltip(val) {
            if(val>=80)
                return '管理员权限：'+(val * 0.1);
            else
                return val * 0.1;
        },
        add(){
            this.visable=true;
            this.title=0;
            this.form={
                roleId:null,
                roleName:'',
                level:20,
                description:''
            }
        },
        edit(row){
            this.visable=true;
            this.title=1;
            this.form={
                roleId:row.roleId,
                roleName:row.roleName,
                level:row.level*10,
                description:row.description
            }
        },
        AddOrUpdate(){
            if(this.title==0){
                this.form.level = this.form.level/10
                addRole(this.form).then(r=>{
                    this.getInfo();
                    this.visable=false;
                    this.$message.success('添加成功')
                }).catch()
            }else{
                this.form.level = this.form.level/10
                updateRole(this.form).then(r=>{
                    this.getInfo();
                    this.visable=false;
                    this.$message.success('修改成功')
                }).catch()
            }
        },
        deleteThis(id){
            deleteRole(id).then(r=>{
                this.getInfo();
                this.$message.success('删除成功')
            }).catch
        }
    },   
    components:{
        'api-info':ApiInfo,
        'menu-info':MenuInfo
    }
}
</script>
<style>
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